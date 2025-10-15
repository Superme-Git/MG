
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshUserExp__ extends mkio.Protocol { }

/** 通知客户端刷新人物经验
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshUserExp extends __SRefreshUserExp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786445;

	public int getType() {
		return 786445;
	}

	public long curexp; // 当前经验

	public SRefreshUserExp() {
	}

	public SRefreshUserExp(long _curexp_) {
		this.curexp = _curexp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(curexp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		curexp = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshUserExp) {
			SRefreshUserExp _o_ = (SRefreshUserExp)_o1_;
			if (curexp != _o_.curexp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)curexp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(curexp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshUserExp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(curexp - _o_.curexp);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

