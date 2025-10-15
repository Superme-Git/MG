#include "stdafx.h"
#include "BattleManager.h"
#include "Battler.h"
#include "BattleIllusion.h"
#include "GameOperateState.h"
#include "GameScene.h"
#include "RoleSkillCommon.h"
#include "NewRoleGuideManager.h"
#include "GameTable/gm/Cbattlelist.h"
#include "GameTable/npc/CMonsterConfig.h"
#include "MessageManager.h"
#include "GameApplication.h"
#include "MainRoleDataManager.h"
#include "fire/pb/battle/SSendRoundStart.hpp"
#include "BattleScriptPlayer.h"
#include "fire/pb/hook/CSetAutoBattle.hpp"
#include "GameTable/skill/CSkillTypeConfig.h"


//std::map<int, int> aiactions; not move to lua
void fire::pb::battle::SSendRoundStart::Process(Manager * manager, Manager::Session::ID sid)
{
	int iTick = Nuclear::GetMilliSeconds();
	printf("receive round start:%d\n", iTick);
	if (NULL == GetBattleManager())
	{
		return;
	}
	GetBattleManager()->SetShowBattlerDemo(false);

	g_BattleScriptPlayer.DoBattleClear();
	GetBattleManager()->ResetOperateTime(time);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.SetOperateTime", time);

	g_BattleScriptPlayer.DealAIActionBeforeOpearte(aiactions);

	BattlerList BL = GetBattleManager()->GetBattlerList();
	for (BattlerList::iterator it = BL.begin(); it != BL.end(); it++)
	{
		(*it)->ResetMoveOffset();
		(*it)->ResetTargetPos();
	}
}

BattleIllusion* BattleManager::FindBattleIllusionBySprite(Nuclear::ISprite * sprite)
{
	BattleIllusionList::iterator itor = find_if(m_listBattleIllusion.begin(), m_listBattleIllusion.end(), std::bind2nd(std::mem_fun(&BattleIllusion::FitSprite), sprite));
	if (itor != m_listBattleIllusion.end())
		return (*itor);
	return NULL;
}
Battler* BattleManager::FindBattlerBySprite(Nuclear::ISprite * sprite)
{
	if (NULL == sprite)
	{
		return NULL;
	}
	BattlerList::iterator itor = find_if(m_listBattler.begin(), m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::FitSprite), sprite));
	if (itor != m_listBattler.end())
		return (*itor);
	return NULL;
}
Battler* BattleManager::FindBattlerByID(int iBattleID)
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		if ((*itor)->GetBattleID() == iBattleID)
			return (*itor);
	}
	return NULL;
}
Battler* BattleManager::FindBattlerCharacterByRoleID(int64_t roleID)
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		if ((*itor)->GetBattleDataID() == roleID)
			return (*itor);
	}
	return NULL;
}

int BattleManager::GetFirstEnemyID()
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		if (IsEnemy((*itor)->GetBattleID()))
			return (*itor)->GetBattleID();
	}
	return 0;
}
int BattleManager::GetFirstFriendID()
{
	for (BattlerList::iterator itor = m_listBattler.begin(); itor != m_listBattler.end(); itor++)
	{
		if (IsFriend((*itor)->GetBattleID()) || IsMySelf((*itor)->GetBattleID()))
			return (*itor)->GetBattleID();
	}
	return 0;
}
int BattleManager::GetMainBattleCharID()
{
	if (m_pMainBattler[eOperateBattler_Char])
		return m_pMainBattler[eOperateBattler_Char]->GetBattleID();
	return 0;
}
int BattleManager::GetMainBattlePetID()
{
	if (m_pMainBattler[eOperateBattler_Pet])
		return m_pMainBattler[eOperateBattler_Pet]->GetBattleID();
	return 0;
}
int BattleManager::GetOperateMainBatterID()
{
	if (IsMainCharOperate())
		return GetMainBattleCharID();
	else if (IsMainPetOperate())
		return GetMainBattlePetID();
	else
		return 0;
}
int BattleManager::GetDefaultTargetID(eBattleOperate operatetype)
{
	if (operatetype == eOperate_Item || operatetype == eOperate_Skill)
	{
		return 0;
	}
	else if (operatetype == eOperate_Attack)
	{
		return GetFirstEnemyID();
	}
	return 0;
}

void BattleManager::SetDefaultSkillID(int SkillID)
{
	m_pDefaultSkillID[GetOperateMainBatterType()] = SkillID;
}
int BattleManager::GetDefaultSkillID()
{
	return m_pDefaultSkillID[GetOperateMainBatterType()];
}
int BattleManager::GetCharacterDefaultSkillID()
{
	return m_pDefaultSkillID[eOperateBattler_Char];
}

