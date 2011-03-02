/*
 * File          : JLiftTypeNodeDel.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:49 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.extension;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jif.ast.InstTypeNode;
import jif.ast.InstTypeNode_c;
import jif.extension.JifTypeNodeDel;
import jif.types.JifPolyType;
import jif.types.JifSubstClassType_c;
import jif.types.JifTypeSystem;
import jif.types.ParamInstance;
import jif.types.label.Label;
import jlift.ast.JLiftNodeFactory;
import jlift.ast.VarLabelNode;
import jlift.util.NumberProvider;
import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.TypeBuilder;

public class JLiftTypeNodeDel extends JifTypeNodeDel {

	protected Node parent;

	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public Node disambiguateOverride(Node parent, AmbiguityRemover ar)
			throws SemanticException {
		setParent(parent);
		return super.disambiguateOverride(parent, ar);
	}

	// TODO: do this before disambiguation, if at all possible
    @Override
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
    	/* DHK: automatically add label parameters... I hope */
    	TypeNode tn = (TypeNode) super.disambiguate(ar);

    	JifTypeSystem ts = (JifTypeSystem) tn.type().typeSystem();
        Type t = ts.unlabel(tn.type());

        if (t instanceof JifPolyType) {
        	JifPolyType jpt = (JifPolyType) t;

        	if (!jpt.params().isEmpty()) {
        		if (parent != null) {
        			if (!(parent instanceof InstTypeNode_c)) {
        				Position pos = node().position();
						//System.err.println("parent: " + parent + " and " + parent.getClass());
        				if (Report.should_report("param", 1))
        					Report.report(1,"parameterizing type node " + tn + " at " + pos);

        	        	JLiftNodeFactory nf = (JLiftNodeFactory) ar.nodeFactory();
        	        	List params = new LinkedList();

        	        	// now add a variable for each parameter in t

        	        	Iterator it = ((JifPolyType) t).params().iterator();
        	        	while(it.hasNext()) {
        	        		ParamInstance next = (ParamInstance) it.next();
        	        		if (next.isLabel()) {
        	        			Label varLabel = ts.freshLabelVariable(node.position(),
        	        					next.name() +"_var@" + NumberProvider.getNextNumber(),
        	        					"variable for the instantiation of " + t + " at position " + node.position());
								VarLabelNode vln = nf.VarLabelNode(pos, varLabel);
        	        			params.add(vln);
        	        		}
        	        	}

        	        	// need to return the result of disambiguating
        	        	// the LABELNODE on the INSTNODE on the TYPENODE

        	        	Node returnNode;

        	        	if (ts.isLabeled(tn.type())) {
        	        		Type unlabeled = ts.unlabel(tn.type());
        	        		Label label = ts.labelOfType(tn.type());

        	        		TypeNode unlabeledTypeNode = nf.CanonicalTypeNode(unlabeled.position(), unlabeled);
        	        		InstTypeNode instNode = nf.InstTypeNode(pos, unlabeledTypeNode, params);
        	        		returnNode = nf.LabeledTypeNode(tn.position(), (TypeNode) instNode.disambiguate(ar), nf.CanonicalLabelNode(label.position(), label));
        	        	}
        	        	else {
        	        		InstTypeNode instNode = nf.InstTypeNode(pos, tn, params);
        	        		returnNode = instNode;
        	        	}
        				returnNode = returnNode.buildTypes(new TypeBuilder(ar.job(), ar.typeSystem(), ar.nodeFactory()));
        				returnNode = returnNode.disambiguate(ar);
        				if (Report.should_report("param", 1))
        					Report.report(1, "parameterized " + tn + " into " + returnNode);
    					return returnNode;
        			}
        			else if (jpt instanceof JifSubstClassType_c) {
        				JifSubstClassType_c jsct = (JifSubstClassType_c) jpt;
        				if (jsct.base().toString().contains("java.lang.String")) {

        				}
        			}

        		}
        	}
        }

    	return tn;
    }
}
