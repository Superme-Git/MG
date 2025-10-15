#pragma once
#include "afxwin.h"
#include "afxcmn.h"
#include "MakeHashTableThread.h"

// CMakeHashTableDlg dialog

class CMakeHashTableDlg : public CDialog
{
	DECLARE_DYNAMIC(CMakeHashTableDlg)

public:
	CMakeHashTableDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CMakeHashTableDlg();

// Dialog Data
	enum { IDD = IDD_MAKEHASHTABLE };

	void SetDocument( CWinPFSDoc* doc) { m_doc = doc; }

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

	afx_msg LRESULT OnCheckResult( WPARAM wParam, LPARAM lParam);

public:
	CEdit m_editName;
	CButton m_btnOuputFileName;
	CListCtrl m_list;
	std::wstring m_pfsName;

	CWinPFSDoc*			m_doc;
	CMakeHashTableThread*	m_thread;	
	virtual void OnOK();
	virtual BOOL OnInitDialog();

protected:
	virtual void OnCancel();
};
