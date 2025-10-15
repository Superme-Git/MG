#pragma once
#include "afxcmn.h"

#include <vector>
#include <map>
#include <string>
#include "afxwin.h"



class CAnimationRefManagerDlg : public CDialog
{
	DECLARE_DYNAMIC(CAnimationRefManagerDlg)

public:
	enum { IDD = IDD_DLG_ACTREFMAN };
	virtual ~CAnimationRefManagerDlg();


	CAnimationRefManagerDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	void Refresh();
	std::map<std::wstring,std::wstring> m_ActRefMap;
	std::vector<std::wstring> m_ActList;
	CListCtrl m_RefList;
protected:
	virtual void OnOK();
public:
	afx_msg void OnBnClickedBtnActrefDel();
	afx_msg void OnBnClickedBtnActrefEdt();
	afx_msg void OnNMClickLstActref(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedBtnActrefNew();
	CComboBox m_ActRefName2;
	CString m_ActRefName1;
	int m_CurEdt;
	virtual BOOL OnInitDialog();
};
