package jlift.types.label;

import jif.types.JifTypeSystem;
import jif.types.hierarchy.LabelEnv;
import jif.types.hierarchy.LabelEnv.SearchState;
import jif.types.label.AccessPath;
import jif.types.label.Label;
import jlift.types.LabelPrimitiveType_c;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.util.Position;


public class JLiftInstLabel_c extends JLiftDynamicLabel_c {

	protected int id;
	protected JLiftDynamicLabel_c dlc;

	public JLiftInstLabel_c(JLiftDynamicLabel_c dlc) {
		super(dlc.path(), (JifTypeSystem) dlc.typeSystem(), dlc.position(), null);
		this.dlc = dlc;
		Type unlabeled = ((JifTypeSystem) this.ts).unlabel(dlc.path().type());
		if (unlabeled != null)
			id = ((LabelPrimitiveType_c) unlabeled).id();
	}

	public JLiftInstLabel_c(int id, AccessPath path, JifTypeSystem ts, Position pos) {
		super(path, ts, pos, null);
		this.id = id;
	}

	@Override
	public String toString() {
//		if (dlc != null)
//			return "instlab(*" + this.dlc.path() + ":" + id + ")";
		return "instlab(" + id + ")";
	}

	public JLiftInstLabel_c subst(int oldId, int newId) {
		if (id != oldId)
			return this;

		JLiftInstLabel_c substLabel = new JLiftInstLabel_c(this.dlc);
		substLabel.id = newId;

		return substLabel;
	}

	@Override
	public int id() {
		return this.id;
	}

	@Override
	public boolean leq_(Label L, LabelEnv env, SearchState state) {

		if (L instanceof JLiftInstLabel_c) {
			JLiftInstLabel_c jil = (JLiftInstLabel_c) L;

			if (jil.dlc.incomparable)
				return false;
			if (this.dlc.incomparable)
				return false;

			if (jil.id() == id()) {
				// TODO: appeal to dynamic label analysis!
				return true;
			}
		}
		return super.leq_(L, env, state);
	}

	@Override
	public boolean equalsImpl(TypeObject o) {
		if (o instanceof JLiftInstLabel_c) {
			JLiftInstLabel_c jil = (JLiftInstLabel_c) o;
			if (jil.id() == id()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return id() * 7;
	}
}
