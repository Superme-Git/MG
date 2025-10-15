
package fire.pb.master;

import fire.pb.master.proc.PAcceptPrenticeProc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAppMaster__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAppMaster extends __CAppMaster__ {
	@Override
	protected void process() {
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid > 0){
			new PAcceptPrenticeProc(roleid, flag).submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816485;

	public int getType() {
		return 816485;
	}

	public int flag; // 1=同意 0=拒绝

	public CAppMaster() {
	}

	public CAppMaster(int _flag_) {
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		flag = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAppMaster) {
			CAppMaster _o_ = (CAppMaster)_o1_;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAppMaster _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

