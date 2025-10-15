// PVRUtilDlg.h : ͷ�ļ�
//

#pragma once
#include "afxwin.h"


// CPVRUtilDlg �Ի���
class CPVRUtilDlg : public CDialog
{
// ����
public:
	CPVRUtilDlg(CWnd* pParent = NULL);	// ��׼���캯��

// �Ի�������
	enum { IDD = IDD_PVRUTIL_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV ֧��


// ʵ��
protected:
	HICON m_hIcon;

	// ���ɵ���Ϣӳ�亯��
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	CStatic m_progress;
	afx_msg void OnBnClickedOk();
	CEdit m_message;
	afx_msg void OnTimer(UINT_PTR nIDEvent);

	afx_msg void OnBnClickedCheckToPvr();
	afx_msg void OnBnClickedCheckToAtitc();
	afx_msg void OnBnClickedCheckToDxt1();
	afx_msg void OnBnClickedCheckToEtc1();
	afx_msg void OnBnClickedCheckGenMipmaps();
	afx_msg void OnBnClickedSelectInputdir();
	afx_msg void OnBnClickedSelectOutputdir();
	afx_msg void OnBnClickedProcessing();


	afx_msg void OnBnClickedClearLog();
	afx_msg void OnBnClickedCheckToRgba8aa();
	afx_msg void OnBnClickedCheckToRgba8aas();
	afx_msg void OnBnClickedCheckMergeAs();
	afx_msg void OnBnClickedCheckToQualityFast();
	afx_msg void OnBnClickedCheckToQualityNormal();
	afx_msg void OnBnClickedCheckToQualityHigh();
	afx_msg void OnBnClickedCheckToQualityBest();
	afx_msg void OnBnClickedCheckToDxt1Alpha();
	afx_msg void OnBnClickedCheckToAtitcRgb();
	afx_msg void OnBnClickedCheckToAtitcRgbaI();
	afx_msg void OnBnClickedCheckToAtitcRgbaE();
	afx_msg void OnBnClickedCancel();
	afx_msg void OnBnClickedCheckOutputExtOrig();
	afx_msg void OnBnClickedCheckOutputExtPvr();
};
