
package fire.pb.move;

import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.scene.Scene;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqSeeEachOther__ extends mkio.Protocol { }

/** 主动请求跟某个在周围的角色互相看见，如果在周围，就推下来
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqSeeEachOther extends __CReqSeeEachOther__ {
	@Override
	protected void process() {
		// protocol handle
		final long reqroleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (reqroleId < 0)
			return;
		Role reqrole = RoleManager.getInstance().getRoleByID(reqroleId);
		Role seerole = RoleManager.getInstance().getRoleByID(roleid);
		
		if(reqrole == null || seerole == null)
			return;//不在线
		
		if(reqrole.getScene() != seerole.getScene())
			return;//不在同一场景
		
		Scene s = reqrole.getScene();
		int reqscreenindex = s.getScreenIndex(reqrole.getPos());
		int seescreenindex = s.getScreenIndex(seerole.getPos());
		if(Math.abs(reqscreenindex - seescreenindex) > 1)
			return;//不在附近屏块中，互相看不到
		
		MapConfig mapcfg = ConfigManager.getInstance().getConf(MapConfig.class).get(s.getMapID());
		if(mapcfg == null)
			return;
		if(mapcfg.getVisibletype() == 1)//自己可见
			return;
		if(mapcfg.getVisibletype() == 2)//小队可见
		{
			final long reqteamid = reqrole.getTeamID();
			final long addteamid = seerole.getTeamID();
			if(reqteamid == 0 || addteamid == 0 || reqteamid != addteamid)
				return;
		}
		
		reqrole.seeSomeone(seerole);
		seerole.seeSomeone(reqrole);
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790486;

	public int getType() {
		return 790486;
	}

	public long roleid;

	public CReqSeeEachOther() {
	}

	public CReqSeeEachOther(long _roleid_) {
		this.roleid = _roleid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqSeeEachOther) {
			CReqSeeEachOther _o_ = (CReqSeeEachOther)_o1_;
			if (roleid != _o_.roleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqSeeEachOther _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

