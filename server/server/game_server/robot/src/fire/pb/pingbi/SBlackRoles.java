
package fire.pb.pingbi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBlackRoles__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBlackRoles extends __SBlackRoles__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819144;

	public int getType() {
		return 819144;
	}

	public java.util.ArrayList<fire.pb.pingbi.BlackRoleInfo> blackroles;

	public SBlackRoles() {
		blackroles = new java.util.ArrayList<fire.pb.pingbi.BlackRoleInfo>();
	}

	public SBlackRoles(java.util.ArrayList<fire.pb.pingbi.BlackRoleInfo> _blackroles_) {
		this.blackroles = _blackroles_;
	}

	public final boolean _validator_() {
		for (fire.pb.pingbi.BlackRoleInfo _v_ : blackroles)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(blackroles.size());
		for (fire.pb.pingbi.BlackRoleInfo _v_ : blackroles) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.pingbi.BlackRoleInfo _v_ = new fire.pb.pingbi.BlackRoleInfo();
			_v_.unmarshal(_os_);
			blackroles.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBlackRoles) {
			SBlackRoles _o_ = (SBlackRoles)_o1_;
			if (!blackroles.equals(_o_.blackroles)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += blackroles.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(blackroles).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

