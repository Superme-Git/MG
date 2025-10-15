
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqRoleInfo extends __CReqRoleInfo__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid >0){
			new PReqRoleInfoProc(roleid, reqkey).submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786508;

	public int getType() {
		return 786508;
	}

	public int reqkey; // 请求类型 1表示请求人物信息界面;2 表示战斗结束

	public CReqRoleInfo() {
	}

	public CReqRoleInfo(int _reqkey_) {
		this.reqkey = _reqkey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(reqkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		reqkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqRoleInfo) {
			CReqRoleInfo _o_ = (CReqRoleInfo)_o1_;
			if (reqkey != _o_.reqkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += reqkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(reqkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqRoleInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = reqkey - _o_.reqkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

