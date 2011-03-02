/*
 * File          : RuntimeExceptionChecker.java
 * Project       : jlift
 * Description   : Visitor that adds runtime exceptions to the throws set of a method.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:58:55 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.visit;

import java.util.Collection;
import java.util.HashSet;

import jlift.types.JLiftTypeSystem;
import polyglot.ast.FieldDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;

// need to do this in SCC order.  sigh.

public class RuntimeExceptionChecker extends ExceptionChecker {

	protected MethodDecl currentMethod;
	protected JLiftTypeSystem jlts;
	private final HashSet<Type> runtimeExceptionsForCurrentMethod;

	public RuntimeExceptionChecker(Job job, TypeSystem ts, NodeFactory nf) {
		super(job,ts,nf);
		jlts = (JLiftTypeSystem) ts;
		currentMethod = null;
		runtimeExceptionsForCurrentMethod = new HashSet<Type>();
	}

	// for each method, if it could throw a runtime exception that's
	// not specified by the header, implicitly add a "throws RuntimeException"
	// to the header

	// needs to run **before** the ExceptionChecker

	@Override
	public NodeVisitor enterCall(Node n) throws SemanticException {

		if (n instanceof MethodDecl) {
			if (Report.should_report("rtexc", 5))
				Report.report(5, "entering method " + n + " -- clearing runtime exceptions for current method " + currentMethod + " -- " + runtimeExceptionsForCurrentMethod);
			this.currentMethod = (MethodDecl) n;
			runtimeExceptionsForCurrentMethod.clear();
		}

		// don't exception-check field declarations
		if (n instanceof FieldDecl) {
			return this;
		}

        return n.del().exceptionCheckEnter(this);
	}

	@Override
	protected void reportUncaughtException(Type t, Position pos)
			throws SemanticException {
    	// if t is a subclass of runtime exception

    	if (jlts.exceptionTypeShouldAffectSecurity(t)) {
			if (Report.should_report("rtexc", 5))
				Report.report(5, "exception " + t + " possibly affects security");

    		runtimeExceptionsForCurrentMethod.add(t);
    	}
    	else {
    		String reason = "";
    		if (!ts.isUncheckedException(t)) {
    			reason += "not a runtime exception";
    		}
    		if (!jlts.exceptionTypeShouldAffectSecurity(t)) {
    			if (!reason.equals(""))
    				reason += ", and ";
    			reason += "doesn't affect security ";
    		}

    		if (Report.should_report("rtexc", 5)) {
    			Report.report(5, "not adding exception " + t + " for reason: " + reason);
    		}
    	}
	}

	public Collection<Type> getRuntimeExceptions() {
		return runtimeExceptionsForCurrentMethod;
	}
}
