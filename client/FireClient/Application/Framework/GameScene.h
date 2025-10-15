#ifndef __GAMESCENE_H
#define __GAMESCENE_H

#include "SceneCommon.h"
#include "Singleton.hpp"
#include "fire/pb/move/PickUpItem.hpp"
#include <nuieffect.h>
#include "regionbuffer.h"
#include "SceneEffect.h"
#include "SceneObject.h"
#include "Npc.h"
#include "MainCharacter.h"

#include "../../../../dependencies/LJXML/Include/LJXML.hpp"
#include "fire/pb/move/NpcBasic.hpp"

class ISprite;
class SceneObject;
class MainCharacter;
class Character;
class Npc;
class ScenePet;
class DroptItem;
class MovieSceneNpc;
class FollowNpc;
class CScenePuppy;

//地图区域信息
enum eRegionType
{
	eRegionType_QIEZUO	= 0x1,
	eRegionType_BOSA	= 0x2,
	eRegionType_Mask	= 0x4,
    eRegionType_PVP		= 1<<10,
};
typedef std::map<int64_t,SceneObject*> SceneObjMap;


enum eGameSceneTimerType
{
	eGSTimerType_TimerNull,
	eGSTimerType_GiveServantTips,
	eGSTimerType_MapChangeRun,
	eGSTimerType_MovieMapChange,
	eGSTimerType_BeginHideQuest,
	eGSTimerType_EndHideQuest,
	eGSTimerType_MoneyOverLimit,		//资产超过上限
	eGSTimerType_MapChangeDrawName,		//跳转到新地图画地图的名字
	eGSTimerType_EnterDreamRun,			//进入或返回“梦-境”
	eGSTimerType_ExistEquipReplace,		//退出装备替换界面
	eGSTimerType_WeeHours,				//零点
	eGSTimerType_EndCreateRoleEffect,	//选择角色界面点击创建角色时特效结束
	eGSTimerType_InPetIslandTip,		//玩家逗留时间过短时 加条提示
	eGSTimerType_AvoidCreepBuffTip,		//buffer剩余两分钟时 加条提示
	eGSTimerType_NpcAutoPopoStart,		//NPC自动冒泡提示周末活动开始
	eGSTimerType_NpcAutoPopoRun,		//NPC自动冒泡提示周末活动运行中，每两分钟check一次
	eGSTimerType_F9TipsRun,				//当同屏人数超过50且小于40级时的运行中
};


class EnterDreamEffectNotify: public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

class GameSceneTimer : public Nuclear::INuclearTimer
{
public:
	GameSceneTimer();
    
	~GameSceneTimer();
    
	bool SetSchedule(eGameSceneTimerType type, int cdtime);

	void CancelSchedule();

	virtual void OnTimer();
private:
	eGameSceneTimerType m_TimerType;
};

//地图NPC配置
struct stClientNpcInfo
{
	int baseId;
	Nuclear::NuclearDirection dir;
	Nuclear::NuclearLocation loc;
};
typedef std::map<int, stClientNpcInfo> IdToClientNpcInfoMap;

class GameScene : public CSingleton<GameScene>
{
public:
	GameScene();
	~GameScene();
	
	CBroadcastEvent<NoParam> EventMapChange;	//地图跳转多播事件

public:
    void RefreshAllSpriteNameTexture(); //lua
	void ReleaseAllSpriteNameTexture();
	void ReloadAllSpriteNameTexture();
	void AddScenePetData(int64_t roleid, int showpetid, std::wstring showpetname, char colour, char size, char showeffect);

	void SetChangeMapForbidRun(bool b);
	bool IsChangeMapForbidRun();

	const int& GetMapInfo(); //lua
	IdToClientNpcInfoMap& GetClientNpcMap();

	SceneObjMap&	GetSceneObject(eSceneObjType type);

	//yanji 20150818 获取某种类型的所有NPC（不考虑排序）
	std::vector<int> GetAllNpcIDByType(eSceneObjType type);

	bool	LoadMap(Nuclear::NuclearLocation ptLocation);				//进入游戏世界 //lua
	void	ClearScene();										//切换地图时用 //lua

	void LoadClientNpcInfo(); //lua
	int  GetClientNpcCount(); //lua

