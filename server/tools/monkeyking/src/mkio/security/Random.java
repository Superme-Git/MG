package mkio.security;

import com.locojoy.base.Octets;

public final class Random extends Security {
	private static java.util.Random nonce = new java.util.Random();

	static {
		nonce.setSeed(System.currentTimeMillis());
	}

	public Random() {

	}

	public Octets doUpdate(Octets o) {
		nonce.nextBytes(o.array());
		return o;
	}
}