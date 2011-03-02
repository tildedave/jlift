/*
 * File          : RuntimeExceptionAddedPass.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:49 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.frontend;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import jlift.util.CallGraph;
import jlift.visit.FindProcedureDeclVisitor;
import jlift.visit.RuntimeExceptionAdder;
import jlift.visit.RuntimeExceptionChecker;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.frontend.AbstractPass;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.Goal;
import polyglot.main.Report;
import polyglot.types.ProcedureInstance;
import polyglot.types.Type;

public class RuntimeExceptionAddedPass extends AbstractPass implements Pass {

	// TODO: refactor this and some other classes to inherit from an SCCPass or something
	protected jlift.ExtensionInfo extInfo;

	public RuntimeExceptionAddedPass(Goal goal, jlift.ExtensionInfo extInfo) {
		super(goal);
		this.extInfo = extInfo;
	}

	@Override
	public boolean run() {
		CallGraph callGraph = extInfo.getCallGraph();

		Iterator<HashSet<ProcedureInstance>> sccIterator = callGraph.sccIterator();

		while (sccIterator.hasNext()) {
			HashSet<ProcedureInstance> scc = sccIterator.next();
			addRuntimeExceptionsForScc(scc);
		}

		return true;
	}

	private void addRuntimeExceptionsForScc(HashSet<ProcedureInstance> scc) {
		// search for exceptions that descend from/are RuntimeException, but are
		// not uncheckedException and add them to the throw set of each element in the
		// SCC

		Collection<Type> runtimeExceptionsForScc = new HashSet<Type>();
		for(ProcedureInstance pi : scc) {
			runtimeExceptionsForScc.addAll(getRuntimeExceptionsForProcedureInstance(pi));
		}

		if (shouldReport(2))
			report(2,"add: " + runtimeExceptionsForScc + " to throwsSet of " + scc);

		for(ProcedureInstance pi : scc) {
			setRuntimeExceptionsForProcedureInstance(pi, runtimeExceptionsForScc);
		}
	}

	private Collection<Type> getRuntimeExceptionsForProcedureInstance(
			ProcedureInstance pi) {
		// find the node in the AST that corresponds with the method decl of pi
		// run the RuntimeExceptionVisitor on it
		Job job = extInfo.getCallGraph().getJob(pi);
		ProcedureDecl declForProcedureInstance = getProcedureDeclForProcedureInstance(pi);
		if (shouldReport(1))
			report(1, "add runtime exceptions for procedure instance: " + declForProcedureInstance + " @ " + declForProcedureInstance.position());
		RuntimeExceptionChecker rec = new RuntimeExceptionChecker(job, extInfo.typeSystem(), extInfo.nodeFactory());
		rec = (RuntimeExceptionChecker) rec.begin();
		declForProcedureInstance.visit(rec);
		Collection<Type> runtimeExceptions = rec.getRuntimeExceptions();
		return runtimeExceptions;
	}

	private void report(int i, String string) {
		Report.report(i, string);
	}

	private boolean shouldReport(int i) {
		return Report.should_report("rtexc", i);
	}

	private void setRuntimeExceptionsForProcedureInstance(ProcedureInstance pi,
			Collection<Type> runtimeExceptionsForScc) {
		ProcedureDecl pd = getProcedureDeclForProcedureInstance(pi);

		Job job = extInfo.getCallGraph().getJob(pi);
		RuntimeExceptionAdder nv = new RuntimeExceptionAdder(job, extInfo.typeSystem(), extInfo.nodeFactory(), pi, runtimeExceptionsForScc);
		nv = (RuntimeExceptionAdder) nv.begin();
		Node ast = job.ast().visit(nv);
		job.ast(ast);
		nv.finish();

		// TODO: do we need to go through all procedures that call pi as well?  I think we might
		// TODO: looks like no.  better write an explicit test though
	}

	private ProcedureDecl getProcedureDeclForProcedureInstance(ProcedureInstance pi) {
		Job job = extInfo.getCallGraph().getJob(pi);

		FindProcedureDeclVisitor nv = new FindProcedureDeclVisitor(job, pi);
		nv = (FindProcedureDeclVisitor) nv.begin();
		job.ast().visit(nv);
		nv.finish();

		ProcedureDecl md = nv.procedureDecl();
		return md;
	}
}