	bool	ChangeMap(int64_t sceneID,Nuclear::NuclearLocation ptLocation,std::wstring  mapName,int changetype,bool highlevel = false);	//切换地图场景 //lua
	std::wstring GetMapName();						//得到地图名字 //lua
	std::wstring GetMapNameUsingInMiniMap();		//得带用于小地图显示的地图名 //lua
	int		GetMapID();		//得到地图baseID，表里的ID  //lua
	bool    IsInPrison(); //lua
    bool    IsInFuben();//lua
	int		GetMapBattleGroundID(); //lua
	int		GetMapDataID();		//得到地图数据ID //lua
	int64_t GetMapSceneID(); //lua
	eNpcMissionState GetNpcStateByID(int64_t npckey, int npcbaseid);
	Npc*	AddSceneNpc(stNpcData* data); //lua
	Character*	AddSceneCharacter(stCharacterData* pData); //lua
	bool	AddMainCharacter(int64_t roleid, int sex, int shape, int camp); //lua
	bool	AddScenePet(const stMapPetData& petData); //lua
	bool	AddSceneDroptItem(const fire::pb::move::PickUpItem& data); //掉落物 //lua
	bool	AddSceneFollowNpc(const int& basedata, int index);//添加跟随npc //lua
	bool	AddSceneFossickNpc(stNpcData* data); //lua

	void	RemoveNpcByID(int64_t npcID); //lua
	void	RemoveCharacterByID(int64_t characterID); //lua
	void	RemovePetByMasterID(int64_t masterID); //lua
	void	RemoveSceneObjectByID(eSceneObjType type,int64_t id); //lua
	void	RemoveSceneObject(eSceneObjType type); //lua

	Npc*	FindNpcByBaseID(int npcbaseID); //lua
	Npc*	FindNpcByID(int64_t npcID); //lua
	ScenePet*	FindPetByMasterID(int64_t masterID); //lua
	DroptItem* FindSceneItemByID(int64_t itemID); //lua

	CScenePuppy* FindPuppyByID(int64_t id); //lua

	Character*	FindCharacterByID(int64_t characterID); //lua
	Character*	FindCharacterByTeamIDandIndex(int64_t teamID,int index); //lua
	void ClearTeamFollowWhileCharacterDelete(Character* pCharacter); //lua
	FollowNpc*	FindFollowNpcByIndex(int nNumber);
	void RefreshFollowNpcDistance();

	size_t		GetCharacterNum(); //得到当前屏幕的玩家数量 //lua

	Nuclear::NuclearLocation getJumpPoint1Pos(); // 获得跳转点1坐标

	int     GetNotTransparentManNum(); //得到非透明人的数量 //lua

	void ClearMouseOverObjects();

	void OnSceneObjectRemove(SceneObject* pObj);

	SceneObject* GetMouseOverObjects();
	std::list<SceneObject*> GetMouseOverObjectsList();

	void ResetMouseOverObjectsListWithNpcBaseID(int npcBaseID);

	SceneObject* FindSceneObjectBySprite(Nuclear::ISprite * sprite, bool bInBattle = false); //lua

	SceneObject* FindSceneObjectByTypeID(eSceneObjType type, int64_t id, bool bInBattle = false); //lua

	//void	OnTick(int now, int delta) ;
	bool	OnBeforeRender(int now);

