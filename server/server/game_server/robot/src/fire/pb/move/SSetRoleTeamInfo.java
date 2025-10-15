
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSetRoleTeamInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSetRoleTeamInfo extends __SSetRoleTeamInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790444;

	public int getType() {
		return 790444;
	}

	public long roleid;
	public long teamid;
	public int teamindex;
	public int teamstate;
	public int teamnormalnum; // 正常队员的数量 by changhao

	public SSetRoleTeamInfo() {
	}

	public SSetRoleTeamInfo(long _roleid_, long _teamid_, int _teamindex_, int _teamstate_, int _teamnormalnum_) {
		this.roleid = _roleid_;
		this.teamid = _teamid_;
		this.teamindex = _teamindex_;
		this.teamstate = _teamstate_;
		this.teamnormalnum = _teamnormalnum_;
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
		_os_.marshal(teamindex);
		_os_.marshal(teamstate);
		_os_.marshal(teamnormalnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		teamid = _os_.unmarshal_long();
		teamindex = _os_.unmarshal_int();
		teamstate = _os_.unmarshal_int();
		teamnormalnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSetRoleTeamInfo) {
			SSetRoleTeamInfo _o_ = (SSetRoleTeamInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (teamid != _o_.teamid) return false;
			if (teamindex != _o_.teamindex) return false;
			if (teamstate != _o_.teamstate) return false;
			if (teamnormalnum != _o_.teamnormalnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)teamid;
		_h_ += teamindex;
		_h_ += teamstate;
		_h_ += teamnormalnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(teamid).append(",");
		_sb_.append(teamindex).append(",");
		_sb_.append(teamstate).append(",");
		_sb_.append(teamnormalnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSetRoleTeamInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(teamid - _o_.teamid);
		if (0 != _c_) return _c_;
		_c_ = teamindex - _o_.teamindex;
		if (0 != _c_) return _c_;
		_c_ = teamstate - _o_.teamstate;
		if (0 != _c_) return _c_;
		_c_ = teamnormalnum - _o_.teamnormalnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

