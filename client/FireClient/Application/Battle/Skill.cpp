//  Skill.cpp
//  FireClient
#include "stdafx.h"
#include "Skill.h"

Skill::Skill(sBattleDemoScript& aScript, int SubSkillIndex)
:m_Script(aScript),
m_SubSkillIndex(SubSkillIndex),
m_iLimitTime(0),
m_BeginMode(0),
m_DelayTime(0)
{
    m_bDanti = false;
}
Skill::~Skill()
{
	std::list<SkillStage*>::iterator it = m_lStages.begin();
	for (it; it != m_lStages.end(); it++)
	{
		delete (*it);
	}
	m_lStages.clear();
}

void Skill::Begin()
{
    
}
void Skill::Run(int now, int delta)
{
	if (m_BeginMode == 2)
	{
		return;
	}
	if (m_DelayTime > 0)
	{
		m_DelayTime -= delta;
		if (m_DelayTime < 0)
		{
			m_DelayTime = 0;
		}
		else if (m_DelayTime > 0)
		{
			return;
		}
	}
    if (!m_lStages.empty())
    {
        SkillStage* pStage = m_lStages.front();
		if (pStage->m_bIsStageComplete)
		{
			m_lStages.pop_front();
			delete pStage;
		}
		if (!m_lStages.empty())
		{
			pStage = m_lStages.front();
			if (!pStage->m_bIsStageComplete)
			{
				pStage->Run(now, delta);
			}
		}
    }
}
void Skill::SetLimitTime(int aMs)
{
    m_iLimitTime = aMs;    
}

