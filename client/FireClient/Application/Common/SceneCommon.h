#ifndef __SCENECOMMON_H
#define __SCENECOMMON_H

#include "GameCommon.h"
#include "../ProtoDef/rpcgen/fire/pb/Pet.hpp"
#include "../ProtoDef/rpcgen/fire/pb/attr/AttrType.hpp"
#include "../ProtoDef/rpcgen/fire/pb/Petskill.hpp"
#include "utils/StringCover.h"
#include "Utils/Utils2.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIAnimationManager.h"
#include "WindowRendererSets/Falagard/FalStaticText.h"
#include "CallLuaUtil.h"

#include "GameApplication.h"

//四舍五入函数
#define Round_Off(x) ((x) >= 0?(int)((x)+0.5f):(int)((x)-0.5f))

typedef std::vector<DWORD>	ServiceList;


const int g_logicGrid = 16;
const int g_gridWidth = 24;
const int g_gridHeight = 16;
const int g_gridDistance = 10;

const int g_defaultPkMapID = 32;		//PK台上默认的战斗地图id

const float g_moveSpeed = 140.0f;
const float g_freeMoveSpeed = 100.0f;	//随意行走的速度

const float g_defaultActionFrequence = 1.0f;
const static int g_jumpAreaUseGridCount=5;  //一个跳转点占的格子数N*N
const static int g_jumpAreaOffsetX=1;		// 跳转区域中心对应的跳转效果的location偏格子数,负值则左偏
const static int g_jumpAreaOffsetY=0;		// 跳转区域中心对应的跳转效果的location下偏格子数,负值则上移

const static int g_npcAreaColorID=0;		//npc变色区域组件ID
const static std::wstring g_npcAreaColorComponentName=L"bodyonly";  //npc变色区域组件名
const static int g_npcDeckComponentID=1;	//npc饰品组件ID
const static std::wstring g_npcDeckComponentName=L"deck";  //npc饰品组件名

const static int g_characterWeaponComponentID=3; //人物武器组件ID
const static int g_characterHairComponentID=2; //头发组件ID
const static std::wstring g_characterHairComponentName=L"haironly";  //头发组件资源名
const static int g_characterBodyComponentID=0; //人物身体组件ID

const static int g_characterPantsComponentID=1; //人物特征变色区域ID
const static std::wstring g_characterPantsComponentName=L"pantsonly"; //人物特征变色区域名字

const static int g_characterDeckComponentID=4; //人物饰品组件ID

const int g_factionBattleMapID = 1403;	//公会战地图ID
const int g_pvpBattleMapID1 = 1414;		//PVP 40~69级地图ID
const int g_pvpBattleMapID2 = 1415;		//PVP 70~89级地图ID
const int g_pvpBattleMapID3 = 1416;		//PVP 90~109级地图ID

const int g_undeadEnemyMapID = 1506;	

const static std::string g_musicResPath="/sound/scene/";

const Nuclear::NuclearLocation g_randomVisitLoctions[8] = 
{
	Nuclear::NuclearLocation(-1,-1),
	Nuclear::NuclearLocation(-1,0),
	Nuclear::NuclearLocation(-1,1),
	Nuclear::NuclearLocation(0,1),
	Nuclear::NuclearLocation(1,1),
	Nuclear::NuclearLocation(1,0),
	Nuclear::NuclearLocation(1,-1),
	Nuclear::NuclearLocation(0,-1),
};

#define DRAW_NAME_OFFSET	10	//绘制精灵脚下名字偏移
#define DARW_POPO_OFFSET    100
#define MOUSEOVER_SPRITE_COLOR 0xFF0000FF


enum eMoveMask
{
	eMoveMaskNull = 0x00,
	eMoveMaskNormal = 0x01,	//普通行走阻挡
	eMoveMaskFly = 0x02,	//飞行阻挡
	eMoveMaskHigh = 0x08,	//二层阻挡点
	eMoveMaskMax
};

enum eBattleInfo
{
	eOutBattle = 0,
	eInBattle = 1,
	eWatchBattle = 2,
};

