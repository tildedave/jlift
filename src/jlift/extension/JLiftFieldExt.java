package jlift.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jif.ast.JifInstantiator;
import jif.ast.JifNodeFactory;
import jif.extension.JifFieldDel;
import jif.extension.JifFieldExt;
import jif.translate.ToJavaExt;
import jif.types.JifContext;
import jif.types.JifTypeSystem;
import jif.types.NamedLabel;
import jif.types.PathMap;
import jif.types.label.Label;
import jif.types.label.VarLabel;
import jif.visit.LabelChecker;
import jlift.JLiftOptions;
import jlift.types.JLiftLabelConstraint;
import jlift.types.JLiftTypeSystem;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.Receiver;
import polyglot.ast.Special;
import polyglot.types.ClassType;
import polyglot.types.FieldInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class JLiftFieldExt extends JifFieldExt {

	public JLiftFieldExt(ToJavaExt toJava) {
		super(toJava);
	}


    @Override
	public Node labelCheckIncrement(LabelChecker lc) throws SemanticException
	{
	    JifNodeFactory nf;
		try {
			nf = (JifNodeFactory) lc.nodeFactory().getClass().newInstance();
		} catch (InstantiationException e) {
			throw new InternalCompilerError(e);
		} catch (IllegalAccessException e) {
			throw new InternalCompilerError(e);
		}
	    Field fe = (Field) node();
	    Position pos = fe.position();
	    FieldAssign fae = nf.FieldAssign(pos, fe, Assign.ADD_ASSIGN,
	                                     nf.IntLit(pos, IntLit.INT, 1));

	    fae = (FieldAssign)lc.labelCheck(fae);

	    return fae.left();
	}

	@Override
	public Node labelCheck(LabelChecker lc) throws SemanticException
	{
		JifContext A = lc.jifContext();
		JifTypeSystem ts = lc.jifTypeSystem();

		Field fe = (Field) node();
		Receiver target = checkTarget(lc, fe);
		PathMap Xe = getPathMap(target);

		List throwTypes = new ArrayList(fe.del().throwTypes(ts));

		if (! ((JifFieldDel)node().del()).targetIsNeverNull()) {
			// null pointer exception may be thrown.
			Type npe = ts.NullPointerException();
			checkAndRemoveThrowType(throwTypes, npe);
			Xe = Xe.exc(Xe.NV(), npe);
		}

		// Must be done after visiting target to get PC right.

		// Find the field instance again. This ensures that
		// we have the correctly instantiated type, as label checking
		// of the target may have produced a new type for the target.
		ReferenceType targetType = targetType(ts, A, target, fe);
		FieldInstance fi = ts.findField(targetType, fe.name());
		fe = fe.fieldInstance(fi);

		Label L = ts.labelOfField(fi, A.pc());

		if (target instanceof Expr) {
			Label objLabel = getPathMap(target).NV();

			/*
			if (targetType instanceof jif.types.JifSubstClassType_c) {
			    // DHK: not sure why this isn't being checked properly.  I feel like
			    // possibly I commented out the line of code that made this work 
			    // once upon a time and now I can't find it again.
			    jif.types.JifSubstClassType_c jst = (jif.types.JifSubstClassType_c) targetType;
			}
			*/


			L = JifInstantiator.instantiate(L, A, (Expr)target, targetType, objLabel);

			Type ft = JifInstantiator.instantiate(fi.type(), A, (Expr)target, targetType, objLabel);

			fe = (Field)fe.type(ft);

		}

		if (JLiftOptions.getInstance().shouldUsePathVariables()) {
			JLiftTypeSystem jts = (JLiftTypeSystem) lc.jifTypeSystem();
			VarLabel fieldLabel = jts.freshLabelVariable(fi.position(), "field:" + fi.name(), "variable for field " + fi.name());
			lc.constrain(new NamedLabel("", fieldLabel), JLiftLabelConstraint.DEFINE, new NamedLabel("", L), lc.context().labelEnv(), node().position());
			L = fieldLabel;
		}

		PathMap X = Xe.NV(lc.upperBound(L, Xe.NV()));

		checkThrowTypes(throwTypes);
		return updatePathMap(fe.target(target), X);
	}

	static protected Receiver checkTarget(LabelChecker lc, Field fe)
	throws SemanticException
	{
		JifTypeSystem ts = lc.jifTypeSystem();

		if (! (fe.target() instanceof Expr)) {
			JifContext A = lc.context();
			return (Receiver) updatePathMap(fe.target(), ts.pathMap().N(A.pc()).NV(A.pc()));
		}

		Expr target = (Expr) lc.labelCheck(fe.target());
		return target;
	}

	static protected ReferenceType targetType(JifTypeSystem ts, JifContext A,
			Receiver target, Field fe)
	{
		String name = fe.name();
		ReferenceType rt = A.currentClass();
		if (target instanceof Special) {
			Special st = (Special) target;
			if (st.kind() == Special.SUPER)
				rt = (ReferenceType) A.currentClass().superType();
			else {
				rt = findSuperClassContainerForFieldName(name, rt);
				try {
					if (rt == null) {
						if (A.currentClass().isMember()) {
							rt = findContainingContainerForFieldName(name, A.currentClass());
						}
					}

					FieldInstance fi = ts.findField(rt, name);
					rt = fi.container();
				}
				catch (SemanticException x) {
					throw new InternalCompilerError("Cannot find the field "
							+ name + " in " + rt, x);
				}
			}
		}
		else {
			rt = (ReferenceType) ts.unlabel(target.type());
		}

		return rt;
	}

	private static ReferenceType findContainingContainerForFieldName(
			String name, ClassType rt) {
		boolean found = false;
		do {
			for (Iterator i = rt.fields().iterator(); i.hasNext(); ) {
				FieldInstance fi = (FieldInstance) i.next();
				if (name.equals(fi.name())) {
					found = true;
					break;
				}
			}
			if (found)
				break;

			rt = (ClassType) rt.container();
		} while (rt.isMember());

		return rt;
	}

	private static ReferenceType findSuperClassContainerForFieldName(String name,
			ReferenceType rt) {
		boolean found = false;
		do {
			for (Iterator i = rt.fields().iterator(); i.hasNext(); ) {
				FieldInstance fi = (FieldInstance) i.next();
				if (name.equals(fi.name())) {
					found = true;
					break;
				}
			}
			if (found)
				break;

			rt = (ReferenceType) rt.superType();
		} while (rt != null);
		return rt;
	}
}
