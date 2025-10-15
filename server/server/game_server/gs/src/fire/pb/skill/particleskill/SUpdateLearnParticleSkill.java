
package fire.pb.skill.particleskill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateLearnParticleSkill__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateLearnParticleSkill extends __SUpdateLearnParticleSkill__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800506;

	public int getType() {
		return 800506;
	}

	public fire.pb.skill.particleskill.ParticleSkill skill;

	public SUpdateLearnParticleSkill() {
		skill = new fire.pb.skill.particleskill.ParticleSkill();
	}

	public SUpdateLearnParticleSkill(fire.pb.skill.particleskill.ParticleSkill _skill_) {
		this.skill = _skill_;
	}

	public final boolean _validator_() {
		if (!skill._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(skill);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		skill.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateLearnParticleSkill) {
			SUpdateLearnParticleSkill _o_ = (SUpdateLearnParticleSkill)_o1_;
			if (!skill.equals(_o_.skill)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += skill.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skill).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

