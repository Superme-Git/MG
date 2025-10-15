
package fire.msp.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GNotifyTeamChange__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GNotifyTeamChange extends __GNotifyTeamChange__ {
	@Override
	protected void process() {
		fire.pb.scene.manager.SceneTeamManager.getInstance().onUpdateTeam(teamid, changetype, roleid, roleid2);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 728903;

	public int getType() {
		return 728903;
	}

	public int changetype; // 队伍改变的类型,参考TeamChangeType
	public long teamid; // 队伍ID
	public long roleid; // 与改变相关的roleid
	public long roleid2; // 如果是交换队员位置，会有第二个roleid

	public GNotifyTeamChange() {
	}

	public GNotifyTeamChange(int _changetype_, long _teamid_, long _roleid_, long _roleid2_) {
		this.changetype = _changetype_;
		this.teamid = _teamid_;
		this.roleid = _roleid_;
		this.roleid2 = _roleid2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(changetype);
		_os_.marshal(teamid);
		_os_.marshal(roleid);
		_os_.marshal(roleid2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		changetype = _os_.unmarshal_int();
		teamid = _os_.unmarshal_long();
		roleid = _os_.unmarshal_long();
		roleid2 = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GNotifyTeamChange) {
			GNotifyTeamChange _o_ = (GNotifyTeamChange)_o1_;
			if (changetype != _o_.changetype) return false;
			if (teamid != _o_.teamid) return false;
			if (roleid != _o_.roleid) return false;
			if (roleid2 != _o_.roleid2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += changetype;
		_h_ += (int)teamid;
		_h_ += (int)roleid;
		_h_ += (int)roleid2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(changetype).append(",");
		_sb_.append(teamid).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(roleid2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GNotifyTeamChange _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = changetype - _o_.changetype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(teamid - _o_.teamid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(roleid2 - _o_.roleid2);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

