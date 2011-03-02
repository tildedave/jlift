package jlift.extension;

import jif.visit.LabelChecker;
import polyglot.ast.Node;
import polyglot.types.SemanticException;

public interface JLiftAssignMixin {

	public Node lhsLabelCheck(LabelChecker lc) throws SemanticException;

}
