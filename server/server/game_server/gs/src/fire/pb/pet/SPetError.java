
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPetError__ extends mkio.Protocol { }

/** 宠物相关操作出错
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPetError extends __SPetError__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788449;

	public int getType() {
		return 788449;
	}

	public int peterror; // 具体参见 PetErr的定义

	public SPetError() {
	}

	public SPetError(int _peterror_) {
		this.peterror = _peterror_;
	}

	public final boolean _validator_() {
		if (peterror > -1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(peterror);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		peterror = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPetError) {
			SPetError _o_ = (SPetError)_o1_;
			if (peterror != _o_.peterror) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += peterror;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(peterror).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPetError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = peterror - _o_.peterror;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

