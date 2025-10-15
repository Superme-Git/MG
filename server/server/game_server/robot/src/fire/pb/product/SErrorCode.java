
package fire.pb.product;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SErrorCode__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SErrorCode extends __SErrorCode__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 803441;

	public int getType() {
		return 803441;
	}

	public int errorcode;

	public SErrorCode() {
	}

	public SErrorCode(int _errorcode_) {
		this.errorcode = _errorcode_;
	}

	public final boolean _validator_() {
		if (errorcode >= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(errorcode);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		errorcode = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SErrorCode) {
			SErrorCode _o_ = (SErrorCode)_o1_;
			if (errorcode != _o_.errorcode) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += errorcode;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(errorcode).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SErrorCode _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = errorcode - _o_.errorcode;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

