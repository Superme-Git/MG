package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.YYLogger;
import fire.msp.role.GSetNpcBattleInfo;
import fire.pb.GsClient;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RolledAwardItem;
import fire.pb.activity.timernpc.TimerNpcData;
import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.activity.timernpc.TimesLevel;
import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.map.EventTimerNpc;
import fire.pb.map.SEventTimerNpcDianKa;
import fire.pb.mission.SWaitRollTime;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.instance.LevelGroupAward;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import mkdb.Lockeys;

public class EventNpcBattleEndHandler extends BasicEndHandler {
	private final long teamleaderid; //teamleader id
	private final int biaoid; //事件刷新表.xlsx id
	private final int eventId;
	private final long npcKey;
	private final int actId;
	
	public EventNpcBattleEndHandler(final long roleid, final int biaoid, final int eventId, final long npcKey) {
		this.teamleaderid = roleid;
		this.biaoid = biaoid;
		this.eventId = eventId;
		this.npcKey = npcKey;
		this.actId = 0;
	}
	
	public EventNpcBattleEndHandler(final long roleid, final int biaoid, final int eventId, final long npcKey, final int actId) {
		this.teamleaderid = roleid;
		this.biaoid = biaoid;
		this.eventId = eventId;
		this.npcKey = npcKey;
		this.actId = actId;
	}

	@Override
	public boolean handleOutBattle(int index, int petkey) throws Exception {
		return true;
	}

	@Override
	public boolean handleDeath() {
		if (actId > 0) {
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
		}
		else {
			EventTimerNpc eventTimer = BaoTuMapManager.getInstance().getTimerNpc(biaoid);
			if (eventTimer == null)
				return false;
			
			if (eventTimer.match == 1) {
				xtable.Npcbattleinfo.remove(npcKey);
			}
			
			xbean.eventNpcInfoCol npcInfo = xtable.Eventnpcinfotable.get(biaoid);
			if (npcInfo == null)
				return false;
			if (battle.getBattleresult() == BattleField.BATTLE_WIN) {
				TimerNpcService.getInstance().setEventNpcState(npcInfo, npcKey, 0, TimerNpcService.WIN_BATTLE,
						eventTimer.getXiaoshi());
			} 
			else {
				TimerNpcService.getInstance().setEventNpcState(npcInfo, npcKey, 0, TimerNpcService.LOSE_BATTLE,
						eventTimer.getXiaoshi());
				GsClient.pSendWhileCommit(new GSetNpcBattleInfo(npcKey, 0));
			}
		}
		
		return true;
	}

	@Override
	protected boolean handleAward() {
		EventTimerNpc eventTimer = BaoTuMapManager.getInstance().getTimerNpc(biaoid);
		if (eventTimer == null)
			return false;
		
		int shoulie = 0;
		int awardcnt = eventTimer.awardCnt;
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) { //点卡服
			shoulie = ((SEventTimerNpcDianKa)eventTimer).getShoulie();
		}
		
		boolean isTeam = false;
		Team team = TeamManager.getTeamByRoleId(teamleaderid);
		if (team != null) {
			isTeam = true;
		}
		
