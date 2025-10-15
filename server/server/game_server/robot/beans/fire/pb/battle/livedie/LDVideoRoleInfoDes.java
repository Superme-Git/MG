
package fire.pb.battle.livedie;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class LDVideoRoleInfoDes implements Marshal {
	public fire.pb.battle.livedie.LDRoleInfoDes role1; // 对手1
	public fire.pb.battle.livedie.LDRoleInfoDes role2; // 对手1
	public java.util.ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes> teamlist1; // //如果是组队，队员详情
	public java.util.ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes> teamlist2; // //如果是组队，队员详情
	public int battleresult; // 1胜利  -1失败  0平局
	public int rosenum; // 点赞次数
	public int roseflag; // 是否可以点赞 0可以  1不可
	public java.lang.String videoid; // 录像id

	public LDVideoRoleInfoDes() {
		role1 = new fire.pb.battle.livedie.LDRoleInfoDes();
		role2 = new fire.pb.battle.livedie.LDRoleInfoDes();
		teamlist1 = new java.util.ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes>();
		teamlist2 = new java.util.ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes>();
		videoid = "";
	}

	public LDVideoRoleInfoDes(fire.pb.battle.livedie.LDRoleInfoDes _role1_, fire.pb.battle.livedie.LDRoleInfoDes _role2_, java.util.ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes> _teamlist1_, java.util.ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes> _teamlist2_, int _battleresult_, int _rosenum_, int _roseflag_, java.lang.String _videoid_) {
		this.role1 = _role1_;
		this.role2 = _role2_;
		this.teamlist1 = _teamlist1_;
		this.teamlist2 = _teamlist2_;
		this.battleresult = _battleresult_;
		this.rosenum = _rosenum_;
		this.roseflag = _roseflag_;
		this.videoid = _videoid_;
	}

	public final boolean _validator_() {
		if (!role1._validator_()) return false;
		if (!role2._validator_()) return false;
		for (fire.pb.battle.livedie.LDTeamRoleInfoDes _v_ : teamlist1)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.livedie.LDTeamRoleInfoDes _v_ : teamlist2)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(role1);
		_os_.marshal(role2);
		_os_.compact_uint32(teamlist1.size());
		for (fire.pb.battle.livedie.LDTeamRoleInfoDes _v_ : teamlist1) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(teamlist2.size());
		for (fire.pb.battle.livedie.LDTeamRoleInfoDes _v_ : teamlist2) {
			_os_.marshal(_v_);
		}
		_os_.marshal(battleresult);
		_os_.marshal(rosenum);
		_os_.marshal(roseflag);
		_os_.marshal(videoid, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		role1.unmarshal(_os_);
		role2.unmarshal(_os_);
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.livedie.LDTeamRoleInfoDes _v_ = new fire.pb.battle.livedie.LDTeamRoleInfoDes();
			_v_.unmarshal(_os_);
			teamlist1.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.livedie.LDTeamRoleInfoDes _v_ = new fire.pb.battle.livedie.LDTeamRoleInfoDes();
			_v_.unmarshal(_os_);
			teamlist2.add(_v_);
		}
		battleresult = _os_.unmarshal_int();
		rosenum = _os_.unmarshal_int();
		roseflag = _os_.unmarshal_int();
		videoid = _os_.unmarshal_String("UTF-16LE");
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LDVideoRoleInfoDes) {
			LDVideoRoleInfoDes _o_ = (LDVideoRoleInfoDes)_o1_;
			if (!role1.equals(_o_.role1)) return false;
			if (!role2.equals(_o_.role2)) return false;
			if (!teamlist1.equals(_o_.teamlist1)) return false;
			if (!teamlist2.equals(_o_.teamlist2)) return false;
			if (battleresult != _o_.battleresult) return false;
			if (rosenum != _o_.rosenum) return false;
			if (roseflag != _o_.roseflag) return false;
			if (!videoid.equals(_o_.videoid)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += role1.hashCode();
		_h_ += role2.hashCode();
		_h_ += teamlist1.hashCode();
		_h_ += teamlist2.hashCode();
		_h_ += battleresult;
		_h_ += rosenum;
		_h_ += roseflag;
		_h_ += videoid.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(role1).append(",");
		_sb_.append(role2).append(",");
		_sb_.append(teamlist1).append(",");
		_sb_.append(teamlist2).append(",");
		_sb_.append(battleresult).append(",");
		_sb_.append(rosenum).append(",");
		_sb_.append(roseflag).append(",");
		_sb_.append("T").append(videoid.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

