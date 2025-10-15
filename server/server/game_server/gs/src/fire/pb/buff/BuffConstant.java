package fire.pb.buff;

import fire.pb.buff.continual.DecisionBuff;
import fire.pb.buff.continual.HateDecisionBuff;


public class BuffConstant
{
	//清除类型
	public static final int CLEAR_TYPE_OUT_BATTLE = 0;//出战斗清除（下线也清除）
	public static final int CLEAR_TYPE_OFFLINE = 1;//下线清除
	public static final int CLEAR_TYPE_OFFLINE_KEEP_TIMING = 2;//下线不清除（如果有计时，继续计时）
	public static final int CLEAR_TYPE_OFFLINE_STOP_TIMING = 3;//下线不清除（如果有计时，封存计时）
	public static final int CLEAR_TYPE_USE_SKILL = 4;//使用技能清除
	public static final int CLEAR_TYPE_USE_SKILL_ATTACK = 5;//使用技能清除
	public static final int CLEAR_TYPE_IN_BATTLE_HURT = 8;//战斗内受伤清除（出战斗也清除）
	public static final int CLEAR_TYPE_IN_BATTLE_DEATH = 9;//战斗内死亡清除（出战斗也清除）

	
	public static final int CONTINUAL_REST = 501008;//休息
	public static final int CONTINUAL_STRONG_POWER_FIRST = 509003; //蛮力
	public static final int CONTINUAL_STRONG_POWER_SECOND = 509040; //高级蛮力
	public static final int CONTINUAL_GHOST = 509084;//亡魂复活
	public static final int CONTINUAL_PET_GHOST_FIRST = 509029;//不死亡魂
	public static final int CONTINUAL_PET_GHOST_SECOND = 509066;//高级不死亡魂
	 
	public static final int CONTINUAL_ANTI_GHOST = 508140;//光环驱魔
	public static final int CONTINUAL_ANTI_GHOST_FIRST = 509030;//驱魔
	public static final int CONTINUAL_ANTI_GHOST_SECOND = 509067;//高级驱魔

	public static final int CONTINUAL_FIRM_HAND_FIRST = 509034;//坚定意志
	public static final int CONTINUAL_FIRM_HAND_SECOND = 509071;//高级坚定意志
	public static final int CONTINUAL_HALLELUJAH_FIRST = 509017;//神迹
	public static final int CONTINUAL_HALLELUJAH_SECOND = 509054;//高级神迹
	/**/
	public static final int CONTINUAL_PASSIVE_HIDDEN_FIRST = 509031;// 潜行 --被动隐身
	public static final int CONTINUAL_PASSIVE_HIDDEN_SECOND = 509068;// 高级潜行 --被动隐身
	public static final int CONTINUAL_HIDDEN_FIRST = 509082;//隐身	
	public static final int CONTINUAL_HIDDEN_SECOND = 509083;//高级隐身
	public static final int CONTINUAL_ROLE_HIDDEN = 506201;//角色隐身
	public static final int CONTINUAL_DETECT_HIDDEN_FIRST = 509032;//反隐	
	public static final int CONTINUAL_DETECT_HIDDEN_SECOND = 509069;//高级反隐
	public static final int CONTINUAL_DETECT_HIDDEN = 504010;//感知
	public static final int CONTINUAL_BEHUNTER = 503001;//狩猎状态
	public static final int CONTINUAL_BEHUNTER_BOW = 503007;//远程武器	

	public static final int CONTINUAL_ETERNITY_FIRST = 509033;//永恒自由
	public static final int CONTINUAL_ETERNITY_SECOND = 509070;//高级永恒自由

	public static final int CONTINUAL_DEFENCE_FIRST = 509021;//生物力场-防御	
	public static final int CONTINUAL_DEFENCE_SECOND = 509058;//高级生物力场-高级防御
	
	public static final int CONTINUAL_PET_POISON = 509080;	//瘟疫
	//public static final int CONTINUAL_PET_POISON_HIGH = 206072;	//高级瘟疫
	/*	public static final int CONTINUAL_PET_POISON = 509080;	//瘟疫
*/	public static final int CONTINUAL_DIE_FORBID = 509081;	//灵魂禁锢

