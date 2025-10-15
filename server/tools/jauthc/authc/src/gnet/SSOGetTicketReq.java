
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSOGetTicketReq__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSOGetTicketReq extends __SSOGetTicketReq__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8016;

	public int getType() {
		return 8016;
	}

	public gnet.SSOUser user;
	public int loginip;
	public int toaid;
	public int tozoneid;
	public com.locojoy.base.Octets info;
	public com.locojoy.base.Octets local_context;
	public com.locojoy.base.Octets server_context;
	public com.locojoy.base.Octets reserved;

	public SSOGetTicketReq() {
		user = new gnet.SSOUser();
		info = new com.locojoy.base.Octets();
		local_context = new com.locojoy.base.Octets();
		server_context = new com.locojoy.base.Octets();
		reserved = new com.locojoy.base.Octets();
	}

	public SSOGetTicketReq(gnet.SSOUser _user_, int _loginip_, int _toaid_, int _tozoneid_, com.locojoy.base.Octets _info_, com.locojoy.base.Octets _local_context_, com.locojoy.base.Octets _server_context_, com.locojoy.base.Octets _reserved_) {
		this.user = _user_;
		this.loginip = _loginip_;
		this.toaid = _toaid_;
		this.tozoneid = _tozoneid_;
		this.info = _info_;
		this.local_context = _local_context_;
		this.server_context = _server_context_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		if (!user._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(user);
		_os_.marshal(loginip);
		_os_.marshal(toaid);
		_os_.marshal(tozoneid);
		_os_.marshal(info);
		_os_.marshal(local_context);
		_os_.marshal(server_context);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		user.unmarshal(_os_);
		loginip = _os_.unmarshal_int();
		toaid = _os_.unmarshal_int();
		tozoneid = _os_.unmarshal_int();
		info = _os_.unmarshal_Octets();
		local_context = _os_.unmarshal_Octets();
		server_context = _os_.unmarshal_Octets();
		reserved = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSOGetTicketReq) {
			SSOGetTicketReq _o_ = (SSOGetTicketReq)_o1_;
			if (!user.equals(_o_.user)) return false;
			if (loginip != _o_.loginip) return false;
			if (toaid != _o_.toaid) return false;
			if (tozoneid != _o_.tozoneid) return false;
			if (!info.equals(_o_.info)) return false;
			if (!local_context.equals(_o_.local_context)) return false;
			if (!server_context.equals(_o_.server_context)) return false;
			if (!reserved.equals(_o_.reserved)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += user.hashCode();
		_h_ += loginip;
		_h_ += toaid;
		_h_ += tozoneid;
		_h_ += info.hashCode();
		_h_ += local_context.hashCode();
		_h_ += server_context.hashCode();
		_h_ += reserved.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(user).append(",");
		_sb_.append(loginip).append(",");
		_sb_.append(toaid).append(",");
		_sb_.append(tozoneid).append(",");
		_sb_.append("B").append(info.size()).append(",");
		_sb_.append("B").append(local_context.size()).append(",");
		_sb_.append("B").append(server_context.size()).append(",");
		_sb_.append("B").append(reserved.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

