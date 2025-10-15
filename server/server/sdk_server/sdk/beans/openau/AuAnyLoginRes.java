
package openau;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class AuAnyLoginRes implements Marshal {
	public int retcode;
	public int userid;
	public com.locojoy.base.Octets platinfo;
	public com.locojoy.base.Octets realaccount;
	public int reserved1;
	public com.locojoy.base.Octets reserved2;

	public AuAnyLoginRes() {
		retcode = ErrorCodes.error_succeed;
		userid = -1;
		platinfo = new com.locojoy.base.Octets();
		realaccount = new com.locojoy.base.Octets();
		reserved2 = new com.locojoy.base.Octets();
	}

	public AuAnyLoginRes(int _retcode_, int _userid_, com.locojoy.base.Octets _platinfo_, com.locojoy.base.Octets _realaccount_, int _reserved1_, com.locojoy.base.Octets _reserved2_) {
		this.retcode = _retcode_;
		this.userid = _userid_;
		this.platinfo = _platinfo_;
		this.realaccount = _realaccount_;
		this.reserved1 = _reserved1_;
		this.reserved2 = _reserved2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(retcode);
		_os_.marshal(userid);
		_os_.marshal(platinfo);
		_os_.marshal(realaccount);
		_os_.marshal(reserved1);
		_os_.marshal(reserved2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		retcode = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		platinfo = _os_.unmarshal_Octets();
		realaccount = _os_.unmarshal_Octets();
		reserved1 = _os_.unmarshal_int();
		reserved2 = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AuAnyLoginRes) {
			AuAnyLoginRes _o_ = (AuAnyLoginRes)_o1_;
			if (retcode != _o_.retcode) return false;
			if (userid != _o_.userid) return false;
			if (!platinfo.equals(_o_.platinfo)) return false;
			if (!realaccount.equals(_o_.realaccount)) return false;
			if (reserved1 != _o_.reserved1) return false;
			if (!reserved2.equals(_o_.reserved2)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += retcode;
		_h_ += userid;
		_h_ += platinfo.hashCode();
		_h_ += realaccount.hashCode();
		_h_ += reserved1;
		_h_ += reserved2.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(retcode).append(",");
		_sb_.append(userid).append(",");
		_sb_.append("B").append(platinfo.size()).append(",");
		_sb_.append("B").append(realaccount.size()).append(",");
		_sb_.append(reserved1).append(",");
		_sb_.append("B").append(reserved2.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

