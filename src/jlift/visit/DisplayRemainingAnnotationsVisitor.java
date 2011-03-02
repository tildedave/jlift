package jlift.visit;

import jif.ast.DeclassifyExpr;
import jif.ast.LabelNode;
import jif.ast.NewLabel;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorInfo;
import polyglot.util.ErrorQueue;
import polyglot.util.StdErrorQueue;
import polyglot.visit.ErrorHandlingVisitor;
import polyglot.visit.NodeVisitor;

public class DisplayRemainingAnnotationsVisitor extends ErrorHandlingVisitor {

	public static int globalCount = 0;
	protected ErrorQueue eq;


	public DisplayRemainingAnnotationsVisitor(Job job, TypeSystem ts,
			NodeFactory nf) {
		super(job, ts, nf);
		eq = new StdErrorQueue(System.err, 1000, "Annotations");
		System.out.println("display remaining annotations");
	}

	@Override
	public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
		if (n instanceof LabelNode) {
			if (parent instanceof NewLabel == false)
				eq.enqueue(new ErrorInfo(ErrorInfo.WARNING, "annotation", n.position()));
		}
		if (n instanceof DeclassifyExpr) {
			eq.enqueue(new ErrorInfo(ErrorInfo.WARNING, "annotation", n.position()));
		}

		return n;
	}

}
