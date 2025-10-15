#pragma once
#include <vector>
#include "afxwin.h"



class CSelectActionDlg : public CDialog
{
	DECLARE_DYNAMIC(CSelectActionDlg)
private:
	CListBox m_ActList;
public:
	std::vector<size_t> m_SelectedActions;
	std::vector<std::wstring> m_Actions;
	enum { IDD = IDD_DLG_SELECT_ACTION };
	virtual ~CSelectActionDlg();

	CSelectActionDlg(CWnd* pParent = NULL);   

protected:
	virtual void OnOK();
	DECLARE_MESSAGE_MAP()
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	virtual BOOL OnInitDialog();

};
