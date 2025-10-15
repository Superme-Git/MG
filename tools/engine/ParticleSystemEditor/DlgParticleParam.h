#pragma once


// CDlgParticleParam 对话框

class CDlgParticleParam : public CDialog
{
	DECLARE_DYNAMIC(CDlgParticleParam)

public:
	CDlgParticleParam(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CDlgParticleParam();

// 对话框数据
	enum { IDD = IDD_DIALOG_ParticleParam };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();

public:
	CDocument* GetEditorDoc();
	void SetCtrlPos(); //打开文件时调用
	void InitCtrl();
	void CheckLinkButton();
	void SetParaListToParticleSys();
	COLORREF GetParticleVarColor(float dis);

public:
	virtual BOOL OnInitDialog();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnEnKillFocus(UINT nID);

private:
	BOOL m_bLinkLifeTime;
	BOOL m_bLinkSpeed;
	BOOL m_bLinkSize;
	BOOL m_bLinkSpin;
	BOOL m_bAlignToHole; //控制单个粒子的运动方向 --影响点
	BOOL m_bTrackFx; //控制单个粒子的运动方向 --攻击目标点
	BOOL m_bParticleRandAngle; //粒子顶点旋转

	BOOL m_bLinkAngle;
	BOOL m_bLinkAlpha;
	BOOL m_bLinkColor;

	BOOL m_bParticleColorChange;
	BOOL m_bParticleColorRand;

	BOOL m_bAlignToMotion; //控制单个粒子的角度
	BOOL m_bAlignToPath;

	BOOL m_bParticleRandAngleSelf; //粒子纹理旋转

	int m_curSliderIndex;

	CStatic m_wndShowParticleColor;
	CStatic m_wndShowParticleTexture;//在该区域双击鼠标设置旋转中心

private:
	float m_fParticleLifeMin; //0--1000 ---100.0f
	float m_fParticleLifeMax; //0--1000 ---100.0f
	float m_fEmissionDirection; //0--3600 --360.0f
	float m_fEmissionSpread; //0--3600 --360.0f
	float m_fSpeedMin; //0--2000 --200.0f
	float m_fSpeedMax; //0--2000 --200.0f
	float m_fGravity; //-1000--1000 --100.0f
	float m_fGravityRand; //-1000--1000 --100.0f
	float m_fRadialAcc; //-200--200 --20.0f
	float m_fRadialAccRand; //-200--200 --20.0f
	float m_fTangAcc; //-200--200 --20.0f
	float m_fTangAccRand; //-200--200 --20.0f

	float m_fParSizeStrtX; //0--10000 --100.0f
	float m_fParSizeStrtY; //0--10000 --100.0f
	float m_fParSizeEndX; //0--10000 --100.0f
	float m_fParSizeEndY; //0--10000 --100.0f
	float m_fParSizeVar; //0--100 --1.0f
	float m_fSizeRandom; //0-1000 --10.0f

	float m_fParSpinStrt; //-3600--3600 --360.0f
	float m_fParSpinEnd; //-3600--3600 --360.0f
	float m_fParSpinVar; //0--100 --1.0f
	float m_fSpinRandom; //0-3600 -- 360.0f

	float m_fParAngleStrt; //-3600--3600 --360.0f
	float m_fParAngleEnd; //-3600--3600 --360.0f
	float m_fParAngleVar; //0--100 --1.0f

	int m_nParAlphaStrt; //0--255
	int m_nParAlphaEnd; //0--255
	float m_fParAlphaVar; //0--100 --1.0f
	int m_nParAlphaMid; //0--2550 ----------朱迪想出的单mid模拟2个关键帧..从255----2550
	float m_fParAlphaMidTime; //0--100 --1.0f

	int m_nParColorStrtR; //0--255
	int m_nParColorStrtG; //0--255
	int m_nParColorStrtB; //0--255
	int m_nParColorEndR; //0--255
	int m_nParColorEndG; //0--255
	int m_nParColorEndB; //0--255
	float m_fParColorVar; //0--100 --1.0f

	float m_fAngleRandom; //0-3600 -- 360.0f
	float m_fAlignToMotionAngle;//-3600-3600 -- 360.0f

	float m_fRotateCentX; //原始图片中心为(0,0),左上角(-0.5,-0.5),右下角(0.5,0.5)
	float m_fRotateCentY; //

private:
	CSliderCtrl m_sliderParticleLifeTimeMin;
	CSliderCtrl m_sliderParticleLifeTimeMax;

	CComboBox m_comboSizeRandType;

	CSliderCtrl m_sliderEmissionDirection;
	CSliderCtrl m_sliderEmissionSpread;
	CSliderCtrl m_sliderStartSpeedMin;
	CSliderCtrl m_sliderStartSpeedMax;
	CSliderCtrl m_sliderGravity;
	CSliderCtrl m_sliderGravityRand;
	CSliderCtrl m_sliderRadialAcceleration;
	CSliderCtrl m_sliderRadialAccelerationRand;
	CSliderCtrl m_sliderTangAcc;
	CSliderCtrl m_sliderTangAccRand;

	CSliderCtrl m_sliderParticleSizeStartX;
	CSliderCtrl m_sliderParticleSizeStartY;
	CSliderCtrl m_sliderParticleSizeEndX;
	CSliderCtrl m_sliderParticleSizeEndY;
	CSliderCtrl m_sliderParticleSizeVar;
	CSliderCtrl m_sliderSizeRandom;

	CSliderCtrl m_sliderParticleSpinStart;
	CSliderCtrl m_sliderParticleSpinEnd;
	CSliderCtrl m_sliderParticleSpinVar;
	CSliderCtrl m_sliderSpinRandom;

	CSliderCtrl m_sliderParticleAngleStart;
	CSliderCtrl m_sliderParticleAngleEnd;
	CSliderCtrl m_sliderParticleAngleVar;
	CSliderCtrl m_sliderAngleRandom;

	CSliderCtrl m_sliderParticleAlphaStart;
	CSliderCtrl m_sliderParticleAlphaEnd;
	CSliderCtrl m_sliderParticleAlphaVar;

	CSliderCtrl m_sliderParAlphaMid;
	CSliderCtrl m_sliderParAlphaMidTime;

	CSliderCtrl m_sliderParticleColorStartR;
	CSliderCtrl m_sliderParticleColorStartG;
	CSliderCtrl m_sliderParticleColorStartB;
	CSliderCtrl m_sliderParticleColorEndR;
	CSliderCtrl m_sliderParticleColorEndG;
	CSliderCtrl m_sliderParticleColorEndB;
	CSliderCtrl m_sliderParticleColorVar;
	CSliderCtrl m_sliderAlignToMotion;

private:
	afx_msg void OnBnClickedCheckLinklifetime();
	afx_msg void OnBnClickedCheckLinkspeed();
	afx_msg void OnBnClickedCheckLinksize();
	afx_msg void OnBnClickedCheckLinkspin();
	afx_msg void OnBnClickedCheckObjtrack();
	afx_msg void OnBnClickedCheckAligntoparticlehole();
	afx_msg void OnBnClickedCheckRandangle();
	afx_msg void OnCbnSelchangeComboSizerandtype();

	afx_msg void OnBnClickedCheckLinkangleuv();
	afx_msg void OnBnClickedCheckLinkalpha();
	afx_msg void OnBnClickedCheckLinkcolor();	
	afx_msg void OnBnClickedCheckColorchange();
	afx_msg void OnBnClickedCheckRandangleself();
	afx_msg void OnBnClickedCheckAligntomotion();
	afx_msg void OnBnClickedCheckAligntopath();
	afx_msg void OnBnClickedCheckRandcolor();
	afx_msg void OnBnClickedButtonStartcolor();
	afx_msg void OnBnClickedButtonEndcolor();
public:
	afx_msg void OnPaint();
	afx_msg void OnBnClickedResetrotatecenterpos();
	afx_msg void OnLButtonDblClk(UINT nFlags, CPoint point);
	BOOL m_bAlignToEmitter;
	afx_msg void OnBnClickedCheckAligntoemitter();
};
