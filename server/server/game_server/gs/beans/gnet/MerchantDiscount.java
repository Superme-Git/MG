
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class MerchantDiscount implements Marshal {
	public int id;
	public com.locojoy.base.Octets name;
	public int reserved;
	public java.util.ArrayList<gnet.DiscountGrade> discount;

	public MerchantDiscount() {
		name = new com.locojoy.base.Octets();
		discount = new java.util.ArrayList<gnet.DiscountGrade>();
	}

	public MerchantDiscount(int _id_, com.locojoy.base.Octets _name_, int _reserved_, java.util.ArrayList<gnet.DiscountGrade> _discount_) {
		this.id = _id_;
		this.name = _name_;
		this.reserved = _reserved_;
		this.discount = _discount_;
	}

	public final boolean _validator_() {
		for (gnet.DiscountGrade _v_ : discount)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(name);
		_os_.marshal(reserved);
		_os_.compact_uint32(discount.size());
		for (gnet.DiscountGrade _v_ : discount) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		name = _os_.unmarshal_Octets();
		reserved = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			gnet.DiscountGrade _v_ = new gnet.DiscountGrade();
			_v_.unmarshal(_os_);
			discount.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MerchantDiscount) {
			MerchantDiscount _o_ = (MerchantDiscount)_o1_;
			if (id != _o_.id) return false;
			if (!name.equals(_o_.name)) return false;
			if (reserved != _o_.reserved) return false;
			if (!discount.equals(_o_.discount)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += name.hashCode();
		_h_ += reserved;
		_h_ += discount.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append("B").append(name.size()).append(",");
		_sb_.append(reserved).append(",");
		_sb_.append(discount).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

