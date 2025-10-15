#pragma once
// CCheckDlg dialog
#include "CheckProgressDlg.h"
#include "afxwin.h"

class CCheckDlg : public CDialog
{
	DECLARE_DYNAMIC(CCheckDlg)

public:
	CCheckDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CCheckDlg();

// Dialog Data
	enum { IDD = IDD_CHECK };

	BOOL Create( CWnd* parent) { return CDialog::Create( IDD_CHECK); }
	CCheckProgressDlg& GetCheckProgressDlg() { return m_progressdialog; }

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	CCheckProgressDlg	m_progressdialog;

	DECLARE_MESSAGE_MAP()
	virtual void OnCancel();
	virtual void OnOK();
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedPrevstep();
	afx_msg void OnPaint();
};