//npc任务状态
enum eNpcMissionState
{
	eNpcMissionNoQuest = 0,			//没有任务
	eNpcMissionDisplay = 1,           //隐藏npc有任务，显示 
	eNpcMissionInCompleteQuest = 2,   //有任务，未完成
	eNpcMissionBattle = 3,			//战斗npc
	eNpcMissionCompleteQuest = 4,		//有可交任务
	eNpcMissionNewQuest = 5,		    //有可接任务
	eNpcMissionMainQuest = 6,			//有主线任务				
};

//任务状态
enum eMissionServerIconState
{
	eMissionIconNull = 0,			//没有任务
	eMissionIconAcceptalbe = 1,	//可接任务
	eMissionIconNoComplete = 2,	//接取任务，未完成
	eMissionIconComplete = 3,		//可交任务状态
};

enum eCharacterState
{
	eCharacterStateNull,
	eCharacterStateInBattle = 1,	//战斗状态
	eCharacterStateWatchBattle = 2,	//观战状态
	eCharacterStatePlayCG = 8,	//动画播放状态
	eCharacterStateFB_ATTACK1 = 16384,//15:工会战高级攻城
	eCharacterStateFB_ATTACK2 = 32768,//16:工会战中级攻城
	eCharacterStateFB_ATTACK3 = 65536,//17:工会战初级攻城
	eCharacterStateFB_REPAIR1 = 131072,//18:工会战高级修理
	eCharacterStateFB_REPAIR2 = 262144,//19:工会战中级修理
	eCharacterStateFB_REPAIR3 = 524288,//20:工会战初级修理
	eCharacterStateGM = 2097152,//22:GM状态
	eCharacterStateMax
};

enum eNpcState
{
	eNpcStateNull,
	eNpcStateInBattle = 1,	//战斗状态
	eNpcStateMax
};

enum eSceneObjType
{
	eSceneObjItem,
	eSceneObjNpc,			//服务器通知加载的npc
	eSceneObjCharacter,
	eSceneObjPuppy,		//场景随机动物
	eSceneObjPet, 
	eSceneObjFollowNpc,	//跟随npc
	eSceneObjMovie,		//场景动画中的npc
	eSceneObjTaskShowNpc,	//任务加载npc
	eSceneObjClientNpc,	//客户端场景NPC
	eSceneObjBattler,
	eSceneObjBattleIllusion,//战斗中的一些幻想，惨象
	eSceneObjBoat,
	eSceneObjMax,
};

enum eNpcType
{
	eNpcTypeNone = 0,
	eNpcTypeImportant,		//重要
	eNpcTypeTranslate,      //传送
	eNpcTypeTrade,			//商业
	eNpcTypeNormal,			//普通
	eNpcTypeFossick,		//采集类npc
	eNpcTypeCreeps,			//明雷
	eNpcTypeTalkMonster,    //可以说话的怪物,如强盗宝图生成之强盗
	eNpcMaze = 9,          //迷宫传送点
	eNpcFollow = 10,          //跟随npc类型
	eNpcAcpTask = 11,          //身上有可接任务
	eNpcUndeadScene = 13,		//不死军团玩法里场景中需要跪的 npc
	eNpcUndeadSceneItem = 14,	//不死军团玩法里的宝箱
	eNpcNoTurn = 15,			//不会转向的npc
	eNpcInFuBen = 16,			//副本里的npc
	eNpcItemBox = 18,			//宝箱NPC,18号，和景物NPC的类别区分开
	eNpcClickToTrigger = 19,	//点击npc触发
	eNpcInCompleteTask = 20,          //身上有未完成的任务
	eNpcFinishTask = 21,          //身上有可交任务
	eNpcDisPlayInTask = 22,          //隐藏类npc
	eNpcTaskShow=26,//任务显示npc
	eNpcTypeMax,
	eNpcFuyuanBox = 29,//福源宝箱 和npc区别开不执行转向
};

//char[0]为0x0F，则代表拥有0,1,2,3,4个状态
enum eState
{
	eStateNull = -1,
	eStateBattle		= 0,
	eStateBusiness		= 1,
	eStateTeamLeader	= 2,
	eStateSelling		= 3,
	eStateMax
};

