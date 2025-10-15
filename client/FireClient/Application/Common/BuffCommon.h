#ifndef _FIRECLIENT_BUFFCOMMON_H_
#define _FIRECLIENT_BUFFCOMMON_H_

#define BATTLE_BUFF_SHOWCOUNT_MAX	10	//战况里最多显示10个buff

#define BUFF_SHOWCOUNT_MAX	15		//最多显示15个buff图标

struct stBuffBase
{
	int			buffid;
	int64_t		lefttime;			//剩余时间
	int			leftround;		//剩余回合
	int64_t		leftquantity;		//剩余量
	bool		bIsBattleBuff;		//是否是战斗buff
	std::list<std::wstring>	tipsparam;	//显示Buff Tip的参数
	stBuffBase() :buffid(0), lefttime(-1), leftround(0), leftquantity(0), bIsBattleBuff(false)
	{
		tipsparam.clear();
	}
	~stBuffBase();
};

enum eBuffTargetType
{
	eeBuffTargetNullBuff = 0,
	eBuffTargetCharacter = 1,
	eBuffTargetPet = 2,
	eBuffTargetCreeps = 3,
	eBuffTargetNpc = 4,
};

enum eBuffIconType
{
	eBuffIconMainNULL,
	eBuffIconMainCharacter,
	eBuffIconFightPet,
	eBuffIconMax,
};

enum eBuffType
{
	eBuffNULL				=	0,
	eBuffWeapon				=	500000,	//武器Buff
	eBuffHuWan				=	500001,	//护腕Buff
	eBuffZhuangShi			=	500002,	//饰品Buff
	eBuffKaiJia				=	500003,	//铠甲Buff
	eBuffYaoDai				=	500004,	//腰带Buff
	eBuffXieZi				=	500005,	//靴子Buff
	eBuffTouShi				=	500006,	//头饰Buff
	eBuffBeiShi				=	500007,	//背饰Buff
	eBuffNoNormalAnLei		=	500008,	//不遇普通暗雷状态
	eBuffAutoAddHP			=	500009,	//战斗结束后自动补充满当前生命
	eBuffAutoAddMP			=	500010,	//战斗结束后自动补充满当前法力
	eBuffDoubleExp			=	500011,	//双倍时间
	eBuffZhenFa				=	500012,	//光环效果
	eBuffQiYue				=	500013,	//协约状态
	eBuffBainShen			=	500014,	//变身
	eBuffZhongDu			=	500016,	//掉血受伤中毒
	eBuffForcePK			=	500017,	//强制PK
	eBuffPKProtect			=	500018,	//PK保护状态
	eBuffDueDou				=	500019,	//决斗状态
	eBuffTripleExp			=	500020,	//三倍时间
	eBuffBattleDeath		=	500033,	//战斗死亡状态
	eBuffBattleInvisible	=	500059,	//战斗主动隐身状态
	eBuffBattleHideSelf		=	500060,	//战斗被动隐身状态
	eBuffBattleShake		=	500070,	//战斗混乱抖动
	eBuffBattleGhost		=	509084,	//战斗鬼魂状态
	eBuffPetAutoAddHp		=	500138,
	eBuffPetAutoAddMp		=	500139,
	eBuffPetAutoAddLoyal	=	500140,
	eBuffPetAutoAddQiLi		=	500305,	//自动补气力值的buff
	eBuffTrade				=	506002,	//交易状态
	eBuffInBattle			=	506004,	//战斗状态
	eBuffWatchBattle		=	506005,	//观战
	eBuffInTeam				=	506006,	//队伍中
	eBuffTeamCaptain		=	506007,	//队长
	eBuffTeamMember			=	506008,	//队员
	eBuffTeamNormal			=	506009,	//归队队员状态
	eBuffTeamAbsent			=	506010,	//暂离队员状态
	eBuffTeamReturn			=	506011,	//归队中队员
	eBuffTeamMemberOffline	=	506012,	//离线队员
	eBuffPetOnShowing		=	506015,	//宠物观看状态
	eBuffPetOnFight			=	506016,	//宠物参战状态
	eBuffPetLowLoyal		=	506017,	//宠物低忠诚状态
	eBuffPlayingSceneMovie	=   506020, //剧情动画状态 
	eBuffCharacterAutoAddHP	=	500327,	//人物和宠物的提示购买自动补给血和蓝的buff，点击弹出购买框
	eBuffCharacterAutoAddMP	=	500328,
	eBuffPetHPAuto			=	500329,
	eBuffPetMPAuto			=	500330,
	eBuffPetLoyalAuto		=	500331,
	eBuffMax,
};


typedef std::set<int>				ParnterNpcBuffList;
typedef std::map<int, int>			ParnterNpcBuffMap;
typedef std::vector<stBuffBase*>	BuffList;
typedef std::map<int, BuffList>		BuffListMap;

#endif // _FIRECLIENT_BUFFCOMMON_H_
