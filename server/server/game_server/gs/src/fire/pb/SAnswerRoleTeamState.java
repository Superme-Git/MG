
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAnswerRoleTeamState__ extends mkio.Protocol { }

/** 返回玩家请求的其他玩家的组队情况
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAnswerRoleTeamState extends __SAnswerRoleTeamState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786465;

	public int getType() {
		return 786465;
	}

	public long roleid;
	public int level; // 等级
	public int teamstate; // 是否有队伍，0表示没有队伍，1表示有队伍

	public SAnswerRoleTeamState() {
	}

	public SAnswerRoleTeamState(long _roleid_, int _level_, int _teamstate_) {
		this.roleid = _roleid_;
		this.level = _level_;
		this.teamstate = _teamstate_;
	}

	public final boolean _validator_() {
		if (roleid <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(level);
		_os_.marshal(teamstate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		level = _os_.unmarshal_int();
		teamstate = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAnswerRoleTeamState) {
			SAnswerRoleTeamState _o_ = (SAnswerRoleTeamState)_o1_;
			if (roleid != _o_.roleid) return false;
			if (level != _o_.level) return false;
			if (teamstate != _o_.teamstate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += level;
		_h_ += teamstate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(level).append(",");
		_sb_.append(teamstate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SAnswerRoleTeamState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		_c_ = teamstate - _o_.teamstate;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

