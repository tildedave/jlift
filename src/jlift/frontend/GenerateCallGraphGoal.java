/*
 * File          : GenerateCallGraphGoal.java
 * Project       : jlift
 * Description   : Goal to construct the callgraph.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:36:41 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.frontend;

import jlift.util.CallGraph;
import jlift.visit.CallGraphVisitor;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.frontend.Scheduler;
import polyglot.frontend.goals.Barrier;
import polyglot.frontend.goals.Goal;
import polyglot.frontend.goals.VisitorGoal;
import polyglot.types.TypeSystem;

public class GenerateCallGraphGoal extends Barrier implements Goal {

	protected CallGraph theCallGraph;
	protected TypeSystem theTypeSystem;
	protected NodeFactory theNodeFactory;

	public GenerateCallGraphGoal(Scheduler jifScheduler, CallGraph theCallGraph, TypeSystem theTypeSystem, NodeFactory theNodeFactory) {
		super(jifScheduler);
		this.theCallGraph = theCallGraph;
		this.theTypeSystem = theTypeSystem;
		this.theNodeFactory = theNodeFactory;
	}

	@Override
	public Goal goalForJob(Job job) {
		return this.scheduler.internGoal(new VisitorGoal(job, new CallGraphVisitor(job, theCallGraph, theTypeSystem, theNodeFactory)));
	}

}
