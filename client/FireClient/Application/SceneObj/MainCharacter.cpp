#include "stdafx.h"
#include "MainCharacter.h"
#include "GameScene.h"
#include "FossickNpc.h"
#include "GameOperateState.h"
#include "MainRoleDataManager.h"
#include "BattleManager.h"
#include "fire/pb/move/CRoleMove.hpp"
#include "fire/pb/move/CRoleTurn.hpp"
#include "fire/pb/move/CRoleStop.hpp"
#include "fire/pb/move/CRelocateRolePos.hpp"
#include "fire/pb/move/CSendAutoMovePathID.hpp"
#include "fire/pb/npc/CVisitNpc.hpp"
#include "GameUIManager.h"
#include "SceneNpc.h"
#include "IniManager.h"
#include "LoginManager.h"
#include "GameTable/common/CCommon.h"
#include "ConfigManager.h"
#include "ProtoDef/fire/pb/mission/CReqGoto.hpp"
#include "ProtoDef/fire/pb/circletask/CReqGotoPatrol.hpp"
#include "GameApplication.h"
#include <common/nuworldlogiccoord.h>
#include "engine/nuengine.h"
#include "effect/nueffectmanager.h"
#if (defined WIN7_32) && (defined _DEBUG)
#include "SceneMovieManager.h"
#endif
#include "GameTable/move/CAutoMovePath.h"
#include "GameTable/move/CAutoMovePathPoint.h"

#define FOSSICK_DIS 7
const int c_iRandomPacingRadius = 16;

void MainCharacter::sPurgeData()
{
	MainCharacter *instance = MainCharacter::GetInstance();
	if (instance)
	{
		instance->RemoveAutoWalkingEffect();
	}
}
MainCharacter::MainCharacter()
	: m_dwLButtonDownTime(0)
	, m_bLButtonDown(false)
	, m_iLastVerifyTime(500)
	, m_iAreaCheckCoolDown(2000)
	, m_eMoveState(eMove_Normal)
	, m_ptLastMoveStart(-1, -1)
	, m_ptLastMoveTarget(-1, -1)
	, m_PartTarget(-1, -1)
	, m_bReadyToPacing(false)
	, m_gotoVisitNpcID(0)
	, m_gotoVisitNpcKey(0)
	, m_gotoPickUpItemID(0)
	, m_pAutoWalkingEffect(NULL)
	, m_bPVPArea(false)
	, m_iLastMoveBeginning(0, 0)	//测试用
	, m_pPaceWalkingEffect(NULL)
	, m_bHide(false)
	, m_iPacingWithAvoidCreepBuffTipsTime(-1)
	, m_bJumpToIslandTips(false)
	, m_bNeedToIslandTips(false)
	, m_bJustFlyGoto(false)
	, m_lastTalkNpcID(0)
	, m_iCurItemkey(0)
	, m_bJumpEnd(false)
	, m_bflyToEnable(false)
	, m_bCanMove(false)
	, m_bIsSending(false)
	, m_AutoMovePathID(0)
	, m_AutoMovePathPointIndex(0)
	, m_pFlyWalkingEffect(NULL)
	, m_FlyStopTime(0)
{
	GetBattleManager()->EventBeginBattle += CBroadcastEvent<NoParam>::EventBind(this, &MainCharacter::OnBattleBegin);
	GetBattleManager()->EventEndBattle += CBroadcastEvent<NoParam>::EventBind(this, &MainCharacter::OnBattleEnd);
	m_NotVerifyQueue.clear();

	m_fFlyToCd = 500;
	m_fFlyToCdDt = 0;
	m_bLockGoto = false;
	m_IsMainCharacter = true;
}
MainCharacter::~MainCharacter()
{
	m_NotVerifyQueue.clear();
}

Nuclear::NuclearLocation MainCharacter::ReCalculateTarget(Nuclear::NuclearLocation& ptMoveTarget, bool bHighlevel)
{
	Nuclear::NuclearLocation ptCurrent = GetLogicLocation();

	return FindFirstReachablePoint(ptCurrent, ptMoveTarget, bHighlevel);
}
Nuclear::NuclearLocation MainCharacter::FindFirstReachablePoint(Nuclear::NuclearLocation& pStart, Nuclear::NuclearLocation& pEnd, bool bHighlevel)
{
	eMoveMask movemask = bHighlevel ? eMoveMaskHigh : eMoveMaskNormal;
	if (Nuclear::distance(pStart, pEnd) < g_logicGrid)
	{
		if (gGetScene()->IsMoveBlock(pEnd, movemask))
		{
			pEnd = pStart;
		}
		return pEnd;
	}
	while (Nuclear::distance(pStart, pEnd) >= g_logicGrid)
	{
		if (abs(pStart.x - pEnd.x) > abs(pStart.y - pEnd.y))
		{
			pEnd.x += (pStart.x - pEnd.x) > 0 ? g_logicGrid : -g_logicGrid;
		}
		else if (abs(pStart.x - pEnd.x) < abs(pStart.y - pEnd.y))
		{
			pEnd.y += (pStart.y - pEnd.y) > 0 ? g_logicGrid : -g_logicGrid;
		}
		else
		{
			pEnd.x += (pStart.x - pEnd.x) > 0 ? g_logicGrid : -g_logicGrid;
			pEnd.y += (pStart.y - pEnd.y) > 0 ? g_logicGrid : -g_logicGrid;
		}
		if (!gGetScene()->IsMoveBlock(pEnd, movemask))
			break;
	}
	return pEnd;
}
Nuclear::NuclearLocation MainCharacter::FindFirstBlockPoint(Nuclear::NuclearLocation& pStart, Nuclear::NuclearLocation& pEnd)
{
	Nuclear::NuclearLocation pBlock = pStart;
	while (Nuclear::distance(pBlock, pEnd) > g_logicGrid)
	{
		if (gGetScene()->IsMoveBlock(pBlock, GetMoveMask()))
			break;
		if (abs(pStart.x - pEnd.x) > abs(pStart.y - pEnd.y))
		{
			pBlock.x += (pStart.x - pEnd.x) > 0 ? g_logicGrid : -g_logicGrid;
		}
		else if (abs(pStart.x - pEnd.x) < abs(pStart.y - pEnd.y))
		{
			pBlock.y += (pStart.y - pEnd.y) > 0 ? g_logicGrid : -g_logicGrid;
		}
		else
		{
			pBlock.x += (pStart.x - pEnd.x) > 0 ? g_logicGrid : -g_logicGrid;
			pBlock.y += (pStart.y - pEnd.y) > 0 ? g_logicGrid : -g_logicGrid;
		}

	}
	return pBlock;
}



bool MainCharacter::FindReachablePointNearby(const Nuclear::NuclearLocation& start, const Nuclear::NuclearLocation& end, Nuclear::NuclearLocation& foundPos)
{
	for (int r = -1; r <= 1; ++r)
	{
		for (int c = -1; c <= 1; ++c)
		{
			if (!(r == 0 && c == 0))
			{
				Nuclear::NuclearLocation tmpEnd(end.x + c * g_logicGrid, end.y + r * g_logicGrid);
				if (!gGetScene()->IsMoveBlock(tmpEnd, GetMoveMask()) && Nuclear::distance(start, tmpEnd) > g_logicGrid)
				{
					foundPos = tmpEnd;
					return true;
				}
			}
		}
	}

	return false;
}

std::wstring MainCharacter::GetName()
{
	return s2ws(GetMainRoleDataStrValue("strName"));
}
std::vector<Nuclear::NuclearPoint> MainCharacter::GetAstarPath() {
	std::vector<Nuclear::NuclearPoint> pathVec;
	for (astar::Path::iterator it = m_pathSaved.begin(); it != m_pathSaved.end(); it++)
	{
		Nuclear::NuclearPoint aPoint(it->x, it->y);
		pathVec.push_back(aPoint);
	}
	return pathVec;
}
int MainCharacter::GetCurItemkey()
{
	return m_iCurItemkey;
}
std::wstring MainCharacter::GetTitle()
{
	CallLuaUtil util;
	if (util.callLua("MainRoleDataManager.GetCurTilte_"))
	{
		return s2ws(util.getRetCString());
	}
	return L"";
}
int MainCharacter::GetDistanceWithPos(int nPosX, int nPosy)
{
	float dis = Nuclear::distance(GetGridLocation(), Nuclear::NuclearLocation(nPosX, nPosy));
	return dis;
}

