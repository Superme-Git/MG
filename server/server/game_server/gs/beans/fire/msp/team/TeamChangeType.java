
package fire.msp.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TeamChangeType implements Marshal , Comparable<TeamChangeType>{
	public final static int CREATE = 1; // 创建队伍
	public final static int DISMISS = 2; // 销毁队伍
	public final static int SWITCH_LEADER = 3; // 交换队长
	public final static int ADD_NORMAL_MEMBER = 4; // 添加正常队员
	public final static int ADD_ABSENT_MEMBER = 5; // 添加暂离队员
	public final static int REMOVE_MEMBER = 6; // 删除队员
	public final static int SWITCH_MEMBER = 7; // 交换队员
	public final static int CHANGE_MEMBER_NORMAL = 8; // 成为正常队员
	public final static int CHANGE_MEMBER_ABSENT = 9; // 成为暂离队员
	public final static int MEMBER_OFFLINE = 10; // 队员下线


	public TeamChangeType() {
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
		if (_o1_ instanceof TeamChangeType) {
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

	public int compareTo(TeamChangeType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

