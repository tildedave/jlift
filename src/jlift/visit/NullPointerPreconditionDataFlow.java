package jlift.visit;

import java.util.List;
import java.util.Map;
import java.util.Set;

import polyglot.ast.NodeFactory;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.DataFlow;
import polyglot.visit.FlowGraph;

public class NullPointerPreconditionDataFlow extends DataFlow {

	public NullPointerPreconditionDataFlow(Job job, TypeSystem ts,
			NodeFactory nf) {
		super(job, ts, nf, false, true);
	}

	@Override
	protected void check(FlowGraph graph, Term n, boolean entry, Item inItem,
			Map outItems) throws SemanticException {
		System.out.println("check: " + n);
		System.out.println("\tin: " + inItem);
		System.out.println("\tout: " + outItems);
	}

	@Override
	protected Item confluence(List items, Term node, boolean entry,
			FlowGraph graph) {
		System.out.println("confluence: " + node);
		System.out.println("\t" + items);
		return new NullPointerItem(node);
	}

	@Override
	protected Map flow(Item in, FlowGraph graph, Term n, boolean entry,
			Set edgeKeys) {
		System.out.println("flow: " + n);

		return itemToMap(new NullPointerItem(n), edgeKeys);
	}

	@Override
	protected Item createInitialItem(FlowGraph graph, Term node, boolean entry) {
		return new NullPointerItem(node);
	}

	public static class NullPointerItem extends DataFlow.Item {

		protected Term t;

		public NullPointerItem(Term t) {
			this.t = t;
		}

		@Override
		public boolean equals(Object i) {
			return false;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public String toString() {
			return t.toString();
		}
	}
}
