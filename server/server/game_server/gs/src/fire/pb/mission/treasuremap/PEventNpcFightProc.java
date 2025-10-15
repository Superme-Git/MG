package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.map.EventTimerNpc;
import fire.pb.npc.SNpcBattleTime;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PEventNpcFightProc extends Procedure {
	private final long roleId;
	private final int npcId;
	private final long npcKey;
	
	public PEventNpcFightProc(final long roleId, final int npcId, final long npcKey) {
		this.roleId = roleId;
		this.npcId = npcId;
		this.npcKey = npcKey;
	}
	
	@Override
	protected boolean process() throws Exception {
		int biaoId = TimerNpcService.getInstance().getEventId(npcId);
		//事件错误
		if (biaoId == -1) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",biaoId == -1"));
			return false;
		}
		
		EventTimerNpc eventTimer = BaoTuMapManager.getInstance().getTimerNpc(biaoId);
		if (eventTimer == null) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",biaoId:").append(biaoId)
					.append(",eventTimer == null"));
			return false;
		}
		
		List<Long> allRoles = new ArrayList<Long>();
		if (eventTimer.teamRequire == 1) { //需要组队
			//检查队伍
			Team team = TeamManager.getTeamByRoleId(roleId);
			if (team == null) {
				List<String> paras = new ArrayList<String>();
				paras.add(String.valueOf(eventTimer.teamCount));
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 150522, npcId, paras);	
				return false;
			}
			if (team.getTeamLeaderId() != roleId) {
				List<String> paras = new ArrayList<String>();
				paras.add(String.valueOf(eventTimer.teamCount));
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 150522, npcId, paras);
				return false;
			}
			
			allRoles.addAll(team.getNormalMemberIds());
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoles));
			if (allRoles.size() < eventTimer.teamCount) {
				List<String> paras = new ArrayList<String>(1);
				paras.add(String.valueOf(eventTimer.teamCount));
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 150522, npcId, paras);	
				return false;
			}
			List<String> levelApplyNames = new ArrayList<String>();
			List<String> levelMaxApplyNames = new ArrayList<String>();
			for (Long mem : team.getNormalMemberIds()) {
				PropRole pRole = new PropRole(mem, true);
				if (pRole.getLevel() < eventTimer.teamLevelMin) {
					levelApplyNames.add(pRole.getName());
				}
				else if (pRole.getLevel() > eventTimer.teamLevelMax) {
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
				paras.add(String.valueOf(eventTimer.teamLevelMin));
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
				paras.add(String.valueOf(eventTimer.teamLevelMax));
				for (Long mem : team.getNormalMemberIds()) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150525, npcId, paras);
				}
				
				return false;
			}
			
