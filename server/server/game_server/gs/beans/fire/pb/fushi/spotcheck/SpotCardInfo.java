
package fire.pb.fushi.spotcheck;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SpotCardInfo implements Marshal , Comparable<SpotCardInfo>{
	public int num; // 数量
	public int price; // 价格

	public SpotCardInfo() {
	}

	public SpotCardInfo(int _num_, int _price_) {
		this.num = _num_;
		this.price = _price_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(num);
		_os_.marshal(price);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		num = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SpotCardInfo) {
			SpotCardInfo _o_ = (SpotCardInfo)_o1_;
			if (num != _o_.num) return false;
			if (price != _o_.price) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += num;
		_h_ += price;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(num).append(",");
		_sb_.append(price).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SpotCardInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		_c_ = price - _o_.price;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

