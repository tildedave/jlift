/*
 * File          : JLiftTypeSystem.java
 * Project       : jlift
 * Description   : Extension of JifTypeSystem.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:40:32 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.List;

import jif.types.JifTypeSystem;
import jlift.types.label.JLiftVarLabel;
import jlift.types.label.SummaryVarLabel;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.util.Position;

public interface JLiftTypeSystem extends JifTypeSystem {

	/** get a new summary variable to stick in place of top label */
	SummaryVarLabel freshSummaryVariable(Position position, String argName, String string);
	// various tools to make our static analysis play nice with inheritance and interfaces,
	// hopefully
	List<ReferenceType> getSubtypes(ReferenceType refType);
	List<ProcedureInstance> getProceduresInSubtypes(ProcedureInstance pi);
	List<ReferenceType> getSubtypesAndImplementers(ProcedureInstance pi);

	/** instance label is the label on an instance of a class; for example,
	 * <pre>
	 * class C { int a; }
	 *
	 * new C().a; -- the label on this type is an instance variable.
	 * </pre>
	 * Constrast with:
	 * <pre>
	 * class C { int{Alice:} a; }
	 *
	 * new C().a; -- the label on this type is {Alice:} (actually the
	 * label on new C() joined w/Alice)
	 * </pre>
	 * this is to allow our analysis to track labels inside of classes; otherwise
	 * we would need to label EVERY field member.
	 *
	 * This is sort of like parameterizing every class.
	 * @param name: the name of the label
	 * @param position: the position of the label
	 */
//	Label instanceLabel(String name, Position position);

	/**
	 *
	 * @param type to check
	 * @return true or false (let's ignore null pointer exceptions et al)
	 */
	boolean exceptionTypeShouldAffectSecurity(Type type);
	boolean allowSmallLeaks();

	/**
	 * utility method for creating a label constraint
	 * @param l1
	 * @param kind
	 * @param l2
	 * @param labelEnv
	 * @param pos
	 * @return
	 */
//	JLiftLabelConstraint labelConstraint(Label l1, LabelConstraint.Kind kind, Label l2,
//			LabelEnv labelEnv, Position pos, LabelConstraintMessage msg, boolean report);
//	JLiftLabelConstraint labelConstraint(Label newLhs, Kind kind, Label newRhs,
//			LabelEnv env, Position position, Node node, LabelConstraintMessage labelConstraintMessage, boolean b);
//	JLiftLabelConstraint definitionalLabelConstraint(VarLabel pathVar, Label l,
//			LabelEnv labelEnv, Position pos);
//	JLiftLabelConstraint definitionalLabelConstraint(VarLabel pathVar, Label l,
//			LabelEnv labelEnv, Position pos, Node node);

	JLiftVarLabel freshLabelVariable(Position pos, String s, String description);
	JLiftSolver createRehofSolver(String name);

	JLiftDefaultSignature defaultSignature();
}
