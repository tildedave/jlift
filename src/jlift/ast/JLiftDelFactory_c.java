/*
 * File          : JLiftDelFactory_c.java
 * Project       : jlift
 * Description   : Delegate factory for JLift (overriding default jif delegates)
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:28:44 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.ast;

import jif.ast.JifDelFactory_c;
import jlift.extension.JLiftCallDel;
import jlift.extension.JLiftCastDel;
import jlift.extension.JLiftDeclassifyExprDel;
import jlift.extension.JLiftFieldDeclDel;
import jlift.extension.JLiftFormalDel;
import jlift.extension.JLiftInstanceofDel;
import jlift.extension.JLiftTypeNodeDel;
import polyglot.ast.DelFactory;
import polyglot.ast.JL;

public class JLiftDelFactory_c extends JifDelFactory_c implements
		DelFactory {

	public JLiftDelFactory_c() {
		super();
	}

	@Override
	protected JL delCallImpl() {
		return new JLiftCallDel();
	}

	@Override
	protected JL delTypeNodeImpl() {
		return new JLiftTypeNodeDel();
	}

	@Override
	protected JL delFieldDeclImpl() {
		return new JLiftFieldDeclDel();
	}

	@Override
	protected JL delFormalImpl() {
		return new JLiftFormalDel();
	}

	@Override
	protected JL delCastImpl() {
		return new JLiftCastDel();
	}

	@Override
	protected JL delInstanceofImpl() {
		return new JLiftInstanceofDel();
	}

	protected JL delDeclassifyExprImpl() {
		return new JLiftDeclassifyExprDel();
	}
}
