package fire.pb.activity.timernpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mkdb.Lockeys;
import fire.msp.role.GSetNpcBattleInfo;
import fire.pb.GsClient;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RolledAwardItem;
import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.mission.SWaitRollTime;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.util.DateValidate;

public class TimerNpcBattleEndHandler extends BasicEndHandler {

	private final int actId;
	private final int npcId;
	private final long npcKey;
	private final long roleId;
	
	public TimerNpcBattleEndHandler(final int actId, final int npcId, final long npcKey, final long roleid){
		this.actId = actId;
		this.npcId = npcId;
		this.npcKey = npcKey;
		this.roleId = roleid;
	}
	
	@Override
	public boolean handleDeath() {
		super.handleDeath();
		TimerNpcData timerData = TimerNpcService.getInstance().getNpcData(actId);
		if (timerData == null)
			return false;
		
		if (timerData.match == 1)
			xtable.Npcbattleinfo.remove(npcKey);
		
		//赢了清空怪物，输了重置怪的状态
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actId);
		if (npcInfo == null)
			return true;
		
		xbean.timerNpcInfo tInfo = npcInfo.getNpcinfo().get(npcKey);
		if (tInfo == null)
			return true;
		
		if (battle.getBattleresult() == BattleField.BATTLE_WIN) {
			if (timerData.battletime > 1) {
				tInfo.setBattleendtime(tInfo.getBattleendtime() + 1);
				if (tInfo.getBattleendtime() >= timerData.battletime)
					TimerNpcService.getInstance().removeMonster(npcInfo, npcKey);
			}
			else
				TimerNpcService.getInstance().removeMonster(npcInfo, npcKey);
		}
		else {
			tInfo.setBattletime(tInfo.getBattletime() - 1);
			tInfo.setNpcstatus(0);
			GsClient.pSendWhileCommit(new GSetNpcBattleInfo(npcKey, 0));
		}
		
