#pragma once
#include "afxwin.h"
#include "ScrollNotifyEdit.h"

// CCopyrightDlg dialog
class CCopyrightDlg : public CDialog
{
	DECLARE_DYNAMIC(CCopyrightDlg)

public:
	CCopyrightDlg( CWnd* pParent = NULL);   // standard constructor
	virtual ~CCopyrightDlg();

// Dialog Data
	enum { IDD = IDD_COPYRIGHT };

	BOOL Create( CWnd* parent) { return CDialog::Create( IDD_COPYRIGHT, parent); }
	void SetCopyrightText( void* buffer)
	{
		m_copyright.SetWindowText( reinterpret_cast<LPCTSTR>( buffer));
	}

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	afx_msg HRESULT	OnScrollNotify( WPARAM wParam, LPARAM lParam);

	DECLARE_MESSAGE_MAP()
	CScrollNotifyEdit	m_copyright;
	virtual void OnCancel();
	virtual void OnOK();

public:
	virtual BOOL OnInitDialog();
	
public:
	afx_msg void OnBnClickedAgree();
};
