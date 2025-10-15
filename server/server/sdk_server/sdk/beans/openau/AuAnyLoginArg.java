
package openau;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 登录
*/
public class AuAnyLoginArg implements Marshal {
	public com.locojoy.base.Octets account;
	public com.locojoy.base.Octets response;
	public com.locojoy.base.Octets challenge;
	public com.locojoy.base.Octets loginipoctets;
	public int loginip;
	public int plattype;
	public int gameid;
	public com.locojoy.base.Octets gamekey;
	public com.locojoy.base.Octets serverid;
	public com.locojoy.base.Octets selfchannel; // 渠道号
	public com.locojoy.base.Octets childchannel; // 子渠道号
	public com.locojoy.base.Octets deviceid; // 设备唯一标识

	public AuAnyLoginArg() {
		account = new com.locojoy.base.Octets();
		response = new com.locojoy.base.Octets();
		challenge = new com.locojoy.base.Octets();
		loginipoctets = new com.locojoy.base.Octets();
		loginip = 0;
		gamekey = new com.locojoy.base.Octets();
		serverid = new com.locojoy.base.Octets();
		selfchannel = new com.locojoy.base.Octets();
		childchannel = new com.locojoy.base.Octets();
		deviceid = new com.locojoy.base.Octets();
	}

	public AuAnyLoginArg(com.locojoy.base.Octets _account_, com.locojoy.base.Octets _response_, com.locojoy.base.Octets _challenge_, com.locojoy.base.Octets _loginipoctets_, int _loginip_, int _plattype_, int _gameid_, com.locojoy.base.Octets _gamekey_, com.locojoy.base.Octets _serverid_, com.locojoy.base.Octets _selfchannel_, com.locojoy.base.Octets _childchannel_, com.locojoy.base.Octets _deviceid_) {
		this.account = _account_;
		this.response = _response_;
		this.challenge = _challenge_;
		this.loginipoctets = _loginipoctets_;
		this.loginip = _loginip_;
		this.plattype = _plattype_;
		this.gameid = _gameid_;
		this.gamekey = _gamekey_;
		this.serverid = _serverid_;
		this.selfchannel = _selfchannel_;
		this.childchannel = _childchannel_;
		this.deviceid = _deviceid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(account);
		_os_.marshal(response);
		_os_.marshal(challenge);
		_os_.marshal(loginipoctets);
		_os_.marshal(loginip);
		_os_.marshal(plattype);
		_os_.marshal(gameid);
		_os_.marshal(gamekey);
		_os_.marshal(serverid);
		_os_.marshal(selfchannel);
		_os_.marshal(childchannel);
		_os_.marshal(deviceid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		account = _os_.unmarshal_Octets();
		response = _os_.unmarshal_Octets();
		challenge = _os_.unmarshal_Octets();
		loginipoctets = _os_.unmarshal_Octets();
		loginip = _os_.unmarshal_int();
		plattype = _os_.unmarshal_int();
		gameid = _os_.unmarshal_int();
		gamekey = _os_.unmarshal_Octets();
		serverid = _os_.unmarshal_Octets();
		selfchannel = _os_.unmarshal_Octets();
		childchannel = _os_.unmarshal_Octets();
		deviceid = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AuAnyLoginArg) {
			AuAnyLoginArg _o_ = (AuAnyLoginArg)_o1_;
			if (!account.equals(_o_.account)) return false;
			if (!response.equals(_o_.response)) return false;
			if (!challenge.equals(_o_.challenge)) return false;
			if (!loginipoctets.equals(_o_.loginipoctets)) return false;
			if (loginip != _o_.loginip) return false;
			if (plattype != _o_.plattype) return false;
			if (gameid != _o_.gameid) return false;
			if (!gamekey.equals(_o_.gamekey)) return false;
			if (!serverid.equals(_o_.serverid)) return false;
			if (!selfchannel.equals(_o_.selfchannel)) return false;
			if (!childchannel.equals(_o_.childchannel)) return false;
			if (!deviceid.equals(_o_.deviceid)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += account.hashCode();
		_h_ += response.hashCode();
		_h_ += challenge.hashCode();
		_h_ += loginipoctets.hashCode();
		_h_ += loginip;
		_h_ += plattype;
		_h_ += gameid;
		_h_ += gamekey.hashCode();
		_h_ += serverid.hashCode();
		_h_ += selfchannel.hashCode();
		_h_ += childchannel.hashCode();
		_h_ += deviceid.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("B").append(account.size()).append(",");
		_sb_.append("B").append(response.size()).append(",");
		_sb_.append("B").append(challenge.size()).append(",");
		_sb_.append("B").append(loginipoctets.size()).append(",");
		_sb_.append(loginip).append(",");
		_sb_.append(plattype).append(",");
		_sb_.append(gameid).append(",");
		_sb_.append("B").append(gamekey.size()).append(",");
		_sb_.append("B").append(serverid.size()).append(",");
		_sb_.append("B").append(selfchannel.size()).append(",");
		_sb_.append("B").append(childchannel.size()).append(",");
		_sb_.append("B").append(deviceid.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

