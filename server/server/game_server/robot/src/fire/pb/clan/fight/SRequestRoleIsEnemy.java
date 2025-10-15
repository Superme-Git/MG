
package fire.pb.clan.fight;
import robot.LoginRole;
import robot.LoginRoleMgr;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestRoleIsEnemy__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestRoleIsEnemy extends __SRequestRoleIsEnemy__ {
	@Override
	protected void process() {
		// protocol handle
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if(role != null && role.getRoleTask() != null)
			role.getRoleTask().processProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808542;

	public int getType() {
		return 808542;
	}

	public java.util.HashMap<Long,Integer> rolelist; // -1是未知1是自己2是敌对 by changhao

	public SRequestRoleIsEnemy() {
		rolelist = new java.util.HashMap<Long,Integer>();
	}

	public SRequestRoleIsEnemy(java.util.HashMap<Long,Integer> _rolelist_) {
		this.rolelist = _rolelist_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(rolelist.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : rolelist.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			int _v_;
			_v_ = _os_.unmarshal_int();
			rolelist.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestRoleIsEnemy) {
			SRequestRoleIsEnemy _o_ = (SRequestRoleIsEnemy)_o1_;
			if (!rolelist.equals(_o_.rolelist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolelist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolelist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