//			lock(xdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoles));
		}
		else {
			Team team = TeamManager.getTeamByRoleId(roleId);
			if (team == null || team.isAbsentMember(roleId)) {
				PropRole pRole = new PropRole(roleId, true);
				if (pRole.getLevel() < eventTimer.teamLevelMin) {
					List<String> paras = new ArrayList<String>(1);
					paras.add(String.valueOf(eventTimer.teamLevelMin));
					if (eventTimer.refreshGroup == 3) {
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId, 160440, npcId, paras);
					}
					else {
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId, 145496, npcId, paras);
					}
					return false;
				}
			}
			else {
				//判断队员等级
				if (team.isTeamLeader(roleId)) {
					allRoles.addAll(team.getNormalMemberIds());
					lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoles));
					
					if (allRoles.size() < eventTimer.teamCount) {
						List<String> paras = new ArrayList<String>(1);
						paras.add(String.valueOf(eventTimer.teamCount));
						fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 150522, npcId, paras);	
						return false;
					}
					List<String> levelApplyNames = new ArrayList<String>();
					List<String> levelMaxApplyNames = new ArrayList<String>();
					for (Long mem : team.getNormalMemberIds()) {
						PropRole pRole = new PropRole(mem, true);
						if (pRole.getLevel() < eventTimer.teamLevelMin) {
							levelApplyNames.add(pRole.getName());
						}
						else if (pRole.getLevel() > eventTimer.teamLevelMax) {
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
						paras.add(String.valueOf(eventTimer.teamLevelMin));
						if (eventTimer.refreshGroup == 3) {
							for (Long mem : team.getNormalMemberIds()) {
								fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 160441, npcId, paras);
							}
						}
						else {
							for (Long mem : team.getNormalMemberIds()) {
								fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150523, npcId, paras);
							}
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
						paras.add(String.valueOf(eventTimer.teamLevelMax));
						for (Long mem : team.getNormalMemberIds()) {
							fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150525, npcId, paras);
						}
						
						return false;
					}
				}
			}
		}
		
		EventTimerNpcData eventData = BaoTuMapManager.getInstance().getEventTimerNpcData(eventTimer.refreshGroup);
		if (eventData == null) {
			return false;
		}
		
		EventTimerGroupData eventGroupData = eventData.getGroupDataById(eventTimer.id);
		if (eventGroupData == null) {
			return false;
		}
		
		//检查是否需要判断奖励次数
		if (eventTimer.havetimes == 1) {
			boolean havetime = false;
			for (Long roleId : allRoles) {
				if (TimerNpcService.checkEventNpcBattleTimes(eventGroupData, roleId, biaoId, eventTimer.getRefreshGroup())) {
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
		
		xbean.eventNpcInfoCol npcInfo = xtable.Eventnpcinfotable.get(biaoId);
		if (npcInfo == null) {
			TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",进入定时怪的战斗:")
					.append(",npcId:").append(npcId)
					.append(",npcKey:").append(npcKey)
					.append(",biaoId:").append(biaoId)
					.append(",npcInfo == null"));
			return false;
		}
		
		if (TimerNpcService.getInstance().getEventNpcState(npcInfo, npcKey) != TimerNpcService.CAN_BATTLE) {
			if (eventTimer.refreshGroup == 1 || eventTimer.refreshGroup == 2) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, /*170029*/145148, npcId, null);
			}
			else if (eventTimer.refreshGroup == 3) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 150213, npcId, null);
			}
			
			if (npcInfo.getNpcinfo().containsKey(Long.valueOf(npcKey))) {
				TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",找的怪在战斗中:")
						.append(npcInfo.getNpcinfo().get(npcKey).getBattletime()));
			}
			
			return false;
		}
		
		//判断npc是否需要进行匹配进入战斗
		if (eventTimer.match == 1) {
			long nowtime = System.currentTimeMillis();
			//在规定时间内点击npc进入战斗, 如果在规定时间还没有人点击,则第一个点击怪的进入战斗
			xbean.eventNpcInfo eInfo = npcInfo.getNpcinfo().get(npcKey);
			if (eInfo == null)
				return false;
			
			if ((nowtime - eInfo.getCreatetime()) < eventTimer.matchtime * 1000) {
				xbean.npcBattleInfoCol npcBattle = xtable.Npcbattleinfo.get(npcKey); 
				if (npcBattle == null) {
					npcBattle = xbean.Pod.newnpcBattleInfoCol();
					xtable.Npcbattleinfo.insert(npcKey, npcBattle);
				}
				npcBattle.getBattleroles().put(roleId, allRoles.size());
				
				Long onMatchNpcKey = xtable.Role2npcbattle.select(roleId);
				if (onMatchNpcKey != null) {
					TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",正在事件怪匹配中,取消原来的匹配:")
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
				long lasttime = (eInfo.getCreatetime() + eventTimer.matchtime * 1000) - nowtime;
				mkdb.Procedure.psendWhileCommit(allRoles, new SNpcBattleTime(npcId, npcKey, eventTimer.matchtime * 1000, lasttime));
				//返回,等匹配线程执行
				return true;
			}
			//过了匹配时间,直接往下走
			else {
				xbean.npcBattleInfoCol npcBattle = xtable.Npcbattleinfo.get(npcKey); 
				if (npcBattle != null) {
					if (npcBattle.getBattleroles().size() != 0) {
						TimerNpcService.logger.info(new StringBuilder().append("roleId:").append(roleId).append(",事件npc在需要匹配战斗:")
								.append(",npcId:").append(npcId)
								.append(",npcKey:").append(npcKey)
								.append(eInfo.getBattletime()).append(",配置的战斗人数是:").append(eventTimer.battletime));
//						return false; //FIXME 此处先记下日志,高并发时可能会问题,应该不是必出问题
					}
				}
			}
		}
		
		PEnterEventNpcBattle enterbattle = new PEnterEventNpcBattle(npcId, npcKey, roleId); 
		return enterbattle.call();
	}

}
