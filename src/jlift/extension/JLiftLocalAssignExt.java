package jlift.extension;

import java.util.ArrayList;
import java.util.List;

import jif.extension.JifLocalAssignExt;
import jif.translate.ToJavaExt;
import jif.types.JifContext;
import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.LabelConstraintMessage;
import jif.types.NamedLabel;
import jif.types.PathMap;
import jif.types.label.Label;
import jif.visit.LabelChecker;
import jlift.JLiftOptions;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;

public class JLiftLocalAssignExt extends JifLocalAssignExt implements JLiftAssignMixin {

	protected JLiftAssignExtMixin mixin;

	public JLiftLocalAssignExt(ToJavaExt toJava) {
		super(toJava);
		mixin = new JLiftAssignExtMixin(toJava);
	}

	@Override
	public Node labelCheck(LabelChecker lc) throws SemanticException {
		return mixin.labelCheck(lc, node(), this);
	}

    @Override
	public Node labelCheckLHS(LabelChecker lc)
    throws SemanticException
    {
        final Assign assign = (Assign) node();
        Local lve = (Local) assign.left();

        JifTypeSystem ts = lc.jifTypeSystem();
        JifContext A = lc.jifContext();
        A = (JifContext) lve.del().enterScope(A);

        List throwTypes = new ArrayList(assign.del().throwTypes(ts));

        final LocalInstance li = lve.localInstance();

        Label L = ts.labelOfLocal(li, A.pc());

        Expr rhs = (Expr) lc.context(A).labelCheck(assign.right());
        PathMap Xr = rhsPathMap(lc.context(A), rhs, throwTypes);
        PathMap X;

        if (assign.operator() != Assign.ASSIGN) {
            PathMap Xv = ts.pathMap();
            Xv = Xv.N(A.pc());
            Xv = Xv.NV(lc.upperBound(L, A.pc()));

            if (assign.throwsArithmeticException()) {
                Type arithExc = ts.ArithmeticException();
                checkAndRemoveThrowType(throwTypes, arithExc);
                X = Xv.join(Xr).exc(Xr.NV(), arithExc);
            }
            else {
                X = Xv.join(Xr);
            }
        }
        else {
            X = Xr;
        }

        lc.constrain(new NamedLabel("rhs.nv",
                                    "label of successful evaluation of right hand of assignment",
                                    X.NV()),
                    LabelConstraint.LEQ,
                    new NamedLabel("label of var " + li.name(), L),
                    A.labelEnv(),
                    lve.position(),
                    new LabelConstraintMessage() {
            @Override
			public String msg() {
                return "Label of right hand side not less " +
                "restrictive than the label for local variable " +
                li.name();
            }
            @Override
			public String detailMsg() {
                return "More information is revealed by the successful " +
                "evaluation of the right hand side of the " +
                "assignment than is allowed to flow to " +
                "the local variable " + li.name() + ".";
            }
            @Override
			public String technicalMsg() {
                return "Invalid assignment: path NV of rhs is " +
                "not less restrictive than the declared label " +
                "of the local variable <" + li.name() + ">.";
            }
        }
        );

        if (JLiftOptions.getInstance().safeMutateConstraintSet) {
        	// also constrain PC label
        	lc.constrain(new NamedLabel("pc", "program counter label for assignment", A.pc()),
        			LabelConstraint.LEQ,
        			new NamedLabel("label of var " + li.name(), L),
        			A.labelEnv(),
        			lve.position(),
        			new LabelConstraintMessage() {
        		@Override
        		public String msg() {
        			return "Program counter not less " +
        			"restrictive than the label for local variable " +
        			li.name();
        		}
        	}
        	);
        }

        Expr lhs = (Expr) updatePathMap(lve, X);
        checkThrowTypes(throwTypes);
        return updatePathMap(assign.right(rhs).left(lhs), X);
    }

	public Node lhsLabelCheck(LabelChecker lc) throws SemanticException {
		return labelCheckLHS(lc);
	}
}