int BattleManager::GetAutoCommandOperateType(int RoleType)//0Player,1Pet
{
	switch (m_LastBattleOperateCmd[(eOperateBattlerType)(RoleType)].BattleOperate)
	{
	case eOperate_Attack:
	{
		return (int)eOperate_Attack;
	}
	break;
	case eOperate_Defence:
	{
		return (int)eOperate_Defence;
	}
	break;
	case eOperate_Skill:
	{
		return (int)eOperate_Skill;
	}
	break;
	default:
	{
		return (int)eOperate_Attack;
	}
	break;
	}
}
int BattleManager::GetAutoCommandOperateID(int RoleType)//0Player,1Pet
{
	return m_LastBattleOperateCmd[(eOperateBattlerType)(RoleType)].OperatorID;
}
void BattleManager::SetAutoCommandOperateType(int RoleType, int Value)//0Player,1Pet
{
	if (Value != eOperate_Attack && Value != eOperate_Defence && Value != eOperate_Skill)
	{
		Value = eOperate_Attack;
	}
	m_LastBattleOperateCmd[(eOperateBattlerType)(RoleType)].BattleOperate = (eBattleOperate)Value;
}
void BattleManager::SetAutoCommandOperateID(int RoleType, int Value)//0Player,1Pet
{
	m_LastBattleOperateCmd[(eOperateBattlerType)(RoleType)].OperatorID = Value;
}
std::wstring BattleManager::GetAutoCommandDescribe(eOperateBattlerType e)
{
	std::wstring operatecommand = L"";
	switch (m_LastBattleOperateCmd[e].BattleOperate)
	{
	case eOperate_Attack:
	{
		operatecommand += MHSD_UTILS::GETSTRING(743).c_str();
	}
	break;
	case eOperate_Defence:
	{
		operatecommand += MHSD_UTILS::GETSTRING(744).c_str();
	}
	break;
	case eOperate_Catch:
	{
		operatecommand += MHSD_UTILS::GETSTRING(682).c_str();
	}
	break;
	default:
		break;
	}
	return operatecommand;
}
std::wstring BattleManager::GetCurrentAutoOperateCommand()
{
	if (cocos2d::gGetScriptEngine()->executeGlobalFunction("MainRoleData_GetBattlePetID") > 0)
	{
		return GetAutoCommandDescribe(eOperateBattler_Char) + L" | " + GetAutoCommandDescribe(eOperateBattler_Pet);
	}
	else
	{
		return GetAutoCommandDescribe(eOperateBattler_Char);
	}
}

