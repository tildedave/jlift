package jlift.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import polyglot.main.Report;
import polyglot.util.InternalCompilerError;

public class MaxFlowGraph {

	protected SparseTwoDimensionalArray capacityWeight;
	protected SparseTwoDimensionalArray flowWeight;
	protected static int currentNodeNumber = 0;
	protected MaxFlowNode secretSource;
	protected MaxFlowNode publicSink;
	protected static Map<Integer, MaxFlowNode> numberToNodeMap = new HashMap<Integer, MaxFlowNode>();

	public static class MaxFlowNode {
		Integer nodeNumber;
		String nodeString;
		Object nodeObject;
		Map<MaxFlowNode, Integer> outgoingEdges;
		Map<MaxFlowNode, Integer> incomingEdges;

		public void addFlowToNode(MaxFlowNode destNode, int bandwidth) {
			if (destNode == null)
				throw new InternalCompilerError("can't add null edge");
			outgoingEdges.put(destNode, bandwidth);
			destNode.incomingEdges.put(this, bandwidth);
		}

		public MaxFlowNode(String nodeString, Object nodeObject) {
			super();
			this.nodeNumber = currentNodeNumber;
			this.nodeObject = nodeObject;
			this.nodeString = nodeString;

			numberToNodeMap.put(this.nodeNumber, this);
			currentNodeNumber++;

			this.outgoingEdges = new HashMap<MaxFlowNode, Integer>();
			this.incomingEdges = new HashMap<MaxFlowNode, Integer>();
		}

		@Override
		public String toString() {
			return "[" + nodeString + "]"; // + ":" + nodeEdges;
		}

		public Object getNodeObject() {
			return nodeObject;
		}

		public Map<MaxFlowNode, Integer> getOutgoingEdges() {
			return outgoingEdges;
		}

		public Map<MaxFlowNode, Integer> getIncomingEdges() {
			return incomingEdges;
		}
	}

	private final Set<MaxFlowNode> nodes;

	protected MaxFlowGraph(MaxFlowNode secretSource, MaxFlowNode publicSink,
			Set<MaxFlowNode> nodes) {
		super();
		this.secretSource = secretSource;
		this.publicSink = publicSink;
		this.nodes = nodes;
		nodes.add(secretSource);
		nodes.add(publicSink);
	}

	public MaxFlowGraph() {
		this(new MaxFlowNode("SECRET", null), new MaxFlowNode("PUBLIC", null), new LinkedHashSet<MaxFlowNode>());
	}

	public void addNode(MaxFlowNode originalNode) {
		this.nodes.add(originalNode);
	}

	public String toDotString() {
		Map<MaxFlowNode, Integer> nodeToNodeNumberMap = new HashMap<MaxFlowNode, Integer>();
		int currentInt = 0;

		StringBuffer dotString = new StringBuffer();

		dotString.append("digraph G {\n");

		for(MaxFlowNode mfn : this.nodes) {
			currentInt++;
			dotString.append("\tnode" + currentInt + " [shape=rectangle,label=\"" + mfn.nodeString.replace("\"", "\\\"") + "\"];\n");
			nodeToNodeNumberMap.put(mfn, currentInt);
		}

		for(MaxFlowNode mfn : this.nodes) {
			Integer fromNumber = nodeToNodeNumberMap.get(mfn);
			for(Map.Entry<MaxFlowNode,Integer> connectingNode : mfn.outgoingEdges.entrySet()) {
				Integer toNumber = nodeToNodeNumberMap.get(connectingNode.getKey());
				if (cut.contains(new Triple<MaxFlowNode, MaxFlowNode, Integer>(mfn,connectingNode.getKey(), connectingNode.getValue())))
					dotString.append("\tnode" + fromNumber + " -> node" + toNumber + " [label=\"(" + connectingNode.getValue().toString().replace("\"", "\\\"") + ")\",color=red];\n");
				else
					dotString.append("\tnode" + fromNumber + " -> node" + toNumber + " [label=\"(" + connectingNode.getValue().toString().replace("\"", "\\\"") + ")\"];\n");
			}
		}

		dotString.append("}\n");

		return dotString.toString();
	}

