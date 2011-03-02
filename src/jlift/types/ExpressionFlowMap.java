/*
 * File          : ExpressionFlowMap.java
 * Project       : jlift
 * Description   : Utility data structure for encapsulating the
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:43:28 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jif.ast.JifMethodDecl;
import jif.ast.JifProcedureDecl;
import jif.types.JifMethodInstance;
import jif.types.JifProcedureInstance;
import jif.types.JifSubstType;
import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.LabeledType;
import jif.types.hierarchy.LabelEnv_c;
import jif.types.label.ArgLabel;
import jif.types.label.Label;
import jif.types.label.VarLabel;
import jlift.JLiftOptions;
import jlift.types.label.JLiftVarLabel;
import jlift.util.CacheCallBack;
import jlift.util.DFSGraph;
import jlift.util.MultiMap;
import polyglot.ast.Assign;
import polyglot.ast.CodeDecl;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.ProcedureCall;
import polyglot.main.Report;
import polyglot.types.ArrayType;
import polyglot.types.CodeInstance;
import polyglot.types.Type;
import polyglot.types.VarInstance;

/**
 *
 * @author dhking
 *
 */

//contains information about how expressions flow to one another
public class ExpressionFlowMap {

	protected Map<JLiftVarLabel, Node> variableToNodeMap;

	protected MultiMap<JLiftVarLabel, JLiftVarLabel> directVariableAffectMap;
	protected MultiMap<JLiftVarLabel, JLiftVarLabel> directVariableAffectingMap;

	protected MultiMap<Node, Label> specialSinks;
	protected Map<Node, JLiftVarLabel> nodeToVariableMap;

	protected Map<JLiftVarLabel, Collection<JLiftVarLabel>> cacheVarLabelAffectMap;
	protected Map<JLiftVarLabel, Collection<JLiftVarLabel>> cacheVarLabelAffectingMap;

	protected Map<Node, Collection<Node>> cacheNodeAffectMap;
	protected Map<Node, Collection<Node>> cacheNodeAffectingMap;

	protected DFSGraph<JLiftVarLabel> varGraph;
	protected DFSGraph<JLiftVarLabel> mirrorVarGraph;
	protected CacheCallBack<JLiftVarLabel> callBackObject;

	protected Map<JLiftVarLabel, Label> lvarMap;

	private final Map<JLiftVarLabel, JLiftLabelConstraint> variableToConstraintMap;