	public static final int CONTINUAL_FIRE_ABSORB_FIRST = 509025; //火吸收
	public static final int CONTINUAL_FIRE_ABSORB_SECOND = 509062; //高级火吸收
	public static final int CONTINUAL_WATER_ABSORB_FIRST = 509026; //水吸收
	public static final int CONTINUAL_WATER_ABSORB_SECOND = 509063; //高级水吸收
	public static final int CONTINUAL_EARTH_ABSORB_FIRST = 509027; //土吸收
	public static final int CONTINUAL_EARTH_ABSORB_SECOND = 509064; //高级土吸收
	public static final int CONTINUAL_THUNDER_ABSORB_FIRST = 509028; //雷吸收
	public static final int CONTINUAL_THUNDER_ABSORB_SECOND = 509065; //高级雷吸收
	
	//持续性BUFF，各buff的含义对照 c持续性Buff表.xlsx
//	public static final int CONTINUAL_TEAM_CONTRACT = 500013;	
	public static final int CONTINUAL_FROZEN_MANA = 502002;//禁魔
	public static final int CONTINUAL_FROZEN = 502003;//眩晕
	public static final int CONTINUAL_CHAOS = 502004; //恐惧
	public static final int CONTINUAL_CHAOS_ADV = 502009; //强化 恐惧	
	public static final int CONTINUAL_SLEEP2 = 504011;//睡眠	
	public static final int CONTINUAL_SLEEP = 509131;//睡眠	
	public static final int CONTINUAL_SHAPECHANGE = 505005;//变形
	public static final int CONTINUAL_CHANRAOGENXU = 506002;//缠绕根须 by changhao
	public static final int CONTINUAL_HATEDECISION = 501010;//嘲讽	
	public static final int CONTINUAL_TOTEM_DISMISS = 506107;//图腾消失
	public static final int CONTINUAL_FORGET_AIM = 506306; //	失忆诅咒
	
	public static final int CONTINUAL_SCARE_HOWL = 509105; //破胆怒吼
	public static final int CONTINUAL_PEEL_ARMOUR = 509106; //卸甲
	public static final int CONTINUAL_REVENGE = 508102; //复仇
	public static final int CONTINUAL_ELEMENT_AWKEN_FLAG = 508138; //元素唤醒--强力母牛(预留）标记用
	public static final int CONTINUAL_ELEMENT_AWKEN_RELIVE = 508139; //元素唤醒--强力母牛(预留）复活用
	public static final int CONTINUAL_FERITY_INSTINCT_FLAG = 508136; //	野性本能--持久力强(预留）标记用
	public static final int CONTINUAL_FERITY_INSTINCT_IMMUNE = 508137; //	野性本能--持久力强(预留）免疫用
	public static final int CONTINUAL_GODBLESS_ONLYONCE = 506004; //	天定之国一次性神佑buff
	public static final int CONTINUAL_FERITY_INSTINCT_IMMUNE2 = 509201; //	无敌(同   野性本能--持久力强(预留）)免疫用
		

	public static final int CONTINUAL_STUPOR_BUFF = 500008;  
	public static final int ROLE_RECOVER_HP = 500009;
	public static final int ROLE_RECOVER_MP = 500010;
	public static final int CONTINUAL_FORMATION = 500012;
	public static final int CONTINUAL_INIT_SPEED_BONUS = 500015;	
	public static final int CONTINUAL_DEATH = 500033;	
	public static final int CONTINUAL_SKILL_BASE_EFFECTS = 500058;
	public static final int CONTINUAL_ASSIST_SKILL_EFFECTS = 500082;	
	public static final int CONTINUAL_LUANMIN = 500112;
	public static final int CONTINUAL_KE_JU = 500099;

	//
	public static final int CONTINUAL_SPECIAL_PRODUCT_BETTLE = 507019;
	public static final int CONTINUAL_PLAY_CG = 507020;
	// 公会boss小弟buff（小弟死亡后会伤害boss）
	public static final int CONTINUAL_CLANBOSS = 509200;
	public static final int CONTINUAL_CLANBOSS_SPASE = 500344;
	
	//战场环境在回合末的处理所需要的BUFF
	public static final int CONTINUAL_WARM_HOT = 50;
	public static final int CONTINUAL_COLD_DOT = 50;

	//一次性BUFF，各buff的含义对照 c持续性Buff表.xlsx
	public static final int INSTANT_RELIVE = 510107;// 复活技能
	public static final int INSTANT_ADD_HP_ABL = 510000;
	public static final int INSTANT_ADD_MP_ABL = 510001;
	public static final int INSTANT_ADD_HP_PCT = 510002;
	public static final int INSTANT_ADD_MP_PCT = 510003;
	public static final int INSTANT_ADD_WOUND = 510023;	
	public static final int INSTANT_RECOVER_WOUND = 510019;	
	public static final int INSTANT_HEAL = 510024;	
	public static final int INSTANT_ITEM_RELIVE = 510046;	
	
