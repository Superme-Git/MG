
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CStepSpace__ extends mkio.Protocol { }

/** 踩某个角色空间
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CStepSpace extends __CStepSpace__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806643;

	public int getType() {
		return 806643;
	}

	public long spaceroleid; // 目标空间的角色id

	public CStepSpace() {
	}

	public CStepSpace(long _spaceroleid_) {
		this.spaceroleid = _spaceroleid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(spaceroleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		spaceroleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CStepSpace) {
			CStepSpace _o_ = (CStepSpace)_o1_;
			if (spaceroleid != _o_.spaceroleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)spaceroleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(spaceroleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CStepSpace _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(spaceroleid - _o_.spaceroleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