	public void initializeFlowWeights() {
		this.capacityWeight = new SparseTwoDimensionalArray();
		for(MaxFlowNode mfn : this.nodes) {
			for(Map.Entry<MaxFlowNode, Integer> connectingNode : mfn.outgoingEdges.entrySet()) {
				this.capacityWeight.put(mfn.nodeNumber,
						connectingNode.getKey().nodeNumber,
						connectingNode.getValue());
			}
		}
	}

	enum NodeColor {
		/**
		 * White = unvisited node
		 */
		White,
		/**
		 * Gray = node that is on the queue
		 */
		Gray,
		/**
		 * Black = visited node
		 */
		Black
	}

	Map<MaxFlowNode, NodeColor> colorForNode = new HashMap<MaxFlowNode, NodeColor>();
	Map<MaxFlowNode, Integer> depthForNode = new HashMap<MaxFlowNode, Integer>();
	Map<MaxFlowNode, Integer> finishingTimeForNode = new HashMap<MaxFlowNode, Integer>();
	Map<MaxFlowNode, MaxFlowNode> predecessorForNode = new HashMap<MaxFlowNode, MaxFlowNode>();
	int dfsTime = 0;
	private final HashSet<Triple<MaxFlowNode, MaxFlowNode, Integer>> cut = new HashSet<Triple<MaxFlowNode,MaxFlowNode, Integer>>();

	public void resetSearchStructures() {
		this.colorForNode = new HashMap<MaxFlowNode, NodeColor>();
		this.depthForNode = new HashMap<MaxFlowNode, Integer>();
		this.finishingTimeForNode = new HashMap<MaxFlowNode, Integer>();
		this.predecessorForNode = new HashMap<MaxFlowNode, MaxFlowNode>();
		this.dfsTime = 0;
	}

	public void doBFSForFordFulkerson() {
		// does the BFS on the graph -- uses flowWeight for edges so
		// that we can iteratively run ford-fulkerson

		resetSearchStructures();

		for(MaxFlowNode mfn : this.nodes) {
			colorForNode.put(mfn, NodeColor.White);
			depthForNode.put(mfn, -1);
			predecessorForNode.put(mfn, null);
		}

		Queue<MaxFlowNode> bfsQueue = new LinkedList<MaxFlowNode>();
		// start with secretSource

		colorForNode.put(secretSource, NodeColor.Gray);
		depthForNode.put(secretSource, 0);
		predecessorForNode.put(secretSource, null);

		bfsQueue.add(secretSource);

		while (!bfsQueue.isEmpty()) {
			MaxFlowNode u = bfsQueue.peek();
			Set<Integer> inspectNumbers = new HashSet<Integer>();
			inspectNumbers.addAll(capacityWeight.get(u.nodeNumber).keySet());
			inspectNumbers.addAll(flowWeight.get(u.nodeNumber).keySet());

			for(int vNumber : inspectNumbers) {
				// of these, verify that the edge exists in the flowWeight graph
				MaxFlowNode v = numberToNodeMap.get(vNumber);
				int capacity = capacityWeight.get(u.nodeNumber,v.nodeNumber);
				int flow = flowWeight.get(u.nodeNumber, v.nodeNumber);
				if (capacity == -1 || (capacity - flow != 0)) {
					if (colorForNode.get(v).equals(NodeColor.White)) {
						colorForNode.put(v, NodeColor.Gray);
						depthForNode.put(v, depthForNode.get(u));
						predecessorForNode.put(v, u);
						bfsQueue.add(v);
					}
				}
			}
			bfsQueue.remove();
			colorForNode.put(u, NodeColor.Black);
		}

		// now we can get a path from publicSink to secretSource by using the
		// predecessorForNode map and repeatedly stepping back
	}

