/*
 * File          : RuntimeExceptionsAdded.java
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

import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.AbstractGoal;
import polyglot.frontend.goals.Goal;

public class RuntimeExceptionsAdded extends AbstractGoal {

	protected ExtensionInfo extInfo;

	protected RuntimeExceptionsAdded(JLiftScheduler scheduler,
			ExtensionInfo extInfo) {
		super(null); // has no job
		this.extInfo = extInfo;
	}

	public static Goal createGoal(JLiftScheduler scheduler,
			ExtensionInfo extInfo) {
		return new RuntimeExceptionsAdded(scheduler, extInfo);
	}

	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		return new RuntimeExceptionAddedPass(this, (jlift.ExtensionInfo) extInfo);
	}

}
