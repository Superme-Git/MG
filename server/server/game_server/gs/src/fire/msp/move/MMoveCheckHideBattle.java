
package fire.msp.move;

import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.SceneBattle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MMoveCheckHideBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MMoveCheckHideBattle extends __MMoveCheckHideBattle__ {
	@Override
	protected void process() {
		// protocol handle
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(null == role)
			return;
		
		//RoleHideBattleInfo roleHideBattleInfo = new RoleHideBattleInfo(roleid);
		SceneBattle.process(role, length, System.currentTimeMillis(), monsterconfig);
		//new fire.pb.map.PMoveCheckHideBattle(roleid,sceneid,ownerid,length,monsterconfig).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724915;

	public int getType() {
		return 724915;
	}

	public long roleid;
	public long sceneid; // 场景ID
	public long ownerid; // ownerID
	public int length; // 这次行走的步数
	public int monsterconfig; // 暗雷区配置ID，如果为0，表示这里不是暗雷区

	public MMoveCheckHideBattle() {
	}

	public MMoveCheckHideBattle(long _roleid_, long _sceneid_, long _ownerid_, int _length_, int _monsterconfig_) {
		this.roleid = _roleid_;
		this.sceneid = _sceneid_;
		this.ownerid = _ownerid_;
		this.length = _length_;
		this.monsterconfig = _monsterconfig_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(sceneid);
		_os_.marshal(ownerid);
		_os_.marshal(length);
		_os_.marshal(monsterconfig);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		sceneid = _os_.unmarshal_long();
		ownerid = _os_.unmarshal_long();
		length = _os_.unmarshal_int();
		monsterconfig = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MMoveCheckHideBattle) {
			MMoveCheckHideBattle _o_ = (MMoveCheckHideBattle)_o1_;
			if (roleid != _o_.roleid) return false;
			if (sceneid != _o_.sceneid) return false;
			if (ownerid != _o_.ownerid) return false;
			if (length != _o_.length) return false;
			if (monsterconfig != _o_.monsterconfig) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)sceneid;
		_h_ += (int)ownerid;
		_h_ += length;
		_h_ += monsterconfig;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append(length).append(",");
		_sb_.append(monsterconfig).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MMoveCheckHideBattle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(ownerid - _o_.ownerid);
		if (0 != _c_) return _c_;
		_c_ = length - _o_.length;
		if (0 != _c_) return _c_;
		_c_ = monsterconfig - _o_.monsterconfig;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

