package fire.pb.mission;

import fire.pb.mission.MissionExeTypes;
import fire.pb.mission.MissionFinTypes;
import fire.pb.mission.MissionStatus;
import fire.pb.mission.SSpecialScenarioQuestConfig;
import fire.pb.mission.util.NpcselectCommitParam;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class TCommitMission {
	private enum HandlingType {FAIL, ACCEPT, COMMIT}
	private final long roleid;
	private final long npckey;
	private final int optionid;
	private final int missionid;
	
	public TCommitMission(final long roleid, final int questid, final long npckey, final int optionid) {
		this.roleid = roleid;
		this.npckey = npckey;
		this.optionid = optionid;
		this.missionid = questid;
	}
	
	private HandlingType submitMission(final long memid) {
		return submitMission(memid, null);
	}
	
	private HandlingType submitMission(final long memid, final HandlingType leadertype) {
		RoleMission mission = new MissionColumn(memid, true).getMission(missionid);
		if (mission == null) {
			return HandlingType.FAIL;
		}
		final MissionConfig conf = mission.getConf();
		if (conf == null)
			return HandlingType.FAIL;
		if (leadertype == null || leadertype == HandlingType.ACCEPT) {
			if (UtilHelper.isBranchScenarioMission(missionid)
					&& UtilHelper.isFirstStepInLine(UtilHelper.getMissionStep(missionid))) {
				if (mission.getState() == MissionStatus.UNACCEPT) {
					new PAcceptMajorMission(memid, missionid, true).submit();
					return HandlingType.ACCEPT;
				}
				if (leadertype == HandlingType.ACCEPT) {
					return HandlingType.FAIL;
				}
			}
		}
		
		if (conf.hasGoto()) {
			if (conf.hasAni()) {
				if (!fire.pb.map.Transfer.canChangeMap(roleid, SRoleEnterScene.QUEST_CG, conf.exeIndo.mapID)) {
					Module.logger.debug( "无法跳转地图" );
					return HandlingType.FAIL;
				}
			} else {
				if(!fire.pb.map.Transfer.canChangeMap(roleid, SRoleEnterScene.QUEST, conf.exeIndo.mapID)) {
					Module.logger.debug( "无法跳转地图" );
					return HandlingType.FAIL;
				}
			}
		}
		final int exetype = UtilHelper.getMissionExeType(conf.exeIndo.missionType);
		final int finishtype = UtilHelper.getMissionFinType(conf.exeIndo.missionType);
		if (roleid == memid && finishtype == MissionFinTypes.OTHER) {
			SSpecialScenarioQuestConfig sconf = mission.getSConf();
			if (sconf != null) {

			}
		}
		if (exetype == MissionExeTypes.NUL && finishtype == MissionFinTypes.AREA) {
			if (roleid != memid)
				return HandlingType.FAIL;
			fire.msp.task.GScenarioQuestVerifyPos tosend = new fire.msp.task.GScenarioQuestVerifyPos();
			tosend.roleid = memid;
			tosend.mapid = conf.exeIndo.mapID;
			tosend.bottom = conf.exeIndo.bottomPos;
			tosend.left = conf.exeIndo.leftPos;
			tosend.right = conf.exeIndo.rightPos;
			tosend.top = conf.exeIndo.topPos;
			tosend.scenarioquestid = missionid;
			fire.pb.GsClient.sendToScene(tosend);
			return HandlingType.COMMIT;
		}
		new PCommitMajorMission(memid, missionid,
				new NpcselectCommitParam(npckey, optionid), roleid == memid).submit();
		return HandlingType.COMMIT;
	}
	
	public HandlingType handling() {
		if (npckey != 0 && npckey != -1) {
			if (!fire.pb.map.SceneNpcManager.checkDistance(npckey, roleid))
				return HandlingType.FAIL;
		}
		final MissionConfig conf = MissionManager.getInstance().getMissionConfig(missionid);
		if (conf == null)
			return HandlingType.FAIL;
		final Team team = TeamManager.selectTeamByRoleId(roleid);
		
		HandlingType resultDealType=HandlingType.FAIL;
		
		if (team == null || team.isAbsentMember(roleid)) {
			if (conf.exeIndo.teamState == 2) {
				return HandlingType.FAIL;
			}
			resultDealType=submitMission(roleid);	
			return resultDealType;
		} else {
			if (conf.exeIndo.teamState == 0) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 141711, null);
				return HandlingType.FAIL;
			}
			if (team.isTeamLeader(roleid)) {
				HandlingType ret = submitMission(roleid);
				RoleMission mission = new MissionColumn(roleid, true).getMission(missionid);
				if(mission != null) {
					int teamshare = mission.getConf().exeIndo.share;
					if (teamshare == 0) {
						//不共享
					} else {
						for (long memid : team.getNormalMemberIds()) {
							if (memid != roleid){
								resultDealType=submitMission(memid, ret);
							}
						}
					}	
				} else {
					for (long memid : team.getNormalMemberIds()) {
						if (memid != roleid){
							resultDealType=submitMission(memid, ret);
						}
					}
				}		
				
				return ret;
			}
		}
		return HandlingType.FAIL;
	}
}
