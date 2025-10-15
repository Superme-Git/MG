#include "stdafx.h"
#include "Pet.h"
#include "GameScene.h"
#include "BattleManager.h"

#include "GameApplication.h"

#define TOTAL_GOSSIP 3
#define GOSSIP_INTERVAL 6000
#define FOLLOW_DISTANCE 6

ScenePet::ScenePet()
: m_pMaster(NULL)
, m_ePetMoveState(ePetMove_Stop)
, m_iTalkInterval(GOSSIP_INTERVAL)
, m_iMoveStopTime(Nuclear::GetMilliSeconds())
, m_pShowEffect(NULL)
{
	m_eSceneType = eSceneObjPet;
	SetNameShadowColor(0xFF000000);
}
ScenePet::~ScenePet()
{
	if (m_pShowEffect)
	{
		RemoveDurativeEffect(m_pShowEffect);
		m_pShowEffect = NULL;
	}
}

bool ScenePet::SetAttribute(const stMapPetData& data)
{

	bool bChangeTitle=false;
	if (m_mapData.showpetname!=data.showpetname)
	{
		bChangeTitle=true;
	}
	
	m_mapData = data;
	
	SetID(data.roleid);
    union uclr{
        struct sclr {
            unsigned char r,g,b,a;
        } s;
        DWORD c;
        
        uclr(DWORD aC)
        {
            c = aC;
            unsigned char r = s.r;
            s.r = s.b;
            s.b = r;
        }
        operator DWORD()
        {
            return c;
        }
    };
    
	SetNameColour(uclr(GetPetColour(data.showpetcolour)));
	SetNameShadowColor(uclr(GetPetNameShadowColor(data.showpetcolour)));
	SetMouseOverNameColor(uclr(GetPetNameMouseOverColor(data.showpetcolour)));
	SetMouseOverShadowColor(uclr(GetPetNameMouseOverShadowColor(data.showpetcolour)));

	if (bChangeTitle)
	{
		UpdatNameTexture(true);
	}
	
	m_pMaster = gGetScene()->FindCharacterByID(data.roleid);

	if( m_pMaster == NULL)
		return false;
	//判断是在第几层
	bool maincharinhighlevel = m_pMaster->IsInHighLevel();
	
	int petData_id = GetTableNumberFromLua("pet.cpetattr", data.showpetid, "id");

	if (NULL == GetSprite() && petData_id != -1)
	{	
		int petData_modelid = GetTableNumberFromLua("pet.cpetattr", data.showpetid, "modelid");
		int petData_area1colour = GetTableNumberFromLua("pet.cpetattr", data.showpetid, "area1colour");
		int petData_area2colour = GetTableNumberFromLua("pet.cpetattr", data.showpetid, "area2colour");

		NewSpriteModel(petData_modelid, maincharinhighlevel);
		GetSprite()->SetMoveSpeed(g_freeMoveSpeed/1000);
		SetSpriteComponent(eSprite_DyePartA, petData_area1colour);
		SetSpriteComponent(eSprite_DyePartB, petData_area2colour);
	}
	else if(GetSprite())
	{
        ChangeSpriteModel(m_mapData.GetShapeID());		
		SetInHighLevel(maincharinhighlevel);
	}
    else
    {
        std::wostringstream strstream;
		strstream << "Error Info:Can't Find Pet ID in\" bool Pet::SetAttribute(const stMapPetData& data)\" ID = " << data.showpetid <<  std::endl;
		XPLOG_ERROR(strstream.str().c_str());
        return false;
    }

	Nuclear::NuclearLocation randomlocation =	gGetScene()->GetNearByPos(m_pMaster,FOLLOW_DISTANCE-2);
	
	eMoveMask movemask = maincharinhighlevel?eMoveMaskHigh:eMoveMaskNormal;
	if (gGetScene()->IsMoveBlock(randomlocation,movemask)
	|| !gGetScene()->IsTheSameArea(randomlocation,m_pMaster->GetLogicLocation(),m_pMaster->IsInHighLevel()))
	{
		SetLogicLocation(m_pMaster->GetLogicLocation());
	}
	else
	{
		SetLogicLocation(randomlocation);
	}

	int dir = (rand()%4)*2+1;
	SetDirection(Nuclear::NuclearDirection(dir));

	if(m_pShowEffect)
	{
		RemoveDurativeEffect(m_pShowEffect);
		m_pShowEffect = NULL;
	}    
    return true;
}
void ScenePet::SetArea1Color(int colorID)
{
	if (GetSprite())
	{
		DWORD color = GetNpcColorByColorID(GetSprite()->GetModelName(), colorID);
		SetComponent(g_npcAreaColorID, g_npcAreaColorComponentName, color);
	}

}
void ScenePet::SetArea2Color(int color)
{
	SetComponent(2, L"Area2", color ? color : 0xFFFFFFFF);

}

