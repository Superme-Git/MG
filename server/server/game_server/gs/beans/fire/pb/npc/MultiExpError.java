
package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class MultiExpError implements Marshal , Comparable<MultiExpError>{
	public final static int LevelConditionErr = -2; // 等级不够
	public final static int ExistOtherMultiExpBuff = -3; // 存在其他多倍经验的buff，不能领取别的。
	public final static int NoOneMultiExpBuff = -4; // 不存在多倍经验的buff，不能退还
	public final static int NoHaveBuffTime = -5; // 没有可以领取的时间，不能领取
	public final static int OverMaxHours = -6; // 身上多倍时间的buff大于4小时


	public MultiExpError() {
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
		if (_o1_ instanceof MultiExpError) {
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

	public int compareTo(MultiExpError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

