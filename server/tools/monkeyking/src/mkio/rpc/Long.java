package mkio.rpc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

public final class Long implements Marshal {
	private long value;

	@Override
	public OctetsStream marshal(final OctetsStream os) {
		return os.marshal(value);
	}

	@Override
	public OctetsStream unmarshal(final OctetsStream os) throws MarshalException {
		value = os.unmarshal_long();
		return os;
	}

	public Long() {

	}

	public Long(final long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(final long value) {
		this.value = value;
	}

	@Override
	public java.lang.String toString() {
		return java.lang.String.valueOf(value);
	}
	
}
