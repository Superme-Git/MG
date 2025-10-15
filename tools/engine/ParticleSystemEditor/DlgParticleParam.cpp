// DlgParticleParam.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "DlgParticleParam.h"

#include "ParticleSystemEditorDoc.h"
#include "MainFrm.h"
// CDlgParticleParam 对话框

using namespace Nuclear;

IMPLEMENT_DYNAMIC(CDlgParticleParam, CDialog)

CDlgParticleParam::CDlgParticleParam(CWnd* pParent /*=NULL*/)
: CDialog(CDlgParticleParam::IDD, pParent)
, m_bLinkLifeTime(FALSE)
, m_bLinkSpeed(FALSE)
, m_bLinkSize(FALSE)
, m_bLinkSpin(FALSE)
, m_bTrackFx(FALSE)
, m_bParticleRandAngle(FALSE)
, m_bAlignToHole(FALSE)
, m_bLinkAlpha(FALSE)
, m_bParticleColorChange(FALSE)
, m_bLinkAngle(FALSE)
, m_bAlignToMotion(FALSE)
, m_bAlignToPath(FALSE)
, m_bParticleColorRand(FALSE)
, m_bLinkColor(FALSE)
, m_bParticleRandAngleSelf(FALSE)
, m_bAlignToEmitter(FALSE)
{
	m_fParticleLifeMin = 1.0f;			//0--100 ---10.0f
	m_fParticleLifeMax = 5.0f;			//0--100 ---10.0f
	m_fEmissionDirection = 0.0f;		//0--3600 --360.0f
	m_fEmissionSpread = 0.0f;			//0--3600 --360.0f
	m_fSpeedMin = 0.0f;					//0--2000 --200.0f
	m_fSpeedMax = 100.0f;				//0--10000 --1000.0f
	m_fGravity = 0.0f;					//-1000--1000 --100.0f
	m_fGravityRand = 0.0f;				//-1000--1000 --100.0f
	m_fRadialAcc = 0.0f;				//-200--200 --20.0f
	m_fRadialAccRand = 0.0f;			//-200--200 --20.0f
	m_fTangAcc = 0.0f;					//-200--200 --20.0f
	m_fTangAccRand = 0.0f;				//-200--200 --20.0f
	m_fParSizeStrtX = 1.0f;				//0--10000 --100.0f
	m_fParSizeStrtY = 1.0f;				//0--10000 --100.0f
	m_fParSizeEndX = 1.0f;				//0--10000 --100.0f
	m_fParSizeEndY = 1.0f;				//0--10000 --100.0f
	m_fParSizeVar = 0.5f;				//0--100 --1.0f
	m_fSizeRandom = 0.0f;

	m_fParSpinStrt = 0.0f;				//-3600--3600 --360.0f
	m_fParSpinEnd = 0.0f;				//-3600--3600 --360.0f
	m_fParSpinVar = 0.5f;				//0--100 --1.0f
	m_fSpinRandom = 0.0f;
	
	m_fParAngleStrt = 0.0f;				//-3600--3600 --360.0f
	m_fParAngleEnd = 0.0f;					//-3600--3600 --360.0f

	m_fParAngleVar = 0.5f;					//0--100 --1.0f
	m_nParAlphaStrt = 255;				//0--255
	m_nParAlphaEnd = 0;					//0--255
	m_fParAlphaVar = 0.5f;				//0--100 --1.0f
	m_nParAlphaMid = 128;
	m_fParAlphaMidTime = 0.5f;

	m_nParColorStrtR = 255;				//0--255
	m_nParColorStrtG = 255;				//0--255
	m_nParColorStrtB = 0;				//0--255
	m_nParColorEndR = 255;				//0--255
	m_nParColorEndG = 0;				//0--255
	m_nParColorEndB = 0;				//0--255
	m_fParColorVar = 0.5f;				//0--100 --1.0f

	m_fAngleRandom = 0.0f;
	m_fAlignToMotionAngle = 0.0f;
	m_fRotateCentX = 0.0f;
	m_fRotateCentY = 0.0f;

	m_curSliderIndex = 0;
}

CDlgParticleParam::~CDlgParticleParam()
{
}

