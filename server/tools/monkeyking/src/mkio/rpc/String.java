package mkio.rpc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

public final class String implements Marshal {
	private java.lang.String value;

	@Override
	public OctetsStream marshal(final OctetsStream os) {
		return os.marshal(value, mkdb.Const.IO_CHARSET);
	}

	@Override
	public OctetsStream unmarshal(final OctetsStream os) throws MarshalException {
		value = os.unmarshal_String(mkdb.Const.IO_CHARSET);
		return os;
	}

	public String() {
	}

	public String(final java.lang.String value) {
		this.value = value;
	}

	public java.lang.String getValue() {
		return value;
	}

	public void setValue(final java.lang.String value) {
		this.value = value;
	}

	@Override
	public java.lang.String toString() {
		return value;
	}
	
}
