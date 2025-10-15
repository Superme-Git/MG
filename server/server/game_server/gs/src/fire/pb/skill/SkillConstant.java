package fire.pb.skill;

import java.util.HashMap;
import java.util.Map;

public class SkillConstant
{
	//技能大类别
	public static final int MAX_INBORN_SKILL_COUNT = 8;//人物最大职业技能
	//技能大类别	
	
	public static final int TYPE_ROLE_SCHOOL_SKILL = 1;//人物职业技能
	public static final int TYPE_PET_BATTLE_SKILL = 2;//宠物战斗技能
	public static final int TYPE_ROLE_ASSIST_SKILL = 3;//人物辅助技能
	public static final int TYPE_ROLE_SPECIAL_SKILL = 4;//人物特技	
	public static final int TYPE_ROLE_PRACTICE_SKILL = 9;//人物修炼技能
//	public static final int TYPE_ROLE_OTHER_SKILL = 10;//人物其他技能
	
	public static final int BATTLE_ROLE_SUMMON_TOTEM_HEAL = 161002;//治疗图腾（召唤）
	public static final int BATTLE_ROLE_SUMMON_TOTEM_CRAZE = 161005;//狂热图腾（召唤）
	
	public static final int BATTLE_ROLE_BEHUNTER = 130000;//猎人狩猎姿态技能
	public static final int BATTLE_ROLE_HUNTER_1 = 130001;//多重打击主动技能
	public static final int BATTLE_ROLE_HUNTER_2 = 130002;//穿刺打击主动技能
	public static final int BATTLE_ROLE_HUNTER_3 = 130005;//精准打击主动技能

	public static final int BATTLE_HUOBAN_BEHUNTER_1 = 731000;//猎人狩猎姿态技能
	public static final int BATTLE_HUOBAN_HUNTER_1_1 = 731001;//多重打击主动技能
	public static final int BATTLE_HUOBAN_HUNTER_2_1 = 731002;//穿刺打击主动技能
	public static final int BATTLE_HUOBAN_HUNTER_3_1 = 731005;//精准打击主动技能

	public static final int BATTLE_HUOBAN_BEHUNTER_2 = 732000;//猎人狩猎姿态技能
	public static final int BATTLE_HUOBAN_HUNTER_1_2 = 732001;//多重打击主动技能
	public static final int BATTLE_HUOBAN_HUNTER_2_2 = 732002;//穿刺打击主动技能
	public static final int BATTLE_HUOBAN_HUNTER_3_2 = 732005;//精准打击主动技能

	public static final int BATTLE_HUOBAN_BEHUNTER_3 = 733000;//猎人狩猎姿态技能
	public static final int BATTLE_HUOBAN_HUNTER_1_3 = 733001;//多重打击主动技能
	public static final int BATTLE_HUOBAN_HUNTER_2_3 = 733002;//诱捕打击主动技能
	public static final int BATTLE_HUOBAN_HUNTER_3_3 = 733005;//稳固打击主动技能	

	public static final int BATTLE_HUOBAN_BEHUNTER_4 = 191000;//猎人狩猎姿态技能
	public static final int BATTLE_HUOBAN_HUNTER_1_4 = 191001;//多重打击主动技能
	public static final int BATTLE_HUOBAN_HUNTER_2_4 = 191002;//诱捕打击主动技能
	public static final int BATTLE_HUOBAN_HUNTER_3_4 = 191005;//稳固打击主动技能	
	
	public static final int BATTLE_HUMAN_WARRIOR_3_1 = 110003; // 毁灭打击
	public static final int BATTLE_HUOBAN_WARRIOR_3_1 = 711003; // 毁灭打击
	public static final int BATTLE_HUOBAN_WARRIOR_3_2 = 712003; // 毁灭打击
	public static final int BATTLE_HUOBAN_WARRIOR_3_3 = 713003; // 毁灭打击
	public static final int BATTLE_HUOBAN_WARRIOR_3_4 = 714003; // 毁灭打击
	

