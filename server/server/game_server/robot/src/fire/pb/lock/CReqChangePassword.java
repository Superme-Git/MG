
package fire.pb.lock;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqChangePassword__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqChangePassword extends __CReqChangePassword__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818937;

	public int getType() {
		return 818937;
	}

	public java.lang.String oldpassword; // 旧密码
	public java.lang.String newpassword; // 新密码

	public CReqChangePassword() {
		oldpassword = "";
		newpassword = "";
	}

	public CReqChangePassword(java.lang.String _oldpassword_, java.lang.String _newpassword_) {
		this.oldpassword = _oldpassword_;
		this.newpassword = _newpassword_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(oldpassword, "UTF-16LE");
		_os_.marshal(newpassword, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		oldpassword = _os_.unmarshal_String("UTF-16LE");
		newpassword = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqChangePassword) {
			CReqChangePassword _o_ = (CReqChangePassword)_o1_;
			if (!oldpassword.equals(_o_.oldpassword)) return false;
			if (!newpassword.equals(_o_.newpassword)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += oldpassword.hashCode();
		_h_ += newpassword.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(oldpassword.length()).append(",");
		_sb_.append("T").append(newpassword.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

