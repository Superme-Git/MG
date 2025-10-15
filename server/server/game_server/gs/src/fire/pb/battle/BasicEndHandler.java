
package fire.pb.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.script.ScriptException;

import mkdb.Lockeys;
import mkdb.Procedure;
import xbean.PetInfo;
import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RolledAwardItem;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.effect.Role;
import fire.pb.effect.RoleImpl;
import fire.pb.hook.PSubDpointCntProc;
import fire.pb.hook.RoleHookManager;
import fire.pb.main.ConfigManager;
import fire.pb.map.SMineArea;
import fire.pb.map.SShowAreainfo;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.pet.PAddPetExpProc;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.FireProp;
import fire.pb.util.MessageUtil;
import fire.script.AwardJSEngine;
import fire.script.JavaScript;
import fire.script.ParseStringExpression;
import gnet.link.Onlines;

public class BasicEndHandler extends BattleEndHandler {

	static Properties prop = ConfigManager.getInstance().getPropConf("battle");

	static final int TEN_BOSS_BATTLE_AWARDID = 1207;
	static final int TEN_MONSTER_BATTLE_AWARDID = 1206;
	static final int BOSS_ITEM_AWARD = 1263;
	private int mapLevel;
	private int awardid;	
	private int mapMaxLevel;

	public BasicEndHandler() {

		super();

	}

