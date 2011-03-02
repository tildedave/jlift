package jlift.types.principal;

import jif.translate.PrincipalToJavaExpr;
import jif.types.JifTypeSystem;
import jif.types.label.AccessPath;
import jif.types.principal.DynamicPrincipal_c;
import jlift.types.PrincipalPrimitiveType_c;
import jlift.util.NumberProvider;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.util.Position;

public class JLiftDynamicPrincipal_c extends DynamicPrincipal_c {

	protected int uniqId;
	private boolean incomparable;

	public JLiftDynamicPrincipal_c(AccessPath path, JifTypeSystem ts,
			Position pos, PrincipalToJavaExpr toJava) {
		super(path, ts, pos, toJava);
		//uniqId = NumberProvider.getNextNumber();
		//System.out.println("new dynamic principal: " + this);
		if (path.toString().contains("incomparable")) {
			this.incomparable = true;
		}
		else {
			this.incomparable = false;
		}
		Type unlabeled = ts.unlabel(path.type());
		if (unlabeled != null && unlabeled instanceof PrincipalPrimitiveType_c) {
			this.uniqId = ((PrincipalPrimitiveType_c) (unlabeled)).id();
			//System.out.println("associate dynamic principal @ " + this.position + "(" + this + ") with " + uniqId);
		}
	}

    @Override
	public String toString() {
        if (Report.should_report(Report.debug, 1)) {
            return "<pr-dynamic " + path() + ">";
        }
        return path().toString() + "@" + uniqId;
    }

    public int id() {
    	if (this.incomparable) {
    		return NumberProvider.getNextNumber();
    	}
    	return uniqId;
    }

    public JLiftDynamicPrincipal_c id(int id) {
    	JLiftDynamicPrincipal_c prin = new JLiftDynamicPrincipal_c(path(), (JifTypeSystem) ts, position(), null);
    	prin.uniqId = id;

    	return prin;
    }

    @Override
    public boolean equalsImpl(TypeObject o) {
    	if (o instanceof JLiftDynamicPrincipal_c) {
    		JLiftDynamicPrincipal_c jdp = (JLiftDynamicPrincipal_c) o;
    		//System.out.println("comparing " + this + " with " + jdp);
    		return jdp.id() == id();
    	}
    	return super.equalsImpl(o);
    }
}
