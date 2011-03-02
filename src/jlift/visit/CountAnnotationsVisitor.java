package jlift.visit;

import jif.ast.DeclassifyExpr;
import jif.ast.LabelNode;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;

public class CountAnnotationsVisitor extends NodeVisitor {

	public static int globalCount = 0;

	public CountAnnotationsVisitor() {
	}

	@Override
	public Node leave(Node old, Node n, NodeVisitor v) {
		if (n instanceof LabelNode) {
			++globalCount;
			//Report.report(1,"annotation # " + globalCount + ": " + n + "@" + n.position());
		}
		if (n instanceof DeclassifyExpr) {
			++globalCount;
		}
		return super.leave(old, n, v);
	}

}
