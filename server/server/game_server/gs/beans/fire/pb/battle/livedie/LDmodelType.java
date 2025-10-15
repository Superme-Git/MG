
package fire.pb.battle.livedie;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class LDmodelType implements Marshal , Comparable<LDmodelType>{
	public final static int DAY_FIGHT = 1; // 本日生死战排行
	public final static int WEEK_FIGHT = 2; // 本周生死战排行
	public final static int HIS_FIGHT = 3; // 历史生死战排行
	public final static int SELF_FIGHT = 4; // 自己


	public LDmodelType() {
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
		if (_o1_ instanceof LDmodelType) {
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

	public int compareTo(LDmodelType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

