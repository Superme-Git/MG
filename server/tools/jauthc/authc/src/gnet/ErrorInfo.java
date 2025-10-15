
package gnet;
import mkio.LoginIns;
import mkio.LoginManager;
import mkio.ILoginUI.Action;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __ErrorInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class ErrorInfo extends __ErrorInfo__ {
	@Override
	protected void process() {
		// protocol handle		
		LoginIns login = LoginManager.getInstance().getLoginBySession(this.getConnection());
		try {
			
			login.getLoginUI().onAuthError(Action.eServer, this.errcode, this.info.getString());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 102;

	public int getType() {
		return 102;
	}

	public int errcode;
	public com.locojoy.base.Octets info;

	public ErrorInfo() {
		info = new com.locojoy.base.Octets();
	}

	public ErrorInfo(int _errcode_, com.locojoy.base.Octets _info_) {
		this.errcode = _errcode_;
		this.info = _info_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(errcode);
		_os_.marshal(info);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		errcode = _os_.unmarshal_int();
		info = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ErrorInfo) {
			ErrorInfo _o_ = (ErrorInfo)_o1_;
			if (errcode != _o_.errcode) return false;
			if (!info.equals(_o_.info)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += errcode;
		_h_ += info.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(errcode).append(",");
		_sb_.append("B").append(info.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

