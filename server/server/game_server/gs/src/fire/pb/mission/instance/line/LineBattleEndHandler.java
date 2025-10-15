package fire.pb.mission.instance.line;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.YYLogger;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RolledAwardItem;
import fire.pb.battle.BasicEndHandler;
import fire.pb.main.ConfigManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.SLineTask;
import fire.pb.mission.SWaitRollTime;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import mkdb.Lockeys;

public class LineBattleEndHandler extends BasicEndHandler {
//	private final long roleid;
	private final int serviceId;
	private final long npcKey;
	private final long instanceOnlyId;
	private final Integer instanceId;
	private final int awardtime;
	private final int ptawardtime;
	private final int awardid;
	private final int itemawardid;
	private final int yuanzhuawardid; //援助奖励

	public int getServiceId() {
		return serviceId;
	}

	public int getAwardtime() {
		return awardtime;
	}

	public LineBattleEndHandler(
//			final long roleid,
			final int serviceId, 
			final long npcKey,	
			final long instanceOnlyId, 
			final Integer instanceId, final int awardtime, final int ptawardtime, final int awardid, final int yuanzhuawardid
			, final int itemawardid) {
//		this.roleid = roleid;
		this.serviceId = serviceId;
		this.npcKey = npcKey;
		this.instanceOnlyId = instanceOnlyId;
		this.instanceId = instanceId;
		this.awardtime = awardtime;
		this.ptawardtime = ptawardtime;
		this.awardid = awardid;
		this.yuanzhuawardid = yuanzhuawardid;
		this.itemawardid = itemawardid;
	}

	@Override
	protected void transformAfterBattle() {
	}

