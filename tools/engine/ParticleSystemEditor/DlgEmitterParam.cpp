// DlgEmitterParam.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "DlgEmitterParam.h"


#include "ParticleSystemEditorDoc.h"
#include "MainFrm.h"
using namespace Nuclear;
// CDlgEmitterParam 对话框

IMPLEMENT_DYNAMIC(CDlgEmitterParam, CDialog)

CDlgEmitterParam::CDlgEmitterParam(CWnd* pParent /*=NULL*/)
	: CDialog(CDlgEmitterParam::IDD, pParent)
	, m_bEmitterType(TRUE)
	, m_bPathOrder(FALSE)
	, m_bParticleFileMode(FALSE)
	, m_bMotionToPath(FALSE)
	, m_bUVmirrorh(FALSE)
	, m_bUVmirrorv(FALSE)
	, m_nBlendAddMode(FALSE)
	, m_bSingleParticleAni(FALSE)
	, m_bParticleAniTextureIndex1(FALSE)
{
	m_nSysPosX = 400;					//0--1280
	m_nSysPosY = 300;					//0--1024
	m_nSysPosX2 = 100;					//0--1280
	m_nSysPosY2 = 100;					//0--1024
	m_nSysPosRadiusX = 100;
	m_nSysPosRadiusY = 100;
	m_nSysPosWidth = 100;
	m_nSysPosHeight = 100;
	m_fEmitterSpin = 0.0f; //角度

	m_fSysLife = 5.0f;					//0--100 ---10.0f
	m_nEmissionRate = 100;				//0--1000
	m_nEmitterSpeed = 100;				//0--1000
	m_nSysMaxParticles = 1000;
	m_nBlendMode = 0;

	m_fAniSpeedRand = 0.0f;
	m_nAniPlaySpeed = 10;
	m_fPSLzoom = 1.0f;					//0.0f--5.0f --default 1.0f

}

CDlgEmitterParam::~CDlgEmitterParam()
{
}

