// LauncherDlg.h : ͷ�ļ�
//

#pragma once
#include "DIYButton.h"
#include "WebBrowserCtrl.h"
#include "ODProgressCtrl.h"
#include "LayoutManager.h"
#include "TrayIcon.h"

class CUpdateThread;

// CLauncherDlg �Ի���
class CLauncherDlg : public CDialog
{
// ����
public:
	CLauncherDlg(CWnd* pParent = NULL);	// ��׼���캯��
	~CLauncherDlg();

// �Ի�������
	enum { IDD = IDD_LAUNCHER_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV ֧��

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
	CUpdateThread* m_pUpdateThread;
	void* m_pThisWatcher;
	CTrayIcon m_trayIcon;

	HBRUSH m_hBKBrush;
	CStatic m_globalStatus;
	CStatic m_stepMessage;

	COLORREF m_globalStatusControlColor;// RGB(r,g,b)�͵���ɫֵ
	COLORREF m_stepMessageControlColor;	//

	// ���ɵ���Ϣӳ�亯��
	virtual BOOL OnInitDialog();
	virtual void OnCancel();
	virtual void OnOK();
	
	BOOL _TryStartUpdateThread();

	BOOL _TryStartDownloadWebPageThread();

protected:
	DECLARE_MESSAGE_MAP()
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg LRESULT OnTrayNotify(WPARAM wParam, LPARAM lParam);

	// ˢ��webҳ
	void ReNavigageWebPage();

	// ��ֹ������ռ�Ĺ�����
	void ForbidWebBrowseScrollBar();

public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnManualUpdate();
	afx_msg void OnRepairSetup();
	afx_msg void OnShowLauncher();
	afx_msg void OnDestroy();
	afx_msg void OnQuitApp();
	afx_msg void OnClose();
	afx_msg void OnBnClickedCheckUpdate();
	afx_msg void OnBnClickedLaunchGame();
	afx_msg void OnBnClickedMinimize();
	afx_msg BOOL OnToolTipNotify( UINT id, NMHDR * pNotifyStruct, LRESULT * result );
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg LRESULT OnNcHitTest(CPoint point);
	afx_msg LRESULT OnGlobalStatus(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnCheckStart(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnCheckStep(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnCheckEnd(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnProgressStart(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnProgressStep(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnProgressEnd(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnUpdateStart(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnUpdateStep(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnUpdateEnd(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnLauncherUpdateResult(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnDownloadWebPageEnd( WPARAM wParam, LPARAM lParam);
	afx_msg HBRUSH OnCtlColor(CDC* pDC, CWnd* pWnd, UINT nCtlColor);
};
