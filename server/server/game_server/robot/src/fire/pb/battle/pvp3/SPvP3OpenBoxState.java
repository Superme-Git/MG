
package fire.pb.battle.pvp3;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP3OpenBoxState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP3OpenBoxState extends __SPvP3OpenBoxState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793654;

	public int getType() {
		return 793654;
	}

	public byte boxtype; // 1:首胜，2：十战，3：五胜
	public byte state; // 0：不可领取，1：可领取，2：已领取

	public SPvP3OpenBoxState() {
	}

	public SPvP3OpenBoxState(byte _boxtype_, byte _state_) {
		this.boxtype = _boxtype_;
		this.state = _state_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(boxtype);
		_os_.marshal(state);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		boxtype = _os_.unmarshal_byte();
		state = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP3OpenBoxState) {
			SPvP3OpenBoxState _o_ = (SPvP3OpenBoxState)_o1_;
			if (boxtype != _o_.boxtype) return false;
			if (state != _o_.state) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)boxtype;
		_h_ += (int)state;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(boxtype).append(",");
		_sb_.append(state).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPvP3OpenBoxState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = boxtype - _o_.boxtype;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

