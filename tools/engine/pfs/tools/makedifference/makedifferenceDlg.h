// makedifferenceDlg.h : header file
//

#pragma once
#include "afxwin.h"
#include "afxcmn.h"

#include "MakeDifferenceThread.h"
#include "MyThreadListener.h"
#include "BrowseLastPfsPathThread.h"
#include "MergePatchThread.h"

// CMakeDifferenceDlg dialog
class CMakeDifferenceDlg : public CDialog
{
// Construction
public:
	CMakeDifferenceDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	enum { IDD = IDD_MAKEDIFFERENCE_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;
	CMakeDifferenceThread		m_makedifferencethread;
	CMergePatchThread			m_mergePatchThread;
	CMyThreadListener			m_makedifferencelistener;

	CBrowseLastPfsPathThread	m_browselastpfspaththread;
	CMyThreadListener			m_browselastpfspathlistener;

	float						m_scale;
	int							m_allcount;
	int							m_savecount;

	std::wstring				m_lastpfspath;
	std::wstring				m_savepath;

	void EnableBrowseButton( BOOL enable);
	void EnableButton( BOOL enable);
	bool StopMakeDifferenceThread();
	void SetProgressEditText();
	void BrowseLastPfsPath();

	// 生成差异包时检查旧版本安装包是否符合要求
	bool CheckAllSetupPfs();

	// 合并差异包时检查所有选择的差异包是否符合要求
	bool CheckAllPatch();

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()

public:
	CEdit m_editLastPfs;
	CButton m_btnLastPfs;
	CEdit m_editCurPfs;
	CButton m_btnCurPfs;
	CProgressCtrl m_pcProgress;
	CEdit m_editInfo;
	CButton m_btnOk;
	CButton m_btnCancel;
	CEdit m_editProgress;
	CListCtrl m_lcLastPfs;
	CEdit m_editSavePath;
	CButton m_btnSavePath;
	CButton m_btnDeleteLastPfs;
	CButton m_cbMakeServer;
	CButton m_cbIsZip;
	CComboBox m_cbbExtension;

	bool		m_bMergePatch;		// 是否是合并差异包

	afx_msg void OnBnClickedButton1();
	afx_msg void OnBnClickedButton2();
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedButton3();
	afx_msg void OnBnClickedButton4();
	afx_msg void OnBnClickedButton5();

	afx_msg HRESULT OnMakeProgress( WPARAM wParam, LPARAM lParam);
	afx_msg HRESULT OnBrowseLastPfsPath( WPARAM wParam, LPARAM lParam);
	
protected:
	virtual void OnCancel();

};
