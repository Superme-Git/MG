#pragma once
// CProgressDlg dialog
#include "InstallThread.h"

class CProgressDlg : public CDialog 
{
	DECLARE_DYNAMIC(CProgressDlg)

public:
	CProgressDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CProgressDlg();

// Dialog Data
	enum { IDD = IDD_PROGRESS };

	BOOL Create( CWnd* parent) { return CDialog::Create( IDD_PROGRESS, parent); }

	afx_msg LRESULT OnInstallProgress( WPARAM wParam, LPARAM lParam);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	void OnEnd();

	virtual BOOL OnInitDialog();
	virtual void OnCancel();
	virtual void OnOK();

	DECLARE_MESSAGE_MAP()

	CStatic				m_filename;
	CProgressCtrl		m_fileprogress;
	CStatic				m_totalname;
	CProgressCtrl		m_totalprogress;

	float				m_filescale;
	float				m_filewritten;

	float				m_totalscale;
	float				m_totalwritten;
};
