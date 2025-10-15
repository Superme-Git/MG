
package fire.pb.skill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendSpecialSkills__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendSpecialSkills extends __SSendSpecialSkills__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800443;

	public int getType() {
		return 800443;
	}

	public java.util.ArrayList<Integer> skills; // 特技
	public java.util.ArrayList<Integer> effects; // 特效

	public SSendSpecialSkills() {
		skills = new java.util.ArrayList<Integer>();
		effects = new java.util.ArrayList<Integer>();
	}

	public SSendSpecialSkills(java.util.ArrayList<Integer> _skills_, java.util.ArrayList<Integer> _effects_) {
		this.skills = _skills_;
		this.effects = _effects_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(skills.size());
		for (Integer _v_ : skills) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(effects.size());
		for (Integer _v_ : effects) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			skills.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			effects.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendSpecialSkills) {
			SSendSpecialSkills _o_ = (SSendSpecialSkills)_o1_;
			if (!skills.equals(_o_.skills)) return false;
			if (!effects.equals(_o_.effects)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += skills.hashCode();
		_h_ += effects.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skills).append(",");
		_sb_.append(effects).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