void CDlgEmitterParam::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Check(pDX, IDC_CHECK_EMITTERTYPE, m_bEmitterType);
	DDX_Control(pDX, IDC_COMBO_EffectType, m_comboEffectType);
	DDX_Control(pDX, IDC_COMBO_PSLTYPE, m_comboPslType);
	DDX_Check(pDX, IDC_CHECK_ORDER, m_bPathOrder);
	DDX_Check(pDX, IDC_CHECK_FILLMODE, m_bParticleFileMode);

	DDX_Control(pDX, IDC_SLIDER_SYSTEMPOSITION_POSX, m_sliderSystemPosX);
	DDX_Control(pDX, IDC_SLIDER_SYSTEMPOSITION_POSY, m_sliderSystemPosY);
	DDX_Control(pDX, IDC_SLIDER_SYSTEMPOSITION_POSX2, m_sliderSystemPosX2);
	DDX_Control(pDX, IDC_SLIDER_SYSTEMPOSITION_POSY2, m_sliderSystemPosY2);
	DDX_Control(pDX, IDC_SLIDER_SYSTEMPOSITION_RADIUSX, m_sliderSysPosRadiusX);
	DDX_Control(pDX, IDC_SLIDER_SYSTEMPOSITION_RADIUSY, m_sliderSysPosRadiusY);
	DDX_Control(pDX, IDC_SLIDER_SYSTEMPOSITION_POSWIDTH, m_sliderSysPosWidth);
	DDX_Control(pDX, IDC_SLIDER_SYSTEMPOSITION_POSHEIGHT, m_sliderSysPosHeight);
	DDX_Control(pDX, IDC_SLIDER_EMITTERSPIN, m_sliderEmitterSpin);

	DDX_Text(pDX, IDC_EDIT_SysPosX, m_nSysPosX);
	DDV_MinMaxInt(pDX, m_nSysPosX, 0, 1280);
	DDX_Text(pDX, IDC_EDIT_SysPosY, m_nSysPosY);
	DDV_MinMaxInt(pDX, m_nSysPosY, 0, 1024);
	DDX_Text(pDX, IDC_EDIT_SysPosX2, m_nSysPosX2);
	DDV_MinMaxInt(pDX, m_nSysPosX2, 0, 1280);
	DDX_Text(pDX, IDC_EDIT_SysPosY2, m_nSysPosY2);
	DDV_MinMaxInt(pDX, m_nSysPosY2, 0, 1024);
	DDX_Text(pDX, IDC_EDIT_SysPosRadiusX, m_nSysPosRadiusX);
	DDV_MinMaxInt(pDX, m_nSysPosRadiusX, 0, 600);
	DDX_Text(pDX, IDC_EDIT_SysPosRadiusY, m_nSysPosRadiusY);
	DDV_MinMaxInt(pDX, m_nSysPosRadiusY, 0, 600);
	DDX_Text(pDX, IDC_EDIT_SysPosWidth, m_nSysPosWidth);
	DDV_MinMaxInt(pDX, m_nSysPosWidth, 0, 1280);
	DDX_Text(pDX, IDC_EDIT_SysPosHeight, m_nSysPosHeight);
	DDV_MinMaxInt(pDX, m_nSysPosHeight, 0, 1024);
	DDX_Text(pDX, IDC_EDIT_EMITTERSPIN, m_fEmitterSpin);
	DDV_MinMaxFloat(pDX, m_fEmitterSpin, -360.0f, 360.0f);

	DDX_Check(pDX, IDC_CHECK_MotionToPath, m_bMotionToPath);
	DDX_Control(pDX, IDC_COMBO_ParticlePathInitPos, m_comboParticlePathInitPosType);

	DDX_Control(pDX, IDC_SLIDER_SYSTEMLIFETIME, m_sliderSystemLifeTime);
	DDX_Control(pDX, IDC_SLIDER_SYSTEMEMISSIONRATE, m_sliderEmissionRate);
	DDX_Control(pDX, IDC_SLIDER_SYSMAXPARTICLES, m_sliderSysMaxParticles);
	DDX_Control(pDX, IDC_SLIDER_EMITTERSPEED, m_sliderEmitterSpeed);
	DDX_Text(pDX, IDC_EDIT_SysLife, m_fSysLife);
	DDV_MinMaxFloat(pDX, m_fSysLife, 0, 100);
	DDX_Text(pDX, IDC_EDIT_SysMaxParticles, m_nSysMaxParticles);
	DDV_MinMaxInt(pDX, m_nSysMaxParticles, 1, 2000);
	DDX_Text(pDX, IDC_EDIT_EmissionRate, m_nEmissionRate);
	DDV_MinMaxInt(pDX, m_nEmissionRate, 0, 1000);
	DDX_Text(pDX, IDC_EDIT_EMITTERSPEED, m_nEmitterSpeed);
	DDV_MinMaxInt(pDX, m_nEmitterSpeed, 0, 1000);

	DDX_Check(pDX, IDC_CHECK_UVMIRRORH, m_bUVmirrorh);
	DDX_Check(pDX, IDC_CHECK_UVMIRRORV, m_bUVmirrorv);
	DDX_Control(pDX, IDC_COMBO_BLENDMODE_SRC, m_comboBlendModeSrc);
	DDX_Radio(pDX, IDC_RADIO_BLEND, m_nBlendAddMode);
	DDX_Check(pDX, IDC_CHECK_SingleParticleAni, m_bSingleParticleAni);
	DDX_Check(pDX, IDC_CHECK_ParticleANI_1, m_bParticleAniTextureIndex1);

	DDX_Control(pDX, IDC_SLIDER_ANIPLAYSPEED, m_sliderAniPlaySpeed);
	DDX_Text(pDX, IDC_EDIT_AniPlaySpeed, m_nAniPlaySpeed);
	DDV_MinMaxInt(pDX, m_nAniPlaySpeed, 0, 50);
	DDX_Text(pDX, IDC_EDIT_AniSpeedRand, m_fAniSpeedRand);
	DDV_MinMaxFloat(pDX, m_fAniSpeedRand, 0.0f, 10.0f);
	DDX_Control(pDX, IDC_SLIDER_AniSpeedRand, m_sliderAniSpeedRand);

	DDX_Control(pDX, IDC_SLIDER_PSLZOOM, m_sliderPSLzoom);
	DDX_Text(pDX, IDC_EDIT_PSLZOOM, m_fPSLzoom);
	DDV_MinMaxFloat(pDX, m_fPSLzoom, 0.0f, 5.0f);

	DDX_Control(pDX, IDC_COMBO_CONFIGUREREQUIRE, m_comboConfigReq);

}


BEGIN_MESSAGE_MAP(CDlgEmitterParam, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgEmitterParam::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CDlgEmitterParam::OnBnClickedCancel)
	ON_BN_CLICKED(IDC_CHECK_EMITTERTYPE, &CDlgEmitterParam::OnBnClickedCheckEmittertype)
	ON_CBN_SELCHANGE(IDC_COMBO_EffectType, &CDlgEmitterParam::OnCbnSelchangeComboEffecttype)
	ON_CBN_SELCHANGE(IDC_COMBO_PSLTYPE, &CDlgEmitterParam::OnCbnSelchangeComboPsltype)
	ON_BN_CLICKED(IDC_CHECK_ORDER, &CDlgEmitterParam::OnBnClickedCheckOrder)
	ON_BN_CLICKED(IDC_CHECK_FILLMODE, &CDlgEmitterParam::OnBnClickedCheckFillmode)
	ON_WM_HSCROLL()

	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_SysPosX, IDC_EDIT_VertexCol, &CDlgEmitterParam::OnEnKillFocus)

	ON_BN_CLICKED(IDC_CHECK_MotionToPath, &CDlgEmitterParam::OnBnClickedCheckMotiontopath)
	ON_CBN_SELCHANGE(IDC_COMBO_ParticlePathInitPos, &CDlgEmitterParam::OnCbnSelchangeComboParticlepathinitpos)

	ON_BN_CLICKED(IDC_CHECK_UVMIRRORH, &CDlgEmitterParam::OnBnClickedCheckUvmirrorh)
	ON_BN_CLICKED(IDC_CHECK_UVMIRRORV, &CDlgEmitterParam::OnBnClickedCheckUvmirrorv)
	ON_CBN_SELCHANGE(IDC_COMBO_BLENDMODE_SRC, &CDlgEmitterParam::OnCbnSelchangeComboBlendmode)

	ON_BN_CLICKED(IDC_RADIO_BLEND, &CDlgEmitterParam::OnBnClickedRadioBlend)
	ON_BN_CLICKED(IDC_RADIO_ADD, &CDlgEmitterParam::OnBnClickedRadioAdd)
	ON_BN_CLICKED(IDC_CHECK_SingleParticleAni, &CDlgEmitterParam::OnBnClickedCheckSingleparticleani)
	ON_BN_CLICKED(IDC_CHECK_ParticleANI_1, &CDlgEmitterParam::OnBnClickedCheckParticleani1)

	ON_CBN_SELCHANGE(IDC_COMBO_CONFIGUREREQUIRE, &CDlgEmitterParam::OnCbnSelchangeComboConfigurerequire)

	ON_BN_CLICKED(IDC_RADIO_ONEADD, &CDlgEmitterParam::OnBnClickedRadioOneadd)
