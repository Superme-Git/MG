package fire.pb.map;

import java.util.Properties;

import fire.pb.PropConf;
import fire.pb.battle.HideBattle;
import fire.pb.circletask.CircleTask;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.EnterAnYePatrolBattle;
import fire.pb.circletask.EnterCircTaskPatrolBattle;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.fushi.DayPayManager;
import fire.pb.item.SpecialItem;
import fire.pb.main.ConfigManager;
import fire.pb.mission.EnterScenarioPatrolBattle;
import fire.pb.mission.MissionConfig;
import fire.pb.mission.MissionManager;
import fire.pb.mission.RoleMission;
import fire.pb.scene.movable.MoveUnit;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.FireProp;
import fire.pb.util.Misc;


/**
 * 地图中的战斗处理
 * @author DevUser
 *
 */
public class SceneBattle {
//	public static boolean printHideBattleSteps = false;//是否打印遇怪的步数,临时功能,以后去掉
//	public static long printHideBattleRoleid = 0;//是否打印遇怪的步数,临时功能,以后去掉
	/**
	 * 检测战斗单位的合法性
	 * @param role	
	 * @return	可进入战斗的战斗单位/否
	 */
	private static boolean checkBattleUnit(fire.pb.map.Role role, long curTick){
		
		Team team = TeamManager.selectTeamByRoleId(role.getRoleID());
		if(team == null || team.isAbsentMember(role.getRoleID()))
		{
			if(curTick - role.getLastHideBattleTick() < MIN_BATTLE_INTERVAL_NOTEAM)
				return false;
		}
		else if(team.isNormalMember(role.getRoleID()))
		{
			return false;
		}
		else if(team.isTeamLeader(role.getRoleID()))
		{
			if(curTick - role.getLastHideBattleTick() < MIN_BATTLE_INTERVAL_TEAM)
				return false;
		}
		
		//如果正在执行结婚任务的情比金坚阶段，不遇明雷暗雷
//		MarryTaskInfo marryInfo = MarryTaskStepManager.getMarryTaskManager().getCurrentRoleMarryInfo(role.getRoleID());
//		if(null != marryInfo){
//			if(marryInfo.getTaskStep() == Constant.QING_BI_JIN_JIAN || marryInfo.getTaskStep() == Constant.COMMIT_QING_BI_JIN_JIAN){
//				return false;
//			}
//		}
		return true;
	}
	/**
	 * 明雷战斗触发
	 * @param role
	 * @param monster
	 * @return battleID
	 */
/*	public static void activeMonstershowBattle( Role role, long monsterkey, long curTick ) {
		fire.pb.map.Scene scene = fire.pb.map.SceneManager.getInstance().getSceneByMapID( 1002 );
		byte conf = scene.getMonsterInfo().getMonsterConfig( 54, 96 );
		if(0 == conf)
			return;
		
		final int teamSize = MapTeamManager.getInstance().getTeamMembers(role.getRoleID()).size() + 1;
		ShowBattle battle = new ShowBattle(role.getRoleID(), conf, teamSize );
		battle.setMonsterID( monsterkey );
		role.setRoadLen(0);
		role.setLastBattleTick(curTick);
		battle.launch();
	}*/
	