	public void doBFSFromSecret() {
		// does the BFS on the graph -- uses flowWeight for edges so
		// that we can iteratively run ford-fulkerson

		resetSearchStructures();

		for(MaxFlowNode mfn : this.nodes) {
			colorForNode.put(mfn, NodeColor.White);
			depthForNode.put(mfn, -1);
			predecessorForNode.put(mfn, null);
		}

		Queue<MaxFlowNode> bfsQueue = new LinkedList<MaxFlowNode>();
		// start with secretSource

		colorForNode.put(secretSource, NodeColor.Gray);
		depthForNode.put(secretSource, 0);
		predecessorForNode.put(secretSource, null);

		bfsQueue.add(secretSource);

		while (!bfsQueue.isEmpty()) {
			MaxFlowNode u = bfsQueue.peek();

			Set<MaxFlowNode> neighbors = new HashSet<MaxFlowNode>(u.getOutgoingEdges().keySet());

			for(MaxFlowNode v : neighbors) {
				if (colorForNode.get(v).equals(NodeColor.White)) {
					colorForNode.put(v, NodeColor.Gray);
					depthForNode.put(v, depthForNode.get(u));
					predecessorForNode.put(v, u);
					bfsQueue.add(v);
				}
			}
			bfsQueue.remove();
			colorForNode.put(u, NodeColor.Black);
		}
	}

	public void doReversedBFSFromPublic() {
		// does the BFS on the graph -- uses flowWeight for edges so
		// that we can iteratively run ford-fulkerson

		resetSearchStructures();

		for(MaxFlowNode mfn : this.nodes) {
			colorForNode.put(mfn, NodeColor.White);
			depthForNode.put(mfn, -1);
			predecessorForNode.put(mfn, null);
		}

		Queue<MaxFlowNode> bfsQueue = new LinkedList<MaxFlowNode>();
		// start with secretSource

		colorForNode.put(publicSink, NodeColor.Gray);
		depthForNode.put(publicSink, 0);
		predecessorForNode.put(publicSink, null);

		bfsQueue.add(publicSink);

		while (!bfsQueue.isEmpty()) {
			MaxFlowNode u = bfsQueue.peek();

			Set<MaxFlowNode> reversedNeighbors = new HashSet<MaxFlowNode>(u.getIncomingEdges().keySet());

			for(MaxFlowNode v : reversedNeighbors) {
				if (colorForNode.get(v).equals(NodeColor.White)) {
					colorForNode.put(v, NodeColor.Gray);
					depthForNode.put(v, depthForNode.get(u));
					predecessorForNode.put(v, u);
					bfsQueue.add(v);
				}
			}
			bfsQueue.remove();
			colorForNode.put(u, NodeColor.Black);
		}

		// now we can get a path from publicSink to secretSource by using the
		// predecessorForNode map and repeatedly stepping back
	}

	public void doDFSFromSecret() {
		resetSearchStructures();
		for(MaxFlowNode mfn : this.nodes) {
			colorForNode.put(mfn, NodeColor.White);
			depthForNode.put(mfn, -1);
			predecessorForNode.put(mfn, null);
		}

		this.dfsTime = 0;
		doDFSVisit(this.secretSource);
	}

	public void doReversedDFSFromPublic() {
		resetSearchStructures();
		for(MaxFlowNode mfn : this.nodes) {
			colorForNode.put(mfn, NodeColor.White);
			depthForNode.put(mfn, -1);
			predecessorForNode.put(mfn, null);
		}

		this.dfsTime = 0;
		doReversedDFSVisit(this.publicSink);
	}

