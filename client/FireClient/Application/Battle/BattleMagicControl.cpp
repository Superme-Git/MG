#include "stdafx.h"
#include "BattleMagicControl.h"
#include "BattleManager.h"
#include "Battler.h"
#include "MainRoleDataManager.h"

#include "GameApplication.h"

BattleMagicControl::BattleMagicControl()
{
	m_pNewBattleList.clear();
}
BattleMagicControl::~BattleMagicControl()
{
}
bool BattleManager::IsInBattle()
{
	return ((m_eBattleState != eBattleState_Null) || gGetGameApplication()->IsInLittleGame());
}

Battler* BattleMagicControl::FindBattlerByID(int battleID)
{
	if (GetBattleManager())
	{
		return GetBattleManager()->FindBattlerByID(battleID);
	}
	else
		return NULL;
}
int BattleMagicControl::GetRoundNum()
{
	if (GetBattleManager())
	{
		return GetBattleManager()->GetRoundCount();
	}
	else
		return 0;
}

void BattleMagicControl::AddNewBattler(bool bShow)
{
	for (NewBattlerList::iterator iter = m_pNewBattleList.begin();iter != m_pNewBattleList.end();iter++)
	{
		GetBattleManager()->AddBattler((*iter));
		Battler* pNewBattler = GetBattleMagicControl()->FindBattlerByID((*iter)->iBattleID);
		if (pNewBattler)
		{
			if (bShow)
				pNewBattler->SetAlpha(0);
			else
				pNewBattler->SetNormalAlpha(0, true);
			pNewBattler->m_bShowName = false;
		}
	}
}
void BattleMagicControl::DeleteAllBattleMagic()
{
	for (NewBattlerList::iterator iter = m_pNewBattleList.begin();iter != m_pNewBattleList.end();iter++)
	{
		delete (*iter);
		(*iter) = NULL;
	}
	m_pNewBattleList.clear();
}
void BattleMagicControl::DeleteRemoveBattler()
{
	for (std::list<int>::iterator it=m_RemoveBattlerList.begin();it!=m_RemoveBattlerList.end();it++)
	{
		Battler* pBattler = GetBattleMagicControl()->FindBattlerByID((*it));
		if (pBattler)
		{
			Nuclear::NuclearLocation ptDisapper = pBattler->GetLocation();
			Nuclear::GetEngine()->GetWorld()->PlayEffect(MHSD_UTILS::GetEffectPath(10235),Nuclear::XPEL_BATTLE_MID,ptDisapper.x,ptDisapper.y,1,false,2);
			pBattler->SetDisappear(true);
		}
	}
	m_RemoveBattlerList.clear();
}
void BattleMagicControl::ShowNewBattler()
{
	for (NewBattlerList::iterator iter = m_pNewBattleList.begin();iter != m_pNewBattleList.end();)
	{
		Battler* pNewBattler = GetBattleMagicControl()->FindBattlerByID((*iter)->iBattleID);
		if (pNewBattler)
		{
			pNewBattler->SetAlpha(0);
			pNewBattler->SetNormalAlpha(255,false);
			pNewBattler->ShowHpBar(true);
			if (GetBattleManager()
			 && GetBattleManager()->GetMainBattlePet() == pNewBattler
			 && GetBattleManager()->GetSummonDemoPetKey() != 0)
			{
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainRoleData_SetBattlePet", GetBattleManager()->GetSummonDemoPetKey());
				GetBattleManager()->SetSummonDemoPetKey(0);
			}
		}
	
		delete (*iter);
		(*iter) = NULL;
		iter = m_pNewBattleList.erase(iter);
	}
	m_pNewBattleList.clear();
}
