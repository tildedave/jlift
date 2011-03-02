/*
 * File          : JLiftLabelConstraint.java
 * Project       : jlift
 * Description   : Specialized label constraint.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:56:52 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import jif.types.LabelConstraint;
import jif.types.LabelConstraintMessage;
import jif.types.LabelSubstitution;
import jif.types.NamedLabel;
import jif.types.hierarchy.LabelEnv;
import jif.types.label.JoinLabel;
import jif.types.label.Label;
import jifclipse.types.JifclipseLabelConstraint;
import jlift.JLiftOptions;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.types.CodeInstance;
import polyglot.types.SemanticException;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class JLiftLabelConstraint extends JifclipseLabelConstraint {

	public static LabelConstraintMessage emptyLabelConstraintMessage;
	public static StrippedNamedLabel label;

	public static class JLiftKind extends LabelConstraint.Kind {
		public JLiftKind(String name) {
			super(name);
		}

	}

	public static final Kind DEFINE = new JLiftKind(" ==_{def} ");
	//protected Node node;

	public JLiftLabelConstraint(NamedLabel lhs, Kind kind, NamedLabel rhs,
			LabelEnv env, Position pos, LabelConstraintMessage msg,
			boolean report) {
		this(lhs.label(), kind, rhs.label(), JLiftTypeSystem_c.emptyEnvironment, pos, msg, report, null);
//		super(new StrippedNamedLabel(lhs), kind, new StrippedNamedLabel(rhs), JLiftTypeSystem_c.emptyEnvironment, pos, emptyLabelConstraintMessage,
//				null, report);

		checkDefiniteness();
	}

	public JLiftLabelConstraint(NamedLabel lhs, Kind kind, NamedLabel rhs,
			LabelEnv env, Position pos) {
//		super(new StrippedNamedLabel(lhs), kind, new StrippedNamedLabel(rhs), JLiftTypeSystem_c.emptyEnvironment, pos,
//				emptyLabelConstraintMessage, null, true);
		this(lhs.label(), kind, rhs.label(), JLiftTypeSystem_c.emptyEnvironment, pos, emptyLabelConstraintMessage, true, null);
	}

	protected JLiftLabelConstraint(NamedLabel lhs, Kind kind, NamedLabel rhs,
			LabelEnv env, Position position, LabelConstraintMessage messages,
			boolean report, Node node) {
//		super(new StrippedNamedLabel(lhs), kind, new StrippedNamedLabel(rhs), JLiftTypeSystem_c.emptyEnvironment, position,
//				emptyLabelConstraintMessage, node, report);
		this(lhs.label(), kind, rhs.label(), JLiftTypeSystem_c.emptyEnvironment, position, messages, true, node);

	}

	public JLiftLabelConstraint(Label newLhs, Kind kind, Label newRhs,
			LabelEnv env, Position position, LabelConstraintMessage messages,
			boolean report, Node node) {
		// don't store named labels -- pass a stripped name label to the superclass
		// in theory this performs memory efficiency...
		super(label, kind, label, env, position, messages, node, report);
		this.lhs = newLhs;
		this.rhs = newRhs;
	}

	public void checkDefiniteness() {
		if (this.kind == LabelConstraint.LEQ && !isSingleComponent(rhs)) {
//			throw new InternalCompilerError("non-definite RHS: " + this);
			Report.report(1,"non-definite RHS: " + this);
//			try {
//				throw new InternalCompilerError("bork bork bork");
//			}
//			catch (InternalCompilerError e) {
//				e.printStackTrace();
//			}
		}
	}

	public void setNode(Node n) {
		this.node = n;
	}

	public Node getNode() {
		return this.node;
	}

	@Override
	public Node node() {
		// TODO Auto-generated method stub
		return super.node();
	}

	// DHK: convenience method
	public JLiftLabelConstraint subst(LabelSubstitution ls)
			throws SemanticException {

		if (JLiftOptions.getInstance().shouldUseImperativeSubstitution()) {
			applySubstitutionImperatively(ls);

			return this;
		}

		Label newLhs = lhs().subst(ls);
		Label newRhs = rhs().subst(ls);
		return new JLiftLabelConstraint(newLhs, kind(), newRhs,
				env(), position(), messages, report, node);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(lhs());
		sb.append(kind());
		sb.append(rhs());
		sb.append("@");
		sb.append(position());

		return sb.toString();
	}

	public String shortString() {
		StringBuffer sb = new StringBuffer();
		sb.append(lhs());
		sb.append(kind());
		sb.append(rhs());

		return sb.toString();
	}

	@Override
	public Collection getEquations() {
		if (kind() == JLiftLabelConstraint.DEFINE) {
			Collection eqns = new LinkedList();
			addLEQEqns(eqns, rhs(), lhs());
			return eqns;
		}
		else if (JLiftOptions.getInstance().allowNondefiniteConstraints == false) {
			Collection eqns = new LinkedList();
	        boolean singleLhs = isSingleComponent(lhs);
	        boolean singleRhs = isSingleComponent(rhs);
			if (kind() == LEQ) {
	        	if (singleRhs)
	        		addLEQEqns(eqns, lhs, rhs);
	        	else {
	        		System.out.println("Bad mojo in LEQ constraint: " + this + " " + this.node().getClass());
	        		JoinLabel j = (JoinLabel) rhs;
	        		for(Label l : (Collection<Label>) j.joinComponents()) {
	        			addLEQEqns(eqns, lhs, l);
	        		}
	        	}
	        }
	        else if (kind() == EQUAL) {
				if (singleLhs) {
	        		addLEQEqns(eqns, rhs, lhs);
	        		if (singleRhs)
	        			addLEQEqns(eqns, lhs, rhs);
	        	}
				else if (singleRhs) {
		            addLEQEqns(eqns, lhs, rhs);
	        	}
	        	else
	        		throw new InternalCompilerError("Bad mojo in EQUAL constraint: " + this + " " + this.node().getClass());
	        }
	        else {
	            throw new InternalCompilerError("Unknown kind of equation: " + kind);
	        }
	        return eqns;
	        //return super.getEquations();
		}

		return super.getEquations();
	}

	private boolean isSingleComponent(Label rhs) {
		//return rhs instanceof VarLabel || rhs instanceof PairLabel || rhs instanceof DynamicLabel;
		return !(rhs instanceof JoinLabel) || (!rhs.hasVariableComponents());

	}

	public LabelConstraintMessage getMessages() {
		return this.messages;
	}

	@Override
	public int hashCode() {
		return 2 * lhs.hashCode() + 3 * kind().hashCode() + 5 * rhs.hashCode()
				+ 7 * position().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JLiftLabelConstraint) {
			JLiftLabelConstraint jlc = (JLiftLabelConstraint) obj;
			return jlc.lhs().equals(lhs()) && jlc.rhs().equals(rhs())
					&& jlc.kind().equals(kind());
		}

		return false;
	}

	public void setLhs(Label newLhs) {
		this.lhs = newLhs;
		//((StrippedNamedLabel) this.namedLHS).setLabel(newLhs);
	}

	public void setRhs(Label newRhs) {
		this.rhs = newRhs;
		//((StrippedNamedLabel) this.namedRHS).setLabel(newRhs);
	}

	public void applySubstitutionImperatively(LabelSubstitution ls)
			throws SemanticException {
		setRhs(rhs.subst(ls));
		setLhs(lhs.subst(ls));
	}

	@Override
	public JLiftLabelConstraint clone() {
		JLiftLabelConstraint jlc = new JLiftLabelConstraint(lhs, kind,
				rhs, env, position(), messages, report, node);
		jlc.setCodeInstance(this.codeInstance);

		return jlc;
	}

	protected CodeInstance codeInstance;

	public CodeInstance getCodeInstance() {
		return codeInstance;
	}

	public void setCodeInstance(CodeInstance ci) {
		this.codeInstance = ci;
	}

	@Override
	protected Map namedDescrips() {
        return StrippedNamedLabel.emptyMap;
	}

    @Override
	protected Map namedLabels() {
        return StrippedNamedLabel.emptyMap;
    }

    @Override
    public NamedLabel namedLhs() {
    	return null;
    }

    @Override
    public NamedLabel namedRhs() {
    	return null;
    }
}
