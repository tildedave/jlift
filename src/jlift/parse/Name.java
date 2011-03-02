package jlift.parse;

import jif.parse.Amb;
import jif.parse.Grm;
import polyglot.ast.Expr;
import polyglot.ast.Receiver;
import polyglot.ast.TypeNode;
import polyglot.util.Position;

public class Name extends jif.parse.Name {

	public Name(Grm parser, Position pos, String name)
			throws Exception {
		super(parser, pos, null, name);
	}

	public Name(Grm parser, Position pos, Amb prefix, String name)
			throws Exception {
		super(parser, pos, prefix, name);
	}

	@Override
	public Expr toExpr() throws Exception {
		if (prefix == null) {
			if (name.equals("this"))
				return parser.nf.This(pos);
			return parser.nf.AmbExpr(pos, name);
		}

		Expr expr = processName();
		if (expr != null)
			return expr;
		return super.toExpr();
	}

	protected Expr processName() throws Exception {
		if (name.equals("class")) {
			return parser.nf.ClassLit(pos, prefix.toType());
		}
		if (name.equals("this")) {
			return parser.nf.This(pos, prefix.toType());
		}
		return null;
	}

	@Override
	public Receiver toReceiver() throws Exception {
		Receiver rec = processName();
		if (rec != null)
			return rec;
		return super.toReceiver();
	}

	@Override
	public TypeNode toType() throws Exception {
        if (prefix == null) {
            return parser.nf.AmbTypeNode(pos, parser.nf.Id(pos, name));
        }

        // this causes us some problems.  I don't know why.
        //return parser.nf.AmbTypeNode(pos, parser.nf.AmbQualifierNode(prefix.pos, parser.nf.Id(prefix.pos,prefix.toName())), name);
        // HORRIBLE, HORRIBLE HACK:
        // EVEN MORE HORRIBLE OF A HACK TO HANDLE AN UPPERCASE PACKAGE IN SOURCEJAMMER.

        String packageName = prefix.toName();
        if (Character.isUpperCase(packageName.charAt(0)) && packageName.contains(".") == false && !packageName.equals("JLibDiff"))
        	return parser.nf.AmbTypeNode(pos, prefix.toType(), name);
        else
        	return parser.nf.AmbTypeNode(pos, prefix.toPackage(), name);
//
//		System.err.println("convert " + this + " to type -- " + super.toType());
//		return super.toType();
	}
}