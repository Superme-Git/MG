

#include "stdafx.h"
#include "EffectEditor.h"
#include "PropertyView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif



IMPLEMENT_DYNCREATE(CPropertyView, CView)


BEGIN_MESSAGE_MAP(CPropertyView, CView)
	ON_WM_SIZE()
	ON_WM_SIZING()
END_MESSAGE_MAP()




void CPropertyView::ResetCount()
{
	m_TabPropPanel.ResetCont();
}

void CPropertyView::ResetSize()
{
	if (m_bInited)
	{
		CRect rect;
		GetClientRect(&rect);
		m_TabPropPanel.MoveWindow(&rect, TRUE);
	}
}

void CPropertyView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);
	ResetSize();
}


void CPropertyView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);
	ResetSize();
}

void CPropertyView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	ResetSize();
	ResetCount();
}

void CPropertyView::OnInitialUpdate()
{
	CView::OnInitialUpdate();
	ResetSize();
	ResetCount();

}

#ifdef _DEBUG
void CPropertyView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CPropertyView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif

CEffectEditorDoc* CPropertyView::GetDocument() const 
{
	XPASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CEffectEditorDoc)));
	return (CEffectEditorDoc*)m_pDocument;
}
#endif 



void CPropertyView::OnDraw(CDC* pDC)
{
	CDocument* pDoc = GetDocument();
	if (!m_bInited)
	{
		m_bInited = true;
		m_TabPropPanel.Create(m_TabPropPanel.IDD, this);
		ResetSize();
		ResetCount();
		m_TabPropPanel.ShowWindow(SW_SHOW);
	}
}




CPropertyView::~CPropertyView()
{
}

CPropertyView::CPropertyView() : m_bInited(false)
{

}

