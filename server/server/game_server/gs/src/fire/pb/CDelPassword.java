package fire.pb;

import fire.pb.tel.utils.GoodsSafeLocksUtils;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CDelPassword__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CDelPassword extends __CDelPassword__ {

	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0) {
			return;
		}
		GoodsSafeLocksUtils.unSafelock(roleid, initpd, repeatpd);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786568;

	public int getType() {
		return 786568;
	}

	public java.lang.String initpd; // 初始密码
	public java.lang.String repeatpd; // 重复密码

	public CDelPassword() {
		initpd = "";
		repeatpd = "";
	}

	public CDelPassword(java.lang.String _initpd_, java.lang.String _repeatpd_) {
		this.initpd = _initpd_;
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
		_os_.marshal(repeatpd, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		initpd = _os_.unmarshal_String("UTF-16LE");
		repeatpd = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CDelPassword) {
			CDelPassword _o_ = (CDelPassword)_o1_;
			if (!initpd.equals(_o_.initpd)) return false;
			if (!repeatpd.equals(_o_.repeatpd)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += initpd.hashCode();
		_h_ += repeatpd.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(initpd.length()).append(",");
		_sb_.append("T").append(repeatpd.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}
