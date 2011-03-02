/*
 * File          : JLiftExceptionsChecked.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:50 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.frontend;

import jlift.visit.JLiftExceptionChecker;
import polyglot.ast.NodeFactory;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.frontend.Scheduler;
import polyglot.frontend.VisitorPass;
import polyglot.frontend.goals.ExceptionsChecked;
import polyglot.frontend.goals.Goal;
import polyglot.types.TypeSystem;

// TODO: probably don't want this inheriting from ExceptionsChecked, it's artificial

public class JLiftExceptionsChecked extends ExceptionsChecked {

    protected TypeSystem ts;
	protected NodeFactory nf;

	protected JLiftExceptionsChecked(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
		this.ts = ts;
		this.nf = nf;
	}

	public static Goal create(Scheduler scheduler, Job job, TypeSystem ts, NodeFactory nf) {
        return scheduler.internGoal(new JLiftExceptionsChecked(job, ts, nf));
    }

	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		return new VisitorPass(this, new JLiftExceptionChecker(job, ts, nf));
	}
}