void MainCharacter::SetAttribute(int64_t roleid, int sex, int shape, int camp)
{
	SetID(roleid);
	SetLevel(GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL));
	m_data.eSex = (eSexType)sex;
	m_data.shape = shape;
	m_data.camp = camp;
	if (NULL == GetSprite())
	{
		NewSpriteModel(shape);
	}

	if (GetSprite())
	{
		GetSprite()->SetUpdateEveryFrame(true);
	}

	gGetScene()->AttachCameraTo(GetSprite());

}
void MainCharacter::SetRandomPacing()
{
	if (!m_pTeamFollower)
	{
		Nuclear::NuclearLocation target = GetLogicLocation();
		for (int i = c_iRandomPacingRadius; i > 0; i--)
		{
			target = gGetScene()->GetNearByPos(this, c_iRandomPacingRadius);
			if (target != GetLogicLocation() && Nuclear::distance(target, GetLogicLocation()) >= g_logicGrid)
			{
				m_bReadyToPacing = true;
				GetMainCharacter()->ActiveMoveTo(target);
				break;
			}
		}
	}
}
void MainCharacter::SetGotoTargetPos(int nXPos, int nYPos)
{
	m_gotoTargetLocation.x = nXPos;
	m_gotoTargetLocation.y = nYPos;
}
void MainCharacter::SetGotoTargetPosType(int nType)
{
	m_nTargetPosType = nType;
}
void MainCharacter::SetTeamInfo(int64_t teamid, int teamindex, int teamstate)
{
	Character::SetTeamInfo(teamid, teamindex, teamstate);
	if (m_pTeamFollower)
	{
		SetMoveState(eMove_Follow);
		HandleEnterOrLeavePVPArea();
	}
	else
	{
		if (m_eMoveState != eMove_Pacing && m_eMoveState != eMove_Fly)
		{
			SetMoveState(eMove_Normal);
			ResetLastMoveTarget();
		}
		HandleEnterOrLeavePVPArea();
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("TaskManager_CToLua_CheckAreaQuest");
	}
}
void MainCharacter::SetMoveState(eMoveState e)
{
	if (e == eMove_Pacing)
	{
		m_bReadyToPacing = false;
		if (NULL == m_pPaceWalkingEffect)
		{
			m_pPaceWalkingEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10159), 0, 0, 0, true, true, false, true);
		}
		if (m_pAutoWalkingEffect)
		{
			RemoveDurativeEffect(m_pAutoWalkingEffect);
			m_pAutoWalkingEffect = NULL;
		}
	}
	else
	{
		if (e == eMove_Follow)
		{
			if (m_pAutoWalkingEffect)
			{
				RemoveDurativeEffect(m_pAutoWalkingEffect);
				m_pAutoWalkingEffect = NULL;
			}
		}
		if (m_pPaceWalkingEffect)
		{
			RemoveDurativeEffect(m_pPaceWalkingEffect);
			m_pPaceWalkingEffect = NULL;
		}
	}
	if (e == eMove_Pacing && m_eMoveState != eMove_Normal)
		return;
	if (e == eMove_Normal)
	{
		if (m_eMoveState == eMove_Follow)
		{
			StopMove();
		}
		m_bReadyToPacing = false;
		if (gGetGameOperateState()->GetGameCursorType() == eGameCursorType_Pacing)
		{
			gGetGameOperateState()->ResetCursor();
		}

		m_ptLastMoveStart = Nuclear::NuclearLocation(-1, -1);
	}
	else if (e == eMove_Pacing)
	{
		m_bReadyToPacing = false;
		if (gGetGameOperateState()->GetGameCursorType() == eGameCursorType_Pacing)
		{
			gGetGameOperateState()->ResetCursor();
		}
	}

	m_eMoveState = e;

	if (GetBattleManager())
	{
		if (!GetBattleManager()->IsInBattle())
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LogoInfoDialog.GetSingletonDialogAndShowIt");
		}
	}

}
bool MainCharacter::GetIsSending()
{
	return m_bIsSending;
}
void MainCharacter::SetIsSending(bool isSend)
{
	m_bIsSending = isSend;
}
void MainCharacter::SetShapeID(int shapeid)
{
	Character::SetShapeID(shapeid);
	cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainRoleDataManager.SetMainCharacterShape", shapeid);
}
void MainCharacter::SetCurItemkey(int key)
{
	m_iCurItemkey = key;
}


bool MainCharacter::OnVisitFossickNpc(Npc* pNpc)
{
	if (NULL == pNpc)
	{
		return false;
	}
	if (pNpc->IsVisiable() == false)
		return false;

	m_bLButtonDown = false;
	int dixs1 = Nuclear::distance(gGetScene()->PixelToGrid(pNpc->GetLocation()), gGetScene()->PixelToGrid(GetLocation()));

	if (pNpc->GetNpcTypeID() == eNpcTypeFossick)
	{
		if (dixs1 >= FOSSICK_DIS)
		{
			return !ActiveMoveTo(pNpc->GetRandomVisitPosition(), pNpc->GetNpcBaseID(), true, pNpc->GetID());
		}
		else
		{
			FossickNpc* pFossicNpc = static_cast<FossickNpc*>(pNpc);
			if (!pFossicNpc->IsInCoolState())
			{
				cocos2d::gGetScriptEngine()->pushInt64ToLuaStack(pNpc->GetID());
				cocos2d::gGetScriptEngine()->executeGlobalFunction("MainRoleDataManager.SetLastVisitNpcID", 1);

				if (pNpc->GetID() > 0)
				{
					fire::pb::npc::CVisitNpc visitNpc(pNpc->GetID());
					gGetNetConnection()->send(visitNpc);
				}
			}
		}
	}
	return true;
}
bool MainCharacter::OnFlyWalkToVisitNpc(Npc* pNpc)
{
	if (NULL == pNpc)
	{
		return false;
	}
	if (pNpc->IsVisiable() == false)
		return false;
	int dixs1 = Nuclear::distance(gGetScene()->PixelToGrid(pNpc->GetLocation()), gGetScene()->PixelToGrid(GetLocation()));

	m_bJustFlyGoto = false;
	bool bResult = false;
	if (dixs1 >= g_gridDistance)
	{
		int64_t nNpcKey = pNpc->GetID();
		int nNpcId = pNpc->GetNpcBaseID();
		GetMainCharacter()->WalkToPos(m_gotoTargetLocation.x, m_gotoTargetLocation.y, nNpcKey, nNpcId);
		bResult = true;
	}
	else
	{
		bResult = HandleVisiteNpc();
	}
	return bResult;
}
bool MainCharacter::OnFlyWalkToVisitPos(int64_t nNpcKey, int nNpcId)
{
	int dixs1 = Nuclear::distance(m_gotoTargetLocation, gGetScene()->PixelToGrid(GetLocation()));
	bool bResult = false;
	int nDisToTarget = g_gridDistance;
	if (nNpcKey == 0 && nNpcId == 0)
	{
		nDisToTarget = 3;
	}
	if (dixs1 >= nDisToTarget)
	{
		Nuclear::NuclearLocation loc;
		loc.x = m_gotoTargetLocation.x*g_logicGrid;
		loc.y = m_gotoTargetLocation.y*g_logicGrid;

		if (gGetGameUIManager()->getCurrentItemId() != -1)
		{
			GetMainCharacter()->WalkToPos(m_gotoTargetLocation.x, m_gotoTargetLocation.y);
		}
		else
		{
			GetMainCharacter()->WalkToPos(m_gotoTargetLocation.x, m_gotoTargetLocation.y, nNpcKey, nNpcId);
		}
		bResult = true;
	}
	else
	{
		bResult = HandleVisiteNpc();
		if (gGetGameUIManager()->getCurrentItemId() != -1)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("Taskuseitemdialog.getInstanceAndLoadData", gGetGameUIManager()->getCurrentItemId(), GetMainCharacter()->GetCurItemkey());
			GetMainCharacter()->SetGotoTargetPos(0, 0);
			gGetGameUIManager()->setCurrentItemId(-1);
			gGetGameUIManager()->setTreasureMapId(-1);
		}
	}

	return bResult;
}
bool MainCharacter::OnVisitNpc(Npc* pNpc)
{
	if (NULL == pNpc)
	{
		return false;
	}
	if (pNpc->IsVisiable() == false)
		return false;

	m_bLButtonDown = false;

	Nuclear::NuclearLocation myGrid = GetGridLocation();
	Nuclear::NuclearLocation npcGrid = pNpc->GetGridLocation();

	Nuclear::NuclearLocation myPos = GetLocation();
	Nuclear::NuclearLocation npcPos = pNpc->GetLocation();

	Nuclear::NuclearLocation myPosGrid = gGetScene()->PixelToGrid(myPos);
	Nuclear::NuclearLocation npcPosGrid = gGetScene()->PixelToGrid(npcPos);
	int dixs1 = Nuclear::distance(myPosGrid, npcPosGrid);

	if (pNpc->GetNpcTypeID() == eNpcItemBox)
	{
		if (dixs1 >= FOSSICK_DIS)
		{
			return !ActiveMoveTo(pNpc->GetRandomVisitPosition(), pNpc->GetNpcBaseID(), true, pNpc->GetID());
		}
		else
		{
			//fire::pb::npc::COpenChest OpenChest(pNpc->GetID());
			//gGetNetConnection()->send(OpenChest);
			//return true;
			int time = StringCover::WStrToNum<int>(GameTable::common::GetCCommonTableInstance().getRecorder(304).value);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("ReadTimeProgressDlg.Start", StringCover::to_string(pNpc->GetName()).c_str(), 4, time, 0, -1);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithDoubleData("ReadTimeProgressDlg.setType", pNpc->GetID());
			return true;
		}
	}

	if (dixs1 >= g_gridDistance)	//10个格子以外不允许访问
	{
		return !ActiveMoveTo(pNpc->GetLogicLocation(), pNpc->GetNpcBaseID(), true, pNpc->GetID());
	}

	fire::pb::move::CRoleMove RoleMove(fire::pb::move::Pos(GetLogicLocation().x, GetLogicLocation().y), fire::pb::move::Pos(GetLogicLocation().x, GetLogicLocation().y), gGetScene()->GetMapSceneID());
	gGetNetConnection()->send(RoleMove);

	if (pNpc->GetNpcTypeID() == eNpcFuyuanBox){
		fire::pb::npc::CVisitNpc visitNpc(pNpc->GetID());
		gGetNetConnection()->send(visitNpc);
		return true;
	}

	//朝向NPC
	int dir = (int)GetDirection();

	TurnTo(pNpc);

	//只有朝向变了，才发送改变朝向协议----杨斌
	if (dir != (int)GetDirection())
	{
		fire::pb::move::CRoleTurn RoleTurn((int)GetDirection());
		gGetNetConnection()->send(RoleTurn);
	}

	cocos2d::gGetScriptEngine()->pushInt64ToLuaStack(pNpc->GetID());
	cocos2d::gGetScriptEngine()->executeGlobalFunction("MainRoleDataManager.SetLastVisitNpcID", 1);

	if (pNpc->GetID() > 0)
	{
		fire::pb::npc::CVisitNpc visitNpc(pNpc->GetID());
		gGetNetConnection()->send(visitNpc);
	}
	//	VMProtectEnd
	return true;
}
void MainCharacter::OnBattleBegin()
{
	SceneObject::StopMove();
}
void MainCharacter::OnBattleEnd()
{
	if (GetMainCharacter()->GetFlyToEnable() == true)
	{
		GetMainCharacter()->SetFlyToEnable(false);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("treasureChosedDlg.startRunTimer");
	}
	ContinueMove();
}
bool MainCharacter::OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	Nuclear::NuclearLocation mousepoint;
	Nuclear::NuclearLocation maploc = gGetScene()->MousePointToPixel(mousepoint);
	//maploc的x坐标最好是3的倍数
	maploc.x = maploc.x / 3 * 3;
	if (maploc.x <= 0)
		maploc.x = 1;
	switch (msg)
	{
#ifndef WIN32
	case Nuclear::WM_LBUTTONUP:
#else
	case WM_LBUTTONUP:
#endif
	{
		m_bLButtonDown = false;
		return true;
	}
	break;
#ifndef WIN32
	case Nuclear::WM_RBUTTONDOWN:
#else
	case WM_RBUTTONDOWN:
#endif
	{
		//取消准备自动遇敌
		m_bReadyToPacing = false;
		return false;
	}
	break;
#ifndef WIN32
	case Nuclear::WM_LBUTTONDOWN:
#else
	case WM_LBUTTONDOWN:
#endif
	{
		return OnLBButtonDown(wParam, lParam);
	}
	break;
#ifndef WIN32
	case Nuclear::WM_MOUSEMOVE:
#else
	case WM_MOUSEMOVE:
#endif
	{
		return false;
	}
	break;
	case Nuclear::WM_LONGPRESS:
	{

		break;
	}
	default:
		return false;
	}
	return false;
}

