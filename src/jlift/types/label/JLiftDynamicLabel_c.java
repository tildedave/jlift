package jlift.types.label;

import java.util.Set;

import jif.translate.LabelToJavaExpr;
import jif.types.JifTypeSystem;
import jif.types.hierarchy.LabelEnv;
import jif.types.hierarchy.LabelEnv.SearchState;
import jif.types.label.AccessPath;
import jif.types.label.DynamicLabel_c;
import jif.types.label.Label;
import jlift.util.NumberProvider;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.util.Position;

public class JLiftDynamicLabel_c extends DynamicLabel_c {

	protected int uniqueId;
	protected boolean incomparable;

	public JLiftDynamicLabel_c(AccessPath path, JifTypeSystem ts, Position pos,
			LabelToJavaExpr trans) {
		super(path, ts, pos, trans);

		uniqueId = NumberProvider.getNextNumber();
		if (path().toString().contains("incomparable")) {
			incomparable = true;
		}
		else
			incomparable = false;
		Type unlabeled = ts.unlabel(path.type());
//		if (unlabeled != null)
//			System.out.println("associate dynamic label @ " + this.position + "(" + this + ") with " + ((LabelPrimitiveType_c) (unlabeled)).id());
	}

    @Override
	public String componentString(Set printedLabels) {
        if (Report.should_report(Report.debug, 1)) {
            return "<dynamic " + path() + ">";
        }
        return "*"+path() + "@" + uniqueId;
    }

	public int id() {
		return uniqueId;
	}

	@Override
	public boolean equalsImpl(TypeObject o) {
		return super.equalsImpl(o);
	}

	@Override
	public boolean leq_(Label L, LabelEnv env, SearchState state) {
		// superclass behavior just cares about access path equivalence, which might hold here...
		if (L instanceof JLiftDynamicLabel_c) {
			JLiftDynamicLabel_c that = (JLiftDynamicLabel_c) L;

			if (this.incomparable == false)
				return false;
			if (that.incomparable == false)
				return false;

			return that.id() == id();
		}
		else {
			return super.leq_(L, env, state);
		}
	}
}