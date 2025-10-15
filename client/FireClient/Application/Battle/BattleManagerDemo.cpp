#include "stdafx.h"
#include "BattleManager.h"
#include "BattleMagicControl.h"
#include "GameOperateState.h"
#include "Battler.h"
#include "BattleIllusion.h"
#include "MainRoleDataManager.h"
#include "GameScene.h"
#include "GameUIManager.h"
#include "MessageManager.h"
#include "rpcgen/fire/pb/battle/BattleType.hpp"
#include "MainCharacter.h"
#include "BattleScriptPlayer.h"
#include "SimpleAudioEngine.h"
#include "ConfigManager.h"
#include "NewRoleGuideManager.h"
#include "MusicSoundVolumeMixer.h"
#include "fire/pb/battle/CStopRePlay.hpp"

#define NPCSoundRes L"/sound/chat/"

BattleManager::BattleManager()
: m_eBattleState(eBattleState_Null)
, m_iRoundCount(0)
, m_iSummonID(0)
, m_pMouseOverBattler(NULL)
, m_iCurSelectedSkillID(0)
, m_iOperateTime(c_nBattleOperateTime*1000)
, m_bBattleIDChange(false)
, m_iAutoRound(-1)
, m_iAutoMaxRound(AUTO_ROUND_NUM)
, m_bWatchModel(false)
, m_bReplayModel(false)
, m_bShowHpBar(false)
, m_iEnemyFormation(0)
, m_iFriendFormation(0)
, m_iDealBeforeBattleEnd(0)
, m_BattleType(fire::pb::battle::BattleType::BATTLE_PVE)
, m_SummonDemoKey(0)
, m_bRecallDemo(false)
, m_BattleID(-1)
, m_bAlreadyPackTips(false)
, m_eCurDemoOperate(eOperate_Null)
, m_iBattleGroundid(0)
, m_iBattleMusicid(0)
, m_hCurrentBattleGround(Nuclear::INVALID_WAR_BACKGROUND_HANDLE)
, m_iEffectComponentNum(120)
, m_fDemoSpeedRate(1.0f)
, m_bIsAutoBattle(false)
, m_bIsAutoBattleQuick(false)
, m_bFirstEnter(true)
, m_bFirstShowQuickButton(false)
, m_bShowDemoing(false)
, m_iDemoShowDelayTime(0)
{
	m_pMainBattler[eOperateBattler_Char] = NULL;
	m_pMainBattler[eOperateBattler_Pet] = NULL;

	m_pDefaultSkillID[eOperateBattler_Char] = 0;
	m_pDefaultSkillID[eOperateBattler_Pet] = 0;

	m_iDefaultTargetID[eOperateBattler_Char] = 0;
	m_iDefaultTargetID[eOperateBattler_Pet] = 0;

	m_listBattler.clear();
	m_listBattleIllusion.clear();
	m_listAreaBlock.clear();

	m_listAIAction.clear();
}
BattleManager::~BattleManager()
{
	g_BattleScriptPlayer.DoBattleClear();
	ClearBattleData();

	m_pDefaultSkillID[eOperateBattler_Char] = 0;
	m_pDefaultSkillID[eOperateBattler_Pet] = 0;
}

bool BattleManager::IsEscapeForbiddenBattle()
{
	return m_BattleType == fire::pb::battle::BattleType::BATTLE_DUEL_SINGLE
		|| m_BattleType == fire::pb::battle::BattleType::BATTLE_DUEL_TEAM
		|| m_BattleType == fire::pb::battle::BattleType::BATTLE_LIVEDIE;
}
bool BattleManager::IsInPVPBattle()
{
	return m_BattleType%10 ==1;
}
Nuclear::NuclearFRectt BattleManager::GetBattlerRect(int battlerId)
{
	Battler* pMonster = FindBattlerByID(battlerId);
	if (pMonster != NULL)
	{
		Nuclear::NuclearLocation pt = pMonster->GetScreenPosition();
		pt.y -= 120;
		pt.x -= 60;

		return Nuclear::NuclearFRectt(pt.x, pt.y, pt.x + 120, pt.y + 120);
	}
	return Nuclear::NuclearFRectt(0, 0, 0, 0);
}
CEGUI::Window* BattleManager::GetBattlerWindow(int battlerId)
{
	Battler* pMonster = FindBattlerByID(battlerId);
	if (pMonster != NULL)
	{
		return pMonster->GetBattleWindow();
	}
	return NULL;
}
int BattleManager::GetLastBattleId()
{
	return m_lastBattleId;
}

