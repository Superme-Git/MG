#pragma once
#include "afxcmn.h"
#include "ComboListCtrl/ComboListCtrl.h"
#include "Project.h"
#include "afxwin.h"
#include "afxdtctl.h"

// CEditVersionDlg dialog

class CEditVersionDlg : public CDialog
{
	DECLARE_DYNAMIC(CEditVersionDlg)

public:
	CEditVersionDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CEditVersionDlg();

	void OpenProject( CProject* project);

// Dialog Data
	enum { IDD = IDD_EDITVERSION };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

public:
	CComboListCtrl		m_list;
	CEdit				m_version;
	CEdit				m_luv;
	CProject*			m_project;
	bool				m_changed;

	virtual BOOL OnInitDialog();
	bool AddVersionMeta( int type, LPCWSTR lpszUrl, LPCWSTR lpszNetwork);
	afx_msg void OnBnClickedButton1();
	afx_msg void OnBnClickedButton2();
	afx_msg LRESULT OnEndLabelEditVersionList(WPARAM wParam, LPARAM lParam);
	LRESULT PopulateComboList(WPARAM wParam, LPARAM lParam);

protected:
	virtual void OnOK();
public:
	afx_msg void OnEnChangeEdit1();
protected:
	virtual void OnCancel();
public:
	CDateTimeCtrl m_time;
};
