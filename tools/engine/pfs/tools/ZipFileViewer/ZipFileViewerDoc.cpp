// ZipFileViewerDoc.cpp : CZipFileViewerDoc 类的实现
//

#include "stdafx.h"
#include "ZipFileViewer.h"
#include "ZipFileViewerDoc.h"
#include "MainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CZipFileViewerDoc

IMPLEMENT_DYNCREATE(CZipFileViewerDoc, CDocument)

BEGIN_MESSAGE_MAP(CZipFileViewerDoc, CDocument)
END_MESSAGE_MAP()


// CZipFileViewerDoc 构造/析构

CZipFileViewerDoc::CZipFileViewerDoc()
{
}

CZipFileViewerDoc::~CZipFileViewerDoc()
{
}

class CFrameHelper : public CMainFrame
{
	friend CZipFileViewerDoc;
};

BOOL CZipFileViewerDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	reinterpret_cast<CEditView*>(m_viewList.GetHead())->SetWindowText(NULL);

	CFrameHelper * pFrame = (CFrameHelper *)AfxGetMainWnd();
	if( pFrame != NULL )
	{
		pFrame->m_wndStatusBar.SetPaneText(1, L"", TRUE);
	}

	return TRUE;
}


// CZipFileViewerDoc 序列化

void CZipFileViewerDoc::Serialize(CArchive& ar)
{
	// CEditView 包含一个处理所有序列化的编辑控件
	//reinterpret_cast<CEditView*>(m_viewList.GetHead())->SerializeRaw(ar);
}


// CZipFileViewerDoc 命令

BOOL CZipFileViewerDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	if (!CDocument::OnOpenDocument(lpszPathName))
		return FALSE;

	CFrameHelper * pFrame = (CFrameHelper *)AfxGetMainWnd();
	if( pFrame != NULL )
	{
		pFrame->m_wndStatusBar.SetPaneText(1, lpszPathName, TRUE);
	}

	return TRUE;
}
