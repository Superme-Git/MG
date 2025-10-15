
package fire.pb.mission;

import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.task.ArenaFight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAcceptMission__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAcceptMission extends __SAcceptMission__ {
	@Override
	protected void process() {
		// protocol handle
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if(role!= null && role.getRoleTask() != null)
			if(!(role.getRoleTask() instanceof ArenaFight))
				role.getRoleTask().processProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805445;

	public int getType() {
		return 805445;
	}

	public fire.pb.mission.MissionInfo missioninfo;

	public SAcceptMission() {
		missioninfo = new fire.pb.mission.MissionInfo();
	}

	public SAcceptMission(fire.pb.mission.MissionInfo _missioninfo_) {
		this.missioninfo = _missioninfo_;
	}

	public final boolean _validator_() {
		if (!missioninfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(missioninfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missioninfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAcceptMission) {
			SAcceptMission _o_ = (SAcceptMission)_o1_;
			if (!missioninfo.equals(_o_.missioninfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missioninfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missioninfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

