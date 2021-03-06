/*
 * File          : JLiftCallExt.java
 * Project       : jlift
 * Description   : New extension object for JLift calls.  Creates a SummaryCallHelper when
 *  			   appropriate.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:33:26 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jif.ast.JifUtil;
import jif.extension.CallHelper;
import jif.extension.ConstructorChecker;
import jif.extension.JifCallDel;
import jif.extension.JifCallExt;
import jif.translate.ToJavaExt;
import jif.types.JifClassType;
import jif.types.JifContext;
import jif.types.JifMethodInstance;
import jif.types.JifTypeSystem;
import jif.types.PathMap;
import jif.types.SemanticDetailedException;
import jif.types.label.Label;
import jif.visit.LabelChecker;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Receiver;
import polyglot.ast.Special;
import polyglot.types.SemanticException;
import polyglot.util.InternalCompilerError;

/** The Jif extension of the <code>Call</code> node.
 *
 *  @see polyglot.ast.Call_c
 */
public class JLiftCallExt extends JifCallExt
{
    public JLiftCallExt(ToJavaExt toJava) {
        super(toJava);
    }

    @Override
	public Node labelCheck(LabelChecker lc) throws SemanticException {
        Call me = (Call) node();

        JifContext A = lc.jifContext();
        A = (JifContext) me.del().enterScope(A);
        JifTypeSystem ts = lc.jifTypeSystem();

        if (A.checkingInits()) {
            // in the constructor prologue, the this object cannot be the receiver or an argument
            if (me.target() instanceof Expr && JifUtil.effectiveExpr((Expr)me.target()) instanceof Special) {
                throw new SemanticDetailedException("No methods may be called on \"this\" object in a constructor prologue.",
                                                    "In a constructor body before the call to the super class, no " +
                                                    "reference to the \"this\" object is allowed to escape. This means " +
                                                    "that no methods of the current object may be called.",
                                                    me.position());
            }
            for (Iterator iter = me.arguments().iterator(); iter.hasNext();) {
                Expr arg = (Expr)iter.next();
                if (JifUtil.effectiveExpr(arg) instanceof Special) {
                    throw new SemanticDetailedException("The \"this\" object cannot be used as a method argument in a constructor prologue.",
                                                        "In a constructor body before the call to the super class, no " +
                                                        "reference to the \"this\" object is allowed to escape. This means " +
                                                        "that the \"this\" object cannot be used as a method argument.",
                                                        arg.position());
                }

            }

        }

        List throwTypes = new ArrayList(me.del().throwTypes(ts));
        JifMethodInstance mi = (JifMethodInstance)me.methodInstance();
//        System.err.println("throwtypes for " + mi + ": " + throwTypes);

        if (mi.flags().isStatic()) {
            new ConstructorChecker().checkStaticMethodAuthority(mi, A, me.position());
        }

        Receiver target = (Receiver) lc.context(A).labelCheck(me.target());

        A = (JifContext) A.pushBlock();

        boolean npExc = false;
        Label objLabel = null;

        if (target instanceof Expr) {
            Expr e = (Expr) target;

            if (e.type() == null)
                throw new InternalCompilerError("Type of " + e + " is null", e.position());

            PathMap Xs = getPathMap(target);
            A.setPc(Xs.N(), lc);

            if (! (target instanceof Special)) {
                // a NPE may be thrown depending on the target.
                npExc = (!((JifCallDel)node().del()).targetIsNeverNull());
                objLabel = Xs.NV();
                if (!ts.isUncheckedException(ts.NullPointerException())) {
                	// DHK: don't include NPEs
                	A.setPc(Xs.NV(), lc);
                }
            }
            else {
                objLabel = ((JifClassType) lc.context().currentClass()).thisLabel();
            }
        }

        CallHelper helper = lc.createCallHelper(objLabel, target, mi.container(), mi, me.arguments(), node().position());

        LabelChecker callLC = lc.context(A);
        helper.checkCall(callLC, throwTypes, npExc);


        // now use the call helper to bind the var labels that were created
        // during type checking of the call (see JifCallDel#typeCheck)
        JLiftCallDel del = (JLiftCallDel)me.del();
        helper.bindVarLabels(callLC,
                             del.receiverVarLabel(),
                             del.argVarLabels(),
                             del.paramVarLabels());


        A = (JifContext) A.pop();

        //subst arguments of inst_type
        if (helper.returnType() != me.type()) {
            me = (Call) me.type(helper.returnType());
        }

        checkThrowTypes(throwTypes);
        return updatePathMap(me.target(target).arguments(helper.labelCheckedArgs()), helper.X());
    }
}
