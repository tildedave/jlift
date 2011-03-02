/*
 * File          : JLiftNodeFactory_c.java
 * Project       : jlift
 * Description   : <Insert Description>
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:29:24 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.ast;

import java.util.Collections;
import java.util.List;

import jif.ast.DeclassifyExpr;
import jif.ast.DeclassifyExpr_c;
import jif.ast.JifConstructorDecl;
import jif.ast.JifExtFactory;
import jif.ast.JifNodeFactory_c;
import jif.ast.LabelNode;
import jif.types.label.Label;
import polyglot.ast.Block;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Expr;
import polyglot.ast.Id;
import polyglot.ast.Special;
import polyglot.ast.Special_c;
import polyglot.ast.TypeNode;
import polyglot.ast.Special.Kind;
import polyglot.types.Flags;
import polyglot.util.Position;

public class JLiftNodeFactory_c extends JifNodeFactory_c implements
		JLiftNodeFactory {

    protected JLiftNodeFactory_c(JifExtFactory extFact) {
        super(extFact, new JLiftDelFactory_c());
    }

	public JLiftNodeFactory_c() {
		this(new JLiftExtFactory_c());
	}

	public VarLabelNode VarLabelNode(Position pos, Label varLabel) {
		return new VarLabelNode(pos, varLabel);
	}

	@Override
	public Special Special(Position pos, Kind kind, TypeNode outer) {
        Special n = new Special_c(pos, kind, outer);
        n = (Special)n.ext(extFactory().extSpecial());
        n = (Special)n.del(delFactory().delSpecial());
        return n;
	}

	@Override
	public ConstructorDecl ConstructorDecl(Position pos, Flags flags, Id name,
			List formals, List throwTypes, Block body) {
		ConstructorDecl n = new JLiftConstructorDecl_c(pos, flags, name, null, null, formals,
				throwTypes, Collections.EMPTY_LIST,
				body);
		n = (ConstructorDecl)n.ext(extFactory().extConstructorDecl());
		n = (ConstructorDecl)n.del(delFactory().delConstructorDecl());
		return n;
	}

	@Override
	public JifConstructorDecl JifConstructorDecl(Position pos, Flags flags,
			Id name, LabelNode startLabel, LabelNode returnLabel, List formals,
			List throwTypes, List constraints, Block body) {
		JifConstructorDecl n = new JLiftConstructorDecl_c(pos, flags, name, null, null, formals,
				throwTypes, Collections.EMPTY_LIST,
				body);
		n = (JifConstructorDecl)n.ext(extFactory().extConstructorDecl());
		n = (JifConstructorDecl)n.del(delFactory().delConstructorDecl());
		return n;
	}

	@Override
	public DeclassifyExpr DeclassifyExpr(Position pos, Expr expr, LabelNode bound,
            LabelNode label)
    {
        DeclassifyExpr n = new DeclassifyExpr_c(pos, expr, bound, label);
        n = (DeclassifyExpr)n.ext(jifExtFactory().extDeclassifyExpr());
        n = (DeclassifyExpr)n.del(((JLiftDelFactory_c) delFactory()).delDeclassifyExprImpl());
        return n;
    }

	@Override
	public DeclassifyExpr DeclassifyExpr(Position pos, Expr expr, LabelNode label)
    {
        DeclassifyExpr n = new DeclassifyExpr_c(pos, expr, null, label);
        n = (DeclassifyExpr)n.ext(jifExtFactory().extDeclassifyExpr());
        n = (DeclassifyExpr)n.del(((JLiftDelFactory_c) delFactory()).delDeclassifyExprImpl());
        return n;
    }
}
