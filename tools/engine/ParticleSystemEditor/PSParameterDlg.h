#pragma once
#include "afxcmn.h"
#include "..\engine\particlesystem\ParticleSysCommon.h"

// CPSParameterDlg �Ի���

class CPSParameterDlg : public CDialog
{
	DECLARE_DYNAMIC(CPSParameterDlg)

public:
	CPSParameterDlg(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CPSParameterDlg();

// �Ի�������
	enum { IDD = IDD_DIALOG_PSPARAMETERDLG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();

private:
	int m_nPSlayer;
	Nuclear::PSParameter m_psParam;

public:
	void SetPSParameter(const Nuclear::PSParameter& param);
	const Nuclear::PSParameter& GetpsParameter() { return m_psParam; }

	BOOL GetPsCycleFlag() { return m_bPSCycle; }

private:
	afx_msg void OnEnChangeEditLayer();
	afx_msg void OnNMCustomdrawSliderposx(NMHDR *pNMHDR, LRESULT *pResult);
	virtual BOOL OnInitDialog();

private:
	CSliderCtrl m_sliderPSposx;
	CSliderCtrl m_sliderPSposy;
	CSliderCtrl m_sliderTrackObjectx;
	CSliderCtrl m_sliderTrackObjecty;

private:
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnBnClickedCheckCycle();

private:
	BOOL m_bPSCycle;
	bool m_bInitCtrlPos; //����Ԥ���ӵ�...
};
