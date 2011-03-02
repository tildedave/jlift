package jlift.types;

import jlift.util.NumberProvider;
import polyglot.types.PrimitiveType;
import polyglot.types.PrimitiveType_c;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;

public class PrincipalPrimitiveType_c extends PrimitiveType_c {

	protected int uniqId;

	public PrincipalPrimitiveType_c(TypeSystem ts) {
		super(ts, new PrimitiveType.Kind("principal"));
		uniqId = NumberProvider.getNextNumber();
	}

	@Override
	public String toString() {
		return super.toString() + "(" + uniqId + ")";
	}

	@Override
	public boolean equalsImpl(TypeObject t) {
		if (t instanceof PrincipalPrimitiveType_c)
			return true;
		else if (t instanceof PrimitiveType_c) {
			PrimitiveType_c pt = (PrimitiveType_c) t;
			//System.out.println("comparing " + this + " and " + t);
			if (pt.kind().toString().equals("principal")) {
				//System.err.println("equal");
				return true;
			}
			//System.err.println("not equal");
		}

		return false;
	}

	public int id() {
		return uniqId;
	}
}