void BattleManager::InitAreaBlock()
{
	ClearAreaBlock();
	m_listAreaBlock.resize(9);
	for (size_t i = 0; i < m_listAreaBlock.size(); i++)
	{
		int pos = i;
		if (pos >= 5)		
			pos += 5;
		m_listAreaBlock[i] = new AreaBlock(pos + 1);
	}
}

void BattleManager::ClearBattleData()
{
	ClearTempWatcherData();

	ClearTempBattlerData();

	m_iDealBeforeBattleEnd = 0;

	m_iBattleGroundid = 0;
	ResetBattleGround();
	m_iBattleMusicid = 0;

	m_AlreadyUseItemList.clear();

	m_eCurDemoOperate = eOperate_Null;
	m_bAlreadyPackTips = false;
	
	m_iDefaultTargetID[eOperateBattler_Char] = 0;
	m_iDefaultTargetID[eOperateBattler_Pet] = 0;

	SetBattleState(eBattleState_Null);
	
	CloseAllBattleDialog();

	GetBattleMagicControl()->DeleteAllBattleMagic();

	cocos2d::gGetScriptEngine()->executeGlobalFunction("MainPetDataManager_ResetPetState");

	if (GetSummonDemoPetKey() != 0)
	{
		cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainRoleData_SetBattlePet", GetSummonDemoPetKey());
		SetSummonDemoPetKey(0);
	}

	m_SummonDemoKey = 0;
	m_iSummonID = 0;
	m_bBattleIDChange = false;
	m_pMouseOverBattler = NULL;
	m_pMainBattler[eOperateBattler_Char] = NULL;
	m_pMainBattler[eOperateBattler_Pet] = NULL;

	m_iCurSelectedSkillID = 0;
    if (m_bWatchModel)
        RemoveExitWatchBtn();
	m_bWatchModel = false;

	if (m_bReplayModel)
	{
		RemoveExitReplayBtn();
		fire::pb::battle::CStopRePlay StopRePlay;
		gGetNetConnection()->send(StopRePlay);
	}
	m_bReplayModel = false;

	m_iEnemyFormation = 0;
	m_iFriendFormation = 0;
	ClearBattler();
	ClearAreaBlock();
	ClearRoundAIAction();
	FreeJobPic();
}
void BattleManager::ClearBattler()
{
	for (BattlerList::iterator itor = m_listBattler.begin();itor != m_listBattler.end();itor++)
	{
        (*itor)->SetSpriteReleaseFlag(Nuclear::XPANI_RELEASE_IMMEDIATE_GC);
		delete (*itor);
		(*itor) = NULL;
	}
	m_listBattler.clear();
	for (BattleIllusionList::iterator itor = m_listBattleIllusion.begin();itor != m_listBattleIllusion.end();itor++)
	{
		delete (*itor);
		(*itor) = NULL;
	}
	m_listBattleIllusion.clear();
}
void BattleManager::ClearIllusionAll()
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		(*itor)->ClearPhantom();
	}
}
void BattleManager::ClearAreaBlock()
{
	for (AreaBlockList::iterator itor = m_listAreaBlock.begin(); itor != m_listAreaBlock.end(); itor++)
	{
		(*itor)->Release();
		delete (*itor);
		(*itor) = NULL;
	}
	m_listAreaBlock.clear();
}
void BattleManager::ClearRoundAIAction()
{
	for (AIActionList::iterator itor = m_listAIAction.begin();itor != m_listAIAction.end();itor++)
	{
		delete (*itor);
		(*itor) = NULL;
	}
	m_listAIAction.clear();
}
void BattleManager::ClearMainBattlePet()
{
	m_pMainBattler[eOperateBattler_Pet] = NULL;
	m_pDefaultSkillID[eOperateBattler_Pet] = 0;	
}

