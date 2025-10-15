#include "../common/nuxpmaths.h"
#include "../renderer/nurenderer.h"
#include "../engine/nuconfigmanager.h"
#include "nuregionmap.h"
#include "nulkotree.h"

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

	bool LkoTree::render(Canvas* canvas, bool isAlpha, bool realRender)
	{
		if (!realRender) return false;

		DEList::iterator iter = m_drawList.begin();
		Renderer *pRenderer = m_pEB->GetRenderer();
		Region::LkoEle* pDE;
		NuclearRect rect = m_pEB->GetViewport();
		rect.bottom -= rect.top;
		rect.right -= rect.left;
		rect.top = rect.left = 0;
		int time = m_pEB->GetGameTime();
		DrawPictureParam param;
		ConfigManager *pCfgMan = m_pEB->GetConfigManager();
		NuclearColor color(m_color);
		bool IsRenderNightWithRenderTarget = true;
		if (pCfgMan)
		{
			IsRenderNightWithRenderTarget = pCfgMan->IsRenderNightEffectWithRenderTarget();
			if (isAlpha)
			{
				color.a = static_cast<unsigned char>(pCfgMan->GetMaskAlpha() * color.a);
			}
		}
		param.pColor = &color;
		param.colorCount = 1;

		bool bLight = false;
		for (;iter!=m_drawList.end();++iter)
		{
			bLight = false;
			pDE = (*iter);
			if (pDE->pTreeNode->LinkedObjectMoveParam.m_dwFlag & FIRE_LINKEDOBJ_LIGHT)
			{
				if (!pDE->pTreeNode->CheckLightTime(time))
					continue;
				if( !IsRenderNightWithRenderTarget )
					bLight = true;
			}

			if (CheckNeedDraw(pDE->rect, pDE->pTreeNode->mTransResult, rect))
			{
				param.handle = pDE->pPicResrc->handle;
				param.pMatrix = &pDE->pTreeNode->mTransResult;
				param.pRect = &pDE->rect;

//				if( !IsRenderNightWithRenderTarget )
//				{
//                    assert(false);
//					//能进来，代表pCfgMan没有问题
////					XPRENDER_EFFECT oldEffect = pRenderer->GetRenderEffect();
////					if (bLight && pCfgMan->IsRenderNightEffectByShader())
////					{
////						pRenderer->SelectRenderEffect(XPRE_NULL);
////					}
////					pRenderer->DrawPicture(param);
////					if (bLight && pCfgMan->IsRenderNightEffectByShader())
////					{
////						pRenderer->SelectRenderEffect(oldEffect);
////					}
//				}
//				else
				{
					pRenderer->DrawPicture(param);
				}
			}
		}
		return true;
	}

	bool LkoTree::CheckNeedDraw(const NuclearFRectt &rect, const NuclearMatrix3 &m, const NuclearRect& viewport)
	{
		NuclearFPoint pt, ptOut;
		NuclearFRectt fvp = viewport.ToFRECT();
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