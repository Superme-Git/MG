
package fire.pb.skill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPractiseSkill__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPractiseSkill extends __CPractiseSkill__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800460;

	public int getType() {
		return 800460;
	}

	public long npckey;
	public int skillid;
	public byte kind; // 0=修炼  1=试练

	public CPractiseSkill() {
	}

	public CPractiseSkill(long _npckey_, int _skillid_, byte _kind_) {
		this.npckey = _npckey_;
		this.skillid = _skillid_;
		this.kind = _kind_;
	}

	public final boolean _validator_() {
		if (kind < 0 || kind > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(skillid);
		_os_.marshal(kind);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		skillid = _os_.unmarshal_int();
		kind = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPractiseSkill) {
			CPractiseSkill _o_ = (CPractiseSkill)_o1_;
			if (npckey != _o_.npckey) return false;
			if (skillid != _o_.skillid) return false;
			if (kind != _o_.kind) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += skillid;
		_h_ += (int)kind;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(skillid).append(",");
		_sb_.append(kind).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPractiseSkill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = skillid - _o_.skillid;
		if (0 != _c_) return _c_;
		_c_ = kind - _o_.kind;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

