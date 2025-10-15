// uninstallDlg.h : header file
//

#pragma once
#include "UninstallThread.h"
#include "afxcmn.h"
#include "afxwin.h"

// CuninstallDlg dialog
class CuninstallDlg : public CDialog
{
// Construction
public:
	CuninstallDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	enum { IDD = IDD_UNINSTALL_DIALOG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;

	CUninstallThread			m_thread;
	CUninstallThreadListener	m_listener;
	std::wstring				m_path;
	std::wstring				m_appname;

	bool						m_success;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
	virtual void OnOK();

	// �������Ƿ�����
	bool CheckRunProgram( LPCTSTR pszClassName, LPCTSTR pszProgramName);

	// ���patcher�Ƿ�������
	bool CheckPatcherProgram();

	// �����Ϸ�ͻ����Ƿ�������
	bool CheckClientProgram();

	// �õ���Ϸ�ͻ�������
	bool GetClientProgramName( std::wstring& programe);

public:
	afx_msg void OnDestroy();
	afx_msg HRESULT OnUninstallProgress( WPARAM wParam, LPARAM lParam);
public:
	CProgressCtrl m_progress;
	CStatic m_prompt;
public:
	BOOL m_bDeleteAll;
};
