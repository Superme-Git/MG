
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetPetInfo__ extends mkio.Protocol { }

/** 宠物信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetPetInfo extends __SGetPetInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788526;

	public int getType() {
		return 788526;
	}

	public fire.pb.Pet petinfo; // 宠物信息

	public SGetPetInfo() {
		petinfo = new fire.pb.Pet();
	}

	public SGetPetInfo(fire.pb.Pet _petinfo_) {
		this.petinfo = _petinfo_;
	}

	public final boolean _validator_() {
		if (!petinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petinfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petinfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetPetInfo) {
			SGetPetInfo _o_ = (SGetPetInfo)_o1_;
			if (!petinfo.equals(_o_.petinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

