#ifndef __BATTLECOMMON_H
#define __BATTLECOMMON_H

#include "SceneCommon.h"

#define OneSideMaxNum 14	//战斗一方最多14个单位
//友方编号从1-14
#define FriendMinID 1
#define FriendMaxID 14
//地方编号从15-28
#define EnemyMinID 15
#define EnemyMaxID 28

#define WatchMinID	31	//十个观战位
#define WatchMaxID	40

#define BATTLE_GRID	25

#define AUTO_ROUND_NUM 29	//自动回合数30

#define AI_PLAYACTION_DELAY 400	//AI的演示时间

const int c_iBattleJumpActionTime = 1000;
const float c_fBattleJumpSpeedRate = 0.6f;//0.5或许好一点
const float c_fBattleIllusionJumpSpeedRate = 0.9f;//0.5或许好一点
//使用对象
const int BattleUseMainCharacter	= 1<<0;		//战斗主角
const int BattleUseMainPet			= 1<<1;		//主角宠物
const int BattleUseFriendCharacter	= 1<<2;		//友方角色
const int BattleUseFriendPet		= 1<<3;		//友方宠物
const int BattleUseFriendNPC		= 1<<4;		//友方NPC
const int BattleUseEnemyCharacter	= 1<<5;		//敌方角色
const int BattleUseEnemyPet			= 1<<6;		//敌方宠物
const int BattleUseEnemyNPC			= 1<<7;		//敌方NPC
const int BattleUseEnemyCreep		= 1<<8;		//敌方怪物

const int BattleUseMarriage			= 1<<9;		//夫妻
const int BattleUseBrother			= 1<<10;	//结交
const int BattleUseMaster			= 1<<11;	//师徒
// << 12 不要用策划给占了
const int BattleUseFriendArea		= 1<<13;	//友方地面
//const int BattleUseEnemyArea		= 1<<14;	//敌方地面
//十个观战位，直接写死好了
const Nuclear::NuclearLocation c_nWatcherPosition[10] = 
{
	Nuclear::NuclearLocation(700,150),
	Nuclear::NuclearLocation(650,130),
	Nuclear::NuclearLocation(750,170),
	Nuclear::NuclearLocation(750,50),
	Nuclear::NuclearLocation(800,70),
	Nuclear::NuclearLocation(100,450),
	Nuclear::NuclearLocation(150,470),
	Nuclear::NuclearLocation(50,430),
	Nuclear::NuclearLocation(50,550),
	Nuclear::NuclearLocation(0,530)
};

const Nuclear::NuclearLocation c_nBattleMiddle = Nuclear::NuclearLocation(34,27);

const Nuclear::NuclearDirection	c_nFriendDir = Nuclear::XPDIR_TOPLEFT;			//友方单位朝向 左上
const Nuclear::NuclearDirection	c_nEnemyDir	 = Nuclear::XPDIR_BOTTOMRIGHT;		//敌方单位朝向 右下
const Nuclear::NuclearDirection	c_nWatchDir = Nuclear::XPDIR_BOTTOMLEFT;		//观战单位朝向 左下

//战斗单位种类
enum eBattlerType
{
	eBattlerCharacter	= 1,	//角色
	eBattlerPet			= 2,	//宠物
	eBattlerNaKaMa		= 3,	//伙伴（玩家自带助战）
	eBattlerMonster		= 4,	//暗雷野怪
	eBattlerNPC			= 5,	//战斗npc（24000以上）
	eBattlerNaKaMaSys	= 6,	//系统安排的助战
	eBattlerMax,
};
//战斗中的操作单位
enum eOperateBattlerType
{
	eOperateBattler_Char ,
	eOperateBattler_Pet,
	eOperateBattlerMax
};
//战斗状态
enum eBattleState
{
	eBattleState_Null = 0,
	eBattleState_Begin,				//切入战斗
	eBattleState_AIBeforeOperate,	//操作前AI演示
	eBattleState_OperateChar,		//战斗人物操作状态
	eBattleState_OperatePet,			//战斗宠物操作状态
	eBattleState_Wait,				//等待状态
	eBattleState_Demo,				//战斗演示状态
	eBattleState_DemoOperate,		//演示和操作状态中的过度阶段
	eBattleState_BeforeBattleEnd,	//战斗结束前AI
	eBattleStateMax
};

enum eBattleOperate
{
	eOperate_Null		= 0,
	eOperate_Attack		= 1,	//普通攻击
	eOperate_Skill		= 2,	//技能
	eOperate_Item		= 3,	//物品
	eOperate_Defence	= 4,	//防御
	eOperate_Protect	= 5,	//保护	
	eOperate_Summon		= 6,	//召唤
	eOperate_Recall		= 7,	//召回
	eOperate_Catch		= 8,	//捕捉
	eOperate_Runaway	= 9,	//逃跑
	eOperate_Rest		= 10,	//休息
	eOperate_Special	= 11,	//特殊技能
	eOperate_SummonInstant		= 12,	//瞬间召唤
	eOperate_EscapeInstant		= 13,	//瞬间逃跑
	eOperate_Failure			= 14,	//操作失败
	eOperate_AIBattleEnd		= 15,	//这个客户端不用
	eOperate_EnvironmentDemo	= 16,	//战场环境Demo
	eOperate_EnvironmentChange	= 17,//战场环境改变
	eOperate_RoundEndDemo = 18,	//回合末结算demo	与战场环境改变一样，没有DemoSender		
	eOperate_UniqueSkill = 19,//绝技
	eOperate_FailureNoWonder = 20,//操作失败_不带叹号的！	
};

