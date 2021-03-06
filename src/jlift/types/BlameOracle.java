/*
 * File          : BlameOracle.java
 * Project       : jlift
 * Description   : Encapsulates error retrieval/display.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:42:28 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.VarMap;
import jif.types.hierarchy.LabelEnv;
import jif.types.hierarchy.LabelEnv_c;
import jif.types.label.JoinLabel;
import jif.types.label.Label;
import jif.types.label.VarLabel;
import jifclipse.types.ConstraintFailure;
import jlift.JLiftOptions;
import jlift.frontend.PreconditionPass;
import jlift.types.SolverDependencyGraph.SolverChange;
import jlift.types.label.JLiftVarLabel;
import polyglot.util.ErrorInfo;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StdErrorQueue;

// TODO: refactor this code into the solver dependency graph
public class BlameOracle {

	protected JLiftSolver failingSolver;
	protected JifTypeSystem ts;
	protected SolverDependencyGraph depGraph;
	private final Collection<LinkedList<JLiftLabelConstraint>> errorTraces;
	private final Collection<String> declassificationStrings;

	protected Collection<JLiftLabelConstraint> constraints;

	static protected int errorNumber = 0;

	public BlameOracle(JifTypeSystem ts, JLiftSolver rehofSolver,
			SolverDependencyGraph depGraph, Collection<LinkedList<JLiftLabelConstraint>> errorTraces) {
		super();
		this.ts = ts;
		this.failingSolver = rehofSolver;
		this.depGraph = depGraph;
		this.errorTraces = errorTraces;
		this.declassificationStrings = new HashSet<String>();
	}

	public BlameOracle(JifTypeSystem jts,
			SolverDependencyGraph solverDependencyGraph) {
		this.ts = jts;
		this.depGraph = solverDependencyGraph;
		this.errorTraces = null;
		this.failingSolver = null;
		this.declassificationStrings = new HashSet<String>();
	}

	public LinkedList<JLiftLabelConstraint> reportError(
			JLiftLabelConstraint failedConstraint) {
		++errorNumber;
		LinkedList<SolverChange> errorSet = getErrorSet(failedConstraint);
		if (errorSet == null)
			return null;

		if (errorTraces == null) {
			if (!JLiftOptions.getInstance().forTestHarness()) {
				displayErrorSet(failedConstraint, errorSet, System.err);

				PrintStream fps;

				try {
					fps = new PrintStream(new FileOutputStream("error.txt", true));
					displayErrorSet(failedConstraint, errorSet, fps);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			LinkedList<JLiftLabelConstraint> currentError = new LinkedList<JLiftLabelConstraint>();
			currentError.add(failedConstraint);
			for(SolverChange sc : errorSet) {
				currentError.add(sc.getConstraint());
			}

			errorTraces.add(currentError);
		}

		LinkedList<JLiftLabelConstraint> involvedConstraints = new LinkedList<JLiftLabelConstraint>();
		for (SolverChange sc : errorSet) {
			involvedConstraints.add(sc.getConstraint());
		}

		return involvedConstraints;
	}

	private void displayErrorSet(JLiftLabelConstraint failedConstraint,
			LinkedList<SolverChange> errorSet, PrintStream ps) {
		StdErrorQueue eq = new StdErrorQueue(ps, 100000000, "display");

		ps.println("--- ERROR # " + errorNumber + " ---");
		ps.println("constraint: " + failedConstraint.toString());
		if (failedConstraint.position() != null)
			ps.println("generated by: " + failedConstraint.position().nameAndLineString());
		eq.displayError(new ErrorInfo(ErrorInfo.SEMANTIC_ERROR, "constraint generated by", failedConstraint.position()));
		ps.println("position: " + failedConstraint.position());
		ps.println("environment: " + failedConstraint.env() + "\n");
		ps.println("explanation:");
		ps.println();
		String prepend = "";

		int size = 0;
		int numLines = 0;
//		Position lastPosition = null;
		Collection<String> seenPositions = new HashSet<String>();

		ps.println("failed: " + failedConstraint.shortString());
		Label lhs = failedConstraint.lhs();
		Label rhs = failedConstraint.rhs();

		for (SolverChange sc : errorSet) {
			lhs = sc.getSubtypeConstraint().getLhs();
			Position pos = sc.getConstraint().position();
//			if (lastPosition != null &&
//				!(pos.file().equals(lastPosition.file()) && pos.line() == lastPosition.line())) {
//				++numLines;
//			}
//			lastPosition = pos;
			seenPositions.add(pos.file() + ":" + pos.line());

			if (isPCChange(sc)) {
				ps.println(prepend + "pc chain: " + sc.getConstraint());
				eq.displayError(new ErrorInfo(ErrorInfo.SEMANTIC_ERROR,
						"constraint generated by", sc.getConstraint().position()));
//				if (sc.getConstraint() != null) {
//					ps.println("(" + sc.getConstraint().getNode().getClass() + ")");
//				}

			} else {

				ps.println(prepend + "why: " + sc.getConstraint().toString() + " (" + sc.getConstraint().getNode() + ")");
				ps.println(prepend + "explanation: " + sc.getConstraint().detailMsg());
				eq.displayError(new ErrorInfo(ErrorInfo.SEMANTIC_ERROR,
						"constraint generated by", sc.getConstraint().position()));

//				if (sc.getConstraint() != null) {
//					ps.println("(" + sc.getConstraint().getNode().getClass() + ")");
//				}
				++size;
				prepend += "   ";
				ps.println(prepend + "failed: " + lhs + " <= " + rhs);
			}
		}

		ps.println(prepend + "unsatisfiable: " + lhs + " <= " + rhs);


		ps.println("error set size: " + errorSet.size());
		ps.println("explanation size: " + size);
		ps.println("# lines: " + seenPositions.size());
		Label failedLhs = failedConstraint.lhs();

		displayExceptionErrors(ps, eq, failedLhs);
		ps.println();
	}

	private void displayExceptionErrors(PrintStream ps, StdErrorQueue eq,
			Label l) {
		if (l instanceof VarLabel) {
			VarLabel vl = (VarLabel) l;
			if (vl.name().startsWith("Exception:")) {
				ps.println();
				eq.displayError(new ErrorInfo(ErrorInfo.SEMANTIC_ERROR, vl.name() + " generated by: ", vl.position()));
				System.out.println("ideally find node for " + vl.name() + " @ " + vl.position());
				for(JLiftLabelConstraint jlc : this.constraints) {
					if (jlc.lhs().equals(vl) && jlc.kind() == JLiftLabelConstraint.DEFINE) {
						//System.out.println("found it -- " + jlc.getNode());
						PreconditionPass.nodesToPreconditionAnalyze.add(jlc.getNode());
					}
				}
			}
		}
		if (l instanceof JoinLabel) {
			Collection<Label> joinLabel = ((JoinLabel) l).joinComponents();
			for(Label joinl : joinLabel) {
				displayExceptionErrors(ps, eq, joinl);
			}
		}
	}

	private boolean isPCChange(SolverChange sc) {
		return (sc.getConstraint().kind() == LabelConstraint.EQUAL  || sc.getConstraint().kind() == JLiftLabelConstraint.DEFINE)  &&
		sc.getConstraint().lhs() instanceof JLiftVarLabel &&
		((JLiftVarLabel) sc.getConstraint().lhs()).name().startsWith("pc");
	}

	public LinkedList<SolverChange> getErrorSet(LabelConstraint failedConstraint) {
		LinkedList<SolverChange> errorSet = getErrorSetSafe(failedConstraint.lhs(), failedConstraint.rhs(), -1, new LinkedList<ConstraintFailure>(), new LinkedList<LabelConstraint>());

		return errorSet;
	}

	private LinkedList<SolverChange> getErrorSetSafe(
			Label lhs, Label rhs, int lastTime, List<ConstraintFailure> failedList, List<LabelConstraint> whyList) {

		if (isUnsatisfiable(lhs, rhs)) {
			failedList.add(new ConstraintFailure(lhs, rhs, null));
			return new LinkedList<SolverChange>();
		} else {
			SolverChange reasonChange = depGraph.failureChange(lhs, rhs);

			if (reasonChange == null) {
				throw new InternalCompilerError("ERROR: could not find reason " + lhs + " <= " + rhs + " failed!");
//				Throwable t = new Throwable();
//				for(int i = 0; i < depGraph.getChangeList().size(); ++i) {
//					System.err.println("\t(" + i + ") " + depGraph.getChangeList().get(i));
//				}
//				t.printStackTrace();
//				return new LinkedList<SolverChange>();
			}

			Label failedExpression = reasonChange.getSubtypeConstraint().getLhs();
			Label recurseLhs = failedExpression;

			if (reasonChange.getTime() >= lastTime && lastTime != -1) {
				throw new InternalCompilerError(
						"reason for a constraint failing going forward in time?\n"
						+ lhs + " <= " + rhs + " failed at time "
						+ lastTime + " because of\n" + reasonChange);
			}

			failedList.add(new ConstraintFailure(lhs, rhs, null));
			whyList.add(reasonChange.getConstraint());

			LinkedList<SolverChange> recursiveExplanations =
				getErrorSetSafe(recurseLhs, rhs, reasonChange.getTime(), failedList, whyList);

			if (recursiveExplanations != null)
				recursiveExplanations.addFirst(reasonChange);

			return recursiveExplanations;
		}
	}

	private boolean isUnsatisfiable(Label lhs, Label rhs) {
		VarMap blankVarMap = new VarMap(ts, ts.bottomLabel());
		if (labelLessThan(blankVarMap, lhs, rhs, new LabelEnv_c(ts, false)))
			return false;
		else
			return true;
	}

	private boolean labelLessThan(VarMap bounds, Label lhs, Label rhs,
			LabelEnv env) {
		Label lhsbound = labelOf(bounds, lhs, env);
		Label rhsbound = labelOf(bounds, rhs, env);

		return env.leq(lhsbound, rhsbound);
	}

	private Label labelOf(VarMap bounds, Label lhs, LabelEnv env) {
		return env.triggerTransforms(bounds.applyTo(lhs));
	}

	public boolean getErrorSetFillLists(JLiftLabelConstraint failedConstraint,
			List<ConstraintFailure> failedList, List<LabelConstraint> whyList) {
		return (getErrorSetSafe(failedConstraint.lhs(), failedConstraint.rhs(), -1, failedList, whyList) != null);
	}

	public void setConstraints(Collection<JLiftLabelConstraint> constraints) {
		this.constraints = constraints;
	}
}
