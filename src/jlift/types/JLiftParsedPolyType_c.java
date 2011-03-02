/*
 * File          : JLiftParsedPolyType_c.java
 * Project       : jlift
 * Description   : Extension of JifParsedPolyType_c that keeps track of
 * 				   interfaces/subtypes/implementers.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:38:24 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jif.types.JifParsedPolyType_c;
import jif.types.JifSubstClassType_c;
import polyglot.frontend.Source;
import polyglot.types.ClassType;
import polyglot.types.LazyClassInitializer;
import polyglot.types.ParsedClassType;
import polyglot.types.ReferenceType;
import polyglot.types.Type;

public class JLiftParsedPolyType_c extends JifParsedPolyType_c implements
JLiftParsedPolyType {

	public JLiftParsedPolyType_c(JLiftTypeSystem_c ts,
			LazyClassInitializer init, Source fromSource) {
		super(ts, init, fromSource);
	}

	@Override
	public void superType(Type t)
	{
		// first set this thing's supertype
		super.superType(t);
		// then set t's subtype
		if (t instanceof JLiftParsedPolyType_c)
			((JLiftParsedPolyType_c) t).addSubtype(this);
	}

	@Override
	public void addInterface(Type t) {
		super.addInterface(t);
		if (t instanceof JLiftParsedPolyType_c) {
			((JLiftParsedPolyType_c) t).addImplementer(this);
		}
		else if (t instanceof JifSubstClassType_c) {
			Type ct = ((JifSubstClassType_c) t).base();
			if (ct instanceof JLiftParsedPolyType_c) {
				((JLiftParsedPolyType_c) ct).addImplementer(this);
			}
			else if (ct instanceof JifParsedPolyType_c) {
				// need to register w/ type system
				//System.err.println("need to register that " + ct + " is implemented by " + this + " w/ type system");
				((JLiftTypeSystem_c) this.typeSystem()).registerInterfaceImplemented((ParsedClassType) ct, this);
			}
		}
		else if (t instanceof JifParsedPolyType_c) {
			// need to register w/ type system
			//System.err.println("need to register that " + t + " is implemented by " + this + " w/ type system");
			((JLiftTypeSystem_c) this.typeSystem()).registerInterfaceImplemented((ParsedClassType) t, this);
		}

	}

	@Override
	public void setInterfaces(List interfaces) {
		super.setInterfaces(interfaces);
		Iterator it = interfaces.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof JLiftParsedPolyType_c) {
				((JLiftParsedPolyType_c) o).addImplementer(this);
			}
			if (o instanceof JifSubstClassType_c) {
			}
		}
	}


	List<ReferenceType> subtypes = new LinkedList<ReferenceType>();

	public void addSubtype(ReferenceType t) {
		this.subtypes.add(t);
	}

	public List<ReferenceType> getSubtypes() {
		return this.subtypes;
	}

	List<ReferenceType> implementers = new LinkedList<ReferenceType>();

	public void addImplementer(ReferenceType t) {
		this.implementers.add(t);
	}

	public List<ReferenceType> getImplementers() {
		return this.implementers;
	}

	@Override
	public void kind(Kind kind) {
		this.kind = kind;
	}

	@Override
	public void addMemberClass(ClassType t) {
		memberClasses.add(t);
	}

}