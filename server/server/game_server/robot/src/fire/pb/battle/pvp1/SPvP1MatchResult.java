
package fire.pb.battle.pvp1;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP1MatchResult__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP1MatchResult extends __SPvP1MatchResult__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793542;

	public int getType() {
		return 793542;
	}

	public fire.pb.battle.pvp1.PvP1RoleSingleMatch target; // 目标

	public SPvP1MatchResult() {
		target = new fire.pb.battle.pvp1.PvP1RoleSingleMatch();
	}

	public SPvP1MatchResult(fire.pb.battle.pvp1.PvP1RoleSingleMatch _target_) {
		this.target = _target_;
	}

	public final boolean _validator_() {
		if (!target._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(target);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		target.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP1MatchResult) {
			SPvP1MatchResult _o_ = (SPvP1MatchResult)_o1_;
			if (!target.equals(_o_.target)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += target.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(target).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

