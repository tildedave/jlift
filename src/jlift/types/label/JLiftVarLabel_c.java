/*
 * File          : JLiftVarLabel_c.java
 * Project       : jlift
 * Description   : Special VarLabel_c.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:42:59 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types.label;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jif.types.JifTypeSystem;
import jif.types.label.VarLabel;
import jlift.JLiftOptions;
import jlift.util.MultiMap;
import polyglot.types.TypeObject;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class JLiftVarLabel_c extends jif.types.label.VarLabel_c implements JLiftVarLabel {
	private final transient int uid = ++counter;
	private static int counter = 0;

	protected String name;

	// TODO: this is very ugly.  why are we creating duplicate variables in the first place?
	protected static MultiMap<String, VarLabel> nameToVarLabelMap = new MultiMap<String, VarLabel>();
	protected static Map<VarLabel, String> varLabelToEventualNameMap = new HashMap<VarLabel, String>();
	protected static Map<VarLabel, String> varLabelToTraceMap = new HashMap<VarLabel, String>();

	public JLiftVarLabel_c(String name, String description, JifTypeSystem ts, Position pos) {
		super(name, description, ts, pos);
		this.name = name;

		int varDebugLevel = JLiftOptions.getInstance().varDebugLevel();
		boolean varDebug = (varDebugLevel > 0);

		String stackTraceString = "";
		if (varDebug) {
			Throwable t = new Throwable();
			t.fillInStackTrace();
			Object[] stackTrace = t.getStackTrace();
			for(int i = 1; i <= varDebugLevel; ++i) {
				stackTraceString += "\t" + stackTrace[i] + "\n";
			}
		}
		if (nameToVarLabelMap.containsKey(name) && nameToVarLabelMap.get(name).size() > 0) {
			// determine if there is a clash.  if there is a clash, add an index
			boolean foundVariable = true;
			boolean printed = false;
			for(VarLabel v : nameToVarLabelMap.get(name))
				if (v.position() != null && v.position().equals(pos)) {
					foundVariable = false;
					this.name = varLabelToEventualNameMap.get(v);
					if (varDebug) {
						if (printed == false) {
							System.err.println("--------------------------------------------");
							System.err.println("VARIABLE NAMING CONFLICT: variable with name " + name);
							System.err.println("created by: ");
							System.err.println(stackTraceString);
							printed = true;
						}
						System.err.println("conflicts with " + v + ": created by stack trace:\n" + varLabelToTraceMap.get(v));
					}
				}

			if (foundVariable) {
				int nameIndex = nameToVarLabelMap.get(name).size() + 1;
				this.name = name + ":" + nameIndex;
				nameToVarLabelMap.put(name, this);
				if (varDebug) {
					System.err.println("--- CLASH ON NAME: " + name);
					for(VarLabel v : nameToVarLabelMap.get(name))
						System.err.println("\t" + v + " @ " + v.position());
				}
			}
		}
		else {
			nameToVarLabelMap.put(name, this);
		}
		setDescription(description);
		varLabelToEventualNameMap.put(this, this.name);
		varLabelToTraceMap.put(this, stackTraceString);

	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean equalsImpl(TypeObject o) {
		//return this == o;
		if (o instanceof JLiftVarLabel_c) {
			boolean nameEqual = name.equals(((JLiftVarLabel_c) o).name);
			boolean positionEqual = (((position != null) && position.equals(o.position())) || position == o.position());

			if (nameEqual && !positionEqual && o.toString().equals(toString()))
				throw new InternalCompilerError(this + " and " + o + " are not equal, but have the same name -- " + position() + " vs " + o.position());

			return nameEqual && positionEqual;
		}
		return this == o;
	}
	@Override
	public int hashCode() {
		return -56393 + name.hashCode() + ((position == null) ? -1000 : position.hashCode());
		//return super.hashCode();
	}

	@Override
	public String componentString(Set printedLabels) {
//		if (Report.should_report(Report.debug, 2)) {
//		return "<var " + name + " " + uid +  " " + System.identityHashCode(this)+ ">";
//		}
//		if (Report.should_report(Report.debug, 1)) {
//		return "<var " + name + ">";
//		}
//		if (hashCodeToIndexMap.containsKey(hashCode()) && hashCodeToIndexMap.get(hashCode()) > 1) {
//		Integer index = hashCodeToIndexMap.get(hashCode());
//		return name + "_" + index;
//		}
		return name;
	}

	@Override
	public boolean isRuntimeRepresentable() {
		return true;
	}
}
