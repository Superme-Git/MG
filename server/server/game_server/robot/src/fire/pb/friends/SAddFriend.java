
package fire.pb.friends;

import robot.LoginRole;
import robot.LoginRoleMgr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAddFriend__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAddFriend extends __SAddFriend__ {
	@Override
	protected void process() {
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if(role !=null && role.getRoleTask() != null)
			role.addFriend(friendinfobean.roleid);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806440;

	public int getType() {
		return 806440;
	}

	public fire.pb.friends.InfoBean friendinfobean;

	public SAddFriend() {
		friendinfobean = new fire.pb.friends.InfoBean();
	}

	public SAddFriend(fire.pb.friends.InfoBean _friendinfobean_) {
		this.friendinfobean = _friendinfobean_;
	}

	public final boolean _validator_() {
		if (!friendinfobean._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(friendinfobean);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		friendinfobean.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAddFriend) {
			SAddFriend _o_ = (SAddFriend)_o1_;
			if (!friendinfobean.equals(_o_.friendinfobean)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += friendinfobean.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friendinfobean).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