void BattleManager::RefreshAllSpriteNameTexture()
{    
    for(BattlerList::iterator itor = m_listBattler.begin();itor != m_listBattler.end(); itor++)
    {
        Battler* entity = *itor;
        if(entity)
        {
            entity->UpdatNameTexture(true);
        }
    }
}
void BattleManager::ReleaseAllSpriteNameTexture()
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		Battler* entity = *itor;
		if (entity)
		{
			entity->ReleaseNameTexture();
		}
	}
}
void BattleManager::ReloadAllSpriteNameTexture()
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		Battler* entity = *itor;
		if (entity)
		{
			entity->ReloadNameTexture();
		}
	}
}
void BattleManager::ResetBattlerDefaultAction()
{
	for (BattlerList::iterator it=m_listBattler.begin();it!=m_listBattler.end();it++)
	{
		if ((*it) && !(*it)->IsWatcher() && !(*it)->IsGeneralDie())
		{
			(*it)->SetBattleAttackFinish(false);
			(*it)->PlayAction(eActionBattleStand);
			(*it)->SetDefaultAction(eActionBattleStand);
		}
	}
}
void BattleManager::ResetBattlerHpBar()
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		if ((*itor) && !(*itor)->IsWatcher())
		{
			(*itor)->UpdateHpBarLocation();
	}
}
}