void CDlgParticleParam::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_PICTURECTRL_PARTICLECOLOR, m_wndShowParticleColor);
	DDX_Control(pDX, IDC_PICTURE_RotateCenterSetWnd, m_wndShowParticleTexture);

	DDX_Control(pDX, IDC_SLIDER_PARTICLELIFETIME_MIN, m_sliderParticleLifeTimeMin);
	DDX_Control(pDX, IDC_SLIDER_PARTICLELIFETIME_MAX, m_sliderParticleLifeTimeMax);
	DDX_Control(pDX, IDC_SLIDER_EMISSIONDIRECTION, m_sliderEmissionDirection);
	DDX_Control(pDX, IDC_SLIDER_EMISSIONSPREAD, m_sliderEmissionSpread);
	DDX_Control(pDX, IDC_SLIDER_STARTSPEED_MIN, m_sliderStartSpeedMin);
	DDX_Control(pDX, IDC_SLIDER_STARTSPEED_MAX, m_sliderStartSpeedMax);
	DDX_Control(pDX, IDC_SLIDER_GRAVITY, m_sliderGravity);
	DDX_Control(pDX, IDC_SLIDER_GRAVITY_RAND, m_sliderGravityRand);
	DDX_Control(pDX, IDC_SLIDER_RADIALACCELERATION, m_sliderRadialAcceleration);
	DDX_Control(pDX, IDC_SLIDER_RADIALACCELERATION_RAND, m_sliderRadialAccelerationRand);
	DDX_Control(pDX, IDC_SLIDER_TANGACC, m_sliderTangAcc);
	DDX_Control(pDX, IDC_SLIDER_TANGACC_RAND, m_sliderTangAccRand);

	DDX_Control(pDX, IDC_SLIDER_PARTICLESIZE_STRTX, m_sliderParticleSizeStartX);
	DDX_Control(pDX, IDC_SLIDER_PARTICLESIZE_STRTY, m_sliderParticleSizeStartY);
	DDX_Control(pDX, IDC_SLIDER_PARTICLESIZE_ENDX, m_sliderParticleSizeEndX);
	DDX_Control(pDX, IDC_SLIDER_PARTICLESIZE_ENDY, m_sliderParticleSizeEndY);

	DDX_Control(pDX, IDC_SLIDER_PARTICLESIZE_VAR, m_sliderParticleSizeVar);
	DDX_Control(pDX, IDC_SLIDER_PARTICLESPIN_STRT, m_sliderParticleSpinStart);
	DDX_Control(pDX, IDC_SLIDER_PARTICLESPIN_END, m_sliderParticleSpinEnd);
	DDX_Control(pDX, IDC_SLIDER_PARTICLESPIN_VAR, m_sliderParticleSpinVar);
	DDX_Check(pDX, IDC_CHECK_LINKLIFETIME, m_bLinkLifeTime);
	DDX_Check(pDX, IDC_CHECK_LINKSPEED, m_bLinkSpeed);
	DDX_Check(pDX, IDC_CHECK_LINKSIZE, m_bLinkSize);
	DDX_Check(pDX, IDC_CHECK_LINKSPIN, m_bLinkSpin);
	DDX_Check(pDX, IDC_CHECK_OBJTRACK, m_bTrackFx);
	DDX_Control(pDX, IDC_SLIDER_SIZERAND, m_sliderSizeRandom);
	DDX_Control(pDX, IDC_SLIDER_SPINRANDOM, m_sliderSpinRandom);
	DDX_Check(pDX, IDC_CHECK_RANDANGLE, m_bParticleRandAngle);
	DDX_Check(pDX, IDC_CHECK_AlignToParticleHole, m_bAlignToHole);
	DDX_Control(pDX, IDC_COMBO_SIZERANDTYPE, m_comboSizeRandType);

	DDX_Text(pDX, IDC_EDIT_ParticleLifeMin, m_fParticleLifeMin);
	DDV_MinMaxFloat(pDX, m_fParticleLifeMin, 0.0f, 100.0f);
	DDX_Text(pDX, IDC_EDIT_ParticleLifeMax, m_fParticleLifeMax);
	DDV_MinMaxFloat(pDX, m_fParticleLifeMax, 0.0f, 100.0f);
	DDX_Text(pDX, IDC_EDIT_EmissionDirection, m_fEmissionDirection);
	DDV_MinMaxFloat(pDX, m_fEmissionDirection, 0.0f, 360.0f);
	DDX_Text(pDX, IDC_EDIT_EmissionSpread, m_fEmissionSpread);
	DDV_MinMaxFloat(pDX, m_fEmissionSpread, 0.0f, 360.0f);
	DDX_Text(pDX, IDC_EDIT_SpeedMin, m_fSpeedMin);
	DDV_MinMaxFloat(pDX, m_fSpeedMin, 0.0f, 1000.0f);
	DDX_Text(pDX, IDC_EDIT_SpeedMax, m_fSpeedMax);
	DDV_MinMaxFloat(pDX, m_fSpeedMax, 0.0f, 1000.0f);
	DDX_Text(pDX, IDC_EDIT_Gravity, m_fGravity);
	DDV_MinMaxFloat(pDX, m_fGravity, -100.0f, 100.0f);
	DDX_Text(pDX, IDC_EDIT_GravityRand, m_fGravityRand);
	DDV_MinMaxFloat(pDX, m_fGravityRand, 0.0f, 5.0f);
	DDX_Text(pDX, IDC_EDIT_RadialAcc, m_fRadialAcc);
	DDV_MinMaxFloat(pDX, m_fRadialAcc, -20.0f, 20.0f);
	DDX_Text(pDX, IDC_EDIT_RadialAccRand, m_fRadialAccRand);
	DDV_MinMaxFloat(pDX, m_fRadialAccRand, 0.0f, 5.0f);
	DDX_Text(pDX, IDC_EDIT_TangAcc, m_fTangAcc);
	DDV_MinMaxFloat(pDX, m_fTangAcc, -20.0f, 20.0f);
	DDX_Text(pDX, IDC_EDIT_TangAccRand, m_fTangAccRand);
	DDV_MinMaxFloat(pDX, m_fTangAccRand, 0.0f, 5.0f);
	DDX_Text(pDX, IDC_EDIT_ParSizeStrtX, m_fParSizeStrtX);
	DDV_MinMaxFloat(pDX, m_fParSizeStrtX, 0.0f, 100.0f);
	DDX_Text(pDX, IDC_EDIT_ParSizeStrtY, m_fParSizeStrtY);
	DDV_MinMaxFloat(pDX, m_fParSizeStrtY, 0.0f, 100.0f);
	DDX_Text(pDX, IDC_EDIT_ParSizeEndX, m_fParSizeEndX);
	DDV_MinMaxFloat(pDX, m_fParSizeEndX, 0.0f, 100.0f);
	DDX_Text(pDX, IDC_EDIT_ParSizeEndY, m_fParSizeEndY);
	DDV_MinMaxFloat(pDX, m_fParSizeEndY, 0.0f, 100.0f);
	DDX_Text(pDX, IDC_EDIT_ParSizeVar, m_fParSizeVar);
	DDV_MinMaxFloat(pDX, m_fParSizeVar, 0.0f, 1.0f);
	DDX_Text(pDX, IDC_EDIT_SizeRandom, m_fSizeRandom);
	DDV_MinMaxFloat(pDX, m_fSizeRandom, 0.0f, 10.0f);
	DDX_Text(pDX, IDC_EDIT_ParSpinStrt, m_fParSpinStrt);
	DDV_MinMaxFloat(pDX, m_fParSpinStrt, -360.0f, 360.0f);
	DDX_Text(pDX, IDC_EDIT_ParSpinEnd, m_fParSpinEnd);
	DDV_MinMaxFloat(pDX, m_fParSpinEnd, -360.0f, 360.0f);
	DDX_Text(pDX, IDC_EDIT_ParSpinVar, m_fParSpinVar);
	DDV_MinMaxFloat(pDX, m_fParSpinVar, 0.0f, 1.0f);
	DDX_Text(pDX, IDC_EDIT_SpinRandom, m_fSpinRandom);
	DDV_MinMaxFloat(pDX, m_fSpinRandom, 0.0f, 360.0f);


	DDX_Control(pDX, IDC_SLIDER_PARTICLEALPHA_STRT, m_sliderParticleAlphaStart);
	DDX_Control(pDX, IDC_SLIDER_PARTICLEALPHA_END, m_sliderParticleAlphaEnd);
	DDX_Control(pDX, IDC_SLIDER_PARTICLEALPHA_VAR, m_sliderParticleAlphaVar);
	DDX_Control(pDX, IDC_SLIDER_PARTICLECOLOR_STRT_G, m_sliderParticleColorStartG);
	DDX_Control(pDX, IDC_SLIDER_PARTICLECOLOR_STRT_B, m_sliderParticleColorStartB);
	DDX_Control(pDX, IDC_SLIDER_PARTICLECOLOR_VAR, m_sliderParticleColorVar);
	DDX_Control(pDX, IDC_SLIDER_PARTICLECOLOR_STRT_R, m_sliderParticleColorStartR);
	DDX_Control(pDX, IDC_SLIDER_PARTICLECOLOR_END_R, m_sliderParticleColorEndR);
	DDX_Control(pDX, IDC_SLIDER_PARTICLECOLOR_END_G, m_sliderParticleColorEndG);
	DDX_Control(pDX, IDC_SLIDER_PARTICLECOLOR_END_B, m_sliderParticleColorEndB);

	DDX_Check(pDX, IDC_CHECK_LINKALPHA, m_bLinkAlpha);

	DDX_Check(pDX, IDC_CHECK_COLORCHANGE, m_bParticleColorChange);

	DDX_Check(pDX, IDC_CHECK_LinkAngleUV, m_bLinkAngle);
	DDX_Control(pDX, IDC_SLIDER_PARTICLEANGLE_STRT, m_sliderParticleAngleStart);
	DDX_Control(pDX, IDC_SLIDER_PARTICLEANGLE_END, m_sliderParticleAngleEnd);
	DDX_Control(pDX, IDC_SLIDER_PARTICLEANGLE_VAR, m_sliderParticleAngleVar);
	DDX_Control(pDX, IDC_SLIDER_ANGLERANDOM, m_sliderAngleRandom);
	DDX_Check(pDX, IDC_CHECK_ALIGNTOMOTION, m_bAlignToMotion);
	DDX_Control(pDX, IDC_SLIDER_AlignToMotionAngle, m_sliderAlignToMotion);

	DDX_Text(pDX, IDC_EDIT_AlignToMotionAngle, m_fAlignToMotionAngle);
	DDV_MinMaxFloat(pDX, m_fAlignToMotionAngle, -360.0f, 360.0f);
	DDX_Text(pDX, IDC_EDIT_ParAngleStrt, m_fParAngleStrt);
	DDV_MinMaxFloat(pDX, m_fParAngleStrt, -360.0f, 360.0f);
	DDX_Text(pDX, IDC_EDIT_ParAngleEnd, m_fParAngleEnd);
	DDV_MinMaxFloat(pDX, m_fParAngleEnd, -360.0f, 360.0f);
	DDX_Text(pDX, IDC_EDIT_ParAngleVar, m_fParAngleVar);
	DDV_MinMaxFloat(pDX, m_fParAngleVar, 0.0f, 1.0f); 
	DDX_Text(pDX, IDC_EDIT_AngleRandom, m_fAngleRandom);
	DDV_MinMaxFloat(pDX, m_fAngleRandom, 0.0f, 360.0f);
	DDX_Text(pDX, IDC_EDIT_ParAlphaStrt, m_nParAlphaStrt);
	DDV_MinMaxInt(pDX, m_nParAlphaStrt, 0, 255);
	DDX_Text(pDX, IDC_EDIT_ParAlphaEnd, m_nParAlphaEnd);
	DDV_MinMaxInt(pDX, m_nParAlphaEnd, 0, 255);
	DDX_Text(pDX, IDC_EDIT_ParAlphaVar, m_fParAlphaVar);
	DDV_MinMaxFloat(pDX, m_fParAlphaVar, 0.0f, 1.0f);
	DDX_Text(pDX, IDC_EDIT_ParColorStrtR, m_nParColorStrtR);
	DDV_MinMaxInt(pDX, m_nParColorStrtR, 0, 255);
	DDX_Text(pDX, IDC_EDIT_ParColorStrtG, m_nParColorStrtG);
	DDV_MinMaxInt(pDX, m_nParColorStrtG, 0, 255);
	DDX_Text(pDX, IDC_EDIT_ParColorStrtB, m_nParColorStrtB);
	DDV_MinMaxInt(pDX, m_nParColorStrtB, 0, 255);
	DDX_Text(pDX, IDC_EDIT_ParColorEndR, m_nParColorEndR);
	DDV_MinMaxInt(pDX, m_nParColorEndR, 0, 255);
	DDX_Text(pDX, IDC_EDIT_ParColorEndG, m_nParColorEndG);
	DDV_MinMaxInt(pDX, m_nParColorEndG, 0, 255);
	DDX_Text(pDX, IDC_EDIT_ParColorEndB, m_nParColorEndB);
	DDV_MinMaxInt(pDX, m_nParColorEndB, 0, 255);
	DDX_Text(pDX, IDC_EDIT_ParColorVar, m_fParColorVar);
	DDV_MinMaxFloat(pDX, m_fParColorVar, 0.0f, 1.0f);

	DDX_Control(pDX, IDC_SLIDER_ParAlphaMid, m_sliderParAlphaMid);
	DDX_Text(pDX, IDC_EDIT_ParAlphaMid, m_nParAlphaMid);
	DDV_MinMaxInt(pDX, m_nParAlphaMid, 0, 2550);
	DDX_Control(pDX, IDC_SLIDER_ParAlphaMidTime, m_sliderParAlphaMidTime);
	DDX_Text(pDX, IDC_EDIT_ParAlphaMidTime, m_fParAlphaMidTime);
	DDV_MinMaxFloat(pDX, m_fParAlphaMidTime, 0.0f, 1.0f);
	DDX_Check(pDX, IDC_CHECK_AlignToPath, m_bAlignToPath);
	DDX_Check(pDX, IDC_CHECK_RandColor, m_bParticleColorRand);
	DDX_Check(pDX, IDC_CHECK_LINKCOLOR, m_bLinkColor);
	DDX_Check(pDX, IDC_CHECK_RANDANGLESELF, m_bParticleRandAngleSelf);
	DDX_Check(pDX, IDC_CHECK_ALIGNTOEMITTER, m_bAlignToEmitter);
}

BEGIN_MESSAGE_MAP(CDlgParticleParam, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgParticleParam::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CDlgParticleParam::OnBnClickedCancel)
	ON_WM_HSCROLL()
	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_SysPosX, IDC_EDIT_VertexCol, &CDlgParticleParam::OnEnKillFocus)

	ON_CBN_SELCHANGE(IDC_COMBO_SIZERANDTYPE, &CDlgParticleParam::OnCbnSelchangeComboSizerandtype)

	ON_BN_CLICKED(IDC_CHECK_LINKLIFETIME, &CDlgParticleParam::OnBnClickedCheckLinklifetime)
	ON_BN_CLICKED(IDC_CHECK_LINKSPEED, &CDlgParticleParam::OnBnClickedCheckLinkspeed)
	ON_BN_CLICKED(IDC_CHECK_LINKSIZE, &CDlgParticleParam::OnBnClickedCheckLinksize)
	ON_BN_CLICKED(IDC_CHECK_LINKSPIN, &CDlgParticleParam::OnBnClickedCheckLinkspin)
	ON_BN_CLICKED(IDC_CHECK_OBJTRACK, &CDlgParticleParam::OnBnClickedCheckObjtrack)
	ON_BN_CLICKED(IDC_CHECK_RANDANGLE, &CDlgParticleParam::OnBnClickedCheckRandangle)
	ON_BN_CLICKED(IDC_CHECK_AlignToParticleHole, &CDlgParticleParam::OnBnClickedCheckAligntoparticlehole)

	ON_BN_CLICKED(IDC_CHECK_COLORCHANGE, &CDlgParticleParam::OnBnClickedCheckColorchange)
	ON_BN_CLICKED(IDC_CHECK_LINKALPHA, &CDlgParticleParam::OnBnClickedCheckLinkalpha)
	ON_BN_CLICKED(IDC_CHECK_LinkAngleUV, &CDlgParticleParam::OnBnClickedCheckLinkangleuv)
	ON_BN_CLICKED(IDC_CHECK_ALIGNTOMOTION, &CDlgParticleParam::OnBnClickedCheckAligntomotion)
	ON_BN_CLICKED(IDC_CHECK_AlignToPath, &CDlgParticleParam::OnBnClickedCheckAligntopath)
	ON_BN_CLICKED(IDC_CHECK_RandColor, &CDlgParticleParam::OnBnClickedCheckRandcolor)
	ON_BN_CLICKED(IDC_CHECK_LINKCOLOR, &CDlgParticleParam::OnBnClickedCheckLinkcolor)
	ON_BN_CLICKED(IDC_CHECK_RANDANGLESELF, &CDlgParticleParam::OnBnClickedCheckRandangleself)

	ON_BN_CLICKED(IDC_BUTTON_STARTCOLOR, &CDlgParticleParam::OnBnClickedButtonStartcolor)
	ON_BN_CLICKED(IDC_BUTTON_ENDCOLOR, &CDlgParticleParam::OnBnClickedButtonEndcolor)

	ON_WM_PAINT()
	ON_BN_CLICKED(IDC_ResetRotateCenterPos, &CDlgParticleParam::OnBnClickedResetrotatecenterpos)
	ON_WM_LBUTTONDBLCLK()
	ON_BN_CLICKED(IDC_CHECK_ALIGNTOEMITTER, &CDlgParticleParam::OnBnClickedCheckAligntoemitter)
