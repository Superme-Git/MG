
package fire.pb.fushi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestChargeReturnProfit__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestChargeReturnProfit extends __SRequestChargeReturnProfit__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812480;

	public int getType() {
		return 812480;
	}

	public java.util.ArrayList<fire.pb.fushi.ChargeReturnProfit> listchargereturnprofit;

	public SRequestChargeReturnProfit() {
		listchargereturnprofit = new java.util.ArrayList<fire.pb.fushi.ChargeReturnProfit>();
	}

	public SRequestChargeReturnProfit(java.util.ArrayList<fire.pb.fushi.ChargeReturnProfit> _listchargereturnprofit_) {
		this.listchargereturnprofit = _listchargereturnprofit_;
	}

	public final boolean _validator_() {
		for (fire.pb.fushi.ChargeReturnProfit _v_ : listchargereturnprofit)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(listchargereturnprofit.size());
		for (fire.pb.fushi.ChargeReturnProfit _v_ : listchargereturnprofit) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.fushi.ChargeReturnProfit _v_ = new fire.pb.fushi.ChargeReturnProfit();
			_v_.unmarshal(_os_);
			listchargereturnprofit.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestChargeReturnProfit) {
			SRequestChargeReturnProfit _o_ = (SRequestChargeReturnProfit)_o1_;
			if (!listchargereturnprofit.equals(_o_.listchargereturnprofit)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += listchargereturnprofit.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(listchargereturnprofit).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

