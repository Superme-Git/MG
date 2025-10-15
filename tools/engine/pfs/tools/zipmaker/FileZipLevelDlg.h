#pragma once
#include "afxcmn.h"
#include "ComboListCtrl/ComboListCtrl.h"
#include <set>
#include <string>
#include "Project.h"
// CFileZipLevelDlg dialog

class CFileZipLevelDlg : public CDialog
{
	DECLARE_DYNAMIC(CFileZipLevelDlg)

public:
	CFileZipLevelDlg( CWnd* pParent = NULL);   // standard constructor
	virtual ~CFileZipLevelDlg();

	void SetChanged();
	void OpenProject( CProject* project);

// Dialog Data
	enum { IDD = IDD_FILEZIPLEVEL };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	DECLARE_MESSAGE_MAP()

	CComboListCtrl				m_lcFileZipLevel;
	CProject*					m_project;

public:
	virtual BOOL OnInitDialog();
	afx_msg LRESULT PopulateComboList(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam);
	afx_msg void OnBnClickedAddfilezip();
	afx_msg void OnBnClickedDelfilezip();
protected:
	virtual void OnOK();

	bool AddListItem( const std::wstring& sExt, int nZipLevel, int nSetupLevel);
	bool AddListItem( const std::wstring& sExt, const std::wstring& sZipDesc, const std::wstring& sSetupDesc);
public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
};
