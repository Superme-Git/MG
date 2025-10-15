


#pragma once

class CViewExSplitWnd : public CSplitterWnd
{
	DECLARE_DYNAMIC(CViewExSplitWnd)

	
public:
	CWnd* GetActivePane(int* pRow = NULL, int* pCol = NULL);
	~CViewExSplitWnd();
	CViewExSplitWnd();
};

class CMainFrame : public CFrameWnd
{
	
protected: 
	CMainFrame();
	DECLARE_DYNCREATE(CMainFrame)


protected:
	CViewExSplitWnd m_wndSplitter2;
	CViewExSplitWnd m_wndSplitter;

public:
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif
	virtual ~CMainFrame();
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

	virtual BOOL OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext);
	CViewExSplitWnd& GetSplitterWnd2() { return m_wndSplitter2; }

	CViewExSplitWnd& GetSplitterWnd() { return m_wndSplitter; }

protected:  
	DECLARE_MESSAGE_MAP()
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	CToolBar    m_wndToolBar;

	CStatusBar  m_wndStatusBar;

public:
	afx_msg void OnClose();
};


