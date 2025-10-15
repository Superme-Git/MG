//  BattleManager_Protocol.c
//  FireClient

#include "stdafx.h"
#include "BattleManager.h"

#include "fire/pb/battle/SSendBattleEnd.hpp"
#include "fire/pb/battle/SSendAddFighters.hpp"
#include "fire/pb/battle/SSendRoundScript.hpp"
#include "fire/pb/battle/SRemoveWatcher.hpp"
#include "BattleScriptPlayer.h"
#include "NewRoleGuideManager.h"
#include "SceneMovieManager.h"

//服务器发下来的进入战斗消息
void fire::pb::battle::SSendBattleStart::Process(Manager * manager, Manager::Session::ID sid)
{
	if (SceneMovieManager::GetInstance())
		SceneMovieManager::GetInstance()->RequestBreakMovie(true);
	int iTick = Nuclear::GetMilliSeconds();
	if (NULL == GetBattleManager())
	{
		return;
	}
	GetBattleManager()->SetBattleType(battletype);
	//LOG_OutLog_Info(MHSD_UTILS::GETSTRING(708).c_str());
	if (this->enemyside == 0)
	{
		GetBattleManager()->SetBattleIDChange(false);
	}
	else
	{
		GetBattleManager()->SetBattleIDChange(true);
	}
	GetBattleManager()->SetFriendFormation(friendsformation);
	GetBattleManager()->SetEnemyFormation(enemyformation);
	GetBattleManager()->SetFriendFormationLvl(friendsformationlevel);
	GetBattleManager()->SetEnemyFormationLvl(enemyformationlevel);
	int startID = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", "NEWER_BATTLE_ID_START");
	int endID = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", "NEWER_BATTLE_ID_END");
	if (battleconfigid >= startID && battleconfigid <= endID)
	{
		GetBattleManager()->BeginAutoOperate();
	}
	GetBattleManager()->BeginBattleScene(battletype, roundnum, battleconfigid, background, backmusic, false);
	GetBattleManager()->SetShowBattlerDemo(false);
}

//std::map<int, int> aiactions; can no move to lua
void fire::pb::battle::SSendBattleEnd::Process(Manager * manager, Manager::Session::ID sid )
{
	if (NULL == GetBattleManager())
	{
		return;
	}    
	GetBattleManager()->DealAIActionBeforeBattleEnd(aiactions);
	GetBattleManager()->RefreshBattlerDemo();
}
// fighterlist not move to lua
void fire::pb::battle::SSendAddFighters::Process(Manager * manager, Manager::Session::ID sid )
{
	if (NULL == GetBattleManager())
	{
		return;
	}
	for (std::list<fire::pb::battle::FighterInfo>::iterator itor = fighterlist.begin();itor != fighterlist.end();itor++)
	{
		if (itor->index > WatchMaxID)
			continue;
		sBattlerData data;
		data.iBattleID = itor->index;
		GetBattleManager()->SCBattleIDChange(data.iBattleID);
		data.BattlerType = (eBattlerType)itor->fightertype;
		data.bGM = itor->bgm;
		data.BattlerDataID = itor->dataid;
		data.strName = itor->fightername; 
		data.subtype = (itor->subtype)&0xFFFF;
        data.colorInx = ((itor->subtype)&0xFFFF0000) >> 16;
		data.strTitle = itor->title;
		data.iTitleID = itor->titleid;
		data.MaxHp = itor->maxhp;
		data.Hp = itor->hp;
		data.eQuipEffect = itor->components[eSprite_Effect]; 
		data.Ep = itor->ep;		
		data.UpLimitHp = itor->uplimithp;
		data.iShapeID = itor->shape;
		data.componentsMap = itor->components;
        data.footprint = itor->footlogoid;
		data.petkeys = itor->petkeys;
		for (std::map<int, int>::iterator it = itor->buffs.begin();it != itor->buffs.end();it++)
		{
 			data.BattleBuff[it->first] = it->second;
		}
		if (data.iBattleID <= EnemyMaxID)
		{
			GetBattleManager()->AddTempBattlerData(data);
		}
		else
		{
			GetBattleManager()->AddTempWatcherData(data);
		}
	}
}

