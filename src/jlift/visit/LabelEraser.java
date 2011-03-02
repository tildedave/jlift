package jlift.visit;

import java.util.LinkedList;
import java.util.List;

import jif.ast.DeclassifyExpr;
import jif.ast.InstTypeNode;
import jif.ast.JifCanonicalTypeNode;
import jif.ast.JifConstructorDecl;
import jif.ast.JifFormal_c;
import jif.ast.JifLocalDecl_c;
import jif.ast.JifMethodDecl;
import jif.ast.JifProcedureDecl;
import jif.ast.LabelNode;
import jif.ast.LabeledTypeNode;
import jif.ast.ParamNode;
import jif.types.JifTypeSystem;
import jif.types.label.ThisLabel;
import polyglot.ast.ArrayTypeNode;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.FieldDecl;
import polyglot.ast.Formal;
import polyglot.ast.LocalDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.TypeNode;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.PrimitiveType;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ErrorHandlingVisitor;
import polyglot.visit.NodeVisitor;

public class LabelEraser extends ErrorHandlingVisitor {

	public static int numErased = 0;
	protected boolean insideField = false;

	public LabelEraser(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
	}

	@Override
	protected NodeVisitor enterCall(Node parent, Node n)
			throws SemanticException {
		if (n instanceof FieldDecl) {
			insideField = true;
		}
		return super.enterCall(parent, n);
	}

	@Override
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v)
			throws SemanticException {
		if (n instanceof FieldDecl) {
			insideField = false;
		}
		if (n instanceof JifProcedureDecl) {
			if (n instanceof JifConstructorDecl) {
				JifConstructorDecl jcd = (JifConstructorDecl) n;

				if (jcd.returnLabel() != null)
					++numErased;

				ConstructorDecl erase = jcd.returnLabel(null).formals(eraseFormals(jcd.formals()));
				if (Report.should_report("labelerase", 1))
					Report.report(1,"erased " + jcd + "@" + jcd.position() + " to " + erase);
				return erase;
			}
			else if (n instanceof JifMethodDecl) {
				JifMethodDecl jmd = (JifMethodDecl) n;
				String name = jmd.name();
				if (!name.equals("main") && !name.endsWith("Main") && !name.equals("test")) {

					if (jmd.returnLabel() != null)
						++numErased;
					if (jmd.startLabel() != null)
						++numErased;

					TypeNode returnTypeNode = jmd.returnType();
					TypeNode erasedTypeNode = eraseType(returnTypeNode);

					List<TypeNode> erasedThrowTypes = eraseThrowTypes(jmd.throwTypes());
					MethodDecl erase = ((JifMethodDecl) jmd.returnType(erasedTypeNode)).returnLabel(null).startLabel(null).formals(eraseFormals(jmd.formals())).throwTypes(erasedThrowTypes);
//					PrettyPrinter pp = new PrettyPrinter();
//					SimpleCodeWriter scw = new SimpleCodeWriter(System.out, 80);
//					((JifMethodDecl_c) jmd).prettyPrintHeader(jmd.flags(),scw, pp);
//					System.out.print(" erased to ");
//					((JifMethodDecl_c) erase).prettyPrintHeader(erase.flags(), scw, pp);
					if (Report.should_report("labelerase", 1)) {
						Report.report(1,"erased " + jmd + "@" + jmd.position() + " to " + erase);
						Report.report(1,"\t" + returnTypeNode + "(" + returnTypeNode.getClass() + ") erased to " + erasedTypeNode + "(" + erasedTypeNode.getClass() + ")");
						Report.report(1,"\t" + jmd.throwTypes() + " erased to " + erasedThrowTypes);
					}
					return erase;
				}
			}
		}
		if (n instanceof DeclassifyExpr) {
			DeclassifyExpr de = (DeclassifyExpr) n;
			if (Report.should_report("labelerase", 1))
				Report.report(1, "removed declassify " + de + "@" + de.position());

			++numErased;

			return de.expr();
		}
		if (n instanceof LocalDecl) {
			JifLocalDecl_c ld = (JifLocalDecl_c) n;
			LocalDecl erasedType = ld.type(eraseType(ld.type()));
			if (ld.type() instanceof LabeledTypeNode) {
				LabeledTypeNode ltn = (LabeledTypeNode) ld.type();
				if (ltn.labelPart() instanceof ThisLabel) {
					++numErased;
					return erasedType;
				}
//				if (ltn.labelPart().label() != null && ltn.labelPart().label().isBottom()) {
//					++numErased;
//					return erasedType;
//				}
				else {
					System.err.println("not erasing " + ltn.labelPart() + " from " + n + " @ " + n.position());
				}
			}
			return ld;
		}
		if (n instanceof ArrayTypeNode) {
			ArrayTypeNode atn = (ArrayTypeNode) n;
			if (insideField == false) {
				++numErased;
				return atn.base(eraseType(atn.base()));
			}
		}

		return super.leaveCall(old, n, v);
	}

	private List<TypeNode> eraseThrowTypes(List<TypeNode> throwTypes) {
		LinkedList<TypeNode> returnList = new LinkedList<TypeNode>();
		for(TypeNode tn : throwTypes) {
			returnList.add(eraseType(tn));
		}
		return returnList;
	}

	private TypeNode eraseType(TypeNode type) {
		if (type instanceof LabeledTypeNode) {
			LabeledTypeNode ltn = (LabeledTypeNode) type;
//			if (!ltn.typePart().toString().startsWith("Runtime")) {
				++numErased;
				return eraseType(ltn.typePart());
//			}
//			else {
//				System.err.println("not erasing: " + type);
//			}
		}
		if (type instanceof InstTypeNode) {
			InstTypeNode itn = (InstTypeNode) type;
			for(ParamNode pn : ((List<ParamNode>) itn.params())) {
				if (!(pn instanceof LabelNode))
					return type;
			}
			++numErased;
			return eraseType(itn.base());
		}
		if (type instanceof ArrayTypeNode) {
			ArrayTypeNode at = (ArrayTypeNode) type;
			return at.base(eraseType(at.base()));
		}

		if (type instanceof PrimitiveType && type.toString().contains("label")) {
			return nf.CanonicalTypeNode(type.position(), ts.Object());
		}
		/*
		if (type instanceof JifCanonicalTypeNode) {
			JifCanonicalTypeNode jctn = (JifCanonicalTypeNode) type;
			JifTypeSystem jts = (JifTypeSystem) ts;

			return jctn.type(jts.unlabel(jctn.type()));
		}
		*/
		return type;
	}

	private List<Formal> eraseFormals(List<Formal> formals) {
		LinkedList<Formal> returnList = new LinkedList<Formal>();
		for(Formal formal : formals) {
			returnList.add(eraseFormal(formal));
		}
		return returnList;
	}

	private Formal eraseFormal(Formal formal) {
		JifFormal_c jf = (JifFormal_c) formal;
		return jf.type(eraseType(jf.type()));
	}
}