END_MESSAGE_MAP()


// CDlgEmitterParam 消息处理程序

void CDlgEmitterParam::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnOK();
}

void CDlgEmitterParam::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnCancel();
}

CDocument* CDlgEmitterParam::GetEditorDoc()
{
	CMainFrame* pwnd = (CMainFrame*)AfxGetMainWnd();
	CFrameWnd* pchildWnd = pwnd->GetActiveFrame(); 
	CDocument* pCurrentDoc = pchildWnd->GetActiveDocument();

	return pCurrentDoc;//(CParticleSystemEditorDoc*)
}

void CDlgEmitterParam::SetCtrlPos() //打开文件时调用
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	m_bEmitterType = pParticlesys->GetPSL()->m_nEmitterType;
	m_comboEffectType.SetCurSel(pParticlesys->GetPSL()->m_nMoveType);
	m_comboPslType.SetCurSel(pParticlesys->GetPSL()->m_nPosType);
	m_nPslType = pParticlesys->GetPSL()->m_nPosType;
	m_bParticleFileMode = pParticlesys->GetPSL()->m_nParFillMode;
	m_comboParticlePathInitPosType.SetCurSel(pParticlesys->GetPSL()->m_nParticlePathInitPosType);

	m_fSysLife = pParticlesys->GetPSL()->m_fSysLife;					
	m_nEmissionRate = pParticlesys->GetPSL()->m_nEmissionRate;		
	m_nEmitterSpeed = pParticlesys->GetPSL()->m_nEmitterSpeed;				
	m_nSysMaxParticles = pParticlesys->GetPSL()->m_nSysMaxParticles;
	m_sliderSystemLifeTime.SetPos((int)(m_fSysLife *10));
	m_sliderEmissionRate.SetPos(m_nEmissionRate);
	m_sliderEmitterSpeed.SetPos(m_nEmitterSpeed);
	m_sliderSysMaxParticles.SetPos(m_nSysMaxParticles);
	
	m_bMotionToPath = pParticlesys->GetPSL()->m_nMotionToPath;
	m_bPathOrder = pParticlesys->GetPSL()->m_nPathOrder;

	bool enableFillMode = (m_nPslType == Nuclear::PSLTYPE_CIRCLE) || (m_nPslType == Nuclear::PSLTYPE_RECT);
	if( !enableFillMode )
		m_bParticleFileMode = false;
	else
		m_bPathOrder = false;
	UpdateData(false);
	((CButton*)GetDlgItem(IDC_BUTTON_SETCUSTOMPATH))->EnableWindow(m_nPslType==Nuclear::PSLTYPE_CUSTOMPATH);
	((CButton*)GetDlgItem(IDC_CHECK_FILLMODE))->EnableWindow(enableFillMode);
	m_comboParticlePathInitPosType.EnableWindow(m_nPslType==Nuclear::PSLTYPE_CUSTOMPATH);

	bool bOrderPath = (m_nPslType == Nuclear::PSLTYPE_CUSTOMPATH) || (m_nPslType == Nuclear::PSLTYPE_RECT);
	((CButton*)GetDlgItem(IDC_CHECK_ORDER))->EnableWindow(bOrderPath);
	((CButton*)GetDlgItem(IDC_CHECK_MotionToPath))->EnableWindow(bOrderPath);

	m_sliderSystemPosX.EnableWindow(true);
	m_sliderSystemPosY.EnableWindow(true);
	m_sliderSystemPosX2.EnableWindow(true);
	m_sliderSystemPosY2.EnableWindow(true);
	m_sliderSysPosRadiusX.EnableWindow(true);
	m_sliderSysPosRadiusY.EnableWindow(true);
	m_sliderSysPosWidth.EnableWindow(true);
	m_sliderSysPosHeight.EnableWindow(true);

	switch(m_nPslType)
	{
	case Nuclear::PSLTYPE_POINT:
	case Nuclear::PSLTYPE_CUSTOMPATH:
		m_sliderSystemPosX2.EnableWindow(false);
		m_sliderSystemPosY2.EnableWindow(false);
		m_sliderSysPosRadiusX.EnableWindow(false);
		m_sliderSysPosRadiusY.EnableWindow(false);
		m_sliderSysPosWidth.EnableWindow(false);
		m_sliderSysPosHeight.EnableWindow(false);
		break;
	case Nuclear::PSLTYPE_LINE:
		m_sliderSysPosRadiusX.EnableWindow(false);
		m_sliderSysPosRadiusY.EnableWindow(false);
		m_sliderSysPosWidth.EnableWindow(false);
		m_sliderSysPosHeight.EnableWindow(false);
		break;
	case Nuclear::PSLTYPE_CIRCLE:
		m_sliderSystemPosX2.EnableWindow(false);
		m_sliderSystemPosY2.EnableWindow(false);
		m_sliderSysPosWidth.EnableWindow(false);
		m_sliderSysPosHeight.EnableWindow(false);
		break;
	case Nuclear::PSLTYPE_RECT:
		m_sliderSystemPosX2.EnableWindow(false);
		m_sliderSystemPosY2.EnableWindow(false);
		m_sliderSysPosRadiusX.EnableWindow(false);
		m_sliderSysPosRadiusY.EnableWindow(false);
		break;
	default:
		break;
	}

	m_nSysPosX = (int)pParticlesys->GetPSL()->m_point0.x;					
	m_nSysPosY = (int)pParticlesys->GetPSL()->m_point0.y;	
	m_nSysPosX2 = (int)pParticlesys->GetPSL()->m_point1.x;					
	m_nSysPosY2 = (int)pParticlesys->GetPSL()->m_point1.y;		
	m_nSysPosRadiusX = pParticlesys->GetPSL()->m_nSysPosRadiusX;
	m_nSysPosRadiusY = pParticlesys->GetPSL()->m_nSysPosRadiusY;
	m_nSysPosWidth = (int)pParticlesys->GetPSL()->m_rct.Width();
	m_nSysPosHeight = (int)pParticlesys->GetPSL()->m_rct.Height();						
	m_fEmitterSpin = pParticlesys->GetPSL()->m_fEmitterSpin;	

	m_sliderSystemPosX.SetPos(m_nSysPosX);
	m_sliderSystemPosY.SetPos(m_nSysPosY);
	m_sliderSystemPosX2.SetPos(m_nSysPosX2);
	m_sliderSystemPosY2.SetPos(m_nSysPosY2);
	m_sliderSysPosRadiusX.SetPos(m_nSysPosRadiusX);
	m_sliderSysPosRadiusY.SetPos(m_nSysPosRadiusY);
	m_sliderSysPosWidth.SetPos(m_nSysPosWidth);
	m_sliderSysPosHeight.SetPos(m_nSysPosHeight);
	m_sliderEmitterSpin.SetPos((int)(m_fEmitterSpin *10));

	m_bUVmirrorh = pParticlesys->GetPSL()->m_nUVmirrorh;
	m_bUVmirrorv = pParticlesys->GetPSL()->m_nUVmirrorv;

	for(int i=0; i<Nuclear::NUMBLENDINGMODES; i++)
	{
		if(Nuclear::g_BlendingModes[i].mode == pParticlesys->GetPSL()->m_nBlendMode)
		{
			//	CString str = Nuclear::g_BlendingModes[i].name;
			int nIndex = m_comboBlendModeSrc.FindString(-1, Nuclear::g_BlendingModes[i].name);
			m_comboBlendModeSrc.SetCurSel(nIndex);
		}
	}

	int nmode = pParticlesys->GetPSL()->m_nDestBlendMode;
	switch(nmode)
	{
	case PSLRENDSTATE_ALPHA:
		m_nBlendAddMode = 0;
		break;
	case PSLRENDSTATE_LIGHTING:
		m_nBlendAddMode = 1;
		break;
	case PSLRENDSTATE_ADD:
		m_nBlendAddMode = 2;
	    break;
	default:
	    break;
	}

	m_bSingleParticleAni = pParticlesys->GetPSL()->m_nSingleParticleAni;
	m_bParticleAniTextureIndex1 = pParticlesys->GetPSL()->m_nParticleAniTextureIndex1;

	m_sliderAniPlaySpeed.EnableWindow(m_bSingleParticleAni);
	m_sliderAniSpeedRand.EnableWindow(m_bSingleParticleAni);
	((CButton*)GetDlgItem(IDC_CHECK_ParticleANI_1))->EnableWindow(m_bSingleParticleAni);

	m_nAniPlaySpeed = pParticlesys->GetPSL()->m_nAniPlaySpeed;
	m_fAniSpeedRand = pParticlesys->GetPSL()->m_fAniSpeedRand;
	m_sliderAniPlaySpeed.SetPos(m_nAniPlaySpeed);
	m_sliderAniSpeedRand.SetPos( (int)(m_fAniSpeedRand *100) );

	m_fPSLzoom = pParticlesys->GetPSL()->m_fPSLzoom;			
	m_sliderPSLzoom.SetPos((int)(m_fPSLzoom *100));

	m_comboConfigReq.SetCurSel(pParticlesys->GetPSL()->m_nPSConfigReq);

	UpdateData(false);
}

