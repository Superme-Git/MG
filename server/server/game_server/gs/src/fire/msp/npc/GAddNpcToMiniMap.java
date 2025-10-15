
package fire.msp.npc;

import gnet.link.Onlines;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import fire.pb.move.Pos;
import fire.pb.move.SAddActivityNpc;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GAddNpcToMiniMap__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GAddNpcToMiniMap extends __GAddNpcToMiniMap__ {
	@Override
	protected void process() {
		Scene scene=SceneManager.getInstance().getSceneByID(sceneid);
		SAddActivityNpc saan = new SAddActivityNpc(baseids,new ArrayList<fire.pb.move.Pos>());
		for (fire.msp.npc.Pos pos : poses) {
			saan.poslist.add(new Pos((short)pos.x, (short)pos.y));
		}
		if (roleid == 0){ 
		    Set<Long> roleids = getAllSceneRoles(scene);
		    Onlines.getInstance().send(roleids, saan);
		}else {
			Onlines.getInstance().send(roleid, saan);
		}
	}
	
	private Set<Long> getAllSceneRoles(Scene scene) {
       Set<Long> result = new HashSet<Long>();
       for (Role role :  scene.getSceneRoles().values()) {
		      result.add(role.getRoleID());	
		}
		return result;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730409;

	public int getType() {
		return 730409;
	}

	public java.util.ArrayList<fire.msp.npc.Pos> poses;
	public java.util.ArrayList<Integer> baseids;
	public long roleid; // 如果发给一个人,roleid不能为0，如果是发给地图上所有人,设为0即可
	public long sceneid;

	public GAddNpcToMiniMap() {
		poses = new java.util.ArrayList<fire.msp.npc.Pos>();
		baseids = new java.util.ArrayList<Integer>();
	}

	public GAddNpcToMiniMap(java.util.ArrayList<fire.msp.npc.Pos> _poses_, java.util.ArrayList<Integer> _baseids_, long _roleid_, long _sceneid_) {
		this.poses = _poses_;
		this.baseids = _baseids_;
		this.roleid = _roleid_;
		this.sceneid = _sceneid_;
	}

	public final boolean _validator_() {
		for (fire.msp.npc.Pos _v_ : poses)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(poses.size());
		for (fire.msp.npc.Pos _v_ : poses) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(baseids.size());
		for (Integer _v_ : baseids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(roleid);
		_os_.marshal(sceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.msp.npc.Pos _v_ = new fire.msp.npc.Pos();
			_v_.unmarshal(_os_);
			poses.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			baseids.add(_v_);
		}
		roleid = _os_.unmarshal_long();
		sceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GAddNpcToMiniMap) {
			GAddNpcToMiniMap _o_ = (GAddNpcToMiniMap)_o1_;
			if (!poses.equals(_o_.poses)) return false;
			if (!baseids.equals(_o_.baseids)) return false;
			if (roleid != _o_.roleid) return false;
			if (sceneid != _o_.sceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += poses.hashCode();
		_h_ += baseids.hashCode();
		_h_ += (int)roleid;
		_h_ += (int)sceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(poses).append(",");
		_sb_.append(baseids).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