void BattleManager::SetBattleState(eBattleState state)
{
	if (m_eBattleState == eBattleState_Begin && state != eBattleState_Begin)
	{
		if (m_BattleType == fire::pb::battle::BattleType::BATTLE_BOSS)
		{
            if (GetTipsManager())
                GetTipsManager()->AddMsgTips(141614);
		}
		bool havebaby = false;
		for (BattlerList::iterator it=m_listBattler.begin();it!=m_listBattler.end();it++)
		{
			if ((*it)->AddBabyTips())
			{
				havebaby = true;
			}
		}
		if (havebaby && GetTipsManager())
			GetTipsManager()->AddMsgTips(142017);
	}
	if ((m_eBattleState == eBattleState_OperateChar || m_eBattleState == eBattleState_OperateChar) && state == eBattleState_Demo)
	{
		m_iOperateTime = 0;
		GetBattleManager()->BeginAutoOperate();
		m_bIsAutoBattleQuick = false;
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("BattleAutoFightDlg.CSetDaoJiShi", 0);
		SendAutoCommand(true);
	}
	m_eBattleState = state;
	if (m_eBattleState == eBattleState_OperateChar)
	{
		ResetBattlerDefaultAction();
	}
	if (m_bWatchModel)
		return;
	if (m_eBattleState != eBattleState_Begin && m_eBattleState != eBattleState_AIBeforeOperate)
	{
		if (!m_bShowHpBar)
		{
			for_each(m_listBattler.begin(),m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::ShowHpBar),true));
		}
	}
	else
	{
		m_bShowHpBar = false;
	}	
}
bool BattleManager::AddBattler(sBattlerData* pData)
{
	Battler* pBattler = FindBattlerByID(pData->iBattleID);
	if (pBattler)
	{
		pBattler->SetAttribute(pData);
		if (m_pMainBattler[eOperateBattler_Char]
			&& m_pMainBattler[eOperateBattler_Char]->GetBattleID() + 5 == pData->iBattleID
			&& !m_bWatchModel && !m_bReplayModel)
		{
			m_pMainBattler[eOperateBattler_Pet] = pBattler;
		}
		return true;
	}
	else
	{
		pBattler = new Battler();
		if (pData->BattlerDataID == GetMainRoleDataNumValue<int64_t>("roleid") && !m_bReplayModel)
		{
			m_pMainBattler[eOperateBattler_Char] = pBattler;
		}
		if (m_pMainBattler[eOperateBattler_Char]
		 && m_pMainBattler[eOperateBattler_Char]->GetBattleID() + 5 == pData->iBattleID
		 && !m_bWatchModel && !m_bReplayModel)
		{
			m_pMainBattler[eOperateBattler_Pet] = pBattler;
		}
		pBattler->SetAttribute(pData);
		pBattler->SetVisible(false);	//先是设置visible为false，之后100ms现身，避免转向的动作
		m_listBattler.push_back(pBattler);
		if (m_bShowHpBar)
		{
			pBattler->ShowHpBar(true);
		}
		return true;
	}
}
bool BattleManager::RemoveBattler(int iBattleID)
{
	for (std::list<sBattlerData>::iterator it=m_listTempWatcherData.begin();it!=m_listTempWatcherData.end();it++)
	{
		if (it->iBattleID == iBattleID)
		{
			m_listTempWatcherData.erase(it);
			break;
		}
	}
	for (BattlerList::iterator itor = m_listBattler.begin();itor != m_listBattler.end();itor++)
	{
		if ((*itor)->GetBattleID() == iBattleID)
		{
			if ((*itor) == m_pMainBattler[eOperateBattler_Pet])
				m_pMainBattler[eOperateBattler_Pet] = NULL;
			delete (*itor);
			itor = m_listBattler.erase(itor);
			return true;
		}
	}
	return false;
}
BattleIllusion* BattleManager::AddBattleIllusion(sBattlerData data,float scale)
{
	BattleIllusion* pBattleIllusion = new BattleIllusion();
	pBattleIllusion->SetAttribute(data,scale);
	m_listBattleIllusion.push_back(pBattleIllusion);
	return pBattleIllusion;
}
BattleIllusion* BattleManager::AddBattleIllusion(int shapeid,float scale)
{
	sBattlerData data;
	data.iShapeID = shapeid;
	data.BattlerType = eBattlerMonster;
	return AddBattleIllusion(data,scale);
}
void BattleManager::SetBattleDemoSpeedUp(float rate)
{
	m_fDemoSpeedRate = rate;
	for_each(m_listBattler.begin(),m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::SetBattlerDemoSpeedRate),m_fDemoSpeedRate));
	for_each(m_listBattleIllusion.begin(),m_listBattleIllusion.end(), std::bind2nd(std::mem_fun(&BattleIllusion::SetBattlerDemoSpeedRate),m_fDemoSpeedRate));
}
void BattleManager::AddNotAddFighterList()
{
	if (!m_listTempBattlerData.empty())
	{
		for (std::list<sBattlerData>::iterator it = m_listTempBattlerData.begin(); it != m_listTempBattlerData.end();)
		{
			AddBattler(&(*it));
			it = m_listTempBattlerData.erase(it);
		}
	}
}

