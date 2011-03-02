/*
 * File          : SummaryLabelCheckGoal.java
 * Project       : jlift
 * Description   : Goal to label check the API summary constraints.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:39:05 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.frontend;

import jif.ast.JifNodeFactory;
import jif.types.JifTypeSystem;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.AbstractGoal;

public class SummaryLabelCheckGoal extends AbstractGoal {

	public SummaryLabelCheckGoal(Job job) {
		super(job);
	}

	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		jlift.ExtensionInfo jifext = (jlift.ExtensionInfo)extInfo;
        final JifTypeSystem ts = (JifTypeSystem) jifext.typeSystem();
        final JifNodeFactory nf = (JifNodeFactory) jifext.nodeFactory();
        return new SummaryLabelCheckPass(this, ts, nf, jifext.getCallGraph(), jifext.getSummaryInfo(), jifext);
	}

}