	/**
	 * 暗雷和明雷战斗过程处理
	 * @param role			角色
	 * @param newRoadLen	当前行走的路程
	 * @param curTick		当前时刻
	 */
	public static void process(/*RoleHideBattleInfo role,*/Role role, int newRoadLen, long curTick,int conf){
		
		final fire.pb.buff.BuffAgent agent = new fire.pb.buff.BuffRoleImpl(
				role.getRoleID(),true);
		
		if (SceneBattle.checkSpecialPatrol(role, newRoadLen))
			return;
		
		
		if(conf > 0)// 暗雷战斗
		{
			final boolean isScenarioMap = RoleMission.isScenarioIgnoreMap(role.getRoleID(), role.getMapId());
			if(isScenarioMap) {	
				//主线任务要求该地图不遇怪
				return;
			}

			if(! checkBattleUnit(role, curTick))
				return;
			int teamSize = TeamManager.getFightTeamMembers(role.getRoleID()).size();
			if (teamSize < 5){
//				XiaKeColumn xiaKeColumn = XiaKeColumn.getXiaKeColumn(role.getRoleID(), true);
//				int size = xiaKeColumn.getFightXiaKes().size();
				if (role.getHuobanSize() > 0) 
					teamSize = teamSize+role.getHuobanSize();
			}
			if (teamSize > 5)
				teamSize = 5;
			//不遇怪buff,分两种,一种是无视地图等级的,一种是要看地图等级的
			if (agent.existBuff(FireProp.getIntValue("battle", "mtgserv.nobattleBuffid"))) 
				return;
			if(agent.existBuff(SpecialItem.STUPOR_BUFF_ID)){
				//判断场景等级
				SMineArea areaInfo = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.SMineArea.class).get(conf);
				if(null == areaInfo)
					return;
				
				int areaLevel = areaInfo.getLevel();
				int roleLevel = xtable.Properties.selectLevel(role.getRoleID());
				
				if(areaLevel <= roleLevel){
					return ;
					
				}
				
			}
			
			role.setRoadLen(role.getRoadLen() + newRoadLen);
//			if (printHideBattleSteps && printHideBattleRoleid ==role.getRoleID()) 
//			Message.sendPopByRoleID(role.getRoleID(), "步数增加,现在步数:"+role.getRoadLen()/16/3);
			if(checkEnterHideBattle(role.getRoadLen()/16/3))
			{   
//				if (printHideBattleSteps && printHideBattleRoleid == role.getRoleID()) 
//				Message.sendPopByRoleID(role.getRoleID(), "遭遇暗雷战斗!距上次遇暗雷已行走的步数:"+role.getRoadLen()/16/3);
				new HideBattle(role.getRoleID(), conf, teamSize ).launch();
				role.setRoadLen(0);
//				if (printHideBattleSteps && printHideBattleRoleid == role.getRoleID()) 
//				Message.sendPopByRoleID(role.getRoleID(), "步数清0,现在步数:"+role.getRoadLen()/16/3);
				role.setLastHideBattleTick(curTick);
			}
		}
	}
	
	/**
	 * 师门巡逻,帮派日常任务的平定内患任务 .是否可以拉入战斗,如果可以拉如战斗，则拉入.
	 * @param role
	 * @param newRoadLen
	 * @return
	 */
	public static boolean checkSpecialPatrol(/*RoleHideBattleInfo role,*/Role role, int newRoadLen){
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(role.getRoleID());
		if (null != team){
			if (!team.getAbsentMemberIds().contains(role.getRoleID()) && team.getTeamLeaderId() != role.getRoleID())
				return false;
		}
		//final boolean isFDQPatrol = false;
		//FactionDayQuest.isFDQPatroling(role.getRoleID());
		//final boolean isCTSchoolPatrol = SpecialQuest.isCTSchoolPatrol(role.getRoleID());
		final boolean isCTPatrol = CircleTask.isHavePatrol(role.getRoleID()) > 0 ? true : false;
		final int isAnYePatrol = RoleAnYeTask.isHavePatrol(role.getRoleID());
		final int ScenarioPatrolQid = RoleMission.getScenarioPatroling(role.getRoleID());
		
		boolean flag = false;
		if (!isCTPatrol && ScenarioPatrolQid <= 0 && isAnYePatrol < 0){
			role.setPatrolStep(0);
			flag = false;
		}
		else{
			if(isAnYePatrol >= 0){
				if(!RoleAnYeTask.getPatrolMap(role.getRoleID()).contains(role.getMapId()))
					flag = false;
				else{
					role.setPatrolStep(role.getPatrolStep() + newRoadLen);
					if (role.getPatrolStep() < MoveUnit.PERSTEP_PIX * PropConf.Specialquest.PATROL_MINSTEP)
						flag = false;
					else if (role.getPatrolStep() >= MoveUnit.PERSTEP_PIX * PropConf.Specialquest.PATROL_MAXSTEP){
						final EnterAnYePatrolBattle enter = new EnterAnYePatrolBattle(role.getRoleID(), role.getMapId());
						enter.enterBattle();
						role.setPatrolStep(0);
						flag = true;
					}
					else if (Misc.checkRatePercent(PropConf.Specialquest.PATROL_BATTL)){
						final EnterAnYePatrolBattle enter = new EnterAnYePatrolBattle(role.getRoleID(), role.getMapId());
						enter.enterBattle();
						role.setPatrolStep(0);
						flag = true;
					}
					SceneManager.logger.debug("暗夜马雷巡逻  玩家当前步数" + role.getPatrolStep() +"newRoadLen" + newRoadLen);
				}
			}
			if(isCTPatrol){
				if (!CircleTask.getCTPatrolMap(role.getRoleID()).contains(role.getMapId()))
					flag = false;
				else{
					role.setPatrolStep(role.getPatrolStep() + newRoadLen);
					if (role.getPatrolStep() < MoveUnit.PERSTEP_PIX * PropConf.Specialquest.PATROL_MINSTEP)
						flag = false;
					else if (role.getPatrolStep() >= MoveUnit.PERSTEP_PIX * PropConf.Specialquest.PATROL_MAXSTEP){
						final EnterCircTaskPatrolBattle enter = new EnterCircTaskPatrolBattle(role.getRoleID(), role.getMapId());
						enter.enterBattle();
						role.setPatrolStep(0);
						flag = true;
					}
					else if (Misc.checkRatePercent(PropConf.Specialquest.PATROL_BATTL)){
						//final EnterCircTaskSchoolPatrolBattle enter = new EnterCircTaskSchoolPatrolBattle(role.getRoleID());
						final EnterCircTaskPatrolBattle enter = new EnterCircTaskPatrolBattle(role.getRoleID(), role.getMapId());
						enter.enterBattle();
						role.setPatrolStep(0);
						flag = true;
					}
					SceneManager.logger.debug("循环任务师门巡逻  玩家当前步数" + role.getPatrolStep() +"newRoadLen" + newRoadLen);
				}
			}
			if(ScenarioPatrolQid > 0) {
				final MissionConfig conf = MissionManager.getInstance().getMissionConfig( ScenarioPatrolQid );
				if (conf == null)
					flag = false;
				else {
					if (role.getMapId() != conf.exeIndo.mapID/*配置文件获取巡逻地图id*/)
						flag = false;
					else {
						role.setPatrolStep(role.getPatrolStep() + newRoadLen);
						SceneManager.logger.debug("剧情任务巡逻护送  玩家当前步数:" + role.getPatrolStep() +",newRoadLen:" + newRoadLen);
						if (role.getPatrolStep() < MoveUnit.PERSTEP_PIX * PropConf.Specialquest.PATROL_MINSTEP)
							flag = false;
						else if (role.getPatrolStep() >= MoveUnit.PERSTEP_PIX * PropConf.Specialquest.PATROL_MAXSTEP){
							final EnterScenarioPatrolBattle enter = new EnterScenarioPatrolBattle(role.getRoleID());
							enter.enterBattle();
							role.setPatrolStep(0);
							flag = true;
						}
						else if (Misc.checkRatePercent(PropConf.Specialquest.PATROL_BATTL)){
							final EnterScenarioPatrolBattle enter = new EnterScenarioPatrolBattle(role.getRoleID());
							enter.enterBattle();
							role.setPatrolStep(0);
							flag = true;
						}				
					}
				}
			}
		}
		return flag;
	}
	/**
	 * 检测进入暗雷战斗的条件是否满足
	 * 
	 * @param role
	 * @return
	 */
	public static boolean checkEnterHideBattle(final int roadLen)
	{

		if (roadLen < PropConf.Battle.STEPLEN_NOBATTLE)
			return false;
		else if (roadLen >= PropConf.Battle.STEPLEN_BATTLE)
		{
			return true;
		}

		// logger.debug("百分之五的触怪");
		return Misc.checkRate(10000, 400+(roadLen-5)*(30+(roadLen-5)/5*10));
	}
	
	public static final int MIN_BATTLE_INTERVAL_NOTEAM = 20000;		// 单角色战斗每场战斗最小持续时间
	public static final int MIN_BATTLE_INTERVAL_TEAM = 30000;		// 组队战斗每场战斗最小的持续时间
}
