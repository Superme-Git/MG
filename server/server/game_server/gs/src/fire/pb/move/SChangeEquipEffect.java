
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SChangeEquipEffect__ extends mkio.Protocol { }

/** 改变玩家装备特效
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SChangeEquipEffect extends __SChangeEquipEffect__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790491;

	public int getType() {
		return 790491;
	}

	public long playerid; // 玩家id
	public int effect; // 玩家装备特效

	public SChangeEquipEffect() {
	}

	public SChangeEquipEffect(long _playerid_, int _effect_) {
		this.playerid = _playerid_;
		this.effect = _effect_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(playerid);
		_os_.marshal(effect);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		playerid = _os_.unmarshal_long();
		effect = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SChangeEquipEffect) {
			SChangeEquipEffect _o_ = (SChangeEquipEffect)_o1_;
			if (playerid != _o_.playerid) return false;
			if (effect != _o_.effect) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)playerid;
		_h_ += effect;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(playerid).append(",");
		_sb_.append(effect).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SChangeEquipEffect _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(playerid - _o_.playerid);
		if (0 != _c_) return _c_;
		_c_ = effect - _o_.effect;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

