
package fire.pb.ranklist;


import fire.pb.pet.Pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestRankPetData__ extends mkio.Protocol { }

/** 请求榜单上宠物的详细信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestRankPetData extends __CRequestRankPetData__ {
	@Override
	protected void process() {
		// protocol handle
		final long srcRoleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (srcRoleid < 0)
			return;
		
		Pet pet = Pet.getPet(uniquepetid, true);
		if(pet != null){
			SSendRankPetData rankPetData = new SSendRankPetData();
			rankPetData.petinfo = pet.getProtocolPet();
			rankPetData.uniquepetid = uniquepetid;
			
			gnet.link.Onlines.getInstance().send(srcRoleid, rankPetData);
		}
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810240;

	public int getType() {
		return 810240;
	}

	public long uniquepetid; // 宠物的唯一id

	public CRequestRankPetData() {
	}

	public CRequestRankPetData(long _uniquepetid_) {
		this.uniquepetid = _uniquepetid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(uniquepetid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		uniquepetid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestRankPetData) {
			CRequestRankPetData _o_ = (CRequestRankPetData)_o1_;
			if (uniquepetid != _o_.uniquepetid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)uniquepetid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(uniquepetid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestRankPetData _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(uniquepetid - _o_.uniquepetid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