		List<Long> roleIds = new ArrayList<Long>(5);
		roleIds.addAll(battle.getRoleids().keySet());
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));
		
		List<Long> rollRoleIds = new ArrayList<Long>(5);
		
		int matchLevel = 0;
		LevelGroupAward award = null;
		List<LevelGroupAward> listAward = BaoTuMapManager.getInstance().getAwardGroup(eventId);
		if (listAward != null) {
			if (eventTimer.matchtype == 2) {
				for (Long rid : battle.getRoleids().keySet()) {
					final int roleLevel = xtable.Properties.selectLevel(rid);
					matchLevel += roleLevel;
				}
				
				matchLevel /= battle.getRoleids().size();
			}
			
			for (LevelGroupAward awards : listAward) {
				if (matchLevel >= awards.levelmin && matchLevel <= awards.levelmax) {
					award = awards;
					break;
				}
			}
		}
		
		int awardId = eventTimer.awardId;
		long now = System.currentTimeMillis();
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			for (Long roleId : roleIds) {
				 //普通服处理
				xbean.RoleEventNpcInfoColumn roleEventCol = xtable.Roleeventnpcinfos.get(roleId);
				if (roleEventCol == null) {
					roleEventCol = xbean.Pod.newRoleEventNpcInfoColumn();
					xtable.Roleeventnpcinfos.insert(roleId, roleEventCol);
				}
				
				xbean.RoleEventNpcInfo roleEventNpcInfo = roleEventCol.getEventinfo().get(eventId);
				if (roleEventNpcInfo == null) {
					roleEventNpcInfo = xbean.Pod.newRoleEventNpcInfo();
					
					roleEventNpcInfo.setAwardtimes(0);
					roleEventNpcInfo.setLastawardtime(now);
					
					roleEventCol.getEventinfo().put(eventId, roleEventNpcInfo);
				}
				
				if (!DateValidate.inTheSameDay(now, roleEventNpcInfo.getLastawardtime())) {
					roleEventNpcInfo.setAwardtimes(0);
					roleEventNpcInfo.setLastawardtime(now);
				}
				Map<String, Object> paras = new HashMap<String, Object>();
				if (battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL) != null) {
					paras.put("MonsterLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL));
				}
				if (battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM) != null) {
					paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
				}
				paras.put(RewardMgr.TEAM_LEVEL, matchLevel);
				
				if (isTeam) {
					if (roleId == teamleaderid)
						paras.put(RewardMgr.IS_LEADER, 1);
					else
						paras.put(RewardMgr.IS_LEADER, 0);
				}
				else {
					paras.put(RewardMgr.IS_LEADER, 0);
				}
				
				//高于10次不能获取奖励
				if (roleEventNpcInfo.getAwardtimes() >= eventTimer.awardCnt) {
					if (eventTimer.awardId2 != 0) {
						RewardMgr.getInstance().distributeAllAward(roleId, eventTimer.awardId2, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli, awardId, PAddExpProc.OTHER, "事件刷怪胜利奖励" + eventTimer.awardId2);
						continue;
					}
					else {
						fire.pb.talk.MessageMgr.sendMsgNotify(roleId, eventTimer.awardOverStepNotice, null);
						continue;
					}
				}
				
				rollRoleIds.add(roleId);
				if (awardId != 0) {
					RewardMgr.getInstance().distributeAllAward(roleId, awardId, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli, awardId, PAddExpProc.OTHER, "事件刷怪胜利奖励" + awardId);
					//运营日志 封印古神 完成
					YYLogger.completeActivityLog(roleId, eventTimer.getActid(), YYLogger.SUCC_FLAG, "");
				}
				
				roleEventNpcInfo.setAwardtimes(roleEventNpcInfo.getAwardtimes() + 1);
				roleEventNpcInfo.setLastawardtime(now);
				
				// 战胜后 给活跃度
				if (eventId == 1) {
					RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleId);
					if (actrole != null)
						actrole.handleActiveLivenessData(eventTimer.getActid());
					
					fire.pb.course.CourseManager.achieveCourse(roleId, fire.pb.course.CourseType.ACTIVE_COURSE, eventTimer.getActid(), 0);
				}
				
				if (roleEventNpcInfo.getAwardtimes() >= eventTimer.awardCnt) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleId, eventTimer.awardOverStepNotice, null);
				}
			}
		}
		else { // 点卡服
			
			EventTimerNpcData eventData = BaoTuMapManager.getInstance().getEventTimerNpcData(eventTimer.refreshGroup);
			if (eventData == null) {
				return false;
			}
			
			EventTimerGroupData eventGroupData = eventData.getGroupDataById(eventTimer.id);
			if (eventGroupData == null) {
				return false;
			}
			
			for (Long roleId : roleIds) {
				//分级次数
				int rolelevel = xtable.Properties.selectLevel(roleId);
				TimesLevel timeLevel = eventGroupData.getTimesLevel(rolelevel);
				if (timeLevel != null) {
					awardcnt = timeLevel.times;
				}
				
				if (shoulie == 0) {
					Map<String, Object> paras = new HashMap<String, Object>();
					if (battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL) != null) {
						paras.put("MonsterLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL));
					}
					if (battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM) != null) {
						paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
					}
					paras.put(RewardMgr.TEAM_LEVEL, matchLevel);
					
					if (isTeam) {
						if (roleId == teamleaderid)
							paras.put(RewardMgr.IS_LEADER, 1);
						else
							paras.put(RewardMgr.IS_LEADER, 0);
					}
					else {
						paras.put(RewardMgr.IS_LEADER, 0);
					}
					
					RewardMgr.getInstance().distributeAllAward(roleId, eventTimer.awardId2, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli, awardId, PAddExpProc.OTHER, "事件刷怪胜利奖励" + eventTimer.awardId2);
					
					rollRoleIds.add(roleId);
					if (awardId != 0) {
						RewardMgr.getInstance().distributeAllAward(roleId, awardId, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli, awardId, PAddExpProc.OTHER, "事件刷怪胜利奖励" + awardId);
						//运营日志 封印古神 完成
						YYLogger.completeActivityLog(roleId, eventTimer.getActid(), YYLogger.SUCC_FLAG, "");
					}
					
					// 战胜后 给活跃度
					RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleId);
					if (actrole != null)
						actrole.handleActiveLivenessData(eventTimer.getActid());
					
					fire.pb.course.CourseManager.achieveCourse(roleId, fire.pb.course.CourseType.ACTIVE_COURSE, eventTimer.getActid(), 0);
				}
				else {
					xbean.RoleActiveTimerNpcInfo roleActiveInfo = xtable.Roletimernpcinfos.get(roleId);
					if (roleActiveInfo == null) {
						roleActiveInfo = xbean.Pod.newRoleActiveTimerNpcInfo();
						xtable.Roletimernpcinfos.insert(roleId, roleActiveInfo);
					}
					
					xbean.RoleTimerNpcInfo roleTimerNpcActiveInfo = roleActiveInfo.getActinfo().get(shoulie);
					if (roleTimerNpcActiveInfo == null) {
						roleTimerNpcActiveInfo = xbean.Pod.newRoleTimerNpcInfo();
						
						roleTimerNpcActiveInfo.setAwardtimes(0);
						roleTimerNpcActiveInfo.setLastawardtime(now);
						
						roleActiveInfo.getActinfo().put(shoulie, roleTimerNpcActiveInfo);
					}
					
					if (!DateValidate.inTheSameDay(now,	roleTimerNpcActiveInfo.getLastawardtime())) {
						roleTimerNpcActiveInfo.setAwardtimes(0);
						roleTimerNpcActiveInfo.setLastawardtime(now);
					}
					
					Map<String, Object> paras = new HashMap<String, Object>();
					if (battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL) != null) {
						paras.put("MonsterLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL));
					}
					if (battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM) != null) {
						paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
					}
					paras.put(RewardMgr.TEAM_LEVEL, matchLevel);
					
					if (isTeam) {
						if (roleId == teamleaderid)
							paras.put(RewardMgr.IS_LEADER, 1);
						else
							paras.put(RewardMgr.IS_LEADER, 0);
					}
					else {
						paras.put(RewardMgr.IS_LEADER, 0);
					}
					
					//高于10次不能获取奖励
					if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
						if (eventTimer.awardId2 != 0) {
							RewardMgr.getInstance().distributeAllAward(roleId, eventTimer.awardId2, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli, awardId, PAddExpProc.OTHER, "事件刷怪胜利奖励" + eventTimer.awardId2);
							continue;
						}
						else {
							fire.pb.talk.MessageMgr.sendMsgNotify(roleId, eventTimer.awardOverStepNotice, null);
							continue;
						}
					}
					
					rollRoleIds.add(roleId);
					if (awardId != 0) {
						RewardMgr.getInstance().distributeAllAward(roleId, awardId, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli, awardId, PAddExpProc.OTHER, "事件刷怪胜利奖励" + awardId);
						//运营日志 封印古神 完成
						YYLogger.completeActivityLog(roleId, eventTimer.getActid(), YYLogger.SUCC_FLAG, "");
					}
					
					roleTimerNpcActiveInfo.setAwardtimes(roleTimerNpcActiveInfo.getAwardtimes() + 1);
					roleTimerNpcActiveInfo.setLastawardtime(now);
					
					// 战胜后 给活跃度
					RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleId);
					if (actrole != null)
						actrole.handleActiveLivenessData(eventTimer.getActid());
					
					fire.pb.course.CourseManager.achieveCourse(roleId, fire.pb.course.CourseType.ACTIVE_COURSE, eventTimer.getActid(), 0);
					
					if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
						fire.pb.talk.MessageMgr.sendMsgNotify(roleId, eventTimer.awardOverStepNotice, null);
					}
				}
			}
		}
		
		if (award != null && rollRoleIds.size() > 0) {
			int times = RewardMgr.getInstance().getTimes(award.awardid);
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
						award.awardid, paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli,
						battle.getAreaconf(), PAddExpProc.BATTLE_AWARD,
						"活动明雷怪", true, true, rollawarditems, false);
			}

			if (rollawarditems.isEmpty()) {
				return true;
			}
			
			for (Long rid : roleIds) {
				if (!rollRoleIds.contains(rid)) {
					SWaitRollTime waitTime = new SWaitRollTime(160438);
					mkdb.Procedure.psend(rid, waitTime);
				}
			}

			setNeedRoll(true);
			new fire.pb.team.teammelon.PTeamRollMelon(rollawarditems, rollRoleIds,	battle.getBattleid(), null).call();
		}
		
		return true;
	}
}
