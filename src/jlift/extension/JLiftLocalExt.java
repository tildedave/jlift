package jlift.extension;

import jif.extension.JifLocalExt;
import jif.translate.ToJavaExt;
import jif.visit.LabelChecker;
import polyglot.ast.Node;
import polyglot.types.SemanticException;

public class JLiftLocalExt extends JifLocalExt {

	public JLiftLocalExt(ToJavaExt toJava) {
		super(toJava);
	}

	@Override
	public Node labelCheck(LabelChecker lc) throws SemanticException {
		// TODO Auto-generated method stub
		return super.labelCheck(lc);
	}

}
