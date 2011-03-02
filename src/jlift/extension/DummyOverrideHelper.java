/*
 * File          : DummyOverrideHelper.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:49 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.extension;

import java.util.List;

import jif.extension.CallHelper;
import jif.types.JifProcedureInstance;
import jif.types.label.Label;
import jif.visit.LabelChecker;
import polyglot.ast.Receiver;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.util.Position;

public class DummyOverrideHelper extends CallHelper {

	public DummyOverrideHelper(Label receiverLabel, Receiver receiver,
			ReferenceType calleeContainer, JifProcedureInstance pi,
			List actualArgs, Position position) {
		super(receiverLabel, receiver, calleeContainer, pi, actualArgs, position, true);
	}

	@Override
	public void checkOverride(LabelChecker lc) throws SemanticException {
		// do nothing
	}

}
