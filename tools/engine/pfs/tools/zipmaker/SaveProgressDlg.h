#pragma once
#include "afxcmn.h"
#include "afxwin.h"
#include "OperatorThread.h"

#define WM_OPERATOREND		(WM_USER+101)

class CSaveProgressDlg : public CDialog
{
	DECLARE_DYNAMIC(CSaveProgressDlg)

public:
	CSaveProgressDlg( bool bOpen, CWnd* pParent = NULL);   // standard constructor
	virtual ~CSaveProgressDlg();

// Dialog Data
	enum { IDD = IDD_SAVEPROGRESS };


	void SetOperators( COperator::COperatorVector& operators)
	{
		m_thread.SetOperators( operators);
	}

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

private:
	CProgressCtrl			m_progress;
	COperatorThread			m_thread;
	int						m_allfilecount;
	int						m_writtencount;
	float					m_scale;
	CSaveThreadListener		m_listener;
	bool					m_bOpen;
	virtual void OnCancel();

	HRESULT OnSaveProgress( WPARAM wParam, LPARAM lParam);
	BOOL OnInitDialog();
protected:
	virtual void OnOK();
public:
	CEdit m_editProgress;
	CEdit m_editFile;
};
