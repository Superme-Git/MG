//  SkillBuilder.cpp
//  FireClient
#include "stdafx.h"
#include "SkillBuilder.h"
#include "Battler.h"
#include "BattleManager.h"
#include "GameScene.h"
#include "GameTable/battle/CSkillInfo.h"
#include "GameTable/battle/CStageInfo.h"
#include "GameTable/battle/CStageInfo2.h"
#include "GameTable/gm/Cbattlelist.h"
#include "BattleMagicControl.h"
#include "BattleIllusion.h"
#include "MainRoleDataManager.h"
#include "BattleScriptPlayer.h"
#include "SimpleAudioEngine.h"
#include "GameTable/skill/CSkillTypeConfig.h"
#include "GameUIManager.h"
#include "ArtTextManager.h"
#include "ConfigManager.h"

enum eStageType
{
	eStage_Action, 
	eStage_Move, 
	eStage_Effect, 
	eStage_Teleport, 
	eStage_Result, 
	eStage_ChangeModel, 
	eStage_Alpha
};

enum eExecuteType
{
    eExecute_Normal, 
	eExecute_Parallel, 
	eExecute_Ser
};

enum eMoveType
{
	eMove_Target, 
	eMove_Pos, 
	eMove_Back, 
	eMove_Release
};

enum eMagicPosType
{
	eMagicPos_Null = -1,
	eMagicPos_Static = 0,	//静止于人物
	eMagicPos_Friend = 1,	//友方阵型中间
	eMagicPos_Enemy = 2,	//敌方阵型中间
	eMagicPos_Fly = 3,	//飞行魔法
	eMagicPos_Middle = 4,	//战场中央
	eMagicPos_ToSelf = 5,	//从目标到自身
	eMagicPos_HeadTop = 6,	//头顶
	eMagicPos_Banner = 7,	//技能名称
};

static Nuclear::NuclearLocation GetSidePos(bool aBIsFriend)
{
	int resolution = 0;

	if (GetBattleManager()->GetBattleID() == -1)//如果是普通随机战斗,服务器发过来的是-1
	{
		resolution = 7;//1280 * 720
	}
	else//如果是在BattleInfo里面配置的任务战斗则会有值,用此值到客户端的BattleList表中读取对应的分辨率ID
	{
		resolution = GameTable::gm::GetCbattlelistTableInstance().getRecorder(GetBattleManager()->GetBattleID()).positionsid;
	}
    std::wstring loc = L"";
    
    if (!aBIsFriend )
    {
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine)
		{
			CallLuaUtil util;
			util.addArg(resolution);
			if (util.callLua(pScriptEngine->getLuaState(), "GetResolutionPositionsBytarget"))
			{
				loc = s2ws(util.getRetCString());
			}
		}
    }
    else
    {
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine)
		{
			CallLuaUtil util;
			util.addArg(resolution);
			if (util.callLua(pScriptEngine->getLuaState(), "GetResolutionPositionsByme"))
			{
				loc = s2ws(util.getRetCString());
			}
		}
    }
	std::vector<std::wstring>	delimiters;
	delimiters.push_back(L";");
	std::vector<std::wstring>	outputstrings;
	StringCover::split_string(loc, delimiters, outputstrings);

	Nuclear::NuclearRect VP = gGetScene()->GetViewport();
	if (outputstrings.size() != 2)//如果当前点坐标数据有误则设置坐标为屏幕中心
	{
		return Nuclear::NuclearLocation(VP.left + VP.Width() / 2, VP.top + VP.Height() / 2);
	}

	Nuclear::IEngine* pEngine = Nuclear::GetEngine();
	int VPSizeW = pEngine->m_adapter->get_logic_w();//之里取得的宽高应该和上面VP取得的一样
	int VPSizeH = pEngine->m_adapter->get_logic_h();

	int longa = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetResolutionLong", resolution);
	int wide = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetResolutionWide", resolution);

	float fScaleW = VPSizeW * 1.0f / longa;//分别计算现在的宽高和表中配置的标准分辨率宽高的比
	float fScaleH = VPSizeH * 1.0f / wide;

	float fScaleOriginalWH = longa * 1.0f / wide;//计算一下原始标准分辨率的宽高之间比例,例如3:2 16:9

	float fScaleFinal;//这是保留最终是用宽的还是高的比例

	//根据现有分辨率与标准分辨率宽高比来计算最终的战斗区域大小和最终缩放比
	if (fScaleW >= fScaleH)//使用高的比例来缩放
	{
		VPSizeW = VPSizeH * fScaleOriginalWH;
		fScaleFinal = fScaleH;
	}
	else//使用宽的比例来缩放
	{
		VPSizeH = VPSizeW / fScaleOriginalWH;
		fScaleFinal = fScaleW;
	}

	Nuclear::NuclearLocation ptBattleLoc;
	ptBattleLoc = Nuclear::NuclearLocation(StringCover::StrToNum<int>(StringCover::to_string(outputstrings[0])), StringCover::StrToNum<int>(StringCover::to_string(outputstrings[1])));

	ptBattleLoc.x *= fScaleFinal;//对当前点坐标进行最终缩放
	ptBattleLoc.y *= fScaleFinal;

	int VPX = (VP.Width() - VPSizeW) / 2;//计算当前新战斗区域在硬件区域中居中的坐标
	int VPY = (VP.Height() - VPSizeH) / 2;

	return Nuclear::NuclearLocation(VP.left + VPX, VP.top + VPY) + ptBattleLoc;//这里一定要加上当前VP的Left和Top,这两个值非0
}

class CAlphaStage : public SkillStage
{
	int                     m_hBattler;
	unsigned char			m_nAlpha;
	CAlphaStage();
public:
	CAlphaStage(int aPBattler, int aTicks, int alpha = 0) : SkillStage(aTicks)
	{
		m_hBattler = aPBattler;
		m_nAlpha = alpha;
	}

	virtual void OnStageBegin()
	{
		Battler* pBattler = GetBattleManager()->FindBattlerByID(m_hBattler);

		if (pBattler == NULL)
			return;
		float fA = (float)m_nAlpha / 255;
		pBattler->SetAlphaCtrl(fA);
	}

	virtual void OnStageEnd()
	{

	}
};
class CChangeModelStage : public SkillStage
{
	int                     m_hBattler;
	int						m_nChangeModel;
	CChangeModelStage();
public:
	CChangeModelStage(int aPBattler, int aTicks, int changeMode = 0) : SkillStage(aTicks)
	{
		m_hBattler = aPBattler;
		m_nChangeModel = changeMode;
	}

	virtual void OnStageBegin()
	{
		Battler* pBattler = GetBattleManager()->FindBattlerByID(m_hBattler);

		if (pBattler == NULL)
			return;
		pBattler->ChangeSpriteModel(m_nChangeModel, 0);
		pBattler->SetDefaultAction(pBattler->GetActionType());
	}

	virtual void OnStageEnd()
	{

	}
};
class CPetSummonStage : public SkillStage
{
	int                 m_hBattler;
	unsigned char		m_cNormalAplha;
public:
	CPetSummonStage(int ahBattler, int aTicks) : SkillStage(aTicks){
		m_hBattler = ahBattler;
	}

	virtual void OnStageBegin()
	{
		Battler* pBattler = GetBattleManager()->FindBattlerByID(m_hBattler);
		if (pBattler == NULL) {
			m_iLifeTicks = 1;
			return;
		}
		pBattler->PlayEffect(MHSD_UTILS::GetEffectPath(11046));
		pBattler->SetCurrentAlpha(1);
		m_cNormalAplha = pBattler->GetNormalAlpha();
	}

	virtual void OnStageEnd()
	{
		Battler* pBattler = GetBattleManager()->FindBattlerByID(m_hBattler);
		if (pBattler == NULL) {
			m_iLifeTicks = 1;
			return;
		}
		pBattler->SetCurrentAlpha(pBattler->GetNormalAlpha());
	}

	virtual void OnStageTick(int now, int delta)
	{
		Battler* pBattler = GetBattleManager()->FindBattlerByID(m_hBattler);
		if (pBattler == NULL) {
			m_iLifeTicks = 1;
			return;
		}
		unsigned int alpha = m_cNormalAplha * ((float)(now - m_iBeginTick) / m_iLifeTicks);
		if (alpha > m_cNormalAplha) {
			alpha = m_cNormalAplha;
		}
		pBattler->SetCurrentAlpha(alpha);
	}
};
class CResultStage : public SkillStage
{
	int             m_hSkillUser;
	int             m_hTarget;
	Skill*         m_pSkill;
	int				m_OnhitDelay;
	int				m_OnhitDir;
	int             m_iResultIndex;

	CResultStage();
public:
	CResultStage(int aPUser, int aPTarget, int aOnhitDelay, int aOnhitDir, Skill* aPSkill, int ResultIndex, int aTicks) : SkillStage(aTicks)
	{
		m_bIsTriggerStage = true;
		m_bIsTriggered = false;

		m_hSkillUser = aPUser;
		m_hTarget = aPTarget;
		m_pSkill = aPSkill;
		m_OnhitDelay = aOnhitDelay;
		m_OnhitDir = aOnhitDir;
		if (m_OnhitDir != 2 && m_OnhitDir != 4 && m_OnhitDir != 6 && m_OnhitDir != 8)
		{
			m_OnhitDir = 2;
		}
		m_iResultIndex = ResultIndex;
	}

	virtual void OnStageBegin()
	{
		Battler* pSkillUser = GetBattleManager()->FindBattlerByID(m_hSkillUser);
		Battler* pTarget = GetBattleManager()->FindBattlerByID(m_hTarget);

		//play action and wait it finish
		printf("result count:%d \n", m_pSkill->GetResultList().size());
		if (m_pSkill->GetResultList().empty()) return;

		sBattleDemoResult rResult = m_pSkill->GetResultList()[m_iResultIndex];
		sBattleDemoResult* pResult = &rResult;

		if (pResult && pResult->bgeneresult != true)
		{
			//受击目标buff改变的处理
			for (std::list<sBattleBuffResult>::const_iterator itor = pResult->BuffResultList.begin(); itor != pResult->BuffResultList.end(); itor++)
			{
				Battler* pBuffBattler = GetBattleManager()->FindBattlerByID(itor->battleid);
				if (pBuffBattler)
				{
					pBuffBattler->ProcessBuffChange(*itor);
				}
			}
			if (pTarget)
			{
				pTarget->ProcessResultAction(pResult->eTargetResult, pResult->FlagType, m_OnhitDelay, m_OnhitDir, pResult->HPChange, pResult->MPChange, pResult->MaxHPChange, pResult->SPChange, false, true, pResult->HPChangeGodBless);

				if (pResult->ShapeChange != 0)
				{
					int mId = (pResult->ShapeChange == -1) ? 0 : pResult->ShapeChange;
					pTarget->ChangeSpriteModel(mId, 0);
				}
				if (pResult->EPChange != 0)
					pTarget->SetEPValue(pTarget->GetEPValue() + pResult->EPChange);
			}
			Battler* m_pProtecter = GetBattleManager()->FindBattlerByID(pResult->ProtecterID);
			if (m_pProtecter)
			{
				pResult->eProtecterResult |= eBattleResult_Defence;
				m_pProtecter->ProcessResultAction(pResult->eProtecterResult, pResult->FlagType, m_OnhitDelay, m_OnhitDir, pResult->ProtectHPChange, 0, pResult->ProtectMaxHPChange, 0, false, false, pResult->HPChangeGodBless,false);
			}
			//攻击者触发吸血效果
			if (pSkillUser)
			{
				if (pResult->StealHPEffect != 0)
				{
					pSkillUser->ProcessResultAction(eBattleResult_HPChange, pResult->FlagType, 0, 2, pResult->StealHPEffect, 0);
					pSkillUser->PlayEffect(MHSD_UTILS::GetEffectPath(11011));
				}
				if (pResult->StealMPEffect != 0)
				{
					pSkillUser->ProcessResultAction(eBattleResult_MPChange, pResult->FlagType, 0, 2, 0, pResult->StealMPEffect);
				}
				if (pResult->ReturnHurtEffect != 0)//攻击者触发被反伤效果
				{
					pResult->eAttackerResult |= eBattleResult_Hit;
					pSkillUser->ProcessResultAction(pResult->eAttackerResult, pResult->FlagType, m_OnhitDelay, m_OnhitDir, pResult->ReturnHurtEffect, 0, pResult->ReturnHurtMaxHP, 0, true, false, pResult->HPChangeGodBless);
					if (m_pProtecter)
						m_pProtecter->PopEffectText(eFanZhen);
					else if (pTarget)
						pTarget->PopEffectText(eFanZhen);
					m_iLifeTicks += 200;
				}
			}
			m_pSkill->m_Script.listResults[m_pSkill->m_SubSkillIndex].listResults[m_iResultIndex].bgeneresult = true;
		}
	}
	virtual void OnStageEnd()
	{

	}
};
class CEffectStage : public SkillStage
{
	int                     m_hBattler;
	Nuclear::NuclearLocation		m_pLocation;
	std::wstring            m_strEffectName;
	eMagicPosType        m_posType;
	bool                    m_bIsWorld;
	bool                    m_bHasTrail;
	Skill*                 m_pSkill;
	int						m_Phantom;
	int						m_PhantomAlpha;
	int						m_TopMidBottom;
	int						m_EffectTime;
	int						m_EffectTimeMode;
	int						m_iRot;
	int                     m_uBattler;
	int						m_LightType;
	int                     m_apperaTime;
	int						m_disappearTime;
	int                     m_totalTime;
	CEffectStage();
public:
	CEffectStage(int aPBattler, int aPhantom, int aPhantomAlpha, Nuclear::NuclearLocation aLoc, std::wstring aEffectName, eMagicPosType aPosType,
		bool aBIsWorldEffect, bool aBHasTrail, Skill* pSkill, int aTicks, int TopMidBottom, int EffectTime = 1, int EffectTimeMode = 0, int iRot = 0) : SkillStage(aTicks)
	{
		m_hBattler = aPBattler;
		m_pLocation = aLoc;
		m_strEffectName = aEffectName;
		m_posType = aPosType;
		m_bIsWorld = aBIsWorldEffect;
		m_bHasTrail = aBHasTrail;
		m_pSkill = pSkill;
		m_Phantom = aPhantom;
		m_PhantomAlpha = aPhantomAlpha;
		m_TopMidBottom = TopMidBottom;
		m_EffectTime = EffectTime;
		m_EffectTimeMode = EffectTimeMode;
		m_iRot = iRot;
		m_LightType = 0;
	}

