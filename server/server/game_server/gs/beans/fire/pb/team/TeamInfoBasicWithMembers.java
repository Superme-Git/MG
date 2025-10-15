
package fire.pb.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TeamInfoBasicWithMembers implements Marshal {
	public fire.pb.team.TeamInfoBasic teaminfobasic; // //队伍基本数据 by changhao
	public java.util.LinkedList<fire.pb.team.TeamMemberSimple> memberlist; // //成员基本数据 by changhao
	public int status; // 状态 0是正常1是申请中by changhao

	public TeamInfoBasicWithMembers() {
		teaminfobasic = new fire.pb.team.TeamInfoBasic();
		memberlist = new java.util.LinkedList<fire.pb.team.TeamMemberSimple>();
	}

	public TeamInfoBasicWithMembers(fire.pb.team.TeamInfoBasic _teaminfobasic_, java.util.LinkedList<fire.pb.team.TeamMemberSimple> _memberlist_, int _status_) {
		this.teaminfobasic = _teaminfobasic_;
		this.memberlist = _memberlist_;
		this.status = _status_;
	}

	public final boolean _validator_() {
		if (!teaminfobasic._validator_()) return false;
		for (fire.pb.team.TeamMemberSimple _v_ : memberlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(teaminfobasic);
		_os_.compact_uint32(memberlist.size());
		for (fire.pb.team.TeamMemberSimple _v_ : memberlist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(status);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teaminfobasic.unmarshal(_os_);
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.TeamMemberSimple _v_ = new fire.pb.team.TeamMemberSimple();
			_v_.unmarshal(_os_);
			memberlist.add(_v_);
		}
		status = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TeamInfoBasicWithMembers) {
			TeamInfoBasicWithMembers _o_ = (TeamInfoBasicWithMembers)_o1_;
			if (!teaminfobasic.equals(_o_.teaminfobasic)) return false;
			if (!memberlist.equals(_o_.memberlist)) return false;
			if (status != _o_.status) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += teaminfobasic.hashCode();
		_h_ += memberlist.hashCode();
		_h_ += status;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teaminfobasic).append(",");
		_sb_.append(memberlist).append(",");
		_sb_.append(status).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

