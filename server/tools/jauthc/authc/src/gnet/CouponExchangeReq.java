
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CouponExchangeReq__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CouponExchangeReq extends __CouponExchangeReq__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 218;

	public int getType() {
		return 218;
	}

	public int userid;
	public int coupon_number; // 要兑换的点券数量
	public int cash_number; // 兑换的数量
	public int reserved1;
	public int reserved2;

	public CouponExchangeReq() {
	}

	public CouponExchangeReq(int _userid_, int _coupon_number_, int _cash_number_, int _reserved1_, int _reserved2_) {
		this.userid = _userid_;
		this.coupon_number = _coupon_number_;
		this.cash_number = _cash_number_;
		this.reserved1 = _reserved1_;
		this.reserved2 = _reserved2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(userid);
		_os_.marshal(coupon_number);
		_os_.marshal(cash_number);
		_os_.marshal(reserved1);
		_os_.marshal(reserved2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		coupon_number = _os_.unmarshal_int();
		cash_number = _os_.unmarshal_int();
		reserved1 = _os_.unmarshal_int();
		reserved2 = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CouponExchangeReq) {
			CouponExchangeReq _o_ = (CouponExchangeReq)_o1_;
			if (userid != _o_.userid) return false;
			if (coupon_number != _o_.coupon_number) return false;
			if (cash_number != _o_.cash_number) return false;
			if (reserved1 != _o_.reserved1) return false;
			if (reserved2 != _o_.reserved2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += coupon_number;
		_h_ += cash_number;
		_h_ += reserved1;
		_h_ += reserved2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(coupon_number).append(",");
		_sb_.append(cash_number).append(",");
		_sb_.append(reserved1).append(",");
		_sb_.append(reserved2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CouponExchangeReq _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = coupon_number - _o_.coupon_number;
		if (0 != _c_) return _c_;
		_c_ = cash_number - _o_.cash_number;
		if (0 != _c_) return _c_;
		_c_ = reserved1 - _o_.reserved1;
		if (0 != _c_) return _c_;
		_c_ = reserved2 - _o_.reserved2;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