Nuclear::NuclearLocation BattleManager::GetWatcherLocation(int iBattleID)
{
	int resolution = 0;

	if (m_BattleID == -1)
	{
		resolution = 7;
	}
	else
	{
		resolution = GameTable::gm::GetCbattlelistTableInstance().getRecorder(m_BattleID).positionsid;
	}

	int index = iBattleID - WatchMinID;

	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	std::wstring loc = L"";
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(resolution);
		util.addArg(index);
		if (util.callLua(pScriptEngine->getLuaState(), "GetResolutionPositionsBywatch"))
		{
			loc = s2ws(util.getRetCString());
		}
	} cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("GetResolutionPositionsBywatch", resolution, index);
	std::vector<std::wstring>	delimiters;
	delimiters.push_back(L";");
	std::vector<std::wstring>	outputstrings;
	StringCover::split_string(loc, delimiters, outputstrings);

	Nuclear::NuclearRect VP = gGetScene()->GetViewport();
	if (outputstrings.size() != 2)
	{
		return Nuclear::NuclearLocation(VP.left + VP.Width() / 2, VP.top + VP.Height() / 2);
	}

	Nuclear::IEngine* pEngine = Nuclear::GetEngine();
	int VPSizeW = pEngine->m_adapter->get_logic_w();
	int VPSizeH = pEngine->m_adapter->get_logic_h();
	int longa = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetResolutionLong", resolution);
	int wide = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetResolutionWide", resolution);
	float fScaleW = VPSizeW * 1.0f / longa;
	float fScaleH = VPSizeH * 1.0f / wide;

	float fScaleOriginalWH = longa * 1.0f / wide;

	float fScaleFinal;

	if (fScaleW >= fScaleH)
	{
		VPSizeW = VPSizeH * fScaleOriginalWH;
		fScaleFinal = fScaleH;
	}
	else
	{
		VPSizeH = VPSizeW / fScaleOriginalWH;
		fScaleFinal = fScaleW;
	}

	Nuclear::NuclearLocation ptBattleLoc;
	ptBattleLoc = Nuclear::NuclearLocation(StringCover::StrToNum<int>(StringCover::to_string(outputstrings[0])), StringCover::StrToNum<int>(StringCover::to_string(outputstrings[1])));

	ptBattleLoc.x *= fScaleFinal;
	ptBattleLoc.y *= fScaleFinal;

	int VPX = (VP.Width() - VPSizeW) / 2;
	int VPY = (VP.Height() - VPSizeH) / 2;

	return Nuclear::NuclearLocation(VP.left + VPX, VP.top + VPY) + ptBattleLoc;
}
Nuclear::NuclearLocation BattleManager::GetBattleUILocation(int BattleID)
{
	int formationid = 0;
	if (BattleID <= FriendMaxID && BattleID >= FriendMinID)
		formationid = m_iFriendFormation;
	else if (BattleID <= EnemyMaxID && BattleID >= EnemyMinID)
		formationid = m_iEnemyFormation;
	int positionid = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("GetBattleLocation", formationid, BattleID);
	if (positionid == -1)
		return Nuclear::NuclearLocation(-1, -1);

	int resolution = 0;

	if (m_BattleID == -1)
	{
		resolution = 7;
	}
	else
	{
		resolution = GameTable::gm::GetCbattlelistTableInstance().getRecorder(m_BattleID).positionsid;
	}
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	std::wstring loc = L"";
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(resolution);
		util.addArg(positionid - 1);
		if (util.callLua(pScriptEngine->getLuaState(), "GetResolutionPositionsByresolution"))
		{
			loc = s2ws(util.getRetCString());
		}
	}



	std::vector<std::wstring>	delimiters;
	delimiters.push_back(L";");
	std::vector<std::wstring>	outputstrings;
	StringCover::split_string(loc, delimiters, outputstrings);

	Nuclear::NuclearRect VP = gGetScene()->GetViewport();
	if (outputstrings.size() != 2)
	{
		return Nuclear::NuclearLocation(VP.left + VP.Width() / 2, VP.top + VP.Height() / 2);
	}

	Nuclear::IEngine* pEngine = Nuclear::GetEngine();
	int VPSizeW = pEngine->m_adapter->get_logic_w();
	int VPSizeH = pEngine->m_adapter->get_logic_h();

	int longa = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetResolutionLong", resolution);
	int wide = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetResolutionWide", resolution);

	float fScaleW = VPSizeW * 1.0f / longa;
	float fScaleH = VPSizeH * 1.0f / wide;

	float fScaleOriginalWH = longa * 1.0f / wide;
	float fScaleFinal;
	if (fScaleW >= fScaleH)
	{
		VPSizeW = VPSizeH * fScaleOriginalWH;
		fScaleFinal = fScaleH;
	}
	else
	{
		VPSizeH = VPSizeW / fScaleOriginalWH;
		fScaleFinal = fScaleW;
	}

	Nuclear::NuclearLocation ptBattleLoc;
	ptBattleLoc = Nuclear::NuclearLocation(StringCover::StrToNum<int>(StringCover::to_string(outputstrings[0])), StringCover::StrToNum<int>(StringCover::to_string(outputstrings[1])));

	ptBattleLoc.x *= fScaleFinal;
	ptBattleLoc.y *= fScaleFinal;

	int VPX = (VP.Width() - VPSizeW) / 2;
	int VPY = (VP.Height() - VPSizeH) / 2;

	return Nuclear::NuclearLocation(VPX, VPY) + ptBattleLoc;
	return ptBattleLoc;
}
Nuclear::NuclearLocation BattleManager::GetBattleLocation(int BattleID)
{
	int formationid = 0;
	if (BattleID <= FriendMaxID && BattleID >= FriendMinID)
		formationid = m_iFriendFormation;
	else if (BattleID <= EnemyMaxID && BattleID >= EnemyMinID)
		formationid = m_iEnemyFormation;

	int positionid = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("GetBattleLocation", formationid, BattleID);
	if (positionid <= 0)
		return Nuclear::NuclearLocation(-1, -1);

	int resolution = 0;

	if (m_BattleID == -1)
	{
		resolution = 7;
	}
	else
	{
		resolution = GameTable::gm::GetCbattlelistTableInstance().getRecorder(m_BattleID).positionsid;
	}

	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	std::wstring loc = L"";
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(resolution);
		util.addArg(positionid - 1);
		if (util.callLua(pScriptEngine->getLuaState(), "GetResolutionPositionsByresolution"))
		{
			loc = s2ws(util.getRetCString());
		}
	}
	std::vector<std::wstring>	delimiters;
	delimiters.push_back(L";");
	std::vector<std::wstring>	outputstrings;
	StringCover::split_string(loc, delimiters, outputstrings);

	Nuclear::NuclearRect VP = gGetScene()->GetViewport();
	if (outputstrings.size() != 2)
	{
		return Nuclear::NuclearLocation(VP.left + VP.Width() / 2, VP.top + VP.Height() / 2);
	}

	Nuclear::IEngine* pEngine = Nuclear::GetEngine();
	int VPSizeW = pEngine->m_adapter->get_logic_w();
	int VPSizeH = pEngine->m_adapter->get_logic_h();

	int longa = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetResolutionLong", resolution);
	int wide = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetResolutionWide", resolution);
	float fScaleW = VPSizeW * 1.0f / longa;
	float fScaleH = VPSizeH * 1.0f / wide;

	float fScaleOriginalWH = longa * 1.0f / wide;

	float fScaleFinal;

	if (fScaleW >= fScaleH)
	{
		VPSizeW = VPSizeH * fScaleOriginalWH;
		fScaleFinal = fScaleH;
	}
	else
	{
		VPSizeH = VPSizeW / fScaleOriginalWH;
		fScaleFinal = fScaleW;
	}

	Nuclear::NuclearLocation ptBattleLoc;
	ptBattleLoc = Nuclear::NuclearLocation(StringCover::StrToNum<int>(StringCover::to_string(outputstrings[0])), StringCover::StrToNum<int>(StringCover::to_string(outputstrings[1])));

	ptBattleLoc.x *= fScaleFinal;
	ptBattleLoc.y *= fScaleFinal;

	int VPX = (VP.Width() - VPSizeW) / 2;
	int VPY = (VP.Height() - VPSizeH) / 2;

	return Nuclear::NuclearLocation(VP.left + VPX, VP.top + VPY) + ptBattleLoc;
}
Nuclear::NuclearLocation BattleManager::BattleGridToPixelGrid(Nuclear::NuclearLocation ptGrid)
{
	return	Nuclear::NuclearLocation((ptGrid.x*BATTLE_GRID - 12), (ptGrid.y*BATTLE_GRID - 12));
}
Nuclear::NuclearLocation BattleManager::PixelGridToBattleGrid(Nuclear::NuclearLocation ptPixel)
{
	return Nuclear::NuclearLocation((ptPixel.x + 24) / BATTLE_GRID, (ptPixel.y + 24) / BATTLE_GRID);
}
Nuclear::NuclearLocation BattleManager::GetBattleCenter()
{
	Nuclear::NuclearRect cp = gGetScene()->GetViewport();
	return Nuclear::NuclearLocation((cp.right + cp.left) / 2, (cp.top + cp.bottom) / 2);
}

