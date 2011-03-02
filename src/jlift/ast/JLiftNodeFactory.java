/*
 * File          : JLiftNodeFactory.java
 * Project       : jlift
 * Description   : Interface for JLiftNodeFactory (for creating new AST nodes)
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:30:43 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.ast;

import jif.ast.JifNodeFactory;
import jif.types.label.Label;
import polyglot.util.Position;

public interface JLiftNodeFactory extends JifNodeFactory {
	public VarLabelNode VarLabelNode(Position pos, Label varLabel);
}
