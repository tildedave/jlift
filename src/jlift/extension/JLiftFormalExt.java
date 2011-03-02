/*
 * File          : JLiftFormalExt.java
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

import jif.extension.JifFormalDel;
import jif.extension.JifFormalExt;
import jif.translate.ToJavaExt;
import jif.types.JifContext;
import jif.types.JifLocalInstance;
import jif.types.JifTypeSystem;
import jif.visit.LabelChecker;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.types.SemanticException;

public class JLiftFormalExt extends JifFormalExt {

	public JLiftFormalExt(ToJavaExt toJava) {
		super(toJava);
	}

	@Override
	public Node labelCheck(LabelChecker lc) throws SemanticException {
		JifContext A = lc.jifContext();
		A = (JifContext) node().del().enterScope(A);

		Formal f = (Formal)node();
		JifFormalDel jfd = (JifFormalDel)f.del();
		JifTypeSystem ts = lc.jifTypeSystem();
		JifLocalInstance li = (JifLocalInstance)f.localInstance();
		return node();
	}

}
