
package fire.pb.attr;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleCurrency implements Marshal , Comparable<RoleCurrency>{
	public final static int GUILD_DKP = 1; // 公会DKP
	public final static int GUILD_DED = 2; // 公会贡献
	public final static int TEACHER_SCORE = 3; // 师徒值
	public final static int ACTIVE_SCORE = 4; // 活动积分
	public final static int HONOR_SCORE = 5; // 荣誉值
	public final static int POP_SCORE = 6; // 声望值
	public final static int FRIEND_SCORE = 7; // 好友积分
	public final static int PROF_CONTR = 9; // 职业贡献
	public final static int EREDITPOINT_SCORE = 10; // 信用点


	public RoleCurrency() {
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
		if (_o1_ instanceof RoleCurrency) {
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

	public int compareTo(RoleCurrency _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

