
package openau;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __ServerIDResponse__ extends mkio.Protocol { }

/** gs连上deliver上，发给gs；用户登录完成后，发给客户端
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class ServerIDResponse extends __ServerIDResponse__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8902;

	public int getType() {
		return 8902;
	}

	public int plattype; // 当前服务器组，登录的是那个平台
	public com.locojoy.base.Octets serverid; // 当前服务器组的标识

	public ServerIDResponse() {
		serverid = new com.locojoy.base.Octets();
	}

	public ServerIDResponse(int _plattype_, com.locojoy.base.Octets _serverid_) {
		this.plattype = _plattype_;
		this.serverid = _serverid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(plattype);
		_os_.marshal(serverid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		plattype = _os_.unmarshal_int();
		serverid = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ServerIDResponse) {
			ServerIDResponse _o_ = (ServerIDResponse)_o1_;
			if (plattype != _o_.plattype) return false;
			if (!serverid.equals(_o_.serverid)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += plattype;
		_h_ += serverid.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(plattype).append(",");
		_sb_.append("B").append(serverid.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

