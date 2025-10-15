
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __Response__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class Response extends __Response__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 103;

	public int getType() {
		return 103;
	}

	public com.locojoy.base.Octets identity; // 帐号
	public com.locojoy.base.Octets response; // 密码(使用平台sdk时发session)
	public int logintype; // 登录类型(sdk类型) 1:locojoy platform sdk
	public int gameid;
	public com.locojoy.base.Octets gamekey;
	public com.locojoy.base.Octets serverid;
	public com.locojoy.base.Octets selfchannel; // 渠道号
	public com.locojoy.base.Octets childchannel; // 子渠道号
	public com.locojoy.base.Octets deviceid; // 设备唯一标识
	public com.locojoy.base.Octets mid;
	public int reserved1;
	public com.locojoy.base.Octets reserved2;

	public Response() {
		identity = new com.locojoy.base.Octets();
		response = new com.locojoy.base.Octets();
		gamekey = new com.locojoy.base.Octets();
		serverid = new com.locojoy.base.Octets();
		selfchannel = new com.locojoy.base.Octets();
		childchannel = new com.locojoy.base.Octets();
		deviceid = new com.locojoy.base.Octets();
		mid = new com.locojoy.base.Octets();
		reserved2 = new com.locojoy.base.Octets();
	}

	public Response(com.locojoy.base.Octets _identity_, com.locojoy.base.Octets _response_, int _logintype_, int _gameid_, com.locojoy.base.Octets _gamekey_, com.locojoy.base.Octets _serverid_, com.locojoy.base.Octets _selfchannel_, com.locojoy.base.Octets _childchannel_, com.locojoy.base.Octets _deviceid_, com.locojoy.base.Octets _mid_, int _reserved1_, com.locojoy.base.Octets _reserved2_) {
		this.identity = _identity_;
		this.response = _response_;
		this.logintype = _logintype_;
		this.gameid = _gameid_;
		this.gamekey = _gamekey_;
		this.serverid = _serverid_;
		this.selfchannel = _selfchannel_;
		this.childchannel = _childchannel_;
		this.deviceid = _deviceid_;
		this.mid = _mid_;
		this.reserved1 = _reserved1_;
		this.reserved2 = _reserved2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(identity);
		_os_.marshal(response);
		_os_.marshal(logintype);
		_os_.marshal(gameid);
		_os_.marshal(gamekey);
		_os_.marshal(serverid);
		_os_.marshal(selfchannel);
		_os_.marshal(childchannel);
		_os_.marshal(deviceid);
		_os_.marshal(mid);
		_os_.marshal(reserved1);
		_os_.marshal(reserved2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		identity = _os_.unmarshal_Octets();
		response = _os_.unmarshal_Octets();
		logintype = _os_.unmarshal_int();
		gameid = _os_.unmarshal_int();
		gamekey = _os_.unmarshal_Octets();
		serverid = _os_.unmarshal_Octets();
		selfchannel = _os_.unmarshal_Octets();
		childchannel = _os_.unmarshal_Octets();
		deviceid = _os_.unmarshal_Octets();
		mid = _os_.unmarshal_Octets();
		reserved1 = _os_.unmarshal_int();
		reserved2 = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Response) {
			Response _o_ = (Response)_o1_;
			if (!identity.equals(_o_.identity)) return false;
			if (!response.equals(_o_.response)) return false;
			if (logintype != _o_.logintype) return false;
			if (gameid != _o_.gameid) return false;
			if (!gamekey.equals(_o_.gamekey)) return false;
			if (!serverid.equals(_o_.serverid)) return false;
			if (!selfchannel.equals(_o_.selfchannel)) return false;
			if (!childchannel.equals(_o_.childchannel)) return false;
			if (!deviceid.equals(_o_.deviceid)) return false;
			if (!mid.equals(_o_.mid)) return false;
			if (reserved1 != _o_.reserved1) return false;
			if (!reserved2.equals(_o_.reserved2)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += identity.hashCode();
		_h_ += response.hashCode();
		_h_ += logintype;
		_h_ += gameid;
		_h_ += gamekey.hashCode();
		_h_ += serverid.hashCode();
		_h_ += selfchannel.hashCode();
		_h_ += childchannel.hashCode();
		_h_ += deviceid.hashCode();
		_h_ += mid.hashCode();
		_h_ += reserved1;
		_h_ += reserved2.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("B").append(identity.size()).append(",");
		_sb_.append("B").append(response.size()).append(",");
		_sb_.append(logintype).append(",");
		_sb_.append(gameid).append(",");
		_sb_.append("B").append(gamekey.size()).append(",");
		_sb_.append("B").append(serverid.size()).append(",");
		_sb_.append("B").append(selfchannel.size()).append(",");
		_sb_.append("B").append(childchannel.size()).append(",");
		_sb_.append("B").append(deviceid.size()).append(",");
		_sb_.append("B").append(mid.size()).append(",");
		_sb_.append(reserved1).append(",");
		_sb_.append("B").append(reserved2.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

