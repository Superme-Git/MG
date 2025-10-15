#include <windows.h>
#include <assert.h>
#include <vector>
#include <set>
#include "Sprite.h"


Sprite::Sprite(Nuclear::ISprite *pSprite) : m_pSprite(pSprite), m_actState(0), m_isTel(false)
{
	m_pSprite->SetDefaultAction(L"stand1", Nuclear::XPSALT_SYNC, 1.0f, false);
	m_pSprite->SetMoveSpeed(300.f/1000);
}

Sprite::~Sprite(void)
{
}

void Sprite::GoToRun()
{
	switch (m_actState)
	{
	case 0:
	case 4:
		m_actState = 1;
		PlayAction(L"runleft");
		break;
	case 3:
		if (!m_pSprite->IsMoving())
		{
			PlayAction(L"hrun");
		}
	case 6:
		m_actState = 7;
	case 7:
		PlayAction(L"run2");
		break;
	default:
		break;
	}
}

void Sprite::Attack()
{
	if (m_actState >= 6)
	{
		PlayAction(L"attack1");
		return;
	}
	switch (rand() % 3)
	{
	case 0:
		PlayAction(L"attack1");
		break;
	case 1:
		PlayAction(L"attack2");
		break;
	case 2:
		PlayAction(L"attack3");
		break;
	}
	m_actState = 4;
}

void Sprite::StopAttack()
{
	if (m_actState == 4)
	{
		m_actState = 0;
		m_pSprite->PlayAction(L"stand1", 1.0, true);
	}
}

void Sprite::Die()
{
	m_pSprite->PlayAction(L"death", 1.0, true);
	m_actState = 5;
}

bool Sprite::UpdateAction(Nuclear::XPUSA_TYPE type)
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
				PlayAction(L"runleft");
				break;
			case 1:
				m_actState = 2;
				PlayAction(L"runright");
				break;
			case 3:
				PlayAction(L"hrun");
				break;
			case 6:
				m_actState = 7;
			case 7:
				PlayAction(L"run2");
				break;
			default:
				assert(false);
			}
		} else {
			switch (m_actState)
			{
			case 0:
			case 1:
			case 2:
				m_actState = 0;
				m_pSprite->PlayAction(L"stand1", 1.0, true);
				break;
			case 3:
				m_pSprite->PlayAction(L"hstand", 1.0, true);
				break;
			case 4:
				Attack();
				break;
			case 5:
				//死亡动作播完了，干掉吧
				return false;
				break;
			case 7:
				m_actState = 6;
			case 6:
				PlayAction(L"stand1");
				break;
			
			}
		}
		break;
	case Nuclear::XPUSAT_TELEPORTEND:
		m_isTel = false;
		break;
	}
	return true;
}

void Sprite::SetRideName(const std::wstring& name)
{ 
	if (name == L"")
	{
		if (m_actState == 3)
		{
			m_actState = 0;
			m_pSprite->SetDefaultAction(L"stand1", false);
			m_pSprite->SetMoveSpeed(225.f/1000);
			UpdateAction(Nuclear::XPUSAT_PLAYACTIONFINISH);
		}
	} else {
		if (m_actState != 3)
		{
			m_actState = 3;
			m_pSprite->SetDefaultAction(L"hstand", false);
			m_pSprite->SetMoveSpeed(337.5f/1000);
			UpdateAction(Nuclear::XPUSAT_PLAYACTIONFINISH);
		}
	}
	m_pSprite->SetRideName(name); 
}