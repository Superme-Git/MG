package fire.pb.circletask.catchit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.PAddExpProc;
import fire.pb.RoleBaseInfo;
import fire.pb.RoleConfigManager;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RolledAwardItem;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.ResultType;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircleTask;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.UpdateCircleTaskState;
import fire.pb.common.SCommon;
import fire.pb.course.CourseManager;
import fire.pb.course.CourseType;
import fire.pb.effect.Role;
import fire.pb.effect.RoleImpl;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Lockeys;
import mkdb.Procedure;

public class CatchItTaskEndHandler extends BasicEndHandler {
	
	private final long npckey;
	private final long roleid;
	private final int circletype;
	private final int teamnum;
	private final int teamshare;
	
	public CatchItTaskEndHandler(final long npckey, final long roleid, final int circletype, final int teamnum, final int teamshare) {
		this.npckey = npckey;
		this.roleid = roleid;
		this.circletype = circletype;
		this.teamnum = teamnum;
		this.teamshare = teamshare;
	}
	
	@Override
	protected boolean handleAward() {
		//因需要删除地图上刷出来的明雷怪,所以在这里特殊处理奖励
		fire.pb.map.SceneNpcManager.removeNpc(npckey);
		Team team = TeamManager.getTeamByRoleId(roleid);
		
		List<Long> roles = new ArrayList<Long>();
		if (teamshare == 1) {
			if (team != null) {
				roles.addAll(team.getNormalMemberIds());
				Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roles));
			}
			else
				return false;
		}
		else {
			roles.add(roleid);
		}
		
		CatchItQuest.cancelCatchItQuestTimer(roleid, circletype);
		
		xbean.CircleTaskMap questMap = xtable.Rolecircletask.get(roleid);
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
		
		if (ct.teamnum > 0) {
			if (team == null)
				return false;
		}
		
		if (teamnum > 0) {
			//给队长奖励
			CircleTask.giveTeamLeaderAward(roleid, questinfo, ct, circletype);
		}
		
		fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		if (teamshare == 1) {
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
				if (baseInfo.roleid == roleid) {
					paras.put(RewardMgr.IS_LEADER, 1);
				}
				else {
					paras.put(RewardMgr.IS_LEADER, 0);
				}
				
				paras.put(RewardMgr.SWXS, baseInfo.SwXs);
				if (baseInfo.roleid == roleid) {
					paras.put(RewardMgr.IS_LEADER, 1);
				}
				else {
					paras.put(RewardMgr.IS_LEADER, 0);
				}
				paras.put(RewardMgr.TEAM_LEVEL, roleLevelSum);
				paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
				
				CircleTask.giveCTAward(baseInfo.roleid, roleLevelSum, circletype, npc, info, paras, false);
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
				paras.put(RewardMgr.TEAM_LEVEL, roleLevelSum);
				paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
				for (int i = 0; i < times; i++) {
					RewardMgr.getInstance().distributeAllAward(roles.get(0),
							awardLevel, paras,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guiwang,
							battle.getAreaconf(), PAddExpProc.BATTLE_AWARD,
							"鬼王", true, true, rollawarditems, false);
				}

				if (rollawarditems.isEmpty()) {
					// 刷新任务状态，开启下一环捉鬼
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
				paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
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
			CircleTask.giveCTAward(roleid, roleLevel, circletype, npc, questinfo, null, false);
			RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
			if (actrole != null && ct.getActiveid() > 0) {
				actrole.handleActiveLivenessData(ct.getActiveid());
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
		handlePetFighters();
		for (xbean.Fighter f : getDeadRoles()) {
			long roleid = f.getUniqueid();
			Role role = new RoleImpl(roleid);
			// Map<Integer, Float> result = role.fullHpAndRecoverWound();
			Map<Integer, Float> result1 = role.fullHpAndRecoverWound();
			Map<Integer, Float> result2 = role.fullMp();
			
			SRefreshRoleData sr = new SRefreshRoleData();
			sr.datas.putAll(result1);
			sr.datas.putAll(result2);
			Procedure.psendWhileCommit(roleid, sr);
		}
		fire.pb.map.Npc npc = fire.pb.map.SceneNpcManager.getNpcByKey(npckey);
		if (npc == null)
			return false;
		
		xbean.Fighter fightLeader = battle.getFighters().get(1);
		final Integer val = battle.getRoundresult().get(1);
		if (val != null) {
			if ((val & ResultType.RESULT_DEATH) != 0 || (val & ResultType.RESULT_RUNAWAY) !=0) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(fightLeader.getUniqueid(), 100124, npc.getNpcID(), null);
			}
		}
		return true;
	}
}
