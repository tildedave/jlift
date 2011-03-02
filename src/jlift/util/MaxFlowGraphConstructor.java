/**
 *
 */
package jlift.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jif.types.LabelConstraint;
import jif.types.label.JoinLabel;
import jif.types.label.Label;
import jif.types.label.PairLabel;
import jlift.types.ExpressionFlowMap;
import jlift.types.JLiftLabelConstraint;
import jlift.types.label.JLiftVarLabel;
import jlift.util.MaxFlowGraph.MaxFlowNode;
import polyglot.ast.FieldAssign;

public class MaxFlowGraphConstructor {

	private final Collection<JLiftLabelConstraint> procConstraints;
	private final ExpressionFlowMap flowMap;
	private final MultiMap<JLiftVarLabel, JLiftVarLabel> mm;

	public MaxFlowGraphConstructor(
			Collection<JLiftLabelConstraint> procConstraints, ExpressionFlowMap flowMap,
			MultiMap<JLiftVarLabel, JLiftVarLabel> mm) {
		this.procConstraints = procConstraints;
		this.flowMap = flowMap;
		this.mm = mm;
	}

	public MaxFlowGraph constructMaxFlowGraph() {
		MaxFlowGraph maxFlowGraph = new MaxFlowGraph();
		// all variables have one label, unless your name matches NV[0-9]+
		Map<JLiftVarLabel, MaxFlowNode> varLabelToIncomingNodeMap = new HashMap<JLiftVarLabel, MaxFlowNode>();
		Map<JLiftVarLabel, MaxFlowNode> varLabelToOutgoingNodeMap = new HashMap<JLiftVarLabel, MaxFlowNode>();

		for (JLiftVarLabel v : mm.keySet()) {
			if (v.name().matches("NV[0-9]+")) {
				// two
				MaxFlowNode incomingNode = new MaxFlowNode(v.name()
						+ ":incoming\\n" + flowMap.getNodeForVariable(v), v);
				MaxFlowNode outgoingNode = new MaxFlowNode(v.name()
						+ ":outgoing\\n" + flowMap.getNodeForVariable(v), v);

				maxFlowGraph.addNode(incomingNode);
				maxFlowGraph.addNode(outgoingNode);

				// only possible cuts -- unless this is a field assign! (this is
				// a bit of a bug!)

				if (!(flowMap.getNodeForVariable(v) instanceof FieldAssign)) {
					incomingNode.addFlowToNode(outgoingNode, 1);
				} else {
					incomingNode.addFlowToNode(outgoingNode, -1);
				}

				varLabelToIncomingNodeMap.put(v, incomingNode);
				varLabelToOutgoingNodeMap.put(v, outgoingNode);
			} else {
				// one
				MaxFlowNode varNode = new MaxFlowNode(v.name(), v);

				maxFlowGraph.addNode(varNode);

				varLabelToIncomingNodeMap.put(v, varNode);
				varLabelToOutgoingNodeMap.put(v, varNode);
			}
		}

		for (Map.Entry<JLiftVarLabel, Collection<JLiftVarLabel>> e : mm
				.entrySet()) {
			MaxFlowNode outgoingV = varLabelToOutgoingNodeMap.get(e.getKey());
			for (JLiftVarLabel in : e.getValue()) {
				MaxFlowNode incomingV = varLabelToIncomingNodeMap.get(in);

				if (outgoingV != null && incomingV != null) {
					boolean addEdge = true;
					if (outgoingV.toString().startsWith("[NV")
							&& incomingV.toString().startsWith("[NV")) {
						int incomingColon = incomingV.toString().indexOf(":");
						int outgoingColon = outgoingV.toString().indexOf(":");

						if (incomingColon != -1
								&& outgoingColon != -1
								&& outgoingV.toString().substring(0,outgoingColon).equals(
										incomingV.toString().substring(0,incomingColon)))
							addEdge = false;
					}

					if (incomingV != outgoingV && addEdge)
						// can't cut these
						outgoingV.addFlowToNode(incomingV, -1);
				}
			}
		}

		for (JLiftLabelConstraint c : procConstraints) {
			// look at the LHS and the RHS. if there is a pairlabel on the
			// LHS, secret flows to that variable

			// if there is a bottom label on the RHS, variable flows to secret

			Label lhs = c.lhs();
			Label rhs = c.rhs();

			if (c.kind() == LabelConstraint.EQUAL) {
				addMaxFlowAffectForLhsAndRhs(maxFlowGraph,
						varLabelToIncomingNodeMap, varLabelToOutgoingNodeMap,
						c.rhs(), c.lhs());
				addMaxFlowAffectForLhsAndRhs(maxFlowGraph,
						varLabelToIncomingNodeMap, varLabelToOutgoingNodeMap,
						c.lhs(), c.rhs());
			} else if (c.kind() == JLiftLabelConstraint.DEFINE) {
				addMaxFlowAffectForLhsAndRhs(maxFlowGraph,
						varLabelToIncomingNodeMap, varLabelToOutgoingNodeMap,
						c.rhs(), c.lhs());
			} else {
				addMaxFlowAffectForLhsAndRhs(maxFlowGraph,
						varLabelToIncomingNodeMap, varLabelToOutgoingNodeMap,
						c.lhs(), c.rhs());
			}
		}
		return maxFlowGraph;
	}

