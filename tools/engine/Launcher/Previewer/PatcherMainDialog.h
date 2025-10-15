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
	
// ʵ��
protected:
	HICON m_hIcon;
	HICON m_hIconLarge;
	HICON m_hIconSmall;

	CDIYButton m_btnCheckUpdate;	// ������
	CDIYButton m_btnManualUpdate;   // �ֶ�����
	CDIYButton m_btnRepairSetup;    // �汾��֤
	CDIYButton m_btnLaunchGame;     // ������Ϸ
	CDIYButton m_btnMinimize;		// ��С��
	CDIYButton m_btnClose;			// �ر�

	CWebBrowserCtrl* m_htmlView;
	CODProgressCtrl m_downloadPgrsCtrl;
	CODProgressCtrl m_updatePgrsCtrl;
	CLayoutManager m_layoutManager;
	
	HBRUSH m_hBKBrush;

	CStatic m_globalStatus;
	CStatic m_stepMessage;

	COLORREF m_globalStatusControlColor;// RGB(r,g,b)�͵���ɫֵ
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
