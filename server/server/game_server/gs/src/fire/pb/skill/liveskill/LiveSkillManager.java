package fire.pb.skill.liveskill;

import java.util.Arrays;

import org.apache.log4j.Logger;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.game.MoneyType;
import fire.pb.item.DSFoodAndDrugEffect;
import fire.pb.main.ConfigManager;
import fire.pb.skill.SkillRole;
import fire.pb.talk.MessageMgr;

/***
 * 生活技能管理
 * @author changhao
 *
 */
public class LiveSkillManager {
	public static final Logger logger = Logger.getLogger("LIVESKILL");
	
	public final static int ITEM_TYPE_DRUG_MAKINGS = 275; //炼药材料 by changhao
	
	public final static int ITEM_TYPE_DRUG = 291; //炼药 by changhao
	
	public final static int ITEM_TYPE_FORGE = 310; //锻造符 by changhao

	public final static int ITEM_TYPE_TAILOR = 326; //裁缝符 by changhao
	
	public final static int ITEM_TYPE_SMELT = 342; //炼金符 by changhao
	
	public final static int ITEM_TYPE_COOKING_FOOD = 290; //烹饪食品 by changhao
	
	public final static int ITEM_TYPE_PRACTICE_ROLE_ITEM = 566; //修炼人物道具 by changhao
	
	public final static int ITEM_TYPE_PRACTICE_PET_ITEM = 550; //修炼宠物道具 by changhao
	
	public final static int ITEM_TYPE_DRUG_JIN_CHUANG = 320100; //金疮药 by changhao
	
	public final static int ITEM_TYPE_DRUG_MAKINGS_LOW = 320150; //最低等级炼药材料 by changhao
	
	public final static int ITEM_TYPE_FRIEND_GIFT = 37573; //同心结 by changhao
	
	public final static int LIVE_SKILL_TYPE_MAKE_DRUG = 310101; //炼药技能
	
	public final static int LIVE_SKILL_TYPE_MAKE_FRIEND_GIFT = 340101; //同心结技能 by changhao
	
	public final static int LIVE_SKILL_TYPE_MAKE_FARM = 340001; //打工赚钱 by changhao
	
	public final static int LIVE_SKILL_TYPE_COOKING = 310201; //烹饪技能 by changhao
	
	public final static int LIVE_SKILL_TYPE_MAKE_FORGE = 300101; //锻造  by changhao
	
	public final static int LIVE_SKILL_TYPE_MAKE_TAILOR = 300201; //裁缝  by changhao
	
	public final static int LIVE_SKILL_TYPE_MAKE_SMELT = 300301; //炼金  by changhao

	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_HP = 1; //生命 by changhao
	
	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_PY_ATT = 2; //物攻 by changhao
	
	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_IMAGE_ATT = 3; //法攻 by changhao
	
	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_PHY_DEF = 4; //物防 by changhao
	
	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_IMAGE_DEF = 5; //法防 by changhao
	
	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_ANGER = 6; //愤怒 by changhao
	
	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_HEAL = 7;
	
	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_CONTROL = 8;
	
	public final static int LIVE_SKILL_ENHANCEMENT_TYPE_SPEED = 9;
	
	public final static int PARTICLE_SKILL_ROLE_START = 360011; //人物修炼技能起始到+4 by changhao
	public final static int PARTICLE_SKILL_PET_START = 360020; //宠物修炼技能起始到+4 by changhao
	
	public final static int PARTICLE_SKILL_NUM = 4; //角色宠物技能各4个 by changhao
	
	public final static int LIVE_SKILL_WARRIOR_ID = 1117; //战士 by changhao
	public final static int LIVE_SKILL_MAGIC_ID = 1517; // 法师 by changhao
	public final static int LIVE_SKILL_PRIEST_ID = 1617; //牧师 by changhao
	public final static int LIVE_SKILL_DRUID_ID = 1417; //德鲁伊 by changhao
	public final static int LIVE_SKILL_HUNTER_ID = 1317; //猎人 by changhao
	public final static int LIVE_SKILL_PALADIN_ID = 1217; //圣骑士 by changhao
	public final static int LIVE_SKILL_SAMAN_ID = 1717; //萨满 by changhao
	public final static int LIVE_SKILL_ROUGE_ID = 1817; //盗贼 by changhao
	public final static int LIVE_SKILL_WARLOCK_ID = 1917; //术士 by changhao
	