void MainCharacter::RefreshRoleInfoOfThisServer()
{
	std::string strIniPath(LASTACCOUNT_SERVER_INI);
	IniManager IniMgr(strIniPath);

	std::wstring accountnum(L"");
	bool bExistLast = IniMgr.GetValueByName(L"AccountInfo", L"num", accountnum);
	//
	if (bExistLast)
	{
		int unum = StringCover::WStrToNum<int>(accountnum);
		unum = (unum < 10) ? unum : 10;
		bool exist_user_already = false;
		for (int user_i = 1; user_i <= unum; user_i++)
		{
			std::wstring username(L"");
			IniMgr.GetValueByName(L"Account" + StringCover::intTowstring(user_i), L"username", username);
			if (username == gGetLoginManager()->GetAccount())
			{
				exist_user_already = true;

				std::wstring servernum(L"0");
				IniMgr.GetValueByName(L"Account" + StringCover::intTowstring(user_i), L"servernum", servernum);
				int snum = StringCover::WStrToNum<int>(servernum);
				bool exist_server_already = false;
				for (int server_i = 1; server_i <= snum; server_i++)
				{
					std::wstring servername(L"");
					IniMgr.GetValueByName(L"Account" + StringCover::intTowstring(user_i),
						L"server" + StringCover::intTowstring(server_i) + L"name", servername);
					if (servername == gGetLoginManager()->GetSelectServer())
					{
						exist_server_already = true;
						IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
							L"server" + StringCover::intTowstring(server_i) + L"lvl", StringCover::intTowstring(GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)));

						int ShapeConfig_littleheadID = GetTableNumberFromLua("npc.cnpcshape", GetMainRoleDataNumValue<int>("shape"), "littleheadID");
						IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
							L"server" + StringCover::intTowstring(server_i) + L"icon", StringCover::intTowstring(ShapeConfig_littleheadID));

						const int64_t curRoleID = GetMainRoleDataNumValue<int64_t>("roleid");
						IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
							L"server" + StringCover::intTowstring(server_i) + L"roleid", StringCover::intTowstring(curRoleID));

						const std::wstring &curRoleName = s2ws(GetMainRoleDataStrValue("strName"));
						IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
							L"server" + StringCover::intTowstring(server_i) + L"rolename", curRoleName);
					}
				}

				if (!exist_server_already)
				{
					IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i), L"servernum", StringCover::intTowstring(snum + 1));
					IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
						L"server" + StringCover::intTowstring(snum + 1) + L"name", gGetLoginManager()->GetSelectServer());
					IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
						L"server" + StringCover::intTowstring(snum + 1) + L"lvl", StringCover::intTowstring(GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)));

					int ShapeConfig_littleheadID = GetTableNumberFromLua("npc.cnpcshape", GetMainRoleDataNumValue<int>("shape"), "littleheadID");
					IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
						L"server" + StringCover::intTowstring(snum + 1) + L"icon", StringCover::intTowstring(ShapeConfig_littleheadID));

					const int64_t curRoleID = GetMainRoleDataNumValue<int64_t>("roleid");
					IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
						L"server" + StringCover::intTowstring(snum + 1) + L"roleid", StringCover::intTowstring(curRoleID));

					const std::wstring &curRoleName = s2ws(GetMainRoleDataStrValue("strName"));
					IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
						L"server" + StringCover::intTowstring(snum + 1) + L"rolename", curRoleName);

				}
			}

		}

		if (!exist_user_already)
		{
			int writepos = unum + 1;
			if (unum >= 10)
			{
				int findmin = 99;
				for (int user_i = 1; user_i <= unum; user_i++)
				{
					std::wstring strcount(L"");
					IniMgr.GetValueByName(L"Account" + StringCover::intTowstring(user_i), L"servernum", strcount);
					int servercount = StringCover::WStrToNum<int>(strcount);
					int selfMax = 0;
					for (int j = 0; j < servercount; j++)
					{
						std::wstring serverlevel(L"");
						IniMgr.GetValueByName(L"Account" + StringCover::intTowstring(user_i), L"server" + StringCover::intTowstring(j) + L"lvl", serverlevel);

						int vl = StringCover::WStrToNum<int>(serverlevel);
						if (selfMax < vl)
							selfMax = vl;
					}
					if (selfMax <= findmin)
					{
						findmin = selfMax;
						writepos = user_i;
					}
				}
				IniMgr.RemoveSection("Account" + StringCover::intToString(writepos));
				IniMgr.WriteValueByName(L"AccountInfo", L"num", StringCover::intTowstring(10));
			}
			else
			{
				IniMgr.WriteValueByName(L"AccountInfo", L"num", StringCover::intTowstring(unum + 1));
			}
			IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(writepos), L"servernum", L"1");
			IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(writepos),
				L"username", gGetLoginManager()->GetAccount());
			IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(writepos),
				L"channelid", gGetLoginManager()->GetChannelId());
			IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(writepos),
				L"server1name", gGetLoginManager()->GetSelectServer());
			IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(writepos),
				L"server1lvl", StringCover::intTowstring(GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)));

			int ShapeConfig_littleheadID = GetTableNumberFromLua("npc.cnpcshape", GetMainRoleDataNumValue<int>("shape"), "littleheadID");
			IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(writepos),
				L"server1icon", StringCover::intTowstring(ShapeConfig_littleheadID));

			const int64_t curRoleID = GetMainRoleDataNumValue<int64_t>("roleid");
			IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(writepos),
				L"server1roleid", StringCover::intTowstring(curRoleID));

			const std::wstring &curRoleName = s2ws(GetMainRoleDataStrValue("strName"));
			IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(writepos),
				L"server1rolename", curRoleName);
		}
	}
	else
	{
		IniMgr.WriteValueByName(L"AccountInfo", L"num", L"1");
		IniMgr.WriteValueByName(L"Account1", L"username", gGetLoginManager()->GetAccount());
		IniMgr.WriteValueByName(L"Account1", L"servernum", L"1");
		IniMgr.WriteValueByName(L"Account1", L"server1name", gGetLoginManager()->GetSelectServer());
		IniMgr.WriteValueByName(L"Account1", L"server1lvl", StringCover::intTowstring(GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)));
		IniMgr.WriteValueByName(L"Account1", L"channelid", gGetLoginManager()->GetChannelId());

		int ShapeConfig_littleheadID = GetTableNumberFromLua("npc.cnpcshape", GetMainRoleDataNumValue<int>("shape"), "littleheadID");
		IniMgr.WriteValueByName(L"Account1", L"server1icon", StringCover::intTowstring(ShapeConfig_littleheadID));

		const int64_t curRoleID = GetMainRoleDataNumValue<int64_t>("roleid");
		IniMgr.WriteValueByName(L"Account1", L"server1roleid", StringCover::intTowstring(curRoleID));

		const std::wstring &curRoleName = s2ws(GetMainRoleDataStrValue("strName"));
		IniMgr.WriteValueByName(L"Account1", L"server1rolename", curRoleName);
	}
}
void MainCharacter::HandleEnterOrLeavePVPArea()
{
	bool bInPVP = gGetScene()->IsInPVPArea(GetLogicLocation());
	if ((bInPVP && !m_bPVPArea) || (!bInPVP && m_bPVPArea))
	{
		m_bPVPArea = !m_bPVPArea;
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ShowHide.EnterLeavePVPArea");
	}
}
void MainCharacter::InitPosition(Nuclear::NuclearLocation loc, bool bhighlevel)
{
	SetLogicLocation(loc);
	//设置当前层
	SetInHighLevel(bhighlevel);

	m_iLastMoveBeginning = loc;	//测试用

	m_ptLastMoveTarget = loc;
	m_PartTarget = loc;
	m_data.ptTargetPos = loc;

	PlayAction(eActionStand);

	m_bPVPArea = gGetScene()->IsInPVPArea(GetLogicLocation());
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ShowHide.EnterLeavePVPArea");

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("TaskManager_CToLua_CheckAreaQuest");

	//朝向地图中央
	Nuclear::NuclearLocation mapmiddle;
	Nuclear::GetEngine()->GetWorld()->GetMapSize(mapmiddle);
	SetDirection(mapmiddle.x - GetLocation().x, mapmiddle.y - GetLocation().y);

	//StopPacingMove();
}
void MainCharacter::StopPacingMove()
{
	if (m_eMoveState == eMove_Pacing || m_bReadyToPacing)
	{
		SetMoveState(eMove_Normal);
		StopMove();
	}
}
void MainCharacter::PlayMouseBtnDownEffect(Nuclear::NuclearLocation maploc)
{
	Nuclear::NuclearLocation logicloc = Nuclear::NuclearWorldLogicCoord::world2logic((int)maploc.x, (int)maploc.y);
	unsigned char  groundtype = gGetScene()->GetGroundType(logicloc);

	switch (groundtype)
	{
	case 0:
		Nuclear::GetEngine()->GetWorld()->PlayEffect(MHSD_UTILS::GetEffectPath(10263), Nuclear::XPEL_NORMAL_UNDER_SPRITE, maploc.x, maploc.y, 1, false, 2);
		break;
	case 1:	//尘土地
		Nuclear::GetEngine()->GetWorld()->PlayEffect(MHSD_UTILS::GetEffectPath(10260), Nuclear::XPEL_NORMAL_UNDER_SPRITE, maploc.x, maploc.y, 1, false, 2);
		break;
	case 2:	//草地
		Nuclear::GetEngine()->GetWorld()->PlayEffect(MHSD_UTILS::GetEffectPath(10261), Nuclear::XPEL_NORMAL_UNDER_SPRITE, maploc.x, maploc.y, 1, false, 2);
		break;
	case 3:	//水面
		Nuclear::GetEngine()->GetWorld()->PlayEffect(MHSD_UTILS::GetEffectPath(10262), Nuclear::XPEL_NORMAL_UNDER_SPRITE, maploc.x, maploc.y, 1, false, 2);
		break;
	default:
		break;
	}

}
void MainCharacter::InitMouseBtnDownEffect()
{
	Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());
	if (pEngine)
	{
		pEngine->GetEffectManager()->CreateEffect(MHSD_UTILS::GetEffectPath(10260), false, true);
		pEngine->GetEffectManager()->CreateEffect(MHSD_UTILS::GetEffectPath(10261), false, true);
		pEngine->GetEffectManager()->CreateEffect(MHSD_UTILS::GetEffectPath(10262), false, true);
		pEngine->GetEffectManager()->CreateEffect(MHSD_UTILS::GetEffectPath(10263), false, true);
	}
}
bool MainCharacter::OnLBButtonDown(WPARAM wParam, LPARAM lParam)
{
	Nuclear::GetEngine()->GetWorld()->SetUseSmooth(true);
	if (gGetScene()
		&& gGetScene()->isLoadMaping() == false
		&& gGetScene()->IsMapJumping() == false)
	{
		gGetScene()->UpdateMouseOverObjects(Nuclear::NuclearLocation(wParam, lParam));
	}

	//杨斌-----npc重叠弹出列表
	std::list<SceneObject*> objs = gGetScene()->GetMouseOverObjectsList();

	int npcNum = 0;
	int SpriteNum = 0;

	std::list<SceneObject*>::iterator it_obj = objs.begin();//得到选中的npc和玩家数量
	for (; it_obj != objs.end(); it_obj++)
	{
		if (gGetScene()->FindCharacterByID((*it_obj)->GetID()) && (*it_obj)->IsSceneObjectType(eSceneObjCharacter))
		{
			SpriteNum++;
		}
		else
		{
			npcNum++;
		}
	}

	if ((IsOnTeam() && IsTeamLeader() && npcNum > 0) || (!IsOnTeam() && npcNum > 0))
	{
		if (npcNum > 1)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CNpcNameList.getInstanceAndShow");

			for (it_obj = objs.begin(); it_obj != objs.end(); it_obj++)
			{
				//排除玩家
				if (gGetScene()->FindCharacterByID((*it_obj)->GetID()) && (*it_obj)->IsSceneObjectType(eSceneObjCharacter))
				{
					continue;
				}

				if (gGetScene()->FindNpcByID((*it_obj)->GetID()))
				{
					wstring wid = StringCover::NumTowstring<int64_t>((*it_obj)->GetID());
					CEGUI::String id = CEGUI::PFSResourceProvider::WStringToGUIString(wid);
					wstring wname = (*it_obj)->GetName();
					CEGUI::String name = CEGUI::PFSResourceProvider::WStringToGUIString(wname);
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("CNpcNameList.AddNpc", id.c_str(), name.c_str(), "", "");
				}
			}

			gGetGameOperateState()->ResetCursor();//yangbin--创建列表后，把鼠标指针状态变成normal，不然会出现点击列表外空白部分，列表关闭，但是主角不行走的问题
		}
		else
		{
			SceneObject* pMouseOverObjects = NULL;
			for (it_obj = objs.begin(); it_obj != objs.end(); it_obj++)
			{
				if (gGetScene()->FindCharacterByID((*it_obj)->GetID()) && (*it_obj)->IsSceneObjectType(eSceneObjCharacter))
				{
					continue;
				}

				pMouseOverObjects = *it_obj;

				if (pMouseOverObjects)
				{

					if (pMouseOverObjects && !pMouseOverObjects->IsSceneObjectType(eSceneObjClientNpc))
					{
						pMouseOverObjects->OnSetCursor();
					}

					GameCursorTypeProcess(pMouseOverObjects, wParam, lParam);
				}
				break;
			}

		}
	}
	else
	{
		SceneObject* obj = NULL;
		for (it_obj = objs.begin(); it_obj != objs.end(); it_obj++)
		{
			obj = *it_obj;

			if (gGetGameConfigManager()->GetConfigValue(L"contactcharacter") != 0
				&& obj->IsSceneObjectType(eSceneObjCharacter)
				&& gGetScene()->FindCharacterByID(obj->GetID())
				&& gGetScene()->FindCharacterByID(obj->GetID()) != this
				&& !GetBattleManager()->IsInBattle())
			{

				gGetScene()->FindCharacterByID(obj->GetID())->PlayEffect(MHSD_UTILS::GetEffectPath(10300), false, true, false, 1, -1);
				char userid[128];
				sprintf(userid, "%ld", (long int)obj->GetID());

				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("PetAndUserIcon.ShowFriendHeadCpp", userid, ws2s(obj->GetName()).c_str(), "", "");
				break;

			}
		}
		eGameCursorType CurCurType = gGetGameOperateState()->GetGameCursorType();
		if (CurCurType == eGameCursorType_Talk)
		{
			if (!IsOnTeam())
			{
				gGetGameOperateState()->ResetCursor();
			}
			else
			{
				if (IsTeamLeader())
				{
					gGetGameOperateState()->ResetCursor();
				}
			}
		}
		GameCursorTypeProcess(obj, wParam, lParam);
	}

	return true;
}
void MainCharacter::VisitNpcFormList(int64_t id)
{
	SceneObject* pMouseOverObjects = NULL;
	std::list<SceneObject*> objs = gGetScene()->GetMouseOverObjectsList();
	std::list<SceneObject*>::iterator it_obj = objs.begin();
	bool isExist = false;
	for (; it_obj != objs.end(); it_obj++)
	{
		if (id == (*it_obj)->GetID() && gGetScene()->FindNpcByID((*it_obj)->GetID()))
		{
			pMouseOverObjects = *it_obj;
			pMouseOverObjects->OnSetCursor();
			isExist = true;
			break;
		}
	}
	if (isExist)
	{
		GameCursorTypeProcess(pMouseOverObjects, 0, 0);
	}
	else{
		gGetGameOperateState()->ResetCursor();
	}

}
bool MainCharacter::GameCursorTypeProcess(SceneObject* pMouseOverObjects, WPARAM wParam, LPARAM lParam)
{
	SetGotoTargetPosType(0);

	eGameCursorType CurCurType = gGetGameOperateState()->GetGameCursorType();
	switch (CurCurType)
	{
	case eGameCursorType_Forbid:
	{
		gGetGameOperateState()->ResetCursor();
		return true;
	}
	break;
	case eGameCursorType_Pacing:
	case eGameCursorType_Freemove:
	case eGameCursorType_Normal:
	{
		if (GetMoveState() == eMove_Fly)
		{
			return true;
		}

		Nuclear::NuclearLocation mousepoint((int)wParam, (int)lParam);

		Nuclear::NuclearLocation maploc = gGetScene()->MousePointToPixel(mousepoint);

		maploc.x = maploc.x / 3 * 3;
		if (maploc.x <= 0)
			maploc.x = 1;

		m_bLButtonDown = true;
		m_dwLButtonDownTime = Nuclear::GetMilliSeconds();
		if (!gGetScene()->IsChangeMapForbidRun())
		{
			if (GetMovingStat())
			{
				SetMovingStat(false);
				return true;
			}
			PlayMouseBtnDownEffect(maploc);

			RemoveLastSelectedNpc();

			if (ActiveMoveTo(Nuclear::NuclearWorldLogicCoord::world2logic(maploc)))
			{
				if (m_MapWalker.GetAStarState())
				{
					m_MapWalker.ClearGoTo();
				}
				return false;
			}
			else
				return false;
		}
		else
			return false;
	}
	break;
	case eGameCursorType_Talk:
	{
		if (m_MapWalker.GetAStarState())
		{
			m_MapWalker.ClearGoTo();
		}

		return TalkCursorOperate(pMouseOverObjects);
	}
	break;
	case eGameCursorType_Pickup:
	{
		return PickUpCursorOperate(pMouseOverObjects);
	}
	break;
	}
	return false;
}
void MainCharacter::RemoveLastSelectedNpc()
{
	Npc* pLastNpc = gGetScene()->FindNpcByID(m_lastTalkNpcID);
	if (pLastNpc)
		pLastNpc->RemoveVisitEffect();
	m_lastTalkNpcID = 0;
}
bool MainCharacter::PickUpCursorOperate(SceneObject* pMouseOverObjects)
{
	if (pMouseOverObjects && pMouseOverObjects->IsSceneObjectType(eSceneObjNpc))
	{
		return TalkCursorOperate(pMouseOverObjects);
	}
	if (pMouseOverObjects && pMouseOverObjects->IsSceneObjectType(eSceneObjItem))
	{
		Nuclear::NuclearLocation ptLogicItem = pMouseOverObjects->GetLogicLocation();

		if (Nuclear::distance(ptLogicItem, GetLogicLocation()) >= g_logicGrid * 3)
		{
			ActiveMoveTo(ptLogicItem);
			m_gotoPickUpItemID = pMouseOverObjects->GetID();
			return false;
		}
		DroptItem* pDropItem = gGetScene()->FindSceneItemByID(pMouseOverObjects->GetID());
		//fire::pb::move::CPickUpItem pickupitem(pMouseOverObjects->GetID());
		//gGetNetConnection()->send(pickupitem);
	}

	gGetGameOperateState()->ResetCursor();
	return true;
}
bool MainCharacter::TalkCursorOperate(SceneObject* pMouseOverObjects)
{
	if (pMouseOverObjects)
	{
		if (pMouseOverObjects->IsSceneObjectType(eSceneObjNpc)
			&& gGetScene()->FindNpcByID(pMouseOverObjects->GetID()))
		{
			RemoveLastSelectedNpc();

			Npc* pNpc = gGetScene()->FindNpcByID(pMouseOverObjects->GetID());
			if (pNpc)
			{
				pNpc->AddVisitEffect();
				m_lastTalkNpcID = pNpc->GetID();
			}


			if (OnVisitNpc(pNpc))
			{
				StopPacingMove();
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			gGetGameOperateState()->ResetCursor();
			return false;
		}
	}
	else
	{
		gGetGameOperateState()->ResetCursor();
		return false;
	}
}
bool MainCharacter::CanActiveMoveTo()
{
	if (IsOnTeam())
	{
		if (GetTeamState() == eTeamMemberAbsent)
		{
			return true;
		}

		else if (IsTeamLeader())
		{
			for (size_t i = 1; i < 5; i++)
			{
				Character* pCharacter = gGetScene()->FindCharacterByTeamIDandIndex(m_data.teamID, m_data.teamindex + i);
				if (pCharacter && pCharacter->IsSetCharacterState(eCharacterStatePlayCG))
				{
					if (GetTipsManager())
						GetTipsManager()->AddMsgTips(141778);
					return false;
				}
			}
			return true;
		}
		else
			return false;
	}
	//	else
	return true;
}
void MainCharacter::FlyOrWarkToPos(int mapid, int xPos, int yPos, int npcid) {

	if (gGetScene()->GetMapID() != mapid)
	{
		FlyToPos(mapid, xPos, yPos, npcid);
	}
	else
	{
		m_MapWalker.SetTarget(xPos*g_logicGrid, yPos*g_logicGrid, mapid, npcid, 0, false, true);
	}
}
void MainCharacter::WalkToPos(int xPos, int yPos, int64_t nNpcKey, int nNpcId)
{
	bool canIMove = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("TeamManager.CanIMove_cpp");
	if (!canIMove)
	{
		if (GetTipsManager()) GetTipsManager()->AddMsgTips(150030);	//处于组队状态，无法传送
		return;
	}
	int nCheFu = 0;
	m_MapWalker.SetTarget(xPos*g_logicGrid, yPos*g_logicGrid, gGetScene()->GetMapID(), nNpcId, nCheFu, false, true, nNpcKey);
}
void MainCharacter::FlyToPos(int mapid, int xPos, int yPos, int npcid, int64_t npckey, bool randompos, int nTargetPosX, int nTargetPosY, bool forcefly)
{
	bool canIMove = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("TeamManager.CanIMove_cpp");
	if (!canIMove)
	{
		if (GetTipsManager()) GetTipsManager()->AddMsgTips(150030);	//处于组队状态，无法传送
		return;
	}

	if (npcid != 0)
	{
		gGetGameUIManager()->setCurrentItemId(-1);
		gGetGameUIManager()->setTreasureMapId(-1);
	}

	float dis = Nuclear::distance(GetGridLocation(), Nuclear::NuclearLocation(xPos, yPos));
	if (gGetScene()->GetMapID() == mapid && !forcefly)
	{
		if (dis < g_gridDistance)
		{
			Npc* npc = gGetScene()->FindNpcByBaseID(npcid);
			if (npc)
			{
				OnVisitNpc(npc);
			}
			Npc* npcbykey = gGetScene()->FindNpcByID(npckey);
			if (npcbykey)
			{
				OnVisitNpc(npcbykey);
			}
		}
	}
	else if (!gGetScene()->IsMapJumping())
	{
		if (!randompos)
		{
			if (m_bLockGoto == false)
			{
				fire::pb::mission::CReqGoto Goto(mapid, xPos, yPos);
				gGetNetConnection()->send(Goto);

				m_bLockGoto = true;
			}
			else
			{
				return;
			}

		}
		else
		{
			fire::pb::circletask::CReqGotoPatrol Goto; //巡逻
			gGetNetConnection()->send(Goto);
		}


		m_gotoVisitNpcID = npcid;
		m_gotoVisitNpcKey = npckey;
		m_gotoTargetLocation.x = nTargetPosX;
		m_gotoTargetLocation.y = nTargetPosY;
		m_gotoTargetMapId = mapid;
		if (xPos == nTargetPosX && yPos == nTargetPosY)
		{
			m_bJustFlyGoto = false;
		}
		else
		{
			m_bJustFlyGoto = true;
		}

	}
}
bool MainCharacter::ActiveMoveTo(Nuclear::NuclearLocation ptMoveTarget, int npcid, bool trytofindagain, int64_t npckey)
{
	if (CanActiveMoveTo())
	{
		m_gotoPickUpItemID = 0;

		m_gotoVisitNpcID = npcid;
		m_gotoVisitNpcKey = npckey;

		if (MoveTo(ptMoveTarget))
		{
			if (m_bReadyToPacing)
			{
				if (Nuclear::distance(GetLogicLocation(), ptMoveTarget) > 100)
				{
					SetMoveState(eMove_Pacing);
				}
				else
				{
					if (GetTipsManager())
						GetTipsManager()->AddMsgTips(141458);
				}
			}
			else if (m_eMoveState == eMove_Pacing)
			{
				SetMoveState(eMove_Normal);
			}
			return true;
		}
		else
		{
			if (trytofindagain)
			{
				m_gotoVisitNpcID = 0;
				m_gotoVisitNpcKey = 0;
			}
			else
			{
				HandleVisiteNpc();
				HandleReturnTeam(true);
				if (m_MapWalker.GetTargetMapID() == gGetScene()->GetMapID()
					&& m_MapWalker.GetFactionTargetMapID() == 0)
				{
					m_MapWalker.ClearGoTo();
				}
			}
			return false;
		}
	}
	else
	{
		m_MapWalker.ClearGoTo();
		return false;
	}
}
void MainCharacter::DealWhileMainCharacterMove()
{
	gGetScene()->SetShieldCharacter(false);
}
bool MainCharacter::MoveTo(Nuclear::NuclearLocation ptMoveTarget, bool bAgain)
{
	m_pathSaved.clear();
	Nuclear::NuclearLocation currentloc = GetLogicLocation();

	Nuclear::NuclearLocation ptNoBlock = currentloc;
	if (m_eMoveState != eMove_Fly)
	{
		Nuclear::GetEngine()->GetWorld()->SetUseSmooth(true);
		m_bJumpToIslandTips = false;	//重置移动结束给跳跃提示的标志位
		if (gGetScene()->IsMoveBlock(ptMoveTarget, GetMoveMask()))		//如果移动到阻挡点上，用反向直线算法
		{	//这里先注掉
			//ReCalculateTarget(ptMoveTarget);
			if (!gGetScene()->IsMoveBlock(ptMoveTarget, GetMoveMask() == eMoveMaskNormal ? eMoveMaskHigh : eMoveMaskNormal) && m_bNeedToIslandTips)
			{
				m_bJumpToIslandTips = true;
			}
			bool findpath = false;
			Nuclear::NuclearLocation curLoc = GetLogicLocation();
			Nuclear::NuclearLocation movetarget = ptMoveTarget;
			int radius = (abs(curLoc.x - ptMoveTarget.x) + abs(curLoc.y - ptMoveTarget.y)) / g_logicGrid;
			//开始按象限寻路
			for (int i = 1; i <= radius; i++)
			{
				if (findpath)
					break;
				for (int j = 0; j <= i; j++)
				{
					movetarget.x = ptMoveTarget.x + j*((ptMoveTarget.x - curLoc.x) < 0 ? g_logicGrid : -g_logicGrid);
					movetarget.y = ptMoveTarget.y + (i - j)*((ptMoveTarget.y - curLoc.y) < 0 ? g_logicGrid : -g_logicGrid);
					if (!gGetScene()->IsMoveBlock(movetarget, GetMoveMask()) && Nuclear::distance(currentloc, movetarget) >= g_logicGrid)
					{
						findpath = true;
						ptMoveTarget = movetarget;
						break;
					}
				}
			}
			if (!findpath)
			{
				if (!FindReachablePointNearby(currentloc, ptMoveTarget, ptMoveTarget))
				{
					return false;
				}
			}
		}

		if (gGetScene()->IsMoveBlock(currentloc, GetMoveMask()))
		{
			bool findptNoBlock = false;
			Nuclear::NuclearLocation temploc(0, 0);
			//计算该点周围的非阻挡点
			for (int i = 1; i < 9; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					temploc.x = g_randomVisitLoctions[j].x*i + currentloc.x;
					temploc.y = g_randomVisitLoctions[j].y*i + currentloc.y;
					if (!gGetScene()->IsMoveBlock(temploc, GetMoveMask()))
					{
						ptNoBlock = temploc;
						i = 9;
						findptNoBlock = true;
						break;
					}
				}
			}
			if (!findptNoBlock)
			{
				return false;
			}
		}
		if (GetMoveMask() != eMoveMaskNull && !gGetScene()->IsTheSameArea(ptNoBlock, ptMoveTarget, GetMainCharacter()->IsInHighLevel()))
		{
			bool findpath = false;
			Nuclear::NuclearLocation curLoc = ptNoBlock;
			Nuclear::NuclearLocation movetarget = ptMoveTarget;
			int radius = (abs(curLoc.x - ptMoveTarget.x) + abs(curLoc.y - ptMoveTarget.y)) / g_logicGrid;
			//开始按象限寻路
			for (int i = 1; i <= radius; i++)
			{
				if (findpath)
					break;
				for (int j = 0; j <= i; j++)
				{
					movetarget.x = ptMoveTarget.x + j*((ptMoveTarget.x - curLoc.x) < 0 ? g_logicGrid : -g_logicGrid);
					movetarget.y = ptMoveTarget.y + (i - j)*((ptMoveTarget.y - curLoc.y) < 0 ? g_logicGrid : -g_logicGrid);
					if (!gGetScene()->IsMoveBlock(movetarget, GetMoveMask()) && gGetScene()->IsTheSameArea(curLoc, movetarget, GetMainCharacter()->IsInHighLevel()))
					{
						if (!IsOnTeamFollow() && m_bNeedToIslandTips)
						{
							m_bJumpToIslandTips = true;
						}
						findpath = true;
						ptMoveTarget = movetarget;
						break;
					}
				}
			}
			if (!findpath)
				return false;
		}
	}

	if (CanMove() && SceneObject::MoveTo(ptMoveTarget, m_eMoveState == eMove_Fly))
	{
		DealWhileMainCharacterMove();
		if (m_eMoveState == eMove_Pacing && bAgain)
		{
			m_ptLastMoveStart = m_ptLastMoveTarget;
		}
		else if (m_bReadyToPacing)
		{
			m_ptLastMoveStart = ptNoBlock;
		}
		m_ptLastMoveTarget = ptMoveTarget;

		m_iLastMoveBeginning = ptNoBlock;

		GetSprite()->GetPath(m_path);

		//yanji 20150825 画出自动寻路路径用
		GetSprite()->GetPath(m_pathSaved);

		if (m_eMoveState != eMove_Fly)
		{
			if (!m_path.empty())
			{
				if (m_eMoveState != eMove_Follow)
				{
					m_PartTarget = ptNoBlock;
					CalculatePartTarget();								//计算局部的目标点
					fire::pb::move::CRoleMove RoleMove(fire::pb::move::Pos(currentloc.x, currentloc.y), fire::pb::move::Pos(m_PartTarget.x, m_PartTarget.y), gGetScene()->GetMapSceneID());
					gGetNetConnection()->send(RoleMove);
					Nuclear::NuclearLocation block(Round_Off(m_path.begin()->x), Round_Off(m_path.begin()->y));
					m_NotVerifyQueue.push_back(fire::pb::move::Pos((short)block.x, (short)block.y));
					m_path.pop_front();
				}
				EventMainCharacterMove.Bingo();
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			m_PartTarget = ptMoveTarget;
			fire::pb::move::CRoleMove RoleMove(fire::pb::move::Pos(currentloc.x, currentloc.y), fire::pb::move::Pos(m_PartTarget.x, m_PartTarget.y), gGetScene()->GetMapSceneID());
			gGetNetConnection()->send(RoleMove);
			return true;
		}
	}
	else
	{

		return false;
	}

}
void MainCharacter::DrawBackByService(Nuclear::NuclearLocation currentloc)
{
	if (gGetScene()->IsMoveBlock(currentloc, GetMoveMask()))
	{
		Nuclear::NuclearLocation temploc(0, 0);
		for (int i = 1; i < 9; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				temploc.x = g_randomVisitLoctions[j].x*i + currentloc.x;
				temploc.y = g_randomVisitLoctions[j].y*i + currentloc.y;
				if (!gGetScene()->IsMoveBlock(temploc, GetMoveMask()))
				{
					SetLogicLocation(temploc);
				}
			}
		}
		SetLogicLocation(currentloc);
	}
	else
	{
		SetLogicLocation(currentloc);
	}
}
void MainCharacter::CalculatePartTarget()
{
	double pathlength = 0;
	for (astar::Path::iterator it = m_path.begin(); it != m_path.end(); it++)
	{
		Nuclear::NuclearLocation loc((int)it->x, (int)it->y);
		pathlength += sqrt((double)(loc.Dis2(m_PartTarget)));
		m_PartTarget = loc;
		if (pathlength > 500)
		{
			break;
		}
	}
}
void MainCharacter::SendVerifyCommand(int now, int delta)
{
	//判断
	m_iLastVerifyTime -= delta;
	if (m_iLastVerifyTime <= 0)
	{	//500ms就发一次验证
		m_iLastVerifyTime = 500;
		//跳跃时候不用发
		if ((!m_NotVerifyQueue.empty() || IsMoving()))
		{
			Nuclear::NuclearLocation	CurLogicPos = GetLogicLocation();

			m_NotVerifyQueue.clear();
			fire::pb::move::CCheckMove CheckMove(fire::pb::move::Pos(CurLogicPos.x, CurLogicPos.y), m_NotVerifyQueue, gGetScene()->GetMapSceneID());
			gGetNetConnection()->send(CheckMove);
		}
	}
}
bool MainCharacter::CanTurn()
{
	//	//战斗中不能转向
	if (GetBattleManager()->IsInBattle())
		return false;
	return true;
}
bool MainCharacter::CanMove()
{
	//战斗中不能行走
	if (GetBattleManager()->IsInBattle())
	{
		return false;
	}

	if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ReadTimeProgressDlg.CannotMove"))
	{
		return false;
	}
	return true;
}
void MainCharacter::HandleReturnTeam(bool findpathfailure)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("TeamManager.HandleMainCharacterReturnTeam", findpathfailure);
}
bool MainCharacter::JumpMap(int mapId)
{
	Nuclear::NuclearLocation lc = gGetScene()->getJumpPointByMapId(mapId);
	fire::pb::mission::CReqGoto Goto(mapId, lc.x, lc.y);
	gGetNetConnection()->send(Goto);
	SetIsSending(true);
	return true;
}
bool MainCharacter::HandleVisiteNpc()
{

	if (GetBattleManager()->IsInBattle())
	{
		return false;
	}
	if (m_gotoVisitNpcID != 0)
	{
		if (m_gotoVisitNpcKey != 0)
		{
			Npc* pNpc = gGetScene()->FindNpcByID(m_gotoVisitNpcKey);
			if (pNpc && pNpc->GetNpcBaseID() == m_gotoVisitNpcID)
			{
				OnVisitNpc(pNpc);
				m_gotoVisitNpcKey = 0;
				m_gotoVisitNpcID = 0;
				return true;
			}
		}
		else
		{
			Npc* pNpc = gGetScene()->FindNpcByBaseID(m_gotoVisitNpcID);
			if (pNpc)
			{
				OnVisitNpc(pNpc);
				m_gotoVisitNpcID = 0;
				return true;
			}
		}
	}
	if (m_gotoTargetLocation != Nuclear::NuclearLocation(0, 0))
	{
		int luaGoToFlag = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("TaskHelper.HandleVisitPos", m_nTargetPosType);
		m_gotoTargetLocation == Nuclear::NuclearLocation(0, 0);
		//return true;
	}
	return false;

}
void MainCharacter::ContinueMove()
{
	//如果是组队行走状态，就不需要了
	if (m_eMoveState == eMove_Follow)
	{
		return;
	}

	//如果是寻路遇怪，那么出战斗后停止移动-----杨斌
	if (m_eMoveState == eMove_Normal)
	{
		RemoveAutoWalkingEffect();
		return;
	}

	if (m_MapWalker.GetAStarState())
	{
		m_MapWalker.CrossGoTo();
	}
	else if (GetLogicLocation() != m_ptLastMoveTarget && Nuclear::distance(GetLogicLocation(), m_ptLastMoveTarget) >= 2)
	{
		if (!MoveTo(m_ptLastMoveTarget, false))
		{
			HandleVisiteNpc();
			HandleReturnTeam(true);
		}
	}
	else
	{
		HandleVisiteNpc();
		HandleReturnTeam();
		if (m_eMoveState == eMove_Pacing)
		{
			RunPacingEvent();
		}
	}
}
void MainCharacter::SendStopMoveMessage()
{
	if (m_eMoveState != eMove_Follow && !IsOnTeamFollow() && IsMoving())
	{
		Nuclear::NuclearLocation	CurLogicPos = GetLogicLocation();

		fire::pb::move::CRoleStop RoleStop;
		RoleStop.pos.x = CurLogicPos.x;
		RoleStop.pos.y = CurLogicPos.y;
		RoleStop.poslist = m_NotVerifyQueue;
		RoleStop.sceneid = gGetScene()->GetMapSceneID();
		gGetNetConnection()->send(RoleStop);

		m_NotVerifyQueue.clear();
		m_path.clear();
	}
}
void MainCharacter::StopMove()
{
	m_bJumpToIslandTips = false;
	if (!IsMoving())
		return;
	SendStopMoveMessage();
	Character::StopMove();
}
void MainCharacter::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	if (type == Nuclear::XPUSAT_MOVEINFLEXION)
	{
		astar::Path path;
		GetSprite()->GetPath(path);
		if (path.empty() && m_bJumpToIslandTips)
		{
			m_bJumpToIslandTips = false;
		}
	}
	Character::UpdateAction(type);

	if (type == Nuclear::XPUSAT_MOVEINFLEXION)
	{
		if (m_eMoveState != eMove_Fly)
		{
			if (m_path.empty())
			{
				HandleEnterOrLeavePVPArea();
				if (m_eMoveState != eMove_Follow && !IsOnTeamFollow())
				{
					HandleVisiteNpc();
					HandleReturnTeam();
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("TaskManager_CToLua_CheckAreaQuest");
				}

				if (m_eMoveState == eMove_Pacing)
				{
					if (!GetBattleManager()->IsInBattle())
						RunPacingEvent();
				}
				if (!GetBattleManager()->IsInBattle()
					&& m_MapWalker.GetTargetMapID() == gGetScene()->GetMapID()
					&& m_MapWalker.GetFactionTargetMapID() == 0)
				{
					m_MapWalker.ClearGoTo(true);
				}
			}
			else
			{
				if (m_eMoveState != eMove_Follow && !IsOnTeamFollow())
				{
					if (m_PartTarget.x == (int)m_path.begin()->x && m_PartTarget.y == (int)m_path.begin()->y)
					{
						CalculatePartTarget();
						fire::pb::move::CRoleMove RoleMove(fire::pb::move::Pos(GetLogicLocation().x, GetLogicLocation().y), fire::pb::move::Pos(m_PartTarget.x, m_PartTarget.y), gGetScene()->GetMapSceneID());
						gGetNetConnection()->send(RoleMove);
					}
					Nuclear::NuclearLocation block(Round_Off(m_path.begin()->x), Round_Off(m_path.begin()->y));
					m_NotVerifyQueue.push_back(fire::pb::move::Pos((short)block.x, (short)block.y));
				}
				m_path.pop_front();
			}
		}
		else
		{
			if ((size_t)m_AutoMovePathPointIndex < m_AutoMovePathPointArr.size() - 1)
			{
				int OffsetYStep = GetOffsetYStep(m_AutoMovePathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointID);
				GetSprite()->SetFlyOffsetYStep(OffsetYStep);
				GetSprite()->SetFlyOffsetYCur(m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointZ);

				m_AutoMovePathPointIndex += 1;

				int TargetX = m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointX;
				int TargetY = m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointY;

				GetSprite()->SetFlyOffsetYTgt(m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointZ);

				if (m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_SceneID > 0)
				{
					int iCameraZ = Nuclear::GetEngine()->GetWorld()->GetFlyOffsetYCur() - m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_CameraZ;
					if (iCameraZ < 0)
					{
						iCameraZ *= -1;
					}
					Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYStep(iCameraZ / 6000.0f * 5000);

					Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYTgt(m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_CameraZ);

					SetSpriteComponent(eSprite_Horse, StringCover::WStrToNum<int>(m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_RideName));
					GetSprite()->SetMoveSpeed(m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_Speed * 1.0f / 1000 * g_moveSpeed / 1000);
					AddFlyWalkingEffect(m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_EffectName);
					if (m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_SceneID != gGetScene()->GetMapID())
					{
						SendAutoMovePathID(m_AutoMovePathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointID);
						m_AutoMovePathPointIndex += 1;
						int TargetX2 = m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointX;
						int TargetY2 = m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointY;
						OffsetYStep = GetOffsetYStep(m_AutoMovePathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex - 1].m_PathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex - 1].m_PointID);
						GetSprite()->SetFlyOffsetYStep(OffsetYStep);
						GetSprite()->SetFlyOffsetYCur(m_AutoMovePathPointArr[m_AutoMovePathPointIndex - 1].m_PointZ);
						GetSprite()->SetFlyOffsetYTgt(m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointZ);
						GetSprite()->StopMove();
						FlyToPos(m_AutoMovePathPointArr[m_AutoMovePathPointIndex - 1].m_SceneID, TargetX / 16, TargetY / 16, 0, 0, false, TargetX2 / 16, TargetY2 / 16);
					}
					else
					{
						m_PartTarget.x = TargetX;
						m_PartTarget.y = TargetY;
						MoveTo(Nuclear::NuclearLocation(TargetX, TargetY), false);
					}
				}
				else
				{
					SendAutoMovePathID(m_AutoMovePathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex - 1].m_PathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex - 1].m_PointID);
					m_PartTarget.x = TargetX;
					m_PartTarget.y = TargetY;
					MoveTo(Nuclear::NuclearLocation(TargetX, TargetY), false);
				}
			}
			else
			{
				StopAutoMove();
			}
		}
	}
	CheckGoto();

}
void MainCharacter::CheckGoto()
{
	if (gGetGameApplication() && gGetGameApplication()->isShowProgressBar())
	{
		return;
	}

	if (m_bJustFlyGoto == false)
		return;

	int nCurMapId = gGetScene()->GetMapID();
	if (nCurMapId != m_gotoTargetMapId)
	{
		return;
	}
	if (GetBattleManager()->IsInBattle())
	{
		return;
	}
	if (m_gotoVisitNpcKey != 0)
	{
		Npc* pNpc = gGetScene()->FindNpcByID(m_gotoVisitNpcKey);
		if (pNpc && pNpc->GetNpcBaseID() == m_gotoVisitNpcID)
		{
			OnFlyWalkToVisitNpc(pNpc);
			return;
		}
	}
	if (m_gotoVisitNpcID != 0)
	{
		Npc* pNpc = gGetScene()->FindNpcByBaseID(m_gotoVisitNpcID);
		if (pNpc)
		{
			OnFlyWalkToVisitNpc(pNpc);
			return;
		}
	}

	if (m_gotoTargetLocation != Nuclear::NuclearLocation(0, 0) && nCurMapId == m_gotoTargetMapId)
	{
		bool bResult = OnFlyWalkToVisitPos(m_gotoVisitNpcKey, m_gotoVisitNpcID);
		if (bResult)
		{
			m_bJustFlyGoto = false;
			m_gotoTargetMapId = 0;
		}
		return;
	}
	if (m_gotoVisitNpcKey == 0 &&
		m_gotoVisitNpcID == 0 &&
		m_gotoTargetLocation == Nuclear::NuclearLocation(0, 0)
		)
	{
		m_bJustFlyGoto = false;

	}
}
void MainCharacter::Run(int now, int delta)
{
	Character::Run(now, delta);

	if (GetBattleManager()->IsInBattle())
		return;

	if (m_eMoveState != eMove_Follow && !IsOnTeamFollow())
	{
		SendVerifyCommand(now, delta);
	}

	if (m_iAreaCheckCoolDown >= 0)
	{
		m_iAreaCheckCoolDown -= delta;
		if (m_iAreaCheckCoolDown < 0)
		{
			m_iAreaCheckCoolDown = 2000;
			HandleEnterOrLeavePVPArea();
			if (m_eMoveState != eMove_Follow && !IsOnTeamFollow())
			{
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("TaskManager_CToLua_CheckAreaQuest");
			}
			if (GetSceneNpcManager())
			{
				GetSceneNpcManager()->UpdateSceneNpc(GetLogicLocation());
			}
		}
	}
	if (m_bLockGoto)
	{
		m_fFlyToCdDt += delta;
		if (m_fFlyToCdDt >= m_fFlyToCd)
		{
			m_fFlyToCdDt = 0;
			m_bLockGoto = false;
		}
	}

	UpdateCheckMeetTarget(delta);

	if (!GetIsSending())
	{
		if (gGetScene()->getJumpEffectShow())
		{
			UpdateCheckJumpMap(delta);
		}
	}

	if (m_eMoveState == eMove_Fly)
	{
		if (!m_bJustFlyGoto)
		{
			if (!GetSprite()->IsMoving())
			{
				Nuclear::NuclearLocation CurPos = GetLogicLocation();

				if (CurPos == m_LastPos)
				{
					m_FlyStopTime += delta;
					if (m_FlyStopTime > 3000)
					{
						StopAutoMove();
						fire::pb::mission::CReqGoto Goto(gGetScene()->GetMapID(), CurPos.x, CurPos.y);
						gGetNetConnection()->send(Goto);
					}
				}
				else
				{
					m_FlyStopTime = 0;
				}
			}
		}
		m_LastPos = GetLogicLocation();
	}
	else
	{
		m_FlyStopTime = 0;
	}
}
void MainCharacter::UpdateCheckMeetTarget(int dt)
{
	if (m_gotoVisitNpcID == 0)
	{
		return;
	}

	Npc* pNpc = gGetScene()->FindNpcByID(m_gotoVisitNpcKey);
	if (!pNpc)
	{
		pNpc = gGetScene()->FindNpcByBaseID(m_gotoVisitNpcID);
	}
	if (!pNpc)
	{
		return;
	}

	int nParam1 = 0;
	int nParam2 = 0;
	int nParam3 = 0;
	int nParam4 = 0;
	int nR = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("getVisitNpcDis", nParam1, nParam2, nParam3, nParam4);

	Nuclear::NuclearLocation myGrid = GetGridLocation();
	Nuclear::NuclearLocation npcGrid = pNpc->GetGridLocation();
	float fDis = Nuclear::distance(myGrid, npcGrid);
	if (fDis > nR)
	{
		return;
	}

	HandleVisiteNpc();
}
void MainCharacter::UpdateCheckJumpMap(int dt)
{
	int mapId;
	mapId = gGetScene()->getJumpMapID(GetGridLocation());
	if (mapId != -1)
	{
		JumpMap(mapId);
	}
}