bool ScenePet::CanBeAttack()
{
	return false;
}

void ScenePet::Run(int now,int delta)
{
	if (m_iTalkInterval >= 0)
		m_iTalkInterval -= delta;
	if (m_pMaster == NULL)
		return;
	if (distance(GetLogicLocation(),m_pMaster->GetLogicLocation()) >= 400
	 || !gGetScene()->IsTheSameArea(GetLogicLocation(),m_pMaster->GetLogicLocation(),m_pMaster->IsInHighLevel())
	 || IsInHighLevel() != m_pMaster->IsInHighLevel())
	{
		bool maincharinhighlevel = m_pMaster->IsInHighLevel();
		SetInHighLevel(maincharinhighlevel);
		Nuclear::NuclearLocation randomlocation =	gGetScene()->GetNearByPos(m_pMaster,FOLLOW_DISTANCE-2);
			eMoveMask movemask = maincharinhighlevel?eMoveMaskHigh:eMoveMaskNormal;
		if (gGetScene()->IsMoveBlock(randomlocation,movemask)
		|| !gGetScene()->IsTheSameArea(randomlocation,m_pMaster->GetLogicLocation(),m_pMaster->IsInHighLevel()))
		{
			SetLogicLocation(m_pMaster->GetLogicLocation());
		}
		else
		{
			SetLogicLocation(randomlocation);
		}
		m_iMoveStopTime = Nuclear::GetMilliSeconds();
		m_ePetMoveState = ePetMove_Stop;
	}
	if ((m_ePetMoveState != ePetMove_Random || m_pMaster->IsMoving()) )
		RunFollow();
	if (m_ePetMoveState == ePetMove_Stop && Nuclear::GetMilliSeconds() > m_iMoveStopTime + 1500)
		RunRandom();

	SceneObject::Run(now,delta);
}
void ScenePet::RunFollow(bool forcemove)
{
	if (distance(GetLogicLocation(), m_pMaster->GetLogicLocation())	> g_logicGrid*FOLLOW_DISTANCE && (!IsMoving() || forcemove))
	{
		m_ePetMoveState = ePetMove_Follow;
		GetSprite()->SetMoveSpeed(g_moveSpeed/1000);
		MoveTo(m_pMaster->GetLogicLocation());
	}
	else if (distance(GetLogicLocation(), m_pMaster->GetLogicLocation()) < g_logicGrid*FOLLOW_DISTANCE/2 && IsMoving())
	{
		m_ePetMoveState = ePetMove_Stop;
		StopMove();
	}
}
void ScenePet::RunRandom()
{
	m_ePetMoveState = rand()%6 == 0?ePetMove_Random:ePetMove_Stop;
	if (m_ePetMoveState == ePetMove_Stop)
	{
		if (rand()%10 == 0 && m_iTalkInterval < 0 && !GetBattleManager()->IsInBattle())
			RandomTalk();
		m_iMoveStopTime = Nuclear::GetMilliSeconds();
	}
	else if (m_ePetMoveState == ePetMove_Random)
	{
		Nuclear::NuclearLocation targetpos = gGetScene()->GetNearByPos(m_pMaster,5);
		eMoveMask movemask = IsInHighLevel()?eMoveMaskHigh:eMoveMaskNormal;
		if (gGetScene()->IsMoveBlock(targetpos,movemask)
		|| !gGetScene()->IsTheSameArea(targetpos,GetLogicLocation(),IsInHighLevel()))
		{
			m_ePetMoveState = ePetMove_Stop;
			m_iMoveStopTime = Nuclear::GetMilliSeconds();
		}
		else
		{
			GetSprite()->SetMoveSpeed(g_freeMoveSpeed/1000);
			MoveTo(targetpos);
			astar::Path path;
			GetSprite()->GetPath(path);
			if (path.empty())
			{
				m_ePetMoveState = ePetMove_Stop;
				m_iMoveStopTime = Nuclear::GetMilliSeconds();
			}
		}
	}
}
void ScenePet::RandomTalk()
{

}
void ScenePet::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	SceneObject::UpdateAction(type);

	if (type == Nuclear::XPUSAT_MOVEINFLEXION)
	{
		astar::Path path;
		GetPath(path);
		if (path.empty() && m_pMaster && m_pMaster->IsMoving())
		{
			RunFollow(true);
		}
	}
}
void ScenePet::StopMove()
{
	if (!IsMoving())
		return;
	if (m_pMaster && m_pMaster->IsMoving())
		return;
	SceneObject::StopMove();
	m_ePetMoveState = ePetMove_Stop;
}
