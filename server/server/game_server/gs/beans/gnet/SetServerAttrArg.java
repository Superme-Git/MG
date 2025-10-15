
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 获取及设置服务器全局信息 设置服务器最大在线人数，获取服务器当前在线人数
*/
public class SetServerAttrArg implements Marshal , Comparable<SetServerAttrArg>{
	public int gmuserid;
	public int localsid;
	public int attribute;
	public int value;

	public SetServerAttrArg() {
	}

	public SetServerAttrArg(int _gmuserid_, int _localsid_, int _attribute_, int _value_) {
		this.gmuserid = _gmuserid_;
		this.localsid = _localsid_;
		this.attribute = _attribute_;
		this.value = _value_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(gmuserid);
		_os_.marshal(localsid);
		_os_.marshal(attribute);
		_os_.marshal(value);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		gmuserid = _os_.unmarshal_int();
		localsid = _os_.unmarshal_int();
		attribute = _os_.unmarshal_int();
		value = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SetServerAttrArg) {
			SetServerAttrArg _o_ = (SetServerAttrArg)_o1_;
			if (gmuserid != _o_.gmuserid) return false;
			if (localsid != _o_.localsid) return false;
			if (attribute != _o_.attribute) return false;
			if (value != _o_.value) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += gmuserid;
		_h_ += localsid;
		_h_ += attribute;
		_h_ += value;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(gmuserid).append(",");
		_sb_.append(localsid).append(",");
		_sb_.append(attribute).append(",");
		_sb_.append(value).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SetServerAttrArg _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = gmuserid - _o_.gmuserid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = attribute - _o_.attribute;
		if (0 != _c_) return _c_;
		_c_ = value - _o_.value;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

