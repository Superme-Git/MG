
package fire.pb.skill.particleskill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestParticleSkillList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestParticleSkillList extends __SRequestParticleSkillList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800504;

	public int getType() {
		return 800504;
	}

	public java.util.LinkedList<fire.pb.skill.particleskill.ParticleSkill> skilllist;
	public int curcontribution; // 当前帮贡 by changhao
	public int factionlevel; // 公会等级 by changhao

	public SRequestParticleSkillList() {
		skilllist = new java.util.LinkedList<fire.pb.skill.particleskill.ParticleSkill>();
	}

	public SRequestParticleSkillList(java.util.LinkedList<fire.pb.skill.particleskill.ParticleSkill> _skilllist_, int _curcontribution_, int _factionlevel_) {
		this.skilllist = _skilllist_;
		this.curcontribution = _curcontribution_;
		this.factionlevel = _factionlevel_;
	}

	public final boolean _validator_() {
		for (fire.pb.skill.particleskill.ParticleSkill _v_ : skilllist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(skilllist.size());
		for (fire.pb.skill.particleskill.ParticleSkill _v_ : skilllist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(curcontribution);
		_os_.marshal(factionlevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.skill.particleskill.ParticleSkill _v_ = new fire.pb.skill.particleskill.ParticleSkill();
			_v_.unmarshal(_os_);
			skilllist.add(_v_);
		}
		curcontribution = _os_.unmarshal_int();
		factionlevel = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestParticleSkillList) {
			SRequestParticleSkillList _o_ = (SRequestParticleSkillList)_o1_;
			if (!skilllist.equals(_o_.skilllist)) return false;
			if (curcontribution != _o_.curcontribution) return false;
			if (factionlevel != _o_.factionlevel) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += skilllist.hashCode();
		_h_ += curcontribution;
		_h_ += factionlevel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skilllist).append(",");
		_sb_.append(curcontribution).append(",");
		_sb_.append(factionlevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

