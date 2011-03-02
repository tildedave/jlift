/*
 * File          : JLiftMethodInstance_c.java
 * Project       : jlift
 * Description   : Specialized Method Instance.  Remove this as soon as we
 * 				   update backend to jif 3.1.1.  (Needed to compile sort-of 1.5
 * 				   code)
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:57:02 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.List;

import jif.types.JifMethodInstance_c;
import jif.types.JifTypeSystem;
import jif.types.label.Label;
import jlift.types.label.SummaryVarLabel;
import polyglot.main.Report;
import polyglot.types.Flags;
import polyglot.types.LazyClassInitializer;
import polyglot.types.MethodInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.Position;

public class JLiftMethodInstance_c extends JifMethodInstance_c {

	private SummaryVarLabel summaryVarLabel;

	public JLiftMethodInstance_c(JifTypeSystem ts, Position pos,
			ReferenceType container, Flags flags, Type returnType, String name,
			Label pcBound, boolean isDefaultPCBound, List formalTypes,
			List formalArgLabels, Label returnLabel,
			boolean isDefaultReturnLabel, List excTypes, List constraints) {
		// nothing interesting here
		super(ts, pos, container, flags, returnType, name, pcBound, isDefaultPCBound,
				formalTypes, formalArgLabels, returnLabel, isDefaultReturnLabel,
				excTypes, constraints);
	}

    //DHK: cribbed this from newer polyglot source --
	//we don't need this any more
    @Override
	public boolean canOverrideImpl(MethodInstance mj, boolean quiet) throws SemanticException {
        MethodInstance mi = this;

        if (!(mi.name().equals(mj.name()) && mi.hasFormals(mj.formalTypes()))) {
            if (quiet) return false;
            throw new SemanticException(mi.signature() + " in " + mi.container() +
                                        " cannot override " +
                                        mj.signature() + " in " + mj.container() +
                                        "; incompatible " +
                                        "parameter types",
                                        mi.position());
        }

        // HACK: Java5 allows return types to be covariant.  We'll allow covariant
        // return if we mj is defined in a class file.
        boolean allowCovariantReturn = false;

        if (mj.container() instanceof ParsedClassType) {
            ParsedClassType ct = (ParsedClassType) mj.container();
            if (ct.initializer() instanceof LazyClassInitializer) {
                LazyClassInitializer init = (LazyClassInitializer) ct.initializer();
                if (init.fromClassFile()) {
                    allowCovariantReturn = true;
                }
            }
        }

        allowCovariantReturn = true;

        if ((allowCovariantReturn && ! ts.isSubtype(mi.returnType(), mj.returnType())) ||
            (! allowCovariantReturn && ! ts.typeEquals(mi.returnType(), mj.returnType()))) {
            if (Report.should_report(Report.types, 3))
                Report.report(3, "return type " + mi.returnType() +
                              " != " + mj.returnType());
            System.err.println("is left subtype of right? " + ts.isSubtype(mi.returnType(), mj.returnType()));
            if (quiet) return false;
            throw new SemanticException(mi.signature() + " in " + mi.container() +
                                        " cannot override " +
                                        mj.signature() + " in " + mj.container() +
                                        "; attempting to use incompatible " +
                                        "return type\n" +
                                        "found: " + mi.returnType() + "\n" +
                                        "required: " + mj.returnType(),
                                        mi.position());
        }

        if (! ts.throwsSubset(mi, mj)) {
            if (Report.should_report(Report.types, 3))
                Report.report(3, mi.throwTypes() + " not subset of " +
                              mj.throwTypes());
            if (quiet) return false;
            throw new SemanticException(mi.signature() + " in " + mi.container() +
                                        " cannot override " +
                                        mj.signature() + " in " + mj.container() +
                                        "; the throw set " + mi.throwTypes() + " is not a subset of the " +
                                        "overridden method's throw set " + mj.throwTypes() + ".",
                                        mi.position());
        }

        if (mi.flags().moreRestrictiveThan(mj.flags())) {
            if (Report.should_report(Report.types, 3))
                Report.report(3, mi.flags() + " more restrictive than " +
                              mj.flags());
            if (quiet) return false;
            throw new SemanticException(mi.signature() + " in " + mi.container() +
                                        " cannot override " +
                                        mj.signature() + " in " + mj.container() +
                                        "; attempting to assign weaker " +
                                        "access privileges",
                                        mi.position());
        }

        if (mi.flags().isStatic() != mj.flags().isStatic()) {
            if (Report.should_report(Report.types, 3))
                Report.report(3, mi.signature() + " is " +
                              (mi.flags().isStatic() ? "" : "not") +
                              " static but " + mj.signature() + " is " +
                              (mj.flags().isStatic() ? "" : "not") + " static");
            if (quiet) return false;
            throw new SemanticException(mi.signature() + " in " + mi.container() +
                                        " cannot override " +
                                        mj.signature() + " in " + mj.container() +
                                        "; overridden method is " +
                                        (mj.flags().isStatic() ? "" : "not") +
                                        "static",
                                        mi.position());
        }

        if (mi != mj && !mi.equals(mj) && mj.flags().isFinal()) {
	    // mi can "override" a final method mj if mi and mj are the same method instance.
            if (Report.should_report(Report.types, 3))
                Report.report(3, mj.flags() + " final");
            if (quiet) return false;
            throw new SemanticException(mi.signature() + " in " + mi.container() +
                                        " cannot override " +
                                        mj.signature() + " in " + mj.container() +
                                        "; overridden method is final",
                                        mi.position());
        }

        return true;
    }

    public SummaryVarLabel summaryVarLabel() {
    	return this.summaryVarLabel;
    }

    public void setSummaryVarLabel(SummaryVarLabel sv) {
    	this.summaryVarLabel = sv;
    }

    @Override
    public int hashCode() {
    	return flags.hashCode() + name.hashCode() + position.hashCode();
    }
}
