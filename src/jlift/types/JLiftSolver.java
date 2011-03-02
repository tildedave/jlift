/*
 * File          : RehofSolver.java
 * Project       : jlift
 * Description   : Solver that tracks why changes to variables happen
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:23:36 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import jif.types.Equation;
import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.SolverGLB;
import jif.types.VarMap;
import jif.types.hierarchy.LabelEnv;
import jif.types.label.Label;
import jif.types.label.NotTaken;
import jif.types.label.VarLabel;
import jifclipse.InternalCompilerInfo;
import jifclipse.types.JifclipseBlameEngine;
import jifclipse.types.JifclipseLabelConstraint;
import jifclipse.types.JifclipseSolver;
import jifclipse.types.JifclipseTypeSystem;
import jlift.JLiftOptions;
import jlift.types.label.JLiftVarLabel;
import polyglot.frontend.Compiler;
import polyglot.types.SemanticException;
import polyglot.util.ErrorInfo;
import polyglot.util.InternalCompilerError;

//BIG TODO:
//clean this up

public class JLiftSolver extends SolverGLB implements JifclipseSolver {

	protected HashSet<LabelConstraint> errorConstraints;
	protected LinkedHashSet<JifclipseLabelConstraint> allConstraints;

	protected JifclipseBlameEngine be;
	protected SemanticException error = null;

	// DHK: might be able to get rid of these
	protected boolean isRecursiveSearch;
	int recursionLevel = 0;

	protected Collection<LinkedList<JLiftLabelConstraint>> errorTraces;

//	static protected MultiMap<LabelConstraint, JLiftSolver> failedSolvers = new MultiMap<LabelConstraint, JLiftSolver>();
//	static protected MultiMap<JLiftSolver, JLiftSolver> solverTree = new MultiMap<JLiftSolver, JLiftSolver>();

	static {
		// destroy the error.txt file -- but only once
		PrintStream fps;
		try {
			fps = new PrintStream(new FileOutputStream("error.txt",false));
			fps.close();
			new File("error.txt").delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public JLiftSolver(JifTypeSystem ts, Compiler compiler, String solverName, Collection<LinkedList<JLiftLabelConstraint>> traces) {
		super(ts, compiler, solverName);
		this.bounds = new JLiftVarMap(ts, getDefaultBound());
		this.be = new SolverDependencyGraph(ts);
		this.errorConstraints = new HashSet<LabelConstraint>();

		this.isRecursiveSearch = false;
		if (traces != null)
			this.errorTraces = traces;
		this.allConstraints = new LinkedHashSet<JifclipseLabelConstraint>();
	}


	public JLiftSolver(JifTypeSystem ts, Compiler compiler, String solverName) {
		this(ts,compiler, solverName,null);
	}


	public JLiftSolver(JLiftSolver solver) {
		super(solver);
		this.bounds = bounds.copy();
		// TODO: this is a hack, and should be redone when I recode this to handle
		// backtracking correctly
		this.be = solver.be; //new SolverDependencyGraph((SolverDependencyGraph) solver.be);
		this.errorConstraints = new HashSet<LabelConstraint>(solver.errorConstraints);

		this.isRecursiveSearch = true;
		this.allConstraints = new LinkedHashSet<JifclipseLabelConstraint>(solver.allConstraints);
	}

	@Override
	protected VarMap solve_bounds() throws SemanticException {
		VarMap returnMap = super.solve_bounds();
		return returnMap;
	}

	@Override
	protected boolean search(Equation eqn) {

		// if we've already found an error, then a recursive search will not work -- we will instead
		// spend a lot of time running into an error that we've already found!
		if (!errorConstraints.isEmpty())
			return false;

		//System.err.println("starting recursive search to " + eqn + " at time " + ((SolverDependencyGraph) be).getTime());
//		System.err.println("backtracking " + eqn.constraint());

		if (shouldReport(2)) {
			report(2, "===== Starting recursive search =====");
		}
		if (shouldReport(2))
			report(2, recursionLevel + "|-> starting recursive search " + eqn.constraint());

		JLiftSolver js = new JLiftSolver(this);
		js.recursionLevel = this.recursionLevel + 1;

		// make sure this equation is satisfied before continuing.
		js.addEquationToQueueHead(eqn);

		try {
			setBounds(js.solve_bounds());
			if (shouldReport(2)) report(2, "Solution succeeded, finishing up");
			return true;
		}
		catch (SemanticException dummy) {
			if (shouldReport(2)) report(2, "Solution failed, backtracking");

			if (shouldReport(2))
				report(2, recursionLevel + "|-> search failed with " + eqn.constraint());


			//System.err.println("failed recursive search to " + eqn);

			return false;
		}
	}

	@Override
	protected void reportError(LabelConstraint c, Collection variables)
	throws SemanticException {

		// TODO: re-enable this and treat backtracking right
		//		if (this.isRecursiveSearch) {
		//			super.reportError(c, variables);
		//		}

		if (errorConstraints.contains(c))
			return;

		if (JLiftOptions.getInstance().forTestHarness())
			super.reportError(c, variables);

		InternalCompilerInfo ici = ((JifclipseTypeSystem) this.typeSystem()).getInternalCompilerInfo();

		// no polymorphism for subtyping for mutability reasons
		((SolverDependencyGraph) be).setConstraints((Collection) this.allConstraints);
		if (!ici.getErrorConstraints().contains(c) && be.explainError(c)) {
			ici.addErrorConstraint(c);
			errorConstraints.add(c);

			compiler.errorQueue().enqueue(new ErrorInfo(ErrorInfo.SEMANTIC_ERROR, errorMsg(c), c.position()));
		}
	}

	/**
	 * This method changes the bounds of variables in the RHS of Equation eqn,
	 * to make the equation satisfied. If the equation has no variables in the
	 * RHS, it just checks that the equation holds, and returns.
	 *
	 * Otherwise, if the RHS has exactly one variable, then it refines that
	 * variable
	 * {@linkplain SolverGLB#refineVariableEquation(VarLabel, Equation) (see here)}
	 * and returns. If the RHS has more than one variable, then the method
	 * performs a search, attempting to refine each variable in turn, and then
	 * recursively attempting to solve the set of equations.
	 */
	@Override
	protected void solve_eqn(Equation eqn) throws SemanticException {
		// there are occurrences of variables on the RHS of the equation
		// there may be a join of components on the RHS of the equation.
		// we will need to try all possible ways of satisfying this equation,
		// trying the simple ones (i.e. var labels) first.

		List rhsVariables = new LinkedList(eqn.rhs().variableComponents());
		if (rhsVariables.isEmpty()) {
			// the RHS has no variable components in it, it has nothing for us to
			// modify the bound of. It had better hold...

			// L <= L', where L' cannot contain variables. Failure will throw
			// an exception.
			if (eqn.constraint().kind() == LabelConstraint.LEQ)
				checkEquation(eqn);
			return;
		}

		// at this point we know that at least one component of the RHS of eqn
		// has a variable...

		// get a count of them, to figure out if we need to do a search...
		if (rhsVariables.size() == 1) {
			// only a single component is a variable
			refineVariableEquation((VarLabel)rhsVariables.get(0), eqn);
		}
		else {
			if (!allActivesAreMultiVarRHS()) {
				// some of the active equations have single variables
				// on the RHS. Satisfy those first, to reduce the search
				// effort.
				if (shouldReport(3))
					report(3, "Deferring multi var RHS constraint");
				addEquationToQueue(eqn);
				return;
			}

			// we will do a very simple search, not delaying the
			// solution of any constraints...
			// we only need one of the components to satisfy the constraints.
			// we will just try each one in turn.
			// copy the bounds before we start modifying anything, so we can
			// restore it again later...
			VarMap origBounds = bounds().copy();

			for (Iterator i = rhsVariables.iterator(); i.hasNext();) {
				VarLabel comp = (VarLabel)i.next();

				refineVariableEquation(comp, eqn);
				// check that the equation is now satisfied.
				Label lhsbound = triggerTransforms(bounds().applyTo(eqn.lhs()), eqn.env());
				Label rhsbound = triggerTransforms(bounds().applyTo(eqn.rhs()), eqn.env());

				if (eqn.env().leq(lhsbound, rhsbound) && search(eqn)) {
					// we were successfully able to find a solution to the
					// constraints!
//					if (failedSolvers.get(eqn.constraint()) != null)
//					failedSolvers.get(eqn.constraint()).clear();

					return;
				}

				// search failed!
				// set the bounds back to their original settings, and
				// try with another component
				setBounds(origBounds);
			}

			// if we fall through to here, then the search failed.
			if (shouldReport(1)) {
				report(1, "Search for refinement to constraint " + eqn + " failed.");
			}
		}

	}

	@Override
	protected void checkEquation(Equation eqn) throws SemanticException {
		if (eqn.rhs().hasVariableComponents()) {
			throw new InternalCompilerError("RHS of equation " + eqn
					+ " should not contain variables.");
		}

		// This equation must have been woken up. We need to
		// check whether it is solvable given the current variables.

		Label rhsLabel = triggerTransforms(bounds().applyTo(eqn.rhs()), eqn.env());
		if (shouldReport(4)) report(4, "RHS = " + rhsLabel);

		Label lhsBound = triggerTransforms(bounds().applyTo(eqn.lhs()), eqn.env());
		if (shouldReport(4)) report(4, "LHS APP = " + lhsBound);

		// Check to see if it is currently satisfiable.
		if (!eqn.env().leq(lhsBound, rhsLabel)) {
			//            //try bounding the dynamic labels
			//            if (dynCheck(lhsBound, rhsLabel, eqn.env())) return;

			// This equation isn't satisfiable.
			reportError(eqn.constraint(), eqn.variableComponents());
		}
	}

	@Override
	protected void processConstraint(LabelConstraint c)
	throws SemanticException {
//		if (c.lhs() instanceof JLiftVarLabel && c.kind() == LabelConstraint.EQUAL) {
//			if (fixedValueVars.contains(c.lhs())) {
//				Label existingBound = bounds.boundOf((VarLabel) c.lhs());
//				if (existingBound.variableComponents().isEmpty() && c.rhs().variableComponents().isEmpty()) {
//					reportError(c, new HashSet());
//				}
//				else {
//					this.addConstraint(new JLiftLabelConstraint(new NamedLabel("", c.rhs()), LabelConstraint.EQUAL, new NamedLabel("",this.bounds.boundOf((VarLabel) c.lhs())), new LabelEnv_c(ts, false), c.position()));
//				}
//			}
//		}
		super.processConstraint(c);
		if (c.lhs() instanceof JLiftVarLabel && c.kind() == LabelConstraint.EQUAL) {
			((SolverDependencyGraph) be).reportChange(bounds(), c, bounds().applyTo(c.lhs()), (JLiftVarLabel) c.lhs(), c.rhs(), c.lhs());
		}
	}

	@Override
	protected void refineVariableEquation(VarLabel v, Equation eqn)
	throws SemanticException {
		super.refineVariableEquation(v, eqn);
		if (v instanceof JLiftVarLabel) { // should be
			((SolverDependencyGraph) be).reportChange(bounds(), eqn.constraint(), bounds().applyTo(v), (JLiftVarLabel) v, eqn.lhs(), eqn.rhs());
		}
	}

	/**
	 * Find a contradicting equation.
	 */
	@Override
	protected Equation findContradictiveEqn(LabelConstraint c) {
		// contract the stupid logic that's in the parent
		return (Equation) c.getEquations().iterator().next();
	}

	public Compiler compiler() {
		return ts.extensionInfo().compiler();
	}


	public Collection<JifclipseLabelConstraint> getConstraints() {
		return allConstraints;
	}


	public JifTypeSystem typeSystem() {
		return ts;
	}

	public String getErrorMsg(LabelConstraint c) {
		// make errorMsg publicly accessible
		return errorMsg(c);
	}

	@Override
	public String errorStringConstraint(LabelConstraint c) {
		// make this public
		return super.errorStringConstraint(c);
	}

	@Override
	public String errorStringDefns(LabelConstraint c) {
		// make this public
		return super.errorStringDefns(c);
	}

	public JifclipseBlameEngine blameEngine() {
		return be;
	}

	@Override
	public void addConstraint(LabelConstraint c) throws SemanticException {
		if (c instanceof JifclipseLabelConstraint)
			this.allConstraints.add((JifclipseLabelConstraint) c);
		//super.addConstraint(c);

        if (status != STATUS_NOT_SOLVED) {
            throw new InternalCompilerError("Computed solution already. "
                    + "Cannot add more constraints");
        }

        if (shouldReport(5)) report(5, (constraint_counter + 1) + ": " + c);
        if (shouldReport(6)) report(6, ">>> " + c.msg());
        inc_counter();

        if (!c.lhs().isCanonical() || !c.rhs().isCanonical()) {
            throw new SemanticException(errorMsg(c), c.position());
        }

        if (c.lhs() instanceof NotTaken && c.kind() == LabelConstraint.LEQ) {
            // if the LHS is NotTaken, then the constraint will always be
            // satisfied.
            return;
        }

        if (c.rhs() instanceof NotTaken && c.kind() == LabelConstraint.LEQ) {
            // if the RHS is NotTaken (and the LHS isn't), then the
            // constraint can never be satisfied.

            throw new SemanticException(errorMsg(c), c.position());
        }
        processConstraint(c);
        addConstraintEquations(c);
	}


	public Collection<LabelConstraint> getErrorConstraints() {
		return this.errorConstraints;
	}

    /**
     * Go through each equation in the constraint, add the equation
     * if needed, and register dependencies for the equation.
     * @param c
     * @throws SemanticException
     */
    @Override
	protected void addConstraintEquations(LabelConstraint c) throws SemanticException {
        Collection eqns = c.getEquations();
        Equation eqn = null;
        for (Iterator i = eqns.iterator(); i.hasNext();) {
            eqn = (Equation)i.next();
            LabelEnv eqnEnv = eqn.env();
            Label lhs = eqn.lhs();
            Label rhs = eqn.rhs();
            if (!eqnEnv.hasVariables() && !lhs.hasVariables()
                    && !rhs.hasVariables()) {
                // The equation has no variables. We can check now if it is
                // satisfied or not

                if (!eqnEnv.leq(triggerTransforms(lhs, eqnEnv),
                                triggerTransforms(rhs, eqnEnv))) {

                    if (shouldReport(2)) {
                        report(2, "Statically failed " + eqn);
                    }
                    if (shouldReport(3)) {
                        report(3, "Statically failed " + triggerTransforms(lhs, eqnEnv) + " <= " + triggerTransforms(rhs, eqnEnv));
                    }
                    // The equation is not satisfied.
                	reportError(c, null);
//                    throw new SemanticException(errorMsg(eqn.constraint()),
//                                                eqn.position());
                	throw new SemanticException("got static failure");
                }
                else {
                    // The equation is satisfied, no need to add it to
                    // the queue.
                }
            }
            else {
                if (shouldReport(5)) report(5, "Adding equation: " + eqn);
                eqnEnv.setSolver(this);
                equations.add(eqn);
                addDependencies(eqn);
            }

        }
    }
}
