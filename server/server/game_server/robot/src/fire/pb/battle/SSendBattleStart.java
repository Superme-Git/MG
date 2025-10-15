
package fire.pb.battle;

import robot.LoginRole;
import robot.LoginRoleMgr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendBattleStart__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendBattleStart extends __SSendBattleStart__ {
	@Override
	protected void process() {
		// protocol handle
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if(role != null && role.getRoleTask() != null)
			role.getRoleTask().processProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793433;

	public int getType() {
		return 793433;
	}

	public int enemyside;
	public int friendsformation; // 己方的光环
	public int enemyformation; // 敌人的光环
	public int friendsformationlevel; // 己方的光环
	public int enemyformationlevel; // 敌人的光环
	public int battleconfigid; // 战斗配置ID，默认为-1，即不是通过战斗配置来生成的战斗
	public int battletype; // 枚举值见BattleType
	public int roundnum; // 回合数，默认是0
	public byte background; // 战斗底图id
	public byte backmusic; // 战斗背景音乐id

	public SSendBattleStart() {
	}

	public SSendBattleStart(int _enemyside_, int _friendsformation_, int _enemyformation_, int _friendsformationlevel_, int _enemyformationlevel_, int _battleconfigid_, int _battletype_, int _roundnum_, byte _background_, byte _backmusic_) {
		this.enemyside = _enemyside_;
		this.friendsformation = _friendsformation_;
		this.enemyformation = _enemyformation_;
		this.friendsformationlevel = _friendsformationlevel_;
		this.enemyformationlevel = _enemyformationlevel_;
		this.battleconfigid = _battleconfigid_;
		this.battletype = _battletype_;
		this.roundnum = _roundnum_;
		this.background = _background_;
		this.backmusic = _backmusic_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(enemyside);
		_os_.marshal(friendsformation);
		_os_.marshal(enemyformation);
		_os_.marshal(friendsformationlevel);
		_os_.marshal(enemyformationlevel);
		_os_.marshal(battleconfigid);
		_os_.marshal(battletype);
		_os_.marshal(roundnum);
		_os_.marshal(background);
		_os_.marshal(backmusic);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		enemyside = _os_.unmarshal_int();
		friendsformation = _os_.unmarshal_int();
		enemyformation = _os_.unmarshal_int();
		friendsformationlevel = _os_.unmarshal_int();
		enemyformationlevel = _os_.unmarshal_int();
		battleconfigid = _os_.unmarshal_int();
		battletype = _os_.unmarshal_int();
		roundnum = _os_.unmarshal_int();
		background = _os_.unmarshal_byte();
		backmusic = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendBattleStart) {
			SSendBattleStart _o_ = (SSendBattleStart)_o1_;
			if (enemyside != _o_.enemyside) return false;
			if (friendsformation != _o_.friendsformation) return false;
			if (enemyformation != _o_.enemyformation) return false;
			if (friendsformationlevel != _o_.friendsformationlevel) return false;
			if (enemyformationlevel != _o_.enemyformationlevel) return false;
			if (battleconfigid != _o_.battleconfigid) return false;
			if (battletype != _o_.battletype) return false;
			if (roundnum != _o_.roundnum) return false;
			if (background != _o_.background) return false;
			if (backmusic != _o_.backmusic) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += enemyside;
		_h_ += friendsformation;
		_h_ += enemyformation;
		_h_ += friendsformationlevel;
		_h_ += enemyformationlevel;
		_h_ += battleconfigid;
		_h_ += battletype;
		_h_ += roundnum;
		_h_ += (int)background;
		_h_ += (int)backmusic;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(enemyside).append(",");
		_sb_.append(friendsformation).append(",");
		_sb_.append(enemyformation).append(",");
		_sb_.append(friendsformationlevel).append(",");
		_sb_.append(enemyformationlevel).append(",");
		_sb_.append(battleconfigid).append(",");
		_sb_.append(battletype).append(",");
		_sb_.append(roundnum).append(",");
		_sb_.append(background).append(",");
		_sb_.append(backmusic).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendBattleStart _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = enemyside - _o_.enemyside;
		if (0 != _c_) return _c_;
		_c_ = friendsformation - _o_.friendsformation;
		if (0 != _c_) return _c_;
		_c_ = enemyformation - _o_.enemyformation;
		if (0 != _c_) return _c_;
		_c_ = friendsformationlevel - _o_.friendsformationlevel;
		if (0 != _c_) return _c_;
		_c_ = enemyformationlevel - _o_.enemyformationlevel;
		if (0 != _c_) return _c_;
		_c_ = battleconfigid - _o_.battleconfigid;
		if (0 != _c_) return _c_;
		_c_ = battletype - _o_.battletype;
		if (0 != _c_) return _c_;
		_c_ = roundnum - _o_.roundnum;
		if (0 != _c_) return _c_;
		_c_ = background - _o_.background;
		if (0 != _c_) return _c_;
		_c_ = backmusic - _o_.backmusic;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

