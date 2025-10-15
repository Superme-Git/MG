package fire.pb;

import fire.pb.tel.utils.GoodsSafeLocksUtils;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CResetPassword__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CResetPassword extends __CResetPassword__ {

	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0) {
			return;
		}
		GoodsSafeLocksUtils.reSetPassword(roleid, initpd, newtpd, repeatpd);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786566;

	public int getType() {
		return 786566;
	}

	public java.lang.String initpd; // 初始密码
	public java.lang.String newtpd; // 新密码
	public java.lang.String repeatpd; // 重复密码

	public CResetPassword() {
		initpd = "";
		newtpd = "";
		repeatpd = "";
	}

	public CResetPassword(java.lang.String _initpd_, java.lang.String _newtpd_, java.lang.String _repeatpd_) {
		this.initpd = _initpd_;
		this.newtpd = _newtpd_;
		this.repeatpd = _repeatpd_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(initpd, "UTF-16LE");
		_os_.marshal(newtpd, "UTF-16LE");
		_os_.marshal(repeatpd, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		initpd = _os_.unmarshal_String("UTF-16LE");
		newtpd = _os_.unmarshal_String("UTF-16LE");
		repeatpd = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CResetPassword) {
			CResetPassword _o_ = (CResetPassword)_o1_;
			if (!initpd.equals(_o_.initpd)) return false;
			if (!newtpd.equals(_o_.newtpd)) return false;
			if (!repeatpd.equals(_o_.repeatpd)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += initpd.hashCode();
		_h_ += newtpd.hashCode();
		_h_ += repeatpd.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(initpd.length()).append(",");
		_sb_.append("T").append(newtpd.length()).append(",");
		_sb_.append("T").append(repeatpd.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}
