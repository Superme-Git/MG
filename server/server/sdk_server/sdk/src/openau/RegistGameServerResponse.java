
package openau;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __RegistGameServerResponse__ extends mkio.Protocol { }

/** plattype返回AuAnyPlatTypes.unknow：
		1. 如果请求时,plattype为AuAnyPlatTypes.unknow，auany没有平台的实现
		2. 如果请求时,plattype不为AuAnyPlatTypes.unknow，auany上没有请求plattype对应的实现
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class RegistGameServerResponse extends __RegistGameServerResponse__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8901;

	public int getType() {
		return 8901;
	}

	public int plattype; // 当前服务器组，登录的是那个平台

	public RegistGameServerResponse() {
	}

	public RegistGameServerResponse(int _plattype_) {
		this.plattype = _plattype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(plattype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		plattype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RegistGameServerResponse) {
			RegistGameServerResponse _o_ = (RegistGameServerResponse)_o1_;
			if (plattype != _o_.plattype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += plattype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(plattype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(RegistGameServerResponse _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = plattype - _o_.plattype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

