#pragma once
#include "afxcmn.h"
#include <vector>
#include <string>

// CErrorFileDlg dialog

class CErrorFileDlg : public CDialog
{
	DECLARE_DYNAMIC(CErrorFileDlg)

public:
	CErrorFileDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CErrorFileDlg();

// Dialog Data
	enum { IDD = IDD_ERRORFILE };
	void InsertFile( LPCTSTR file)
	{
		m_files.InsertItem( m_files.GetItemCount(), file);
	}
	void Clear()
	{
		m_files.DeleteAllItems();
	}
	void Show()
	{
		if( m_files.GetItemCount() > 0)
			ShowWindow( SW_SHOW);
	}

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	CListCtrl m_files;
public:
	virtual BOOL OnInitDialog();
};
