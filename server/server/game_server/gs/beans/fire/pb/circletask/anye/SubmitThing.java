
package fire.pb.circletask.anye;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SubmitThing implements Marshal , Comparable<SubmitThing>{
	public int key; // 给予物品或者道具的key
	public int num; // 给予物品或者道具的数量

	public SubmitThing() {
	}

	public SubmitThing(int _key_, int _num_) {
		this.key = _key_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		if (key <= 0) return false;
		if (num <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(key);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		key = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SubmitThing) {
			SubmitThing _o_ = (SubmitThing)_o1_;
			if (key != _o_.key) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += key;
		_h_ += num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(key).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SubmitThing _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

