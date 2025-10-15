
package fire.pb.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TeamInfoOctets implements Marshal , Comparable<TeamInfoOctets>{
	public long teamid; // 玩家的队伍ID，如果没有队伍则为0
	public byte teamindexstate; // 高四位是index[1,5]，低四位是state玩家的队伍状态[1,5]
	public byte hugindex; // [1,5]拥抱对象的index
	public byte normalnum; // 正常队员的数量 by changaho

	public TeamInfoOctets() {
	}

	public TeamInfoOctets(long _teamid_, byte _teamindexstate_, byte _hugindex_, byte _normalnum_) {
		this.teamid = _teamid_;
		this.teamindexstate = _teamindexstate_;
		this.hugindex = _hugindex_;
		this.normalnum = _normalnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(teamid);
		_os_.marshal(teamindexstate);
		_os_.marshal(hugindex);
		_os_.marshal(normalnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teamid = _os_.unmarshal_long();
		teamindexstate = _os_.unmarshal_byte();
		hugindex = _os_.unmarshal_byte();
		normalnum = _os_.unmarshal_byte();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TeamInfoOctets) {
			TeamInfoOctets _o_ = (TeamInfoOctets)_o1_;
			if (teamid != _o_.teamid) return false;
			if (teamindexstate != _o_.teamindexstate) return false;
			if (hugindex != _o_.hugindex) return false;
			if (normalnum != _o_.normalnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)teamid;
		_h_ += (int)teamindexstate;
		_h_ += (int)hugindex;
		_h_ += (int)normalnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamid).append(",");
		_sb_.append(teamindexstate).append(",");
		_sb_.append(hugindex).append(",");
		_sb_.append(normalnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(TeamInfoOctets _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(teamid - _o_.teamid);
		if (0 != _c_) return _c_;
		_c_ = teamindexstate - _o_.teamindexstate;
		if (0 != _c_) return _c_;
		_c_ = hugindex - _o_.hugindex;
		if (0 != _c_) return _c_;
		_c_ = normalnum - _o_.normalnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

