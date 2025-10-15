#pragma once

#include "resource.h"
#include "LayoutManager.h"
#include "DIYButton.h"
#include "ODDropListCtrl.h"

// CSelectServerDialog dialog
namespace PFSX
{
	class CVersionInfo;
}

class CSelectServerDialog : public CDialog
{
	DECLARE_DYNAMIC(CSelectServerDialog)

public:
	CSelectServerDialog(const PFSX::CVersionInfo& rVersionInfo, CWnd* pParent = NULL);   // standard constructor
	virtual ~CSelectServerDialog();

// Dialog Data
	enum { IDD = IDD_SELECT_SERVER_DIALOG };

	int GetSelectServerType()
	{
		return m_lcDrop.GetCurSel();
	}

	void SetSelectServerType( int type)
	{
		m_lcDrop.SetCurSel( type);
	}

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

	CLayoutManager		m_layoutManager;
	CDIYButton			m_btnOpenWebpage;	// 打开更新包下载页面
	CDIYButton			m_btnOk;			// 确认
	CDIYButton			m_btnCancel;		// 取消
	CDIYButton			m_btnDroplist;		// 弹出下拉框按钮
	
	CODDropListCtrl		m_lcDrop;			// 下拉框

	CRect				m_rcCombo;			// Combo显示文字Rect
	DWORD				m_clrCombo;			// Combo显示文字颜色

	CRect				m_rcDropList;		// DropList显示位置

	const PFSX::CVersionInfo& m_rVersionInfo; // versionInfo

public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	afx_msg void OnBnClickedCombobutton();
	afx_msg LRESULT OnNcHitTest(CPoint point);
	afx_msg LRESULT OnItemChanged( WPARAM wParam, LPARAM lParam);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnNcLButtonDown(UINT nHitTest, CPoint point);
	afx_msg void OnBnClickedOpenWebpage();
	afx_msg BOOL OnToolTipNotify( UINT id, NMHDR * pNotifyStruct, LRESULT * result );
};
