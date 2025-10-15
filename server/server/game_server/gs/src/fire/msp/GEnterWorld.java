
package fire.msp;


import fire.pb.scene.SceneClient;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.God;
import fire.pb.scene.movable.Role;
import fire.pb.scene.movable.SceneTeam;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GEnterWorld__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GEnterWorld extends __GEnterWorld__ {
	@Override
	protected void process() {
		
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(null == role)
		{
			role = God.createRole(roleid, rolename, level, schoolid, mapinfo, laststaticmapinfo, 0 != protect, 0, 0);
			if (null == role)
				return;

			role.init(showpet);
			role.init(mode);
			role.init(title);
			role.init(components);
			role.setSceneState(scenestate);
			role.setLastStaticMapInfo(laststaticmapinfo);
			role.setMaxShowNum(this.showrolenum);
			role.setEquipEffect(equipeffect);
		}
		//因有离线托管的情况,上线时先清理一下能看见的角色
		role.clearISee();
		
		role.enterScene();
		SceneTeam team = role.getTeam();
		if(team!=null)
			team.sendTeamInfoToRole(role);//发送给角色自己的队伍信息
		role.setBattleReplayStateAndBroacast(false);
		RoleManager.getInstance().addRoleToSchool(roleid, schoolid);
		RoleManager.getInstance().addRoleToCamp(roleid, 0);
		SceneClient.pSend(new MEnterScene(roleid,role.getScene().getSceneID(),role.getPos().getX(),role.getPos().getY()));
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720897;

	public int getType() {
		return 720897;
	}

	public long roleid;
	public int level;
	public java.lang.String rolename;
	public fire.msp.RoleMapInfo mapinfo;
	public fire.msp.RoleMapInfo laststaticmapinfo;
	public int protect;
	public int schoolid;
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举
	public fire.msp.modeinfo mode;
	public fire.msp.showpetinfo showpet;
	public fire.msp.showtitleinfo title;
	public int scenestate;
	public int showrolenum;
	public int equipeffect;

	public GEnterWorld() {
		rolename = "";
		mapinfo = new fire.msp.RoleMapInfo();
		laststaticmapinfo = new fire.msp.RoleMapInfo();
		components = new java.util.HashMap<Byte,Integer>();
		mode = new fire.msp.modeinfo();
		showpet = new fire.msp.showpetinfo();
		title = new fire.msp.showtitleinfo();
	}

	public GEnterWorld(long _roleid_, int _level_, java.lang.String _rolename_, fire.msp.RoleMapInfo _mapinfo_, fire.msp.RoleMapInfo _laststaticmapinfo_, int _protect_, int _schoolid_, java.util.HashMap<Byte,Integer> _components_, fire.msp.modeinfo _mode_, fire.msp.showpetinfo _showpet_, fire.msp.showtitleinfo _title_, int _scenestate_, int _showrolenum_, int _equipeffect_) {
		this.roleid = _roleid_;
		this.level = _level_;
		this.rolename = _rolename_;
		this.mapinfo = _mapinfo_;
		this.laststaticmapinfo = _laststaticmapinfo_;
		this.protect = _protect_;
		this.schoolid = _schoolid_;
		this.components = _components_;
		this.mode = _mode_;
		this.showpet = _showpet_;
		this.title = _title_;
		this.scenestate = _scenestate_;
		this.showrolenum = _showrolenum_;
		this.equipeffect = _equipeffect_;
	}

	public final boolean _validator_() {
		if (!mapinfo._validator_()) return false;
		if (!laststaticmapinfo._validator_()) return false;
		if (!mode._validator_()) return false;
		if (!showpet._validator_()) return false;
		if (!title._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(level);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(mapinfo);
		_os_.marshal(laststaticmapinfo);
		_os_.marshal(protect);
		_os_.marshal(schoolid);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(mode);
		_os_.marshal(showpet);
		_os_.marshal(title);
		_os_.marshal(scenestate);
		_os_.marshal(showrolenum);
		_os_.marshal(equipeffect);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		level = _os_.unmarshal_int();
		rolename = _os_.unmarshal_String("UTF-16LE");
		mapinfo.unmarshal(_os_);
		laststaticmapinfo.unmarshal(_os_);
		protect = _os_.unmarshal_int();
		schoolid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		mode.unmarshal(_os_);
		showpet.unmarshal(_os_);
		title.unmarshal(_os_);
		scenestate = _os_.unmarshal_int();
		showrolenum = _os_.unmarshal_int();
		equipeffect = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GEnterWorld) {
			GEnterWorld _o_ = (GEnterWorld)_o1_;
			if (roleid != _o_.roleid) return false;
			if (level != _o_.level) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (!mapinfo.equals(_o_.mapinfo)) return false;
			if (!laststaticmapinfo.equals(_o_.laststaticmapinfo)) return false;
			if (protect != _o_.protect) return false;
			if (schoolid != _o_.schoolid) return false;
			if (!components.equals(_o_.components)) return false;
			if (!mode.equals(_o_.mode)) return false;
			if (!showpet.equals(_o_.showpet)) return false;
			if (!title.equals(_o_.title)) return false;
			if (scenestate != _o_.scenestate) return false;
			if (showrolenum != _o_.showrolenum) return false;
			if (equipeffect != _o_.equipeffect) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += level;
		_h_ += rolename.hashCode();
		_h_ += mapinfo.hashCode();
		_h_ += laststaticmapinfo.hashCode();
		_h_ += protect;
		_h_ += schoolid;
		_h_ += components.hashCode();
		_h_ += mode.hashCode();
		_h_ += showpet.hashCode();
		_h_ += title.hashCode();
		_h_ += scenestate;
		_h_ += showrolenum;
		_h_ += equipeffect;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(level).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(mapinfo).append(",");
		_sb_.append(laststaticmapinfo).append(",");
		_sb_.append(protect).append(",");
		_sb_.append(schoolid).append(",");
		_sb_.append(components).append(",");
		_sb_.append(mode).append(",");
		_sb_.append(showpet).append(",");
		_sb_.append(title).append(",");
		_sb_.append(scenestate).append(",");
		_sb_.append(showrolenum).append(",");
		_sb_.append(equipeffect).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

