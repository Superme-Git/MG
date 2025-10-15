package fire.pb.battle;


import gnet.link.Onlines;
import mkdb.Procedure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import xbean.BattleInfo;
import fire.log.YYLogger;
import fire.log.beans.TeamOpBean;
import fire.msp.role.GSetRoleBattleInfo;
import fire.pb.DataInit;
import fire.pb.GsClient;
import fire.pb.PropConf;
import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.PlayPKEndHandler;
import fire.pb.battle.livedie.LiveDieMange;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.effect.Module;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.conf.InstanceSaveConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.item.Equip;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.item.PEnhancementTimeout;
import fire.pb.item.TimeOutItem;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColour;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.school.SchoolConst;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Result;
import fire.pb.skill.SkillAgent;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.state.State;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.script.FightJSEngine;
import fire.script.JavaScript;

public class PNewBattle extends Procedure {
	public static final long BATTLE_START_DELAY = 1500;
	private long battleID; //战斗id（唯一key）
	private int battleConfigId = -1;//默认值为-1，当为-1时，不通过战斗配置Id来生成战斗
	private xbean.BattleInfo battle;//战斗信息
	private BattleEndHandler handler=null;//战斗结束时的处理
	
	protected final long hostRoleID;//主方roleid
	protected final long guestRoleID;//客方roleid
	
	private int fightPos = 0;
	private String monsterName = null;
	
	
	private final java.util.List<Monster> monsters;//怪物
	
	private List<Long> hostRoleIds;
	private List<Long> guestRoleIds; 
	
	
	private boolean ispvp; //是否是pvp战斗
	private boolean babyfight=false;//是否是含有宝宝的战斗
	private int battleType = 0;//battle的类型，参见fire.pb.battle.BattleType,%10 == 0为pve战斗，%10 == 1为pvp战斗
	private long monsterNpckey = -1; //外面挂钩的怪物npckey
	private int areatype = 0;//明雷或暗雷xbean.BattleInfo.AREA_BATTLEHIDE/AREA_BATTLESHOW
	private int areaconf = 0;//暗雷区域的配置ID
	private int monstertype = 0; //战斗的怪物类型
	
	private int hostform = 0;//主方光环
	private int guestform = 0; //客方光环
	
	private int hostformLevel = 0;
	private int guestformLevel = 0;
	
	private boolean canrunaway = true;//该场战斗是否能逃跑

	private int battleRound = -1;
	
	public int isLiveDieTeamFight=0;//是否是生死战组队战斗    
	private long instnpckey = 0; //公会副本npckey
	private long landkey = 0; //公会副本key

	private Map<Integer, Integer> params = new HashMap<Integer, Integer>();//与战斗有关的一些参数
	public PNewBattle addParams(int key, int value){
		params.put(key, value);
		return this;
	}
	public PNewBattle removeParams(int key){
		params.remove(key);
		return this;
	}
	
	private PNewBattle(final long roleID, final long otherRoleID){
		hostRoleID = roleID;
		guestRoleID = otherRoleID;
		monsters=null;
	}
	private PNewBattle(final long roleID,final long otherRoleID,int battleType){
		this(roleID, otherRoleID);
		this.battleType=battleType;
	}
	
	public PNewBattle(final long roleID,final long otherRoleID,int battleType,BattleEndHandler handler){
		this(roleID, otherRoleID,battleType);
		this.handler=handler;
	}
	
	public PNewBattle(final long roleID,final long otherRoleID,int battleType,BattleEndHandler handler, int battleRound){
		this(roleID, otherRoleID,battleType);
		this.handler=handler;
		this.battleRound = battleRound;
	}

	
	/**利用battleConfigId来初始化一场战斗*/
	public PNewBattle(final long roleID, final int battleConfId,BattleEndHandler handler){
		this(roleID, battleConfId);
		this.handler=handler;
	}
	
	/**利用battleConfigId来初始化一场战斗*/
	public PNewBattle(final long roleID, final int battleConfId){
		hostRoleID = roleID;
		guestRoleID = 0;;
	    this.monsters = BattleField.getMonstersFromBattleConfig(battleConfId);
	    this.battleConfigId = battleConfId;
		this.handler = new BasicEndHandler();
	}
	
	/**利用battleConfigId来初始化一场战斗*/
	public PNewBattle(final long roleID, final int battleConfId,int monsterPos, String monsterNname,int npcShape, BattleEndHandler handler){
		hostRoleID = roleID;
		guestRoleID = 0;
		this.fightPos = monsterPos;
		this.monsterName = monsterNname;
	    this.monsters = BattleField.getMonstersFromBattleConfig(battleConfId, fightPos, monsterName, npcShape);
	    this.battleConfigId = battleConfId;
		this.handler = handler;
	}
	
	/**利用battleConfigId来初始化一场战斗*/
	public PNewBattle(final long roleID, final int battleConfId, int monsterPos, String monsterNname, BattleEndHandler handler){
		hostRoleID = roleID;
		guestRoleID = 0;
		this.fightPos = monsterPos;
		this.monsterName = monsterNname;
	    this.monsters = BattleField.getMonstersFromBattleConfig(battleConfId, fightPos, monsterName);
	    this.battleConfigId = battleConfId;
		this.handler = handler;
	}
	
	/**利用battleConfigId来初始化一场战斗*/
	public PNewBattle(final long roleID, final int battleConfId, int monsterPos, int shape, String monsterNname, BattleEndHandler handler){
		hostRoleID = roleID;
		guestRoleID = 0;
		this.fightPos = monsterPos;
		this.monsterName = monsterNname;
	    this.monsters = BattleField.getMonstersFromBattleConfig(battleConfId, fightPos, monsterName, shape);
	    this.battleConfigId = battleConfId;
		this.handler = handler;
	}
	
	/**用来代替PNewBattle(final long roleID, final java.util.List<Long> monsterIDs)
	 * 因为怪物的属性越来越复杂,仅用monsterid已经不能完整的代表一个怪物的属性.所以在战斗前要先把怪物的list生成.可以调用
	 * BattleField.genMonsters(List<Long> monsterIDs)来生成怪物的list.
	 */
	public PNewBattle(final long roleID, final java.util.List<Monster> monsters){
		hostRoleID = roleID;
		guestRoleID = 0;
	    this.monsters=monsters;
	    this.handler=new BasicEndHandler();
	}
	
	/**
	 * 用来代替PNewBattle(final long roleID, final java.util.List<Long> monsterIDs)
	 * 因为怪物的属性越来越复杂,仅用monsterid已经不能完整的代表一个怪物的属性.所以在战斗前要先把怪物的list生成.可以调用
	 * BattleField.genMonsters(List<Long> monsterIDs)来生成怪物的list.
	 */
	public PNewBattle(final long roleID, final java.util.List<Monster> monsters,BattleEndHandler handler){
		this(roleID, monsters);
		this.handler=handler;
	}

	public static void checkTimeoutEquip(final long roleid) {
		Equip equip = new Equip(roleid, true);
		for (ItemBase item : equip) {
			if (item.isTimeout()) {
				equip.onUnequip((EquipItem)((TimeOutItem)item).getEffectItem());
			}
		}
	}
	/**检查附魔到期时间 by changhao*/
	public static void checkEnhancementTimeoutEquip(final long roleid) {
		
		PEnhancementTimeout.UpdateEnhancementTimeout(roleid);
	}	
	
	public void setInstNpcKey(final long npckey) {
		this.instnpckey = npckey;
	}
	
	public void setLandkey(final long landkey) {
		this.landkey = landkey;
	}
	