enum eBattleResult
{
	eBattleResult_Null,
	eBattleResult_HPChange		= 1<<0,	//生命值变化
	eBattleResult_MPChange		= 1<<1,	//魔法值变化
	eBattleResult_SPChange		= 1<<2,	//目标怒气变化
	eBattleResult_ULHPChange	= 1<<3,	//目标当前血上限变化
	eBattleResult_Rest			= 1<<4,	//目标休息
	eBattleResult_Hit			= 1<<5,	//目标受伤
	eBattleResult_Critic		= 1<<6,	//目标被暴击
	eBattleResult_Defence		= 1<<7,	//目标防御
	eBattleResult_Parry			= 1<<8,	//目标招架
	eBattleResult_Dodge			= 1<<9,	//目标闪避
	eBattleResult_Runaway		= 1<<10,//目标逃跑
	eBattleResult_Seized		= 1<<11,//目标被捕捉
	eBattleResult_Summonback	= 1<<12,//目标被召回
	eBattleResult_Death			= 1<<13,//目标死亡,倒在原地(现在只有人可以这样)
	eBattleResult_FlyOut		= 1<<14,//目标被击飞
	eBattleResult_Ghost			= 1<<15,//目标进入鬼魂状态
	eBattleResult_Relive		= 1<<16,//目标被复活
	eBattleResult_Summon		= 1<<17,//目标被召唤
	eBattleResult_NotDefence	= 1<<18,//忽略防御
	eBattleResult_Absorb		= 1<<19,//吸收
	eBattleResult_DestroyMP		= 1<<20,//打蓝
	eBattleResult_GodBless		= 1<<21,//神佑
	//eBattleResult_RiseHalf	= 1<<21,//目标倒地后原地复活（半血半蓝）
	//eBattleResult_RiseFull	= 1<<22,//目标倒地后原地复活（满血满蓝）
	eBattleResult_EPChange		= 1<< 23,//连击点
	eBattleResultMax,
};
//操作状态（准备中，请等待，掉线）
enum eOperateState
{
	eOperateState_Null,
	eOperateState_Prepare,	//准备中
	eOperateState_Wait,		//请等待
	eOperateState_FallLine,	//掉线
	eOperateStateMax
};
struct sBattleOperateCommand
{
	int					TargetID;			//被击者
	eBattleOperate		BattleOperate;		//操作类型
	int					OperatorID;		//对于技能，则表示技能ID，对于物品，表示物品ID，对于召唤，表示宠物ID
	sBattleOperateCommand()
	{
		TargetID = 0;
		BattleOperate = eOperate_Attack;
		OperatorID = 0;
	}
	void Reset()
	{
		TargetID = 0;
		BattleOperate = eOperate_Attack;
		OperatorID = 0;
	}
};
struct sBattleNpcData
{
	int				NpcBaseID;
	std::wstring	name;
};

typedef std::map<int,int>	BattlerBuffMap;
//战斗单位数据结构
struct sBattlerData
{
	int				iBattleID;			//战斗编号 1~25	1~20为作战单位 21~25为参战单位
	std::wstring	strName;
	std::wstring	strTitle;
	int				iTitleID;
	eBattlerType 	BattlerType;
	char			bGM;
	int				school; 
	int				subtype; // 子类型，宠物为资质，怪物为宝宝野宠等
	int64_t			BattlerDataID;	//对于战斗人物，为场景人物ID；对于宠物，为宠物ID；对于NPC，则为怪物NPC表ID
	int				UpLimitHp;		//血量上限
	int				MaxHp;			//最大血量	最大血量-血量上限就是伤
	int				Hp;				//当前血量
	int				Ep;				//当前效果点
	int				iShapeID;		// 可变造型ID
	int				level;
    int             colorInx;
    int             footprint; //footlogoid
	std::list<int>  petkeys; // 已经出战的宠物
	int             eQuipEffect;
    