void CDlgEmitterParam::InitCtrl()
{
	m_comboEffectType.ResetContent();
	m_comboEffectType.InsertString(0, L"DEFAULT");
	m_comboEffectType.InsertString(1, L"FOLLOWSPRITE");
	m_comboEffectType.InsertString(2, L"FULLSCREEN");
	m_comboEffectType.SetCurSel(0);

	m_comboPslType.ResetContent();
	m_comboPslType.InsertString(0, L"POINT");
	m_comboPslType.InsertString(1, L"LINE");
	m_comboPslType.InsertString(2, L"CIRCLE");
	m_comboPslType.InsertString(3, L"RECT");
	m_comboPslType.InsertString(4, L"CUSTOMPATH");	
	m_comboPslType.SetCurSel(0);

	m_comboParticlePathInitPosType.ResetContent();
	m_comboParticlePathInitPosType.InsertString(0, L"路径头");
	m_comboParticlePathInitPosType.InsertString(1, L"随机");
	m_comboParticlePathInitPosType.InsertString(2, L"上次消失点");
	m_comboParticlePathInitPosType.SetCurSel(0);

	m_comboBlendModeSrc.ResetContent();
	for(int i=0; i<Nuclear::NUMBLENDINGMODES; i++)
	{
		m_comboBlendModeSrc.InsertString( i, Nuclear::g_BlendingModes[i].name );
	}
	m_comboBlendModeSrc.SetCurSel(0);

	m_comboConfigReq.ResetContent();
	m_comboConfigReq.InsertString(0, L"低配置");
	m_comboConfigReq.InsertString(1, L"中配置");
	m_comboConfigReq.InsertString(2, L"高配置");
	m_comboConfigReq.SetCurSel(0);


	m_nPslType = 0; //点模式
	m_bParticleFileMode = false;

	((CButton*)GetDlgItem(IDC_CHECK_ORDER))->EnableWindow(false);
	((CButton*)GetDlgItem(IDC_CHECK_FILLMODE))->EnableWindow(false);
	((CButton*)GetDlgItem(IDC_BUTTON_SETCUSTOMPATH))->EnableWindow(false);

	((CButton*)GetDlgItem(IDC_CHECK_MotionToPath))->EnableWindow(false);
	m_comboParticlePathInitPosType.EnableWindow(false);

	m_sliderSystemPosX2.SetRange(0, 1280);
	m_sliderSystemPosX2.SetPos(400);
	m_sliderSystemPosY2.SetRange(0, 1024);
	m_sliderSystemPosY2.SetPos(300);
	m_sliderSystemPosX.SetRange(0, 1280);
	m_sliderSystemPosX.SetPos(400);
	m_sliderSystemPosY.SetRange(0, 1024);
	m_sliderSystemPosY.SetPos(300);
	m_sliderSysPosRadiusX.SetRange(0, 600);
	m_sliderSysPosRadiusX.SetPos(100);
	m_sliderSysPosRadiusY.SetRange(0, 600);
	m_sliderSysPosRadiusY.SetPos(100);
	m_sliderSysPosWidth.SetRange(0, 1280);
	m_sliderSysPosWidth.SetPos(100);
	m_sliderSysPosHeight.SetRange(0, 1024);
	m_sliderSysPosHeight.SetPos(100);
	m_sliderEmitterSpin.SetRange(-3600, 3600, true);	//-360 -- 360
	m_sliderEmitterSpin.SetPos(0);						//default 0.0f

	m_sliderSystemPosX2.EnableWindow(false);
	m_sliderSystemPosY2.EnableWindow(false);
	m_sliderSysPosRadiusX.EnableWindow(false);
	m_sliderSysPosRadiusY.EnableWindow(false);
	m_sliderSysPosWidth.EnableWindow(false);
	m_sliderSysPosHeight.EnableWindow(false);

	m_sliderSystemLifeTime.SetRange(0, 1000);			//0.0f--100.0f
	m_sliderSystemLifeTime.SetPos(50);
	m_sliderEmissionRate.SetRange(0, 1000);
	m_sliderEmissionRate.SetPos(100);
	m_sliderEmitterSpeed.SetRange(0, 1000);		//0--1000
	m_sliderEmitterSpeed.SetPos(100);
	m_sliderSysMaxParticles.SetRange(1, 2000);
	m_sliderSysMaxParticles.SetPos(1000);

	m_bUVmirrorh = false;
	m_bUVmirrorv = false;
	m_nBlendAddMode = 0;
	m_bSingleParticleAni = false;
	m_bParticleAniTextureIndex1 = false;

	m_sliderAniPlaySpeed.EnableWindow(m_bSingleParticleAni);
	m_sliderAniSpeedRand.EnableWindow(m_bSingleParticleAni);
	((CButton*)GetDlgItem(IDC_CHECK_ParticleANI_1))->EnableWindow(m_bSingleParticleAni);
	GetDlgItem(IDC_EDIT_AniPlaySpeed)->EnableWindow(m_bSingleParticleAni);

	m_sliderAniPlaySpeed.SetRange(0, 50);
	m_sliderAniPlaySpeed.SetPos(10);
	m_sliderAniSpeedRand.SetRange(0, 1000);
	m_sliderAniSpeedRand.SetPos(0);

	m_sliderPSLzoom.SetRange(0, 500);				//0.0f--5.0f
	m_sliderPSLzoom.SetPos(100);					//default 1.0f

	UpdateData(false);
}

