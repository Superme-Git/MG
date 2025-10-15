
package fire.pb.skill.liveskill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestLiveSkillList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestLiveSkillList extends __SRequestLiveSkillList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800514;

	public int getType() {
		return 800514;
	}

	public java.util.LinkedList<fire.pb.skill.liveskill.LiveSkill> skilllist;

	public SRequestLiveSkillList() {
		skilllist = new java.util.LinkedList<fire.pb.skill.liveskill.LiveSkill>();
	}

	public SRequestLiveSkillList(java.util.LinkedList<fire.pb.skill.liveskill.LiveSkill> _skilllist_) {
		this.skilllist = _skilllist_;
	}

	public final boolean _validator_() {
		for (fire.pb.skill.liveskill.LiveSkill _v_ : skilllist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(skilllist.size());
		for (fire.pb.skill.liveskill.LiveSkill _v_ : skilllist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.skill.liveskill.LiveSkill _v_ = new fire.pb.skill.liveskill.LiveSkill();
			_v_.unmarshal(_os_);
			skilllist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestLiveSkillList) {
			SRequestLiveSkillList _o_ = (SRequestLiveSkillList)_o1_;
			if (!skilllist.equals(_o_.skilllist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += skilllist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skilllist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