	void SetLightingParam(int uBattler, int acttype, int appearTime, int disappearTime, int totalTime)
	{
		m_uBattler = uBattler;
		m_LightType = acttype;
		m_apperaTime = appearTime;
		m_disappearTime = disappearTime;
		m_totalTime = totalTime;
	}
	virtual void OnStageBegin()
	{
		Battler* pBattler = GetBattleManager()->FindBattlerByID(m_hBattler);

		if (pBattler == NULL)
		{
			return;
		}

		BattleIllusion* pPhantom = NULL;
		if (m_Phantom > 0)
		{
			pPhantom = pBattler->FindAndAddPhantom(m_Phantom);
			pPhantom->SetAlpha(m_PhantomAlpha);
		}

		//play action and wait it finish
		if (m_bHasTrail)
		{
			Nuclear::IEffect* pEffect = NULL;
			pEffect = Nuclear::GetEngine()->GetWorld()->SetEffect(m_strEffectName, Nuclear::XPEL_BATTLE_LOW, m_pLocation.x, m_pLocation.y, false);
			g_BattleScriptPlayer.AddGroundEffect(pEffect);
		}
		else if (m_posType == eMagicPos_Friend || m_posType == eMagicPos_Enemy)
		{
			Nuclear::IEffect* pEffect = NULL;
			if (m_TopMidBottom >= 0)
				pEffect = Nuclear::GetEngine()->GetWorld()->PlayEffect(m_strEffectName, Nuclear::XPEL_BATTLE_HEIGHT, m_pLocation.x, m_pLocation.y, 1, false, 2);
			else
				pEffect = Nuclear::GetEngine()->GetWorld()->PlayEffect(m_strEffectName, Nuclear::XPEL_BATTLE_LOW, m_pLocation.x, m_pLocation.y, 1, false, 2);
			g_BattleScriptPlayer.AddPlayEffect(pEffect);
		}
		else if (m_posType == eMagicPos_ToSelf)
		{
			stBattleDemoResultArr result = m_pSkill->GetResultList();
			for (unsigned int i = 0; i < result.size(); i++) {
				Battler* pTarget = GetBattleManager()->FindBattlerByID(result[i].TargetID);
				if (pTarget)
				{
					pTarget->PlayEffect(m_strEffectName, false, true, false, m_EffectTime, m_TopMidBottom, m_EffectTimeMode);
				}
			}
		}
		else if (m_posType == eMagicPos_HeadTop)//头顶
		{
			if (pBattler == NULL)
			{
				return;
			}
			int x = 0;
			int y = 0;
			if (pBattler->IsEnemySide())
			{
				x = 100;
				y = -100;
			}
			else
			{
				x = -100;
				y = 100;
			}

			pBattler->PlayEffect(m_strEffectName, x, y, m_EffectTime, true, false, true, m_TopMidBottom, m_EffectTimeMode, m_iRot);
		}
		else if (m_posType == eMagicPos_Middle)
		{
			if (m_TopMidBottom >= 0)
				Nuclear::GetEngine()->GetWorld()->PlayEffect(m_strEffectName, Nuclear::XPEL_BATTLE_HEIGHT, m_pLocation.x, m_pLocation.y, 1, false, 2);
			else
				Nuclear::GetEngine()->GetWorld()->PlayEffect(m_strEffectName, Nuclear::XPEL_BATTLE_LOW, m_pLocation.x, m_pLocation.y, 1, false, 2);
		}
		else if (m_posType == eMagicPos_Banner)//banner
		{
			Nuclear::NuclearRect cp = gGetScene()->GetViewport();
			if (m_TopMidBottom >= 0)
				Nuclear::GetEngine()->GetWorld()->PlayEffect(m_strEffectName, Nuclear::XPEL_BATTLE_HEIGHT, cp.right - 280, cp.top + 250, 1, false, 2);
			else
				Nuclear::GetEngine()->GetWorld()->PlayEffect(m_strEffectName, Nuclear::XPEL_BATTLE_LOW, cp.right - 280, cp.top + 250, 1, false, 2);
		}
		else
		{
			if (m_LightType == 0)
			{
				if (m_Phantom > 0)
				{
					if (pPhantom)
					{
						pPhantom->PlayEffect(m_strEffectName, m_pLocation.x, m_pLocation.y, -1, true, false, true, m_TopMidBottom, m_EffectTimeMode, m_iRot);//挂在残影上的特效都是循环播放的,由残影的消亡来管理特效的消亡
					}
				}
				else
				{
					if (pBattler)
					{
						pBattler->PlayEffect(m_strEffectName, m_pLocation.x, m_pLocation.y, m_EffectTime, true, false, true, m_TopMidBottom, m_EffectTimeMode, m_iRot);
					}
				}
			}
			else if (0 < m_LightType && m_LightType < 100)
			{
				Battler* pUser = GetBattleManager()->FindBattlerByID(m_uBattler);
				if (pUser != NULL && pBattler != NULL)
				{
					pUser->CreateLightEffect(m_strEffectName, pBattler, m_LightType, m_apperaTime, m_disappearTime, m_totalTime);
				}
			}
		}
	}

	virtual void OnStageEnd()
	{

	}
};
class CSkillEffectNotify : public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};
class CActionStage : public SkillStage, public IActionListener
{
	eActionType  m_action;
	int             m_hCharacter;
	int             m_hTarget;
	sBattleDemoScript* m_pScript;
	int				m_TargetDir;
	int				m_Phantom;
	int				m_PhantomAlpha;
	bool			m_bHoldLastFrame;
	CActionStage();
public:
	CActionStage(int aCharacter, int aPhantom, int aPhantomAlpha, int aTarget, int aTargetDir, eActionType aAction, sBattleDemoScript* aScript, int aTicks, bool bHoldLastFrame = false) : SkillStage(aTicks)
	{
		m_bIsTriggerStage = true;
		m_bIsTriggered = false;
		m_hCharacter = aCharacter;
		m_hTarget = aTarget;
		m_action = aAction;
		m_pScript = aScript;
		m_TargetDir = aTargetDir;
		if (m_TargetDir != 2 && m_TargetDir != 4 && m_TargetDir != 6 && m_TargetDir != 8)
		{
			m_TargetDir = 2;
		}
		m_Phantom = aPhantom;
		m_PhantomAlpha = aPhantomAlpha;
		m_bHoldLastFrame = bHoldLastFrame;
	}
	virtual void OnUpdateAction(Nuclear::XPUSA_TYPE aType, eActionType aAction)
	{
		Battler* pCharacter = GetBattleManager()->FindBattlerByID(m_hCharacter);
		if (pCharacter == NULL)
		{
			return;
		}
		switch (aType)
		{
			//现在客户端出现卡战斗的情况也有可能是因为动作实际上并没有播出来，因此在出现DefaultAction 结束回调的时候，也要根据m_actionType进行回调
		case Nuclear::XPUSAT_PLAYACTIONFINISH:
		case Nuclear::XPUSAT_DEFAULTACTIONFINISH:
		{
			BattleIllusion* pPhantom = NULL;
			if (m_Phantom > 0)
			{
				pPhantom = pCharacter->FindPhantom(m_Phantom);
				if (!pPhantom)
				{
					m_bIsTriggered = true;
					return;
				}
			}

			printf("action stage call back....");
			if (pPhantom)
			{
				pPhantom->SetDefaultAction(eActionBattleStand);
				pPhantom->PlayAction(eActionBattleStand, 1.0f, eActionNull);
			}
			else
			{
				if (pCharacter->GetActionType() == m_action)
				{
					pCharacter->SetDefaultAction(eActionBattleStand);
					pCharacter->NewPlayAction(eActionBattleStand, 1.0f, true, eActionNull, true, true);
				}
			}
			m_bIsTriggered = true;
		}
		default:
			break;
		}
	}
	virtual void OnStageBegin()
	{
		Battler* pCharacter = GetBattleManager()->FindBattlerByID(m_hCharacter);
		Battler* pTarget = GetBattleManager()->FindBattlerByID(m_hTarget);
		if (pCharacter == NULL)
		{
			return;
		}
		pCharacter->StopHitMove();
		BattleIllusion* pPhantom = NULL;
		if (m_Phantom > 0)
		{
			pPhantom = pCharacter->FindAndAddPhantom(m_Phantom);
			pPhantom->SetAlpha(m_PhantomAlpha);
		}
		Nuclear::NuclearDirection TargetDir = Nuclear::XPDIR_BOTTOMRIGHT;//这里为了策划填表方便所以如果填写的是2,则人物实际上是向6的方向看,其它方向依此类推
		if (pTarget)
		{
			if (!pTarget->IsFriendSide())
			{
				if (m_TargetDir == 2)
				{
					TargetDir = Nuclear::XPDIR_TOPLEFT;
				}
				else if (m_TargetDir == 4)
				{
					TargetDir = Nuclear::XPDIR_TOPRIGHT;
				}
				else if (m_TargetDir == 6)
				{
					TargetDir = Nuclear::XPDIR_BOTTOMLEFT;
				}
				else if (m_TargetDir == 8)
				{
					TargetDir = Nuclear::XPDIR_BOTTOMRIGHT;
				}
			}
			else
			{
				if (m_TargetDir == 2)
				{
					TargetDir = Nuclear::XPDIR_BOTTOMRIGHT;
				}
				else if (m_TargetDir == 4)
				{
					TargetDir = Nuclear::XPDIR_BOTTOMLEFT;
				}
				else if (m_TargetDir == 6)
				{
					TargetDir = Nuclear::XPDIR_TOPRIGHT;
				}
				else if (m_TargetDir == 8)
				{
					TargetDir = Nuclear::XPDIR_TOPLEFT;
				}
			}
		}
		//play action and wait it finish
		if (pPhantom)
		{
			if (m_action != eActionNull)
			{
				if (m_bHoldLastFrame)
				{
					pPhantom->GetSprite()->SetHoldLastFrame(true);
				}
				else
				{
					pPhantom->GetSprite()->SetHoldLastFrame(false);
				}
				pPhantom->PlayAction(m_action, 1.0f, eActionNull);
			}
			if (pTarget)
			{
				pPhantom->SetDirection(TargetDir);
			}
			pPhantom->RegistListener(this);
		}
		else
		{
			if (m_action != eActionNull)
			{
				if (m_bHoldLastFrame)
				{
					pCharacter->GetSprite()->SetHoldLastFrame(true);
				}
				else
				{
					pCharacter->GetSprite()->SetHoldLastFrame(false);
				}
				pCharacter->NewPlayAction(m_action, 1.0f, true, eActionNull, true, true);
				if ((m_action == eActionAttack || m_action == eActionMagic1) && m_pScript)
				{
					pCharacter->ProcessAttackAction(&m_pScript->stMagicCasting);
					//只消耗一次
					m_pScript->stMagicCasting.HPConsume = 0;
					m_pScript->stMagicCasting.MPConsume = 0;
					m_pScript->stMagicCasting.SPConsume = 0;
				}
			}
			if (pTarget)
			{
				pCharacter->SetDirection(TargetDir);
			}
			pCharacter->RegistListener(this);
		}
	}
	virtual void OnStageEnd()
	{
		Battler* pCharacter = GetBattleManager()->FindBattlerByID(m_hCharacter);
		if (pCharacter == NULL)
		{
			return;
		}
		BattleIllusion* pPhantom = NULL;
		if (m_Phantom > 0)
		{
			pPhantom = pCharacter->FindPhantom(m_Phantom);
			if (pPhantom)
			{
				pPhantom->UnregistListener(this);
				pPhantom->SetDefaultAction(eActionBattleStand);
				pPhantom->PlayAction(eActionBattleStand, 1.0f, eActionNull);
			}
			else
			{
				return;//走到这里有可能是删除残影的阶段在此之前就已经执行了,导致残影对象已经析构所以不需要再继续执行了
			}
		}
		else
		{
			pCharacter->UnregistListener(this);
			if (pCharacter->GetActionType() == m_action)
			{
				pCharacter->SetDefaultAction(eActionBattleStand);
				pCharacter->NewPlayAction(eActionBattleStand, 1.0f, true, eActionNull, true, true);
			}
		}
	}
	virtual ~CActionStage()
	{
		if (!m_bIsStageComplete)
		{
			m_bIsStageComplete = true;
			OnStageEnd();
		}
	}
};
class CConsumeStage : public SkillStage
{
	CConsumeStage();
public:
	CConsumeStage(int aConsume, int aTicks) : SkillStage(aTicks)
	{
	}

	virtual void OnStageBegin()
	{
	}

	virtual void OnStageEnd()
	{
	}

};
class CAddBattlerStage : public SkillStage
{
	CAddBattlerStage();
public:
	CAddBattlerStage(int aTicks) :SkillStage(aTicks)
	{

	}

	void PushBattler(sBattlerData* pBattler)
	{
	}

	virtual void OnStageBegin()
	{
	}

	virtual void OnStageEnd()
	{
	}
};
class CMessageTipStage : public SkillStage
{
	int m_msgid;
	CMessageTipStage();
public:
	CMessageTipStage(int id, int aTicks) : SkillStage(aTicks){
		m_msgid = id;
	}

	virtual void OnStageBegin()
	{
		if (GetTipsManager())
			GetTipsManager()->AddMsgTips(m_msgid);
	}

