
package fire.pb.fushi;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ChargeReturnProfit implements Marshal , Comparable<ChargeReturnProfit>{
	public int id;
	public int value; // 当前值 by changhao
	public int maxvalue; // 最大值 by changhao
	public int status; // 0是领取了1是未领取2是未到达 by changhao

	public ChargeReturnProfit() {
	}

	public ChargeReturnProfit(int _id_, int _value_, int _maxvalue_, int _status_) {
		this.id = _id_;
		this.value = _value_;
		this.maxvalue = _maxvalue_;
		this.status = _status_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(value);
		_os_.marshal(maxvalue);
		_os_.marshal(status);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		value = _os_.unmarshal_int();
		maxvalue = _os_.unmarshal_int();
		status = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ChargeReturnProfit) {
			ChargeReturnProfit _o_ = (ChargeReturnProfit)_o1_;
			if (id != _o_.id) return false;
			if (value != _o_.value) return false;
			if (maxvalue != _o_.maxvalue) return false;
			if (status != _o_.status) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += value;
		_h_ += maxvalue;
		_h_ += status;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(value).append(",");
		_sb_.append(maxvalue).append(",");
		_sb_.append(status).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ChargeReturnProfit _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = value - _o_.value;
		if (0 != _c_) return _c_;
		_c_ = maxvalue - _o_.maxvalue;
		if (0 != _c_) return _c_;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