	@Override
	protected boolean handleAward() {
		List<xbean.Fighter> winFighters = getWinRoles();
		if (winFighters.isEmpty()) {
			winFighters = getDeadRoles();
		}
		Team team = TeamManager.selectTeamByRoleId(winFighters.get(0).getUniqueid());
		Collection<Long> roleids = new ArrayList<Long>(5);
		if (team != null) {
			roleids.addAll(team.getNormalMemberIds());
		}
		else {
			return false;
		}
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));
		
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceOnlyId);
		if (instInfo == null) {
			return false;
		}
		
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(instanceId);
		if (taskCofig == null) {
			return false;
		}

		int totalRound = instInfo.getFinalcounts() + battle.getRound();
		instInfo.setFinalcounts(totalRound);
		
		int step = instInfo.getCurrentstep();
		
		instInfo.getRoleids().clear();
		instInfo.getRoleids().addAll(roleids);
		
		fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(231);
		int commonCnt = Integer.parseInt(common.getValue());
		List<Long> awardRoles = new ArrayList<Long>(5);
		
		long currentTime = System.currentTimeMillis();
		
		if (awardid != 0 && yuanzhuawardid != 0) {
			//新的奖励逻辑
			for (Long rid : roleids) {
				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(rid);
				if (infoCol == null)
					continue;
				
				xbean.InstanceTaskInfo taskInfo = infoCol.getInstinfo().get(instanceId);
				if (taskInfo == null)
					continue;
				
				Map<String, Object> paras = new HashMap<String, Object>(10);
				putBattleParas(paras);
				paras.put("Time", 1);
				paras.put(RewardMgr.IS_LEADER, team.isTeamLeader(rid) ? 1 : 0);
				paras.put(RewardMgr.RING, step);
				paras.put(RewardMgr.FUBEN_LEVEL, taskCofig.getMinlevel());
				paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
				paras.put(RewardMgr.FUBEN_ID, instanceId);
				
				//副本id包含在奖励列表里,并且奖励列表数量小于奖励次数
//				boolean giveAward = taskInfo.getInsts().contains(instanceId);
				
				Integer awardTimes = taskInfo.getStepawardtimes().get(step);
				if (awardTimes == null) {
					awardTimes = 0;
				}
				
				//设置奖励类型
				xbean.InstanceTimeAwardInfo awardInfo = infoCol.getInstcount().get(instanceId);
				if (awardInfo == null) {
					awardInfo = xbean.Pod.newInstanceTimeAwardInfo();
					infoCol.getInstcount().put(instanceId, awardInfo);
				}
				
				if ((infoCol.getCounts() < commonCnt && taskInfo.getAwarding() == 1) || awardInfo.getStepawardtimes().size() > 0) {//奖励
					Integer awardStepTimes = awardInfo.getStepawardtimes().get(step);
					if (awardStepTimes == null) {
						awardStepTimes = 0;
					}
					
					if (awardInfo.getStepawardtimes().size() != taskCofig.stepnum) {
						//还没有拿到所有关卡奖励
						if (awardStepTimes == 0) {
							doAward(rid, step, awardid, paras);
							awardRoles.add(rid);
							
							awardInfo.getStepawardtimes().put(step, 1);
							if (awardInfo.getStepawardtimes().size() == taskCofig.stepnum) {
								//已经通关一次
								awardInfo.getStepawardtimes().clear();
								taskInfo.setCounts(taskInfo.getCounts() + 1);
								taskInfo.setAwarding(0);
								LineInstManager.logger.info(new StringBuilder().append("通关精英副本, roleid:").append(rid)
										.append(", instanceId:").append(instanceId)
										.append(", at step:").append(step));
								//该副本已经完成,并拿完奖励了,添加副本活跃;只处理当天接的副本的,如果在副本中跨天,则不处理
								if (DateValidate.inTheSameDay(infoCol.getAccepttime(), currentTime)) {
									RoleLiveness actrole = RoleLiveness.getRoleLiveness(rid);
									if (actrole != null)
										actrole.handleActiveLivenessData(RoleLiveness.FUBEN);
								}
							}
							else if (awardInfo.getStepawardtimes().size() == 1) {
								//第一次进入一个副本,就扣次
								infoCol.setCounts(infoCol.getCounts() + 1);
							}
							
							if (step == taskCofig.stepnum) {
								infoCol.setFanpai(1);
							}
						}
						else if (awardStepTimes < (ptawardtime + commonCnt)) {
							infoCol.setFanpai(0);
							doAward(rid, step, yuanzhuawardid, paras);
						}
					} else if (awardTimes < (ptawardtime + commonCnt)) {
						infoCol.setFanpai(0);
						doAward(rid, step, yuanzhuawardid, paras);
					}
				}
				else if (awardTimes < (ptawardtime + commonCnt)) {
					infoCol.setFanpai(0);
					doAward(rid, step, yuanzhuawardid, paras);
				}
				else if (awardTimes > (ptawardtime + commonCnt)) {
					infoCol.setFanpai(0);
				}
				
				taskInfo.getStepawardtimes().put(step, awardTimes + 1);
				infoCol.setReset(0);
			}
		}
		
		if (itemawardid != 0 && awardRoles.size() > 0) {
			int times = RewardMgr.getInstance().getTimes(itemawardid);
			ArrayList<RolledAwardItem> rollawarditems = new ArrayList<RolledAwardItem>();

			Map<String, Object> paras = new HashMap<String, Object>(20);
			if (battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL) != null) {
				paras.put("MonsterLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL));
			}
			paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
			paras.put(RewardMgr.FUBEN_ID, instanceId);
			for (int i = 0; i < times; i++) {
				
				for (Long roleid : awardRoles)
				{
					RewardMgr.getInstance().distributeAllAward(roleid,
							itemawardid, paras,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli,
							battle.getAreaconf(), PAddExpProc.BATTLE_AWARD,
							"精英副本", true, true, null, true);						
				}				
				
				RewardMgr.getInstance().distributeAllAward(awardRoles.get(0),
						itemawardid, paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli,
						battle.getAreaconf(), PAddExpProc.BATTLE_AWARD,
						"精英副本", true, true, rollawarditems, false);
			}

			if (rollawarditems.isEmpty()) {
				SceneNpcManager.premoveNpcWhileCommit(npcKey);
				LineInstManager.getInstance().doNextStep(instInfo, instanceId, instanceOnlyId);
				return true;
			}
			
			
			java.util.List<Long> watcherids = new java.util.ArrayList<Long>();
			for (Long rid : roleids) {
				if (!awardRoles.contains(rid)) {
					SWaitRollTime waitTime = new SWaitRollTime(160438);
					mkdb.Procedure.psend(rid, waitTime);
					watcherids.add(rid);
				}
			}

			setNeedRoll(true);
			SceneNpcManager.premoveNpcWhileCommit(npcKey);
			new fire.pb.team.teammelon.PTeamRollMelon(rollawarditems, awardRoles, instanceOnlyId, 2, instanceId, battle.getBattleid(), watcherids).call();
			return true;
		}
		else {
			SceneNpcManager.premoveNpcWhileCommit(npcKey);
			LineInstManager.getInstance().doNextStep(instInfo, instanceId, instanceOnlyId);
			return true;
		}
	}
	
	/**
	 * 发奖励
	 * @param roleid
	 * @param awardid
	 * @param paras
	 */
	public void doAward(final long roleid, final int step, final int awardid, Map<String, Object> paras) {
		RewardMgr.getInstance().distributeAllAward(roleid,
				awardid,
				paras,                       
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli,
				awardid, PAddExpProc.BATTLE_AWARD,
				"精英副本", true);
		
		LineInstManager.logger.info(new StringBuilder().append("获得精英副本奖励, roleid:").append(roleid)
				.append(", instanceId:").append(instanceId)
				.append(", step:").append(step)
				.append(", awardid:").append(awardid));
		
		//精英副本 发奖
		//处理运营日志
		YYLogger.completeActivityLog(roleid, RoleLiveness.FUBEN, YYLogger.SUCC_FLAG, "");
	}
}

