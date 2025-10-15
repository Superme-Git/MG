#pragma once
#include "afxwin.h"
#include "GlobalInfo.h"

// CAddFileZipLevelDlg dialog

class CAddFileZipLevelDlg : public CDialog
{
	DECLARE_DYNAMIC(CAddFileZipLevelDlg)

public:
	CAddFileZipLevelDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CAddFileZipLevelDlg();

// Dialog Data
	enum { IDD = IDD_ADDZIPLEVEL };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	virtual void OnOK();
	void AddLevels( CAttributeDesc::CAttributeDescMap& map, CComboBox& box);

public:
	virtual BOOL OnInitDialog();
public:
	CEdit	m_editFileExt;
	CString m_strExt;
	CComboBox m_ziplevel;
	CComboBox m_setuplevel;
	CString m_zipdesc;
	CString m_setupdesc;
};