BOOL CDlgEmitterParam::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化
	InitCtrl();

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CDlgEmitterParam::OnBnClickedCheckEmittertype()
{
	UpdateData(true);
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	int nType = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleSystemEditorView()->GetVertexType();
	if(nType != 3)
	{
		pParticlesys->GetPSL()->m_nEmitterType = m_bEmitterType;
		pParticlesys->Reset();
	}
}

void CDlgEmitterParam::OnCbnSelchangeComboEffecttype()
{
	int nCurSel = m_comboEffectType.GetCurSel();

	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_nMoveType = nCurSel;
}

void CDlgEmitterParam::OnCbnSelchangeComboPsltype()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	m_nPslType = m_comboPslType.GetCurSel();

	pParticlesys->GetPSL()->m_nPosType = m_nPslType; 

	((CButton*)GetDlgItem(IDC_BUTTON_SETCUSTOMPATH))->EnableWindow(m_nPslType==Nuclear::PSLTYPE_CUSTOMPATH);

	bool enableFillMode = (m_nPslType == Nuclear::PSLTYPE_CIRCLE) || (m_nPslType == Nuclear::PSLTYPE_RECT);
	if( !enableFillMode )
		m_bParticleFileMode = false;
	else
		m_bPathOrder = false;
	UpdateData(false);

	bool bOrderPath = (m_nPslType == Nuclear::PSLTYPE_CUSTOMPATH) || (m_nPslType == Nuclear::PSLTYPE_RECT);

	((CButton*)GetDlgItem(IDC_CHECK_ORDER))->EnableWindow(bOrderPath);
	((CButton*)GetDlgItem(IDC_CHECK_MotionToPath))->EnableWindow(bOrderPath);
	m_comboParticlePathInitPosType.EnableWindow(m_nPslType==Nuclear::PSLTYPE_CUSTOMPATH);

	((CButton*)GetDlgItem(IDC_CHECK_FILLMODE))->EnableWindow(enableFillMode);

	m_sliderSystemPosX.EnableWindow(true);
	m_sliderSystemPosY.EnableWindow(true);
	m_sliderSystemPosX2.EnableWindow(true);
	m_sliderSystemPosY2.EnableWindow(true);
	m_sliderSysPosRadiusX.EnableWindow(true);
	m_sliderSysPosRadiusY.EnableWindow(true);
	m_sliderSysPosWidth.EnableWindow(true);
	m_sliderSysPosHeight.EnableWindow(true);

	switch(m_nPslType)
	{
	case Nuclear::PSLTYPE_POINT:
	case Nuclear::PSLTYPE_CUSTOMPATH:
		m_sliderSystemPosX2.EnableWindow(false);
		m_sliderSystemPosY2.EnableWindow(false);
		m_sliderSysPosRadiusX.EnableWindow(false);
		m_sliderSysPosRadiusY.EnableWindow(false);
		m_sliderSysPosWidth.EnableWindow(false);
		m_sliderSysPosHeight.EnableWindow(false);
		break;
	case Nuclear::PSLTYPE_LINE:
		m_sliderSysPosRadiusX.EnableWindow(false);
		m_sliderSysPosRadiusY.EnableWindow(false);
		m_sliderSysPosWidth.EnableWindow(false);
		m_sliderSysPosHeight.EnableWindow(false);
		break;
	case Nuclear::PSLTYPE_CIRCLE:
		m_sliderSystemPosX2.EnableWindow(false);
		m_sliderSystemPosY2.EnableWindow(false);
		m_sliderSysPosWidth.EnableWindow(false);
		m_sliderSysPosHeight.EnableWindow(false);
		break;
	case Nuclear::PSLTYPE_RECT:
		m_sliderSystemPosX2.EnableWindow(false);
		m_sliderSystemPosY2.EnableWindow(false);
		m_sliderSysPosRadiusX.EnableWindow(false);
		m_sliderSysPosRadiusY.EnableWindow(false);
		break;
	default:
		break;
	}

	SetParaListToParticleSys();
	pParticlesys->Reset();
}

