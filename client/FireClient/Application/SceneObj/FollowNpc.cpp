#include "stdafx.h"
#include "FollowNpc.h"

#include "GameApplication.h"
#include "GameScene.h"


FollowNpc::FollowNpc()
{
}
FollowNpc::~FollowNpc()
{
}

void FollowNpc::refreshDistance()
{
	m_nMinFollow = MinFollowDistance;
	m_nMaxFollow = MaxFollowDistance;

	CallLuaUtil util;
	util.addArg(MinFollowDistance);
	util.addArg(MaxFollowDistance);
	if (util.callLua("TeamManager_RefreshDistance_cpp", 2))
	{
		m_nMaxFollow = util.getRetInt(MaxFollowDistance);
		m_nMinFollow = util.getRetInt(MinFollowDistance);
	}
}

bool FollowNpc::Init(int nTableId, int nNum, const int& basedata)
{
	MainCharacter* pMainChar = GetMainCharacter();
	if (!pMainChar)
	{
		return false;
	}

	Nuclear::NuclearLocation loc = GetMainCharacter()->GetLogicLocation();
	stNpcData npcData;
	npcData.NpcBaseID = GetTableNumberFromLua("npc.cnpcconfig", basedata, "id");
	npcData.ShapeID = GetTableNumberFromLua("npc.cnpcconfig", basedata, "modelID");
	npcData.ptPos = loc;
	//npcData.moveSpeed = nSpeed;
	m_nNumber = nNum;
	SetAttribute(&npcData);

	refreshDistance();

	return true;
}

bool FollowNpc::SetAttribute(stNpcData* pData)
{
	Npc::SetAttribute(pData);
	return true;
}
void FollowNpc::StopMove()
{
	if (!IsMoving())
		return;
	Npc::StopMove();
}
void FollowNpc::RunFollow()
{
	Nuclear::NuclearLocation ptFollow = GetMainCharacter()->GetLogicLocation();
	
	int LogicDistance = Nuclear::distance(GetLogicLocation(), ptFollow);

	if (LogicDistance >= m_nMaxFollow )
	{
		 MainCharacter* pMainChar = GetMainCharacter();
		 if (!pMainChar)
		 {
			 return ;
		 }
		 if (pMainChar->GetRideModeID() > 0)
		 {
			 int nSpeed = pMainChar->GetRideSpeedByModelID(pMainChar->GetRideModeID());
			 GetSprite()->SetMoveSpeed(g_moveSpeed /1000.0 * nSpeed /1000.0);
		 }

		 cocos2d::CCScriptEngineProtocol *engine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		 if (engine->executeGlobalFunction("TeamManager.IsOnTeam_cpp"))
		 {
			 int leaderid = engine->executeGlobalFunction("TeamManager.GetLeaderId");
			 if (leaderid != 0)
			 {
				 Character* pCharacter = gGetScene()->FindCharacterByID(leaderid);
				 if (pCharacter)
				 {
					 int nSpeed = pCharacter->GetRideSpeedByModelID(pCharacter->GetRideModeID());
					 GetSprite()->SetMoveSpeed(g_moveSpeed / 1000.0 * nSpeed / 1000.0);
				 }
			 }
			 
		 }
		MoveTo(ptFollow);
	}
	else if (LogicDistance <= m_nMinFollow)
	{
		StopMove();
	}	
}
void FollowNpc::Run(int now, int delta)
{
	Npc::Run(now,delta);
	RunFollow();
}
void FollowNpc::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	Npc::UpdateAction(type);
}
bool FollowNpc::PlayAction(eActionType actionType, float freq)
{
	Npc::PlayAction(actionType, freq);
	return true;
}
