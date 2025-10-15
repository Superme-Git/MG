
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleForbid implements Marshal {
	public byte ltype; // default 0
	public int time; // 剩余封禁时间
	public int createtime; // 封禁开始时间
	public com.locojoy.base.Octets reason;

	public RoleForbid() {
		ltype = 0;
		time = 0;
		createtime = 0;
		reason = new com.locojoy.base.Octets();
	}

	public RoleForbid(byte _ltype_, int _time_, int _createtime_, com.locojoy.base.Octets _reason_) {
		this.ltype = _ltype_;
		this.time = _time_;
		this.createtime = _createtime_;
		this.reason = _reason_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(ltype);
		_os_.marshal(time);
		_os_.marshal(createtime);
		_os_.marshal(reason);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ltype = _os_.unmarshal_byte();
		time = _os_.unmarshal_int();
		createtime = _os_.unmarshal_int();
		reason = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleForbid) {
			RoleForbid _o_ = (RoleForbid)_o1_;
			if (ltype != _o_.ltype) return false;
			if (time != _o_.time) return false;
			if (createtime != _o_.createtime) return false;
			if (!reason.equals(_o_.reason)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)ltype;
		_h_ += time;
		_h_ += createtime;
		_h_ += reason.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ltype).append(",");
		_sb_.append(time).append(",");
		_sb_.append(createtime).append(",");
		_sb_.append("B").append(reason.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