//0 stand1 1 runleft 2 runright 3 在马上
enum eActionType
{
	eActionNull			= 0,
	eActionStand		= 1,		//普通站立
	eActionStandRandom	= 2,	//站立随机动作
	eActionBattleStand	= 3,	//战斗警戒
	eActionRun			= 5,			//跑步，不存在左腿还是右腿先跑
	eActionBattleRun	= 6,	//战斗中跑
	eActionOnHit1		= 7,		//受击1
	eActionAttack		= 8,		//攻击
	eActionMagic1		= 9,		//施法1	现在有三个
	eActionRunaway		= 10,		//逃跑
	eActionDefence		= 11,		//防御;
	eActionDodge		= 12,		//闪避
	eActionDying		= 13,		//倒地
	eActionDeath		= 14,		//死亡
	eActionRoll			= 15,		//击飞
	eActionDeathStill	= 16,  // 静止倒地

	//使用3D之后扩展的动作
	eActionBattleJump	= 27,	//战斗中跳跃 用于跳斩
	eActionBattleStandReady	= 30,//战斗警戒状态
	eActionAttack2		= 42,		//攻击2
	eActionMax
};

//宠物颜色值
enum ePetColour
{
	eBattleNpc = -1,		//战斗中对方npc砖红色
	eColourNull = 0,
	eWhitePet,		//白色 1
	eGreenPet,	//绿色 2
	eBluePet,	//蓝色 3
	ePurplePet,	//紫色 4
	eOrangePet,	//橙色 5
	eGoldPet,	//金色 6
	ePinkPet,   //粉色 7
	eRedPet,    //红色 8
	eMaxColor,	//
};

struct stTeamContract
{
	int			time;
	int			money;
	std::map<int64_t, std::wstring> contractMember;
	std::map<int64_t, int64_t>	   contractVote;
	stTeamContract() :time(0), money(0){}

	std::map<int64_t, int> CountTicket()
	{
		std::map<int64_t, int> tickedCount;

		std::map<int64_t, int64_t>::const_iterator it = contractVote.begin();
		while (it != contractVote.end())
		{
			if (tickedCount.count(it->second) > 0)
				++tickedCount[it->second];
			else
				tickedCount[it->second] = 1;
			++it;
		}

		return tickedCount;
	}
};

struct stMovieSceneNpcData
{
	int64_t			id;
	std::wstring	name;
	int             title;
	int             model;
	int             colorTemplate;
	Nuclear::NuclearDirection   dir;
	std::wstring  chengwei;
	DWORD chengweiColor;

	stMovieSceneNpcData(int64_t _id = 0, const std::wstring& _name = L"", int _title = 0, int _model = 0, Nuclear::NuclearDirection _dir = Nuclear::XPDIR_BOTTOMLEFT, int _colorTemplate = 1,
		const std::wstring& _chengwei = L"", DWORD _cwColor = 0xFFFFFFFF) :
		id(_id),
		name(_name),
		title(_title),
		model(_model),
		dir(_dir),
		colorTemplate(_colorTemplate),
		chengwei(_chengwei),
		chengweiColor(_cwColor)
	{

	}

	stMovieSceneNpcData& operator=(const stMovieSceneNpcData& rhs)
	{
		id = rhs.id;
		name = rhs.name;
		title = rhs.title;
		model = rhs.model;
		dir = rhs.dir;
		colorTemplate = rhs.colorTemplate;
		chengwei = rhs.chengwei;
		chengweiColor = rhs.chengweiColor;
		return *this;
	}
};

//玩家信息数据结构
struct stCharacterInfo
{
	int64_t			id;
	std::wstring	name;
	eSchoolType		school;
	int				level;
	std::wstring	title;
	std::wstring	faction;
};

struct stMapPetData
{
	int64_t roleid;		//主人id
	int showpetid;		//宠物的基本id，用于寻找外形
	ePetColour showpetcolour;		//宠物的颜色
	std::wstring showpetname; // 名字
	char	level;
	char    showskilleffect;// 气质类技能的特效ID[1,128]

