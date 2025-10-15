
package fire.pb.battle.pvp5;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP5BattleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP5BattleInfo extends __SPvP5BattleInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793670;

	public int getType() {
		return 793670;
	}

	public byte ismine; // 1:属于我的战况，0：别人的战况
	public int msgid;
	public java.util.LinkedList<com.locojoy.base.Octets> parameters;

	public SPvP5BattleInfo() {
		parameters = new java.util.LinkedList<com.locojoy.base.Octets>();
	}

	public SPvP5BattleInfo(byte _ismine_, int _msgid_, java.util.LinkedList<com.locojoy.base.Octets> _parameters_) {
		this.ismine = _ismine_;
		this.msgid = _msgid_;
		this.parameters = _parameters_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ismine);
		_os_.marshal(msgid);
		_os_.compact_uint32(parameters.size());
		for (com.locojoy.base.Octets _v_ : parameters) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ismine = _os_.unmarshal_byte();
		msgid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			parameters.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP5BattleInfo) {
			SPvP5BattleInfo _o_ = (SPvP5BattleInfo)_o1_;
			if (ismine != _o_.ismine) return false;
			if (msgid != _o_.msgid) return false;
			if (!parameters.equals(_o_.parameters)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)ismine;
		_h_ += msgid;
		_h_ += parameters.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ismine).append(",");
		_sb_.append(msgid).append(",");
		_sb_.append(parameters).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

