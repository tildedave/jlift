/*
 * File          : JLiftContext_c.java
 * Project       : jlift
 * Description   : Specialized implementation of JifContext.  Generates PC variables.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:54:15 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.HashSet;
import java.util.Set;

import jif.types.JifContext;
import jif.types.JifContext_c;
import jif.types.JifTypeSystem;
import jif.types.NamedLabel;
import jif.types.PathMap;
import jif.types.label.Label;
import jif.types.label.NotTaken;
import jif.types.label.VarLabel;
import jif.visit.LabelChecker;
import jlift.JLiftOptions;
import jlift.util.NumberProvider;
import jlift.visit.SummaryConstraintGenerator;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.types.Context;
import polyglot.types.Context_c;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class JLiftContext_c extends JifContext_c {

	protected VarLabel currentPCVariable;
	protected Label currentPCLabel;
	public static int pcNumber = 0;
	protected LabelChecker lc;
	private boolean insideDeclassify;

	public JLiftContext_c(JifTypeSystem ts, TypeSystem jlts, LabelChecker lc) {
		super(ts, jlts);
		this.lc = lc;
		this.insideDeclassify = false;
	}

	public void setLabelChecker(LabelChecker lc)
	{
		this.lc = lc;
	}

	public LabelChecker getLabelChecker()
	{
		return this.lc;
	}

	@Override
	public Set authority() {
		if (super.authority() == null)
			return new HashSet();
		return super.authority();
	}

	@Override
	public Object copy() {
		JLiftContext_c ctxt = (JLiftContext_c)super.copy();
		if (ctxt.currentPCVariable == null)
			ctxt.currentPCVariable = this.currentPCVariable;
		if (ctxt.lc == null)
			ctxt.lc = this.lc;
		if (ctxt.currentPCLabel == null)
			ctxt.currentPCLabel = this.currentPCLabel;
		ctxt.insideDeclassify = this.insideDeclassify;

		return ctxt;
	}

	@Override
	public Label pc() {
		if (JLiftOptions.getInstance().shouldIgnoreImplicitFlows()) {
			return ((JifTypeSystem) ts).bottomLabel();
		}
		else {
			if (shouldUsePCVariables())
				return this.currentPCVariable;
			return super.pc();
		}
	}

	private boolean shouldUsePCVariables() {
		// should use PC variables if we haven't disabled it in the options
//		if (JifOptions.shouldGenerateDeclassificationConstraints())
//			return false;

		return (JLiftOptions.getInstance().noVariablesForPCs == false);
	}

	@Override
	public void setPc(Label pc, LabelChecker lc) {
		Position pcPosition;
		if (lc instanceof SummaryConstraintGenerator) {
			SummaryConstraintGenerator scg = (SummaryConstraintGenerator) lc;
			pcPosition = scg.getCurrentNode().position();
		}
		else {
			pcPosition = pc.position();
		}

		this.setPc(pc, lc, pcPosition);
	}

	public void setPc(Label pc, LabelChecker lc, Position p) {
		Label currentPC = super.pc();

		if (JLiftOptions.getInstance().shouldIgnoreImplicitFlows() || insideDeclassify) {
			if (!(pc instanceof NotTaken)) {
				super.setPc(((JifTypeSystem) ts).bottomLabel(), lc);
			}
			else {
				super.setPc(pc, lc);
			}
			return;
		}

		if (!shouldUsePCVariables()) {
			super.setPc(pc, lc);
			return;
		}

		if (currentPC != pc) {
			VarLabel oldPCVariable = this.currentPCVariable;
			generateNewPCVariable(p);
			super.setPc(currentPCVariable, lc);
			this.currentPCLabel = pc;
			try{
				if (!(pc instanceof NotTaken)) {
					JLiftTypeSystem jts = (JLiftTypeSystem) ts;
//					LabelConstraint pcLabelEqualityConstraint = jts.definitionalLabelConstraint(currentPCVariable,
//							pc,
//							labelEnv(),
//							p);

					Label newPC;
					// ?????????
					if (oldPCVariable != null && JLiftOptions.getInstance().safeMutateConstraintSet)
						newPC = jts.join(oldPCVariable, pc);
					else
						newPC = pc;
					lc.constrain(new NamedLabel("",currentPCVariable),
							JLiftLabelConstraint.DEFINE,
							new NamedLabel("",newPC),
							labelEnv(),
							p);
				}
			}
			catch (SemanticException e) {
				throw new InternalCompilerError("Unexpected Semantic Exception: " + e);
			}

			Throwable stack = new Throwable();
			stack.fillInStackTrace();
			if (Report.should_report("pcvars", 1)) {
				Report.report(1,"---");
				Report.report(1,"setPc called by " + stack.getStackTrace()[1].toString());
				if (lc instanceof SummaryConstraintGenerator) {
					Node currentNode = ((SummaryConstraintGenerator) lc).getCurrentNode();
					Report.report(1,"current node " + currentNode + " (" + currentNode.getClass() + ") with " + currentNode.position());
				}
				Report.report(1,"pc now " + pc() + " and " + currentPCLabel);
				Report.report(1,"---");
			}
		}
	}

	private void generateNewPCVariable(Position pos) {
		int number = NumberProvider.getNextNumber();
		currentPCVariable = ((JLiftTypeSystem) ts).freshLabelVariable(pos, "pc" + number, "pc at position " + pos);
	}

	@Override
	public void setCurrentCodePCBound(Label currentCodePCBound) {
		if (JLiftOptions.getInstance().shouldIgnoreImplicitFlows()) {
			if (currentCodePCBound instanceof NotTaken)
			super.setCurrentCodePCBound(((JifTypeSystem) ts).bottomLabel());
		}

		super.setCurrentCodePCBound(currentCodePCBound);
	}

	@Override
	public Label currentCodePCBound() {
		if (JLiftOptions.getInstance().shouldIgnoreImplicitFlows()) {
			return ((JifTypeSystem) ts).bottomLabel();
		}
		return super.currentCodePCBound();
	}

	public Context insideDeclassify(boolean b) {
		JLiftContext_c jc = (JLiftContext_c) this.copy();
		jc.insideDeclassify = b;
		return jc;
	}

	public boolean insideDeclassify() {
		return this.insideDeclassify;
	}

	@Override
	protected Context_c push() {
		Context_c retContext = super.push();
		JLiftLabelCheckingUtil.globalContextStack.push((JifContext) retContext);
		return retContext;
	}

	@Override
	public Context pop() {
		Context retContext = super.pop();
		JLiftLabelCheckingUtil.globalContextStack.pop();
		return retContext;
	}

    @Override
	public PathMap pathMapForLocal(LocalInstance li, LabelChecker lc) {
        JifTypeSystem ts = lc.jifTypeSystem();
        Label L = null;
        if (checkedEndorsements != null && checkedEndorsements.containsKey(li)) {
            L = checkedEndorsements.get(li);
        }
        else {
        	if (JLiftOptions.getInstance().safeMutateConstraintSet) {
        		L = ts.labelOfLocal(li, ts.bottomLabel());
        	}
        	else {
        		L = ts.labelOfLocal(li, this.pc());
        	}
        }

        PathMap X = ts.pathMap();
        X = X.N(this.pc());
        if (JLiftOptions.getInstance().safeMutateConstraintSet) {
        	X = X.NV(lc.upperBound(L, ts.bottomLabel()));
        }
        else {
        	X = X.NV(lc.upperBound(L, this.pc()));
        }
        return X;
    }


}