	virtual void OnStageEnd()
	{
	}
};
class CSoundEffectStage : public SkillStage
{
    std::wstring m_strPath;
    CSoundEffectStage();
public:
    CSoundEffectStage(std::wstring aPath, int aTicks) : SkillStage(aTicks)
    , m_strPath(aPath)
    {}
    
    virtual void OnStageBegin()
    {
		size_t nSize = m_strPath.rfind(L"/");
		std::wstring strFileName = m_strPath.substr(nSize+1, m_strPath.length()-nSize-1);
		if (strFileName.size() <= 4) return;
        if (gGetGameConfigManager()&&gGetGameConfigManager()->isPlayEffect())
        {
			std::string newSoundRes;
			std::wstring newws;
			newws = LJFM::LJFMF::GetFullPathFileName(m_strPath);
			if (!newws.empty())
			{
				newSoundRes = StringCover::to_string(newws);
				CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffect(newSoundRes.c_str(), false);
			}
        }
    }    
    virtual void OnStageEnd(){
    }
};
class CMoveStage : public SkillStage, public IActionListener
{
	int             m_hMover;
	int             m_hTarget;
	Nuclear::NuclearLocation m_ptTarget;
	int				m_MoveType;
	int				m_bIsBack;
	int				m_TargetPos;//上8,下2,左4,右6
	int				m_Phantom;
	int				m_PhantomAlpha;
	bool			m_DelPhantom;
	int				m_iMoveOffsetX;
	int				m_iMoveOffsetY;
	eActionType	m_eActionType;
	bool			m_bMoveToTarget;
	int				m_iMoveTargetX;
	int				m_iMoveTargetY;
	int				m_iMoveTime;
	bool			m_bCheckMoveSide;
	int				m_iPhantomOffsetX;
	int				m_iPhantomOffsetY;

	float			m_fMoveDetX;
	float			m_fMoveDetY;
	CMoveStage();
public:
	CMoveStage(int aPMover, int aPhantom, int aPhantomAlpha, bool aDelPhantom, int aPTarget, int TargetPos, int aTicks, bool aBBack = false,
		int iMoveOffsetX = 0, int iMoveOffsetY = 0, eActionType actionType = eActionNull,
		bool bMoveToTarget = false, int iMoveTargetX = 0, int iMoveTargetY = 0, int iMoveTime = 0, bool bCheckMoveSide = false, int iPhantomOffsetX = 0, int iPhantomOffsetY = 0) : SkillStage(aTicks)
	{
		m_bIsTriggerStage = true;
		m_bIsTriggered = false;
		m_hMover = aPMover;
		m_hTarget = aPTarget;
		m_bIsBack = aBBack;
		m_TargetPos = TargetPos;
		if (m_TargetPos != 2 && m_TargetPos != 4 && m_TargetPos != 6 && m_TargetPos != 8 && m_TargetPos != 5 && m_TargetPos != 22
			&& TargetPos != 11 && TargetPos != 12 && TargetPos != 13)
		{
			m_TargetPos = 2;
		}
		m_Phantom = aPhantom;
		m_PhantomAlpha = aPhantomAlpha;
		m_DelPhantom = aDelPhantom;
		m_iMoveOffsetX = iMoveOffsetX;
		m_iMoveOffsetY = iMoveOffsetY;
		m_eActionType = actionType;
		m_bMoveToTarget = bMoveToTarget;
		m_iMoveTargetX = iMoveTargetX;
		m_iMoveTargetY = iMoveTargetY;
		m_iMoveTime = iMoveTime;
		m_bCheckMoveSide = bCheckMoveSide;
		m_iPhantomOffsetX = iPhantomOffsetX;
		m_iPhantomOffsetY = iPhantomOffsetY;
		m_fMoveDetX = 0.0f;
		m_fMoveDetY = 0.0f;
	}

	virtual void OnUpdateAction(Nuclear::XPUSA_TYPE aType, eActionType aAction)
	{
		if (m_DelPhantom == true)
		{
			return;
		}
		Battler* pMover = GetBattleManager()->FindBattlerByID(m_hMover);
		Battler* pTarget = GetBattleManager()->FindBattlerByID(m_hTarget);

		if (pMover == NULL)
		{
			return;
		}
		if (pMover->IsDeathEx())
		{
			return;
		}

		switch (aType)
		{
		case Nuclear::XPUSAT_MOVEINFLEXION:
		{
			BattleIllusion* pPhantom = NULL;
			if (m_Phantom > 0)
			{
				pPhantom = pMover->FindPhantom(m_Phantom);
				if (!pPhantom)
				{
					m_bIsTriggered = true;
					return;
				}
			}
			printf("move stage call back....");
			if (pTarget)
			{
				if (pPhantom)
				{
					pPhantom->TurnTo(pTarget);
				}
				else
				{
					pMover->TurnTo(pTarget);
				}
			}
			if (pPhantom)
			{
				pPhantom->SetDefaultAction(eActionBattleStand);
				pPhantom->PlayAction(eActionBattleStand, 1.0f, eActionNull);
			}
			else
			{
				pMover->SetDefaultAction(eActionBattleStand);
				pMover->NewPlayAction(eActionBattleStand, 1.0f, true, eActionNull, true, true);
			}
			m_bIsTriggered = true;
			break;
		}
		default:
			break;
		}
	}
	virtual void OnStageTick(int now, int delta)
	{
		SkillStage::OnStageTick(now, delta);

		Battler* pTarget = GetBattleManager()->FindBattlerByID(m_hTarget);
		if (m_iMoveOffsetX != 0 || m_iMoveOffsetY != 0 && !pTarget && !m_bIsBack)
		{
			Battler* pMover = GetBattleManager()->FindBattlerByID(m_hMover);
			if (pMover == NULL)
			{
				return;
			}
			if (pMover->IsDeathEx())
			{
				this->m_bIsStageComplete = true;
				return;
			}
			ComponentsObject* pPlayer = pMover;
			BattleIllusion* pPhantom = NULL;
			if (m_Phantom > 0)
			{
				pPhantom = pMover->FindPhantom(m_Phantom);
				if (!pPhantom)
				{
					return;
				}
				pPlayer = pPhantom;
			}
			Nuclear::NuclearLocation OldPos;
			if (m_bCheckMoveSide)
			{
				OldPos = pPlayer->GetLogicLocation();
			}
			else
			{
				OldPos = pPlayer->GetSprite()->GetLocation();
			}
			Nuclear::NuclearLocation TgtPos(m_iMoveTargetX, m_iMoveTargetY);

			bool XOK = false;
			bool YOK = false;

			Nuclear::NuclearLocation NewPos = OldPos;

			Nuclear::NuclearLocation Dist = TgtPos - OldPos;

			if (m_iMoveTime != 0)
			{
				int detTm = m_iMoveTime - m_iRealExecuteTicks;
				detTm = (detTm != 0) ? detTm : 1;
				m_iMoveOffsetX = Dist.x * 1000.0f / detTm;
				m_iMoveOffsetY = Dist.y * 1000.0f / detTm;
			}

			float _moveX = m_iMoveOffsetX * delta * 0.001f;
			float _moveY = m_iMoveOffsetY * delta * 0.001f;
			m_fMoveDetX += _moveX;
			m_fMoveDetY += _moveY;
			int _iX = m_fMoveDetX;
			int _iY = m_fMoveDetY;
			m_fMoveDetX -= _iX;
			m_fMoveDetY -= _iY;

			if (m_bMoveToTarget)
			{
				if (XOK == false)
				{
					if (abs(OldPos.x - m_iMoveTargetX) <= abs(_iX))
					{
						NewPos.x = m_iMoveTargetX;
						XOK = true;
					}
					else
					{
						NewPos.x += _iX;
					}
				}
				if (YOK == false)
				{
					if (abs(OldPos.y - m_iMoveTargetY) <= abs(_iY))
					{
						NewPos.y = m_iMoveTargetY;
						YOK = true;
					}
					else
					{
						NewPos.y += _iY;
					}
				}
				if (TgtPos.Dis2(OldPos) < NewPos.Dis2(OldPos))
				{
					NewPos.x = m_iMoveTargetX;
					NewPos.y = m_iMoveTargetY;
					XOK = true;
					YOK = true;
				}
			}
			else
			{
				NewPos.x = OldPos.x + _iX;
				NewPos.y = OldPos.y + _iY;
			}
			if (m_bCheckMoveSide)
			{
				pPlayer->SetLogicLocation(NewPos);
			}
			else
			{
				pPlayer->GetSprite()->SetLocation(NewPos);
			}
			if (m_Phantom == 0)
			{
				((Battler*)pPlayer)->UpdateHpBarLocation();
			}
			if (XOK && YOK)
			{
				m_bIsStageComplete = true;
			}
		}
	}
	virtual void OnStageBegin()
	{
		Battler* pMover = GetBattleManager()->FindBattlerByID(m_hMover);
		Battler* pTarget = GetBattleManager()->FindBattlerByID(m_hTarget);

		if (pMover == NULL)
		{
			return;
		}
		if (pMover->IsDeathEx())
		{
			m_bIsStageComplete = true;
			return;
		}
		pMover->StopTeleport();

		BattleIllusion* pPhantom = NULL;
		if (m_Phantom != 0)
		{
			if (m_DelPhantom == true)
			{
				pMover->DelPhantom(m_Phantom);
				m_bIsStageComplete = true;
				return;
			}
			else
			{
				pPhantom = pMover->FindAndAddPhantom(m_Phantom);
				pPhantom->SetAlpha(m_PhantomAlpha);
				Nuclear::NuclearLocation CurPos = pPhantom->GetLogicLocation();
				pPhantom->SetLogicLocation(Nuclear::NuclearLocation(CurPos.x + m_iPhantomOffsetX, CurPos.y + m_iPhantomOffsetY));
			}
		}

		//play action and wait it finish
		if (pTarget)
		{
			if (m_TargetPos == 2)
			{
				m_ptTarget = pTarget->GetAttackerPosition();
			}
			else if (m_TargetPos == 4)
			{
				m_ptTarget = pTarget->GetAttackerSidePosition(false);
			}
			else if (m_TargetPos == 6)
			{
				m_ptTarget = pTarget->GetAttackerSidePosition(true);
			}
			else if (m_TargetPos == 8)
			{
				m_ptTarget = pTarget->GetAttackerPositionBehind();
			}
			else if (m_TargetPos == 5)
			{
				m_ptTarget = pTarget->GetLocation();
			}
			else if (m_TargetPos == 11)
			{
				m_ptTarget = GetSidePos(pMover->IsFriendSide());
			}
			else if (m_TargetPos == 12)
			{
				m_ptTarget = GetSidePos(pMover->IsEnemySide());
			}
			else if (m_TargetPos == 13)
			{
				Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
				m_ptTarget = Nuclear::NuclearLocation((cp.right + cp.left) / 2, (cp.top + cp.bottom) / 2);
			}
			else if (m_TargetPos == 22)
			{
				m_ptTarget = pTarget->GetProtecterPosition();
			}
			if (pPhantom)
			{
				Nuclear::NuclearLocation ptCur = pPhantom->GetLocation();
				if (m_ptTarget.Dis2(ptCur) < 10)
				{
					pPhantom->SetLocation(m_ptTarget);
					m_bIsStageComplete = true;
					return;
				}
				pPhantom->RunTo(m_ptTarget);
			}
			else
			{
				Nuclear::NuclearLocation ptCur = pMover->GetLocation();
				if (m_ptTarget.Dis2(ptCur) < 10)
				{
					pMover->SetLocation(m_ptTarget);
					m_bIsStageComplete = true;
					return;
				}
				pMover->RunTo(m_ptTarget);
			}
		}
		else
		{
			if (pPhantom)
			{
				if (m_eActionType != eActionNull)
				{
					pPhantom->PlayAction(m_eActionType, 1.0f, eActionNull);
					pPhantom->SetDefaultAction(m_eActionType);
				}
			}
			else
			{
				if (m_eActionType != eActionNull)
				{
					pMover->NewPlayAction(m_eActionType, 1.0f, true, eActionNull, true, true);
					pMover->SetDefaultAction(m_eActionType);
				}
				Nuclear::NuclearLocation pos = Nuclear::NuclearWorldLogicCoord::logic2world(Nuclear::NuclearLocation(m_iMoveTargetX, m_iMoveTargetY));
				pMover->SetTargetPos(pos.x, pos.y);
			}
		}
		if (m_bIsBack)
		{
			if (pPhantom)
			{
				pPhantom->RunBack();
			}
			else
			{
				pMover->RunBack();
			}
		}
		if (m_iMoveOffsetX != 0 || m_iMoveOffsetY != 0 && !pTarget && !m_bIsBack)
		{
			return;
		}

		if (pPhantom)
		{
			pPhantom->RegistListener(this);
		}
		else
		{
			pMover->RegistListener(this);
		}
	}
	virtual void OnStageEnd()
	{
		if (m_DelPhantom == true)
		{
			return;
		}
		Battler* pMover = GetBattleManager()->FindBattlerByID(m_hMover);
		Battler* pTarget = GetBattleManager()->FindBattlerByID(m_hTarget);
		BattleIllusion* pPhantom = NULL;
		ComponentsObject* pPlayer = pMover;
		if (pMover == NULL)
		{
			return;
		}
		if (pMover->IsDeathEx())
		{
			return;
		}
		if (m_Phantom > 0)
		{
			pPhantom = pMover->FindPhantom(m_Phantom);
			if (pPhantom)
			{
				pPlayer = pPhantom;
			}
			else
			{
				return;//走到这里有可能是删除残影的阶段在此之前就已经执行了,导致残影对象已经析构所以不需要再继续执行了
			}
		}
		if (!pTarget && m_bIsBack)
		{
			if (pMover->IsFriendSide())
			{
				pMover->SetDirection(Nuclear::XPDIR_TOPLEFT);
			}
			else if (pMover->IsEnemySide())
			{
				pMover->SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
			}
		}
		if (m_bMoveToTarget)
		{
			Nuclear::NuclearLocation loc;
			loc.x = m_iMoveTargetX;
			loc.y = m_iMoveTargetY;
			if (m_bCheckMoveSide)
			{
				pPlayer->SetLogicLocation(loc);
			}
			else
			{
				pPlayer->GetSprite()->SetLocation(loc);
			}
		}
		if (m_iMoveOffsetX != 0 || m_iMoveOffsetY != 0 && !pTarget && !m_bIsBack)
		{
			return;
		}
		if (pPhantom)
		{
			pPhantom->UnregistListener(this);
			pPhantom->SetDefaultAction(eActionBattleStand);
			pPhantom->PlayAction(eActionBattleStand, 1.0f, eActionNull);
		}
		else
		{
			pMover->UnregistListener(this);
			pMover->SetDefaultAction(eActionBattleStand);
			pMover->NewPlayAction(eActionBattleStand, 1.0f, true, eActionNull, true, true);
		}
	}
	virtual ~CMoveStage()
	{
		if (!m_bIsStageComplete)
		{
			m_bIsStageComplete = true;
			OnStageEnd();
		}
	}
};

