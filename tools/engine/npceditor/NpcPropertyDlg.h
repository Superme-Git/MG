#pragma once

#include "resource.h"
#include "Npc.h"
//#include "MainDlg.h"
#include "AddNpcDlg.h"

// CNpcPropertyDlg dialog

class CMainDlg;
class CAddNpcDlg;
class CNewNpcBaseDlg;

class CNpcPropertyDlg : public CDialog
{
	DECLARE_DYNAMIC(CNpcPropertyDlg)

public:
	CNpcPropertyDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CNpcPropertyDlg();
	void ChangeSelNpc(CNpc* pNpc);
	void ChangeSelNpcPos(int x, int y);
	void Reset();

	
	
// Dialog Data
	enum { IDD = IDD_NPCPANEL };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual BOOL OnInitDialog();
	
	//CNpc* m_pCurSelNpc;
	//CEdit* m_pBaseID;
	//CEdit* m_pModeName;
	//CEdit* m_pName;
	//CEdit* m_pXpos;
	//CEdit* m_pYpos;
    CMainDlg* m_pMainDlg;
	CAddNpcDlg m_AddNpcDlg;

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedButtonAddnpc();
	afx_msg void OnBnClickedButtonNewnpc();
	afx_msg void OnBnClickedNpcOk();
	afx_msg void OnBnClickedNpcSave();
	afx_msg void OnBnClickedNpcCancel();
};