	public static final int INSTANT_SUMMON_BACK = 510138;		//	单位消失
	public static final int INSTANT_SUMMON_FIGHTER = 510139;	//	召唤单位

	//伤害一次性BUFF
	public final static int INSTANT_PHY_DAMAGE = 510005;	
	public final static int INSTANT_NORMAL_PHY_DAMAGE = 510108;	
	public final static int INSTANT_ATACK_BACK_PHY_DAMAGE = 510109; //物理反击
	public final static int INSTANT_COMBO_PHY_DAMAGE = 510110;	
	public final static int INSTANT_EXTRA_PHY_DAMAGE = 510111;	
	public final static int INSTANT_SKILL_PHY_DAMAGE = 510113;
	
	public final static int INSTANT_POISON_DAMAGE = 510025;	
	public final static int INSTANT_NORMAL_MAGIC_DAMGE = 510004;	
	public final static int INSTANT_GET_TARGET_INFO = 510054;//获取对方信息	
	
	
	//持续性buff的父类型（持续性buff表里，buff类型的前1，2位）
	public static final int TYPE_FATHER_CONTINUAL_UNNORMAL = 1;
	
	public static final int TYPE_FATHER_CONTINUAL_HOT = 21;
	
	public static final int TYPE_FATHER_CONTINUAL_DOT = 11;
	
	public static final int TYPE_FATHER_CONTINUAL_GOD = 22;
	
	public static final int TYPE_FATHER_CONTINUAL_GHOST = 31;
	
	public static final int TYPE_FATHER_CONTINUAL_POISON = 111;//毒类型
	
	//回合末结算的buff类型，及顺序
	public static final int[] ROUND_END_PROCESS_BUFF_TYPES = new int[4];
	static
	{
		ROUND_END_PROCESS_BUFF_TYPES[0] = TYPE_FATHER_CONTINUAL_HOT;
		ROUND_END_PROCESS_BUFF_TYPES[1] = TYPE_FATHER_CONTINUAL_DOT;
		ROUND_END_PROCESS_BUFF_TYPES[2] = TYPE_FATHER_CONTINUAL_GOD;
		ROUND_END_PROCESS_BUFF_TYPES[3] = TYPE_FATHER_CONTINUAL_GHOST;
	}
	
	
	public static final String DECISON_BUFF_CLASS_NAME = DecisionBuff.class.getName();
	public static final String HATEDECISON_BUFF_CLASS_NAME = HateDecisionBuff.class.getName();
	
	public class StateType
	{   
		public static final int STATE_TRANSPORT = 507001; //押镖
		public static final int STATE_TRADE = 507002;//交易
		public static final int STATE_PRIVATE_STORE = 507003;//摆摊
		public static final int STATE_BATTLE_FIGHTER = 507004;//战斗
		public static final int STATE_BATTLE_WATCHER = 507005;//观战
		public static final int STATE_TEAM = 507006;//在队伍中
		public static final int STATE_TEAM_LEADER = 507007;//在队伍中且是队长
		public static final int STATE_TEAM_MEMBER = 507008;//在队伍中是队员
		public static final int STATE_TEAM_MEMBER_NORMAL = 507009;//归队队员
		public static final int STATE_TEAM_MEMBER_ABSENT = 507010;//暂离队员
		public static final int STATE_TEAM_MEMBER_RETURN = 507011;//归队中队员
		public static final int STATE_TEAM_MEMBER_OFFLINE = 507012;//离线队员
		public static final int STATE_MINI_GAME = 507018; // 小游戏
		public static final int STATE_ROLL = 507025; // roll点 by changhao
		public static final int STATE_INSTANCE_ZONE = 507026; //在日常副本中不能再进行组队
		public static final int STATE_REPLAY = 507027; //播放录像
		public static final int STATE_NO_DAY_PAY = 507028;//没有付日卡 by changhao
		public static final int STATE_IN_CLAN_FIGHT_BATTLE_FIELD = 507029;//在公会战场中 by changhao
	}
	
	public class BuffAgentType {
		public final static int Role = 1; // 人物角色
		public final static int PET = 2; // 宠物
		public final static int MONSTER = 3; // 怪物
		public final static int NPC = 4; // NPC
	}
}