		return true;
	}
	
	@Override
	protected boolean handleAward() {
		TimerNpcData timerData = TimerNpcService.getInstance().getNpcData(actId);
		if (timerData == null)
			return false;
		
		ActivityInfo activeInfo = TimerNpcService.getInstance().getActiveInfo(actId);
		if (activeInfo == null)
			return false;
		int awardcnt = timerData.awardCnt;
		
		List<Long> roleIds = new ArrayList<Long>(5);
		roleIds.addAll(battle.getRoleids().keySet());
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));
		
		int matchLevel = 0;
		if (timerData.matchlevel == 2) {
			for (Long rid : roleIds) {
				final int roleLevel = xtable.Properties.selectLevel(rid);
				matchLevel += roleLevel;
			}
			
			matchLevel /= roleIds.size();
		}
		
		List<Long> rollAwardIds = new ArrayList<Long>(5); //发分级奖励的roleid
		
		int awardId = TimerNpcService.getInstance().getAwardId(actId, npcId);
		if (awardId != -1) {
			long now = System.currentTimeMillis();
			if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
				for (Long rId : roleIds) {
					xbean.RoleActiveTimerNpcInfo roleActiveInfo = xtable.Roletimernpcinfos.get(rId);
					if (roleActiveInfo == null) {
						roleActiveInfo = xbean.Pod.newRoleActiveTimerNpcInfo();
						xtable.Roletimernpcinfos.insert(rId, roleActiveInfo);
					}
					
					xbean.RoleTimerNpcInfo roleTimerNpcActiveInfo = roleActiveInfo.getActinfo().get(activeInfo.getBaseId());
					if (roleTimerNpcActiveInfo == null) {
						roleTimerNpcActiveInfo = xbean.Pod.newRoleTimerNpcInfo();
						
						roleTimerNpcActiveInfo.setAwardtimes(0);
						roleTimerNpcActiveInfo.setLastawardtime(now);
						
						roleActiveInfo.getActinfo().put(activeInfo.getBaseId(), roleTimerNpcActiveInfo);
					}
					
					if (!DateValidate.inTheSameDay(now,	roleTimerNpcActiveInfo.getLastawardtime())) {
						roleTimerNpcActiveInfo.setAwardtimes(0);
						roleTimerNpcActiveInfo.setLastawardtime(now);
					}
					//高于10次不能获取奖励
					if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
						fire.pb.talk.MessageMgr.sendMsgNotify(rId, timerData.awardOverStepNotice, null);
						continue;
					}
					
					awardToRole(rId, awardId);
					
					roleTimerNpcActiveInfo.setAwardtimes(roleTimerNpcActiveInfo.getAwardtimes() + 1);
					roleTimerNpcActiveInfo.setLastawardtime(now);

					//baseid 对应活动配置表新 里的序号
					RoleLiveness actrole = RoleLiveness.getRoleLiveness(rId);
					if (actrole != null)
						actrole.handleActiveLivenessData(timerData.actid);
					
					rollAwardIds.add(rId);
					
					if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
						fire.pb.talk.MessageMgr.sendMsgNotify(rId, timerData.awardOverStepNotice, null);
					}
					
					fire.pb.course.CourseManager.achieveCourse(rId, fire.pb.course.CourseType.ACTIVE_COURSE, activeInfo.getBaseId(), 0);
				}
			}
			else { //点卡服处理狩猎活动
				if (timerData.shoulie == 0) { //不是狩猎活动
					for (Long rId : roleIds) {
						//分级次数
						int rolelevel = xtable.Properties.selectLevel(rId);
						TimesLevel timeLevel = timerData.getTimesLevel(rolelevel);
						if (timeLevel != null) {
							awardcnt = timeLevel.times;
						}
						xbean.RoleActiveTimerNpcInfo roleActiveInfo = xtable.Roletimernpcinfos.get(rId);
						if (roleActiveInfo == null) {
							roleActiveInfo = xbean.Pod.newRoleActiveTimerNpcInfo();
							xtable.Roletimernpcinfos.insert(rId, roleActiveInfo);
						}
						
						xbean.RoleTimerNpcInfo roleTimerNpcActiveInfo = roleActiveInfo.getActinfo().get(activeInfo.getBaseId());
						if (roleTimerNpcActiveInfo == null) {
							roleTimerNpcActiveInfo = xbean.Pod.newRoleTimerNpcInfo();
							
							roleTimerNpcActiveInfo.setAwardtimes(0);
							roleTimerNpcActiveInfo.setLastawardtime(now);
							
							roleActiveInfo.getActinfo().put(activeInfo.getBaseId(), roleTimerNpcActiveInfo);
						}
						
						if (!DateValidate.inTheSameDay(now,	roleTimerNpcActiveInfo.getLastawardtime())) {
							roleTimerNpcActiveInfo.setAwardtimes(0);
							roleTimerNpcActiveInfo.setLastawardtime(now);
						}
						//高于10次不能获取奖励
						if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
							fire.pb.talk.MessageMgr.sendMsgNotify(rId, timerData.awardOverStepNotice, null);
							continue;
						}
						
						awardToRole(rId, awardId);
						
						//baseid 对应活动配置表新 里的序号
						RoleLiveness actrole = RoleLiveness.getRoleLiveness(rId);
						if (actrole != null)
							actrole.handleActiveLivenessData(timerData.actid);
						
						rollAwardIds.add(rId);
						
						if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
							fire.pb.talk.MessageMgr.sendMsgNotify(rId, timerData.awardOverStepNotice, null);
						}
						
						fire.pb.course.CourseManager.achieveCourse(rId, fire.pb.course.CourseType.ACTIVE_COURSE, activeInfo.getBaseId(), 0);
					}
				}
				else { //是狩猎活动,根据狩猎id记次
					for (Long rId : roleIds) {
						//分级次数
						int rolelevel = xtable.Properties.selectLevel(rId);
						TimesLevel timeLevel = timerData.getTimesLevel(rolelevel);
						if (timeLevel != null) {
							awardcnt = timeLevel.times;
						}
						
						xbean.RoleActiveTimerNpcInfo roleActiveInfo = xtable.Roletimernpcinfos.get(rId);
						if (roleActiveInfo == null) {
							roleActiveInfo = xbean.Pod.newRoleActiveTimerNpcInfo();
							xtable.Roletimernpcinfos.insert(rId, roleActiveInfo);
						}
						
						xbean.RoleTimerNpcInfo roleTimerNpcActiveInfo = roleActiveInfo.getActinfo().get(timerData.shoulie);
						if (roleTimerNpcActiveInfo == null) {
							roleTimerNpcActiveInfo = xbean.Pod.newRoleTimerNpcInfo();
							
							roleTimerNpcActiveInfo.setAwardtimes(0);
							roleTimerNpcActiveInfo.setLastawardtime(now);
							
							roleActiveInfo.getActinfo().put(timerData.shoulie, roleTimerNpcActiveInfo);
						}
						
						if (!DateValidate.inTheSameDay(now,	roleTimerNpcActiveInfo.getLastawardtime())) {
							roleTimerNpcActiveInfo.setAwardtimes(0);
							roleTimerNpcActiveInfo.setLastawardtime(now);
						}
						//高于10次不能获取奖励
						if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
							fire.pb.talk.MessageMgr.sendMsgNotify(rId, timerData.awardOverStepNotice, null);
							continue;
						}
						
						awardToRole(rId, awardId);
						
						roleTimerNpcActiveInfo.setAwardtimes(roleTimerNpcActiveInfo.getAwardtimes() + 1);
						roleTimerNpcActiveInfo.setLastawardtime(now);

						//baseid 对应活动配置表新 里的序号
						RoleLiveness actrole = RoleLiveness.getRoleLiveness(rId);
						if (actrole != null)
							actrole.handleActiveLivenessData(timerData.actid);
						
						rollAwardIds.add(rId);
						
						if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
							fire.pb.talk.MessageMgr.sendMsgNotify(rId, timerData.awardOverStepNotice, null);
						}
						
						fire.pb.course.CourseManager.achieveCourse(rId, fire.pb.course.CourseType.ACTIVE_COURSE, activeInfo.getBaseId(), 0);
					}
				}
			}
		}// 普通奖励发完
		
		//下面要发分级奖励了
		if (rollAwardIds.size() > 0) {
			if (matchLevel == 0) {//按单个角色发奖励
				for (Long rId : rollAwardIds) {
					int rolelevel = xtable.Properties.selectLevel(rId);
					LevelGroupAward levelaward = timerData.getLevelAward(rolelevel);
					if (levelaward != null) {
						awardToRole(rId, levelaward.awardid);
					}
				}
			}
			else { //按队伍平均等级发奖励
				LevelGroupAward levelaward = timerData.getLevelAward(matchLevel);
				if (levelaward == null)
					return true;
				
				boolean shared = RewardMgr.getInstance().isShared(levelaward.awardid);
				if (shared) {
					int times = RewardMgr.getInstance().getTimes(levelaward.awardid);
					ArrayList<RolledAwardItem> rollawarditems = new ArrayList<RolledAwardItem>();

					Map<String, Object> paras = new HashMap<String, Object>(20);
					if (battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL) != null) {
						paras.put("MonsterLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL));
					}
					if (battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM) != null) {
						paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
					}
					paras.put(RewardMgr.TEAM_LEVEL, matchLevel);
					paras.put(RewardMgr.IS_LEADER, 0);
					
					for (int i = 0; i < times; i++) {
						RewardMgr.getInstance().distributeAllAward(roleIds.get(0),
								levelaward.awardid, paras,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli,
								battle.getAreaconf(), PAddExpProc.BATTLE_AWARD,
								"活动明雷怪", true, true, rollawarditems, false);
					}

					if (rollawarditems.isEmpty()) {
						return true;
					}
					
					for (Long rid : roleIds) {
						if (!rollAwardIds.contains(rid)) {
							SWaitRollTime waitTime = new SWaitRollTime(160438);
							mkdb.Procedure.psend(rid, waitTime);
						}
					}

					setNeedRoll(true);
					new fire.pb.team.teammelon.PTeamRollMelon(rollawarditems, rollAwardIds,	battle.getBattleid(), null).call();
				}
				else { //不需要roll点
					for (Long rId : rollAwardIds) {
						awardToRole(rId, levelaward.awardid);
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 发奖励
	 * @param rId
	 * @param awardId
	 */
	private void awardToRole(final long rId, final int awardId) {
		Map<String, Object> paras = new HashMap<String, Object>(20);
		paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
		if (rId == roleId) {
			paras.put(RewardMgr.IS_LEADER, 1);
		}
		else {
			paras.put(RewardMgr.IS_LEADER, 0);
		}
		RewardMgr.getInstance().distributeAllAward(rId, awardId, 
				paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 
				awardId, PAddExpProc.OTHER, "活动明雷怪" + awardId);
	}
}
