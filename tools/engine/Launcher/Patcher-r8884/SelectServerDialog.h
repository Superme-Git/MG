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
	CDIYButton			m_btnOpenWebpage;	// �򿪸��°�����ҳ��
	CDIYButton			m_btnOk;			// ȷ��
	CDIYButton			m_btnCancel;		// ȡ��
	CDIYButton			m_btnDroplist;		// ����������ť
	
	CODDropListCtrl		m_lcDrop;			// ������

	CRect				m_rcCombo;			// Combo��ʾ����Rect
	DWORD				m_clrCombo;			// Combo��ʾ������ɫ

	CRect				m_rcDropList;		// DropList��ʾλ��

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
