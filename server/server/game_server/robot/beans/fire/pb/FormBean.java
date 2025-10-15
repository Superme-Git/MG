
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class FormBean implements Marshal , Comparable<FormBean>{
	public int activetimes;
	public int level; // 等级
	public int exp; // 经验 by changhao

	public FormBean() {
	}

	public FormBean(int _activetimes_, int _level_, int _exp_) {
		this.activetimes = _activetimes_;
		this.level = _level_;
		this.exp = _exp_;
	}

	public final boolean _validator_() {
		if (level < 0) return false;
		if (exp < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(activetimes);
		_os_.marshal(level);
		_os_.marshal(exp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		activetimes = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		exp = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof FormBean) {
			FormBean _o_ = (FormBean)_o1_;
			if (activetimes != _o_.activetimes) return false;
			if (level != _o_.level) return false;
			if (exp != _o_.exp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += activetimes;
		_h_ += level;
		_h_ += exp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(activetimes).append(",");
		_sb_.append(level).append(",");
		_sb_.append(exp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(FormBean _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = activetimes - _o_.activetimes;
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		_c_ = exp - _o_.exp;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