	public ExpressionFlowMap(Collection<JLiftLabelConstraint> constraintSet, String dotName) {

		variableToNodeMap = new LinkedHashMap<JLiftVarLabel, Node>();
		directVariableAffectMap = new MultiMap<JLiftVarLabel, JLiftVarLabel>();

		specialSinks = new MultiMap<Node, Label>();

		//directVariableAffectedMap = new LinkedHashMap<VarLabel, Collection<VarLabel>>();
		nodeToVariableMap = new LinkedHashMap<Node, JLiftVarLabel>();
		variableToConstraintMap = new HashMap<JLiftVarLabel, JLiftLabelConstraint>();

		this.cacheVarLabelAffectMap = new HashMap<JLiftVarLabel, Collection<JLiftVarLabel>>();
		this.cacheNodeAffectMap = new HashMap<Node, Collection<Node>>();

		this.directVariableAffectingMap = new MultiMap<JLiftVarLabel, JLiftVarLabel>();
		this.cacheNodeAffectingMap = new HashMap<Node, Collection<Node>>();
		this.cacheVarLabelAffectingMap = new HashMap<JLiftVarLabel, Collection<JLiftVarLabel>>();

		this.lvarMap = new HashMap<JLiftVarLabel, Label>();

		buildVariableListFromConstraintSet(constraintSet);
		callBackObject = new CacheCallBack<JLiftVarLabel>() {
			@Override
			public void registerCacheUpdate(Collection<JLiftVarLabel> vSCC, Collection<JLiftVarLabel> cacheNode) {
				// convert VarLabel into a node

				Collection<Node> affectedNodes = new HashSet<Node>();
				for(VarLabel affectedVar : cacheNode) {
					if (variableToNodeMap.containsKey(affectedVar)) {
						affectedNodes.add(variableToNodeMap.get(affectedVar));
					}
				}

				for(VarLabel v : vSCC) {
					if (variableToNodeMap.containsKey(v)) {
						Node n = variableToNodeMap.get(v);
						cacheNodeAffectMap.put(n, affectedNodes);
					}
				}
			}
		};
		varGraph = new DFSGraph<JLiftVarLabel>(directVariableAffectMap, callBackObject);
		if (dotName != null) {
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(dotName);
				PrintStream ps = new PrintStream(fos);
				ps.println(varGraph.toDotString());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public VarLabel getVariableForNode(Node n) {
		return nodeToVariableMap.get(n);
	}

	private void buildVariableListFromConstraintSet(
			Collection<JLiftLabelConstraint> constraintSet) {
		for(JLiftLabelConstraint c : constraintSet) {
			// for each constraint, add the variable-affecting relationship
			// to variableAffectMap
			//System.err.println("constraint: " + c);
			addAffectingVariableFromConstraint(c);
			if (isDefinitionalConstraint(c)) {
				JLiftVarLabel lhsVar = getDefinedVariable(c);
				if (lhsVar.name().startsWith("NV") ||
						lhsVar.name().endsWith(":return_observed") ||
						lhsVar.name().endsWith(":value_returned") ||
						lhsVar.name().endsWith(":normal_termination") ||
						lhsVar.name().endsWith(":return") ||
						lhsVar.name().contains("caller_pc@") ||
						lhsVar.name().startsWith("R") ||
						lhsVar.name().startsWith("PARAM")) {
					report(3,"associate " + lhsVar + " with " + c.getNode());
					associateVariableWithConstraint(c, lhsVar);
				}
				else if (lhsVar.name().startsWith("L_var@") && !c.rhs().hasVariableComponents()) {
					lvarMap.put(lhsVar, c.rhs());
				}
				else if (lhsVar.name().startsWith("field:") && c.getNode() instanceof Field) {
					report(3,"associate " + lhsVar + " with " + c.getNode());
					associateVariableWithConstraint(c, lhsVar);
				}
				else {
					report(4,"definitional constraint for variable " + lhsVar + " not associated with " + c.getNode() + " (" + c.getNode().getClass() + ")?");
				}
			}
			if (c.kind() == JLiftLabelConstraint.LEQ && // && c.getNode() instanceof Special &&
				!c.rhs().hasVariables() && c.lhs() instanceof VarLabel) {
				JLiftVarLabel lhsVar = (JLiftVarLabel) c.lhs();
				if (lhsVar.name().startsWith("NV")) {
					report(4,"node " + c.getNode() + " associated with sink of label " + c.rhs());
					specialSinks.put(c.getNode(), c.rhs());
				}
			}
		}
	}

	private void associateVariableWithConstraint(JLiftLabelConstraint c,
			JLiftVarLabel lhsVar) {
		variableToNodeMap.put(lhsVar, c.getNode());
		nodeToVariableMap.put(c.getNode(), lhsVar);
		variableToConstraintMap.put(lhsVar, c);
	}

	private boolean shouldReport(int lvl) {
		return Report.should_report("flowmap", lvl);
	}

	private void report(int lvl, String msg) {
		if (shouldReport(lvl))
			Report.report(lvl, msg);
	}

	private boolean isDefinitionalConstraint(LabelConstraint c) {
		return (c.lhs() instanceof VarLabel &&
				(c.kind() == LabelConstraint.EQUAL ||
						c.kind() == JLiftLabelConstraint.DEFINE));
	}

	private JLiftVarLabel getDefinedVariable(LabelConstraint c) {
		if (c.lhs() instanceof VarLabel && c.kind() == LabelConstraint.EQUAL)
			return (JLiftVarLabel) c.lhs();
		else if (c.lhs() instanceof VarLabel && c.kind() == JLiftLabelConstraint.DEFINE)
			return (JLiftVarLabel) c.lhs();
		return null;
	}

	private void addAffectingVariableFromConstraint(LabelConstraint c) {
		Set<JLiftVarLabel> affectingVars = null;
		Set<JLiftVarLabel> affectedVars = null;
		if (c.kind() == LabelConstraint.LEQ) {
			// the variables on the LHS affect any variable(s) on the RHS
			affectingVars = c.lhs().variableComponents();
			affectedVars = c.rhs().variableComponents();
		}
		else if (c.kind() == LabelConstraint.EQUAL) {
			// affecting works both ways here
			affectingVars = c.rhs().variableComponents();
			affectedVars = c.lhs().variableComponents();

			for(JLiftVarLabel lhsVar : affectingVars) {
				for(JLiftVarLabel rhsVar : affectedVars) {
					variableAffectsVariable(lhsVar, rhsVar);
					variableAffectsVariable(rhsVar, lhsVar);
				}
			}
			return;
		}
		else if (c.kind() == JLiftLabelConstraint.DEFINE) { // if (c.kind() == LabelConstraint.DEFINE) {
			affectingVars = c.rhs().variableComponents();
			affectedVars = c.lhs().variableComponents();
		}

		if (affectingVars != null && affectedVars != null) {
			for(JLiftVarLabel lhsVar : affectingVars) {
				for(JLiftVarLabel rhsVar : affectedVars) {
					variableAffectsVariable(lhsVar, rhsVar);
					variableAffectsVariable(rhsVar, rhsVar);
					variableAffectsVariable(lhsVar, lhsVar);
				}
			}
		}
	}


	private void variableAffectsVariable(JLiftVarLabel affecting, JLiftVarLabel affected) {
		addVariableToAffectSet(affecting, affected);
		addVariableToAffectingSet(affecting, affected);
	}

	private void addVariableToAffectSet(JLiftVarLabel affecting, JLiftVarLabel affected) {
		directVariableAffectMap.put(affecting, affected);
		if (!directVariableAffectMap.containsKey(affected)) {
			directVariableAffectMap.put(affected);
		}
	}

	private void addVariableToAffectingSet(JLiftVarLabel affecting, JLiftVarLabel affected) {
		directVariableAffectingMap.put(affected, affecting);
		if (!directVariableAffectingMap.containsKey(affecting)) {
			directVariableAffectingMap.put(affecting);
		}
	}

	public Double getAffectingPercentage(Node n) {
		Collection<Node> nodeList = getAffectedNodes(n);
		if (nodeList == null) {
			return 0.0;
		}
		else {
			double numNodes = nodeToVariableMap.size();
			double affectedNodes = nodeList.size();
			return affectedNodes / numNodes;
		}
	}

	public Collection<Node> getAffectedNodes(Node n) {
		if (cacheNodeAffectMap.containsKey(n))
			return cacheNodeAffectMap.get(n);

		JLiftVarLabel v = nodeToVariableMap.get(n);
		if (v != null) {
			Collection<Node> returnSet = variableToNodeSet(getAffectedVarLabels(v));
			cacheNodeAffectMap.put(n, returnSet);
			return returnSet;
		} else
			return null;
	}


	public Collection<Node> getAffectingNodes(Node n) {
		if (cacheNodeAffectingMap.containsKey(n))
			return cacheNodeAffectingMap.get(n);

		JLiftVarLabel v = nodeToVariableMap.get(n);
		if (v != null) {
			Collection<Node> returnSet = variableToNodeSet(getAffectingVarLabels(v));
			cacheNodeAffectingMap.put(n, returnSet);
			return returnSet;
		} else
			return null;
	}



	private Collection<JLiftVarLabel> getAffectingVarLabels(JLiftVarLabel v) {
		if (mirrorVarGraph == null) {
			generateMirrorVarGraph();
		}
		Collection<JLiftVarLabel> affectingVars = mirrorVarGraph.getReachSet(v);
		return affectingVars;
	}

	private void generateMirrorVarGraph() {
		callBackObject = new CacheCallBack<JLiftVarLabel>() {
			@Override
			public void registerCacheUpdate(Collection<JLiftVarLabel> vSCC, Collection<JLiftVarLabel> cacheNode) {
				// convert VarLabel into a node

				Collection<Node> affectingNodes = new HashSet<Node>();
				for(VarLabel affectedVar : cacheNode) {
					if (variableToNodeMap.containsKey(affectedVar)) {
						affectingNodes.add(variableToNodeMap.get(affectedVar));
					}
				}

				for(VarLabel v : vSCC) {
					if (variableToNodeMap.containsKey(v)) {
						Node n = variableToNodeMap.get(v);
						cacheNodeAffectMap.put(n, affectingNodes);
					}
				}
			}
		};

		mirrorVarGraph = new DFSGraph<JLiftVarLabel>(directVariableAffectingMap, callBackObject);
	}

	private Collection<JLiftVarLabel> getAffectedVarLabels(JLiftVarLabel v) {
		Collection<JLiftVarLabel> affectedVars = varGraph.getReachSet(v);
		return affectedVars;
	}

	private Set<Node> variableToNodeSet(Collection<JLiftVarLabel> collection) {
		Set<Node> nodeSet = new LinkedHashSet<Node>();
		for(VarLabel v : collection) {
			nodeSet.add(variableToNodeMap.get(v));
		}

		return nodeSet;
	}

	public abstract class LabelCriteria {
		JifTypeSystem jts;
		LabelEnv_c labelEnv;

		LabelCriteria(JifTypeSystem jts) {
			this.jts = jts;
			this.labelEnv = new LabelEnv_c(jts, true);
		}


		public boolean check(Label l1) {
			if (l1 == null || l1.hasVariableComponents())
				return false;
			return checkImpl(l1);
		}

		protected abstract boolean checkImpl(Label label);
		public abstract boolean returnOnUnknownResult();
	}


	public Node getSinkOfLabelOccludedByNode(Node constraintNode, final Label targetLabel) {
		return getSinkOfLabelWithCriteria(constraintNode, new LabelCriteria((JifTypeSystem) targetLabel.typeSystem()) {
			@Override
			protected boolean checkImpl(Label label) {
				// we are looking for label <= targetLabel, targetLabel </= label
				report(5,"check that " + label + " <= " + targetLabel + " and " + targetLabel + " </= " + label);
				JLiftOptions.getInstance().disableIntegrity();
				boolean result = labelEnv.leq(label, targetLabel) && !labelEnv.leq(targetLabel, label);
				JLiftOptions.getInstance().enableIntegrity();
				return result;
			}

			@Override
			public String toString() {
				return "label strictly less than " + targetLabel;
			}

			@Override
			public boolean returnOnUnknownResult() {
				return false;
			}
		});
	}

	public Node getSinkOfLabel(Node constraintNode, final Label targetLabel) {
//		System.err.println("does " + constraintNode + " flow to a sink of label " + targetLabel + "?");
//		System.err.println("nodes that it flows to: " + getAffectedNodes(constraintNode));
		Node sink = getSinkOfLabelWithCriteria(constraintNode, new LabelCriteria((JifTypeSystem) targetLabel.typeSystem()) {
			@Override
			protected boolean checkImpl(Label label) {
				// we are looking for label <= targetLabel, targetLabel <= label
				report(5,"check that " + label + " <= " + targetLabel + " and " + targetLabel + " <= " + label);
				JLiftOptions.getInstance().disableIntegrity();
				boolean targetLabelLeq = labelEnv.leq(targetLabel, label);
				boolean labelLeq = labelEnv.leq(label, targetLabel);
				JLiftOptions.getInstance().enableIntegrity();
				report(6, targetLabel + " <= " + label + ": " + targetLabelLeq);
				report(6, label + " <= " + targetLabel + ": " + labelLeq);
				return labelLeq && targetLabelLeq;
			}

			@Override
			public String toString() {
				return "label equal to " + targetLabel;
			}

			@Override
			public boolean returnOnUnknownResult() {
				return true;
			}
		});
//		if (sink == null)
//		System.err.println("couldn't find it");
//		else
//		System.err.println("found: " + sink);
		return sink;
	}

	public Node getSinkOfLabelWithCriteria(Node constraintNode, LabelCriteria lc) {
		Collection<Node> affectedNodes = getAffectedNodes(constraintNode);
		if (affectedNodes == null) {
			return null;
		}

		report(3, "find sink for node " + constraintNode + " with criteria " + lc);
		report(4,"affected nodes: " + affectedNodes);

		for(Node n : affectedNodes) {
			Collection<Label> sinkLabels = getExplicitLabelsForSinkNode(n);
			report(5,"sinkLabels for n: " + n + " --> " + sinkLabels);
			for(Label sinkLabel : sinkLabels) {
				if (lc.check(sinkLabel)) {
					report(3,"found sink: " + n);
					return n;
				}
				if (isParamLabel(sinkLabel)) {
					// try to look inside L_var

					Label checkLabel = null;
					VarLabel lvar = (VarLabel) sinkLabel;

					if (lvarMap.containsKey(lvar)) {
						Label rhsLabel = lvarMap.get(lvar);

						if (!rhsLabel.hasVariableComponents()) {
							checkLabel = rhsLabel;
						}
						else {
						}
					}
					else {
					}

					if (checkLabel != null) {
						if (lc.check(checkLabel))
							return n;
					}
					else if (lc.returnOnUnknownResult()) {
						return n;
					}
				}
			}
		}
		return null;
	}

	private boolean isParamLabel(Label sinkLabel){
		return (sinkLabel instanceof JLiftVarLabel &&
				((JLiftVarLabel) sinkLabel).name().startsWith("L_var@"));
	}

	private Collection<Label> getExplicitLabelsForSinkNode(Node n) {
		Collection<Label> sinkLabels = new LinkedList<Label>();

		if (specialSinks.containsKey(n)) {
			sinkLabels.addAll(specialSinks.get(n));
		}

		if (n instanceof Assign) {
			Expr left = ((Assign) n).left();
			Collection<Label> leftLabels = getExplicitLabelsForType(left.type());
			if (leftLabels != null)
				sinkLabels.addAll(leftLabels);
		}
		if (n instanceof ProcedureCall) {
			ProcedureCall pc = (ProcedureCall) n;
			JifProcedureInstance pi = (JifProcedureInstance) pc.procedureInstance();
			sinkLabels.addAll(getExplicitLabelsForProcedureInstance(pi));
		}
		if (n instanceof JifProcedureDecl) {
			JifProcedureDecl pd = (JifProcedureDecl) n;
			if (pd.returnLabel() != null) {
				Label returnLabel = getExplicitLabelForLabel(pd.returnLabel().label());
				if (returnLabel != null)
					sinkLabels.add(returnLabel);
			}
			if (pd.startLabel() != null) {
				Label startLabel = getExplicitLabelForLabel(pd.startLabel().label());
				if (startLabel != null)
					sinkLabels.add(startLabel);
			}
			if (n instanceof JifMethodDecl) {
				sinkLabels.addAll(getExplicitLabelsForType(((JifMethodDecl) n).returnType().type()));
			}
		}

		return sinkLabels;
	}

	private Collection<Label> getExplicitLabelsForProcedureInstance(
			JifProcedureInstance pi) {
		Collection<Label> sinkLabels = new LinkedList<Label>();

		if (pi instanceof JifMethodInstance) {
			Collection<Label> returnTypeLabels = getExplicitLabelsForType(((JifMethodInstance) pi).returnType());
			sinkLabels.addAll(returnTypeLabels);
		}

		if (pi.container() instanceof JifSubstType) {
			JifSubstType jst = (JifSubstType) pi.container();
			sinkLabels.addAll(jst.actuals());
		}

		Collection<Label> formalTypeLabels = getExplicitLabelsForTypeList(pi.formalTypes());
		sinkLabels.addAll(formalTypeLabels);

		Label pcBound = getExplicitLabelForLabel(pi.pcBound());
		if (pcBound != null)
			sinkLabels.add(pcBound);

		return sinkLabels;
	}

	private Collection<Label> getExplicitLabelsForTypeList(List<Type> formalTypes) {

		Collection<Label> sinkLabels = new LinkedList<Label>();
		for(Type t : formalTypes) {
			Collection<Label> labelsForType = getExplicitLabelsForType(t);
			sinkLabels.addAll(labelsForType);
		}

		return sinkLabels;
	}

	private Collection<Label> getExplicitLabelsForType(Type returnType) {
		Collection<Label> returnLabels = new LinkedList<Label>();
		if (returnType instanceof LabeledType) {
			Label label = getExplicitLabelForLabel(((LabeledType) returnType).labelPart());
			if (label != null)
				returnLabels.add(label);
			returnLabels.addAll(getExplicitLabelsForType(((LabeledType) returnType).typePart()));
		}
		if (returnType instanceof ArrayType) {
			returnLabels.addAll(getExplicitLabelsForType(((ArrayType) returnType).base()));
		}

		return returnLabels;
	}

	private Label getExplicitLabelForLabel(Label l) {
		if (l.hasVariableComponents()) {
			return null;
		}
		if (l instanceof ArgLabel) {
			ArgLabel al = (ArgLabel) l;
			if (al.upperBound().hasVariableComponents())
				return null;
			return al.upperBound();
		}

		return l;
	}

	public boolean isSource(Node node) {
		boolean isSource = !getExplicitLabelsForSourceNode(node).isEmpty();
		return isSource;
	}

	public Collection<Label> getExplicitLabelsForSourceNode(Node node) {
		Collection<Label> sourceLabels = new LinkedList<Label>();
		if (node instanceof CodeDecl) {
			CodeDecl cd = (CodeDecl) node;
			CodeInstance ci = cd.codeInstance();
			if (ci instanceof JifProcedureInstance) {
				Collection<Label> piLabels = getExplicitLabelsForProcedureInstance((JifProcedureInstance) ci);
//				if (!piLabels.isEmpty())
//				System.err.println("ci: " + ci + " piLabels: " + piLabels);
				if (piLabels != null)
					sourceLabels.addAll(piLabels);
			}
		}
		if (node instanceof LocalDecl) {
			LocalDecl ld = (LocalDecl) node;
			Collection<Label> ldLabels = getExplicitLabelsForType(ld.declType());
			if (ldLabels != null)
				sourceLabels.addAll(ldLabels);
//			if (exLabel != null) {
////			System.err.println("ldType: " + ld.declType() + " exLabel: " + exLabel);
//			sourceLabels.add(exLabel);
//			}
		}
		else if (node instanceof Local) {
			VarInstance vi = ((Local) node).localInstance();
			Collection<Label> viLabels = getExplicitLabelsForType(vi.type());
			if (viLabels != null)
				sourceLabels.addAll(viLabels);
//			Label exLabel = getExplicitLabelsForType(vi.type());
//			if (exLabel != null) {
////			System.err.println("vi: " + vi + " exLabel: " + exLabel);
//			sourceLabels.add(exLabel);
//			}
		}
		else if (node instanceof Field) {
			VarInstance vi = ((Field) node).fieldInstance();

//			Label exLabel = getExplicitLabelsForType(vi.type());
//			if (exLabel != null) {
//			//System.err.println("vi: " + vi + " exLabel: " + exLabel);
//			sourceLabels.add(exLabel);
//			}
			Collection<Label> viLabels = getExplicitLabelsForType(vi.type());
			if (viLabels != null)
				sourceLabels.addAll(viLabels);
		}
		else if (node instanceof Expr && !(node instanceof Assign) && !(node instanceof ProcedureCall)) {
			Expr e = (Expr) node;
//			Label explicitLabel = getExplicitLabelsForType(e.type());
//			if (explicitLabel != null) {
////			System.err.println("expr: " + e + " with " + e.type() + " and " + explicitLabel);
//			sourceLabels.add(explicitLabel);
//			}
			Collection<Label> exprLabels = getExplicitLabelsForType(e.type());
			if (exprLabels != null)
				sourceLabels.addAll(exprLabels);
		}
		//System.err.println("sourceLabels for " + node + " (" + node.getClass() + ") :" + sourceLabels);
		return sourceLabels;
	}
	/*
	private boolean isSinkConstraint(LabelConstraint c) {
		return containsPairLabel(getSinkLabelForConstraint(c));
	}

	private boolean isSourceConstraint(LabelConstraint c) {
		return containsPairLabel(getSourceLabelForConstraint(c));
	}


	private Label getSinkLabelForConstraint(LabelConstraint c) {
		// tau <= c <<-  c is the sink
		// v == tau <<-  tau is the sink
		if (c.kind() == LabelConstraint.LEQ || c.kind() == LabelConstraint.EQUAL)
			return c.rhs();
		// v ==_{def} tau ||--> tau <= v <<-- v is the sink (a define should not be a sink)
		else if (c.kind() == LabelConstraint.DEFINE)
			return c.lhs();

		return null;
	}

	private Label getSourceLabelForConstraint(LabelConstraint c) {
		// v == tau <<-  tau is the source
		if (c.kind() == LabelConstraint.EQUAL)  {
			return c.rhs();
		}
		// tau <= v <<-  tau is the source
		if (c.kind() == LabelConstraint.LEQ)
			return c.lhs();
		// v ==_{def} tau ||--> tau <= v <<-- tau is the source
		if (c.kind() == LabelConstraint.DEFINE)
			return c.rhs();

		return null;
	}

	private boolean foundPairLabel;

	private boolean containsPairLabel(Label lhs) {
		foundPairLabel = false;

		LabelSubstitution ls = new LabelSubstitution() {
			@Override
			public Label substLabel(Label L) throws SemanticException {
				if (L instanceof PairLabel) {
					foundPairLabel = true;
				}
				return L;
			}
		};

		try {
			lhs.subst(ls);
		} catch (SemanticException e) {
			throw new InternalCompilerError(e);
		}

		return foundPairLabel;
	}
	 */

	public Map<JLiftVarLabel, Node> getVariableToNodeMap() {
		return variableToNodeMap;
	}

	public Collection<JLiftVarLabel> getDirectSuccessors(JLiftVarLabel v) {
		Collection<JLiftVarLabel> directVars = directVariableAffectMap.get(v);
		return directVars;
	}

	public Collection<JLiftVarLabel> getSCCForVarLabel(JLiftVarLabel v) {
		return this.varGraph.getSCCForNode(v);
	}

	public Node getNodeForVariable(VarLabel v) {
		return variableToNodeMap.get(v);
	}

	public DFSGraph<JLiftVarLabel> getGraph() {
		return varGraph;
	}

	public Map<JLiftVarLabel, JLiftLabelConstraint> getVariableToConstraintMap() {
		return variableToConstraintMap;
	}

	public int size() {
		return this.nodeToVariableMap.size();
	}
}