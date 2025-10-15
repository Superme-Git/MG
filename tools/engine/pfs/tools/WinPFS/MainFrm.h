// MainFrm.h : CMainFrame 类的接口
//
#pragma once


class CWinPFSView;
class CMainFrame : public CFrameWnd
{
	
protected: // 仅从序列化创建
	CMainFrame();
	DECLARE_DYNCREATE(CMainFrame)

// 属性
protected:
	CSplitterWnd m_wndSplitter;
public:

// 操作
public:
	void OnUpdateItemCount( NMHDR* nmhdr, LRESULT* pResult );

// 重写
public:
	virtual BOOL OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext);
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

// 实现
public:
	virtual ~CMainFrame();
	CWinPFSView* GetRightPane();

protected:  // 控件条嵌入成员
	CStatusBar  m_wndStatusBar;
	CToolBar    m_wndToolBar;
	CImageList	m_ilToolBar;
	//CReBar      m_wndReBar;
	//CDialogBar      m_wndDlgBar;

// 生成的消息映射函数
protected:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnUpdateViewStyles(CCmdUI* pCmdUI);
	afx_msg void OnViewStyle(UINT nCommandID);
	DECLARE_MESSAGE_MAP()
	virtual void OnUpdateFrameTitle(BOOL bAddToTitle);
};


