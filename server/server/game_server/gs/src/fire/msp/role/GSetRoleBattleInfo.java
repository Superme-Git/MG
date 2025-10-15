
package fire.msp.role;

import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSetRoleBattleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSetRoleBattleInfo extends __GSetRoleBattleInfo__ {
	@Override
	protected void process() {
		
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(role == null)
			return;
		if(battlestate == 0)
		{
			role.setBattleFighterStateAndBroacast(false);
			role.setBattleWatcherStateAndBroacast(false);
			role.setBattleReplayStateAndBroacast(false);
		}
		else if(battlestate == 1)
		{
			role.setBattleFighterStateAndBroacast(true);
		}
		else if(battlestate == 2)
		{
			role.setBattleWatcherStateAndBroacast(true);
		}
		else if(battlestate == 4)
		{
			role.setBattleReplayStateAndBroacast(true);
		}
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730900;

	public int getType() {
		return 730900;
	}

	public long roleid; // roleid
	public int battlestate; // 0：无；1：战斗者；2：观战者

	public GSetRoleBattleInfo() {
	}

	public GSetRoleBattleInfo(long _roleid_, int _battlestate_) {
		this.roleid = _roleid_;
		this.battlestate = _battlestate_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(battlestate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		battlestate = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSetRoleBattleInfo) {
			GSetRoleBattleInfo _o_ = (GSetRoleBattleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (battlestate != _o_.battlestate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += battlestate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(battlestate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSetRoleBattleInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = battlestate - _o_.battlestate;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

