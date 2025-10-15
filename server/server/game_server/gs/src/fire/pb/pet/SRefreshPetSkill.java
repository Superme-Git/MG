
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshPetSkill__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshPetSkill extends __SRefreshPetSkill__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788454;

	public int getType() {
		return 788454;
	}

	public int petkey;
	public java.util.LinkedList<fire.pb.Petskill> skills;
	public java.util.HashMap<Integer,Long> expiredtimes;

	public SRefreshPetSkill() {
		skills = new java.util.LinkedList<fire.pb.Petskill>();
		expiredtimes = new java.util.HashMap<Integer,Long>();
	}

	public SRefreshPetSkill(int _petkey_, java.util.LinkedList<fire.pb.Petskill> _skills_, java.util.HashMap<Integer,Long> _expiredtimes_) {
		this.petkey = _petkey_;
		this.skills = _skills_;
		this.expiredtimes = _expiredtimes_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		for (fire.pb.Petskill _v_ : skills)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.compact_uint32(skills.size());
		for (fire.pb.Petskill _v_ : skills) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(expiredtimes.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : expiredtimes.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.Petskill _v_ = new fire.pb.Petskill();
			_v_.unmarshal(_os_);
			skills.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			long _v_;
			_v_ = _os_.unmarshal_long();
			expiredtimes.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshPetSkill) {
			SRefreshPetSkill _o_ = (SRefreshPetSkill)_o1_;
			if (petkey != _o_.petkey) return false;
			if (!skills.equals(_o_.skills)) return false;
			if (!expiredtimes.equals(_o_.expiredtimes)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += skills.hashCode();
		_h_ += expiredtimes.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(skills).append(",");
		_sb_.append(expiredtimes).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

