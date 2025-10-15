package fire.pb.move;

import fire.msp.move.MMoveCheckHideBattle;
import fire.pb.scene.SceneClient;
import fire.pb.scene.manager.MonstershowManager;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.movable.Role;
import fire.pb.scene.movable.SceneTeam;


/**
 * 地图中的战斗处理
 *
 */
public class SceneBattle {
	/**
	 * 检测场景是否为战斗场景
	 * @param scene
	 * @return
	 */
	/*
	private static boolean checkBattleScene(fire.pb.scene.Scene scene){
		
		return null != scene.getMapInfo().getMonsterInfo();
	}*/
	
	/**
	 * 检测战斗单位的合法性
	 * @param role	
	 * @return	可进入战斗的战斗单位/否
	 */
	/*
	private static boolean checkBattleUnit(Role role, long curTick){
		
		final int ts = SceneTeamManager.getInstance().getTeamState(role.getRoleID());
		if (!role.isVisible()) 
		    return false;
		if(SceneTeam.TEAM_NORMAL == ts )
			return false;
		
		else if(SceneTeam.TEAM_NONE == ts || SceneTeam.TEAM_APART == ts){
			if(curTick - role.getLastBattleTick() < MIN_BATTLE_INTERVAL_NOTEAM)
				return false;
		}
		
		else if(SceneTeam.TEAM_LEADER == ts){
			if(curTick - role.getLastBattleTick() < MIN_BATTLE_INTERVAL_TEAM)
				return false;
		}
		
		//如果正在执行结婚任务的情比金坚阶段，不遇明雷暗雷
		MarryTaskInfo marryInfo = MarryTaskStepManager.getMarryTaskManager().getCurrentRoleMarryInfo(role.getRoleID());
		if(null != marryInfo){
			if(marryInfo.getTaskStep() == Constant.QING_BI_JIN_JIAN || marryInfo.getTaskStep() == Constant.COMMIT_QING_BI_JIN_JIAN){
				return false;
			}
		}
		return true;
	}
	*/
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
	/*
	@SuppressWarnings("unchecked")
	public static void process(Role role, int newRoadLen, long curTick){
		if(! checkBattleScene(role.scene))
			return;
		if(! checkBattleUnit(role, curTick))
			return;
	
		final int conf = role.getMonsterConfig();
		if(0 == conf)
			return;
		
		final int teamSize = SceneTeamManager.getInstance().getNormalTeamMembers(role.getRoleID()).size() + 1;
		if(conf > 0)			// 暗雷战斗
		{
			
			final fire.pb.buff.BuffAgent agent = new fire.pb.buff.BuffRoleImpl(
					role.getRoleID(),true);
			//不遇怪buff,分两种,一种是无视地图等级的,一种是要看地图等级的
			if (agent.existBuff(FireProp.getIntValue("battle", "mtgserv.nobattleBuffid"))) 
				return;
			if(agent.existBuff(SpecialItem.STUPOR_BUFF_ID)){
				//判断场景等级
				SMineArea areaInfo = ((Map<Integer,SMineArea>)fire.pb.main.ConfigManager.getInstance().getConf("fire.pb.map.SMineArea")).get(conf);
				if(null == areaInfo)
					return;
				
				int areaLevel = areaInfo.getLevel();
				int roleLevel = role.getLevel();
				
				if(areaLevel <= roleLevel-5){
					return ;
					
				}
				
			}
			
			//正在执行家族特产任务的buff
			if(agent.existBuff(BuffConstant.CONTINUAL_SPECIAL_PRODUCT_TASK)){
				return ;
			}
			
			role.setRoadLen(role.getRoadLen() + newRoadLen);
			if(checkEnterHideBattle(role.getRoadLen()/16))
			{
				new fire.pb.mission.TaskHideBattle(role.getRoleID(), conf, teamSize ).launch();
				role.setRoadLen(0);
				role.setLastBattleTick(curTick);
			}
		}
		else				// 明雷战斗
		{
			Npc monster = checkEnterShowBattle(role);
			if( monster != null )
			{
			//	new ShowBattle(role.getRoleID(), teamSize, monster, Math.abs( conf ) ).launch();
				
				fire.msp.battle.MStartShowNpcBattle snd = new fire.msp.battle.MStartShowNpcBattle();
				snd.roleid = role.getRoleID();
				snd.teamsize = teamSize;
				snd.npcuniqueid = monster.getUniqueID();
				snd.areaconf = Math.abs( conf );
				snd.npcbaseid = monster.getNpcID();
				SceneClient.pSend(snd);
				
			}
		}
	}*/
	/**
	 * 暗雷和明雷战斗过程处理
	 * 明雷直接判断，明雷不成功则发到GS逻辑去判断
	 * @param role			角色
	 * @param newRoadLen	当前行走的路程
	 * @param curTick		当前时刻
	 */
	public static void process(Role role, int newRoadLen, long curTick)
	{

		if (!role.checkVisible()) return;
		if (role.isJumping()) return;
		
		final int ts = role.getTeamState();
		if(SceneTeam.TEAM_NORMAL == ts )		//正常队员不会进战斗
			return;
		final int conf = role.getMonsterConfig();
		//Scene.logger.debug("move.SceneBattle当前怪配置:" + conf);
		final int teamidx = role.getTeamIndex();
		final int teamSize;
		if (teamidx == 0 || teamidx == -1) {
			teamSize = 1;
		} else
			teamSize = role.getRoleSize();
		if(conf < 0)
		{
			//System.out.println( "检查明雷战斗" );
			NPC monster = checkEnterShowBattle(role);
			if( monster != null )
			{
			//	new ShowBattle(role.getRoleID(), teamSize, monster, Math.abs( conf ) ).launch();
				
				fire.msp.battle.MStartShowNpcBattle snd = new fire.msp.battle.MStartShowNpcBattle();
				snd.roleid = role.getRoleID();
				snd.teamsize = teamSize;
				snd.npcuniqueid = monster.getUniqueID();
				snd.areaconf = Math.abs( conf );
				snd.npcbaseid = monster.getNpcID();
				SceneClient.pSend(snd);
				return;
			}
		}
		
		//如果没进入明雷战斗，则发给逻辑判断
		SceneClient.pSend(new MMoveCheckHideBattle(role.getRoleID(), role.getScene().getSceneID(),  role.getScene().getOwnerID(), newRoadLen, conf));
	}
	
