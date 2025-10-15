
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __ForceLoginRep__ extends mkio.Protocol { }

/** client->link->deliver
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class ForceLoginRep extends __ForceLoginRep__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 105;

	public int getType() {
		return 105;
	}

	public int userid;
	public int localsid;
	public int deny_flag; // 1 ¾Ü¾ø 0 ½ÓÊÜ±»Ìß
	public int reserved;

	public ForceLoginRep() {
		deny_flag = 0;
		reserved = 0;
	}

	public ForceLoginRep(int _userid_, int _localsid_, int _deny_flag_, int _reserved_) {
		this.userid = _userid_;
		this.localsid = _localsid_;
		this.deny_flag = _deny_flag_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(userid);
		_os_.marshal(localsid);
		_os_.marshal(deny_flag);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		localsid = _os_.unmarshal_int();
		deny_flag = _os_.unmarshal_int();
		reserved = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ForceLoginRep) {
			ForceLoginRep _o_ = (ForceLoginRep)_o1_;
			if (userid != _o_.userid) return false;
			if (localsid != _o_.localsid) return false;
			if (deny_flag != _o_.deny_flag) return false;
			if (reserved != _o_.reserved) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += localsid;
		_h_ += deny_flag;
		_h_ += reserved;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(localsid).append(",");
		_sb_.append(deny_flag).append(",");
		_sb_.append(reserved).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ForceLoginRep _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = deny_flag - _o_.deny_flag;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

