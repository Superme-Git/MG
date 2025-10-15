/***********************************************************************
filename: 	SceneNpc.h
purpose:	景物NPC，客户端控制
*************************************************************************/
#pragma once

#include "SceneObject.h"
#include "SceneCommon.h"
#include "Singleton.hpp"
#include "GameTable/npc/CSceneNPCConfig.h"
#include "GameTable/npc/CSceneNPCBase.h"
#define X_DISTANCE	36
#define Y_DISTANCE	36

//x坐标差在36个格子内，y坐标差在36个格子内
inline bool gNpcInScreen(const Nuclear::NuclearLocation& pt1,const Nuclear::NuclearLocation& pt2)
{
	return abs(pt1.x - pt2.x) <= X_DISTANCE*g_logicGrid && abs(pt1.y - pt2.y) <= Y_DISTANCE*g_logicGrid;
}

class SceneNpc : public SceneObject
{
public:
	SceneNpc();
	virtual ~SceneNpc();

	int	m_SceneNpcID;
	GameTable::npc::CSceneNPCConfig	m_SceneNpcConfig;
	GameTable::npc::CSceneNPCBase	m_SceneNpcBase;

	Nuclear::NuclearLocation	m_ptInitLocation;

public:
	void IntervalTalkRun(int delta);	//这个函数在run里触发

	void NearbyTalkRun();				//这个函数每隔3s触发一次

	void CharacterGatherTalkRun();		//这个函数也每隔3s触发一次

	bool SetAttribute(int npcid);
	void SetArea1Color(int colorID);

	virtual int64_t GetID(){return static_cast<int64_t>(m_SceneNpcID);}
	virtual std::wstring GetName();

	virtual int GetShapeID(){return m_SceneNpcBase.shapeid;}

	virtual void Run(int now,int delta);
	virtual void Draw(int now);

	void	RunRandom() { }
	void	UpdateAction(Nuclear::XPUSA_TYPE type);

	virtual bool OnSetCursor();
	virtual bool CanBeVisited();

	void RandomWalk();//场景npc的一些行为

	int GetNpcTypeID(){return m_SceneNpcBase.type;}
	int GetNpcBaseID(){return m_SceneNpcBase.id;}

private:
	void randomTalk(std::vector<int>& talklist);	//场景npc说话

private:
	int64_t	m_iAppearTime;
	int64_t m_iDisappearTime;

	int	m_iTalkInterval;
	std::vector<int>	m_TalkIntervalList;

	int m_iTalkCaseTime;				//用于触发NearbyTalkRun和CharacterGatherTalkRun 3000s

	bool m_bTalkNearby;
	std::vector<int>	m_TalkNearbyList;

	bool m_bTalkCharacterGather;
	std::vector<int>	m_TalkCharacterGatherList;
};

//景物npc管理器
class SceneNpcManager : public CSingleton<SceneNpcManager>
{
public:
	SceneNpcManager();
	~SceneNpcManager();

public:
	void UpdateSceneNpc(const Nuclear::NuclearLocation& maincharLoc);//当角色坐标改变时，刷新所谓屏幕内的SceneNpc

	void AddSceneNpc(int npcid);
	void RemoveSceneNpc(int npcid);

	void ClearSceneNpc();		//跳转地图时删除景物npc

	void InitSceneNpc(int mapid);

	void HideOrShowSceneNpc(bool bVis); //进入或退出剧情动画时要隐藏或显示景物npc

	void Run(int now,int delta);
	void Draw(int now);

	SceneObject* FindSceneObjectBySprite(Nuclear::ISprite * sprite);
	SceneNpc* FindSceneNpcByID(int64_t npcid);

private:
	std::list<int>			m_MapSceneNpcIDList;		//本地图所有的景物NPC的id列表
	std::map<int, SceneNpc*>	m_SceneNpcMap;
};

inline SceneNpcManager* GetSceneNpcManager()
{
	return SceneNpcManager::GetInstance();
}