	//宠物技能
	public static final int BATTLE_PET_HIDDEN_FIRST = 207017;//潜行
	public static final int BATTLE_PET_HIDDEN_SECOND = 207018;//高级潜行	
	
//==============要特殊处理的技能=================
	//辅助技能
	public static final int ASSIST_SKILL_MAKE_WEAPON = 300101;//武器锻造
	public static final int ASSIST_SKILL_MAKE_ASSESSORY = 300201;//饰品锻造
	public static final int ASSIST_SKILL_MAKE_ARMOR = 300301;//防具锻造
	public static final int ASSIST_SKILL_MAKE_MEDICINE = 310101;//炼药
	public static final int ASSIST_SKILL_COOK_MANTOU = 310202;//做馒头
	public static final int ASSIST_SKILL_MAKE_FOOD = 310201;//烹饪
	public static final int ASSIST_SKILL_JIA_JU = 310103;//巧匠之术
	public static final int ASSIST_SKILL_JIAN_SHEN = 320101;//健身术
	public static final int ASSIST_SKILL_TU_NA = 320102;//吐纳术
	public static final int ASSIST_SKILL_QIANG_TI = 320103;//强体术
	public static final int ASSIST_SKILL_YANG_SHENG = 320104;//养生术
	public static final int ASSIST_SKILL_AN_QI = 330101;//暗器
	public static final int ASSIST_SKILL_ZHUI_BU = 330102;//追捕
	public static final int ASSIST_SKILL_TAO_PAO = 330103;//逃跑
	public static final int ASSIST_SKILL_JING_LIAN = 330104;//药材精炼
	public static final int ASSIST_SKILL_JUMP = 350001;//轻功
	//技能类型
	public static final int BATTLE_SKILL_TYPE_OTHERS = 0;//其他
	public static final int BATTLE_SKILL_TYPE_SINGLE_PHY = 1;//单体物理攻击
	public static final int BATTLE_SKILL_TYPE_MULTI_PHY = 2;//多体物理攻击
	public static final int BATTLE_SKILL_TYPE_SINGLE_MAGIC = 3;//单体法术攻击
	public static final int BATTLE_SKILL_TYPE_MULTI_MAGIC = 4;//多体法术攻击
	public static final int BATTLE_SKILL_TYPE_SINGLE_HEAL = 5;//单体治疗法术
	public static final int BATTLE_SKILL_TYPE_MULTI_HEAL = 6;//多体治疗法术
	public static final int BATTLE_SKILL_TYPE_HELPFUL = 7;//辅助类技能
	public static final int BATTLE_SKILL_TYPE_CONTROL = 8;//控制类技能
	public static final int BATTLE_SKILL_TYPE_CTRL_ATTACK = 9;//强制攻击
/*	public static final int BATTLE_SKILL_TYPE_PASSIVE_BATTLE = 10;//进战斗加载的被动技能
	public static final int BATTLE_SKILL_TYPE_PASSIVE_ONLINE = 11;//上线加载的被动技能*/
	public static final int BATTLE_SKILL_TYPE_PASSIVE = 10;//被动技能
	public static final int BATTLE_SKILL_TYPE_SPECIAl = 11;//装备附魔技能
	public static final int BATTLE_SKILL_GO_TO_SCHOOL = 12;//回新手村技能
	public static final int BATTLE_SKILL_TYPE_HALO = 13;//光环技能 全体
	public static final int BATTLE_SKILL_TYPE_PASSIVE_TRIGGER = 14;//被动触发技能,如死亡时候起效等。
	public static final int BATTLE_SKILL_TYPE_SUMMON= 15;//召唤技能
	public static final int BATTLE_SKILL_TYPE_HALO2 = 16;//光环技能 九宫格
	
	//选择目标方aimCampType
	public final static int TARGET_AIM_CAMPTYPE_FRIEND = 1; // 己方
 	public final static int TARGET_AIM_CAMPTYPE_ENEMY = 2;//敌方
	public final static int TARGET_AIM_CAMPTYPE_SELF = 3;//自己
	public final static int TARGET_AIM_CAMPTYPE_ALL = 4;//所有人	
	public final static int TARGET_AIM_CAMPTYPE_ALLFRIENDBUTSELF = 5;//己方过滤掉自己
	public final static int TARGET_AIM_CAMPTYPE_SELFEMPTY = 6;//己方空位
	public final static int TARGET_AIM_CAMPTYPE_ENEMYEMPTY = 7;//敌方空位
/*	public final static int TARGET_AIM_SELFAROUND = 6;//自己周围9宫格	
	public final static int TARGET_AIM_AIMAROUND = 7;//目标周围9宫格	
*/	
	
