
package fire.msp.move;

import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GAddPowerRestore__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GAddPowerRestore extends __GAddPowerRestore__ {
	@Override
	protected void process() {
		// protocol handle
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(role == null)
		{
			fire.pb.scene.SceneManager.logger.error("GAddPowerRestore时，role为null，roleid=" + roleid);
			return;
		}
		if(role.getJumpRole() == null)
			return;
		if(powerrestore > 0)
			role.getJumpRole().setPowerRestore(powerrestore);
		else
			role.getJumpRole().setPowerRestore(0);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724930;

	public int getType() {
		return 724930;
	}

	public long roleid;
	public int powerrestore; // 如果为-1，则为直接清除气力储备

	public GAddPowerRestore() {
	}

	public GAddPowerRestore(long _roleid_, int _powerrestore_) {
		this.roleid = _roleid_;
		this.powerrestore = _powerrestore_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(powerrestore);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		powerrestore = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GAddPowerRestore) {
			GAddPowerRestore _o_ = (GAddPowerRestore)_o1_;
			if (roleid != _o_.roleid) return false;
			if (powerrestore != _o_.powerrestore) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += powerrestore;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(powerrestore).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GAddPowerRestore _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = powerrestore - _o_.powerrestore;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

