#pragma once

#include "resource.h"

class CMainDlg;


// CJumpPointInfDlg dialog

class CJumpPointInfDlg : public CDialog
{
	DECLARE_DYNAMIC(CJumpPointInfDlg)

public:
	CJumpPointInfDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CJumpPointInfDlg();

	void ChangeJPInf(int mapID,int dest_X,int dest_Y);

	void JumpIsland(int idx); //跳到某一个孤岛的第一个坐标

	CMainDlg* m_pMainDlg;

// Dialog Data
	enum { IDD = IDD_DIALOG_JPINF };
	

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual BOOL OnInitDialog();

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedJPInfOK();
	afx_msg void OnBnClickedDeljump();
	afx_msg void OnBnClickedAddjump();
	afx_msg void OnBnClickedSavejpinf();
};
