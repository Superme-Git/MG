
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSOGetTicketRep__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSOGetTicketRep extends __SSOGetTicketRep__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8017;

	public int getType() {
		return 8017;
	}

	public int retcode;
	public gnet.SSOUser user;
	public com.locojoy.base.Octets ticket;
	public com.locojoy.base.Octets local_context;
	public com.locojoy.base.Octets reserved;

	public SSOGetTicketRep() {
		user = new gnet.SSOUser();
		ticket = new com.locojoy.base.Octets();
		local_context = new com.locojoy.base.Octets();
		reserved = new com.locojoy.base.Octets();
	}

	public SSOGetTicketRep(int _retcode_, gnet.SSOUser _user_, com.locojoy.base.Octets _ticket_, com.locojoy.base.Octets _local_context_, com.locojoy.base.Octets _reserved_) {
		this.retcode = _retcode_;
		this.user = _user_;
		this.ticket = _ticket_;
		this.local_context = _local_context_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		if (!user._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(retcode);
		_os_.marshal(user);
		_os_.marshal(ticket);
		_os_.marshal(local_context);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		retcode = _os_.unmarshal_int();
		user.unmarshal(_os_);
		ticket = _os_.unmarshal_Octets();
		local_context = _os_.unmarshal_Octets();
		reserved = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSOGetTicketRep) {
			SSOGetTicketRep _o_ = (SSOGetTicketRep)_o1_;
			if (retcode != _o_.retcode) return false;
			if (!user.equals(_o_.user)) return false;
			if (!ticket.equals(_o_.ticket)) return false;
			if (!local_context.equals(_o_.local_context)) return false;
			if (!reserved.equals(_o_.reserved)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += retcode;
		_h_ += user.hashCode();
		_h_ += ticket.hashCode();
		_h_ += local_context.hashCode();
		_h_ += reserved.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(retcode).append(",");
		_sb_.append(user).append(",");
		_sb_.append("B").append(ticket.size()).append(",");
		_sb_.append("B").append(local_context.size()).append(",");
		_sb_.append("B").append(reserved.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

