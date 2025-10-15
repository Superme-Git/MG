
package openau;
import org.apache.log4j.Logger;
import com.locojoy.sdk.Config;
import com.locojoy.sdk.PlatManager;
import com.locojoy.sdk.XioManager;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __RegistGameServerRequest__ extends mkio.Protocol { }

/** deliver 向 auany 注册
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class RegistGameServerRequest extends __RegistGameServerRequest__ {
	private static Logger logger = Logger.getLogger(XioManager.class);
	@Override
	protected void process() {
		final mkio.Mkio xio = getConnection();
		final String servername = serverid.getString(Config.OCTETS_CHARSET_ANSI);
		final int ptxio = XioManager.getInstance().updateGameServerIDAndPlatType(xio, servername, plattype);
		final int ptprocess = PlatManager.checkPlatTypeExisting(ptxio);
		logger.info("RegistGameServerRequest.process serverid=" + servername + ",plattype=" + plattype + ",ptprocess=" + ptprocess);
		new RegistGameServerResponse(ptprocess).send(xio);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8900;

	public int getType() {
		return 8900;
	}

	public int plattype; // 如果是 unknow 就由 aunay 决定登录哪个平台
	public com.locojoy.base.Octets serverid; // 当前服务器组的标识 delivery 的配置中指定
	public int ip1;
	public int ip2;
	public int ip3;

	public RegistGameServerRequest() {
		plattype = AuAnyPlatTypes.unknow;
		serverid = new com.locojoy.base.Octets();
	}

	public RegistGameServerRequest(int _plattype_, com.locojoy.base.Octets _serverid_, int _ip1_, int _ip2_, int _ip3_) {
		this.plattype = _plattype_;
		this.serverid = _serverid_;
		this.ip1 = _ip1_;
		this.ip2 = _ip2_;
		this.ip3 = _ip3_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(plattype);
		_os_.marshal(serverid);
		_os_.marshal(ip1);
		_os_.marshal(ip2);
		_os_.marshal(ip3);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		plattype = _os_.unmarshal_int();
		serverid = _os_.unmarshal_Octets();
		ip1 = _os_.unmarshal_int();
		ip2 = _os_.unmarshal_int();
		ip3 = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RegistGameServerRequest) {
			RegistGameServerRequest _o_ = (RegistGameServerRequest)_o1_;
			if (plattype != _o_.plattype) return false;
			if (!serverid.equals(_o_.serverid)) return false;
			if (ip1 != _o_.ip1) return false;
			if (ip2 != _o_.ip2) return false;
			if (ip3 != _o_.ip3) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += plattype;
		_h_ += serverid.hashCode();
		_h_ += ip1;
		_h_ += ip2;
		_h_ += ip3;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(plattype).append(",");
		_sb_.append("B").append(serverid.size()).append(",");
		_sb_.append(ip1).append(",");
		_sb_.append(ip2).append(",");
		_sb_.append(ip3).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