void CDlgEmitterParam::OnBnClickedCheckOrder()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	pParticlesys->GetPSL()->m_nPathOrder = m_bPathOrder;
	pParticlesys->Reset();
}

void CDlgEmitterParam::OnBnClickedCheckFillmode()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	pParticlesys->GetPSL()->m_nParFillMode = m_bParticleFileMode;
	pParticlesys->Reset();
}

void CDlgEmitterParam::CheckLinkButton()
{
}

void CDlgEmitterParam::SetParaListToParticleSys()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_point0.x			= (float)m_nSysPosX;
	pParticlesys->GetPSL()->m_point0.y			= (float)m_nSysPosY; 
	pParticlesys->GetPSL()->m_point1.x			= (float)m_nSysPosX2;
	pParticlesys->GetPSL()->m_point1.y			= (float)m_nSysPosY2;
	pParticlesys->GetPSL()->m_nSysPosRadiusX	= m_nSysPosRadiusX;
	pParticlesys->GetPSL()->m_nSysPosRadiusY	= m_nSysPosRadiusY;
	pParticlesys->GetPSL()->m_fEmitterSpin		= m_fEmitterSpin;

	int dx = m_nSysPosWidth/2;
	int dy = m_nSysPosHeight/2;
	pParticlesys->GetPSL()->m_rct = Nuclear::CRECT(-dx, -dy, dx, dy);

	pParticlesys->GetPSL()->m_fSysLife			= m_fSysLife;// /10.0f;
	pParticlesys->GetPSL()->m_nEmitterSpeed	= m_nEmitterSpeed;
	if(pParticlesys->GetPSL()->m_nSysMaxParticles != m_nSysMaxParticles && pParticlesys->GetPSL()->m_nAbsrtactPsl != 1)
	{
		pParticlesys->GetPSL()->m_nSysMaxParticles	= m_nSysMaxParticles;
		Nuclear::CGeneralParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<Nuclear::CGeneralParticleSystem*>(pParticlesys) )
			pPS->ResizeParticleArry(m_nSysMaxParticles, m_nEmissionRate);
	}
	if(pParticlesys->GetPSL()->m_nEmissionRate != m_nEmissionRate && pParticlesys->GetPSL()->m_nAbsrtactPsl != 1)
	{
		pParticlesys->GetPSL()->m_nEmissionRate	= m_nEmissionRate;
		Nuclear::CGeneralParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<Nuclear::CGeneralParticleSystem*>(pParticlesys) )
			pPS->ResizeParticleArry(m_nSysMaxParticles, m_nEmissionRate);
	}

	pParticlesys->GetPSL()->m_nAniPlaySpeed	= m_nAniPlaySpeed;
	pParticlesys->GetPSL()->m_fAniSpeedRand	= m_fAniSpeedRand;
	pParticlesys->GetPSL()->m_fPSLzoom	= m_fPSLzoom;

	pParticlesys->Reset();
}

