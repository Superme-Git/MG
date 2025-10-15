
package fire.msp.role;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSetNpcBattleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSetNpcBattleInfo extends __GSetNpcBattleInfo__ {
	@Override
	protected void process() {
		// protocol handle
		NPC npc = SceneNpcManager.getInstance().getNpcByKey(npckey);
		if (npc == null)
			return;
		
		if (battlestate == 0) {
			npc.setBattleFighterStateAndBroacast(false);
		}
		else if (battlestate == 1) {
			npc.setBattleFighterStateAndBroacast(true);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730911;

	public int getType() {
		return 730911;
	}

	public long npckey; // roleid
	public int battlestate; // 0：无；1：战斗者

	public GSetNpcBattleInfo() {
	}

	public GSetNpcBattleInfo(long _npckey_, int _battlestate_) {
		this.npckey = _npckey_;
		this.battlestate = _battlestate_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(battlestate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		battlestate = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSetNpcBattleInfo) {
			GSetNpcBattleInfo _o_ = (GSetNpcBattleInfo)_o1_;
			if (npckey != _o_.npckey) return false;
			if (battlestate != _o_.battlestate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += battlestate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(battlestate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSetNpcBattleInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = battlestate - _o_.battlestate;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