Nuclear::PictureHandle BattleManager::GetJobPicHandle(int schoolID)
{
	std::map<int, Nuclear::PictureHandle>::iterator itFind = m_mapJobPic.find(schoolID);
	if (itFind != m_mapJobPic.end())
	{
		return itFind->second;
	}

	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	Nuclear::PictureHandle pichandle = Nuclear::INVALID_PICTURE_HANDLE;
	if (pScriptEngine && schoolID > 0)
	{
		CallLuaUtil util;
		util.addArg(schoolID);
		if (util.callLua(pScriptEngine->getLuaState(), "GetSchoolPicPath"))
		{
			std::wstring pic = s2ws(util.getRetCString());
			if (pic != L"")
				pichandle = Nuclear::GetEngine()->GetRenderer()->LoadPicture(pic);
		}
	}
	if (pichandle != Nuclear::INVALID_PICTURE_HANDLE)
		m_mapJobPic[schoolID] = pichandle;
	return pichandle;
}
void BattleManager::FreeJobPic()
{
	std::map<int, Nuclear::PictureHandle>::iterator itBegin = m_mapJobPic.begin();
	std::map<int, Nuclear::PictureHandle>::iterator itEnd = m_mapJobPic.end();
	while (itBegin != itEnd)
	{
		Nuclear::PictureHandle pic = itBegin->second;
		Nuclear::GetEngine()->GetRenderer()->FreePicture(pic);
		itBegin++;
	}
	m_mapJobPic.clear();
}
void BattleManager::PlayAISpeak(std::wstring soundpath, Battler* pExecutor)
{
	if (gGetGameConfigManager() && gGetGameConfigManager()->isPlayEffect())
	{
		std::wstring strSoundRes = NPCSoundRes + soundpath;

		std::wstring newws;
		newws = LJFM::LJFMF::GetFullPathFileName(strSoundRes);
		std::string newSoundRes;
		newSoundRes = StringCover::to_string(newws);
		LJFM::LJFMF AFile;
		if (!newSoundRes.empty())
		{
			// 不允许同一个AI语音重叠播放
			if (!CocosDenshion::SimpleAudioEngine::sharedEngine()->getEffectIsPlaying(newSoundRes.c_str())) {
				CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffect(newSoundRes.c_str(), false);
				if (MusicSoundVolumeMixer::GetInstance())
					MusicSoundVolumeMixer::GetInstance()->notifySoundStart(newSoundRes);
			}
		}
	}
}
void BattleManager::PlayBattleSound(int musicid)
{
    CocosDenshion::SimpleAudioEngine::sharedEngine()->stopBackgroundMusic(true);
    
    if (gGetGameConfigManager()&&gGetGameConfigManager()->isPlayBackMusic())
    {
		std::wstring ws;
		int configid = GetTableNumberFromLua("battle.cbattlebackmusic", musicid, "id");
		if (configid != -1)
		{
			ws = GetTableWStringFromLua("battle.cbattlebackmusic", musicid, "path");
		}
		else
		{
			musicid = rand() % 10 + 1;
			ws = GetTableWStringFromLua("battle.cbattlebackmusic", musicid, "path");
		}
		//std::wstring ws = StringCover::to_wstring(g_musicResPath) + L"battle.ogg";
		std::wstring newws;
		newws = LJFM::LJFMF::GetFullPathFileName(ws);
		std::string newSoundRes;
		newSoundRes = StringCover::to_string(newws);
		if (!newSoundRes.empty())
		{
			CocosDenshion::SimpleAudioEngine::sharedEngine()->playBackgroundMusic(newSoundRes.c_str(), true);
			int BackMusicValue = gGetGameConfigManager()->GetConfigValue(L"soundvalue");
			if (MusicSoundVolumeMixer::GetInstance())
				MusicSoundVolumeMixer::GetInstance()->setBackgroundMusicVolume((float)(BackMusicValue / 255.0f));
		}
    }
}
void BattleManager::ProcessRoundEndScript(std::vector<sBattleDemoResult>& list)
{
	for (std::vector<sBattleDemoResult>::iterator it = list.begin();it != list.end();it++)
	{
		Battler* pBattler = FindBattlerByID(it->TargetID);
		if (pBattler)
		{
			for (std::list<sBattleBuffResult>::const_iterator itor=it->BuffResultList.begin();itor!=it->BuffResultList.end();itor++)
			{
				Battler* pBuffBattler = FindBattlerByID(itor->battleid);
				if (pBuffBattler)
				{
					pBuffBattler->ProcessBuffChange(*itor);
				}
			}
			pBattler->ProcessResultAction(it->eTargetResult, it->FlagType, 0, 2, it->HPChange, it->MPChange, it->MaxHPChange, it->SPChange);
			if (it->ShapeChange != 0)
			{
				int mId = (it->ShapeChange == -1) ? 0 : it->ShapeChange;
				pBattler->ChangeSpriteModel(mId);
			}
			if (it->EPChange != 0)
				pBattler->SetEPValue(pBattler->GetEPValue() + it->EPChange);
		}
	}
	list.clear();
}

