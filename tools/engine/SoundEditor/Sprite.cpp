#include "stdafx.h"
#include <assert.h>
#include "Sprite.h"

Sprite::Sprite(Nuclear::ISprite *pSprite) : m_pSprite(pSprite), m_actState(0)
{
	m_pSprite->SetDefaultAction(L"stand1", Nuclear::XPSALT_SYNC, 1.0f, false);
	m_pSprite->SetMoveSpeed(225.f/1000);
}

Sprite::~Sprite(void)
{
}

void Sprite::GoToRun()
{
	switch (m_actState)
	{
	case 0:
		m_actState = 1;
		PlayAction(m_runleft);
		break;
	default:
		break;
	}
}

void Sprite::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	switch (type)
	{
	case Nuclear::XPUSAT_PLAYACTIONFINISH:
		if (m_pSprite->IsMoving())
		{
			switch (m_actState)
			{
			case 0:
			case 2:
				m_actState = 1;
				PlayAction(m_runleft);
				break;
			case 1:
				m_actState = 2;
				PlayAction(m_runright);
				break;
			default:
				assert(false);
			}
		} else {
			m_actState = 0;
			PlayAction(m_stand);
		}
		break;
	case Nuclear::XPUSAT_DEFAULTACTIONFINISH:
		m_actState = 0;
		PlayAction(m_stand);
		break;
	}
	
}


void Sprite::SetRideName(const std::wstring& name)
{ 
	if (name == L"")
	{
		m_pSprite->SetMoveSpeed(225.f/1000);
	} else {
		m_pSprite->SetMoveSpeed(337.5f/1000);
	}
	m_pSprite->SetRideName(name); 
}