// SpriteSoundEditorView.cpp : CSpriteSoundEditorView 类的实现
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

// CSpriteSoundEditorView 构造/析构

CSpriteSoundEditorView::CSpriteSoundEditorView() : m_bIsInited(false)
{
	// TODO: 在此处添加构造代码

}

CSpriteSoundEditorView::~CSpriteSoundEditorView()
{
}

BOOL CSpriteSoundEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	return CView::PreCreateWindow(cs);
}

void CSpriteSoundEditorView::MainLoop()
{
	CSpriteSoundEditorDoc* pDoc = GetDocument();
	if (!m_bIsInited)
	{
		if (!pDoc->CheckEngine(GetSafeHwnd()))
		{
			AfxMessageBox(L"引擎初始化失败！");
			exit(0);
			return;
		}
		SetTimer(1, 50, NULL);
		m_bIsInited = true;
	}
	pDoc->MainLoop();
}

// CSpriteSoundEditorView 绘制

void CSpriteSoundEditorView::OnDraw(CDC* /*pDC*/)
{
	CSpriteSoundEditorDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
	MainLoop();
}


// CSpriteSoundEditorView 诊断

#ifdef _DEBUG
void CSpriteSoundEditorView::AssertValid() const
{
	CView::AssertValid();
}

void CSpriteSoundEditorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CSpriteSoundEditorDoc* CSpriteSoundEditorView::GetDocument() const // 非调试版本是内联的
{
	XPASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CSpriteSoundEditorDoc)));
	return (CSpriteSoundEditorDoc*)m_pDocument;
}
#endif //_DEBUG


// CSpriteSoundEditorView 消息处理程序

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
