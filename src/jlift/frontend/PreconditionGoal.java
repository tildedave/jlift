package jlift.frontend;

import jlift.util.CallGraph;
import polyglot.ast.NodeFactory;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.AbstractGoal;
import polyglot.types.TypeSystem;

public class PreconditionGoal extends AbstractGoal {

	protected CallGraph theCallGraph;
	protected TypeSystem theTypeSystem;
	protected NodeFactory theNodeFactory;

	public PreconditionGoal(Job job, CallGraph theCallGraph,
			TypeSystem theTypeSystem, NodeFactory theNodeFactory) {
		super(job);
		this.theCallGraph = theCallGraph;
		this.theTypeSystem = theTypeSystem;
		this.theNodeFactory = theNodeFactory;
	}

	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		return new PreconditionPass(this, theCallGraph, theTypeSystem, theNodeFactory);
	}

}