END_MESSAGE_MAP()


// CDlgParticleParam 消息处理程序

void CDlgParticleParam::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnOK();
}

void CDlgParticleParam::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnCancel();
}

CDocument* CDlgParticleParam::GetEditorDoc()
{
	CMainFrame* pwnd = (CMainFrame*)AfxGetMainWnd();
	CFrameWnd* pchildWnd = pwnd->GetActiveFrame(); 
	CDocument* pCurrentDoc = pchildWnd->GetActiveDocument();

	return pCurrentDoc;//(CParticleSystemEditorDoc*)
}

void CDlgParticleParam::SetCtrlPos()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	m_fParticleLifeMin = pParticlesys->GetPSL()->m_fParticleLifeMin;			
	m_fParticleLifeMax = pParticlesys->GetPSL()->m_fParticleLifeMax;			
	m_fEmissionDirection = pParticlesys->GetPSL()->m_fEmissionDirection;			
	m_fEmissionSpread = pParticlesys->GetPSL()->m_fEmissionSpread;				
	m_fSpeedMin = pParticlesys->GetPSL()->m_fSpeedMin;					
	m_fSpeedMax = pParticlesys->GetPSL()->m_fSpeedMax;					
	m_fGravity = pParticlesys->GetPSL()->m_fGravity;					
	m_fGravityRand = pParticlesys->GetPSL()->m_fGravityRand;					
	m_fRadialAcc = pParticlesys->GetPSL()->m_fRadialAcc;				
	m_fRadialAccRand = pParticlesys->GetPSL()->m_fRadialAccRand;				
	m_fTangAcc = pParticlesys->GetPSL()->m_fTangAcc;					
	m_fTangAccRand = pParticlesys->GetPSL()->m_fTangAccRand;

	m_fParSizeStrtX = pParticlesys->GetPSL()->m_fParSizeStrtX;	
	m_fParSizeStrtY = pParticlesys->GetPSL()->m_fParSizeStrtY;				
	m_fParSizeEndX = pParticlesys->GetPSL()->m_fParSizeEndX;				
	m_fParSizeEndY = pParticlesys->GetPSL()->m_fParSizeEndY;
	m_fParSizeVar = pParticlesys->GetPSL()->m_fParSizeVar;

	m_fParSpinStrt = pParticlesys->GetPSL()->m_fParSpinStrt;					
	m_fParSpinEnd = pParticlesys->GetPSL()->m_fParSpinEnd;					
	m_fParSpinVar = pParticlesys->GetPSL()->m_fParSpinVar;

	m_fSizeRandom = pParticlesys->GetPSL()->m_fSizeRandom;
	m_fSpinRandom = pParticlesys->GetPSL()->m_fSpinRandom;
	m_bLinkLifeTime = false;
	m_bLinkSpeed = false;

	m_bLinkSize = false;
	m_bLinkSpin = false;
	m_bTrackFx = pParticlesys->GetPSL()->m_nTrackFx;
	m_bAlignToHole = pParticlesys->GetPSL()->m_nAlignToHole;
	m_bParticleRandAngle = pParticlesys->GetPSL()->m_nParticleRandAngle;

	bool bres = m_bAlignToMotion || m_bAlignToPath;
	m_sliderAlignToMotion.EnableWindow(bres);
	m_sliderSpinRandom.EnableWindow(!bres);
	bres = bres || m_bParticleRandAngle;
	m_sliderParticleSpinStart.EnableWindow(!bres);
	m_sliderParticleSpinEnd.EnableWindow(!bres);
	m_sliderParticleSpinVar.EnableWindow(!bres);

	m_sliderParticleLifeTimeMin.SetPos((int)(m_fParticleLifeMin *10));
	m_sliderParticleLifeTimeMax.SetPos((int)(m_fParticleLifeMax *10));

	m_comboSizeRandType.SetCurSel(pParticlesys->GetPSL()->m_nParticleSizeRandType);

	m_sliderEmissionDirection.SetPos((int)(m_fEmissionDirection *10));
	m_sliderEmissionSpread.SetPos((int)(m_fEmissionSpread *10));
	m_sliderStartSpeedMin.SetPos((int)(m_fSpeedMin *10));
	m_sliderStartSpeedMax.SetPos((int)(m_fSpeedMax *10));
	m_sliderGravity.SetPos((int)(m_fGravity *10));
	m_sliderGravityRand.SetPos((int)(m_fGravityRand *10));
	m_sliderRadialAcceleration.SetPos((int)(m_fRadialAcc *10));
	m_sliderRadialAccelerationRand.SetPos((int)(m_fRadialAccRand *10));
	m_sliderTangAcc.SetPos((int)(m_fTangAcc *10));
	m_sliderTangAccRand.SetPos((int)(m_fTangAccRand *10));

	m_sliderParticleSizeStartX.SetPos((int)(m_fParSizeStrtX *100));
	m_sliderParticleSizeStartY.SetPos((int)(m_fParSizeStrtY *100));
	m_sliderParticleSizeEndX.SetPos((int)(m_fParSizeEndX *100));
	m_sliderParticleSizeEndY.SetPos((int)(m_fParSizeEndY *100));	
	m_sliderParticleSizeVar.SetPos((int)(m_fParSizeVar *100));	
	m_sliderParticleSpinStart.SetPos((int)(m_fParSpinStrt *10));
	m_sliderParticleSpinEnd.SetPos((int)(m_fParSpinEnd *10));
	m_sliderParticleSpinVar.SetPos((int)(m_fParSpinVar *100));	
	m_sliderSizeRandom.SetPos((int)(m_fSizeRandom *100));
	m_sliderSpinRandom.SetPos((int)(m_fSpinRandom *10));
	
	m_fParAngleStrt = pParticlesys->GetPSL()->m_fParAngleStrt;					
	m_fParAngleEnd = pParticlesys->GetPSL()->m_fParAngleEnd;					
	m_fParAngleVar = pParticlesys->GetPSL()->m_fParAngleVar;	

	m_nParAlphaStrt = pParticlesys->GetPSL()->m_nParAlphaStrt;				
	m_nParAlphaEnd = pParticlesys->GetPSL()->m_nParAlphaEnd;					
	m_fParAlphaVar = pParticlesys->GetPSL()->m_fParAlphaVar;	
	m_nParAlphaMid = pParticlesys->GetPSL()->m_nParAlphaMid;
	m_fParAlphaMidTime = pParticlesys->GetPSL()->m_fParAlphaMidTime;

	m_nParColorStrtR = pParticlesys->GetPSL()->m_nParColorStrtR;				
	m_nParColorStrtG = pParticlesys->GetPSL()->m_nParColorStrtG;				
	m_nParColorStrtB = pParticlesys->GetPSL()->m_nParColorStrtB;				
	m_nParColorEndR = pParticlesys->GetPSL()->m_nParColorEndR;				
	m_nParColorEndG = pParticlesys->GetPSL()->m_nParColorEndG;				
	m_nParColorEndB = pParticlesys->GetPSL()->m_nParColorEndB;				
	m_fParColorVar = pParticlesys->GetPSL()->m_fParColorVar;			

	m_fAngleRandom = pParticlesys->GetPSL()->m_fAngleRandom;
	m_fAlignToMotionAngle = pParticlesys->GetPSL()->m_fAlignToMotionAngle;

	m_bLinkAlpha = false;
	m_bLinkColor = false;

	m_bParticleColorChange = pParticlesys->GetPSL()->m_nParticleColorChange;
	m_bParticleColorRand = pParticlesys->GetPSL()->m_nParticleColorRand;
	m_bAlignToMotion = pParticlesys->GetPSL()->m_nAlignToMotion;
	m_bAlignToEmitter = pParticlesys->GetPSL()->m_nAlignToEmitter;
	m_bAlignToPath = pParticlesys->GetPSL()->m_nAlignToPath;

	m_bParticleRandAngleSelf = pParticlesys->GetPSL()->m_nParticleRandAngleSelf;

	m_sliderParticleAngleStart.EnableWindow(!m_bParticleRandAngleSelf);
	m_sliderParticleAngleEnd.EnableWindow(!m_bParticleRandAngleSelf);
	m_sliderParticleAngleVar.EnableWindow(!m_bParticleRandAngleSelf);


	m_sliderParticleAngleStart.SetPos((int)(m_fParAngleStrt *10));
	m_sliderParticleAngleEnd.SetPos((int)(m_fParAngleEnd *10));
	m_sliderParticleAngleVar.SetPos((int)(m_fParAngleVar *100));	

	m_sliderParticleAlphaStart.SetPos(m_nParAlphaStrt);
	m_sliderParticleAlphaEnd.SetPos(m_nParAlphaEnd);
	m_sliderParticleAlphaVar.SetPos((int)(m_fParAlphaVar *100));

	m_sliderParAlphaMid.SetPos(m_nParAlphaMid);
	m_sliderParAlphaMidTime.SetPos((int)(m_fParAlphaMidTime *100));

	m_sliderAngleRandom.SetPos((int)(m_fAngleRandom *10));
	m_sliderAlignToMotion.SetPos((int)(m_fAlignToMotionAngle *10));

	m_sliderParticleColorStartR.SetPos(m_nParColorStrtR);
	m_sliderParticleColorStartG.SetPos(m_nParColorStrtG);
	m_sliderParticleColorStartB.SetPos(m_nParColorStrtB);

	m_sliderParticleColorEndR.SetPos(m_nParColorEndR);
	m_sliderParticleColorEndG.SetPos(m_nParColorEndG);
	m_sliderParticleColorEndB.SetPos(m_nParColorEndB);

	m_sliderParticleColorVar.SetPos((int)(m_fParColorVar *100));

	m_fRotateCentX = pParticlesys->GetPSL()->m_fRotateCentX;
	m_fRotateCentY = pParticlesys->GetPSL()->m_fRotateCentY;

	CRect rct;
	m_wndShowParticleTexture.GetWindowRect(&rct);
	ScreenToClient(&rct);
	InvalidateRect(&rct, true);
	
	UpdateData(false);
}

