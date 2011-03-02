/*
 * File          : SummaryCallHelper.java
 * Project       : jlift
 * Description   : Extension of the Jif CallHelper to use/replace summary variables
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:24:46 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import jif.ast.JifNodeFactory;
import jif.ast.Jif_c;
import jif.extension.CallHelper;
import jif.types.JifClassType;
import jif.types.JifContext;
import jif.types.JifMethodInstance;
import jif.types.JifProcedureInstance;
import jif.types.JifTypeSystem;
import jif.types.LabeledType;
import jif.types.PathMap;
import jif.types.label.ArgLabel;
import jif.types.label.Label;
import jif.visit.LabelChecker;
import jlift.JLiftOptions;
import jlift.SummaryConstraintInfo;
import jlift.types.CallConstraint;
import jlift.types.JLiftLabelConstraint;
import jlift.types.JLiftTypeSystem;
import jlift.types.label.JLiftVarLabel;
import jlift.types.label.SummaryVarLabel;
import jlift.visit.SummaryConstraintGenerator;
import polyglot.ast.Expr;
import polyglot.ast.Local;
import polyglot.ast.Receiver;
import polyglot.main.Report;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.LocalInstance;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.ErrorInfo;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StdErrorQueue;

public class SummaryCallHelper extends JLiftCallHelper
{
	protected CallConstraint callConstraint;
	private boolean needConstraints;
	private Label newReturnValueLabel;
	private Label newReturnLabel;

	public SummaryCallHelper(Label receiverLabel, Receiver receiver,
			ReferenceType calleeContainer, JifProcedureInstance pi,
			List actualArgs, Position position) {
		super(receiverLabel, receiver, calleeContainer, pi, actualArgs,
				position);
		this.needConstraints = false;
	}

	public SummaryCallHelper(Label newLabel, ClassType ct,
			JifProcedureInstance instance, List list, Position position) {
		super(newLabel, ct, instance, list, position);
	}

	@Override
	public void checkOverride(LabelChecker lc) throws SemanticException {
		// we don't check method override when checking w/summary constraints
		return;
	}

	@Override
	public void checkCall(LabelChecker lc, List throwTypes,
			boolean targetMayBeNull) throws SemanticException {
		JLiftTypeSystem ts = (JLiftTypeSystem) pi.typeSystem();

		LinkedHashSet<ProcedureInstance> procedureInstancesToCheck = new LinkedHashSet<ProcedureInstance>();
		procedureInstancesToCheck.addAll(ts.getProceduresInSubtypes(pi));

		if (Report.should_report("summary", 3))
			Report.report(3, "checking call to " + pi + " from position "
					+ this.position + " (" + procedureInstancesToCheck.size()
					+ " procedure instances to check)");

		if (!procedureInstancesToCheck.contains(pi))
			procedureInstancesToCheck.add(pi);

		if (Report.should_report("summary", 4))
			Report.report(4, "call to " + pi + " requires checking "
					+ procedureInstancesToCheck.size()
					+ " possible subtypes/implementors");

		List throwTypesCopy = null;
		List finalThrowTypes = null;

		// need to set the return value label to the join of all the
		// return value labels from the procedures that we've checked
		Label finalReturnValueLabel = ts.bottomLabel();
		// ditto w/ return label
		Label finalReturnLabel = ts.bottomLabel();

		for (ProcedureInstance proc : procedureInstancesToCheck) {
			throwTypesCopy = new LinkedList(throwTypes);

			JifProcedureInstance jifProc = ((JifProcedureInstance) proc);
			ReferenceType procContainer = jifProc.container();

			SummaryCallHelper procCaller;
			if (jifProc.equals(pi))
				procCaller = this;
			else
				procCaller = new SummaryCallHelper(receiverLabel,
						this.receiverExpr, procContainer,
						(JifProcedureInstance) proc, actualArgs, this.position);

			procCaller.checkIndividualCall(lc, throwTypesCopy, targetMayBeNull);
			checkThrowTypes(throwTypes, throwTypesCopy);

			if (jifProc == pi)
				finalThrowTypes = throwTypesCopy;

			Label returnLabel = procCaller.resolveReturnLabel(lc);
			Label returnValueLabel = procCaller.resolveReturnValueLabel(lc,
					returnLabel);


			finalReturnLabel = ts.join(finalReturnLabel, returnLabel);
			finalReturnValueLabel = ts.join(finalReturnValueLabel, returnValueLabel);

//			System.err.println("\tnow return label for " + pi + " after " + proc + ": " + finalReturnLabel);
//			System.err.println("\tnow return value label for " + pi + " after " + proc + ": " + finalReturnValueLabel);
		}

		this.newReturnLabel = finalReturnLabel;
		this.newReturnValueLabel = finalReturnValueLabel;

//		System.err.println("final return label for " + pi + ": " + finalReturnLabel);
//		System.err.println("final return value label for " + pi + ": " + finalReturnValueLabel);


		throwTypes.clear();
		if (finalThrowTypes != null) {
			throwTypes.addAll(finalThrowTypes);
		}

		if (Report.should_report("summary", 3))
			Report.report(3, "finished checking call to " + pi
					+ " at position " + this.position);
	}

	private void checkThrowTypes(List throwTypes, List throwTypesCopy) {
		// kinder than Jif_c.checkThrowTypes
		//			Jif_c.checkThrowTypes(throwTypesCopy);
		for (Iterator iter = throwTypesCopy.iterator(); iter.hasNext();) {
			Type thrw = (Type)iter.next();
			if (thrw.typeSystem().isUncheckedException(thrw)) {
				iter.remove();
			}
		}
		if (!throwTypesCopy.isEmpty()) {
			//System.err.println("The types " + throwTypes + " are " + "declared to be thrown, but " + "are not label checked!");
			StdErrorQueue eq = new StdErrorQueue(System.err, 1000, "throw types warning");
			eq.enqueue(new ErrorInfo(ErrorInfo.WARNING, "The types " + throwTypes + " are " + "declared to be thrown, but " + "are not label checked!", this.position));
		}
	}

	private void checkIndividualCall(LabelChecker lc, List throwTypes,
			boolean targetMayBeNull) throws SemanticException {
		SummaryConstraintGenerator slc = (SummaryConstraintGenerator) lc;

		// need to add caller_pc and individual arg labels/types later
		this.callConstraint = new CallConstraint(pi, null, null, null, null,
				receiverExpr, slc.getCurrentNode());

		super.checkCall(slc, throwTypes, targetMayBeNull);

		// now do all the final stuff -- in particular, get the summary
		// constraints from the call constraint
		doFinalCallChecking(slc);
	}

	@Override
	protected void constrainEachArg(LabelChecker lc) throws SemanticException {
		// actualArgs contains the actual arguments (expressions) of which
		// their labels should be substituted for the summary variables

		if (Report.should_report("summary", 6))
			Report.report(6, "constraining args for " + this.pi);

		SummaryConstraintGenerator slc = (SummaryConstraintGenerator) lc;
		SummaryConstraintInfo summaryInfo = slc.getSummaryInfo();

		if (!slc.getCallGraph().hasProcedure(pi)) {
			if (Report.should_report("summary", 4))
				Report.report(4, "procedure " + pi + " is not in the call graph, " +
				"so we are not getting its summary constraints");

			// no way for Jif to say "returns an array with any label you want", so
			// this is the next best thing
			if (JLiftOptions.getInstance().looseArraysInSignatures) {
				replaceArrayLabelsWithVariablesInProcedure(pi);
				if (Report.should_report("summary", 4))
					Report.report(4, "checking call to " + pi
							+ " (after introducing variables for array bases)");
			}

			super.constrainEachArg(lc);
			return;
		}

		constrainEachArgForCallConstraint(lc);

		JifProcedureInstance currentProcedure = getCurrentProcedure(lc);

		// pi.equals(currentProcedure) if it contains a call to its
		// implementation in the superclass

		if (slc.getCallGraph().inSameConnectedComponent(pi, currentProcedure))
		{
			if (Report.should_report("summary", 4))
				Report.report(4, "procedure " + pi + " (" + pi.container()
						+ ") is in the same connected component as "
						+ currentProcedure + "(" + currentProcedure.container()
						+ ")");

			// this.callConstraint = new CallConstraint(pi, null, null,
			// receiverExpr, slc.getCurrentNode());
			return;
		}

		if (isPCBoundUnlabeled(pi))
			needConstraints = true;

		List<Label> varList = summaryInfo.getLabelsFromFormalTypes(pi);
		int index = 0; // for doing what Jif originally does, if we really have
		// to
		Iterator<Expr> itActual = actualArgs.iterator();
		Iterator<Type> itFormalTypes = pi.formalTypes().iterator();

		JifContext A = lc.context();
		JifTypeSystem jts = (JifTypeSystem) A.typeSystem();

		for (Label l : varList) {
			Expr Ej = itActual.next();

			if (l instanceof SummaryVarLabel) {
				needConstraints = true;
			} else {
				// do whatever Jif was going to do
				constrainArg(lc, index, Ej, (Type) pi.formalTypes().get(index));
			}

			index++;
		}

		// add constraints from checking that the constraints are

		if (Report.should_report("summary", 6))
			Report.report(6, "finished constraining args for call to " + pi
					+ " inside " + lc.context().currentCode());
	}

	private void replaceArrayLabelsWithVariablesInProcedure(
			JifProcedureInstance pi) {
		JifProcedureInstance newPi = (JifProcedureInstance) pi.copy();
		if (newPi instanceof JifMethodInstance) {
			JifMethodInstance jmi = ((JifMethodInstance) pi);
			Type replacedArrayType = replaceArrayLabelsWithVariablesInType(jmi.returnType(), position);
			newPi = (JifProcedureInstance) jmi.returnType(replacedArrayType);
		}

		List<Type> newFormalTypes = new LinkedList<Type>();

		Iterator<Expr> itActual = actualArgs.iterator();
		for (Type t : (List<Type>) newPi.formalTypes()) {
			newFormalTypes.add(replaceArrayLabelsWithVariablesInType(t,
					itActual.next().position()));
		}

		newPi.setFormalTypes(newFormalTypes);
		this.pi = newPi;
		this.callConstraint.setProcedureInstance(newPi);
	}

	private static int tempArrayIndex = 0;

	private Type replaceArrayLabelsWithVariablesInType(Type type,
			Position argPosition) {
		Type t = type;
		JLiftTypeSystem jts = (JLiftTypeSystem) t.typeSystem();

		if (t instanceof LabeledType) {
			LabeledType lt = (LabeledType) t;
			if (lt.typePart() instanceof ArrayType) {
				ArrayType at = (ArrayType) lt.typePart();
				if (at.base() instanceof LabeledType) {
					// it will be
					LabeledType base = (LabeledType) at.base();
					tempArrayIndex++;
					JLiftVarLabel arrayBaseLabel = jts.freshLabelVariable(argPosition, "temp_array_base_" + tempArrayIndex, "temp array");
					return lt.typePart(at.base(base.labelPart(arrayBaseLabel)));
				} else {
					throw new InternalCompilerError("array base not labeled");
				}
			}
		}

		return t;
	}

	// TODO: make this work with arrays and binding array_base -- we'll need to
	// store the actual array argument (DHK: IS THIS STILL A PROBLEM?)
	protected void constrainEachArgForCallConstraint(LabelChecker lc) {
		Iterator<Expr> itActual = actualArgs.iterator();
		Iterator<Type> itFormals = pi.formalTypes().iterator();

		JifContext A = lc.context();
		JifTypeSystem jts = (JifTypeSystem) A.typeSystem();

		List<Label> actualLabels = new LinkedList<Label>();
		List<Type> actualTypes = new LinkedList<Type>();
		List<Type> actualFormals = new LinkedList<Type>();

		while (itActual.hasNext()) {
			Expr Ej = itActual.next();
			Type formalj = itFormals.next();

			// A |- Xj[nv] <= argLj
			PathMap Xj = Jif_c.getPathMap(Ej);
			Label actualj = Xj.NV();
			Type typej = Ej.type();

			actualLabels.add(actualj);
			actualTypes.add(typej);
			try {
				actualFormals.add(instantiate(A, formalj));
			} catch (SemanticException e) {
				throw new InternalCompilerError(e);
			}
		}

		this.callConstraint.setActualArgLabels(actualLabels);
		this.callConstraint.setActualArgTypes(actualTypes);
		this.callConstraint.setActualFormalTypes(actualFormals);
	}

	private JifProcedureInstance getCurrentProcedure(LabelChecker lc) {
		JifContext A = lc.context();
		if (A.currentCode() instanceof JifProcedureInstance)
			return (JifProcedureInstance) A.currentCode();
		else
			// jif doesn't support initializer blocks, so this should never
			// happen
			throw new InternalCompilerError("current code context in a call is not a procedure instance");
	}

	@Override
	protected void constrainPCBound(LabelChecker lc, Label caller_pc)
	throws SemanticException {
		SummaryConstraintGenerator slc = (SummaryConstraintGenerator) lc;

		if (!JLiftOptions.getInstance().shouldIgnoreImplicitFlows()) {

			if (Report.should_report("summary", 6))
				Report.report(6, "constraining PC bound for " + pi);

			if (!slc.getCallGraph().hasProcedure(pi)) {
				//System.out.println("constraining PC bound " + caller_pc);
				super.constrainPCBound(lc, caller_pc);
				return;
			}

			this.callConstraint.setCallerPc(caller_pc);

			if (!isPCBoundUnlabeled(pi)) {
				//System.out.println("constraining PC bound " + caller_pc);
				super.constrainPCBound(slc, caller_pc);
			}
		}
	}

	private void doFinalCallChecking(SummaryConstraintGenerator slc)
	throws SemanticException {
		SummaryConstraintInfo summaryInfo = slc.getSummaryInfo();
		JLiftTypeSystem jts = (JLiftTypeSystem) slc.typeSystem();

		if (needConstraints) {

			//System.out.println("we need to include summary constraints from the called function");

			Collection<JLiftLabelConstraint> summaryConstraints = new LinkedHashSet<JLiftLabelConstraint>();
			Collection<CallConstraint> callConstraints = new LinkedHashSet<CallConstraint>();
			this.callConstraint.getConstraintsFromCall(summaryInfo, slc.context(), summaryConstraints, callConstraints);
			for (JLiftLabelConstraint c : summaryConstraints) {
				slc.constrain(c);
			}
			for (CallConstraint cc : callConstraints) {
				slc.addCallConstraint(cc);
			}
		}
	}

	private boolean isPCBoundUnlabeled(JifProcedureInstance pi) {
		return pi.pcBound() instanceof SummaryVarLabel;
	}

	public static CallHelper DummyOverrideHelper(JifMethodInstance overridden,
			JifMethodInstance overriding, LabelChecker lc) {

		JifTypeSystem jts = (JifTypeSystem) overridden.typeSystem();
		JifNodeFactory nf = (JifNodeFactory) lc.nodeFactory();

		JifClassType subContainer = (JifClassType) overriding.container();
		Label receiverLabel = subContainer.thisLabel();
		Receiver receiver = nf.This(overriding.position());
		ReferenceType calleeContainer = overridden.container().toReference();

		List actualArgs = new ArrayList(overriding.formalTypes().size());

		for (Iterator iter = overriding.formalTypes().iterator(); iter.hasNext();) {
			Type t = (Type) iter.next();
			if (jts.isLabeled(t)) {
				ArgLabel al = (ArgLabel) jts.labelOfType(t);
				LocalInstance formalInst = (LocalInstance) al.formalInstance();
				Local l = nf.Local(formalInst.position(),
						nf.Id(al.position(), al.name())).localInstance(
								formalInst);
				actualArgs.add(l);
			} else {
				throw new InternalCompilerError("Formal type is not labeled!");
			}
		}

		return new DummyOverrideHelper(receiverLabel, receiver,
				calleeContainer, overridden, actualArgs, overriding.position());
	}

	@Override
	public Label instantiate(JifContext A, Label L) throws SemanticException {
		if (this.callConstraint != null) {
			return callConstraint.substLabel(super.instantiate(A, L));
		} else {
			return super.instantiate(A, L);
		}
	}

	@Override
	protected Label resolveReturnLabel(LabelChecker lc)
			throws SemanticException {
		if (this.newReturnLabel != null)
			return instantiate(lc.context(), this.newReturnLabel);
		else
			return super.resolveReturnLabel(lc);
	}

	@Override
	protected Label resolveReturnValueLabel(LabelChecker lc, Label returnLabel)
			throws SemanticException {
		if (this.newReturnValueLabel != null)
			return instantiate(lc.context(), this.newReturnValueLabel);
		else
			return super.resolveReturnValueLabel(lc, returnLabel);
	}
}
