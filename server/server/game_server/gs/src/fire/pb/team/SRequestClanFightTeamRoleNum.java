
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestClanFightTeamRoleNum__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestClanFightTeamRoleNum extends __SRequestClanFightTeamRoleNum__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794562;

	public int getType() {
		return 794562;
	}

	public int teamnum; // team num by changhao
	public int rolenum; // role num by changhao

	public SRequestClanFightTeamRoleNum() {
	}

	public SRequestClanFightTeamRoleNum(int _teamnum_, int _rolenum_) {
		this.teamnum = _teamnum_;
		this.rolenum = _rolenum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(teamnum);
		_os_.marshal(rolenum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teamnum = _os_.unmarshal_int();
		rolenum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestClanFightTeamRoleNum) {
			SRequestClanFightTeamRoleNum _o_ = (SRequestClanFightTeamRoleNum)_o1_;
			if (teamnum != _o_.teamnum) return false;
			if (rolenum != _o_.rolenum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += teamnum;
		_h_ += rolenum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamnum).append(",");
		_sb_.append(rolenum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRequestClanFightTeamRoleNum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = teamnum - _o_.teamnum;
		if (0 != _c_) return _c_;
		_c_ = rolenum - _o_.rolenum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

