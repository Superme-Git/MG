
package fire.pb.skill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendLearnedAssistSkill__ extends mkio.Protocol { }

/** 辅助技能
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendLearnedAssistSkill extends __SSendLearnedAssistSkill__ {
	@Override
	protected void process() {
		 robot.LoginRole role = robot.LoginRoleMgr.findLoginRole(this);
		if(role != null){
			for(fire.pb.skill.AssistSkill as: assistskills){
				if(as.id == 350001){//机器人已经学会了轻功
					role.setInitump(false);
					role.setCanJump(true);
					break;
				}
			}
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800437;

	public int getType() {
		return 800437;
	}

	public java.util.LinkedList<fire.pb.skill.AssistSkill> assistskills;

	public SSendLearnedAssistSkill() {
		assistskills = new java.util.LinkedList<fire.pb.skill.AssistSkill>();
	}

	public SSendLearnedAssistSkill(java.util.LinkedList<fire.pb.skill.AssistSkill> _assistskills_) {
		this.assistskills = _assistskills_;
	}

	public final boolean _validator_() {
		for (fire.pb.skill.AssistSkill _v_ : assistskills)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(assistskills.size());
		for (fire.pb.skill.AssistSkill _v_ : assistskills) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.skill.AssistSkill _v_ = new fire.pb.skill.AssistSkill();
			_v_.unmarshal(_os_);
			assistskills.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendLearnedAssistSkill) {
			SSendLearnedAssistSkill _o_ = (SSendLearnedAssistSkill)_o1_;
			if (!assistskills.equals(_o_.assistskills)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += assistskills.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(assistskills).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