	private void addMaxFlowAffectForLhsAndRhs(MaxFlowGraph maxFlowGraph,
			Map<JLiftVarLabel, MaxFlowNode> varLabelToIncomingNodeMap,
			Map<JLiftVarLabel, MaxFlowNode> varLabelToOutgoingNodeMap,
			Label lhs, Label rhs) {
		Set<JLiftVarLabel> lhsVars = lhs.variableComponents();
		Set<JLiftVarLabel> rhsVars = rhs.variableComponents();

		if (rhsVars.size() > 1)
			return;

		if (lhs instanceof PairLabel && isNotBottomConfidentiality(lhs)) {
			secretAffectsVariable(maxFlowGraph, varLabelToIncomingNodeMap,
					rhsVars);
		} else if (lhs instanceof JoinLabel) {
			Collection<Label> joinComponents = ((JoinLabel) lhs).joinComponents();
			for (Label l : joinComponents) {
				if (l instanceof PairLabel && isNotBottomConfidentiality(l)) {
					secretAffectsVariable(maxFlowGraph,
							varLabelToIncomingNodeMap, rhsVars);
				}
			}
		}

		if (rhs instanceof PairLabel
				&& (!isNotBottomConfidentiality(rhs))) {
			// everything in lhs flows to public sink
			variableAffectsPublic(maxFlowGraph, varLabelToOutgoingNodeMap,
					lhsVars);
		}
	}

	private boolean isNotBottomConfidentiality(Label lhs) {
		boolean retVal = (!lhs.confProjection().isBottomConfidentiality() && !lhs.toString().contains("Bob"));
		return retVal;
	}

	private void variableAffectsPublic(MaxFlowGraph maxFlowGraph,
			Map<JLiftVarLabel, MaxFlowNode> varLabelToOutgoingNodeMap,
			Set<JLiftVarLabel> lhsVars) {
		for (JLiftVarLabel lhsVar : lhsVars) {
			MaxFlowNode outgoingLhsVar = varLabelToOutgoingNodeMap.get(lhsVar);
			if (outgoingLhsVar != null) {
				outgoingLhsVar.addFlowToNode(maxFlowGraph.getPublicSink(), -1);
			}
		}
	}

	private void secretAffectsVariable(MaxFlowGraph maxFlowGraph,
			Map<JLiftVarLabel, MaxFlowNode> varLabelToIncomingNodeMap,
			Set<JLiftVarLabel> rhsVars) {
		for (JLiftVarLabel rhsVar : rhsVars) {
			// LHS affects RHS --> secret flows to variable

			MaxFlowNode incomingRhsVar = varLabelToIncomingNodeMap.get(rhsVar);
			if (incomingRhsVar != null) {
				maxFlowGraph.getSecretSource().addFlowToNode(incomingRhsVar, -1);
			}
		}
	}
}