void CDlgParticleParam::InitCtrl()
{
	m_bLinkLifeTime = false;
	m_bLinkSpeed = false;
	m_bLinkSize = false;
	m_bLinkSpin = false;
	m_bAlignToHole = false;
	m_bTrackFx = false;
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	pDoc->GetParticleSystemEditorView()->SetObjTrackFlag(m_bTrackFx);


	m_sliderParticleLifeTimeMin.SetRange(0, 1000);		//0.0f--100.0f
	m_sliderParticleLifeTimeMin.SetPos(10);
	m_sliderParticleLifeTimeMax.SetRange(0, 1000);		//0.0f--100.0f
	m_sliderParticleLifeTimeMax.SetPos(50);

	m_comboSizeRandType.ResetContent();

	m_comboSizeRandType.InsertString(0, L"XY均等随机");
	m_comboSizeRandType.InsertString(1, L"只随机X");
	m_comboSizeRandType.InsertString(2, L"只随机Y");
	m_comboSizeRandType.InsertString(3, L"XY等比随机");

	m_comboSizeRandType.SetCurSel(0);

	m_sliderEmissionDirection.SetRange(0, 3600);		//0.0f--360.0f
	m_sliderEmissionDirection.SetPos(0);
	m_sliderEmissionSpread.SetRange(0, 3600);			//0.0f--360.0f
	m_sliderEmissionSpread.SetPos(0);
	m_sliderStartSpeedMin.SetRange(0, 10000);			//0.0f--1000.0f
	m_sliderStartSpeedMin.SetPos(0);
	m_sliderStartSpeedMax.SetRange(0, 10000);			//0.0f--1000.0f
	m_sliderStartSpeedMax.SetPos(1000);
	m_sliderGravity.SetRange(-1000, 1000, true);		//-100.0f--100.0f
	m_sliderGravity.SetPos(0);
	m_sliderGravityRand.SetRange(0, 50, true);			//0.0f--5.0f
	m_sliderGravityRand.SetPos(0);
	m_sliderRadialAcceleration.SetRange(-200, 200, true); //-20.0f--20.0f
	m_sliderRadialAcceleration.SetPos(0);
	m_sliderRadialAccelerationRand.SetRange(0, 50, true); //0.0f--5.0f
	m_sliderRadialAccelerationRand.SetPos(0);
	m_sliderTangAcc.SetRange(-200, 200, true);				//-20.0f--20.0f
	m_sliderTangAcc.SetPos(0);
	m_sliderTangAccRand.SetRange(0, 50, true);				//0.0f--5.0f
	m_sliderTangAccRand.SetPos(0);

	m_sliderParticleSizeStartX.SetRange(0, 10000);		//0.0f--100.0f
	m_sliderParticleSizeStartX.SetPos(100);			    //default 1.0f
	m_sliderParticleSizeStartY.SetRange(0, 10000);		//0.0f--100.0f
	m_sliderParticleSizeStartY.SetPos(100);				//default 1.0f
	m_sliderParticleSizeEndX.SetRange(0, 10000);		//0.0f--100.0f
	m_sliderParticleSizeEndX.SetPos(100);				//default 1.0f
	m_sliderParticleSizeEndY.SetRange(0, 10000);		//0.0f--100.0f
	m_sliderParticleSizeEndY.SetPos(100);				//default 1.0f
	m_sliderParticleSizeVar.SetRange(0, 100);			//0.0f--1.0f
	m_sliderParticleSizeVar.SetPos(50);					//default 0.5f
	m_sliderParticleSpinStart.SetRange(-3600, 3600, true);	//-360.0f--360.0f
	m_sliderParticleSpinStart.SetPos(0);
	m_sliderParticleSpinEnd.SetRange(-3600, 3600, true);		//-360.0f--360.0f
	m_sliderParticleSpinEnd.SetPos(0);
	m_sliderParticleSpinVar.SetRange(0, 100);			//0.0f--1.0f
	m_sliderParticleSpinVar.SetPos(50);					//default 0.5f

	m_sliderSizeRandom.SetRange(0, 1000);
	m_sliderSizeRandom.SetPos(0);
	m_sliderSpinRandom.SetRange(0, 3600);
	m_sliderSpinRandom.SetPos(0);

	m_bLinkAlpha = false;
	m_bLinkColor = false;

	m_bParticleColorChange = false;
	m_bParticleColorRand = false;
	m_bAlignToMotion = false;
	m_bAlignToEmitter = false;

	m_sliderParticleAngleStart.SetRange(-3600, 3600, true);	//-360.0f--360.0f
	m_sliderParticleAngleStart.SetPos(0);
	m_sliderParticleAngleEnd.SetRange(-3600, 3600, true);		//-360.0f--360.0f
	m_sliderParticleAngleEnd.SetPos(0);
	m_sliderParticleAngleVar.SetRange(0, 100);			//0.0f--1.0f
	m_sliderParticleAngleVar.SetPos(50);				//default 0.5f

	m_sliderParticleAlphaStart.SetRange(0, 255);
	m_sliderParticleAlphaStart.SetPos(255);
	m_sliderParticleAlphaEnd.SetRange(0, 255);
	m_sliderParticleAlphaEnd.SetPos(0);
	m_sliderParticleAlphaVar.SetRange(0, 100);			//0.0f--1.0f
	m_sliderParticleAlphaVar.SetPos(50);				//default 0.5f
	m_sliderParAlphaMid.SetRange(0, 2550); ////这个是为了实现单mid值模拟两个关键帧把255改为2550的
	m_sliderParAlphaMid.SetPos(128);
	m_sliderParAlphaMidTime.SetRange(0, 100);
	m_sliderParAlphaMidTime.SetPos(50);

	m_sliderAngleRandom.SetRange(0, 3600);
	m_sliderAngleRandom.SetPos(0);
	m_sliderAlignToMotion.SetRange(-3600, 3600, true);
	m_sliderAlignToMotion.SetPos(0);

	m_sliderParticleColorStartR.SetRange(0, 255);
	m_sliderParticleColorStartR.SetPos(255);
	m_sliderParticleColorStartG.SetRange(0, 255);
	m_sliderParticleColorStartG.SetPos(255);
	m_sliderParticleColorStartB.SetRange(0, 255);
	m_sliderParticleColorStartB.SetPos(0);

	m_sliderParticleColorEndR.SetRange(0, 255);
	m_sliderParticleColorEndR.SetPos(255);
	m_sliderParticleColorEndG.SetRange(0, 255);
	m_sliderParticleColorEndG.SetPos(0);
	m_sliderParticleColorEndB.SetRange(0, 255);
	m_sliderParticleColorEndB.SetPos(0);

	m_sliderParticleColorVar.SetRange(0, 100);		//0.0f--1.0f
	m_sliderParticleColorVar.SetPos(50);			//default 0.5f

	m_sliderAlignToMotion.EnableWindow(m_bAlignToMotion);
	m_sliderAlignToMotion.EnableWindow(m_bAlignToPath);

	UpdateData(false);
}