	//索敌类型
	public final static int TARGET_AIM_CONFIRM_NORMAL = 1;	// 常规索敌(单体则依赖于客户端给予的目标选择,如果目标死亡或隐身,随机选择目标；
															//多体则依赖于客户端给予的目标,多余的目标,根据排序变量,挑选符合的目标,多体目标死亡或隐身,则随机选择主目标,其他目标根据排序变量,挑选符合的目标。）
 	public final static int TARGET_AIM_CONFIRM_ASLAST = 2;	//跟上个技能目标相同
	public final static int TARGET_AIM_CONFIRM_ASFIRST = 3;	//跟首个技能目标相同（首个目标
	public final static int TARGET_AIM_CONFIRM_DIFFLAST = 4;//跟上个技能目标不同	
	public final static int TARGET_AIM_CONFIRM_DIFFALL = 5;	//跟之前所有技能的目标不同（这个时候排序变量也是有用的,如果我此时填speed,则选择过滤掉之前子技能目标后,选择速度最高的目标作为打击目标）
	public final static int TARGET_AIM_CONFIRM_NORMAL2 = 6; // 常规索敌2(则依赖于客户端给予的目标选择,如果目标死亡或隐身,则释放失败）
	public final static int TARGET_AIM_CONFIRM_NORMAL3 = 7; // 常规索敌3(单体则依赖于客户端给予的目标选择,如果目标死亡,随机选择目标；
	public final static int TARGET_AIM_CONFIRM_NORMAL4 = 8; // 常规索敌4(则依赖于客户端给予的目标选择,如果目标死亡,则释放失败） 
	public final static int TARGET_AIM_CONFIRM_NORMAL5 = 9; // 死亡才能索敌成功,敌方索隐身目标换目标
	public final static int TARGET_AIM_CONFIRM_NORMAL6 = 10; // 死亡才能索敌成功,敌方索隐身目标失败 
	public final static int TARGET_AIM_CONFIRM_NORMAL7 = 11; // 图腾索敌方式,自己周围9宫格
	public final static int TARGET_AIM_CONFIRM_NORMAL8 = 12; // 上上个目标锁敌人 by changhao 
	
	//技能类型ActionCondition
	public static final int USBSKILL_ACTION_CONDITION_OTHERS = 0;	//其他
	public static final int USBSKILL_ACT_CONDITION_HIT_ALIVE = 1;	//关联技能索敌成功,命中,受击方未全部死亡
	public static final int USBSKILL_ACT_CONDITION_HIT_ALLDIE = 2;	//关联技能索敌成功,命中,受击方全部死亡
	public static final int USBSKILL_ACT_CONDITION_MISS = 3;		//关联技能索敌成功,未命中
	public static final int USBSKILL_ACT_CONDITION_NOTFIND = 4;		//关联技能索敌失败

	//乾坤箱对应的npcId
	public final static int QIANKUN_SKILL_ID = 1000001;
	public final static int QIANKUN_1_NPC_ID = 10093;
	public final static int QIANKUN_2_NPC_ID = 10094;
	public final static int QIANKUN_3_NPC_ID = 10095;
	public final static int QIANKUN_4_NPC_ID = 10096;
	public final static int QIANKUN_5_NPC_ID = 10097;
	
	//基本技能类型
	public static final Map<Integer,BasicSkillBelongs> basicSkillUsers = new HashMap<Integer,BasicSkillBelongs>();
	public static final int BATTLE_SKILL_BASIC_ATTACK = 100001;//普通攻击
	public static final int BATTLE_SKILL_FAST_ATTACK = 100002;//远程物理攻击	
	public static final int BATTLE_SKILL_BASIC_CTRL_ATTACK = 100003;//强制攻击
	public static final int BATTLE_SKILL_BASIC_CHAOS_ATTACK = 100004;//混乱攻击
	public static final int BATTLE_SKILL_BASIC_USE_ITEM = 100005;//使用物品
	
	//宠物技能
	public static final int PET_SKILL_NULL = 200534;
	static
	{
		basicSkillUsers.put(BATTLE_SKILL_BASIC_ATTACK, new BasicSkillBelongs(true, true, true));
		basicSkillUsers.put(BATTLE_SKILL_BASIC_CTRL_ATTACK, new BasicSkillBelongs(true, true, true));
		basicSkillUsers.put(BATTLE_SKILL_BASIC_CHAOS_ATTACK, new BasicSkillBelongs(true, true, true));
		basicSkillUsers.put(BATTLE_SKILL_BASIC_USE_ITEM, new BasicSkillBelongs(true, true, false));
	}
	
	public static class BasicSkillBelongs
	{
		final private boolean belongToRole;
		final private boolean belongToPet;
		final private boolean belongToMonster; 
		

		BasicSkillBelongs(boolean belongToRole, boolean belongToPet, boolean belongToMonster)
		{
			this.belongToRole = belongToRole; 
			this.belongToPet = belongToPet; 
			this.belongToMonster = belongToMonster; 
		}
		
		public boolean isBelongToRole()
		{
			return belongToRole;
		}

		public boolean isBelongToPet()
		{
			return belongToPet;
		}

		public boolean isBelongToMonster()
		{
			return belongToMonster;
		}

	}
	
