
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOneKeyApplyTeamInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOneKeyApplyTeamInfo extends __SOneKeyApplyTeamInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794518;

	public int getType() {
		return 794518;
	}

	public long teamid; // 队伍的id by changhao
	public java.util.LinkedList<fire.pb.team.TeamMemberSimple> memberlist;

	public SOneKeyApplyTeamInfo() {
		memberlist = new java.util.LinkedList<fire.pb.team.TeamMemberSimple>();
	}

	public SOneKeyApplyTeamInfo(long _teamid_, java.util.LinkedList<fire.pb.team.TeamMemberSimple> _memberlist_) {
		this.teamid = _teamid_;
		this.memberlist = _memberlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.team.TeamMemberSimple _v_ : memberlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(teamid);
		_os_.compact_uint32(memberlist.size());
		for (fire.pb.team.TeamMemberSimple _v_ : memberlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teamid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.TeamMemberSimple _v_ = new fire.pb.team.TeamMemberSimple();
			_v_.unmarshal(_os_);
			memberlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOneKeyApplyTeamInfo) {
			SOneKeyApplyTeamInfo _o_ = (SOneKeyApplyTeamInfo)_o1_;
			if (teamid != _o_.teamid) return false;
			if (!memberlist.equals(_o_.memberlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)teamid;
		_h_ += memberlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamid).append(",");
		_sb_.append(memberlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

