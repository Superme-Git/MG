#pragma once

#include <vector>
#include <string>



class CRenameActionDlg : public CDialog
{
	DECLARE_DYNAMIC(CRenameActionDlg)

public:
	enum { IDD = IDD_DLG_RENAMEACTION };
	virtual ~CRenameActionDlg();

	CRenameActionDlg(CWnd* pParent = NULL);   

protected:
	virtual void OnOK();
	DECLARE_MESSAGE_MAP()
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	std::vector<std::wstring> m_ActList;
	BOOL m_bRename;
	CString m_NewActName;
	CString m_OldActName;

};
