/*
 * File          : JLiftFixedSignature.java
 * Project       : jlift
 * Description   : <Insert Description>
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:55:01 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import jif.types.FixedSignature;
import jif.types.JifTypeSystem;
import jif.types.label.Label;
import polyglot.ast.FieldDecl;
import polyglot.util.Position;

public class JLiftFixedSignature extends FixedSignature implements JLiftDefaultSignature {

	protected JifTypeSystem ts;

    public JLiftFixedSignature(JifTypeSystem ts) {
		super(ts);
		this.ts = ts;
	}

	@Override
	public Label defaultFieldLabel(FieldDecl fd) {
    	return ts.noComponentsLabel(fd.position());
    }

	public Label defaultArrayBaseLabel(Position pos) {
		return ts.noComponentsLabel(pos);
	}
}
