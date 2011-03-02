package jlift.extension;

import jif.extension.JifInstanceOfDel;
import jif.types.JifPolyType;
import jif.types.JifSubstType;
import jif.types.JifTypeSystem;
import polyglot.ast.Instanceof;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.visit.TypeChecker;

public class JLiftInstanceofDel extends JifInstanceOfDel {

	private boolean isToSubstJifClass;

	@Override
	public boolean isToSubstJifClass() {
		return isToSubstJifClass;
	}

	@Override
	public Node typeCheck(TypeChecker tc) throws SemanticException {
        // prevent instanceof to arrays of parameterized types
        Instanceof io = (Instanceof)this.node();
        JifTypeSystem ts = (JifTypeSystem)tc.typeSystem();
        Type compareType = io.compareType().type();
        if (ts.isLabeled(compareType)) {
            throw new SemanticException("Cannot perform instanceof on a labeled type.", io.position());
        }

        if (!ts.isParamsRuntimeRep(compareType)) {
            if ((compareType instanceof JifSubstType && !((JifSubstType)compareType).actuals().isEmpty()) ||
                (compareType instanceof JifPolyType && !((JifPolyType)compareType).params().isEmpty()))
            throw new SemanticException("Cannot perform instanceof on " + compareType +
                                        ", since it does " +
                                        "not represent the parameters at runtime.",
                                        io.position());
        }

        this.isToSubstJifClass = (compareType instanceof JifSubstType && ((JifSubstType)compareType).entries().hasNext());

        ts.labelTypeCheckUtil().typeCheckType(tc, compareType);

		return jl().typeCheck(tc);
	}
}
