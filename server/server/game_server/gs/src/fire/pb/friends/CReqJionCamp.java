
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqJionCamp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqJionCamp extends __CReqJionCamp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806558;

	public int getType() {
		return 806558;
	}

	public byte camptype; // 1=部落 2=联盟

	public CReqJionCamp() {
	}

	public CReqJionCamp(byte _camptype_) {
		this.camptype = _camptype_;
	}

	public final boolean _validator_() {
		if (camptype < 1 || camptype > 2) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(camptype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		camptype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqJionCamp) {
			CReqJionCamp _o_ = (CReqJionCamp)_o1_;
			if (camptype != _o_.camptype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)camptype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(camptype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqJionCamp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = camptype - _o_.camptype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

