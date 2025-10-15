#include "stdafx.h"
#include <assert.h>
#include "Sprite.h"

Sprite::Sprite(Nuclear::ISprite *pSprite) : m_pSprite(pSprite), m_actState(0), m_isTel(false)
{
	m_pSprite->SetDefaultAction(L"stand1",  Nuclear::XPSALT_SYNC,1.0f,false);
	m_pSprite->SetMoveSpeed(300.f/1000);
	m_pSprite->SetMoveMask(0x03);
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
		m_pSprite->PlayAction(L"runleft", Nuclear::XPSALT_SYNC,1.0f);
		break;
	case 3:
		if (!m_pSprite->IsMoving())
		{
			m_pSprite->PlayAction(L"hrun",Nuclear::XPSALT_SYNC,1.0f);
		}
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
	case Nuclear::XPUSAT_DEFAULTACTIONFINISH:

		if (m_pSprite->IsMoving() || m_isTel)
		{
			switch (m_actState)
			{
			case 0:
			case 2:
				m_actState = 1;
				m_pSprite->PlayAction(L"runleft",Nuclear::XPSALT_SYNC,1.0f);
				break;
			case 1:
				m_actState = 2;
				m_pSprite->PlayAction(L"runright",Nuclear::XPSALT_SYNC,1.0f);
				break;
			case 3:
				m_pSprite->PlayAction(L"hrun",Nuclear::XPSALT_SYNC,1.0f);
				break;
			default:
				assert(false);
			}
		} else {
			if (m_actState == 3)
			{
				m_pSprite->PlayAction(L"hstand",Nuclear::XPSALT_SYNC,1.0f);
			} else {
				m_actState = 0;
				m_pSprite->PlayAction(L"stand1",Nuclear::XPSALT_SYNC,1.0f);
			}
		}
		break;
	case Nuclear::XPUSAT_TELEPORTEND:
		m_isTel = false;
		break;
	}
	
}


void Sprite::SetRideName(const std::wstring& name)
{ 
	if (name == L"")
	{
		if (m_actState == 3)
		{
			m_actState = 0;
			m_pSprite->SetDefaultAction(L"stand1",Nuclear::XPSALT_SYNC,1.0f, false);
			m_pSprite->SetMoveSpeed(225.f/1000);
			UpdateAction(Nuclear::XPUSAT_PLAYACTIONFINISH);
		}
	} else {
		if (m_actState != 3)
		{
			m_actState = 3;
			m_pSprite->SetDefaultAction(L"hstand",Nuclear::XPSALT_SYNC,1.0f, false);
			m_pSprite->SetMoveSpeed(337.5f/1000);
			UpdateAction(Nuclear::XPUSAT_PLAYACTIONFINISH);
		}
	}
	m_pSprite->SetRideName(name); 
}