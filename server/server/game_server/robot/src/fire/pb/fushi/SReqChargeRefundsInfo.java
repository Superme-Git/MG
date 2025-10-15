
package fire.pb.fushi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqChargeRefundsInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqChargeRefundsInfo extends __SReqChargeRefundsInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812484;

	public int getType() {
		return 812484;
	}

	public int result; // 0无资格 1有资格可以领取 2有资格已经领取
	public int chargevalue; // 返还额度
	public int charge; // 曾经的充值获得总符石

	public SReqChargeRefundsInfo() {
	}

	public SReqChargeRefundsInfo(int _result_, int _chargevalue_, int _charge_) {
		this.result = _result_;
		this.chargevalue = _chargevalue_;
		this.charge = _charge_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(result);
		_os_.marshal(chargevalue);
		_os_.marshal(charge);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		result = _os_.unmarshal_int();
		chargevalue = _os_.unmarshal_int();
		charge = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqChargeRefundsInfo) {
			SReqChargeRefundsInfo _o_ = (SReqChargeRefundsInfo)_o1_;
			if (result != _o_.result) return false;
			if (chargevalue != _o_.chargevalue) return false;
			if (charge != _o_.charge) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += result;
		_h_ += chargevalue;
		_h_ += charge;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(result).append(",");
		_sb_.append(chargevalue).append(",");
		_sb_.append(charge).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SReqChargeRefundsInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = result - _o_.result;
		if (0 != _c_) return _c_;
		_c_ = chargevalue - _o_.chargevalue;
		if (0 != _c_) return _c_;
		_c_ = charge - _o_.charge;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

