package fire.pb;

import fire.pb.tel.utils.GoodsSafeLocksUtils;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CCloseGoodLocks__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CCloseGoodLocks extends __CCloseGoodLocks__ {

	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0) {
			return;
		}
		GoodsSafeLocksUtils.closeGoodLocks(roleid, password,closetype);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786578;

	public int getType() {
		return 786578;
	}

	public java.lang.String password; // 密码
	public byte closetype; // 关闭类型

	public CCloseGoodLocks() {
		password = "";
	}

	public CCloseGoodLocks(java.lang.String _password_, byte _closetype_) {
		this.password = _password_;
		this.closetype = _closetype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(password, "UTF-16LE");
		_os_.marshal(closetype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		password = _os_.unmarshal_String("UTF-16LE");
		closetype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CCloseGoodLocks) {
			CCloseGoodLocks _o_ = (CCloseGoodLocks)_o1_;
			if (!password.equals(_o_.password)) return false;
			if (closetype != _o_.closetype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += password.hashCode();
		_h_ += (int)closetype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(password.length()).append(",");
		_sb_.append(closetype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}