static void BuildEffectName(bool bIsFriend, std::wstring & aEffectName)
{
	if (aEffectName.find(L"_") != std::wstring::npos)
	{
		if (bIsFriend)
		{
			aEffectName += L"2";
		}
		else
		{
			aEffectName += L"1";
		}
	}
}
static Skill* BuildItemAttack(Skill* pSkill, sBattleDemoScript& aScript, int SubSkillIndex)
{
	Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);

	if (pSender == NULL) return pSkill;

	CActionStage* pActionStage = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, 0, 2, eActionAttack, NULL, 2000);
	pSkill->PushStageBack(pActionStage);

	CResultStage* pResultStage = new CResultStage(aScript.stMagicCasting.AttackerID, aScript.listResults[SubSkillIndex].listResults[0].TargetID, 0, 2, pSkill, 0, 1000);
	pSkill->PushStageBack(pResultStage);

	return pSkill;
}
static CResultStage* BuildNormalAttackResult(Skill* pSkill, int iAttacker, int iTarget, int ResultIndex, int DelayApp)
{
	CResultStage* pStage = new CResultStage(iAttacker, iTarget, 200, 2, pSkill, ResultIndex, 200 + DelayApp);
	pStage->SetDelayTicks(DelayApp);
	return pStage;
}
static ParallelSkillStage* BuildNormalAttackAttackAndResult(Skill* pSkill, int iAttacker, int iTarget, int iShapeID, int ResultIndex, int DelayApp, int iDir)
{
	ParallelSkillStage* pParallel = new ParallelSkillStage(10000);
	std::wstring npcshape_attack = GetTableWStringFromLua("npc.cnpcshape", iShapeID, "attack");
	if (!npcshape_attack.empty())
	{
		CSoundEffectStage* pStage = new CSoundEffectStage(L"/sound/monster/" + npcshape_attack + L".ogg", 10);
		pStage->SetDelayTicks(DelayApp);
		pParallel->AddStage(pStage);
	}
	{//Action
		CActionStage* pStage = new CActionStage(iAttacker, 0, 0, iTarget, iDir, eActionAttack, NULL, 1200);
		pStage->SetDelayTicks(DelayApp);
		CSoundEffectStage* pSound = new CSoundEffectStage(L"/sound/skill/mt3-jc-pg.ogg", 100);
		pSound->SetDelayTicks(DelayApp+400);
		pParallel->AddStage(pSound);
		pParallel->AddStage(pStage);
	}
	{//Result
		CResultStage* pStage = BuildNormalAttackResult(pSkill, iAttacker, iTarget, ResultIndex, 500 + DelayApp);
		pParallel->AddStage(pStage);
	}
	pParallel->SetDelayTicks(DelayApp);
	return pParallel;
}
static ParallelSkillStage* BuildNormalAttackMove(int iAttacker, int iTarget, int iProtecter, int iPhantom, int DelayApp)
{
	ParallelSkillStage* pParallel = new ParallelSkillStage(10000);
	if (iAttacker > 0 && iPhantom == 0)
	{
		CMoveStage* pStage = new CMoveStage(iAttacker, 0, 0, false, iTarget, 2, 2000);
		pStage->SetDelayTicks(DelayApp);
		pParallel->AddStage(pStage);
	}
	else if (iPhantom > 0)
	{
		Battler* pBattler = GetBattleManager()->FindBattlerByID(iAttacker);
		Battler* pTarget = GetBattleManager()->FindBattlerByID(iTarget);
		if (!pTarget)
		{
			return pParallel;
		}
		int arrowHeight = 50;
		int iShapeID = pBattler->GetShapeID();
		if (pBattler->GetBattlerData().BattlerType == eBattlerCharacter)
		{
			if (pBattler->GetBattlerData().school == eSchoolLieRen)
			{
				std::wstring modelname = GetTableWStringFromLua("npc.cnpcshape", iShapeID, "shape");
				if (modelname == L"mt_nvde")
					arrowHeight = 130;
			}
		}
		Nuclear::NuclearLocation LocalPos = pBattler->GetLogicLocation();
		Nuclear::NuclearLocation TargetPos = pTarget->GetLogicLocation();
		LocalPos.y = LocalPos.y - arrowHeight;
		TargetPos.y = TargetPos.y - 50;
		int iMoveTime = 180;
		Nuclear::NuclearLocation Dist = TargetPos - LocalPos;
		int iMoveOffsetX = Dist.x * 1000.0f / iMoveTime;//除以几就是在几毫秒内走完
		int iMoveOffsetY = Dist.y * 1000.0f / iMoveTime;
		int iMoveTargetX = TargetPos.x;
		int iMoveTargetY = TargetPos.y;
		CMoveStage* pStage = new CMoveStage(iAttacker, iPhantom, 0, false, 0, 0, 2000, false, iMoveOffsetX, iMoveOffsetY, eActionNull, true,
			iMoveTargetX, iMoveTargetY, iMoveTime, true, 0, -arrowHeight);
		pStage->SetDelayTicks(DelayApp);
		pParallel->AddStage(pStage);
	}
	if (iProtecter > 0)
	{
		CMoveStage* pStage = new CMoveStage(iProtecter, 0, 0, false, iTarget, 22, 2000);
		pStage->SetDelayTicks(DelayApp);
		pParallel->AddStage(pStage);
	}
	pParallel->SetDelayTicks(DelayApp);
	return pParallel;
}
static Skill* BuildNormalAttack(Skill* pSkill, sBattleDemoScript& aScript, int SubSkillIndex)
{
	int iAttacker = aScript.stMagicCasting.AttackerID;
	Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);
	if (pSender == NULL)
	{
		return pSkill;
	}
	if (aScript.stMagicCasting.msgid &&
		(GetBattleManager()->GetMainBattleCharID() == aScript.stMagicCasting.AttackerID ||
		GetBattleManager()->GetMainBattlePetID() == aScript.stMagicCasting.AttackerID))
	{
		CMessageTipStage* pStage = new CMessageTipStage(aScript.stMagicCasting.msgid, 10);
		pSkill->PushStageBack(pStage);
	}
	int iLastResultType = 0;
	ParallelSkillStage* pLastNormalAttackParallelStage = NULL;
	for (unsigned i = 0; i < aScript.listResults[SubSkillIndex].listResults.size(); i++)
	{
		int iPhantom = 0;
		int iTarget = aScript.listResults[SubSkillIndex].listResults[i].TargetID;
		int iProtecter = aScript.listResults[SubSkillIndex].listResults[i].ProtecterID;
		int iResultType = aScript.listResults[SubSkillIndex].listResults[i].ResultType;
		int iShapeID = pSender->GetShapeID();
		int NS_nearorfar = GetTableNumberFromLua("npc.cnpcshape", iShapeID, "nearorfar");

		Battler* pTarget = GetBattleManager()->FindBattlerByID(iTarget);
		if (pTarget == NULL && iResultType != 6)	continue;
		int iNearOrFar = NS_nearorfar;
		int arrowHeight = 50;
		if (pSender->GetBattlerData().BattlerType == eBattlerCharacter)
		{
			if (pSender->GetBattlerData().school == eSchoolLieRen)
			{
				iNearOrFar = 1;
				std::wstring modelname = GetTableWStringFromLua("npc.cnpcshape", iShapeID, "shape");
				if (modelname == L"mt_nvde")
					arrowHeight = 130;
			}
			else if (pSender->GetBattlerData().school == eSchoolDeLuYi)
				iNearOrFar = 0;
			else if (pSender->GetBattlerData().school == eSchoolDaoZei)
				iNearOrFar = 0;

			if (pSender->GetSpriteComponent(eSprite_Weapon)==0)
				iNearOrFar = 0;
		}

		ParallelSkillStage* pParallelBack = NULL;
		if (iResultType == 0)//普通
		{
			if (iNearOrFar == 0)//近程
			{
				pSkill->PushStageBack(BuildNormalAttackMove(iAttacker, iTarget, iProtecter, iPhantom, 0));//攻击者和保护者一起跑
				pLastNormalAttackParallelStage = BuildNormalAttackAttackAndResult(pSkill, iAttacker, iTarget, iShapeID, i, 0, 2);
				pSkill->PushStageBack(pLastNormalAttackParallelStage);
			}
			else if (iNearOrFar == 1)//远程
			{
				ParallelSkillStage* pParallel = new ParallelSkillStage(10000);
				pSkill->PushStageBack(pParallel);
				int TargetDir = 2;
				if (pSender->IsFriendSide())
				{
					TargetDir = pTarget->IsFriendSide() ? 8 : 2;
				}
				else if (pSender->IsEnemySide())
				{
					TargetDir = pTarget->IsFriendSide() ? 2 : 8;
				}
				CActionStage* pActionStage = new CActionStage(iAttacker, 0, 0, iTarget, TargetDir, eActionAttack, NULL, 1200);
				pParallel->AddStage(pActionStage);
				CSoundEffectStage* pSound = new CSoundEffectStage(L"/sound/skill/mt3-yc-pg.ogg", 100);
				pSound->SetDelayTicks(500);
				pParallel->AddStage(pSound);
				iPhantom = 1;
				std::wstring effectname = L"geffect/skill/mt_yuanchengpugong/mt_yuanchengpugong_";
				BuildEffectName(pSender->IsFriendSide(), effectname);
				int iRot = 0;
				float fRot = 0.0f;
				Nuclear::NuclearFPoint FPSrc = pSender->GetLocation().ToFPOINT();
				Nuclear::NuclearFPoint FPDst = pTarget->GetLocation().ToFPOINT();
				FPSrc.y = FPSrc.y - arrowHeight;
				FPDst.y = FPDst.y - 50;
				Nuclear::NuclearFPoint FPSub = FPDst - FPSrc;
				fRot = atan2(FPSub.y, FPSub.x) * 180 / 3.1415926;
				iRot = fRot;
				while (iRot < 0)
				{
					iRot += 360;
				}
				while (iRot > 360)
				{
					iRot -= 360;
				}
				if (pSender->IsFriendSide())
				{
					iRot += 180;
				}
				ParallelSkillStage* pMoveStage = (ParallelSkillStage*)BuildNormalAttackMove(iAttacker, iTarget, iProtecter, iPhantom, 500);//飞行道具和保护者一起跑
				pParallel->AddStage(pMoveStage);
				CEffectStage* pEffectStage = new CEffectStage(iAttacker, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
					effectname, eMagicPos_Static, false, false, pSkill, 1, 1, 1, 0, iRot);
				pEffectStage->SetDelayTicks(500);
				pParallel->AddStage(pEffectStage);
				CMoveStage* pMoveBackStage = new CMoveStage(iAttacker, iPhantom, 0, true, 0, 2, 2000, true);
				pMoveBackStage->SetDelayTicks(730);
				pParallel->AddStage(pMoveBackStage);
				CResultStage* pResultStage = new CResultStage(iAttacker, iTarget, 300, 2, pSkill, i, 700);
				pResultStage->SetDelayTicks(730);
				pParallel->AddStage(pResultStage);
			}
			if (iProtecter > 0)
			{
				pParallelBack = new ParallelSkillStage(2000);
				pSkill->PushStageBack(pParallelBack);
				CMoveStage* pMoveBackStage = NULL;
				pMoveBackStage = new CMoveStage(iProtecter, 0, 0, false, 0, 2, 2000, true);
				pParallelBack->AddStage(pMoveBackStage);
			}
		}
		else if (iResultType == 1)//反击
		{
			int idir = (pSender->IsFriendSide() == pTarget->IsFriendSide()) ? 8 : 2;
			ParallelSkillStage* pParallel = BuildNormalAttackAttackAndResult(pSkill, iTarget, iAttacker, iShapeID, i, 200, idir);
			CEffectStage* pEffectStage = new CEffectStage(iTarget, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
				MHSD_UTILS::GetEffectPath(11053), eMagicPos_Static, false, false, pSkill, 1, 1);
			pEffectStage->SetDelayTicks(400);
			pParallel->AddStage(pEffectStage);
			pSkill->PushStageBack(pParallel);
		}
		else if (iResultType == 2)//连击
		{
			if (iNearOrFar == 0)//近程
			{
				if (iProtecter > 0)
				{
					CMoveStage* pStage = new CMoveStage(iProtecter, 0, 0, false, iTarget, 22, 2000);
					pSkill->PushStageBack(pStage);
				}
				ParallelSkillStage* pParallel = BuildNormalAttackAttackAndResult(pSkill, iAttacker, iTarget, iShapeID, i, 0, 2);
				CEffectStage* pEffectStage = new CEffectStage(iAttacker, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
					MHSD_UTILS::GetEffectPath(11055), eMagicPos_Static, false, false, pSkill, 1, 1);
				pEffectStage->SetDelayTicks(0);
				pParallel->AddStage(pEffectStage);
				pSkill->PushStageBack(pParallel);
			}
			else if (iNearOrFar == 1)//远程
			{
				ParallelSkillStage* pParallel = new ParallelSkillStage(10000);
				pSkill->PushStageBack(pParallel);
				int TargetDir = 2;
				if (pSender->IsFriendSide())
				{
					TargetDir = pTarget->IsFriendSide() ? 8 : 2;
				}
				else if (pSender->IsEnemySide())
				{
					TargetDir = pTarget->IsFriendSide() ? 2 : 8;
				}
				CActionStage* pActionStage = new CActionStage(iAttacker, 0, 0, iTarget, TargetDir, eActionAttack, NULL, 1200);			
				pParallel->AddStage(pActionStage);
				CSoundEffectStage* pSound = new CSoundEffectStage(L"/sound/skill/mt3-yc-pg.ogg", 100);
				pSound->SetDelayTicks(500);
				pParallel->AddStage(pSound);
				iPhantom = 1;
				std::wstring effectname = L"geffect/skill/mt_yuanchengpugong/mt_yuanchengpugong_";
				BuildEffectName(pSender->IsFriendSide(), effectname);
				int iRot = 0;
				float fRot = 0.0f;
				Nuclear::NuclearFPoint FPSrc = pSender->GetLocation().ToFPOINT();
				Nuclear::NuclearFPoint FPDst = pTarget->GetLocation().ToFPOINT();
				FPSrc.y = FPSrc.y - arrowHeight;
				FPDst.y = FPDst.y - 50;
				Nuclear::NuclearFPoint FPSub = FPDst - FPSrc;
				fRot = atan2(FPSub.y, FPSub.x) * 180 / 3.1415926;
				iRot = fRot;
				while (iRot < 0)
				{
					iRot += 360;
				}
				while (iRot > 360)
				{
					iRot -= 360;
				}
				if (pSender->IsFriendSide())
				{
					iRot += 180;
				}
				ParallelSkillStage* pMoveStage = (ParallelSkillStage*)BuildNormalAttackMove(iAttacker, iTarget, iProtecter, iPhantom, 500);//飞行道具和保护者一起跑
				pParallel->AddStage(pMoveStage);
				CEffectStage* pEffectStage = new CEffectStage(iAttacker, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
					effectname, eMagicPos_Static, false, false, pSkill, 1, 1, 1, 0, iRot);
				pEffectStage->SetDelayTicks(500);
				pParallel->AddStage(pEffectStage);
				CEffectStage* pEffectStage2 = new CEffectStage(iAttacker, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
					MHSD_UTILS::GetEffectPath(11055), eMagicPos_Static, false, false, pSkill, 1, 1);
				pEffectStage->SetDelayTicks(0);
				pParallel->AddStage(pEffectStage2);
				CMoveStage* pMoveBackStage = new CMoveStage(iAttacker, iPhantom, 0, true, 0, 2, 2000, true);
				pMoveBackStage->SetDelayTicks(730);
				pParallel->AddStage(pMoveBackStage);
				CResultStage* pResultStage = new CResultStage(iAttacker, iTarget, 300, 2, pSkill, i, 700);
				pResultStage->SetDelayTicks(730);
				pParallel->AddStage(pResultStage);
			}
			if (iProtecter > 0)
			{
				pParallelBack = new ParallelSkillStage(2000);
				pSkill->PushStageBack(pParallelBack);
				CMoveStage* pMoveBackStage = NULL;
				pMoveBackStage = new CMoveStage(iProtecter, 0, 0, false, 0, 2, 2000, true);
				pParallelBack->AddStage(pMoveBackStage);
			}
		}
		else if (iResultType == 3)//追击
		{
			if (iNearOrFar == 0)//近程
			{
				pSkill->PushStageBack(BuildNormalAttackMove(iAttacker, iTarget, iProtecter, iPhantom, 0));
				ParallelSkillStage* pAttackStage = BuildNormalAttackAttackAndResult(pSkill, iAttacker, iTarget, iShapeID, i, 0, 2);
				CEffectStage* pEffectStage = new CEffectStage(iAttacker, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
					MHSD_UTILS::GetEffectPath(11054), eMagicPos_Static, false, false, pSkill, 1, 1);
				pAttackStage->SetDelayTicks(0);
				pAttackStage->AddStage(pEffectStage);
				pSkill->PushStageBack(pAttackStage);
			}
			else if (iNearOrFar == 1)//远程
			{
				ParallelSkillStage* pParallel = new ParallelSkillStage(10000);
				pSkill->PushStageBack(pParallel);
				int TargetDir = 2;
				if (pSender->IsFriendSide())
				{
					TargetDir = pTarget->IsFriendSide() ? 8 : 2;
				}
				else if (pSender->IsEnemySide())
				{
					TargetDir = pTarget->IsFriendSide() ? 2 : 8;
				}
				CActionStage* pActionStage = new CActionStage(iAttacker, 0, 0, iTarget, TargetDir, eActionAttack, NULL, 1200);
				pParallel->AddStage(pActionStage);
				CSoundEffectStage* pSound = new CSoundEffectStage(L"/sound/skill/mt3-yc-pg.ogg", 100);
				pSound->SetDelayTicks(500);
				pParallel->AddStage(pSound);
				iPhantom = 1;
				std::wstring effectname = L"geffect/skill/mt_yuanchengpugong/mt_yuanchengpugong_";
				BuildEffectName(pSender->IsFriendSide(), effectname);
				int iRot = 0;
				float fRot = 0.0f;
				Nuclear::NuclearFPoint FPSrc = pSender->GetLocation().ToFPOINT();
				Nuclear::NuclearFPoint FPDst = pTarget->GetLocation().ToFPOINT();
				FPSrc.y = FPSrc.y - arrowHeight;
				FPDst.y = FPDst.y - 50;
				Nuclear::NuclearFPoint FPSub = FPDst - FPSrc;
				fRot = atan2(FPSub.y, FPSub.x) * 180 / 3.1415926;
				iRot = fRot;
				while (iRot < 0)
				{
					iRot += 360;
				}
				while (iRot > 360)
				{
					iRot -= 360;
				}
				if (pSender->IsFriendSide())
				{
					iRot += 180;
				}
				ParallelSkillStage* pMoveStage = (ParallelSkillStage*)BuildNormalAttackMove(iAttacker, iTarget, iProtecter, iPhantom, 500);//飞行道具和保护者一起跑
				pParallel->AddStage(pMoveStage);
				CEffectStage* pEffectStage = new CEffectStage(iAttacker, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
					effectname, eMagicPos_Static, false, false, pSkill, 1, 1, 1, 0, iRot);
				pEffectStage->SetDelayTicks(500);
				pParallel->AddStage(pEffectStage);
				CEffectStage* pEffectStage2 = new CEffectStage(iAttacker, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
					MHSD_UTILS::GetEffectPath(11054), eMagicPos_Static, false, false, pSkill, 1, 1);
				pEffectStage2->SetDelayTicks(730);
				pParallel->AddStage(pEffectStage2);
				CMoveStage* pMoveBackStage = new CMoveStage(iAttacker, iPhantom, 0, true, 0, 2, 2000, true);
				pMoveBackStage->SetDelayTicks(730);
				pParallel->AddStage(pMoveBackStage);
				CResultStage* pResultStage = new CResultStage(iAttacker, iTarget, 300, 2, pSkill, i, 700);
				pResultStage->SetDelayTicks(730);
				pParallel->AddStage(pResultStage);
			}
			if (iProtecter > 0)
			{
				pParallelBack = new ParallelSkillStage(2000);
				pSkill->PushStageBack(pParallelBack);
				CMoveStage* pMoveBackStage = NULL;
				pMoveBackStage = new CMoveStage(iProtecter, 0, 0, false, 0, 2, 2000, true);
				pParallelBack->AddStage(pMoveBackStage);
			}
		}
		else if (iResultType == 4)//溅射
		{
			if ((iLastResultType == 0 || iLastResultType == 4) && pLastNormalAttackParallelStage)
			{
				CResultStage* pStage = BuildNormalAttackResult(pSkill, iAttacker, iTarget, i, 500);
				pLastNormalAttackParallelStage->AddStage(pStage);
				CEffectStage* pEffectStage = new CEffectStage(iAttacker, iPhantom, 0, Nuclear::NuclearLocation(0, 0),
					MHSD_UTILS::GetEffectPath(11051), eMagicPos_Static, false, false, pSkill, 1, 1);
				pEffectStage->SetDelayTicks(500);
				pLastNormalAttackParallelStage->AddStage(pEffectStage);
			}
		}
		else if (iResultType == 6)//去buffer
		{
			CResultStage* pStage = BuildNormalAttackResult(pSkill, iAttacker, iTarget, i, 0);
			pSkill->PushStageBack(pStage);
		}
		iLastResultType = iResultType;
		if (iLastResultType != 0 && iLastResultType != 4)
		{
			pLastNormalAttackParallelStage = NULL;
		}

		if (iNearOrFar == 0)//近程
		{
			if (i == aScript.listResults[SubSkillIndex].listResults.size() - 1)
			{//Back
				CMoveStage* pMoveBackStage = NULL;
				pMoveBackStage = new CMoveStage(iAttacker, 0, 0, false, 0, 2, 2000, true);
				if (pParallelBack)
				{
					pParallelBack->AddStage(pMoveBackStage);
				}
				else
				{
					pSkill->PushStageBack(pMoveBackStage);
				}
			}
		}
	}
	return pSkill;
}
static Skill* BuildRunaway(Skill* pSkill, sBattleDemoScript& aScript, int SubSkillIndex)
{
	Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);

	if (pSender == NULL)
	{
		return pSkill;
	}

	//主人逃跑，宠物也会跟着逃跑
	Battler* pBattlerPet = NULL;
	if (pSender->GetBattlerType() == eBattlerCharacter)
	{
		pBattlerPet = GetBattleManager()->FindBattlerByID(pSender->GetBattleID() + 5);
	}

	int iTarget = 0;
	if (aScript.listResults[SubSkillIndex].listResults.size() > 0)
	{
		iTarget = aScript.listResults[SubSkillIndex].listResults[0].TargetID;
	}

	if (aScript.listResults[SubSkillIndex].listResults[0].eTargetResult == 0)//逃跑失败
	{
		CActionStage* pActionStage1 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 2, eActionRunaway, NULL, 1000);
		CActionStage* pActionStage2 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 4, eActionRunaway, NULL, 2);
		CActionStage* pActionStage3 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 8, eActionRunaway, NULL, 2);
		CActionStage* pActionStage4 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 6, eActionRunaway, NULL, 2);
		CActionStage* pActionStage5 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 2, eActionRunaway, NULL, 2);
		CActionStage* pActionStage6 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 4, eActionRunaway, NULL, 2);
		CActionStage* pActionStage7 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 8, eActionRunaway, NULL, 2);
		CActionStage* pActionStage8 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 6, eActionRunaway, NULL, 2);
		CActionStage* pActionStage9 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 8, eActionBattleStand, NULL, 2);

		CSoundEffectStage* pSound = new CSoundEffectStage(L"/sound/skill/mt3-taopaoshibai.ogg", 100);
		pSkill->PushStageBack(pSound);
		{
			pSkill->PushStageBack(pActionStage1);
		}

		if (aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattleCharID() || aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattlePetID())
		{
			CMessageTipStage* pStage = new CMessageTipStage(144760, 10);
			pSkill->PushStageBack(pStage);
		}

		pSkill->PushStageBack(pActionStage2);
		pSkill->PushStageBack(pActionStage3);
		pSkill->PushStageBack(pActionStage4);
		pSkill->PushStageBack(pActionStage5);
		pSkill->PushStageBack(pActionStage6);
		pSkill->PushStageBack(pActionStage7);
		pSkill->PushStageBack(pActionStage8);

		{
			pSkill->PushStageBack(pActionStage9);
		}
	}
	else//逃跑成功
	{
		CActionStage* pActionStage1 = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, iTarget, 2, eActionRunaway, NULL, 1000);
		Battler* pBattler = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);

		int MoveOffsetX = 900;
		int MoveOffsetY = 600;

		if (!pBattler->IsFriendSide())
		{
			MoveOffsetX *= -1;
			MoveOffsetY *= -1;
		}
		CSoundEffectStage* pSound = new CSoundEffectStage(L"/sound/skill/mt3-taopaochenggong.ogg", 100);
		CMoveStage* pMoveStage = new CMoveStage(aScript.stMagicCasting.AttackerID, 0, 0, false, 0, 0, 400, false, MoveOffsetX, MoveOffsetY, eActionRunaway);

		if (pBattlerPet != NULL)
		{
			ParallelSkillStage* pParallel = new ParallelSkillStage(1400);
			pParallel->AddStage(pSound);
			pParallel->AddStage(pActionStage1);
			pActionStage1 = new CActionStage(pSender->GetBattleID() + 5, 0, 0, iTarget, 2, eActionRunaway, NULL, 1000);
			pParallel->AddStage(pActionStage1);
			pSkill->PushStageBack(pParallel);

			pParallel = new ParallelSkillStage(1400);
			pParallel->AddStage(pMoveStage);
			pMoveStage = new CMoveStage(pSender->GetBattleID() + 5, 0, 0, false, 0, 0, 400, false, MoveOffsetX, MoveOffsetY, eActionRunaway);
			pParallel->AddStage(pMoveStage);
			pSkill->PushStageBack(pParallel);
		}
		else
		{
			pSkill->PushStageBack(pSound);
			pSkill->PushStageBack(pActionStage1);
			pSkill->PushStageBack(pMoveStage);
		}

	}

	for (int i = 0; i < aScript.listResults[SubSkillIndex].listResults.size();i++)
	{
		CResultStage* pResultStage = new CResultStage(aScript.stMagicCasting.AttackerID, aScript.listResults[SubSkillIndex].listResults[i].TargetID, 0, 2, pSkill, i, 2);
		pSkill->PushStageBack(pResultStage);
	}

	return pSkill;
}
static CMoveStage* BuildCatchMove(int iBattlerID, Nuclear::NuclearLocation LocalPos, Nuclear::NuclearLocation TargetPos, int iMoveTime = 1000, int iStageTime = 1000, eActionType actionType = eActionNull)
{
	Nuclear::NuclearLocation Dist = TargetPos - LocalPos;
	int iMoveOffsetX = Dist.x * 1000.0f / iMoveTime;//除以几就是在几毫秒内走完
	int iMoveOffsetY = Dist.y * 1000.0f / iMoveTime;
	int iMoveTargetX = TargetPos.x;
	int iMoveTargetY = TargetPos.y;
	CMoveStage* pStage = new CMoveStage(iBattlerID, 0, 0, false, 0, 0, iStageTime, false, iMoveOffsetX, iMoveOffsetY, actionType, true, iMoveTargetX, iMoveTargetY, iMoveTime, true);
	return pStage;
}
static Skill* BuildCatch(Skill* pSkill, sBattleDemoScript& aScript, int SubSkillIndex)
{
	int iBattlerID = aScript.stMagicCasting.AttackerID;
	Battler* pBattler = GetBattleManager()->FindBattlerByID(iBattlerID);
	if (pBattler == NULL)
	{
		return pSkill;
	}
	bool bCatchOK = false;
	int iTargetID = 0;
	if (aScript.listResults[SubSkillIndex].listResults.size() != 1)
	{
		return pSkill;
	}
	else
	{
		iTargetID = aScript.listResults[SubSkillIndex].listResults[0].TargetID;
		if (aScript.listResults[SubSkillIndex].listResults[0].eTargetResult & eBattleResult_Seized)
		{
			bCatchOK = true;
		}
	}
	Battler* pTarget = GetBattleManager()->FindBattlerByID(iTargetID);
	if (!pTarget)
	{
		return pSkill;
	}
	Nuclear::NuclearLocation LocalPos = pBattler->GetLogicLocation();
	Nuclear::NuclearLocation TargetPos = Nuclear::NuclearWorldLogicCoord::world2logic(pTarget->GetAttackerPosition());

	CMoveStage* pMoveStage = BuildCatchMove(iBattlerID, LocalPos, TargetPos, 2000, 2000, eActionRun);
	pSkill->PushStageBack(pMoveStage);

	CActionStage* pActionStage = new CActionStage(iBattlerID, 0, 0, iTargetID, 8, eActionNull, NULL, 50);
	pSkill->PushStageBack(pActionStage);

	ParallelSkillStage* pParallelStage = new ParallelSkillStage(5000);
	pSkill->PushStageBack(pParallelStage);

	pMoveStage = BuildCatchMove(iBattlerID, TargetPos, LocalPos, 2000, 2000, eActionRun);
	pParallelStage->AddStage(pMoveStage);

	TargetPos = pTarget->GetLogicLocation();//被捕捉者要从他自己的位置开始移动
	if (bCatchOK == true)//捕捉成功
	{
		pMoveStage = BuildCatchMove(iTargetID, TargetPos, LocalPos, 2200, 2200, eActionRun);
		pParallelStage->AddStage(pMoveStage);

		std::wstring wstrEffectName;
		wstrEffectName = MHSD_UTILS::GetEffectPath(11013);
		CEffectStage* pEffectStage = new CEffectStage(iTargetID, 0, 0, Nuclear::NuclearLocation(0, 0), wstrEffectName, eMagicPos_ToSelf, false, false, pSkill, 200, 1, 2200, 1);
		pParallelStage->AddStage(pEffectStage);

		CResultStage* pResultStage = new CResultStage(iBattlerID, iTargetID, 0, 0, pSkill, 0, 50);
		pSkill->PushStageBack(pResultStage);
		if (iBattlerID == GetBattleManager()->GetMainBattleCharID())
		{
			CMessageTipStage* pStage = new CMessageTipStage(141172, 10);//捕捉成功
			pSkill->PushStageBack(pStage);
		}
	}
	else//捕捉失败
	{
		Nuclear::NuclearLocation NewTarget = TargetPos;
		Nuclear::NuclearLocation Dist = LocalPos - TargetPos;

		float fRate = 0.4 + 0.1*(rand() % 5);
		int totalTime = 2200;
		NewTarget.x += Dist.x * fRate;
		NewTarget.y += Dist.y * fRate;
		int toTm = totalTime*fRate;
		pMoveStage = BuildCatchMove(iTargetID, TargetPos, NewTarget, toTm, toTm, eActionRun);
		pParallelStage->AddStage(pMoveStage);

		std::wstring wstrEffectName;
		wstrEffectName = MHSD_UTILS::GetEffectPath(11013);
		CEffectStage* pEffectStage = new CEffectStage(iTargetID, 0, 0, Nuclear::NuclearLocation(0, 0), wstrEffectName, eMagicPos_ToSelf, false, false, pSkill, 200, 1, toTm, 1);
		pParallelStage->AddStage(pEffectStage);

		pActionStage = new CActionStage(iTargetID, 0, 0, iTargetID, 2, eActionNull, NULL, toTm + 200);
		pActionStage->SetDelayTicks(toTm + 100);
		pParallelStage->AddStage(pActionStage);

		int backtm = toTm*0.5;
		pMoveStage = BuildCatchMove(iTargetID, NewTarget, TargetPos, backtm, backtm + toTm + 100, eActionBattleRun);
		pMoveStage->SetDelayTicks(toTm + 100);
		pParallelStage->AddStage(pMoveStage);

		pActionStage = new CActionStage(iTargetID, 0, 0, iTargetID, 8, eActionBattleStand, NULL, toTm + 100 + backtm);
		pActionStage->SetDelayTicks(toTm + 100 + backtm);
		pParallelStage->AddStage(pActionStage);

		pMoveStage = BuildCatchMove(iTargetID, TargetPos, TargetPos, 10, toTm + backtm + 200, eActionBattleStand);
		pMoveStage->SetDelayTicks(toTm + backtm + 200);
		pParallelStage->AddStage(pMoveStage);

		if (iBattlerID == GetBattleManager()->GetMainBattleCharID())
		{
			CMessageTipStage* pStage = new CMessageTipStage(141173, 10);//捕捉失败
			pSkill->PushStageBack(pStage);
		}
	}
	pActionStage = new CActionStage(iBattlerID, 0, 0, iBattlerID, 8, eActionBattleStand, NULL, 50, true);
	pSkill->PushStageBack(pActionStage);

	return pSkill;
}
static SkillStage* BuildResultStage(Skill* pSkill, sBattleDemoScript& aScript, int SubSkillIndex, const GameTable::battle::CStageInfo& aStageConfig, int & AttackBackTargetID, bool bAttackBack = false, int AttackBackApp = 0, int DelayApp = 0)
{
	Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);
	if (pSender == NULL)
	{
		return NULL;
	}
	ParallelSkillStage* pParStage = NULL;
	for (unsigned i = 0; i < aScript.listResults[SubSkillIndex].listResults.size(); i++)
	{
		CResultStage* pResultStage = NULL;
		sBattleDemoResult rResult = aScript.listResults[SubSkillIndex].listResults[i];
		sBattleDemoResult* pResult = &rResult;
		int iResultType = pResult->ResultType;
		if (bAttackBack == false && iResultType != 1)
		{
			pResultStage = new CResultStage(aScript.stMagicCasting.AttackerID, pResult->TargetID, aStageConfig.resultonhittime, aStageConfig.targety, pSkill, i, aStageConfig.resultlimittime);
			pResultStage->SetDelayTicks(aStageConfig.delay);
		}
		else if (bAttackBack == true && iResultType == 1)
		{
			pResultStage = new CResultStage(pResult->TargetID, aScript.stMagicCasting.AttackerID, aStageConfig.resultonhittime, 2, pSkill, i, aStageConfig.resultlimittime + AttackBackApp);
			pResultStage->SetDelayTicks(aStageConfig.delay + DelayApp);
			AttackBackTargetID = pResult->TargetID;
		}
		if (pResultStage != NULL)
		{
			if (pParStage == NULL)
			{
				pParStage = new ParallelSkillStage(10000);
			}
			pParStage->AddStage(pResultStage);
		}
	}
	if (pParStage)
	{
		pParStage->SetDelayTicks(aStageConfig.delay + DelayApp);
	}
	return pParStage;
}
static SkillStage* BuildEffectStage(Skill* pSkill, sBattleDemoScript& aScript, int SubSkillIndex, const GameTable::battle::CStageInfo& aStageConfig)
{
	Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);

	if (pSender == NULL) return NULL;

	CSoundEffectStage* pSoundStage = NULL;
	if (!aStageConfig.effectsound.empty())
	{
		pSoundStage = new CSoundEffectStage(L"/sound/skill/" + aStageConfig.effectsound + L".ogg", 100);
		pSoundStage->SetDelayTicks(aStageConfig.delay);
	}

	SkillStage* pStage = NULL;
	switch (aStageConfig.postype)
	{
	case eMagicPos_ToSelf://目标身上
	{

		ParallelSkillStage* pParStage = new ParallelSkillStage(10000);
		bool bExist = false;
		for (unsigned int i = 0; i < aScript.listResults[SubSkillIndex].listResults.size(); ++i)
		{
			Battler* pTarget = GetBattleManager()->FindBattlerByID(aScript.listResults[SubSkillIndex].listResults[i].TargetID);
			if (pTarget)
			{
				std::wstring effectname = aStageConfig.effectname;
				if (aStageConfig.youfangxiang)
				{
					BuildEffectName(pTarget->IsFriendSide(), effectname);
				}
				SkillStage* _pStage = new CEffectStage(aScript.listResults[SubSkillIndex].listResults[i].TargetID, 0, aStageConfig.phantomalpha,
					Nuclear::NuclearLocation(aStageConfig.targetx, aStageConfig.targety), effectname, eMagicPos_Static, false, false,
					pSkill, 200, aStageConfig.effectlayer, 1, 0, aStageConfig.movetime);
				if (aStageConfig.actiontype != 0)
				{
					((CEffectStage*)_pStage)->SetLightingParam(aScript.stMagicCasting.AttackerID, aStageConfig.actiontype, aStageConfig.teletime, aStageConfig.blurtime, aStageConfig.teleportlimittime);
				}
				pParStage->AddStage(_pStage);
				_pStage->SetDelayTicks(aStageConfig.delay);
				bExist = true;
			}
		}
		if (bExist)
		{
			pStage = pParStage;
			if (pSoundStage)
			{
				pParStage->AddStage(pSoundStage);
			}
		}
		return pStage;

	}
	break;
	case eMagicPos_HeadTop://头顶
	{
		std::wstring effectname = aStageConfig.effectname;
		if (aStageConfig.youfangxiang)
		{
			BuildEffectName(pSender->IsFriendSide(), effectname);
		}
		pStage = new CEffectStage(aScript.stMagicCasting.AttackerID, aStageConfig.phantomid, aStageConfig.phantomalpha,
			Nuclear::NuclearLocation(aStageConfig.targetx, aStageConfig.targety), effectname, (eMagicPosType)6, false, false,
			pSkill, 200, aStageConfig.effectlayer, 1, 0, aStageConfig.movetime);
	}
	break;
	case eMagicPos_Banner://skill banner
	{
		std::wstring effectname = aStageConfig.effectname;
		if (aStageConfig.youfangxiang)
		{
			BuildEffectName(pSender->IsFriendSide(), effectname);
		}
		pStage = new CEffectStage(aScript.stMagicCasting.AttackerID, aStageConfig.phantomid, aStageConfig.phantomalpha,
			Nuclear::NuclearLocation(aStageConfig.targetx, aStageConfig.targety), effectname, (eMagicPosType)6, false, false,
			pSkill, 200, aStageConfig.effectlayer, 1, 0, aStageConfig.movetime);
	}
	break;
	case eMagicPos_Static:	//静止于人物
	{
		std::wstring effectname = aStageConfig.effectname;
		if (aStageConfig.youfangxiang)
		{
			BuildEffectName(pSender->IsFriendSide(), effectname);
		}
		int iRot = aStageConfig.movetime;
		if (aStageConfig.phantomid > 0)
		{
			if (aStageConfig.movetime > 0)
			{
				if ((int)aScript.listResults[SubSkillIndex].listResults.size() > aStageConfig.effecttype)
				{
					Battler* pTarget = GetBattleManager()->FindBattlerByID(aScript.listResults[SubSkillIndex].listResults[aStageConfig.effecttype].TargetID);
					if (pTarget == NULL)	break;
					float fRot = 0.0f;
					Nuclear::NuclearFPoint FPSrc = pSender->GetLocation().ToFPOINT();
					Nuclear::NuclearFPoint FPDst = pTarget->GetLocation().ToFPOINT();
					int iSrcOffsetY = aStageConfig.resultlimittime;
					int iDstOffsetY = aStageConfig.resultonhittime;
					FPSrc.y = FPSrc.y - iSrcOffsetY;
					FPDst.y = FPDst.y - iDstOffsetY;
					Nuclear::NuclearFPoint FPSub = FPDst - FPSrc;
					fRot = atan2(FPSub.y, FPSub.x) * 180 / 3.1415926;
					iRot = fRot;
					while (iRot < 0)
					{
						iRot += 360;
					}
					while (iRot > 360)
					{
						iRot -= 360;
					}
					if (pSender->IsFriendSide())
					{
						iRot += 180;
					}
				}
			}
		}
		pStage = new CEffectStage(aScript.stMagicCasting.AttackerID, aStageConfig.phantomid, aStageConfig.phantomalpha,
			Nuclear::NuclearLocation(aStageConfig.targetx, aStageConfig.targety), effectname, eMagicPos_Static, false, false,
			pSkill, 200, aStageConfig.effectlayer, 1, 0, iRot);
	}
	break;
	case eMagicPos_Friend:	//友方阵型中间
	case eMagicPos_Enemy:	//敌方阵型中间
	{
		//如果是战场环境Demo，则播放在整个战场中间
		if (aScript.stMagicCasting.BattleOperate != eOperate_EnvironmentDemo)
		{
			bool isFriendSide = false;
			if (pSender->IsFriendSide())
			{
				if (eMagicPos_Friend == aStageConfig.postype)
				{
					isFriendSide = true;
				}
			}
			else
			{
				if (eMagicPos_Friend == aStageConfig.postype)
				{
					isFriendSide = false;
				}
				else
				{
					isFriendSide = true;
				}
			}
			Nuclear::NuclearLocation effectloc = GetSidePos(isFriendSide);

			printf("------effect loc:%d, %d\n", effectloc.x, effectloc.y);
			std::wstring effectname = aStageConfig.effectname;
			if (aStageConfig.youfangxiang)
			{
				BuildEffectName(isFriendSide, effectname);
			}

			pStage = new CEffectStage(aScript.stMagicCasting.AttackerID, aStageConfig.phantomid, aStageConfig.phantomalpha, effectloc, effectname, (eMagicPosType)aStageConfig.postype, true, aStageConfig.hastrail == 1, pSkill, 200, aStageConfig.effectlayer);
		}
		//战场环境播放在战场正中心
		else
		{
			Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();

			if (aStageConfig.effectname != L"")
			{
				std::wstring effectname = aStageConfig.effectname;
				if (aStageConfig.youfangxiang)
				{
					BuildEffectName(pSender->IsFriendSide(), effectname);
				}
				pStage = new CEffectStage(aScript.stMagicCasting.AttackerID, aStageConfig.phantomid, aStageConfig.phantomalpha, Nuclear::NuclearLocation((cp.right + cp.left) / 2, (cp.top + cp.bottom) / 2), effectname, (eMagicPosType)aStageConfig.postype, true, false, pSkill, 200, aStageConfig.effectlayer);
			}
		}
	}
	break;
	case eMagicPos_Middle:	//播放在战场正中心
	{
		Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();

		if (aStageConfig.effectname != L"")
		{
			std::wstring effectname = aStageConfig.effectname;
			if (aStageConfig.youfangxiang)
			{
				BuildEffectName(pSender->IsFriendSide(), effectname);
			}
			pStage = new CEffectStage(aScript.stMagicCasting.AttackerID, aStageConfig.phantomid, aStageConfig.phantomalpha, Nuclear::NuclearLocation((cp.right + cp.left) / 2, (cp.top + cp.bottom) / 2), effectname, (eMagicPosType)aStageConfig.postype, true, false, pSkill, 200, aStageConfig.effectlayer);
		}
	}
	break;
	default:
		break;
	}

	if (pSoundStage)
	{
		ParallelSkillStage* pParStage = new ParallelSkillStage(10000);
		pParStage->AddStage(pSoundStage);
		if (pStage != NULL)
		{
			pParStage->AddStage(pStage);
			pStage->SetDelayTicks(aStageConfig.delay);
		}
		pStage = pParStage;
	}
	return pStage;
}
static SkillStage* BuildMoveStage(Skill* pSkill, sBattleDemoScript& aScript, int SubSkillIndex, const GameTable::battle::CStageInfo& aStageConfig, bool bProtect = false, int iMoveTime = 0)
{
	Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);

	if (pSender == NULL) return NULL;

	CMoveStage* pMoveBackStage = NULL;

	int iBattlerID = aScript.stMagicCasting.AttackerID;
	int iTargetID = aScript.listResults[SubSkillIndex].listResults[aStageConfig.targetx].TargetID;
	int iProtecterID = aScript.listResults[SubSkillIndex].listResults[aStageConfig.targetx].ProtecterID;

	Battler* pBattler = GetBattleManager()->FindBattlerByID(iBattlerID);
	Battler* pTarget = GetBattleManager()->FindBattlerByID(iTargetID);

	if (!bProtect)
	{
		if (aStageConfig.movetype == eMove_Target)
		{
			if (iMoveTime == 0)
			{
				pMoveBackStage = new CMoveStage(iBattlerID, aStageConfig.phantomid, aStageConfig.phantomalpha, false, iTargetID, aStageConfig.targety, 2000);
			}
			else
			{
				if (!pTarget)
				{
					return NULL;
				}
				Nuclear::NuclearLocation LocalPos = pBattler->GetLogicLocation();
				Nuclear::NuclearLocation TargetPos = pTarget->GetLogicLocation();
				Nuclear::NuclearLocation detloc(0, 0);
				bool bFriend = pTarget->IsFriendSide();
				if (aStageConfig.targety == 2)
					detloc = bFriend ? -c_nAttackLocation : c_nAttackLocation;
				else if (aStageConfig.targety == 4)
					detloc = bFriend ? c_nAttackSideLocation : -c_nAttackSideLocation;
				else if (aStageConfig.targety == 6)
					detloc = bFriend ? -c_nAttackSideLocation : c_nAttackSideLocation;
				else if (aStageConfig.targety == 8)
					detloc = bFriend ? c_nAttackCloseLocation : -c_nAttackCloseLocation;
				else if (aStageConfig.targety == 11)
					TargetPos = GetSidePos(pTarget->IsFriendSide());
				else if (aStageConfig.targety == 12)
					TargetPos = GetSidePos(pTarget->IsEnemySide());
				else if (aStageConfig.targety == 13)
				{
					Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
					TargetPos = Nuclear::NuclearLocation((cp.right + cp.left) / 2, (cp.top + cp.bottom) / 2);
				}
				else if (aStageConfig.targety == 22)
					detloc = bFriend ? -c_nProtectLocation : c_nProtectLocation;
				detloc = Nuclear::NuclearWorldLogicCoord::world2logic(detloc);
				TargetPos += detloc;
				int iSrcOffsetY = aStageConfig.resultlimittime;
				int iDstOffsetY = aStageConfig.resultonhittime;
				LocalPos.y = LocalPos.y - iSrcOffsetY;
				TargetPos.y = TargetPos.y - iDstOffsetY;
				Nuclear::NuclearLocation Dist = TargetPos - LocalPos;
				int iMoveOffsetX = Dist.x * 1000.0f / iMoveTime;//除以几就是在几毫秒内走完
				int iMoveOffsetY = Dist.y * 1000.0f / iMoveTime;
				int iMoveTargetX = TargetPos.x;
				int iMoveTargetY = TargetPos.y;
				pMoveBackStage = new CMoveStage(iBattlerID, aStageConfig.phantomid, aStageConfig.phantomalpha, false, 0, aStageConfig.targety, 2000, false,
					iMoveOffsetX, iMoveOffsetY, eActionNull, true, iMoveTargetX, iMoveTargetY, iMoveTime, true, 0, -iSrcOffsetY);
			}
		}
		else if (aStageConfig.movetype == eMove_Back)
		{
			pMoveBackStage = new CMoveStage(iBattlerID, aStageConfig.phantomid, aStageConfig.phantomalpha, false, 0, 2, 2000, true);
		}
		else if (aStageConfig.movetype == eMove_Release)
		{
			pMoveBackStage = new CMoveStage(iBattlerID, aStageConfig.phantomid, aStageConfig.phantomalpha, true, 0, 2, 2000);
		}
	}
	else
	{
		if (iProtecterID == 0)
		{
			return NULL;
		}
		if (aStageConfig.syncprotect == 1 || aStageConfig.syncprotect == 11)
		{
			pMoveBackStage = new CMoveStage(iProtecterID, 0, 0, false, iTargetID, 2, 2000);
		}
		else if (aStageConfig.syncprotect == 2 || aStageConfig.syncprotect == 12)
		{
			pMoveBackStage = new CMoveStage(iProtecterID, 0, 0, false, 0, 2, 2000, true);
		}
	}

	return pMoveBackStage;
}
static SkillStage* BuildActionStage(Skill* pSkill, sBattleDemoScript& aScript, int SubSkillIndex, const GameTable::battle::CStageInfo& aStageConfig)
{
	if (aStageConfig.actiontype < eActionNull || aStageConfig.actiontype >= eActionMax)
	{
		printf("unknown action type:%d\n", aStageConfig.actiontype);
		return NULL;
	}

	//need check action limit time later

	Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);

	if (pSender == NULL) return NULL;

	CSoundEffectStage* pSoundStage = NULL;
	if (aStageConfig.actiontype == eActionMagic1)
	{
		std::wstring npcshape_magic = GetTableWStringFromLua("npc.cnpcshape", pSender->GetShapeID(), "magic");
		if (!npcshape_magic.empty())
		{
			pSoundStage = new CSoundEffectStage(L"/sound/monster/" + npcshape_magic + L".ogg", 100);
			pSoundStage->SetDelayTicks(aStageConfig.delay);
		}
	}

	int iTarget = 0;
	if (aScript.listResults[SubSkillIndex].listResults.size() > 0)
	{
		iTarget = aScript.listResults[SubSkillIndex].listResults[aStageConfig.targetx].TargetID;
	}
	CActionStage* pActionStage = new CActionStage(aScript.stMagicCasting.AttackerID, aStageConfig.phantomid, aStageConfig.phantomalpha, iTarget, aStageConfig.targety, (eActionType)aStageConfig.actiontype, &aScript, aStageConfig.actionlimittime);
	if (pActionStage)
	{
		pActionStage->SetDelayTicks(aStageConfig.delay);
	}
	if (pSoundStage && pActionStage)
	{
		ParallelSkillStage* pRet = new ParallelSkillStage(10000);
		pRet->AddStage(pSoundStage);
		pRet->AddStage(pActionStage);
		pRet->SetDelayTicks(aStageConfig.delay);
		return pRet;
	}
	return pActionStage;
}
static Skill* LoadSkill(int aISkillID, sBattleDemoScript& aScript, int SubSkillIndex)
{
	Skill* pRet = new Skill(aScript, SubSkillIndex);

	int iResultCount = aScript.listResults[SubSkillIndex].listResults.size();
	const GameTable::battle::CSkillInfo& skillinfo = GameTable::battle::GetCSkillInfoTableInstance().getRecorder(aISkillID);

	if (skillinfo.id == -1)
	{
		return BuildNormalAttack(pRet, aScript, SubSkillIndex);
	}
	GameTable::skill::CSkillTypeConfig skilltypecfg = GameTable::skill::GetCSkillTypeConfigTableInstance().getRecorder(aScript.stMagicCasting.OperatorID);
	if (skilltypecfg.id == -1)
	{
		pRet->m_bDanti = false;
	}
	else
	{
		if (skilltypecfg.skilltype == 1 || skilltypecfg.skilltype == 3 || skilltypecfg.skilltype == 5)
		{
			pRet->m_bDanti = true;
		}
	}

	std::vector<std::wstring> delimiters;
	delimiters.push_back(L",");
	std::vector<std::wstring> outputstrings;

	Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);
	if (pSender == NULL)
	{
		return pRet;
	}
	int iShapeID = pSender->GetShapeID();
	int NS_nearorfar = GetTableNumberFromLua("npc.cnpcshape", iShapeID, "nearorfar");
	if (pSender->GetBattlerData().school == eSchoolSaMan)
	{

	}
	if (NS_nearorfar == 0)//近战
	{
		StringCover::split_string(skillinfo.stagelist, delimiters, outputstrings);
	}
	else
	{
		StringCover::split_string(skillinfo.stagelist2, delimiters, outputstrings);
	}

	if (SubSkillIndex == 0)
	{
		if (aScript.stMagicCasting.AttackType == 1)
			pSender->PlayEffect(MHSD_UTILS::GetEffectPath(11056));
		int iShowSkillName = skilltypecfg.showskillname;
		if (iShowSkillName != 0)
		{
			pSender->ShowSkillName(true, aScript.stMagicCasting.OperatorID);
		}
	}

	ParallelSkillStage* pParStage = NULL;
	for (unsigned int i = 0; i < outputstrings.size(); ++i)
	{
		int iStageID = StringCover::StrToNum<int>(StringCover::to_string(outputstrings[i]));


		GameTable::battle::CStageInfo stageconfig;

		if (NS_nearorfar == 0)//近战
		{
			stageconfig = GameTable::battle::GetCStageInfoTableInstance().getRecorder(iStageID);
		}
		else
		{
			const GameTable::battle::CStageInfo2& stageconfig2 = GameTable::battle::GetCStageInfo2TableInstance().getRecorder(iStageID);
			stageconfig.actionlimittime = stageconfig2.actionlimittime;
			stageconfig.actiontype = stageconfig2.actiontype;
			stageconfig.blurtime = stageconfig2.blurtime;
			stageconfig.delay = stageconfig2.delay;
			stageconfig.effectlayer = stageconfig2.effectlayer;
			stageconfig.effectname = stageconfig2.effectname;
			stageconfig.effectsound = stageconfig2.effectsound;
			stageconfig.effecttype = stageconfig2.effecttype;
			stageconfig.executetype = stageconfig2.executetype;
			stageconfig.hastrail = stageconfig2.hastrail;
			stageconfig.id = stageconfig2.id;
			stageconfig.movetype = stageconfig2.movetype;
			stageconfig.phantomalpha = stageconfig2.phantomalpha;
			stageconfig.phantomid = stageconfig2.phantomid;
			stageconfig.movetime = stageconfig2.movetime;
			stageconfig.postype = stageconfig2.postype;
			stageconfig.resultlimittime = stageconfig2.resultlimittime;
			stageconfig.resultonhittime = stageconfig2.resultonhittime;
			stageconfig.resulttype = stageconfig2.resulttype;
			stageconfig.stagenum = stageconfig2.stagenum;
			stageconfig.stagetype = stageconfig2.stagetype;
			stageconfig.syncprotect = stageconfig2.syncprotect;
			stageconfig.targetx = stageconfig2.targetx;
			stageconfig.targety = stageconfig2.targety;
			stageconfig.teleportlimittime = stageconfig2.teleportlimittime;
			stageconfig.teleporttype = stageconfig2.teleporttype;
			stageconfig.teletime = stageconfig2.teletime;
			stageconfig.youfangxiang = stageconfig2.youfangxiang;
		}

		SkillStage* pStage = NULL;
		SkillStage* pStageProtect = NULL;
		SkillStage* pStageAttackBack = NULL;
		SkillStage* pStageSkillNameEffect = NULL;

		if (stageconfig.executetype == eExecute_Parallel)
		{
			if (pParStage == NULL)
			{
				pParStage = new ParallelSkillStage(10000);
				pRet->PushStageBack(pParStage);
			}
		}
		else
		{
			if (pParStage != NULL)
			{
				pParStage = NULL;
			}
		}

		switch (stageconfig.stagetype)
		{
		case eStage_Action:
		{
			if (stageconfig.targetx >= iResultCount)//如果要攻击并转向的目标索引大于本次伤害结果总数则不创建此移动阶段
			{
				continue;
			}
			pStage = BuildActionStage(pRet, aScript, SubSkillIndex, stageconfig);
		}
		break;
		case eStage_Move:
		{
			if (stageconfig.targetx >= iResultCount)//如果要移动的目标索引大于本次伤害结果总数则不创建此移动阶段
			{
				continue;
			}
			if (stageconfig.syncprotect < 10)
			{
				pStage = BuildMoveStage(pRet, aScript, SubSkillIndex, stageconfig, false, stageconfig.movetime);
			}
			pStageProtect = BuildMoveStage(pRet, aScript, SubSkillIndex, stageconfig, true, stageconfig.movetime);
		}
		break;
		case eStage_Effect:
		{
			pStage = BuildEffectStage(pRet, aScript, SubSkillIndex, stageconfig);
		}
		break;
		case eStage_Teleport:
		{
		}
		break;
		case eStage_Result:
		{
			int AttackBackTargetID = 0;
			pStage = BuildResultStage(pRet, aScript, SubSkillIndex, stageconfig, AttackBackTargetID);
			int AttackBackApp = 600;
			int DelayApp = 150;
			pStageAttackBack = BuildResultStage(pRet, aScript, SubSkillIndex, stageconfig, AttackBackTargetID, true, AttackBackApp, DelayApp);
			if (pStageAttackBack)	//反击
			{				
				Battler* pTarget = GetBattleManager()->FindBattlerByID(AttackBackTargetID);
				int idir = (pSender->IsFriendSide() == pTarget->IsFriendSide()) ? 8 : 2;
				CActionStage* pActionStage = new CActionStage(AttackBackTargetID, 0, 0, aScript.stMagicCasting.AttackerID, idir, eActionAttack, &aScript, AttackBackApp + DelayApp);
				pActionStage->SetDelayTicks(DelayApp);
				((ParallelSkillStage*)pStageAttackBack)->AddStage(pActionStage);
			}
		}
		break;
		case eStage_ChangeModel:
		{
			Battler* sender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);
			if (sender == NULL) return NULL;
			pStage = new CChangeModelStage(aScript.stMagicCasting.AttackerID, stageconfig.actionlimittime, stageconfig.phantomid);
		}
		break;
		case eStage_Alpha:
		{
			Battler* sender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);
			if (sender == NULL) return NULL;
			pStage = new CAlphaStage(aScript.stMagicCasting.AttackerID, stageconfig.actionlimittime, stageconfig.phantomalpha);
		}
		break;
		default:
		{
		}
		break;
		}

		if (pStage == NULL)
		{
			//远程不跑或是只操作保护者的情况才会到这里
			if (pStageProtect != NULL)
			{
				pStageProtect->SetDelayTicks(stageconfig.delay);
				if (pParStage == NULL)
				{
					pParStage = new ParallelSkillStage(10000);
					pRet->PushStageBack(pParStage);
				}
				pParStage->AddStage(pStageProtect);
			}
		}
		else
		{
			pStage->SetDelayTicks(stageconfig.delay);
			if (pStageProtect)
			{
				pStageProtect->SetDelayTicks(stageconfig.delay);
				if (pParStage == NULL)
				{
					pParStage = new ParallelSkillStage(10000);
					pRet->PushStageBack(pParStage);
				}
				pParStage->AddStage(pStage);
				pParStage->AddStage(pStageProtect);
				if (pStageAttackBack)
				{
					pRet->PushStageBack(pStageAttackBack);
				}
			}
			else
			{
				if (pParStage)
				{
					pParStage->AddStage(pStage);
				}
				else
				{
					pRet->PushStageBack(pStage);
				}
				if (pStageAttackBack)
				{
					pRet->PushStageBack(pStageAttackBack);
				}
			}

		}
	}
	return pRet;
}