	private void doDFSVisit(MaxFlowNode mfn) {
		colorForNode.put(mfn, NodeColor.Gray);
		depthForNode.put(mfn, dfsTime + 1);
		dfsTime = dfsTime + 1;

		for(Map.Entry<MaxFlowNode, Integer> e : mfn.outgoingEdges.entrySet()) {
			MaxFlowNode v = e.getKey();
			int capacity = capacityWeight.get(mfn.nodeNumber,v.nodeNumber);
			int flow = flowWeight.get(mfn.nodeNumber, v.nodeNumber);
			if (capacity == -1 || (capacity - flow != 0)) {
				if (colorForNode.get(v) == NodeColor.White) {
					predecessorForNode.put(v, mfn);
					doDFSVisit(v);
				}
			}
		}

		colorForNode.put(mfn, NodeColor.Black);
		finishingTimeForNode.put(mfn, dfsTime + 1);
		dfsTime = dfsTime + 1;
	}

	private void doReversedDFSVisit(MaxFlowNode mfn) {
		colorForNode.put(mfn, NodeColor.Gray);
		depthForNode.put(mfn, dfsTime + 1);
		dfsTime = dfsTime + 1;

		for(Map.Entry<MaxFlowNode, Integer> e : mfn.incomingEdges.entrySet()) {
			MaxFlowNode v = e.getKey();
			int capacity = capacityWeight.get(v.nodeNumber,mfn.nodeNumber);
			int flow = flowWeight.get(v.nodeNumber,mfn.nodeNumber);
			if (capacity == -1 || (capacity - flow != 0)) {
				if (colorForNode.get(v) == NodeColor.White) {
					predecessorForNode.put(v, mfn);
					doReversedDFSVisit(v);
				}
			}
		}

		colorForNode.put(mfn, NodeColor.Black);
		finishingTimeForNode.put(mfn, dfsTime + 1);
		dfsTime = dfsTime + 1;
	}

	public void computeMaxFlow() {
		this.flowWeight = new SparseTwoDimensionalArray();

		boolean shouldContinue = true;

		while(shouldContinue) {
			initializeFlowWeights();
			doBFSForFordFulkerson();
			if (this.depthForNode.get(this.publicSink) == -1) {
				if (shouldReport(2))
					report(2, "sink not reachable from source");
				shouldContinue = false;
			}
			else {
				//System.out.println("publicSink found during BFS");
				// public sink is reached.  get path from public sink backwards.
				LinkedList<MaxFlowNode> path = new LinkedList<MaxFlowNode>();
				int leastRemainingCapacitySeen = -1;
				MaxFlowNode currentNode = this.publicSink;
				MaxFlowNode predecessor;

				do {
					path.addFirst(currentNode);

					predecessor = this.predecessorForNode.get(currentNode);
					int capacity = this.capacityWeight.get(predecessor.nodeNumber,currentNode.nodeNumber);
					int currentFlow = this.flowWeight.get(predecessor.nodeNumber,currentNode.nodeNumber);

					int remainingCapacity = capacity - currentFlow;
					if (capacity != -1 &&
							(leastRemainingCapacitySeen == -1 ||
									remainingCapacity < leastRemainingCapacitySeen)) {
						leastRemainingCapacitySeen = remainingCapacity;
					}

					currentNode = predecessor;
				} while (predecessor != this.secretSource);

				path.addFirst(this.secretSource);

				if (shouldReport(2)) {
					report(2, "found augmenting path: ");
					for(MaxFlowNode mfn : path) {
						report(2, "\t" + mfn);
					}
				}

				// for each edge in the path
				Iterator<MaxFlowNode> itPath = path.iterator();
				MaxFlowNode u = itPath.next();
				MaxFlowNode v = null;
				while (itPath.hasNext()) {
					v = itPath.next();
					// check out path from u to v.
					int uNumber = u.nodeNumber;
					int vNumber = v.nodeNumber;

					this.flowWeight.put(uNumber,vNumber, this.flowWeight.get(uNumber,vNumber) + leastRemainingCapacitySeen);
					this.flowWeight.put(vNumber, uNumber,-leastRemainingCapacitySeen);

					u = v;
				}
			}
		}

//		System.out.println("max flow: " + totalFlow);
//
//		computeMinCut();
		//computeDeclassifierFlow();
	}

