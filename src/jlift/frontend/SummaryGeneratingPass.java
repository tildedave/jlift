/*
 * File          : SummaryGeneratingPass.java
 * Project       : jlift
 * Description   : Generate the summary constraints for the entire program.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:38:49 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.frontend;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jlift.ExtensionInfo;
import jlift.SummaryConstraintInfo;
import jlift.types.JLiftLabelConstraint;
import jlift.util.CallGraph;
import jlift.visit.SummaryConstraintGenerator;
import jlift.visit.SummaryConstraintVisitor;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.AbstractPass;
import polyglot.frontend.Job;
import polyglot.frontend.goals.Goal;
import polyglot.main.Report;
import polyglot.types.MemberInstance;
import polyglot.types.ProcedureInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorInfo;
import polyglot.visit.NodeVisitor;

public class SummaryGeneratingPass extends AbstractPass {

	protected TypeSystem ts;
	protected NodeFactory nf;
	protected ExtensionInfo jifext;

	protected SummaryConstraintInfo summaryInfo;
	public CallGraph callGraph;
	protected boolean multiThreaded = false;

	protected List<ProcedureInstance> handledProcedures;
	protected Set<ProcedureInstance> proceduresToGenerate;

	public SummaryGeneratingPass(Goal goal, ExtensionInfo jifext, TypeSystem ts, NodeFactory nf, CallGraph callGraph, SummaryConstraintInfo summaryInfo) {
		super(goal);
		this.ts = ts;
		this.nf = nf;
		this.jifext = jifext;
		this.summaryInfo = summaryInfo;
		this.callGraph = callGraph;
		this.handledProcedures = new LinkedList<ProcedureInstance>();
		this.proceduresToGenerate = new HashSet<ProcedureInstance>();
		//this.multiThreaded = false;
	}

	@Override
	public boolean run() {
		// in FORWARD call graph order (start with the methods that don't call anything),
		// generate summary constraints

		generateProceduresToGenerate();

		if (Report.should_report("callgraph",2))
			callGraph.outputCallGraphToDotFile();
		else if (Report.should_report("callgraph", 1))
			Report.report(1, callGraph.toString());

		if (Report.should_report("summary", 2))
		{
			Iterator<HashSet<ProcedureInstance>> callIt = callGraph.sccIterator();
			int i = 0;
			while(callIt.hasNext())
			{
				++i;
				HashSet<ProcedureInstance> scc = callIt.next();
				Report.report(3, i + ": " + scc.toString());
//				if (Report.should_report("scheduler", 4))
//				Report.report(4, "\tJob: " + callGraph.getJob(jpi));
			}
		}

		try {
			generateSummaryConstraintsSingleThreaded();
		}
		catch (SemanticException e) {
			jifext.compiler().errorQueue().enqueue(new ErrorInfo(ErrorInfo.SEMANTIC_ERROR, e.getMessage(), e.position()));
			return false;
		}

		if (Report.should_report("summary", 1))
			Report.report(1, "finished generating summary constraints");
		if (Report.should_report("summary", 2))
			Report.report(2, "summary info size: " + summaryInfo.size());
		return true;
	}

	private void generateProceduresToGenerate() {
		// find API methods
		Iterator<ProcedureInstance> itCallGraph = callGraph.iterator();
		while (itCallGraph.hasNext()) {
			ProcedureInstance pi = itCallGraph.next();
			if (SummaryConstraintInfo.isTotallyLabeled(ts, pi)) {
//				System.err.println(pi + " is totally labeled");
				Collection<ProcedureInstance> reachSet = callGraph.getReachSet(pi);
//				System.err.println("reach set: " +reachSet);
				proceduresToGenerate.addAll(reachSet);
			}
		}
	}

	private void generateSummaryConstraintsSingleThreaded() throws SemanticException {
		Iterator<HashSet<ProcedureInstance>> it = callGraph.sccIterator();
		int sccCounter = 0;

		while (it.hasNext())
		{
			++sccCounter;
			HashSet<ProcedureInstance> scc = it.next();

			if (Report.should_report("summary", 3))
				Report.report(3, "#" + sccCounter + ": generate summary information for scc " + scc);

			generateSummaryInformationForStronglyConnectedComponent(scc);
		}
	}

	protected void generateSummaryInformationForStronglyConnectedComponent(HashSet<ProcedureInstance> scc) throws SemanticException {
		for (ProcedureInstance proc : scc)
		{
			if (Report.should_report("summary", 3))
				Report.report(3, "generate summary information: " + proc + "@" + proc.position() + " inside " + ((MemberInstance) proc).container());

			generateSummaryInformationForProcedure(proc);
		}


		for (ProcedureInstance proc : scc)
		{
			if (Report.should_report("summary",5)) {
				Report.report(5, "unclosed constraints for " + proc + ":");
				summaryInfo.reportUnclosedSummaryConstraintSet(proc, 5);
			}

			if (scc.size() > 1 && Report.should_report("summary",5)) {
				Report.report(5, "closing constraints for " + scc);
			}

			summaryInfo.closeCallConstraints(proc, scc);

			if (scc.size() > 1) {
				if (Report.should_report("summary",5)) {
					Report.report(5, "closed constraints for " + proc + ":");
					summaryInfo.reportSummaryConstraintSet(proc, 5);
				}

				if (Report.should_report("summary", 4)) {
					Report.report(4, "size of summary constraint info: " + summaryInfo.size());
				}
			}
		}
	}

	protected void generateSummaryInformationForProcedure(ProcedureInstance proc)
	{
		// create a visitor and visit!
		Job job = callGraph.getJob(proc);
		Node ast = job.ast();
		SummaryConstraintGenerator slc = new SummaryConstraintGenerator(job, ts, nf, summaryInfo, callGraph);
		boolean needToGenerate = true;

		if (Report.should_report("memprofile", 1)) {
			System.gc();
			System.gc();
			System.gc();

			System.gc();
			System.gc();
			System.gc();
		}

		long oldSummaryInfoSize = 0;
		long originalFreeMemory = Runtime.getRuntime().freeMemory();

		if (!proceduresToGenerate.contains(proc)) {
			if (Report.should_report("summary", 2)) {
				Report.report(2, "not generating summary constraints for " + proc + " as it is not called from an API function");
			}
			needToGenerate = false;
		}
		else {
			SummaryConstraintVisitor sv = new SummaryConstraintVisitor(job, ts, nf, proc, slc, summaryInfo, ast);
			NodeVisitor sv_ = sv.begin();

			if (sv_ != null)
			{
				ast = ast.visit(sv_);
				job.ast(ast);
			}
		}

		slc.pushConstraintsToSummaryInfo(proc);
		Collection callConstraints = slc.getSummaryInfo().getCallConstraints(proc);
		Collection<JLiftLabelConstraint> nonCallConstraints = slc.getSummaryInfo().getNonCallConstraints(proc);

		if (needToGenerate && Report.should_report("summary",2)) {
			Report.report(2, "summary constraints generated for " + proc);
			Report.report(2, "non-call constraints: " + nonCallConstraints.size() + "/ call constraints: " + callConstraints.size());
		}
		if (needToGenerate && Report.should_report("memprofile", 1)) {
			long memDiff = (originalFreeMemory - Runtime.getRuntime().freeMemory());
			Report.report(1, "non-call constraints: " + nonCallConstraints.size() + " with memory footprint of " + memDiff / (nonCallConstraints.size() * 1.0) + " (" + memDiff + "/" + nonCallConstraints.size() + ")");
		}

		handledProcedures.add(proc);

		if (Report.should_report("summary", 1) && !Report.should_report("summary", 2)) {
			// do this with a line-feed in the event
			// that we're only asking for a very minimal output
			String outputString = "generated summary constraints for procedure #" +
			handledProcedures.size() + "/" + callGraph.getDeclaredProcedures().size();
			System.out.print(outputString +"\r");
			if (handledProcedures.size() == callGraph.getDeclaredProcedures().size())
				System.out.println();
		}
	}
}
