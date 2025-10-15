//  BattleManager_AI.c
//  FireClient

#include "stdafx.h"
#include "BattleManager.h"
#include "Battler.h"
#include "GameUIManager.h"
#include "GameTable/battle/CBattleAIConfig.h"

int BattleManager::DealAIActionMap(BeforeOperateAI aiaction)
{
	int maxTM = 0;
	for (BeforeOperateAI::iterator it = aiaction.begin();it != aiaction.end();it++)
	{
		const GameTable::battle::CBattleAIConfig& BattleAI = GameTable::battle::GetCBattleAIConfigTableInstance().getRecorder(it->second);
		Battler* pAIExecutor = FindBattlerByID(it->first);
		if (BattleAI.id != -1)
		{
			if (BattleAI.talkinfo != L"" && pAIExecutor)	
			{
				std::wstring talkstr;
				if(BattleAI.talkinfo.find(L"<T") == std::wstring::npos)
				{
					std::wstringstream stream;
					stream << L"<T t=\""<<BattleAI.talkinfo<<L"\"></T>";
					talkstr = stream.str();
					pAIExecutor->AddTalk(stream.str(),false);
				}
				else
				{
					talkstr = BattleAI.talkinfo;
					pAIExecutor->AddTalk(BattleAI.talkinfo,false);
				}
				maxTM = (maxTM > 200) ? maxTM : 200;
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
            
			if (BattleAI.tipsinfo != L"")			
			{
				//gGetGameUIManager()->AddMessageTip(BattleAI.tipsinfo);
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(BattleAI.tipsinfo).c_str());
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.AddMessageTip_", 1);
			}
			if (BattleAI.appearchange != 0 && pAIExecutor) 
			{
				pAIExecutor->ChangeSpriteModel(BattleAI.appearchange);
			}
			if (BattleAI.changeground != 0)
			{
				ChangeBattleGround(BattleAI.changeground);
			}
			if (BattleAI.playsound != 0)
			{
				PlayBattleSound(BattleAI.playsound);
			}
			if (BattleAI.playeffect != L"")
			{
				PlayAIEffect(BattleAI.playeffect,pAIExecutor);
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
				if(BattleAI.speakshow != 0)
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
							stream << L"<T t=\"" << BattleAI.talkinfo << L"\"></T>";
							talkstr = stream.str();
						}
						else
						{
							talkstr = BattleAI.talkinfo;
						}
						std::wstring stime = BattleAI.speaktime;
						if (stime == L"")
							stime = L"5";
						std::wstring sinfo = L"file=\"" + speak + L"\" text=\"" + talkstr + L"&\" time=" + stime;
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
	return maxTM;
}
void BattleManager::DealAIActionBeforeBattleEnd(BeforeOperateAI aiaction)
{
	if (aiaction.empty())
	{
		SetBattleState(eBattleState_BeforeBattleEnd);
		m_iDealBeforeBattleEnd = 0;
		EndBattleScene();
	}
	else
	{
		SetBattleState(eBattleState_BeforeBattleEnd);
		m_iDealBeforeBattleEnd = 1500;		
		BeforeOperateAI::iterator it = aiaction.find(-1);
		if (it != aiaction.end())
		{
			m_iDealBeforeBattleEnd += it->second;
		}
		else
		{
			DealAIActionMap(aiaction);
		}
	}
}

void BattleManager::SetDealBeforeBattleEnd(int iDealBeforeBattleEnd)
{
	m_iDealBeforeBattleEnd = iDealBeforeBattleEnd;
}
int BattleManager::GetDealBeforeBattleEnd()
{
	return m_iDealBeforeBattleEnd;
}

void BattleManager::PlayAIEffect(std::wstring effectpath,Battler* pExecutor)
{
	std::vector<std::wstring>	delimiters;
	delimiters.push_back(L":");delimiters.push_back(L",");delimiters.push_back(L";");
	std::vector<std::wstring>	outputstrings;
	StringCover::split_string(effectpath,delimiters,outputstrings);
	int effectnum = int(outputstrings.size())/3;
	for (int i=0;i<effectnum;i++)
	{
		std::wstring effectname = outputstrings[i*3];
		Nuclear::NuclearLocation loc = Nuclear::NuclearLocation(StringCover::StrToNum<int>(StringCover::to_string(outputstrings[i*3+1])),StringCover::StrToNum<int>(StringCover::to_string(outputstrings[i*3+2])));
		if (loc == Nuclear::NuclearLocation(0,0) && pExecutor)
		{
			if (effectname.find(L"3d") == 0)
			{
				pExecutor->Play3DEffect(effectname,L"",true);
			}
			else
			{
				pExecutor->PlayEffect(L"geffect/" + effectname);
			}
		}
		else
		{
			Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
			Nuclear::NuclearLocation effectloc = Nuclear::NuclearLocation(cp.left,cp.top) + loc + (gGetDisPlayMode() == eDisplayMode_1024768?Nuclear::NuclearLocation(112,84):Nuclear::NuclearLocation(0,0));
			Nuclear::GetEngine()->GetWorld()->PlayEffect(L"geffect/" + effectname,Nuclear::XPEL_BATTLE_MID,effectloc.x,effectloc.y,1,false,2);
		}
	}
}