	private static boolean shouldReport(int i) {
		return Report.should_report("maxflow", i);
	}

	private static void report(int i, String string) {
		Report.report(i, string);
	}

	private Collection<Pair<MaxFlowNode, MaxFlowNode>> computeMinCut() {
		Collection<Pair<MaxFlowNode, MaxFlowNode>> returnCut = new LinkedList<Pair<MaxFlowNode,MaxFlowNode>>();

		resetSearchStructures();
		cut.clear();
		doDFSFromSecret();

		// find nodes that are visited, vs nodes that are not visited
		Collection<MaxFlowNode> nodesFromSecret = new LinkedHashSet<MaxFlowNode>();
		Collection<MaxFlowNode> unvisitedNodes = new LinkedHashSet<MaxFlowNode>();

		for(MaxFlowNode mfn : this.nodes) {
			if (colorForNode.get(mfn) == NodeColor.Black)
				nodesFromSecret.add(mfn);
			else
				unvisitedNodes.add(mfn);
		}

		resetSearchStructures();
		doReversedDFSFromPublic();

		Collection<MaxFlowNode> nodesFromPublic = new LinkedHashSet<MaxFlowNode>();
		for(MaxFlowNode mfn : this.nodes) {
			if (colorForNode.get(mfn) == NodeColor.Black)
				nodesFromPublic.add(mfn);
		}

		System.out.println("nodes from public: " + nodesFromPublic);

		int cutSize = 0;
		for(MaxFlowNode mfn : nodesFromSecret) {
			for(Map.Entry<MaxFlowNode, Integer> e : mfn.outgoingEdges.entrySet()) {
				if (!nodesFromSecret.contains(e.getKey()) &&
					 nodesFromPublic.contains(e.getKey())) {
					Triple<MaxFlowNode, MaxFlowNode, Integer> p = new Triple<MaxFlowNode, MaxFlowNode, Integer>(mfn, e.getKey(), e.getValue());
					cut.add(p);
					returnCut.add(new Pair<MaxFlowNode, MaxFlowNode>(mfn, e.getKey()));
					cutSize += e.getValue();
					//System.out.println("cut contains pair: " + p.toString());
				}
			}
		}

//		System.out.println("cut: ");
//		for (Triple<MaxFlowNode, MaxFlowNode, Integer> cutElement : cut) {
//			System.out.println("\t" + cutElement.first + " -> " + cutElement.second + " (" + cutElement.third + ")");
//		}

		int publicNumber = this.publicSink.nodeNumber;
		int totalFlow = 0;
		for(int i = 0; i < this.nodes.size(); ++i) {
			if (flowWeight.hasEntry(i, publicNumber))
				totalFlow += flowWeight.get(i, publicNumber);
		}

		if (shouldReport(1))
			report(1,"maximum flow: " + totalFlow);

		return returnCut;


	}

	public MaxFlowNode getSecretSource() {
		return secretSource;
	}

	public MaxFlowNode getPublicSink() {
		return publicSink;
	}

	public Collection<Pair<MaxFlowNode, MaxFlowNode>> getMinimumCut() {
		initializeFlowWeights();
		computeMaxFlow();

		return computeMinCut();
	}

