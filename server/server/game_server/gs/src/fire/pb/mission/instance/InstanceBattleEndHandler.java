package fire.pb.mission.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RewardObjs;
import fire.pb.battle.BasicEndHandler;
import fire.pb.map.SceneNpcManager;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Lockeys;


public class InstanceBattleEndHandler extends BasicEndHandler {
	final ServiceConfig serviceConfig;
	private final int serviceId;
	private final long npcKey;
	private final long instanceOnlyId;
	private final int instanceId;

	public InstanceBattleEndHandler(final ServiceConfig serviceConfig,
			final int serviceId, 
			final long npcKey,	
			final long instanceOnlyId, 
			final int instanceId) {
		this.serviceConfig = serviceConfig;
		this.serviceId = serviceId;
		this.npcKey = npcKey;
		this.instanceOnlyId = instanceOnlyId;
		this.instanceId = instanceId;
	}

	@Override
	protected void transformAfterBattle() {
	}

	@Override
	protected boolean handleAward() {
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceOnlyId);
		if (instInfo == null) {
			return false;
		}
		
		List<xbean.Fighter> winFighters = getWinRoles();
		if (winFighters.isEmpty()) {
			winFighters = getDeadRoles();
		}

		Team team = TeamManager.selectTeamByRoleId(winFighters.get(0).getUniqueid());
		
		int totalRound = instInfo.getFinalcounts() + battle.getRound();
		instInfo.setFinalcounts(totalRound);
		
		int step = instInfo.getCurrentstep();
		Collection<Long> roleids = new ArrayList<Long>(4);
		if (team != null) {
			roleids.addAll(team.getNormalMemberIds());
		}
		else {
			roleids.addAll(instInfo.getRoleids());
		}
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLEINSTANCETASK,roleids));
		//队伍中有老玩家额外加成20%经验判断
//		Set<Long> roleIds = new HashSet<Long>();
//		boolean hasTeamExtraAdd = false;
//		roleIds.addAll(battle.getRoleids().keySet());
//		hasTeamExtraAdd = VeteranManager.teamHasExtraExpAdd(roleIds);
		
		int matchLevel = 0;
		LevelGroupAward award = null;
		if (serviceConfig.matchtype == 2) {
			for (Long rid : battle.getRoleids().keySet()) {
				final int roleLevel = xtable.Properties.selectLevel(rid);
				matchLevel += roleLevel;
			}
			
			matchLevel /= battle.getRoleids().size();
			
			for (LevelGroupAward awards : serviceConfig.groupAward) {
				if (matchLevel >= awards.levelmin && matchLevel <= awards.levelmax) {
					award = awards;
				}
			}
		}
		
		for (Long rid : battle.getRoleids().keySet()) {
			if (!roleids.contains(rid))
				continue;
			
			xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.get(rid);
			if (taskInfo == null)
				continue;
			
			xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
			if (info == null)
				continue;
			
			Map<String, Object> paras = new HashMap<String, Object>(10);
			putBattleParas(paras);
			paras.put("Time", 1);
			paras.put(RewardMgr.IS_LEADER, team != null && team.isTeamLeader(rid) ? 1 : 0);
			paras.put(RewardMgr.RING, step);
			Integer awardTimes = info.getStepawardtimes().get(step);
			
			final int roleLevel = xtable.Properties.selectLevel(rid);
			Map<Integer, RewardObjs> awardResult = null;
			if (awardTimes == null) {
				awardTimes = 1;
				if (serviceConfig.matchtype == 1) {
					for (LevelGroupAward awards : serviceConfig.groupAward) {
						if (roleLevel >= awards.levelmin && roleLevel <= awards.levelmax) {
							awardResult = RewardMgr.getInstance().distributeAllAward(rid,
									awards.awardid,
									paras,
									fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli,
									awards.awardid, PAddExpProc.BATTLE_AWARD,
									"副本战斗胜利奖励", true);
							break;
						}
					}
				}
				else if (serviceConfig.matchtype == 2) {
					if (award != null) {
						awardResult = RewardMgr.getInstance().distributeAllAward(rid,
								award.awardid,
								paras,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli,
								award.awardid, PAddExpProc.BATTLE_AWARD,
								"副本战斗胜利奖励", true);
					}
				}
				
				info.getStepawardtimes().put(step, awardTimes);
			}
			else if (awardTimes < serviceConfig.sAwardTimes) {
				if (serviceConfig.matchtype == 1) {
					for (LevelGroupAward awards : serviceConfig.groupAward) {
						if (roleLevel >= awards.levelmin && roleLevel <= awards.levelmax) {
							awardResult = RewardMgr.getInstance().distributeAllAward(rid,
									awards.awardid,
									paras,
									fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli,
									awards.awardid, PAddExpProc.BATTLE_AWARD,
									"副本战斗胜利奖励", true);
							break;
						}
					}
				}
				else if (serviceConfig.matchtype == 2) {
					if (award != null) {
						awardResult = RewardMgr.getInstance().distributeAllAward(rid,
								award.awardid,
								paras,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli,
								award.awardid, PAddExpProc.BATTLE_AWARD,
								"副本战斗胜利奖励", true);
					}
				}
				info.getStepawardtimes().put(step, awardTimes + 1);
			}
			else if (awardTimes < (serviceConfig.ptAwardidTimes + serviceConfig.sAwardTimes)) {
				awardResult = RewardMgr.getInstance().distributeAllAward(rid,
						serviceConfig.ptAwardid,
						paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli,
						serviceConfig.ptAwardid, PAddExpProc.BATTLE_AWARD,
						"副本战斗胜利奖励", true);
				info.getStepawardtimes().put(step, awardTimes + 1);
			}
			
			int exp = info.getTotalexp();
			info.setTotalexp(exp);
			do {
				if (awardResult == null)
					break;
				RewardObjs item = awardResult.get(RewardMgr.EXP_AWARD);
				if (item == null)
					break;
				
				info.setTotalexp(info.getTotalexp() + (int) item.getValue());
			} while (false);
		}
		SceneNpcManager.premoveNpcWhileCommit(npcKey);
		InstanceManager.getInstance().doNextStep(instInfo, serviceId, instanceId, instanceOnlyId);
		
		return true;
	}
}