	stMapPetData()
	{
		roleid = 0;
		showpetid = 0;
		level = 1;
		showskilleffect = 0;
	}
	int GetShapeID()
	{
		int petData_id = GetTableNumberFromLua("pet.cpetattr", showpetid, "id");
		if (petData_id == -1)
			return 0;
		int petData_modelid = GetTableNumberFromLua("pet.cpetattr", showpetid, "modelid");
		return petData_modelid;
	}
};

//角色换装信息
struct stCharacterChangeEquipData
{
	int colorTemplate; //颜色模板
	int weaponID; //武器造型ID
	int hatID; //头饰ID
	unsigned char weaponColor;

	stCharacterChangeEquipData() :
		colorTemplate(1),
		weaponID(0),
		hatID(0),
		weaponColor(0)
	{

	}
};

//玩家主角数据结构
struct stMainCharacterData
{
	int64_t roleid; // ID
	std::wstring	strName;		// 名字
	int race;
	int shape;
	int	school;
	int camp;
	std::wstring	schoolName;
	std::wstring	schoolSkill;
	eSexType	sex;			// 性别
	std::map<int, int>	roleattribution;//主角属性
	int	TitleID;		// 称谓iD

	std::map<int, float> roleattrFloat;		//玩家属性浮点值， 用于加点

	std::map<int, int>	mapRoleScore;			//记录各种分数

	int64_t exp;		// 经验
	int64_t nexp;		// 升级 需要经验

	int fightpet;		// 当前战斗宠物
	int showpet;		// 当前展示宠物
	int servantid;
	int64_t masterid;
	int footprint;
	int automovepathid;//巡游路径ID

	//--------------------------------------------------------------------- 加点相关
	int pointSchemeID;				//当前加点方案
	int pointSchemeChangeTimes;		//方案切换次数
	std::map<int, int > pointScheme;//潜能， 即方案分配的 剩余 点数

	int cons;			//体质
	int iq;				//魔力
	int str;			//力量
	int endu;			//耐力
	int agi;			//敏捷

	std::vector<int> cons_save;			//已分配体质
	std::vector<int> iq_save;			//已分配魔力
	std::vector<int> str_save;			//已分配力量
	std::vector<int> endu_save;			//耐力
	std::vector<int> agi_save;			//敏捷

	int totalScore;
	int equipScore;
	int manyPetScore;
	int petScore;
	int levelScore;
	int xiulianScore;
	int roleScore;
	int skillScore;

	stCharacterChangeEquipData EquipData;
	stMainCharacterData()
	{};
	int GetValue(int key) const
	{
		std::map<int, int>::const_iterator it = roleattribution.find(key);
		if (it != roleattribution.end())
		{
			return it->second;
		}
		return 0;
	}

	float GetFloatValue(int key) const
	{
		std::map<int, float>::const_iterator it = roleattrFloat.find(key);
		if (it != roleattrFloat.end())
		{
			return it->second;
		}
		return 0;
	}
	void setFloatValue(int key, float data)
	{
		roleattrFloat[key] = data;
	}
	int GetScoreValue(int key) const
	{
		std::map<int, int>::const_iterator it = mapRoleScore.find(key);
		if (it != mapRoleScore.end())
		{
			return it->second;
		}
		return 0;
	}


};

//npc基本表
struct stNpcBaseData
{
	int				BaseID;
	std::wstring	strNpcModel;	//Npc造型
	eNpcType		npcType;		//Npc类型
	std::wstring	strName;		//Npc名字
	std::wstring	strTitle;		//Npc称谓
	std::wstring	strChitchat;	//Npc闲话
};
struct stNPCConfig
{
	int id;
	double bodytype;
	int npctype;
	int modelID;
	std::wstring name;
	std::wstring foottitle;
	std::wstring headtitle;
	std::wstring title;
	int area1colour;
	int area2colour;
	int mapid;
	int xPos;
	int yPos;
	int hide;
	int ndir;
	int nstate;
};
//npc数据结构
struct stNpcData
{
	int64_t					NpcID;
	int						NpcBaseID;
	Nuclear::NuclearLocation		ptPos;		//坐标
	Nuclear::NuclearLocation		destPos;	//移动目标点
	int						moveSpeed;	//移动速度	
	Nuclear::NuclearDirection	dir;
	std::wstring			strName;
	int						ShapeID;
	eNpcMissionState		dQuestState;
	bool					bHighlevel;
	int                    scenestate;
public:
	stNpcData():
	  ShapeID(0),
	  NpcID(0),
	  NpcBaseID(0),
	  dQuestState(eNpcMissionNoQuest),
	  moveSpeed(0),
	  scenestate(0),
	  bHighlevel(false)
	  {}
};

