
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class DemoAttr implements Marshal , Comparable<DemoAttr>{
	public int fighterid;
	public int attrid; // 属性id
	public int value; // 最终值

	public DemoAttr() {
	}

	public DemoAttr(int _fighterid_, int _attrid_, int _value_) {
		this.fighterid = _fighterid_;
		this.attrid = _attrid_;
		this.value = _value_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(fighterid);
		_os_.marshal(attrid);
		_os_.marshal(value);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		fighterid = _os_.unmarshal_int();
		attrid = _os_.unmarshal_int();
		value = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DemoAttr) {
			DemoAttr _o_ = (DemoAttr)_o1_;
			if (fighterid != _o_.fighterid) return false;
			if (attrid != _o_.attrid) return false;
			if (value != _o_.value) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += fighterid;
		_h_ += attrid;
		_h_ += value;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(fighterid).append(",");
		_sb_.append(attrid).append(",");
		_sb_.append(value).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(DemoAttr _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = fighterid - _o_.fighterid;
		if (0 != _c_) return _c_;
		_c_ = attrid - _o_.attrid;
		if (0 != _c_) return _c_;
		_c_ = value - _o_.value;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

