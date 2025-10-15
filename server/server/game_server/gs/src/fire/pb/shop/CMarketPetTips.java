
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMarketPetTips__ extends mkio.Protocol { }

/** 宠物tips
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMarketPetTips extends __CMarketPetTips__ {
	@Override
	protected void process() {
		// protocol handle
		long reqRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (reqRoleId < 0 || roleid <= 0 || key < 0)
			return;
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(roleid, fire.pb.pet.PetColumnTypes.MARKETPET, true);
				fire.pb.pet.Pet pet = petcol.getPet(key);
				if (pet == null) {
					fire.pb.talk.MessageMgr.psendMsgNotify(reqRoleId, 190064, null);
					return true;
				}
				fire.pb.Pet petMarshal = pet.getProtocolPet();
				SMarketPetTips send = new SMarketPetTips(petMarshal, tipstype);
				mkdb.Procedure.psendWhileCommit(reqRoleId, send);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810649;

	public int getType() {
		return 810649;
	}

	public long roleid; // 角色id
	public int key; // 摆摊容器中的key
	public int tipstype; // 显示tips类型

	public CMarketPetTips() {
	}

	public CMarketPetTips(long _roleid_, int _key_, int _tipstype_) {
		this.roleid = _roleid_;
		this.key = _key_;
		this.tipstype = _tipstype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(key);
		_os_.marshal(tipstype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		key = _os_.unmarshal_int();
		tipstype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMarketPetTips) {
			CMarketPetTips _o_ = (CMarketPetTips)_o1_;
			if (roleid != _o_.roleid) return false;
			if (key != _o_.key) return false;
			if (tipstype != _o_.tipstype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += key;
		_h_ += tipstype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(key).append(",");
		_sb_.append(tipstype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CMarketPetTips _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		_c_ = tipstype - _o_.tipstype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

