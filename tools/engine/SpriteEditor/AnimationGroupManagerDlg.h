#pragma once
#include "afxwin.h"

#include "..\engine\common\groupmanager.h"



class CAnimationGroupManagerDlg : public CDialog
{
	DECLARE_DYNAMIC(CAnimationGroupManagerDlg)

public:
	void UpdateMemberList(const std::wstring &g);
	void UpdateGroupList();
	std::wstring m_CurGroupName;

	Nuclear::GroupManager *m_pGroupMgr;

	std::vector<std::wstring> m_GroupNames;
	std::vector<std::wstring> m_AnimationNames;
	enum { IDD = IDD_DLG_ACTGROUPMAN };
	virtual ~CAnimationGroupManagerDlg();


	CAnimationGroupManagerDlg(Nuclear::GroupManager *pGroupMan, CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedBtnDeleteAnimationMember();
	afx_msg void OnBnClickedBtnAddAnimationMember();
	CListBox m_ListBoxAnimationMembersCan;
	CListBox m_ListBoxAnimationMembers;
	afx_msg void OnLbnDblclkListBoxAnimationGroups();
	afx_msg void OnBnClickedBtnEditAnimationGroup();
	virtual void OnOK();
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedBtnAddAnimationGroup();
	CString m_edtActGroupName;
	afx_msg void OnBnClickedBtnDeleteAnimationGroup();
	CListBox m_ListBoxAnimationGroups;
};
