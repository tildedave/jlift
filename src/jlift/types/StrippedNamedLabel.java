package jlift.types;

import java.util.HashMap;

import jif.types.NamedLabel;
import jif.types.label.Label;
import jif.visit.LabelChecker;

public class StrippedNamedLabel extends NamedLabel {

	public static HashMap emptyMap = new HashMap();

	public StrippedNamedLabel(Label l) {
		super("no name", l);
		this.nameToDescrip = emptyMap;
		this.nameToLabels = emptyMap;
	}

	public StrippedNamedLabel(NamedLabel nl) {
		super("no name", nl.label());
		this.nameToDescrip = emptyMap;
		this.nameToLabels = emptyMap;
	}

	@Override
	public NamedLabel join(LabelChecker lc, String name, String descrip, Label l) {
        if (label==null) {
            label = l;
        }
        else {
            label = lc.upperBound(label, l);
        }

        return this;
	}

	@Override
	public NamedLabel meet(LabelChecker lc, String name, String descrip, Label l) {
		if (label==null) {
            label = l;
        }
        else {
            label = lc.lowerBound(label, l);
        }

        return this;
	}

	public void setLabel(Label l) {
		this.label = l;
	}

}
