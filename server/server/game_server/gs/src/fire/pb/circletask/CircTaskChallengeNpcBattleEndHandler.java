package fire.pb.circletask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mkdb.Lockeys;
import fire.pb.PAddExpProc;
import fire.pb.RoleBaseInfo;
import fire.pb.RoleConfigManager;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RolledAwardItem;
import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.Fighter;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.common.SCommon;
import fire.pb.course.CourseManager;
import fire.pb.course.CourseType;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.activelist.RoleLiveness;

public class CircTaskChallengeNpcBattleEndHandler extends BasicEndHandler{
	private int circletype;
	private final long capitalid;
	private final long npckey;
	public CircTaskChallengeNpcBattleEndHandler(int circletype, final long capitalid, final long npckey){
		super();
		this.circletype = circletype;
		this.capitalid = capitalid;
		this.npckey = npckey;
	}
	
	@Override
	public boolean handleAward(){
		fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByRoleId(capitalid);
		List<Long> roles = new ArrayList<Long>();
		if (team != null) {
			roles.addAll(team.getNormalMemberIds());
			Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roles));
		}
		else {
			roles.add(capitalid);
		}
		
		xbean.CircleTaskMap questMap = xtable.Rolecircletask.get(capitalid);
		if (null == questMap) {
			return false;
		}
		
		xbean.CircleTaskInfo questinfo = questMap.getTaskmap().get(circletype);
		if (null == questinfo) {
			return false;
		}
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(questinfo.getId());
		if (ct == null) {
			return false;
		}
		
		long roleid = 0;
		for (int fighterId : battle.getFighters().keySet()) {
			if (BattleField.checkLiveInBattle(battle, fighterId)) {
				Fighter fighter = battle.getFighterobjects().get(fighterId);
				if (fighter == null)
					continue;

				long numberid = fighter.getFighterBean().getUniqueid();
				if (ct.teamnum > 0) {
					if (team == null)
						return false;
					roleid = team.getTeamLeaderId();
				} else {
					roleid = numberid;
				}
				break;
			}
		}
		
		if (ct.teamnum > 0) {
			//给队长奖励
			CircleTask.giveTeamLeaderAward(capitalid, questinfo, ct, circletype);
		}
		
		fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		if (ct.teamshare == 1) {
			SCommon cc = RoleConfigManager.getRoleCommonConfig(346);
		 	int shenwang1 = (cc == null ? 1 : Integer.parseInt(cc.getValue()));
		 	cc = RoleConfigManager.getRoleCommonConfig(347);
		 	int shenwang2 = (cc == null ? 2 : Integer.parseInt(cc.getValue()));

			int roleLevelSum = 0;
			List<RoleBaseInfo> roleList = new ArrayList<RoleBaseInfo>(5);
			for (Long rId : roles) {
				final int roleLevel = xtable.Properties.selectLevel(rId);
				roleLevelSum += roleLevel;
				RoleBaseInfo baseInfo = new RoleBaseInfo(rId, roleLevel);
				roleList.add(baseInfo);
				
				int num = 0;
				for (Long rIdy : roles) {
					if (rIdy == rId)
						continue;
					
					final int roleLevely = xtable.Properties.selectLevel(rIdy);
					if ((roleLevel - roleLevely) > 9) { //大于等于10级
						num ++;
					}
				}
				
				if (num > 1) {
					baseInfo.setSwXs(shenwang2);
				}
				else if (num > 0) {
					baseInfo.setSwXs(shenwang1);
				}
				else {
					baseInfo.setSwXs(0);
				}
			}
			roleLevelSum /= roles.size();
			for (RoleBaseInfo baseInfo : roleList) {
				xbean.CircleTaskMap quest = xtable.Rolecircletask.get(baseInfo.roleid);
				if (null == quest) {
					continue;
				}
				
				xbean.CircleTaskInfo info = quest.getTaskmap().get(circletype);
				if (null == info) {
					continue;
				}
				
				//给奖励
				Map<String, Object> paras = new HashMap<String, Object>(20);
				paras.put(RewardMgr.SWXS, baseInfo.SwXs);
				if (capitalid == baseInfo.roleid) {
					paras.put(RewardMgr.IS_LEADER, 1);
				}
				else {
					paras.put(RewardMgr.IS_LEADER, 0);
				}
				paras.put(RewardMgr.TEAM_LEVEL, roleLevelSum);
				paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
				
				CircleTask.giveCTAward(baseInfo.roleid, roleLevelSum, circletype, npc, info, paras, false);
				
				if (circletype == 1010000) {
					if (ct.getTaskcnt() == 2) {
						info.setSumnum(info.getSumnum() + 1);
						int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ct);
						if (info.getSumnum() >= circleMax) {
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ct.getMaxnumtips(), null);
						}
					}
				}
				
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(baseInfo.roleid);
				if (actrole != null && ct.getActiveid() > 0){
					actrole.handleActiveLivenessData(ct.getActiveid());
				}
				
				CourseManager.achieveCourse(baseInfo.roleid, CourseType.ACTIVE_COURSE, ct.getActiveid(), 0);
			}
			
			int awardLevel = CircleTaskManager.getInstance().getCircTaskPeizi(questinfo.getQuesttype(), roleLevelSum);
			if (awardLevel != -1) {
				int times = RewardMgr.getInstance().getTimes(awardLevel);
				ArrayList<RolledAwardItem> rollawarditems = new ArrayList<RolledAwardItem>();

				Map<String, Object> paras = new HashMap<String, Object>(20);
				if (battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL) != null) {
					paras.put("MonsterLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL));
				}
				if (battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM) != null) {
					paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
				}
				paras.put(RewardMgr.TEAM_LEVEL, roleLevelSum);
				
				for (int i = 0; i < times; i++) {
					RewardMgr.getInstance().distributeAllAward(roles.get(0),
							awardLevel, paras,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_richang,
							battle.getAreaconf(), PAddExpProc.BATTLE_AWARD,
							"鬼王", true, true, rollawarditems, false);
				}

				if (rollawarditems.isEmpty()) {
					mkdb.Procedure.pexecuteWhileCommit(new UpdateCircleTaskState(roleid, circletype, SpecialQuestState.DONE));
					return true;
				}

				setNeedRoll(true);
