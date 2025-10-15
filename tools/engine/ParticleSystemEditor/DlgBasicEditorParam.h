#pragma once
#include "afxwin.h"
#include "afxcmn.h"

// CDlgBasicEditorParam �Ի���

class CDlgBasicEditorParam : public CDialog
{
	DECLARE_DYNAMIC(CDlgBasicEditorParam)

public:
	CDlgBasicEditorParam(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CDlgBasicEditorParam();

// �Ի�������
	enum { IDD = IDD_DIALOG_BasicSysParam };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()

public:
	CDocument* GetEditorDoc();
	void SetCtrlPos();
	void InitCtrl();

	void UpdateBaseCtrlState(bool bMoveMap, bool bSetParticleHoleFlag, bool bSetEmitterSpinCentFlag);
	
	//�򿪲����ļ���ʱ���ʼ���ؼ�������
	void SetPsParam(float fEmitterSpinAngle, int nLineLength, int nRctWidth, int nRctHeight);
public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	virtual BOOL OnInitDialog();

	BOOL m_bSetMaxBox;
	BOOL m_bCycleFlag;
	BOOL m_bShowBKpic;
	BOOL m_bSetEmitterSpinCentFlag;
	BOOL m_bSetParticleHoleFlag;
	BOOL m_bMoveMap;
	BOOL m_bBlindArea;
	BOOL m_bBlockLine;
	BOOL m_bReflectLine;
	BOOL m_bInterfacePsl;
	BOOL m_bSkillEffectSpritePoint;

	float m_fEmitterSpinAngle; //�Ƕ�
	int m_nLineLength;
	int m_nRctWidth;
	int m_nRctHeight;

	CSliderCtrl m_sliderLineLength;
	CSliderCtrl m_sliderRctWidth;
	CSliderCtrl m_sliderRctHeight;

	afx_msg void OnBnClickedCheckMaxboxflag();
	afx_msg void OnBnClickedCheckCycle();
	afx_msg void OnBnClickedCheckShowbkpic();
	afx_msg void OnEnKillfocusEditEmitterangle();
	afx_msg void OnBnClickedCheckSetemitterspincenter();
	afx_msg void OnBnClickedCheckSetparticlehole();
	afx_msg void OnBnClickedCheckMovebkpic();
	afx_msg void OnBnClickedCheckBlindarea();
	afx_msg void OnBnClickedCheckBlockline();
	afx_msg void OnBnClickedCheckReflectline();
	afx_msg void OnBnClickedCheckskilleffectbottom();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnEnKillFocus(UINT nID);

	afx_msg void OnBnClickedButtonOpenparamfile();
	afx_msg void OnBnClickedButtonSaveparamfile();
	afx_msg void OnBnClickedCheckInterfacialeffect();

private:
	void SetParams();

public:
	afx_msg void OnEnChangeEditRctheight();
	afx_msg void OnEnKillfocusEditTexturerow();
	afx_msg void OnEnKillfocusEditTexturecol();
	// //��ʼֵ
	int m_TextureRow_Count;
	// //ͼƬ����
	int m_TextureCol_Count;
	afx_msg void OnEnKillfocusEditTexturecount();
	// //��������
	int m_Texture_Count;
	afx_msg void OnBnClickedButtonFromtexturelistfindpsl();
};