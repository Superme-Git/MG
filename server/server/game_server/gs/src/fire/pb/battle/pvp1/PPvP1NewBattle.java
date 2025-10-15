package fire.pb.battle.pvp1;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mkdb.Procedure;
import xbean.PetInfo;
import fire.pb.PropRole;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.BattleConfig;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.BattleType;
import fire.pb.battle.Fighter;
import fire.pb.battle.HireNpcManager;
import fire.pb.battle.PNewBattle;
import fire.pb.battle.watch.PEndWatchBattle;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import gnet.link.Onlines;

public class PPvP1NewBattle extends PNewBattle {

	public PPvP1NewBattle(final long roleID, final long otherRoleID, int gradeId) {
		super(roleID, otherRoleID, BattleType.BATTLE_PVP, new SPvPBattleEndHandler(gradeId, roleID, otherRoleID));
	}

	@Override
	public boolean process() {

		Set<Long> watchBattles = new HashSet<Long>();
		Long hostWatchBattleId = xtable.Watcherid2battleid.select(hostRoleID);
		if (hostWatchBattleId != null)
			watchBattles.add(hostWatchBattleId);
		Long guestWatchBattleId = xtable.Watcherid2battleid.select(guestRoleID);
		if (guestWatchBattleId != null)
			watchBattles.add(guestWatchBattleId);

		if (watchBattles.isEmpty() == false) {
			lock(xtable.Locks.BATTLE, watchBattles);
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, new Object[] { hostRoleID, guestRoleID }));
		}

		// 停止观战
		if (hostWatchBattleId != null) {
			boolean succ = new PEndWatchBattle(hostRoleID).call();
			if (!succ)
				return false;
		}

		if (guestWatchBattleId != null) {
			boolean succ = new PEndWatchBattle(guestRoleID).call();
			if (!succ)
				return false;
		}

		if (super.process() == false)
			return false; // TODO 进入战斗失败是否要重新回到等待队列？

		return true;
	}

	/**
	 * 伙伴助战数
	 */
	@Override
	protected int processHireNpcMonster(xbean.BattleInfo battle, List<Long> roleids, BattleConfig battleConfig, boolean ishost) {
		return HireNpcManager.processBuffMonsters(battle, roleids, 4, ishost);
	}

	/**
	 * 获取光环
	 */
	@Override
	protected int getTeamFormation(long roleId) {
		return xtable.Properties.selectDefultform(roleId);
	}

	private static class SPvPBattleEndHandler extends BattleEndHandler {

		public final int gradeId;
		public final long hostRoleId;
		public final long guestRoleId;

		public SPvPBattleEndHandler(int gradeId, long hostRoleId, long guestRoleId) {
			this.gradeId = gradeId;
			this.hostRoleId = hostRoleId;
			this.guestRoleId = guestRoleId;
		}


		@Override
		protected boolean handleDeath() {

			// 没有任何惩罚,要把人物状态恢复原样
			Collection<xbean.Fighter> fighters = battle.getFighters().values();
			Set<Long> hostIds = new HashSet<Long>();
			Set<Long> guestIds = new HashSet<Long>();

			// 血和魔全部还原
			for (xbean.Fighter f : fighters) {
				if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE) {
					xbean.Properties prop = xtable.Properties.get(f.getUniqueid());
					prop.setHp(f.getInihp());
					prop.setMp(f.getInimp());
					prop.setSp(f.getInisp());

					fire.pb.battle.Fighter fighterObject = battle.getFighterobjects().get(f.getBattleindex());
					SRefreshRoleData srrd = new SRefreshRoleData();
					srrd.datas.put(AttrType.HP, (float) f.getInihp());
					srrd.datas.put(AttrType.MP, (float) f.getInimp());
					srrd.datas.put(AttrType.SP, (float) f.getInisp());
					srrd.datas.put(AttrType.UP_LIMITED_HP, (float) fighterObject.getEffectRole().getUplimithp());
					Onlines.getInstance().send(f.getUniqueid(), srrd);
					if (f.getIshost()) {
						hostIds.add(f.getUniqueid());
					} else {
						guestIds.add(f.getUniqueid());
					}
				}
				if (f.getFightertype() == xbean.Fighter.FIGHTER_PET) {
					int masterIndex = f.getBattleindex() - 5;
					int petkey = new PropRole(battle.getFighters().get(masterIndex).getUniqueid(), true).getFightpetkey();
					recoverPetHpMPWithSP(battle, f.getBattleindex(), petkey);
				}
			}

			boolean hostRunaway = false;
			boolean guestRunaway = false;
			if (hostIds.contains(hostRoleId) == false) {
				hostRunaway = true;
			}
			if (guestIds.contains(guestRoleId) == false) {
				guestRunaway = true;
			}

			switch (battle.getBattleresult()) {
			case BattleField.BATTLE_WIN:
				mkdb.Procedure.pexecuteWhileCommit(new PPvP1ProcessBattleResult(gradeId, hostRoleId, guestRoleId, guestRunaway));
				break;
			case BattleField.BATTLE_LOSE:
				mkdb.Procedure.pexecuteWhileCommit(new PPvP1ProcessBattleResult(gradeId, guestRoleId, hostRoleId, hostRunaway));
				break;
			case BattleField.BATTLE_DRAW:
				// 平局没有任何处理
				break;
			default:
				break;
			}

			// 活动结束,离开场景
			if (PvP1Control.getInstance().isFightTime() == false) {
				transformRoleids.addAll(battle.getRoleids().keySet());
			}

			return true;
		}

		@Override
		protected void transformAfterBattle() {
			for (long roleId : transformRoleids) {
				PvP1Control.getInstance().forceLeave(roleId);
			}
		}

		@Override
		protected boolean handleCamera() {
			return true;
		}
		
		@Override
		protected boolean handleAward() {
			return true;
		}

		@Override
		public boolean handleOutBattle(int index, int petkey) throws Exception {

			xbean.Fighter f = battle.getFighters().get(index);
			final long roleid = f.getUniqueid();
			if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE) {
				// 玩家的hpmp都恢复
				xbean.Properties prop = xtable.Properties.get(roleid);
				prop.setSp(f.getInisp());
				Fighter ofighter = battle.getFighterobjects().get(index);
				ofighter.getEffectRole().setHp(f.getInihp());
				ofighter.getEffectRole().setMp(f.getInimp());
				fire.pb.battle.Fighter fighterObject = battle.getFighterobjects().get(f.getBattleindex());
				SRefreshRoleData srrd = new SRefreshRoleData();
				srrd.datas.put(AttrType.HP, (float) ofighter.getEffectRole().getHp());
				srrd.datas.put(AttrType.MP, (float) ofighter.getEffectRole().getMp());
				srrd.datas.put(AttrType.SP, (float) ofighter.getEffectRole().getSp());
				srrd.datas.put(AttrType.UP_LIMITED_HP, (float) fighterObject.getEffectRole().getUplimithp());
				Procedure.psendWhileCommit(roleid, srrd);
				// 宠物的也要恢复
				final int petIndex = f.getBattleindex() + 5;
				// 如果玩家的宠物也参战了
				if (battle.getFighters().containsKey(petIndex)) {
					recoverPetHpMPWithSP(battle, petIndex, petkey);
				}

				// 活动结束离开场景
				if (PvP1Control.getInstance().isFightTime() == false) {
					PvP1Control.getInstance().forceLeave(roleid);
				}
				// 逃跑结算
				// 1v1:PPvP1ProcessBattleResult中处理结算
//				mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
//					@Override
//					public boolean process() {
//						PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(gradeId);
//						if (race == null)
//							return false;
//						if (race.getXRace() == null)
//							return false;
//						PvP1RoleProxy role = PvP1RoleProxy.getPvP1RoleProxy(roleid, false);
//						role.onBattleEnd(false, (roleid == hostRoleId ? guestRoleId : hostRoleId), 0, race);
//						return true;
//					}
//				});

			}
			if (f.getFightertype() == xbean.Fighter.FIGHTER_PET) {
				recoverPetHpMPWithSP(battle, index, petkey);
			}

			return super.handleOutBattle(index, petkey);
		}

		public static void recoverPetHpMPWithSP(xbean.BattleInfo battleInfo, int petIndex, int petkey) {

			xbean.Fighter petFighter = battleInfo.getFighters().get(petIndex);
			if (petFighter == null) {
				return;
			}
			xbean.Fighter masterFighter = battleInfo.getFighters().get(petFighter.getBattleindex() - 5);
			if (masterFighter == null) {
				return;
			}
			long masterID = masterFighter.getUniqueid();
			SRefreshPetData srpd = new SRefreshPetData();
			PetColumn petColumn = new PetColumn(masterID, PetColumnTypes.PET,false);
			PetInfo petInfo = petColumn.getPetInfo(petkey);
			if (petInfo == null)
				return;
			petInfo.setHp(petFighter.getInihp());
			petInfo.setMp(petFighter.getInimp());
			srpd.columnid = PetColumnTypes.PET;
			srpd.petkey = petkey;
			srpd.datas.put(AttrType.HP, (float) petInfo.getHp());
			srpd.datas.put(AttrType.MP, (float) petInfo.getMp());
			Onlines.getInstance().send(masterID, srpd);
		}

		/**
		 * 观战者离开战斗
		 * @param roleId
		 */
		@Override
		public void handleWatcherOutBattle(long roleId) {
			// 活动结束离开场景
			if (PvP1Control.getInstance().isFightTime() == false) {
				PvP1Control.getInstance().forceLeave(roleId);
			}
		}
	}
}
