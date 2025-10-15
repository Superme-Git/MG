
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestTeamMatchList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestTeamMatchList extends __SRequestTeamMatchList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794510;

	public int getType() {
		return 794510;
	}

	public int ret; // 错误返回0正确1目标错误2数量错误3其他未知错误 by changhao
	public int targetid; // 目标ID by changhao
	public java.util.LinkedList<fire.pb.team.TeamInfoBasicWithMembers> teamlist; // 一个队伍简单信息 by changhao

	public SRequestTeamMatchList() {
		teamlist = new java.util.LinkedList<fire.pb.team.TeamInfoBasicWithMembers>();
	}

	public SRequestTeamMatchList(int _ret_, int _targetid_, java.util.LinkedList<fire.pb.team.TeamInfoBasicWithMembers> _teamlist_) {
		this.ret = _ret_;
		this.targetid = _targetid_;
		this.teamlist = _teamlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.team.TeamInfoBasicWithMembers _v_ : teamlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ret);
		_os_.marshal(targetid);
		_os_.compact_uint32(teamlist.size());
		for (fire.pb.team.TeamInfoBasicWithMembers _v_ : teamlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ret = _os_.unmarshal_int();
		targetid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.TeamInfoBasicWithMembers _v_ = new fire.pb.team.TeamInfoBasicWithMembers();
			_v_.unmarshal(_os_);
			teamlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestTeamMatchList) {
			SRequestTeamMatchList _o_ = (SRequestTeamMatchList)_o1_;
			if (ret != _o_.ret) return false;
			if (targetid != _o_.targetid) return false;
			if (!teamlist.equals(_o_.teamlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += ret;
		_h_ += targetid;
		_h_ += teamlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ret).append(",");
		_sb_.append(targetid).append(",");
		_sb_.append(teamlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

