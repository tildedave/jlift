/*
 * File          : SummaryConstraintInfo.java
 * Project       : jlift
 * Description   : General utility class for holding/performing operations on
 *                 label constraints associated with procedures
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:25:55 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import jif.extension.SubtypeChecker;
import jif.types.JifContext;
import jif.types.JifMethodInstance;
import jif.types.JifProcedureInstance;
import jif.types.JifSubstType;
import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.LabelConstraintMessage;
import jif.types.LabelSubstitution;
import jif.types.LabeledType;
import jif.types.NamedLabel;
import jif.types.LabelConstraint.Kind;
import jif.types.hierarchy.LabelEnv_c;
import jif.types.label.ArgLabel;
import jif.types.label.Label;
import jif.types.label.ParamLabel;
import jif.types.label.ThisLabel;
import jif.visit.LabelChecker;
import jlift.extension.JLiftSubtypeChecker;
import jlift.types.CallConstraint;
import jlift.types.JLiftLabelCheckingUtil;
import jlift.types.JLiftLabelConstraint;
import jlift.types.JLiftTypeSystem;
import jlift.types.label.JLiftDynamicLabel_c;
import jlift.types.label.JLiftInstLabel_c;
import jlift.types.label.JLiftVarLabel;
import jlift.types.label.JLiftVarLabel_c;
import jlift.types.label.SummaryVarLabel;
import jlift.util.MultiMap;
import jlift.visit.MockLabelChecker;
import jlift.visit.SummaryConstraintGenerator;
import polyglot.main.Report;
import polyglot.types.ArrayType;
import polyglot.types.ConstructorInstance;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

//container to hold information about summary functions

public class SummaryConstraintInfo {

	protected Map<ProcedureInstance, Collection<JLiftLabelConstraint>> procedureToNonCallConstraintMap;
	protected Map<ProcedureInstance, Collection<CallConstraint>> procedureToCallConstraintMap;
	protected Map<ProcedureInstance, Collection<JLiftLabelConstraint>> closedProcedureToConstraintMap;
	private final Collection<ProcedureInstance> deletedProcedures;
	public JLiftTypeSystem jts;
	private final Map<ProcedureInstance, MultiMap<Type, Label>> exceptionVariables = new HashMap<ProcedureInstance, MultiMap<Type,Label>>();

	public SummaryConstraintInfo(TypeSystem ts) {
		super();
		this.jts = (JLiftTypeSystem) ts;
		this.procedureToNonCallConstraintMap = new HashMap<ProcedureInstance, Collection<JLiftLabelConstraint>>();
		this.procedureToCallConstraintMap = new HashMap<ProcedureInstance, Collection<CallConstraint>>();
		this.closedProcedureToConstraintMap = new HashMap<ProcedureInstance, Collection<JLiftLabelConstraint>>();
		this.deletedProcedures = new LinkedList<ProcedureInstance>();
	}

	public void setConstraints(ProcedureInstance jpi, Collection<JLiftLabelConstraint> nonCallConstraints, Collection<CallConstraint> callConstraints)
	{
		synchronized(procedureToNonCallConstraintMap) {
			procedureToNonCallConstraintMap.put(jpi, nonCallConstraints);
		}
		synchronized (procedureToCallConstraintMap) {
			procedureToCallConstraintMap.put(jpi, callConstraints);
		}
	}

	public Collection<JLiftLabelConstraint> getSummaryConstraints(ProcedureInstance pi) throws SemanticException {
		Collection<JLiftLabelConstraint> closedConstraintsFromCall = closedProcedureToConstraintMap.get(pi);
		if (closedConstraintsFromCall != null) {
			return closedConstraintsFromCall;
		}
		else {
			ProcedureInstance paramPi = getProcedureInstanceForParameterizedProcedure(pi);
			if (paramPi != null) {
				return closedProcedureToConstraintMap.get(paramPi);
			}

			if (haveDeletedProcedure(pi))
				throw new InternalCompilerError("already deleted summary constraints for " + pi);
			else {
				throw new SemanticException("weirdness getting summary constraints from call of " + pi + " (" + pi.getClass() + ") -- could not find suitable procedure");
			}
		}
	}

	public Collection<CallConstraint> getCallConstraints(ProcedureInstance pi) {
		Collection<CallConstraint> returnCallConstraints = procedureToCallConstraintMap.get(pi);
		if (returnCallConstraints == null) {
			ProcedureInstance paramPi = getProcedureInstanceForParameterizedProcedure(pi);
			if (paramPi != null) {
				return procedureToCallConstraintMap.get(paramPi);
			}

			Report.report(1, "weirdness getting call constraints from call of " + pi + " (" + pi.getClass() + ") -- could not find suitable procedure");
			return new LinkedList<CallConstraint>();
		}

		return returnCallConstraints;
	}


	private ProcedureInstance getProcedureInstanceForParameterizedProcedure(
			ProcedureInstance pi) {
		if (pi instanceof MemberInstance) {
			// if it's null, then -- maybe pi is a parameterized instance (?)  dunno
			ReferenceType piContainer = ((MemberInstance) pi).container();
			if (piContainer instanceof JifSubstType) {
				JifSubstType jst = (JifSubstType) piContainer;

				// for each procedure, see if it's contained inside of the same class and has
				// the same name
				// TODO: this could be broken very badly for parameterized/overloaded methods!
				for(ProcedureInstance jpi : closedProcedureToConstraintMap.keySet()) {

					if (jpi.equals(pi)) {
						return jpi;
					}
				}
			}
		}

		return null;
	}

	private boolean haveDeletedProcedure(ProcedureInstance pi) {
		synchronized (deletedProcedures) {
			return deletedProcedures.contains(pi);
		}
	}

	public void closeCallConstraints(ProcedureInstance pi, Collection<ProcedureInstance> scc) throws SemanticException {
		// component represents a scc in the callgraph -- each of their
		// callConstraints refers to one another.  this method will put
		// the least fix point of their constraints into the closed constraint
		// map

		// the way this is coded is (probably?) fairly inefficient, but
		// should be straightforward to see that it is correct

		synchronized (closedProcedureToConstraintMap) {
			synchronized(procedureToCallConstraintMap) {
				synchronized (closedProcedureToConstraintMap) {

					if (Report.should_report("summary", 7))
						Report.report(7, "closing call constraints for procedure " + pi + " in scc " + scc);

					Collection<JLiftLabelConstraint> closedConstraints = new LinkedList<JLiftLabelConstraint>();

					Queue<ProcedureInstance> procedureQueue = new LinkedList<ProcedureInstance>();
					Queue<Collection<JLiftLabelConstraint>> JLiftLabelConstraintQueue = new LinkedList<Collection<JLiftLabelConstraint>>();
					Queue<Collection<CallConstraint>> callConstraintQueue = new LinkedList<Collection<CallConstraint>>();

					Collection<CallConstraint> consideredCallConstraints = new LinkedList<CallConstraint>();

					procedureQueue.add(pi);
					JLiftLabelConstraintQueue.add(procedureToNonCallConstraintMap.get(pi));
					callConstraintQueue.add(procedureToCallConstraintMap.get(pi));

					do {
						// do we not need current?  (keep it for debugging)
						ProcedureInstance current = procedureQueue.remove();

						if (Report.should_report("summary", 7))
							Report.report(7, "current procedure instance considered during closing: " + current);

						// definitely need these
						Collection<JLiftLabelConstraint> nonCallConstraints = JLiftLabelConstraintQueue.remove();
						closedConstraints.addAll(nonCallConstraints);

						Collection<CallConstraint> callConstraints = callConstraintQueue.remove();
						if (Report.should_report("summary", 7)) {
							Report.report(7, "call constraints size: " + callConstraints.size());
							Report.report(7, "call constraints: " + callConstraints);
						}

						for(CallConstraint call : callConstraints)
						{
//							System.err.println("current --> " + current + " calls: "+ call);

							if (Report.should_report("summary", 7))
								Report.report(7, "call: " + call);

							Collection<JLiftLabelConstraint> addConstraints = new LinkedList<JLiftLabelConstraint>();
							Collection<CallConstraint> addCallConstraints = new LinkedList<CallConstraint>();

							call.getConstraintsFromCall(this, null, addConstraints, addCallConstraints);

							if (!consideredCallConstraints.containsAll(addCallConstraints)) {

								consideredCallConstraints.addAll(addCallConstraints);

								if (Report.should_report("summary", 8)) {
									Report.report(8, "adding " + call.getProcedureInstance() + " to the queue");
									Report.report(8, "adding " + addConstraints + " to the queue");
									Report.report(8, "adding " + addCallConstraints + " to the queue");
									Report.report(8, "size of considered constraints: " + consideredCallConstraints.size());
								}
								procedureQueue.add(call.getProcedureInstance());
								JLiftLabelConstraintQueue.add(addConstraints);
								callConstraintQueue.add(addCallConstraints);
							}
						}
					} while (!procedureQueue.isEmpty());

					// if we get here, this should all work (yay)
					this.closedProcedureToConstraintMap.put(pi, closedConstraints);
				}
			}
		}
	}

	/**
	 * This method adds constraints based on the type arguments to a method --
	 * we have to do this in a roundabout way (using a MockLabelChecker) because
	 * of how SubtypeChecker returns its constraints (i.e. calling
	 * lc.constrain). Otherwise, we won't do the right substitutions on the
	 * constraints that the subtype checker adds. (summary constraints in array
	 * types, for example)
	 *
	 * @param constraintsFromCall
	 * @param callConstraint
	 * @param formalType
	 * @param jpi
	 * @param Ej
	 * @throws SemanticException
	 */
	public void addConstraintsFromSubtypeChecker(Collection<JLiftLabelConstraint> constraintsFromCall, CallConstraint callConstraint, Type actualType, Type formalType, JifProcedureInstance jpi)
	throws SemanticException {
		SubtypeChecker sc = new JLiftSubtypeChecker(jts.unlabel(formalType), jts.unlabel(actualType), jpi);

		LabelChecker saveGlobal = JLiftLabelCheckingUtil.global;
		MockLabelChecker mockLabelChecker = new MockLabelChecker(null, callConstraint.getProcedureInstance().typeSystem(), null, true, true);
		sc.addSubtypeConstraints(mockLabelChecker, callConstraint.getNode().position());
		JLiftLabelCheckingUtil.global = saveGlobal;

		// add constraints from the mock label checker
		if (!mockLabelChecker.getConstraints().isEmpty()) {
			if (Report.should_report("summary", 7))
				Report.report(7, "constraints from call to " + callConstraint.getProcedureInstance() + " ("
						+ formalType + "/" + actualType + " ):");

			SummaryConstraintInfo.reportConstraintSet(mockLabelChecker.getConstraints(), 7);
		}

		for (LabelConstraint c : mockLabelChecker.getConstraints())
			constraintsFromCall.add(SummaryConstraintInfo.convertLabelConstraint(c));
	}

	public boolean shouldRenameConstraintsFromCall(JifProcedureInstance jpi) {
		if (jpi.container() instanceof JifSubstType) {
			return true;
		}
		if (JLiftOptions.getInstance().shouldGenerateDeclassificationConstraints)
			return false;
		try {
		    if (getSummaryConstraints(jpi).size() < JLiftOptions.getInstance().ctxSize) {
			return true;
		    }
		} catch (SemanticException impossible) { }
		return JLiftOptions.getInstance().contextSensitiveMethods();
	}

	protected static Map<Position,Integer> callingPositions = new HashMap<Position,Integer>();

	public Collection<JLiftLabelConstraint> renameVariablesInCall(final JLiftTypeSystem ts,
			Collection<JLiftLabelConstraint> constraints, Position callingPosition) {

		final String uniqueIdentifier;
		if (callingPositions.containsKey(callingPosition)) {
			uniqueIdentifier = callingPositions.get(callingPosition).toString();
		}
		else {
			Integer currentIndex = new Integer(callingPositions.size() + 1);
			uniqueIdentifier = currentIndex.toString();
			callingPositions.put(callingPosition, currentIndex);
		}

		if (constraints == null)
			return null;

		LabelSubstitution renameSubst = new LabelSubstitution() {
			@Override
			public Label substLabel(Label L) throws SemanticException {
				if (L instanceof JLiftVarLabel && !(L instanceof SummaryVarLabel)) {
					JLiftVarLabel vl = (JLiftVarLabel) L;
					// don't rename temporary variables for fields
					if (shouldRenameVariable(vl)) {
						// TODO: are there other variables that should not be renamed?  arrays on
						// fields... ugh
						String newName = vl.name() + "_call" + uniqueIdentifier;
						JLiftVarLabel returnVariable = ts.freshLabelVariable(L.position(), newName, vl.description());
						return returnVariable;
					}
				}
				return L;
			}

			private boolean shouldRenameVariable(JLiftVarLabel vl) {
				return !vl.name().startsWith("inst(") && !vl.name().startsWith("L_var@");
//				return vl.name().startsWith("pc") || vl.name().matches("N[0-9]+.*");// || vl.name().startsWith("caller_pc@");
			}
		};

		try {
			Collection<JLiftLabelConstraint> returnConstraints = new LinkedHashSet<JLiftLabelConstraint>();
			for(JLiftLabelConstraint c : constraints) {
				returnConstraints.add(c.subst(renameSubst));
			}

			reportConstraintSet((Collection) returnConstraints, 10);

			return returnConstraints;
		} catch (SemanticException e) {
			throw new InternalCompilerError(e);
		}
	}

	public List<Label> getLabelsFromFormalTypes(ProcedureInstance jpi) {
		List<Label> returnList = new LinkedList<Label>();

		for(Type t : (List<Type>) jpi.formalTypes())
		{
			if (t instanceof LabeledType)
			{
				LabeledType lt = (LabeledType) t;

				if (lt.labelPart() instanceof ArgLabel) {
					ArgLabel al = (ArgLabel) lt.labelPart();

					returnList.add(al.upperBound());
				}
				else {
					throw new InternalCompilerError("type " + t + " is not labeled with an arglabel");
				}
			}
			else
				throw new InternalCompilerError("type " + t + " is not labeled");
		}

		return returnList;
	}

	/* Utility Functions for Label Constraint/Call Constraint substitution */

	public static Collection<CallConstraint> substituteInCallConstraintSet(JLiftVarLabel v, Label l, Collection<CallConstraint> localCallConstraints) {
		if (Report.should_report("subs", 1)) {
			Report.report(1,"substituting " + v + " for " + l + " in call constraint set");
		}
		Collection<CallConstraint> returnCallConstraints = new LinkedList<CallConstraint>();
		for(CallConstraint c : localCallConstraints)
		{
			returnCallConstraints.add(substituteInCallConstraint(v, l, c));
		}
		return returnCallConstraints;
	}

	public static CallConstraint substituteInCallConstraint(JLiftVarLabel v, Label l, CallConstraint c) {
		List<Label> substActuals = new LinkedList<Label>();
		for(Label actual : c.getActualArgLabels())
		{
			Label substLabel = substituteInLabel(v, l, actual);
			substActuals.add(substLabel);
		}
		Label newCallerPc = substituteInLabel(v, l, c.caller_pc());

		return new CallConstraint(c.getProcedureInstance(), c.getActualArgTypes(), substActuals, c.getActualFormalTypes(), newCallerPc, c.getReceiver(), c.getNode());
	}

	public static Collection<JLiftLabelConstraint> substituteInConstraintSet(Label target, Label actualj, Collection<JLiftLabelConstraint> constraintSet) {
		//Collection<JLiftLabelConstraint> returnConstraints = new LinkedList<JLiftLabelConstraint>();
		if (Report.should_report("subs", 2)) {
			Report.report(1,"substituting " + actualj + " for " + target + " in constraint set with size " + constraintSet.size());
		}

		Collection<JLiftLabelConstraint> returnSet = new LinkedHashSet<JLiftLabelConstraint>();

		for(JLiftLabelConstraint c : constraintSet)
		{
			String oldConstraintString = null;
			if (Report.should_report("subs", 3))
				oldConstraintString = c.shortString();

			JLiftLabelConstraint substitutedConstraint = substituteInJLiftLabelConstraint(target,actualj,c);
			returnSet.add(substitutedConstraint);

			String currentConstraintString = null;
			if (Report.should_report("subs", 3))
				substitutedConstraint.shortString();

			// no danger of NPE as if the first condition is satisfied, oldConstraintString
			// will have been initialized
			if (Report.should_report("subs", 3) && !oldConstraintString.equals(currentConstraintString)) {
				Report.report(3, "SUBSTITUTION: " + actualj + " FOR " + target + ":");
				Report.report(3, "\t" + oldConstraintString);
				Report.report(3, "\t" + currentConstraintString);
			}
		}

		return returnSet;
	}

	public static JLiftLabelConstraint substituteInJLiftLabelConstraint(final Label target, final Label actual, JLiftLabelConstraint c) {
		LabelSubstitution s = new LabelSubstitution() {
			@Override
			public Label substLabel(Label l) throws SemanticException {
				if (l.equals(target)) {
					return actual;
				}
				else {
					if (l instanceof ArgLabel) {
						ArgLabel al = (ArgLabel) l;
						if (al.upperBound().equals(target))
							al.setUpperBound(actual);
						return al;
					}
					return l;
				}
			}
		};
		try {
			return c.subst(s);
		} catch (SemanticException e) {
			throw new InternalCompilerError("Unexpected SemanticException when substituting in labelconstraint", e);
		}
	}

	public static Label substituteInLabel(final Label target, final Label actual, Label c) {
		LabelSubstitution s = new LabelSubstitution() {
			@Override
			public Label substLabel(Label l) throws SemanticException {
				if (l.equals(target)) {
					return actual;
				}
				else {
					return l;
				}
			}
		};
		try {
			return c.subst(s).simplify();
		}
		catch (SemanticException e) {
			throw new InternalCompilerError("Unexpected SemanticException", e);
		}
	}

	// used externally for debugging -- assumes jpi's summary constraints have been
	// closed
	public void reportSummaryConstraintSet(ProcedureInstance jpi, int level) {
		if(Report.should_report("summary", level))
		{
			Report.report(level, "summary constraints for " + jpi);
			try {
				Collection<JLiftLabelConstraint> constraints = getSummaryConstraints(jpi);
				for(JLiftLabelConstraint c : constraints)
				{
					Report.report(level, "-->" + c.shortString());
				}
			}
			catch (SemanticException e) {
				throw new InternalCompilerError(e);
			}

		}
	}

	// used externally for debugging
	public void reportUnclosedSummaryConstraintSet(ProcedureInstance jpi, int level) {
		if(Report.should_report("summary", level)) {
			Report.report(level, "summary constraints for " + jpi);
			Collection<JLiftLabelConstraint> constraints = this.procedureToNonCallConstraintMap.get(jpi);
			Collection<CallConstraint> callConstraints = this.procedureToCallConstraintMap.get(jpi);

			for(JLiftLabelConstraint c : constraints) {
				Report.report(level, "-->" + c);
			}
			for(CallConstraint c : callConstraints) {
				Report.report(level, "-->" + c);
			}
		}
	}

	// used externally for debugging
	public static void reportConstraintSet(Collection<LabelConstraint> collection, int level) {
		if(Report.should_report("summary", level) || level == 0) {
			Report.report(level, "summary constraints for constraint set");

			for(LabelConstraint c : collection) {
				Report.report(level, "-->" + c.toString());
			}
		}
	}

	public static Collection<JLiftLabelConstraint> unwrapArgLabelsInJLiftLabelConstraintSet(Collection<JLiftLabelConstraint> tempLocalConstraints, SummaryConstraintGenerator lc) {
		Collection<JLiftLabelConstraint> returnConstraints = new LinkedList<JLiftLabelConstraint>();
		for(JLiftLabelConstraint c : tempLocalConstraints)
		{
			JLiftLabelConstraint newConstraint = unwrapArgLabelsInLabelConstraint(c, lc);
			returnConstraints.add(newConstraint);
		}
		return returnConstraints;
	}

	public static JLiftLabelConstraint unwrapArgLabelsInLabelConstraint(JLiftLabelConstraint c, final SummaryConstraintGenerator lc) {
		LabelSubstitution ls = getUnwrappingSubstitution(lc);
		LabelSubstitution ds;
		if (JLiftOptions.getInstance().dynamicLabelAnalysis) {
			ds = getReplaceDynamicLabelSubstitution(lc);
		}
		else {
			ds = new LabelSubstitution() {
				@Override
				public Label substLabel(Label L) throws SemanticException {
					return super.substLabel(L);
				}
			};
		}
		try {
			JLiftLabelConstraint jlc = c.subst(ls).subst(ds);
			if (JLiftOptions.getInstance().dynamicLabelAnalysis) {
				for(LabelSubstitution dynSub : lc.getLabelSubstitutionChain()) {
					jlc = jlc.subst(dynSub);
				}
			}
			return jlc;
		} catch (SemanticException e) {
			throw new InternalCompilerError("unexpected SemanticException", e);
		}
	}

	private static LabelSubstitution getReplaceDynamicLabelSubstitution(
			SummaryConstraintGenerator lc) {
		return new LabelSubstitution() {
			@Override
			public Label substLabel(Label L) throws SemanticException {
				if (L instanceof JLiftDynamicLabel_c && !(L instanceof JLiftInstLabel_c)) {
					Label returnLabel = new JLiftInstLabel_c((JLiftDynamicLabel_c) L);
					return returnLabel;
				}
				return L;
			}
		};
	}

	private static LabelSubstitution getUnwrappingSubstitution(
			final SummaryConstraintGenerator lc) {
		return new LabelSubstitution() {
			@Override
			public Label substLabel(Label L) throws SemanticException {
				if (L instanceof ArgLabel)
				{
					return ((ArgLabel) L).upperBound();
				}
				return L;
			}
		};
	}

	public static boolean constraintSetHasSummaryVariables(Collection<JLiftLabelConstraint> procConstraints)
	{
		boolean returnValue;

		for(JLiftLabelConstraint c : procConstraints)
		{
			if (SummaryConstraintInfo.labelHasSummaryVariables(c.lhs()))
				returnValue = true;
			if (SummaryConstraintInfo.labelHasSummaryVariables(c.rhs()))
				returnValue = true;
		}

		return false;
	}

	public static boolean labelHasSummaryVariables(Label c)
	{
		Collection<SummaryVarLabel> variableLabels = getSummaryVariablesFromLabel(c);
		if (!variableLabels.isEmpty()) {
			if (Report.should_report("summary",4))
				Report.report(4, "** label " + c + " contains summary variables " + variableLabels);
		}
		return !variableLabels.isEmpty();
	}

	public void dumpClosedConstraints(int level)
	{
		for(ProcedureInstance jpi : this.closedProcedureToConstraintMap.keySet())
		{
			reportSummaryConstraintSet(jpi, level);
		}
	}

	public static Type replaceVariablesWithSummaryVariables(Type lblType)
	{
		// I think this is only useful for array_base variables -- called from disambiguating
		// formals.

		LabelSubstitution ls = new LabelSubstitution() {
			@Override
			public Label substLabel(Label L) throws SemanticException {
				if ((L instanceof JLiftVarLabel) && !(L instanceof SummaryVarLabel))
				{
					JLiftVarLabel vl = (JLiftVarLabel) L;
					return new SummaryVarLabel("sum(" + vl.name() + ")", vl.description(), (JifTypeSystem) vl.typeSystem(), vl.position());
				}
				return L;
			}
		};

		try {
			JifTypeSystem jts = (JifTypeSystem) lblType.typeSystem();
			if (jts.unlabel(lblType) instanceof ArrayType)
			{
				ArrayType at = (ArrayType) jts.unlabel(lblType);
				if (jts.isLabeled(at.base()))
				{
					return at.base(jts.labeledType(at.base().position(), jts.unlabel(at.base()), jts.labelOfType(at.base()).subst(ls)));
				}
			}

			return lblType;
		}
		catch (SemanticException e) {
			throw new InternalCompilerError("Unexpected Semantic Exception: " + e);
		}
	}

	public static Collection<JLiftLabelConstraint> substituteAwaySummaryVariableInConstraintSet(JifContext jifContext, SummaryVarLabel svj, Label actualj, Collection<JLiftLabelConstraint> constraintSet, String procedureName, Position pos) {

		if (Report.should_report("subs", 1))
			Report.report(1,"substituting " + actualj + " for " + svj + " in constraint set");

		if (Report.should_report("subs", 2)) {
			Report.report(2,"old constraint set: ");
			for(JLiftLabelConstraint c : constraintSet) {
				Report.report(2, "*--->" + c.toString());
			}
		}

		int firstParen = svj.name().indexOf("(");
		int secondParen = svj.name().indexOf(")");
		String name = svj.name().substring(firstParen + 1, secondParen);

		String variableName = name;
		variableName += "@callto:" + procedureName;
//		System.err.println("call to " + procedureName + " with position " + pos);

		JLiftVarLabel v = new JLiftVarLabel_c(variableName, svj.description(), (JifTypeSystem) svj.typeSystem(), pos);

		// TODO: sometimes this needs to be a definitional constraints.  sometimes it does not.  why?
		Kind constraintType;
//		if (svj.name().contains("caller_pc") && ((JLiftContext_c) jifContext).insideDeclassify())
//			constraintType = JLiftLabelConstraint.DEFINE;
//		else
//			constraintType = JLiftLabelConstraint.EQUAL;

		constraintType = JLiftLabelConstraint.DEFINE;

		JLiftLabelConstraint equalityConstraint = new JLiftLabelConstraint(new NamedLabel("",v),
				constraintType,
				new NamedLabel("", actualj),
				new LabelEnv_c((JifTypeSystem) v.typeSystem(), false),
				pos);

		if (Report.should_report("subs", 1))
			Report.report(1,"introducing new constraint " + equalityConstraint);

		Collection<JLiftLabelConstraint> returnSet = substituteInConstraintSet(svj, v, constraintSet);
		returnSet.add(equalityConstraint);

		if (Report.should_report("subs", 2)) {
			Report.report(2,"new constraint set: ");
			System.err.println(actualj + " (" +actualj.getClass() + ")");
			if (actualj instanceof ArgLabel) {
				System.err.println(((ArgLabel) actualj).upperBound());
			}
			for(JLiftLabelConstraint c : returnSet) {
				Report.report(2, "*--->" + c.toString());
			}
		}

		return returnSet;
	}

	public static Collection<JLiftLabelConstraint> substituteAwayThisLabelInConstraintSet(String receiverName, Position receiverPosition, Label receiverLabel, Collection<JLiftLabelConstraint> constraintSet, TypeSystem ts) {
		JLiftTypeSystem jts = (JLiftTypeSystem) ts;
		JLiftVarLabel thisVariable = jts.freshLabelVariable(receiverPosition, receiverName, "variable for this");

		Collection<JLiftLabelConstraint> returnSet = new LinkedHashSet<JLiftLabelConstraint>();

		for(JLiftLabelConstraint c : constraintSet) {
			returnSet.add(substituteAwayThisLabelInLabelConstraint(thisVariable, c));
		}

		if(receiverLabel != null) {
			JLiftLabelConstraint thisEqualsConstraint = new JLiftLabelConstraint(new NamedLabel("", thisVariable), JLiftLabelConstraint.EQUAL, new NamedLabel("", receiverLabel), new LabelEnv_c(jts, true), receiverLabel.position());
			constraintSet.add(thisEqualsConstraint);
		}

		return returnSet;
	}

	public static JLiftLabelConstraint substituteAwayThisLabelInLabelConstraint(JLiftVarLabel thisVariable, JLiftLabelConstraint c) {
		LabelSubstitution ls = getRemoveThisSubstitution(thisVariable);

		try {
			return c.subst(ls);
		}
		catch (SemanticException e) {
			throw new InternalCompilerError("unexpected SemanticException", e);
		}
	}

	private static LabelSubstitution getRemoveThisSubstitution(
			final JLiftVarLabel thisLabelVar) {
		return new LabelSubstitution() {
			@Override
			public Label substLabel(Label L) throws SemanticException {
				if (L instanceof ThisLabel) {
					return thisLabelVar;
				}
				return L;
			}
		};
	}

	public static String getNameForProcedureInstance(ProcedureInstance pi) {
		if (pi instanceof JifMethodInstance)
			return ((MethodInstance) pi).name();
		if (pi instanceof ConstructorInstance) {
			ReferenceType container = ((ConstructorInstance) pi).container();
			if (container instanceof JifSubstType) {
				container = (ReferenceType) ((JifSubstType) container).base();
			}
			return container.toString();
		}
		return null;
	}

	public Collection<JLiftLabelConstraint> getNonCallConstraints(ProcedureInstance proc) {
		return procedureToNonCallConstraintMap.get(proc);
	}

	public void deleteProcedureFromClosedMap(ProcedureInstance candidate) {
		synchronized(closedProcedureToConstraintMap) {
			synchronized (deletedProcedures) {
				closedProcedureToConstraintMap.remove(candidate);
				deletedProcedures.add(candidate);
				System.gc();
			}
		}
	}

	public boolean hasSummaryConstraintsForProcedure(ProcedureInstance pi) {
		return closedProcedureToConstraintMap.containsKey(pi);
	}

	public int size() {
		int size = 0;

		synchronized (closedProcedureToConstraintMap) {
			for (Entry<ProcedureInstance, Collection<JLiftLabelConstraint>> e : closedProcedureToConstraintMap.entrySet()) {
				size += e.getValue().size();
			}
		}

		return size;
	}

	public static boolean isTotallyLabeled(TypeSystem ts, ProcedureInstance pi) {
		JifProcedureInstance jpi = (JifProcedureInstance) pi;
		JifTypeSystem jts = (JifTypeSystem) ts;
		if (jpi.pcBound() instanceof SummaryVarLabel) {
			return false;
		}
		for(Type tp : ((Collection<Type>) jpi.formalTypes())) {
			if (jts.isLabeled(tp)) {
				Label l = jts.labelOfType(tp);
				if (l instanceof SummaryVarLabel)
					return false;
			}
		}

		return true;
	}

	public static JLiftLabelConstraint convertLabelConstraint(final LabelConstraint c) {
		if (c instanceof JLiftLabelConstraint)
			return (JLiftLabelConstraint) c;
		else {
			return new JLiftLabelConstraint(c.namedLhs(), c.kind(), c.namedRhs(), c.env(), c.position(), new LabelConstraintMessage() {
				@Override
				public String msg() {
					return c.msg();
				}

				@Override
				public String detailMsg() {
					return c.detailMsg();
				}

				@Override
				public String technicalMsg() {
					return c.technicalMsg();
				}
			}, c.report());
		}
	}

	public static Collection<SummaryVarLabel> getSummaryVariablesFromLabel(Label label) {
		final Collection<SummaryVarLabel> variableLabels = new LinkedList<SummaryVarLabel>();
		LabelSubstitution s = new LabelSubstitution() {
			@Override
			public Label substLabel(Label l) throws SemanticException {
				if (l instanceof SummaryVarLabel)
					variableLabels.add((SummaryVarLabel) l);
				if (l instanceof ArgLabel)
					substLabel(((ArgLabel) l).upperBound());
				return l;
			}
		};
		try {
			label.subst(s);
			return variableLabels;
		}
		catch (SemanticException e) {
			throw new InternalCompilerError("Unexcepted SemanticException " + e);
		}
	}

	public static Collection<JLiftLabelConstraint> applyLabelSubstitutionToConstraintSet(
			LabelSubstitution ls,
			Collection<JLiftLabelConstraint> summaryConstraints) {
		Collection<JLiftLabelConstraint> returnConstraints = new LinkedHashSet<JLiftLabelConstraint>();
		for(JLiftLabelConstraint jlc : summaryConstraints) {
			try {
				returnConstraints.add(jlc.subst(ls));
			} catch (SemanticException e) {
				throw new InternalCompilerError(e);
			}
		}

		return returnConstraints;
	}


	public static Collection<CallConstraint> applyLabelSubstitutionToCallConstraintSet(
			LabelSubstitution ls,
			Collection<CallConstraint> callConstraints) {
		Collection<CallConstraint> returnConstraints = new LinkedHashSet<CallConstraint>();
		for(CallConstraint cc : callConstraints) {
			try {
				returnConstraints.add(cc.subst(ls));
			} catch (SemanticException e) {
				throw new InternalCompilerError(e);
			}
		}

		return returnConstraints;
	}

	public static String canCheckConstraintSet(
			JifProcedureInstance pi, Collection<JLiftLabelConstraint> procConstraints) {
		if (!isTotallyLabeled(pi.typeSystem(), pi)) {
			return (pi + " is not totally labeled");
		}

		for(JLiftLabelConstraint c : procConstraints)
		{
			if (labelHasSummaryVariables(c.lhs()))
				return (c.lhs() + " contains summary variables");
			if (labelHasSummaryVariables(c.rhs()))
				return (c.rhs() + " contains summary variables");
			if (labelHasParamLabels(c.lhs()))
				return (c.lhs() + " contains parameter labels");
			if (labelHasParamLabels(c.rhs()))
				return (c.rhs() + " contains parameter labels");
		}

		return "";
	}

	public static boolean labelHasParamLabels(Label l) {
		final Collection<ParamLabel> paramLabels = new HashSet<ParamLabel>();
		LabelSubstitution hasParamLabels = new LabelSubstitution() {
			@Override
			public Label substLabel(Label L) throws SemanticException {
				if (L instanceof ParamLabel) {
					paramLabels.add((ParamLabel) L);
				}
				return L;
			}
		};

		try {
			l.subst(hasParamLabels);
		} catch (SemanticException e) {
			throw new InternalCompilerError(e);
		}
		return !paramLabels.isEmpty();
	}

	public void setExceptionVariables(ProcedureInstance pi,
			MultiMap<Type, Label> typeToExceptionMap) {
		this.exceptionVariables.put(pi, typeToExceptionMap);
	}

	public MultiMap<Type, Label> getExceptionLabelsForProcedure(ProcedureInstance pi) {
		return this.exceptionVariables.get(pi);
	}
}
