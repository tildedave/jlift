/*
 * File          : JLiftCallDel.java
 * Project       : jlift
 * Description   : Specialized call delegate for JLift.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:31:41 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

//DHK: We need this class because otherwise we don't have
//access to certain members (?)
//TODO: revisit -- do we need this?

package jlift.extension;

import java.util.List;

import jif.extension.JifCallDel;
import jif.types.label.VarLabel;
import polyglot.ast.Call;
import polyglot.ast.Node;
import polyglot.ast.Receiver;
import polyglot.types.SemanticException;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class JLiftCallDel extends JifCallDel {

	public VarLabel receiverVarLabel() {
		return this.receiverVarLabel;
	}

	public List argVarLabels() {
		return this.argVarLabels;
	}

	public List paramVarLabels() {
		return this.paramVarLabels;
	}

	@Override
	public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
		Call call = (Call) super.disambiguate(ar);
		if (call.target() != null) {
			if (call.target().del() instanceof JLiftTypeNodeDel) {
				JLiftTypeNodeDel del = (JLiftTypeNodeDel) call.target().del();
				del.setParent(node());
				Node newTarget = del.disambiguate(ar);
				Call newCall = call.target((Receiver) newTarget);
				return newCall.buildTypes(new TypeBuilder(ar.job(), ar.typeSystem(), ar.nodeFactory())).disambiguate(new AmbiguityRemover(ar.job(), ar.typeSystem(), ar.nodeFactory()));
			}
		}
		return call;
	}

        @Override
	public Node typeCheck(TypeChecker tc) throws SemanticException {
	    //System.out.println("type checking call: " + node().toString());
	    return super.typeCheck(tc);
	}

}