void MainCharacter::clearGotoNpc()
{
	m_gotoVisitNpcID = 0;
}
void MainCharacter::RelocateMainRolePos()
{
	fire::pb::move::Pos curpos(GetLogicLocation().x, GetLogicLocation().y);
	fire::pb::move::CRelocateRolePos RelocateRolePos(gGetScene()->GetMapSceneID(), curpos, IsInHighLevel() ? 1 : 0);
	gGetNetConnection()->send(RelocateRolePos);
}
void MainCharacter::ClearReadyToPacing()
{
	m_bReadyToPacing = false;
	if (gGetGameOperateState()->GetGameCursorType() == eGameCursorType_Pacing)
	{
		gGetGameOperateState()->ResetCursor();
	}
}
void MainCharacter::RunPacingEvent()
{
	if (m_ptLastMoveStart != Nuclear::NuclearLocation(-1, -1))
	{
		MoveTo(m_ptLastMoveStart);
	}
}
void MainCharacter::ReadyToPacing()
{
	if (GetTipsManager())
		GetTipsManager()->AddMsgTips(141457);	//请点击地面，开启自动遇敌状态。
	m_bReadyToPacing = true;
	gGetGameOperateState()->SetOperateState(eCursorState_None);
	gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_Pacing);
}
void MainCharacter::ReturnSchool()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ChangeSchoolMap");
}

