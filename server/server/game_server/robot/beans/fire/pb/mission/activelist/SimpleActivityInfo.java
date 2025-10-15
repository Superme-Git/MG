
package fire.pb.mission.activelist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SimpleActivityInfo implements Marshal , Comparable<SimpleActivityInfo>{
	public int num; // 活动次数
	public int num2; // 活动次数2
	public int activevalue; // 活动获得活跃度

	public SimpleActivityInfo() {
	}

	public SimpleActivityInfo(int _num_, int _num2_, int _activevalue_) {
		this.num = _num_;
		this.num2 = _num2_;
		this.activevalue = _activevalue_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(num);
		_os_.marshal(num2);
		_os_.marshal(activevalue);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		num = _os_.unmarshal_int();
		num2 = _os_.unmarshal_int();
		activevalue = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SimpleActivityInfo) {
			SimpleActivityInfo _o_ = (SimpleActivityInfo)_o1_;
			if (num != _o_.num) return false;
			if (num2 != _o_.num2) return false;
			if (activevalue != _o_.activevalue) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += num;
		_h_ += num2;
		_h_ += activevalue;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(num).append(",");
		_sb_.append(num2).append(",");
		_sb_.append(activevalue).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SimpleActivityInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		_c_ = num2 - _o_.num2;
		if (0 != _c_) return _c_;
		_c_ = activevalue - _o_.activevalue;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

