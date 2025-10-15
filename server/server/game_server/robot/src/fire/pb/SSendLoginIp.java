
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendLoginIp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendLoginIp extends __SSendLoginIp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786504;

	public int getType() {
		return 786504;
	}

	public int loginip;
	public int lastip;
	public long lasttime;

	public SSendLoginIp() {
	}

	public SSendLoginIp(int _loginip_, int _lastip_, long _lasttime_) {
		this.loginip = _loginip_;
		this.lastip = _lastip_;
		this.lasttime = _lasttime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(loginip);
		_os_.marshal(lastip);
		_os_.marshal(lasttime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		loginip = _os_.unmarshal_int();
		lastip = _os_.unmarshal_int();
		lasttime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendLoginIp) {
			SSendLoginIp _o_ = (SSendLoginIp)_o1_;
			if (loginip != _o_.loginip) return false;
			if (lastip != _o_.lastip) return false;
			if (lasttime != _o_.lasttime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += loginip;
		_h_ += lastip;
		_h_ += (int)lasttime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(loginip).append(",");
		_sb_.append(lastip).append(",");
		_sb_.append(lasttime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendLoginIp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = loginip - _o_.loginip;
		if (0 != _c_) return _c_;
		_c_ = lastip - _o_.lastip;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(lasttime - _o_.lasttime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

