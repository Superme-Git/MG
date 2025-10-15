
package fire.pb.pet;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CShowPetInfo__ extends mkio.Protocol { }

/** 客户端请求查看场景中的宠物信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CShowPetInfo extends __CShowPetInfo__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0 || !fire.pb.StateCommon.isOnline(roleid))
			return;

		final fire.pb.PropRole propRole = new fire.pb.PropRole(masterid, true);
		if (propRole.getShowpetkey() > 0) {
			final PetColumn petCol = new PetColumn(masterid, PetColumnTypes.PET, true);

			final SShowPetInfo send = new SShowPetInfo(0, petCol.getPet(propRole.getShowpetkey()).getProtocolPet());
			gnet.link.Onlines.getInstance().send(roleid, send);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788456;

	public int getType() {
		return 788456;
	}

	public long masterid; // 宠物主人的角色id

	public CShowPetInfo() {
	}

	public CShowPetInfo(long _masterid_) {
		this.masterid = _masterid_;
	}

	public final boolean _validator_() {
		if (masterid < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(masterid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		masterid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CShowPetInfo) {
			CShowPetInfo _o_ = (CShowPetInfo)_o1_;
			if (masterid != _o_.masterid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)masterid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(masterid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CShowPetInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(masterid - _o_.masterid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