eOperateBattlerType BattleManager::GetOperateMainBatterType()
{
	if (IsMainCharOperate())
		return eOperateBattler_Char;
	else if (IsMainPetOperate())
		return eOperateBattler_Pet;
	else
	{
		assert(0);
		return eOperateBattlerMax;
	}
}

void BattleManager::CheckPickArea()
{
	bool bShow = true;
	if (!GetBattleManager()->IsInBattleOperate() || GetBattleManager()->IsAutoOperate())
	{
		bShow = false;
	}
	int skillID = GetBattleManager()->GetCurSelectedSkillID();

	for (AreaBlockList::iterator itor = m_listAreaBlock.begin(); itor != m_listAreaBlock.end(); itor++)
	{
		int pos = (*itor)->GetPos();
		Battler* pBattler = FindBattlerByID(pos);
		if (pBattler == NULL)
		{
			bool bCanPick = (*itor)->CanPick(gGetGameOperateState()->GetGameCursorType(), GetCurSelectedSkillID(), GetCurSelectedItem());
			(*itor)->ShowPick(bCanPick);
		}
		else
		{
			(*itor)->ShowPick(false);
		}
	}
}
void BattleManager::CheckPickBattler()
{
	bool bShow = true;
	if (!GetBattleManager()->IsInBattleOperate() || GetBattleManager()->IsAutoOperate())
	{
		bShow = false;
	}
	//检测是否需要点选地形
	CheckPickArea();
	for (int i = 0; i < 28; i++)
	{
		Battler* pBattler = FindBattlerByID(i + 1);
		if (!pBattler)
		{
			continue;
		}
		if (!bShow)
		{
			pBattler->ShowPickBtn(false);
		}
		else
		{
			bool bCanPick = pBattler->CanPick(gGetGameOperateState()->GetGameCursorType(), GetCurSelectedSkillID(), GetCurSelectedItem());
			pBattler->ShowPickBtn(bCanPick);
		}
	}
}
bool BattleManager::CanSaveToDefaultOperate(eBattleOperate e, int targetid)
{
	int clienttargetid = targetid;
	SCBattleIDChange(clienttargetid);
	if (e == eOperate_Skill || e == eOperate_Defence)
	{
		return true;
	}
	else if (e == eOperate_Attack && (IsEnemy(clienttargetid) || clienttargetid == 0))
	{
		return true;
	}
	else
		return false;
}
bool BattleManager::HaveAlreadyUseSummonNpcItem()
{
	if (!m_AlreadyUseItemList.empty())
	{
		for (std::map<int,int>::iterator it=m_AlreadyUseItemList.begin();it!=m_AlreadyUseItemList.end();it++)
		{
            assert(false);
		}
		return false;
	}
	else
		return false;
}
bool BattleManager::IsEnemy(int BattleID)
{
	return BattleID >= EnemyMinID && BattleID <= EnemyMaxID;
}
bool BattleManager::IsFriend(int BattleID)
{
	if (BattleID >= FriendMinID && BattleID <= FriendMaxID)
	{
		if (IsMainCharOperate())
			return  BattleID != m_pMainBattler[eOperateBattler_Char]->GetBattleID();
		else if (IsMainPetOperate())
			return  BattleID != m_pMainBattler[eOperateBattler_Pet]->GetBattleID();
	}
	return false;
}
bool BattleManager::IsMySelf(int BattleID)
{
	if (IsMainCharOperate())
		return  BattleID == m_pMainBattler[eOperateBattler_Char]->GetBattleID();
	else if (IsMainPetOperate())
		return  BattleID == m_pMainBattler[eOperateBattler_Pet]->GetBattleID();
	else
		return false;
}
bool BattleManager::IsAutoOperate()
{
	return m_bIsAutoBattle;
	return m_iAutoRound <= m_iAutoMaxRound + 1 && m_iAutoRound >= 0;
}