	void	OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type); // 当精灵状态发生改变时，回调这个函数更新动作。
	void	UpdateSceneNpcQuestState(const int64_t& npckey, eNpcMissionState& state, bool bBaseID = true);//改变场景中npc的任务状态
	
	void    AddFossicNpcEffect();
	void    RemoveFossicNpcEffect(int itemid);
	void    RemoveFossicNpcCoolingState(int itemid);

	void getGuaJiMapNpc();
	
	// windows 消息
	// return true 表示希望引擎继续处理此消息，反之引擎忽略此消息
	bool	OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam );
	void	AttachCameraTo(Nuclear::ISprite* pXPSprite);
	//鼠标选中单位
	bool	SelectObjs(const Nuclear::NuclearLocation &loc, std::vector<Nuclear::ISelectableObj*> &objs);
	//不同的坐标前缀，map意味着地图的像素坐标，logic代表地图逻辑坐标，grid代表地图格子坐标，格子大小是24像素*16像素
	bool	UpdateMouseOverObjects(Nuclear::NuclearLocation mousepoint);
	bool	UpdateMouseOverNull();		//鼠标没有移到任何SceneObject上
	Nuclear::NuclearRect GetViewport();
	Nuclear::NuclearLocation MousePointToPixel(Nuclear::NuclearLocation pt);
	Nuclear::NuclearLocation PixelToGrid(Nuclear::NuclearLocation ptPixel);
	Nuclear::NuclearLocation GirdToPixel(Nuclear::NuclearLocation ptGrid);

	int		GetGridDistanceByPixel(Nuclear::NuclearLocation pt1, Nuclear::NuclearLocation pt2);
	int		GetDistanceFromMainCharacter(const int64_t& sceneobject_1,eSceneObjType object_1Type);

	Npc*	 AddNpcCommon(stNpcData* pNpcData, eSceneObjType sceneObjType);
	void	 setSceneEffect();

	//判断两个所在点是否是联通的，防止孤岛之间的寻路
	int	 GetIslandAreaID(Nuclear::NuclearLocation desposition,bool bhighlevel=false);
	bool IsTheSameArea(Nuclear::NuclearLocation desposition,Nuclear::NuclearLocation curposition,bool bhighlevel=false); //lua
	bool IsJumpBlock(Nuclear::NuclearLocation logicposition,int jumplevel);
	bool IsMoveBlock(Nuclear::NuclearLocation logicposition,eMoveMask movemask);
	bool IsInChallengeArea(Nuclear::NuclearLocation logicposition);
    bool IsMaskArea(Nuclear::NuclearLocation logicLoc);
    bool IsInPVPArea(Nuclear::NuclearLocation logicLoc);
	
	unsigned char GetGroundType(Nuclear::NuclearLocation logicposition);	//点击地面效果的类型，草地，水，尘土。。。

	void Run(int now, int delta);

	void NpcRun(int now, int delta);

	void SetSceneMapMaze(int mapid,int mazetypeid);	//动态加载地图阻挡
	void CharacterRun(int now, int delta);
	void PetRun(int now, int delta);
	void PuppyRun(int now, int delta);
	void FollowNpcRun(int now, int delta);
	void SceneEffectRun(int now, int delta);  //场景特效，主要是指屏幕效果，如渐黑渐亮

	void SetShieldCharacter(bool b,bool includemain = false);
	bool isInShieldCharacterState();

	void Draw(int now);

	void OnDrawNameCB(Nuclear::ISprite* pSprite);

	//进出战斗时一些处理
	void OnBattleBegin();
	void OnBattleEnd();
	
	void DrawFinishQuestAndChengjiuEffect();

	//地图跳转虚化相关
	void EnableJumpMapFadeEffect(bool b); //是否开启跳地图时是否画虚化效果
	void ToggleEnableJumpMapFadeEffect();
	void DrawJumpMapFade(); //画地图跳转虚化
	void StartJumMap();
	void StartEndBattleSceneFade();
	void EndJumpMap();

	void EndLoadMap();
	bool isLoadMaping();
	bool IsMapJumping();
	void SetLoadMapProgress(int pro);

	void SetFirstEnterScene(bool b);
	bool IsFirstEnterMap();

	//是否是安全区地图
	bool IsSafeMap(); //lua
	bool IsAnLeiQuMap();
	bool IsMySchoolMap(int mapid);
	bool IsMySchoolInsideMap(int mapid);
	bool IsInMySchoolInsideMap();
	bool IsInMySchoolMap();
	bool IsInLinanMap();
	bool IsInLinanNeijingMap();
	bool IsInLinanNeijingMap(int mapid);

	int	 GetMySchoolChefuPosition(Nuclear::NuclearLocation& loc);
	int	 GetLinanChefuPosition(Nuclear::NuclearLocation& loc);
	int	 GetFamilyManagerPosition(Nuclear::NuclearLocation& loc);

	Nuclear::NuclearDirection GetClientConfigNpcDirByBaseID(int npcbaseID);

	//场景剧情模式相关
	void OnEnterMovieScene(); //进入剧情模式
	void OnExitMovieScene(int curMovieMapID); //退出剧情模式
	void OnExitMovieScene(int curMovieMapID, int mapId, const Nuclear::NuclearLocation& pos); //退出剧情模式
	void HideOrShowAllSprite(bool bVis); //隐藏或显示所有精灵
    
    void OnEnterSceneNpcSpeakState(); //进入剧情npc speak模式
    void OnExitSceneNpcSpeakState(); //进入剧情npc speak模式
    void HideOrShowAllCharExceptMainChar(bool bVis); //隐藏或显示主角以外角色
	void HideAllCharExceptTeammate();
    
	void UpdateNpcStateAfterEndMovie();//退出剧情后刷新npc的任务状态
	void RefreshScenarioDisplayNpcState(int npcbaseid, eNpcMissionState state);
	bool AddMovieSceneNpc(int64_t id,const std::wstring& strName,int title, int modelid, int Screen_X, int Screen_Y, int dir, int colorTemplate, 
		eActionType actype = eActionStand, float scale = 1.0f, const std::wstring& effName = L"", int repeat = 1, int ex = 0, int ey = 0, const std::wstring& chengwei = L"", const std::wstring& cwColor = L""); //增加一个场景动画NPC
	void DelMovieSceneNpc(int64_t id); //删除一个场景动画NPC
	void DelAllMovieSceneNpc();  //删除所有的场景动画NPC
	void DelAllSceneObj();		 //删除所有的场景对象
	MovieSceneNpc* FindMovieSceneNpcByID(int64_t id);
	void OnSceneMovieChangeMap(int newMapId,int oldMoveMapID,int Logic_X, int Logic_Y); //场景剧情模式中的跳转地图
	void MovieNpcRun(int now, int delta);

	void handleFirstJuqingPlayedEnded();
	
	void HideJumpEffect();
	void ShowJumpEffect();
	bool getJumpEffectShow();
	void setJumpEffectShow(bool show);
	void AddSceneParticle(const std::wstring& strEffectName,float width,float height,float x,float y,bool cycle=false);
	void RemoveSceneParticle(int handle);	//删除一个粒子特效

	bool CanEnterDream();
	bool isOnDreamScene();
	void SetDreamStatus(int dreamStatus);
	void DrawDreamImage();
	bool isOnDreamFade();
	void BeginDreamFade();
	void SetDreamFadeTime(int time);
	void SetDreamHideSceneTime(int time);
	int GetDreamHideSceneTime();
	void OnChangeDisplayMode();
	int GetDreamState();
	void EmptyEnterDreamEffect();

	//进入新地图画地图名字相关
	void DrawEnterMapNameEffect();
	int getJumpMapID(Nuclear::NuclearLocation);
	Nuclear::NuclearLocation getJumpPointByMapId(int mapId);

	//场景特效相关
	typedef std::vector<SceneEffect*> VECSceneEffect;
	void DrawSceneEffect();
	void ClearSceneEffect();
	void AddSceneEffect(SceneEffect* pEffect);
	void RemoveSceneEffect(SceneEffect* pEffect);

	void BeginEnterMovieEffect(int Handle);
	
	void AddFirstEnterBlackEffect();
	void EndFirstEnterBlackEffect();
    void DrawMovieCapturePic(float pro);
    void ReleaseMovieCapturePic();

	//角色换装换色相关接口
	bool AddMainCharacterNpc(fire::pb::move::NpcBasic npcData);
	std::wstring GetTitleName(FireNet::Marshal::OctetsStream stream);

	void ReleaseFinishQuestEffect();

	int GetNearByCharacterNum(const Nuclear::NuclearLocation& scenenpcloc,const int& idistance);	//景物npc用 得到该景物npc附近的character数量

	Nuclear::NuclearLocation GetNearByPos(SceneObject* pObject,int radius);	//中心和半径
	Nuclear::NuclearLocation GetNearByPosPreferY(Nuclear::NuclearLocation midpos,int radius,bool bhighlevel=false);

	void UpdateInstanceNpcVisible(int npcbaseid,bool bvisible);	//副本npc是否可见相关

	//npc自动冒泡泡提示周末活动相关
	int64_t GetTimeByString(const std::wstring& strTime);
	bool LoadNpcAutoPopInf(); //载入npc自动冒泡泡提示周末活动信息
	void InitNpcAutoPopo(); //初始化npc自动冒泡泡提示周末活动
	void StartNpcAutoPopoTimer(); //开始添加计时器
	void CheckNpcAutoPopo();//每两分钟看有什么泡泡要冒

	//////////////////////////////////////////////////////////////////////////
	//震屏相关
	void BeginShake(int id, int range, int time);		//开始震屏
	void UpdateShake(int now);			//更新震屏
	void DecNotTransparentManNum();  //减少一个非透明人
	void AddNotTransparentManNum();  //增加一个非透明人

	bool IsJumpForbiddenMap();

	void SetFactionBossInfo(int64_t sceneid, int shapeid, int baseid);
	void UpdateFactionBossShape();
	void ShowFactionBossPickups();
	void HideFactionBossPickups();

	void OnDeleteSprite(Nuclear::ISprite* pISprite);
    
    void PlayMapBackGroundMusic();

	void EnableJumpMapForAutoBattle(bool b);
    
	int GetSceneNPCNum(); //lua
    Npc* GetSceneNPC(int num); //lua
    
	int GetSceneCharNum(); //lua
    Character* GetSceneCharacter(int num); //lua

	void addSceneTaskShowNpc(int nNpcId);
	void deleteSceneTaskShowNpc(int nNpcId);

	void pauseSceneEffects();
	void resumeSceneEffects();

