
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __PassportGetRoleListReq__ extends mkio.Protocol { }

/** 获取某账号的角色列表,主要是网站通过AU来游戏取角色列表展示
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class PassportGetRoleListReq extends __PassportGetRoleListReq__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 214;

	public int getType() {
		return 214;
	}

	public int xid;
	public int userid;

	public PassportGetRoleListReq() {
	}

	public PassportGetRoleListReq(int _xid_, int _userid_) {
		this.xid = _xid_;
		this.userid = _userid_;
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
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		xid = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PassportGetRoleListReq) {
			PassportGetRoleListReq _o_ = (PassportGetRoleListReq)_o1_;
			if (xid != _o_.xid) return false;
			if (userid != _o_.userid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += xid;
		_h_ += userid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(xid).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(PassportGetRoleListReq _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = xid - _o_.xid;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

