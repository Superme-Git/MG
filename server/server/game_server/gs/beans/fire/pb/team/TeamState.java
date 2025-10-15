
package fire.pb.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TeamState implements Marshal , Comparable<TeamState>{
	public final static int eNormalTeam = 1; // 正常状态
	public final static int eWaitingSummonTeam = 2; // 等待中的队伍状态
	public final static int eSummonTeam = 3; // 召集令队伍状态


	public TeamState() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TeamState) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(TeamState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