void CDlgEmitterParam::OnEnKillFocus(UINT nID)
{
	UpdateData(true);

	NormalizeParam(m_nSysPosX, 0, 1280);
	NormalizeParam(m_nSysPosY, 0, 1024);
	NormalizeParam(m_nSysPosX2, 0, 1280);
	NormalizeParam(m_nSysPosY2, 0, 1024);
	NormalizeParam(m_nSysPosRadiusX, 0, 600);
	NormalizeParam(m_nSysPosRadiusY, 0, 600);
	NormalizeParam(m_nSysPosWidth, 0, 1280);
	NormalizeParam(m_nSysPosHeight, 0, 1024);
	NormalizeParam(m_fEmitterSpin, -360.0f, 360.0f);

	m_sliderSystemPosX.SetPos(m_nSysPosX);
	m_sliderSystemPosY.SetPos(m_nSysPosY);
	m_sliderSystemPosX2.SetPos(m_nSysPosX2);
	m_sliderSystemPosY2.SetPos(m_nSysPosY2);
	m_sliderSysPosRadiusX.SetPos(m_nSysPosRadiusX);
	m_sliderSysPosRadiusY.SetPos(m_nSysPosRadiusY);
	m_sliderSysPosWidth.SetPos(m_nSysPosWidth);
	m_sliderSysPosHeight.SetPos(m_nSysPosHeight);
	m_sliderEmitterSpin.SetPos((int)(m_fEmitterSpin *10));

	NormalizeParam(m_fSysLife, 0.0f, 100.0f);
	NormalizeParam(m_nEmissionRate, 0, 1000);
	NormalizeParam(m_nEmitterSpeed, 0, 1000);
	NormalizeParam(m_nSysMaxParticles, 1, 2000);
	m_sliderSystemLifeTime.SetPos((int)(m_fSysLife *10));
	m_sliderEmissionRate.SetPos(m_nEmissionRate);
	m_sliderEmitterSpeed.SetPos(m_nEmitterSpeed);
	m_sliderSysMaxParticles.SetPos(m_nSysMaxParticles);

	NormalizeParam(m_nAniPlaySpeed, 0, 50);
	NormalizeParam(m_fAniSpeedRand, 0.0f, 10.0f);
	m_sliderAniPlaySpeed.SetPos(m_nAniPlaySpeed);
	m_sliderAniSpeedRand.SetPos( (int)(m_fAniSpeedRand*100) );

	NormalizeParam(m_fPSLzoom, 0.0f, 5.0f);
	m_sliderPSLzoom.SetPos((int)(m_fPSLzoom *100));



	UpdateData(false);
	SetParaListToParticleSys();
}

