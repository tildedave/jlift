package jlift.visit;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import jlift.util.CallGraph;
import polyglot.ast.Call;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.FieldAssign;
import polyglot.ast.If;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.types.ProcedureInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ErrorHandlingVisitor;
import polyglot.visit.FlowGraph;
import polyglot.visit.FlowGraph.Peer;

public class NullPointerAnalysisVisitor extends ErrorHandlingVisitor {

	public abstract class Precondition {
		@Override
		public abstract String toString();
	}

	public class StrPrecondition extends Precondition {
		protected String str;

		public StrPrecondition(String str) {
			super();
			this.str = str;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	public class ImpliesPrecondition extends Precondition {
		protected String precond;
		protected Collection<Precondition> consequences;

		public ImpliesPrecondition(String precond,
				Collection<Precondition> consequences) {
			super();
			this.precond = precond;
			this.consequences = consequences;
		}

		@Override
		public String toString() {
			return precond + " => (" + consequences + ")";
		}
	}

	class SubstitutionPrecondition extends Precondition {

		protected String variable;
		protected String expressionStr;
		protected Precondition pc;

		public SubstitutionPrecondition(String variable, String expressionStr,
				Precondition pc) {
			super();
			this.variable = variable;
			this.expressionStr = expressionStr;
			this.pc = pc;
		}

		@Override
		public String toString() {
			//return "(" + pc + ")[" + expressionStr + "/" + variable + "]";
			return pc.toString().replace(variable.toString(), expressionStr.toString());
		}

	}

	private final CallGraph theCallGraph;
	protected final ProcedureInstance pi;
	protected final Node nodeToFind;

	public NullPointerAnalysisVisitor(Job job, TypeSystem ts, NodeFactory nf, CallGraph theCallGraph, Node n, ProcedureInstance pi) {
		super(job, ts, nf);
		this.theCallGraph = theCallGraph;
		this.pi = pi;
		this.nodeToFind = n;
	}

	@Override
	protected Node leaveCall(Node n) throws SemanticException {
		if (isNodeToFind(n)) {
			FlowGraph g = theCallGraph.getFlowGraph(pi);
			Peer p = g.peer((Term) n, Term.ENTRY);

			System.out.println(g.finishPeers());

			Collection<Precondition> pc = new LinkedHashSet<Precondition>();
			pc.add(new StrPrecondition(((Call) n).target() + " == null"));

			System.out.println(generatePreconditionsFromPeer(g, p, new HashSet<Peer>(), pc));
		}
		return n;
	}

	private boolean isNodeToFind(Node n) {
//		return n instanceof Call && n.toString().contains("myField.getClass()");
		return n.toString().equals(nodeToFind.toString());
	}

	private Collection<Precondition> generatePreconditionsFromPeer(FlowGraph g, Peer p, Collection<Peer> seenPeers, Collection<Precondition> preconditions) {
		Collection<Precondition> returnPreconditions = new LinkedHashSet<Precondition>(preconditions);
		System.out.println(p);
		seenPeers.add(p);

		if (!isEntry(g, p) && p.node() != null) {
			Collection<FlowGraph.Edge> preds = p.succs();
//			System.out.println("predecessors of " + p + ": " + preds.size());
//			System.out.println("\t" + preds);
			if (p.node() instanceof Stmt) {
//				System.out.println("statement: " + p.node());
				Stmt s = (Stmt) p.node();
				if (s instanceof If) {
					If i = (If) s;
					Expr cond = i.cond();
					Stmt ifBlock = i.consequent();
					Stmt elseBlock = i.alternative();

					Peer ifPeer = g.peer(ifBlock, Term.ENTRY);
					Peer elsePeer = null;
					if (elseBlock != null)
						elsePeer = g.peer(elseBlock, Term.ENTRY);

					Collection<Precondition> ifPreconditions = generatePreconditionsFromPeer(g, ifPeer, seenPeers, returnPreconditions);
					Collection<Precondition> elsePreconditions = null;
					if (elsePeer != null)
						elsePreconditions = generatePreconditionsFromPeer(g, ifPeer, seenPeers, returnPreconditions);

					returnPreconditions = new LinkedHashSet<Precondition>();
					returnPreconditions.add(new ImpliesPrecondition(cond.toString(), ifPreconditions));
					if (elsePreconditions != null)
						returnPreconditions.add(new ImpliesPrecondition("~" + cond.toString(), elsePreconditions));
				}
				else {
					if (s instanceof Eval) {
						Eval ev = (Eval) s;
						Expr exp = ev.expr();
						if (exp instanceof FieldAssign) {
							FieldAssign fa = (FieldAssign) exp;
							returnPreconditions = substituteInPreconditions(returnPreconditions, fa.left(), fa.right());
						}
					}
				}
			}

			if (preds.size() > 1) {
				System.out.println(p + " -> " + preds);
			}

			for (FlowGraph.Edge predEdge : preds) {
				Peer target = predEdge.getTarget();
				if (!seenPeers.contains(target)) {
					returnPreconditions = generatePreconditionsFromPeer(g, target, seenPeers, returnPreconditions);
				}
			}

		}

		return returnPreconditions;
	}

	private Collection<Precondition> substituteInPreconditions(
			Collection<Precondition> pcs, Expr left, Expr right) {
		Collection<Precondition> returnPC = new LinkedHashSet<Precondition>();
		for(Precondition pc :  pcs) {
			returnPC.add(new SubstitutionPrecondition(left.toString(), right.toString(), pc));
		}

		return returnPC;
	}

	private boolean isEntry(FlowGraph g, Peer p) {
		for (Peer ep : (Collection<Peer>) g.entryPeers()) {
			if (ep.toString().equals(p.toString()))
				return true;
		}
		return false;
	}

}
