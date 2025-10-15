#pragma once
#include "resource.h"
#include "DIYButton.h"
#include "ODDropListCtrl.h"
#include "DIYEditCtrl.h"

namespace PFSX
{
	class CVersionInfo;
}

class SetDownloadSvrDlg : public CDialog
{
	DECLARE_DYNAMIC(SetDownloadSvrDlg)
public:
	SetDownloadSvrDlg(const PFSX::CVersionInfo& rVersionInfo, CWnd* pParent = NULL);
	virtual ~SetDownloadSvrDlg();

	int GetSelectServerType()
	{
		return m_dropList.GetCurSel();
	}

	void SetSelectServerType( int type)
	{
		m_dropList.SetCurSel( type);
	}

	BOOL UseP2P()
	{
		return m_selP2P.GetCheck();
	}

	void SetP2P(BOOL p2p)
	{
		m_bP2P = p2p;
		if(this->m_hWnd != NULL)
		{
			if (m_bP2P)
				OnBtnClickedOptP2P();
			else
				OnBtnClickedOptSvr();
		}
	}

	int GetLowerLimit()
	{	
		return _ttoi(m_iLowerVal);
	}

	int GetUpperLimit()
	{
		return _ttoi(m_iUpperVal);
	}

	void SetLowerLimit(int val)
	{
		TCHAR temp[32] = {'\0'};
		_itot(val, temp, 10);
		m_iLowerVal.SetString(temp);
		if(this->m_hWnd != NULL)
			m_editDown.SetWindowText(m_iLowerVal);
	}

	void SetUpperLimit(int val)
	{
		TCHAR temp[32] = {'\0'};
		_itot(val, temp, 10);
		m_iUpperVal.SetString(temp);
		if(this->m_hWnd != NULL)
			m_editUp.SetWindowText(m_iUpperVal);
	}

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	CLayoutManager m_layoutManager;
	CDIYButton m_btnOK;
	CDIYButton m_btnCancel;
	CDIYButton m_btnDroplist;
	CDIYButton m_selSvr;
	CDIYButton m_selP2P;
		CFont font;
	CDIYEditCtrl m_editUp;
	CDIYEditCtrl m_editDown;

	CODDropListCtrl m_dropList;
	CRect m_rc;
	CRect m_rcDropList;		// DropList显示位置
	DWORD m_clrCombo;			// Combo显示文字颜色

	CString m_iLowerVal;
	CString m_iUpperVal;
	BOOL m_bP2P;

	const PFSX::CVersionInfo& m_rVersionInfo; // versionInfo

	DECLARE_MESSAGE_MAP()

public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnPaint();
	virtual BOOL OnInitDialog();
	afx_msg void OnBtnClickedOk();
	afx_msg void OnBtnClickedCancel();
	afx_msg void OnBtnClickedCbx();
	afx_msg void OnBtnClickedOptSvr();
	afx_msg void OnBtnClickedOptP2P();
	afx_msg BOOL OnToolTipNotify( UINT id, NMHDR * pNotifyStruct, LRESULT * result );
	afx_msg LRESULT OnNcHitTest(CPoint point);
	afx_msg LRESULT OnItemChanged( WPARAM wParam, LPARAM lParam);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnNcLButtonDown(UINT nHitTest, CPoint point);
};