bool BattleManager::HandleRefusePK(const CEGUI::EventArgs& e)
{
	return true;
}
bool BattleManager::HandleAcceptPK(const CEGUI::EventArgs& e)
{
	return true;
}

int BattleManager::SCBattleIDChange(int& id)
{
	if (m_bBattleIDChange)
	{
		if (id <= FriendMaxID && id >= FriendMinID)
			id += OneSideMaxNum;
		else if (id <= EnemyMaxID && id >= EnemyMinID)
			id -= OneSideMaxNum;
	}
	return id;
}
bool BattleManager::UpdateMouseOverBattler(const Nuclear::NuclearLocation& aLocation)
{
	Nuclear::NuclearLocation ptMouse = aLocation;
	Nuclear::NuclearLocation maploc = gGetScene()->MousePointToPixel(ptMouse);
	std::vector<Nuclear::ISelectableObj*> objs;
	gGetScene()->SelectObjs(maploc, objs);

	if (!objs.empty())
	{
		Nuclear::ISelectableObj* pSelectObj = *(objs.begin());
		Nuclear::ISprite* sprobj = dynamic_cast<Nuclear::ISprite*>(pSelectObj);

		m_pMouseOverBattler = FindBattlerBySprite(sprobj);
		if (m_pMouseOverBattler)
		{
			m_pMouseOverBattler->OnSetCursor();
			return true;
		}
		else
		{
			if (gGetGameOperateState()->GetOperateState() != eCursorState_None)
			{
				gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_Forbid);
			}
			else
				gGetGameOperateState()->ResetBattleCursor();
			return true;
		}
	}
	else
	{
		m_pMouseOverBattler = NULL;
        if (gGetGameOperateState()->GetGameCursorType() == eGameCursorType_BattleSkill)
        {
            return true;
        }else        
		if (gGetGameOperateState()->GetOperateState() != eCursorState_None)
		{
			gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_Forbid);
		}
		return true;
	}
	return false;
}
bool BattleManager::OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
{
	if (!IsInBattleOperate() || IsAutoOperate())
	{
		return false;
	}
	switch (msg)
	{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	case WM_LBUTTONUP:
#else
    case Nuclear::WM_LBUTTONUP:
#endif
		{
		}
		break;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	case WM_LBUTTONDOWN:
#else
	case Nuclear::WM_LBUTTONDOWN:
#endif
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("BattleTips.CSetBattleID", 0, 0);
		}
		break;
	}
	return false;
}
bool BattleManager::PickArea(int battlerID)
{
	switch (gGetGameOperateState()->GetGameCursorType())
	{
		case eGameCursorType_BattleSkill:	//技能
		{
			int SkillType = gGetSkillTypeByID(GetCurSelectedSkillID());
			if (SkillType == eSkillType_Equip)
			{
				SendBattleCommand(battlerID, eOperate_Special);
			}
			else
			{
				SendBattleCommand(battlerID, eOperate_Skill);
			}
			gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_BattleNormal);
			return true;
		}
		break;
		case eGameCursorType_BattleItem:		//道具
		{
				SendBattleCommand(battlerID, eOperate_Item);
				gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_BattleNormal);
			return true;
		}
	}
	return false;
}
bool BattleManager::PickBattler(Battler* pBattler)
{
	if (pBattler == NULL)
	{
		return false;
	}
	std::wstring strEffectName = L"geffect/sprite/targetring";
	int targetID = pBattler->GetBattleID();
	switch (gGetGameOperateState()->GetGameCursorType())
	{
	case eGameCursorType_BattleNormal:	
	case eGameCursorType_BattleAttack:
	{
		if (pBattler->AffectByAttack())
		{
			SendBattleCommand(targetID, eOperate_Attack);
			pBattler->PlayEffect(strEffectName, false, true, false, 1, -2);
		}
		return true;
	}
	break;
	case eGameCursorType_BattleSkill:
	{
		if (pBattler->AffectedBySkill(GetCurSelectedSkillID()))
		{
			int SkillType = gGetSkillTypeByID(GetCurSelectedSkillID());
			if (SkillType == eSkillType_Equip)
			{
				SendBattleCommand(targetID, eOperate_Special);
			}
			else
			{
				SendBattleCommand(targetID, eOperate_Skill);
			}

			pBattler->PlayEffect(strEffectName);
			gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_BattleNormal);
		}
		return true;
	}
	break;
	case eGameCursorType_BattleItem:
	{
		if (pBattler->AffectedByItem(GetCurSelectedItem().m_Battleuse))
		{
			SendBattleCommand(targetID, eOperate_Item);
			pBattler->PlayEffect(strEffectName);
			gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_BattleNormal);
		}
		return true;
	}
	break;
	case eGameCursorType_BattleProtect:	
	{
		if (pBattler->AffectByProtect())
		{
			SendBattleCommand(targetID, eOperate_Protect);
		}
		return true;
	}
	break;
	case eGameCursorType_BattleCatch:
	{
		if (pBattler->AffectedByCatch())
		{
			if (cocos2d::gGetScriptEngine()->executeGlobalFunction("MainPetDataManager_IsMyPetFull"))
			{
				if (GetTipsManager())
				{
					GetTipsManager()->AddMsgTips(150080);
				}
			}
			else
			{
				const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(pBattler->GetBattleDataID());
				if (MonsterData.id != -1)
				{
					int petData_id = GetTableNumberFromLua("pet.cpetattr", MonsterData.petid, "id");
					if (petData_id != -1)
					{
						int petData_takelevel = GetTableNumberFromLua("pet.cpetattr", MonsterData.petid, "takelevel");
						std::wstring petData_name = GetTableWStringFromLua("pet.cpetattr", MonsterData.petid, "name");

						int iMainBattleCharLevel = GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL);
						if (petData_takelevel <= iMainBattleCharLevel)
						{
							SendBattleCommand(targetID, eOperate_Catch);
						}
						else
						{
							if (GetTipsManager())
							{
								std::vector<std::wstring> wsa;
								wsa.push_back(petData_name);
								wsa.push_back(StringCover::intTowstring(petData_takelevel));
								GetTipsManager()->AddMsgTips(150079, 0, wsa);
							}
						}
					}
				}
			}
		}
		return true;
	}
	break;
	default:
	{
		return false;
	}
	break;
	}
}