	public static NPC checkEnterShowBattle(Role role){
		// 检查附近的明雷怪
		return MonstershowManager.getInstance().getMonsterFightInFuture(role);
	}
	/**
	 * 师门巡逻,帮派日常任务的平定内患任务 .是否可以拉入战斗,如果可以拉如战斗，则拉入.
	 * @param role
	 * @param newRoadLen
	 * @return
	 */
	/*
	public static boolean checkSpecialPatrol(Role role, int newRoadLen){
		final boolean isSchoolPatrol = SpecialQuest.isPatroling(role.getRoleID());
		final boolean isFDQPatrol = FactionDayQuest.isFDQPatroling(role.getRoleID());
		boolean flag = false;
		if (!isSchoolPatrol && !isFDQPatrol){
			role.setPatrolStep(0);
			flag = false;
		}
		else{
			final fire.pb.PropRole prole = new fire.pb.PropRole(role.getRoleID(), true);
			if (isSchoolPatrol){
				if (role.scene.getMapID() != SpecialQuestManager.getInstance().getSchoolMapID(prole.getSchool()))
					flag = false;
				else{
					role.setPatrolStep(role.getPatrolStep() + newRoadLen);
					if (role.getPatrolStep() < MoveCheck.PERSTEP_PIX * FireProp.getIntValue(SpecialQuestManager.sqprop, "fire.pb.specialquest.patrolminstep"))
						flag = false;
					else if (role.getPatrolStep() >= MoveCheck.PERSTEP_PIX * FireProp.getIntValue(SpecialQuestManager.sqprop, "fire.pb.specialquest.patrolmaxstep")){
						final EnterSchoolPatrolBattle enter = new EnterSchoolPatrolBattle(role.getRoleID());
						enter.enterBattle();
						role.setPatrolStep(0);
						flag = true;
					}
					else if (Misc.checkRatePercent(FireProp.getIntValue(SpecialQuestManager.sqprop, "fire.pb.specialquest.patrolbattle"))){
						final EnterSchoolPatrolBattle enter = new EnterSchoolPatrolBattle(role.getRoleID());
						enter.enterBattle();
						role.setPatrolStep(0);
						flag = true;
					}
					Scene.logger.debug("宠物巡逻  玩家当前步数" + role.getPatrolStep() +"newRoadLen" + newRoadLen);
				}
			}
			else if (isFDQPatrol){
				if (!FactionExecutor.isYourselfFactionDynamicScene(role.getRoleID()))
					return false;
				role.setPatrolStep(role.getPatrolStep() + newRoadLen);
				if (role.getPatrolStep() <=  MoveCheck.PERSTEP_PIX * FireProp.getIntValue(SpecialQuestManager.sqprop, "fire.pb.specialquest.PatrolNoBattle"))
					flag = false;
				else{
					if (Misc.checkRatePercent(FireProp.getIntValue(SpecialQuestManager.sqprop, "fire.pb.specialquest.PatrolBattleRate"))){
						fire.pb.specialquest.EnterSpecialQuestBattle enter = new fire.pb.specialquest.EnterSpecialQuestBattle(role.getRoleID(), 0, SpecialQuestID.factiondailyquestid, SpecialQuestType.FactionCatchJianXi);
						enter.enterBattle();
						role.setPatrolStep(0);
						flag = true;
					}
					else{
						if (role.getRoadLen() >=  MoveCheck.PERSTEP_PIX * FireProp.getIntValue(SpecialQuestManager.sqprop, "fire.pb.specialquest.FDQPatrolBattle")){
							fire.pb.specialquest.EnterSpecialQuestBattle enter = new fire.pb.specialquest.EnterSpecialQuestBattle(role.getRoleID(), 0, SpecialQuestID.factiondailyquestid, SpecialQuestType.FactionCatchJianXi);
							enter.enterBattle();
							role.setPatrolStep(0);
							flag = true;
						}
					}
					// 按策划和测试要求，打印给相关人员测试用。
					// 暂时去掉
				//	fire.pb.talk.Message.getInstance().sendMsgScreen(role.getRoleID(), "帮派日常巡逻 玩家当前步数" + role.getPatrolStep() +"newRoadLen" + newRoadLen,  new java.util.ArrayList<Octets>(), new java.util.ArrayList<ShowInfo>());
				}
			}		
			
		}
		return flag;
	}*/
	/**
	 * 检测进入暗雷战斗的条件是否满足
	 * 
	 * @param role
	 * @return
	 */
	/*
	public static boolean checkEnterHideBattle(final int roadLen)
	{

		if (roadLen < STEPLEN_NOBATTLE)
			return false;
		else if (roadLen >= STEPLEN_BATTLE)
		{
			return true;
		}

		// logger.debug("百分之五的触怪");
		return Misc.checkRate(10000, 300+(roadLen-10)*20);
	}
	static Properties prop = ConfigManager.getInstance().getPropConf("battle");
	private static final int STEPLEN_BATTLE = FireProp.getIntValue(prop, "mtgserv.steplen_battle"); // 遇怪的最大步数

	private static final int STEPLEN_NOBATTLE = FireProp.getIntValue(prop, "mtgserv.steplen_nobattle"); // 战斗结束后不遇怪的路程
	
	public static final int MIN_BATTLE_INTERVAL_NOTEAM = 20000;		// 单角色战斗每场战斗最小持续时间
	public static final int MIN_BATTLE_INTERVAL_TEAM = 30000;		// 组队战斗每场战斗最小的持续时间
	*/
}
