#include "StdAfx.h"
#include "LinkedObjectStatusView.h"
#include "LinkedObjectEditorDoc.h"


IMPLEMENT_DYNCREATE(CLinkedObjectStatusView, CView)

CLinkedObjectStatusView::CLinkedObjectStatusView(void)
{
	m_strFileNameList.clear();
}

CLinkedObjectStatusView::~CLinkedObjectStatusView(void)
{
}


BEGIN_MESSAGE_MAP(CLinkedObjectStatusView, CView)
	ON_WM_LBUTTONDBLCLK()
END_MESSAGE_MAP()


void CLinkedObjectStatusView::OnDraw(CDC* pDC)
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	RECT rect;
	GetClientRect(&rect);

	pDC->SetBkMode(TRANSPARENT);
	int indexX = 0;
	int indexY = 0;
	for(std::list<CString>::iterator it = m_strFileNameList.begin(), ie = m_strFileNameList.end(); it != ie; ++it)
	{
		CString str = *it;
		pDC->TextOut(0, 20*indexY++, str);
	}
	if(! m_strFileNameList.empty() )
	{
		CString str = L"------在文件名所在行上双击鼠标左键可以复制一行------";
		pDC->TextOut(100, 50*indexY, str);
	}
}

void CLinkedObjectStatusView::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	//双击复制一行
	int nsize = (int)( m_strFileNameList.size() );
	if(nsize == 0)
	{
		return;
	}

	int nIndex = point.y/20;
	if(nIndex>nsize-1)
	{
		nIndex = nsize-1;
	}

	std::list<CString>::iterator it = m_strFileNameList.begin();
	for(int i=0; i<nIndex; i++)
	{
		++it;
	}

	CString strfileinfo = *it;

	//复制物件名称到剪贴板
	LPTSTR pText,pGlobal;  
	HGLOBAL hGlobal;  
	pText=(LPTSTR)(LPCTSTR)strfileinfo;  
	if(OpenClipboard())  
	{  
		EmptyClipboard();  
		hGlobal=GlobalAlloc(GMEM_DDESHARE, 2*strfileinfo.GetLength()+sizeof(WCHAR));  
		pGlobal = (TCHAR*)GlobalLock(hGlobal);  
		lstrcpy(pGlobal, pText);  
		GlobalUnlock(hGlobal);  
		SetClipboardData(CF_UNICODETEXT, hGlobal);  
		CloseClipboard();
	}


	CView::OnLButtonDblClk(nFlags, point);
}

#ifdef _DEBUG
void CLinkedObjectStatusView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CLinkedObjectStatusView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif
#endif //_DEBUG

