package jlift.types;

import jlift.util.NumberProvider;
import polyglot.types.PrimitiveType;
import polyglot.types.PrimitiveType_c;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;

public class LabelPrimitiveType_c extends PrimitiveType_c {

	protected int uniqId;
	static PrimitiveType.Kind LABEL = new PrimitiveType.Kind("label");

	public LabelPrimitiveType_c(TypeSystem ts) {
		super(ts, LABEL);
		uniqId = NumberProvider.getNextNumber();
		System.err.println("new label " + uniqId);
	}

	@Override
	public String toString() {
		return super.toString() + "(" + uniqId + ")";
	}

	@Override
	public boolean equalsImpl(TypeObject t) {
		if (t instanceof LabelPrimitiveType_c) {
			//System.err.println("need " + this + "(" + this.position + ") == " + t + " (" + t.position() + ")");
			return true;
		}
		else if (t instanceof PrimitiveType_c) {
			PrimitiveType_c pt = (PrimitiveType_c) t;
			//System.out.println("comparing " + this + " and " + t);
			if (pt.kind().toString().equals("label")) {
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
