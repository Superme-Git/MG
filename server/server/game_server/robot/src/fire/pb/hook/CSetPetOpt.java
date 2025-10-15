
package fire.pb.hook;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetPetOpt__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetPetOpt extends __CSetPetOpt__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810336;

	public int getType() {
		return 810336;
	}

	public short petoptype; // 设置宠物操作类型
	public int petopid; // 设置宠物操作id

	public CSetPetOpt() {
	}

	public CSetPetOpt(short _petoptype_, int _petopid_) {
		this.petoptype = _petoptype_;
		this.petopid = _petopid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petoptype);
		_os_.marshal(petopid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petoptype = _os_.unmarshal_short();
		petopid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetPetOpt) {
			CSetPetOpt _o_ = (CSetPetOpt)_o1_;
			if (petoptype != _o_.petoptype) return false;
			if (petopid != _o_.petopid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petoptype;
		_h_ += petopid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petoptype).append(",");
		_sb_.append(petopid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetPetOpt _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petoptype - _o_.petoptype;
		if (0 != _c_) return _c_;
		_c_ = petopid - _o_.petopid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