//not move to lua
void fire::pb::battle::SSendRoundScript::Process(Manager * manager, Manager::Session::ID sid)
{
	//技能引导，超时引导失败
	gGetNewRoleGuideManager()->FinishGuideInBattle();
	int iTick = Nuclear::GetMilliSeconds();
	if (NULL == GetBattleManager())
	{
		return;
	}
	g_BattleScriptPlayer.m_actionStarttime = Nuclear::GetMilliSeconds();
	std::map<int, int> roundendattrchange;
	for (std::map<int, float>::iterator it = rolechangedattrs.begin(); it != rolechangedattrs.end(); it++)
	{
		roundendattrchange[it->first] = static_cast<int>(it->second);
	}
	GetBattleManager()->SetMainRoleAttributeRoundEnd(roundendattrchange);

	for (std::map<int, float>::iterator it = petchangedattrs.begin(); it != petchangedattrs.end(); it++)
	{
		roundendattrchange[it->first] = static_cast<int>(it->second);
	}
	GetBattleManager()->SetMainPetAttributeRoundEnd(roundendattrchange);
	std::map<int, int> roundendhp;
	for (std::map<int, int>::iterator it = fighterfinallyhps.begin(); it != fighterfinallyhps.end(); it++)
	{
		roundendhp[it->first] = static_cast<int>(it->second);
	}
	GetBattleManager()->SetFighterHPRoundEnd(roundendhp);

	GetBattleManager()->ClearRoundAIAction();

	for (std::list<fire::pb::battle::AIOperation>::iterator itor = aiactions.begin(); itor != aiactions.end(); itor++)
	{
		sBattleAIAction* pAIAction = new sBattleAIAction;
		pAIAction->DemoIndex = itor->actionseq;			
		pAIAction->ExecuteTime = itor->actionmoment;	
		if (pAIAction->ExecuteTime > 0)
		{
			GetBattleManager()->SCBattleIDChange(pAIAction->ExecuteTime);
		}
		pAIAction->BattlerID = itor->actionfighterid;	
		pAIAction->AIActionID = itor->actionid;			

		GetBattleManager()->SCBattleIDChange(pAIAction->BattlerID);	//battleID转换

		g_BattleScriptPlayer.AddAI(pAIAction);
	}

	int i = 1;
	for (std::list<fire::pb::battle::NewResultItem>::iterator itor = playitem.begin(); itor != playitem.end(); itor++, i++)
	{
		sBattleDemoScript DemoScript;
		DemoScript.stMagicCasting.AttackerID = itor->execute.attackerid;
		GetBattleManager()->SCBattleIDChange(DemoScript.stMagicCasting.AttackerID);//battleID转换
		DemoScript.stMagicCasting.BattleOperate = (eBattleOperate)itor->execute.operationtype;
		DemoScript.stMagicCasting.HPConsume = itor->execute.hpconsume;
		DemoScript.stMagicCasting.MPConsume = itor->execute.mpconsume;
		DemoScript.stMagicCasting.SPConsume = itor->execute.spconsume;
		DemoScript.stMagicCasting.OperatorID = itor->execute.operationid;
		DemoScript.stMagicCasting.msgid = itor->execute.msgid;

		for (std::list<fire::pb::battle::DemoBuff>::iterator iter = itor->execute.demobuffs.begin(); iter != itor->execute.demobuffs.end(); iter++)
		{
			int battleid = iter->fighterid;
			GetBattleManager()->SCBattleIDChange(battleid);
			sBattleBuffResult buffdata(battleid, iter->buffid, iter->round);
			DemoScript.stMagicCasting.BuffResultList.push_back(buffdata);
		}
		for (std::map<int, float>::iterator it = itor->rolechangedattrs.begin(); it != itor->rolechangedattrs.end(); it++)
		{
			DemoScript.RoleAttribute[it->first] = (int)(it->second);
		}
		for (std::map<int, float>::iterator it = itor->petchangedattrs.begin(); it != itor->petchangedattrs.end(); it++)
		{
			DemoScript.PetAttribute[it->first] = (int)(it->second);
		}
		for (std::list<fire::pb::battle::NewSubResultItem>::iterator itResultItem = itor->subresultlist.begin(); itResultItem != itor->subresultlist.end(); itResultItem++)
		{
			sNewDemoResult demoResult;
			demoResult.subskillid = itResultItem->subskillid;
			demoResult.subskillstarttime = itResultItem->subskillstarttime;

			for (std::list<fire::pb::battle::NewDemoResult>::iterator itDemoResult = itResultItem->resultlist.begin(); itDemoResult != itResultItem->resultlist.end(); itDemoResult++)
			{
				sBattleDemoResult result;	
				result.TargetID = itDemoResult->targetid;
				result.FlagType = itDemoResult->flagtype; 
				result.ResultType = itDemoResult->resulttype;
				if (result.ResultType == 5)
				{
					GetBattleManager()->m_bLastRound = true;
					continue;
				}
				for (std::map<int, int>::iterator it = itDemoResult->datas.begin(); it != itDemoResult->datas.end(); it++)
				{
					switch (it->first)
					{
					case fire::pb::battle::NewDemoResult::HP_CHANGE:
					{
						result.HPChange = it->second;		
					}
					break;
					case fire::pb::battle::NewDemoResult::MP_CHANGE:
					{
						result.MPChange = it->second;		
					}
					break;
					case fire::pb::battle::NewDemoResult::SP_CHANGE:
					{
						result.SPChange = it->second;		
					}
					break;
					case fire::pb::battle::NewDemoResult::UL_HP_CHANGE:
					{
						result.MaxHPChange = it->second;	
					}
					break;
					case fire::pb::battle::NewDemoResult::TARGET_RESULT:
					{
						result.eTargetResult = it->second;		
					}
					break;
					case fire::pb::battle::NewDemoResult::RETURN_HURT:
					{
						result.ReturnHurtEffect = it->second;
					}
					break;
					case fire::pb::battle::NewDemoResult::ATTACK_BACK:
					{
						result.AttackBackEffect = it->second;
					}
					break;
					case fire::pb::battle::NewDemoResult::STEAL_HP:
					{
						result.StealHPEffect = it->second;	
					}
					break;
					case fire::pb::battle::NewDemoResult::STEAL_MP:
					{
						result.StealMPEffect = it->second;	
					}
					break;
					case fire::pb::battle::NewDemoResult::ATTACKER_RESULT:
					{
						result.eAttackerResult = it->second;	
					}
					break;
					case fire::pb::battle::NewDemoResult::PROTECTER_ID:
					{
						result.ProtecterID = it->second;	
					}
					break;
					case fire::pb::battle::NewDemoResult::PROTECTER_HP_CHANGE:
					{
						result.ProtectHPChange = it->second;
					}
					break;
					case fire::pb::battle::NewDemoResult::PROTECTER_RESULT:
					{
						result.eProtecterResult = it->second;
					}
					break;
					case fire::pb::battle::NewDemoResult::ASSISTER_ID:
					{
						result.AssisterID = it->second;			
					}
					break;
					case fire::pb::battle::NewDemoResult::RETURN_HURT_DEATH:	
					{
						result.ReturnHurtMaxHP = it->second;
					}
					break;
					case fire::pb::battle::NewDemoResult::PROTECTER_MAXHP_CHANGE:
					{
						result.ProtectMaxHPChange = it->second;
					}
					break;
					case fire::pb::battle::NewDemoResult::MESSAGE_ID:
					{
					}
					break;
					case fire::pb::battle::NewDemoResult::HP_GODBLESS:
					{
						result.HPChangeGodBless = it->second;
					}
					break;
					case fire::pb::battle::NewDemoResult::EP_CHANGE:
					{
						result.EPChange = it->second;
					}
					break;
					case fire::pb::battle::NewDemoResult::SHAPE_CHANGE:
					{
						result.ShapeChange = it->second;
					}
					break;
					default:
						break;
					}
				}
				for (std::list<fire::pb::battle::DemoBuff>::iterator itDemoBuff = itDemoResult->demobuffs.begin(); itDemoBuff != itDemoResult->demobuffs.end(); itDemoBuff++)
				{
					int battleid = itDemoBuff->fighterid;
					GetBattleManager()->SCBattleIDChange(battleid);
					sBattleBuffResult buffdata(battleid, itDemoBuff->buffid, itDemoBuff->round);
					result.BuffResultList.push_back(buffdata);
				}
				GetBattleManager()->SCBattleIDChange(result.TargetID);//battleID转换
				GetBattleManager()->SCBattleIDChange(result.ProtecterID);
				GetBattleManager()->SCBattleIDChange(result.AssisterID);
				demoResult.listResults.push_back(result);
			}
			DemoScript.listResults.push_back(demoResult);
		}
		for (std::list<fire::pb::battle::FighterInfo>::iterator it = itor->newfighter.begin(); it != itor->newfighter.end(); it++)
		{
			sBattlerData data;
			data.iBattleID = it->index;
			GetBattleManager()->SCBattleIDChange(data.iBattleID);
			data.BattlerType = (eBattlerType)it->fightertype;
			data.bGM = it->bgm;
			data.iShapeID = it->shape;
			data.BattlerDataID = it->dataid;
			data.strName = it->fightername; 
			data.subtype = (it->subtype) & 0xFFFF;
			data.colorInx = ((it->subtype) & 0xFFFF0000) >> 16;
			data.strTitle = it->title;
			data.UpLimitHp = it->uplimithp;
			data.MaxHp = it->maxhp;
			data.Hp = it->hp;
			data.Ep = it->ep;
			for (std::map<int, int>::iterator iter = it->buffs.begin(); iter != it->buffs.end(); iter++)
			{
				data.BattleBuff[iter->first] = iter->second;
			}
			data.componentsMap = it->components;
			DemoScript.listBattler.push_back(data);
		}
		if (g_BattleScriptPlayer.m_listBattleDemoScript.size() > 0)
		{
			sBattleDemoScript& last = g_BattleScriptPlayer.m_listBattleDemoScript[g_BattleScriptPlayer.m_listBattleDemoScript.size() - 1];
			if(last.stMagicCasting.AttackerID == DemoScript.stMagicCasting.AttackerID 
				&&last.stMagicCasting.OperatorID ==  DemoScript.stMagicCasting.OperatorID)
			{
				DemoScript.stMagicCasting.AttackType = 1;
			}
		}
		g_BattleScriptPlayer.PushDemoScript(DemoScript);
	}

	GetBattleManager()->SetBattlerOperateStateNull();
	GetBattleManager()->SetShowBattlerDemo(false);
	GetBattleManager()->AddNotAddFighterList();
	if (!GetBattleManager()->IsInBattleDemo())
	{
		GetBattleManager()->SetBattleState(eBattleState_Demo);
		GetBattleManager()->FinishBattleOperate();
	}
	GetBattleManager()->AddRoundCount();
	g_BattleScriptPlayer.Play();
}

//删除观战者
void fire::pb::battle::SRemoveWatcher::Process(Manager * manager, Manager::Session::ID sid)
{
	if (NULL == GetBattleManager())
	{
		return;
	}
	GetBattleManager()->RemoveBattler(fighterindex);
}
