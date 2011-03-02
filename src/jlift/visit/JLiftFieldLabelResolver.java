/*
 * File          : JLiftFieldLabelResolver.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:54 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.visit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jif.extension.JifFieldDeclExt;
import jif.types.JifClassType;
import jif.types.JifFieldInstance;
import jif.types.JifTypeSystem;
import jif.types.LabelSubstitution;
import jif.types.Solver;
import jif.types.VarMap;
import jif.types.label.Label;
import jif.types.label.VarLabel;
import jif.visit.LabelChecker;
import jif.visit.LabelSubstitutionVisitor;
import jlift.types.label.SummaryVarLabel;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

public class JLiftFieldLabelResolver extends ContextVisitor {

    protected final Job job;
    protected final JifTypeSystem ts;
    protected VarMap bounds;
    protected Map fieldVarBounds;

	public JLiftFieldLabelResolver(Job job, JifTypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
		this.job = job;
		this.ts = ts;
	}

    @Override
	public NodeVisitor enterCall(Node n) throws SemanticException {
        if (n instanceof ClassMember && ! (n instanceof ClassDecl)) {
            return bypassChildren(n);
        }

        if (n instanceof ClassDecl) {
            this.fieldVarBounds = new HashMap();
        }

	if (n instanceof ClassBody) {
	    // labelcheck the class body
	    ClassBody d = (ClassBody) n;
	    labelCheckClassBody(d);
	}

        return this;
    }

    private void labelCheckClassBody(ClassBody d) throws SemanticException {
        JifClassType ct = (JifClassType) context().currentClassScope();

        LabelChecker lc = new LabelChecker(job, ts, nodeFactory(), false, false, false);

        if (lc == null) {
            throw new InternalCompilerError("Could not label check " +
                                            ct + ".", d.position());
        }

        for (Iterator i = d.members().iterator(); i.hasNext(); ) {
            ClassMember m = (ClassMember) i.next();

            if (m instanceof FieldDecl) {
            	Label fieldInstanceLabel = ((JifFieldInstance) ((FieldDecl) m).fieldInstance()).label();
            	//System.err.println("field instance label: " + fieldInstanceLabel + " (" + fieldInstanceLabel.getClass() + ")");
            	if (!(fieldInstanceLabel instanceof SummaryVarLabel)) {
					JifFieldDeclExt ext = (JifFieldDeclExt) m.ext();
            		ext.labelCheckField(lc, ct);
            	}
            }
        }

        Solver solver = lc.solver();
        this.bounds = solver.solve();
    }

    @Override
	public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        if (n instanceof FieldDecl) {
            FieldDecl f = (FieldDecl) n;
            JifFieldInstance fi = (JifFieldInstance) f.fieldInstance();
            if (fi.label() instanceof VarLabel && (!(fi.label() instanceof SummaryVarLabel) && !(((VarLabel) fi.label()).name().startsWith("inst(")))) {
                this.fieldVarBounds.put(fi.label(),
                                        bounds.boundOf((VarLabel)fi.label()));
            }

            Type lbledType = ts.labeledType(f.declType().position(),
                                            ts.unlabel(f.declType()),
                                            fi.label());
            return f.type(f.type().type(lbledType));

        }

        if (n instanceof ClassBody) {
            // need to go through the entire class body and replace the
            // variables that we have now solved for.
            LabelSubstitutionVisitor lsv =
                new LabelSubstitutionVisitor(new FieldVarLabelSubst(this.fieldVarBounds),
                                             false);
            n =  n.visitChildren(lsv);

        }
        return n;
    }

    private static class FieldVarLabelSubst extends LabelSubstitution {
        Map map;
        public FieldVarLabelSubst(Map fieldVarBounds) {
            map = fieldVarBounds;
        }
        @Override
		public Label substLabel(Label L) throws SemanticException {
            Label b = (Label)map.get(L);
            if (b != null) {
                return b;
            }
            return L;
        }
    }
}
