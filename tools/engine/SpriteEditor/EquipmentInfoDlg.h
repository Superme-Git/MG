#pragma once
#include "afxwin.h"

#include <string>
#include <vector>



class CEquipmentInfoDlg : public CDialog
{
	DECLARE_DYNAMIC(CEquipmentInfoDlg)

public:
	enum { IDD = IDD_DLG_EQUIPINFO };
	virtual ~CEquipmentInfoDlg();


	CEquipmentInfoDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	std::vector<std::wstring> m_Actions;
	CString m_ecType;
	virtual BOOL OnInitDialog();
	int m_Width;

	int m_Height;
	CString m_Name;
	CListBox m_lstActions;


protected:
	virtual void OnOK();
};
