#pragma once
#include "afxwin.h"
#include "afxcmn.h"


// CDlgEmitterParam 对话框

class CDlgEmitterParam : public CDialog
{
	DECLARE_DYNAMIC(CDlgEmitterParam)

public:
	CDlgEmitterParam(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CDlgEmitterParam();

// 对话框数据
	enum { IDD = IDD_DIALOG_EmitterParam };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();

public:
	CDocument* GetEditorDoc();
	void SetCtrlPos();
	void InitCtrl();
	void CheckLinkButton();
	void SetParaListToParticleSys();

	BOOL m_bEmitterType;
	afx_msg void OnBnClickedCheckEmittertype();
	CComboBox m_comboEffectType;
	afx_msg void OnCbnSelchangeComboEffecttype();
	virtual BOOL OnInitDialog();
	CComboBox m_comboPslType;
	int m_nPslType;
	afx_msg void OnCbnSelchangeComboPsltype();
	BOOL m_bPathOrder;
	afx_msg void OnBnClickedCheckOrder();
	BOOL m_bParticleFileMode; //fillmode
	afx_msg void OnBnClickedCheckFillmode();
	afx_msg void OnBnClickedCheckUvmirrorh();
	afx_msg void OnBnClickedCheckUvmirrorv();
	afx_msg void OnCbnSelchangeComboBlendmode();
	afx_msg void OnBnClickedRadioBlend();
	afx_msg void OnBnClickedRadioAdd();
	afx_msg void OnBnClickedCheckSingleparticleani();
	afx_msg void OnBnClickedCheckParticleani1(); //纹理索引是否从头开始
	afx_msg void OnCbnSelchangeComboConfigurerequire();

private:
	int m_nSysPosX; //0--1000
	int m_nSysPosY; //0--1000
	int m_nSysPosX2;
	int m_nSysPosY2;
	int m_nSysPosRadiusX; //0---600
	int m_nSysPosRadiusY; //0---600
	int m_nSysPosWidth; //0---1280
	int m_nSysPosHeight; //0---1024
	float m_fEmitterSpin; //-360.0f---360.0f default:0.0f 角度

	float m_fSysLife; //0--1000 ---100.0f
	int m_nSysMaxParticles; //1---2000 --不允许为0
	int m_nEmissionRate; //0--1000
	int m_nEmitterSpeed; //发射器沿着路径移动的速度..

	BOOL m_bUVmirrorh;
	BOOL m_bUVmirrorv;
	int m_nBlendMode;
	BOOL m_nBlendAddMode;
	BOOL m_bSingleParticleAni;
	BOOL m_bParticleAniTextureIndex1; //单粒子动画，是否都从头开始( 默认是随机选一个开始)

	int m_nAniPlaySpeed; //单粒子动画的播放速度（帧/秒）..... 0---50
	float m_fAniSpeedRand; //与上面参数一起决定播放速度
	float m_fPSLzoom; //0---5.0f ----default: 1.0f

	BOOL m_bMotionToPath; //沿着路径运动

private:
	CSliderCtrl m_sliderSystemPosX;
	CSliderCtrl m_sliderSystemPosY;
	CSliderCtrl m_sliderSystemPosX2;
	CSliderCtrl m_sliderSystemPosY2;
	CSliderCtrl m_sliderSysPosRadiusX;
	CSliderCtrl m_sliderSysPosRadiusY;
	CSliderCtrl m_sliderSysPosWidth;
	CSliderCtrl m_sliderSysPosHeight;
	CSliderCtrl m_sliderEmitterSpin;

	CSliderCtrl m_sliderSystemLifeTime;
	CSliderCtrl m_sliderSysMaxParticles;
	CSliderCtrl m_sliderEmissionRate;
	CSliderCtrl m_sliderEmitterSpeed;

	CSliderCtrl m_sliderAniPlaySpeed; //单粒子动画
	CSliderCtrl m_sliderAniSpeedRand;

	CComboBox m_comboBlendModeSrc;
	CComboBox m_comboConfigReq;
	CSliderCtrl m_sliderPSLzoom;
	CComboBox m_comboParticlePathInitPosType;

public:
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnEnKillFocus(UINT nID);

	afx_msg void OnBnClickedCheckMotiontopath();
	afx_msg void OnCbnSelchangeComboParticlepathinitpos();
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	afx_msg void OnBnClickedRadioOneadd();
};
