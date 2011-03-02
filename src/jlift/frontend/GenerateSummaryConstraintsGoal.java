/*
 * File          : GenerateSummaryConstraintsGoal.java
 * Project       : jlift
 * Description   : Goal to generate the summary constraints.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:36:59 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.frontend;

import polyglot.frontend.Job;
import polyglot.frontend.Scheduler;
import polyglot.frontend.goals.Barrier;
import polyglot.frontend.goals.Goal;

public class GenerateSummaryConstraintsGoal extends Barrier implements Goal {

	protected static Goal summaryGeneratedGoal = null;

	protected GenerateSummaryConstraintsGoal(Scheduler jifScheduler) {
		super(jifScheduler);
	}

	protected synchronized Goal getGoal()
	{
		if (summaryGeneratedGoal == null)
			summaryGeneratedGoal = new SummaryGeneratedGoal();

		return summaryGeneratedGoal;
	}

	@Override
	public Goal goalForJob(Job job) {
		return this.scheduler.internGoal(summaryGeneratedGoal);
	}

}
