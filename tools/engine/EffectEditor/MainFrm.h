#pragma once

class CMainFrame : public CFrameWnd
{
	
protected: 
	CMainFrame();
	DECLARE_DYNCREATE(CMainFrame)


protected:
	CSplitterWnd m_HSplitterWindow;
	CSplitterWnd m_VSplitterWindow;

public:
	void SetStateBarMousePosition(int x, int y);
	void SetStateBarBgPosition(int x, int y);
	void SetStateBarEffectPosition(int x, int y);
	void SetStateBarSelEffectPosition(int x, int y);

	virtual BOOL OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext);
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

	virtual ~CMainFrame();

#ifdef _DEBUG
	virtual void Dump(CDumpContext& dc) const;
	virtual void AssertValid() const;
#endif

protected:  
	CStatusBar  m_wndStatusBar;
	CToolBar    m_wndToolBar;


protected:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo);
};


