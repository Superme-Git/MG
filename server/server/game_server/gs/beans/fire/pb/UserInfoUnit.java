
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class UserInfoUnit implements Marshal {
	public java.lang.String key;
	public java.lang.String value;

	public UserInfoUnit() {
		key = "";
		value = "";
	}

	public UserInfoUnit(java.lang.String _key_, java.lang.String _value_) {
		this.key = _key_;
		this.value = _value_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(key, "UTF-16LE");
		_os_.marshal(value, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		key = _os_.unmarshal_String("UTF-16LE");
		value = _os_.unmarshal_String("UTF-16LE");
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof UserInfoUnit) {
			UserInfoUnit _o_ = (UserInfoUnit)_o1_;
			if (!key.equals(_o_.key)) return false;
			if (!value.equals(_o_.value)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += key.hashCode();
		_h_ += value.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(key.length()).append(",");
		_sb_.append("T").append(value.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