void CDlgEmitterParam::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if( pScrollBar==NULL )
	{ //点击的是滚动条..
		return;
	}

	m_nSysPosX = m_sliderSystemPosX.GetPos();					//0--1280
	m_nSysPosY = m_sliderSystemPosY.GetPos();					//0--1024
	m_nSysPosX2 = m_sliderSystemPosX2.GetPos();					//0--1280
	m_nSysPosY2 = m_sliderSystemPosY2.GetPos();					//0--1024
	m_nSysPosRadiusX = m_sliderSysPosRadiusX.GetPos();
	m_nSysPosRadiusY = m_sliderSysPosRadiusY.GetPos();
	m_nSysPosWidth = m_sliderSysPosWidth.GetPos();
	m_nSysPosHeight = m_sliderSysPosHeight.GetPos();
	m_fEmitterSpin = m_sliderEmitterSpin.GetPos() *0.1f;

	m_fSysLife = m_sliderSystemLifeTime.GetPos() *0.1f;			//0--100 --10.0f
	m_nEmissionRate = m_sliderEmissionRate.GetPos();			//0--1000
	m_nEmitterSpeed = m_sliderEmitterSpeed.GetPos();
	m_nSysMaxParticles = m_sliderSysMaxParticles.GetPos();

	m_nAniPlaySpeed = m_sliderAniPlaySpeed.GetPos();
	m_fAniSpeedRand = m_sliderAniSpeedRand.GetPos() *0.01f;
	m_fPSLzoom = m_sliderPSLzoom.GetPos() *0.01f;				//0--500 --1.0f

	CheckLinkButton();
	SetParaListToParticleSys();

	UpdateData(false); //变量到控件..

	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CDlgEmitterParam::OnBnClickedCheckMotiontopath()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	pParticlesys->GetPSL()->m_nMotionToPath = m_bMotionToPath;
}

void CDlgEmitterParam::OnCbnSelchangeComboParticlepathinitpos()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;
	int nCurSel = m_comboParticlePathInitPosType.GetCurSel();
	pParticlesys->GetPSL()->m_nParticlePathInitPosType = nCurSel;
}

BOOL CDlgEmitterParam::PreTranslateMessage(MSG* pMsg)
{
	// TODO: 在此添加专用代码和/或调用基类
	int nID = GetWindowLong(pMsg->hwnd, GWL_ID);//由窗口句柄获得ID号，GetWindowLong为获得窗口的ID号。
	if(pMsg->message == WM_LBUTTONDOWN) 
	{
		switch(nID)
		{
		case IDC_BUTTON_SETCUSTOMPATH:
			((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleParameterView()->ProcessMsg(pMsg);
			return TRUE;

		default:
			break;
		}
	}

	return CDialog::PreTranslateMessage(pMsg);
}

void CDlgEmitterParam::OnBnClickedCheckUvmirrorh()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;
	UpdateData(true);
	pParticlesys->GetPSL()->m_nUVmirrorh = m_bUVmirrorh;
}

void CDlgEmitterParam::OnBnClickedCheckUvmirrorv()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;
	UpdateData(true);
	pParticlesys->GetPSL()->m_nUVmirrorv = m_bUVmirrorv;
}

void CDlgEmitterParam::OnCbnSelchangeComboBlendmode()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	int nCurSel = m_comboBlendModeSrc.GetCurSel();
	m_nBlendMode = Nuclear::g_BlendingModes[nCurSel].mode;
	pParticlesys->GetPSL()->m_nBlendMode = m_nBlendMode;
}

void CDlgEmitterParam::OnBnClickedRadioBlend()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	//default: D3DBLEND_INVSRCALPHA = 6
	UpdateData(true); //m_nBlendAddMode = 0;	
	pParticlesys->GetPSL()->m_nDestBlendMode = PSLRENDSTATE_ALPHA;
	pParticlesys->Reset();
}

void CDlgEmitterParam::OnBnClickedRadioAdd()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	//D3DBLEND_ONE = 2 
	UpdateData(true); //m_nBlendAddMode = 1;	
	pParticlesys->GetPSL()->m_nDestBlendMode = PSLRENDSTATE_LIGHTING;
	pParticlesys->Reset();
}

void CDlgEmitterParam::OnBnClickedRadioOneadd()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true); 
	pParticlesys->GetPSL()->m_nDestBlendMode = PSLRENDSTATE_ADD;
	pParticlesys->Reset();
}

void CDlgEmitterParam::OnBnClickedCheckSingleparticleani()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	pParticlesys->GetPSL()->m_nSingleParticleAni = m_bSingleParticleAni;
	m_sliderAniPlaySpeed.EnableWindow(m_bSingleParticleAni);
	GetDlgItem(IDC_EDIT_AniPlaySpeed)->EnableWindow(m_bSingleParticleAni);
	m_sliderAniSpeedRand.EnableWindow(m_bSingleParticleAni);
	GetDlgItem(IDC_EDIT_AniSpeedRand)->EnableWindow(m_bSingleParticleAni);

	((CButton*)GetDlgItem(IDC_CHECK_ParticleANI_1))->EnableWindow(m_bSingleParticleAni);
}

void CDlgEmitterParam::OnBnClickedCheckParticleani1()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	pParticlesys->GetPSL()->m_nParticleAniTextureIndex1 = m_bParticleAniTextureIndex1;
}

void CDlgEmitterParam::OnCbnSelchangeComboConfigurerequire()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	int nCurSel = m_comboConfigReq.GetCurSel();
	pParticlesys->GetPSL()->m_nPSConfigReq = nCurSel;
}
