/*
 * File          : MockLabelChecker.java
 * Project       : jlift
 * Description   : LabelChecker that just stores its information.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:58:09 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.visit;

import java.util.Collection;
import java.util.LinkedList;

import jif.types.LabelConstraint;
import jif.visit.LabelChecker;
import jlift.types.JLiftLabelCheckingUtil;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;

public class MockLabelChecker extends LabelChecker {

	private final Collection<LabelConstraint> constraints;

	public MockLabelChecker(Job job, TypeSystem ts, NodeFactory nf, boolean solvePerClassBody, boolean solvePerMethod) {
		super(job, ts, nf, solvePerClassBody, solvePerMethod, false);
		constraints = new LinkedList<LabelConstraint>();
		JLiftLabelCheckingUtil.global = this;
	}

	@Override
	public void constrain(LabelConstraint c) throws SemanticException {
		constraints.add(c);
	}

	public Collection<LabelConstraint> getConstraints() {
		return constraints;
	}
}
