#pragma once
#include "afxwin.h"
#include "PatchMetaFileDoc.h"

// CAddVersionDlg dialog

class CAddVersionDlg : public CDialog
{
	DECLARE_DYNAMIC(CAddVersionDlg)

public:
	CAddVersionDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CAddVersionDlg();

	void SetDocument( CPatchMetaFileDoc* doc, DWORD dwDestVersion )
	{
		m_doc = doc;
		m_dwDestVersion = dwDestVersion;
	}


// Dialog Data
	enum { IDD = IDD_DIALOG_ADDVERSION };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

	virtual void OnOK();

	// 刷新补丁包列表
	void OnRefreshPatchList( const CString& src, const CString& des);

public:
	virtual BOOL OnInitDialog();
	CEdit m_editSrc;
	CEdit m_editDes;
	CComboBox m_cbPatch;

	CString m_srcInit;
	CString m_desInit;

	CString m_src;
	CString m_des;
	CString m_patchs;
	afx_msg void OnEnChangeEditDes();
	afx_msg void OnEnChangeEditSrc();

	CPatchMetaFileDoc*		m_doc;
	DWORD m_dwDestVersion;
};
