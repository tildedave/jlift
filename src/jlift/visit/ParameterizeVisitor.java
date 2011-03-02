/*
 * File          : ParameterizeVisitor.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:53 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.visit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jif.ast.InstTypeNode;
import jif.ast.InstTypeNode_c;
import jif.types.JifPolyType;
import jif.types.JifTypeSystem;
import jif.types.ParamInstance;
import jif.types.label.Label;
import jlift.ast.JLiftNodeFactory;
import jlift.ast.VarLabelNode;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.TypeNode;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import polyglot.visit.TypeBuilder;

public class ParameterizeVisitor extends ContextVisitor {

	public ParameterizeVisitor(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
	}

	static int instCounter;

	@Override
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v)
			throws SemanticException {
		Node superVisitor = super.leaveCall(parent, old, n, v);
		if (n instanceof TypeNode) {
			TypeNode tn = (TypeNode) n;
			return possiblyParameterizeTypeNode(parent, tn);
		}
		return superVisitor;
	}

	private Node possiblyParameterizeTypeNode(Node parent, TypeNode tn)
			throws SemanticException {
		JifTypeSystem ts = (JifTypeSystem) this.ts;
		JLiftNodeFactory nf = (JLiftNodeFactory) this.nf;

		Type t = ts.unlabel(tn.type());

		if (t instanceof JifPolyType) {
			JifPolyType jpt = (JifPolyType) t;

			if (!jpt.params().isEmpty()) {
				if (parent != null) {
					if (!(parent instanceof InstTypeNode_c)) {
						Position pos = tn.position();

						if (Report.should_report("param", 1))
							Report.report(1,"parameterizing type node " + tn + " at " + pos);

						List params = new LinkedList();

			        	// now add a variable for each parameter in t

			        	Iterator it = ((JifPolyType) t).params().iterator();
			        	while(it.hasNext()) {
			        		ParamInstance next = (ParamInstance) it.next();
			        		if (next.isLabel()) {
			        			Label varLabel = ts.freshLabelVariable(tn.position(), next.name() +"_var@" + instCounter, "variable for the instantiation of " + t + " at position " + tn);
			        			++instCounter;
								VarLabelNode vln = nf.VarLabelNode(pos, varLabel);
			        			params.add(vln);
			        		}
			        	}

			        	// need to return the result of disambiguating the LABELNODE on the INSTNODE on the TYPENODE

			        	Node returnNode;

			        	if (ts.isLabeled(tn.type())) {
			        		Type unlabeled = ts.unlabel(tn.type());
			        		Label label = ts.labelOfType(tn.type());

			        		TypeNode unlabeledTypeNode = nf.CanonicalTypeNode(unlabeled.position(), unlabeled);
			        		InstTypeNode instNode = nf.InstTypeNode(pos, unlabeledTypeNode, params);
			        		returnNode = nf.LabeledTypeNode(tn.position(), (TypeNode) instNode.disambiguate(new AmbiguityRemover(job, ts, nf)), nf.CanonicalLabelNode(label.position(), label));
			        	}
			        	else {
			        		InstTypeNode instNode = nf.InstTypeNode(pos, tn, params);
			        		returnNode = instNode;
			        	}
						returnNode = returnNode.buildTypes(new TypeBuilder(job, ts, nf));
						returnNode = returnNode.disambiguate(new AmbiguityRemover(job, ts, nf));
						if (Report.should_report("param", 1))
							Report.report(1, "parameterized " + tn + " into " + returnNode);
						return returnNode;
					}
				}
			}
		}
		return tn;
	}
}