	BattlerBuffMap	BattleBuff;
	eOperateState	OperateState;	//当前操作状态
	std::map<char,int>	componentsMap;
	sBattlerData()
	{
		strName = L"";
		strTitle = L"";
		BattlerDataID = 0;
		bGM = 0;
		iShapeID = 0;
		MaxHp = Hp = UpLimitHp = 0;
		subtype = 0;
		level = 0;
		OperateState = eOperateState_Null;
        colorInx = 0;
        footprint = 0;
	}
};
typedef std::list<int>	BattleBuffList;
typedef std::map<int,int>	BattleBuffMap;
struct sBattleBuffResult
{
	int battleid;	//战斗单位
	int buffid;
	int buffdata;	//-1代表删除该buff，0代表没有回合数buff，大于0为回合数
	sBattleBuffResult()
		:battleid(0),buffid(0),buffdata(0)
	{

	}
	sBattleBuffResult(int battle,int id,int data)
		:battleid(battle),buffid(id),buffdata(data)
	{

	}
};
//攻击施法
struct sBattleDemoAttack
{
	int	AttackerID;
	int	HPConsume;				//耗血
	int	MPConsume;				//耗蓝
	int SPConsume;				//耗怒气
	eBattleOperate	BattleOperate;	//操作类型
	int	OperatorID;				//操作ID，对于魔法，则是魔法ID
	int	AttackType;				//攻击类型  0普通  1法术连击
	int msgid;					//消息id，操作失败要发个消息
	std::list<sBattleBuffResult>		BuffResultList;
	sBattleDemoAttack()
		:AttackerID(0),HPConsume(0),MPConsume(0),SPConsume(0),BattleOperate(eOperate_Attack),OperatorID(0),AttackType(0),msgid(0)
	{
		BuffResultList.clear();
	}
};

//攻击效果
struct sBattleDemoResult
{
	int ResultType;
	int FlagType;
	int	TargetID;
	int	HPChange;
	int	MPChange;
	int MaxHPChange;		//当前血上限变化
	int SPChange;			//当前怒气值变化
	int	eTargetResult;		//被击者效果
	int	ReturnHurtEffect;	//受击方造成的反伤值，如果为0则代表没有反伤
	int	AttackBackEffect;	//受击方造成的反击值，如果为0则代表没有反击
	int	StealHPEffect;		//攻击方产生的吸血值，如果为0则代表没有吸血
	int	StealMPEffect;		//攻击方产生的吸蓝值，如果为0则代表没有吸蓝
	int	eAttackerResult;	//攻击者效果
	int	ProtecterID;
	int	ProtectHPChange;
	int	eProtecterResult;	//保护者效果
	int	AssisterID;			//合击者ID
	int ProtectMaxHPChange;	//保护者血上限变化
	int ReturnHurtMaxHP;	//受击方被反伤致死后的血上限变化
	std::list<sBattleBuffResult>		BuffResultList;
	bool bgeneresult;		//生成结果
	int hitnum;				//打击次数
	int EveryHitHP;			//每次打击的伤害
	int EveryProtectHitHP;	//保护者每次被打击伤害
	eActionType eUseAction;	//使用动作
	int	HPChangeGodBless;	//神佑血量变化
	int	EPChange;			// 受击者效果点变化，为正是加效果点，为负是扣效果点
	int ShapeChange;		// 模型改变

	sBattleDemoResult()
		: ResultType(0), FlagType(0), TargetID(0), HPChange(0), MPChange(0), MaxHPChange(0), SPChange(0), eTargetResult(0), 
		  ReturnHurtEffect(0), AttackBackEffect(0), StealHPEffect(0), StealMPEffect(0), eAttackerResult(0), ProtecterID(0),
		  ProtectHPChange(0), eProtecterResult(0), AssisterID(0), ProtectMaxHPChange(0), ReturnHurtMaxHP(0), bgeneresult(false),
		  hitnum(1), EveryHitHP(0), EveryProtectHitHP(0), eUseAction(eActionNull), HPChangeGodBless(0), EPChange(0),ShapeChange(0)
	{
		BuffResultList.clear();
	}
};

//战斗回合内的AI
struct sBattleAIAction
{
	int DemoIndex;			//第几个回合出手
	int	ExecuteTime;		//-1代表行动前，0代表行动后，1-28代表相应战斗者死亡时
	int BattlerID;			//执行AI的战斗单位ID，有些AI不是某个战斗者执行，比如换场景
	int AIActionID;			//AI的ID
	sBattleAIAction()
		:DemoIndex(0),ExecuteTime(0),BattlerID(0),AIActionID(0)
	{

	}
};

//typedef std::vector<sBattleDemoResult> Result;
typedef std::vector<sBattlerData>		BattlerData;

typedef std::vector<sBattleDemoResult> stBattleDemoResultArr;

struct sNewDemoResult
{
	int subskillid;
	int subskillstarttime;
	stBattleDemoResultArr listResults;	//攻击结果列表
};
typedef std::vector<sNewDemoResult> stNewDemoResultArr;

struct sBattleDemoScript 
{
	sBattleDemoAttack	stMagicCasting;
	stNewDemoResultArr	listResults;
	//Result				listResult;		//攻击结果列表
	BattlerData			listBattler;	//添加的战斗单位列表
	std::map<int,int>	RoleAttribute;	//人物属性
	std::map<int,int>	PetAttribute;	//宠物属性
};


//根据battleid来取要显示的CEGUI的窗口的index和敌友tab
inline void GetBattlerIndexAndSide(int battleid,int& index,bool& bFriend)
{
	if (battleid >= 1 && battleid <= 10)
	{
		index = battleid - 1;
		bFriend = true;
	}
	else if (battleid >= 15 && battleid <= 24)
	{
		index = battleid - 15;
		bFriend = false;
	}
}
#endif
