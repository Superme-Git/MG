
package fire.pb.battle.pvp1;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP1ReadyFight__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP1ReadyFight extends __SPvP1ReadyFight__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793539;

	public int getType() {
		return 793539;
	}

	public byte ready; // 1:准备，0：取消准备

	public SPvP1ReadyFight() {
	}

	public SPvP1ReadyFight(byte _ready_) {
		this.ready = _ready_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ready);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ready = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP1ReadyFight) {
			SPvP1ReadyFight _o_ = (SPvP1ReadyFight)_o1_;
			if (ready != _o_.ready) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)ready;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ready).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPvP1ReadyFight _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = ready - _o_.ready;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

