/*
 * File          : JLiftVarMap.java
 * Project       : jlift
 * Description   : Variable Map for JLift -- extends jif.types.VarMap
 * Author(s)     : dhking
 *
 * Created       : Dec 3, 2007 1:09:59 PM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 */

package jlift.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import jif.types.JifTypeSystem;
import jif.types.VarMap;
import jif.types.label.Label;
import jif.types.label.VarLabel;

public class JLiftVarMap extends VarMap {

	public JLiftVarMap(JifTypeSystem ts, Label defaultBound) {
		super(ts, defaultBound);
	}

	private JLiftVarMap(JifTypeSystem ts, Map bounds, Label defaultBound) {
		super(ts, defaultBound);
		// superclass constructor for bounds is private
		this.bounds = bounds;
	}

	@Override
	public JLiftVarMap copy() {
		return new JLiftVarMap(ts, bounds, defaultBound);
	}

	public String longString() {
		StringBuffer sb = new StringBuffer();
		sb.append("======== VAR MAP ========\n");
		for (Iterator i = bounds.entrySet().iterator(); i.hasNext(); ){
			Map.Entry e = (Map.Entry) i.next();
			VarLabel var = (VarLabel) e.getKey();
			Label bound = (Label) e.getValue();
			String s = var.componentString() + " = " + bound.toString();
			if (var.description() != null) {
				s += "    \t" + var.description();
			}
			sb.append(s + "\n");
		}
		sb.append("=========================\n");

		return sb.toString();
	}

	// added by DHK
	public Collection<VarLabel> domain() {
		return bounds.keySet();
	}
}
