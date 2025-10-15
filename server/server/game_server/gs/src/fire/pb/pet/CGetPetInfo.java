
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetPetInfo__ extends mkio.Protocol { }

/** 客户端请求查看某人的宠物信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetPetInfo extends __CGetPetInfo__ {
	@Override
	protected void process() {

		final long reqRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (reqRoleId < 0) 
			return;

		fire.pb.pet.PetColumn petCol = new fire.pb.pet.PetColumn(roleid, fire.pb.pet.PetColumnTypes.PET, true);
		fire.pb.pet.Pet pet = petCol.getPet(petkey);
		if(pet == null)
			return;

		fire.pb.Pet petMarshal = pet.getProtocolPet();
		fire.pb.pet.SGetPetInfo send = new fire.pb.pet.SGetPetInfo(petMarshal);
		gnet.link.Onlines.getInstance().send(reqRoleId, send);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788525;

	public int getType() {
		return 788525;
	}

	public long roleid; // 角色id
	public int petkey;

	public CGetPetInfo() {
	}

	public CGetPetInfo(long _roleid_, int _petkey_) {
		this.roleid = _roleid_;
		this.petkey = _petkey_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(petkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		petkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetPetInfo) {
			CGetPetInfo _o_ = (CGetPetInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (petkey != _o_.petkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += petkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(petkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetPetInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

