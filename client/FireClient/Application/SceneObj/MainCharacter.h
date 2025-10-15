#pragma once

#include "Character.h"
#include "fire/pb/move/CCheckMove.hpp"
#include "Singleton.hpp"
#include "MapWalker.h"
#include "../Event.h"

#define MaxFollowDistance 72	//超过这个距离开始跟随
#define MinFollowDistance 48	//小于这个距离停止跟随

enum eMoveState
{
	eMove_Normal,
	eMove_Follow,	//组队跟随移动状态
	eMove_Pacing,	//踱步状态，Alt+R来回走
	eMove_Fly,		//飞行状态
	eMoveMax
};

class MainCharacter : public Character,  public CSingleton<MainCharacter>
{
public:
	CBroadcastEvent<NoParam>	EventMainCharacterMove;

	MainCharacter();
	virtual ~MainCharacter();

	MapWalker m_MapWalker;

	std::vector<int> m_Services; // services 为npc所提供的服务 IDs.
	std::vector<int> m_Scenarioquests; // 剧情任务
////////////////人物行走移动相关///////////////////
    
public:
	virtual std::wstring GetName(); //lua

	void SetLButtonDown(bool b){m_bLButtonDown = b;} //lua

	void SetMovingStat(bool b){ m_bCanMove = b;}
	bool GetMovingStat(){ return m_bCanMove; }

	void VisitNpcFormList(int64_t id);

	bool GameCursorTypeProcess(SceneObject* pMouseOverObjects, WPARAM wParam, LPARAM lParam);

	void PlayMouseBtnDownEffect(Nuclear::NuclearLocation pixelloc);	//鼠标点击地面特效 //lua
	static void InitMouseBtnDownEffect();
	void UpdateCheckMeetTarget(int dt);
	void UpdateCheckJumpMap(int dt);

	void clearGotoNpc();

	void SVisitNpcProcess(int64_t npcid, std::vector<int> servers, std::vector<int> quests);

	Nuclear::NuclearLocation GetLastMoveBeginning(){return m_iLastMoveBeginning;}	 //lua
	Nuclear::NuclearLocation GetLastMoveTarget(){return m_ptLastMoveTarget;}	 //lua

	void CalculatePartTarget();		//得到局部的目标点
	void ClearVerifyQueue(){m_NotVerifyQueue.clear();};
	bool ActiveMoveTo(Nuclear::NuclearLocation ptMoveTarget,int npdid = 0,bool trytofindagain = false,int64_t npckey = 0);	//主动寻路至某点 //lua
	
	bool MoveTo(Nuclear::NuclearLocation ptMoveTarget, bool bAgain = true);	//这里传入的参数是逻辑坐标 //lua
	void DealWhileMainCharacterMove();
	virtual void StopMove(); //lua

	void SendStopMoveMessage();
	
	bool CanActiveMoveTo();
	bool CanMove();
	bool CanTurn();
	void SendVerifyCommand(int now,int delta);
    
	void FlyToPos(int mapid, int xPos, int yPos, int npcid, int64_t npckey = 0, bool randompos = false, int nTargetPosX=0, int nTargetPosY=0, bool forcefly = false);
	void WalkToPos(int xPos, int yPos, int64_t nNpcKey = 0 ,int nNpcId = 0);
	bool OnFlyWalkToVisitNpc(Npc* pNpc);
	void FlyOrWarkToPos(int mapid, int xPos, int yPos, int npcid); //lua
	void ResetLastMoveTarget(){m_ptLastMoveTarget = GetLogicLocation();} //lua
	void RunPacingEvent();

	void clearGoTo() { m_MapWalker.ClearGoTo(); }
	MapWalker* GetMapWalker() { return &m_MapWalker; }
	virtual void SetTeamInfo(int64_t teamid, int teamindex, int teamstate);

	Nuclear::NuclearLocation ReCalculateTarget(Nuclear::NuclearLocation& ptMoveTarget,bool bHighlevel=false);
    