//				new fire.pb.team.teammelon.PTeamRollMelon(rollawarditems, roles, battle.getBattleid()).call();
				new fire.pb.team.teammelon.PTeamRollMelon(rollawarditems, roles, roleid, 1, circletype, battle.getBattleid(), null).call();
				return true;
			}
			else if (ct.getShareawardids() != null) {
				//发分级奖励
				int awardid = CircleTaskManager.getInstance().getCircTaskShareZong(questinfo.getId(), roleLevelSum);
				
				int times = RewardMgr.getInstance().getTimes(awardid);
				ArrayList<RolledAwardItem> rollawarditems = new ArrayList<RolledAwardItem>();
				Map<String, Object> paras = new HashMap<String, Object>(20);
				paras.put(RewardMgr.TEAM_LEVEL, roleLevelSum);
				if (battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM) != null) {
					paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
				}
				for (int i = 0; i < times; i++) {
					RewardMgr.getInstance().distributeAllAward(roles.get(0),
							awardid, paras,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_richang,
							awardid, PAddExpProc.BATTLE_AWARD, "战斗", true,
							true, rollawarditems, false);
				}

				if (rollawarditems.isEmpty()) {
					// 刷新任务状态，开启下一环捉鬼
					mkdb.Procedure.pexecuteWhileCommit(new UpdateCircleTaskState(roleid, circletype, SpecialQuestState.DONE));
					return true;
				}
				setNeedRoll(true);
				if (team != null) 
					new fire.pb.team.teammelon.PTeamRollMelon(rollawarditems, roles, roleid, 1, circletype, battle.getBattleid(), null).call();
			}
			else {
				mkdb.Procedure.pexecuteWhileCommit(new UpdateCircleTaskState(roleid, circletype, SpecialQuestState.DONE));
				return true;
			}
		}
		else {
			//给奖励
			final int roleLevel = xtable.Properties.selectLevel(roleid);
			Map<String, Object> paras = new HashMap<String, Object>(20);
			if (battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM) != null) {
				paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
			}
			
			xbean.CircleTaskMap quest = xtable.Rolecircletask.get(roleid);
			if (null == quest) {
				return false;
			}
			xbean.CircleTaskInfo info = quest.getTaskmap().get(circletype);
			if (null == info) {
				return false;
			}
			
			CircleTask.giveCTAward(roleid, roleLevel, circletype, npc, questinfo, null, false);
			RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
			if (actrole != null && ct.getActiveid() > 0){
				actrole.handleActiveLivenessData(ct.getActiveid());
			}
			
			if (circletype == 1010000) {
				info.setSumnum(info.getSumnum() + 1);
				int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ct);
				if (info.getSumnum() >= circleMax) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ct.getMaxnumtips(), null);
				}
			}
			
			//刷新任务状态，开启下一环捉鬼
			mkdb.Procedure.pexecuteWhileCommit(new UpdateCircleTaskState(roleid, circletype, SpecialQuestState.DONE));
			CourseManager.achieveCourse(roleid, CourseType.ACTIVE_COURSE, ct.getActiveid(), 0);
		}
		
		return true;
	}
	
	@Override
	public boolean handleDeath() {
		super.handleDeath();
		CircleTaskManager.logger.debug("[" + capitalid + "]" + "CircTaskChallengeNpcBattleEndHandler!");
		//xbean.Fighter fightLeader = battle.getFighters().get(1);
//		java.util.List<xbean.Fighter> roleids;
//		roleids = super.getWinRoles();
//		SpecialQuestManager.logger.debug("[" + capitalid + "]" + "WinRoles:" + roleids.size());
//		for (xbean.Fighter xfighter : roleids) {
//			if(xfighter.getUniqueid() == this.capitalid)
//			{
//				//SpecialQuest sq = new SpecialQuest(fightLeader.getUniqueid(), false);
//				//sq.stepState(circletype, SpecialQuestState.DONE);
//				new StepStateProc(this.capitalid, circletype, SpecialQuestState.DONE).call();
//				break;
//			}
//		}
		return true;
	}
}