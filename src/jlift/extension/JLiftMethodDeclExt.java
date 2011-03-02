/*
 * File          : JLiftMethodDeclExt.java
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

import jif.extension.JifMethodDeclExt;
import jif.translate.ToJavaExt;
import jif.types.JifProcedureInstance;
import jif.types.PathMap;
import jif.types.label.Label;
import jif.visit.LabelChecker;
import polyglot.types.SemanticException;
import polyglot.types.Type;

public class JLiftMethodDeclExt extends JifMethodDeclExt {

	JLiftProcedureDeclExtMixin mixin;

	public JLiftMethodDeclExt(ToJavaExt toJava) {
		super(toJava);
		this.mixin = new JLiftProcedureDeclExtMixin(toJava);
	}

	@Override
	protected void addReturnConstraints(Label Li, PathMap X,
			JifProcedureInstance mi, LabelChecker lc, Type returnType)
			throws SemanticException {
		mixin.addReturnConstraints(node(), Li, X, mi, lc, returnType);
	}
}
