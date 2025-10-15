#include "StdAfx.h"
#include "SoundEditor.h"
#include "SoundEditorDoc.h"
#include "PropView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CSoundEditorView

IMPLEMENT_DYNCREATE(CPropView, CView)

BEGIN_MESSAGE_MAP(CPropView, CView)
	ON_WM_SIZE()
	ON_WM_SIZING()
END_MESSAGE_MAP()

CPropView::CPropView(void) : m_bIsInited(false)
{
}

CPropView::~CPropView(void)
{
}

void CPropView::OnDraw(CDC* /*pDC*/)
{
	if (!m_bIsInited)
	{
		m_bIsInited = true;
		m_tabPropPanel.Create(m_tabPropPanel.IDD, this);
		ResetSize();
		ResetCont();
		m_tabPropPanel.ShowWindow(SW_SHOW);
	}
}


void CPropView::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		m_tabPropPanel.MoveWindow(&rect, TRUE);
	}
}
void CPropView::ResetCont()
{
	if (m_bIsInited)
		m_tabPropPanel.ResetCont();
}

// CPropertyView diagnostics

#ifdef _DEBUG
void CPropView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CPropView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif

CSoundEditorDoc* CPropView::GetDocument() const // 非调试版本是内联的
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CSoundEditorDoc)));
	return (CSoundEditorDoc*)m_pDocument;
}
#endif //_DEBUG

void CPropView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	// TODO: Add your specialized code here and/or call the base class
	ResetSize();
	ResetCont();
}

void CPropView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);
	ResetSize();
}

void CPropView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);
	ResetSize();
}
