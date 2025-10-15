
package fire.msp.team;

import fire.pb.scene.manager.SceneTeamManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateTeam extends __GCreateTeam__ {
	@Override
	protected void process() {
		// protocol handle
		SceneTeamManager.getInstance().onCreateNewTeam(teamid, roleid);
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 728900;

	public int getType() {
		return 728900;
	}

	public long roleid; // 队长roleId
	public long teamid; // 队伍ID

	public GCreateTeam() {
	}

	public GCreateTeam(long _roleid_, long _teamid_) {
		this.roleid = _roleid_;
		this.teamid = _teamid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(teamid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		teamid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateTeam) {
			GCreateTeam _o_ = (GCreateTeam)_o1_;
			if (roleid != _o_.roleid) return false;
			if (teamid != _o_.teamid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)teamid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(teamid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GCreateTeam _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(teamid - _o_.teamid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

