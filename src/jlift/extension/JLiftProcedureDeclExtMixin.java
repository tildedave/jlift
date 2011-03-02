/*
 * File          : JLiftProcedureDeclExtMixin.java
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

import java.util.Iterator;

import jif.extension.JifProcedureDeclExt_c;
import jif.extension.SubtypeChecker;
import jif.translate.ToJavaExt;
import jif.types.ExceptionPath;
import jif.types.JifContext;
import jif.types.JifProcedureInstance;
import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.LabelConstraintMessage;
import jif.types.NamedLabel;
import jif.types.Path;
import jif.types.PathMap;
import jif.types.label.ArgLabel;
import jif.types.label.DynamicLabel;
import jif.types.label.Label;
import jif.types.label.NotTaken;
import jif.types.label.PairLabel;
import jif.types.label.VarLabel;
import jif.visit.LabelChecker;
import jlift.JLiftOptions;
import jlift.util.MultiMap;
import jlift.visit.SummaryConstraintGenerator;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;

public class JLiftProcedureDeclExtMixin extends JifProcedureDeclExt_c {

	public JLiftProcedureDeclExtMixin(ToJavaExt toJava) {
		super(toJava);
	}

	static String jif_verbose = "jif";

	protected void addReturnConstraints(Node n, Label Li, PathMap X,
			JifProcedureInstance mi, LabelChecker lc, Type returnType)
	throws SemanticException {
		if (Report.should_report(jif_verbose, 2))
			Report.report(2, "Adding constraints for result of " + mi);

		ProcedureDecl mn = (ProcedureDecl) n;
		JifTypeSystem ts = lc.jifTypeSystem();
		JifContext A = lc.jifContext();

		// Add the return termination constraints.

		// fold the call site pc into the return label
		Label Lr = lc.upperBound(mi.returnLabel(), ts.callSitePCLabel(mi));


		//Hack: If no other paths, the procedure must return. Therefore,
		//X.n is not taken, and X.r doesn't contain any information.
		//TODO: implement a more precise single-path rule.
		if (! (X.N() instanceof NotTaken)) {
			boolean singlePath = true;
			for (Iterator iter = X.paths().iterator(); iter.hasNext(); ) {
				Path p = (Path) iter.next();
				if (p.equals(Path.N) || p.equals(Path.R)) continue;
				singlePath = false;
				break;
			}
			if (singlePath) {
				X = X.N(ts.notTaken());
				X = X.R(ts.bottomLabel());
			}
		}


		Label xn = X.N();
		Label xr = X.R();

//		DHK: I think I'm getting rid of these once and for all.
//		they're redundant and break context sensitivity.
// 		DHK: need these to properly determine when an expression is a sink
// 		DHK: removing these again (8/18/08) -- not using SAT means we don't need sink detection
//		if (true) { //JLiftOptions.getInstance().shouldGenerateDeclassificationConstraints()) {
//			if (!(X.N() instanceof NotTaken)) {
//				VarLabel vXn = ts.freshLabelVariable(mn.position(), mn.name() + ":normal_termination", "label representing normal termination of the procedure declaration");
//				lc.constrain(new NamedLabel("", vXn), JLiftLabelConstraint.EQUAL, new NamedLabel("", xn), lc.context().labelEnv(), n.position());
//
//				xn = vXn;
//			}
//
//			if (!(X.R() instanceof NotTaken)) {
//				VarLabel vXr = ts.freshLabelVariable(mn.position(), mn.name() + ":return", "label representing return termination of the procedure declaration");
//				lc.constrain(new NamedLabel("", vXr), JLiftLabelConstraint.EQUAL, new NamedLabel("", xr), lc.context().labelEnv(), n.position());
//
//				xr = vXr;
//			}
//		}

//		System.out.println("return label: " + Lr + " -- " + mi.returnLabel() + " and " + ts.callSitePCLabel(mi));

		if (JLiftOptions.getInstance().safeMutateConstraintSet && isNonAtomicLabel(Lr)) {

			System.out.println(Lr + " is non-atomic");

			lc.constrain(new NamedLabel("X.n",
					"information that may be gained by the body terminating normally",
					xn).
					join(lc,
							"X.r",
							"information that may be gained by exiting the body with a return statement",
							xr),
							LabelConstraint.LEQ,
							new NamedLabel("Lr",
									"return label of the method",
									mi.returnLabel()),
									A.labelEnv(),
									mn.position(),
									new LabelConstraintMessage()
			{
				@Override
				public String msg() {
					return "The non-exception termination of the " +
					"method body may reveal more information " +
					"than is declared by the method return label.";
				}
				@Override
				public String detailMsg() {
					return "The method return label, " + namedRhs() +
					", is an upper bound on how much " +
					"information can be gained by observing " +
					"that this method terminates normally " +
					"(i.e., terminates without throwing " +
					"an exception). The method body may " +
					"reveal more information than this. The " +
					"return label of a method is declared " +
					"after the variables, e.g. " +
					"\"void m(int i):{" + namedRhs() + "}\".";
				}
				@Override
				public String technicalMsg() {
					return "the return(end) label is less restrict than " +
					namedLhs() + " of the body.";
				}
			}
			);

			lc.constrain(new NamedLabel("X.n",
					"information that may be gained by the body terminating normally",
					xn).
					join(lc,
							"X.r",
							"information that may be gained by exiting the body with a return statement",
							xr),
							LabelConstraint.LEQ,
							new NamedLabel("Lr",
									"return label of the method",
									ts.callSitePCLabel(mi)),
									A.labelEnv(),
									mn.position(),
									new LabelConstraintMessage()
			{
				@Override
				public String msg() {
					return "The non-exception termination of the " +
					"method body may reveal more information " +
					"than is declared by the method return label.";
				}
				@Override
				public String detailMsg() {
					return "The method return label, " + namedRhs() +
					", is an upper bound on how much " +
					"information can be gained by observing " +
					"that this method terminates normally " +
					"(i.e., terminates without throwing " +
					"an exception). The method body may " +
					"reveal more information than this. The " +
					"return label of a method is declared " +
					"after the variables, e.g. " +
					"\"void m(int i):{" + namedRhs() + "}\".";
				}
				@Override
				public String technicalMsg() {
					return "the return(end) label is less restrict than " +
					namedLhs() + " of the body.";
				}
			}
			);
		}
		else {
			lc.constrain(new NamedLabel("X.n",
					"information that may be gained by the body terminating normally",
					xn).
					join(lc,
							"X.r",
							"information that may be gained by exiting the body with a return statement",
							xr),
							LabelConstraint.LEQ,
							new NamedLabel("Lr",
									"return label of the method",
									Lr),
									A.labelEnv(),
									mn.position(),
									new LabelConstraintMessage()
			{
				@Override
				public String msg() {
					return "The non-exception termination of the " +
					"method body may reveal more information " +
					"than is declared by the method return label.";
				}
				@Override
				public String detailMsg() {
					return "The method return label, " + namedRhs() +
					", is an upper bound on how much " +
					"information can be gained by observing " +
					"that this method terminates normally " +
					"(i.e., terminates without throwing " +
					"an exception). The method body may " +
					"reveal more information than this. The " +
					"return label of a method is declared " +
					"after the variables, e.g. " +
					"\"void m(int i):{" + namedRhs() + "}\".";
				}
				@Override
				public String technicalMsg() {
					return "the return(end) label is less restrict than " +
					namedLhs() + " of the body.";
				}
			}
			);
		}

		// return value constraints are implemented at the "return" statement, in order
		// to make use of the (more precise) label environment there.

		MultiMap<Type, Label> typeToExceptionMap = new MultiMap<Type, Label>();

		// Add the exception path constraints.
		for (Iterator iter = X.paths().iterator(); iter.hasNext(); ) {
			Path path = (Path) iter.next();

			if (! (path instanceof ExceptionPath)) {
				continue;
			}

			ExceptionPath ep = (ExceptionPath) path;


			Label pathLabel = X.get(ep);


			if (pathLabel instanceof NotTaken)
				throw new InternalCompilerError(
						"An exception path cannot be not taken");

			typeToExceptionMap.put(ep.exception(), pathLabel);

			Type pathType = ep.exception();
			NamedLabel pathNamedLabel = new NamedLabel("exc_"+pathType.toClass().name(),
					"upper bound on information that may be gained " +
					"by observing the method throwing the exception " + pathType.toClass().name(),
					pathLabel);

			for (Iterator j = mi.throwTypes().iterator(); j.hasNext(); ) {
				final Type tj = (Type) j.next();
				Label Lj = ts.labelOfType(tj, Lr);

				// fold the call site pc into the return label
				Lj = lc.upperBound(Lj, ts.callSitePCLabel(mi));

				if (ts.isSubtype(pathType, tj) ||
						ts.isSubtype(tj, pathType)) {
					if (ts.isSubtype(pathType, tj)) {
						SubtypeChecker subtypeChecker = new SubtypeChecker(tj, pathType);
						subtypeChecker.addSubtypeConstraints(lc, mn.position());
					}
					else {

						SubtypeChecker subtypeChecker = new SubtypeChecker(pathType, tj);
						subtypeChecker.addSubtypeConstraints(lc, mn.position());
					}
					if (Report.should_report(jif_verbose, 4))
						Report.report(4,
								">>> X[C'] <= Lj (for exception " + tj + ")");

					lc.constrain(pathNamedLabel,
							LabelConstraint.LEQ,
							new NamedLabel("decl_exc_"+tj.toClass().name(),
									"declared upper bound on information that may be " +
									"gained by observing the method throwing the exception " + tj.toClass().name(),
									Lj),
									A.labelEnv(),
									mi.position(),
									new LabelConstraintMessage()
					{
						@Override
						public String msg() {
							return "More information may be gained " +
							"by observing a " + tj.toClass().fullName() +
							" exception than is permitted by the " +
							"method/constructor signature";
						}
						@Override
						public String technicalMsg() {
							return "the path of <" + tj + "> may leak information " +
							"more restrictive than the join of the declared " +
							"exception label and the return(end) label";
						}
					}
					);
				}
			}
		}

		if (lc instanceof SummaryConstraintGenerator) {
			SummaryConstraintGenerator scg = (SummaryConstraintGenerator) lc;
			scg.getSummaryInfo().setExceptionVariables(mn.procedureInstance(), typeToExceptionMap);
		}
	}

	private boolean isNonAtomicLabel(Label lr) {
		if (lr instanceof PairLabel || lr instanceof VarLabel || lr instanceof DynamicLabel)
			return false;
		if (lr instanceof ArgLabel)
			return isNonAtomicLabel(((ArgLabel) lr).upperBound());
		return true;
	}
}
