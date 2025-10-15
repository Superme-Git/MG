
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SServerIDResponse__ extends mkio.Protocol { }

/** 下发serverID的协议 start
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SServerIDResponse extends __SServerIDResponse__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786529;

	public int getType() {
		return 786529;
	}

	public int serverid;

	public SServerIDResponse() {
	}

	public SServerIDResponse(int _serverid_) {
		this.serverid = _serverid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(serverid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		serverid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SServerIDResponse) {
			SServerIDResponse _o_ = (SServerIDResponse)_o1_;
			if (serverid != _o_.serverid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += serverid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(serverid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SServerIDResponse _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = serverid - _o_.serverid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

