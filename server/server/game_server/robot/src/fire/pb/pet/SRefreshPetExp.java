
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshPetExp__ extends mkio.Protocol { }

/** 宠物只有当前经验改变时，服务器发送此消息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshPetExp extends __SRefreshPetExp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788438;

	public int getType() {
		return 788438;
	}

	public int petkey;
	public long curexp; // 宠物的当前经验

	public SRefreshPetExp() {
	}

	public SRefreshPetExp(int _petkey_, long _curexp_) {
		this.petkey = _petkey_;
		this.curexp = _curexp_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		if (curexp < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(curexp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		curexp = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshPetExp) {
			SRefreshPetExp _o_ = (SRefreshPetExp)_o1_;
			if (petkey != _o_.petkey) return false;
			if (curexp != _o_.curexp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += (int)curexp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(curexp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshPetExp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(curexp - _o_.curexp);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

