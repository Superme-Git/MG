
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __InstantAddCashReq__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class InstantAddCashReq extends __InstantAddCashReq__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 208;

	public int getType() {
		return 208;
	}

	public int userid;
	public int localsid;
	public com.locojoy.base.Octets cardnum; // ≥‰÷µø®ø®∫≈
	public com.locojoy.base.Octets cardpasswd; // ≥‰÷µø®√‹¬Î
	public int reserved;

	public InstantAddCashReq() {
		cardnum = new com.locojoy.base.Octets();
		cardpasswd = new com.locojoy.base.Octets();
	}

	public InstantAddCashReq(int _userid_, int _localsid_, com.locojoy.base.Octets _cardnum_, com.locojoy.base.Octets _cardpasswd_, int _reserved_) {
		this.userid = _userid_;
		this.localsid = _localsid_;
		this.cardnum = _cardnum_;
		this.cardpasswd = _cardpasswd_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(userid);
		_os_.marshal(localsid);
		_os_.marshal(cardnum);
		_os_.marshal(cardpasswd);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		localsid = _os_.unmarshal_int();
		cardnum = _os_.unmarshal_Octets();
		cardpasswd = _os_.unmarshal_Octets();
		reserved = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof InstantAddCashReq) {
			InstantAddCashReq _o_ = (InstantAddCashReq)_o1_;
			if (userid != _o_.userid) return false;
			if (localsid != _o_.localsid) return false;
			if (!cardnum.equals(_o_.cardnum)) return false;
			if (!cardpasswd.equals(_o_.cardpasswd)) return false;
			if (reserved != _o_.reserved) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += localsid;
		_h_ += cardnum.hashCode();
		_h_ += cardpasswd.hashCode();
		_h_ += reserved;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(localsid).append(",");
		_sb_.append("B").append(cardnum.size()).append(",");
		_sb_.append("B").append(cardpasswd.size()).append(",");
		_sb_.append(reserved).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

