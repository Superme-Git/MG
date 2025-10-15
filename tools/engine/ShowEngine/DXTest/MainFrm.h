// MainFrm.h : interface of the CMainFrame class
//


#pragma once

class CMainFrame : public CFrameWnd
{
	
protected: // create from serialization only
	
	DECLARE_DYNCREATE(CMainFrame)

// Attributes
public:
	CMainFrame();

// Operations
public:

// Overrides
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

// Implementation
public:
	virtual ~CMainFrame();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:  // control bar embedded members
	CStatusBar  m_wndStatusBar;
	CToolBar    m_wndToolBar;
	CToolBar    m_wndToolBarShow;

	CSplitterWnd	m_wndSplitter;
	CImageList		m_ilToolBar;
	CImageList		m_ilToolBarDisabled;
	CImageList		m_ilToolBarShow;
	CImageList		m_ilToolBarShowDisabled;

// Generated message map functions
protected:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	DECLARE_MESSAGE_MAP()
	virtual BOOL OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext);
public:
	virtual BOOL OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo);
	afx_msg void OnClose();
};