void BattleManager::BeginBattleScene(int battletype, int roundnum, int battleid, int groundid, int musicid, bool bIsWatch)
{
// yeqing for android test OOM
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	Nuclear::GetEngine()->GCNow();
#endif

	// 新手战斗时，不允许自动播放玩家语音
	if (gGetGameApplication()->GetFirstTimeEnterGame() < 3) {
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("CChatManager.SetAllowAutoPlay", false);
	}

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleManager.SaveBattleData");
	gGetScene()->BeginShake(0, 1, 0);
	SetSummonCount(0);
	if (cocos2d::gGetScriptEngine()->executeGlobalFunction("MainPetDataManager_BattlePetIsMyPackPet"))
	{
		SetSummonCount(1);
	}
	// 开始战斗关闭npc对话框 add by 杨佳凡
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NpcDialog.handleWindowShut");

	gGetGameUIManager()->setTreasureMapId(-1);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("supertreasuremap.DestroyDialog");

	gGetGameUIManager()->setCurrentItemId(-1);
	SetUseItemCount(0, 20);//275
	SetUseItemCount(1, 10);//290,291

	SetRunawayConfirmCount(0, 0);
	SetRunawayConfirmCount(1, 0);

	if (m_bFirstEnter)
	{
		m_bFirstEnter = false;
	}
    
	if (gGetDisPlayMode() > eDisplayMode_800600)
	{
		Nuclear::GetEngine()->GetWorld()->SetWarBackgroundEdge(10);//考虑到震屏幕，要增加背景底图的范围
	}
	if (groundid != 0)
	{
		ChangeBattleGround(groundid);
		Nuclear::GetEngine()->GetWorld()->SetWarBackground(m_hCurrentBattleGround, Nuclear::XPWBT_CENTER);
	}
	else
	{
		ChangeBattleGround(100);
	}
	
	PlayBattleSound(musicid);
	InitAreaBlock();
	m_BattleID = battleid;
	m_BattleType = battletype;
	m_iRoundCount = roundnum;	
	m_bLastRound = false;
	switch(battletype)
	{   
	case fire::pb::battle::BattleType::BATTLE_PVE:
		break;
	case fire::pb::battle::BattleType::BATTLE_PVP:
	case fire::pb::battle::BattleType::BATTLE_CLAN_FIGHT:		
		{
			GetBattleManager()->EndAutoOperate();
		}
		break;
	case fire::pb::battle::BattleType::BATTLE_LIVEDIE:
		{
			GetBattleManager()->EndAutoOperate();
            if (GetTipsManager())
                GetTipsManager()->AddMsgTips(120006);	//<T t="请注意，这是PK战斗！" c="ffff0000"></T>
		}
		break;
	case fire::pb::battle::BattleType::BATTLE_DUEL_SINGLE:	
	case fire::pb::battle::BattleType::BATTLE_DUEL_TEAM :	
		{
			GetBattleManager()->EndAutoOperate();
		}
		break;
	default:
		{
		}
		break;
	}

	gGetGameOperateState()->ResetBattleCursor();

	if (gGetMessageManager())
		gGetMessageManager()->OnBattleBegin();
	    
	SetBattleState(eBattleState_Begin);
	EventBeginBattle.Bingo();

	SCBattleIDChange(m_LastBattleOperateCmd[eOperateBattler_Char].TargetID);
	SCBattleIDChange(m_LastBattleOperateCmd[eOperateBattler_Pet].TargetID);
    
	if (bIsWatch == false)
	{
		if (IsAutoOperate())
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleAutoFightDlg.getInstanceAndShow");
		}
		else
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleAutoFightDlg.getInstance");
		}
	}
    
	if (gGetGameUIManager()->isNpcSoundPlaying())
	{
		CocosDenshion::SimpleAudioEngine::sharedEngine()->stopEffectByPath(gGetGameUIManager()->getCurNpcSound().c_str());
	}

    cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ShowHide.EnterBattle");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("deathNoteDlg.DestroyDialog");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.ChangeBattleRound", 0);
}
void BattleManager::EndBattleScene()
{
	g_BattleScriptPlayer.DoBattleClear();
	
	SCBattleIDChange(m_LastBattleOperateCmd[eOperateBattler_Char].TargetID);
	SCBattleIDChange(m_LastBattleOperateCmd[eOperateBattler_Pet].TargetID);

	m_iRoundCount = 0;

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("BattleTips.CSetBattleID", 0, 0);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleSkillTip.DestroyDialog");

	ClearBattleData();
	gGetGameOperateState()->ResetCursor();

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("PetAndUserIcon.UpdatePetIconShowHideCpp");
   
	if (gGetMessageManager())
	{
		gGetMessageManager()->OnBattleEnd();
	}
    
  //  for (unsigned int i = 0; i < m_summons.size(); ++i)
  //  {
		//m_summons[i].Process(NULL, 0);
  //  }
  //  m_summons.clear();

	if (CEGUI::System::getSingleton().getModalTarget())
    {
		CEGUI::System::getSingleton().getModalTarget()->activate();
    }
    else
    {
		CEGUI::System::getSingleton().setModalTarget(NULL);
    }

	m_lastBattleId = m_BattleID;
	m_BattleID = -1;
	
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("BattleAutoFightDlg.CSetVisible", false);

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LogoInfoDialog.GetSingletonDialogAndShowIt");

	EventEndBattle.Bingo();

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ShowHide.EndBattle");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleManager.ReleaseBattleData");

	// 新手战斗结束后，允许自动播放玩家语音
	if (gGetGameApplication()->GetFirstTimeEnterGame() < 3) {
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("CChatManager.SetAllowAutoPlay", true);
	}