    void RefreshRoleInfoOfThisServer();
	void CheckGoto();
	bool OnFlyWalkToVisitPos(int64_t nNpcKey, int nNpcId);
	void SetGotoTargetPos(int nXPos, int nYPos);
	void SetGotoTargetPosType(int nType);
	int GetGotoTargetPosType(){ return m_nTargetPosType; }
	int GetDistanceWithPos(int nPosX, int nPosy);
	void SetJumpMapEnd(bool bEnd){ m_bJumpEnd = bEnd; }
	bool GetJumpMapEnd(){ return m_bJumpEnd; }

	void ReadyToPacing();
	void StopPacingMove();
	void SetMoveState(eMoveState e);
	bool IsReadyToPacing(){return m_bReadyToPacing;}
	void ClearReadyToPacing();
	eMoveState	GetMoveState(){return m_eMoveState;}
	void SetRandomPacing();
	
	void ContinueMove();//出战斗后得继续走

	void HandleReturnTeam(bool findpathfailure=false);		//处理回归队伍
	bool HandleVisiteNpc();			//处理访问npc

	bool JumpMap(int mapId);

	bool GetIsSending();

	void SetIsSending(bool);

	bool IsPathEmpty();				//是否路径已经为空

	virtual	void SetAttribute(int64_t roleid, int sex, int shape, int camp);
	virtual void InitPosition(Nuclear::NuclearLocation loc,bool bhighlevel = false);
	bool OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);
    
	bool OnLBButtonDown( WPARAM wParam, LPARAM lParam);
	bool TalkCursorOperate(SceneObject* pMouseOverObjects);	//访问npc操作
	bool PickUpCursorOperate(SceneObject* pMouseOverObjects);	//拾取操作

	bool TalkCursorOperateWithNpcBaseID(int baseNpcID);
		
	bool OnVisitNpc(Npc* pNpc);
    bool OnVisitFossickNpc(Npc* pNpc);
	
	virtual void UpdateAction(Nuclear::XPUSA_TYPE type);
	void Run(int now,int delta);

	void OnBattleBegin();
	void OnBattleEnd();
	virtual void Draw(int now);
	virtual bool CanBeGiven(){return false;}
	virtual bool CanTradeWith(){return false;}
	virtual bool CanJoinTeam(){return true;}
	virtual bool CanGiveFlower(){return false;}
	virtual bool CanGiveRose(){return false;}

	void HandleEnterOrLeavePVPArea();

	void HideRole(bool hide);
	bool IsHide(){return m_bHide;}
	
	//离队或暂离时给服务器发送重新定位消息
	void RelocateMainRolePos();

	void AddAutoWalkingEffect();
	void RemoveAutoWalkingEffect();

	void AddFlyWalkingEffect(std::wstring EffectName);
	void DelFlyWalkingEffect();

	void SetNeedToIslandTipsFlag(bool b){ m_bNeedToIslandTips = b; }

	bool IsInPVPArea(){ return m_bPVPArea; } //lua
	void SetInPVPArea(bool b){ m_bPVPArea = b; } //lua

	std::vector<Nuclear::NuclearPoint> GetAstarPath();

	void SetCurItemkey(int key);
	int GetCurItemkey();

	void SetFlyToEnable(bool enable){ m_bflyToEnable = enable; }
	bool GetFlyToEnable(){ return m_bflyToEnable; }

	void SetSuperMapTargetPos(Nuclear::NuclearLocation loc){ superMapPos = loc; } // 高级藏宝图目标地点
	Nuclear::NuclearLocation GetSuperMapTargetPos(){ return superMapPos; }

	void ReturnSchool();
	void GetSchoolMapIdAndPos(int* pMapId, int* pPosX, int* pPosY);

	static void sPurgeData();

	void StartAutoMove(int AutoMovePathID);
	void StopAutoMove();
	void SendAutoMovePathID(int WayID, int PathID, int PoindID);

	std::wstring GetTitle(); //lua

	virtual int GetShapeID(){ return m_data.shape; } //lua
	virtual void SetShapeID(int shapeid); //lua

	void DrawBackByService(Nuclear::NuclearLocation loc);

