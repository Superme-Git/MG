
package fire.pb.npc;

import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.task.ArenaFight;
import robot.task.Task_RoleBase;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGeneralSummonCommand__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGeneralSummonCommand extends __SGeneralSummonCommand__ {
	@Override
	protected void process() {
		// protocol handle
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if(role ==null){
			return;
		}
		
		Task_RoleBase task  = role.getRoleTask();
		if (task instanceof ArenaFight) {
			 role.getRoleTask().processProtocol(this);
		};
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795505;

	public int getType() {
		return 795505;
	}

	public int summontype; // 拉人类型 见TransmitTypes
	public long roleid; // 如果是玩家拉人，则此处填玩家ID
	public long npckey; // npckey
	public int mapid; // 目的地图的baseID，客户端通过这个ID来知道地图名
	public int minimal; // 弹出的召集令是否最小化,1为最小化,0为不最小化

	public SGeneralSummonCommand() {
	}

	public SGeneralSummonCommand(int _summontype_, long _roleid_, long _npckey_, int _mapid_, int _minimal_) {
		this.summontype = _summontype_;
		this.roleid = _roleid_;
		this.npckey = _npckey_;
		this.mapid = _mapid_;
		this.minimal = _minimal_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(summontype);
		_os_.marshal(roleid);
		_os_.marshal(npckey);
		_os_.marshal(mapid);
		_os_.marshal(minimal);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		summontype = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		npckey = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		minimal = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGeneralSummonCommand) {
			SGeneralSummonCommand _o_ = (SGeneralSummonCommand)_o1_;
			if (summontype != _o_.summontype) return false;
			if (roleid != _o_.roleid) return false;
			if (npckey != _o_.npckey) return false;
			if (mapid != _o_.mapid) return false;
			if (minimal != _o_.minimal) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += summontype;
		_h_ += (int)roleid;
		_h_ += (int)npckey;
		_h_ += mapid;
		_h_ += minimal;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(summontype).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(minimal).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SGeneralSummonCommand _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = summontype - _o_.summontype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		_c_ = minimal - _o_.minimal;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

