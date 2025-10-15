package fire.pb.clan;

import robot.LoginRole;
import robot.LoginRoleMgr;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SLeaveClan__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SLeaveClan extends __SLeaveClan__ {
	@Override
	protected void process() {
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if (role != null && role.getRoleTask() != null)
			role.getRoleTask().processProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808452;

	public int getType() {
		return 808452;
	}

	public long memberid; // 成员id

	public SLeaveClan() {
	}

	public SLeaveClan(long _memberid_) {
		this.memberid = _memberid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(memberid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		memberid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SLeaveClan) {
			SLeaveClan _o_ = (SLeaveClan)_o1_;
			if (memberid != _o_.memberid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)memberid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(memberid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SLeaveClan _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(memberid - _o_.memberid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