void BattleManager::SendBattleCommand(int TargetID,eBattleOperate eOperate,bool usingdefault)
{
	eOperateBattlerType eMainBattler = GetOperateMainBatterType();
	assert(eMainBattler == eOperateBattler_Char || eMainBattler == eOperateBattler_Pet);

	sBattleOperateCommand cmd;

	cmd.BattleOperate = eOperate;

	if (eOperate == eOperate_Skill || eOperate == eOperate_Item || eOperate == eOperate_Special)
	{
		if (TargetID != 0)
			cmd.TargetID = TargetID;
		else
			cmd.TargetID = GetDefaultTargetID(eOperate);		
	}
	else
	{
		cmd.TargetID = TargetID;
	}

	switch(eOperate)
	{
	case eOperate_Skill:
	case eOperate_Special:
		{
			if (usingdefault)
			{
				if (GetDefaultSkillID())
					cmd.OperatorID = GetDefaultSkillID();
				else
				{
					cmd.BattleOperate = eOperate_Attack;
				}
			}
			else if (m_iCurSelectedSkillID)
			{
				cmd.OperatorID = m_iCurSelectedSkillID;
			}
			else
			{
				XPTRACE(MHSD_UTILS::GETSTRING(738).c_str());
				cmd.BattleOperate = eOperate_Attack;
			}
		}
		break;
	case eOperate_Item:		
		{
			if (m_CurSelectedItem.m_ThisID != -1)
				cmd.OperatorID = m_CurSelectedItem.m_ThisID;
			else
			{
				XPTRACE(MHSD_UTILS::GETSTRING(740).c_str());
				cmd.BattleOperate = eOperate_Attack;
			}
		}
		break;
	case eOperate_Summon:
		{
			cmd.OperatorID = m_iSummonID;
		}
		break;
	default:
		break;
	}

	SCBattleIDChange(cmd.TargetID);

	if (CanSaveToDefaultOperate(cmd.BattleOperate,cmd.TargetID))
	{
		m_LastBattleOperateCmd[eMainBattler] = cmd;
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("BattleAutoFightDlg.CSendAutoOperateData", eMainBattler, cmd.BattleOperate, cmd.OperatorID);
	}

	if (eMainBattler == eOperateBattler_Char)
	{
		m_SendAction[eOperateBattler_Char].action.operationtype = (int)cmd.BattleOperate;
		m_SendAction[eOperateBattler_Char].action.aim	= cmd.TargetID;
		m_SendAction[eOperateBattler_Char].action.operationid = cmd.OperatorID;
		m_SendAction[eOperateBattler_Char].isrole = 1;
		m_SendAction[eOperateBattler_Char].autooperate = 0;
		gGetNetConnection()->send(m_SendAction[eOperateBattler_Char]);
	}
	else if (eMainBattler == eOperateBattler_Pet)
	{
		m_SendAction[eOperateBattler_Pet].action.operationtype = (int)cmd.BattleOperate;
		m_SendAction[eOperateBattler_Pet].action.aim	= cmd.TargetID;
		m_SendAction[eOperateBattler_Pet].action.operationid = cmd.OperatorID;
		m_SendAction[eOperateBattler_Pet].isrole = 0;
		m_SendAction[eOperateBattler_Pet].autooperate = 0;
		gGetNetConnection()->send(m_SendAction[eOperateBattler_Pet]);
	}
	FinishBattleOperate(eMainBattler);
}

void BattleManager::RefreshTwoItem()
{
	std::map<int, int>::iterator it = m_AlreadyUseItemList.find(0);
	if (it != m_AlreadyUseItemList.end())
	{
		int Count = it->second;
		SetUseItemCount(0, Count);
	}
	it = m_AlreadyUseItemList.find(1);
	if (it != m_AlreadyUseItemList.end())
	{
		int Count = it->second;
		SetUseItemCount(1, Count);
	}
}

