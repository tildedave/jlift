/*
 * File          : CallGraph.java
 * Project       : jlift
 * Description   : A graph of procedures and who calls who.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:47:27 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

// TODO: refactor this to use DFSGraph
// TODO: clean up this code more

package jlift.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
import java.util.Set;
import java.util.Map.Entry;

import polyglot.ast.Node;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.ConstructorInstance;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.ProcedureInstance;
import polyglot.util.Position;
import polyglot.visit.FlowGraph;

public class CallGraph
{
	protected HashMap<CallGraphNode,HashSet<CallGraphNode>> callGraphMap;
	protected HashMap<ProcedureInstance,Job> procedureToJobMap;
	protected HashSet<ParsedClassType> classNames;
	protected HashSet<ProcedureInstance> allProcedures;

	protected HashMap<ProcedureInstance,HashSet<ProcedureInstance>> callerMap;
	protected HashMap<ProcedureInstance,HashSet<ProcedureInstance>> calleeMap;

	protected LinkedList<HashSet<CallGraphNode>> stronglyConnectedComponents;

	protected Map<ProcedureInstance, FlowGraph> piToCFG;
	protected Map<Position, ProcedureInstance> nodeToPi;

	public CallGraph()
	{
		this.callGraphMap = new HashMap<CallGraphNode,HashSet<CallGraphNode>>();
		this.procedureToJobMap = new HashMap<ProcedureInstance, Job>();
		this.classNames = new HashSet<ParsedClassType>();
		this.stronglyConnectedComponents = new LinkedList<HashSet<CallGraphNode>>();
		this.listForSCCIterator = null; // will be generated as necessary
		this.allProcedures = new HashSet<ProcedureInstance>();

		this.callerMap = new HashMap<ProcedureInstance, HashSet<ProcedureInstance>>();
		this.calleeMap = new HashMap<ProcedureInstance, HashSet<ProcedureInstance>>();

		this.piToCFG = new HashMap<ProcedureInstance, FlowGraph>();
		this.nodeToPi = new HashMap<Position, ProcedureInstance>();
	}

	class CallGraphNode
	{
		ProcedureInstance jpi;

		public CallGraphNode(ProcedureInstance jpi) { this.jpi = jpi; }
		public ProcedureInstance getProcedureInstance() { return jpi; }

		@Override
		public boolean equals(Object o)
		{
			if (o instanceof CallGraphNode)
				return ((CallGraphNode) o).getProcedureInstance().equals(jpi);
			return false;
		}

		@Override
		public int hashCode()
		{
			return jpi.hashCode();
		}

		@Override
		public String toString()
		{
			return jpi.signature();
		}
	}

	public void addNewClass(ParsedClassType ct)
	{
		if (Report.should_report("callgraph",3))
			Report.report(3,"call graph: adding new class " + ct);

		classNames.add(ct);
	}

	public void addNewCall(ProcedureInstance currentProcedureInstance, ProcedureInstance mi)
	{
		if (Report.should_report("callgraph",3))
			Report.report(3,"call graph: adding new call of " + mi + " (" + ((MemberInstance) mi).container() + ")" +
					" from " +
					currentProcedureInstance + "@" + ((MemberInstance) currentProcedureInstance).container());

		CallGraphNode cn = getNodeForProcedureInstance(currentProcedureInstance);
		HashSet<CallGraphNode> currentCalls = callGraphMap.get(cn);

		CallGraphNode cm = getNodeForProcedureInstance(mi);

		if (cm == null)
			cm = new CallGraphNode(mi);

		addCallerAndCallee(currentProcedureInstance, mi);

		currentCalls.add(cm);

		callGraphMap.put(cn,currentCalls);
		if (!allProcedures.contains(mi))
			allProcedures.add(mi);
	}

	private void addCallerAndCallee(ProcedureInstance currentMethod, ProcedureInstance calledMethod) {
		// add calledMethod to currentMethod in the callermap
		HashSet<ProcedureInstance> currentCallees = calleeMap.get(currentMethod);
		if (currentCallees == null)
			currentCallees = new HashSet<ProcedureInstance>();
		currentCallees.add(calledMethod);
		calleeMap.put(currentMethod, currentCallees);

		// add currentMethod to calledMethod in the calleemap
		HashSet<ProcedureInstance> currentCallers = callerMap.get(calledMethod);
		if (currentCallers == null)
			currentCallers = new HashSet<ProcedureInstance>();
		currentCallers.add(currentMethod);
		callerMap.put(calledMethod, currentCallers);
	}

	public void addNewMethod(ProcedureInstance method, Job job)
	{
		if (Report.should_report("callgraph",3))
			Report.report(3,"call graph: adding new method " + method + " (" + method.position() + ")");

		callGraphMap.put(new CallGraphNode(method),new HashSet<CallGraphNode>());
		procedureToJobMap.put(method, job);
		if (!allProcedures.contains(method))
			allProcedures.add(method);
	}

	protected CallGraphNode getNodeForProcedureInstance(ProcedureInstance jpi)
	{
		for(CallGraphNode cn : callGraphMap.keySet()) {
			if (cn.getProcedureInstance().equals(jpi))
				return cn;
		}

		return null;
	}

	protected boolean nodeCollectionHasProcedureInstance(Collection<CallGraphNode> nodes, ProcedureInstance jpi)
	{
		for(CallGraphNode cn : nodes)
		{
			if (cn.getProcedureInstance().equals(jpi))
				return true;
		}

		return false;
	}

	private LinkedList<HashSet<ProcedureInstance>> listForSCCIterator;

	public class CallGraphSCCIterator implements
	Iterator<HashSet<ProcedureInstance>> {

		// generate the order once and then cache it for future calls to next()
		Iterator<HashSet<ProcedureInstance>> backendIterator;

		private HashSet<HashSet<ProcedureInstance>> sccProcedures;
		private final HashSet<HashSet<ProcedureInstance>> visitedComponents;
		private final HashSet<HashSet<ProcedureInstance>> unvisitedComponents;
		private final HashSet<ProcedureInstance> visitedProcedures;

		public CallGraphSCCIterator() {
			generateStronglyConnectedComponents();

			this.visitedComponents = new HashSet<HashSet<ProcedureInstance>>();
			this.unvisitedComponents = new HashSet<HashSet<ProcedureInstance>>();
			this.visitedProcedures = new HashSet<ProcedureInstance>();

			this.backendIterator = null;
			setupBackendIterator();
		}

		public boolean hasNext() {
			if (backendIterator == null)
				setupBackendIterator();

			return backendIterator.hasNext();
		}

		private void setupBackendIterator() {

			if (listForSCCIterator == null) {
				generateListForSCCIterator();
			}

			backendIterator = listForSCCIterator.iterator();
		}

		private void generateListForSCCIterator() {
			visitedComponents.clear();
			unvisitedComponents.clear();
			visitedProcedures.clear();
			sccProcedures = new HashSet<HashSet<ProcedureInstance>>();
			for(HashSet<CallGraphNode> cg : stronglyConnectedComponents) {
				HashSet<ProcedureInstance> procSet = procedureSetFromNodeSet(cg);
				sccProcedures.add(procSet);
			}
			unvisitedComponents.addAll(sccProcedures);

//			System.err.println("generating list to iterate over");

//			System.err.println(callerMap);
//			System.err.println(calleeMap);

			listForSCCIterator = new LinkedList<HashSet<ProcedureInstance>>();
			HashSet<ProcedureInstance> nextComponent = getNextComponent();
			while (nextComponent != null) {
				visitedComponents.add(nextComponent);
				unvisitedComponents.remove(nextComponent);
				visitedProcedures.addAll(nextComponent);
				listForSCCIterator.add(nextComponent);
//				System.err.println("adding component #" + listForSCCIterator.size());
//				if (listForSCCIterator.size() >= 1500)
//					throw new InternalCompilerError("profiling");

				nextComponent = getNextComponent();
			}
		}


		public HashSet<ProcedureInstance> next() {
			if (backendIterator == null)
				setupBackendIterator();

			return backendIterator.next();
		}
		/*
			HashSet<CallGraphNode> nextComponent = getNextComponent();
			visitedComponents.add(nextComponent);

			HashSet<ProcedureInstance> returnSet = procedureSetFromNodeSet(nextComponent);
			visitedProcedures.addAll(returnSet);

			return returnSet;
		}
*/
		private HashSet<ProcedureInstance> getNextComponent() {
			// for each component in the strongly connected components
			// list, see if we have visited every other component that
			// calls it
			for(HashSet<ProcedureInstance> comp : unvisitedComponents)
			{
				if (!visitedComponents.contains(comp) && haveVisitedAllDependenciesForProcedures(comp)) {
					if (Report.should_report("callgraph", 6))
						Report.report(6, "we have visited all dependencies of component " + comp);
					return comp;
				}
			}

			return null;
		}

		private boolean haveVisitedAllDependenciesForProcedures(HashSet<ProcedureInstance> compInstances)
		{
			// for each procedure instance in comp, check if we have visited all the dependencies of it, i.e.
			// each method that could possibly call us should be in the visitedProcedures set.

			for (ProcedureInstance jpi : compInstances)
			{
				if (!haveVisitedAllCalleesOfProcedureInstance(jpi, visitedProcedures, compInstances)) {
					if (Report.should_report("callgraph", 6))
						Report.report(6, "we have not visited " + jpi + ", a dependency of component " + compInstances);

					return false;
				}
				else
					if (Report.should_report("callgraph", 7))
						Report.report(6, "we have visited " + jpi + ", a dependency of component " + compInstances);
			}

			return true;
		}

		public void remove() {
		}
	}

	private boolean haveVisitedAllCallersOfProcedureInstance(ProcedureInstance jpi, Set<ProcedureInstance> visitedProcedures)
	{
		Collection<ProcedureInstance> callers = getProceduresThatCallProcedure(jpi);
		for (ProcedureInstance caller : callers)
		{
			//if (!visitedProcedures.contains(caller) && !jpi.toString().equals(caller.toString()))
			if (!visitedProcedures.contains(caller) && !jpi.equals(caller))
				return false;
		}

		return true;
	}

	private boolean haveVisitedAllCalleesOfProcedureInstance(ProcedureInstance jpi, Set<ProcedureInstance> visitedProcedures, HashSet<ProcedureInstance> compInstances)
	{
		Collection<ProcedureInstance> callees = getProceduresThatProcedureCalls(jpi);
//		System.err.println("callees of " + jpi + " are " + callees);
		if  (callees != null) {
			for (ProcedureInstance callee : callees)
			{
				if (!visitedProcedures.contains(callee) &&        // if we haven't visited it
					!compInstances.contains(callee) &&            // and it's not in the same connected component
					//!jpi.toString().equals(callee.toString()) &&  // and it's not us
					!jpi.equals(callee) &&  // and it's not us
					isInCallGraph(callee)) 						  // and we CAN visit it  (constructors invoking superclasses, for example)
					return false;
			}
		}

		return true;
	}

        private boolean isInCallGraph(ProcedureInstance callee) {
	    boolean returnResult = procedureToJobMap.containsKey(callee);
	    if (!returnResult && Report.should_report("callgraph",3))
		Report.report(3, "procedure " + callee + " is not in call graph");
	    return returnResult;
	}

	// iterate through the graph in no specified order.  used to label check the
	// constraint sets after they are generated (after that, we don't care about
	// what order we visit things in)
	public Iterator<ProcedureInstance> iterator() {
		return procedureSetFromNodeSet(callGraphMap.keySet()).iterator();
	}

	@Override
	public String toString()
	{
	    StringBuffer returnBuffer = new StringBuffer();

	    // iterate over sccs.  who calls what?
	    Iterator<HashSet<ProcedureInstance>> callGraphIterator = this.sccIterator();
	    while(callGraphIterator.hasNext()) {
		HashSet<ProcedureInstance> currentSCC = callGraphIterator.next();

		Collection<ProcedureInstance> calledProcedures = new HashSet<ProcedureInstance>();
		for(ProcedureInstance jpi : currentSCC) {
		    Collection<ProcedureInstance> calledByProcedure = getProceduresThatProcedureCalls(jpi);
		    if (calledByProcedure != null)
			calledProcedures.addAll(calledByProcedure);
		}

		returnBuffer.append("SCC: " + currentSCC + "\n");
		for(ProcedureInstance pi : calledProcedures) {
		    returnBuffer.append("\t" + pi + "\n");
		}
	    }

	    return returnBuffer.toString();
	}

	public Job getJob(ProcedureInstance jpi)
	{
		return procedureToJobMap.get(jpi);
	}

	public Collection<Job> getAllJobs()
	{
		return new HashSet<Job>(procedureToJobMap.values());
	}

	public Collection<ProcedureInstance> getProceduresThatCallProcedure(ProcedureInstance method) {
		return callerMap.get(method);
	}

	public Collection<ProcedureInstance> getProceduresThatProcedureCalls(ProcedureInstance method) {
		return calleeMap.get(method);
	}

	public boolean hasProcedure(ProcedureInstance pi) {
		for(CallGraphNode cn : callGraphMap.keySet())
		{
			if (cn.getProcedureInstance().equals(pi))
				return true;
		}

		return false;
	}

	public void generateStronglyConnectedComponents()
	{
		if (stronglyConnectedComponents.size() > 0)
			// already did this
			return;

//		System.err.println("callGraphMap: " + callGraphMap);

		// generate finishing times for each node via DFS
		final Map<CallGraphNode, Integer> finishingTimes = getFinishingTimes(callGraphMap);

//		System.err.println("finishingTimes: " + finishingTimes);

		// reverse graph
		HashMap<CallGraphNode, HashSet<CallGraphNode>> reversedMap = getReverseMap(callGraphMap);

//		System.err.println("reversedMap: " + reversedMap);

		// for DFS take 2, we visit the graph in reverse order
		List<CallGraphNode> traverseOrder = new LinkedList<CallGraphNode>(reversedMap.keySet());
		Collections.sort(traverseOrder,new Comparator<CallGraphNode>() {
					public int compare(CallGraphNode n1, CallGraphNode n2)
					{
						// n1 comes first if it took longer than n2 to finish in the original
						// graph
						return finishingTimes.get(n2).compareTo(finishingTimes.get(n1));
					}
				}
		);

		traverseOrder.addAll(reversedMap.keySet());
//		System.err.println("traverseOrder: " + traverseOrder);

		depthFirstSearch(reversedMap, null, null, null, traverseOrder.iterator(), this.stronglyConnectedComponents);
//		System.err.println("strongly connected components: " + this.stronglyConnectedComponents);
	}

	public LinkedList<HashSet<CallGraphNode>> getStronglyConnectedComponents()
	{
		return this.stronglyConnectedComponents;
	}

	private HashMap<CallGraphNode, HashSet<CallGraphNode>> getReverseMap(HashMap<CallGraphNode, HashSet<CallGraphNode>> graphMap) {

		HashMap<CallGraphNode, HashSet<CallGraphNode>> reversedMap = new HashMap<CallGraphNode, HashSet<CallGraphNode>>();

		for(CallGraphNode n : graphMap.keySet())
			reversedMap.put(n, new HashSet<CallGraphNode>());

		for(Entry<CallGraphNode, HashSet<CallGraphNode>> e : graphMap.entrySet())
		{
			for(CallGraphNode called : e.getValue())
			{
				if (graphMap.containsKey(called))
				{
					// add the current key to the hashset for called
					HashSet<CallGraphNode> currentReversedCalls = reversedMap.get(called);
					currentReversedCalls.add(e.getKey());
				}
			}
		}

		return reversedMap;
	}

	private Map<CallGraphNode, Integer> getFinishingTimes(HashMap<CallGraphNode, HashSet<CallGraphNode>> graphMap)
	{
		HashMap<CallGraphNode, Integer> finishingTimes = new HashMap<CallGraphNode, Integer>();
		depthFirstSearch(graphMap, null, null, finishingTimes, null, null);

		return finishingTimes;
	}

	public enum NodeColor {
		WHITE,
		GRAY,
		BLACK
	}

	protected void depthFirstSearch(HashMap<CallGraphNode, HashSet<CallGraphNode>> graph,
			Map<CallGraphNode, CallGraphNode> predecessors,
			Map<CallGraphNode, Integer> discoveryTimes,
			Map<CallGraphNode, Integer> finishingTimes,
			Iterator<CallGraphNode> definedVisitIterator,
			List<HashSet<CallGraphNode>> scc)
	{
		Map<CallGraphNode, NodeColor> nodeColors = new HashMap<CallGraphNode, NodeColor>();
		for(CallGraphNode n : graph.keySet())
		{
			nodeColors.put(n, NodeColor.WHITE);
			if (predecessors != null)
				predecessors.put(n, null);
		}

		int time = 0;

		Iterator<CallGraphNode> visitNodeIterator = definedVisitIterator;
		if (visitNodeIterator == null)
			visitNodeIterator = graph.keySet().iterator();

		while(visitNodeIterator.hasNext())
		{
			CallGraphNode node = visitNodeIterator.next();

			HashSet<CallGraphNode> currentScc = null;
			if (scc != null)
				currentScc = new HashSet<CallGraphNode>();

			if (nodeColors.get(node).equals(NodeColor.WHITE))
				time = depthFirstSearch_visitNode(graph, node, nodeColors, predecessors, time, discoveryTimes, finishingTimes, currentScc);

			if (scc != null && currentScc.size() > 0)
				scc.add(currentScc);
		}
	}

	protected int depthFirstSearch_visitNode(HashMap<CallGraphNode, HashSet<CallGraphNode>> graph,
			CallGraphNode node,
			Map<CallGraphNode, NodeColor> nodeColors,
			Map<CallGraphNode, CallGraphNode> predecessors,
			int time,
			Map<CallGraphNode, Integer> discoveryTimes,
			Map<CallGraphNode, Integer> finishingTimes,
			HashSet<CallGraphNode> currentScc)
	{
		if (currentScc != null)
			currentScc.add(node);

		nodeColors.put(node, NodeColor.GRAY);

		if (discoveryTimes != null)
			discoveryTimes.put(node, ++time);

		HashSet<CallGraphNode> neighbors = graph.get(node);

		for(CallGraphNode n : neighbors)
		{
			if (graph.containsKey(n) && nodeColors.get(n).equals(NodeColor.WHITE)) {
				if (predecessors != null)
					predecessors.put(n, node);

				time = depthFirstSearch_visitNode(graph, n, nodeColors, predecessors, time, discoveryTimes, finishingTimes, currentScc);
			}
		}

		nodeColors.put(node, NodeColor.BLACK);

		if (finishingTimes != null)
			finishingTimes.put(node, ++time);

		return time;
	}

	public boolean inSameConnectedComponent(ProcedureInstance jpi1, ProcedureInstance jpi2) {
		// it only does this once
		generateStronglyConnectedComponents();

		for(HashSet<CallGraphNode> comp : this.stronglyConnectedComponents)
		{
			if (nodeCollectionHasProcedureInstance(comp, jpi1) &&
				nodeCollectionHasProcedureInstance(comp, jpi2))
				return true;
		}

		return false;
	}

	public Iterator<HashSet<ProcedureInstance>> sccIterator() {
		return new CallGraphSCCIterator();
	}

	private HashSet<ProcedureInstance> procedureSetFromNodeSet(Set<CallGraphNode> name) {
		HashSet<ProcedureInstance> returnSet = new HashSet<ProcedureInstance>();

		for(CallGraphNode cn : name)
		{
			returnSet.add(cn.getProcedureInstance());
		}

		return returnSet;
	}

	public void clear() {
		this.callGraphMap.clear();
		this.classNames.clear();
		this.procedureToJobMap.clear();
		this.stronglyConnectedComponents.clear();
	}

	public String toDotString()
	{
		HashMap<CallGraphNode, String> hashMapToNameString = new HashMap<CallGraphNode, String>();
		int currentIndex = 1;
		StringBuilder returnBuffer = new StringBuilder();
		returnBuffer.append("digraph G {\n");

		// write out all the nodes in the callgraph
		for(CallGraphNode n : callGraphMap.keySet())
		{
			String currentName = "procedure" + currentIndex;
			++currentIndex;
			String procedureName = currentName;
			ProcedureInstance pi = n.getProcedureInstance();

			if (pi instanceof ConstructorInstance) {
				ConstructorInstance ci = (ConstructorInstance) pi;
				procedureName = ci.container().toString() + "(...)";
			}
			else if (pi instanceof MethodInstance) {
				MethodInstance mi = (MethodInstance) pi;
				procedureName = mi.container().toString() + "." + mi.name() + "(...)";
			}

			returnBuffer.append(currentName + " [shape=rectangle,label=\"" + procedureName + "\"];\n");

			hashMapToNameString.put(n, currentName);
		}

		// and the links between them
		for(Entry<CallGraphNode, HashSet<CallGraphNode>> e : callGraphMap.entrySet())
		{
			CallGraphNode from = e.getKey();
			for(CallGraphNode to : e.getValue())
			{
				String fromName = hashMapToNameString.get(from);
				String toName = hashMapToNameString.get(to);
				if (fromName != null && toName != null)
					returnBuffer.append(fromName + " -> " + toName + ";\n");
			}
		}

		returnBuffer.append("}");

		return returnBuffer.toString();
	}

	public Set<ProcedureInstance> getDeclaredProcedures() {
		return procedureToJobMap.keySet();
	}

	public Set<ProcedureInstance> getAllProcedures() {
		return allProcedures;
	}

	public Collection<ProcedureInstance> getReachSet(ProcedureInstance pi) {
		Collection<ProcedureInstance> reachSet = new LinkedHashSet<ProcedureInstance>();
		getReachSetHelper(pi, reachSet);

		return reachSet;
	}

	private void getReachSetHelper(ProcedureInstance pi,
			Collection<ProcedureInstance> reachSet) {
		if (reachSet.contains(pi))
			return;
		reachSet.add(pi);
		Collection<ProcedureInstance> callees = this.getProceduresThatProcedureCalls(pi);
		if (callees != null) {
			for(ProcedureInstance callee : callees) {
				getReachSetHelper(callee, reachSet);
			}
		}
	}

	public void outputCallGraphToDotFile() {
		File f = new File("callgraph.dot");
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(f));
			fos.print(toDotString());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addFlowGraph(ProcedureInstance procedureInstance,
			FlowGraph g) {
		this.piToCFG.put(procedureInstance, g);
	}

	public FlowGraph getFlowGraph(ProcedureInstance pi) {
		return this.piToCFG.get(pi);
	}

	public void addProcedureInstanceForNode(Node n, ProcedureInstance pi) {
		this.nodeToPi.put(n.position(), pi);
	}

	public ProcedureInstance getProcedureInstanceForNode(Node n) {
		//System.out.println(nodeToPi + " and " + n);
		return this.nodeToPi.get(n.position());
	}
}
