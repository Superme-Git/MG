
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __AuGameKeepAlive__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class AuGameKeepAlive extends __AuGameKeepAlive__ {
	@Override
	protected void process() {
		send(getConnection());
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8058;

	public int getType() {
		return 8058;
	}

	public long code;

	public AuGameKeepAlive() {
	}

	public AuGameKeepAlive(long _code_) {
		this.code = _code_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(code);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		code = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AuGameKeepAlive) {
			AuGameKeepAlive _o_ = (AuGameKeepAlive)_o1_;
			if (code != _o_.code) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)code;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(code).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(AuGameKeepAlive _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(code - _o_.code);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

