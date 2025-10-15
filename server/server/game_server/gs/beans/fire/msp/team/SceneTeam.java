
package fire.msp.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SceneTeam implements Marshal {
	public long teamid;
	public long leaderid;
	public java.util.LinkedList<fire.msp.team.SceneTeamMember> memebers;

	public SceneTeam() {
		memebers = new java.util.LinkedList<fire.msp.team.SceneTeamMember>();
	}

	public SceneTeam(long _teamid_, long _leaderid_, java.util.LinkedList<fire.msp.team.SceneTeamMember> _memebers_) {
		this.teamid = _teamid_;
		this.leaderid = _leaderid_;
		this.memebers = _memebers_;
	}

	public final boolean _validator_() {
		for (fire.msp.team.SceneTeamMember _v_ : memebers)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(teamid);
		_os_.marshal(leaderid);
		_os_.compact_uint32(memebers.size());
		for (fire.msp.team.SceneTeamMember _v_ : memebers) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teamid = _os_.unmarshal_long();
		leaderid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.msp.team.SceneTeamMember _v_ = new fire.msp.team.SceneTeamMember();
			_v_.unmarshal(_os_);
			memebers.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SceneTeam) {
			SceneTeam _o_ = (SceneTeam)_o1_;
			if (teamid != _o_.teamid) return false;
			if (leaderid != _o_.leaderid) return false;
			if (!memebers.equals(_o_.memebers)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)teamid;
		_h_ += (int)leaderid;
		_h_ += memebers.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamid).append(",");
		_sb_.append(leaderid).append(",");
		_sb_.append(memebers).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

