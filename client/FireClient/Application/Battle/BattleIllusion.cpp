#include "stdafx.h"
#include "BattleIllusion.h"
#include "Battler.h"
#include "GameTable/npc/CMonsterConfig.h"

#include "GameApplication.h"


BattleIllusion::BattleIllusion()
: m_bDisappear(false)
, m_bFadeOut(false)
, m_bFadeIn(false)
, m_bOnHitTarget(false)
, m_iMoveOffsetX(0)
, m_iMoveOffsetY(0)
, m_ptDisappearLocation(0,0)
, m_ptTargetLocation(0,0)
, m_fDemoSpeedRate(1.0f)
, m_iZHDJumpDelay(0)
, m_iNormalMagicIndex(0)
, m_OwnerBattler(NULL)
{
	m_eSceneType = eSceneObjBattleIllusion;	
}
BattleIllusion::~BattleIllusion()
{
}

Battler* BattleIllusion::GetOwnerBattler()
{
	return m_OwnerBattler;
}

bool BattleIllusion::CanBeAttack()
{
	return false;
}
bool BattleIllusion::CanBeGiven()
{
	return false;
}
bool BattleIllusion::CanJoinTeam()
{
	return false;
}
bool BattleIllusion::CanTradeWith()
{
	return false;
}

void BattleIllusion::RegistListener(IActionListener* aPListener)
{
	for (unsigned int i = 0; i < m_Listeners.size(); i++)
	{
		if (m_Listeners[i] == aPListener)
		{
			return;
		}
	}
	m_Listeners.push_back(aPListener);
}
void BattleIllusion::UnregistListener(IActionListener* aPListener)
{
	for (unsigned int i = 0; i < m_Listeners.size(); i++)
	{
		if (m_Listeners[i] == aPListener)
		{
			m_Listeners[i] = m_Listeners[m_Listeners.size() - 1];
			m_Listeners.pop_back();
			return;
		}
	}
}

void BattleIllusion::SetTargetLocation(Nuclear::NuclearLocation loc)
{
	m_ptTargetLocation = loc;
	SetDirection(m_ptTargetLocation.x,m_ptTargetLocation.y);
}
bool BattleIllusion::SetCharacter(int64_t characterID,const int&shapeID,const std::map<char,int>& components)
{
	if (NULL == GetSprite())
	{
		NewSpriteModel(shapeID);
	}
	return true;
}
bool BattleIllusion::SetCreeps(int64_t creepID,int shapeID)
{
	const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(static_cast<int>(creepID));

	if ( NULL != GetSprite())
		DeleteSpriteModel();

	m_data.iShapeID = shapeID!=0?shapeID:MonsterData.modelID;
	NewSpriteModel(m_data.iShapeID);
	if (GetSprite() != NULL)
	{
		GetSprite()->SetDyePartIndex(0, MonsterData.area1colour);
		GetSprite()->SetDyePartIndex(1, MonsterData.area2colour);
	}
	return true;
}
bool BattleIllusion::SetPet(int64_t petID, int shapeID)
{
	int petData_id = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "id");
	if (petData_id != -1)
	{
		int petData_modelid = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "modelid");
		int petData_area1colour = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "area1colour");
		int petData_area2colour = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "area2colour");

		if (NULL != GetSprite())
			DeleteSpriteModel();
		m_data.iShapeID = shapeID != 0 ? shapeID : petData_modelid;
		NewSpriteModel(m_data.iShapeID);
		SetSpriteComponent(eSprite_DyePartA, petData_area1colour, true);
		SetSpriteComponent(eSprite_DyePartB, petData_area2colour, true);
	}
	return true;
}
bool BattleIllusion::SetAttribute(sBattlerData data,float scale)
{
	m_data = data;
	switch(data.BattlerType)
	{
	case eBattlerCharacter:
		{
			if (!SetCharacter(data.BattlerDataID,data.iShapeID,data.componentsMap))
				return false;
		}
		break;
	case eBattlerPet:
		{	
			if (!SetPet(data.BattlerDataID,data.iShapeID))
				return false;
		}
		break;
	case eBattlerNaKaMa:
	case eBattlerMonster:
	case eBattlerNPC:
	case eBattlerNaKaMaSys:
		{
			if (!SetCreeps(data.BattlerDataID,data.iShapeID))
				return false;
		}
		break;
	default:
		return false;
		break;
	}
	if (GetSprite())
	{
		{
			SetDefaultAction(eActionBattleStand);
		}
		if (abs(scale - 1.0f) > 0.0001f)
		{
			SetScale(scale);
		}

		GetSprite()->SetMoveSpeed(c_fBattlerMoveSpeed / 1000);

        GetSprite()->SetMouseTestMode(Nuclear::XPTM_POLYGON);
		GetSprite()->SetMoveMask(eMoveMaskNull);

		UpdateSpriteComponent(m_data.componentsMap);
	}

	GetSprite()->SetCurveMove(true);
	return true;
}
void BattleIllusion::SetDefaultAction(eActionType actionType,bool holdlastframe,float freq,eActionType actualActionType)
{
	freq = freq/m_fDemoSpeedRate;
	if (actualActionType != eActionNull)
	{
		SceneObject::SetDefaultAction(actualActionType,holdlastframe,freq);
		m_actionType = actionType;
	}
	else
	{
		SceneObject::SetDefaultAction(actionType,holdlastframe,freq);
	}
}
void BattleIllusion::SetFadeOut()
{
	m_bFadeOut = true;
}
void BattleIllusion::SetFadeIn(unsigned char desalpha)
{
	m_FadeInAlpha = desalpha;
	m_bFadeIn = true;
}
void BattleIllusion::SetOwnerBattler(Battler* OwnerBattler)
{
	m_OwnerBattler = OwnerBattler;
}

