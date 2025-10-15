package fire.pb.battle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mkdb.Procedure;
import fire.log.YYLogger;
import fire.log.beans.OpFightBean;
import fire.msp.role.GSetRoleBattleInfo;
import fire.pb.GsClient;
import fire.pb.attr.AttrType;
import fire.pb.battle.ai.BattleAIManager;
import fire.pb.battle.newhand.CReqNewHandBattle;
import fire.pb.buff.BuffPetImpl;
import fire.pb.effect.PRefreshRoleAttr;
import fire.pb.gm.GMInteface;
import fire.pb.item.BagTypes;
import fire.pb.pet.PSetFightPetProc;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.SkillRole;
import fire.pb.state.EntryState;
import fire.pb.state.IState;
import fire.pb.state.State;
import fire.pb.state.StateManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class PBattleEnd extends Procedure {
	final int result;

	public PBattleEnd(final long battleID, final int result) {
		this.battleID = battleID;
		this.result = result;
	}

	public String GetResultStr(int nindex) {
		if (result == BattleField.BATTLE_WIN)
			return nindex < BattleField.GUEST_FIRST_ID ? YYLogger.SUCC_FLAG : YYLogger.FAIL_FLAG;
		if (result == BattleField.BATTLE_LOSE)
			return nindex < BattleField.GUEST_FIRST_ID ? YYLogger.FAIL_FLAG : YYLogger.SUCC_FLAG;
		return YYLogger.DRAW_FLAG;
	}

	public int ResultType(xbean.BattleInfo battle, int nindex) {
		if (result == BattleField.BATTLE_WIN)
			return OpFightBean.OpFightBean_FailDesc_BattleEnd;
		if (result == BattleField.BATTLE_LOSE)
		{
			if(battle.getRoundresult().get(nindex)!=null&&(battle.getRoundresult().get(nindex)&ResultType.RESULT_RUNAWAY)>0)
				return OpFightBean.OpFightBean_FailDesc_Runaway;
			else
				return  OpFightBean.OpFightBean_FailDesc_Faild;
		}
		return OpFightBean.OpFightBean_FailDesc_Feedback;
	}

	@Override
	public boolean process() {
		final xbean.BattleInfo battle = xtable.Battle.get(battleID);
		if (null == battle)
			return false;
		battle.setBattleresult(result);
		// 锁所有队伍的锁
		Set<Long> teamids = new HashSet<Long>();
		int nmapid = -1;
		for (Map.Entry<Long, Integer> rid : battle.getRoleids()
				.entrySet()/* long rid : battle.getRoleids().keySet() */) {
			if (nmapid == -1) {
				fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance().getRoleByID(rid.getKey());
				if (role == null)
					continue;
				nmapid = role.getMapId();
			}

			Long tid = xtable.Roleid2teamid.select(rid.getKey());
			if (tid != null) {
				teamids.add(tid);
				if (rid.getValue() < BattleField.GUEST_FIRST_ID) {
					YYLogger.fightLog(rid.getKey(), new OpFightBean(tid, BattleField.getHostRoleIDs(battle).size(),
							nmapid, GetResultStr(rid.getValue()), ResultType(battle, rid.getValue()), this.battleID));
				} else {
					YYLogger.fightLog(rid.getKey(), new OpFightBean(tid, BattleField.getGuestRoleIDs(battle).size(),
							nmapid, GetResultStr(rid.getValue()), ResultType(battle, rid.getValue()), this.battleID));
				}
			} else{
				YYLogger.fightLog(rid.getKey(), new OpFightBean(-1, 1, nmapid, GetResultStr(rid.getValue()), ResultType(battle, rid.getValue()), this.battleID));
			}
		}
		lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, teamids));
		// 锁所有角色的锁
		Set<Long> rolesets = new HashSet<Long>();
		rolesets.addAll(battle.getRoleids().keySet());
		rolesets.addAll(battle.getAudience().keySet());
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, rolesets));

		final SSendBattleEnd snd = new SSendBattleEnd();

		// 处理战斗结束AI
		snd.aiactions = BattleAIManager.getInstance().getActionWhileBattleEnd(battle);
		// 出战斗时清理buff. addBycn:为了解决师门的一个bug,清除buff提前到roleid2battleid清除之前
		for (Fighter fighter : battle.getFighterobjects().values()) {
			if (fighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER)
				continue;// 怪物不用移除buff，战斗结束就消失了
			boolean succ = fighter.getBuffAgent().removeBuffsWhileBattleEndWithSP();
			if (!succ) {
				StringBuffer sb = new StringBuffer();
				sb.append("(").append(fighter.getFighterBean().getBattleindex()).append(",")
						.append(fighter.getFighterBean().getFightername()).append(",")
						.append(fighter.getFighterBean().getUniqueid()).append(",")
						.append(fighter.getFighterBean().getFightertype()).append(")");
				if (BattleField.logger.isDebugEnabled()) {
					BattleField.logger.debug("战斗者" + sb + "出战斗时buff清除失败。");
				}
			}
		}

		// 为了检测出战斗buff不清楚的情况，在此再检查身上所有的宠物，清除掉出战斗的buff
		for (long roleID : battle.getRoleids().keySet()) {
			if ((CReqNewHandBattle.BASE_BATTLEID <= battle.getConfigid())
					&& (battle.getConfigid() <= CReqNewHandBattle.BASE_BATTLEID + 9)) {
				Map<Integer, Integer> sextskill = new HashMap<Integer, Integer>();
				{// 这里估计应该是只有1个角色
					SkillRole srole = new SkillRole(roleID);
					srole.addExtSkillWithSP(sextskill);
				}
			}

			try {
				PetColumn petcol = new PetColumn(roleID, PetColumnTypes.PET, false);
				for (xbean.PetInfo petinfo : petcol.getPetsMap().values()) {
					BuffPetImpl buffpet = new BuffPetImpl(roleID, petinfo.getKey());
					fire.pb.skill.Result result = buffpet.removeBuffsWhileBattleEnd();
					if (!result.getDeletedBuffs().isEmpty()) {
						int rolefid = battle.getRoleids().get(roleID);
						Fighter petfighter = battle.getFighterobjects().get(rolefid + 5);
						StringBuffer sb = new StringBuffer("");
						if (petfighter != null)
							sb.append("(").append(petfighter.getFighterBean().getBattleindex()).append(",")
									.append(petfighter.getFighterBean().getFightername()).append(",")
									.append(petfighter.getFighterBean().getUniqueid()).append(",")
									.append(petfighter.getFighterBean().getFightertype()).append(")");
						if (BattleField.logger.isDebugEnabled()) {
							BattleField.logger.debug("角色" + roleID + "的宠物" + petinfo.getName() + "出战斗时buff未清除,buffids:"
									+ result.getDeletedBuffs() + ",petfighter=" + sb);
						}
						buffpet.psendSBuffChangeResult(result);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// 客户端冒字显示。liuxinhua edit 于 2015-10-10
		psendWhileCommit(battle.getRoleids().keySet(), snd);

		final SSendBattleEnd audiencesnd = new SSendBattleEnd();
		psendWhileCommit(battle.getAudience().keySet(), audiencesnd);

		// 处理失败的怪物（包括主客方）
		BattleField.processFaileMonsters(battle, true);

		boolean xhwBattle = false;

		// 删除战斗者角色
		for (Map.Entry<Long, Integer> role : battle.getRoleids().entrySet()) {
			try {
				// 处理耐久消耗
				final long roleid = role.getKey();
				xbean.Fighter fighter = battle.getFighters().get(role.getValue());
				if (fighter != null) {
					if (battle.getBattletype() != BattleType.BATTLE_PVP && !xhwBattle) {
						fire.pb.item.Equip equip = (fire.pb.item.Equip) fire.pb.item.Module.getInstance()
								.getItemMaps(roleid, BagTypes.EQUIP, false);
						equip.battleEnd(battle.getConfigid(), fighter.getActioncount());
					}
					Integer fightPetKey = xtable.Properties.selectFightpetkey(roleid);
					if (fightPetKey.intValue() != fighter.getInipetkey()) {
						boolean on = (fighter.getInipetkey() != -1);
						pexecuteWhileCommit(new PSetFightPetProc(roleid, fighter.getInipetkey(), on));
					}
				}

				xtable.Roleid2battleid.remove(roleid);
				// 出战斗发送9屏协议
				GsClient.pSendWhileCommit(new GSetRoleBattleInfo(roleid, 0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 删除观战者
		for (long roleid : battle.getAudience().keySet()) {
			try {
				xtable.Watcherid2battleid.remove(roleid);
				GsClient.pSendWhileCommit(new GSetRoleBattleInfo(roleid, 0));

				for (BattleEndHandler handler : battle.getBattleendhandlers()) {
					handler.handleWatcherOutBattle(roleid);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		battle.getAudience().clear();
		battle.getWatchers().clear();
		BattleField.cancelTimeOutCheck(battle);
		xtable.Battle.remove(battle.getBattleid());

		boolean roll = false;
		try {
			for (BattleEndHandler handler : battle.getBattleendhandlers()) {
				handler.process(battleID, result);

				if (handler.getNeedRoll()) {
					roll = true;
				}
			}

		} catch (Exception e) {
			BattleField.logger.debug("BattleEndHandler handle failed", e);
			return false;
		}

		if (roll == true) {
			snd.aiactions.put(-1, 6000); // 告诉客户端不要退场景,有ROLL点触发 by changhao
		}

		// 执行战斗结束时需要延时执行的Procedure
		for (xbean.BattleEndProcedure p : battle.getEndprocedures())
			p.getEndprocedure().call();
		battle.getEndprocedures().clear();

		for (Map.Entry<Long, Integer> role : battle.getRoleids().entrySet()) {
			outBattleProcess(battle, role.getKey());
			fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(role.getKey(),true);
			xbean.Fighter fighter = battle.getFighters().get(role.getValue());
			
			if(checkattrerror(fighter, erole, AttrType.HIT_RATE)
					||checkattrerror(fighter, erole, AttrType.ATTACK)
					||checkattrerror(fighter, erole, AttrType.DEFEND)
					||checkattrerror(fighter, erole, AttrType.MAGIC_ATTACK)
					||checkattrerror(fighter, erole, AttrType.MAGIC_DEF)
					||checkattrerror(fighter, erole, AttrType.SPEED)
					||checkattrerror(fighter, erole, AttrType.DODGE_RATE)
					||checkattrerror(fighter, erole, AttrType.MEDICAL))
			{
				GMInteface.outputattr(role.getKey());
			}
		}

		BattleField.logger.info(new StringBuilder("战斗ID为\t").append(battleID).append("\t的战斗结束了"));

		// 我们要处理战斗中退队请求 现在开始 by changhao
		for (long roleid : battle.getQuitteamroleids()) {
			pexecuteWhileCommit(new fire.pb.team.PQuitTeamProc(roleid));
		}

		// 我们要处理战斗中退队请求 现在开始 by changhao
		for (long roleid : battle.getAbsentteamroleids()) {
			pexecuteWhileCommit(new fire.pb.team.PAbsentReturnTeam(roleid, 1));
		}

		audiencesnd.aiactions.putAll(snd.aiactions);
		if (roll == true) {
			audiencesnd.aiactions.remove(-1); // 观战者不等待ROLL点 by changhao
		}

		return true;
	}

	private static boolean checkattrerror(xbean.Fighter fighter, fire.pb.effect.RoleImpl erole, int attrtype) {
		return (fighter.getInitattrs().get(attrtype) != erole.getAttrById(attrtype) && erole.getAttrById(attrtype) < 0);
	}
	
	private final long battleID;

	/** 逃跑（暂时还未挪到此） 和 普通战斗结束时 通用的出战斗处理放在此 */
	public static void outBattleProcess(xbean.BattleInfo battle, final long roleId) {
		try {
			
			new mkdb.Procedure()
			{
				@Override
				protected boolean process()
				{
					Module.getInstance().checkBattleBuff(roleId);
					return true;
				}
			}.call();
			
			new PRefreshRoleAttr(roleId).call();// 刷新一遍属性，这里同步执行
			// 有伤发送消息提示
			if (xtable.Properties.selectWound(roleId) > 0) {
				xbean.BeginnerTip tip = xtable.Beginnertip.get(roleId);
				if (tip == null) {
					tip = xbean.Pod.newBeginnerTip();
					xtable.Beginnertip.insert(roleId, tip);
				}
				if (!tip.getTips().containsKey(69)) {
					tip.getTips().put(69, 1);
				} else
					MessageMgr.psendMsgNotifyWhileCommit(roleId, 142794, null);
			}

			xtable.Properties.selectFightpetkey(roleId);
			// 处理状态的转移
			IState state = StateManager.getStateByRoleId(roleId);
			if (!(state instanceof EntryState))
				state.trigger(State.TRIGGER_BATTLE_END);
			Team team = TeamManager.selectTeamByRoleId(roleId);
			if (team != null) {
				team.notifyHpMpChange(roleId);
				if (team.getTeamLeaderId() == roleId)
					TeamManager.processReturnMembers(team.teamId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
