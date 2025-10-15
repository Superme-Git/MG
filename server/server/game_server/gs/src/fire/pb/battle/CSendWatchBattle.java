
package fire.pb.battle;
import fire.pb.battle.watch.PSendWatchBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.util.FireProp;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSendWatchBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSendWatchBattle extends __CSendWatchBattle__ {
	@Override
	protected void process() {
		final long watcherid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (watcherid < 0)
			return;
		if(watcherid==roleid)
			return ;
		BuffAgent agent = new BuffRoleImpl(watcherid,true);
		if (!agent.canAddBuff(FireProp.getIntValue("battle", "mtgserv.battle_watchBuff"))) {
			fire.pb.talk.MessageMgr.sendMsgNotify(watcherid, fire.pb.util.FireProp.getIntValue("message", "mtgserv.buffConflict"), null);
			return;
		}
		new PSendWatchBattle(watcherid,roleid).submit();
		 
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793443;

	public int getType() {
		return 793443;
	}

	public long roleid; // 要观战的战斗角色id

	public CSendWatchBattle() {
	}

	public CSendWatchBattle(long _roleid_) {
		this.roleid = _roleid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSendWatchBattle) {
			CSendWatchBattle _o_ = (CSendWatchBattle)_o1_;
			if (roleid != _o_.roleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSendWatchBattle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

