//  BattleScriptPlayer.cpp
//  FireClient
#include "stdafx.h"
#include "BattleScriptPlayer.h"
#include "SkillBuilder.h"
#include "BattleManager.h"
#include "GameApplication.h"
#include "fire/pb/battle/CSendRoundPlayEnd.hpp"
#include "GameTable/battle/CBattleAIConfig.h"
#include "GameUIManager.h"
#include "MainRoleDataManager.h"
#include "ArtTextManager.h"

BattleScriptPlayer g_BattleScriptPlayer;

#define AI_PLAYACTION_DELAY_B 100
#define AI_PLAYACTION_DELAY_F 0
#define AI_PLAYACTION_DELAY_F_LAST 500

BattleScriptPlayer::BattleScriptPlayer()
: m_iBeforeAITime(0)
, m_iAfterAITime(0)
, m_iDealBeforeOperAI(0)
, m_iRoundDemoIndex(0)
, m_iRoundOverDelayTime(0)
{
	
}

BattleScriptPlayer::~BattleScriptPlayer()
{
    
}
int BattleScriptPlayer::PlayReplayRound(int RoundIndex)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.ChangeBattleRound", RoundIndex + 1);
	return GetBattleManager()->SSendRoundScript_Process(m_RoundArr[RoundIndex]);
}

void BattleScriptPlayer::processDataChangeOnly(sBattleDemoScript& aScript)
{
    BattleManager* pMng = GetBattleManager();
    if(pMng)
    {
        Battler* pBtl = pMng->FindBattlerByID(aScript.stMagicCasting.AttackerID);
        if(pBtl)
        {
            pBtl->ProcessAttackAction(&aScript.stMagicCasting);
        }
        
        for (std::list<sBattleBuffResult>::const_iterator itor=aScript.stMagicCasting.BuffResultList.begin();itor!=aScript.stMagicCasting.BuffResultList.end();itor++)
        {
            Battler* pBuffBattler = pMng->FindBattlerByID(itor->battleid);
            if (pBuffBattler)
            {
                pBuffBattler->ProcessBuffChange(*itor);
            }
        }
        
		for (size_t i = 0; i < aScript.listResults.size(); i++)
        {
			for (stBattleDemoResultArr::iterator it = aScript.listResults[i].listResults.begin(); it != aScript.listResults[i].listResults.end(); it++)
			{
				if (!it->bgeneresult)
				{
					for (std::list<sBattleBuffResult>::const_iterator itor = it->BuffResultList.begin(); itor != it->BuffResultList.end(); itor++)
					{
						Battler* pBuffBattler = pMng->FindBattlerByID(itor->battleid);
						if (pBuffBattler)
						{
							pBuffBattler->ProcessBuffChange(*itor);
						}
					}
					if (it->ResultType != 1)
					{
						Battler* pTarget = pMng->FindBattlerByID(it->TargetID);
						if (pTarget)
						{
							pTarget->ProcessResultAction(it->eTargetResult, it->FlagType, 0, 2, it->HPChange, it->MPChange, it->MaxHPChange, it->SPChange, false, true, it->HPChangeGodBless);
							//pTarget->ProcessSpecialAction();
							if (it->ShapeChange != 0)
							{
								int mId = (it->ShapeChange == -1) ? 0 : it->ShapeChange;
								pTarget->ChangeSpriteModel(mId);
							}
							if (it->EPChange != 0)
								pTarget->SetEPValue(pTarget->GetEPValue() + it->EPChange);
						}
						//保护者触发吸血效果
						Battler* pProtecter = pMng->FindBattlerByID(it->ProtecterID);
						if (pProtecter)
						{
							pProtecter->ProcessResultAction(it->eProtecterResult, it->FlagType, 0, 2, it->ProtectHPChange, 0, it->ProtectMaxHPChange, 0, false, false, it->HPChangeGodBless);
						}
						Battler* pSender = pBtl;
						if (pSender)
						{
							if (it->StealHPEffect != 0)
							{
								pSender->ProcessResultAction(eBattleResult_HPChange, it->FlagType, 0, 2, it->StealHPEffect, 0);
							}
							if (it->StealMPEffect != 0)
							{
								pSender->ProcessResultAction(eBattleResult_MPChange, it->FlagType, 0, 2, 0, it->StealMPEffect);
							}
							if (it->ReturnHurtEffect != 0)
							{
								it->eAttackerResult |= eBattleResult_Hit;
								pSender->ProcessResultAction(it->eAttackerResult, it->FlagType, 0, 2, it->ReturnHurtEffect, 0, it->ReturnHurtMaxHP, 0, true, false, it->HPChangeGodBless);

								if (pProtecter)
									pProtecter->PopEffectText(eFanZhen);
								else if (pTarget)
									pTarget->PopEffectText(eFanZhen);
							}
						}
					}
					if (it->ResultType == 1)
					{
						Battler* pSender = pBtl;
						Battler* pTarget = pMng->FindBattlerByID(it->TargetID);
						if (it->AttackBackEffect != 0)
						{
							pSender->ProcessResultAction(it->eAttackerResult, it->FlagType, 0, 2, it->AttackBackEffect, 0, it->ReturnHurtMaxHP, 0, true, false, it->HPChangeGodBless);
						}
					}
					it->bgeneresult = true;
				}
			}
		}
    }
}

