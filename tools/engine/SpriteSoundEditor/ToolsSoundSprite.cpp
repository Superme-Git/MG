#include "StdAfx.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\sound\directsoundmanager.h"
#include "ToolsSoundSprite.h"

ToolsSoundSprite::ToolsSoundSprite(Nuclear::EngineBase *pEB, const std::wstring &modelname) :
Nuclear::ComponentSprite(pEB, modelname), m_bIsPause(true)
{
}

ToolsSoundSprite::~ToolsSoundSprite(void)
{
}

bool ToolsSoundSprite::PlayActionSilence(const std::wstring& action_name)
{
	if( action_name == L"" )
	{
		if( m_strTmpActionName != L"" )
		{
			m_pTmpAction = NULL;
			m_strTmpActionName = L"";
			m_strTmpActionOutsideName = L"";
			m_iAniStartTime = 0;
		}
		return false;
	}
	std::wstring actionref_name = m_pEngine->GetSpriteManager()->GetActionRef(m_ModelName, action_name);
	if( actionref_name == L"" ) return false;
	const Nuclear::Action *act = m_pEngine->GetSpriteManager()->GetAction(m_ModelName, actionref_name);
	if( act == NULL ) return false;

	m_actionTypeFlag = m_pEngine->GetSpriteManager()->actionType.GetActionType(action_name);
	SetPropFlag(XPSPPF_START_HOLD_LAST_FRAME, false);

	m_strTmpActionOutsideName = action_name;
	m_strTmpActionName = actionref_name;
	m_iAniStartTime = m_pEngine->GetTick();
	m_iAniDurationTime = act->GetTime();
	m_iCurFrame = 0;
	m_nOldFrame = -1;
	m_pTmpAction = act;
	return true;
}

void ToolsSoundSprite::PlayAction(const std::wstring& action_name, const Nuclear::SpriteSoundParam& param)
{
	if (PlayActionSilence(action_name))
	{
		m_pEngine->GetSoundManager()->XPPlaySound(param.soundname, 0, 0, param.vol, param.freq, false);
	}
}

void ToolsSoundSprite::Update()
{
	if (!m_bIsPause)
	{
		UpdateAction();
	}
}

void ToolsSoundSprite::Pause()
{
	m_bIsPause = true;
	m_iCurFrame = 0;
	m_nOldFrame = -1;
}

