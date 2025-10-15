
package fire.pb.battle.livedie;

import fire.pb.battle.BattleField;
import fire.pb.battle.watch.PSendWatchBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.util.FireProp;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CLiveDieBattleWatchFight__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CLiveDieBattleWatchFight extends __CLiveDieBattleWatchFight__ {
	@Override
	protected void process() {
		// protocol handle
		final long watcherid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(watcherid<=0){
			return ;
		}
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				xbean.BattleInfo battle = xtable.Battle.get(battleid);
				if(battle == null){
					fire.pb.talk.MessageMgr.sendMsgNotify(watcherid, 162122, null);
					return false;
				}
				long hostroleid = battle.getInifighters().get(BattleField.HOST_FIRST_ID).getUniqueid();
				long guestroleid = battle.getInifighters().get(BattleField.GUEST_FIRST_ID).getUniqueid();
				if(watcherid==hostroleid||watcherid==guestroleid){
					fire.pb.talk.MessageMgr.sendMsgNotify(watcherid, 160495, null);
					return false;
				}
				BuffAgent agent = new BuffRoleImpl(watcherid,true);
				if (!agent.canAddBuff(FireProp.getIntValue("battle", "mtgserv.battle_watchBuff"))) {
					fire.pb.talk.MessageMgr.sendMsgNotify(watcherid, fire.pb.util.FireProp.getIntValue("message", "mtgserv.buffConflict"), null);
					return false;
				}
				new PSendWatchBattle(watcherid,hostroleid).call();
				return true;
			};
		}.submit();
		
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793847;

	public int getType() {
		return 793847;
	}

	public long battleid; // 战斗id

	public CLiveDieBattleWatchFight() {
	}

	public CLiveDieBattleWatchFight(long _battleid_) {
		this.battleid = _battleid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(battleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		battleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CLiveDieBattleWatchFight) {
			CLiveDieBattleWatchFight _o_ = (CLiveDieBattleWatchFight)_o1_;
			if (battleid != _o_.battleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)battleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(battleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CLiveDieBattleWatchFight _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(battleid - _o_.battleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

