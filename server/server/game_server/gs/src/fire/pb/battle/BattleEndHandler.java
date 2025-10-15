
package fire.pb.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import xbean.BattleInfo;
import xbean.PetInfo;
import fire.pb.DataInit;
import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.effect.PetImpl;
import fire.pb.effect.RoleImpl;
import fire.pb.item.BagTypes;
import fire.pb.item.SEquipNaiJiuXiaoHao;
import fire.pb.item.SRefreshNaiJiu;
import fire.pb.map.SMineArea;
import fire.pb.map.SShowAreainfo;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.scene.movable.NPC;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import gnet.link.Onlines;

/**处理战斗结束后的特殊情况*/
public abstract class BattleEndHandler {

	private static final Logger logger = Logger.getLogger("BATTLE");

	public static final java.util.Map<Integer, SMineArea> hideMapConfigs = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.SMineArea.class);

	public static final java.util.Map<Integer, SShowAreainfo> showMapConfigs = fire.pb.main.ConfigManager.getInstance().getConf(
			fire.pb.map.SShowAreainfo.class);

	protected BattleInfo battle;
	protected int baseMsgid = 141682;
	protected List<Long> transformRoleids = new ArrayList<Long>();// 死亡后需要跳转的roleid	
	protected boolean needRoll;
	public BattleEndHandler() {

		super();
		
		needRoll = false;
	}

	/**战斗结束后的处理*/
	public final boolean process(long battleId, int battleResult) throws Exception {
				
		preProcess();
		processFriendlyDegrees();
		return processDeath();
	}

	protected void preProcess()
	{
		for (xbean.Fighter xf : battle.getFighters().values()) {
			if (xf.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				PNewBattle.checkTimeoutEquip(xf.getUniqueid());
		}
	}
	
	/**
	 * 加好友度
	 */
	protected void processFriendlyDegrees() {

		// 一些战斗类型不加好友度
		switch (battle.getBattletype()) {
		case BattleType.BATTLE_HIDEAREA:
			return;
		}

		// 胜利者加好友度
		List<xbean.Fighter> fighters = getWinRoles();
		if (fighters.isEmpty()) {
			return;
		}
		List<Long> roleIds = new ArrayList<Long>();
		for (xbean.Fighter f : fighters) {
			roleIds.add(f.getUniqueid());
		}
		// 加好友
		mkdb.Procedure proc = new fire.pb.friends.PAddFriendlyDegrees(roleIds);
		mkdb.Procedure.pexecuteWhileCommit(proc);
	}
	
	public final boolean getNeedRoll()
	{
		return needRoll;
	}
	
	public final void setNeedRoll(boolean b)
	{
		needRoll = b;
	}
	
	protected void onRoleDead(final long roleid) {

		fire.pb.item.Equip equip = (fire.pb.item.Equip) fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.EQUIP, false);
		if (equip == null)
			return;
		int victoryType = 1;
		
		BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battle.getConfigid());
		if(battleConfig!=null&&battleConfig.victoryconditionType > 0)
		{
			victoryType = battleConfig.victoryconditionType;
		}
		
		if (victoryType != BattleField.BATTLE_ALL) {
			SRefreshNaiJiu send = new SRefreshNaiJiu();
			send.packid = fire.pb.item.BagTypes.EQUIP;
			
			boolean bsubNaiJiu = false;
			for (fire.pb.item.ItemBase bi : equip) {
				if (bi instanceof fire.pb.item.EquipItem) {
					int pos = bi.getPosition();					
					SEquipNaiJiuXiaoHao lose = fire.pb.item.Module.getInstance().getItemManager().getLoseNaiJiu(pos);
					if (lose == null) {
						continue;
					}
					
					int times = lose.cishu.get(4);
					fire.pb.item.EquipItem eitem = (fire.pb.item.EquipItem)bi;
					if (eitem.getEndure() <= 0) {
						continue;
					}
					
					if (times != 0) {
						int before = eitem.getEquipAttr().getEndure();
						final int lostEndure = Math.max(5, eitem.getCurMaxEndure() / times);
						if (eitem.loseEndure(lostEndure)) {
							if (before != eitem.getEquipAttr().getEndure()) {
								fire.pb.item.EquipNaiJiu endure = new fire.pb.item.EquipNaiJiu();
								endure.keyinpack = bi.getKey();
								endure.endure = eitem.getExtInfo().getEndure();
								send.data.add(endure);
								bsubNaiJiu = true;
							}
						}
					}
				}
			}
			
			//您因死亡损失了1%的装备耐久
			if(bsubNaiJiu == true)
				MessageMgr.psendMsgNotifyWhileCommit(roleid, 160005, null);
			MessageMgr.psendSystemMessageToRole(roleid, 160117, null);
			
			mkdb.Procedure.psendWhileCommit(roleid, send);
		}
		
		fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(roleid, false);
		erole.setSp(0);
	}

	private boolean processDeath() {
		boolean succ1 = false;
		boolean succ2 = true;
		boolean succ3 = true;
		boolean succ4 = true;
		// 死亡规则
		succ1 = new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				boolean result = handleDeath();
				if (battle.getBattletype() != BattleType.BATTLE_PVP&&battle.getBattletype() != BattleType.BATTLE_LIVEDIE && battle.getBattletype() != BattleType.BATTLE_CLAN_FIGHT) {
					//生死战和切磋死亡都不在这里处理装备耐久问题，生死战是失败方都扣除装备耐久，单独处理
					for (xbean.Fighter deadfighter : getDeadRoles()) {
						final long deadroleid = deadfighter.getUniqueid();
						onRoleDead(deadroleid);
					}
				}
				return result;
			}

		}.call();
		recoveryRoleAndPetHp();
		// 如果打赢了,进入这个方法,给予战斗后的奖励
		if (battle.getBattleresult() == BattleField.BATTLE_WIN)
		{
			succ2 = new mkdb.Procedure() {

				@Override
				protected boolean process() throws Exception {
					List<xbean.Fighter> winFighters = getWinRoles();
					List<Long> roleids = new ArrayList<Long>();
					for (xbean.Fighter f : winFighters) {
						roleids.add(f.getUniqueid());
					}
					if (battle.getBattletype() % 10 == 0) {
						addFriendShip(roleids);
					}
					return handleAward();
				}

			}.call();
			handleCamera();
		}
		// 如果打输了
		else if (battle.getBattleresult() == BattleField.BATTLE_LOSE){
			succ3 = new mkdb.Procedure() {

				@Override
				protected boolean process() throws Exception {

					return handleBattleFailed();
				}
			}.call();
			if(battle.getBattletype() % 10 == 1){
				handleCamera();
			}
		// 也可能有打平的情况
		}else if (battle.getBattleresult() == BattleField.BATTLE_DRAW)
		{
			succ4 = new mkdb.Procedure() {

				@Override
				protected boolean process() throws Exception {
					return handleDrawBattle();
				}
			}.call();
			if(battle.getBattletype() % 10 == 1){
				handleCamera();
			}
		}
		notifyBattleEnd();
		if (!transformRoleids.isEmpty())
			transformAfterBattle();
		return succ1 && succ2 && succ3 && succ4;
	}

	protected boolean needToChangeLeader(boolean host) {

		List<xbean.Fighter> deadFighters = new ArrayList<xbean.Fighter>();
		List<xbean.Fighter> fighters = null;
		if (host)// 如果是host方
			fighters = BattleField.getHostFighters(battle);
		else
			fighters = BattleField.getGuestFighters(battle);
		if (fighters == null || fighters.size() == 0)
			return false;
		for (xbean.Fighter fighter : fighters) {
			int index = fighter.getBattleindex();
			final Integer val = battle.getRoundresult().get(index);
			if (fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				if (val != null && (val & ResultType.RESULT_DEATH) != 0)
					deadFighters.add(fighter);
		}
		boolean allDie = false;// 是否团灭
		if (deadFighters.size() == fighters.size())
			allDie = true;
		for (xbean.Fighter f : deadFighters) {
			if (TeamManager.isTeamLeader(f.getUniqueid()))
				if (f.getIshost() && !allDie)
					return true;
		}
		return false;
	}

	protected void transformAfterBattle() {
		
	}

	/***/
	private void recoveryRoleAndPetHp() {

		for (xbean.Fighter f : battle.getFighters().values()) {
			if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE) {
				long roleid = f.getUniqueid();
				RoleImpl roleImpl = new RoleImpl(roleid);
				if (roleImpl.getHp() == 0)
					roleImpl.setHp(1);
				
				SRefreshRoleData sr = new SRefreshRoleData();
				Map<Integer,Float> changedAttrs = roleImpl.recoverWound();
				if(changedAttrs.size()>0){
					sr.datas.putAll(changedAttrs);
				}
				
				sr.datas.put(AttrType.HP, (float) roleImpl.getHp());
				Procedure.psendWhileCommit(roleid, sr);
				Map<Integer, Integer> petFighters = f.getFightedpets();
				for (Entry<Integer, Integer> petFighter : petFighters.entrySet()) {
					int petKey = petFighter.getKey();
					PetColumn petColumn = new PetColumn(roleid, PetColumnTypes.PET, false);
					PetInfo petinfo = petColumn.getPetInfo(petKey);
					if (petinfo == null)
						continue;
					if (petinfo.getHp() == 0)
						petinfo.setHp(1);
					SRefreshPetData srp = new SRefreshPetData();
					srp.columnid = PetColumnTypes.PET;
					srp.petkey = petKey;
					srp.datas.put(AttrType.HP, (float) petinfo.getHp());
					srp.datas.put(AttrType.MP, (float) petinfo.getMp());
					Procedure.psendWhileCommit(roleid, srp);
				}
			}
			if (f.getFightertype() == xbean.Fighter.FIGHTER_PET) {
				xbean.Fighter roleFighter = battle.getFighters().get(f.getBattleindex() - 5);
				if (roleFighter == null)
					return;
				long rid = roleFighter.getUniqueid();
				int fighterPetKey = new PropRole(rid, true).getFightpetkey();
				if (fighterPetKey <= 0)
					continue;
				PetColumn petColumn = new PetColumn(rid, PetColumnTypes.PET, false);
				PetInfo petinfo = petColumn.getPetInfo(fighterPetKey);
				if (petinfo == null)
					continue;
				if (petinfo.getHp() == 0)
					petinfo.setHp(1);
				SRefreshPetData srp = new SRefreshPetData();
				srp.columnid = PetColumnTypes.PET;
				srp.petkey = fighterPetKey;
				srp.datas.put(AttrType.HP, (float) petinfo.getHp());
				srp.datas.put(AttrType.MP, (float) petinfo.getMp());
				Procedure.psendWhileCommit(rid, srp);

			}
		}
	}

	/***/
	protected boolean handleDrawBattle() {

		if (battle.getMonsterid() != 0) {
			fire.pb.map.Npc n = fire.pb.map.SceneNpcManager.getNpcByKey(battle.getMonsterid());
			if (n != null)
				n.setInBattle(NPC.NOT_INBATTLE);
		}
		return true;
	}

	/***/

	protected boolean handleBattleFailed() {

		if (battle.getMonsterid() != 0) {
			fire.pb.map.Npc n = fire.pb.map.SceneNpcManager.getNpcByKey(battle.getMonsterid());
			if (n != null)
				n.setInBattle(NPC.NOT_INBATTLE);
		}
		return true;
	}

	/**处理逃跑捕捉击飞等离开战斗事件*/
	public boolean handleOutBattle(int index,int petkey) throws Exception {

		return true;
	}

	/**逃跑捕捉击飞等离开战斗事件的处理*/
	public final boolean processOutBattle(final int index,final int petkey) throws Exception {

		boolean handleResult = new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				return handleOutBattle(index,petkey);
			}

		}.call();
		boolean removeResult = removeFighterInfo(index);
		return handleResult && removeResult;
	}

	/**观战者离开战斗*/
	public void handleWatcherOutBattle(long roleId) {
	}

	/***/

	private boolean removeFighterInfo(int index) {

		xbean.Fighter f = battle.getFighters().get(index);
		if (f == null)
			return true;

		final Integer val = battle.getRoundresult().get(index);

		// 如果是role
		if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE) {

			if ((val & ResultType.RESULT_RUNAWAY) != 0) {
				final long roleID = BattleField.getRoleIDByIndex(battle, index);
				if (0 == roleID)
					return false;
				// 装备耐久处理
				if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE && battle.getBattletype() != BattleType.BATTLE_PVP) {
					final long roleid = f.getUniqueid();
					fire.pb.item.Equip equip = (fire.pb.item.Equip) fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.EQUIP, false);
					equip.battleEnd(battle.getConfigid(), f.getActioncount());
				}
				// 如果玩家之前有用过宠物(包括逃跑之前又把宠物收回)
				xbean.Fighter pFighter = battle.getFighters().get(f.getBattleindex() + 5);
				Integer petVal = battle.getRoundresult().get(f.getBattleindex() + 5);
				if (pFighter != null) {
					handlePetOutBattle(f, pFighter.getBattleindex(), petVal);
				}
				if (battle.getBattletype() != BattleType.BATTLE_PVP) {
					if (f.getFightedpets().size() > 0) {
						handleFightedpets(f);
					}
				}
				battle.getRoleids().remove(roleID);
				xtable.Roleid2battleid.remove(roleID);
				battle.getFighters().remove(index);
				Fighter fighter = battle.getFighterobjects().remove(index);
				if (fighter != null) {
					fighter.getBuffAgent().removeBuffsWhileBattleEndWithSP();
				}

			}
		} // 如果是宠物
		else if (f.getFightertype() == xbean.Fighter.FIGHTER_PET) {
			if ((val & ResultType.RESULT_RUNAWAY) != 0 || (val & ResultType.RESULT_SUMMONBACK) != 0 || (val & ResultType.RESULT_KICKOUT) != 0) {
				if (!battle.getFighters().containsKey(index))
					return true;// 有可能，人物逃跑时已经顺带移除了宠物的
				xbean.Fighter rolefighter = battle.getFighters().get(index - 5);
				handlePetOutBattle(rolefighter, index, val);
			}
		} // 如果是怪物
		else if (f.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER) {
			if ((val & ResultType.RESULT_KICKOUT) != 0 || (val & ResultType.RESULT_SEIZE) != 0 || (val & ResultType.RESULT_RUNAWAY) != 0|| (val & ResultType.RESULT_SUMMONBACK) != 0 ) {
				battle.getFighters().remove(index);
				battle.getFighterobjects().remove(index);
			}
		}

		return true;
	}

	/***/

	private void handlePetOutBattle(xbean.Fighter rolefighter, int index, Integer val) {

		if (rolefighter == null)
			return;
		Integer petKey = xtable.Properties.selectFightpetkey(rolefighter.getUniqueid());
		if (petKey != null && petKey > 0)
			rolefighter.getFightedpets().put(petKey, val); // 放到这里,可以在战斗结束的时候一起做惩罚

		Fighter petfighter = battle.getFighterobjects().get(index);
		petfighter.getBuffAgent().removeBuffsWhileBattleEndWithSP();// 宠物出战斗清buff
		// 先不处理死亡的宠物,等战斗结束统一处理,因为该宠物已经放到了Fightedpet中
		battle.getFighters().remove(index);
		battle.getFighterobjects().remove(index);
		
	}

	protected abstract boolean handleDeath();

	protected abstract boolean handleAward();

	protected abstract boolean handleCamera();

	public BattleInfo getBattle() {

		return battle;
	}

	public void setBattle(BattleInfo battle) {

		this.battle = battle;
	}

	public int getResult() {

		return battle.getBattleresult();
	}

	// 回复宠物的血和魔
	protected void fullPetHpMp(PetInfo petinfo, long roleid) {

		if (petinfo != null) {
			fire.pb.effect.Role pet = new PetImpl(petinfo);
			int petMaxHp = pet.getMaxHp();
			int petMaxMp = pet.getMaxMp();
			petinfo.setHp(petMaxHp);
			petinfo.setMp(petMaxMp);
			SRefreshPetData srp = new SRefreshPetData();
			srp.columnid = PetColumnTypes.PET;
			srp.petkey = petinfo.getKey();
			srp.datas.put(AttrType.HP, (float) petMaxHp);
			srp.datas.put(AttrType.MP, (float) petMaxMp);
			Onlines.getInstance().send(roleid, srp);
		}
	}


	/**返回所有战斗中的宠物*/
	public List<xbean.Fighter> getAllPetFighters() {

		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		Collection<xbean.Fighter> allFighters = battle.getFighters().values();
		for (xbean.Fighter fighter : allFighters) {
			if (fighter.getFightertype() == xbean.Fighter.FIGHTER_PET)
				fighters.add(fighter);
		}
		return fighters;
	}

	/**宠物的处理规则是一定的,且不受替身娃娃的影响*/
	protected void handlePetFighters() {

		// 取消多次惩罚 [4/6/2016 XGM]
//		List<xbean.Fighter> fighters = getAllPetFighters();
//		for (xbean.Fighter f : fighters) {
//			final Integer val = battle.getRoundresult().get(f.getBattleindex());
//			long rid = battle.getFighters().get(f.getBattleindex() - 5).getUniqueid();
//			int fighterPetKey = new PropRole(rid, true).getFightpetkey();
//			if (!handleSinglePet(rid, fighterPetKey, val)) {
//				logger.debug("handle pet failed,pet index:" + f.getBattleindex());
//				continue;
//			}
//		}
		// 如果玩家在战斗中招出多于一个的宠物,也要处理
		List<xbean.Fighter> roleFighters = getAllRoleFighters();
		for (xbean.Fighter f : roleFighters) {
			handleFightedpets(f);
		}
	}

	/***/

	private void handleFightedpets(xbean.Fighter f) {

		Map<Integer, Integer> petFighters = f.getFightedpets();
		for (Entry<Integer, Integer> petFighter : petFighters.entrySet()) {
			int petKey = petFighter.getKey();
			int val = petFighter.getValue();
			long roleid = f.getUniqueid();
			if (!handleSinglePet(roleid, petKey, val)) {
				logger.debug("handle pet failed,pet index:" + f.getBattleindex());
				continue;
			}
		}
	}

	protected boolean handleSinglePet(long roleid, int petKey, int val) {

		long masterid = roleid;
		PetColumn petColumn = new PetColumn(masterid, PetColumnTypes.PET, false);
		if (petKey < 0) {
			logger.debug("No fight pet. roleId = " + masterid);
			return false;
		}

		if (((val & ResultType.RESULT_DEATH) != 0 || (val & ResultType.RESULT_KICKOUT) != 0 || (val & ResultType.RESULT_GHOST) != 0)) {
			// 如果宠物死了
			punishPetDeath(petColumn, petKey);
		} else {
			// 如果宠物没死
			decreaseLoyaltyAndLife(petColumn, petKey, masterid);
		}
		return true;
	}

	/**死亡惩罚(宠物)*/
	protected void punishPetDeath(PetColumn petcol, int petkey)
	{
		Pet pet = petcol.getPet(petkey);
		changePetLife(pet, -10);
	}

	protected void decreaseLoyaltyAndLife(PetColumn petColumn, int petKey, long masterID) {

		Pet pet = petColumn.getPet(petKey);
		changePetLife(pet, -1);
	}

	protected void changePetLife(Pet pet, int value)
	{
		if (pet == null)
			return;

		pet.addLife(value);

		refreshPetInfo(pet.getPetInfo(), pet.getRoleId());

		boolean isForever = fire.pb.pet.Module.getInstance().getPetManager().isPetLifeForever(pet.getPetAttr().getId());
		if (pet.getLife() < DataInit.PET_FIGHT_LIFE_LIMIT && !isForever) {
			// 设置宠物休息
			fire.pb.pet.PSetFightPetProc proc = new fire.pb.pet.PSetFightPetProc(pet.getRoleId(), pet.getPetkey(), false);
			proc.call();
			// 提示宠物休息了
			fire.pb.talk.MessageMgr.psendMsgNotify(pet.getRoleId(), 150088, null);
		}
	}

	protected void refreshPetInfo(xbean.PetInfo petinfo, long masterID) {

		SRefreshPetData srp = new SRefreshPetData();
		srp.columnid = PetColumnTypes.PET;
		srp.petkey = petinfo.getKey();
		srp.datas.put(AttrType.PET_LIFE, (float) petinfo.getLife());
		Procedure.psendWhileCommit(masterID, srp);

	}

	/**返回所有带死亡buff的玩家*/
	public List<xbean.Fighter> getDeadRoles() {

		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		Collection<xbean.Fighter> allFighters = battle.getFighters().values();
		for (xbean.Fighter fighter : allFighters) {
			int index = fighter.getBattleindex();
			final Integer val = battle.getRoundresult().get(index);
			if (fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				if (val != null && (val & ResultType.RESULT_DEATH) != 0)
					fighters.add(fighter);
		}
		return fighters;
	}

	/**返回所有战斗胜利并活着的玩家*///因策划需求，只要己方胜利，现在死亡也算胜利玩家
	public List<xbean.Fighter> getWinRoles() {

		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		if (battle.getBattleresult() == BattleField.BATTLE_DRAW)
			return fighters;
		boolean hostWin = false;
		if (battle.getBattleresult() == BattleField.BATTLE_WIN)
			hostWin = true;
		Collection<xbean.Fighter> allFighters = battle.getFighters().values();
		for (xbean.Fighter fighter : allFighters) {
			int index = fighter.getBattleindex();
//			final Integer val = battle.getRoundresult().get(index);
			if (fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
//				if (val != null/* && (val & ResultType.RESULT_DEATH) == 0*/)
					if (hostWin && fighter.getIshost())
						fighters.add(fighter);
					else if (!hostWin && !fighter.getIshost())
						fighters.add(fighter);
		}
		return fighters;
	}

	public List<Long> getHostIniFighters() {

		List<Long> result = new ArrayList<Long>();
		Collection<xbean.Fighter> fighters = battle.getInifighters().values();
		for (xbean.Fighter f : fighters) {
			if (f.getBattleindex() < BattleField.A_SIDE_FIGHTER_NUM && f.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				result.add(f.getUniqueid());
		}
		return result;
	}

	public List<Long> getGuestIniFighters() {

		List<Long> result = new ArrayList<Long>();
		Collection<xbean.Fighter> fighters = battle.getInifighters().values();
		for (xbean.Fighter f : fighters) {
			if (f.getBattleindex() > BattleField.A_SIDE_FIGHTER_NUM && f.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				result.add(f.getUniqueid());
		}
		return result;
	}

	/**返回所有还在战斗中的玩家*/
	public List<xbean.Fighter> getAllRoleFighters() {

		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		Collection<xbean.Fighter> allFighters = battle.getFighters().values();
		for (xbean.Fighter fighter : allFighters) {
			if (fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				fighters.add(fighter);
		}
		return fighters;
	}

	public boolean isLeaderAlive(boolean isHost) {

		int index = BattleField.HOST_FIRST_ID;
		if (!isHost)
			index = BattleField.GUEST_FIRST_ID;

		Integer val = battle.getRoundresult().get(index);
		xbean.Fighter fighter = battle.getFighters().get(index);
		if (val != null && (val & ResultType.RESULT_DEATH) == 0 && fighter != null && fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE) {
			return true;
		} else {
			return false;
		}
	}

	protected void notifyBattleEnd() {

	}

	protected boolean distributeItemAward(List<Long> roleids, int lootid, Map<String, Object> paras) {

		if (roleids.size() > 0){
			Team team = TeamManager.selectTeamByRoleId(roleids.get(0));
			long leaderid = 0;
			if (team!=null)
				leaderid = team.getTeamLeaderId();
			for (long roleid : roleids) {
				paras.put(RewardMgr.IS_LEADER, leaderid==roleid?1:0);
				RewardMgr.getInstance().distributeAllAward(roleid, lootid, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guaji, battle.getAreaconf(),
						PAddExpProc.GUAJI_AWARD, "练功区");
			}
		}
		return true;
	}


	/***/
	public void addFriendShip(List<Long> roleids) {

	}

    protected int getMapLv(){
    	if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLEHIDE) {
			SMineArea hideAreaInfo = hideMapConfigs.get(battle.getAreaconf());
			return getMapLevel(hideAreaInfo);
		}
		else if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLESHOW) {
			SShowAreainfo showAreainfo = showMapConfigs.get(battle.getAreaconf());
			return getMapLevel(showAreainfo);
		}
    	return 0;
    }
    
    protected int getMapMaxLv(){
    	if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLEHIDE) {
			SMineArea hideAreaInfo = hideMapConfigs.get(battle.getAreaconf());
			return getMapMaxLevel(hideAreaInfo);
		}
		else if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLESHOW) {
			SShowAreainfo showAreainfo = showMapConfigs.get(battle.getAreaconf());
			return getMapLevel(showAreainfo);
		}
    	return 0;
    }
    
    /***/
	private int getMapLevel(Object areaInfo) {

		if (areaInfo == null)
			return 0;
		if (areaInfo instanceof SMineArea)
			return ((SMineArea) areaInfo).getLevel();
		else if (areaInfo instanceof SShowAreainfo) {
			return ((SShowAreainfo) areaInfo).getTypelevel();
		}
		return 0;
	}
	
    /***/
	private int getMapMaxLevel(Object areaInfo) {

		if (areaInfo == null)
			return 0;
		if (areaInfo instanceof SMineArea)
			return ((SMineArea) areaInfo).getMaxLevel();
		else if (areaInfo instanceof SShowAreainfo) {
			//明雷表先不用
			return ((SShowAreainfo) areaInfo).getTypelevel();
		}
		return 0;
	}
	
}
