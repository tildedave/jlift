package jlift.ast;

import java.util.Iterator;
import java.util.List;

import jif.ast.JifConstructorDecl_c;
import jif.ast.LabelNode;
import jlift.types.JLiftTypeSystem;
import polyglot.ast.Block;
import polyglot.ast.Id;
import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.TypeChecker;

public class JLiftConstructorDecl_c extends JifConstructorDecl_c {

	public JLiftConstructorDecl_c(Position pos, Flags flags, Id name,
			LabelNode startLabel, LabelNode returnLabel, List formals,
			List throwTypes, List constraints, Block body) {
		super(pos, flags, name, startLabel, returnLabel, formals, throwTypes,
				constraints, body);
	}

	@Override
	public Node typeCheck(TypeChecker tc) throws SemanticException {
		if (!((JLiftTypeSystem) tc.typeSystem()).allowSmallLeaks())
			return super.typeCheck(tc);
		else {
			// HACK: copy/pasted from ConstructorDecl_c
			Context c = tc.context();
			TypeSystem ts = tc.typeSystem();

			ClassType ct = c.currentClass();

			if (ct.flags().isInterface()) {
				throw new SemanticException(
						"Cannot declare a constructor inside an interface.",
						position());
			}

			if (ct.isAnonymous()) {
				throw new SemanticException(
						"Cannot declare a constructor inside an anonymous class.",
						position());
			}

			String ctName = ct.name();

			if (! ctName.equals(name.id())) {
				throw new SemanticException("Constructor name \"" + name +
						"\" does not match name of containing class \"" +
						ctName + "\".", position());
			}

			try {
				ts.checkConstructorFlags(flags());
			}
			catch (SemanticException e) {
				throw new SemanticException(e.getMessage(), position());
			}

			if (body == null && ! flags().isNative()) {
				throw new SemanticException("Missing constructor body.",
						position());
			}

			if (body != null && flags().isNative()) {
				throw new SemanticException(
						"A native constructor cannot have a body.", position());
			}

			for (Iterator i = throwTypes().iterator(); i.hasNext(); ) {
				TypeNode tn = (TypeNode) i.next();
				Type t = tn.type();
				if (! t.isThrowable()) {
					throw new SemanticException("Type \"" + t +
							"\" is not a subclass of \"" + ts.Throwable() + "\".",
							tn.position());
				}
			}

			return this;
		}
	}

}