//怪物装备造型信息
struct stNpcEquipData
{
	int Area1Color;  //变色区域1颜色
	int Area2Color;  //变色区域2颜色
	                                                                                                                                                                                                 
	stNpcEquipData():
	Area1Color(0xFFFFFFFF),
	Area2Color(0xFFFFFFFF)
	{
	}
};



//人物数据结构
struct stCharacterData
{
	int64_t					dwID;
	std::wstring			strName;
	int						TitleID;
	std::wstring			strTitle;
	int						face;
	eSexType				eSex;
	eSchoolType				eSchool;
	Nuclear::NuclearLocation	ptPos;		//坐标
	Nuclear::NuclearDirection	dir;
	Nuclear::NuclearLocation	ptTargetPos;//坐标
	int						level;
	int64_t					teamID;
	int						teamindex;
	int						teamstate;
	int						showpetlevel;
	int						shape;//造型
    int                     camp;       //阵营
    short                   relation;   //关系
    int                     gongde; 
    int                     honour;     //荣誉
	int						equipEffect; // 套装特效
	int						actuallyshape;
	int						characterstate;		//角色状态，按位算的，见状态枚举值列表
	int						weaponbaseid;	//装备武器的baseID，没有武器填0 (这个值武器并没有使用，现在代表坐骑信息)
	bool					bhighlevel;		//是否在高层，其实有这个字段，这个是在服务器发协议的时候用
	BYTE					actiondefault;	//基本动作，0为stand，1为dance，2为rest
    int                     footprint;
	int						automovepathid;//巡游路径ID
	int						automovepathid2;//路线ID
	int						automovepathid3;//路点ID
	stCharacterData()
	: dwID(0)
	, TitleID(0)
	, face(0)
	, level(0)
	, teamID(0)
	, teamindex(0)
	, teamstate(0)
	, showpetlevel(0)
	, characterstate(0)
	, ptTargetPos(-1,-1)
	, actuallyshape(0)
	, bhighlevel(false)
	, actiondefault(0)
    , camp(0)
    , footprint(0)
	, automovepathid(0)
	, equipEffect(0)
	, automovepathid2(0)
	, automovepathid3(0)
	{
		
	}
};





inline DWORD GetPetNameColor(int baseid)
{
	int petData_id = GetTableNumberFromLua("pet.cpetattr", baseid, "id");
	if (petData_id != -1)
	{
		std::wstring petData_colour = GetTableWStringFromLua("pet.cpetattr", baseid, "colour");
		return CEGUI::PropertyHelper::stringToColour(petData_colour);
	}
	return ColorDefine::White;
}

inline DWORD GetPetColour(const int& colour)
{
	return ColorDefine::White;
}

inline DWORD GetPetNameShadowColor(int colour)
{
	return ColorDefine::Black;
}

inline DWORD GetPetNameMouseOverColor(int colour)
{
	return ColorDefine::White;
}

inline DWORD GetPetNameMouseOverShadowColor(int colour)
{
	return ColorDefine::Black;
}

inline std::wstring GetColorStr(const int& color)
{
	switch(color)
	{
	case eWhitePet:
		return MHSD_UTILS::GETSTRING(1194).c_str();
	case eGreenPet:
		return MHSD_UTILS::GETSTRING(1195).c_str();
	case eBluePet:
		return MHSD_UTILS::GETSTRING(1196).c_str();
	case ePurplePet:
		return MHSD_UTILS::GETSTRING(1197).c_str();
	case eOrangePet:
		return MHSD_UTILS::GETSTRING(1198).c_str();
	case eGoldPet:
		return MHSD_UTILS::GETSTRING(1199).c_str();
	default:
		return L"";
	}
	return L"";
}