void BattleIllusion::PlayAction(eActionType actionType,float freq,eActionType actualActionType)
{
	freq = freq/m_fDemoSpeedRate;
	if (actualActionType != eActionNull)
	{
		ComponentsObject::PlayAction(actualActionType,freq);
		m_actionType = actionType;
	}
	else
	{
		ComponentsObject::PlayAction(actionType,freq);
	}
}
void BattleIllusion::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	for (unsigned int i = 0; i < m_Listeners.size(); i++)
	{
		m_Listeners[i]->OnUpdateAction(type, m_actionType);
	}
}

bool BattleIllusion::OnSetCursor()
{
	return SceneObject::OnSetCursor();
}
void BattleIllusion::Draw(int now)
{
	SceneObject::Draw(now);
}

void BattleIllusion::Run(int now,int delta)
{
	delta = static_cast<int>(m_fDemoSpeedRate*delta);
	SceneObject::Run(now,delta);
	if (m_bFadeOut)
	{
		unsigned char alpha = GetAlpha();
		if (alpha > 0)
		{
			if (alpha > delta/4)
			{
				SetAlpha(alpha - delta/4);
			}
			else
			{
				SetAlpha(0);
				m_bDisappear = true;
			}
		}
		else
		{
			SetAlpha(0);
			m_bDisappear = true;
		}
	}
	else if (m_bFadeIn)
	{
		unsigned char alpha = GetAlpha();
		if (alpha < m_FadeInAlpha)
		{
			if (alpha < m_FadeInAlpha - delta/4)
			{
				SetAlpha(alpha + delta/4);
			}
			else
			{
				SetAlpha(m_FadeInAlpha);
				m_bFadeIn = false;
			}
		}
	}

	if (m_iMoveOffsetX != 0 || m_iMoveOffsetY != 0)
	{
		Nuclear::NuclearLocation loc = GetSprite()->GetLocation();
		loc.x += m_iMoveOffsetX;
		loc.y += m_iMoveOffsetY;
		SetLocation(loc);
	}

}
void BattleIllusion::RunTo(Nuclear::NuclearLocation ptMoveTarget)
{
	if (m_actionType != eActionBattleRun)
	{
		PlayAction(eActionBattleRun);
	}
	GetSprite()->MoveTo(ptMoveTarget, 0, NULL);
}
void BattleIllusion::RunBack()
{
	if (GetOwnerBattler())
	{
		Nuclear::NuclearLocation ptCur = GetLocation();
		if (GetOwnerBattler()->GetBattleLocation().Dis2(ptCur) < 10)
		{
			SetLocation(GetOwnerBattler()->GetBattleLocation());
			return;
		}

		RunTo(GetOwnerBattler()->GetBattleLocation());
		if (GetOwnerBattler()->IsFriendSide())
		{
			SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
		}
		else if (GetOwnerBattler()->IsEnemySide())
		{
			SetDirection(Nuclear::XPDIR_TOPLEFT);
		}
	}
}
void BattleIllusion::TurnTo(SceneObject* pSceneObj)
{
	if (GetOwnerBattler())
	{
		if (pSceneObj)
		{
			if (GetOwnerBattler()->IsFriendSide())
			{
				SetDirection(Nuclear::XPDIR_TOPLEFT);
			}
			else if (GetOwnerBattler()->IsEnemySide())
			{
				SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
			}
		}
	}
}
