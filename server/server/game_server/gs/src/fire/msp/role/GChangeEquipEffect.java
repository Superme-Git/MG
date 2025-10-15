
package fire.msp.role;

import fire.pb.move.SAddUserScreen;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GChangeEquipEffect__ extends mkio.Protocol { }

/** 改变角色装备特效
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GChangeEquipEffect extends __GChangeEquipEffect__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if (role == null)
			return;
		if (null == role.getScene())
			return;
		
		if (role.getEquipEffecdt() != effect) {
			fire.pb.move.SChangeEquipEffect changeEquip = new fire.pb.move.SChangeEquipEffect();
			changeEquip.effect = effect;
			changeEquip.playerid = roleid;
			
			role.setEquipEffect(effect);
			role.sendWhoSeeMeAndMe(changeEquip);
			
			SAddUserScreen sadduser = new SAddUserScreen();
			sadduser.rolelist.add(role.getRoleBasic());
			gnet.link.Onlines.getInstance().send(role.getRoleID(), sadduser);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730916;

	public int getType() {
		return 730916;
	}

	public long roleid; // 玩家id
	public int effect; // 特效表id

	public GChangeEquipEffect() {
	}

	public GChangeEquipEffect(long _roleid_, int _effect_) {
		this.roleid = _roleid_;
		this.effect = _effect_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(effect);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		effect = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GChangeEquipEffect) {
			GChangeEquipEffect _o_ = (GChangeEquipEffect)_o1_;
			if (roleid != _o_.roleid) return false;
			if (effect != _o_.effect) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += effect;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(effect).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GChangeEquipEffect _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = effect - _o_.effect;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

