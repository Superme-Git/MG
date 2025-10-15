
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GetUserCouponRep__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GetUserCouponRep extends __GetUserCouponRep__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 217;

	public int getType() {
		return 217;
	}

	public int retcode;
	public int userid;
	public int remain_coupon; // 点券余额
	public int today_exchange_coupon; // 今天已使用的点券
	public int today_remain_exchange_coupon; // 今天还可使用的点券
	public int reserved1;
	public int reserved2;

	public GetUserCouponRep() {
	}

	public GetUserCouponRep(int _retcode_, int _userid_, int _remain_coupon_, int _today_exchange_coupon_, int _today_remain_exchange_coupon_, int _reserved1_, int _reserved2_) {
		this.retcode = _retcode_;
		this.userid = _userid_;
		this.remain_coupon = _remain_coupon_;
		this.today_exchange_coupon = _today_exchange_coupon_;
		this.today_remain_exchange_coupon = _today_remain_exchange_coupon_;
		this.reserved1 = _reserved1_;
		this.reserved2 = _reserved2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(retcode);
		_os_.marshal(userid);
		_os_.marshal(remain_coupon);
		_os_.marshal(today_exchange_coupon);
		_os_.marshal(today_remain_exchange_coupon);
		_os_.marshal(reserved1);
		_os_.marshal(reserved2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		retcode = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		remain_coupon = _os_.unmarshal_int();
		today_exchange_coupon = _os_.unmarshal_int();
		today_remain_exchange_coupon = _os_.unmarshal_int();
		reserved1 = _os_.unmarshal_int();
		reserved2 = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GetUserCouponRep) {
			GetUserCouponRep _o_ = (GetUserCouponRep)_o1_;
			if (retcode != _o_.retcode) return false;
			if (userid != _o_.userid) return false;
			if (remain_coupon != _o_.remain_coupon) return false;
			if (today_exchange_coupon != _o_.today_exchange_coupon) return false;
			if (today_remain_exchange_coupon != _o_.today_remain_exchange_coupon) return false;
			if (reserved1 != _o_.reserved1) return false;
			if (reserved2 != _o_.reserved2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += retcode;
		_h_ += userid;
		_h_ += remain_coupon;
		_h_ += today_exchange_coupon;
		_h_ += today_remain_exchange_coupon;
		_h_ += reserved1;
		_h_ += reserved2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(retcode).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(remain_coupon).append(",");
		_sb_.append(today_exchange_coupon).append(",");
		_sb_.append(today_remain_exchange_coupon).append(",");
		_sb_.append(reserved1).append(",");
		_sb_.append(reserved2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GetUserCouponRep _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = remain_coupon - _o_.remain_coupon;
		if (0 != _c_) return _c_;
		_c_ = today_exchange_coupon - _o_.today_exchange_coupon;
		if (0 != _c_) return _c_;
		_c_ = today_remain_exchange_coupon - _o_.today_remain_exchange_coupon;
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