void BattleManager::ClearAlreadyUseItemList()
{
	m_AlreadyUseItemList.clear();
}

void BattleManager::InsertAlreadyUseItemList(int nKey, int nValue)
{
	m_AlreadyUseItemList[nKey] = nValue;
}


void BattleManager::SetAlreadyUseItemList(std::map<int, int> list)
{
	m_AlreadyUseItemList = list;
	RefreshTwoItem();
	
}
void BattleManager::AddAlreadyUseItemList(int itembaseid)
{
	if (m_AlreadyUseItemList.find(itembaseid) != m_AlreadyUseItemList.end())
	{
		m_AlreadyUseItemList[itembaseid]++;
	}
	else
	{
		m_AlreadyUseItemList[itembaseid] = 1;
	}
}

void BattleManager::SendAutoCommand(bool autosend)
{
	if (IsMainCharOperate())
	{
		if (autosend)
		{
			m_SendAction[eOperateBattler_Char].autooperate = 1;
		}
		else
		{
			m_SendAction[eOperateBattler_Char].autooperate = 0;
		}
		m_SendAction[eOperateBattler_Char].action.operationtype = (int)m_LastBattleOperateCmd[eOperateBattler_Char].BattleOperate;
		if (autosend)
		{
			m_SendAction[eOperateBattler_Char].action.aim = 0;
		}
		else
		{
			m_SendAction[eOperateBattler_Char].action.aim = m_LastBattleOperateCmd[eOperateBattler_Char].TargetID;
		}
        printf("operate id:%d\n", m_LastBattleOperateCmd[eOperateBattler_Char].OperatorID);
		m_SendAction[eOperateBattler_Char].action.operationid = m_LastBattleOperateCmd[eOperateBattler_Char].OperatorID;
		m_SendAction[eOperateBattler_Char].isrole = 1;
		gGetNetConnection()->send(m_SendAction[eOperateBattler_Char]);
        FinishBattleOperate(eOperateBattler_Char);
	}
	if (IsMainPetOperate())
	{
		if (autosend)
		{
			m_SendAction[eOperateBattler_Pet].autooperate = 1;
		}
		else
		{
			m_SendAction[eOperateBattler_Pet].autooperate = 0;
		}
		m_SendAction[eOperateBattler_Pet].action.operationtype = (int)m_LastBattleOperateCmd[eOperateBattler_Pet].BattleOperate;
		if (autosend)
		{
			m_SendAction[eOperateBattler_Pet].action.aim = 0;
		}
		else
		{
			m_SendAction[eOperateBattler_Pet].action.aim = m_LastBattleOperateCmd[eOperateBattler_Pet].TargetID;
		}
		m_SendAction[eOperateBattler_Pet].action.operationid = m_LastBattleOperateCmd[eOperateBattler_Pet].OperatorID;
		m_SendAction[eOperateBattler_Pet].isrole = 0;
		gGetNetConnection()->send(m_SendAction[eOperateBattler_Pet]);
		FinishBattleOperate(eOperateBattler_Pet);
	}
}
void BattleManager::ResetOperateTime(int time )
{
	m_iOperateTime = time*1000;
}
void BattleManager::ResetPetAutoOperateCommand()
{
	m_LastBattleOperateCmd[eOperateBattler_Pet].Reset();
}
void BattleManager::CancelCursorForbid()
{
}
void BattleManager::CloseAllBattleDialog()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CharacterOperateDlg.DestroyDialog");
    //	CPetOperateDlg::OnExit();
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("PetOperateDlg.DestroyDialog");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattlePetSummonDlg.DestroyDialog");

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleSkillPanel.DestroyDialog");
	
}
void BattleManager::CloseCharacterOperateDlg()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CharacterOperateDlg.DestroyDialog");
//	//关闭逃跑确认框
//	//if (GetMessageManager())
//	//	GetMessageManager()->CloseConfirmBox(eConfirmBattleEscape);
    
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattlePetSummonDlg.DestroyDialog");

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleBag.DestroyDialog");

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleTiShi.DestroyDialog");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleSkillPanel.DestroyDialog");
}
void BattleManager::ClosePetOperateDlg()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("PetOperateDlg.DestroyDialog");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleBag.DestroyDialog");	
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleTiShi.DestroyDialog");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleSkillPanel.DestroyDialog");
}
void BattleManager::CloseOtherOperateDlg()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleBag.DestroyDialog");
}

