//  BattleManager_Watch.c
//  FireClient

#include "stdafx.h"
#include "BattleManager.h"
#include "rpcgen/fire/pb/battle/BattleType.hpp"
#include "fire/pb/battle/CEndWatchBattle.hpp"
#include "GameApplication.h"
#include "NetConnection.h"

void BattleManager::AddExitWatchBtn(int iBattleID)
{
	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();

	CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
	m_pExitWatchBtn = static_cast <CEGUI::PushButton*>(winMgr.createWindow("TaharezLook/ImageButton",OnlyName));
	if (m_pExitWatchBtn)
	{
		m_pExitWatchBtn->setAlwaysOnTop(true);
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow!=NULL)
		{
			pRootWindow->addChildWindow(m_pExitWatchBtn);
			m_pExitWatchBtn->setSize(CEGUI::UVector2(cegui_absdim(86),cegui_absdim(95)));
			m_pExitWatchBtn->setPosition(CEGUI::UVector2(CEGUI::UDim(1, -96), CEGUI::UDim(1, -105)));
			m_pExitWatchBtn->subscribeEvent(CEGUI::PushButton::EventClicked, CEGUI::Event::Subscriber(&BattleManager::HandleExitWatch, this));

			const char* pNormal = "set:fightui image:quxiao";
			const char* pPushed = "set:fightui image:quxiao";

			m_pExitWatchBtn->setProperty("NormalImage", pNormal);
			m_pExitWatchBtn->setProperty("PushedImage", pPushed);
		}
	}
}
void BattleManager::BeginWatchScene(int leftcount,int battletype,int roundnum,int background,int backmusic, int64_t battlekey)
{
    m_bWatchModel = true;
	BeginBattleScene(battletype, roundnum, -1, background, backmusic, true);
	switch(battletype)
	{
        case fire::pb::battle::BattleType::BATTLE_PVE:	
        case fire::pb::battle::BattleType::BATTLE_PVP:	
		{
			m_bShowHpBar = true;
		};
        case fire::pb::battle::BattleType::BATTLE_LIVEDIE:	
        case fire::pb::battle::BattleType::BATTLE_DUEL_SINGLE:
        case fire::pb::battle::BattleType::BATTLE_DUEL_TEAM :
		{
			m_bShowHpBar = false;
		}
        default:
		{
			m_bShowHpBar = true;
		}
            break;
	}
    AddExitWatchBtn(-1);
}

void BattleManager::RemoveExitWatchBtn()
{
	if (m_pExitWatchBtn)
	{
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow!=NULL)
			pRootWindow->removeChildWindow(m_pExitWatchBtn);
		m_pExitWatchBtn = NULL;
	}
}
void BattleManager::RequestLeaveBattle()
{
	if (m_bWatchModel && gGetNetConnection())
	{
		fire::pb::battle::CEndWatchBattle EndWatchBattle;
		gGetNetConnection()->send(EndWatchBattle);
	}
}
bool BattleManager::HandleExitWatch(const CEGUI::EventArgs& e)
{
	RequestLeaveBattle();
	return true;
}
