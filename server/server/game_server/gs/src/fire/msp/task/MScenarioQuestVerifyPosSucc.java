
package fire.msp.task;



import fire.pb.mission.MissionColumn;
import fire.pb.mission.RoleMission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MScenarioQuestVerifyPosSucc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MScenarioQuestVerifyPosSucc extends __MScenarioQuestVerifyPosSucc__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId( roleid );
		if ( team == null || team.isAbsentMember(roleid) ) {
			new fire.pb.mission.PCommitMajorMission(roleid, scenarioquestid, true).submit();
			return;
		}
		if (team.isTeamLeader(roleid)) {
//			final int exetype = Helper.getTaskExeType( conf.activeInfo.missionType );
//			final int finishtype = Helper.getTaskFinishRequireType( conf.activeInfo.missionType );
//			if ( exetype == TaskExecuteTypes.NUL && ( finishtype == TaskFinishRequire.AREA || finishtype == TaskFinishRequire.NUL )||  
//					exetype == TaskExecuteTypes.NPC && (finishtype == TaskFinishRequire.NUL || finishtype == TaskFinishRequire.QUESTION ) ) {
			RoleMission task = new MissionColumn(roleid, true).getMission(scenarioquestid);
			if(task != null) {
				int teamshare = task.getConf().exeIndo.share;
				if (teamshare == 0) {
					//不共享
					new fire.pb.mission.PCommitMajorMission(roleid, scenarioquestid, true).submit();
				} else {
					for (long memid : team.getNormalMemberIds()) {
						fire.pb.mission.PCommitMajorMission proc = new fire.pb.mission.PCommitMajorMission(memid, scenarioquestid, memid == roleid);
						proc.submit();
					}
				}
			}
//				return;
//			}
//			new fire.pb.mission.PCommitMajorMission(roleid, scenarioquestid, true).submit();
		}
	}
	
//	private final class PCommitMajorMission extends fire.pb.mission.PTaskProcedure {
//		private final long playerid;
//		public PCommitMajorMission( final long playerid ) {
//			this.playerid = playerid;
//		}
//		@Override
//		protected boolean taskProcess() {
//			fire.pb.mission.TaskScenarioColumn sml =
//				new fire.pb.mission.TaskScenarioColumn( playerid, this );
//			return sml.commitMission(scenarioquestid);
//		}
//	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 739907;

	public int getType() {
		return 739907;
	}

	public long roleid;
	public int scenarioquestid;

	public MScenarioQuestVerifyPosSucc() {
	}

	public MScenarioQuestVerifyPosSucc(long _roleid_, int _scenarioquestid_) {
		this.roleid = _roleid_;
		this.scenarioquestid = _scenarioquestid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(scenarioquestid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		scenarioquestid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MScenarioQuestVerifyPosSucc) {
			MScenarioQuestVerifyPosSucc _o_ = (MScenarioQuestVerifyPosSucc)_o1_;
			if (roleid != _o_.roleid) return false;
			if (scenarioquestid != _o_.scenarioquestid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += scenarioquestid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(scenarioquestid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MScenarioQuestVerifyPosSucc _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = scenarioquestid - _o_.scenarioquestid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