	public final static String CookingString = "烹饪"; //烹饪 by changhao
	public final static String MakeFriendGift = "同心结"; // 同心结 by changaho
	public final static String MakeStuff = "打造符"; // 打造符 by changaho
	public final static String MakeDrug = "炼药"; //炼药 by changhao
	public final static String MakeFarm = "打工"; // 打工赚钱 by changhao
	public final static String Practice = "修炼"; //修炼 by changhao
	public final static String LiveSkill = "生活技能"; //生活技能  by changhao
	public final static String Roll ="Roll点"; //ROLL点产生
	public final static String Enhancement = "附魔"; //by changhao
	public final static String StudyLiveSkill = "学习生活技能";
	public final static String ChargetGrab = "充值领取";
	public final static String DrugStuff = "fire.pb.item.drug.GeneralDrugStuff"; //by changhao
	
	public  int drugtotalweight; //药品所有的权重不包括 金疮药 by changhao
	
	//所有药品 by changhao
	public java.util.List<fire.pb.item.SFoodAndDrugEffect> druglist = new java.util.ArrayList<fire.pb.item.SFoodAndDrugEffect>();
	
	public int cookingfoodtotalweight; //烹饪食物所有的权重 by changhao
	
	//所有的烹饪食物 by changhao
	public java.util.List<fire.pb.item.SFoodAndDrugEffect> cookingfoodlist = new java.util.ArrayList<fire.pb.item.SFoodAndDrugEffect>();
	
	public java.util.Map<Integer, Integer> practiceskillidlist = new java.util.HashMap<Integer, Integer>(); //技能表ID和修炼技能ID对应 by changhao
	
	private static LiveSkillManager instance;	
	private LiveSkillManager()
	{
		
	}
	
	public static LiveSkillManager getInstance()
	{
		if(null == instance)
		{
			instance = new LiveSkillManager();
		}
		
		return instance;
	}
	
	public void Init()
	{
		//计算出所有药品和烹饪权重 by changhao
		drugtotalweight = 0;
		cookingfoodtotalweight = 0;
		cookingfoodlist.clear();
		java.util.Map<Integer, fire.pb.item.SFoodAndDrugEffect> config = null;
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) {
			java.util.Map<Integer, DSFoodAndDrugEffect> dSFoodAndDrugEffect = ConfigManager.getInstance().getConf(fire.pb.item.DSFoodAndDrugEffect.class);
			config = new java.util.TreeMap<>(dSFoodAndDrugEffect);
		} else {
			config = ConfigManager.getInstance().getConf(fire.pb.item.SFoodAndDrugEffect.class);
		}
		
		for (fire.pb.item.SFoodAndDrugEffect food : config.values())
		{
			//不包括金疮药 by changhao
			if (food.getTypeid() == ITEM_TYPE_DRUG && food.getId() != ITEM_TYPE_DRUG_JIN_CHUANG)
			{
				drugtotalweight += food.lianyaoWeight;
				druglist.add(food);
			}
			else if (food.getTypeid() == ITEM_TYPE_COOKING_FOOD)
			{
				cookingfoodtotalweight += food.getPengrenWeight();
				cookingfoodlist.add(food);
			}
		}
		
