#include "stdafx.h"
#include "..\common\xpmaths.h"
#include "..\renderer\renderer.h"
#include "..\engine\configmanager.h"
#include "regionmap.h"
#include "lkotree.h"

namespace Nuclear
{
	LkoTree::~LkoTree(void)
	{
		DEList::iterator iter = m_drawList.begin();
		for (;iter!=m_drawList.end();++iter)
		{
			delete(*iter);
		}
	}

	void LkoTree::AddANode(Region::LkoEle *node, bool bIsRoot)
	{
		m_drawList.push_back(node);
		if (m_updateListset.find(node->pTreeNode) == m_updateListset.end())
		{
			if (bIsRoot)
			{
				m_Lists.insert(m_Lists.begin(), node->pTreeNode);
			} else {
				m_Lists.push_back(node->pTreeNode);
			}
			m_updateListset.insert(node->pTreeNode);
		}
	}

	bool LkoTree::Render(Canvas* canvas)
	{
		DEList::iterator iter = m_drawList.begin();
		Renderer *pRenderer = m_pEngine->GetRenderer();
		Region::LkoEle* pDE;
		CRECT rect = m_pEngine->GetViewport();
		rect.bottom -= rect.top;
		rect.right -= rect.left;
		rect.top = rect.left = 0;
		int time = m_pEngine->GetGameTime();
		DrawPictureParam param;
		param.pColor = &m_color;
		param.colorCount = 1;

		bool bLight = false;
		for (;iter!=m_drawList.end();++iter)
		{
			pDE = (*iter);
			if (pDE->pTreeNode->LinkedObjectMoveParam.m_dwFlag & XP_LINKEDOBJ_LIGHT)
			{
				if (!pDE->pTreeNode->CheckLightTime(time))
					continue;
				if( !m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget() )
					bLight = true;
			}

			if (CheckNeedDraw(pDE->rect, pDE->pTreeNode->mTransResult, rect))
			{
				param.handle = pDE->pPicResrc->handle;
				param.pMatrix = &pDE->pTreeNode->mTransResult;
				param.pRect = &pDE->rect;

				if( !m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget() )
				{

					XPRENDER_EFFECT oldEffect = pRenderer->GetRenderEffect();
					if (bLight && m_pEngine->GetConfigManager()->IsRenderNightEffectByShader())
					{
						pRenderer->SelectRenderEffect(XPRE_NULL);
					}


					pRenderer->DrawPicture(param);

					if (bLight && m_pEngine->GetConfigManager()->IsRenderNightEffectByShader())
					{
						pRenderer->SelectRenderEffect(oldEffect);
					}
				}
				else
				{
					pRenderer->DrawPicture(param);
				}

			}
		}
		return true;
	}

	bool LkoTree::CheckNeedDraw(const FRECT &rect, const XPMATRIX3 &m, const CRECT& viewport)
	{
		FPOINT pt, ptOut;
		FRECT fvp = viewport.ToFRECT();
		pt.x = rect.left;
		pt.y = rect.top;
		XPVec3TransformCoord(&ptOut, &pt, &m);
		if (fvp.PtInRect(ptOut))
			return true;
		pt.y = rect.bottom;
		XPVec3TransformCoord(&ptOut, &pt, &m);
		if (fvp.PtInRect(ptOut))
			return true;
		pt.x = rect.right;
		XPVec3TransformCoord(&ptOut, &pt, &m);
		if (fvp.PtInRect(ptOut))
			return true;
		pt.y = rect.top;
		XPVec3TransformCoord(&ptOut, &pt, &m);
		if (fvp.PtInRect(ptOut))
			return true;
		return false;
	}
}