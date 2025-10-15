#include "StdAfx.h"
#include "DistortionStatusView.h"
#include "DistortionEditorDoc.h"


IMPLEMENT_DYNCREATE(CDistortionStatusView, CView)

CDistortionStatusView::CDistortionStatusView(void)
{
}

CDistortionStatusView::~CDistortionStatusView(void)
{
}


BEGIN_MESSAGE_MAP(CDistortionStatusView, CView)
	ON_WM_LBUTTONDBLCLK()
END_MESSAGE_MAP()


void CDistortionStatusView::OnDraw(CDC* pDC)
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::PDistortionObject& distortionObject = pDoc->GetDistortionObject();

//	RECT rect;
//	GetClientRect(&rect);

	bool bflag = false;
	int nIndex=0;
	CString str = L"使用的美术资源:";
	pDC->SetBkMode(TRANSPARENT);
	pDC->TextOut(0, 20*nIndex++, str);
	str = distortionObject.m_distortionObj.strImgFileName.c_str();
	if(str != L"")
	{
		bflag = true;
		str += L"------主节点";
		pDC->TextOut(0, 20*nIndex++, str);
	}
	str = distortionObject.m_distortionObj.strLightFile.c_str();
	if(str != L"")
	{
		bflag = true;
		str += L"------灯光节点";
		pDC->TextOut(0, 20*nIndex++, str);
	}
	if(bflag)
	{
		str = L"------双击鼠标复制对应行上的文件名------";
		pDC->TextOut(100, 50*nIndex++, str);
	}
}

void CDistortionStatusView::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::PDistortionObject& distortionObject = pDoc->GetDistortionObject();
	CString strMainFile = distortionObject.m_distortionObj.strImgFileName.c_str();
	CString strLightFile = distortionObject.m_distortionObj.strLightFile.c_str();

	int nIndex = point.y/20;
	CString str = strMainFile;
	if(nIndex>1)
	{
		str = strLightFile;
	}
	//复制物件名称到剪贴板
	LPTSTR pText,pGlobal;  
	HGLOBAL hGlobal;  
	pText=(LPTSTR)(LPCTSTR)str;  
	if(OpenClipboard())  
	{  
		EmptyClipboard();  
		hGlobal=GlobalAlloc(GMEM_DDESHARE, 2*str.GetLength()+sizeof(WCHAR));  
		pGlobal = (TCHAR*)GlobalLock(hGlobal);  
		lstrcpy(pGlobal, pText);  
		GlobalUnlock(hGlobal);  
		SetClipboardData(CF_UNICODETEXT, hGlobal);  
		CloseClipboard();
	}

	CView::OnLButtonDblClk(nFlags, point);
}


#ifdef _DEBUG
void CDistortionStatusView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CDistortionStatusView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif
#endif //_DEBUG
