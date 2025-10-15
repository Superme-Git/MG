#pragma once


// CAddSetupLevelDlg dialog

class CAddSetupLevelDlg : public CDialog
{
	DECLARE_DYNAMIC(CAddSetupLevelDlg)

public:
	CAddSetupLevelDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CAddSetupLevelDlg();

// Dialog Data
	enum { IDD = IDD_ADDSETUPLEVEL };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
	CEdit		m_editFileExt;
	CString		m_strExt;
	CComboBox	m_cbIsPath;
	CComboBox	m_cbApplyChild;
	CString		m_IsPath;
	CString		m_ApplyChild;
protected:
	virtual void OnOK();
};
