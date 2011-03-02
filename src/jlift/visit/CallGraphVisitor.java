/*
 * File          : CallGraphVisitor.java
 * Project       : jlift
 * Description   : Visitor that builds the CallGraph.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:56:45 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.visit;


import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jif.ast.JifProcedureDecl;
import jif.types.JifPolyType;
import jif.types.JifSubstType;
import jlift.JLiftOptions;
import jlift.types.JLiftTypeSystem;
import jlift.util.CallGraph;
import polyglot.ast.Cast;
import polyglot.ast.ClassDecl;
import polyglot.ast.New_c;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.ProcedureCall;
import polyglot.ast.ProcedureDecl;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.CodeInstance;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ContextVisitor;
import polyglot.visit.DataFlow;
import polyglot.visit.FlowGraph;
import polyglot.visit.FlowGraphBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class CallGraphVisitor extends ContextVisitor {

	protected CallGraph callGraph;
	protected JLiftTypeSystem ts;
	protected NodeFactory nf;
	protected Job job;

	public CallGraphVisitor(Job job, CallGraph callGraph, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
		this.job = job;
		this.callGraph = callGraph;
		this.ts = (JLiftTypeSystem) ts;
		this.nf = nf;
	}

	@Override
	protected Context enterScope(Node parent, Node n) {
		if (n instanceof ProcedureDecl) {
			if (Report.should_report("callgraph",2)) {
				Report.report(2, "** entering scope of " + n + " (" + n.position() + ")");
			}
		}
		return super.enterScope(parent, n);
	}

	@Override
	public NodeVisitor enterCall(Node parent, Node n) throws SemanticException {
		CallGraphVisitor superEnter = (CallGraphVisitor) super.enterCall(parent, n);

		// add declarations to the call graph
		if (n instanceof ClassDecl)
			superEnter.addClassToCallGraph((ClassDecl) n);
		if (n instanceof JifProcedureDecl) {
			JifProcedureDecl pd = (JifProcedureDecl) n;
			// if we're entering a procedure declaration, treat calls from here as being inside of this
			// TODO: actually use the contextvisitor for this
			superEnter.addProcedureToCallGraph(pd);

			if (pd.codeBody() != null) {
				FlowGraphBuilder g = new FlowGraphBuilder(pd, false);
				CFGBuilder v = new CFGBuilder(ts, g, new MockDataFlow(job, ts, nf));
				v.visitGraph();

				callGraph.addFlowGraph(pd.procedureInstance(), g);
			}
		}

		return superEnter;
	}

	@Override
	public Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException {

		Node returnNode = n;
		// add links between classes/procedures to the call graph
		if (n instanceof ProcedureCall) {
			ProcedureCall pcall = (ProcedureCall) n;
			if (pcall.procedureInstance() instanceof MethodInstance) {
				MethodInstance mi = (MethodInstance) pcall.procedureInstance();
				if (isCallToNewInstance(parent, mi) && JLiftOptions.getInstance().shouldRefactorNewInstances()) {
					returnNode = convertNewInstanceToNewCall(parent,
							returnNode, pcall, mi);
				}
			}
			addProcedureCallToCallGraph((ProcedureCall) returnNode);
		}

		if (context().currentCode() instanceof ProcedureInstance)
			callGraph.addProcedureInstanceForNode(n, (ProcedureInstance) context().currentCode());

		return super.leaveCall(parent, n, returnNode, v);
	}

	protected void addProcedureToCallGraph(JifProcedureDecl method) {
		callGraph.addNewMethod(method.procedureInstance(), job);
	}

	protected void addClassToCallGraph(ClassDecl decl) {
		callGraph.addNewClass(decl.type());
	}

	protected void addProcedureCallToCallGraph(ProcedureCall call)
	{
		// TODO: procedure calls from static field initializers

		if (context().currentCode() instanceof ProcedureInstance) {
			ProcedureInstance currentProcedureInstance = (ProcedureInstance) context().currentCode();
			ProcedureInstance callInstance = call.procedureInstance();

			if (callInstance instanceof MemberInstance &&
					currentProcedureInstance instanceof MemberInstance) {

				ReferenceType callContainer = ((MemberInstance) callInstance).container();
				List<ReferenceType> subtypes = new LinkedList<ReferenceType>(new HashSet<ReferenceType>(ts.getSubtypesAndImplementers(callInstance)));
				//System.err.println("subtypes and implementors of " + callInstance + " -- " + subtypes);
				if (!subtypes.contains(callContainer))
					subtypes.add(callContainer);

				if (isCallToSuperclass(call)) {
					boolean removed = true;
					while (removed) {
						removed = subtypes.remove(((MemberInstance) currentProcedureInstance).container());
					}
				}

				// NOTE: do not need to check constructors as you cannot override them

				//if (callContainer instanceof JifParsedPolyType && callInstance instanceof MethodInstance)
				if (callInstance instanceof MethodInstance)
				{
					// for each subtype of the called method, see if it implements the called instance
					for(ReferenceType rt : subtypes)
					{
						if (ts.hasMethod(rt, (MethodInstance) callInstance)) {
							List<MethodInstance> rtMethods = rt.methods();
							boolean addedCall = false;
							for (MethodInstance mi : rtMethods) {
								if (ts.isSameMethod(mi, (MethodInstance) callInstance)) {
									if (Report.should_report("callgraph",4))
										Report.report(4, "call graph: adding call of " + callInstance + " (" + rt + ") from call to supertype at position " + call.position());

									addCall(currentProcedureInstance, mi);
									addedCall = true;
								}
							}
						}
					}
				}/*
				else if (callContainer instanceof JifSubstType) {
				    System.err.println("call container is not a parsed type, it is a subst type!");
				    }*/

				addCall(currentProcedureInstance, callInstance);
			}
		}
	}

	private void addCall(ProcedureInstance from,
			ProcedureInstance to) {
		if (to instanceof MemberInstance) {
			MemberInstance mi = (MemberInstance) to;
			if (mi.container() instanceof JifSubstType) {
				JifSubstType jst = (JifSubstType) mi.container();

				JifPolyType jpt = (JifPolyType) jst.base();

				// need to find the member instance that corresponds to 'to' in jpt.
				Collection<MemberInstance> jptMembers = jpt.members();
				for(MemberInstance jptMem : jptMembers) {
					if (jptMem instanceof ProcedureInstance && jptMem.equals(mi))
						callGraph.addNewCall(from, (ProcedureInstance) jptMem);
				}
			}
		}
		callGraph.addNewCall(from, to);
	}

	private Node convertNewInstanceToNewCall(Node parent, Node returnNode,
			ProcedureCall pcall, MethodInstance mi) {
		// in order to track changes through reflection, we have to keep track of
		// newInstance calls on the class object.  here we limit ourselves to things
		// of the form
		// (A) (expr).newInstance()
		// in this case, we convert it to new A()
		Cast parentCast = (Cast) parent;
		try {
			CodeInstance currentCode = this.context().currentCode();
			if (currentCode instanceof ProcedureInstance) {
				ProcedureInstance pi = (ProcedureInstance) currentCode;
				if (pi instanceof MemberInstance) {
					ReferenceType piContainer = ((MemberInstance) pi).container();
					ConstructorInstance ci = ts.findConstructor((ClassType) parentCast.castType().type(),
							mi.formalTypes(),
							(ClassType) piContainer);

					New_c newNodeToReplaceNewInstance = (New_c) nf.New(pcall.position(), parentCast.castType(), pcall.arguments());

					TypeChecker tc = new TypeChecker(job, ts, nf);
					tc = (TypeChecker) tc.context(context());
					newNodeToReplaceNewInstance = (New_c) newNodeToReplaceNewInstance.buildTypes(new TypeBuilder(job, ts, nf)).disambiguate(new AmbiguityRemover(job, ts, nf));
					newNodeToReplaceNewInstance = (New_c) newNodeToReplaceNewInstance.typeCheck(tc);
					newNodeToReplaceNewInstance = (New_c) newNodeToReplaceNewInstance.constructorInstance(ci);
					returnNode = newNodeToReplaceNewInstance;
				}
			}
		}
		catch (SemanticException e) {
			throw new InternalCompilerError("unexpected SemanticException: " + e);
		}

		return returnNode;
	}

	private boolean isCallToNewInstance(Node parent, MethodInstance mi) {
		return mi.name().equals("newInstance") && parent instanceof Cast;
	}

	private boolean isCallToSuperclass(ProcedureCall call) {
		boolean b = call.toString().startsWith("super(") || (call.toString().startsWith("super."));
		if (b) {
			// TODO: use this breakpoint and a test to get a better idea of
			// what condition to use for this method
			//System.err.println("call: " + call + " is a call to the superclass");
		}
		return b;
	}

	public class MockDataFlow extends DataFlow {

		public MockDataFlow(Job job, TypeSystem ts, NodeFactory nf) {
			super(job, ts, nf, true);
		}

		@Override
		protected void check(FlowGraph graph, Term n, boolean entry,
				Item inItem, Map outItems) throws SemanticException { }

		@Override
		protected Item confluence(List items, Term node, boolean entry,
				FlowGraph graph) { return null; }

		@Override
		protected Item createInitialItem(FlowGraph graph, Term node,
				boolean entry) { return null; }

	}
}
