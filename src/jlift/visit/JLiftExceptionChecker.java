/*
 * File          : JLiftExceptionChecker.java
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

import jlift.types.JLiftTypeSystem;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.ExceptionChecker;

public class JLiftExceptionChecker extends ExceptionChecker {

	protected JLiftTypeSystem jts;

	public JLiftExceptionChecker(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
		jts = (JLiftTypeSystem) ts;
	}

	@Override
	protected void reportUncaughtException(Type t, Position pos)
	throws SemanticException {
		try {
			super.reportUncaughtException(t, pos);
		}
		catch (SemanticException e) {
			// can't override behavior in public static ExceptionChecker, but
			// can prevent the message from making it any further
			if (this.reporter instanceof CodeTypeReporter) {
				if (((CodeTypeReporter) this.reporter).codeType.equals("field initializer")) {
					// nothing!
					return;
				}
			}
			if (e.getMessage().contains("can not throw a") &&
					!jts.exceptionTypeShouldAffectSecurity(t)) {
				// do nothing
				return;
			}
			if (e.getMessage().contains("can not throw a") &&
					e.getMessage().contains("initializer block")) {
				// do nothing
				return;
			}
			if (e.getMessage().contains("must either be caught") &&
					!jts.exceptionTypeShouldAffectSecurity(t)) {
				return;
			}
			else {
				throw e;
			}
		}
	}
}
