package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.List;

import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.battle.PNewBattle;
import fire.pb.main.ConfigManager;
import fire.pb.map.EventTimerNpc;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.mission.treasuremap.EventNpcBattleEndHandler;
import fire.pb.npc.SMacthResult;
import fire.pb.npc.SNpcShare;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.Misc;
import mkdb.Procedure;

public class PEnterEventNpcBattle extends Procedure {
	private final int npcid;
	private final long npckey;
	
	private long roleid;
	
	public PEnterEventNpcBattle(int npcid, long npckey) {
		this.npcid = npcid;
		this.npckey = npckey;
		this.roleid = 0;
	}

	public PEnterEventNpcBattle(int npcid, long npckey, long roleid) {
		this.npcid = npcid;
		this.npckey = npckey;
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		int biaoId = TimerNpcService.getInstance().getEventId(npcid);
		//事件错误
		if (biaoId == -1) {
			return false;
		}
		
		EventTimerNpc eventTimer = BaoTuMapManager.getInstance().getTimerNpc(biaoId);
		if (eventTimer == null) {
			return false;
		}
		
		xbean.eventNpcInfoCol npcInfo = xtable.Eventnpcinfotable.get(biaoId);
		if (npcInfo == null) {
			return false;
		}
		
		if (TimerNpcService.getInstance().getEventNpcState(npcInfo, npckey) != TimerNpcService.CAN_BATTLE) {
			TimerNpcService.logger.info(new StringBuilder().append("进入事件怪的战斗:")
					.append(",npcId:").append(npcid)
					.append(",npcKey:").append(npckey)
					.append(",此怪已经在战斗了:").append(roleid));
			return false;
		}
		
		if (eventTimer.match == 1 && roleid == 0) {
			//从匹配列表里随机出一个玩家的队伍进入战斗
			xbean.npcBattleInfoCol npcBattle = xtable.Npcbattleinfo.get(npckey); 
			if (npcBattle == null)
				return false;
			if (npcBattle.getBattleroles().size() == 0)
				return false;
			//随机一个玩家
			roleid = Misc.getRandom(npcBattle.getBattleroles().keySet());
			if (roleid <= 0) {
				TimerNpcService.logger.info(new StringBuilder().append("进入事件怪的战斗:")
						.append(",npcId:").append(npcid)
						.append(",npcKey:").append(npckey)
						.append(",roleid <= 0:").append(roleid));
				return false;
			}
			
			TimerNpcService.logger.info(new StringBuilder().append("随机事件怪到:")
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
					TimerNpcService.logger.info(new StringBuilder().append("随机到事件怪:")
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
		TimerNpcService.getInstance().setEventNpcState(npcInfo, npckey, roleid, TimerNpcService.IN_BATTLE,
				eventTimer.getXiaoshi());
		
		SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(npcid);
		//获取战斗id
		int battleId = npc.battleinfo;
		if (battleId == -1) {
			TimerNpcService.logger.info(new StringBuilder().append("进入事件怪的战斗:")
					.append(",npcId:").append(npcid)
					.append(",npcKey:").append(npckey)
					.append(",battleId == -1"));
			return false;
		}
		
		//开始战斗
		EventNpcBattleEndHandler handler = new EventNpcBattleEndHandler(roleid, biaoId, eventTimer.getRefreshGroup(), npckey);
		PNewBattle battle = new PNewBattle(roleid, battleId, handler);
		return battle.call();
	}
}
