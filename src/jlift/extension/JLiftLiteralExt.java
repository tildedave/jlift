package jlift.extension;

import jif.extension.JifLiteralExt;
import jif.translate.ToJavaExt;
import jif.types.JifContext;
import jif.types.JifTypeSystem;
import jif.types.PathMap;
import jif.visit.LabelChecker;
import polyglot.ast.Node;
import polyglot.types.SemanticException;

public class JLiftLiteralExt extends JifLiteralExt {

	public JLiftLiteralExt(ToJavaExt toJava) {
		super(toJava);
	}

	@Override
	public Node labelCheck(LabelChecker lc) throws SemanticException {
		JifTypeSystem ts = lc.jifTypeSystem();
        JifContext A = lc.jifContext();
        A = (JifContext) node().del().enterScope(A);

        PathMap X = ts.pathMap();
        X = X.N(A.pc());
        X = X.NV(ts.bottomLabel());
        return updatePathMap(node(), X);
	}

}
