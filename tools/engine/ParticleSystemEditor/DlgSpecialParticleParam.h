#pragma once
#include "afxcmn.h"


// CDlgSpecialParticleParam �Ի���

class CDlgSpecialParticleParam : public CDialog
{
	DECLARE_DYNAMIC(CDlgSpecialParticleParam)

public:
	CDlgSpecialParticleParam(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CDlgSpecialParticleParam();

// �Ի�������
	enum { IDD = IDD_DIALOG_SpecialParticleParam };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();

public:
	CDocument* GetEditorDoc();
	void SetCtrlPos();
	void InitCtrl();
	void SetParaListToParticleSys();
	
	void UpdateCtrlStateParam(int nVertexRow, int nVertexCol, int nVertexType, BOOL bSetShapeTexture, BOOL bAbsrtactPsl);

	virtual BOOL OnInitDialog();
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnEnKillFocus(UINT nID);

	afx_msg void OnCbnSelchangeComboApslvertextype();
	afx_msg void OnBnClickedCheckAbstractpsl();
	afx_msg void OnBnClickedCheckDirmotion();
	afx_msg void OnBnClickedCheckCreatepologyok();

private:
	CSliderCtrl m_sliderVertexRow;
	CSliderCtrl m_sliderVertexCol;

	CComboBox m_comboPslVertexType;


private:
	BOOL m_bAbsrtactPsl;
	BOOL m_bDirMotion;
	BOOL m_bPologyOk;
	BOOL m_bSetShapeTexture;

private:
	//�������������ǲ�д�����ӵ�
	int m_nVertexRow; //0-----40 //ת����Ч��ǰ����֣��ı��κ������Σ�
	int m_nVertexCol; //0-----40
	int m_nVertexType; //�Զ��塢�ı��Ρ������Ρ�2pic--��ͼ�ֳ�����������ŷָ��������˶� 
	
	int m_nLineAngle;
	//����

public:
	CSliderCtrl m_sliderLineAngle;
};