void CDlgParticleParam::CheckLinkButton()
{
	CSliderCtrl* pCurSliderCtrl = (CSliderCtrl*)GetDlgItem(IDC_SLIDER_SYSTEMPOSITION_POSX +m_curSliderIndex);


	if(m_bLinkLifeTime)
	{
		if( &m_sliderParticleLifeTimeMin == pCurSliderCtrl )
		{
			m_sliderParticleLifeTimeMax.SetPos((int)(m_fParticleLifeMin *10));
			m_fParticleLifeMax = m_fParticleLifeMin;
		}
		if( &m_sliderParticleLifeTimeMax == pCurSliderCtrl )
		{
			m_sliderParticleLifeTimeMin.SetPos((int)(m_fParticleLifeMax *10));
			m_fParticleLifeMin = m_fParticleLifeMax;
		}
	}

	if(m_bLinkSpeed)
	{
		if( &m_sliderStartSpeedMin == pCurSliderCtrl )
		{
			m_sliderStartSpeedMax.SetPos((int)(m_fSpeedMin *10));
			m_fSpeedMax = m_fSpeedMin;
		}
		if( &m_sliderStartSpeedMax == pCurSliderCtrl )
		{
			m_sliderStartSpeedMin.SetPos((int)(m_fSpeedMax *10));
			m_fSpeedMin = m_fSpeedMax;
		}
	}

	if(m_bLinkSize)
	{
		if( &m_sliderParticleSizeStartX == pCurSliderCtrl )
		{
			m_sliderParticleSizeStartY.SetPos((int)(m_fParSizeStrtX *100));
			m_fParSizeStrtY = m_fParSizeStrtX;
			m_sliderParticleSizeEndX.SetPos((int)(m_fParSizeStrtX *100));
			m_fParSizeEndX = m_fParSizeStrtX;
			m_sliderParticleSizeEndY.SetPos((int)(m_fParSizeStrtX *100));
			m_fParSizeEndY = m_fParSizeStrtX;
		}
		if( &m_sliderParticleSizeStartY == pCurSliderCtrl )
		{
			m_sliderParticleSizeStartX.SetPos((int)(m_fParSizeStrtY *100));
			m_fParSizeStrtX = m_fParSizeStrtY;
			m_sliderParticleSizeEndX.SetPos((int)(m_fParSizeStrtY *100));
			m_fParSizeEndX = m_fParSizeStrtY;
			m_sliderParticleSizeEndY.SetPos((int)(m_fParSizeStrtY *100));
			m_fParSizeEndY = m_fParSizeStrtY;
		}
		if( &m_sliderParticleSizeEndX == pCurSliderCtrl )
		{
			m_sliderParticleSizeStartX.SetPos((int)(m_fParSizeEndX *100));
			m_fParSizeStrtX = m_fParSizeEndX;
			m_sliderParticleSizeStartY.SetPos((int)(m_fParSizeEndX *100));
			m_fParSizeStrtY = m_fParSizeEndX;
			m_sliderParticleSizeEndY.SetPos((int)(m_fParSizeEndX *100));
			m_fParSizeEndY = m_fParSizeEndX;
		}
		if( &m_sliderParticleSizeEndY == pCurSliderCtrl )
		{
			m_sliderParticleSizeStartX.SetPos((int)(m_fParSizeEndY *100));
			m_fParSizeStrtX = m_fParSizeEndY;
			m_sliderParticleSizeStartY.SetPos((int)(m_fParSizeEndY *100));
			m_fParSizeStrtY = m_fParSizeEndY;
			m_sliderParticleSizeEndX.SetPos((int)(m_fParSizeEndY *100));
			m_fParSizeEndX = m_fParSizeEndY;
		}
	}

	if(m_bLinkSpin)
	{
		if( &m_sliderParticleSpinStart == pCurSliderCtrl )
		{
			m_sliderParticleSpinEnd.SetPos((int)(m_fParSpinStrt *10));
			m_fParSpinEnd = m_fParSpinStrt;
		}
		if( &m_sliderParticleSpinEnd == pCurSliderCtrl )
		{
			m_sliderParticleSpinStart.SetPos((int)(m_fParSpinEnd *10));
			m_fParSpinStrt = m_fParSpinEnd;
		}
	}

	if(m_bLinkAngle)
	{
		if( &m_sliderParticleAngleStart == pCurSliderCtrl )
		{
			m_sliderParticleAngleEnd.SetPos((int)(m_fParAngleStrt *10));
			m_fParAngleEnd = m_fParAngleStrt;
		}
		if( &m_sliderParticleAngleEnd == pCurSliderCtrl )
		{
			m_sliderParticleAngleStart.SetPos((int)(m_fParAngleEnd *10));
			m_fParAngleStrt = m_fParAngleEnd;
		}
	}

	if(m_bLinkAlpha)
	{
		if( &m_sliderParticleAlphaStart == pCurSliderCtrl )
		{
			m_sliderParticleAlphaEnd.SetPos(m_nParAlphaStrt);
			m_nParAlphaEnd = m_nParAlphaStrt;
		}
		if( &m_sliderParticleAlphaEnd == pCurSliderCtrl )
		{
			m_sliderParticleAlphaStart.SetPos(m_nParAlphaEnd);
			m_nParAlphaStrt = m_nParAlphaEnd;
		}
	}

	if(m_bLinkColor)
	{
		if( &m_sliderParticleColorStartR == pCurSliderCtrl )
		{
			m_sliderParticleColorEndR.SetPos(m_nParColorStrtR);
			m_nParColorEndR = m_nParColorStrtR;
		}
		if( &m_sliderParticleColorEndR == pCurSliderCtrl )
		{
			m_sliderParticleColorStartR.SetPos(m_nParColorEndR);
			m_nParColorStrtR = m_nParColorEndR;
		}

		if( &m_sliderParticleColorStartG == pCurSliderCtrl )
		{
			m_sliderParticleColorEndG.SetPos(m_nParColorStrtG);
			m_nParColorEndG = m_nParColorStrtG;
		}
		if( &m_sliderParticleColorEndG == pCurSliderCtrl )
		{
			m_sliderParticleColorStartG.SetPos(m_nParColorEndG);
			m_nParColorStrtG = m_nParColorEndG;
		}

		if( &m_sliderParticleColorStartB == pCurSliderCtrl )
		{
			m_sliderParticleColorEndB.SetPos(m_nParColorStrtB);
			m_nParColorEndB = m_nParColorStrtB;
		}
		if( &m_sliderParticleColorEndB == pCurSliderCtrl )
		{
			m_sliderParticleColorStartB.SetPos(m_nParColorEndB);
			m_nParColorStrtB = m_nParColorEndB;
		}
	}

}

void CDlgParticleParam::SetParaListToParticleSys()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_fParticleLifeMin = m_fParticleLifeMin;// /10.0f;
	pParticlesys->GetPSL()->m_fParticleLifeMax = m_fParticleLifeMax;// /10.0f;

	int nType = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleSystemEditorView()->GetVertexType();
	if(nType != 3)
	{
		pParticlesys->GetPSL()->m_fEmissionDirection = m_fEmissionDirection;// /10.0f;
		pParticlesys->GetPSL()->m_fEmissionSpread	= m_fEmissionSpread;// /10.0f;
	}

	pParticlesys->GetPSL()->m_fSpeedMin		= m_fSpeedMin;// /10.0f; 
	pParticlesys->GetPSL()->m_fSpeedMax		= m_fSpeedMax;// /10.0f; 
	pParticlesys->GetPSL()->m_fGravity		= m_fGravity;// /10.0f;
	pParticlesys->GetPSL()->m_fGravityRand		= m_fGravityRand;// /10.0f;
	pParticlesys->GetPSL()->m_fRadialAcc	= m_fRadialAcc;// /10.0f;
	pParticlesys->GetPSL()->m_fRadialAccRand	= m_fRadialAccRand;// /10.0f;
	pParticlesys->GetPSL()->m_fTangAcc		= m_fTangAcc;// /10.0f;
	pParticlesys->GetPSL()->m_fTangAccRand		= m_fTangAccRand;// /10.0f;
	pParticlesys->GetPSL()->m_fParSizeStrtX		= m_fParSizeStrtX;// /100.0f;
	pParticlesys->GetPSL()->m_fParSizeStrtY		= m_fParSizeStrtY;// /100.0f;
	pParticlesys->GetPSL()->m_fParSizeEndX		= m_fParSizeEndX;// /100.0f;
	pParticlesys->GetPSL()->m_fParSizeEndY		= m_fParSizeEndY;// /100.0f;
	pParticlesys->GetPSL()->m_fParSizeVar		= m_fParSizeVar;// /100.0f;
	pParticlesys->GetPSL()->m_fParSpinStrt		= m_fParSpinStrt;// /10.0f;
	pParticlesys->GetPSL()->m_fParSpinEnd		= m_fParSpinEnd;// /10.0f;
	pParticlesys->GetPSL()->m_fParSpinVar		= m_fParSpinVar;// /100.0f;
	pParticlesys->GetPSL()->m_fSizeRandom = m_fSizeRandom;// /100.0f;
	pParticlesys->GetPSL()->m_fSpinRandom = m_fSpinRandom;// /10.0f;

	pParticlesys->GetPSL()->m_fParAngleStrt		= m_fParAngleStrt;// /10.0f;
	pParticlesys->GetPSL()->m_fParAngleEnd		= m_fParAngleEnd;// /10.0f;
	pParticlesys->GetPSL()->m_fParAngleVar		= m_fParAngleVar;// /100.0f;

	pParticlesys->GetPSL()->m_nParAlphaStrt		= m_nParAlphaStrt;
	pParticlesys->GetPSL()->m_nParAlphaEnd		= m_nParAlphaEnd;
	pParticlesys->GetPSL()->m_fParAlphaVar		= m_fParAlphaVar;// /100.0f;
	pParticlesys->GetPSL()->m_nParAlphaMid		= m_nParAlphaMid;
	pParticlesys->GetPSL()->m_fParAlphaMidTime	= m_fParAlphaMidTime;// /100.0f;

	pParticlesys->GetPSL()->m_fAngleRandom = m_fAngleRandom;// /10.0f;
	pParticlesys->GetPSL()->m_fAlignToMotionAngle = m_fAlignToMotionAngle;// /10.0f;

	pParticlesys->GetPSL()->m_nParColorStrtR = m_nParColorStrtR;
	pParticlesys->GetPSL()->m_nParColorStrtG = m_nParColorStrtG;
	pParticlesys->GetPSL()->m_nParColorStrtB = m_nParColorStrtB;
	pParticlesys->GetPSL()->m_nParColorEndR	= m_nParColorEndR;
	pParticlesys->GetPSL()->m_nParColorEndG	= m_nParColorEndG;
	pParticlesys->GetPSL()->m_nParColorEndB	= m_nParColorEndB;
	pParticlesys->GetPSL()->m_fParColorVar	= m_fParColorVar;

	pParticlesys->Reset();
}

