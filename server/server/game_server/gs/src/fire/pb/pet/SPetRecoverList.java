
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPetRecoverList__ extends mkio.Protocol { }

/** 服务器返回宠物找回列表
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPetRecoverList extends __SPetRecoverList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788584;

	public int getType() {
		return 788584;
	}

	public java.util.LinkedList<fire.pb.pet.PetRecoverInfoBean> pets; // 宠物列表

	public SPetRecoverList() {
		pets = new java.util.LinkedList<fire.pb.pet.PetRecoverInfoBean>();
	}

	public SPetRecoverList(java.util.LinkedList<fire.pb.pet.PetRecoverInfoBean> _pets_) {
		this.pets = _pets_;
	}

	public final boolean _validator_() {
		for (fire.pb.pet.PetRecoverInfoBean _v_ : pets)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(pets.size());
		for (fire.pb.pet.PetRecoverInfoBean _v_ : pets) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.pet.PetRecoverInfoBean _v_ = new fire.pb.pet.PetRecoverInfoBean();
			_v_.unmarshal(_os_);
			pets.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPetRecoverList) {
			SPetRecoverList _o_ = (SPetRecoverList)_o1_;
			if (!pets.equals(_o_.pets)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pets.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pets).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