const float c_fPetScale0 = 0.6f;
const float c_fPetScale1 = 0.7f;
const float c_fPetScale2 = 0.8f;
const float c_fPetScale3 = 0.9f;
const float c_fPetScale4 = 1.0f;

inline const float GetPetFloatScale(int petscale)
{
	switch(petscale)
	{
	case 1:
		return c_fPetScale0;
	case 2:
		return c_fPetScale1;
	case 3:
		return c_fPetScale2;
	case 4:
		return c_fPetScale3;
	case 5:
		return c_fPetScale4;
	default:
		return c_fPetScale4;
	}
}

inline int GetPantColorSchemesByColorid(int colorid)
{
	//如colorid = 2050403，解析为：2 = colorid/1000000 代表造型id；03 = ((colorid%1000000)%10000)%100 代表特征是3号配色方案；
	//特征配色方案id = 3203
	return (3 * 1000 + (colorid / 1000000 - 1) * 100 + ((colorid % 1000000) % 10000) % 100);
}

inline int GetBodyColorSchemesByColorid(int colorid)
{
	//如colorid = 2050403，解析为：2 = colorid/1000000 代表造型id；04 = ((colorid%1000000)%10000)/100 代表衣服是4号配色方案；
	//衣服配色方案id = 2204
	return (2 * 1000 + (colorid / 1000000 - 1) * 100 + ((colorid % 1000000) % 10000) / 100);
}

inline int GetHairColorSchemesByColorid(int colorid)
{
	//如colorid = 2050403，解析为：2 = colorid/1000000 代表造型id；05 = (colorid%1000000)/10000 代表头部是5号配色方案；
	//头部方案id = 1205
	return (1 * 1000 + (colorid / 1000000 - 1) * 100 + (colorid % 1000000) / 10000);
}

inline DWORD GetNpcColorByColorID(const std::wstring& strModelName, int colorID)
{
	return 0xFFFFFFFF;
}

inline DWORD GetABGRColorByStringNew(const std::wstring& colorText)
{
	int a = 0;
	int r = 0;
	int g = 0;
	int b = 0;

	std::vector<std::wstring> colors;
	std::vector<std::wstring>	delimiters;
	delimiters.push_back(L",");

	StringCover::split_string(colorText, delimiters, colors);
	if (colors.size() == 4)
	{
		a = StringCover::WStrToNum<int>(colors[0]);
		r = StringCover::WStrToNum<int>(colors[1]);
		g = StringCover::WStrToNum<int>(colors[2]);
		b = StringCover::WStrToNum<int>(colors[3]);
		return   (a << 24 | b << 16 | g << 8 | r);
	}
	else
	{
		return 0xFFFFFFFF;
	}
}

inline DWORD GetARGBColorByStringNew(const std::wstring& colorText)
{
	int a = 0;
	int r = 0;
	int g = 0;
	int b = 0;

	std::vector<std::wstring> colors;
	std::vector<std::wstring>	delimiters;
	delimiters.push_back(L",");

	StringCover::split_string(colorText, delimiters, colors);
	if (colors.size() == 4)
	{
		a = StringCover::WStrToNum<int>(colors[0]);
		r = StringCover::WStrToNum<int>(colors[1]);
		g = StringCover::WStrToNum<int>(colors[2]);
		b = StringCover::WStrToNum<int>(colors[3]);
		return   (a << 24 | r << 16 | g << 8 | b);
	}
	else
	{
		return 0xFFFFFFFF;
	}
}

inline DWORD GetColorByString(const std::wstring& colorText)
{
	int r=0;
	int g=0;
	int b=0;

	return 0xFFFFFFFF;
}

inline std::wstring gGetActionName(eActionType type,int aid = 0,bool riding=false)
{
	CallLuaUtil util;
	util.addArg(type);
	util.addArg(aid);
	util.addArg(riding);
	if (!util.callLua("GetActionName"))
	{
		return L"";
	}

	return s2ws(util.getRetCString());
}

#endif
