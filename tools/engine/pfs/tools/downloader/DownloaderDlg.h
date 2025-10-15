// DownloaderDlg.h : ͷ�ļ�
//

#pragma once

#include "DownloadTaskListCtrl.h"

// CDownloaderDlg �Ի���
class CDownloaderDlg : public CDialog
{
// ����
public:
	CDownloaderDlg(CWnd* pParent = NULL);	// ��׼���캯��
	~CDownloaderDlg();

// �Ի�������
	enum { IDD = IDD_DOWNLOADER_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV ֧��


// ʵ��
protected:
	HICON m_hIcon;

	CDownloadTaskListCtrl m_taskListCtrl;

	CWinThread* m_pDownloadThread;

	void RecalcLayout();

	// ���ɵ���Ϣӳ�亯��
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
