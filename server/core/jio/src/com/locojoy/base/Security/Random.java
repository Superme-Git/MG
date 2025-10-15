
package com.locojoy.base.Security;

import com.locojoy.base.Octets;

public final class Random extends Security
{
	private static java.util.Random r = new java.util.Random();
	static
	{
		r.setSeed(System.currentTimeMillis());
	}
	protected Random() { }

	public Octets Update(Octets o)
	{
		r.nextBytes(o.array());
		return o;
	}
}