	public Map<MaxFlowNode, Collection<MaxFlowNode>> computeDominators() {

		Collection<MaxFlowNode> nodesOnSecretToPublic = getNodesOnSecretToPublic();

		// Muchnick pg 182

		// IN Node: secretSource
		// OUT Node: publicSink


		Map<MaxFlowNode, Collection<MaxFlowNode>> domin = new HashMap<MaxFlowNode, Collection<MaxFlowNode>>();
		for(MaxFlowNode n : this.nodes) {
			if (n.equals(this.secretSource)) {
				domin.put(this.secretSource, Collections.singleton(this.secretSource));
			}
			else {
				domin.put(n, nodesOnSecretToPublic);
			}
		}

		boolean changed;

		do {
			changed = false;
			for (MaxFlowNode n : this.nodes) {
				if (!n.equals(this.secretSource)) {
					Collection<MaxFlowNode> T = new HashSet<MaxFlowNode>(this.nodes);

					HashSet<MaxFlowNode> incomingEdges = new HashSet<MaxFlowNode>(n.incomingEdges.keySet());
					//incomingEdges.retainAll(nodesOnSecretToPublic);

					for(MaxFlowNode p : incomingEdges) {
						T.retainAll(domin.get(p));
					}

					Collection<MaxFlowNode> D = new HashSet<MaxFlowNode>(T);
					D.add(n);

					if (!D.equals(domin.get(n))) {
						changed = true;
						domin.put(n, D);
					}
				}
			}
		} while (changed == true);

		return domin;
	}

	private Collection<MaxFlowNode> getNodesOnSecretToPublic() {
		Collection<MaxFlowNode> nodesOnSecretToPublic = new HashSet<MaxFlowNode>();
		Collection<MaxFlowNode> nodesOnPublicToSecret = new HashSet<MaxFlowNode>();

		resetSearchStructures();
		// do a DFS from Secret.  only consider incoming edges that are from edges
		// reachable by Secret.
		doBFSFromSecret();

		for(MaxFlowNode mfn : this.nodes) {
			if (this.colorForNode.get(mfn) == NodeColor.Black) {
				nodesOnSecretToPublic.add(mfn);
			}
		}

		resetSearchStructures();
		doReversedBFSFromPublic();

		for(MaxFlowNode mfn : this.nodes) {
			if (this.colorForNode.get(mfn) == NodeColor.Black) {
				nodesOnPublicToSecret.add(mfn);
			}
		}

		nodesOnSecretToPublic.retainAll(nodesOnPublicToSecret);
		return nodesOnSecretToPublic;
	}

	public void pruneNodesNotOnSecretToPublic() {
		Collection<MaxFlowNode> nodesOnSecretToPublic = getNodesOnSecretToPublic();

		for(MaxFlowNode n : this.nodes) {
			n.getIncomingEdges().keySet().retainAll(nodesOnSecretToPublic);
			n.getOutgoingEdges().keySet().retainAll(nodesOnSecretToPublic);
		}

		this.nodes.retainAll(nodesOnSecretToPublic);
	}

	public Map<MaxFlowNode, Collection<MaxFlowNode>> computePostDominators() {

		Collection<MaxFlowNode> nodesOnSecretToPublic = getNodesOnSecretToPublic();

		// reversed version of computeDominators...
		Map<MaxFlowNode, Collection<MaxFlowNode>> pdomin = new HashMap<MaxFlowNode, Collection<MaxFlowNode>>();
		for(MaxFlowNode n : this.nodes) {
			if (n.equals(this.publicSink)) {
				pdomin.put(this.publicSink, Collections.singleton(this.publicSink));
			}
			else {
				pdomin.put(n, nodesOnSecretToPublic);
			}
		}

		boolean changed;

		do {
			changed = false;
			for (MaxFlowNode n : this.nodes) {
				if (!n.equals(this.publicSink)) {
					Collection<MaxFlowNode> T = new HashSet<MaxFlowNode>(this.nodes);

					Set<MaxFlowNode> outgoingEdges = new HashSet<MaxFlowNode>(n.outgoingEdges.keySet());

					for(MaxFlowNode p : outgoingEdges) {
						T.retainAll(pdomin.get(p));
					}

					Collection<MaxFlowNode> D = new HashSet<MaxFlowNode>(T);
					D.add(n);

					if (!D.equals(pdomin.get(n))) {
						changed = true;
						pdomin.put(n, D);
					}
				}
			}
		} while (changed == true);

		return pdomin;

	}
}
