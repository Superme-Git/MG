
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GetAddCashSN2Req__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GetAddCashSN2Req extends __GetAddCashSN2Req__ {
	@Override
	protected void process() {
		// protocol handle
		int sn = 0;
		boolean hasRole = false;
		final xbean.User u = xtable.User.select(userid);
		GetAddCashSN2Rep getAddCashSNRep = new GetAddCashSN2Rep();
		long roleid = 0;
		if (u!=null){
            for (Long rid : u.getIdlist()) {
				xbean.Properties prop = xtable.Properties.select(rid);
				if (prop!=null&&prop.getDeletetime()==0) {
					hasRole = true;
					break;
				}
			}			
            roleid = u.getPrevloginroleid();
		}
//		if (force==0&&(hasRole && forbiden())){//如果有角色,且账号处于封禁状态
//			getAddCashSNRep.sn = sn+1;
//			getAddCashSNRep.userid = userid;
//			getAddCashSNRep.xid = xid;
//			getAddCashSNRep.zoneid = zoneid;
//			getAddCashSNRep.retcode = -19;
//			DeliveryManager.getInstance().send(getAddCashSNRep);
//			return ;
//		}
		if (hasRole && roleid > 0){

			getAddCashSNRep.sn = sn+1;
			getAddCashSNRep.userid = userid;
			getAddCashSNRep.xid = xid;
			getAddCashSNRep.zoneid = zoneid;
			getAddCashSNRep.retcode = 0;
		}else  {
			getAddCashSNRep.sn = sn+1;
			getAddCashSNRep.userid = userid;
			getAddCashSNRep.xid = xid;
			getAddCashSNRep.zoneid = zoneid;
			getAddCashSNRep.retcode = -19;
		}

		DeliveryManager.getInstance().send(getAddCashSNRep);
	}



	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 202;

	public int getType() {
		return 202;
	}

	public int xid;
	public int userid;
	public int zoneid;
	public byte force;

	public GetAddCashSN2Req() {
		xid = -1;
		userid = -1;
		zoneid = -1;
		force = -1;
	}

	public GetAddCashSN2Req(int _xid_, int _userid_, int _zoneid_, byte _force_) {
		this.xid = _xid_;
		this.userid = _userid_;
		this.zoneid = _zoneid_;
		this.force = _force_;
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
		_os_.marshal(force);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		xid = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		zoneid = _os_.unmarshal_int();
		force = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GetAddCashSN2Req) {
			GetAddCashSN2Req _o_ = (GetAddCashSN2Req)_o1_;
			if (xid != _o_.xid) return false;
			if (userid != _o_.userid) return false;
			if (zoneid != _o_.zoneid) return false;
			if (force != _o_.force) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += xid;
		_h_ += userid;
		_h_ += zoneid;
		_h_ += (int)force;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(xid).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(zoneid).append(",");
		_sb_.append(force).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GetAddCashSN2Req _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = xid - _o_.xid;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = force - _o_.force;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

