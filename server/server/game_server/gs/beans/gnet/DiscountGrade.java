
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class DiscountGrade implements Marshal {
	public int amount_begin;
	public int discount;
	public int reserved1;
	public com.locojoy.base.Octets reserved2;

	public DiscountGrade() {
		reserved2 = new com.locojoy.base.Octets();
	}

	public DiscountGrade(int _amount_begin_, int _discount_, int _reserved1_, com.locojoy.base.Octets _reserved2_) {
		this.amount_begin = _amount_begin_;
		this.discount = _discount_;
		this.reserved1 = _reserved1_;
		this.reserved2 = _reserved2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(amount_begin);
		_os_.marshal(discount);
		_os_.marshal(reserved1);
		_os_.marshal(reserved2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		amount_begin = _os_.unmarshal_int();
		discount = _os_.unmarshal_int();
		reserved1 = _os_.unmarshal_int();
		reserved2 = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DiscountGrade) {
			DiscountGrade _o_ = (DiscountGrade)_o1_;
			if (amount_begin != _o_.amount_begin) return false;
			if (discount != _o_.discount) return false;
			if (reserved1 != _o_.reserved1) return false;
			if (!reserved2.equals(_o_.reserved2)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += amount_begin;
		_h_ += discount;
		_h_ += reserved1;
		_h_ += reserved2.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(amount_begin).append(",");
		_sb_.append(discount).append(",");
		_sb_.append(reserved1).append(",");
		_sb_.append("B").append(reserved2.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

