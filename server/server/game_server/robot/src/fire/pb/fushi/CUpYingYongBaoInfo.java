
package fire.pb.fushi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CUpYingYongBaoInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CUpYingYongBaoInfo extends __CUpYingYongBaoInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812492;

	public int getType() {
		return 812492;
	}

	public java.lang.String openid;
	public java.lang.String openkey;
	public java.lang.String paytoken;
	public java.lang.String pf;
	public java.lang.String pfkey;
	public java.lang.String zoneid;
	public java.lang.String platformname;

	public CUpYingYongBaoInfo() {
		openid = "";
		openkey = "";
		paytoken = "";
		pf = "";
		pfkey = "";
		zoneid = "";
		platformname = "";
	}

	public CUpYingYongBaoInfo(java.lang.String _openid_, java.lang.String _openkey_, java.lang.String _paytoken_, java.lang.String _pf_, java.lang.String _pfkey_, java.lang.String _zoneid_, java.lang.String _platformname_) {
		this.openid = _openid_;
		this.openkey = _openkey_;
		this.paytoken = _paytoken_;
		this.pf = _pf_;
		this.pfkey = _pfkey_;
		this.zoneid = _zoneid_;
		this.platformname = _platformname_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(openid, "UTF-16LE");
		_os_.marshal(openkey, "UTF-16LE");
		_os_.marshal(paytoken, "UTF-16LE");
		_os_.marshal(pf, "UTF-16LE");
		_os_.marshal(pfkey, "UTF-16LE");
		_os_.marshal(zoneid, "UTF-16LE");
		_os_.marshal(platformname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		openid = _os_.unmarshal_String("UTF-16LE");
		openkey = _os_.unmarshal_String("UTF-16LE");
		paytoken = _os_.unmarshal_String("UTF-16LE");
		pf = _os_.unmarshal_String("UTF-16LE");
		pfkey = _os_.unmarshal_String("UTF-16LE");
		zoneid = _os_.unmarshal_String("UTF-16LE");
		platformname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CUpYingYongBaoInfo) {
			CUpYingYongBaoInfo _o_ = (CUpYingYongBaoInfo)_o1_;
			if (!openid.equals(_o_.openid)) return false;
			if (!openkey.equals(_o_.openkey)) return false;
			if (!paytoken.equals(_o_.paytoken)) return false;
			if (!pf.equals(_o_.pf)) return false;
			if (!pfkey.equals(_o_.pfkey)) return false;
			if (!zoneid.equals(_o_.zoneid)) return false;
			if (!platformname.equals(_o_.platformname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += openid.hashCode();
		_h_ += openkey.hashCode();
		_h_ += paytoken.hashCode();
		_h_ += pf.hashCode();
		_h_ += pfkey.hashCode();
		_h_ += zoneid.hashCode();
		_h_ += platformname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(openid.length()).append(",");
		_sb_.append("T").append(openkey.length()).append(",");
		_sb_.append("T").append(paytoken.length()).append(",");
		_sb_.append("T").append(pf.length()).append(",");
		_sb_.append("T").append(pfkey.length()).append(",");
		_sb_.append("T").append(zoneid.length()).append(",");
		_sb_.append("T").append(platformname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

