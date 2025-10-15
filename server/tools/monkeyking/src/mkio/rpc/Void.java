package mkio.rpc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

public final class Void implements Marshal {

	@Override
	public OctetsStream marshal(final OctetsStream os) {
		return os;
	}

	@Override
	public OctetsStream unmarshal(final OctetsStream os) throws MarshalException {
		return os;
	}

}
