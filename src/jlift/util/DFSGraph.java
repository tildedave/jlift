/*
 * File          : DFSGraph.java
 * Project       : jlift
 * Description   : Graph of a datatype, can generate SCCs.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:56:04 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import jlift.util.CallGraph.NodeColor;

public class DFSGraph<T> {

	private final MultiMap<T, T> graph;
	private final MultiMap<T, T> reverseGraph;
	private final List<Collection<T>> stronglyConnectedComponents;
	private final Map<T, Collection<T>> nodeToSCCMap;
	//private Map<Collection<T>, Collection<Collection<T>>> directReachSet;
	//private MultiMap<Collection<T>, Collection<T>> directReachSet;
	private final Map<Collection<T>, Collection<T>> sccReachCacheMap;
	private final Map<Collection<T>, Collection<Collection<T>>> sccGraph;
	private final CacheCallBack<T> callBackObject;

	public DFSGraph(MultiMap<T, T> directVariableAffectMap, CacheCallBack<T> callBackObject) {
		this.graph = directVariableAffectMap;
		// reverse graph
		this.reverseGraph = getReverseMap(directVariableAffectMap);

		this.stronglyConnectedComponents = new LinkedList<Collection<T>>();
		this.nodeToSCCMap = new HashMap<T, Collection<T>>();
		//this.directReachSet = new HashMap<Collection<T>, Collection<Collection<T>>>();
		//this.directReachSet = new MultiMap<Collection<T>, Collection<T>>();
		this.sccReachCacheMap = new HashMap<Collection<T>, Collection<T>>();
		this.sccGraph = new HashMap<Collection<T>, Collection<Collection<T>>>();
		generateStronglyConnectedComponents();
		buildSCCGraph();

		this.callBackObject = callBackObject;
//		for(Entry<T,Collection<T>> e : graph.entrySet()) {
//			System.err.println(e);
//		}
//		System.err.println("----");
//		for(Entry<T,Collection<T>> e : reverseGraph.entrySet()) {
//			System.err.println(e);
//		}
//		for(Collection<T> e : stronglyConnectedComponents) {
//			System.err.println(e);
//		}
//		buildDirectReachSets();
	}

	private void buildSCCGraph() {
//		System.err.println("building scc graph: " + stronglyConnectedComponents.size() + " SCCs");
		int numSccs = 0;

		for(Collection<T> scc : stronglyConnectedComponents) {
//			if (numSccs % 1000 == 0)
//				System.err.println("processed " + numSccs);
			Collection<Collection<T>> successorSCCs = new HashSet<Collection<T>>();
			for(T node : scc) {
				Collection<T> successors = graph.get(node);
				//System.err.println("successors of " + node + " = " + successors);
//				System.err.println("scc size: " + scc.size());
//				System.err.println("successors size: " + successors.size());

				for(T succ : successors) {
					Collection<T> succSCC = nodeToSCCMap.get(succ);
					if (succSCC != null && !succSCC.equals(scc))
						successorSCCs.add(succSCC);
				}
			}

			sccGraph.put(scc, successorSCCs);

			numSccs++;
		}
	}

	public Collection<T> getReachSet(T n) {
		Collection<T> nSCC = nodeToSCCMap.get(n);
		//System.err.println("n: " + n + " has SCC " + nSCC);
		Collection<T> reachSet = getReachSetForSCC(nSCC);
		return reachSet;
	}

	private Collection<T> getReachSetForSCC(Collection<T> nSCC) {
		if (sccReachCacheMap.containsKey(nSCC))
			return sccReachCacheMap.get(nSCC);

		Collection<T> reachSet = new LinkedHashSet<T>();

		// add everything in n's SCC
		// recurse on the direct successors of n's SCC
		if (nSCC != null) {
			reachSet.addAll(nSCC);
			Collection<Collection<T>> succSCCs = sccGraph.get(nSCC);
			if (succSCCs != null) {
				for(Collection<T> scc : succSCCs) {
//					if (scc.size() > 1) {
//					System.err.println("scc is > size 1");
//					System.err.println(scc);
//					}
					reachSet.addAll(getReachSetForSCC(scc));
				}
			}
			else
			{
				//System.err.println("\tno successor sccs");
			}



			sccReachCacheMap.put(nSCC, reachSet);
			if (callBackObject != null)
				this.callBackObject.registerCacheUpdate(nSCC, reachSet);
		}

		return reachSet;
	}

	public List<Collection<T>> getStronglyConnectedComponents() {
		generateStronglyConnectedComponents();
		return stronglyConnectedComponents;
	}

	public Collection<T> getReachSetForElement(T element) {
		Collection<T> returnList = new LinkedHashSet<T>();
		// add all of element's of T's strongly connected components... and all of the elements of
		// anything that T can reach, etc... this might be inefficient (ugh)

		return returnList;
	}

	public void generateStronglyConnectedComponents()
	{
		if (stronglyConnectedComponents.size() > 0)
			// already did this
			return;

//		System.err.println("generating strongly connected components");

//		System.err.println("callGraphMap: " + callGraphMap);

//		System.err.println("starting DFS #1");
		// generate finishing times for each node via DFS
		final Map<T, Integer> finishingTimes = getFinishingTimes(graph);

//		System.err.println("finishingTimes: " + finishingTimes);

//		System.err.println("reversedMap: " + reversedMap);

		// for DFS take 2, we visit the graph in reverse order
		List<T> traverseOrder = new LinkedList<T>(reverseGraph.keySet());
		Collections.sort(traverseOrder,new Comparator<T>() {
			public int compare(T n1, T n2)
			{
				// n1 comes first if it took longer than n2 to finish in the original
				// graph
				return finishingTimes.get(n2).compareTo(finishingTimes.get(n1));
			}
		}
		);

		traverseOrder.addAll(reverseGraph.keySet());
//		System.err.println("traverseOrder: " + traverseOrder);

//		System.err.println("starting DFS #2");

		depthFirstSearch(reverseGraph, null, null, null, traverseOrder.iterator(), this.stronglyConnectedComponents);
		//System.err.println("strongly connected components: " + this.stronglyConnectedComponents);

		//System.err.println("graph: " + graph);

		for(Collection<T> scc : stronglyConnectedComponents) {
			for(T node : scc) {
//				System.err.println("node: " + node + " in SCC " + scc);
				nodeToSCCMap.put(node, scc);
			}
			//System.err.println("scc: " + scc);
		}

	}

	private MultiMap<T, T> getReverseMap(MultiMap<T, T> multiMap) {

		MultiMap<T, T> reversedMap = new MultiMap<T, T>();

//		for(T n : multiMap.keySet())
//			reversedMap.put(n, new HashSet<T>());

		for(Entry<T, Collection<T>> e : multiMap.entrySet())
		{
			T caller = e.getKey();
			for(T called : e.getValue())
			{
				if (multiMap.containsKey(called))
				{
					// add the current key to the hashset for called
//					Collection<T> currentReversedCalls = reversedMap.get(called);
//					currentReversedCalls.add(e.getKey());
					reversedMap.put(called, caller);
				}
			}
		}

		return reversedMap;
	}

	private Map<T, Integer> getFinishingTimes(MultiMap<T, T> graph)
	{
		HashMap<T, Integer> finishingTimes = new HashMap<T, Integer>();
		depthFirstSearch(graph, null, null, finishingTimes, null, null);

		return finishingTimes;
	}

	protected void depthFirstSearch(MultiMap<T, T> dfsGraph, Map<T, T> predecessors,
			Map<T, Integer> discoveryTimes,
			Map<T, Integer> finishingTimes,
			Iterator<T> definedVisitIterator,
			List<Collection<T>> list)
	{
		Map<T, NodeColor> nodeColors = new HashMap<T, NodeColor>();
		for(T n : dfsGraph.keySet())
		{
			nodeColors.put(n, NodeColor.WHITE);
			if (predecessors != null)
				predecessors.put(n, null);
		}

		int time = 0;

		Iterator<T> visitNodeIterator = definedVisitIterator;
		if (visitNodeIterator == null)
			visitNodeIterator = dfsGraph.keySet().iterator();

		while(visitNodeIterator.hasNext())
		{
			T node = visitNodeIterator.next();

			HashSet<T> currentScc = null;
			if (list != null)
				currentScc = new HashSet<T>();

			if (nodeColors.get(node).equals(NodeColor.WHITE)) {
				time = depthFirstSearch_visitNode(dfsGraph, node, nodeColors, predecessors, time, discoveryTimes, finishingTimes, currentScc);
			}

			if (list != null && currentScc.size() > 0) {
				list.add(currentScc);
			}
		}
	}

	protected int depthFirstSearch_visitNode(MultiMap<T, T> dfsGraph,
			T node,
			Map<T, NodeColor> nodeColors,
			Map<T, T> predecessors,
			int time,
			Map<T, Integer> discoveryTimes,
			Map<T, Integer> finishingTimes,
			HashSet<T> currentScc)
	{
		if (currentScc != null)
			currentScc.add(node);

		nodeColors.put(node, NodeColor.GRAY);

		if (discoveryTimes != null)
			discoveryTimes.put(node, ++time);

		Collection<T> neighbors = dfsGraph.get(node);

		for(T n : neighbors)
		{
			if (dfsGraph.containsKey(n) && nodeColors.get(n).equals(NodeColor.WHITE)) {
				if (predecessors != null)
					predecessors.put(n, node);

				time = depthFirstSearch_visitNode(dfsGraph, n, nodeColors, predecessors, time, discoveryTimes, finishingTimes, currentScc);
			}
		}

		nodeColors.put(node, NodeColor.BLACK);

		if (finishingTimes != null) {
			finishingTimes.put(node, ++time);
		}

		return time;
	}

	public Collection<T> getSCCForNode(T v) {
		return nodeToSCCMap.get(v);
	}

	public String toDotString() {
		// turns the graph into a dot file
		StringBuffer sb = new StringBuffer();
		Map<Collection<T>, String> sccToNameMap = new HashMap<Collection<T>, String>();
		int var = 0;

		sb.append("digraph G {\n");

		for(Collection<T> scc : stronglyConnectedComponents) {
			sccToNameMap.put(scc, "scc" + var);
			sb.append("\tscc" + var + " [shape=box,label=\"");
			++var;
			Iterator<T> itScc = scc.iterator();
			while(itScc.hasNext()) {
				T mem = itScc.next();
				sb.append(mem);
				if (itScc.hasNext())
					sb.append(",");
			}
			sb.append("\"];\n");
		}

		// now do the links from the graph
		for(Entry<Collection<T>, Collection<Collection<T>>> e : sccGraph.entrySet()) {
			String nameForScc = sccToNameMap.get(e.getKey());
			for(Collection<T> connectedScc : e.getValue()) {
				String destScc = sccToNameMap.get(connectedScc);
				sb.append("\t" + nameForScc + " -> " + destScc + ";\n");
			}
		}

		sb.append("}\n");

		return sb.toString();
	}

	public MultiMap<T, T> getGraph() {
		return graph;
	}

	public boolean isReachable(T start, T find) {
		Queue<T> toVisit = new LinkedList<T>();
		Set<T> visited = new HashSet<T>();
		Set<T> inQueue = new HashSet<T>();
		toVisit.add(start);

		while(!toVisit.isEmpty()) {
			T v = toVisit.remove();
			visited.add(v);
			inQueue.remove(v);

			if (this.graph.get(v) != null) {
				for (T i : this.graph.get(v)) {
					if (!visited.contains(i) && !inQueue.contains(i)) {
						inQueue.add(i);
						toVisit.add(i);
					}
				}
			}
		}

		return visited.contains(find);
	}
}
