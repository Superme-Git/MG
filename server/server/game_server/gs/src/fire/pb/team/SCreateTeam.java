
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SCreateTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SCreateTeam extends __SCreateTeam__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794434;

	public int getType() {
		return 794434;
	}

	public long teamid; // 队伍ID
	public int formation; // 队伍光环
	public int teamstate; // 队伍状态，参考TeamState：1=普通队伍；2=等待中的队伍；3=召集令队伍
	public int smapid; // 地图玩法ID，当处于普通队伍状态时，smapId为0

	public SCreateTeam() {
	}

	public SCreateTeam(long _teamid_, int _formation_, int _teamstate_, int _smapid_) {
		this.teamid = _teamid_;
		this.formation = _formation_;
		this.teamstate = _teamstate_;
		this.smapid = _smapid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(teamid);
		_os_.marshal(formation);
		_os_.marshal(teamstate);
		_os_.marshal(smapid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teamid = _os_.unmarshal_long();
		formation = _os_.unmarshal_int();
		teamstate = _os_.unmarshal_int();
		smapid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SCreateTeam) {
			SCreateTeam _o_ = (SCreateTeam)_o1_;
			if (teamid != _o_.teamid) return false;
			if (formation != _o_.formation) return false;
			if (teamstate != _o_.teamstate) return false;
			if (smapid != _o_.smapid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)teamid;
		_h_ += formation;
		_h_ += teamstate;
		_h_ += smapid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamid).append(",");
		_sb_.append(formation).append(",");
		_sb_.append(teamstate).append(",");
		_sb_.append(smapid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SCreateTeam _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(teamid - _o_.teamid);
		if (0 != _c_) return _c_;
		_c_ = formation - _o_.formation;
		if (0 != _c_) return _c_;
		_c_ = teamstate - _o_.teamstate;
		if (0 != _c_) return _c_;
		_c_ = smapid - _o_.smapid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