void MainCharacter::GetSchoolMapIdAndPos(int* pMapId, int* pPosX, int* pPosY)
{
	if (pMapId || pPosX || pPosY)
	{
		int schoolId = GetMainRoleDataNumValue<int>("school");

		if (pMapId)
		{
			CallLuaUtil util;
			util.addArg(schoolId);
			if (util.callLua("GetSchoolMapId"))
			{
				*pMapId = util.getRetInt(-1);
			}
		}

		if (pPosX || pPosY)
		{
			CallLuaUtil util;
			util.addArg(schoolId);
			if (util.callLua("GetSchoolMapGotoPosition"))
			{
				Nuclear::NuclearLocation* pLoc = (Nuclear::NuclearLocation*)util.getRetUsertype("Nuclear::NuclearPoint");
				if (pLoc)
				{
					if (pPosX)
					{
						*pPosX = pLoc->x;
					}

					if (pPosY)
					{
						*pPosY = pLoc->y;
					}
				}
			}
		}
	}
}

void MainCharacter::SVisitNpcProcess(int64_t npcid, std::vector<int> servers, std::vector<int> quests)
{
	if (GetBattleManager() == NULL || GetMainCharacter() == NULL)
	{
		return;
	}
	if (!GetBattleManager()->IsInBattle())
	{
		if (GetMainCharacter()->GetMoveState() != eMove_Pacing && !GetMainCharacter()->IsPathEmpty())
		{
			if (GetMoveState() != eMove_Fly)
			{
				GetMainCharacter()->StopMove();
			}
		}

		Npc* pNpc = gGetScene()->FindNpcByID(npcid);
		if (pNpc)
		{

			//add by yangbin----start npc转身
			if (!pNpc->IsAction(eActionDeath))
			{
				pNpc->SavePrevDirection();
				pNpc->TurnTo(GetMainCharacter());
			}

			GetMainCharacter()->m_MapWalker.ClearGoTo(false, true);
		}
	}
}
bool MainCharacter::IsPathEmpty()
{
	astar::Path path;
	GetSprite()->GetPath(path);
	return path.empty();
}
void MainCharacter::Draw(int now)
{
	Character::Draw(now);
}
void MainCharacter::HideRole(bool hide)
{
	m_bHide = hide;
	if (hide)
	{
		GetSprite()->SetMoveSpeed(450.f / 1000);
		SetMoveMask(eMoveMaskNull);
	}
	else
	{
		GetSprite()->SetMoveSpeed(g_moveSpeed / 1000);
		if (IsInHighLevel())
		{
			SetMoveMask(eMoveMaskHigh);
		}
		else
		{
			SetMoveMask(eMoveMaskNormal);
		}
	}
}
void MainCharacter::AddAutoWalkingEffect()
{
	if (GetMoveState() == eMove_Fly)
	{
		return;
	}
	if (m_pAutoWalkingEffect == NULL)
	{
		m_pAutoWalkingEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10264), 0, 0, 0, true, true, false, true);
	}
}
void MainCharacter::RemoveAutoWalkingEffect()
{
	RemoveDurativeEffect(m_pAutoWalkingEffect);
	m_pAutoWalkingEffect = NULL;
}
void MainCharacter::AddFlyWalkingEffect(std::wstring EffectName)
{
	if (GetMoveState() != eMove_Fly)
	{
		DelFlyWalkingEffect();
		return;
	}
	if (m_pFlyWalkingEffect != NULL)
	{
		DelFlyWalkingEffect();
	}
	m_pFlyWalkingEffect = SetDurativeEffect(EffectName, 0, 0, 0, true, true, false, true);
}
void MainCharacter::DelFlyWalkingEffect()
{
	RemoveDurativeEffect(m_pFlyWalkingEffect);
	m_pFlyWalkingEffect = NULL;
}
bool MainCharacter::TalkCursorOperateWithNpcBaseID(int npcBaseID) {
	Npc* pNpc = gGetScene()->FindNpcByBaseID(npcBaseID);
	if (!pNpc) return false;
	//设置寻路状态
	m_MapWalker.SetAStarState(true);
	if (CanActiveMoveTo())
		AddAutoWalkingEffect();

	return TalkCursorOperate(pNpc);
}
void MainCharacter::StartAutoMove(int AutoMovePathID)
{
	Nuclear::GetEngine()->GetWorld()->SetUseSmooth(false);
	m_AutoMovePathID = AutoMovePathID;
	StopMove();
	SetMoveState(eMove_Fly);
	m_AutoMovePathPointIndex = 0;
	m_AutoMovePathPointArr.clear();
	m_RideNameLast = L"0";
	std::map<char, int> mapComponentInfo = GetComponentInfo();
	std::map<char, int>::iterator it = mapComponentInfo.find(eSprite_Horse);
	if (it != mapComponentInfo.end())
	{
		m_RideNameLast = StringCover::intTowstring(it->second);
	}
	const GameTable::move::CAutoMovePath& AMP = GameTable::move::GetCAutoMovePathTableInstance().getRecorder(AutoMovePathID);
	if (AMP.id < 0)
	{
		return;
	}
	std::vector<std::wstring> delimiters;
	delimiters.push_back(L",");
	std::vector<std::wstring> wsPaths;
	StringCover::split_string(AMP.paths, delimiters, wsPaths);
	for (unsigned int i = 0; i < wsPaths.size(); i++)
	{
		int CurPathID = StringCover::WStrToNum<int>(wsPaths[i]);
		const GameTable::move::CAutoMovePathPoint& AMPP = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(CurPathID);
		for (int i2 = 0; i2 < AMPP.pointcount; i2++)
		{
			sAutoMovePathPointData AMPPD;
			if (i2 == 0)
			{
				AMPPD.m_EffectName = AMPP.effectname;
				AMPPD.m_RideName = AMPP.ridename;
				AMPPD.m_SceneID = AMPP.sceneid;
				AMPPD.m_CameraZ = AMPP.cameraz;
				AMPPD.m_Speed = AMPP.speed;
			}
			AMPPD.m_PathID = CurPathID;
			AMPPD.m_PointID = i2;
			std::wstring wsPoint = AMPP.points[i2];
			std::vector<std::wstring> wsPoints;
			StringCover::split_string(wsPoint, delimiters, wsPoints);
			AMPPD.m_PointX = StringCover::WStrToNum<int>(wsPoints[0]) * 16;
			AMPPD.m_PointY = StringCover::WStrToNum<int>(wsPoints[1]) * 16;
			AMPPD.m_PointZ = StringCover::WStrToNum<int>(wsPoints[2]);
			m_AutoMovePathPointArr.push_back(AMPPD);
		}
	}
	SetSpriteComponent(eSprite_Horse, StringCover::WStrToNum<int>(m_AutoMovePathPointArr[0].m_RideName));
	GetSprite()->SetMoveSpeed(m_AutoMovePathPointArr[0].m_Speed * 1.0f / 1000 * g_moveSpeed / 1000);
	AddFlyWalkingEffect(m_AutoMovePathPointArr[0].m_EffectName);
	int TargetX = m_AutoMovePathPointArr[0].m_PointX;
	int TargetY = m_AutoMovePathPointArr[0].m_PointY;
	int iCameraZ = Nuclear::GetEngine()->GetWorld()->GetFlyOffsetYCur() - m_AutoMovePathPointArr[0].m_CameraZ;
	if (iCameraZ < 0)
	{
		iCameraZ *= -1;
	}
	Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYStep(iCameraZ / 6000.0f * 5000);
	Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYTgt(m_AutoMovePathPointArr[0].m_CameraZ);
	if (m_AutoMovePathPointArr[0].m_SceneID != gGetScene()->GetMapID())
	{
		SendAutoMovePathID(m_AutoMovePathID, m_AutoMovePathPointArr[0].m_PathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointID);
		m_AutoMovePathPointIndex += 1;
		int TargetX2 = m_AutoMovePathPointArr[1].m_PointX;
		int TargetY2 = m_AutoMovePathPointArr[1].m_PointY;
		int OffsetYStep = GetOffsetYStep(m_AutoMovePathID, m_AutoMovePathPointArr[0].m_PathID, 0);
		GetSprite()->SetFlyOffsetYStep(OffsetYStep);
		GetSprite()->SetFlyOffsetYCur(m_AutoMovePathPointArr[0].m_PointZ);
		GetSprite()->SetFlyOffsetYTgt(m_AutoMovePathPointArr[1].m_PointZ);
		FlyToPos(m_AutoMovePathPointArr[0].m_SceneID, TargetX / 16, TargetY / 16, 0, 0, false, TargetX2 / 16, TargetY2 / 16);
	}
	else
	{
		SendAutoMovePathID(m_AutoMovePathID, m_AutoMovePathPointArr[0].m_PathID, m_AutoMovePathPointArr[m_AutoMovePathPointIndex].m_PointID);
		//int OffsetYStep = GetOffsetYStep(m_AutoMovePathID, m_AutoMovePathPointArr[0].m_PathID, 0, 0);
		GetSprite()->SetFlyOffsetYStep(5000);
		//GetSprite()->SetFlyOffsetYCur(m_AutoMovePathPointArr[0].m_PointZ);
		GetSprite()->SetFlyOffsetYTgt(m_AutoMovePathPointArr[0].m_PointZ);
		MoveTo(Nuclear::NuclearLocation(TargetX, TargetY), false);
	}
	m_HideWindowArr.clear();
	CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
	if (pRootWindow != NULL)
	{
		const size_t child_count = pRootWindow->getChildCount();
		for (size_t i = 0; i < child_count; i++)
		{
			CEGUI::Window *pChild = pRootWindow->getChildAtIdx(i);
			if (pChild->GetVisbleLocal())
			{
				pChild->setVisible(false);
				m_HideWindowArr.push_back(pChild->getName());
			}
		}
	}
	SetAutoMove(1);
}
void MainCharacter::StopAutoMove()
{
	//Nuclear::GetEngine()->GetWorld()->SetUseSmooth(true);
	int iCameraZ = Nuclear::GetEngine()->GetWorld()->GetFlyOffsetYCur();
	if (iCameraZ < 0)
	{
		iCameraZ *= -1;
	}
	Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYStep(iCameraZ / 6000.0f * 5000);
	Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYTgt(0);
	//Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYCur(0);
	//Nuclear::GetEngine()->GetWorld()->SetScale(1.0f);
	GetSprite()->SetFlyOffsetYStep(0);
	GetSprite()->SetFlyOffsetYCur(0);
	GetSprite()->SetFlyOffsetYTgt(0);
	StopMove();
	if (GetMoveState() == eMove_Fly)
	{
		SetMoveState(eMove_Normal);
	}
	SetSpriteComponent(eSprite_Horse, StringCover::WStrToNum<int>(m_RideNameLast));
	m_AutoMovePathPointIndex = 0;
	m_AutoMovePathPointArr.clear();
	m_RideNameLast = L"0";
	GetSprite()->SetMoveSpeed(g_moveSpeed / 1000);

	SendAutoMovePathID(-1, -1, -1);

	CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
	if (pRootWindow != NULL)
	{
		const size_t child_count = m_HideWindowArr.size();
		for (size_t i = 0; i < child_count; i++)
		{
			if (pRootWindow->isChild(m_HideWindowArr[i]))
			{
				CEGUI::Window *pChild = pRootWindow->getChild(m_HideWindowArr[i]);
				if (pChild)
				{
					pChild->setVisible(true);
				}
			}
		}
	}
	m_HideWindowArr.clear();
	SetAutoMove(0);
	DelFlyWalkingEffect();
}
void MainCharacter::SendAutoMovePathID(int WayID, int PathID, int PoindID)
{
	fire::pb::move::CSendAutoMovePathID SendAutoMovePathID(WayID, PathID, PoindID);
	gGetNetConnection()->send(SendAutoMovePathID);
}