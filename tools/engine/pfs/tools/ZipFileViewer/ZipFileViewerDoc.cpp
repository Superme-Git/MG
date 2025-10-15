// ZipFileViewerDoc.cpp : CZipFileViewerDoc ���ʵ��
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


// CZipFileViewerDoc ����/����

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


// CZipFileViewerDoc ���л�

void CZipFileViewerDoc::Serialize(CArchive& ar)
{
	// CEditView ����һ�������������л��ı༭�ؼ�
	//reinterpret_cast<CEditView*>(m_viewList.GetHead())->SerializeRaw(ar);
}


// CZipFileViewerDoc ����

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
