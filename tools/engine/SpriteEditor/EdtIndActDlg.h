#pragma once
#include "afxcmn.h"
#include "afxwin.h"
#include <string>
#include <vector>

class CAnimationInfoDlg;



class CEdtIndActDlg : public CDialog
{
	DECLARE_DYNAMIC(CEdtIndActDlg)

public:
	enum { IDD = IDD_DLG_EDTINDACT };
	void DoSet();

	int m_CurEdt;
	std::vector<std::pair<std::wstring,int> > m_BaseActs;
	CAnimationInfoDlg *m_pInfoDlg;
	int m_nFrame;
	virtual ~CEdtIndActDlg();

	CEdtIndActDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
	virtual void OnOK();
public:
	afx_msg void OnBnClickedBtnSetind();
	afx_msg void OnCbnSelchangeCmbIndactname();
	CComboBox m_cmbIndFrame;
	CComboBox m_cmbIndAct;
	afx_msg void OnNMClickLstEdtindact(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnHdnItemdblclickLstEdtindact(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclkLstEdtindact(NMHDR *pNMHDR, LRESULT *pResult);
	CListCtrl m_lstEdtIndAct;
	virtual BOOL OnInitDialog();

private:
	CButton m_btnIncFrame;
	CButton m_btnCopyFrame;
	CButton m_btnCopyActionName;

public:
	afx_msg void OnBnClickedButton3();
	afx_msg void OnBnClickedButton2();
	afx_msg void OnBnClickedButton1();
};
