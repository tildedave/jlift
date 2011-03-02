package jlift.extension;

import jif.ast.JifUtil;
import jif.extension.JifAssignExt;
import jif.translate.ToJavaExt;
import jif.types.JifContext;
import jif.types.SemanticDetailedException;
import jif.visit.LabelChecker;
import polyglot.ast.Assign;
import polyglot.ast.Node;
import polyglot.ast.Special;
import polyglot.types.SemanticException;
import polyglot.util.InternalCompilerError;

public class JLiftAssignExtMixin extends JifAssignExt {

	public JLiftAssignExtMixin(ToJavaExt a) {
		super(a);
	}

    public Node labelCheck(LabelChecker lc, Node node, JLiftAssignMixin parentExt) throws SemanticException {
        Assign a = (Assign) node;

        JifContext A = lc.jifContext();
        A = (JifContext) a.del().enterScope(A);

        if (A.checkingInits()) {
            // in the constructor prologue, the this object cannot value on the RHS
            if (JifUtil.effectiveExpr(a.right()) instanceof Special) {
                throw new SemanticDetailedException("The \"this\" object cannot be the value assigned in a constructor prologue.",
                                                    "In a constructor body before the call to the super class, no " +
                                                    "reference to the \"this\" object is allowed to escape. This means " +
                                                    "that the right hand side of an assignment is not allowed to refer " +
                                                    "to the \"this\" object.",
                                                    a.right().position());
            }
        }

        if (!A.updateAllowed(a.left())) {
            throw new SemanticException("Cannot assign to \"" + a.left() + "\" in this context.", a.left().position());
        }

        Assign checked = (Assign)parentExt.lhsLabelCheck(lc);

        // Only need subtype constraints for "=" operator.  No other
        // assignment operator works with class types.
        if (a.operator() == Assign.ASSIGN) {
            // Must check that the RHS is a subtype of the LHS.
            // Most of this is done in typeCheck, but if lhs and rhs are
            // instantitation types, we must add constraints for the labels.
            JLiftSubtypeChecker subtypeChecker = new JLiftSubtypeChecker(checked.left().type(),
                                                               		checked.right().type(), null);
            subtypeChecker.addSubtypeConstraints(lc, a.position());
        }

        return checked;
    }

	@Override
	protected Node labelCheckLHS(LabelChecker lc) throws SemanticException {
		throw new InternalCompilerError("mixin should not have labelcheckLHS invoked on it");
	}

}
