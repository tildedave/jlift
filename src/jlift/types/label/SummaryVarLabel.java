/*
 * File          : SummaryVarLabel.java
 * Project       : jlift
 * Description   : Special kind of VarLabel_c.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:44:28 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

// TODO: might need to remove this?  It affects serialization.

package jlift.types.label;

import jif.types.JifTypeSystem;
import polyglot.util.Position;

public class SummaryVarLabel extends JLiftVarLabel_c {

	public SummaryVarLabel(String s, String description, JifTypeSystem ts, Position pos) {
		super(s,description,ts,pos);
	}
}
