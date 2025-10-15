
package fire.pb.circletask;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RewardItemUnit implements Marshal , Comparable<RewardItemUnit>{
	public int baseid; // 奖励道具的基本id
	public int num; // 奖励道具的数量

	public RewardItemUnit() {
	}

	public RewardItemUnit(int _baseid_, int _num_) {
		this.baseid = _baseid_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		if (baseid <= 0) return false;
		if (num <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(baseid);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		baseid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RewardItemUnit) {
			RewardItemUnit _o_ = (RewardItemUnit)_o1_;
			if (baseid != _o_.baseid) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += baseid;
		_h_ += num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(baseid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(RewardItemUnit _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = baseid - _o_.baseid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