void CDlgParticleParam::OnEnKillFocus(UINT nID)
{
	UpdateData(true);

	if(m_bLinkLifeTime)
	{
		if(nID==IDC_EDIT_ParticleLifeMin)
			m_fParticleLifeMax = m_fParticleLifeMin;
		if(nID==IDC_EDIT_ParticleLifeMax)
			m_fParticleLifeMin = m_fParticleLifeMax;
	}
	if(m_bLinkSpeed)
	{
		if(nID==IDC_EDIT_SpeedMin)
			m_fSpeedMax = m_fSpeedMin;
		if(nID==IDC_EDIT_SpeedMax)
			m_fSpeedMin = m_fSpeedMax;
	}

	if(m_bLinkSize)
	{
		if(nID==IDC_EDIT_ParSizeStrtX)
		{
			m_fParSizeStrtY = m_fParSizeStrtX;
			m_fParSizeEndX = m_fParSizeStrtX;
			m_fParSizeEndY = m_fParSizeStrtX;
		}
		if(nID==IDC_EDIT_ParSizeStrtY)
		{
			m_fParSizeStrtX = m_fParSizeStrtY;
			m_fParSizeEndX = m_fParSizeStrtY;
			m_fParSizeEndY = m_fParSizeStrtY;
		}
		if(nID==IDC_EDIT_ParSizeEndX)
		{
			m_fParSizeStrtX = m_fParSizeEndX;
			m_fParSizeStrtY = m_fParSizeEndX;
			m_fParSizeEndY = m_fParSizeEndX;
		}
		if(nID==IDC_EDIT_ParSizeEndY)
		{
			m_fParSizeStrtX = m_fParSizeEndY;
			m_fParSizeStrtY = m_fParSizeEndY;
			m_fParSizeEndX = m_fParSizeEndY;
		}
	}
	if(m_bLinkSpin)
	{
		if(nID==IDC_EDIT_ParSpinStrt)
			m_fParSpinEnd = m_fParSpinStrt;
		if(nID==IDC_EDIT_ParSpinEnd)
			m_fParSpinStrt = m_fParSpinEnd;
	}

	NormalizeParam(m_fParticleLifeMin, 0.0f, 100.0f);
	NormalizeParam(m_fParticleLifeMax, 0.0f, 100.0f);
	NormalizeParam(m_fEmissionDirection, 0.0f, 360.0f);
	NormalizeParam(m_fEmissionSpread, 0.0f, 360.0f);
	NormalizeParam(m_fSpeedMin, 0.0f, 1000.0f);
	NormalizeParam(m_fSpeedMax, 0.0f, 1000.0f);
	NormalizeParam(m_fGravity, -100.0f, 100.0f);
	NormalizeParam(m_fGravityRand, 0.0f, 5.0f);
	NormalizeParam(m_fRadialAcc, -20.0f, 20.0f);
	NormalizeParam(m_fRadialAccRand, 0.0f, 5.0f);
	NormalizeParam(m_fTangAcc, -20.0f, 20.0f);
	NormalizeParam(m_fTangAccRand, 0.0f, 5.0f);
	NormalizeParam(m_fParSizeStrtX, 0.0f, 100.0f);
	NormalizeParam(m_fParSizeStrtY, 0.0f, 100.0f);
	NormalizeParam(m_fParSizeEndX, 0.0f, 100.0f);
	NormalizeParam(m_fParSizeEndY, 0.0f, 100.0f);
	NormalizeParam(m_fParSizeVar, 0.0f, 1.0f);
	NormalizeParam(m_fParSpinStrt, -360.0f, 360.0f);
	NormalizeParam(m_fParSpinEnd, -360.0f, 360.0f);
	NormalizeParam(m_fParSpinVar, 0.0f, 1.0f);
	NormalizeParam(m_fSizeRandom, 0.0f, 10.0f);
	NormalizeParam(m_fSpinRandom, 0.0f, 360.0f);


	m_sliderParticleLifeTimeMin.SetPos((int)(m_fParticleLifeMin *10));
	m_sliderParticleLifeTimeMax.SetPos((int)(m_fParticleLifeMax *10));
	m_sliderEmissionDirection.SetPos((int)(m_fEmissionDirection *10));
	m_sliderEmissionSpread.SetPos((int)(m_fEmissionSpread *10));
	m_sliderStartSpeedMin.SetPos((int)(m_fSpeedMin *10));
	m_sliderStartSpeedMax.SetPos((int)(m_fSpeedMax *10));
	m_sliderGravity.SetPos((int)(m_fGravity *10));
	m_sliderGravityRand.SetPos((int)(m_fGravityRand *10));
	m_sliderRadialAcceleration.SetPos((int)(m_fRadialAcc *10));
	m_sliderRadialAccelerationRand.SetPos((int)(m_fRadialAccRand *10));
	m_sliderTangAcc.SetPos((int)(m_fTangAcc *10));
	m_sliderTangAccRand.SetPos((int)(m_fTangAccRand *10));
	m_sliderParticleSizeStartX.SetPos((int)(m_fParSizeStrtX *100));
	m_sliderParticleSizeStartY.SetPos((int)(m_fParSizeStrtY *100));
	m_sliderParticleSizeEndX.SetPos((int)(m_fParSizeEndX *100));
	m_sliderParticleSizeEndY.SetPos((int)(m_fParSizeEndY *100));	
	m_sliderParticleSizeVar.SetPos((int)(m_fParSizeVar *100));	
	m_sliderParticleSpinStart.SetPos((int)(m_fParSpinStrt *10));
	m_sliderParticleSpinEnd.SetPos((int)(m_fParSpinEnd *10));
	m_sliderParticleSpinVar.SetPos((int)(m_fParSpinVar *100));	
	m_sliderSizeRandom.SetPos((int)(m_fSizeRandom *100));
	m_sliderSpinRandom.SetPos((int)(m_fSpinRandom *10));



	if(m_bLinkAngle)
	{
		if(nID==IDC_EDIT_ParAngleStrt)
			m_fParAngleEnd = m_fParAngleStrt;
		if(nID==IDC_EDIT_ParAngleEnd)
			m_fParAngleStrt = m_fParAngleEnd;
	}
	if(m_bLinkAlpha)
	{
		if(nID==IDC_EDIT_ParAlphaStrt)
			m_nParAlphaEnd = m_nParAlphaStrt;
		if(nID==IDC_EDIT_ParAlphaEnd)
			m_nParAlphaStrt = m_nParAlphaEnd;
	}

	if(m_bLinkColor)
	{
		if(nID==IDC_EDIT_ParColorStrtR)
			m_nParColorEndR = m_nParColorStrtR;
		if(nID==IDC_EDIT_ParColorEndR)
			m_nParColorStrtR = m_nParColorEndR;

		if(nID==IDC_EDIT_ParColorStrtG)
			m_nParColorEndG = m_nParColorStrtG;
		if(nID==IDC_EDIT_ParColorEndG)
			m_nParColorStrtG = m_nParColorEndG;

		if(nID==IDC_EDIT_ParColorStrtB)
			m_nParColorEndB = m_nParColorStrtB;
		if(nID==IDC_EDIT_ParColorEndB)
			m_nParColorStrtB = m_nParColorEndB;
	}

	NormalizeParam(m_fParAngleStrt, -360.0f, 360.0f);
	NormalizeParam(m_fParAngleEnd, -360.0f, 360.0f);
	NormalizeParam(m_fParAngleVar, 0.0f, 1.0f);
	NormalizeParam(m_nParAlphaStrt, 0, 255);
	NormalizeParam(m_nParAlphaEnd, 0, 255);
	NormalizeParam(m_fParAlphaVar, 0.0f, 1.0f);
	NormalizeParam(m_fAngleRandom, 0.0f, 360.0f);
	NormalizeParam(m_fAlignToMotionAngle, -360.0f, 360.0f);
	NormalizeParam(m_nParColorStrtR, 0, 255);
	NormalizeParam(m_nParColorStrtG, 0, 255);
	NormalizeParam(m_nParColorStrtB, 0, 255);
	NormalizeParam(m_nParColorEndR, 0, 255);
	NormalizeParam(m_nParColorEndG, 0, 255);
	NormalizeParam(m_nParColorEndB, 0, 255);
	NormalizeParam(m_fParColorVar, 0.0f, 1.0f);

	NormalizeParam(m_nParAlphaMid, 0, 2550);
	NormalizeParam(m_fParAlphaMidTime, 0.0f, 1.0f);

	m_sliderParticleAngleStart.SetPos((int)(m_fParAngleStrt *10));
	m_sliderParticleAngleEnd.SetPos((int)(m_fParAngleEnd *10));
	m_sliderParticleAngleVar.SetPos((int)(m_fParAngleVar *100));	

	m_sliderParticleAlphaStart.SetPos(m_nParAlphaStrt);
	m_sliderParticleAlphaEnd.SetPos(m_nParAlphaEnd);
	m_sliderParticleAlphaVar.SetPos((int)(m_fParAlphaVar *100));

	m_sliderAngleRandom.SetPos((int)(m_fAngleRandom *10));
	m_sliderAlignToMotion.SetPos((int)(m_fAlignToMotionAngle *10));

	m_sliderParticleColorStartR.SetPos(m_nParColorStrtR);
	m_sliderParticleColorStartG.SetPos(m_nParColorStrtG);
	m_sliderParticleColorStartB.SetPos(m_nParColorStrtB);

	m_sliderParticleColorEndR.SetPos(m_nParColorEndR);
	m_sliderParticleColorEndG.SetPos(m_nParColorEndG);
	m_sliderParticleColorEndB.SetPos(m_nParColorEndB);

	m_sliderParticleColorVar.SetPos((int)(m_fParColorVar *100));

	m_sliderParAlphaMid.SetPos(m_nParAlphaMid);
	m_sliderParAlphaMidTime.SetPos((int)(m_fParAlphaMidTime *100));

	CheckLinkButton();
	UpdateData(false);
	SetParaListToParticleSys();
}

