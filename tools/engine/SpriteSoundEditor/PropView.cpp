// PropView.cpp : implementation file
//

#include "stdafx.h"
#include "SpriteSoundEditor.h"
#include "SpriteSoundEditorDoc.h"
#include "PropView.h"


// CPropView

IMPLEMENT_DYNCREATE(CPropView, CView)

CPropView::CPropView() : m_bIsInited(false)
{

}

CPropView::~CPropView()
{
}

BEGIN_MESSAGE_MAP(CPropView, CView)
END_MESSAGE_MAP()


// CPropView drawing

void CPropView::OnDraw(CDC* pDC)
{
	if (!m_bIsInited)
	{
		m_bIsInited = true;
		m_dlg.Create(m_dlg.IDD, this, this);
		ResetSize();
		ResetCont();
		m_dlg.ShowWindow(SW_SHOW);
	}
}

void CPropView::ResetCont()
{
	m_dlg.ResetCont();
}

void CPropView::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		m_dlg.MoveWindow(&rect);
	}
}


// CPropView diagnostics

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

CSpriteSoundEditorDoc* CPropView::GetDocument() const // 非调试版本是内联的
{
	XPASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CSpriteSoundEditorDoc)));
	return (CSpriteSoundEditorDoc*)m_pDocument;
}

#endif //_DEBUG


// CPropView message handlers

void CPropView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
	ResetCont();
}
