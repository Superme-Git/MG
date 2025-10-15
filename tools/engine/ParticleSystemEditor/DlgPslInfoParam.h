#pragma once


// CDlgPslInfoParam �Ի���

class CDlgPslInfoParam : public CDialog
{
	DECLARE_DYNAMIC(CDlgPslInfoParam)

public:
	CDlgPslInfoParam(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CDlgPslInfoParam();

// �Ի�������
	enum { IDD = IDD_DIALOG_SetPSLinfoParam };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()

public:
	int m_nMaxParticlesThreshold;
	BOOL m_bMaxParticles;
	BOOL m_bFullScreen;
	BOOL m_bSpecialPS;
	BOOL m_bMultTexture;
	BOOL m_bCustomPath;
	BOOL m_bAlphaADD;

public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	afx_msg void OnEnKillfocusEditMaxparticles();
	afx_msg void OnBnClickedCheckMaxparticles();
	afx_msg void OnBnClickedCheckFullscreen();
	afx_msg void OnBnClickedCheckSpecialps();
	afx_msg void OnBnClickedCheckMulttextures();
	afx_msg void OnBnClickedCheckCustompath();
	afx_msg void OnBnClickedCheckAlphaadd();
};
