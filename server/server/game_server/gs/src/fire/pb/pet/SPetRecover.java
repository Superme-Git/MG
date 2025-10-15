
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPetRecover__ extends mkio.Protocol { }

/** 服务器返回宠物找回结果
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPetRecover extends __SPetRecover__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788586;

	public int getType() {
		return 788586;
	}

	public int petid; // 宠物id
	public long uniqid; // 唯一id

	public SPetRecover() {
	}

	public SPetRecover(int _petid_, long _uniqid_) {
		this.petid = _petid_;
		this.uniqid = _uniqid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petid);
		_os_.marshal(uniqid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petid = _os_.unmarshal_int();
		uniqid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPetRecover) {
			SPetRecover _o_ = (SPetRecover)_o1_;
			if (petid != _o_.petid) return false;
			if (uniqid != _o_.uniqid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petid;
		_h_ += (int)uniqid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petid).append(",");
		_sb_.append(uniqid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPetRecover _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petid - _o_.petid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(uniqid - _o_.uniqid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

