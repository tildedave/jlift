/*
 * File          : JLiftFormalDel.java
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
import jif.types.JifLocalInstance;
import jif.types.JifTypeSystem;
import jif.types.label.ArgLabel;
import jif.types.label.Label;
import jlift.SummaryConstraintInfo;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.TypeBuilder;

public class JLiftFormalDel extends JifFormalDel {

    private boolean explicitFinalFlag = false;

    @Override
	public boolean hasExplicitFinalFlag() {
        return explicitFinalFlag;
    }

	@Override
	public Node buildTypes(TypeBuilder tb) throws SemanticException {
		Formal n = (Formal) this.node();
		this.explicitFinalFlag = n.flags().isFinal();
		n = (Formal) super.buildTypes(tb);

		JifTypeSystem jts = (JifTypeSystem) tb.typeSystem();

		if (!this.explicitFinalFlag && n.declType().isPrimitive() &&
				!n.declType().equals(jts.Label()) && !n.declType().equals(jts.Principal())) {
			n = n.flags(n.flags().clearFinal());
		}

		return n;

//		// all formals are final
//		// DHK -- unless they are of a primitive type
//
//		JifLocalInstance li = (JifLocalInstance)n.localInstance();
//
//		if (!n.declType().isPrimitive()) {
//			n = n.flags(n.flags().Final());
//			li = (JifLocalInstance)n.localInstance();
//			li.setFlags(li.flags().Final());
//			System.err.println("making node: " + n + " final");
//		}
//
//		JifTypeSystem jts = (JifTypeSystem)tb.typeSystem();
//
//
//		if (isCatchFormal()) {
//			// formals occuring in a catch clause are treated more like local decls;
//			// their label is a VarLabel
//			li.setLabel(jts.freshLabelVariable(li.position(), li.name(), "label of the formal " + li.name()));
//		}
//		else {
//			Position pos = n.type().position();
//			if (n.type() instanceof LabeledTypeNode) {
//				LabelNode ln = ((LabeledTypeNode)n.type()).labelPart();
//				if (ln != null) {
//					pos = ln.position();
//				}
//			}
//
//			// method and constructor formals have an ArgLabel
//			ArgLabel al = jts.argLabel(pos, li, null);
//			li.setLabel(al);
//		}
//
//		n = n.localInstance(li);
//		return n;
//		return n;
	}

	@Override
	public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
		// in the case that this is a formal with a type that contains
		// label variables, we need to replace those variables within its type
		// with summary variables.  for example, an array formal contains an array_base
		// that might be different on different calls of that method.
		Formal n = (Formal) node();
		JifTypeSystem jts = (JifTypeSystem)ar.typeSystem();

		JifLocalInstance li = (JifLocalInstance)n.localInstance();
		li.setFlags(n.flags());
		li.setName(n.name());
		li.setType(n.declType());
		//System.err.println("JifFormalDel.disamb: n.type: "  + n.type().getClass() );

		if (!n.type().isDisambiguated()) {
			ar.job().extensionInfo().scheduler().currentGoal().setUnreachableThisRun();
			return n;
		}


		if (!isCatchFormal()) {
			ArgLabel al = (ArgLabel)li.label();

			al.setCodeInstance(ar.context().currentCode());

			if (al.upperBound() == null) {
				// haven't set the arg label yet
				// do so now.

				if (!jts.isLabeled(n.declType())) {
					// declared type isn't labeled, use the default arg bound
					Type lblType = n.declType();
					Position pos = lblType.position();
					Label defaultBound = jts.defaultSignature().defaultArgBound(n);
					// dhk: substitute variables for summary variables in the type, for example,
					// if the type is an array type
					lblType = SummaryConstraintInfo.replaceVariablesWithSummaryVariables(lblType);
					lblType = jts.labeledType(pos, lblType, defaultBound);
					n = n.type(n.type().type(lblType));
				}

				// now take the label of the declared type, and set it to
				// be the bound
				al.setUpperBound(jts.labelOfType(n.declType()));

				// now set the label of the declared type to be the arg label
				Type lblType = n.declType();
				lblType = jts.labeledType(lblType.position(), jts.unlabel(lblType), al);
				n = n.type(n.type().type(lblType));

				//System.err.println("changed formal " + node() + " to " + n);
			}
			else {
//				System.err.println("leaving formal " + n + " alone");
			}
		}

		return n;

	}
}
