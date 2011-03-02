package jlift.extension;
import jif.extension.JifJL_c;
import jlift.types.JLiftContext_c;
import polyglot.types.Context;


public class JLiftDeclassifyExprDel extends JifJL_c {

	@Override
	public Context enterScope(Context c) {
		JLiftContext_c jc = (JLiftContext_c) super.enterScope(c);
		return jc.insideDeclassify(true);
	}

}
