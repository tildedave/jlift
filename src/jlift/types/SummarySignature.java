/*
 * File          : SummarySignature.java
 * Project       : jlift
 * Description   : <Insert Description>
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:41:31 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.Iterator;

import jif.ast.JifProcedureDecl;
import jif.types.JifProcedureInstance;
import jif.types.JifTypeSystem;
import jif.types.label.Label;
import jlift.types.label.SummaryVarLabel;
import jlift.util.NumberProvider;
import polyglot.ast.FieldDecl;
import polyglot.ast.Formal;
import polyglot.ast.ProcedureDecl;
import polyglot.ast.TypeNode;
import polyglot.types.Type;
import polyglot.util.Position;

public class SummarySignature extends JLiftFixedSignature {

	JLiftTypeSystem jts;
	private final Integer currentArrayIndex;

	public SummarySignature(JifTypeSystem ts) {
		super(ts);
		this.jts = (JLiftTypeSystem) ts;
		currentArrayIndex = new Integer(0);
	}

	@Override
	public Label defaultPCBound(Position pos, String methodName) {
		return jts.freshSummaryVariable(pos,
				"sum(caller_pc@" + methodName + ":" + pos.toString() + ")",
				"upper bound for the pc at the call site of " + methodName);
	}

	@Override
	public Label defaultArgBound(Formal f) {
		String argName = f.name();
		//System.err.println("default arg bound for formal " + f + "@" + f.position());
		return jts.freshSummaryVariable(f.position(), "sum(" + argName + ")",
				"upper bound for the formal argument " + argName);
	}

	@Override
	public Label defaultArrayBaseLabel(Position pos) {
		//String name = "array_base";
		/*String name = "array_base";
		if (pos != null) {
			name += "@" + pos.toString();
		}
		else {
			name += currentArrayIndex;
			++currentArrayIndex;
		}*/

		String name = "NV" + NumberProvider.getNextNumber();

		return ts.freshLabelVariable(pos, name, "label of the contents of the array at position " + pos);
	}

	@Override
	public Label defaultArrayBaseLabel(Type baseType) {
		return defaultArrayBaseLabel(baseType.position());
	}

	@Override
	public Label defaultReturnLabel(ProcedureDecl pd) {
//		Label Lr = ts.freshLabelVariable(pd.position(), pd.name() + "_return", "return label of the procedure decl " + pd.name());
		Label Lr = ts.bottomLabel();

		for (Iterator i = pd.throwTypes().iterator(); i.hasNext(); ) {
			TypeNode tn = (TypeNode) i.next();
			Label excLabel = ts.labelOfType(tn.type(), ts.bottomLabel());
			Lr = ts.join(Lr, excLabel);
		}

		// DHK: having issues with this.  this pollutes the PC every
		// time this is called.  return value and return are very different things.
		// keeping at bottom should be okay, though we might have more issues with
		// exceptions being thrown earlier?
//		if (pd instanceof MethodDecl) {
//			MethodDecl md = (MethodDecl) pd;
//			Type declReturnType = md.returnType().type();
//			if (ts.isLabeled(declReturnType))
//				Lr = ts.join(Lr, ts.labelOfType(declReturnType));
//		}

		return Lr;
	}

	/* (non-Javadoc)
	 * @see jif.types.FixedSignature#defaultReturnValueLabel(polyglot.ast.ProcedureDecl)
	 */
	@Override
	public Label defaultReturnValueLabel(ProcedureDecl pd) {
		JifProcedureDecl jpd = (JifProcedureDecl) pd;
		JifProcedureInstance pi = (JifProcedureInstance)pd.procedureInstance();

		Label Lrv;
		if (jpd.returnLabel() != null)
			Lrv = jpd.returnLabel().label();
		else {
			Lrv = ((JLiftTypeSystem) ts).freshSummaryVariable(pd.position(), "sum(" + pd.name() + "_rv)", "return value label of the procedure decl " + pd.name());
			//Lrv = ((JLiftTypeSystem) ts).freshLabelVariable(pd.position(), pd.name() + "_rv", "return value label of the procedure decl " + pd.name());
			if (pi instanceof JLiftMethodInstance_c && Lrv instanceof SummaryVarLabel) {
				JLiftMethodInstance_c jmi = (JLiftMethodInstance_c) pi;
				jmi.setSummaryVarLabel((SummaryVarLabel) Lrv);
			}
		}

		JifTypeSystem jts = (JifTypeSystem)pi.typeSystem();

//		for (Iterator i = pi.formalTypes().iterator(); i.hasNext(); ) {
//			Type t = (Type)i.next();
//			ArgLabel a = (ArgLabel)jts.labelOfType(t);
//			Lrv = ts.join(Lrv, a);
//		}
		return Lrv;
	}


	@Override
	public Label defaultFieldLabel(FieldDecl fd) {
    	return jts.freshLabelVariable(fd.position(), "inst(" + fd.name() + ")", "summary variable for " + fd.name());
	}
}