	@Override
	public boolean handleOutBattle(int index, int petkey) throws Exception {

		xbean.Fighter f = battle.getFighters().get(index);
		if (f == null)
			return true;
		if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE) {
			long roleid = f.getUniqueid();
			Role role = new RoleImpl(roleid);
			xbean.Properties prop = xtable.Properties.get(roleid);
			int maxhp = role.getMaxHp();
			//清除wound
			prop.setWound(0);
			SRefreshRoleData sr = new SRefreshRoleData();
			sr.datas.put(AttrType.UP_LIMITED_HP, (float) maxhp);
			Onlines.getInstance().send(roleid, sr);
			if((getBattle().getRoundresult().get(index)&ResultType.RESULT_RUNAWAY)>0)
				Procedure.psendWhileCommit(roleid, new SDeadLess20(1));

		}
		if (f.getFightertype() == xbean.Fighter.FIGHTER_PET) {
			xbean.Fighter roleFighter = battle.getFighters().get(f.getBattleindex() - 5);
			if (roleFighter == null)
				return true;
			long rid = roleFighter.getUniqueid();
			int fighterPetKey = new PropRole(rid, true).getFightpetkey();
			if (fighterPetKey <= 0)
				return true;
			PetColumn petColumn = new PetColumn(rid, PetColumnTypes.PET, false);
			PetInfo petinfo = petColumn.getPetInfo(fighterPetKey);
			if (petinfo == null)
				return true;
			
			SRefreshPetData srp = new SRefreshPetData();
			srp.columnid = PetColumnTypes.PET;
			srp.petkey = fighterPetKey;
			srp.datas.put(AttrType.HP, (float) petinfo.getHp());
			srp.datas.put(AttrType.MP, (float) petinfo.getMp());
			Procedure.psendWhileCommit(rid, srp);
		}
		return true;
	}

	@Override
	protected void notifyBattleEnd() {

		for (long roleid : battle.getRoleids().keySet()) {
			fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.BattleEndEvent(roleid, battle));
		}
		// notifyTaskModule();
	}


	public boolean handleDeath() {

		// 宠物的处理规则是一定的,且不受替身娃娃的影响
		handlePetFighters();
		// 新手发仆人提示
		List<xbean.Fighter> winfighters = getWinRoles();
		for (xbean.Fighter f : winfighters) {
			long roleid = f.getUniqueid();
			Role skillRole = new RoleImpl(roleid);
			int maxhp = skillRole.getMaxHp();
			SRefreshRoleData sr = new SRefreshRoleData();
			sr.datas.put(AttrType.UP_LIMITED_HP, (float) maxhp);
			Onlines.getInstance().send(roleid, sr);
		}
		// 处理死亡的玩家
		List<xbean.Fighter> fighters = super.getDeadRoles();
		for (xbean.Fighter f : fighters) {
			// Bag bag = new Bag(f.getUniqueid(), BagTypes.BAG, false);
			
			long roleid = f.getUniqueid();
			if (battle.getBattleresult() == BattleField.BATTLE_LOSE) {
				basicDeathPunish(roleid);
			}
		}
		return true;
	}

	protected void basicDeathPunish(long roleid) {

		new PBasicDeathPunish(roleid, battle.getConfigid()).call();
	}

	@Override
	protected boolean handleCamera() {
		return true;
	}
	
	@Override
	protected boolean handleAward() {

		if (battle.getBattletype() % 10 == 0 && battle.getRound() == 1 && battle.getAreatype() != xbean.BattleInfo.AREA_BATTLESHOW) { // 如果是1回合秒怪,则加上不遇敌的buff
			noBattleIfEndInOneRound();
		}
		if (battle.getBattletype() % 10 == 0) {
			SMineArea hideAreaInfo = null;
			SShowAreainfo showAreainfo = null;

			int sharedid = 0;
			mapLevel = getMapLv();
			
			if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLEHIDE) {
				hideAreaInfo = hideMapConfigs.get(battle.getAreaconf());

				// locationid = hideAreaInfo.getId();
				awardid = getAwardID(hideAreaInfo);
				
				sharedid = hideAreaInfo.getShared(); //得到共享战利品ID by changhao
				
			} else if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLESHOW) {
				showAreainfo = showMapConfigs.get(battle.getAreaconf());

				awardid = getAwardID(showAreainfo);
			}
			if (hideAreaInfo == null && showAreainfo == null)
				return true;
			List<Long> roleids = new ArrayList<Long>();
			for (xbean.Fighter f : getWinRoles()) {
				roleids.add(f.getUniqueid());
			} 
			if (!expAward()) {
				return false;
			}
			
			itemAward(awardid, sharedid, roleids);
		}
		return true;
	}

	/** */
	protected int getAwardID(Object areaInfo) {

		if (areaInfo == null)
			return 0;
		if (areaInfo instanceof SMineArea)
			return ((SMineArea) areaInfo).getLootid();
		else if (areaInfo instanceof SShowAreainfo) {
			return ((SShowAreainfo) areaInfo).getLootid();
		}

		return 0;
	}

	/***/

	protected boolean itemAward(int lootid, int sharedid, List<Long> roleids) {
		Map<String, Object> paras = new HashMap<String, Object>();
		putBattleParas(paras);
	
		if (lootid > 0)
			super.distributeItemAward(roleids, lootid, paras); //先把必给的奖励给了 by changhao
		
		boolean roll = sharedid != 0 && roleids.size() > 1;
		
		if (roll) //如果需要ROLL点 by changhao
		{
			processRoll(sharedid, roleids, paras); //在发送roll点奖励  by changhao
		}
		else
		{
			if (sharedid != 0) //没有组队直接给,不走ROLL点形式  by changhao
			{
				int times = RewardMgr.getInstance().getTimes(lootid);
				for (int i = 0; i < times; i ++)
				{
					super.distributeItemAward(roleids, sharedid, paras);					
				}						
			}
		}

		return true;
	}


	/***/

	protected void noBattleIfEndInOneRound() {

		int index = BattleField.getLeaderFighterId(battle, true);
		if (index <= 0)
			return;
		long firstRoleid = battle.getFighters().get(index).getUniqueid();
		fire.pb.buff.BuffAgent agent = new fire.pb.buff.BuffRoleImpl(firstRoleid);
		long timeout = 0;
		Team team = TeamManager.selectTeamByRoleId(firstRoleid);
		if (team != null && team.isTeamLeader(firstRoleid) && team.getNormalMemberIds().size() > 1)
			timeout = FireProp.getIntValue(prop, "mtgserv.team_nobattle");
		else
			timeout = FireProp.getIntValue(prop, "mtgserv.single_nobattle");
		ConstantlyBuff cb = fire.pb.buff.Module.getInstance().createConstantlyBuff(FireProp.getIntValue(prop, "mtgserv.nobattleBuffid"));
		if (cb != null)
		{
			cb.setTime(timeout * 1000);
			
			agent.addCBuff(cb);			
		}
	}

	private void processRoll(int lootid, List<Long> roleids, Map<String, Object> paras) //处理ROLL点 by changhao
	{
		if (!roleids.isEmpty())
		{
			int times = RewardMgr.getInstance().getTimes(lootid);
			
			ArrayList<RolledAwardItem> rollawarditems = new ArrayList<RolledAwardItem>();
			
			for (int i = 0; i < times; i ++)
			{
				RewardMgr.getInstance().distributeAllAward(roleids.get(0), lootid, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_rolljiangli, battle.getAreaconf(),
						PAddExpProc.GUAJI_AWARD, "练功区", true, true, rollawarditems, false);				
			}
			
			if (rollawarditems.isEmpty()) //产生的道具 by changhao
			{
				return;
			}
			
			setNeedRoll(true);
			
			new fire.pb.team.teammelon.PTeamRollMelon(rollawarditems, roleids, battle.getBattleid(), null).call(); //执行ROLL点 by changhao
		}		
	}

	/** */
	protected boolean expAward() {
		// 给予任务经验和宠物经验,注意有衰减概率,所以要先算出经验,后乘以一个系数
		String expStr = "";
		String petExpStr = "";
		
		String moneyStr = "";
		String goldStr = "";
	
		int awardid = 0;
		mapMaxLevel = getMapMaxLv();
		
		if (battle.getMonstertype() == HideBattle.HIDEBATTLE_TYPE_BOSS) {
			if(RewardMgr.getInstance().getExpAwardMap().get(FireProp.getIntValue(prop, "mtgserv.battleBossExpAwardid")) !=null) {
				awardid = FireProp.getIntValue(prop, "mtgserv.battleBossExpAwardid");
			}
			if(RewardMgr.getInstance().getPetExpAwardMap().get(FireProp.getIntValue(prop, "mtgserv.battleBossExpAwardid")) !=null)
				petExpStr = RewardMgr.getInstance().getPetExpAwardMap().get(awardid).getJavaScriptString();
		} else {
			if(RewardMgr.getInstance().getExpAwardMap().get(FireProp.getIntValue(prop, "mtgserv.battleExpAwardid")) !=null) {
				awardid = FireProp.getIntValue(prop, "mtgserv.battleExpAwardid");
			}
			if( RewardMgr.getInstance().getPetExpAwardMap().get(FireProp.getIntValue(prop, "mtgserv.battleExpAwardid")) !=null)
				petExpStr = RewardMgr.getInstance().getPetExpAwardMap().get(FireProp.getIntValue(prop, "mtgserv.battleExpAwardid")).getJavaScriptString();
		}
		
		expStr = RewardMgr.getInstance().getExpAwardMap().get(awardid).getJavaScriptString();
		if (RewardMgr.getInstance().getMoneyAwardMap().get(awardid) != null)
			moneyStr = RewardMgr.getInstance().getMoneyAwardMap().get(awardid).getJavaScriptString();
		if (RewardMgr.getInstance().getGoldAwardMap().get(awardid) != null)
			goldStr = RewardMgr.getInstance().getGoldAwardMap().get(awardid).getJavaScriptString();
		
		List<xbean.Fighter> fighters = super.getWinRoles();
		if (!fighters.isEmpty()) {
			long leaderid = 0;
			Team team = TeamManager.selectTeamByRoleId(fighters.get(0).getUniqueid());
			if (team != null)
				leaderid = team.getTeamLeaderId();
			Map<String, Object> paras = new HashMap<String, Object>();
			putBattleParas(paras);
			
			//队伍中有老玩家额外加成20%经验判断
			Set<Long> roleIds = new HashSet<Long>();
			for (xbean.Fighter f : fighters) {
				roleIds.add(f.getUniqueid());
			}
			
			Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));
			
			JavaScript expAward = RewardMgr.getInstance().getExpAwardMap().get(awardid);
			JavaScript petAward = RewardMgr.getInstance().getPetExpAwardMap().get(awardid);
			JavaScript moneyAward = RewardMgr.getInstance().getMoneyAwardMap().get(awardid);
			JavaScript goldAward = RewardMgr.getInstance().getGoldAwardMap().get(awardid);
			
			for (xbean.Fighter f : fighters) {
				long roleid = f.getUniqueid();
				PropRole propRole = new PropRole(roleid, true);
				int roleLevel = propRole.getLevel();
				paras.put(RewardMgr.IS_LEADER, leaderid == roleid ? 1 : 0);
				paras.put(RewardMgr.VIP_LV, 0);
				paras.put(RewardMgr.BROTHER_NUM, 0);
				RewardMgr.putNeededParas(roleid, paras);
				
				if((roleLevel <= (mapMaxLevel + 10)) && (RoleHookManager.getInstance().isHookExpAdd(roleid))){
					paras.put(RewardMgr.ISDBPOINT, 1);
				}
				else{
					paras.put(RewardMgr.ISDBPOINT, 0);
				}
				//人物经验
				AwardJSEngine aw = new AwardJSEngine();
				for (Entry<String, Object> entry : paras.entrySet()) {
					aw.put(entry.getKey(), entry.getValue());
				}
				Double value = expAward.eval(aw, null, null);
				
				/*ParseStringExpression pse = new ParseStringExpression();
				Double value = 0.0;
				try {
					value = pse.ParseStr(expStr, paras);
				} catch (ScriptException e) {
					e.printStackTrace();
					return false;
				}*/
				long exp = value.longValue() + 1L;
				
				/* 修正经验*/
				long reviseExp = 0;
				/*服务器加成和衰减*/
				long addDecExp = 0;
				/* 盈福经验*/
				long yingfuExpVal = 0;
				/* 停等级经验*/
				long pileExp = 0;
				
				//角色等级和服务器的等级差
				xbean.Properties properties = xtable.Properties.select(roleid);
				int roleLv = properties.getLevel();
				int hideLv = 0;
				
				if (roleLv == 69 || roleLv == 89) {
					hideLv = RewardMgr.getInstance().getHideLevel(roleid);
				}
				
				int midLv = fire.pb.timer.Module.getMidLv(roleLv + hideLv, awardid);
				
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid, true);
				if (RewardMgr.getInstance().getYingFuExpMap().get(awardid) == 1 && actrole != null && actrole.getYingFuExp() > 0) {
					int yingfuxishu = xtable.Properties.selectYingfuxishu(roleid);
					if (yingfuxishu > 0) {
						yingfuExpVal = exp * fire.pb.mission.activelist.RoleLivenessManager.YingFuXishu * yingfuxishu / 1000000
								 + exp * fire.pb.mission.activelist.RoleLivenessManager.YingFuXishu / 100;
					}
					else
						yingfuExpVal = exp * fire.pb.mission.activelist.RoleLivenessManager.YingFuXishu / 100;
					exp += yingfuExpVal;
				}
				
				//修正正常经验和盈福经验
				if (midLv != 0) {
					reviseExp = fire.pb.timer.Module.getExpRevise(midLv, exp);
					if (yingfuExpVal > 0)
						yingfuExpVal = fire.pb.timer.Module.getExpRevise(midLv, yingfuExpVal);
					addDecExp = reviseExp - exp;
				}
				else {
					reviseExp = exp;
				}
				
				fire.pb.effect.Role roleImpl = new fire.pb.effect.RoleImpl(roleid, true);
				long nextExp = roleImpl.getNextExp();
				if (properties.getExp() / nextExp == 10) {
					pileExp = exp - (exp * 80 / 100);
					reviseExp = reviseExp - pileExp;
				}
				else if (properties.getExp() / nextExp == 20) {
					pileExp = exp - (exp * 50 / 100);
					reviseExp = reviseExp - pileExp;
				}
				
				if (reviseExp < 0) reviseExp = 0;
				
				//宠物经验
				AwardJSEngine awPet = new AwardJSEngine();
				for (Entry<String, Object> entry : paras.entrySet()) {
					awPet.put(entry.getKey(), entry.getValue());
				}
				value = petAward.eval(awPet, null, null);
				
				/*try {
					value = pse.ParseStr(petExpStr, paras);
				} catch (ScriptException e) {
					e.printStackTrace();
					return false;
				}*/
				long petExp = value.longValue() + 1L;
				
				//人物银币
				AwardJSEngine awMoney = new AwardJSEngine();
				for (Entry<String, Object> entry : paras.entrySet()) {
					awMoney.put(entry.getKey(), entry.getValue());
				}
				value = moneyAward.eval(awMoney, null, null);
				
				/*try {
					value = pse.ParseStr(moneyStr, paras);
				} catch (ScriptException e) {
					e.printStackTrace();
					return false;
				}*/
				long money = value.longValue();
				fire.pb.item.Pack pack = new fire.pb.item.Pack(roleid, false);
				long realAddMoney = pack.addSysMoney(money, "挂机", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guaji, awardid);
				
				//人物金币
				long gold = 0;
				long realAddGold = 0;
				if (goldAward != null) {
					AwardJSEngine awGold = new AwardJSEngine();
					for (Entry<String, Object> entry : paras.entrySet()) {
						awGold.put(entry.getKey(), entry.getValue());
					}
					value = goldAward.eval(awGold, null, null);
					/*try {
						value = pse.ParseStr(goldStr, paras);
					} catch (ScriptException e) {
						e.printStackTrace();
						return false;
					}*/
					gold = value.longValue();
					realAddGold = pack.addSysGold(gold, "挂机", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guaji, awardid);
				}

				//人物需要大于场景10级
				//挂机经验加成
				if(((roleLevel <= (mapMaxLevel + 10))) && (RoleHookManager.getInstance().isHookExpAdd(roleid))){
					reviseExp += RoleHookManager.getInstance().getDpointExp(roleid);
					fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(RoleHookManager.HookSubDPoint);
					int hookSubDpoint = Integer.parseInt(common.getValue());
					new PSubDpointCntProc(roleid, hookSubDpoint).call();
				}
				
				/*PAddExpProc pAddExpProc = new PAddExpProc(roleid, exp, false, PAddExpProc.GUAJI_AWARD, "练功区奖励");
				
				boolean addExpSucc = pAddExpProc.call();*/
				boolean addExpSucc = false;
				do {
					if (yingfuExpVal > 0 || addDecExp != 0 || pileExp > 0) {
						PAddExpProc proc = new PAddExpProc(roleid, reviseExp, true, PAddExpProc.GUAJI_AWARD,
								"挂机奖励", false, yingfuExpVal, addDecExp, pileExp);
						addExpSucc = proc.call();
					} else {
						// 改到外面提示
						PAddExpProc proc = new PAddExpProc(roleid, reviseExp, true, PAddExpProc.GUAJI_AWARD, "挂机奖励");
						addExpSucc = proc.call();
					}
				} while (false);
				
				//给宠物经验
				if (propRole.getFightpetkey() >= 0) {
					new PAddPetExpProc(roleid, propRole.getFightpetkey(), petExp, true).call();
				}

				if (realAddMoney > 0)
					MessageUtil.psendAddorRemoveMoney(roleid, realAddMoney);
				if (realAddGold > 0)
					MessageUtil.psendAddorRemoveGold(roleid, realAddGold);
			}
		}

		return true;
	}

	protected void putBattleParas(Map<String, Object> paras) {
		paras.put("MapLv", mapLevel);
		paras.put("MasterNum", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_CHIEF_NUM).intValue());
		paras.put("MonsterNum", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_NUM).intValue());
		paras.put("TeamNum", battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
		paras.put("TeamLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_AVERAGE_LEVEL));
		paras.put("MonsterLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL));

	}

	/** */
	protected long petExpRegression(long exp, int levelGap) {

		if (levelGap < 0) {
			if (levelGap <= -31)
				return exp * 1 / 100;
			if (levelGap <= -26)
				return exp * 1 / 10;
			if (levelGap <= -21)
				return exp * 1 / 10;
			if (levelGap <= -16)
				return exp * 1 / 10;
			if (levelGap <= -11)
				return exp * 2 / 10;
			if (levelGap <= -6)
				return exp * 4 / 10;
			if (levelGap <= -1)
				return exp * 8 / 10;
		} else {
			if (levelGap <= 5)
				return exp;
			if (levelGap <= 10)
				return exp * 8 / 10;
			if (levelGap <= 15)
				return exp * 6 / 10;
			if (levelGap <= 20)
				return exp * 4 / 10;
			if (levelGap <= 25)
				return exp * 2 / 10;
			else
				return exp * 1 / 10;

		}
		return 0;
	}

	/** */
	protected long roleExpRegression(long exp, int levelGap) {

		if (levelGap < 0) {
			if (levelGap <= -31)
				return exp * 2 / 10;
			if (levelGap <= -26)
				return exp * 3 / 10;
			if (levelGap <= -21)
				return exp * 4 / 10;
			if (levelGap <= -16)
				return exp * 5 / 10;
			if (levelGap <= -11)
				return exp * 6 / 10;
			if (levelGap <= -6)
				return exp * 7 / 10;
			if (levelGap <= -1)
				return exp * 8 / 10;
		} else {
			if (levelGap <= 5)
				return exp;
			if (levelGap <= 10)
				return exp * 8 / 10;
			if (levelGap <= 15)
				return exp * 6 / 10;
			if (levelGap <= 20)
				return exp * 4 / 10;
			if (levelGap <= 25)
				return exp * 2 / 10;
			else
				return exp * 1 / 10;
		}
		return 0;
	}

}
