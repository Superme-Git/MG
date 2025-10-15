
package fire.pb.pet;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetFightPet__ extends mkio.Protocol { }

/** 请求宠物参战
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetFightPet extends __CSetFightPet__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0 || !fire.pb.StateCommon.isOnline(roleid))
			return;

		Long battleId = xtable.Roleid2battleid.select(roleid);
		if (battleId == null) {
			PSetFightPetProc proc = new PSetFightPetProc(roleid, petkey, true);
			proc.submit();
		} else {
			// 如果战斗中换宠,则战斗结束后生效
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					final xbean.BattleInfo battle = xtable.Battle.get(battleId);
					if (battle != null) {
						Integer index = battle.getRoleids().get(roleid);
						if (index != null) {
							xbean.Fighter fighter = battle.getFighters().get(index);
							if (fighter != null) {
								fighter.setInipetkey(petkey);
								fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 180031);
							}
						}
					}
					return true;
				}
			};
			proc.submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788440;

	public int getType() {
		return 788440;
	}

	public int petkey; // 请求参战的宠物

	public CSetFightPet() {
	}

	public CSetFightPet(int _petkey_) {
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
		_os_.marshal(petkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetFightPet) {
			CSetFightPet _o_ = (CSetFightPet)_o1_;
			if (petkey != _o_.petkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetFightPet _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

