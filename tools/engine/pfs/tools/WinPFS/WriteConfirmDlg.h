#pragma once
#include <string>

// CWriteConfirmDlg dialog

class CWriteConfirmDlg : public CDialog
{
	DECLARE_DYNAMIC(CWriteConfirmDlg)

public:
	CWriteConfirmDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CWriteConfirmDlg();

// Dialog Data
	enum { IDD = IDD_WRITECONFIRM };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk2();
	afx_msg void OnBnClickedCancel2();
	CString m_tip;
};

extern int WriteConfirmBox( const CString& tip);