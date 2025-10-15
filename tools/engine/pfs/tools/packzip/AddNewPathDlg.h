#pragma once
#include "afxwin.h"
#include <string>

// CAddNewPathDlg dialog

class CAddNewPathDlg : public CDialog
{
	DECLARE_DYNAMIC(CAddNewPathDlg)

public:
	CAddNewPathDlg( CWnd* pParent = NULL, bool bEditUrl = false);   // standard constructor
	virtual ~CAddNewPathDlg();

// Dialog Data
	enum { IDD = IDD_ADDNEWPATH };

	void SetUrl( const std::wstring& url)
	{
		m_path = url.c_str();
	}

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	virtual void OnOK();

public:
	virtual BOOL OnInitDialog();
	CEdit	m_editPath;
	bool	m_bEditUrl;
	CString m_path;
};
