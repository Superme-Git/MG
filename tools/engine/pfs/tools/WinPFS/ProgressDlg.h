#pragma once
#include "afxwin.h"
#include "afxcmn.h"


// CProgressDlg dialog

class CProgressDlg : public CDialog
{
	DECLARE_DYNAMIC(CProgressDlg)

public:
	CProgressDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CProgressDlg();

// Dialog Data
	enum { IDD = IDD_PROGRESS };

	afx_msg LRESULT OnThreadState( WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnThreadProgress( WPARAM wParam, LPARAM lParam);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	CStatic m_prompt;
	CProgressCtrl m_progress;
	DWORD			m_curPos;

protected:
	virtual void OnOK();
	virtual void OnCancel();
};
