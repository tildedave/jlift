package jlift.extension;

import java.util.Iterator;

import jif.extension.JifCastExt;
import jif.translate.ToJavaExt;
import jif.types.JifSubstType;
import jif.types.LabelConstraint;
import jif.types.LabelConstraintMessage;
import jif.types.NamedLabel;
import jif.types.label.Label;
import jif.visit.LabelChecker;
import jlift.types.JLiftTypeSystem_c;
import polyglot.ast.Cast;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.util.ErrorInfo;
import polyglot.util.StdErrorQueue;

public class JLiftCastExt extends JifCastExt {

	public JLiftCastExt(ToJavaExt toJava) {
		super(toJava);
	}

	@Override
	public Node labelCheck(LabelChecker lc) throws SemanticException {
		Node superLabelCheck = super.labelCheck(lc);
		JLiftTypeSystem_c jts = (JLiftTypeSystem_c) lc.typeSystem();
		// if this is to an lvar type, make sure that they are
		// invariant

		Cast c = (Cast) superLabelCheck;


		boolean castIsToSubstType = jts.unlabel(c.castType().type()) instanceof JifSubstType;
		boolean exprIsSubstType = jts.unlabel(c.expr().type()) instanceof JifSubstType;
		if (castIsToSubstType && exprIsSubstType) {
			JifSubstType castTypeSubstType = (JifSubstType) jts.unlabel(c.castType().type());
			JifSubstType exprSubstType = (JifSubstType) jts.unlabel(c.expr().type());

			if (castTypeSubstType.actuals().size() == exprSubstType.actuals().size()) {
				Iterator itCastActuals = castTypeSubstType.actuals().iterator();
				Iterator itExprActuals = exprSubstType.actuals().iterator();

				while(itCastActuals.hasNext()) {
					Object actualCast = itCastActuals.next();
					Object actualExpr = itExprActuals.next();
					if (actualCast instanceof Label && actualExpr instanceof Label) {
						Label castActual = (Label) actualCast;
						Label exprActual = (Label) actualExpr;

						lc.constrain(new NamedLabel("", castActual),
								LabelConstraint.EQUAL,
								new NamedLabel("", exprActual), lc.context().labelEnv(), node.position(),
								new LabelConstraintMessage() {
							@Override
							public String msg() {
								return "cannot downgrade secrecy through cast to parameterized type";
							}
						});
					}
				}
			}
			else {
				System.err.println("warning: cast to parameterized expression with different # of parameters -- " + c);
			}
		}
		else if (exprIsSubstType && !castIsToSubstType) {
			JifSubstType exprSubstType = (JifSubstType) jts.unlabel(c.expr().type());
			if (exprSubstType.actuals().size() > 0) {
				StdErrorQueue eq = new StdErrorQueue(System.err, 1, "cast warning");
				eq.enqueue(new ErrorInfo(ErrorInfo.WARNING, "warning: cast from parameterized type to a non parameterized type, might lose information-flow", node.position()));
//				System.err.println("expr: " + c.expr().type() + " (" + exprIsSubstType + ")");
//				System.err.println("expr: " + exprSubstType + " ( " + exprSubstType.actuals() + " )" + " (" + exprIsSubstType + ")");
//				System.err.println("cast: " + c.castType().type() + " (" + castIsToSubstType + ")");
			}
		}

		return superLabelCheck;

	}

}
