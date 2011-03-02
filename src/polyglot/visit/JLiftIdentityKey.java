package polyglot.visit;

import polyglot.util.IdentityKey;

public class JLiftIdentityKey extends IdentityKey {

	protected Object obj;

	public JLiftIdentityKey(Object obj) {
		super(obj);
		this.obj = obj;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof JLiftIdentityKey) {
			JLiftIdentityKey okey = (JLiftIdentityKey) other;
			return obj == okey.obj || (obj.equals(okey.obj))
					|| obj.toString().equals(okey.obj.toString());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return obj.toString().hashCode();
	}

}
