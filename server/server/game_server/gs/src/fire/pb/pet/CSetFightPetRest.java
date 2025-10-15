
package fire.pb.pet;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetFightPetRest__ extends mkio.Protocol { }

/** 请求将参战宠物置位休息状态
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetFightPetRest extends __CSetFightPetRest__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0 || !fire.pb.StateCommon.isOnline(roleid))
			return;

		Long battleId = xtable.Roleid2battleid.select(roleid);
		if (battleId == null) {
			PSetFightPetProc proc = new PSetFightPetProc(roleid, 0, false);
			proc.submit();
		} else {
			// 如果战斗中设置宠物休息,则战斗结束后生效
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					final xbean.BattleInfo battle = xtable.Battle.get(battleId);
					if (battle != null) {
						Integer index = battle.getRoleids().get(roleid);
						if (index != null) {
							xbean.Fighter fighter = battle.getFighters().get(index);
							if (fighter != null) {
								fighter.setInipetkey(-1);
								fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 150090);
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
	public static final int PROTOCOL_TYPE = 788442;

	public int getType() {
		return 788442;
	}


	public CSetFightPetRest() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetFightPetRest) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetFightPetRest _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