	/**角色进入战斗*/
	public static boolean enterBattleRole(final xbean.BattleInfo battle, long roleID, int index,boolean isHost,int formId, int formLevel){
		checkTimeoutEquip(roleID);
		checkEnhancementTimeoutEquip(roleID);
		fire.pb.PropRole role = new fire.pb.PropRole(roleID, false);
		fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(roleID,false);

		if(role.getProperties().getPlaytimetimes() != 10)
			battle.setGmactiontimetimes(role.getProperties().getPlaytimetimes());
		
		final xbean.Fighter fighter = xbean.Pod.newFighter();
	
		fighter.setBattleindex(index);
		fighter.setFightertype(xbean.Fighter.FIGHTER_ROLE);
		fighter.setUniqueid(roleID);
		fighter.setFightername(role.getName());
		fighter.setInihp(role.getHp());
		fighter.setInilevel(erole.getLevel());
		fighter.setInimp(role.getMp());
		fighter.setIshost(isHost);
		fighter.setSubtype(role.getSchool());
		battle.setAutokey(battle.getAutokey() + 1);
		fighter.setFighterkey(battle.getAutokey());
		// 初始化药材使用次数
		fighter.getUseditems().put(0,xbean.Fighter.DrugStuffTime);
		// 初始化食品药品使用次数
		fighter.getUseditems().put(1,xbean.Fighter.DrugFoodTime);
		// 初始化战斗结束时需要设置为参战的宠物key
		fighter.setInipetkey(role.getFightpetkey());
		//pvp时候恢复用的。pve不恢复
		fighter.setInisp(erole.getSp());
		battle.getFighters().put(fighter.getBattleindex(), fighter);
		battle.getRoundresult().put(fighter.getBattleindex(), 0);
		battle.getRoleids().put(fighter.getUniqueid(), fighter.getBattleindex());
		fire.pb.battle.Fighter ofighter = new fire.pb.battle.Fighter(battle,index);
		
		battle.getFighterobjects().put(index, ofighter);
		if(xtable.Roleid2battleid.select(roleID) != null)
			xtable.Roleid2battleid.remove(roleID);
		xtable.Roleid2battleid.insert(roleID, battle.getBattleid());
		
		if(battle.getBattletype()==BattleType.BATTLE_PVP){
			//满血满魔
			fire.pb.battle.Fighter fo = battle.getFighterobjects().get(index);
			fo.getEffectRole().fullHpAndRecoverWound();
			fo.getEffectRole().fullMp();
			SRefreshRoleData srrd = new SRefreshRoleData();
			srrd.datas.put(AttrType.HP, (float) fo.getEffectRole().getMaxHp());
			srrd.datas.put(AttrType.MP, (float) fo.getEffectRole().getMaxMp());
			srrd.datas.put(AttrType.UP_LIMITED_HP, (float) fo.getEffectRole().getUplimithp());
			Onlines.getInstance().send(roleID, srrd);
		}

		if (battle.getBattletype() == BattleType.BATTLE_LINE&&fire.pb.mission.instance.line.LineInstManager.checkAaward(roleID) == true)
		{						
			ofighter.setBounusNum(1);			
		}
		
		//设置初始怒气值
		int nNuQi = (int) (erole.getSp()+erole.getAttrById(AttrType.TEMP_SP));
		if (nNuQi > PropConf.Battle.BATTLEENTER_SP_MAX+erole.getAttrById(AttrType.TEMP_SP))
			nNuQi = (int)(PropConf.Battle.BATTLEENTER_SP_MAX+erole.getAttrById(AttrType.TEMP_SP));
		erole.setSp(nNuQi);

		//本来时在切磋的时候才要存原始的fighter,但是后来新的需求导致还是都存一份吧
		xbean.Fighter fighterClone=xbean.Pod.newFighter();
		fighterClone.setBattleindex(index);
		fighterClone.setFightertype(xbean.Fighter.FIGHTER_ROLE);
		fighterClone.setUniqueid(roleID);
		fighterClone.setFightername(role.getName());
		fighterClone.setInihp(role.getHp());
		fighterClone.setInilevel(erole.getLevel());
		fighterClone.setInimp(role.getMp());
		fighterClone.setInisp(erole.getSp());
		battle.getInifighters().put(index, fighterClone);
		boolean issuppress = BattleField.isFormSuppressed(battle, isHost);
		BattleField.Position pos = addFormationBuff(battle, index, issuppress,formId, formLevel);//添加阵型buff,设置角色位置
		
		//保存进战斗前的二级属性。在此之前，也就是enterBattleRole之前加的buff都算战斗外加载buff，不改变属性。后面的都是战斗内加载的
		fighter.getInitattrs().put(AttrType.HIT_RATE, erole.getAttrById(AttrType.HIT_RATE));
		fighter.getInitattrs().put(AttrType.ATTACK, erole.getAttrById(AttrType.ATTACK));
		fighter.getInitattrs().put(AttrType.DEFEND, erole.getAttrById(AttrType.DEFEND));
		fighter.getInitattrs().put(AttrType.MAGIC_ATTACK, erole.getAttrById(AttrType.MAGIC_ATTACK));
		fighter.getInitattrs().put(AttrType.MAGIC_DEF, erole.getAttrById(AttrType.MAGIC_DEF));
		fighter.getInitattrs().put(AttrType.SPEED, erole.getAttrById(AttrType.SPEED));
		fighter.getInitattrs().put(AttrType.DODGE_RATE, erole.getAttrById(AttrType.DODGE_RATE));
		fighter.getInitattrs().put(AttrType.MEDICAL, erole.getAttrById(AttrType.MEDICAL));		
		fighter.getInitattrs().put(AttrType.EFFECT_POINT, 1.0f);
		
		fighter.setPositionx(pos.x);
		fighter.setPositiony(pos.y);
		//WulinmijiUtil.setOpFightJsEngineMijiParameter(ofighter.getFighterBean().getUniqueid(), 
		//		battle.getEngine().getEngine());
		//ofighter.getSkillAgent().addSkillBuffWhileBattle(battle);
		
		if (BattleField.logger.isDebugEnabled()) {
			BattleField.logger.debug("============加入战斗\t" + fighter);
	
			BattleField.logger.debug("=====printFinalAttrs begin=====");
			BattleField.logger.debug("monster id:" + role.getRoleId()
					+ "\t" + "name:" + role.getName()
					+ "\t" + "lv:" + role.getLevel());
			for (int i = 0; i < Module.fightAttrTypeIds.length; i++)
			{
				int attrId = Module.fightAttrTypeIds[i];
				float value = erole.getAttrById(attrId);
				if(value > 0)
					BattleField.logger.debug("attr id:" + attrId + "\t" + "value:" + value);
			}
			BattleField.logger.debug("=====printFinalAttrs end=====");
		}
		
		if(fire.pb.hook.RoleHookManager.getInstance().getLockScreenStateByRoleId(roleID) == true||(xtable.Roleonoffstate.get(roleID)!=null && (xtable.Roleonoffstate.get(roleID) == State.OFFLINE_PROTECT_STATE||xtable.Roleonoffstate.get(roleID) == State.TRUSTEESHIP_STATE)))
		{
			if(!battle.getOfflineroleids().contains(roleID))
				battle.getOfflineroleids().add(roleID);
		}
		
		//加站前的被动技能 by changhao
		//PNewBattle.addPassiveSkillBuffs(battle, fighter.getBattleindex());
		
		PropRole prole = new PropRole(roleID, true);
		if(prole.getSchool() == SchoolConst.HUNTER)
		{
			Equip equip = new Equip(roleID, true);
			ItemBase bi = equip.getWeapon();
			if(bi!= null && bi.getItemAttr().getTypeid() == 1800)
			{
				ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_BEHUNTER_BOW);
				buff.setRound(-1);//被动战斗技能buff只有回合，不设置时间和量
				ofighter.getBuffAgent().addCBuff(buff);
			}
		}
		
