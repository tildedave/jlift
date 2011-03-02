package jlift.extension;

import jif.extension.JifCastDel;
import jif.types.JifPolyType;
import jif.types.JifSubstType;
import jlift.types.JLiftTypeSystem;
import polyglot.ast.Cast;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.visit.TypeChecker;

public class JLiftCastDel extends JifCastDel {

	@Override
	public Node typeCheck(TypeChecker tc) throws SemanticException {
		Cast c = (Cast)this.node();
		JLiftTypeSystem ts = (JLiftTypeSystem)tc.typeSystem();

		if (ts.allowSmallLeaks()) {
			Type castType = c.castType().type();

			if (ts.isLabeled(castType)) {
				throw new SemanticException("Cannot cast to a labeled type.", c.position());
			}

			if (!ts.isParamsRuntimeRep(castType)) {
				if ((castType instanceof JifSubstType && !((JifSubstType)castType).actuals().isEmpty()) ||
						(castType instanceof JifPolyType && !((JifPolyType)castType).params().isEmpty()))
					throw new SemanticException("Cannot cast to " + castType +
							", since it does " +
							"not represent the parameters at runtime.",
							c.position());
			}

			//DHK: this is only used for outputting -- shouldn't be a problem for Jlift
			//as it exists now
			//this.isToSubstJifClass = (castType instanceof JifSubstType && !((JifSubstType)castType).actuals().isEmpty());

			return jl().typeCheck(tc);
		}
		else
			return super.typeCheck(tc);
	}
}
