#pragma once

#include "DIYButton.h"
#include "WebBrowserCtrl.h"
#include "ODProgressCtrl.h"
#include "LayoutManager.h"
//#include "TrayIcon.h"

// CPatcherMainDialog dialog

class CPatcherMainDialog : public CDialog
{
	DECLARE_DYNAMIC(CPatcherMainDialog)

public:
	CPatcherMainDialog(CWnd* pParent = NULL);   // standard constructor
	virtual ~CPatcherMainDialog();

// Dialog Data
	enum { IDD = IDD_PATCHERMAINDIALOG };
protected:
	
// 实现
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
	
	HBRUSH m_hBKBrush;

	CStatic m_globalStatus;
	CStatic m_stepMessage;

	COLORREF m_globalStatusControlColor;// RGB(r,g,b)型的颜色值
	COLORREF m_stepMessageControlColor;	//

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual BOOL OnInitDialog();

	DECLARE_MESSAGE_MAP()
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnPaint();
	afx_msg void OnManualUpdate();
	afx_msg void OnRepairSetup();
	afx_msg void OnDestroy();
	afx_msg void OnClose();
	afx_msg void OnBnClickedCheckUpdate();
	afx_msg void OnBnClickedLaunchGame();
	afx_msg void OnBnClickedMinimize();
	afx_msg BOOL OnToolTipNotify( UINT id, NMHDR * pNotifyStruct, LRESULT * result );
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg LRESULT OnNcHitTest(CPoint point);
	afx_msg HBRUSH OnCtlColor(CDC* pDC, CWnd* pWnd, UINT nCtlColor);
};
