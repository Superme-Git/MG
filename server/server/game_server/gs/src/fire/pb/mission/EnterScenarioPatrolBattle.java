package fire.pb.mission;

import org.apache.log4j.Logger;

import fire.pb.mission.MissionStatus;

public class EnterScenarioPatrolBattle {

	private long roleid;
	
	public static Logger logger = Logger.getLogger("TASK");
	
	public EnterScenarioPatrolBattle(long roleid){
		this.roleid = roleid;
	}
	
	public void enterBattle(){
		final fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
		
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		if (null != team)
			if (!team.getAbsentMemberIds().contains(roleid) && team.getTeamLeaderId() != roleid){
			return;
		}
		
		int questid = RoleMission.getScenarioPatroling(roleid);
		if (questid == 0) {
			return;
		}
		
		final MissionConfig conf = MissionManager.getInstance().getMissionConfig( questid );
		if (conf == null)
			throw new NullPointerException( "任务id=" + questid + "的配置为null" );
		
		MissionColumn sml = new MissionColumn( roleid, true );
		RoleMission mission = sml.getMission(questid);
		if ( mission == null ) {
			Module.logger.debug( "没有该任务的信息" );
			return;
		}
		final int status = mission.getState();
		if ( status == MissionStatus.PROCESSING ) {	
			final int aiid = conf.aiInfo.aIID;
			if (aiid <= 0){
				return;
			}
			new MissionBattle(roleid, aiid, 
					new MissionBattleEndHandler(questid, conf.aiInfo.battleResult, roleid),
					conf.scenarioInfo.animationID).submit();
			logger.debug("玩家   " + prole.getName() + "进入剧情任务的巡逻战斗!");
		}
	}

}
