
package gnet;


import mkio.HelperSecurity;
import mkio.LoginIns;
import mkio.LoginManager;
import mkio.ILoginUI.Action;
import mkio.security.HmacMd5Hash;
import mkio.security.Security;

import com.locojoy.base.Octets;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __Challenge__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class Challenge extends __Challenge__ {
	@Override
	protected void process() {
		
		// protocol handle
					
		LoginIns login = 
			LoginManager.getInstance().getLoginBySession(this.getConnection());
		
		mkdb.Trace.debug(this.getClass().getName() + ": " + login.getParam().username );
				
		if ( login.getParam().username.isEmpty() )
		{
			//用于获取服务器信息
			// 把 serverattr 保存到 login 里面。
			login.serverattr = this.serverattr;
			login.saready = true;
			login.getParam().version = this.version;
			LoginManager.getInstance().close(login);			
			login.getLoginUI().onAuthError(Action.eServerAttr, 0, "server attr");			
			return;
		}
		
		if ( !login.checkVersion( this.version ) )
		{
			LoginManager.getInstance().close(login);
			login.getLoginUI().onAuthError(Action.eVersion, this.version, "version not match");
			return;
		}
		//skip the USBKey and so on
		//begin send reponse
		if ( !this.processChallege(login) )
		{
			LoginManager.getInstance().close(login);
			login.getLoginUI().onAuthError(Action.eProtocol, this.getType(), "ProcessChallenge");
			return;
		}

	}

	protected boolean processChallege( LoginIns login )
	{
		//主要是加密password
		final boolean USE_KEY = false;
		
		Octets responce = new Octets();
		
		if( HelperSecurity.SIZE_CHALLENGE != this.nonce.size() )
		{
			return false;
		}
				
		Octets username = new Octets(login.getParam().username.getBytes());
		Octets password = new Octets(login.getParam().password.getBytes());
		
		
		
		if(login.getParam().logintype == 1) //auany login
		{
			responce.replace(password);
			
		}
		else if ( !USE_KEY )
		{
			Security hmac =	new HmacMd5Hash();
			Octets key = new Octets();
			
			HelperSecurity.makePassword(username, password, key);
					
			hmac.setParameter(key);
			hmac.doUpdate(this.nonce);
			responce = hmac.doFinal(responce);
			responce.resize(HelperSecurity.SIZE_MD5);

		}
	
		Response res = new Response();
		res.identity.replace(login.getParam().username.getBytes());	
		res.response.replace(responce);
		res.logintype = login.getParam().logintype;
		res.mid =  new Octets(login.getParam().mid.getBytes());
		res.reserved1 = login.getParam().reserved1;
		res.reserved2 = new Octets(login.getParam().reserved2.getBytes());
		
		//if( !res.send(this.getConnection()) )
		if( !login.send(res) )
		{
			mkdb.Trace.error("Send Response Protocol failed");
		}
		
		return true;				
	}
	
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 101;

	public int getType() {
		return 101;
	}

	public com.locojoy.base.Octets nonce;
	public int version;
	public gnet.ServerAttr serverattr;

	public Challenge() {
		nonce = new com.locojoy.base.Octets();
		serverattr = new gnet.ServerAttr();
	}

	public Challenge(com.locojoy.base.Octets _nonce_, int _version_, gnet.ServerAttr _serverattr_) {
		this.nonce = _nonce_;
		this.version = _version_;
		this.serverattr = _serverattr_;
	}

	public final boolean _validator_() {
		if (!serverattr._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(nonce);
		_os_.marshal(version);
		_os_.marshal(serverattr);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		nonce = _os_.unmarshal_Octets();
		version = _os_.unmarshal_int();
		serverattr.unmarshal(_os_);
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Challenge) {
			Challenge _o_ = (Challenge)_o1_;
			if (!nonce.equals(_o_.nonce)) return false;
			if (version != _o_.version) return false;
			if (!serverattr.equals(_o_.serverattr)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += nonce.hashCode();
		_h_ += version;
		_h_ += serverattr.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("B").append(nonce.size()).append(",");
		_sb_.append(version).append(",");
		_sb_.append(serverattr).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

