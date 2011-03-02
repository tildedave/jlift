package jlift.extension;

import java.util.ArrayList;
import java.util.List;

import jif.extension.JifThrowDel;
import jif.extension.JifThrowExt;
import jif.translate.ToJavaExt;
import jif.types.JifContext;
import jif.types.JifTypeSystem;
import jif.types.PathMap;
import jif.visit.LabelChecker;
import jlift.types.JLiftPathMap;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Throw;
import polyglot.types.SemanticException;

public class JLiftThrowExt extends JifThrowExt {

	public JLiftThrowExt(ToJavaExt toJava) {
		super(toJava);
	}

	@Override
	public Node labelCheckStmt(LabelChecker lc) throws SemanticException {
        Throw ths = (Throw) node();

        JifTypeSystem ts = lc.jifTypeSystem();
        JifContext A = lc.jifContext();
        A = (JifContext) ths.del().enterScope(A);

        List throwTypes = new ArrayList(ths.del().throwTypes(ts));

        Expr e = (Expr) lc.context(A).labelCheck(ths.expr());
        PathMap Xe = getPathMap(e);
        PathMap X = Xe;

        if (!((JifThrowDel)ths.del()).thrownIsNeverNull() && !ts.NullPointerException().equals(e.type())) {
            checkAndRemoveThrowType(throwTypes, ts.NullPointerException());
            X = X.exc(Xe.NV(), ts.NullPointerException());
        }
        checkAndRemoveThrowType(throwTypes, e.type());
        //System.out.println("lc -- " + ((SummaryConstraintGenerator) lc).getCurrentNode() + ":" + lc.hashCode());
        X = ((JLiftPathMap) X).exc(Xe.NV(), e.type());

        // the evaluation doesn't terminate normally.
        X = X.N(ts.notTaken()).NV(ts.notTaken());

//        if (JLiftOptions.getInstance().mutateConstraintSet) {
//        	System.err.println("hi hi -- " + Xe.NV() + " <= " + A.pc());
//        	lc.constrain(new NamedLabel("", Xe.NV()),
//        			LabelConstraint.LEQ,
//        			new NamedLabel("", A.pc()),
//        			A.labelEnv(),
//        			node().position());
//        }

        checkThrowTypes(throwTypes);
        return updatePathMap(ths.expr(e), X);
	}
}
