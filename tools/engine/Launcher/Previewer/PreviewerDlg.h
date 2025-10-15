// PreviewerDlg.h : header file
//

#include "DIYButton.h"
#include "WebBrowserCtrl.h"
#include "ODProgressCtrl.h"
#include "LayoutManager.h"
// #include "TrayIcon.h"

#pragma once

// CPreviewerDlg dialog
class CPreviewerDlg : public CDialog
{
// Construction
public:
	CPreviewerDlg(CWnd* pParent = NULL);	// standard constructor
	~CPreviewerDlg();

// Dialog Data
	enum { IDD = IDD_PREVIEWER_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;
	HICON m_hIconLarge;
	HICON m_hIconSmall;
	CDIYButton m_btnCheckUpdate;	// 检查更新
	CDIYButton m_btnManualUpdate;   // 手动更新
	CDIYButton m_btnRepairSetup;    // 版本验证
	CDIYButton m_btnLaunchGame;     // 进入游戏
	CDIYButton m_btnMinimize;		// 最小化
	CDIYButton m_btnClose;			// 关闭

	CWebBrowserCtrl* m_htmlView;
	CODProgressCtrl m_downloadPgrsCtrl;
	CODProgressCtrl m_updatePgrsCtrl;
	CLayoutManager m_layoutManager;
	void* m_pThisWatcher;

	// CTrayIcon m_trayIcon;	
	CWinThread* m_pDownloadOnlineResourceThread; // 下载

	HBRUSH m_hBKBrush;
	CStatic m_globalStatus;
	CStatic m_stepMessage;

	COLORREF m_globalStatusControlColor;// RGB(r,g,b)型的颜色值
	COLORREF m_stepMessageControlColor;	//

	// 刷新web页
	void ReNavigageWebPage();

	// 禁止浏览器空间的滚动条
	void ForbidWebBrowseScrollBar();

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg LRESULT OnDownloadWebPageEnd( WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnDeployWebPageEnd( WPARAM wParam, LPARAM lParam);
	afx_msg BOOL OnToolTipNotify( UINT id, NMHDR * pNotifyStruct, LRESULT * result );
	DECLARE_MESSAGE_MAP()
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnBnClickedDownloadLauncherZip();
	afx_msg void OnBnClickedUnzipLocalZip();
	afx_msg void OnBnClickedShowOfflineWebpage();
	afx_msg void OnBnClickedShowOnlineWebpage();
	afx_msg void OnBnClickedShowBlankWebpage();
};
