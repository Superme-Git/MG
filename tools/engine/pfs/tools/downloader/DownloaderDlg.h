// DownloaderDlg.h : 头文件
//

#pragma once

#include "DownloadTaskListCtrl.h"

// CDownloaderDlg 对话框
class CDownloaderDlg : public CDialog
{
// 构造
public:
	CDownloaderDlg(CWnd* pParent = NULL);	// 标准构造函数
	~CDownloaderDlg();

// 对话框数据
	enum { IDD = IDD_DOWNLOADER_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV 支持


// 实现
protected:
	HICON m_hIcon;

	CDownloadTaskListCtrl m_taskListCtrl;

	CWinThread* m_pDownloadThread;

	void RecalcLayout();

	// 生成的消息映射函数
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnWindowPosChanged(WINDOWPOS* lpwndpos);
	afx_msg void OnGetMinMaxInfo(MINMAXINFO* lpMMI);
protected:
	virtual void OnOK();
public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
};
