/*
 * File          : FindProcedureDeclVisitor.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:53 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.visit;

import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.frontend.Job;
import polyglot.types.ProcedureInstance;
import polyglot.visit.NodeVisitor;

public class FindProcedureDeclVisitor extends NodeVisitor {

	protected ProcedureInstance findPi;
	protected ProcedureDecl pd;

	public FindProcedureDeclVisitor(Job job, ProcedureInstance pi) {
		super();
		this.findPi = pi;
		this.pd = null;
	}

	@Override
	public Node leave(Node old, Node n, NodeVisitor v) {
		if (n instanceof ProcedureDecl) {
			ProcedureDecl nProcedureDecl = (ProcedureDecl) n;
			if (nProcedureDecl.procedureInstance().equals(findPi))
				this.pd = nProcedureDecl;
		}

		return n;
	}

	public ProcedureDecl procedureDecl() {
		return pd;
	}



}
