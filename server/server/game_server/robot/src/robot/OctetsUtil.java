package robot;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

public class OctetsUtil {
	public static Octets toOctets(int value) {
		OctetsStream oct = new OctetsStream();
		oct.marshal(value);
		return oct;
	}

	public static Octets toOctets(byte value) {
		OctetsStream oct = new OctetsStream();
		oct.marshal(value);
		return oct;
	}

	public static Octets toOctets(Marshal value) {
		return value.marshal(new OctetsStream());
	}

	public static Octets toOctets(String value) {
		OctetsStream oct = new OctetsStream();
		oct.marshal(value, "UTF-16LE");
		return oct;
	}

	public static int toInt(Octets octets) throws MarshalException {
		OctetsStream oct = new OctetsStream(octets);
		return oct.unmarshal_int();
	}
}
