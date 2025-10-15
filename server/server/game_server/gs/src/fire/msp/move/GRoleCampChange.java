
package fire.msp.move;

import fire.pb.friends.SJionCamp;
import fire.pb.scene.manager.RoleManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRoleCampChange__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRoleCampChange extends __GRoleCampChange__ {
	@Override
	protected void process() {
		
		final fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if (role == null) return;
		RoleManager.getInstance().removeRoleFromCamp(roleid);
		role.setCamp(camptype);
		SJionCamp send = new SJionCamp();
		send.camptype = camptype;
		send.roleid = roleid;
		send.selecttype = 0;
		role.sendWhoSeeMeAndMe(send);
		RoleManager.getInstance().addRoleToCamp(roleid, camptype);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724934;

	public int getType() {
		return 724934;
	}

	public long roleid; // 角色
	public byte camptype; // 0=中立  1= 部落 2= 联盟
	public byte selecttype; // 0=选择阵营 1=更换阵营

	public GRoleCampChange() {
	}

	public GRoleCampChange(long _roleid_, byte _camptype_, byte _selecttype_) {
		this.roleid = _roleid_;
		this.camptype = _camptype_;
		this.selecttype = _selecttype_;
	}

	public final boolean _validator_() {
		if (selecttype < 0 || selecttype > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(camptype);
		_os_.marshal(selecttype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		camptype = _os_.unmarshal_byte();
		selecttype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRoleCampChange) {
			GRoleCampChange _o_ = (GRoleCampChange)_o1_;
			if (roleid != _o_.roleid) return false;
			if (camptype != _o_.camptype) return false;
			if (selecttype != _o_.selecttype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)camptype;
		_h_ += (int)selecttype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(camptype).append(",");
		_sb_.append(selecttype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GRoleCampChange _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = camptype - _o_.camptype;
		if (0 != _c_) return _c_;
		_c_ = selecttype - _o_.selecttype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

