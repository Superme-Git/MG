


#pragma once

class CMainFrame : public CMDIFrameWnd
{
	DECLARE_DYNAMIC(CMainFrame)
public:
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif
	virtual ~CMainFrame();

	void SetStateBarMousePosition(int x, int y);

	void HideToolBars();
	void ShowToolBars();
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

	CMainFrame();

protected:  
	DECLARE_MESSAGE_MAP()
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	int m_nToolBarCount;

	CToolBar    m_LayerEditToolBar;

	CToolBar    m_ToolBar;
	CStatusBar  m_StatusBar;
};