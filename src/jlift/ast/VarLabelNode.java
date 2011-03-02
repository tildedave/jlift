/*
 * File          : VarLabelNode.java
 * Project       : jlift
 * Description   : A node that may contain a variable label.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:31:03 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.ast;

import jif.ast.LabelNode_c;
import jif.types.label.Label;
import polyglot.util.Position;

public class VarLabelNode extends LabelNode_c {

	public VarLabelNode(Position pos, Label label) {
		super(pos, label);
	}
}
