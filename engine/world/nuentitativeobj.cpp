#include "../common/nulog.h"
#include "../renderer/nurenderer.h"
#include "../nuientitativetitlemanager.h"
#include "nuentitativeobj.h"

namespace Nuclear
{
	EntitativeObj::EntitativeObj(EngineBase *pEB) : Renderable(pEB), 
		m_titleHandle(INVALID_ENTITATIVE_TITLE_HANDLE), m_titleAlign(XPTA_DEFAULT), m_nTitleWidth(0), 
		m_nTitleHeight(0), m_nVisible(XP_VIS_ALL), m_bIsHighlight(false), m_mouseTestMode(XPTM_PIC_RECT)
	{}

	void EntitativeObj::SetTitle(EntitativeTitleHandle handle, NuclearTitleAlign align)
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

	bool EntitativeObj::TestTitleAlpha(const NuclearLocation &testloc, int r)
	{
		const NuclearLocation location = GetTitleLocation();
		if (m_titleHandle != INVALID_ENTITATIVE_TITLE_HANDLE)
		{
			IEntitativeTitleManager *pETM = m_pEB->GetEntitativeTitleManager();
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
	
	void EntitativeObj::RenderTitle(Canvas* canvas, bool realRender)
	{
		if (realRender && m_titleHandle != INVALID_ENTITATIVE_TITLE_HANDLE)
		{
			IEntitativeTitleManager *pETM = m_pEB->GetEntitativeTitleManager();
			if (pETM)
			{
				NuclearPoint location = GetTitleLocation();
				NuclearRect vp = m_pEB->GetViewport();
				NuclearPoint pos = location;
				pos.x -= vp.left;
				pos.y -= vp.top;
				pos *= m_pEB->GetWorldScale();
				pETM->RenderTitle(m_titleHandle, pos);
				if (canvas)
				{
					NuclearRect rect;
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