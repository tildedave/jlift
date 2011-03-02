/*
 * File          : RuntimeExceptionAdder.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:53 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.visit;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import jif.types.JifProcedureInstance;
import jif.types.LabeledType;
import jlift.types.JLiftTypeSystem;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.ProcedureDecl;
import polyglot.ast.TypeNode;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.ProcedureInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ContextVisitor;
import polyglot.visit.TypeBuilder;

public class RuntimeExceptionAdder extends ContextVisitor {

	protected ProcedureInstance findPi;
	protected Collection<Type> addExceptions;
	protected JLiftTypeSystem jlts;

	public RuntimeExceptionAdder(Job job,
			TypeSystem ts,
			NodeFactory nf,
			ProcedureInstance pi,
			Collection<Type> runtimeExceptionsForScc) {
		super(job, ts, nf);
		this.jlts = (JLiftTypeSystem) ts;
		this.findPi = pi;
		this.addExceptions = runtimeExceptionsForScc;
	}

	@Override
	protected Node leaveCall(Node n) throws SemanticException {
		Node returnNode = super.leaveCall(n);
		if (returnNode instanceof ProcedureDecl) {
			ProcedureDecl pd = (ProcedureDecl) returnNode;
			if (pd.procedureInstance().equals(findPi)) {

				LinkedHashSet<TypeNode> throwTypeNodes = new LinkedHashSet<TypeNode>(pd.throwTypes());
				LinkedHashSet<Type> throwTypes = new LinkedHashSet<Type>();
				for(Type t : addExceptions) {
					Type addT = null;
					if (pd.procedureInstance() instanceof JifProcedureInstance) {
						JifProcedureInstance jpi = (JifProcedureInstance) pd.procedureInstance();
						if (shouldReport(4))
							report(4,"should label " + t + " with " + jpi.returnLabel());
						addT = jlts.labeledType(t.position(), jlts.unlabel(t), jpi.returnLabel());
						if (shouldReport(3))
							report(3, "adding " + addT + " to throw set");
					}
					else {
						addT = t;
					}
					if (notAlreadyDeclaredToBeThrown(addT,throwTypeNodes)) {
						throwTypes.add(addT);
						throwTypeNodes.add(nf.CanonicalTypeNode(pd.position(),addT));
					}
				}

				if (pd instanceof MethodDecl)
					pd = ((MethodDecl) pd).throwTypes(new LinkedList(throwTypeNodes));
				else if (pd instanceof ConstructorDecl)
					pd = ((ConstructorDecl) pd).throwTypes(new LinkedList(throwTypeNodes));

				ProcedureDecl returnMethod = (ProcedureDecl) pd.buildTypes(new TypeBuilder(job, ts, nf)).disambiguate(new AmbiguityRemover(job, ts, nf));

				if (shouldReport(2))
					report(2, "changing throw types of " + returnNode + " from " + ((ProcedureDecl) returnNode).throwTypes() + " to " + returnMethod.throwTypes());

//				MethodInstance newMethodInstance = returnMethod2.methodInstance().throwTypes(new LinkedList(throwTypes));
//				System.err.println("returnMethod2: " + returnMethod2.methodInstance().throwTypes());
				return returnMethod;//.methodInstance(newMethodInstance);
			}
		}
		return returnNode;
	}

	private boolean notAlreadyDeclaredToBeThrown(Type addT,
			LinkedHashSet<TypeNode> throwTypeNodes) {
		for(TypeNode tn : throwTypeNodes) {
			Type currentType = tn.type();
			if (currentType instanceof LabeledType)
				currentType = jlts.unlabel(currentType);
			if (addT.equals(currentType))
				return false;
		}

		return true;
	}

	private void report(int i, String string) {
		Report.report(i, string);
	}

	private boolean shouldReport(int i) {
		return Report.should_report("rtexc", i);
	}
}