		return true;
	}
	
	/**
	 * 在战斗开始时发送战斗角色需要加载的被动战斗技能buff
	 * @param battle
	 * @param fighterId
	 * @return positionId
	 */
	private static BattleField.Position addFormationBuff(final xbean.BattleInfo battle, final int fighterId, boolean isSuppress, int formId, int formLevel)
	{
		int index = (fighterId-1)%BattleField.A_SIDE_FIGHTER_NUM;
		if(formId == 0)
		{
			FormationConfig formconf = fire.pb.battle.Module.getInstance().getFormationById(0, 0);
			return formconf.getPositions().get(index+1);
		}
		else
		{
			fire.pb.battle.Fighter fighter = battle.getFighterobjects().get(fighterId);
			ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_FORMATION);
			FormationConfig formconf = fire.pb.battle.Module.getInstance().getFormationById(formId, formLevel);
			buff.getBuffBean().getEffects().clear();
			if (!isSuppress && index < formconf.getEffects().length && formconf.getEffects()[index] != null)
				buff.getBuffBean().getEffects().putAll(formconf.getEffects()[index]);
			if (isSuppress && index < formconf.getSuppressEffects().length && formconf.getSuppressEffects()[index] != null)
				buff.getBuffBean().getEffects().putAll(formconf.getEffects()[index]);
			if(!buff.getEffects().isEmpty())
				fighter.getBuffAgent().addCBuff(buff);
			return formconf.getPositions().get(index+1);
		}
	}
	
	/**
	 * 宠物进入战斗
	 * @param battle	战斗
	 * @param roleID	宠物主人角色ID
	 * @param index		宠物的战斗索引
	 * @return false 添加失败 ；true 添加成功
	 */
	public static boolean enterBattlePet(final xbean.BattleInfo battle, final long roleID, final int index,boolean isHost){
		final xbean.Properties pro = xtable.Properties.select(roleID);
		if(null == pro)
			return false;
		
		final int petKey = pro.getFightpetkey();
		if(petKey < 1)
			return false;
		
		final PetColumn col = new PetColumn(roleID, PetColumnTypes.PET, true);
		final xbean.PetInfo pet = col.getPetInfo(petKey);
		if(null == pet)
			return false;
//		if(pet.getLoy() <= 0)
//		{
//			return false;//宠物忠诚度为0时，不出战
//		}
		
		//如果寿命是永久的 就不用检查了
		boolean isLifeForever = fire.pb.pet.Module.getInstance().getPetManager().isPetLifeForever(pet.getId());
		if(pet.getLife() < DataInit.PET_FIGHT_LIFE_LIMIT && !isLifeForever)
		{
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleID, 141392, null);
			return false;
		}
		
		PetColumn petColumn = new PetColumn(roleID, PetColumnTypes.PET, false);
		xbean.PetInfo petInfo = petColumn.getPetInfo(petKey);
		if (petInfo == null)
			return false;
		final fire.pb.effect.PetImpl petImpl = new fire.pb.effect.PetImpl(petInfo);
		
		final xbean.Fighter fighter = xbean.Pod.newFighter();
		fighter.setBattleindex(index);
		fighter.setFightertype(xbean.Fighter.FIGHTER_PET);
		fighter.setFightername(pet.getName());
		fighter.setUniqueid(pet.getId());
		fighter.setInihp(petImpl.getHp());
		fighter.setInilevel(petImpl.getLevel());
		fighter.setInimp(petImpl.getMp());
		fighter.setIshost(isHost);
		fighter.setSubtype(Pet.getClour(PetColour.WHITE));
		battle.setAutokey(battle.getAutokey() + 1);
		fighter.setFighterkey(battle.getAutokey());
		//宠物的位置不管什么光环都是一样的
		FormationConfig formconf = fire.pb.battle.Module.getInstance().getFormationById(0, 0);
		BattleField.Position battlepos = formconf.getPositions().get(index%BattleField.A_SIDE_FIGHTER_NUM); 
		fighter.setPositionx(battlepos.x);
		fighter.setPositiony(battlepos.y);
		battle.getFighters().put(fighter.getBattleindex(), fighter);
		battle.getRoundresult().put(fighter.getBattleindex(), 0);
		battle.getDecisions().remove(index);
		Fighter ofighter = new fire.pb.battle.Fighter(battle,index);
		
		for(int existbuffId : ofighter.getBuffAgent().getAllBuffBeans().keySet())
		{
			ConstantlyBuffConfig buffcfg = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(existbuffId);
			if(buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_OUT_BATTLE
					|| buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_DEATH
					|| buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_HURT)
			{
				ofighter.getBuffAgent().removeCBuff(existbuffId);
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("宠物Buff："+existbuffId+"被带到了战斗外，战斗ID = " + battle.getBattleid()+"，角色ID = " +roleID);}
			}
		}
		
		battle.getFighterobjects().put(index,ofighter );
		if(battle.getBattletype()==BattleType.BATTLE_PVP){
			//满血满魔
			fire.pb.battle.Fighter fo = battle.getFighterobjects().get(index);
			fo.getEffectRole().fullHp();
			fo.getEffectRole().fullMp();
			PlayPKEndHandler.refreshPetHpMp(roleID, fo);
		}
		//本来时在切磋的时候才要存原始的fighter,但是后来新的需求导致还是都存一份吧
		xbean.Fighter fighterClone=xbean.Pod.newFighter();
		fighterClone.setBattleindex(index);
		fighterClone.setFightertype(xbean.Fighter.FIGHTER_PET);
		fighterClone.setUniqueid(pet.getId());
		fighterClone.setFightername(pet.getName());
		fighterClone.setInihp(petImpl.getHp());
		fighterClone.setInimp(petImpl.getMp());
		battle.getInifighters().put(index, fighterClone);
		//加载宠物被动技能buff
		ofighter.getSkillAgent().addSkillBuffWhileBattle(battle, ofighter);
		//保存进战斗前的二级属性。在此之前，也就是enterBattlePet之前加的buff都算战斗外加载buff，不改变属性。后面的都是战斗内加载的
		fighter.getInitattrs().put(AttrType.HIT_RATE, petImpl.getAttrById(AttrType.HIT_RATE));
		fighter.getInitattrs().put(AttrType.ATTACK, petImpl.getAttrById(AttrType.ATTACK));
		fighter.getInitattrs().put(AttrType.DEFEND, petImpl.getAttrById(AttrType.DEFEND));
		fighter.getInitattrs().put(AttrType.MAGIC_ATTACK, petImpl.getAttrById(AttrType.MAGIC_ATTACK));
		fighter.getInitattrs().put(AttrType.MAGIC_DEF, petImpl.getAttrById(AttrType.MAGIC_DEF));
		fighter.getInitattrs().put(AttrType.SPEED, petImpl.getAttrById(AttrType.SPEED));
		fighter.getInitattrs().put(AttrType.DODGE_RATE, petImpl.getAttrById(AttrType.DODGE_RATE));
		fighter.getInitattrs().put(AttrType.EFFECT_POINT, 1.0f);
	
		if (BattleField.logger.isDebugEnabled()) {
			BattleField.logger.debug("============加入战斗\t" + fighter);
		
			BattleField.logger.debug("=====printFinalAttrs begin=====");
			BattleField.logger.debug("pet id:" + pet.getId()
					+ "\t" + "name:" + pet.getName()
					+ "\t" + "lv:" + pet.getLevel());
			for (int i = 0; i < Module.fightAttrTypeIds.length; i++)
			{
				int attrId = Module.fightAttrTypeIds[i];
				float value = petImpl.getAttrById(attrId);
				if(value > 0)
					BattleField.logger.debug("attr id:" + attrId + "\t" + "value:" + value);
			}
			BattleField.logger.debug("=====printFinalAttrs end=====");
		}
		xbean.Fighter owner = battle.getFighters().get(index-5);
		owner.getFightedpets().put(petKey, ResultType.RESULT_SUMMONPET);
		
		return true;
	}
	/**
	 * 在战斗开始时发送战斗者需要加载的被动战斗技能buff
	 * @param battle
	 * @param fighterId
	 */
	public static void addPassiveSkillBuffs(final xbean.BattleInfo battle, final int fighterId)
	{
		Fighter fighter = battle.getFighterobjects().get(fighterId);
		SkillAgent skillagent = fighter.getSkillAgent();
		if(skillagent == null) return;
		Result result = new Result(true);
		boolean bregfighter = false;
		for(int skillId : skillagent.getAllBattleSkills())
		{
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
			if(sconf == null)
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());}
				continue;
			}
			if(!sconf.isActiveSkill() && sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_PASSIVE 
					&& sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
			{//被动技能，且有buff(一定是ConstantlyBuff)
				if(bregfighter == false)
				{
					battle.getEngine().setOpFighter(fighter,sconf.getUsedAttrTypea());// 设置技能释放者参数
					bregfighter = true;
				}
				battle.getEngine().setSkillLevel(skillagent.getLevel(skillId));
				for (SubSkillConfig subskill : sconf.getSubSkills())
				{
					for (BuffUnit buffarg : subskill.getBuffUnits())
					{
						if(buffarg==null) continue;
						ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);
						int nround = buffarg.roundJavascript.eval(battle.getEngine(),fighter,null).intValue();
						if(nround != 0 )
							buff.setRound(nround);//被动战斗技能buff只有回合，时间和量不设置
						for(Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
						{//buff效果
							buff.getEffects().put(entry.getKey(), entry.getValue().eval(battle.getEngine(),fighter,null).floatValue());
						}
						result.updateResult(fighter.getBuffAgent().addCBuff(buff));
					}
				}				
			}				
		}
		//将战斗被动技能Buff发给客户端
		fighter.getBuffAgent().psendSBuffChangeResult(result);
	}

	
	/**
	 * 怪物加入战斗  可能将来会用怪物的base id来获得一些属性
	 * @param battle	战斗
	 * @param baseID	怪物的ID
	 * @param index		战斗索引
	 * @return false 添加失败 ；true 添加成功
	 */
	public static boolean enterBattleMonster(final xbean.BattleInfo battle, final Monster monster, int index, boolean isHost,int formId, int formLevel, ConstantlyBuff cbuff){
		
		if(monster.isNpcMonster())
		{
			if(monster.getSMonsterConfig().getLevelType() == 6)
			{
				Float cfglv = battle.getBattledatas().get(xbean.BattleInfo.DATA_BATTLE_CONFIG_LEVEL);
				if(cfglv != null)
					monster.setLevel(cfglv.intValue());
			}
		}
		final xbean.Fighter fighter = xbean.Pod.newFighter();
		xbean.Monster monsterBean= monster.getMonsterbean(battle);
		if(monsterBean == null) return false;
		
		index = BattleField.getAvailableFighterId(battle, index, isHost);//查看fighterID是否可用，如不可用查找到一个可用的id
		if(index == 0)//没有可用的fighterId（fighter已满）
			return false;
		
		fighter.setBattleindex(index);
		fighter.setFightertype(monsterBean.getFightnpctype());
		fighter.setFightername(monster.getName());
		fighter.setUniqueid(monsterBean.getMonsterid());
		fighter.setIshost(isHost);
		fighter.setInihp(monsterBean.getHp());
		//怪的站位也要跟光环走，因为光环会影响人的站位，招出来的怪也得相应变
		fighter.setSubtype(monsterBean.getMonstertype());
		battle.setAutokey(battle.getAutokey() + 1);
		fighter.setFighterkey(battle.getAutokey());
		battle.getFighters().put(fighter.getBattleindex(), fighter);
		battle.getRoundresult().put(fighter.getBattleindex(), 0);
		battle.getMonsters().put(index, monsterBean);
		battle.getDecisions().remove(index);
		Fighter ofighter =  new fire.pb.battle.Fighter(battle,index);
		battle.getFighterobjects().put(index,ofighter);
		
		boolean issuppress = BattleField.isFormSuppressed(battle, isHost);
		BattleField.Position battlepos2 =addFormationBuff(battle, index, issuppress, formId, formLevel);
		fighter.setPositionx(battlepos2.x);
		fighter.setPositiony(battlepos2.y);
		if(cbuff != null){
			ofighter.getBuffAgent().addCBuff(cbuff);
		}
		ofighter.getSkillAgent().addSkillBuffWhileBattle(battle, ofighter);
		
		//保存进战斗前的二级属性。在此之前，也就是enterBattlePet之前加的buff都算战斗外加载buff，不改变属性。后面的都是战斗内加载的
		if(monster.getSMonsterConfig() != null)
			fighter.getInitattrs().put(AttrType.EFFECT_POINT, (float)monster.getSMonsterConfig().getDefaultBattleEp());
		else
			fighter.getInitattrs().put(AttrType.EFFECT_POINT, 1.0f);

		return true;
	}
	
	/**
	 * 怪物加入战斗  可能将来会用怪物的base id来获得一些属性
	 * @param battle	战斗
	 * @param baseID	怪物的ID
	 * @param index		战斗索引
	 * @return false 添加失败 ；true 添加成功
	 */
	public static boolean enterBattleMonster(final xbean.BattleInfo battle, final Monster monster, int index, boolean isHost,int formId, int formLevel){
		return enterBattleMonster(battle, monster, index, isHost, formId, formLevel, null);
	}

	/**
	 * 通过角色id，获取本方要参战的所有角色id
	 * @param roleID
	 * @return
	 */
	public static java.util.List<Long> getSideRoleIDs(final long roleID){
		List<Long> roleIds = new LinkedList<Long>();
		Team team = TeamManager.getTeamByRoleId(roleID);
		if(team == null)
		{
			roleIds.add(roleID);
			return roleIds;
		}
		if(team.isAbsentMember(roleID))
		{
			roleIds.add(roleID);
			return roleIds;
		}
		if(team.isTeamLeader(roleID))
		{
			return team.getFighterMemberIds();
		}
		
		return roleIds;
	}
	
	private boolean checkNewBattle(){
		// 检查当前是否在战斗中，如果在战斗中，则无效
		// TODO后面改用buff 进行判断
		if(null != xtable.Roleid2battleid.select(hostRoleID))
			return false;
		
		if(0 != guestRoleID){
			if(null != xtable.Roleid2battleid.select(guestRoleID))
				return false;
		}
		if(hostRoleIds.isEmpty())
			return false;
		
		
		// 检查所有战斗者是否处于冲突状态
		for (long hostRoleId : hostRoleIds)
		{
			if(!checkCanFight(hostRoleId))
			{
				if (BattleField.logger.isInfoEnabled()) {BattleField.logger.info("战斗者(roleId = " + hostRoleId+ ")不能进入战斗，导致战斗不能开始。"
						+ hostRoleIds.toString());}
				return false;
			}
		}
		
		if(ispvp)
		{
			if(guestRoleIds.isEmpty())
				return false;
			// 检查是否在交易，如果在交易 ，则 关闭交易
			for (long guestRoleId : guestRoleIds)
			{
				if(!checkCanFight(guestRoleId))
				{   //guest不符合条件,要通知host队长
					fire.pb.talk.MessageMgr.sendMsgNotify(hostRoleID, fire.pb.util.FireProp.getIntValue("message", "mtgserv.buffConflict"), null);
					if (BattleField.logger.isInfoEnabled()) {BattleField.logger.info("战斗者(roleId = " + guestRoleId+ ")不能加入战斗，导致战斗不能开始。"
							+ guestRoleIds.toString());}
					return false;
				}
			}
		}
		
		
		return true;
	}
	
	private boolean checkCanFight(long roleId)
	{
		
		BuffAgent buffagent = new BuffRoleImpl(roleId);
		// 检查buff冲突
		if (!buffagent.canAddBuff(BuffConstant.StateType.STATE_BATTLE_FIGHTER))
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, fire.pb.util.FireProp.getIntValue("message", "mtgserv.buffConflict"), null);
			return false;
		}
		
		return true;
	}

	public long outcosttime(long nowTime ,int nStep)
	{
		long nowTime1 = System.currentTimeMillis();
		if (BattleField.logger.isDebugEnabled()) {
			BattleField.logger.debug("--------------------------------------------------------------------------------");		
			BattleField.logger.debug("PNewBattle.process step:"+nStep+" 花费时间为:"+(nowTime1 - nowTime)+"毫秒");		
			BattleField.logger.debug("--------------------------------------------------------------------------------");
		}
		return nowTime1;
	}
	
	public boolean process(){
//		long nowTime = System.currentTimeMillis();
		/**************************确定战斗的基本类型是pvp还是pve**************************/
		ispvp = checkpvp();
		/***************************获取己方和敌方的角色，锁队伍，锁所有角色**********************************/
		//锁队伍
		List<Long> teamids = new LinkedList<Long>();
		Long hostteamid = xtable.Roleid2teamid.select(hostRoleID);
		if(hostteamid != null)
			teamids.add(hostteamid);
		if(ispvp)
		{
			Long guestteamid = xtable.Roleid2teamid.select(guestRoleID);
			if(guestteamid != null)
				teamids.add(guestteamid);
		}
		if(!teamids.isEmpty())
			lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, teamids));
		//获取要进战斗的所有角色
		hostRoleIds = getSideRoleIDs(hostRoleID);
		if(hostRoleIds.isEmpty())
			return false;
		if(ispvp)
		{
			guestRoleIds = getSideRoleIDs(guestRoleID);
			if(guestRoleIds.isEmpty())
				return false;
		}
		//锁所有战斗角色
		List<Long> allRoleIDs = new LinkedList<Long>();
		allRoleIDs.addAll(hostRoleIds);
		if(ispvp)
			allRoleIDs.addAll(guestRoleIds);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
		
		/*************************检查能否进战斗**************************/
		//观战状态下不能进入pve战斗,但是可以被拉入pvp战斗
		if (!ispvp){
			if(xtable.Watcherid2battleid.select(hostRoleID)!=null){
				return false;
			}
		}
		if(!checkNewBattle())
			return false;
		
		/**************************锁战斗外npc**************************/
		if (monsterNpckey>0)
		{
			if(!setNpcInBattle(monsterNpckey)) {
				fire.pb.item.Module.getInstance().getLogger().info("该npc正在战斗");
				return false;
			}
		}
