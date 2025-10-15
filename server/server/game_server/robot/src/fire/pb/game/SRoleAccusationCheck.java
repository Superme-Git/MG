
package fire.pb.game;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRoleAccusationCheck__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRoleAccusationCheck extends __SRoleAccusationCheck__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810373;

	public int getType() {
		return 810373;
	}

	public int errorcode; // 错误码 0:成功

	public SRoleAccusationCheck() {
	}

	public SRoleAccusationCheck(int _errorcode_) {
		this.errorcode = _errorcode_;
	}

	public final boolean _validator_() {
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
		if (_o1_ instanceof SRoleAccusationCheck) {
			SRoleAccusationCheck _o_ = (SRoleAccusationCheck)_o1_;
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

	public int compareTo(SRoleAccusationCheck _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = errorcode - _o_.errorcode;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

