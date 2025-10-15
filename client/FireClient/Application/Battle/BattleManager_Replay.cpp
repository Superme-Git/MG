#include "stdafx.h"
#include "BattleManager.h"
#include "rpcgen/fire/pb/battle/BattleType.hpp"
#include "BattleScriptPlayer.h"
#include "NewRoleGuideManager.h"
#include "SceneMovieManager.h"
#include "BattleReplayManager.h"
#include "fire/pb/battle/SSendBattleStart.hpp"
#include "fire/pb/battle/SSendAddFighters.hpp"
#include "fire/pb/battle/SSendRoundScript.hpp"
#include "fire/pb/battle/CStopRePlay.hpp"


//==================================================================================
//添加回放按钮
void BattleManager::AddExitReplayBtn(int iBattleID)
{
	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();

	CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
	m_pExitReplayBtn = static_cast <CEGUI::PushButton*>(winMgr.createWindow("TaharezLook/ImageButton", OnlyName));
	if (m_pExitReplayBtn)
	{
		m_pExitReplayBtn->setAlwaysOnTop(true);
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow != NULL)
		{
			pRootWindow->addChildWindow(m_pExitReplayBtn);
			//m_pExitReplayBtn->setProperty("UnifiedAreaRect","{{0.0,0},{0.0,0},{0.0,40},{0.0,20}}");
			//m_pExitReplayBtn->setPosition(UVector2(cegui_absdim(1180),cegui_absdim(601)));
			m_pExitReplayBtn->setSize(CEGUI::UVector2(cegui_absdim(86), cegui_absdim(95)));
			m_pExitReplayBtn->setPosition(CEGUI::UVector2(CEGUI::UDim(1, -96), CEGUI::UDim(1, -105)));
			//m_pExitReplayBtn->setText(MHSD_UTILS::GETSTRING(728).c_str());
			m_pExitReplayBtn->subscribeEvent(CEGUI::PushButton::EventClicked, CEGUI::Event::Subscriber(&BattleManager::HandleExitReplay, this));

			const char* pNormal = "set:fightui image:quxiao";
			const char* pPushed = "set:fightui image:quxiao";

			m_pExitReplayBtn->setProperty("NormalImage", pNormal);
			m_pExitReplayBtn->setProperty("PushedImage", pPushed);

		}
	}
}
//删除回放按钮
void BattleManager::RemoveExitReplayBtn()
{
	if (m_pExitReplayBtn)
	{
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow != NULL)
			pRootWindow->removeChildWindow(m_pExitReplayBtn);
		m_pExitReplayBtn = NULL;
	}
}
//点击退出回放按钮
bool BattleManager::HandleExitReplay(const CEGUI::EventArgs& e)
{
	RequestLeaveBattleReplay();
	return true;
}
//开始回放
void BattleManager::BeginReplayScene(int leftcount, int battletype, int roundnum, int background, int backmusic, int64_t battlekey)
{
	m_bReplayModel = true;
	BeginBattleScene(battletype, roundnum, -1, background, backmusic, true);
	AddExitReplayBtn(-1);

	g_BattleScriptPlayer.m_RoundIndex = 0;

}
//回放状态下离开战斗
void BattleManager::RequestLeaveBattleReplay()
{
	if (m_bReplayModel)
	{
		SetBattleState(eBattleState_BeforeBattleEnd);
		m_iDealBeforeBattleEnd = 0;
		EndBattleScene();
		GetBattleReplayManager()->OnOverReplay();
	}
	else
	{
		fire::pb::battle::CStopRePlay StopRePlay;
		gGetNetConnection()->send(StopRePlay);
	}
}
int BattleManager::BeginBattleReplay(std::wstring FileName)
{
	//Download
	return 0;
}
int BattleManager::BeginPlayBattleReplay(std::wstring FileName)
{
	if (IsInBattle())
	{
		return -1;
	}

	fire::pb::battle::SSendBattleStart SSBS;
	fire::pb::battle::SSendAddFighters SSAF;
	int Result = 0;
	Result = LoadBattleReplay(FileName, SSBS, SSAF, g_BattleScriptPlayer.m_RoundArr);
	if (Result != 0)
	{
		RequestLeaveBattleReplay();
		return -1;
	}
	Result = SSendBattleStart_Process(SSBS);
	if (Result != 0)
	{
		RequestLeaveBattleReplay();
		return -1;
	}
	Result = SSendAddFighters_Process(SSAF);
	if (Result != 0)
	{
		RequestLeaveBattleReplay();
		return -1;
	}
	Result = g_BattleScriptPlayer.PlayReplayRound(g_BattleScriptPlayer.m_RoundIndex);
	if (Result != 0)
	{
		RequestLeaveBattleReplay();
		return -1;
	}
	return 0;
}
int BattleManager::LoadBattleReplay(std::wstring FileName, fire::pb::battle::SSendBattleStart& SSBS, fire::pb::battle::SSendAddFighters& SSAF, std::vector<fire::pb::battle::SSendRoundScript>& RoundArr)
{
	std::fstream FS;
	FS.open(ws2s(FileName).c_str(), std::ios_base::binary | std::ios_base::in);
	if (!FS.is_open())
	{
		return -1;
	}
	FS.seekg(0, std::ios_base::end);
	unsigned int FSSize = FS.tellg();
	FS.seekg(0, std::ios_base::beg);

	char* pData = new char[FSSize];
	FS.read(pData, FSSize);
	FS.close();

	FireNet::Marshal::OctetsStream OS;
	OS.push_byte(pData, FSSize);
	delete pData;

	//fire::pb::battle::SSendBattleStart SSBS;
	SSBS.unmarshal(OS);

	//fire::pb::battle::SSendAddFighters SSAF;
	unsigned int FighterCount = 0;
	char c = OS.unmarshal_char();
	if (c < 0)
	{
		FighterCount = 256 + c;
	}
	else
	{
		FighterCount = c;
	}
	
	if (FighterCount > 100)
	{
		return -1;
	}
	for (unsigned int i = 0; i < FighterCount; i++)
	{
		fire::pb::battle::FighterInfo Fighter;
		Fighter.unmarshal(OS);
		SSAF.fighterlist.push_back(Fighter);
	}

	unsigned int RountCount = 0;
	c = OS.unmarshal_char();
	if (c < 0)
	{
		RountCount = 256 + c;
	}
	else
	{
		RountCount = c;
	}
	//std::vector<fire::pb::battle::SSendRoundScript> RoundArr;
	if (RountCount > 300)
	{
		return -1;
	}
	RoundArr.clear();
	RoundArr.resize(RountCount);
	for (unsigned int i = 0; i < RountCount; i++)
	{
		unsigned int NewResultItemCount = 0;
		c = OS.unmarshal_char();
		if (c < 0)
		{
			NewResultItemCount = 256 + c;
		}
		else
		{
			NewResultItemCount = c;
		}

		if (NewResultItemCount > 100)
		{
			return -1;
		}

		for (unsigned int i2 = 0; i2 < NewResultItemCount; i2++)
		{
			fire::pb::battle::NewResultItem NRT;
			NRT.unmarshal(OS);
			RoundArr[i].playitem.push_back(NRT);
		}
		unsigned int AIOperationCount = 0;
		c = OS.unmarshal_char();
		if (c < 0)
		{
			AIOperationCount = 256 + c;
		}
		else
		{
			AIOperationCount = c;
		}
		if (AIOperationCount > 100)
		{
			return -1;
		}
		for (unsigned int i2 = 0; i2 < AIOperationCount; i2++)
		{
			fire::pb::battle::AIOperation AO;
			AO.unmarshal(OS);
			RoundArr[i].aiactions.push_back(AO);
		}
		unsigned int HPCount = OS.unmarshal_int32();
		if (HPCount > 100)
		{
			return -1;
		}
		for (unsigned int i2 = 0; i2 < HPCount; i2++)
		{
			int iID = OS.unmarshal_int32();
			int iHP = OS.unmarshal_int32();

			RoundArr[i].fighterfinallyhps[iID] = iHP;
		}
		unsigned int MPCount = OS.unmarshal_int32();
		if (MPCount > 100)
		{
			return -1;
		}
		for (unsigned int i2 = 0; i2 < MPCount; i2++)
		{
			int iID = OS.unmarshal_int32();
			int iMP = OS.unmarshal_int32();

			RoundArr[i].fighterfinallymps[iID] = iMP;
		}
	}
	return 0;
}