bool BattleScriptPlayer::IsSkillsOver()
{
	for (size_t i = 0; i < m_pCurSkills.size(); i++)
	{
		if (!m_pCurSkills[i]->IsComplete())
		{
			return false;
		}
	}
	return true;
}
bool BattleScriptPlayer::IsSkillsZero()
{
	if (m_pCurSkills.size() > 0)
	{
		return false;
	}
	return true;
}
void BattleScriptPlayer::DoBattleClear()
{
    DoRoundClear();
	ClearGroundEffect();
	ClearPlayEffect();
}
void BattleScriptPlayer::DoRoundClear()
{
	std::list<SkillStage*>::iterator it = m_listPersistStage.begin();
	for (it; it != m_listPersistStage.end(); it++)
	{
		delete (*it);
	}
	m_listPersistStage.clear();
	ClearSkills();
    m_iRoundDemoIndex = 0;
	for (ListBattleDemoScript::iterator iter = m_listBattleDemoScript.begin(); iter != m_listBattleDemoScript.end(); ++iter)
	{
		processDataChangeOnly(*iter);
		OnDemoEnd(*iter);
    }
    m_listBattleDemoScript.clear();
	m_listBattleDemoScriptBackup.clear();
    m_actiontime.clear();
    if (!GetBattleManager()->m_MainRoleAttributeRoundEnd.empty()) {
		CMainRoleDataManager::UpdateMainBattlerAttribute(GetBattleManager()->m_MainRoleAttributeRoundEnd);
        GetBattleManager()->m_MainRoleAttributeRoundEnd.clear();
    }
    
    if (!GetBattleManager()->m_MainPetAttributeRoundEnd.empty())
    {
		CMainRoleDataManager::UpdateMainPetAttribute(GetBattleManager()->m_MainPetAttributeRoundEnd);
        GetBattleManager()->m_MainPetAttributeRoundEnd.clear();
    }

	for (AIActionList::iterator itor = m_listAIAction.begin(); itor != m_listAIAction.end(); itor++)
	{
		delete (*itor);
		(*itor) = NULL;
	}
	m_listAIAction.clear();

	//清除所有幻影
	GetBattleManager()->ClearIllusionAll();
}
void BattleScriptPlayer::Play()
{
	ClearSkills();
    m_iLastTick = 0;
	if (GetBattleManager()->IsInReplayModel() == true)
	{
		m_iRoundOverDelayTime = 1100;
	}
	else
	{
		m_iRoundOverDelayTime = 100;// 500;
		if (GetBattleManager()->m_bLastRound)
		{
			m_iRoundOverDelayTime = AI_PLAYACTION_DELAY_F_LAST;
		}
	}
    m_bFinished = false;
}
void BattleScriptPlayer::Run(int now, int delta)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.SetOperateTime", -2);
    //ai 的延时
	int tempDelta = delta;
    if (m_iBeforeAITime > 0)
    {
        m_iBeforeAITime -= delta;
		if (m_iBeforeAITime > 0)
			return;
		tempDelta = -m_iBeforeAITime;
    }
    
    if (m_iAfterAITime > 0)
    {
		m_iAfterAITime -= delta;
		if (m_iAfterAITime > 0)
			return;
		tempDelta = -m_iAfterAITime;
    }
    
    if (!m_listPersistStage.empty())
	{
        SkillStage* pStage = m_listPersistStage.front();
        if (pStage)
		{
            pStage->Run(now, delta);
            if(pStage->m_bIsStageComplete)
            {
                m_listPersistStage.pop_front();
				delete pStage;
            }
        }
    }
    if (m_listBattleDemoScript.empty())
	{
		if (!m_listPersistStage.empty())
		{
			return;//还有没执行完的stage
		}

		if (m_bFinished)
		{
            return;
        }

		if (m_iRoundOverDelayTime > 0)
		{
			m_iRoundOverDelayTime -= delta;
			if (GetBattleManager()->m_bLastRound == false)
			{
				if (m_iRoundOverDelayTime >= 100)
				{
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.SetOperateTime", 30);
				}
				else
				{
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.SetOperateTime", 29);
				}
			}
			if (m_iRoundOverDelayTime > 0)
				return;
		}
		//check last hp		
		for (std::map<int, int>::iterator it = GetBattleManager()->m_fighterfinallyhps.begin(); it != GetBattleManager()->m_fighterfinallyhps.end(); it++)
		{
			Battler* pBattler = GetBattleManager()->FindBattlerByID(it->first);
			if (pBattler != NULL && pBattler->GetHPValue() != it->second)
			{
				pBattler->SetHPValue(it->second);
			}
		}
		if (GetBattleManager()->IsInReplayModel() == true)
		{
		}
		else
		{
			fire::pb::battle::CSendRoundPlayEnd SendRoundPlayEnd;
			std::list<int>::iterator iBegin = m_actiontime.begin();
			std::list<int>::iterator iEnd = m_actiontime.end();
			int totalTm = 0;
			for(std::list<int>::iterator it = iBegin; it != iEnd; it++)
			{
				totalTm += *it;
			}
			int endTime = Nuclear::GetMilliSeconds();
			endTime = endTime - m_actionStarttime;
			m_actiontime.push_back(endTime - totalTm);
			m_actiontime.push_back(endTime);
			SendRoundPlayEnd.actiontime = m_actiontime;
			gGetNetConnection()->send(SendRoundPlayEnd);
		}
        OnScriptFinished();
        m_bFinished = true;

		if (GetBattleManager()->IsInReplayModel() == true)
		{
			m_RoundIndex++;
			if (m_RoundIndex >= (int)m_RoundArr.size())
			{
				GetBattleManager()->RequestLeaveBattleReplay();
			}
			else
			{
				OnScriptFinished();
				PlayReplayRound(m_RoundIndex);
				return;
			}
		}

        return;
    }
    
    m_iLastTick += delta;
    
	if (IsSkillsZero())
    {
        m_iBeforeAITime = DealAIBeforeDemoExecute();
        if (!m_listBattleDemoScript.empty())
        {
			while( !m_listBattleDemoScript.empty() )
			{
				bool buildok = true;
				if (m_listBattleDemoScript.front().listResults.size() > 0)
				{
					for (size_t i = 0; i < m_listBattleDemoScript.front().listResults.size(); i++)
					{
						Skill* S = gBuildSkill(m_listBattleDemoScript.front(), i);
						if (S)
						{
							m_pCurSkills.push_back(S);
						}
						else
						{
							ClearSkills();
							m_listBattleDemoScript.pop_front();
							buildok = false;
							break;
						}
					}
				}
				else
				{
					m_listBattleDemoScript.pop_front();
					buildok = false;
				}
				if (buildok == true)
					break;
				m_iRoundDemoIndex++;
				m_iBeforeAITime = DealAIBeforeDemoExecute();
			}
		}
		if(m_iBeforeAITime > delta || m_listBattleDemoScript.empty())
		{
			OnDemoBegin();
			return;
		}	
    }
        
	for (size_t i = 0; i < m_pCurSkills.size(); i++)
	{
		if (i > 0)
		{
			if (m_pCurSkills[i]->m_BeginMode == 2)
			{
				if (m_pCurSkills[i - 1]->IsComplete())
				{
					m_pCurSkills[i]->m_BeginMode = 1;
				}
				else
				{
					continue;
				}
			}
		}
		m_pCurSkills[i]->Run(now, tempDelta);
	}    

	if (IsSkillsOver())
	{
		//下一个
		m_actiontime.push_back(m_iLastTick);
		m_iLastTick = 0;
		OnDemoEnd(m_listBattleDemoScript.front());
		ClearSkills();
		Battler* pBattler = GetBattleManager()->FindBattlerByID(m_listBattleDemoScript.front().stMagicCasting.AttackerID);
		if (pBattler)
		{
			pBattler->ClearPhantom();
			Nuclear::NuclearLocation loc = GetBattleManager()->GetBattleLocation(pBattler->GetBattleID());
			pBattler->SetTargetPos(loc.x, loc.y);
			pBattler->SetLocation(loc);
			pBattler->UpdateHpBarLocation();
			if(pBattler->IsDeathEx() == false)
			{
				pBattler->SetDefaultAction(eActionBattleStand);
				pBattler->GetSprite()->SetHoldLastFrame(false);
			}

			pBattler->ProcessAttackAction(&m_listBattleDemoScript.front().stMagicCasting);
			//只消耗一次
			m_listBattleDemoScript.front().stMagicCasting.HPConsume = 0;
			m_listBattleDemoScript.front().stMagicCasting.MPConsume = 0;
			m_listBattleDemoScript.front().stMagicCasting.SPConsume = 0;
		}
		m_listBattleDemoScript.pop_front();
		m_iAfterAITime = DealAIAfterDemoExecute();
		return;
	}
    return;
}
void BattleScriptPlayer::OnDemoBegin()
{
    
}
void BattleScriptPlayer::OnDemoEnd(sBattleDemoScript& script)
{
    if (!m_listBattleDemoScript.empty())
    {        
        sBattleDemoAttack* pAttack = &script.stMagicCasting;
		for (std::list<sBattleBuffResult>::const_iterator itor=pAttack->BuffResultList.begin();itor!=pAttack->BuffResultList.end();itor++)
		{
			Battler* pBuffBattler = GetBattleManager()->FindBattlerByID(itor->battleid);
			if (pBuffBattler)
			{
				pBuffBattler->ProcessBuffChange(*itor);
			}
		}
        
        if (!script.RoleAttribute.empty())
        {
            std::map<int,int>::iterator it = script.RoleAttribute.find(fire::pb::attr::AttrType::SP);
            if (it != script.RoleAttribute.end())
            {
				int spchange = script.RoleAttribute[fire::pb::attr::AttrType::SP] - GetMainRoleDataAttr(fire::pb::attr::AttrType::SP);
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainRoleData_UpdateSpChange", spchange);
				script.stMagicCasting.SPConsume = 0;
            }
			CMainRoleDataManager::UpdateMainBattlerAttribute(script.RoleAttribute);
        }
        if (!script.PetAttribute.empty())
        {
			CMainRoleDataManager::UpdateMainPetAttribute(script.PetAttribute);
        }
    }
}
void BattleScriptPlayer::OnScriptFinished()
{
    DoRoundClear();
}
void BattleScriptPlayer::DealAIAction(sBattleAIAction* pAction)
{
	const GameTable::battle::CBattleAIConfig& BattleAI = GameTable::battle::GetCBattleAIConfigTableInstance().getRecorder(pAction->AIActionID);
	Battler* pAIExecutor = GetBattleManager()->FindBattlerByID(pAction->BattlerID);
	if (BattleAI.id != -1)
	{
		if (BattleAI.talkinfo != L"" && pAIExecutor)	//说话信息
		{
			std::wstring talkstr;
			if (BattleAI.talkinfo.find(L"<T") == std::wstring::npos)
			{
				std::wstringstream stream;
				stream << L"<T t=\""<<BattleAI.talkinfo<<L"\" c=\"FF693F00\"></T>";                
				pAIExecutor->AddTalk(stream.str(), false);
				talkstr = stream.str();
			}
			else
			{
				talkstr = BattleAI.talkinfo;
				pAIExecutor->AddTalk(BattleAI.talkinfo,false);
			}
			if (BattleAI.talkshow != 0)
			{
				if (pAIExecutor != NULL)
				{
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(BattleAI.talkshow);		//频道
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(1/*pAIExecutor->GetID()*/);				//ID
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(pAIExecutor->GetShapeID());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(pAIExecutor->GetTitleID());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(0);
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(pAIExecutor->GetName()).c_str());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(talkstr).c_str());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatManager.AddMsg", 7);
				}
			}
		}
		if (BattleAI.tipsinfo != L"")					//提示框信息
		{
			//gGetGameUIManager()->AddMessageTip(BattleAI.tipsinfo);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(BattleAI.tipsinfo).c_str());
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.AddMessageTip_", 1);
		}
		if (BattleAI.appearchange != 0 && pAIExecutor)//换造型
		{	
			pAIExecutor->ChangeSpriteModel(BattleAI.appearchange);
		}
		if (BattleAI.changeground != 0)
		{
			GetBattleManager()->ChangeBattleGround(BattleAI.changeground);
		}
		if (BattleAI.playsound != 0)
		{
			GetBattleManager()->PlayBattleSound(BattleAI.playsound);
		}
		if (BattleAI.playeffect != L"" && pAIExecutor)
		{
			GetBattleManager()->PlayAIEffect(BattleAI.playeffect,pAIExecutor);
		}
		if (BattleAI.speak != L"" && pAIExecutor)
		{
			std::wstring speak;
			int mID = pAIExecutor->GetModelID();
			if (1010101 <= mID && mID <= 1010110)
			{
				std::wstring sexfile = BattleAI.speak;
				int sex = mID % 2;
				if (sex == 1)
					sexfile = Nuclear::replace(sexfile, L".", L"m.");
				else if (sex == 0)
					sexfile = Nuclear::replace(sexfile, L".", L"w.");
				GetBattleManager()->PlayAISpeak(sexfile, pAIExecutor);
				speak = sexfile;
			}
			else
			{
				speak = BattleAI.speak;
				GetBattleManager()->PlayAISpeak(BattleAI.speak, pAIExecutor);
			}
			if (BattleAI.speakshow != 0)
			{
				if (pAIExecutor != NULL)
				{
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(BattleAI.speakshow);		//频道
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(1/*pAIExecutor->GetID()*/);				//ID
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(pAIExecutor->GetShapeID());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(pAIExecutor->GetTitleID());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(0);
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(pAIExecutor->GetName()).c_str());

					std::wstring talkstr;
					if (BattleAI.talkinfo.find(L"<T") == std::wstring::npos)
					{
						std::wstringstream stream;
						stream << L"<T t=\"" << BattleAI.talkinfo << L"\" c=\"FF693F00\"></T>";
						talkstr = stream.str();
					}
					else
					{
						talkstr = BattleAI.talkinfo;
					}
					std::wstring stime = BattleAI.speaktime;
					if (stime == L"")
						stime = L"5";
					std::wstring sinfo = L"file=\"" + speak + L"\" text=\""+ talkstr + L"&\" time=" + stime;
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(sinfo).c_str());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatManager.AddMsg", 7);


					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(BattleAI.speakshow);
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(1);
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(pAIExecutor->GetShapeID());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(pAIExecutor->GetTitleID());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(0);
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(pAIExecutor->GetName()).c_str());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(talkstr).c_str());
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(-1);
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatOutBoxOperatelDlg.AddChatMsg_", 8);
				}
			}
		}
	}
}
void BattleScriptPlayer::DealAIActionWhileBattlerDie(int battleID)
{
	if (m_listAIAction.empty())
	{
		return;
	}
	for (AIActionList::iterator it = m_listAIAction.begin();it != m_listAIAction.end();)
	{
		if ((*it)->DemoIndex == m_iRoundDemoIndex && (*it)->ExecuteTime == battleID)//battleID表示单位死亡时触发ID
		{
			DealAIAction((*it));
			delete (*it);
			it = m_listAIAction.erase(it);
		}
		else
		{
			it++;
		}
	}
}
int BattleScriptPlayer::DealAIBeforeDemoExecute()
{
    int iDealAIBeforeDemo = AI_PLAYACTION_DELAY_B;
	if (!GetBattleManager()->IsInBattleDemo())
	{
		GetBattleManager()->SetBattleState(eBattleState_Demo);
		GetBattleManager()->FinishBattleOperate();
	}
    
	if (m_listAIAction.empty())
	{
		return iDealAIBeforeDemo;
	}
	for (AIActionList::iterator it = m_listAIAction.begin();it != m_listAIAction.end();)
	{
		if ((*it)->DemoIndex == m_iRoundDemoIndex && (*it)->ExecuteTime == -1)	//-1表示回合前
		{
			DealAIAction((*it));
			iDealAIBeforeDemo = AI_PLAYACTION_DELAY_B;	//有AI的话延时0.4s再播Demo
			delete (*it);
			it = m_listAIAction.erase(it);
		}
		else
		{
			it++;
		}
	}
	return iDealAIBeforeDemo;
}
int BattleScriptPlayer::DealAIAfterDemoExecute()
{
	if (!GetBattleManager()->IsInBattleDemo())
	{
		GetBattleManager()->SetBattleState(eBattleState_Demo);
		GetBattleManager()->FinishBattleOperate();
	}
	int iDealAIAfterDemo = AI_PLAYACTION_DELAY_F;
	if (m_listAIAction.empty())
	{
        m_iRoundDemoIndex++;
		return iDealAIAfterDemo;
	}
	for (AIActionList::iterator it = m_listAIAction.begin();it != m_listAIAction.end();)
	{
		if ((*it)->DemoIndex == m_iRoundDemoIndex && (*it)->ExecuteTime == 0)	//0表示回合后
		{
			DealAIAction((*it));
			iDealAIAfterDemo = AI_PLAYACTION_DELAY_F;	//有AI的话延时0.5s再播Demo
			delete (*it);
			it = m_listAIAction.erase(it);
		}
		else
		{
			it++;
		}
	}
	m_iRoundDemoIndex++;
	return iDealAIAfterDemo;
}
void BattleScriptPlayer::DealAIActionBeforeOpearte(BeforeOperateAI aiaction)
{
	m_AIActionBeforeOperate.clear();
	m_AIActionBeforeOperate = aiaction;
	GetBattleManager()->AddNotAddFighterList();
    
	DealAIActionBeforeOpearte();
}
void BattleScriptPlayer::DealAIActionBeforeOpearte()
{
    assert(m_iRoundDemoIndex == 0);
    assert(m_listBattleDemoScript.empty());
	
    //如果没有回合前AI，直接进入操作阶段
    GetBattleManager()->SetBattleDemoSpeedUp(1.0f);	//恢复播放
    for (BattlerList::iterator itor = GetBattleManager()->m_listBattler.begin();itor != GetBattleManager()->m_listBattler.end();itor++)
    {
        if (*itor)
        {
            (*itor)->ResetBattlerLocation();
            (*itor)->ResetSpeed();
        }
    }
    if (m_AIActionBeforeOperate.empty())
    {
        m_iDealBeforeOperAI = AI_PLAYACTION_DELAY_B;
        GetBattleManager()->BeginBattleOperate(eOperateBattler_Char);
    }
    else
    {
        int AIDelay = GetBattleManager()->DealAIActionMap(m_AIActionBeforeOperate);
        m_AIActionBeforeOperate.clear();	//清空m_AIActionBeforeOperate
        m_iDealBeforeOperAI = AI_PLAYACTION_DELAY_B + AIDelay;//回合操作前的AI演示时间，暂时定为0.5s，到时候修改
        GetBattleManager()->SetBattleState(eBattleState_AIBeforeOperate);
    }
}
void BattleScriptPlayer::AddGroundEffect(Nuclear::IEffect* aPEffect)
{
	if (aPEffect == NULL)	return;
    for (unsigned int i = 0; i < m_GroundEffects.size(); i++) {
        if (aPEffect == m_GroundEffects[i]) {
            return;
        }
    }
    
    m_GroundEffects.push_back(aPEffect);
}
void BattleScriptPlayer::RemoveGroundEffect(Nuclear::IEffect* aPEffect)
{
    for (unsigned int i = 0; i < m_GroundEffects.size(); i++) {
        if (aPEffect == m_GroundEffects[i]) {
            Nuclear::GetEngine()->GetWorld()->RemoveEffect(m_GroundEffects[i]);
            m_GroundEffects[i] = m_GroundEffects[m_GroundEffects.size() - 1];
            m_GroundEffects.pop_back();
        }
    }
}
void BattleScriptPlayer::ClearGroundEffect()
{
    for (unsigned int i = 0; i < m_GroundEffects.size(); i++) {
        Nuclear::GetEngine()->GetWorld()->RemoveEffect(m_GroundEffects[i]);
    }
    m_GroundEffects.clear();
}
void BattleScriptPlayer::AddPlayEffect(Nuclear::IEffect* aPEffect)
{
	if (aPEffect == NULL)	return;
	for (unsigned int i = 0; i < m_PlayEffects.size(); i++) {
		if (aPEffect == m_PlayEffects[i]) {
			return;
		}
	}
	m_PlayEffects.push_back(aPEffect);
}
void BattleScriptPlayer::ClearPlayEffect()
{
	for (unsigned int i = 0; i < m_PlayEffects.size(); i++) {
		Nuclear::GetEngine()->GetWorld()->RemoveEffectEx(m_PlayEffects[i]);
	}
	m_PlayEffects.clear();
}
void BattleScriptPlayer::ClearSkills()
{
	for (size_t i = 0; i < m_pCurSkills.size(); i++)
	{
		delete m_pCurSkills[i];
		m_pCurSkills[i] = NULL;
	}
	m_pCurSkills.resize(0);
	m_pCurSkills.clear();
}
void BattleScriptPlayer::AddPersistStage(SkillStage* aPStage)
{
    m_listPersistStage.push_back(aPStage);
}
void BattleScriptPlayer::AddAI(sBattleAIAction* aPAI)
{
    m_listAIAction.push_back(aPAI);
}
Skill* BattleScriptPlayer::GetCurrentSkill()
{
	return NULL;
}
