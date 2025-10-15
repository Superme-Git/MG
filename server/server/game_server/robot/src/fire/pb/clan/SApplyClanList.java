package fire.pb.clan;

import robot.LoginRole;
import robot.LoginRoleMgr;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SApplyClanList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SApplyClanList extends __SApplyClanList__ {
	@Override
	protected void process() {
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if (role != null && role.getRoleTask() != null)
			role.getRoleTask().processProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808494;

	public int getType() {
		return 808494;
	}

	public long roleid; // 角色id
	public java.util.ArrayList<fire.pb.clan.ApplyClan> applyclanlist; // 申请过的公会列表

	public SApplyClanList() {
		applyclanlist = new java.util.ArrayList<fire.pb.clan.ApplyClan>();
	}

	public SApplyClanList(long _roleid_, java.util.ArrayList<fire.pb.clan.ApplyClan> _applyclanlist_) {
		this.roleid = _roleid_;
		this.applyclanlist = _applyclanlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.ApplyClan _v_ : applyclanlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.compact_uint32(applyclanlist.size());
		for (fire.pb.clan.ApplyClan _v_ : applyclanlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.ApplyClan _v_ = new fire.pb.clan.ApplyClan();
			_v_.unmarshal(_os_);
			applyclanlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SApplyClanList) {
			SApplyClanList _o_ = (SApplyClanList)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!applyclanlist.equals(_o_.applyclanlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += applyclanlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(applyclanlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
