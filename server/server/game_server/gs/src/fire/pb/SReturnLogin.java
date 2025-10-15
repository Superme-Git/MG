
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReturnLogin__ extends mkio.Protocol { }

/** 服务器：通知客户端返回登录界面
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReturnLogin extends __SReturnLogin__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786483;

	public int getType() {
		return 786483;
	}

	public int reason;
	public long ext; // 扩展 by changhao

	public SReturnLogin() {
	}

	public SReturnLogin(int _reason_, long _ext_) {
		this.reason = _reason_;
		this.ext = _ext_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(reason);
		_os_.marshal(ext);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		reason = _os_.unmarshal_int();
		ext = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReturnLogin) {
			SReturnLogin _o_ = (SReturnLogin)_o1_;
			if (reason != _o_.reason) return false;
			if (ext != _o_.ext) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += reason;
		_h_ += (int)ext;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(reason).append(",");
		_sb_.append(ext).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SReturnLogin _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = reason - _o_.reason;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(ext - _o_.ext);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

