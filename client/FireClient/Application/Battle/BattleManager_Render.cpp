//  BattleManager_Render.c
//  FireClient

#include "stdafx.h"
#include "BattleManager.h"
#include "Battler.h"

void BattleManager::ChangeBattleGround(const int& groundid)
{
	m_iBattleGroundid = groundid;
	int configid = GetTableNumberFromLua("battle.cbattlebackground", groundid, "id");
	if (configid != -1)
	{
		Nuclear::WarBackgroundHandle changehandle = Nuclear::GetEngine()->GetWorld()->LoadWarBackgound(GetTableWStringFromLua("battle.cbattlebackground", groundid, "path"));
		if (changehandle != Nuclear::INVALID_WAR_BACKGROUND_HANDLE
			&& m_hCurrentBattleGround != changehandle)
		{
			if (m_hCurrentBattleGround != Nuclear::INVALID_WAR_BACKGROUND_HANDLE)
				Nuclear::GetEngine()->GetWorld()->FreeWarBackgroundHandle(m_hCurrentBattleGround);

			m_hCurrentBattleGround = changehandle;
			Nuclear::GetEngine()->GetWorld()->SetWarBackground(m_hCurrentBattleGround, Nuclear::XPWBT_SCALE);
		}
	}
}
void BattleManager::ResetBattleGround(bool changedisplaymode)
{
    if (m_hCurrentBattleGround != Nuclear::INVALID_WAR_BACKGROUND_HANDLE)
    {
        Nuclear::GetEngine()->GetWorld()->FreeWarBackgroundHandle(m_hCurrentBattleGround);
        m_hCurrentBattleGround = Nuclear::INVALID_WAR_BACKGROUND_HANDLE;
    }
    
    return;
}

bool BattleManager::OnBeforeRender(int now)
{
	for_each(m_listBattler.begin(), m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::OnBeforeRender), now));
	return true;
}
void BattleManager::DrawUnderUI(int now)
{
	for (BattlerList::iterator it = m_listBattler.begin(); it != m_listBattler.end(); ++it)
	{
		Battler* pBattle = *it;
		eSceneObjType objType = pBattle->GetSceneObjectType();
		if (objType == eSceneObjNpc || objType == eSceneObjFollowNpc || objType == eSceneObjTaskShowNpc || objType == eSceneObjClientNpc)
		{
			pBattle->Draw(now);
		}
	}
}
void BattleManager::Draw(int now)
{
	switch(m_eBattleState)
	{
        case eBattleState_OperateChar:
        case eBattleState_OperatePet:
        case eBattleState_DemoOperate:
		{
		}
            break;
        case eBattleState_Wait:		
		{
            cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.SetOperateTime", -1);
		}
            break;
        case eBattleState_Demo:		
        {
			if (IsInReplayModel() == true)
			{
			}
			else
			{
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.SetOperateTime", -2);
			}
        }
            break;
        default:
            break;
	}
}

