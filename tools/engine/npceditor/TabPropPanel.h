#pragma once
#include "afxcmn.h"
#include "MiniMapPanel.h"
#include "NpcPropertyDlg.h"
#include "JumpPointInfDlg.h"
//2015 Yao
#include "CMonsterPanel.h"

// CTabPropPanel dialog
class CPropView;
class CTabPropPanel : public CDialog
{
	DECLARE_DYNAMIC(CTabPropPanel)
//自定义类型、枚举
public:
	// Dialog Data
	enum { IDD = IDD_TAB_PROP_PANEL };
	CNpcPropertyDlg* m_pdlgNpcPropertyDlg;
	CJumpPointInfDlg* m_pJPInfDlg; //跳转点信息面板
	CMonsterPanel* m_pMonPanel;


//成员属性
private:
	CTabCtrl m_Tab;
	CPropView *m_pView;
	CMiniMapPanel m_miniMapPanel;
	bool m_bIsInited;
	

//方法们
public:
	CTabPropPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~CTabPropPanel();

	void ResetCont();
	void ResetSize();



protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

public:
	virtual BOOL OnInitDialog();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnTcnSelchangePropTab(NMHDR *pNMHDR, LRESULT *pResult);
	virtual void OnOK() {}
	virtual void OnCancel() {}
	afx_msg void OnBnClickedButtonJumpisland();
};
