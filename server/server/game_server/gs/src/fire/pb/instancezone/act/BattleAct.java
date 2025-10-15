package fire.pb.instancezone.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;
import fire.msp.role.GSetNpcBattleInfo;
import fire.pb.GsClient;
import fire.pb.battle.BattleType;
import fire.pb.battle.PNewBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.Module;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.instancezone.conf.NpcConfig;
import fire.pb.instancezone.faction.FactionInstBattleEndHandler;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class BattleAct extends ServiceAct {
	public int battleId;

	public BattleAct(int actId, Map<String, Object> params) {
		super(actId, params);
		this.battleId = (Integer) params.get(ConfigParamName.BATTLE_ID);
	}

	@Override
	public int getType() {
		return ActionConfig.TYPE_BATTLE;
	}

	@Override
	public boolean defaultprocess(InstanceZone instzone) {
		if (instzone instanceof FactionInstZone) {
			NpcConfig npcCfg = instzone.getZoneConfig().npcs.get(npcbaseId);
			if (npcCfg.multibattle == 1) {
				//判断队伍里是否有人存在不能打boss的buff
				List<Long> allRoleIDs = new LinkedList<Long>();
				Team team = TeamManager.getTeamByRoleId(roleId);
				if (team != null&&team.isNormalMember(roleId)) {
					allRoleIDs.addAll(team.getNormalMemberIds());
				}
				else {
					allRoleIDs.add(roleId);
				}
				
				Map<Long, List<String>> paras = new HashMap<Long, List<String>>();
				for (Long rid : allRoleIDs) {
					BuffAgent agent = new BuffRoleImpl(rid);
					if (agent.existBuff(BuffConstant.CONTINUAL_CLANBOSS_SPASE)) {
						ConstantlyBuff buff = agent.getBuff(BuffConstant.CONTINUAL_CLANBOSS_SPASE);
						if (buff != null) {
							List<String> para = paras.get(rid);
							if (para == null) {
								para = new ArrayList<String>(1);
							}
							
//							long remianingTime = buff.getRemianingTime(agent);
							para.add(xtable.Properties.selectRolename(rid));
//							para.add(String.valueOf(remianingTime/1000));
							paras.put(rid, para);
						}
					}
				}
				
				if (!paras.isEmpty()) {
					for (Long mem : allRoleIDs) {
						for (List<String> para : paras.values()) {
							MessageMgr.psendMsgNotify(mem, 166073, para);
						}
					}
					return false;
				}
			}
			
			Module.getInstance().setDynamicNpcState(instzone.getLandkey(), npckey, 1);
			PNewBattle pnewbattle = new PNewBattle(roleId, battleId,
					new FactionInstBattleEndHandler(roleId, npckey,
							npcCfg.awardid, instzone.getLandkey(),
							instzone.getZoneConfig(),
							instzone.getCurActiveSave()));
			pnewbattle.setInstNpcKey(npckey);
			pnewbattle.setLandkey(instzone.getLandkey());
			pnewbattle.setBattleType(BattleType.BATTLE_INST_BATTLE);

			mkdb.Procedure.pexecuteWhileCommit(new Runnable() {
				@Override
				public void run() {
					mkdb.Procedure.execute(pnewbattle, new SetBattleState(instzone.getLandkey(), npckey, roleId));
				}
			});
		} else {
			PNewBattle pnewbattle = new PNewBattle(roleId, battleId,
					instzone.getBattleEndHandler(getNpckey()));
			pnewbattle.setMonsterNpckey(getNpckey());
			if (mkdb.Transaction.current() != null)
				mkdb.Procedure.pexecuteWhileCommit(pnewbattle);
			else
				pnewbattle.submit();
		}

		return true;
	}
	
	static class SetBattleState implements Procedure.Done<PNewBattle> {
		private final long landkey;
		private final long npckey;
		private final long roleId;
		public SetBattleState(long landkey, long npckey, long roleId) {
			this.landkey = landkey;
			this.npckey = npckey;
			this.roleId = roleId;
		}

		@Override
		public void doDone(PNewBattle p) {
			if (p.isSuccess())
				//标识npc在战斗
				GsClient.pSendWhileCommit(new GSetNpcBattleInfo(npckey, 1));
			else {
				Module.getInstance().setDynamicNpcState(landkey, npckey, 0);
				
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 141163, null);
				fire.pb.instancezone.Module.logger.info(new StringBuilder().append("landkey:").append(landkey)
						.append(",roleid").append(roleId)
						.append(",npckey:")
						.append(npckey).append(",发现进入战斗失败的情况:"));
			}
		}
	}
}