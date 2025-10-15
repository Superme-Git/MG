
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __AddCash__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class AddCash extends __AddCash__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 515;

	public int getType() {
		return 515;
	}

	public int userid;
	public int zoneid;
	public int sn;
	public int cash;

	public AddCash() {
		zoneid = -1;
		sn = 0;
		cash = 0;
	}

	public AddCash(int _userid_, int _zoneid_, int _sn_, int _cash_) {
		this.userid = _userid_;
		this.zoneid = _zoneid_;
		this.sn = _sn_;
		this.cash = _cash_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(userid);
		_os_.marshal(zoneid);
		_os_.marshal(sn);
		_os_.marshal(cash);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		zoneid = _os_.unmarshal_int();
		sn = _os_.unmarshal_int();
		cash = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AddCash) {
			AddCash _o_ = (AddCash)_o1_;
			if (userid != _o_.userid) return false;
			if (zoneid != _o_.zoneid) return false;
			if (sn != _o_.sn) return false;
			if (cash != _o_.cash) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += zoneid;
		_h_ += sn;
		_h_ += cash;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(zoneid).append(",");
		_sb_.append(sn).append(",");
		_sb_.append(cash).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(AddCash _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = sn - _o_.sn;
		if (0 != _c_) return _c_;
		_c_ = cash - _o_.cash;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

