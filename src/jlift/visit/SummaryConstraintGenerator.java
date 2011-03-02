/*
 * File          : SummaryConstraintGenerator.java
 * Project       : jlift
 * Description   :
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 10:10:21 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.visit;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jif.extension.CallHelper;
import jif.types.JifContext;
import jif.types.JifMethodInstance;
import jif.types.JifProcedureInstance;
import jif.types.LabelConstraint;
import jif.types.LabelConstraintMessage;
import jif.types.LabelSubstitution;
import jif.types.NamedLabel;
import jif.types.hierarchy.LabelEnv;
import jif.types.label.Label;
import jif.types.label.VarLabel;
import jif.visit.LabelChecker;
import jifclipse.types.JifclipseLabelConstraint;
import jlift.SummaryConstraintInfo;
import jlift.extension.JLiftCallHelper;
import jlift.extension.SummaryCallHelper;
import jlift.types.CallConstraint;
import jlift.types.JLiftContext_c;
import jlift.types.JLiftLabelCheckingUtil;
import jlift.types.JLiftLabelConstraint;
import jlift.types.label.JLiftInstLabel_c;
import jlift.util.CallGraph;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Receiver;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.MethodInstance;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

public class SummaryConstraintGenerator extends LabelChecker {

	static protected Node currentNode = null;

	public Node getCurrentNode() { return SummaryConstraintGenerator.currentNode; }
	public void setCurrentNode(Node newNode) { SummaryConstraintGenerator.currentNode = newNode; }

	//static LinkedList<Node> nodesThatHaveBeenChecked = new LinkedList<Node>();

	protected Collection<CallConstraint> callConstraints;
	protected Collection<JLiftLabelConstraint> localConstraints;
	// need the summary info to help out the call helper
	protected SummaryConstraintInfo summaryInfo;
	protected CallGraph callGraph;
	private final Collection<JLiftLabelConstraint> toConstrain;
	private final Map<Integer, Label> labelMapping;
	public static Map<Node, Label> pcAfterLabelCheckingNodeMap = new HashMap<Node, Label>();

	public SummaryConstraintGenerator(Job job, TypeSystem ts, NodeFactory nf, SummaryConstraintInfo summaryInfo, CallGraph callGraph) {
		super(job, ts, nf, false, false, false);
		this.localConstraints = new LinkedHashSet<JLiftLabelConstraint>();
		this.callConstraints = new LinkedHashSet<CallConstraint>();
		this.summaryInfo = summaryInfo;
		this.callGraph = callGraph;
		this.toConstrain = new LinkedHashSet<JLiftLabelConstraint>();
		this.labelMapping = new LinkedHashMap<Integer, Label>();

		JLiftContext_c myContext = (JLiftContext_c) this.context();
		if (myContext.getLabelChecker() == null)
			myContext.setLabelChecker(this);

		JLiftLabelCheckingUtil.global = this;
	}

	@Override
	public void enteringClassBody(ClassType ct) {
		// nothing
	}

	@Override
	public void enteringMethod(MethodInstance mi) {
		// nothing
	}

	@Override
	public Object copy() {
		SummaryConstraintGenerator theCopy = (SummaryConstraintGenerator) super.copy();
		if (theCopy.callConstraints == null)
			theCopy.callConstraints = this.callConstraints;
		if (theCopy.localConstraints == null)
			theCopy.localConstraints = this.localConstraints;
		if (theCopy.summaryInfo == null)
			theCopy.summaryInfo = this.summaryInfo;
		if (theCopy.callGraph == null)
			theCopy.callGraph = this.callGraph;

		return theCopy;
	}

	@Override
	public LabelChecker context(JifContext c) {
		SummaryConstraintGenerator returnLC = (SummaryConstraintGenerator) super.context(c);
		JLiftContext_c ctx = (JLiftContext_c) returnLC.context();
		ctx.setLabelChecker(returnLC);

		return returnLC;
	}

	@Override
	public void constrain(LabelConstraint c) throws SemanticException {
		if (needToReverseLabelConstraint(c)) {
			// in some cases, we have are given a constraint like:  {L} == {v};
			// i.e. the underlying Jif engine gives us a bad constraint.  in this
			// situation, we reverse the constraint to {v} == {L}, and then add it
			constrain(reverseConstraint(c));
		}
		else {
			// archive the constraint if it's not stupid
			if (!constraintIsRedundant(c)) {
				// the below is rarely, if ever, useful
				if (Report.should_report("summary", 8)) {
					Throwable stack = new Throwable();
					stack.fillInStackTrace();
					Report.report(8, "adding constraint " + c + " because of:\n\t- "
							+ stack.getStackTrace()[1].toString() + "\n\t- "
							+ stack.getStackTrace()[2].toString() + "\n\t- "
							+ stack.getStackTrace()[3].toString() + "\n\t- "
							+ stack.getStackTrace()[4].toString() + "\n\t- "
							+ stack.getStackTrace()[5].toString() + "\n\t- "
							+ stack.getStackTrace()[6].toString() + "\n\t- "
							+ stack.getStackTrace()[7].toString() + "\n\t- "
							+ stack.getStackTrace()[8].toString() + "\n"
							);
				}

				JLiftLabelConstraint jlc = SummaryConstraintInfo.convertLabelConstraint(c);
				//System.err.println("\tc:" + c);
				JLiftLabelConstraint constraintToAdd = SummaryConstraintInfo.unwrapArgLabelsInLabelConstraint(jlc, this);

				if (Report.should_report("summary", 8))
					Report.report(8, "now constraining: " + constraintToAdd);
//				System.err.println("\tc:" + c);
//				if (hasParamLabels(constraintToAdd)) {
//				System.err.println("constraintToAdd: " + constraintToAdd + " added by " + constraintToAdd.getNode());
//				}
				toConstrain.add(constraintToAdd);
			}
		}
	}

	private LabelConstraint reverseConstraint(final LabelConstraint c) {
		LabelConstraint reversedConstraint = new JLiftLabelConstraint(c.rhs(),
				c.kind(),
				c.lhs(),
				c.env(),
				c.position(),
				new LabelConstraintMessage() {
			@Override
			public String detailMsg() {
				return c.detailMsg();
			}
			@Override
			public String technicalMsg() {
				return c.technicalMsg();
			}
			@Override
			public String msg() {
				return c.msg();
			}
		}, c.report(), ((JifclipseLabelConstraint) c).node());

		return reversedConstraint;
	}
	private boolean needToReverseLabelConstraint(LabelConstraint c) {
		return (c.kind() == LabelConstraint.EQUAL &&
				!(c.lhs() instanceof VarLabel) &&
				c.rhs() instanceof VarLabel);
	}
	@Override
	public void constrain(NamedLabel lhs, LabelConstraint.Kind kind, NamedLabel rhs, LabelEnv env,
			Position pos, boolean report, LabelConstraintMessage msg) throws SemanticException {
		JLiftLabelConstraint c = new JLiftLabelConstraint(lhs, kind, rhs, env, pos, msg, report);
		if (msg != null) msg.setConstraint(c);
		constrain(c);
		c.checkDefiniteness();
	}


	public void constrainWithRealNode() throws SemanticException {
//		for(JLiftLabelConstraint c : localConstraints)
//		System.err.println("^^@> " + c);

		for(JLiftLabelConstraint c : toConstrain) {
			if (c.getNode() == null) {
				c.setNode(SummaryConstraintGenerator.currentNode);
			}
			if (c.getCodeInstance() == null) {
				c.setCodeInstance(this.context().currentCode());
			}

			if (Report.should_report("summary", 9)) {
				Report.report(9, "adding constraint " + c + " to local constraints");
			}

			localConstraints.add(c);
		}

		toConstrain.clear();
	}

//	private void setDistance(LabelConstraint c) {
//	int weightForNode = SourceDistanceCalculator.getDistanceFromSourceForNode(c.getNode());
//	c.setDistance(weightForNode);
//	}

	private boolean constraintIsRedundant(LabelConstraint c) {
		if (c.kind() == LabelConstraint.LEQ && (c.lhs().isBottom() || c.rhs().isTop())) {
			return true;
		}
		if (c.lhs().equals(c.rhs()))
			return true;

		if (containsConstraint(c))
			return true;

		return false;
	}

	@Override
	public Node labelCheck(Node n) throws SemanticException {
		LabelChecker oldLabelChecker = JLiftLabelCheckingUtil.global;
		JLiftLabelCheckingUtil.global = this;

		Node oldCurrentNode = SummaryConstraintGenerator.currentNode;
		SummaryConstraintGenerator.currentNode = n;
		Node returnNode = super.labelCheck(n);
		if (!pcAfterLabelCheckingNodeMap.containsKey(returnNode)) {
			pcAfterLabelCheckingNodeMap.put(returnNode, context().pc());
		}
		SummaryConstraintGenerator.currentNode = returnNode;
		constrainWithRealNode();
		SummaryConstraintGenerator.currentNode = oldCurrentNode;

		JLiftLabelCheckingUtil.global = oldLabelChecker;

		return returnNode;
	}

	public SummaryConstraintInfo getSummaryInfo() {
		return this.summaryInfo;
	}

	public CallGraph getCallGraph() {
		return this.callGraph;
	}

	public void addCallConstraint(CallConstraint callConstraint) {
		callConstraints.add(callConstraint);
	}

	public void pushConstraintsToSummaryInfo(ProcedureInstance pi) {
		synchronized (summaryInfo) {
			summaryInfo.setConstraints(pi, localConstraints, callConstraints);
		}
	}

	// used externally for debugging
	public void reportRawSummaryConstraints(ProcedureInstance jpi, int level) {
		if(Report.should_report("summary", level))
		{
			Report.report(level, "raw summary constraints for " + jpi);

			for(LabelConstraint c : this.localConstraints)
			{
				Report.report(level, "-->" + c.toString());
			}
			for(CallConstraint c : this.callConstraints)
			{
				Report.report(level, "-->" + c);
			}
		}
	}

	public boolean containsConstraint(LabelConstraint c) {
		return localConstraints.contains(c) || toConstrain.contains(c);
	}

	public Collection<JLiftLabelConstraint> getCurrentLocalConstraints() {
		return localConstraints;
	}


	public JLiftLabelConstraint definitionalLabelConstraint(
			Label lhs, Label rhs, LabelEnv env,
			Position pos) {
		return new JLiftLabelConstraint(new NamedLabel("", lhs), JLiftLabelConstraint.DEFINE,
				new NamedLabel("", rhs),
				env,
				pos);
	}

	public static JLiftLabelConstraint equalitylabelConstraint(Label lhs,
			Label rhs, LabelEnv env, Position pos) {
		return new JLiftLabelConstraint(new NamedLabel("", lhs), JLiftLabelConstraint.EQUAL,
				new NamedLabel("", rhs),
				env,
				pos);
	}

	@Override
	public JLiftCallHelper createCallHelper(Label receiverLabel,
			Receiver receiver,
			ReferenceType calleeContainer,
			JifProcedureInstance pi,
			List actualArgs,
			Position position) {
		return new SummaryCallHelper(receiverLabel, receiver, calleeContainer, pi, actualArgs, position);
	}


	@Override
	public CallHelper createOverrideHelper(JifMethodInstance overridden,
			JifMethodInstance overriding) {
		return SummaryCallHelper.DummyOverrideHelper(overridden, overriding, this);
	}

	public void addLabelMapping(final int id, final Label label) {
		if (Report.should_report("dynamic", 2))
			Report.report(2, "mapping inst label " + id + " to " + label);
		this.getLabelMapping().put(id, label);

		//		System.out.println("OLD MAPPING: " + this.getLabelMapping());
//		// modify existing mappings
//		HashMap<Integer, Label> newMapping = new HashMap<Integer, Label>();
//		for (Map.Entry<Integer, Label> e : labelMapping.entrySet()) {
//			try {
//				Label substLabel = e.getValue().subst(new LabelSubstitution() {
//					@Override
//					public Label substLabel(Label L) throws SemanticException {
//						if (L instanceof JLiftInstLabel_c) {
//							JLiftInstLabel_c jil = (JLiftInstLabel_c) L;
//							if (jil.id() == id) {
//								return label;
//							}
//						}
//						return super.substLabel(L);
//					}
//				});
//				System.out.println("\tREMAP " + id + " TO " + label);
//				newMapping.put(e.getKey(), substLabel);
//			} catch (SemanticException e1) {
//				throw new InternalCompilerError(e1);
//			}
//		}
//		System.out.println("NEW MAPPING: " + newMapping);
//		this.labelMapping = newMapping;
	}

	public List<LabelSubstitution> getLabelSubstitutionChain() {
		LinkedList<LabelSubstitution> returnList = new LinkedList<LabelSubstitution>();
		for (final Map.Entry<Integer, Label> e : labelMapping.entrySet()) {
			returnList.add(new LabelSubstitution() {
				@Override
				public Label substLabel(Label L) throws SemanticException {
					if (L instanceof JLiftInstLabel_c) {
						JLiftInstLabel_c jil = (JLiftInstLabel_c) L;
						if (jil.id() == e.getKey()) {
							return e.getValue();
						}
					}
					return L;
				}
			});
		}
		return returnList;
	}

	public Map<Integer, Label> getLabelMapping() {
		return labelMapping;
	}
}
