// SpriteSoundEditorView.cpp : CSpriteSoundEditorView ���ʵ��
//

#include "stdafx.h"
#include "SpriteSoundEditor.h"
#include "SpriteSoundEditorDoc.h"
#include "SpriteSoundEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CSpriteSoundEditorView

IMPLEMENT_DYNCREATE(CSpriteSoundEditorView, CView)

BEGIN_MESSAGE_MAP(CSpriteSoundEditorView, CView)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_WM_TIMER()
END_MESSAGE_MAP()

// CSpriteSoundEditorView ����/����

CSpriteSoundEditorView::CSpriteSoundEditorView() : m_bIsInited(false)
{
	// TODO: �ڴ˴���ӹ������

}

CSpriteSoundEditorView::~CSpriteSoundEditorView()
{
}

BOOL CSpriteSoundEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: �ڴ˴�ͨ���޸�
	//  CREATESTRUCT cs ���޸Ĵ��������ʽ

	return CView::PreCreateWindow(cs);
}

void CSpriteSoundEditorView::MainLoop()
{
	CSpriteSoundEditorDoc* pDoc = GetDocument();
	if (!m_bIsInited)
	{
		if (!pDoc->CheckEngine(GetSafeHwnd()))
		{
			AfxMessageBox(L"�����ʼ��ʧ�ܣ�");
			exit(0);
			return;
		}
		SetTimer(1, 50, NULL);
		m_bIsInited = true;
	}
	pDoc->MainLoop();
}

// CSpriteSoundEditorView ����

void CSpriteSoundEditorView::OnDraw(CDC* /*pDC*/)
{
	CSpriteSoundEditorDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
	MainLoop();
}


// CSpriteSoundEditorView ���

#ifdef _DEBUG
void CSpriteSoundEditorView::AssertValid() const
{
	CView::AssertValid();
}

void CSpriteSoundEditorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CSpriteSoundEditorDoc* CSpriteSoundEditorView::GetDocument() const // �ǵ��԰汾��������
{
	XPASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CSpriteSoundEditorDoc)));
	return (CSpriteSoundEditorDoc*)m_pDocument;
}
#endif //_DEBUG


// CSpriteSoundEditorView ��Ϣ�������

void CSpriteSoundEditorView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);
	RECT rect;
	GetClientRect(&rect);
	GetDocument()->SetClientRect(rect);
}

void CSpriteSoundEditorView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);
	RECT rect;
	GetClientRect(&rect);
	GetDocument()->SetClientRect(rect);
}

void CSpriteSoundEditorView::OnTimer(UINT_PTR nIDEvent)
{
	switch (nIDEvent)
	{
	case 1:
		MainLoop();
		break;
	}
	CView::OnTimer(nIDEvent);
}
