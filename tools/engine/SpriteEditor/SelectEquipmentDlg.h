#pragma once
#include "afxwin.h"

#include <vector>
#include <string>



class CSelectEquipmentDlg : public CDialog
{
	DECLARE_DYNAMIC(CSelectEquipmentDlg)

public:
	enum { IDD = IDD_DLG_SELEQUIP };
	virtual ~CSelectEquipmentDlg();

	CSelectEquipmentDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()

public:
	std::vector<std::wstring> m_Equipments;
	std::wstring m_Selected;
	CListBox m_lstEquip;

	CString m_ecName;

protected:
	virtual void OnOK();

public:
	afx_msg void OnLbnDblclkLstEquip();
	virtual BOOL OnInitDialog();

};
