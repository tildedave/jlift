/*
 * File          : JLiftExtFactory_c.java
 * Project       : jlift
 * Description   : Extension factory for JLift (overriding default jif extension objects)
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:29:01 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.ast;

import jif.ast.JifExtFactory_c;
import jif.translate.ArrayAccessAssignToJavaExt_c;
import jif.translate.CallToJavaExt_c;
import jif.translate.CastToJavaExt_c;
import jif.translate.ConstructorDeclToJavaExt_c;
import jif.translate.DowngradeExprToJavaExt_c;
import jif.translate.FieldToJavaExt_c;
import jif.translate.FormalToJavaExt_c;
import jif.translate.LabelExprToJavaExt_c;
import jif.translate.LitToJavaExt_c;
import jif.translate.LocalAssignToJavaExt_c;
import jif.translate.LocalDeclToJavaExt_c;
import jif.translate.MethodDeclToJavaExt_c;
import jif.translate.ReturnToJavaExt_c;
import jif.translate.ThrowToJavaExt_c;
import jlift.extension.JLiftArrayAccessAssignExt;
import jlift.extension.JLiftCallExt;
import jlift.extension.JLiftCastExt;
import jlift.extension.JLiftConstructorDeclExt;
import jlift.extension.JLiftDeclassifyExprExt;
import jlift.extension.JLiftFieldAssignExt;
import jlift.extension.JLiftFieldExt;
import jlift.extension.JLiftFormalExt;
import jlift.extension.JLiftLabelExprExt;
import jlift.extension.JLiftLiteralExt;
import jlift.extension.JLiftLocalAssignExt;
import jlift.extension.JLiftLocalDeclExt;
import jlift.extension.JLiftMethodDeclExt;
import jlift.extension.JLiftReturnExt;
import jlift.extension.JLiftThrowExt;
import polyglot.ast.Ext;

public class JLiftExtFactory_c extends JifExtFactory_c {

	public JLiftExtFactory_c() {
		super();
	}

	@Override
	protected Ext extCallImpl() {
		return new JLiftCallExt(new CallToJavaExt_c());
	}

	@Override
	protected Ext extMethodDeclImpl() {
		return new JLiftMethodDeclExt(new MethodDeclToJavaExt_c());
	}

	@Override
	protected Ext extCastImpl() {
		return new JLiftCastExt(new CastToJavaExt_c());
	}

	@Override
	protected Ext extConstructorDeclImpl() {
		return new JLiftConstructorDeclExt(new ConstructorDeclToJavaExt_c());
	}

	@Override
	protected Ext extFormalImpl() {
		return new JLiftFormalExt(new FormalToJavaExt_c());
	}

	@Override
	protected Ext extFieldImpl() {
		return new JLiftFieldExt(new FieldToJavaExt_c());
	}

	@Override
	protected Ext extFieldAssignImpl() {
		return new JLiftFieldAssignExt(new FieldToJavaExt_c());
	}

	@Override
	protected Ext extDeclassifyExprImpl() {
		return new JLiftDeclassifyExprExt(new DowngradeExprToJavaExt_c());
	}

	@Override
	protected Ext extReturnImpl() {
		return new JLiftReturnExt(new ReturnToJavaExt_c());
	}

	@Override
	protected Ext extLocalDeclImpl() {
		return new JLiftLocalDeclExt(new LocalDeclToJavaExt_c());
	}

	@Override
	protected Ext extLitImpl() {
		return new JLiftLiteralExt(new LitToJavaExt_c());
	}

	@Override
	protected Ext extLocalAssignImpl() {
		return new JLiftLocalAssignExt(new LocalAssignToJavaExt_c());
	}

	@Override
	protected Ext extArrayAccessAssignImpl() {
		return new JLiftArrayAccessAssignExt(new ArrayAccessAssignToJavaExt_c());
	}

	@Override
	protected Ext extThrowImpl() {
		return new JLiftThrowExt(new ThrowToJavaExt_c());
	}

	@Override
	protected Ext extLabelExprImpl() {
		return new JLiftLabelExprExt(new LabelExprToJavaExt_c());
	}

	@Override
	protected Ext extNewLabelImpl() {
		return new JLiftLabelExprExt(new LabelExprToJavaExt_c());
	}
}
