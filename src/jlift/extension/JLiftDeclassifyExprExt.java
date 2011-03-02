package jlift.extension;

import jif.ast.DowngradeExpr;
import jif.extension.JifDeclassifyExprExt;
import jif.translate.ToJavaExt;
import jif.types.JifContext;
import jif.types.PathMap;
import jif.types.label.Label;
import jif.visit.LabelChecker;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.types.SemanticException;

public class JLiftDeclassifyExprExt extends JifDeclassifyExprExt {

	public JLiftDeclassifyExprExt(ToJavaExt toJava) {
		super(toJava);
	}

	@Override
	public Node labelCheck(LabelChecker lc) throws SemanticException {
		final DowngradeExpr d = (DowngradeExpr) node();

		JifContext A = lc.jifContext();
		A = (JifContext) d.del().enterScope(A);

		Label originalPC = A.pc();
		Expr e = (Expr) lc.context(A).labelCheck(d.expr());
		//A.setPc(originalPC, lc);
		PathMap Xe = getPathMap(e);

		Xe = downgradeExprPathMap(lc.context(A), Xe);

		Label downgradeTo = d.label().label();
		Label downgradeFrom = null;
		boolean boundSpecified;
		if (d.bound() != null) {
			boundSpecified = true;
			downgradeFrom = d.bound().label();
		}
		else {
			boundSpecified = false;
			downgradeFrom = lc.typeSystem().freshLabelVariable(d.position(),
					"downgrade_from",
					"The label the downgrade expression is downgrading from");
		}

//		System.out.println("Declassification: " + node());


//		lc.constrain(new NamedLabel("expr.nv", Xe.NV()),
//				boundSpecified?LabelConstraint.LEQ:LabelConstraint.EQUAL,
//						new NamedLabel("downgrade_bound", downgradeFrom),
//						A.labelEnv(),
//						d.position(),
//						boundSpecified, /* report this constraint if the bound was specified*/
//						new LabelConstraintMessage() {
//			@Override
//			public String msg() {
//				return "The label of the expression to " +
//				d.downgradeKind()+" is " +
//				"more restrictive than label of data that " +
//				"the "+d.downgradeKind()+" expression is allowed to "+d.downgradeKind()+".";
//			}
//			@Override
//			public String detailMsg() {
//				return "This "+d.downgradeKind()+" expression is allowed to " +
//				""+d.downgradeKind()+" information labeled up to " +
//				namedRhs() + ". However, the label of the " +
//				"expression to "+d.downgradeKind()+" is " +
//				namedLhs() + ", which is more restrictive than " +
//				"allowed.";
//			}
//			@Override
//			public String technicalMsg() {
//				return "Invalid "+d.downgradeKind()+": NV of the " +
//				"expression is out of bound.";
//			}
//		}
//		);

		JifContext dA = declassifyConstraintContext(lc, A, downgradeFrom, downgradeTo);
		checkOneDimenOnly(lc, dA, downgradeFrom, downgradeTo, d.position());

		// DHK: treat declassifications atomically.  sigh.  how should this really work?
		// otherwise I have lots of problems with passing declassified results to methods.
//		PathMap X = Xe.NV(lc.upperBound(dA.pc(), downgradeTo)).N(originalPC);
		PathMap X = Xe.NV(downgradeTo).N(originalPC);

		return updatePathMap(d.expr(e), X);
	}
}
