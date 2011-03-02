package jlift.frontend;

import java.util.HashSet;
import java.util.Set;

import jlift.util.CallGraph;
import jlift.visit.NullPointerAnalysisVisitor;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.AbstractPass;
import polyglot.frontend.Job;
import polyglot.frontend.goals.Goal;
import polyglot.types.ProcedureInstance;
import polyglot.types.TypeSystem;

public class PreconditionPass extends AbstractPass {

	protected CallGraph theCallGraph;
	protected TypeSystem ts;
	protected NodeFactory nf;

	public static Set<Node> nodesToPreconditionAnalyze = new HashSet<Node>();

	public PreconditionPass(Goal goal, CallGraph callGraph, TypeSystem ts, NodeFactory nf) {
		super(goal);
		this.theCallGraph = callGraph;
		this.ts = ts;
		this.nf = nf;
	}

	@Override
	public boolean run() {
		//System.out.println("bork bork bork -- " + nodesToPreconditionAnalyze);
		for (Node n : nodesToPreconditionAnalyze) {
			preconditionAnalyze(n, theCallGraph.getProcedureInstanceForNode(n));
		}

		return true;
	}

	private void preconditionAnalyze(Node n, ProcedureInstance pi) {
		System.out.println(n + " -> " + pi);
		Job j = theCallGraph.getJob(pi);
		NullPointerAnalysisVisitor npav = new NullPointerAnalysisVisitor(j, ts, nf, theCallGraph, n, pi);

		j.ast().visit(npav);
	}
}