BOOL CDlgParticleParam::OnInitDialog()
{
	CDialog::OnInitDialog();

	InitCtrl();

	// TODO:  在此添加额外的初始化

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CDlgParticleParam::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if( pScrollBar==NULL )
	{ //点击的是滚动条..
//		int todo;
		return;
	}

	int nCurSliderID = pScrollBar->GetDlgCtrlID();
	m_curSliderIndex = nCurSliderID - IDC_SLIDER_SYSTEMPOSITION_POSX;
	if(m_curSliderIndex >= IDC_SLIDER_PARTICLECOLOR_STRT_R - IDC_SLIDER_SYSTEMPOSITION_POSX)
	{
		Invalidate(false);
	}

	m_fParticleLifeMin = m_sliderParticleLifeTimeMin.GetPos() *0.1f;	//0--100 --10.0f
	m_fParticleLifeMax = m_sliderParticleLifeTimeMax.GetPos() *0.1f;	//0--100 --10.0f
	m_fEmissionDirection = m_sliderEmissionDirection.GetPos() *0.1f;	//0--3600 --360.0f
	m_fEmissionSpread = m_sliderEmissionSpread.GetPos() *0.1f;		//0--3600 --360.0f
	m_fSpeedMin = m_sliderStartSpeedMin.GetPos() *0.1f;				//0--10000 --1000.0f
	m_fSpeedMax = m_sliderStartSpeedMax.GetPos() *0.1f;				//0--10000 --1000.0f
	m_fGravity = m_sliderGravity.GetPos() *0.1f;				//-1000--1000 --100.0f
	m_fGravityRand = m_sliderGravityRand.GetPos() *0.1f;				//0--50 --5.0f
	m_fRadialAcc = m_sliderRadialAcceleration.GetPos() *0.1f;	//-200--200 --20.0f
	m_fRadialAccRand = m_sliderRadialAccelerationRand.GetPos() *0.1f;	//0--50 --5.0f
	m_fTangAcc = m_sliderTangAcc.GetPos() *0.1f;				//0--200 --20.0f
	m_fTangAccRand = m_sliderTangAccRand.GetPos() *0.1f;				//0--50 --5.0f

	m_fParSizeStrtX = m_sliderParticleSizeStartX.GetPos() *0.01f;		//0--10000 --100.0f
	m_fParSizeStrtY = m_sliderParticleSizeStartY.GetPos() *0.01f;		//0--10000 --100.0f
	m_fParSizeEndX = m_sliderParticleSizeEndX.GetPos() *0.01f;			//0--10000 --100.0f
	m_fParSizeEndY = m_sliderParticleSizeEndY.GetPos() *0.01f;			//0--10000 --100.0f
	m_fParSizeVar = m_sliderParticleSizeVar.GetPos() *0.01f;			//0--100 --1.0f
	m_fSizeRandom = m_sliderSizeRandom.GetPos() *0.01f;

	m_fParSpinStrt = m_sliderParticleSpinStart.GetPos() *0.1f;		//0--3600 --360.0f
	m_fParSpinEnd = m_sliderParticleSpinEnd.GetPos() *0.1f;			//0--3600 --360.0f
	m_fParSpinVar = m_sliderParticleSpinVar.GetPos() *0.01f;			//0--100 --1.0f
	m_fSpinRandom = m_sliderSpinRandom.GetPos() *0.1f;

	m_fParAngleStrt = m_sliderParticleAngleStart.GetPos() *0.1f;		//0--3600 --360.0f
	m_fParAngleEnd = m_sliderParticleAngleEnd.GetPos() *0.1f;			//0--3600 --360.0f
	m_fParAngleVar = m_sliderParticleAngleVar.GetPos() *0.01f;			//0--100 --1.0f

	m_nParAlphaStrt = m_sliderParticleAlphaStart.GetPos();		//0--255
	m_nParAlphaEnd = m_sliderParticleAlphaEnd.GetPos();			//0--255
	m_fParAlphaVar = m_sliderParticleAlphaVar.GetPos() *0.01f;			//0--100 --1.0f

	m_fAngleRandom = m_sliderAngleRandom.GetPos() *0.1f;
	m_fAlignToMotionAngle = m_sliderAlignToMotion.GetPos() *0.1f;

	m_nParColorStrtR = m_sliderParticleColorStartR.GetPos();	//0--255
	m_nParColorStrtG = m_sliderParticleColorStartG.GetPos();	//0--255
	m_nParColorStrtB = m_sliderParticleColorStartB.GetPos();	//0--255
	m_nParColorEndR = m_sliderParticleColorEndR.GetPos();		//0--255
	m_nParColorEndG = m_sliderParticleColorEndG.GetPos();		//0--255
	m_nParColorEndB = m_sliderParticleColorEndB.GetPos();		//0--255
	m_fParColorVar = m_sliderParticleColorVar.GetPos() *0.01f;	//0--100 --1.0f

	m_nParAlphaMid = m_sliderParAlphaMid.GetPos();			//0--255
	m_fParAlphaMidTime = m_sliderParAlphaMidTime.GetPos() *0.01f;			//0--100 --1.0f

	CheckLinkButton();
	SetParaListToParticleSys();

	UpdateData(false); //变量到控件..

	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CDlgParticleParam::OnBnClickedCheckObjtrack()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	if(m_bTrackFx)
	{
		m_bAlignToHole = false;
		UpdateData(false);
		pParticlesys->GetPSL()->m_nAlignToHole = m_bAlignToHole;

		bool bMoveMap = false;
		bool bSetParticleHoleFlag = false;
		bool bSetEmitterSpinCentFlag = false;
		pDoc->GetParticleParameterView()->UpdateBaseCtrlState(bMoveMap, bSetParticleHoleFlag, bSetEmitterSpinCentFlag);

		pDoc->GetParticleSystemEditorView()->SetMoveBKPicFlag(false);
		pDoc->GetParticleSystemEditorView()->SetParticleHoleFlag(false);
		pDoc->GetParticleSystemEditorView()->SetEmitterSpinCentFlag(false);
	}
	pParticlesys->GetPSL()->m_nTrackFx = m_bTrackFx;

	pDoc->GetParticleSystemEditorView()->SetObjTrackFlag(m_bTrackFx);
	pParticlesys->Reset();
}

void CDlgParticleParam::OnBnClickedCheckAligntoparticlehole()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	if(m_bAlignToHole)
	{
		m_bTrackFx = false;
		UpdateData(false);
		pParticlesys->GetPSL()->m_nTrackFx = m_bTrackFx;
		pDoc->GetParticleSystemEditorView()->SetObjTrackFlag(m_bTrackFx);
	}
	pParticlesys->GetPSL()->m_nAlignToHole = m_bAlignToHole;
	pParticlesys->Reset();
}

void CDlgParticleParam::OnBnClickedCheckRandangle()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	if(m_bParticleRandAngle)
	{
		m_bAlignToPath = false;
		m_bAlignToMotion = false;
	}
	pParticlesys->GetPSL()->m_nAlignToMotion = m_bAlignToMotion;
	pParticlesys->GetPSL()->m_nAlignToPath = m_bAlignToPath;
	pParticlesys->GetPSL()->m_nParticleRandAngle = m_bParticleRandAngle;
	m_sliderAlignToMotion.EnableWindow(!m_bParticleRandAngle);

	m_sliderParticleSpinStart.EnableWindow(!m_bParticleRandAngle);
	m_sliderParticleSpinEnd.EnableWindow(!m_bParticleRandAngle);
	m_sliderParticleSpinVar.EnableWindow(!m_bParticleRandAngle);
	//	m_sliderSpinRandom.EnableWindow(!m_bParticleRandAngle); //这个值用来做随机运动---2009-5-31
	m_sliderSpinRandom.EnableWindow(true);
	UpdateData(false);
	pParticlesys->Reset();
}

void CDlgParticleParam::OnBnClickedCheckLinklifetime()
{
	UpdateData(true);
	if(m_bLinkLifeTime)
	{
		m_sliderParticleLifeTimeMax.SetPos((int)(m_fParticleLifeMin *10));
		m_fParticleLifeMax = m_fParticleLifeMin;
		SetParaListToParticleSys();
		UpdateData(false);
	}
}

void CDlgParticleParam::OnBnClickedCheckLinkspeed()
{
	UpdateData(true);
	if(m_bLinkSpeed)
	{
		m_sliderStartSpeedMax.SetPos((int)(m_fSpeedMin *10));
		m_fSpeedMax = m_fSpeedMin;
		SetParaListToParticleSys();
		UpdateData(false);
	}
}

void CDlgParticleParam::OnBnClickedCheckLinksize()
{
	UpdateData(true);
	if(m_bLinkSize)
	{
		m_sliderParticleSizeStartY.SetPos((int)(m_fParSizeStrtX *100));
		m_fParSizeStrtY = m_fParSizeStrtX;
		m_sliderParticleSizeEndX.SetPos((int)(m_fParSizeStrtX *100));
		m_fParSizeEndX = m_fParSizeStrtX;
		m_sliderParticleSizeEndY.SetPos((int)(m_fParSizeStrtX *100));
		m_fParSizeEndY = m_fParSizeStrtX;
		SetParaListToParticleSys();
		UpdateData(false);
	}
}

void CDlgParticleParam::OnBnClickedCheckLinkspin()
{
	UpdateData(true);
	if(m_bLinkSpin)
	{
		m_sliderParticleSpinEnd.SetPos((int)(m_fParSpinStrt *10));	
		m_fParSpinEnd = m_fParSpinStrt;
		SetParaListToParticleSys();
		UpdateData(false);
	}
}

void CDlgParticleParam::OnCbnSelchangeComboSizerandtype()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	int nCurSel = m_comboSizeRandType.GetCurSel();
	pParticlesys->GetPSL()->m_nParticleSizeRandType = nCurSel;
}

void CDlgParticleParam::OnBnClickedCheckLinkangleuv()
{
	UpdateData(true);
	if(m_bLinkAngle)
	{
		m_sliderParticleAngleEnd.SetPos((int)(m_fParAngleStrt *10));	
		m_fParAngleEnd = m_fParAngleStrt;
		SetParaListToParticleSys();
		UpdateData(false);
	}
}

void CDlgParticleParam::OnBnClickedCheckLinkalpha()
{
	UpdateData(true);
	if(m_bLinkAlpha)
	{
		m_sliderParticleAlphaEnd.SetPos(m_nParAlphaStrt);		
		m_nParAlphaEnd = m_nParAlphaStrt;
		SetParaListToParticleSys();
		UpdateData(false);
	}
}

void CDlgParticleParam::OnBnClickedCheckLinkcolor()
{
	UpdateData(true);
	if(m_bLinkColor)
	{
		m_sliderParticleColorEndR.SetPos(m_nParColorStrtR);
		m_sliderParticleColorEndG.SetPos(m_nParColorStrtG);
		m_sliderParticleColorEndB.SetPos(m_nParColorStrtB);

		m_nParColorEndR = m_nParColorStrtR;
		m_nParColorEndG = m_nParColorStrtG;
		m_nParColorEndB = m_nParColorStrtB;

		SetParaListToParticleSys();
		UpdateData(false);
		Invalidate(true);
	}
}

void CDlgParticleParam::OnBnClickedCheckAligntomotion()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	if(m_bAlignToMotion)
	{
		m_bAlignToPath = false;
		m_bParticleRandAngle = false;
	}
	pParticlesys->GetPSL()->m_nAlignToMotion = m_bAlignToMotion;
	pParticlesys->GetPSL()->m_nAlignToPath = m_bAlignToPath;
	pParticlesys->GetPSL()->m_nParticleRandAngle = m_bParticleRandAngle;
	m_sliderAlignToMotion.EnableWindow(m_bAlignToMotion);

	m_sliderParticleSpinStart.EnableWindow(!m_bAlignToMotion);
	m_sliderParticleSpinEnd.EnableWindow(!m_bAlignToMotion);
	m_sliderParticleSpinVar.EnableWindow(!m_bAlignToMotion);
	m_sliderSpinRandom.EnableWindow(!m_bAlignToMotion);
	UpdateData(false);
	pParticlesys->Reset();
}

void CDlgParticleParam::OnBnClickedCheckAligntopath()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	if(m_bAlignToPath)
	{
		m_bAlignToMotion = false;
		m_bParticleRandAngle = false;
	}
	pParticlesys->GetPSL()->m_nAlignToMotion = m_bAlignToMotion;
	pParticlesys->GetPSL()->m_nAlignToPath = m_bAlignToPath;
	pParticlesys->GetPSL()->m_nParticleRandAngle = m_bParticleRandAngle;
	m_sliderAlignToMotion.EnableWindow(m_bAlignToPath);

	m_sliderParticleSpinStart.EnableWindow(!m_bAlignToPath);
	m_sliderParticleSpinEnd.EnableWindow(!m_bAlignToPath);
	m_sliderParticleSpinVar.EnableWindow(!m_bAlignToPath);
	m_sliderSpinRandom.EnableWindow(!m_bAlignToPath);
	UpdateData(false);
	pParticlesys->Reset();
}

