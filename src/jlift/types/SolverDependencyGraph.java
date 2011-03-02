/*
 * File          : SolverDependencyGraph.java
 * Project       : jlift
 * Description   : Solver Dependency Graph.  See NAS-TR-0069-2007.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:24:11 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.VarMap;
import jif.types.hierarchy.LabelEnv_c;
import jif.types.label.Label;
import jifclipse.types.ConstraintFailure;
import jifclipse.types.JifclipseBlameEngine;
import jlift.types.label.JLiftDynamicLabel_c;
import jlift.types.label.JLiftVarLabel;

public class SolverDependencyGraph implements JifclipseBlameEngine {

	// records the VarMap at each stage of the computation, and connections between
	// different levels

	// the type system
	protected JifTypeSystem jts;
	// list of changes made during a run of the solver
	protected List<SolverChange> changeList;
	// we don't use environments b/c of context sensitivity
	protected LabelEnv_c emptyEnvironment;
	protected int time;
	private final BlameOracle bo;

	public SolverDependencyGraph(JifTypeSystem ts) {
		this.jts = ts;
		this.changeList = new LinkedList<SolverChange>();
		this.emptyEnvironment = new LabelEnv_c(jts, false);
		this.bo = new BlameOracle(jts, this);
		time = 0;
	}

	public SolverDependencyGraph(SolverDependencyGraph depGraph) {
		this.jts = depGraph.jts;
		this.changeList = new LinkedList<SolverChange>(depGraph.changeList);
		this.emptyEnvironment = depGraph.emptyEnvironment;
		this.time = depGraph.time;
		this.bo = depGraph.bo;
	}

	class SubtypeConstraint {

		private final Label lhs;
		private final Label rhs;
		private final LabelConstraint c;

		public SubtypeConstraint(Label lhs, Label rhs, LabelConstraint becauseConstraint) {
			this.lhs = lhs;
			this.rhs = rhs;
			this.c = becauseConstraint;
		}

		/**
		 * @return the lhs
		 */
		public Label getLhs() {
			return lhs;
		}

		/**
		 * @return the rhs
		 */
		public Label getRhs() {
			return rhs;
		}

		@Override
		public String toString() {
			return getLhs().toString() + " <= " + getRhs().toString() + "@" + c.position();
		}
	}

	class SolverChange {

		/** time */
		protected int time;
		/** the variable that was changed */
		protected JLiftVarLabel v;
		/** the label it was changed to */
		protected Label l;
		/** because of constraint */
		protected JLiftLabelConstraint becauseConstraint;

		private final SubtypeConstraint subtypeConstraint;

		/** initialize */

		public SolverChange(int time, JLiftVarLabel v, Label l, JLiftLabelConstraint becauseConstraint, Label lhs, Label rhs) {
			this.time = time;
			this.v = v;
			this.l = l;
			this.becauseConstraint = becauseConstraint;
			this.subtypeConstraint = new SubtypeConstraint(lhs, rhs, becauseConstraint);
		}

		public SubtypeConstraint getSubtypeConstraint() {
			return subtypeConstraint;
		}

		/**
		 * @return the becauseConstraint
		 */
		public JLiftLabelConstraint getConstraint() {
			return becauseConstraint;
		}

		/**
		 * @return the l
		 */
		public Label getLabel() {
			return l;
		}

		/**
		 * @return the v
		 */
		public JLiftVarLabel getVariable() {
			return v;
		}

		public int getTime() {
			return time;
		}

		@Override
		public String toString() {
			return "(" + time + "): " + v + " changed to " + l + " because of " + becauseConstraint.shortString();
		}
	}

	public void reportChange(VarMap newMap, LabelConstraint c, Label newLabel, JLiftVarLabel changedVariable, Label lhs, Label rhs)  {
		// what was it changed to?  newMap(changedVariable)
		++time;
		if (c instanceof JLiftLabelConstraint) {
			JLiftLabelConstraint jlc = (JLiftLabelConstraint) c;
			SolverChange change = new SolverChange(time, changedVariable, newLabel, jlc, lhs, rhs);
			changeList.add(change);
		}
	}

	protected boolean satisfiedUnderVarMap(Label lhs, Label rhs, VarMap map) {
		Label lhsBound = map.applyTo(lhs);
		Label rhsBound = map.applyTo(rhs);

		// environments are useless to us
		boolean returnBool = emptyEnvironment.leq(lhsBound, rhsBound);

		if (lhsBound instanceof JLiftDynamicLabel_c && rhsBound instanceof JLiftDynamicLabel_c) {

		}
		return returnBool;
	}

	public LabelConstraint failureConstraint(LabelConstraint violated) {
		SolverChange change = failureChange(violated);
		if (change == null)
			return violated;
		else
			return change.getConstraint();
	}

	public SolverChange failureChange(LabelConstraint violated) {
		return failureChange(violated.lhs(), violated.rhs());
	}


	public SolverChange failureChange(Label lhs, Label rhs) {
		// construct a variable map for each step of the solver, using the information
		// in the change list

		VarMap theVarMap = new VarMap(jts, jts.bottomLabel());

		if (!satisfiedUnderVarMap(lhs, rhs, theVarMap)) {
			return null;
		}

		Iterator<SolverChange> itChanges = changeList.iterator();

		while(itChanges.hasNext())
		{
			SolverChange currentChange = itChanges.next();
			theVarMap.setBound(currentChange.getVariable(), currentChange.getLabel());

			if (!satisfiedUnderVarMap(lhs, rhs, theVarMap)) {
				return currentChange;
			}
		}

		return null;
	}

	public boolean explainError(LabelConstraint failedConstraint) {
		if (failedConstraint instanceof JLiftLabelConstraint)
			bo.reportError((JLiftLabelConstraint) failedConstraint);
		return true;
	}

	public boolean getErrorSet(LabelConstraint failedConstraint,
			List<ConstraintFailure> failedList, List<LabelConstraint> whyList) {
		if (failedConstraint instanceof JLiftLabelConstraint) {
			bo.getErrorSetFillLists((JLiftLabelConstraint) failedConstraint, failedList, whyList);
		}
		return true;
	}

	public int getTime() {
		return time;
	}

	public List<SolverChange> getChangeList() {
		return changeList;
	}

	public void setConstraints(Collection<JLiftLabelConstraint> allConstraints) {
		bo.setConstraints(allConstraints);
	}
}
