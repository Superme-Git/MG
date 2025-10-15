package fire.pb.activity.timernpc;

import java.util.ArrayList;
import java.util.List;

import fire.msp.role.GSetNpcBattleInfo;
import fire.pb.GsClient;
import fire.pb.battle.PNewBattle;
import fire.pb.main.ConfigManager;
import fire.pb.map.EventTimerNpc;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.mission.treasuremap.EventNpcBattleEndHandler;
import fire.pb.npc.SMacthResult;
import fire.pb.npc.SNpcShare;
import fire.pb.scene.movable.NPC;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.Misc;
import mkdb.Procedure;

public class PEnterNpcBattle extends Procedure {
	private final int npcid;
	private final long npckey;
	private final int actid;
	
	private long roleid;
	
	public PEnterNpcBattle(int npcid, long npckey, int actid) {
		this.npcid = npcid;
		this.npckey = npckey;
		this.actid = actid;
		this.roleid = 0;
	}

	public PEnterNpcBattle(int npcid, long npckey, int actid, long roleid) {
		this.npcid = npcid;
		this.npckey = npckey;
		this.actid = actid;
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		TimerNpcData timerData = TimerNpcService.getInstance().getNpcData(actid);
		if (timerData == null) {
			return false;
		}
		
		//检查NPC状态
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actid);
		if (npcInfo == null) {
			return false;
		}
		if (!npcInfo.getNpcinfo().containsKey(npckey)) {
			return false;
		}
		//怪物已经消失
		xbean.timerNpcInfo tInfo = npcInfo.getNpcinfo().get(npckey);
		if (tInfo == null) {
			return false;
		}
		if (tInfo.getNpcstatus() == 2) {
			return false;
		}
		
		if (timerData.match == 1 && roleid == 0) {
			//从匹配列表里随机出一个玩家的队伍进入战斗
			xbean.npcBattleInfoCol npcBattle = xtable.Npcbattleinfo.get(npckey); 
			if (npcBattle == null)
				return false;
			if (npcBattle.getBattleroles().size() == 0)
				return false;
			//随机一个玩家
			roleid = Misc.getRandom(npcBattle.getBattleroles().keySet());
			if (roleid <= 0) {
				TimerNpcService.logger.info(new StringBuilder().append("进入定时怪的战斗:")
						.append(",npcId:").append(npcid)
						.append(",npcKey:").append(npckey)
						.append(",roleid <= 0:").append(roleid));
				return false;
			}
			
			TimerNpcService.logger.info(new StringBuilder().append("随机到:")
					.append(roleid).append("进入战斗")
					.append(",npcId:").append(npcid)
					.append(",npcKey:").append(npckey));
			
			//给其他角色发送没有进入战斗的提示
			for (Long rid : npcBattle.getBattleroles().keySet()) {
				xtable.Role2npcbattle.remove(rid);
				if (rid != roleid) {
					List<Long> allRoles = new ArrayList<Long>();
					Team team = TeamManager.selectTeamByRoleId(rid);
					if (team == null) {
						continue;
					}
					
					allRoles.addAll(team.getNormalMemberIds());
					mkdb.Procedure.psend(allRoles, new SMacthResult(npckey, 0));
					
					//给队伍中的人员发送没有匹配到npc的信息
					for (Long rrr: allRoles) {
						MessageMgr.sendMsgNotify(rrr, 170051, null);
					}
				}
			}
			
			List<Long> allRoles = new ArrayList<Long>();
			Team team = TeamManager.selectTeamByRoleId(roleid);
			if (team != null) {
				allRoles.addAll(team.getNormalMemberIds());
				
				int teamsize = npcBattle.getBattleroles().get(roleid);
				if (teamsize != allRoles.size()) {
					TimerNpcService.logger.info(new StringBuilder().append("随机到:")
							.append(roleid).append(",队伍人数发生变化")
							.append(",npcId:").append(npcid)
							.append(",npcKey:").append(npckey));
					
					mkdb.Procedure.psend(allRoles, new SMacthResult(npckey, 0));
					
					//给队伍中的人员发送队伍信息发生变化的消息
					for (Long rrr : allRoles) {
						MessageMgr.sendMsgNotify(rrr, 170053, null);;
					}
					return false;
				}
				
				mkdb.Procedure.psend(allRoles, new SMacthResult(npckey, 1));
			}
			
			xtable.Npcbattleinfo.remove(npckey);
		}
		
		//正在战斗中
		if (timerData.battletime > 1) {
			tInfo.setBattletime(tInfo.getBattletime() + 1);
		}
		tInfo.setNpcstatus(1);
		
		SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(npcid);
		//获取战斗id
		int battleId = npc.battleinfo;
		if (battleId == -1) {
			TimerNpcService.logger.info(new StringBuilder().append("进入定时怪的战斗:")
					.append(",npcId:").append(npcid)
					.append(",npcKey:").append(npckey)
					.append(",battleId == -1"));
			return false;
		}
		
		if (tInfo.getBattletime() >= timerData.battletime) {
			if (timerData.xiaoshi == 1) {//怪物消失
				TimerNpcService.getInstance().removeMonster(npcInfo, npckey);
			}
			else
				GsClient.pSendWhileCommit(new GSetNpcBattleInfo(npckey, 1));
		}
		else if (timerData.battletime == 1) {
			if (timerData.xiaoshi == 1) {//怪物消失
				TimerNpcService.getInstance().removeMonster(npcInfo, npckey);
			}
			else {
				tInfo.setRoleid(roleid);
				GsClient.pSendWhileCommit(new GSetNpcBattleInfo(npckey, 1));
			}
		}
		
		NPC npcMainMonster = fire.pb.scene.manager.SceneNpcManager.getInstance().getNpcByKey(npckey);
		if (npcMainMonster == null) {
			return false;
		}
		
		if (timerData.eventbiaoid > 0) {
			EventTimerNpc eventTimer = BaoTuMapManager.getInstance().getTimerNpc(timerData.eventbiaoid);
			if (eventTimer == null) {
				return false;
			}
			//开始战斗
			EventNpcBattleEndHandler handler = new EventNpcBattleEndHandler(roleid, timerData.eventbiaoid, 
					eventTimer.getRefreshGroup(), npckey, actid);
			PNewBattle battle = new PNewBattle(roleid, battleId, fire.pb.battle.BattleField.GUEST_FIRST_ID, npcMainMonster.getName(), handler);
			return battle.call();
		}
		else {
			//开始战斗
			TimerNpcBattleEndHandler handler = new TimerNpcBattleEndHandler(actid, npcid, npckey, roleid);
			PNewBattle battle = new PNewBattle(roleid, battleId, fire.pb.battle.BattleField.GUEST_FIRST_ID, npcMainMonster.getName(), handler);
			return battle.call();
		}
	}
	
	
}
