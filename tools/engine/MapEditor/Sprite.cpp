#include "stdafx.h"
#include <assert.h>
#include "..\engine\common\log.h"
#include "..\engine\sprite\spritemanager.h"
#include "Sprite.h"


void CMapEditorApplicationDelegate::OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type)
{
	Sprite* pSprite = dynamic_cast<Sprite*>(sprite);
	XPASSERT(pSprite);
	pSprite->UpdateAnimation(type);
}

void Sprite::UpdateAnimation(Nuclear::XPUSA_TYPE type)
{
	switch (type)
	{
	case Nuclear::XPUSAT_PLAYACTIONFINISH:
	case Nuclear::XPUSAT_DEFAULTACTIONFINISH:
		if (IsMoving())
		{
			switch (m_AniState)
			{
			case 0:
			case 2:
				m_AniState = 1;
				PlayAction(m_RunLeft);
				break;
			case 1:
				m_AniState = 2;
				PlayAction(m_RunRight);
				break;
			default:
				assert(false);
			}
		} else {
			m_AniState = 0;
			PlayAction(m_Stand);
		}
		break;
	}
}

void Sprite::RunTo(int x, int y)
{
	astar::Path trail;
	astar::Direction dir;
	Nuclear::FPOINT loc = GetLocation();
	dir.x = x - loc.x;
	dir.y = y - loc.y;
	dir.z = 0.0f;
	astar::Point pt(loc.x, loc.y, dir);
	trail.push_back(Nuclear::WorldLogicCoord::world2logic(pt));
	pt.x = (float)x;
	pt.y = (float)y;
	trail.push_back(Nuclear::WorldLogicCoord::world2logic(pt));
	PlayAction(m_RunLeft);
	m_AniState = 1;
	MoveTo(trail);
}

void Sprite::SwitchToRun()
{
	switch (m_AniState)
	{
	case 0:
		m_AniState = 1;
		PlayAction(m_RunLeft);
		break;
	default:
		break;
	}
}

Sprite::~Sprite(void)
{
}

Sprite::Sprite(Nuclear::EngineBase *pEB, const std::wstring &modelname) : 
Nuclear::ComponentSprite(pEB, modelname), m_AniState(0)
{

	SetMoveSpeed(225.f/1000);
}

