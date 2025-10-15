// CheckClientDlg.h : header file
//
#pragma once
#include "CheckThread.h"
#include "afxwin.h"
#include "afxcmn.h"
#include "../Patcher/GlobalNotification.h"

// CCheckClientDlg dialog
class CCheckClientDlg : public CDialog
{
// Construction
public:
	CCheckClientDlg(CWnd* pParent = NULL);	// standard constructor
	~CCheckClientDlg();

// Dialog Data
	enum { IDD = IDD_CHECKCLIENT_DIALOG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	virtual BOOL OnInitDialog();
	virtual void OnOK();
	virtual void OnCancel();
	bool StartCheck();
	void EnableButton(BOOL bEnable);
	bool StartCheckImpl();
	bool CheckNecessaryFiles();
	bool CheckOneNecessaryFile( const std::wstring& filename);
	void InsertCheckItems();

// Implementation
protected:
	HICON					m_hIcon;
	CCheckThread*			m_checkthread;
	CCheckThreadListener	m_listener;
	CWndWatcher				m_watch;
	CComboBox				m_cbServer;
	CEdit					m_editDownload;
	CRect					m_rcExpand;
	CStatic					m_staticGlobal;
	CStatic					m_staticFile;
	CProgressCtrl			m_progressCheck;
	BOOL					m_bResetConfig;
	BOOL					m_bCheckExeFile;
	CListCtrl				m_lcCheck;

protected:	
	// Generated message map functions
	DECLARE_MESSAGE_MAP()
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg void OnDestroy();
	afx_msg void OnBnClickedButtonCheck();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg LRESULT OnGlobalStatus( WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnProgressStart( WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnProgressStep( WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnProgressEnd( WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnReportResult( WPARAM wParam, LPARAM lParam);
public:
	afx_msg void OnNMRclickListCheck(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnChecklistSelectAll();
	afx_msg void OnChecklistRevertSelection();
};