//		nowTime = outcosttime(nowTime, 1);

		/******************************初始化战斗******************************/
		NewBattleInfo();// 创建战斗，初始化战斗
		
		//如果是通过战斗信息表新建的战斗，获取BattleConfig
		BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battleConfigId);
		if(battleConfig!=null){
			processBattleConfig(battleConfig);
		}
		battle.setBattlebegintime(System.currentTimeMillis());
		if(battleRound > 0){
			battle.setBattlemaxround(battleRound);
		}
		battle.setLivedieteam(isLiveDieTeamFight);//设置是否是组队生死战
		
		
		processTeamFormation();//处理双方光环，计算光环相克
		
//		nowTime = outcosttime(nowTime, 2);
		/******************************添加所有的战斗单元******************************/
		enterRoles(hostRoleIds,true);// 添加主方战斗者和宠物
		calcBattleConfigLevel(battleConfig);//如果有战斗配置，且战斗配置里有等级配置，则计算该等级
		if (ispvp){ // PVP 添加客方角色
			enterRoles(guestRoleIds,false);// 添加客方战斗者和宠物
		} else{// PVE 添加怪物，包括己方的怪物
			enterMonsters(monsters, battleConfig);//添加怪物
		}
		
		for (Fighter f : battle.getFighterobjects().values())
		{
			if(battle.getBattletype() == BattleType.BATTLE_CLANBOSS 
					&& f.getFightertype() > xbean.Fighter.FIGHTER_PARTNER 
					&& f.getFighterBean().getSubtype() == Monster.MONSTER_CLANBOSS)
			{
				InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(battle.getHostroleid(), false);
				if (instzone != null) {
					if (instzone instanceof FactionInstZone) {
						xbean.ClanInstances clanInst = ((FactionInstZone) instzone).getClanInstData();
						
						xbean.ClanBossInfo bossinfo = clanInst.getBossinfo().get(battleConfigId);
						if (bossinfo != null) {
							f.getEffectRole().setHp((int) bossinfo.getHp());
							bossinfo.setBossnpckey(instnpckey);
						}
						else {
							bossinfo = xbean.Pod.newClanBossInfo();
							int curactsave = instzone.getCurActiveSave();
							InstanceSaveConfig savecfg = instzone.getZoneConfig().saves.get(curactsave);
							
							bossinfo.setBossmonsterid(savecfg.haveboss);
							bossinfo.setHp(f.getFighterBean().getInihp());
							bossinfo.setMaxhp(f.getFighterBean().getInihp());
							bossinfo.setFighteindx(f.getFighterId());
							bossinfo.setBossnpckey(instnpckey);
							clanInst.getBossinfo().put(battleConfigId, bossinfo);
						}
						
						//开始同步血量
						new fire.pb.battle.PSynchroBossHp(
								bossinfo.getBossmonsterid(),
								battle.getHostroleid(),
								bossinfo.getMaxhp(),
								bossinfo.getHp(),
								(byte)0, 0).call();
					}
				}
			}
			BattleField.calTempSpeed(f);
		}

		if (battle.getBattletype() == BattleType.BATTLE_CLANBOSS) {
			for (Long roleId : battle.getRoleids().keySet()) {
				BuffAgent hostAgent = new BuffRoleImpl(roleId, false);
				hostAgent.addCBuffWithSP(BuffConstant.CONTINUAL_CLANBOSS_SPASE);
			}
		}

		//这里生死战不能带伙伴
		int nHuobanNum = 0;
		if(this.battleType!=BattleType.BATTLE_LIVEDIE && this.battleType != BattleType.BATTLE_CLAN_FIGHT)
		{
			nHuobanNum = processHireNpcMonster(battle, hostRoleIds,battleConfig, true);//处理雇佣npc buff，只有PVE时，PVP暂时不带雇佣npc
			if (ispvp)
				nHuobanNum += processHireNpcMonster(battle, guestRoleIds,battleConfig, false);//处理雇佣npc buff，只有PVE时，PVP暂时不带雇佣npc
		}
		if(nHuobanNum > 0)
		{
			for (Fighter f : battle.getFighterobjects().values())
			{
				if(f.isPartner())
				{
					SkillAgent skillagent = f.getSkillAgent();
					for(int skillId : skillagent.getAllBattleSkills())
					{
						battle.getEngine().setSkillLevel(skillagent.getLevel(skillId));
						FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
						if(sconf == null)
						{
							if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("怪物 "+f.getFighterBean().getFightername()+" FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());}
							continue;
						}
						if(!sconf.isActiveSkill() && (sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO||sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO2) 
								&& sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
						{//光环技能，且有buff(一定是ConstantlyBuff)
							if(!skillagent.hasEffectSkill(skillId))
								continue;//如果同时有对应的高级宠物技能，则低级的不能生效
							{
								BuffUnit buffarg = sconf.getSubSkills()[0].getBuffUnits()[0];
								if(buffarg==null) continue;
								ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);
								
								int nround = buffarg.roundJavascript.eval(battle.getEngine(),f,null).intValue();
								if(nround != 0 )
									buff.setRound(nround);//被动战斗技能buff只有回合，时间和量不设置
								for(Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
								{//buff效果
									buff.getEffects().put(entry.getKey(), entry.getValue().eval(battle.getEngine(),f,null).floatValue());
								}
								//	
								buff.getBuffBean().setFighterkey(f.getFighterId());
								if(sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO)
								{
									for (int friend : BattleField.getFriends(battle, f.getFighterId()))
									{
										if(!BattleField.validateaim(battle, sconf.getSubSkills()[0], f.getFighterId(), friend))
											continue;									
										Fighter fighter = battle.getFighterobjects().get(friend);
										if (fighter != null)
										{
											// 2.1.4 复制buff
											ConstantlyBuff buffcopy = buff.copy();
											Result result = new Result(true);
											result.updateResult(fighter.getBuffAgent().addCBuff(buffcopy));
											fighter.getBuffAgent().psendSBuffChangeResult(result);
										}
									}
								}
								else if(sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO2)
								{
									List<Integer> poss = fire.pb.battle.Module.getInstance().getEffectPoss(
											FormationConfig.GetPosbyFighteid(f.getFighterId()<15? battle.getHostform():battle.getGuestform(),f.getFighterId()));
									poss = FormationConfig.GetPosbyFighteids(f.getFighterId()<15? battle.getHostform():battle.getGuestform(),poss,f.getFighterId()<15);
									if(poss != null)
									{
										for (int friend : poss)
										{
											if(!BattleField.validateaim(battle, sconf.getSubSkills()[0], f.getFighterId(), friend))
												continue;									
											Fighter fighter = battle.getFighterobjects().get(friend);
											if (fighter != null)
											{
												// 2.1.4 复制buff
												ConstantlyBuff buffcopy = buff.copy();
												Result result = new Result(true);
												result.updateResult(fighter.getBuffAgent().addCBuff(buffcopy));
												fighter.getBuffAgent().psendSBuffChangeResult(result);
											}
										}
									}
								}
							}
						}				
					}
				}
			}
			//检测历程
			fire.pb.course.CourseManager.achieveCourse(hostRoleID, fire.pb.course.CourseType.HUOBAN_COURSE, nHuobanNum, 0);
		}

		//计算乱敏的速度
		for (Fighter f : battle.getFighterobjects().values())
		{
			BattleField.calTempSpeed(f);
		}
//		nowTime = outcosttime(nowTime, 3);
		
		
		//设置角色背包满的情况,判断是否有队伍
		boolean flag=true;
		for (Long roleId : battle.getRoleids().keySet()) {
			if(roleId==null){
				continue;
			}
			final PetColumn petcol = new PetColumn(roleId, PetColumnTypes.PET, true);
			if (petcol.size() < petcol.getCapacity()) {
				flag=false;
				break;
			}
		}
		if(flag){
			battle.setBabybagfull(1);
		}
		
		/******************************战斗可以开始，发送协议******************************/
		sendProtocol();// 向战斗双方发送战斗开始消息
		log();//日志
		writeLogs(hostRoleID, guestRoleID);
		battle.setPhase(BattleInfo.PHASE_WAITOPERATION);
		mkdb.Executor.getInstance().schedule(new DelayRoundStart(battleID, allRoleIDs), BATTLE_START_DELAY,  TimeUnit.MILLISECONDS);
		
/*		if((CReqNewHandBattle.BASE_BATTLEID <= battleConfigId )&& (battleConfigId <= CReqNewHandBattle.BASE_BATTLEID + 9))
		{ //新手战斗特别技能
			Map<Integer, Integer> sextskill = new HashMap<Integer, Integer>();			
			for(long roleid : battle.getRoleids().keySet())
			{//这里估计应该是只有1个角色
				PropRole prole = new PropRole(roleid, false);
				SkillRole srole = new SkillRole(roleid);
				int skillID = (160+prole.getSchool()) * 1000 + 006;
				sextskill.put(skillID, 1);
				srole.addExtSkillWithSP(sextskill);
				break;
			}
		} */
		//如果是生死战，发送观战连接
		if(battle.getBattletype() == BattleType.BATTLE_LIVEDIE){
			//发送链接
			fire.pb.PropRole hostRole = new PropRole(hostRoleID, true);
			fire.pb.PropRole guestRole = new PropRole(guestRoleID, true);
			MessageMgr.sendSystemMsg(162099,Arrays.asList(guestRole.getName(),hostRole.getName(),getBattleId()+""));
			LiveDieMange.liveDieFightRoleids.put(hostRoleID, guestRoleID);
		}
//		nowTime = outcosttime(nowTime, 4);
		
		//如果是公会副本战斗,设置npc状态
		if (battle.getBattletype() == BattleType.BATTLE_INST_BATTLE) {
			fire.pb.instancezone.Module.getInstance().setDynamicNpcState(landkey, instnpckey, 1, getBattleId());
		}
		return true; 
	}

	/**
	 * 通过战斗的客方，检查战斗的基本类型是pvp还是pve
	 * @return
	 */
	private boolean checkpvp()
	{
		if(null == monsters || monsters.size() == 0)
		{
			if(guestRoleID >0)
				return true;			
			else{
				throw new IllegalArgumentException("战斗没有怪物列表，也没有客方角色ID，无法开启战斗");
			}
		}
		return false;
	}
	
	private void NewBattleInfo()
	{
		// 创建战斗
		battle = xbean.Pod.newBattleInfo();
		battleID = xtable.Battle.insert(battle);
		battle.setBattleid(battleID);
		battle.setGmactiontimetimes(10);

		//设置自定义参数
		battle.getParams().putAll(params);
		
		//设置战斗类型
		if(isClanBossFight())
		{
			battle.setBattletype(BattleType.BATTLE_CLANBOSS);
			battleType = BattleType.BATTLE_CLANBOSS;
		}
		else if(battleType != 0)
			battle.setBattletype(battleType);
		else if(guestRoleID > 0)//有敌方玩家的战斗
			battle.setBattletype(BattleType.BATTLE_PVP);
		else
			battle.setBattletype(BattleType.BATTLE_PVE);
		
		battle.setHostroleid(hostRoleID);//设置操作者id
		
		battle.setMonsterid(monsterNpckey);
		battle.setAreaconf(areaconf);
		battle.setAreatype(areatype);
		battle.setConfigid(battleConfigId);
		battle.setMonstertype(monstertype);
		battle.setCanrunaway(canrunaway);
		//初始化JS引擎
		battle.setEngine(new FightJSEngine());
		//设置虚拟战场Fighter
		battle.setFieldfighter(new BattleFieldFighter(battle, 0));
		//battle handler
		if (handler != null)
		{
			handler.setBattle(battle);
			battle.getBattleendhandlers().add(handler);
		}
		
		
	}
	
	//设置光环
	private void processTeamFormation(){
		hostform = getTeamFormation(hostRoleID);
		battle.setHostform(hostform);
		hostformLevel = getFormationLevel(hostRoleID);
		battle.setHostformlevel(hostformLevel);
		
		guestform = getTeamFormation(guestRoleID);
		battle.setGuestform(guestform);
		guestformLevel = getFormationLevel(guestRoleID);
		battle.setGuestformlevel(guestformLevel);
		
		FormationConfig hostformconf =  fire.pb.battle.Module.getInstance().getFormationById(hostform, hostformLevel);
		FormationConfig guestformconf =  fire.pb.battle.Module.getInstance().getFormationById(guestform, guestformLevel);
		if(hostformconf.getmapSuppressform1().containsKey(guestform))
			battle.setHostformeffcet(hostformconf.getmapSuppressform1().get(guestform));
		else if(hostformconf.getmapSuppressform2().containsKey(guestform))
			battle.setHostformeffcet(hostformconf.getmapSuppressform2().get(guestform));
		
		else if(guestformconf.getmapSuppressform1().containsKey(hostform))
			battle.setHostformeffcet(-guestformconf.getmapSuppressform1().get(hostform));
		else if(guestformconf.getmapSuppressform2().containsKey(hostform))
			battle.setHostformeffcet(-guestformconf.getmapSuppressform2().get(hostform));
		
		else
			battle.setHostformeffcet(0.0f);
	}
	
	//获取光环
	protected int getTeamFormation(long roleId){
		return TeamManager.getTeamFormationForBattle(roleId);
	}
	
	protected int getFormationLevel(long roleId){
		return TeamManager.getTeamFormationLevelForBattle(roleId);
	}
	
	protected int processHireNpcMonster(xbean.BattleInfo battle,List<Long> roleids, BattleConfig battleConfig, boolean ishost){
		//if(ispvp) return;
		int huoBanNumber = 4;
		huoBanNumber = 5 - roleids.size();
		if(battleConfig != null && battleConfig.xiezhannum < huoBanNumber){
			huoBanNumber = battleConfig.xiezhannum;
		}
		return	HireNpcManager.processBuffMonsters(battle, roleids, huoBanNumber,ishost);
	}
	
	/**
	 * 添加一方角色
	 * 
	 * @param enterRoleIDs
	 * @param ishost 主方/客方
	 */
	private void enterRoles(List<Long> enterRoleIDs, boolean ishost)
	{
		int index = ishost ? BattleField.HOST_FIRST_ID : BattleField.GUEST_FIRST_ID;
		int form = ishost ? hostform : guestform;
		int formLevle = ishost?hostformLevel:guestformLevel;
		
		
		// 添加所有的战斗单元
		for(long roleID : enterRoleIDs){
			enterBattleRole(battle, roleID, index, ishost, form, formLevle);
			enterBattlePet(battle, roleID, index + 5,ishost);
			index ++;
		}
		if(ishost)//主方要记录个数
			battle.getEngine().setHostRoleNum(BattleField.getHostRoleIDs(battle).size());
		
		//处理数据
		BattleField.calcRoleDatas(battle, ishost);
	}
	
	/**
	 * 添加所有怪物（包括主方客方）
	 * @param monsters
	 * @param battleConfig 如果为null，则不是通过战斗信息表新建的战斗
	 */
	private void enterMonsters(List<Monster> monsters, BattleConfig battleConfig)
	{
		int index = BattleField.GUEST_FIRST_ID;//默认从敌方第一个开始
		int monsterMaxCount = BattleField.A_SIDE_FIGHTER_NUM;//一方怪物上限
		if(battleConfig!=null)
		{
			try{
				monsterMaxCount = battleConfig.monsterNumScript.eval(battle.getEngine(),null,null).intValue();
			}catch(Exception e){
				BattleField.logger.error("计算怪物个数的脚本出错" + e);
			}
		}
		int guestMonsterNum = 0;
		for (Monster monster : monsters)
		{
			boolean isHost = false;
			if(monster.getFighterId()>=1)
			{
				index = monster.getFighterId();//有指定ID
				isHost = BattleField.isHost(index);//对于指定ID来说，判断其主客方
			}
			int form = isHost? hostform : guestform;
			int formLevel = isHost? hostformLevel : guestformLevel;
			enterBattleMonster(battle, monster, index, isHost, form, formLevel);
			if (monster.getFighterId() >= BattleField.HOST_FIRST_ID && monster.getFighterId() < BattleField.GUEST_FIRST_ID)
				continue;// 添加的是友方怪物
			else
			{// 添加的是敌方怪物
				guestMonsterNum++;
				if(guestMonsterNum >= monsterMaxCount)
					break;//达到怪物上限
			}
		}
		
		//处理数据
		BattleField.calcMonsterDatas(battle);
	}
	
	//处理战斗信息表配置
	private void processBattleConfig(BattleConfig cfg)
	{
		for(int aiid : cfg.battleAIs)
		{//初始化战场AI（怪物的战斗AI初始化在怪物初始化时）
			xbean.BattleAI ai = xbean.Pod.newBattleAI();
			ai.setId(aiid);
			battle.getBattleais().add(ai);
		}
		battle.setBackground(cfg.background);
		battle.setBgm(cfg.bgm);
	}
	
	//如果有战斗配置，且战斗配置里有等级配置，则计算该等级
	private void calcBattleConfigLevel(BattleConfig cfg)
	{
		if(cfg != null && cfg.leveljs != null)
		{
			Float teamlv = battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_AVERAGE_LEVEL);
			if(teamlv == null)
				teamlv = 1f;
			battle.getEngine().put(RewardMgr.TEAM_LEVEL, teamlv.intValue());
			
			Float maxlv = battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_MAX_LEVEL);
			if(maxlv == null)
				maxlv = 1f;
			battle.getEngine().put(RewardMgr.MAX_ROLE_LV, maxlv.intValue());
			
			Float minlv = battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_MIN_LEVEL);
			if(minlv == null)
				minlv = 1f;
			battle.getEngine().put(RewardMgr.MIN_ROLE_LV, minlv.intValue());
			
			Double cfglevel = cfg.leveljs.eval(battle.getEngine(),null,null);
			if(!cfglevel.equals(0))
				battle.getBattledatas().put(xbean.BattleInfo.DATA_BATTLE_CONFIG_LEVEL, cfglevel.floatValue());
		}
	}
	
	// 向战斗双方发送战斗消息
	private void sendProtocol()
	{
		final SSendBattleStart sndhost = new SSendBattleStart();
		final SSendBattleStart sndguest = new SSendBattleStart();
		sndhost.battleconfigid = battleConfigId;
		sndhost.battletype = battle.getBattletype();
		sndhost.friendsformationlevel = battle.getHostformlevel();
		sndhost.enemyformationlevel = battle.getGuestformlevel();
		
		sndguest.battleconfigid = battleConfigId;
		sndguest.battletype = battle.getBattletype();
		sndguest.friendsformationlevel = battle.getGuestformlevel();
		sndguest.enemyformationlevel = battle.getHostformlevel();
		sndhost.roundnum = -1;
		sndguest.roundnum = -1;
		Set<Long> hostRoleIds = BattleField.getHostRoleIDs(battle);
		Set<Long> guestRoleIds = BattleField.getGuestRoleIDs(battle);
		
		sndhost.background = (byte)battle.getBackground();
		sndguest.background = (byte)battle.getBackground();
		sndhost.backmusic = (byte)battle.getBgm();
		sndguest.backmusic = (byte)battle.getBgm();
		
		sndhost.enemyside = 0;
		sndhost.enemyformation = guestform;
		sndhost.friendsformation= hostform;
		psendWhileCommit(hostRoleIds, sndhost);
		sndguest.enemyside = 1;
		sndguest.enemyformation = hostform;
		sndguest.friendsformation= guestform;
		psendWhileCommit(guestRoleIds, sndguest);
		// 向战斗角色发送自己的二级属性初始值和当前值
		for (Map.Entry<Long, Integer> entry : battle.getRoleids().entrySet())
		{
			long roleId = entry.getKey();
			int fighterId = entry.getValue();
			Fighter fighter = battle.getFighterobjects().get(fighterId);
			SSendRoleInitAttrs sroleattr = new SSendRoleInitAttrs();
			sroleattr.roleinitattrs.putAll(fighter.getFighterBean().getInitattrs());
			psendWhileCommit(roleId, sroleattr);
			SRefreshRoleData sRefreshRoleData = new SRefreshRoleData();
			for(int attrId :  fighter.getFighterBean().getInitattrs().keySet())
				sRefreshRoleData.datas.put(attrId, fighter.getEffectRole().getAttrById(attrId));
			sRefreshRoleData.datas.put(AttrType.SP, fighter.getEffectRole().getAttrById(AttrType.SP));
			psendWhileCommit(roleId, sRefreshRoleData);
			Fighter petfighter = battle.getFighterobjects().get(fighterId + 5);
			if (petfighter != null)
			{
				SSendPetInitAttrs spetattr = new SSendPetInitAttrs();
				spetattr.petinitattrs.putAll(petfighter.getFighterBean().getInitattrs());
				psendWhileCommit(roleId, spetattr);
				SRefreshPetData sRefreshPetData = new SRefreshPetData();
				sRefreshPetData.columnid = PetColumnTypes.PET;
				PropRole prole = new PropRole(roleId, true);
				sRefreshPetData.petkey = prole.getFightpetkey();
				for(int attrId :  petfighter.getFighterBean().getInitattrs().keySet())
					sRefreshPetData.datas.put(attrId, petfighter.getEffectRole().getAttrById(attrId));
				psendWhileCommit(roleId, sRefreshPetData);
			}
			fighter.sendAlreadyUsedItems();
		}
		// 向所有战斗角色发送添加战场内的参战者信息
		final SSendAddFighters addsnd = new SSendAddFighters();
		for(xbean.Fighter f : battle.getFighters().values()){
			FighterInfo fi=BattleField.getFighterInfo(f,battle);
			if(f.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER){
				Integer battleTitleId = battle.getMonsters().get(f.getBattleindex()).getExtradata().get(xbean.Monster.BATTLE_TITLE_ID);
				if(battleTitleId != null) fi.titleid = battleTitleId;
			}
			addsnd.fighterlist.add(fi);
			//将这部分数据放入战斗信息中，如果需要存储录像时候需要使用
			battle.getFigterinfoonbegin().add(fi);
		}
		psendWhileCommit(battle.getRoleids().keySet(), addsnd);
		
		//发送其他未发送的buff
		BattleField.sendRoundChangeResult(battle);
		
		//进战斗通知场景
		for(long roleid : battle.getRoleids().keySet())
		{
			GsClient.pSendWhileCommit(new GSetRoleBattleInfo(roleid,1));
		}
	}
	
	//日志
	private void log()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" 战斗开始，ID = ").append(battleID).append("\t战斗配置  = ").append(battleConfigId)
		.append("\t战斗类型 = ").append(battle.getBattletype()).append("\t区域配置 = ").append(battle.getAreaconf())
		.append("\t主方阵型ID = ").append(battle.getHostform()).append("\t客方阵型ID = ").append(battle.getGuestform()).append("\t参战者：");
		for (xbean.Fighter fighter : battle.getFighters().values())
		{
			sb.append("(fighterid=" ).append(fighter.getBattleindex()).append(",Fightername=").append(fighter.getFightername())
			.append(",Uniqueid=").append(fighter.getUniqueid()).append(",Fightertype=").append(fighter.getFightertype());
			if(fighter.getInitattrs().containsKey(AttrType.WULI_CHUANTOU)&&fighter.getInitattrs().get(AttrType.WULI_CHUANTOU).intValue() > 486)
				sb.append("物理穿透 > 486" ).append(fighter.getBattleindex()).append("); ");
			else
				sb.append("); ");
		}
		BattleField.logger.info(sb.toString());		
	}

	/**
	 * setNpcInBattle: 如果是通过npc进战斗的，这里设置npc的战斗状态
	 * @param npckey
	*/
	private boolean setNpcInBattle(long npckey)
	{
		if(npckey < 0)
			return true;
		Npc npc = SceneNpcManager.getNpcByKey(npckey);
		if(npc == null)
			return false;
		if(npc.getNpcShare().getMulbattle() == 1)//可以同时进行多场战斗，不需要设置战斗状态
			return true;
		if (npc.getInBattle() != Npc.NOT_INBATTLE) // 如果已经在战斗中了,回滚
			return false;
		npc.setInBattle(Npc.INBATTLE);
		return true;
	}
	
	
	/****************************************设置战斗参数**************************************************/
	/**
	 * 判断本场战斗的类型是否是公会boss站
	 */
	public boolean isClanBossFight()
	{
		if(4000 < battleConfigId && battleConfigId < 4100)
			return true;
		return false;
	}
	
	/**
	 * 设置本场战斗的区域类型
	 */
	public void setAreaType(final int value){
		areatype = value;
	}
	/**
	 * 设置本场战斗的区域配置id
	 */
	public void setAreaConf(final int value){
		areaconf = value;
	}
	/**
	 * 设置本场战斗所涉及的怪物类型
	 * @param value
	 */
	public void setMonsterType(final int value){
		monstertype = value;
	}
	
	/**
	 * 设置与战斗挂钩的怪物npc的npckey
	 * @param value
	 */
	public void setMonsterNpckey( final long value ) {
		this.monsterNpckey = value;
	}
	
	/**
	 * 设置战斗类型pve pvp 切磋等
	 * @param battleType
	 */
	public void setBattleType(int battleType) {
		this.battleType = battleType;
	}
	
	/**
	 * 设置本场战斗是否可以逃跑
	 * @param canrunaway
	 */
	public void setCanRunaway(boolean canrunaway) {
		this.canrunaway = canrunaway;
	}
	
	public long getBattleId(){
		
		return battleID;
	}
	public boolean isBabyfight() {
		return babyfight;
	}
	public void setBabyfight(boolean babyfight) {
		this.babyfight = babyfight;
	}
	
	/**
	 * 获取YYLogger需要的信息
	 * @param entry
	 * @param isHost	主队还是客队
	 * @return
	 */
	public List<int[]> getMembersYYLogInfo(boolean isHost){
		List<int []> ret = new ArrayList<int[]>();
		if(battle == null) 
			return ret;
		java.util.Set<Long> setRoleIds = null;
		if(isHost){
			setRoleIds = BattleField.getHostRoleIDs(battle);
		}else {
			setRoleIds = BattleField.getGuestRoleIDs(battle);
		}
		if(setRoleIds == null)
			return ret;
		for (Long member : setRoleIds) {
			xbean.Properties prop = xtable.Properties.select(member);
			if(prop != null){
				ret.add(new int[]{prop.getSchool(), prop.getShape(), prop.getLevel()});
			}
		}
		return ret;
	}
	
	/**
	 * 运营日志
	 */
	private void writeLogs(long hostRoleId, long guestRoleId){
		long curTime = System.currentTimeMillis();
		if(battle == null) 
			return;
		
		if(hostRoleId > 0){
			Team hostTeam = TeamManager.selectTeamByRoleId(hostRoleId);
			if(hostTeam == null) return;
			
			xbean.TeamInfo hostteaminfo = hostTeam.getTeamInfo();
			if(hostteaminfo == null) return;
			
			fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance().getRoleByID(hostRoleId);
			if (role == null) return;

			long goTime = (curTime - hostteaminfo.getCreatetime()) / 1000;
			TeamOpBean hostBean = new TeamOpBean(hostTeam.getTeamId(), battle.getConfigid(), battle.getHostform(), 
					battle.getHostformlevel(), getMembersYYLogInfo(true), goTime, hostRoleId, role.getMapId(), battleID);
			YYLogger.teamLog(hostRoleId, hostBean);
		}
		
		if(guestRoleId > 0){
			Team guestTeam = TeamManager.selectTeamByRoleId(hostRoleId);
			if(guestTeam == null) return;
			
			xbean.TeamInfo guestteaminfo = guestTeam.getTeamInfo();
			if(guestteaminfo == null) return;
			
			fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance().getRoleByID(guestRoleId);
			if (role == null) return;
			
			long guestTeamGoTime = (curTime - guestteaminfo.getCreatetime()) / 1000;
			TeamOpBean guestBean = new TeamOpBean(guestTeam.getTeamId(), battle.getConfigid(), battle.getGuestform(), 
					battle.getGuestformlevel(), getMembersYYLogInfo(false), guestTeamGoTime, guestRoleId, role.getMapId(), battleID);
			YYLogger.teamLog(guestRoleId, guestBean);
		}
	}
}
