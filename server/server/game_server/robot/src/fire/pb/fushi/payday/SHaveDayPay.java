
package fire.pb.fushi.payday;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SHaveDayPay__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SHaveDayPay extends __SHaveDayPay__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812593;

	public int getType() {
		return 812593;
	}

	public int daypay; // 0是没有1是已经消耗 by changhao

	public SHaveDayPay() {
	}

	public SHaveDayPay(int _daypay_) {
		this.daypay = _daypay_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(daypay);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		daypay = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SHaveDayPay) {
			SHaveDayPay _o_ = (SHaveDayPay)_o1_;
			if (daypay != _o_.daypay) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += daypay;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(daypay).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SHaveDayPay _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = daypay - _o_.daypay;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

