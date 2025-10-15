package fire.pb.activity.timernpc;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.npc.BattleToNpcError;
import fire.pb.npc.SBattleToNpcError;
import fire.pb.npc.SNpcBattleTime;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PTimerNpcFightProc extends Procedure {
	private final long roleId;
	private final int npcId;
	private final long npcKey;
	
	public PTimerNpcFightProc(final long roleid, final int npcId, final long npcKey) {
		this.roleId = roleid;
		this.npcId = npcId;
		this.npcKey = npcKey;
	}
	
	@Override
	protected boolean process() throws Exception {
		int actId = TimerNpcService.getInstance().getActId(npcId);
		//活动错误
		if (actId == -1) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",actId == -1"));
			return false;
		}
		
		TimerNpcData timerData = TimerNpcService.getInstance().getNpcData(actId);
		if (timerData == null) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",actId:").append(actId)
					.append(",timerData == null"));
			return false;
		}
		
		int teamCnt = TimerNpcService.getInstance().getTeamNumberCount(actId);
		List<Long> allRoles = new ArrayList<Long>();
		
		if (teamCnt > 0) {
			//检查队伍
			Team team = TeamManager.getTeamByRoleId(roleId);
			if (team == null || team.getTeamLeaderId() != roleId) {
				List<String> paras = new ArrayList<String>(1);
				paras.add(String.valueOf(teamCnt));
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 150522, npcId, paras);
				return false;
			}
			
			allRoles.addAll(team.getNormalMemberIds());
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoles));
			
			if (allRoles.size() < teamCnt) {
				List<String> paras = new ArrayList<String>(1);
				paras.add(String.valueOf(teamCnt));
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 150522, npcId, paras);	
				return false;
			}
			List<String> levelApplyNames = new ArrayList<String>();
			List<String> levelMaxApplyNames = new ArrayList<String>();
			for (Long mem : team.getNormalMemberIds()) {
				PropRole pRole = new PropRole(mem, true);
				if (pRole.getLevel() < timerData.levelMin) {
					levelApplyNames.add(pRole.getName());
				}
				else if (pRole.getLevel() > timerData.levelMax) {
					levelMaxApplyNames.add(pRole.getName());
				}
			}
			//有人等级不足
			if (levelApplyNames.size() > 0) {
				StringBuffer strParam = new StringBuffer();
				int nIndex = 0;
				for (String name : levelApplyNames) {
					if (nIndex != (levelApplyNames.size() - 1)) {
						strParam.append(name).append(",");
					}
					else {
						strParam.append(name);
					}
				}
				List<String> paras = new ArrayList<String>(2);
				paras.add(strParam.toString());
				paras.add(String.valueOf(timerData.levelMin));
				for (Long mem : team.getNormalMemberIds()) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150523, npcId, paras);
				}
				
				return false;
			}
			
			if (levelMaxApplyNames.size() > 0) {
				StringBuffer strParam = new StringBuffer();
				int nIndex = 0;
				for (String name : levelMaxApplyNames) {
					if (nIndex != (levelMaxApplyNames.size() - 1)) {
						strParam.append(name).append(",");
					}
					else {
						strParam.append(name);
					}
				}
				List<String> paras = new ArrayList<String>(2);
				paras.add(strParam.toString());
				paras.add(String.valueOf(timerData.levelMax));
				for (Long mem : team.getNormalMemberIds()) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150525, npcId, paras);
				}
				
				return false;
			}
		}
		else {
			Team team = TeamManager.getTeamByRoleId(roleId);
			if (team == null || team.isAbsentMember(roleId)) {
				PropRole pRole = new PropRole(roleId, true);
				if (pRole.getLevel() > timerData.levelMax || pRole.getLevel() < timerData.levelMin) {
					List<String> paras = new ArrayList<String>(1);
					paras.add(String.valueOf(timerData.levelMin));
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId, 145496, npcId, paras);
					return false;
				}
//				List<String> paras = new ArrayList<String>(1);
//				paras.add(String.valueOf(teamCnt));
//				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 150522, npcId, paras);
//				return false;
			}
			
			if (team != null) {
				allRoles.addAll(team.getNormalMemberIds());
				lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoles));
				
				List<String> levelApplyNames = new ArrayList<String>();
				List<String> levelMaxApplyNames = new ArrayList<String>();
				for (Long mem : team.getNormalMemberIds()) {
					PropRole pRole = new PropRole(mem, true);
					if (pRole.getLevel() < timerData.levelMin) {
						levelApplyNames.add(pRole.getName());
					}
					else if (pRole.getLevel() > timerData.levelMax) {
						levelMaxApplyNames.add(pRole.getName());
					}
				}
				//有人等级不足
				if (levelApplyNames.size() > 0) {
					StringBuffer strParam = new StringBuffer();
					int nIndex = 0;
					for (String name : levelApplyNames) {
						if (nIndex != (levelApplyNames.size() - 1)) {
							strParam.append(name).append(",");
						}
						else {
							strParam.append(name);
						}
					}
					List<String> paras = new ArrayList<String>(2);
					paras.add(strParam.toString());
					paras.add(String.valueOf(timerData.levelMin));
					for (Long mem : team.getNormalMemberIds()) {
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150523, npcId, paras);
					}
					
					return false;
				}
				
				if (levelMaxApplyNames.size() > 0) {
					StringBuffer strParam = new StringBuffer();
					int nIndex = 0;
					for (String name : levelMaxApplyNames) {
						if (nIndex != (levelMaxApplyNames.size() - 1)) {
							strParam.append(name).append(",");
						}
						else {
							strParam.append(name);
						}
					}
					List<String> paras = new ArrayList<String>(2);
					paras.add(strParam.toString());
					paras.add(String.valueOf(timerData.levelMax));
					for (Long mem : team.getNormalMemberIds()) {
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150525, npcId, paras);
					}
					
					return false;
				}
			}
		}
		
		//检查是否需要判断奖励次数
		if (timerData.havetimes == 1) {
			boolean havetime = false;
			for (Long roleId : allRoles) {
				if (TimerNpcService.checkBattleAwardTimes(roleId, actId)) {
					havetime = true;
					break;
				}
			}
			
			//TODO 需要提示
			if (!havetime) {
				for (Long mem : allRoles) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 170054, null);
				}
				return false;
			}
		}
		
		//检查NPC状态
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actId);
		if (npcInfo == null) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",actId:").append(actId)
					.append(",npcInfo == null"));
			return false;
		}
		if (!npcInfo.getNpcinfo().containsKey(npcKey)) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",actId:").append(actId)
					.append(",!npcInfo.getNpcinfo().containsKey(npcKey)"));
			return false;
		}
		//怪物已经消失
		xbean.timerNpcInfo tInfo = npcInfo.getNpcinfo().get(npcKey);
		if (tInfo == null) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",tInfo == null"));
			return false;
		}
		if (tInfo.getNpcstatus() == 2) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",tInfo.getNpcstatus() == 2"));
			return false;
		}
		
		//正在战斗中
		if (timerData.battletime > 1) {
			if (tInfo.getBattletime() >= timerData.battletime) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145148, npcId, null);
				TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",找的怪在战斗中:")
						.append(",npcId:").append(npcId)
						.append(",npcKey:").append(npcKey)
						.append(tInfo.getBattletime()).append(",配置的战斗人数是:").append(timerData.battletime));
				sendErrorResponse(BattleToNpcError.NpcOnBattle);
				return true;
			}
		}
		else {
			if (tInfo.getNpcstatus() == 1) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145148, npcId, null);
				TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",找的怪在战斗中:")
						.append(",npcId:").append(npcId)
						.append(",npcKey:").append(npcKey)
						.append(tInfo.getBattletime()).append(",配置的战斗人数是:").append(timerData.battletime));
				sendErrorResponse(BattleToNpcError.NpcOnBattle);
				return true;
			}
		}
		
		//判断npc是否需要进行匹配进入战斗
		if (timerData.match == 1) {
			long nowtime = System.currentTimeMillis();
			//在规定时间内点击npc进入战斗, 如果在规定时间还没有人点击,则第一个点击怪的进入战斗
			if ((nowtime - tInfo.getCreatetime()) < timerData.matchsec * 1000) {
				xbean.npcBattleInfoCol npcBattle = xtable.Npcbattleinfo.get(npcKey); 
				if (npcBattle == null) {
					npcBattle = xbean.Pod.newnpcBattleInfoCol();
					xtable.Npcbattleinfo.insert(npcKey, npcBattle);
				}
				npcBattle.getBattleroles().put(roleId, allRoles.size());
				
				Long onMatchNpcKey = xtable.Role2npcbattle.select(roleId);
				if (onMatchNpcKey != null) {
					TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",正在匹配中,取消原来的匹配:")
							.append(",npcId:").append(npcId)
							.append(",npcKey:").append(onMatchNpcKey)
							.append(",要匹配的怪:").append(npcKey));
					
					xbean.npcBattleInfoCol npcMatchBattle = xtable.Npcbattleinfo.get(onMatchNpcKey);
					if (npcMatchBattle != null) {
						npcMatchBattle.getBattleroles().remove(roleId);
					}
					xtable.Role2npcbattle.remove(roleId);
				}
				
				xtable.Role2npcbattle.insert(roleId, npcKey);
				//给客户端发送战斗匹配剩余时间
				long lasttime = (tInfo.getCreatetime() + timerData.matchsec * 1000) - nowtime;
				mkdb.Procedure.psendWhileCommit(allRoles, new SNpcBattleTime(npcId, npcKey, timerData.matchsec * 1000, lasttime));
				//返回,等匹配线程执行
				return true;
			}
			//过了匹配时间,直接往下走
			else {
				xbean.npcBattleInfoCol npcBattle = xtable.Npcbattleinfo.get(npcKey); 
				if (npcBattle != null) {
					if (npcBattle.getBattleroles().size() != 0) {
						TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",定时npc在需要匹配战斗:")
								.append(",npcId:").append(npcId)
								.append(",npcKey:").append(npcKey)
								.append(tInfo.getBattletime()).append(",配置的战斗人数是:").append(timerData.battletime));
						//return false; //FIXME 此处先记下日志,高并发时可能会问题,应该不是必出问题
					}
				}
			}
		}
		
		PEnterNpcBattle enterbattle = new PEnterNpcBattle(npcId, npcKey, actId, roleId); 
		return enterbattle.call();
	}
	
	private void sendErrorResponse(int errorCode) {
		SBattleToNpcError battleToNpcError = new SBattleToNpcError();
		battleToNpcError.battleerror = errorCode;
		mkdb.Procedure.psendWhileCommit(roleId, battleToNpcError);
	}

}
