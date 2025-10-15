
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __InstantAddCashRep__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class InstantAddCashRep extends __InstantAddCashRep__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 209;

	public int getType() {
		return 209;
	}

	public int retcode;
	public int userid;
	public int localsid;
	public int reserved;

	public InstantAddCashRep() {
	}

	public InstantAddCashRep(int _retcode_, int _userid_, int _localsid_, int _reserved_) {
		this.retcode = _retcode_;
		this.userid = _userid_;
		this.localsid = _localsid_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(retcode);
		_os_.marshal(userid);
		_os_.marshal(localsid);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		retcode = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		localsid = _os_.unmarshal_int();
		reserved = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof InstantAddCashRep) {
			InstantAddCashRep _o_ = (InstantAddCashRep)_o1_;
			if (retcode != _o_.retcode) return false;
			if (userid != _o_.userid) return false;
			if (localsid != _o_.localsid) return false;
			if (reserved != _o_.reserved) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += retcode;
		_h_ += userid;
		_h_ += localsid;
		_h_ += reserved;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(retcode).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(localsid).append(",");
		_sb_.append(reserved).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(InstantAddCashRep _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

