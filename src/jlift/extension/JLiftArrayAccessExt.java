package jlift.extension;

import jif.ast.JifNodeFactory;
import jif.extension.JifArrayAccessExt;
import jif.translate.ToJavaExt;
import jif.visit.LabelChecker;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.Assign;
import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class JLiftArrayAccessExt extends JifArrayAccessExt {

	public JLiftArrayAccessExt(ToJavaExt toJava) {
		super(toJava);
	}

	@Override
	public Node labelCheckIncrement(LabelChecker lc) throws SemanticException
    {
        JifNodeFactory nf;
		try {
			nf = (JifNodeFactory) lc.nodeFactory().getClass().newInstance();
		} catch (InstantiationException e) {
			throw new InternalCompilerError(e);
		} catch (IllegalAccessException e) {
			throw new InternalCompilerError(e);
		}

        ArrayAccess ae = (ArrayAccess) node();
        Position pos = ae.position();
        ArrayAccessAssign aae = nf.ArrayAccessAssign(pos, ae, Assign.ADD_ASSIGN,
                                                     nf.IntLit(pos, IntLit.INT, 1));

        aae = (ArrayAccessAssign)lc.labelCheck(aae);

        return aae.left();
    }

}
