
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshCurrency__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshCurrency extends __SRefreshCurrency__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787450;

	public int getType() {
		return 787450;
	}

	public int packid;
	public java.util.HashMap<Byte,Long> currency;

	public SRefreshCurrency() {
		currency = new java.util.HashMap<Byte,Long>();
	}

	public SRefreshCurrency(int _packid_, java.util.HashMap<Byte,Long> _currency_) {
		this.packid = _packid_;
		this.currency = _currency_;
	}

	public final boolean _validator_() {
		if (packid < 1 || packid > 10) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packid);
		_os_.compact_uint32(currency.size());
		for (java.util.Map.Entry<Byte, Long> _e_ : currency.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			long _v_;
			_v_ = _os_.unmarshal_long();
			currency.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshCurrency) {
			SRefreshCurrency _o_ = (SRefreshCurrency)_o1_;
			if (packid != _o_.packid) return false;
			if (!currency.equals(_o_.currency)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		_h_ += currency.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(currency).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

