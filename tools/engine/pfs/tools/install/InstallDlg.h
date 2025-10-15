// installDlg.h : header file
//
#pragma once
#include <vector>
#include <string>
#include "../../pfsutility.h"


class CInstallState;
class CInstallDlg : public CDialog
{
// Construction
public:
	CInstallDlg(CWnd* pParent = NULL);	// standard constructor
	~CInstallDlg();

// Dialog Data
	enum { IDD = IDD_INSTALL_DIALOG };
	
	void ChangeState( CInstallState& state);

	void SetInstallPath( const std::wstring& path) { m_installpath = path; }
	const std::wstring& GetInstallPath() { return m_installpath; }

protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support

	void GetAppPath();
	
	bool Init();

// Implementation
protected:
	HICON m_hIcon;
	std::wstring				m_apppath;
	
	size_t						m_index;
	CInstallState*				m_curstate;

	std::wstring				m_installpath;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	virtual void OnOK();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnTimer(UINT_PTR nIDEvent);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
protected:
	virtual void OnCancel();
public:
	afx_msg LRESULT OnNcHitTest(CPoint point);
	afx_msg void OnLButtonDblClk(UINT nFlags, CPoint point);
	afx_msg void OnNcLButtonDblClk(UINT nHitTest, CPoint point);
};
