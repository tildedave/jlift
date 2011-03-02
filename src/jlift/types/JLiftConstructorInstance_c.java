package jlift.types;

import java.util.List;

import jif.types.JifConstructorInstance_c;
import jif.types.JifTypeSystem;
import jif.types.label.Label;
import polyglot.types.ClassType;
import polyglot.types.Flags;
import polyglot.util.Position;

public class JLiftConstructorInstance_c extends JifConstructorInstance_c {

	public JLiftConstructorInstance_c(JifTypeSystem ts, Position pos,
			ClassType container, Flags flags, Label pcBound,
			boolean isDefaultPCBound, Label returnLabel,
			boolean isDefaultReturnLabel, List formalTypes,
			List formalArgLabels, List excTypes, List constraints) {
		super(ts, pos, container, flags, pcBound, isDefaultPCBound, returnLabel,
				isDefaultReturnLabel, formalTypes, formalArgLabels, excTypes,
				constraints);
	}

	@Override
	public int hashCode() {
		return container.hashCode() + flags.hashCode() + position.hashCode();
	}

}