void CDlgParticleParam::OnBnClickedCheckRandangleself()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);

	pParticlesys->GetPSL()->m_nParticleRandAngleSelf = m_bParticleRandAngleSelf;

	m_sliderParticleAngleStart.EnableWindow(!m_bParticleRandAngleSelf);
	m_sliderParticleAngleEnd.EnableWindow(!m_bParticleRandAngleSelf);
	m_sliderParticleAngleVar.EnableWindow(!m_bParticleRandAngleSelf);

	pParticlesys->Reset();
}


void CDlgParticleParam::OnBnClickedButtonStartcolor()
{
	CColorDialog dlg;
	if( IDOK != dlg.DoModal() )
		return;

	COLORREF color = dlg.GetColor(); //BGR --> ARGB (alpha值设为255)
	//	m_dwBKcolor = (0xffffffff & 0xff000000) | (color & 0x000000ff)<<16 | (color & 0x0000ff00) | (color & 0x00ff0000)>>16;

	m_nParColorStrtR = color & 0x000000ff;
	m_nParColorStrtG = (color & 0x0000ff00) >> 8;
	m_nParColorStrtB = (color & 0x00ff0000) >> 16;

	UpdateData(false); //变量到控件..

	m_sliderParticleColorStartR.SetPos(m_nParColorStrtR);
	m_sliderParticleColorStartG.SetPos(m_nParColorStrtG);
	m_sliderParticleColorStartB.SetPos(m_nParColorStrtB);

	if(m_bLinkColor)
	{
		m_nParColorEndR = m_nParColorStrtR;
		m_nParColorEndG = m_nParColorStrtG;
		m_nParColorEndB = m_nParColorStrtB;
		m_sliderParticleColorEndR.SetPos(m_nParColorStrtR);
		m_sliderParticleColorEndG.SetPos(m_nParColorStrtG);
		m_sliderParticleColorEndB.SetPos(m_nParColorStrtB);
	}

	Invalidate(true);
	SetParaListToParticleSys();
}

void CDlgParticleParam::OnBnClickedButtonEndcolor()
{
	CColorDialog dlg;
	if( IDOK != dlg.DoModal() )
		return;

	COLORREF color = dlg.GetColor(); //BGR --> ARGB (alpha值设为255)
	//	m_dwBKcolor = (0xffffffff & 0xff000000) | (color & 0x000000ff)<<16 | (color & 0x0000ff00) | (color & 0x00ff0000)>>16;

	m_nParColorEndR = color & 0x000000ff;
	m_nParColorEndG = (color & 0x0000ff00) >> 8;
	m_nParColorEndB = (color & 0x00ff0000) >> 16;

	UpdateData(false); //变量到控件..

	m_sliderParticleColorEndR.SetPos(m_nParColorEndR);
	m_sliderParticleColorEndG.SetPos(m_nParColorEndG);
	m_sliderParticleColorEndB.SetPos(m_nParColorEndB);

	if(m_bLinkColor)
	{
		m_nParColorStrtR = m_nParColorEndR;
		m_nParColorStrtG = m_nParColorEndG;
		m_nParColorStrtB = m_nParColorEndB;

		m_sliderParticleColorStartR.SetPos(m_nParColorEndR);
		m_sliderParticleColorStartG.SetPos(m_nParColorEndG);
		m_sliderParticleColorStartB.SetPos(m_nParColorEndB);
	}

	Invalidate(true);
	SetParaListToParticleSys();
}

void CDlgParticleParam::OnBnClickedCheckColorchange()
{
	UpdateData(true);

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_nParticleColorChange = m_bParticleColorChange;

	if(m_bParticleColorChange)
	{
		m_bParticleColorRand = false;
		pParticlesys->GetPSL()->m_nParticleColorRand = false;
		UpdateData(false);
	}
	pParticlesys->Reset();
}

void CDlgParticleParam::OnBnClickedCheckRandcolor()
{
	UpdateData(true);

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_nParticleColorRand = m_bParticleColorRand;

	if(m_bParticleColorRand)
	{
		m_bParticleColorChange = false;
		pParticlesys->GetPSL()->m_nParticleColorChange = false;
		UpdateData(false);
	}
	pParticlesys->Reset();
}

COLORREF CDlgParticleParam::GetParticleVarColor(float dis)
{
	BYTE r1 = m_sliderParticleColorStartR.GetPos();	//0--255
	BYTE g1 = m_sliderParticleColorStartG.GetPos();	//0--255
	BYTE b1 = m_sliderParticleColorStartB.GetPos();	//0--255
	BYTE r2 = m_sliderParticleColorEndR.GetPos();	//0--255
	BYTE g2 = m_sliderParticleColorEndG.GetPos();	//0--255
	BYTE b2 = m_sliderParticleColorEndB.GetPos();	//0--255
	float var = m_sliderParticleColorVar.GetPos() /100.0f;	//0--100 --1.0f

	//	dis = powf(dis, var);

	float coefdis = Nuclear::GetPowfLerpValue(dis, var);

	// 	float tempvar = var;
	// 	float tempdis = dis;
	// 	if(var>0.5f)
	// 	{
	// 		tempvar = 1.0f - var;
	// 		tempdis = 1.0f - dis;
	// 	}
	// 	int ndis = (int)(tempdis*40);
	// 	float fcoef = tempdis*40 - ndis;
	// 	int nvar = (int)(tempvar*20);	
	// 	float dis1 = Nuclear::XP_POWF[ndis][nvar];
	// 	int i = (ndis+1)>40 ? 40:(ndis+1);
	// 	float dis2 = Nuclear::XP_POWF[i][nvar];
	// 	float coefdis = (1-fcoef)*dis1 + fcoef*dis2;
	// 	
	// 	if(var>0.5f)
	// 		coefdis = 1.0f-coefdis;

	/*
	// 	var *= 2.0f;
	// 	if( fabs(var-1.0f) > 0.000001f )
	// 	{ //这个代数逼近在某些区间误差比较大...
	// 		float a = 1-dis;
	// 		float b = a*a*var*(var-1);
	// 		dis = 1-var*a+b/2-b*(var-2)*a/6;
	// 	}
	*/
	/*
	if(fabs(dis)<0.00001){
	dis=1;
	}else{
	int m=0;
	while(dis<1){
	dis*=2;
	m-=1;
	}
	dis=(dis-1)/(1+dis);
	float r=2*(dis+dis*dis*dis/3+dis*dis*dis*dis*dis/5)+m*log(2.0f);

	r*=var;	
	int intG=(int)r;
	float z=r-intG;
	if(z<0){
	intG-=1;
	z+=1;
	}
	dis =(1+z+z*z/2+z*z*z/6+z*z*z*z/24+z*z*z*z*z/120)*exp(intG*1.0f);
	}	
	*/

	BYTE r = (BYTE)( coefdis*r1 + (1.0-coefdis)*r2 );
	BYTE g = (BYTE)( coefdis*g1 + (1.0-coefdis)*g2 );
	BYTE b = (BYTE)( coefdis*b1 + (1.0-coefdis)*b2 );
	COLORREF color = RGB(r, g, b);
	return color;
}

void CDlgParticleParam::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: 在此处添加消息处理程序代码
	// 不为绘图消息调用 CDialog::OnPaint()

	int oldmode = dc.SetBkMode(TRANSPARENT);

	RECT rct;
	m_wndShowParticleColor.GetWindowRect(&rct);
	ScreenToClient(&rct);

	int cx = rct.right - rct.left;
	int cy = rct.bottom - rct.top;
	COLORREF crColor; 

	CDC MemDC;
	CBitmap Bmp;
	Bmp.CreateCompatibleBitmap(&dc, cx, cy);
	MemDC.CreateCompatibleDC(&dc);		
	CBitmap* pOldBitmap = MemDC.SelectObject(&Bmp);

	for(int j=0; j<cy; j++)
	{
		crColor = GetParticleVarColor( 1.0f - j/(float)cy );
		for(int i=0; i<cx; i++)
		{
			MemDC.SetPixel(i, j, crColor); 
		}
	}
	dc.StretchBlt(rct.left, rct.top, cx, cy, &MemDC, 0, 0, cx, cy, SRCCOPY);
				// +m_nHScrollPos   +m_nVScrollPos
	MemDC.SelectObject(pOldBitmap);
	MemDC.DeleteDC();
	dc.SetBkMode(oldmode);

	m_wndShowParticleTexture.GetWindowRect(&rct);
	ScreenToClient(&rct);

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	pDoc->GetParticleParameterView()->DrawParticleTexture(&dc, rct);
}

void CDlgParticleParam::OnBnClickedResetrotatecenterpos()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	CRect rct;
	m_wndShowParticleTexture.GetWindowRect(&rct);
	ScreenToClient(&rct);
	m_fRotateCentX = 0.0f;
	m_fRotateCentY = 0.0f;
	InvalidateRect(&rct, true);
	pParticlesys->GetPSL()->m_fRotateCentX = m_fRotateCentX;
	pParticlesys->GetPSL()->m_fRotateCentY = m_fRotateCentY;
}

void CDlgParticleParam::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	//原始图片中心为坐标原点(0,0),左上角(-0.5,-0.5),右下角(0.5,0.5)
	//m_rctSetRotatePos左上角为(-1,-1),右下角(1,1)

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	CRect rct;
	m_wndShowParticleTexture.GetWindowRect(&rct);
	ScreenToClient(&rct);

	if( rct.PtInRect(point) )
	{
		int centx = (rct.left + rct.right)/2;
		int centy = (rct.top + rct.bottom)/2;
		int cx = rct.Width();
		int cy = rct.Height();
		m_fRotateCentX = 2.0f*(point.x - centx)/cx;
		m_fRotateCentY = 2.0f*(point.y - centy)/cy;

		InvalidateRect(&rct, true);
		pParticlesys->GetPSL()->m_fRotateCentX = m_fRotateCentX;
		pParticlesys->GetPSL()->m_fRotateCentY = m_fRotateCentY;

		pParticlesys->Reset();
	}

	CDialog::OnLButtonDblClk(nFlags, point);
}

void CDlgParticleParam::OnBnClickedCheckAligntoemitter()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	XPASSERT(pDoc);
	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	pParticlesys->GetPSL()->m_nAlignToEmitter = m_bAlignToEmitter;
	pParticlesys->Reset();
}
