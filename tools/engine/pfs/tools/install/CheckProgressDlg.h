#pragma once
#include "afxcmn.h"
#include "CheckThread.h"
// CCheckProgressDlg dialog

class CCheckProgressDlg : public CDialog
{
	DECLARE_DYNAMIC(CCheckProgressDlg)

public:
	CCheckProgressDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CCheckProgressDlg();

// Dialog Data
	enum { IDD = IDD_CHECKPROGRESS };
	BOOL Create( CWnd* parent) { return CDialog::Create( IDD_CHECKPROGRESS, parent); }

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	
	DECLARE_MESSAGE_MAP()

	HRESULT OnCheckProgress( WPARAM wParam, LPARAM lParam);

	void OnCheckEnd();

public:
	CProgressCtrl	m_progress;
	float			m_scale;
	size_t			m_filewritten;

	CCheckThread	m_checkthread;
	CProgressThreadListener		m_listener;

public:
	virtual BOOL OnInitDialog();
protected:
	virtual void OnCancel();
	virtual void OnOK();
};
