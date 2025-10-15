

#include "stdafx.h"
#include "EffectEditor.h"
#include "EffectEditorDoc.h"
#include "TimeBarView.h"
#include "../engine/effect/particleeffect.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNCREATE(CTimeBarView, CView)


BEGIN_MESSAGE_MAP(CTimeBarView, CView)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_WM_KEYDOWN()
END_MESSAGE_MAP()




void CTimeBarView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	CEffectEditorDoc *pDoc = GetDocument();
	switch (nChar)
	{
	case VK_DELETE:
		{
			const EffectClipSet& selectedClips = pDoc->GetSelectedClips();
			EffectClipSet::const_iterator iter = selectedClips.begin();
			bool hasDel = false;
			bool hasError = false;
			for (;iter!=selectedClips.end();)
			{
#pragma warning (push)
#pragma warning (disable : 4311)
				if (m_TimeBar.DeleteAnObj(reinterpret_cast<DWORD>(*iter)))
#pragma warning (pop)
				{
					if (pDoc->DelEffectClip(*iter))
					{
						pDoc->UnSelectClip(*(iter++));
						hasDel = true;
					} else {
						iter++;
						hasError = true;
					}
				}
			}
			if (hasDel)
			{
				pDoc->m_bNeedReplay = true;
				if (!hasError)
					pDoc->UpdateAllViews(this);
			}
			if (hasError)
			{
				pDoc->UpdateAllViews(NULL);
			}
		}
		break;
	}

	__super::OnKeyDown(nChar, nRepCnt, nFlags);
}

bool CTimeBarView::OnSelectedItem(DWORD objID)
{
	CEffectEditorDoc *pDoc = GetDocument();
#pragma warning (push)
#pragma warning (disable : 4312)
	Nuclear::AbstractEffectClip* pClip = reinterpret_cast<Nuclear::AbstractEffectClip*>(objID);
#pragma warning (pop)
	if (pDoc->SelectClip(pClip))
	{
		pDoc->m_bNeedReplay = false;
		pDoc->UpdateAllViews(this);
	}
	return true;
}

bool CTimeBarView::OnUnSelectedAllItem()
{
	GetDocument()->UnSelectedAllClip();
	GetDocument()->UpdateAllViews(this);
	return true;
}

bool CTimeBarView::OnUnSelectedItem(DWORD objID)
{
	CEffectEditorDoc *pDoc = GetDocument();
#pragma warning (push)
#pragma warning (disable : 4312)
	Nuclear::AbstractEffectClip* pClip = reinterpret_cast<Nuclear::AbstractEffectClip*>(objID);
#pragma warning (pop)
	if (pDoc->UnSelectClip(pClip) > 0)
	{
		pDoc->UpdateAllViews(this);
		return true;
	}
	return false;
}

bool CTimeBarView::OnModifyMaxFrame(int nMaxFrame)
{
	CEffectEditorDoc *pDoc = GetDocument();
	if (pDoc->SetTotalFrameNum(nMaxFrame))	{		
		pDoc->m_bNeedReplay = true;		
		pDoc->UpdateAllViews(this);		
		return true;	
	} else {		
		return false;	
	}
}

bool CTimeBarView::OnModifySpare(int pos)
{	
	CEffectEditorDoc *pDoc = GetDocument();
	if (pDoc->SetDivideLayer(pos + 1))	{
		pDoc->m_bNeedReplay = false;
		pDoc->UpdateAllViews(this);
		return true;
	} else {
		return false;
	}
}

bool CTimeBarView::OnModify(const ClipObj& obj)
{
	Nuclear::AbstractEffectClip* pClip;
	CEffectEditorDoc *pDoc = GetDocument();
#pragma warning (push)
#pragma warning (disable : 4312)
	pClip = reinterpret_cast<Nuclear::AbstractEffectClip*>(obj.objID);
#pragma warning (pop)
	if (pClip == NULL)
		return false;

	int state = pClip->GetAnimation()->GetPlayState();

	bool ok = true;

	int endFrame = obj.endFrame - 1;
	if (obj.isSpecObj)
		endFrame = -1;
	if (endFrame != pClip->GetEndFrame() || obj.startFrame != pClip->GetStartFrame())
	{
		ok = pDoc->ModifyStartEndFrame(pClip, obj.startFrame, endFrame, this);
	}
	if (obj.layerID != pClip->GetLayer())
	{
		ok = pDoc->ModifyLayer(pClip, obj.layerID) && ok;
	}
	if (ok)	{		
		pDoc->m_bNeedReplay = true;
		pDoc->UpdateAllViews(this);
	}	
	return ok;
}

void CTimeBarView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	CEffectEditorDoc *pDoc = GetDocument();
	m_TimeBar.RemoveAll();
	m_TimeBar.SetMaxFrame(pDoc->GetTotalFrames());
	m_TimeBar.SetLayerSeparatorPos(pDoc->GetDivideLayer() - 1);
	switch (pDoc->GetEffectFileType())
	{
	case EFT_EFFECT:
		m_TimeBar.SetLayerCount(Nuclear::GEffect::MAX_CLIP_LAYER);
		break;
	case EFT_SEBIND:
		m_TimeBar.SetLayerCount(1);
		break;
	}

	ClipObj obj;
	Nuclear::AbstractEffectClip_LIST clipList;
	Nuclear::AbstractEffectClip_LIST::iterator iter;
	Nuclear::AbstractEffectClip* pClip;
	eAnimationPlayType playType;
	for (int i=0;i<Nuclear::GEffect::MAX_CLIP_LAYER;++i)
	{
		obj.layerID = i;
		clipList.clear();
		pDoc->GetClipListByLayer(i, clipList);
		for (iter=clipList.begin();iter!=clipList.end();++iter)
		{
			pClip = *iter;
			if (!pDoc->GetClipStartEndFrame(pClip, playType, obj.startFrame, obj.endFrame))
				continue;
			obj.isSpecObj = (playType != APT_NORMAL);
#pragma warning (push)
#pragma warning (disable : 4311)
			obj.objID = reinterpret_cast<DWORD>(pClip);
#pragma warning (pop)
			obj.name = pClip->GetAnimation()->GetName();
			m_TimeBar.AddAnObj(obj);
		}
	}
}

void CTimeBarView::OnInitialUpdate()
{
	CEffectEditorDoc *pDoc = GetDocument();
	CView::OnInitialUpdate();
	m_TimeBar.SetNotify(this);
	pDoc->AddNotify(this);
}

void CTimeBarView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);
	ReSizeTimeBar();
}

void CTimeBarView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);
	ReSizeTimeBar();
}

void CTimeBarView::ReSizeTimeBar()
{
	if (m_TimeBar.m_hWnd)
	{
		CRect rect;
		GetClientRect(&rect);
		m_TimeBar.MoveWindow(&rect, true);
	}
}




#ifdef _DEBUG
CEffectEditorDoc* CTimeBarView::GetDocument() const 
{
	XPASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CEffectEditorDoc)));
	return (CEffectEditorDoc*)m_pDocument;
}

void CTimeBarView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CTimeBarView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif
#endif 


void CTimeBarView::OnDraw(CDC* pDC)
{
	CDocument* pDoc = GetDocument();
	if (!m_bInited)
	{
		m_bInited = true;
		m_TimeBar.Create(m_TimeBar.IDD, this);
		ReSizeTimeBar();
		m_TimeBar.ShowWindow(SW_SHOW);
	}
}




CTimeBarView::~CTimeBarView()
{
}
CTimeBarView::CTimeBarView() : m_bInited(false)
{

}

