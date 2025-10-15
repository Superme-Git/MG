
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SItemSign__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SItemSign extends __SItemSign__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787486;

	public int getType() {
		return 787486;
	}

	public int keyinpack;
	public int sign;
	public int packid;

	public SItemSign() {
	}

	public SItemSign(int _keyinpack_, int _sign_, int _packid_) {
		this.keyinpack = _keyinpack_;
		this.sign = _sign_;
		this.packid = _packid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(keyinpack);
		_os_.marshal(sign);
		_os_.marshal(packid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		keyinpack = _os_.unmarshal_int();
		sign = _os_.unmarshal_int();
		packid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SItemSign) {
			SItemSign _o_ = (SItemSign)_o1_;
			if (keyinpack != _o_.keyinpack) return false;
			if (sign != _o_.sign) return false;
			if (packid != _o_.packid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += keyinpack;
		_h_ += sign;
		_h_ += packid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(keyinpack).append(",");
		_sb_.append(sign).append(",");
		_sb_.append(packid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SItemSign _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = sign - _o_.sign;
		if (0 != _c_) return _c_;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

