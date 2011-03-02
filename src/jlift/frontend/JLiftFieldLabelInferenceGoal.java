/*
 * File          : JLiftFieldLabelInferenceGoal.java
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

import jif.FieldLabelInferenceGoal;
import jif.ast.JifNodeFactory;
import jif.types.JifTypeSystem;
import jlift.visit.JLiftFieldLabelResolver;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.frontend.VisitorPass;

public class JLiftFieldLabelInferenceGoal extends FieldLabelInferenceGoal {

	public JLiftFieldLabelInferenceGoal(Job job) {
		super(job);
	}

	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		ExtensionInfo jifext = extInfo;
		final JifTypeSystem ts = (JifTypeSystem) jifext.typeSystem();
		final JifNodeFactory nf = (JifNodeFactory) jifext.nodeFactory();
		return new VisitorPass(this, new JLiftFieldLabelResolver(this.job(), ts, nf));
	}

}