// yeqing for android test OOM
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	Nuclear::GetEngine()->GCNow();
#endif
}
void BattleManager::Run(int now, int delta)
{
	for (BattlerList::iterator itor = m_listBattler.begin();itor != m_listBattler.end();)
	{
		if ((*itor))
		{
			if ((*itor)->IsDisappear())
			{
				int battleID = (*itor)->GetBattleID();
				if ((*itor) == m_pMainBattler[eOperateBattler_Pet])
					m_pMainBattler[eOperateBattler_Pet] = NULL;
				delete (*itor);
				itor = m_listBattler.erase(itor);
			}
			else
			{
				(*itor)->Run(now,delta);
				itor++;
			}	
		}
		else
			itor++;
	}
	for (BattleIllusionList::iterator itor = m_listBattleIllusion.begin();itor != m_listBattleIllusion.end();)
	{
		if ((*itor))
		{
			if ((*itor)->IsDisappear())
			{
				delete (*itor);
				itor = m_listBattleIllusion.erase(itor);
			}
			else
			{
				(*itor)->Run(now,delta);
				itor++;
			}
		}
		else
			itor++;
	}
	if (!m_listTempBattlerData.empty())
	{
		int addnum = 0;
		if (IsInWatchModel())
		{
			addnum = std::min(3,delta/30);
		}
		else
		{
			addnum = std::min(1,delta/30);
		}
		for (std::list<sBattlerData>::iterator it=m_listTempBattlerData.begin();it!=m_listTempBattlerData.end();)
		{
			AddBattler(&(*it));
			it = m_listTempBattlerData.erase(it);
			addnum--;
			if (addnum <= 0)
			{
				break;
			}
		}
	}
	else
	{
		for (std::list<sBattlerData>::iterator it=m_listTempWatcherData.begin();it!=m_listTempWatcherData.end();)
		{
			AddBattler(&(*it));
			it = m_listTempWatcherData.erase(it);
			break;
		}
	}
	
	switch(m_eBattleState)
	{
	case eBattleState_Begin:		
		{
		}
		break;
	case eBattleState_AIBeforeOperate:
		{
			g_BattleScriptPlayer.m_iDealBeforeOperAI -= delta;
			if (g_BattleScriptPlayer.m_iDealBeforeOperAI < 0)
			{
				g_BattleScriptPlayer.m_iDealBeforeOperAI = 0;
				BeginBattleOperate(eOperateBattler_Char);
			}
		}
		break;
	case eBattleState_BeforeBattleEnd:
		{
			m_iDealBeforeBattleEnd -= delta;
			if (m_iDealBeforeBattleEnd < 0)
			{
				m_iDealBeforeBattleEnd = 0;
				EndBattleScene();
			}
		}
		break;
	case eBattleState_OperateChar:	
	case eBattleState_OperatePet:
		{
			BattleOperateRun(now,delta);
		}
		break;
	case eBattleState_DemoOperate:
		{
			BattleOperateRun(now,delta);
            g_BattleScriptPlayer.Run(now, delta);
		}
		break;
	case eBattleState_Demo:		
		{
            g_BattleScriptPlayer.Run(now, delta);
		}
		break;
	}
	if (m_iDemoShowDelayTime > 0)
	{
		m_iDemoShowDelayTime -= delta;
		if (m_iDemoShowDelayTime <= 0)
		{
			SetShowBattlerDemo(true);
		}
	}
}
void BattleManager::BattleOperateRun(int now, int delta)
{
	m_iOperateTime -= delta;
	if (m_bWatchModel)
	{
		if (m_iOperateTime < 0)
		{
			SetBattleState(eBattleState_Wait);
		}
	}
	else if (m_bReplayModel)//回放状态,倒计时结束后显示"请等待"
	{
		m_iOperateTime = -1;
		if (m_iOperateTime < 0)
		{
			SetBattleState(eBattleState_Wait);
		}
	}
	else
	{
		if (m_eBattleState != eBattleState_DemoOperate)
		{
			if (m_iOperateTime >= 500 && m_iOperateTime <= c_nBattleOperateTime*1000)
			{
				if (IsAutoOperate())
				{
					if (m_bIsAutoBattleQuick == true)
					{
						m_bIsAutoBattleQuick = false;
						cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("BattleAutoFightDlg.CSetDaoJiShi", 0);
						SendAutoCommand(true);
					}
					else
					{
						if (m_iOperateTime <= 30000 && m_iOperateTime > 29400)
						{
							cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("BattleAutoFightDlg.CSetDaoJiShi", 3);
						}
						else if (m_iOperateTime <= 29400 && m_iOperateTime > 28700)
						{
							cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("BattleAutoFightDlg.CSetDaoJiShi", 2);
						}
						else if (m_iOperateTime <= 28700 && m_iOperateTime > 28000)
						{
							cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("BattleAutoFightDlg.CSetDaoJiShi", 1);
						}
						else if (m_iOperateTime <= 28000)
						{
							cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("BattleAutoFightDlg.CSetDaoJiShi", 0);
							SendAutoCommand(true);
						}
					}
				}
				else
				{
					int a = 1;
				}
			}
			else if (m_iOperateTime >= 0 && m_iOperateTime < 500)
			{
                gGetNewRoleGuideManager()->FinishGuide();
				m_iOperateTime = 0;
				GetBattleManager()->BeginAutoOperate();
				m_bIsAutoBattleQuick = false;
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("BattleAutoFightDlg.CSetDaoJiShi", 0);
				SendAutoCommand(true);
			}
		}
		else
		{
			int a = 1;
		}
	}
}
void BattleManager::OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type)
{
	if (FindBattlerBySprite(sprite))
		FindBattlerBySprite(sprite)->UpdateAction(type);
	if (FindBattleIllusionBySprite(sprite))
		FindBattleIllusionBySprite(sprite)->UpdateAction(type);
}
void BattleManager::ClearAllFlag()
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		(*itor)->SetFlag(L"");
	}
}