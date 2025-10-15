#pragma once

#include <string>
#include "afxwin.h"



class CPackPropertyDlg : public CDialog
{
	DECLARE_DYNAMIC(CPackPropertyDlg)

	bool m_bSkip;
	bool m_bCancel;
	
public:
	enum { IDD = IDD_DLG_PACKPRO };
	void ClearCancel() { m_bCancel = false; }


	void SetCancel() { m_bCancel = true; }
	void ClearSkip() { m_bSkip = false; }
	void SetSkip() { m_bSkip = true; }
	bool IsCancel() const { return m_bCancel; }
	bool IsSkip() const { return m_bSkip; }
	void Clear();

	void Finish();
	void AddMsg(const wchar_t *msg);
	void SetCurDir(const std::wstring &path);
	void Init(const std::wstring &path);
	int m_state; 

	virtual ~CPackPropertyDlg();

	CPackPropertyDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
	virtual void OnOK();
public:
	CListBox m_lstPackMsg;
	afx_msg void OnBnClickedBtnSkippack();
	afx_msg void OnBnClickedBtnPackpro();
	virtual BOOL OnInitDialog();

protected:
	virtual void OnCancel();
};
