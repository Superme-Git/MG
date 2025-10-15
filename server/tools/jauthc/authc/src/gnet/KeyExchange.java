
package gnet;
import mkio.HelperSecurity;
import mkio.LoginIns;
import mkio.LoginManager;
//import mkio.security.ARCFourSecurity;
import mkio.security.CompressARCFourSecurity;
import mkio.security.DecompressARCFourSecurity;
import mkio.security.SecurityFilter;

import com.locojoy.base.Octets;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __KeyExchange__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class KeyExchange extends __KeyExchange__ {
	final boolean USE_KEY = false;	

	protected void process() {
		// protocol handle
		LoginIns login = 
			LoginManager.getInstance().getLoginBySession(this.getConnection());
		mkdb.Trace.debug(this.getClass().getName() + ": " + login.getParam().username );

		Octets username = new Octets(login.getParam().username.getBytes());
		Octets password = new Octets(login.getParam().password.getBytes());
		
		if(!USE_KEY) //skip usb key
		{	
			/*KeyExchange ke = new KeyExchange();
			ke.blkickuser = login.getParam().iskickuser? 1 : 0;
			ke.nonce.replace(this.nonce);
			if (!login.send(ke))
			{
				mkdb.Trace.error("Send KeyExchange Protocol failed!");
			}*/
			
			
			Octets key = new Octets();
			Octets md5pwd = new Octets();
			
			HelperSecurity.makePassword(username, password, md5pwd);
			HelperSecurity.generateKeyByPassword(username, md5pwd, this.nonce, key);			
			//set output Security
			SecurityFilter.setMkioOutputSecurity(
					this.getConnection(), new CompressARCFourSecurity(), key);
			
			Octets	random  = new Octets();
			//random update
			HelperSecurity.randomUpdate(random);
			HelperSecurity.generateKeyByPassword(username, md5pwd, random, key);
			//set input Security
			
//			SecurityFilter.setXioInputSecurity(
//					this.getConnection(), new DecompressARCFourSecurity(), key);
			//»•µÙSec.dllµƒ“¿¿µ
			SecurityFilter.setMkioInputSecurity( 
					this.getConnection(), new DecompressARCFourSecurity(), key);
			//send exchange
			KeyExchange ke = new KeyExchange();
			ke.blkickuser = login.getParam().iskickuser? 1 : 0;
			ke.nonce.replace(random.array(), 0, random.size());
			//if( !ke.send(this.getConnection()) )
			if (!login.send(ke))
			{
				
				mkdb.Trace.error("Send KeyExchange Protocol failed");
			}
			
		}
	}
	
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 106;

	public int getType() {
		return 106;
	}

	public com.locojoy.base.Octets nonce;
	public int blkickuser;

	public KeyExchange() {
		nonce = new com.locojoy.base.Octets();
		blkickuser = 0;
	}

	public KeyExchange(com.locojoy.base.Octets _nonce_, int _blkickuser_) {
		this.nonce = _nonce_;
		this.blkickuser = _blkickuser_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(nonce);
		_os_.marshal(blkickuser);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		nonce = _os_.unmarshal_Octets();
		blkickuser = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof KeyExchange) {
			KeyExchange _o_ = (KeyExchange)_o1_;
			if (!nonce.equals(_o_.nonce)) return false;
			if (blkickuser != _o_.blkickuser) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += nonce.hashCode();
		_h_ += blkickuser;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("B").append(nonce.size()).append(",");
		_sb_.append(blkickuser).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

