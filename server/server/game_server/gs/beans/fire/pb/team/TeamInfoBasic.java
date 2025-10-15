
package fire.pb.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TeamInfoBasic implements Marshal {
	public long teamid; // 队伍ID
	public long leaderid; // 队长ID
	public int minlevel; // 等级下限
	public int maxlevel; // 等级上限
	public java.lang.String leadername; // 队长名字
	public int leaderlevel; // 队长等级
	public int leaderschool; // 队长职业
	public int membernum; // 队伍人数
	public int membermaxnum; // 队伍最大人数 匹配队伍需要 by changhao
	public int targetid; // 队伍目标 by changhao

	public TeamInfoBasic() {
		leadername = "";
	}

	public TeamInfoBasic(long _teamid_, long _leaderid_, int _minlevel_, int _maxlevel_, java.lang.String _leadername_, int _leaderlevel_, int _leaderschool_, int _membernum_, int _membermaxnum_, int _targetid_) {
		this.teamid = _teamid_;
		this.leaderid = _leaderid_;
		this.minlevel = _minlevel_;
		this.maxlevel = _maxlevel_;
		this.leadername = _leadername_;
		this.leaderlevel = _leaderlevel_;
		this.leaderschool = _leaderschool_;
		this.membernum = _membernum_;
		this.membermaxnum = _membermaxnum_;
		this.targetid = _targetid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(teamid);
		_os_.marshal(leaderid);
		_os_.marshal(minlevel);
		_os_.marshal(maxlevel);
		_os_.marshal(leadername, "UTF-16LE");
		_os_.marshal(leaderlevel);
		_os_.marshal(leaderschool);
		_os_.marshal(membernum);
		_os_.marshal(membermaxnum);
		_os_.marshal(targetid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teamid = _os_.unmarshal_long();
		leaderid = _os_.unmarshal_long();
		minlevel = _os_.unmarshal_int();
		maxlevel = _os_.unmarshal_int();
		leadername = _os_.unmarshal_String("UTF-16LE");
		leaderlevel = _os_.unmarshal_int();
		leaderschool = _os_.unmarshal_int();
		membernum = _os_.unmarshal_int();
		membermaxnum = _os_.unmarshal_int();
		targetid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TeamInfoBasic) {
			TeamInfoBasic _o_ = (TeamInfoBasic)_o1_;
			if (teamid != _o_.teamid) return false;
			if (leaderid != _o_.leaderid) return false;
			if (minlevel != _o_.minlevel) return false;
			if (maxlevel != _o_.maxlevel) return false;
			if (!leadername.equals(_o_.leadername)) return false;
			if (leaderlevel != _o_.leaderlevel) return false;
			if (leaderschool != _o_.leaderschool) return false;
			if (membernum != _o_.membernum) return false;
			if (membermaxnum != _o_.membermaxnum) return false;
			if (targetid != _o_.targetid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)teamid;
		_h_ += (int)leaderid;
		_h_ += minlevel;
		_h_ += maxlevel;
		_h_ += leadername.hashCode();
		_h_ += leaderlevel;
		_h_ += leaderschool;
		_h_ += membernum;
		_h_ += membermaxnum;
		_h_ += targetid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamid).append(",");
		_sb_.append(leaderid).append(",");
		_sb_.append(minlevel).append(",");
		_sb_.append(maxlevel).append(",");
		_sb_.append("T").append(leadername.length()).append(",");
		_sb_.append(leaderlevel).append(",");
		_sb_.append(leaderschool).append(",");
		_sb_.append(membernum).append(",");
		_sb_.append(membermaxnum).append(",");
		_sb_.append(targetid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

