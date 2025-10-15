
package fire.pb.skill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateAssistSkill__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateAssistSkill extends __SUpdateAssistSkill__ {
	@Override
	protected void process() {
		 robot.LoginRole role = robot.LoginRoleMgr.findLoginRole(this);
		if(role!= null){
			if(assistskill.id == 350001){
				role.setInitump(false);
				role.setCanJump(true);
			}
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800439;

	public int getType() {
		return 800439;
	}

	public fire.pb.skill.AssistSkill assistskill;

	public SUpdateAssistSkill() {
		assistskill = new fire.pb.skill.AssistSkill();
	}

	public SUpdateAssistSkill(fire.pb.skill.AssistSkill _assistskill_) {
		this.assistskill = _assistskill_;
	}

	public final boolean _validator_() {
		if (!assistskill._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(assistskill);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		assistskill.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateAssistSkill) {
			SUpdateAssistSkill _o_ = (SUpdateAssistSkill)_o1_;
			if (!assistskill.equals(_o_.assistskill)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += assistskill.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(assistskill).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SUpdateAssistSkill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = assistskill.compareTo(_o_.assistskill);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

