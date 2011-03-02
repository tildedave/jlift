package jlift.extension;

import jif.ast.LabelExpr;
import jif.ast.NewLabel;
import jif.extension.JifLabelExprExt;
import jif.translate.ToJavaExt;
import jif.visit.LabelChecker;
import jlift.types.LabelPrimitiveType_c;
import jlift.visit.SummaryConstraintGenerator;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.types.Type;

public class JLiftLabelExprExt extends JifLabelExprExt {

	public JLiftLabelExprExt(ToJavaExt toJava) {
		super(toJava);
	}

	@Override
	public Node labelCheck(LabelChecker lc) throws SemanticException {
		LabelExpr le = (LabelExpr) node();
		if (le instanceof NewLabel) {
			NewLabel nl = (NewLabel) le;
			Type t = nl.type();
			if (t instanceof LabelPrimitiveType_c) {
				LabelPrimitiveType_c jdl = (LabelPrimitiveType_c) t;

				SummaryConstraintGenerator scg = (SummaryConstraintGenerator) lc;
				scg.addLabelMapping(jdl.id(), nl.label().label());
			}
		}
		// TODO Auto-generated method stub
		return super.labelCheck(lc);
	}

}
