#include "stdafx.h"
#include "..\common\log.h"
#include "..\renderer\renderer.h"
#include "..\ientitativetitlemanager.h"
#include "entitativeobj.h"

namespace Nuclear
{
	EntitativeObj::EntitativeObj(EngineBase *pEB) : Renderable(pEB), 
		m_titleHandle(INVALID_ENTITATIVE_TITLE_HANDLE), m_titleAlign(XPTA_DEFAULT), m_nTitleWidth(0), 
		m_nTitleHeight(0), m_nVisible(XP_VIS_ALL), m_bIsHighlight(false), m_mouseTestMode(XPTM_ALPHA)
	{}

	void EntitativeObj::SetTitle(EntitativeTitleHandle handle, XPTITLE_ALIGN align)
	{
		m_titleHandle = handle;
		m_titleAlign = align;
		m_nTitleWidth = 0;
		m_nTitleHeight = 0;
	}

	EntitativeTitleHandle EntitativeObj::UnsetTitle()
	{
		EntitativeTitleHandle result = m_titleHandle;
		m_titleHandle = INVALID_ENTITATIVE_TITLE_HANDLE;
		m_titleAlign = XPTA_DEFAULT;
		m_nTitleWidth = 0;
		m_nTitleHeight = 0;
		return result;
	}

	bool EntitativeObj::TestTitleAlpha(const Location &testloc, int r)
	{
		const Location location = GetTitleLocation();
		if (m_titleHandle != INVALID_ENTITATIVE_TITLE_HANDLE)
		{
			IEntitativeTitleManager *pETM = m_pEngine->GetEntitativeTitleManager();
			if (pETM)
			{
				int left = location.x - m_nTitleWidth / 2;
				if (testloc.x < left)
					return false;
				if (testloc.x > left + m_nTitleWidth)
					return false;
				if (m_titleAlign == XPTA_CENTER_BOTTOM)
				{
					if (testloc.y < location.y - m_nTitleHeight)
						return false;
					if (testloc.y > location.y)
						return false;
				} else {
					if (testloc.y < location.y)
						return false;
					if (testloc.y > location.y + m_nTitleHeight)
						return false;
				}
				
				return pETM->TestTitleAlpha(m_titleHandle, testloc);
			}
		}
		return false;
	}
	
	void EntitativeObj::RenderTitle(Canvas* canvas)
	{
		if (m_titleHandle != INVALID_ENTITATIVE_TITLE_HANDLE)
		{
			IEntitativeTitleManager *pETM = m_pEngine->GetEntitativeTitleManager();
			if (pETM)
			{
				CPOINT location = GetTitleLocation();
				CRECT vp = m_pEngine->GetViewport();
				CPOINT pos = location;
				pos.x -= vp.left;
				pos.y -= vp.top;
				pETM->RenderTitle(m_titleHandle, pos);
				if (canvas)
				{
					CRECT rect;
					rect.left = location.x - m_nTitleWidth / 2;
					rect.right = rect.left + m_nTitleWidth;
					rect.bottom = rect.top = location.y;
					if (m_titleAlign == XPTA_CENTER_BOTTOM)
					{
						rect.top -= m_nTitleHeight;
					} else {
						rect.bottom += m_nTitleHeight;
					}
					canvas->Draw(this, rect);
				}
			}
		}
		
	}
}