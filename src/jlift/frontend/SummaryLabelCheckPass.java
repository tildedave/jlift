/* File          : SummaryLabelCheckPass.java
 * Project       : jlift
 * Description   : Label check completely labeled procedures.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:39:24 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.frontend;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jif.types.JifProcedureInstance;
import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.Solver;
import jif.types.VarMap;
import jif.types.label.VarLabel;
import jif.visit.JifLabelSubst;
import jifclipse.types.JifclipseTypeSystem;
import jlift.ExtensionInfo;
import jlift.JLiftOptions;
import jlift.SummaryConstraintInfo;
import jlift.mediation.XMLOutputter;
import jlift.types.ExpressionFlowMap;
import jlift.types.JLiftLabelConstraint;
import jlift.types.JLiftSolver;
import jlift.types.JLiftTypeSystem;
import jlift.types.JLiftVarMap;
import jlift.types.label.JLiftVarLabel;
import jlift.util.CallGraph;
import jlift.util.DFSGraph;
import jlift.util.MaxFlowGraph;
import jlift.util.MaxFlowGraphConstructor;
import jlift.util.MultiMap;
import jlift.util.Pair;
import jlift.util.MaxFlowGraph.MaxFlowNode;
import jlift.visit.CountAnnotationsVisitor;
import jlift.visit.LabelEraser;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.ProcedureCall;
import polyglot.frontend.AbstractPass;
import polyglot.frontend.Job;
import polyglot.frontend.goals.Goal;
import polyglot.main.Report;
import polyglot.types.ConstructorInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Named;
import polyglot.types.ProcedureInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorInfo;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StdErrorQueue;

public class SummaryLabelCheckPass extends AbstractPass {

	protected JifTypeSystem ts;
	protected NodeFactory nf;
	protected SummaryConstraintInfo summaryInfo;
	protected CallGraph callGraph;
	protected ExtensionInfo extInfo;
	protected boolean foundPairLabel;
	private String cantCheckReason;

	public SummaryLabelCheckPass(Goal goal, TypeSystem ts, NodeFactory nf,
			CallGraph callGraph, SummaryConstraintInfo summaryInfo,
			ExtensionInfo extInfo) {
		super(goal);
		this.ts = (JifTypeSystem) ts;
		this.nf = nf;
		this.summaryInfo = summaryInfo;
		this.callGraph = callGraph;
		this.extInfo = extInfo;
	}

	@Override
	public boolean run() {
		// for each node in the call graph, get their closed constraints from
		// summaryInfo and label check them -- but only if they have no summary
		// variables in them

		JLiftTypeSystem jts = (JLiftTypeSystem) ts;
		Set<Job> checkedJobs = new HashSet<Job>();

		if (JLiftOptions.getInstance().eraseLabels) {
			System.out.println("# annotations: " + CountAnnotationsVisitor.globalCount);
			System.out.println("# labels erased: " + LabelEraser.numErased);
		}

		if (Report.should_report("labelcheck", 3)) {
			Report.report(3, "label check order:");
			Iterator<ProcedureInstance> it = callGraph.iterator();
			int i = 0;
			while (it.hasNext()) {
				++i;
				ProcedureInstance jpi = it.next();
				Report.report(3, i + ": " + jpi.toString());
			}
		}

		// summaryInfo.dumpClosedConstraints(5);
		Iterator<ProcedureInstance> it = callGraph.iterator();
		boolean areErrors = false;

		while (it.hasNext()) {
			ProcedureInstance proc = it.next();
			if (summaryInfo.hasSummaryConstraintsForProcedure(proc)) {
				Collection<JLiftLabelConstraint> procConstraints;
				try {
					procConstraints = summaryInfo.getSummaryConstraints(proc);
				} catch (SemanticException e) {
					throw new InternalCompilerError(e);
				}

				if (Report.should_report("excstats", 1)) {
					System.out.println("number of procedures called by " + proc + ": " + callGraph.getReachSet(proc).size());
					reportExceptionStats(procConstraints, proc.toString(), proc.position());
				}

				if (Report.should_report("implicitstats", 1)) {
					reportImplicitStats(procConstraints, proc.toString());
				}

				Job job = callGraph.getJob(proc);
				ErrorQueue eq = job.compiler().errorQueue();
				int errorsBefore = eq.errorCount();

				if (canCheckConstraintSet((JifProcedureInstance) proc,
						procConstraints)) {
					if (Report.should_report("labelcheck", 2))
						Report.report(2, "procedure " + proc
								+ " has no summary constraint variables");

					if (Report.should_report("labelcheck", 1)) {
						Report.report(1, " === label check === ");
						Report.report(1, proc.toString());
						Report.report(1, " =================== ");
					}

					if (JLiftOptions.getInstance().forTestHarness()) {
						extInfo.compiler().errorQueue().enqueue(
								new ErrorInfo(ErrorInfo.WARNING,
										"label checking " + proc, proc
												.position()));
					}

					// we assume that the API function is accessed through a
					// COMPLETELY PUBLIC object reference
					procConstraints = SummaryConstraintInfo.substituteAwayThisLabelInConstraintSet(
									getName(proc) + "_apireceiver", proc.position(),
									null, procConstraints, jts);

					// DHK: Don't think we're using this any more, though we
					// should have some mechanism to enforce
					// what it was getting at
					// SummaryConstraintInfo.substituteInConstraintSet(summaryInfo.getAPICallerPC(),
					// proc.pcBound(), procConstraints);

					summaryInfo.reportSummaryConstraintSet(proc, 5);

					JLiftSolver solverForProcConstraints = jts.createRehofSolver(getName(proc));
					((JifclipseTypeSystem) ts).getInternalCompilerInfo().registerSolver(solverForProcConstraints);

					if (JLiftOptions.getInstance().shouldGenerateDeclassificationConstraints()) {
						XMLOutputter xmlo = new XMLOutputter(proc, callGraph, procConstraints);
						System.out.println("time to output constraints for " + proc +  " -- " + procConstraints.size());
						xmlo.convertConstraints(getName(proc));
					}

					//tryAndSummarizeProceduresForConstraints(procConstraints);

					try {

						// and then add stuff to it
						for (LabelConstraint c : procConstraints) {
							solverForProcConstraints.addConstraint(c);
						}

						// and then solve it

						VarMap varMap = solverForProcConstraints.solve();
						substituteForVarMap(solverForProcConstraints, varMap);
						// getSlicesForErrors(solverForProcConstraints.getErrorConstraints());

					} catch (SemanticException e) {
						// label checking fails
						Position pos = e.position() != null ? e.position() : job.ast().position();
						eq.enqueue(ErrorInfo.SEMANTIC_ERROR, e.getMessage(), pos);
					}
				} else {
					if (Report.should_report("labelcheck", 2))
						Report.report(4, "we cannot check " + proc + " because: " + cantCheckReason);
					SummaryConstraintInfo.reportConstraintSet((Collection) procConstraints, 6);
				}

				int errorsAfter = eq.errorCount();
				if (errorsAfter != errorsBefore)
					areErrors = true;
			}
		}

		//return !areErrors;
		return true;
	}

	private void tryAndSummarizeProceduresForConstraints(
			Collection<JLiftLabelConstraint> procConstraints) {

		HashSet<VarLabel> allVariables = new HashSet<VarLabel>();
		HashSet<ProcedureInstance> allProcedures = new HashSet<ProcedureInstance>();
		Map<VarLabel, ProcedureInstance> varToProcMap = new HashMap<VarLabel, ProcedureInstance>();
		Map<Object, Integer> objToNameMap = new HashMap<Object, Integer>();
		Map<VarLabel, JLiftLabelConstraint> varToConstraintMap = new HashMap<VarLabel, JLiftLabelConstraint>();

		for(JLiftLabelConstraint jlc : procConstraints) {
			HashSet<VarLabel> constraintVars = new HashSet<VarLabel>();
			constraintVars.addAll(jlc.lhs().variableComponents());
			constraintVars.addAll(jlc.rhs().variableComponents());

			allVariables.addAll(constraintVars);

			if (jlc.lhs() instanceof VarLabel && jlc.kind() == JLiftLabelConstraint.DEFINE) {
				String name = ((VarLabel) jlc.lhs()).name();
				if (name.startsWith("Exception:") || name.startsWith("pc") || name.startsWith("NV")) {
					ProcedureInstance pi = callGraph.getProcedureInstanceForNode(jlc.node());
					allProcedures.add(pi);
					varToProcMap.put((VarLabel) jlc.lhs(), pi);
				}
			}

			for(VarLabel v : constraintVars) {
				if (v.name().startsWith("local:")) {
					ProcedureInstance pi = callGraph.getProcedureInstanceForNode(jlc.node());
					allProcedures.add(pi);
					varToProcMap.put(v, pi);
				}
			}
		}

		for(JLiftLabelConstraint jlc : procConstraints) {
			Collection<VarLabel> varLabels = new HashSet<VarLabel>();
			varLabels.addAll(jlc.rhs().variableComponents());
			varLabels.addAll(jlc.lhs().variableComponents());

			for(VarLabel v : varLabels) {
				if (!varToProcMap.containsKey(v)) {
					System.out.println(v + ":" + jlc.getNode().getClass());
				}
			}
		}

		Integer nodeNum = 0;

		Map<Integer, Object> nameToObjMap = new HashMap<Integer, Object>();
		for (VarLabel v : allVariables) {
			if (!varToProcMap.containsKey(v)) {
				++nodeNum;
				objToNameMap.put(v, nodeNum);
				nameToObjMap.put(nodeNum, v);
			}
		}

		for (ProcedureInstance pi : allProcedures) {
			++nodeNum;
			objToNameMap.put(pi, nodeNum);
			nameToObjMap.put(nodeNum, pi);
		}

		for (Map.Entry<VarLabel,ProcedureInstance> e : varToProcMap.entrySet()) {
			objToNameMap.put(e.getKey(), objToNameMap.get(e.getValue()));
			//System.out.println("associate: " + e.getKey() + " with " + e.getValue() + " (node" + objToNameMap.get(e.getValue()) + ")");
		}

		StringBuffer dotString = new StringBuffer();
		dotString.append("digraph G {\n");
		for(int i = 1; i <= nodeNum; ++i) {
			dotString.append("\tnode" + i + " [label=\"" + nameToObjMap.get(i) + "\"]\n");
		}

		MultiMap<Object, Object> graphEdges = new MultiMap<Object, Object>();

		for(JLiftLabelConstraint jlc : procConstraints) {
			Collection<VarLabel> rhsVariables = new HashSet<VarLabel>();
			VarLabel lhsVariable = null;

			if (jlc.kind() == JLiftLabelConstraint.DEFINE) {
				rhsVariables.addAll(jlc.lhs().variableComponents());
				if (jlc.rhs() instanceof VarLabel)
					lhsVariable = (VarLabel) jlc.rhs();

				doLink(rhsVariables, lhsVariable, nameToObjMap, objToNameMap, dotString, graphEdges);
			}
			if (jlc.kind() == JLiftLabelConstraint.LEQ) {
				rhsVariables.addAll(jlc.rhs().variableComponents());
				if (jlc.lhs() instanceof VarLabel)
					lhsVariable = (VarLabel) jlc.lhs();

				System.out.println(jlc);
				doLink(rhsVariables, lhsVariable, nameToObjMap, objToNameMap, dotString, graphEdges);
			}
			if (jlc.kind() == JLiftLabelConstraint.EQUAL) {
				if (jlc.rhs() instanceof VarLabel) {
					System.out.println(jlc);
					doLink(jlc.lhs().variableComponents(), (VarLabel) jlc.rhs(), nameToObjMap, objToNameMap, dotString, graphEdges);
				}
				if (jlc.lhs() instanceof VarLabel) {
					System.out.println(jlc);
					doLink(jlc.rhs().variableComponents(), (VarLabel) jlc.lhs(), nameToObjMap, objToNameMap, dotString, graphEdges);
				}
			}
		}

		dotString.append("}\n");

		System.out.println(dotString.toString());

		for(ProcedureInstance p : allProcedures) {
			System.out.println(p);
			System.out.println("Going out:");
			for (Object o : graphEdges.get(p)) {
				if (!o.equals(p))
					System.out.println("\t" + o);
			}
			System.out.println("Coming in:");
			for (Map.Entry<Object,Collection<Object>> e : graphEdges.entrySet()) {
				if (e.getValue().contains(p) && !e.getKey().equals(p))
					System.out.println("\t" + e.getKey());
			}
		}

		System.out.println("naive: " + allVariables.size() + " vs compact: " + nodeNum);
	}

	private void doLink(Collection<VarLabel> rhsVariables, VarLabel lhsVariable, Map<Integer, Object> nameToObjMap, Map<Object, Integer> objToNameMap, StringBuffer dotString, MultiMap<Object, Object> graphEdges) {
		Set<Integer> rhsObjs = new HashSet<Integer>();
		for(VarLabel v : rhsVariables) {
			if (objToNameMap.containsKey(v))
				rhsObjs.add(objToNameMap.get(v));
			else
				System.out.println("map does not contain key for " + v + " ??? ");
		}


		if (objToNameMap.containsKey(lhsVariable)) {
			Integer lhsName = objToNameMap.get(lhsVariable);

			if (lhsName == null) {
				System.out.println("map does not contain key for " + lhsVariable + " ??? ");
			}
			else {
				for(Integer rhsObj : rhsObjs) {
//					System.out.println(rhsPIs + " ---> " + lhsVariable);
					dotString.append("\tnode" + rhsObj + " -> node" + lhsName + "\n");
					graphEdges.put(nameToObjMap.get(rhsObj), nameToObjMap.get(lhsName));
				}
			}
		}

	}

	private void outputDeclassificationsFromMaxFlow(JifTypeSystem ts,
			ProcedureInstance proc,
			Collection<JLiftLabelConstraint> procConstraints) {
		// construct a maximum flow graph from the variable constraints.
		// wonderful...

		ExpressionFlowMap flowMap = new ExpressionFlowMap(procConstraints,
				getName(proc));
		DFSGraph<JLiftVarLabel> dfsGraph = flowMap.getGraph();
		MultiMap<JLiftVarLabel, JLiftVarLabel> mm = dfsGraph.getGraph();

		System.out.println("** CONSTRUCTING MAX FLOW GRAPH FOR " + getName(proc) + " **");

		System.out.println(procConstraints);

		MaxFlowGraphConstructor mfgc = new MaxFlowGraphConstructor(procConstraints, flowMap, mm);
		MaxFlowGraph maxFlowGraph = mfgc.constructMaxFlowGraph();
		//maxFlowGraph.pruneNodesNotOnSecretToPublic();
		outputMaxFlowGraphToDotFile(proc, maxFlowGraph);

		System.out.println("** CONSTRUCTED MAX FLOW GRAPH FOR " + getName(proc) + " **");

		//maxFlowGraph.pruneNodesNotOnSecretToPublic();

		Collection<Pair<MaxFlowNode, MaxFlowNode>> minCut = maxFlowGraph.getMinimumCut();

		System.out.println("** SUGGESTED DECLASSIFIERS " + getName(proc) + " **");

		Collection<JLiftVarLabel> suggestedDeclassifiers = new HashSet<JLiftVarLabel>();
		Map<JLiftVarLabel, MaxFlowNode> declassifierToIncomingNode = new HashMap<JLiftVarLabel, MaxFlowNode>();

		determineMinimumDeclassifiersFromCut(flowMap, minCut,
				suggestedDeclassifiers, declassifierToIncomingNode);

		Collection<MaxFlowNode> suggestedDeclassifiersMaxFlowNode = declassifierToIncomingNode.values();

		System.out.println("** CALCULATING DOMINATORS ** ");
		Map<MaxFlowNode, Collection<MaxFlowNode>> dominators = maxFlowGraph.computeDominators();

		if (Report.should_report("sat", 2)) {
			for(Map.Entry<MaxFlowNode, Collection<MaxFlowNode>> e: dominators.entrySet()) {
				System.out.println(e.getKey());
				System.out.println("\t" + e.getValue());
			}
		}

		System.out.println("** CALCULATING POST-DOMINATORS ** ");
		Map<MaxFlowNode, Collection<MaxFlowNode>> postdominators = maxFlowGraph.computePostDominators();

		if (Report.should_report("sat", 2)) {
			for(Map.Entry<MaxFlowNode, Collection<MaxFlowNode>> e: postdominators.entrySet()) {
				System.out.println(e.getKey());
				System.out.println("\t" + e.getValue());
			}
		}

		MultiMap<MaxFlowNode, MaxFlowNode> equivalenceClasses = new MultiMap<MaxFlowNode, MaxFlowNode>();

		generateDeclassifierEquivalenceClasses(
				suggestedDeclassifiersMaxFlowNode, dominators, postdominators,
				equivalenceClasses);

		System.out.println("** SUGGESTED DECLASSIFIER EQUIVALENCE CLASSES **");

		int declassifierNum = 1;

		for (MaxFlowNode mfn : equivalenceClasses.keySet()) {
			// System.out.println(mfn);
			System.out.println("#" + declassifierNum);
			++declassifierNum;

			for (MaxFlowNode n : equivalenceClasses.get(mfn)) {
				JLiftVarLabel vl = (JLiftVarLabel) n.getNodeObject();
				System.out.println("\t" + vl + ":"
						+ flowMap.getNodeForVariable(vl));
			}
		}

		outputMaxFlowGraphToDotFile(proc, maxFlowGraph);
	}

	private void generateDeclassifierEquivalenceClasses(
			Collection<MaxFlowNode> suggestedDeclassifiersMaxFlowNode,
			Map<MaxFlowNode, Collection<MaxFlowNode>> dominators,
			Map<MaxFlowNode, Collection<MaxFlowNode>> postdominators,
			MultiMap<MaxFlowNode, MaxFlowNode> equivalenceClasses) {
		// for each suggested declassifier d, search all nodes.
		// two conditions, either of which means you get into the equivalence
		// class of solutions...
		// 1) e doms d AND d pdoms e
		// OR
		// 2) d doms e AND e pdoms d

		for (MaxFlowNode d : suggestedDeclassifiersMaxFlowNode) {
			Collection<MaxFlowNode> dominators_of_d = dominators.get(d);
			Collection<MaxFlowNode> pdominators_of_d = postdominators.get(d);

			System.out.println("d:" + d);
			System.out.println("\tdominators:" + dominators_of_d);
			System.out.println("\tpost-dominators:" + pdominators_of_d);

			for (MaxFlowNode n : dominators_of_d) {
				if (nodeIsForExpressionVariable(n)) {
					// n dom d
					// check if d pdoms n
					Collection<MaxFlowNode> pdominators_of_n = postdominators
							.get(n);

					if (pdominators_of_n.contains(d))
						equivalenceClasses.put(d, n);
				}
			}

			for (MaxFlowNode n : pdominators_of_d) {
				if (nodeIsForExpressionVariable(n)) {
					// n pdom d
					// check if d doms n
					Collection<MaxFlowNode> dominators_of_n = dominators.get(n);
					if (dominators_of_n.contains(d))
						equivalenceClasses.put(d, n);
				}
			}
		}
	}

	private boolean nodeIsForExpressionVariable(MaxFlowNode n) {
		JLiftVarLabel variable = ((JLiftVarLabel) n.getNodeObject());
		if (variable != null) {
			if (!variable.name().matches("NV[0-9]+") || n.toString().contains("outgoing"))
				return false;
			// only one outgoing edge n.getOutgoingEdges();
			if (n.getOutgoingEdges().get(n.getOutgoingEdges().keySet().iterator().next()) == -1)
				return false;

			return true;
		}
		return false;
	}

	private void outputMaxFlowGraphToDotFile(ProcedureInstance proc,
			MaxFlowGraph maxFlowGraph) {
		String dotString = maxFlowGraph.toDotString();
		try {
			FileOutputStream fos = new FileOutputStream(getName(proc)
					+ "_maxflow.dot");
			PrintStream ps = new PrintStream(fos);

			ps.print(dotString);

			fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void determineMinimumDeclassifiersFromCut(
			ExpressionFlowMap flowMap,
			Collection<Pair<MaxFlowNode, MaxFlowNode>> mfos,
			Collection<JLiftVarLabel> suggestedDeclassifiers,
			Map<JLiftVarLabel, MaxFlowNode> declassifierToIncomingNode) {
		for (Pair<MaxFlowNode, MaxFlowNode> p : mfos) {
			JLiftVarLabel v = (JLiftVarLabel) p.getFirst().getNodeObject();
			Node n = flowMap.getNodeForVariable(v);

			declassifierToIncomingNode.put(v, p.getFirst());
			suggestedDeclassifiers.add(v);

			System.out.println(v + " (" + n.position() + ")");
			System.out.println("\t" + n);
		}
	}

	private void reportExceptionStats(
			Collection<JLiftLabelConstraint> procConstraints, String procName,
			Position pos) {
		HashMap<String, Integer> seenExceptions = new HashMap<String, Integer>();
		MultiMap<String, Position> exceptionsForPositions = new MultiMap<String, Position>();
		Pattern exceptionPattern = Pattern
				.compile("Exception:([A-Za-z]+)([0-9]+)(.*)");
		ErrorQueue eq = new StdErrorQueue(System.err, 1000000000, "whatever");
		boolean hasOutputHeader = false;

		for (JLiftLabelConstraint c : procConstraints) {
			if (c.kind() == JLiftLabelConstraint.DEFINE
					|| c.kind() == JLiftLabelConstraint.EQUAL) {
				if (c.lhs().toString().contains("Exception:")) {
					JLiftVarLabel lhs = (JLiftVarLabel) c.lhs();
					Matcher m = exceptionPattern.matcher(lhs.name());
					if (m.matches()) {
						String exName = m.group(1);

						if (exceptionsForPositions.containsKey(exName)
								&& exceptionsForPositions.get(exName).contains(
										c.position())) {
							continue;
						} else {
							exceptionsForPositions.put(exName, c.position());

							if (seenExceptions.containsKey(exName)) {
								seenExceptions.put(exName, seenExceptions
										.get(exName) + 1);
							} else {
								seenExceptions.put(exName, 1);
							}

							if (Report.should_report("excstats", 2)) {
								if (!hasOutputHeader) {
									Report.report(1, "exceptions for "
											+ procName + " (" + pos + "):");
									hasOutputHeader = true;
								}
								eq.enqueue(new ErrorInfo(ErrorInfo.WARNING,
										"can throw " + exName, c.lhs()
												.position()));
							}
						}
					}

				}
			}
		}

		if (!seenExceptions.isEmpty()) {
			Report.report(1, "exceptions for " + procName + " (" + pos + "):");
			for (Map.Entry<String, Integer> e : seenExceptions.entrySet())
				Report.report(1, "\t" + e.getKey() + " = " + e.getValue());
		}
	}

	private void reportImplicitStats(
			Collection<JLiftLabelConstraint> procConstraints, String procName) {
		int implicitFlows = 0;
		int totalConstraints = procConstraints.size();
		for (JLiftLabelConstraint c : procConstraints) {
			if (c.kind() == JLiftLabelConstraint.DEFINE
					|| c.kind() == JLiftLabelConstraint.EQUAL) {
				if (c.lhs().toString().contains("Exception:")) {
					++implicitFlows;
				} else if (c.lhs().toString().startsWith("pc")) {
					++implicitFlows;
				}
			}
		}

		if (implicitFlows > 0) {
			Report.report(1, "implicit flows for " + procName + ": "
					+ implicitFlows + " constraints/" + totalConstraints
					+ " total");
		}
	}

	private void getSlicesForErrors(Collection<LabelConstraint> errorConstraints) {

		String appJar = "\"/home/dhking/workspace/retrofitting/jifpoker/jifpoker.jar\"";
		String mainClass = "Lpoker/PokerTable";

		for (LabelConstraint c : errorConstraints) {
			JLiftLabelConstraint jlc = (JLiftLabelConstraint) c;
			if (jlc.getCodeInstance() instanceof MethodInstance) {
				MethodInstance mi = (MethodInstance) jlc.getCodeInstance();
				String callerName = mi.name();
				System.err.print(callerName + ":"
						+ ((Named) mi.container()).name());
			} else {
				System.err.print("<init>:" + ((Named) ((ConstructorInstance) jlc.getCodeInstance()).container()).name());
				// System.err.println(jlc.getNode() + " in " +
				// jlc.getCodeInstance());
			}
			if (jlc.getNode() instanceof ProcedureCall) {
				ProcedureCall pc = (ProcedureCall) jlc.getNode();
				if (pc.procedureInstance() instanceof Named) {
					System.err.print(":"
							+ ((Named) pc.procedureInstance()).name());
				}
			}
			System.err.print(",");
			// String strCaller = callerName;
			//
			// try {
			// AnalysisScope scope =
			// AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,
			// FileProvider.getFile(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			//
			// System.err.println(jlc.getNode() + " (" +
			// jlc.getNode().getClass() + ")");
		}
	}

	private boolean canCheckConstraintSet(JifProcedureInstance pi,
			Collection<JLiftLabelConstraint> procConstraints) {
		// return
		// !SummaryConstraintInfo.constraintSetHasSummaryVariables(procConstraints);
		this.cantCheckReason = SummaryConstraintInfo.canCheckConstraintSet(pi,
				procConstraints);
		return (cantCheckReason.length() == 0);
	}

	private void substituteForVarMap(Solver solverForProcConstraints,
			VarMap bounds) {

		if (!JLiftOptions.getInstance().shouldGenerateJifCode()) {
			// if we are not generating any output code, then it doesn't
			// matter what we substitute here
			return;
		}

		Collection<String> filesToVisit = new HashSet<String>();
		for (VarLabel v : ((JLiftVarMap) bounds).domain()) {
			filesToVisit.add(v.position().file());
		}

		// System.err.println("we should visit: ");
		// System.err.println(filesToVisit);

		for (String fileName : filesToVisit) {
			Job fileJob = extInfo.getJobForFile(fileName);
			if (fileJob != null && bounds != null) {
				JifLabelSubst jls = new JifLabelSubst(fileJob, ts, nf,
						solverForProcConstraints);

				Node n = fileJob.ast();
				Node newN = n;

				jls = (JifLabelSubst) jls.begin();

				if (jls != null) {
					newN = n.visit(jls);
					jls.finish(newN);
				}

				fileJob.ast(newN);
			}
		}
	}

	private String getName(ProcedureInstance proc) {
		if (proc instanceof MethodInstance)
			return ((MethodInstance) proc).name();
		else if (proc instanceof ConstructorInstance)
			return proc.designator();
		return null;
	}
}
