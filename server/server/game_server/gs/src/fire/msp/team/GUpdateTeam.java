
package fire.msp.team;

import fire.pb.scene.Scene;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GUpdateTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GUpdateTeam extends __GUpdateTeam__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.movable.SceneTeam mapTeam = fire.pb.scene.movable.God.createTeam(sceneteam.teamid, sceneteam.leaderid);
		StringBuilder sb = new StringBuilder("GUpdateTeam : teamid = ").append(sceneteam.teamid);
		for (SceneTeamMember memeber: sceneteam.memebers) {
			fire.pb.scene.movable.Role r = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(memeber.roleid);
			if(null == r) continue;
			//r.setTeamState(memeber.state);
			//TODO
			mapTeam.getMembers().add(r);
			sb.append("; roleid=").append(memeber.roleid).append("; state=").append(memeber.state);
		}
		Scene.LOG.debug(sb.toString());
		//SceneTeamManager.getInstance().onUpdateTeam(mapTeam);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 728901;

	public int getType() {
		return 728901;
	}

	public fire.msp.team.SceneTeam sceneteam; // 新的SceneTeam
	public java.util.HashMap<Long,Integer> newmemberid; // 新成员ID与状态

	public GUpdateTeam() {
		sceneteam = new fire.msp.team.SceneTeam();
		newmemberid = new java.util.HashMap<Long,Integer>();
	}

	public GUpdateTeam(fire.msp.team.SceneTeam _sceneteam_, java.util.HashMap<Long,Integer> _newmemberid_) {
		this.sceneteam = _sceneteam_;
		this.newmemberid = _newmemberid_;
	}

	public final boolean _validator_() {
		if (!sceneteam._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(sceneteam);
		_os_.compact_uint32(newmemberid.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : newmemberid.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		sceneteam.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			int _v_;
			_v_ = _os_.unmarshal_int();
			newmemberid.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GUpdateTeam) {
			GUpdateTeam _o_ = (GUpdateTeam)_o1_;
			if (!sceneteam.equals(_o_.sceneteam)) return false;
			if (!newmemberid.equals(_o_.newmemberid)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += sceneteam.hashCode();
		_h_ += newmemberid.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sceneteam).append(",");
		_sb_.append(newmemberid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

