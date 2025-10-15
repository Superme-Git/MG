
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GetAddCashSNReq__ extends mkio.Protocol { }

/** 充值相关
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GetAddCashSNReq extends __GetAddCashSNReq__ {
	@Override
	protected void process() {
		// protocol handle
		int sn = 0;

		GetAddCashSNRep getAddCashSNRep = new GetAddCashSNRep();
		getAddCashSNRep.sn = sn+1;
		getAddCashSNRep.userid = userid;
		getAddCashSNRep.xid = xid;
		getAddCashSNRep.zoneid = zoneid;
		getAddCashSNRep.retcode = 0;
		DeliveryManager.getInstance().send(getAddCashSNRep);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 200;

	public int getType() {
		return 200;
	}

	public int xid;
	public int userid;
	public int zoneid;

	public GetAddCashSNReq() {
		xid = -1;
		userid = -1;
		zoneid = -1;
	}

	public GetAddCashSNReq(int _xid_, int _userid_, int _zoneid_) {
		this.xid = _xid_;
		this.userid = _userid_;
		this.zoneid = _zoneid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(xid);
		_os_.marshal(userid);
		_os_.marshal(zoneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		xid = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		zoneid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GetAddCashSNReq) {
			GetAddCashSNReq _o_ = (GetAddCashSNReq)_o1_;
			if (xid != _o_.xid) return false;
			if (userid != _o_.userid) return false;
			if (zoneid != _o_.zoneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += xid;
		_h_ += userid;
		_h_ += zoneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(xid).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(zoneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GetAddCashSNReq _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = xid - _o_.xid;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

