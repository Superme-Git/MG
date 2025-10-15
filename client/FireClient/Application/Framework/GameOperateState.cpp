#include "stdafx.h"
#include "GameOperateState.h"
#include "GameUIManager.h"
#include "MainCharacter.h"

#include "BattleManager.h"

GameOperateState::GameOperateState()
: m_eGameCursorType(eGameCursorType_Normal)
, m_eCursorState(eCursorState_None)
, m_pUserData(NULL)
, m_BattleCommandString(L"")
, m_iDragonItemCard(0)
, m_iSceneSkillID(0)
{

}

GameOperateState::~GameOperateState()
{
	m_pUserData = NULL;
}

bool GameOperateState::IsBattleOperateState()
{
	return m_eCursorState == eCursorState_Skill || m_eCursorState == eCursorState_Item|| m_eCursorState == eCursorState_Protect || m_eCursorState == eCursorState_Catch || m_eCursorState == eCursorState_Dart;
}

//进入指挥状态
void GameOperateState::ChangeCursorToBattleCommand(std::wstring commandcontent)
{
	ChangeGameCursorType(eGameCursorType_Forbid);
	SetOperateState(eCursorState_Command);
	m_BattleCommandString = commandcontent;
}

void GameOperateState::ChangeCursorToDragonItemCard(int itemcardtype)
{
	ChangeGameCursorType(eGameCursorType_Forbid);
	SetOperateState(eCursorState_DragonCard);
	m_iDragonItemCard = itemcardtype;
}

//更改鼠标样式
void GameOperateState::ChangeGameCursorType(eGameCursorType type, bool forceforbid)
{
	m_eGameCursorType = type;
	if (GetBattleManager())
	{
		GetBattleManager()->CheckPickBattler();
	}
}

//带用户参数的鼠标变换
void GameOperateState::ChangeGameCursorWithUserDate(eGameCursorType type, void* pData)
{
	ChangeGameCursorType(type);
	m_pUserData = pData;
}

//重置鼠标
bool GameOperateState::ResetCursor()
{
	if (GetMainCharacter() && GetMainCharacter()->IsReadyToPacing())
	{
		ChangeGameCursorType(eGameCursorType_Pacing);
	}
	else
	{
		ChangeGameCursorType(eGameCursorType_Normal);
	}
	SetOperateState(eCursorState_None);
	if (gGetGameUIManager() && !gGetGameUIManager()->IsGameUIVisible())
	{
		gGetGameUIManager()->SetGameUIVisible(true);
	}
	m_pUserData = NULL;
	m_iDragonItemCard = 0;
	m_iSceneSkillID = 0;
	return true;
}

//重置战斗鼠标操作
bool GameOperateState::ResetBattleCursor()
{
	if (m_eCursorState == eCursorState_Command)
	{
		m_BattleCommandString = L"";
	}
	ChangeGameCursorType(eGameCursorType_BattleNormal);
	SetOperateState(eCursorState_None);
	if (gGetGameUIManager() && !gGetGameUIManager()->IsGameUIVisible())
	{
		gGetGameUIManager()->SetGameUIVisible(true);
	}
	m_pUserData = NULL;
	return true;
}

//设置鼠标操作状态
void GameOperateState::SetOperateState(eCursorState e)
{
	m_eCursorState = e;
	if (m_eCursorState >= eCursorState_Attack &&  m_eCursorState <= eCursorState_Command && m_eCursorState != eCursorState_SceneSkill)
	{
		gGetGameUIManager()->SetGameUIVisible(false);
	}
	else if (m_eCursorState == eCursorState_None)
	{
		gGetGameUIManager()->SetGameUIVisible(true);
	}
}


