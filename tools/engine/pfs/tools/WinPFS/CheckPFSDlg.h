#pragma once
#include "afxcmn.h"
#include "CheckPFSThread.h"

// CCheckPFSDlg dialog

class CCheckPFSDlg : public CDialog
{
	DECLARE_DYNAMIC(CCheckPFSDlg)

public:
	CCheckPFSDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CCheckPFSDlg();

// Dialog Data
	enum { IDD = IDD_CHECKPFS };

	void SetDocument( CWinPFSDoc* doc) { m_doc = doc; }

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

public:
	CListCtrl m_list;
	virtual BOOL OnInitDialog();
	afx_msg LRESULT OnCheckResult( WPARAM wParam, LPARAM lParam);

protected:
	virtual void OnOK();
	virtual void OnCancel();

	CWinPFSDoc*			m_doc;
	CCheckPFSThread*	m_thread;		
};
