#pragma once
#include <afxrich.h>
// CRightView view
#include "LeftViewListener.h"
#include "FileViewer.h"
#include "Project.h"
#include "RecentProjectDlg.h"

class CRightView : public CView, public CLeftViewListener
{
	DECLARE_DYNCREATE(CRightView)

public:
	// 设置文件数据
	virtual void SetZipTreeItem( CZipTreeItem* item);

	bool OpenProject( CProject* project);
	void CloseProject();

protected:
	CRightView();           // protected constructor used by dynamic creation
	virtual ~CRightView();

virtual void OnDraw(CDC* pDC);      // overridden to draw this view
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	DECLARE_MESSAGE_MAP()

	typedef std::vector<CFileViewer*>		CFileViewerVector;
	CFileViewerVector						m_viewers;
	CFileViewer*							m_lastviewer;

	CRecentProjectDlg						m_dlgRecentProject;

public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
};


