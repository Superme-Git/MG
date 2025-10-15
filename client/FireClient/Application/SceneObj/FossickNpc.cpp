#include "stdafx.h"
#include "FossickNpc.h"
#include "MainRoleDataManager.h"
#include "GameUIManager.h"
#include "../ProtoDef/rpcgen/fire/pb/mission/ReadTimeType.hpp"


FossickNpc::FossickNpc()
:m_iEndCoolingTime(0)
,m_bIsCooling(false)
,m_pEffect(NULL)
{
	
}
FossickNpc::~FossickNpc()
{
	m_pEffect = NULL;
}

bool FossickNpc::InCoolTips()
{
	return true;
}
void FossickNpc::Run(int now, int delta)
{
	
}
void FossickNpc::AddEffect()
{
	if (CanFossick() && NULL == m_pEffect)
		m_pEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10219), 0, 0, 0, false);
}
bool FossickNpc::SetAttribute(stNpcData* pData)
{
	if(!pData)
	{
		return false;
	}
	return true;
}
bool FossickNpc::CanFossick()
{
	if(m_bIsCooling)
		return false;
	return false;
}
void FossickNpc::RemoveEffect(int itemid)
{
}
void FossickNpc::EndCoolingState(int itemid)
{
	
}

void FossickNpc::BeginFossick(const int& needbattle)
{
}
void FossickNpc::FinishOneFossick(const int& result)
{
	
}
void FossickNpc::BeginCooling()
{
}