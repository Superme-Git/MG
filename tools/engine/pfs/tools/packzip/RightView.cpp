// RightView.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "RightView.h"
#include "BinaryFileViewer.h"
#include "BmpFileViewer.h"
#include "GlobalInfo.h"
// CRightView

IMPLEMENT_DYNCREATE(CRightView, CView)

CRightView::CRightView()
: m_lastviewer( NULL)
{
}

CRightView::~CRightView()
{
	for( CFileViewerVector::iterator itor = m_viewers.begin(); itor != m_viewers.end(); ++itor)
		delete *itor;
}

BEGIN_MESSAGE_MAP(CRightView, CView)
	ON_WM_CREATE()
	ON_WM_SIZE()
END_MESSAGE_MAP()


void CRightView::OnDraw(CDC* pDC)
{
	CDocument* pDoc = GetDocument();
	// TODO: add draw code here
}


// CTestView diagnostics

#ifdef _DEBUG
void CRightView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CRightView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif
#endif //_DEBUG

void CRightView::SetZipTreeItem( CZipTreeItem* item)
{
	if( !item)
		return;
	if( item->GetIsPath())
		return;
	std::wstring sExt = CGlobalInfo::GetFileExt( item->GetName());
	CFileViewer* current = NULL;
	for( CFileViewerVector::iterator itor = m_viewers.begin(); itor != m_viewers.end(); ++itor)
	{
		if( (*itor)->IsBelong( sExt))
		{
			current = *itor;
			break;
		}
	}
	if( current)
	{
		if( current != m_lastviewer)
		{
			if( m_lastviewer)
				m_lastviewer->Show( false);
			m_lastviewer = current;
		}
		current->Move();
		current->SetTreeItem( *item);
		current->Show( true);
	}
}


int CRightView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (__super::OnCreate(lpCreateStruct) == -1)
		return -1;

	// TODO:  Add your specialized creation code here
	m_dlgRecentProject.Create( this);
	m_dlgRecentProject.ShowWindow( SW_SHOW);

	CBmpFileViewer* bmp = new CBmpFileViewer;
	bmp->Create( this);
	m_viewers.push_back( bmp);
	CBinaryFileViewer* binary = new CBinaryFileViewer;
	binary->Create( this);
	m_viewers.push_back( binary);

	return 0;
}

void CRightView::OnSize(UINT nType, int cx, int cy)
{
	__super::OnSize(nType, cx, cy);
	// TODO: Add your message handler code here
	if( m_dlgRecentProject.IsWindowVisible())
		m_dlgRecentProject.MoveWindow( 0, 0, cx, cy);
	if( m_lastviewer)
		m_lastviewer->Move();
}

bool CRightView::OpenProject( CProject* project)
{
	m_dlgRecentProject.ShowWindow( SW_HIDE);
	return true;
}

void CRightView::CloseProject()
{
	if( m_lastviewer)
	{
		m_lastviewer->Show( false);
		m_lastviewer = NULL;
	}
	CRect rc;
	GetClientRect( &rc);
	m_dlgRecentProject.MoveWindow( rc);
	m_dlgRecentProject.OpenProject();
	m_dlgRecentProject.ShowWindow( SW_SHOW);
}