#pragma once
#include "afxcmn.h"
#include "TestPanel.h"
#include "EnvPanel.h"
#include "BkgPanel.h"
#include "BkgVolPanel.h"
#include "StepPanel.h"
#include "MiniMapPanel.h"

// CTabPropPanel dialog
class CPropView;
class CTabPropPanel : public CDialog
{
	DECLARE_DYNAMIC(CTabPropPanel)
//自定义类型、枚举
public:
	// Dialog Data
	enum { IDD = IDD_TAB_PROP_PANEL };

//成员属性
private:
	CTabCtrl m_Tab;
	CPropView *m_pView;
	CTestPanel m_testPanel;
	CEnvPanel m_envPanel;
	CBkgPanel m_bkgPanel;
	CBkgVolPanel m_bkgVolPanel;
	CStepPanel m_stepPanel;
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
};
