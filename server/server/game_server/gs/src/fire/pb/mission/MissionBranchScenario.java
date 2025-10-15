package fire.pb.mission;

import java.util.Set;

import fire.pb.mission.MissionStatus;




public class MissionBranchScenario extends RoleMission {

	public MissionBranchScenario(final long roleid, final xbean.Mission data) {
		super(roleid, data.isData());
		this.data = data;
		conf = MissionManager.getInstance().getMissionConfig(getId());
	}
	
	@Override
	public boolean abandon() {
		if (conf == null) {
			Module.logger.error("剧情任务id=" + getId() + "配置为空");
			return false;
		}
		if (UtilHelper.getMissionLineid(conf.missionId) == 2032
				|| UtilHelper.getMissionLineid(conf.missionId) == 2209) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 141484, null);
			return false;
		}
		final xbean.Properties prop = xtable.Properties.select( roleid );
		if (conf.exeIndo.transformID != 0) {
			// 变身回来
			UtilHelper.clearTransformID(roleid, conf.missionId);
		}

		SRefreshMissionState send = new SRefreshMissionState();
		send.missionid = getId();
		send.missionstatus = MissionStatus.ABANDON;
		if (send.missionid< 1001 || send.missionid>  999999)
			Module.logger.error("Marshal Error.method:abandon.taskid:"+send.missionid);
		mkdb.Transaction.tsendWhileCommit( roleid, send );
		final int taskline = UtilHelper.getMissionLineid(getId());
		if (PAcceptMajorMission.canAccept(roleid, UtilHelper.getMissionId(taskline, 1))) {
			data.setStatus(MissionStatus.UNACCEPT);
			data.setId(UtilHelper.getMissionId(taskline, UtilHelper.getFirstStep()));
		} else {
			xbean.AcceptedScenarioMission xxx = xtable.Acceptedscenariomission.get(roleid);
			if (xxx != null) {
				xxx.getAccepted().remove(taskline);
			} 
		}
		SReqMissionCanAccept send2 = new SReqMissionCanAccept();
		Module.getInstance().getCanAcceptMission4Abandon(roleid, send2.missions);
		mkdb.Procedure.psend(roleid, send2);
		
		Set<Integer> canAccepts = MissionManager.getInstance().getTasklineCanAccept(prop.getSchool());
		if (!canAccepts.contains(UtilHelper.getMissionLineid(conf.missionId))) {
			xbean.AcceptedScenarioMission ast = xtable.Acceptedscenariomission.get(roleid);
			if (ast != null) {
				ast.getAccepted().remove(UtilHelper.getMissionLineid(conf.missionId));
			}
			xbean.CommitedScenarioMission cst = xtable.Commitedscenariomission.get(roleid);
			if (cst == null) {
				cst = xbean.Pod.newCommitedScenarioMission();
				xtable.Commitedscenariomission.insert(roleid, cst);
			}
		}
		return true;
	}

	
}
