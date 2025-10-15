
package fire.pb.title;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __COnTitle__ extends mkio.Protocol { }

/** 客户端请求佩戴称谓
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class COnTitle extends __COnTitle__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 798435;

	public int getType() {
		return 798435;
	}

	public int titleid;

	public COnTitle() {
	}

	public COnTitle(int _titleid_) {
		this.titleid = _titleid_;
	}

	public final boolean _validator_() {
		if (titleid < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(titleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		titleid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof COnTitle) {
			COnTitle _o_ = (COnTitle)_o1_;
			if (titleid != _o_.titleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += titleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(titleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(COnTitle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = titleid - _o_.titleid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