	//==============要特殊处理的技能=================
	public static final int BATTLE_PET_JOY_SORROWS_SKILL 		= 999999;//203121; //同甘共苦技能
	public static final int BATTLE_HUOBAN_FULL_POWER_SKILL 		= 999999;//712006; //血脉愤张技能	--充满能量
	public static final int BATTLE_HUOBAN_SCARE_HOWL_SKILL 		= 999999;//713004; //破胆怒吼--吓唬人的吼叫
	public static final int BATTLE_HUOBAN_FIND_WEAK_SKILL 		= 999999;//714005; //洞悉弱点--找到弱点
	public static final int BATTLE_HUOBAN_PEEL_ARMOUR_SKILL 	= 999999;//714004; //卸甲--护甲剥离
	public static final int BATTLE_HUOBAN_CRUSADES_HEART_SKILL 	= 999999;//754005; //十字军之心	
 	public static final int BATTLE_HUOBAN_TETANUS_SKILL 		= 999999;//感染伤口-破伤风
	public static final int BATTLE_HUOBAN_ICE_DEFEND_SKILL 		= 999999;//寒冰护体--
	
	public static final int BATTLE_HUOBAN_REVENGE_SKILL 		= 712008;//复仇 --阿男意志
 	public static final int BATTLE_HUOBAN_MYSTERY_FORBID_SKILL 	= 754006;//奥术禁锢 --王者壁垒
	public static final int BATTLE_HUOBAN_ELEMENT_AWKEN_SKILL 	= 773007;//元素唤醒--强力母牛
 	public static final int BATTLE_HUOBAN_PANDORA_BOX_SKILL 	= 764007;//超度邪恶--潘多拉盒子 --圣赞美诗
	public static final int BATTLE_HUOBAN_DIE_FORBID_SKILL 		= 742007;//野兽禁锢 --孀妇哀怨
	public static final int BATTLE_HUOBAN_FERITY_INSTINCT_SKILL = 741007;//野性本能--持久力强
	public static final int BATTLE_HUOBAN_HADES_FORBID_SKILL 	= 792008;//魔魂禁锢 --邪恶领袖
	public static final int BATTLE_EQUIP_LESS_SP_SKILL 			= 430025;//愤怒
	public static final int BATTLE_EQUIP_MORE_SP_SKILL 			= 430026;//暴怒
	public final static int SPECIAL_SKILL_SHEN_NONG 			= 430028;//药性专家
	public final static int SPECIAL_SKILL_HOT_CURE 				= 430029;//回春
	public static final int BATTLE_EQUIP_COOL_HEART_SKILL 		= 430030;//专注
	public static final int BATTLE_EQUIP_DEEP_HURT_SKILL 		= 430031;//斩杀
	public final static int SPECIAL_SKILL_ESCAPE_EXPERT			= 430032;//	迷踪
	public final static int SPECIAL_SKILL_HUNTER_EXPERT			= 430033;//	狩猎
	public final static int SPECIAL_SKILL_LESS_LV_LIMIT			= 430034;//	简易
	public final static int SPECIAL_SKILL_NO_LV_LIMIT 			= 430035;//	无等级限制
	public final static int SPECIAL_SKILL_GOD_EYE 				= 430036;//	洞察
	
	
	/*public static final int BATTLE_HUOBAN_B52_SKILL = 715005; //狂轰乱炸--B52轰炸机*/
 	/**/
	public static boolean isdroopBoomingSkill(int skillId){
		if(skillId >=203101 && skillId <= 203120){
			return true;
		}
		return false;
	}
	
	public class SkillError {
		public final static int WrongSchool = 1; // 该技能不属于你的职业
		public final static int MainSBCantLearn = 2; // 人物等级不够,不能领悟主技能
		public final static int SBCantLearn = 3; // 依赖的技能等级不够,不能领悟
		public final static int MainSBLimit = 4; // 当前技能已学到最高等级,不能继续学习
		public final static int SBLimit = 5; // 请先升级主技能
		public final static int SBExpLimit = 6; // 当前经验不足,不能学习
		public final static int SBMoneyLimit = 7; // 当前金钱不足,不能学习
		public final static int SBActivityLimit = 8; // 当前行动力不足,不能学习
		public final static int UnkownError = 9; // 未知错误 by changhao
		public final static int LiveSkillIdError = 10; // 生活技能ID错误 by changhao
		public final static int MoneyNotEnough = 11; // 金钱不足 by changhao
		public final static int ContributeNotEnough = 12; // 公会贡献不足 by changhao
		public final static int LevelNotEnough = 13; // 等级不够不能学习 by changhao
		public final static int NeedFaction = 14; // 需要公会 by changhao
		public final static int SkillMaxLimit = 15; // 技能已经最大了 by changhao
		public final static int ItemNotExist = 16; // 道具不存在 by changhao
		public final static int OneStuffCannot = 17; // 一个道具不能炼药 by changhao
		public final static int SkillLevelNotEnough = 18; // 技能等级不足 by changhao
		public final static int NoCookingFood = 19; // 没有可以烹饪的食物 by changhao
		public final static int EnergyNotEnough = 20; // 活力不足 by changhao
	}
}