private:
	//找到start->end的向量上，与start可达的第一个点
	Nuclear::NuclearLocation FindFirstReachablePoint(Nuclear::NuclearLocation& pStart,Nuclear::NuclearLocation& pEnd,bool bHighlevel = false);
	//找到start->end的向量上，与start最近的第一个阻挡点
	Nuclear::NuclearLocation FindFirstBlockPoint(Nuclear::NuclearLocation& pStart,Nuclear::NuclearLocation& pEnd);

	bool FindReachablePointNearby(const Nuclear::NuclearLocation& start, const Nuclear::NuclearLocation& end, Nuclear::NuclearLocation& foundPos);
	void RemoveLastSelectedNpc();

private:
	Nuclear::NuclearLocation		m_ptLastMoveTarget;		//上一次移动的目标点	逻辑坐标
	Nuclear::NuclearLocation		m_ptLastMoveStart;		//上一次移动的起始点	逻辑坐标
	int								m_iLastVerifyTime;		//上一次轨迹验证时间
	int								m_iAreaCheckCoolDown;	//人物移动进行区域检测的时间	定为 2s吧
	astar::Path						m_path;
	astar::Path						m_pathSaved;			//yanji 20150824 画出自动寻路用
	std::list<fire::pb::move::Pos>	m_NotVerifyQueue;

	Nuclear::NuclearLocation		m_PartTarget;			//局部目标点，发给服务器的是局部目标点

	Nuclear::NuclearLocation		m_iLastMoveBeginning;	//移动起始点，用于测试被拉回特用

private:
	struct sAutoMovePathPointData
	{
		int m_PointID;
		int m_PathID;
		int m_SceneID;
		int m_CameraZ;
		int m_Speed;
		std::wstring m_RideName;
		std::wstring m_EffectName;
		int m_PointX;
		int m_PointY;
		int m_PointZ;
		sAutoMovePathPointData()
		{
			m_PointID = 0;
			m_PathID = 0;
			m_SceneID = 0;
			m_CameraZ = 0;
			m_Speed = 0;
			m_PointX = 0;
			m_PointY = 0;
			m_PointZ = 0;
		}
	};
	int m_AutoMovePathID;
	int m_AutoMovePathPointIndex;
	std::vector<sAutoMovePathPointData> m_AutoMovePathPointArr;
	std::wstring m_RideNameLast;

	Nuclear::NuclearLocation m_LastPos;
	int m_FlyStopTime;

	std::vector<CEGUI::String> m_HideWindowArr;

	bool	m_bPVPArea;
	bool	m_bHide;

private:
	DWORD					m_dwLButtonDownTime;
	bool					m_bLButtonDown;

	bool					m_bCanMove;   // 用于死亡提醒时 不让玩家移动

	int						m_gotoTargetMapId;		//将要去访问的mapId
	int						m_gotoVisitNpcID;		//将要去访问的npcID
	int64_t					m_gotoVisitNpcKey;		//将要去访问的npckey	因为采集出的bug弥补一下
	int64_t					m_gotoPickUpItemID;		//将要去拾取的物品ID
	Nuclear::NuclearLocation       m_gotoTargetLocation; //将要去访问的地点

	int64_t                 m_lastTalkNpcID;

	int m_iPacingWithAvoidCreepBuffTipsTime;	//提示间隔为5s

	bool m_bJumpToIslandTips;	//标志位，寻路至孤岛时，在寻到孤岛边缘给出“跳过去”的提示
	bool m_bNeedToIslandTips;

	int m_iCurItemkey;
	Nuclear::NuclearLocation superMapPos;

	bool m_bflyToEnable;
	float m_fFlyToCd;
	float m_fFlyToCdDt;
	bool m_bLockGoto;

	bool m_bIsSending;  // 跳转地图中

	eMoveState	m_eMoveState;
	bool		m_bReadyToPacing;
	bool        m_bJustFlyGoto;
	int			m_nTargetPosType;
	bool		m_bJumpEnd;

	Nuclear::IEffect*	m_pAutoWalkingEffect;	//自动寻路effect
	Nuclear::IEffect*	m_pPaceWalkingEffect;	//来回踱步effect
	Nuclear::IEffect*	m_pFlyWalkingEffect;	//巡游effect
};

inline MainCharacter* GetMainCharacter()
{
	return MainCharacter::GetInstance();
}