void BattleManager::SetBattlerOperateStateNull()
{
	for_each(m_listBattler.begin(),m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::ClientSetOperateState),eOperateState_Null));

	for_each(m_listBattler.begin(),m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::SetDemoShowEnd),false));
}
void BattleManager::SetShowBattlerDemo(bool state)
{
	m_bShowDemoing = state;
	if (!m_bShowDemoing)
	{
		SetDelayDemoShow(0);
	}
	RefreshBattlerDemo();
}
void BattleManager::RefreshBattlerDemo()
{
	if (m_bShowDemoing)
	{
		for_each(m_listBattler.begin(),m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::RrfreshDemoShow),true));
	}
	else
	{
		for_each(m_listBattler.begin(),m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::RrfreshDemoShow),false));
	}
}
void BattleManager::BeginBattleOperate(eOperateBattlerType eMainBattler)
{
	if (m_bWatchModel)
	{
		SetBattleState(eBattleState_OperateChar);
        cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.ChangeBattleRound", m_iRoundCount+1);
	}
	////如果是回放状态////
	else if (m_bReplayModel)
	{
		SetBattleState(eBattleState_Wait);
		//播放回合数更新特效
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.ChangeBattleRound", m_iRoundCount+1);
	}
	else
	{
		if (eMainBattler == eOperateBattler_Char)
		{
			for_each(m_listBattler.begin(),m_listBattler.end(), std::bind2nd(std::mem_fun(&Battler::ClientSetOperateState),eOperateState_Prepare));
            cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LuaBattleUIManager.ChangeBattleRound", m_iRoundCount+1);
		}
		if (IsAutoOperate() && eMainBattler == eOperateBattler_Char)
		{
			m_iAutoRound--;		
		}
		//////////////////////
		gGetGameOperateState()->ResetBattleCursor();				
		m_iCurSelectedSkillID = m_pDefaultSkillID[eMainBattler];

		if (eMainBattler == eOperateBattler_Char)
		{
			SetBattleState(eBattleState_OperateChar);	
			if (!IsAutoOperate())
			{
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CharacterOperateDlg.InitOperateBtnEnbale");
				CheckPickBattler();
			}

			if (!gGetNewRoleGuideManager()->isBattleGuideFinsh(m_BattleID, m_iRoundCount+1))
			{
				gGetNewRoleGuideManager()->GuideStartBattle(m_BattleID, m_iRoundCount+1);
			}
		}
		else if (eMainBattler == eOperateBattler_Pet)
		{
			SetBattleState(eBattleState_OperatePet);	
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("PetOperateDlg.GetSingletonDialogAndShowIt");
			CheckPickBattler();		    
		}
	}
}
void BattleManager::FinishBattleOperate()
{
	gGetGameOperateState()->ResetBattleCursor();
	SetCurSelectedSkillID(0);
	CloseCharacterOperateDlg();
	ClosePetOperateDlg();
	CloseOtherOperateDlg();
}
void BattleManager::FinishBattleOperate(eOperateBattlerType eMainBattler)
{
	//鼠标还原
	gGetGameOperateState()->ResetBattleCursor();
	SetCurSelectedSkillID(0);
	gGetMessageManager()->CloseAllConfirmBox();
	if (eMainBattler == eOperateBattler_Char)
	{
		CloseCharacterOperateDlg();
		CloseOtherOperateDlg();
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleSkillPanel.DestroyDialog");
		if (m_pMainBattler[eOperateBattler_Pet])
			BeginBattleOperate(eOperateBattler_Pet);
		else
			SetBattleState(eBattleState_Wait);
	}
	else if (eMainBattler == eOperateBattler_Pet)
	{
		ClosePetOperateDlg();
		CloseOtherOperateDlg();
		
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("BattleSkillPanel.DestroyDialog");
		SetBattleState(eBattleState_Wait);
	}
	CheckPickBattler();
}
void BattleManager::BeginAutoOperate()
{
	if (m_bWatchModel)
		return;
	m_bIsAutoBattle = true;
	if (IsInBattle())
	{
		m_bIsAutoBattleQuick = true;
	}

	fire::pb::hook::CSetAutoBattle cmd;
	cmd.isautobattle = 1;
	gGetNetConnection()->send(cmd);
    cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("BattleAutoFightDlg.CSetAutoFight", true);
}
void BattleManager::EndAutoOperate()
{
	if (!m_bIsAutoBattle)
		return;
	m_bIsAutoBattle = false;
	if (m_iOperateTime >= 500 && m_iOperateTime <= c_nBattleOperateTime * 1000)
	{
		if (m_iOperateTime <= 28000 || m_eBattleState == eBattleState_Wait)
		{
			fire::pb::hook::CSetAutoBattle cmd;
			cmd.isautobattle = 0;
			gGetNetConnection()->send(cmd);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("BattleAutoFightDlg.CSetAutoFight", false);
			return;
		}
	}
	if (IsInBattle())
	{
		m_bIsAutoBattleQuick = false;
	}

	fire::pb::hook::CSetAutoBattle cmd;
	cmd.isautobattle = 0;
	gGetNetConnection()->send(cmd);


    cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("BattleAutoFightDlg.CSetAutoFight", false);
	if (m_eBattleState == eBattleState_DemoOperate || m_eBattleState == eBattleState_OperateChar || m_eBattleState == eBattleState_OperatePet || m_eBattleState == eBattleState_Wait)
	{
		BeginBattleOperate(eOperateBattler_Char);
    }
}
void BattleManager::UpdateItemRemove(int itemthisid)
{
}
bool BattleManager::IsInBattleOperate()
{
	return (m_eBattleState == eBattleState_OperateChar || m_eBattleState == eBattleState_OperatePet && m_pMainBattler[eOperateBattler_Pet]) && !m_bWatchModel && !m_bReplayModel;
}