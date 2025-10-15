
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class GPair implements Marshal , Comparable<GPair>{
	public int key;
	public int value;

	public GPair() {
		key = 0;
		value = 0;
	}

	public GPair(int _key_, int _value_) {
		this.key = _key_;
		this.value = _value_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(key);
		_os_.marshal(value);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		key = _os_.unmarshal_int();
		value = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GPair) {
			GPair _o_ = (GPair)_o1_;
			if (key != _o_.key) return false;
			if (value != _o_.value) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += key;
		_h_ += value;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(key).append(",");
		_sb_.append(value).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GPair _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		_c_ = value - _o_.value;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