int BattleManager::SSendBattleStart_Process(fire::pb::battle::SSendBattleStart& SSBS)
{
	if (SceneMovieManager::GetInstance())
	{
		SceneMovieManager::GetInstance()->RequestBreakMovie(true);
	}
	if (NULL == GetBattleManager())
	{
		return -1;
	}
	GetBattleManager()->SetBattleType(SSBS.battletype);
	//LOG_OutLog_Info(MHSD_UTILS::GETSTRING(708).c_str());
	if (SSBS.enemyside == 0)
	{
		GetBattleManager()->SetBattleIDChange(false);
	}
	else
	{
		GetBattleManager()->SetBattleIDChange(true);
	}
	GetBattleManager()->SetFriendFormation(SSBS.friendsformation);
	GetBattleManager()->SetEnemyFormation(SSBS.enemyformation);
	GetBattleManager()->SetFriendFormationLvl(SSBS.friendsformationlevel);
	GetBattleManager()->SetEnemyFormationLvl(SSBS.enemyformationlevel);
	GetBattleManager()->BeginReplayScene(0, SSBS.battletype, SSBS.roundnum, SSBS.background, SSBS.backmusic, false);
	return 0;
}
int BattleManager::SSendAddFighters_Process(fire::pb::battle::SSendAddFighters& SSAF)
{
	if (NULL == GetBattleManager())
	{
		return -1;
	}
	for (std::list<fire::pb::battle::FighterInfo>::iterator itor = SSAF.fighterlist.begin(); itor != SSAF.fighterlist.end(); itor++)
	{
		if (itor->index > WatchMaxID)
		{
			continue;
		}
		sBattlerData data;
		data.iBattleID = itor->index;
		GetBattleManager()->SCBattleIDChange(data.iBattleID);
		data.BattlerType = (eBattlerType)itor->fightertype;
		data.bGM = itor->bgm;
		data.BattlerDataID = itor->dataid;
		data.strName = itor->fightername;
		data.subtype = (itor->subtype) & 0xFFFF;
		data.colorInx = ((itor->subtype) & 0xFFFF0000) >> 16;
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
		for (std::map<int, int>::iterator it = itor->buffs.begin(); it != itor->buffs.end(); it++)
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
	return 0;
}
int BattleManager::SSendRoundScript_Process(fire::pb::battle::SSendRoundScript& SSRS)
{
	//技能引导，超时引导失败
	gGetNewRoleGuideManager()->FinishGuideInBattle();
	if (NULL == GetBattleManager())
	{
		return -1;
	}
	GetBattleManager()->ClearRoundAIAction();

	for (std::list<fire::pb::battle::AIOperation>::iterator itor = SSRS.aiactions.begin(); itor != SSRS.aiactions.end(); itor++)
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
	for (std::list<fire::pb::battle::NewResultItem>::iterator itor = SSRS.playitem.begin(); itor != SSRS.playitem.end(); itor++, i++)
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
			if (last.stMagicCasting.AttackerID == DemoScript.stMagicCasting.AttackerID
				&&last.stMagicCasting.OperatorID == DemoScript.stMagicCasting.OperatorID)
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
	g_BattleScriptPlayer.Play();
	return 0;
}