private:
	//第一次播放的新手剧情
	bool isFirstJuqingPlayed;
	bool handleConfirmPlayXinshouJuqing(const CEGUI::EventArgs& e);
	bool handleCancelXinshouJuqing(const CEGUI::EventArgs& e);

	void LoadJumpMapPointInf();//加载跳转点
	void ReleaseAllJumpPointEffect();

	void EndDrawEnterMapNameEffect();

private:
	struct stNpcAutoPopoInf
	{
		int npcBaseID;      //npcID
		int64_t startTime;  //开始时间
		int64_t endTime;   //结束时间
		int interval;     //时间间隔
		std::wstring content; //泡泡内容
		int64_t     lastTime;   //上次显示泡泡时间
	};

	float           m_fStartNpcAutoPopoTimer;
	bool            m_bStartNpcAutoPopoTimer;
	float           m_fNpcAutoPopoTimer;
	bool            m_bNpcAutoPopoTimer;
	std::vector<stNpcAutoPopoInf> m_vecNpcAutoPopoInf;

	struct stSpecialSceneEffect
	{
		int effectid;
		int64_t begintime;
		int64_t endtime;
		int mapid;
		Nuclear::NuclearLocation loc;
		BYTE layer;
		bool bintime;
		Nuclear::IEffect* pWorldEffect;
		stSpecialSceneEffect()
			:mapid(0), effectid(0), bintime(false), pWorldEffect(NULL)
		{ }
	};
	std::list<stSpecialSceneEffect> m_SpecialSceneEffectList;

	bool m_bFirstEnterScene;	//是否是第一次进场景

	int m_iJumpMapFadePicHandle;  //地图跳转的截图的句柄
	bool m_bMapJumping; //是否在跳转地图中
	bool m_bLoadMaping; //是否是在加载地图中
	int  m_iFadeElapseTime; //
	bool m_bIsLoadMapFrame; //当前帧是否是加载地图这一帧
	int m_iLoadMapProgress;
	bool m_bEnbaleJumpMapFadeEffect; //跳地图时是否画虚化效果

	bool m_bJunmPointEffectShow;

	int c_iBingfengMapIDBeg;
	int c_iBingfengMapIDEnd;

	int m_nFactionBossShapeID;
	int m_nFactionBossBaseID;

	std::list<SceneObject*>	m_MouseOverObjectsList;	//鼠标上的SceneObject列表
	SceneObject*	m_pMouseOverObjects;

	int64_t			m_MapSceneID;
	int				m_MapDataID;		//地图数据ID（如果是地图就是ID）
	std::wstring	m_MapDataName;		//地图名字，如果是地图就是名

	bool			m_bChangeMapForbidRun;	//跳转地图时不能移动

	SceneObjMap		m_SceneSprite[eSceneObjMax];

	IdToClientNpcInfoMap	m_ClientNpcMap;		//小地图配置的npc信息

	int m_MapInfoId;	//这里边有地图名字，描述，资源路径等信息

	Nuclear::NuclearLocation jumpPoint1; // 跳转点1
	Nuclear::NuclearLocation jumpPoint2; // 跳转点2

	Nuclear::NuclearLocation jumpToPoint1; // 跳转到的点
	Nuclear::NuclearLocation jumpToPoint2; //

	int jumpMapId1;  // 跳转点地图ID1
	int jumpMapId2;  // 跳转点地图ID2

	bool m_bJumpMapForAutoBattle;

	bool m_bIsShaking;					//是否在震屏
	int m_nLastShakeTime;				//上次震屏时间
	Nuclear::NuclearRect m_CurViewportRect;	//当前viewport的rect
	int m_nShakeBetweenTime;			//震屏间隔时间
	int m_nShakeRange;					//震屏幅度
	int m_nShakeEndTime;				//震屏结束时间
	int m_nShakeID;
	int m_nShakeTop;
	int m_nShakeLeft;

	int m_nNotTransparentManNum; //非透明人数量

	int		m_iTowerBowCD;
	Npc*	m_pLeftTower1;
	Npc*	m_pLeftTower2;
	Npc*	m_pRightTower1;
	Npc*	m_pRightTower2;

	Nuclear::PictureHandle m_iEnterMapNameEffect;
	int m_iEnterMapElapseTime;

	bool m_bIsOnDrawEnterMapNameEffect; //是否是在画进入地图名字特效中

	VECSceneEffect m_vecSceneEffect;
	SceneEffect* m_pFirstEnterBlackEffect;
	Nuclear::PictureHandle m_iMovieCapturePic;

	int m_DreamStatus;
	int m_iDreamFadeElapseTime;
	bool m_bIsInDreamFade;  //是否是在淡出淡入中
	Nuclear::IEffect* m_pOnDreamEffect;
	Nuclear::IEffect* m_pEnterDreamEffect;

	int m_iDreamTotalFadeTime;  //淡出淡入时间
	int m_iDreamHideSceneTime;

	int m_nowTime;

	//地图区域的一些信息
	RegionBuffer<unsigned short>	m_RegionBuffer;	//地图区域信息
	RegionBuffer<unsigned char>	m_JumpPointBuffer;//地图跳跃阻挡信息
	RegionBuffer<unsigned char>	m_IsolatedIslandBuffer;	//地图孤岛信息
	RegionBuffer<unsigned char>	m_IsolatedIslandBuffer2;//二层孤岛信息

	//切换场景相关
	Nuclear::PictureHandle m_ChangeMapPictureHandle;
	int m_LastChangeMapPicID;

	//粒子系统
	std::list<int>	m_ParticleList;
	
	//结束剧情任务后的特效
	Nuclear::IEffect* m_pFinishQuestEffect;

	Nuclear::IEffect*	m_pUndeadSceneFogEffect;
	int	 m_lastmonsterindex;

	bool			m_bShieldCharacter;
	
	//所有的跳转点特效
	std::vector<Nuclear::IEffect*> m_vecJumpPointEffect;
};

inline Nuclear::NuclearLocation gGetRandomDirection(int dir,int offset,int sidelength)
{
	if (dir >= 4)
	{
		dir = dir%4;
	}
	switch(dir)
	{
	case 0:
		return Nuclear::NuclearLocation(offset*g_logicGrid,sidelength*g_logicGrid);
		break;
	case 1:
		return Nuclear::NuclearLocation(offset*g_logicGrid,-sidelength*g_logicGrid);
		break;
	case 2:
		return Nuclear::NuclearLocation(sidelength*g_logicGrid,offset*g_logicGrid);
		break;
	case 3:
		return Nuclear::NuclearLocation(-sidelength*g_logicGrid,offset*g_logicGrid);
		break;
	default:
		return Nuclear::NuclearLocation(0,0);
		break;
	}
}

inline GameScene* gGetScene()
{
	return GameScene::GetInstance();
}

extern Nuclear::NuclearLocation gGetCursorPosition();

#endif