Skill* gBuildSkill(sBattleDemoScript& aScript, int SubSkillIndex)
{
	Skill* pSkill = new Skill(aScript, SubSkillIndex);

	pSkill->m_DelayTime = aScript.listResults[SubSkillIndex].subskillstarttime;
	pSkill->m_BeginMode = 0;
	if (pSkill->m_DelayTime < 0)
	{
		pSkill->m_DelayTime = 0;
		pSkill->m_BeginMode = 2;
	}    
    for (size_t i=0;i<aScript.listBattler.size();i++)
    {
        printf("add battler\n");
        sBattlerData* pNewBattler = new sBattlerData;
        (*pNewBattler) =  aScript.listBattler[i];
       
		if (5 < pNewBattler->iBattleID && pNewBattler->iBattleID < 11)		//宠物
		{
			if (aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattleCharID())
			{
				Battler* pCaller = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);
				if (pCaller != NULL)
				{
					pCaller->SetPetUsed(aScript.stMagicCasting.OperatorID);
					int ct = GetBattleManager()->GetSummonCount();
					GetBattleManager()->SetSummonCount(ct + 1);
				}
			}
		}
		GetBattleMagicControl()->AddNewBattlerData(pNewBattler);
    }
    
    printf("attacker id:%d, operate type:%d, operate id:%d\n", aScript.stMagicCasting.AttackerID, aScript.stMagicCasting.BattleOperate, aScript.stMagicCasting.OperatorID);

	if (aScript.stMagicCasting.BattleOperate == eOperate_Attack)//普通攻击
	{
		Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);
		if (pSender != NULL && SubSkillIndex == 0)
			pSender->CheckBuffBeforeOperate(aScript.stMagicCasting.BattleOperate);
		return BuildNormalAttack(pSkill, aScript, SubSkillIndex);
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_Skill)//技能
	{
		delete pSkill;
		Battler* pSender = GetBattleManager()->FindBattlerByID(aScript.stMagicCasting.AttackerID);
		if (pSender != NULL && SubSkillIndex == 0)
			pSender->CheckBuffBeforeOperate(aScript.stMagicCasting.BattleOperate);
		pSkill = LoadSkill(aScript.listResults[SubSkillIndex].subskillid, aScript, SubSkillIndex);
		pSkill->m_DelayTime = aScript.listResults[SubSkillIndex].subskillstarttime;
		pSkill->m_BeginMode = 0;
		if (pSkill->m_DelayTime < 0)//如果是负数则此子技能将会跟随上一个子技能之后执行,在BattleScriptPlayer里面处理
		{
			pSkill->m_DelayTime = 0;
			pSkill->m_BeginMode = 2;
		}
		if (aScript.listBattler.size() > 0)
		{
			GetBattleMagicControl()->AddNewBattler();
		}
		return pSkill;
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_Item)//物品
	{
		delete pSkill;
		pSkill = LoadSkill(aScript.listResults[SubSkillIndex].subskillid, aScript, SubSkillIndex);
		pSkill->m_DelayTime = aScript.listResults[SubSkillIndex].subskillstarttime;
		pSkill->m_BeginMode = 0;
		if (pSkill->m_DelayTime < 0)//如果是负数则此子技能将会跟随上一个子技能之后执行,在BattleScriptPlayer里面处理
		{
			pSkill->m_DelayTime = 0;
			pSkill->m_BeginMode = 2;
		}
		return pSkill;
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_Summon)//召唤
	{
		if (0 <= SubSkillIndex && SubSkillIndex < (int)aScript.listResults.size())
		{
			if (aScript.listBattler.size() > 0)
				GetBattleMagicControl()->AddNewBattler(false);
			if (aScript.listResults[SubSkillIndex].subskillid != 0)
			{
				delete pSkill;
				pSkill = LoadSkill(aScript.listResults[SubSkillIndex].subskillid, aScript, SubSkillIndex);
				pSkill->m_DelayTime = aScript.listResults[SubSkillIndex].subskillstarttime;
				pSkill->m_BeginMode = 0;
				if (pSkill->m_DelayTime < 0)//如果是负数则此子技能将会跟随上一个子技能之后执行,在BattleScriptPlayer里面处理
				{
					pSkill->m_DelayTime = 0;
					pSkill->m_BeginMode = 2;
				}
				return pSkill;
			}
			else
			{
				CActionStage* pActionStage = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, 0, 2, eActionMagic1, NULL, 700);
				pSkill->PushStageBack(pActionStage);
				pSkill->PushStageBack(new CPetSummonStage(aScript.listBattler[0].iBattleID, 600));
				CResultStage* pResultStage = new CResultStage(aScript.stMagicCasting.AttackerID, aScript.listResults[SubSkillIndex].listResults[0].TargetID, 0, 2, pSkill, 0, 1000);
				pSkill->PushStageBack(pResultStage);
				return pSkill;
			}
		}
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_Catch)//捕捉
	{
		return BuildCatch(pSkill, aScript, SubSkillIndex);
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_Runaway)//逃跑
	{
		return BuildRunaway(pSkill, aScript, SubSkillIndex);
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_Rest)//休息
	{
		if (aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattleCharID() || aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattlePetID())
		{
			CMessageTipStage* pStage = new CMessageTipStage(150123, 10);
			pSkill->PushStageBack(pStage);
			return pSkill;
		}
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_Special)//特殊技能
	{
		delete pSkill;
		pSkill = LoadSkill(aScript.listResults[SubSkillIndex].subskillid, aScript, SubSkillIndex);
		pSkill->m_DelayTime = aScript.listResults[SubSkillIndex].subskillstarttime;
		pSkill->m_BeginMode = 0;
		if (pSkill->m_DelayTime < 0)//如果是负数则此子技能将会跟随上一个子技能之后执行,在BattleScriptPlayer里面处理
		{
			pSkill->m_DelayTime = 0;
			pSkill->m_BeginMode = 2;
		}
		return pSkill;
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_SummonInstant)//瞬间召唤
	{
		printf("show battler\n");
		GetBattleMagicControl()->AddNewBattler(false);
		CActionStage* pActionStage = new CActionStage(aScript.stMagicCasting.AttackerID, 0, 0, 0, 2, eActionMagic1, NULL, 700);
		pSkill->PushStageBack(pActionStage);
		ParallelSkillStage* pParStage = new ParallelSkillStage(600);
		for (unsigned i = 0; i < aScript.listBattler.size(); i++)
			pParStage->AddStage(new CPetSummonStage(aScript.listBattler[i].iBattleID, 600));
		pSkill->PushStageBack(pParStage);
		CResultStage* pResultStage = new CResultStage(aScript.stMagicCasting.AttackerID, aScript.listBattler[0].iBattleID/*aScript.listResults[SubSkillIndex].listResults[0].TargetID*/, 0, 2, pSkill, 0, 1000);
		pSkill->PushStageBack(pResultStage);
		return pSkill;
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_Failure)//操作失败
	{
		if (aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattleCharID() || aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattlePetID())
		{
			CMessageTipStage* pMessageTipStage = new CMessageTipStage(aScript.stMagicCasting.msgid, 10);//141488
			pSkill->PushStageBack(pMessageTipStage);
			CEffectStage* pEffectStage = new CEffectStage(aScript.stMagicCasting.AttackerID, 0, 0, Nuclear::NuclearLocation(0, 0), MHSD_UTILS::GetEffectPath(11016), eMagicPos_Static, false, false, pSkill, 200, 1);
			pSkill->PushStageBack(pEffectStage);
			return pSkill;
		}
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_FailureNoWonder)//操作失败不带叹号的
	{
		if (aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattleCharID() || aScript.stMagicCasting.AttackerID == GetBattleManager()->GetMainBattlePetID())
		{
			CMessageTipStage* pMessageTipStage = new CMessageTipStage(aScript.stMagicCasting.msgid, 10);//141488
			pSkill->PushStageBack(pMessageTipStage);
			return pSkill;
		}
	}
	else if (aScript.stMagicCasting.BattleOperate == eOperate_RoundEndDemo)//回合末结算demo	与战场环境改变一样，没有DemoSender
	{
		GetBattleManager()->ProcessRoundEndScript(aScript.listResults[SubSkillIndex].listResults);
		//处理主角和宠物属性改变
		if (!aScript.RoleAttribute.empty())
		{
			std::map<int, int>::iterator it = aScript.RoleAttribute.find(fire::pb::attr::AttrType::SP);
			if (it != aScript.RoleAttribute.end())
			{
				int spchange = aScript.RoleAttribute[fire::pb::attr::AttrType::SP] - GetMainRoleDataAttr(fire::pb::attr::AttrType::SP);
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainRoleData_UpdateSpChange", spchange);
				aScript.RoleAttribute.erase(it);
			}
			CMainRoleDataManager::UpdateMainBattlerAttribute(aScript.RoleAttribute);
			aScript.RoleAttribute.clear();
		}
		if (!aScript.PetAttribute.empty())
		{
			CMainRoleDataManager::UpdateMainPetAttribute(aScript.PetAttribute);
			aScript.PetAttribute.clear();
		}
	}    
    printf("unknown skill:%d\n", aScript.stMagicCasting.BattleOperate);
    delete pSkill;
    return NULL;
}
