/*
 * File          : JLiftFieldDeclDel.java
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

import jif.extension.JifFieldDeclDel;
import jif.types.ConstArrayType;
import jif.types.JifFieldInstance;
import jif.types.JifTypeSystem;
import jif.types.SemanticDetailedException;
import jif.visit.ConstChecker;
import jlift.types.JLiftTypeSystem;
import polyglot.ast.Expr;
import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.types.ArrayType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.TypeChecker;

public class JLiftFieldDeclDel extends JifFieldDeclDel {

	@Override
	public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
		Node superDisambiguated = super.disambiguate(ar);

        JifTypeSystem jts = (JifTypeSystem)ar.typeSystem();

        FieldDecl originalDecl = (FieldDecl) node();

        FieldDecl n = (FieldDecl)superDisambiguated;
        JifFieldInstance fi = (JifFieldInstance) n.fieldInstance();

        if (originalDecl.declType().isCanonical() &&
        	!jts.isLabeled(originalDecl.declType())) {
        	fi.setLabel(jts.defaultSignature().defaultFieldLabel(n));
        }

		return superDisambiguated;
	}

    @Override
	public Node typeCheck(TypeChecker tc) throws SemanticException {
        FieldDecl fd = (FieldDecl) node();

        if (fd.name().indexOf('$') >= 0 &&
                !((JifTypeSystem)tc.typeSystem()).isMarkerFieldName(fd.name())) {
            // check that the field isn't one of the special marker fields
            throw new SemanticException("Field names can not contain the character '$'.");
        }

        if (fd.fieldInstance().flags().isStatic()) {
            Expr init = fd.init();

            if (init != null) {
                // if the static field has an initializer, then the
                // initialization expression must be constant.
                ConstChecker cc = new ConstChecker(tc.context().currentClass());
                init.visit(cc);
                if (!cc.isConst() && !((JLiftTypeSystem) tc.typeSystem()).allowSmallLeaks()) {
                    throw new SemanticDetailedException("Jif does not support " +
                                                        "static fields without constant initializers.",
                                                        "The initializer of a static field of a class is " +
                                                        "executed when the class is loaded. Information may be " +
                                                        "leaked if the time of class loading is observable. " +
                                                        "To prevent this covert channel, Jif requires static " +
                                                        "field initializers to be constant.",
                                                        fd.position());
                }
            }
        }

        // if the declared type is an array type, make sure it is the same all the way through
        if (fd.fieldInstance().type().isArray()) {
            JifTypeSystem jts = (JifTypeSystem)tc.typeSystem();
            ArrayType at = jts.unlabel(fd.fieldInstance().type()).toArray();
            checkArrayTypeConsistency(at);
        }
        // DHK: calling the super class will give us an error, as we are more
        // permissive (we allow non-constant static fields), so we explicitly call
        // our grandparent
        return jl().typeCheck(tc);
    }

    // copied from JifLocalDeclDel
    void checkArrayTypeConsistency(ArrayType at) throws SemanticException {
        boolean isConst = false;
        if (at instanceof ConstArrayType) {
            ConstArrayType cat = (ConstArrayType)at;
            isConst = cat.isConst();
        }
        JifTypeSystem jts = (JifTypeSystem)at.typeSystem();
        Type base = jts.unlabel(at.base());
        if (base.isArray()) {
            boolean baseConst = false;
            if (base instanceof ConstArrayType) {
                ConstArrayType cat = (ConstArrayType)base;
                baseConst = cat.isConst();
            }
            if (isConst != baseConst) {
                throw new SemanticException("A const modifier for an array must apply to all dimensions.");
            }
            checkArrayTypeConsistency(base.toArray());
        }
    }

//    @Override
//    public NodeVisitor exceptionCheckEnter(ExceptionChecker ec)
//    		throws SemanticException {
//    	return ec.push();
//    }
//
//    @Override
//    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
//    	return this.node();
//    }
}
