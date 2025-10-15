#pragma once
#include "afxwin.h"
#include "afxcmn.h"
#include "FolderTree.h"

// CFolderDlg dialog
class CFolderDlg : public CDialog
{
	DECLARE_DYNAMIC(CFolderDlg)

public:
	CFolderDlg( CWnd* pParent = NULL);   // standard constructor
	virtual ~CFolderDlg();

// Dialog Data
	enum { IDD = IDD_FOLDER };
	BOOL Create( CWnd* parent) { return CDialog::Create( IDD_FOLDER, parent); }

protected:
	

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	virtual void OnCancel();
	virtual void OnOK();
	afx_msg void OnBnClickedPrestep();

	CStatic			m_needspace;
	CStatic			m_freespace;
	CEdit			m_folder;
	CFolderTree		m_foldertree;
	size_t			m_selectspace;
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnTvnSelchangedTreeFolder(NMHDR *pNMHDR, LRESULT *pResult);
};
