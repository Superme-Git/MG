
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSetTeamFormation__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSetTeamFormation extends __SSetTeamFormation__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794465;

	public int getType() {
		return 794465;
	}

	public int formation;
	public int formationlevel; // 光环等级
	public byte msg; // 等于1时给消息提示，等于0时不给消息提示

	public SSetTeamFormation() {
	}

	public SSetTeamFormation(int _formation_, int _formationlevel_, byte _msg_) {
		this.formation = _formation_;
		this.formationlevel = _formationlevel_;
		this.msg = _msg_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(formation);
		_os_.marshal(formationlevel);
		_os_.marshal(msg);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		formation = _os_.unmarshal_int();
		formationlevel = _os_.unmarshal_int();
		msg = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSetTeamFormation) {
			SSetTeamFormation _o_ = (SSetTeamFormation)_o1_;
			if (formation != _o_.formation) return false;
			if (formationlevel != _o_.formationlevel) return false;
			if (msg != _o_.msg) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += formation;
		_h_ += formationlevel;
		_h_ += (int)msg;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(formation).append(",");
		_sb_.append(formationlevel).append(",");
		_sb_.append(msg).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSetTeamFormation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = formation - _o_.formation;
		if (0 != _c_) return _c_;
		_c_ = formationlevel - _o_.formationlevel;
		if (0 != _c_) return _c_;
		_c_ = msg - _o_.msg;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