		practiceskillidlist.clear();
		java.util.Map<Integer, fire.pb.skill.SLifeSkill> config1 = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class);
		
		for (java.util.Map.Entry<Integer, fire.pb.skill.SLifeSkill> e1 : config1.entrySet())
		{
			if (e1.getValue().skillType == 3)
			{
				practiceskillidlist.put(e1.getValue().skillId, e1.getKey());				
			}
		}
	}
	
	/***
	 * 通过等级得到可以烹饪食物的总权重 by changhao
	 * @return
	 */
	public int GetCookingWeightByLevel(int skilllevel)
	{
		int totalweight = 0;
		for (fire.pb.item.SFoodAndDrugEffect e : LiveSkillManager.getInstance().cookingfoodlist)
		{
			if (skilllevel >= e.getNeedPengrenLevel()) //可以烹饪的食物 by changhao
			{
				totalweight += e.getPengrenWeight();
			}
		}		
		
		return totalweight;
	}
	
	/***
	 * 通过增加的活跃度值计算当前活力值 (1点活跃度=（0.075*玩家等级+2.5） * 1.2F 活力by changhao
	 * @param activeness
	 * @return
	 */
	public int CalcEnergyByActiveNess(int energy, int rolelevel, float activeness)
	{
		int curenergy = (int)((float)activeness * (0.075F * (float)rolelevel + 2.5F) * 1.2F) + energy;
		return Math.min(curenergy, CalcEnergyMaxLimit(rolelevel));
	}
	
	/***
	 * 活力上限公式　＝　 500 + 等级 * 20
	 * @param rolelevel
	 * @return
	 */
	public int CalcEnergyMaxLimit(int rolelevel)
	{
		return 500 + rolelevel * 20;
	}
	
	/***
	 * 扣除制造消耗的活力 by changhao
	 */
	public boolean ConsumeMakeEnergy(long roleid, int skillid, int skilllevel,String reason, YYLoggerTuJingEnum way, boolean showMsg)
	{
		xbean.Properties prop = xtable.Properties.get(roleid);	
		
		int energy = prop.getEnergy();
		fire.pb.skill.SLifeSkillCost cost = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkillCost.class).get(skilllevel);
		if (cost == null)
		{
			return false;					
		}
		
		fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(skillid);
		
		if (config == null)
		{
			return false;						
		}
		
		if (config.strengthCostRule > 0)
		{
			int needenergy = cost.strengthCostList.get(config.strengthCostRule - 1); //需要的活力 by changhao
			if (energy < needenergy) //活力不够 by changhao
			{
				return false;						
			}
			
			AddEnergy(roleid, prop, -needenergy, way);
			if(showMsg){
				//提示消耗
				MessageMgr.psendMsgNotifyWhileCommit(roleid, 160137,Arrays.asList(needenergy+""));
			}
			logger.info("玩家id\t"+roleid+reason+"消耗活力"+needenergy);
		}
		
		return true;
	}
	
	/**
	 * 返回制造消耗的活力数量(如果返回-1，说明数据错误)
	 * @param skillid
	 * @param skilllevel
	 * @return
	 */
	public int getEneryMakeCost(int skillid, int skilllevel){
		fire.pb.skill.SLifeSkillCost cost = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkillCost.class).get(skilllevel);
		if (cost == null)
		{
			return -1;					
		}
		fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(skillid);
		
		if (config == null)
		{
			return -1;					
		}
		int needenergy = cost.strengthCostList.get(config.strengthCostRule - 1); //需要的活力 by changhao
		return needenergy;
	}
	
	
	
	/***
	 * 增加减少活力 by changhao
	 * @param roleid
	 * @param prop
	 * @param add
	 */
	public void AddEnergy(long roleid, xbean.Properties prop, int add, YYLoggerTuJingEnum way)
	{
		int energy = prop.getEnergy();
		int v = Math.max(energy + add, 0);
		int e = Math.min(CalcEnergyMaxLimit(prop.getLevel()), v);
		prop.setEnergy(e);
		
		fire.pb.attr.SRefreshRoleData refreshdata = new fire.pb.attr.SRefreshRoleData();
		refreshdata.datas.put( fire.pb.attr.AttrType.ENERGY, (float)prop.getEnergy() );
		
		fire.log.YYLogger.OpTokenUseLog(roleid, way, MoneyType.MoneyType_Energy,
				add, e, new fire.log.beans.ItemBean());
		
		mkdb.Procedure.psendWhileCommit(roleid, refreshdata);	
	}
	
	/***
	 * 通过制作那种打造符得到对应的生活技能 by changhao
	 * @param config
	 * @return
	 */
	public int GetMakeStuffSkillIdByItemId(fire.pb.item.GroceryItemShuXing config)
	{
		int skillid = 0;
		if (config.getTypeid() == LiveSkillManager.ITEM_TYPE_FORGE)
		{
			skillid = LiveSkillManager.LIVE_SKILL_TYPE_MAKE_FORGE;
		}
		else if (config.getTypeid() == LiveSkillManager.ITEM_TYPE_TAILOR)
		{
			skillid = LiveSkillManager.LIVE_SKILL_TYPE_MAKE_TAILOR;					
		}
		else if (config.getTypeid() == LiveSkillManager.ITEM_TYPE_SMELT)
		{
			skillid = LiveSkillManager.LIVE_SKILL_TYPE_MAKE_SMELT;
		}
		else
		{
			return 0;
		}
		
		return skillid;
	}
	
	/***
	 * 取得职业技能等级（对应附魔） by changhao
	 * @param roleid
	 * @param school
	 * @return
	 */
	public int GetEnhanceLevelBySchool(long roleid, int school)
	{
		int id = 0;
		if (school == 11)
		{
			id = LiveSkillManager.LIVE_SKILL_WARRIOR_ID;
		}
		else if (school == 12)
		{
			id = LiveSkillManager.LIVE_SKILL_PALADIN_ID;
		}
		else if (school == 13)
		{
			id = LiveSkillManager.LIVE_SKILL_HUNTER_ID;		
		}
		else if (school == 14)
		{
			id = LiveSkillManager.LIVE_SKILL_DRUID_ID;	
		}
		else if (school == 15)
		{
			id = LiveSkillManager.LIVE_SKILL_MAGIC_ID;	
		}
		else if (school == 16)
		{
			id = LiveSkillManager.LIVE_SKILL_PRIEST_ID;	
		}
		else if (school == 17)
		{
			id = LiveSkillManager.LIVE_SKILL_SAMAN_ID;	
		}
		else if (school == 18)
		{
			id = LiveSkillManager.LIVE_SKILL_ROUGE_ID;	
		}
		else if (school == 19)
		{
			id = LiveSkillManager.LIVE_SKILL_WARLOCK_ID;	
		}
		
		SkillRole skillrole = new SkillRole(roleid, true);
		return skillrole.getLevel(id);
	}
	
	/**
	 * 通过打造符ID,得到需要的等级 by changhao
	 * @param config
	 * @return
	 */
	public int GetMakeStuffNeedLevelByItemId(fire.pb.item.GroceryItemShuXing config)
	{
		if (config.getTypeid() == LiveSkillManager.ITEM_TYPE_FORGE)
		{
			return config.getNeedForgeLevel();
		}
		else if (config.getTypeid() == LiveSkillManager.ITEM_TYPE_TAILOR)
		{
			return config.getNeedTailorLevel();				
		}
		else if (config.getTypeid() == LiveSkillManager.ITEM_TYPE_SMELT)
		{
			return config.getNeedSmeltLevel();
		}
		else
		{
			return 999;
		}		
	}
	
	/***
	 * 通过职业找到对应的生活技能
	 * @param school
	 * @return
	 */
	public int GetEnhanceLiveSkillBySchool(int school)
	{
		int id = 0;
		if (school == 11)
		{
			id = LiveSkillManager.LIVE_SKILL_WARRIOR_ID;
		}
		else if (school == 12)
		{
			id = LiveSkillManager.LIVE_SKILL_PALADIN_ID;
		}
		else if (school == 13)
		{
			id = LiveSkillManager.LIVE_SKILL_HUNTER_ID;		
		}
		else if (school == 14)
		{
			id = LiveSkillManager.LIVE_SKILL_DRUID_ID;	
		}
		else if (school == 15)
		{
			id = LiveSkillManager.LIVE_SKILL_MAGIC_ID;	
		}
		else if (school == 16)
		{
			id = LiveSkillManager.LIVE_SKILL_PRIEST_ID;	
		}
		else if (school == 17)
		{
			id = LiveSkillManager.LIVE_SKILL_SAMAN_ID;	
		}
		else if (school == 18)
		{
			id = LiveSkillManager.LIVE_SKILL_ROUGE_ID;	
		}
		else if (school == 19)
		{
			id = LiveSkillManager.LIVE_SKILL_WARLOCK_ID;	
		}
		
		fire.pb.skill.SInbornInfo info = ConfigManager.getInstance().getConf(fire.pb.skill.SInbornInfo.class).get(id);		
		int liveskillid = info.pointToSkillList.get(1);
		
		return liveskillid;
	}
	
	/***
	 * 通过职业ID得到对应的附魔道具ID by changhao
	 * @param school
	 * @return
	 */
	public int GetEnhanceItemIdBySchool(int school)
	{
		int liveskillid = GetEnhanceLiveSkillBySchool(school);
		
		int itemid = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(liveskillid).enhanceitemid;
		
		return itemid;
	}
	
	/***
	 * 得到当前修炼技能最大等级 1角色等级不足2公会等级不足3是帮贡不足4最大等级by changhao
	 * @param skillid
	 * @return
	 */
	public java.util.Map<Integer, Integer> GetPracticeSkillMaxLevel(int rolelevel, int factionlevel, int contribute, int maxskilllevel)
	{	
		java.util.Map<Integer, Integer> ret = new java.util.HashMap<Integer, Integer>();
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
		{
			java.util.Map<Integer, fire.pb.skill.SPointCardParticeSkillLevelup> config = ConfigManager.getInstance().getConf(fire.pb.skill.SPointCardParticeSkillLevelup.class);
			int maxlevel = 0;
			for (fire.pb.skill.SPointCardParticeSkillLevelup e : config.values())
			{
				if (rolelevel < e.getPlayerlevel())
				{
					ret.put(1, maxlevel);
					return ret;
				}
				
				if (factionlevel < e.getFactionlevel())
				{
					ret.put(2, maxlevel);
					return ret;
				}
				
				if (contribute < e.getMaxcon())
				{
					ret.put(3, maxlevel);
					return ret;
				}
				
				maxlevel ++;
			}
			
			if (maxlevel > maxskilllevel)
			{
				maxlevel = maxskilllevel;
				ret.put(4, maxlevel);
			}
		}
		else
		{
			java.util.Map<Integer, fire.pb.skill.SParticeSkillLevelup> config = ConfigManager.getInstance().getConf(fire.pb.skill.SParticeSkillLevelup.class);
			int maxlevel = 0;
			for (fire.pb.skill.SParticeSkillLevelup e : config.values())
			{
				if (rolelevel < e.getPlayerlevel())
				{
					ret.put(1, maxlevel);
					return ret;
				}
				
				if (factionlevel < e.getFactionlevel())
				{
					ret.put(2, maxlevel);
					return ret;
				}
				
				if (contribute < e.getMaxcon())
				{
					ret.put(3, maxlevel);
					return ret;
				}
				
				maxlevel ++;
			}
			
			if (maxlevel > maxskilllevel)
			{
				maxlevel = maxskilllevel;
				ret.put(4, maxlevel);
			}
		}
		return ret;
	}
	
	/***
	 * 得到这个修炼的技能是宠物的还是角色的，0是错误1是宠物的2是角色的 by changhao
	 * @param skillid
	 * @return
	 */
	public int GetPracticleSkillPlayerOrPet(int skillid)
	{
		if (skillid >= PARTICLE_SKILL_ROLE_START && skillid < PARTICLE_SKILL_ROLE_START + 4)
		{
			return 2;
		}
		
		if (skillid >= PARTICLE_SKILL_PET_START && skillid < PARTICLE_SKILL_PET_START + 4)
		{
			return 1;
		}		

		return 0;
	}
	
	/***
	 * 得到修炼技能升这级需要的经验 by changhao
	 * @param skillid
	 * @param level
	 * @return
	 */
	public int GetPracticleSkillNeedExp(int skillid, int level)
	{
		int exp = 0;
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
		{
			fire.pb.skill.SPointCardParticeSkillLevelup test = ConfigManager.getInstance().getConf(fire.pb.skill.SPointCardParticeSkillLevelup.class).get(level + 1);
			if (test == null)
			{
				return -1;
			}
			
			fire.pb.skill.SPointCardParticeSkillLevelup config = ConfigManager.getInstance().getConf(fire.pb.skill.SPointCardParticeSkillLevelup.class).get(level);
			if (config == null) //超过最大等级了 by changhao
			{
				return -1;
			}
			
			int type = GetPracticleSkillPlayerOrPet(skillid);
			int index = 0;
			if (type == 2)
			{
				index = skillid - PARTICLE_SKILL_ROLE_START;
			}
			else if (type == 1)
			{
				index = PARTICLE_SKILL_NUM + skillid - PARTICLE_SKILL_PET_START;
			}
			exp = config.vecskillexp.get(index);
		}
		else
		{
			fire.pb.skill.SParticeSkillLevelup test = ConfigManager.getInstance().getConf(fire.pb.skill.SParticeSkillLevelup.class).get(level + 1);
			if (test == null)
			{
				return -1;
			}
			
			fire.pb.skill.SParticeSkillLevelup config = ConfigManager.getInstance().getConf(fire.pb.skill.SParticeSkillLevelup.class).get(level);
			if (config == null) //超过最大等级了 by changhao
			{
				return -1;
			}
			
			int type = GetPracticleSkillPlayerOrPet(skillid);
			int index = 0;
			if (type == 2)
			{
				index = skillid - PARTICLE_SKILL_ROLE_START;
			}
			else if (type == 1)
			{
				index = PARTICLE_SKILL_NUM + skillid - PARTICLE_SKILL_PET_START;
			}			
			exp = config.vecskillexp.get(index);
		}
		return exp;
	}
	
	/***
	 * 得到修炼技能对应的战斗被动技能 by changhao
	 * @param playerorpet
	 * @return
	 */
	public int GetPracticeSkillBattleSkill(int skillid)
	{
		fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(skillid);
		if (config != null)
		{
			return config.skillId;
		}
		
		return 0;
	}
	
	/***
	 * 得到修炼技能里被动技能对应修炼技能ID by changhao
	 * @param passedskillid
	 * @return
	 */
	public int GetPracticePassivenessSkillLevelBattleSkill(int passedskillid)
	{
		Integer id = practiceskillidlist.get(passedskillid);
		if (id != null)
		{
			return id;
		}
		
		return 0;
	}
	

	
	/***
	 * 返回所有的修炼技能对应的被动技能ids who == 1是宠物 2是人物 by changhao
	 * @param roleskill
	 * @return
	 */
	public java.util.List<Integer> GetAllPracticeSkillBattleSkill(SkillRole roleskill, int who)
	{
		java.util.List<Integer> ids = new java.util.ArrayList<Integer>();
		
		for (java.util.Map.Entry<Integer, xbean.PracticeSkill> e : roleskill.getPracticeSkills().entrySet())
		{
			int skillid = e.getKey();
			if (e.getValue().getLevel() > 0)
			{
				int which = LiveSkillManager.getInstance().GetPracticleSkillPlayerOrPet(skillid);
				//得到修炼技能对应的被动技能id by changhao
				if (which == who) //给人物的修炼技能 by changhao
				{
					int passiveskillid = LiveSkillManager.getInstance().GetPracticeSkillBattleSkill(skillid);
					if (passiveskillid != 0)
					{
						ids.add(passiveskillid);
					}				
				}				
			}
		}		
		
		return ids;
	}
	
	/***
	 * 生活技能相关需要的活力 by changhao
	 * @param roleId
	 * @param skillid
	 * @param skilllevel
	 * @return
	 */
	public int NeedEnergy(int skillid, int skilllevel)
	{
		fire.pb.skill.SLifeSkillCost cost = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkillCost.class).get(skilllevel);
		if (cost == null)
		{
			return 999999;					
		}
		
		fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(skillid);
		
		if (config == null)
		{
			return 999999;						
		}
		
		if (config.strengthCostRule > 0)
		{
			int needenergy = cost.strengthCostList.get(config.strengthCostRule - 1); //需要的活力 by changhao
			return needenergy;
		}
		
		return 999999;		
	}
}
