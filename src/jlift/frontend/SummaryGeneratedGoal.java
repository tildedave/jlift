/*
 * File          : SummaryGeneratedGoal.java
 * Project       : jlift
 * Description   : Goal to generate the summary constraints for the entire program.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:37:53 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.frontend;

import jif.ast.JifNodeFactory;
import jif.types.JifTypeSystem;
import jlift.ExtensionInfo;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.AbstractGoal;

public class SummaryGeneratedGoal extends AbstractGoal {

	public SummaryGeneratedGoal() {
		super(null);
	}

	@Override
	public Pass createPass(polyglot.frontend.ExtensionInfo extInfo) {
		ExtensionInfo jifext = (ExtensionInfo)extInfo;
        final JifTypeSystem ts = (JifTypeSystem) jifext.typeSystem();
        final JifNodeFactory nf = (JifNodeFactory) jifext.nodeFactory();
        //return new LabelCheckPass(this, this.job(),
        //                         new LabelChecker(this.job(), ts, nf, !jifext.getJifOptions().solveGlobally, !jifext.getJifOptions().solveGlobally));
        return new SummaryGeneratingPass(this, jifext, ts, nf, jifext.getCallGraph(), jifext.getSummaryInfo());
	}

//	@Override
//	public Collection prerequisiteGoals(Scheduler scheduler) {
//		Collection prereqGoals = new LinkedList();
//		prereqGoals.add(scheduler.internGoal(new DynamicLabelAnalysisGoal()));
//
//		return prereqGoals;
//	}

}
