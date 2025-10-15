#ifndef __GAMECURSOR_H
#define __GAMECURSOR_H

#include "Singleton.hpp"
enum eGameCursorType
{
	eGameCursorType_Normal			= 1,	//普通
	eGameCursorType_Attack			= 2,	//攻击或者观战
	eGameCursorType_Team				= 3,	//组队
	eGameCursorType_Trade			= 4,	//交易
	eGameCursorType_Freemove			= 5,	//自由移动
	eGameCursorType_Talk				= 6,	//与NPC对话
	eGameCursorType_Given			= 7,	//给予
	eGameCursorType_Pickup			= 8,	//拾取
	//以下是战斗中
	eGameCursorType_BattleNormal		= 9,
	eGameCursorType_BattleAttack		= 10,
	eGameCursorType_BattleSkill		= 11,
	eGameCursorType_BattleCatch		= 12,
	eGameCursorType_BattleProtect	= 13,
	eGameCursorType_BattleItem		= 14,
	eGameCursorType_BattleDart		= 15,	//战斗内飞镖
	//禁止样式
	eGameCursorType_Forbid			= 16,	//战斗外：组队，交易等，战斗内：选择技能，物品，保护，捕捉之后，禁止的样式	
	eGameCursorType_Flower			= 17,
	eGameCursorType_Repair			= 18,	//修理
	eGameCursorType_BattleCommand	= 19,	//指挥
	eGameCursorType_FBAttack			= 20,	
	eGameCursorType_FBRepair			= 21,	
	eGameCursorType_DragonCard		= 22,	
	eGameCursorType_SceneSkill		= 23,	//场景技能
	eGameCursorType_Pacing			= 24,	//自动寻路
	eGameCursorType_Firework			= 25,
	eGameCursorType_Identify			= 26,
	eGameCursorType_ChanZi			= 27,	
	eGameCursorType_Replace			= 28,
	eGameCursorType_Trigger			= 29,	//点击npc触发
	eGameCursorType_SupplyFlag		= 30,
	eGameCursorType_TriggerMagnifier	= 31,	//点击npc触发放大镜
	eGameCursorType_TriggerBrush		= 32,	//点击npc触发刷子
	eGameCursorType_BreakChest		= 33,
	eGameCursorType_Max,
};

enum eCursorState
{
	eCursorState_None,
	eCursorState_Attack,		//攻击
	eCursorState_Team,		//组队
	eCursorState_Trade,		//交易
	eCursorState_Given,		//给予
	eCursorState_SceneSkill,	//场景中使用技能
	eCursorState_GiveFlower,
	eCursorState_GiveRose,
	//以下是战斗内的
	eCursorState_Skill,	
	eCursorState_Item,
	eCursorState_Dart,
	eCursorState_Protect,
	eCursorState_Catch,
	eCursorState_Command,	//指挥
	eCursorState_DragonCard,
	eCursorState_Max
};

class GameOperateState : public CSingleton<GameOperateState>
{
public:
	GameOperateState();
	~GameOperateState();

	void ChangeCursorToDragonItemCard(int itemcardtype);
	void ChangeCursorToBattleCommand(std::wstring commandcontent);
	int  GetSceneSkillID(){ return m_iSceneSkillID; }
	void SetSceneSkillID(int id){ m_iSceneSkillID = id; }

	int	 GetDragonItemCardType(){ return m_iDragonItemCard; }
	std::wstring GetBattleCommandString(){ return m_BattleCommandString; }
	void ClearUserData(){ m_pUserData = NULL; }
	void*GetUserData(){ return m_pUserData; }
	void ChangeGameCursorWithUserDate(eGameCursorType type, void* pData = NULL);
	bool IsBattleOperateState();
	void SetOperateState(eCursorState e);
	eCursorState	GetOperateState(){ return m_eCursorState; }

	bool ResetBattleCursor();
	bool ResetCursor();
	void ChangeGameCursorType(eGameCursorType type, bool forceforbid = false);
	eGameCursorType	GetGameCursorType(){ return m_eGameCursorType; }

private:
	eGameCursorType				m_eGameCursorType;
	eCursorState				m_eCursorState;

	void*						m_pUserData;
	std::wstring				m_BattleCommandString;
	
	int							m_iDragonItemCard;
	int							m_iSceneSkillID;
};

inline GameOperateState* gGetGameOperateState()
{
	return GameOperateState::GetInstance();
}
#endif
