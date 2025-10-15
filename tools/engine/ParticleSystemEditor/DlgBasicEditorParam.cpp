// DlgBasicEditorParam.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "DlgBasicEditorParam.h"

#include "ParticleSystemEditorDoc.h"
#include "MainFrm.h"

// CDlgBasicEditorParam 对话框

using namespace Nuclear;

IMPLEMENT_DYNAMIC(CDlgBasicEditorParam, CDialog)

CDlgBasicEditorParam::CDlgBasicEditorParam(CWnd* pParent /*=NULL*/)
	: CDialog(CDlgBasicEditorParam::IDD, pParent)
	, m_bSetMaxBox(FALSE)
	, m_bCycleFlag(FALSE)
	, m_bShowBKpic(TRUE)
	, m_fEmitterSpinAngle(0)
	, m_bSetEmitterSpinCentFlag(FALSE)
	, m_bSetParticleHoleFlag(FALSE)
	, m_bMoveMap(FALSE)
	, m_nLineLength(0)
	, m_nRctWidth(0)
	, m_nRctHeight(0)
	, m_bBlindArea(FALSE)
	, m_bBlockLine(FALSE)
	, m_bReflectLine(FALSE)
	, m_bInterfacePsl(FALSE)
	, m_bSkillEffectSpritePoint(FALSE)
	, m_TextureRow_Count(1)
	, m_TextureCol_Count(1)
	, m_Texture_Count(1)
{
}

CDlgBasicEditorParam::~CDlgBasicEditorParam()
{
}

void CDlgBasicEditorParam::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Check(pDX, IDC_CHECK_MAXBOXFLAG, m_bSetMaxBox);
	DDX_Check(pDX, IDC_CHECK_CYCLE, m_bCycleFlag);
	DDX_Check(pDX, IDC_CHECK_SHOWBKPIC, m_bShowBKpic);
	DDX_Text(pDX, IDC_EDIT_EmitterAngle, m_fEmitterSpinAngle);
	DDV_MinMaxFloat(pDX, m_fEmitterSpinAngle, -360.0f, 360.0f);
	DDX_Check(pDX, IDC_CHECK_SetEmitterSpinCenter, m_bSetEmitterSpinCentFlag);
	DDX_Check(pDX, IDC_CHECK_SetParticleHole, m_bSetParticleHoleFlag);
	DDX_Check(pDX, IDC_CHECK_MoveBKPic, m_bMoveMap);
	DDX_Control(pDX, IDC_SLIDER_LineLength, m_sliderLineLength);
	DDX_Control(pDX, IDC_SLIDER_RctWidth, m_sliderRctWidth);
	DDX_Control(pDX, IDC_SLIDER_RctHeight, m_sliderRctHeight);
	DDX_Text(pDX, IDC_EDIT_LineLength, m_nLineLength);
	DDV_MinMaxInt(pDX, m_nLineLength, 0, 1000);
	DDX_Text(pDX, IDC_EDIT_RctWidth, m_nRctWidth);
	DDV_MinMaxInt(pDX, m_nRctWidth, 0, 1000);
	DDX_Text(pDX, IDC_EDIT_RctHeight, m_nRctHeight);
	DDV_MinMaxInt(pDX, m_nRctHeight, 0, 200);
	DDX_Check(pDX, IDC_CHECK_BlindArea, m_bBlindArea);
	DDX_Check(pDX, IDC_CHECK_BlockLine, m_bBlockLine);
	DDX_Check(pDX, IDC_CHECK_ReflectLine, m_bReflectLine);
	DDX_Check(pDX, IDC_CHECK_InterfacialEffect, m_bInterfacePsl);
	DDX_Check(pDX, IDC_CHECK_skillEffectBottom, m_bSkillEffectSpritePoint);
	DDX_Text(pDX, IDC_EDIT_TextureRow, m_TextureRow_Count);
	DDX_Text(pDX, IDC_EDIT_TextureCol, m_TextureCol_Count);
	DDX_Text(pDX, IDC_EDIT_TextureCount, m_Texture_Count);
}


BEGIN_MESSAGE_MAP(CDlgBasicEditorParam, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgBasicEditorParam::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CDlgBasicEditorParam::OnBnClickedCancel)
	ON_BN_CLICKED(IDC_CHECK_MAXBOXFLAG, &CDlgBasicEditorParam::OnBnClickedCheckMaxboxflag)
	ON_BN_CLICKED(IDC_CHECK_CYCLE, &CDlgBasicEditorParam::OnBnClickedCheckCycle)
	ON_BN_CLICKED(IDC_CHECK_SHOWBKPIC, &CDlgBasicEditorParam::OnBnClickedCheckShowbkpic)
	ON_EN_KILLFOCUS(IDC_EDIT_EmitterAngle, &CDlgBasicEditorParam::OnEnKillfocusEditEmitterangle)
	ON_BN_CLICKED(IDC_CHECK_SetEmitterSpinCenter, &CDlgBasicEditorParam::OnBnClickedCheckSetemitterspincenter)
	ON_BN_CLICKED(IDC_CHECK_SetParticleHole, &CDlgBasicEditorParam::OnBnClickedCheckSetparticlehole)
	ON_BN_CLICKED(IDC_CHECK_MoveBKPic, &CDlgBasicEditorParam::OnBnClickedCheckMovebkpic)
	ON_WM_HSCROLL()

	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_LineLength, IDC_EDIT_RctHeight, &CDlgBasicEditorParam::OnEnKillFocus)


	ON_BN_CLICKED(IDC_CHECK_BlindArea, &CDlgBasicEditorParam::OnBnClickedCheckBlindarea)
	ON_BN_CLICKED(IDC_CHECK_BlockLine, &CDlgBasicEditorParam::OnBnClickedCheckBlockline)
	ON_BN_CLICKED(IDC_CHECK_ReflectLine, &CDlgBasicEditorParam::OnBnClickedCheckReflectline)
	ON_BN_CLICKED(IDC_BUTTON_OpenParamFile, &CDlgBasicEditorParam::OnBnClickedButtonOpenparamfile)
	ON_BN_CLICKED(IDC_BUTTON_SaveParamFile, &CDlgBasicEditorParam::OnBnClickedButtonSaveparamfile)
	ON_BN_CLICKED(IDC_CHECK_InterfacialEffect, &CDlgBasicEditorParam::OnBnClickedCheckInterfacialeffect)
	ON_BN_CLICKED(IDC_CHECK_skillEffectBottom, &CDlgBasicEditorParam::OnBnClickedCheckskilleffectbottom)
	
	ON_EN_CHANGE(IDC_EDIT_TextureRow, &CDlgBasicEditorParam::OnEnKillfocusEditTexturerow)
	ON_EN_CHANGE(IDC_EDIT_TextureCol, &CDlgBasicEditorParam::OnEnKillfocusEditTexturecol)
	ON_EN_CHANGE(IDC_EDIT_TextureCount, &CDlgBasicEditorParam::OnEnKillfocusEditTexturecount)
END_MESSAGE_MAP()


// CDlgBasicEditorParam 消息处理程序
// DlgBasicEditorParam.cpp : 实现文件
//

void CDlgBasicEditorParam::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnOK();
}

void CDlgBasicEditorParam::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnCancel();
}

CDocument* CDlgBasicEditorParam::GetEditorDoc()
{
	CMainFrame* pwnd = (CMainFrame*)AfxGetMainWnd();
	CFrameWnd* pchildWnd = pwnd->GetActiveFrame(); 
	CDocument* pCurrentDoc = pchildWnd->GetActiveDocument();
	
	return pCurrentDoc;//(CParticleSystemEditorDoc*)
}

void CDlgBasicEditorParam::SetCtrlPos()//打开文件时候调用
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();

	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	m_bCycleFlag = pParticlesys->GetPSParameter().cycle;

	m_nLineLength = 0;
	m_nRctWidth = 0;
	m_nRctHeight = 0;
	m_sliderLineLength.SetPos(0);
	m_sliderRctWidth.SetPos(0);
	m_sliderRctHeight.SetPos(0);

	m_Texture_Count = pParticlesys->GetPSL()->m_nTextureNum;
	m_TextureCol_Count = pParticlesys->GetPSL()->m_nColNum;
	m_TextureRow_Count = pParticlesys->GetPSL()->m_nRowNum;

	m_bInterfacePsl = pParticlesys->GetPSL()->m_nInterfacePsl;
	m_bSkillEffectSpritePoint = pParticlesys->GetPSL()->m_nSkillEffectSpritePoint;

	UpdateData(false);
}

void CDlgBasicEditorParam::UpdateBaseCtrlState(bool bMoveMap, bool bSetParticleHoleFlag, bool bSetEmitterSpinCentFlag)
{
	bMoveMap = bMoveMap;
	bSetParticleHoleFlag = bSetParticleHoleFlag;
	bSetEmitterSpinCentFlag = bSetEmitterSpinCentFlag;
	UpdateData(false);
}

void CDlgBasicEditorParam::SetPsParam(float fEmitterSpinAngle, int nLineLength, int nRctWidth, int nRctHeight)
{
	m_fEmitterSpinAngle = fEmitterSpinAngle;
	m_nLineLength = nLineLength >= 0 ? nLineLength:0;
	m_nRctWidth = nRctWidth >= 0 ? nRctWidth:0;
	m_nRctHeight = nRctHeight >= 0 ? nRctHeight:0;
	UpdateData(false);

	m_sliderLineLength.SetPos(m_nLineLength);
	m_sliderRctWidth.SetPos(m_nRctWidth);
	m_sliderRctHeight.SetPos(m_nRctHeight);
}

void CDlgBasicEditorParam::InitCtrl()//初始化参数
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	
	m_bShowBKpic = true;
	m_bCycleFlag = false;

	((CButton*)GetDlgItem(IDC_BUTTON_StrtANI))->EnableWindow(false);
	((CButton*)GetDlgItem(IDC_BUTTON_EndANI))->EnableWindow(false);

	((CButton*)GetDlgItem(IDC_BUTTON_SETBKPIC))->EnableWindow(m_bShowBKpic);
	((CButton*)GetDlgItem(IDC_BUTTON_SETBKCOLOR))->EnableWindow(!m_bShowBKpic);

	m_bMoveMap = false;
	m_bSetEmitterSpinCentFlag = false;
	m_bSetParticleHoleFlag = false;
	m_bSetMaxBox = false;

	pDoc->GetParticleSystemEditorView()->SetMoveBKPicFlag(m_bMoveMap);
	pDoc->GetParticleSystemEditorView()->SetParticleHoleFlag(m_bSetParticleHoleFlag);
	pDoc->GetParticleSystemEditorView()->SetEmitterSpinCentFlag(m_bSetEmitterSpinCentFlag);
	pDoc->GetParticleSystemEditorView()->SetMaxBoxFlag(m_bSetMaxBox);

	m_sliderLineLength.SetRange(0, 1000);
	m_sliderLineLength.SetPos(0);
	m_sliderRctWidth.SetRange(0, 1000);
	m_sliderRctWidth.SetPos(0);
	m_sliderRctHeight.SetRange(0, 200);
	m_sliderRctHeight.SetPos(0);
}

BOOL CDlgBasicEditorParam::PreTranslateMessage(MSG* pMsg)
{
	// TODO: 在此添加专用代码和/或调用基类

	//	return GetEditorDoc()->GetParticleParameterView()->PreTranslateMessage(pMsg);

	int nID = GetWindowLong(pMsg->hwnd, GWL_ID);//由窗口句柄获得ID号，GetWindowLong为获得窗口的ID号。
	if(pMsg->message == WM_LBUTTONDOWN) 
	{
		switch(nID)
		{
		case IDC_BUTTON_psParamColor:
		case IDC_BUTTON_Replay:
		case IDC_BUTTON_TestManager:
		case IDC_BUTTON_OPENPARTICLESYS:
		case IDC_BUTTON_SAVEMODIFYPSL:
		case IDC_BUTTON_SAVEPARTICLESYS:
		case IDC_BUTTON_RESETPARTICLESYS:
		case IDC_BUTTON_AllPSLtoPTC:
		case IDC_BUTTON_LOADTEXTURE:
		case IDC_BUTTON_mnPICtest:
		case IDC_BUTTON_EndANI:
		case IDC_BUTTON_SETBKCOLOR:
		case IDC_BUTTON_SETBKPIC:
		case IDC_BUTTON_FINDPSLTEXTURES:
		case IDC_BUTTON_PslFileInfo:
		case IDC_BUTTON_SAMPLE1:
		case IDC_BUTTON_SAMPLE2:
		case IDC_BUTTON_SAMPLE3:
		case IDC_BUTTON_SAMPLE4:
		case IDC_BUTTON_SAMPLE5:
		case IDC_BUTTON_SAMPLE6:
		case IDC_BUTTON_SAMPLE7:
		case IDC_BUTTON_SAMPLE8:
		case IDC_BUTTON_SAMPLE9:
		case IDC_BUTTON_SAMPLE10:
		case IDC_BUTTON_FromTextureListFindPsl:
			((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleParameterView()->ProcessMsg(pMsg);
			return TRUE;

		case IDC_BUTTON_PrintPSANI:
			((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleParameterView()->ProcessMsg(pMsg);
			((CButton*)GetDlgItem(IDC_BUTTON_StrtANI))->EnableWindow(true);
			return TRUE;

		case IDC_BUTTON_StrtANI:
			((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleParameterView()->ProcessMsg(pMsg);
			((CButton*)GetDlgItem(IDC_BUTTON_EndANI))->EnableWindow(true);
			return TRUE;

		default:
		    break;
		}
	}

	if (pMsg->message == WM_KEYDOWN)
	{
		switch(pMsg->wParam)
		{
		case 'P':
		case 'O':
			return ((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleParameterView()->PreTranslateMessage(pMsg);
			break;

		default:
			break;
		}
	}

	return CDialog::PreTranslateMessage(pMsg);
}

BOOL CDlgBasicEditorParam::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化
	InitCtrl();

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CDlgBasicEditorParam::OnBnClickedCheckMaxboxflag()
{
	//鼠标右键拖动设置粒子效果最大包围盒
 	UpdateData(true);
	if(m_bSetMaxBox)
	{
		m_bBlindArea = false;
		m_bBlockLine = false;
		m_bReflectLine = false;

		UpdateData(false);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetBlindAreaFlag(m_bBlindArea);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetBlockLineFlag(m_bBlockLine);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetReflectLineFlag(m_bReflectLine);
	}
	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetMaxBoxFlag(m_bSetMaxBox);
}

void CDlgBasicEditorParam::OnBnClickedCheckBlindarea()
{
	//鼠标右键拖动设置粒子盲区，处于改区域的粒子就不绘制了。目前用途：文字背景特效，当文字被遮挡了一半...
	UpdateData(true);
	if(m_bBlindArea)
	{
		m_bSetMaxBox = false;
		m_bBlockLine = false;
		m_bReflectLine = false;

		UpdateData(false);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetMaxBoxFlag(m_bSetMaxBox);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetBlockLineFlag(m_bBlockLine);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetReflectLineFlag(m_bReflectLine);
	}
	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetBlindAreaFlag(m_bBlindArea);
}

void CDlgBasicEditorParam::OnBnClickedCheckBlockline()
{
	UpdateData(true);
	if(m_bBlockLine)
	{
		m_bBlindArea = false;
		m_bSetMaxBox = false;
		m_bReflectLine = false;

		UpdateData(false);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetMaxBoxFlag(m_bSetMaxBox);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetBlindAreaFlag(m_bBlindArea);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetReflectLineFlag(m_bReflectLine);
	}
	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetBlockLineFlag(m_bBlockLine);
}

void CDlgBasicEditorParam::OnBnClickedCheckReflectline()
{
	UpdateData(true);
	if(m_bReflectLine)
	{
		m_bBlindArea = false;
		m_bBlockLine = false;
		m_bSetMaxBox = false;

		UpdateData(false);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetBlindAreaFlag(m_bBlindArea);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetBlockLineFlag(m_bBlockLine);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetMaxBoxFlag(m_bSetMaxBox);
	}
	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetReflectLineFlag(m_bReflectLine);
}

void CDlgBasicEditorParam::OnBnClickedCheckCycle()
{
	UpdateData(true);
	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleParameterView()->SetParticleSysCycle( (m_bCycleFlag!=0) );
}

void CDlgBasicEditorParam::OnBnClickedCheckShowbkpic()
{
	UpdateData(true);
	((CButton*)GetDlgItem(IDC_BUTTON_SETBKPIC))->EnableWindow(m_bShowBKpic);
	((CButton*)GetDlgItem(IDC_BUTTON_SETBKCOLOR))->EnableWindow(!m_bShowBKpic);

	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetShowBKPicFlag(m_bShowBKpic);
}

void CDlgBasicEditorParam::OnEnKillfocusEditEmitterangle()
{
	UpdateData(true);
	NormalizeParam(m_fEmitterSpinAngle, -360.0f, 360.0f);
	UpdateData(false);

//	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleParameterView()->SetEmitterangle(m_fEmitterSpinAngle);

	SetParams();
}

void CDlgBasicEditorParam::OnBnClickedCheckSetemitterspincenter()
{
	// TODO: 在此添加控件通知处理程序代码
	UpdateData(true);
	//鼠标设置发射器旋转中心..
	if(m_bSetEmitterSpinCentFlag)
	{
		m_bMoveMap = false;
		m_bSetParticleHoleFlag = false;
		UpdateData(false);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetMoveBKPicFlag(m_bMoveMap);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetParticleHoleFlag(m_bSetParticleHoleFlag);
	}
	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetEmitterSpinCentFlag(m_bSetEmitterSpinCentFlag);
}

void CDlgBasicEditorParam::OnBnClickedCheckSetparticlehole()
{
	UpdateData(true);
	//鼠标设置粒子影响点..
	if(m_bSetParticleHoleFlag)
	{
		m_bMoveMap = false;
		m_bSetEmitterSpinCentFlag = false;
		UpdateData(false);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetMoveBKPicFlag(m_bMoveMap);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetEmitterSpinCentFlag(m_bSetEmitterSpinCentFlag);
	}
	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetParticleHoleFlag(m_bSetParticleHoleFlag);
}

void CDlgBasicEditorParam::OnBnClickedCheckMovebkpic()
{
	// TODO:   
	UpdateData(true);
	//鼠标移动地图..
	if(m_bMoveMap)
	{
		m_bSetEmitterSpinCentFlag = false;
		m_bSetParticleHoleFlag = false;
		UpdateData(false);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetEmitterSpinCentFlag(m_bSetEmitterSpinCentFlag);
		((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetParticleHoleFlag(m_bSetParticleHoleFlag);
	}
	((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleSystemEditorView()->SetMoveBKPicFlag(m_bMoveMap);
}

void CDlgBasicEditorParam::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	m_nLineLength = m_sliderLineLength.GetPos();
	m_nRctWidth = m_sliderRctWidth.GetPos();
	m_nRctHeight = m_sliderRctHeight.GetPos();

	UpdateData(false);	
	SetParams();

	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CDlgBasicEditorParam::OnEnKillFocus(UINT nID)
{
	UpdateData(true);

	NormalizeParam(m_nLineLength, 0, 1000);
	NormalizeParam(m_nRctWidth, 0, 1000);
	NormalizeParam(m_nRctHeight, 0, 200);

	m_sliderLineLength.SetPos(m_nLineLength);
	m_sliderRctWidth.SetPos(m_nRctWidth);
	m_sliderRctHeight.SetPos(m_nRctHeight);

	UpdateData(false);
	SetParams();
}

void CDlgBasicEditorParam::SetParams()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)((CParticleSystemEditorDoc*)GetEditorDoc());
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::PSParameter psParameter = pParticleSys->GetPSParameter();
	float fAngle = TORADIANS(m_fEmitterSpinAngle);
	psParameter.m_psModifyParam.m_fEmitterSpinAngle = fAngle;

	psParameter.m_psModifyParam.m_fLineLength = (float)m_nLineLength;
	psParameter.m_psModifyParam.m_fRctWidth = (float)m_nRctWidth;
	psParameter.m_psModifyParam.m_fRctHeight = (float)m_nRctHeight;

	ParticleSystemHandle handle = pDoc->GetParticleParameterView()->GetCurrentPShandle();
	if(handle == INVALID_PS_HANDLE) //正常逻辑这里的handle永远都是1
	{
		MessageBox(L"逻辑错误..");
		return;
	}

	pDoc->GetParticleSystemEditorView()->m_pParSysManager->ModifyParticleSystem(handle, psParameter);
}

void CDlgBasicEditorParam::OnBnClickedButtonOpenparamfile()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)((CParticleSystemEditorDoc*)GetEditorDoc());
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleParameterView()->Openparamfile();
}

void CDlgBasicEditorParam::OnBnClickedButtonSaveparamfile()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)((CParticleSystemEditorDoc*)GetEditorDoc());
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleParameterView()->Saveparamfile();
}

void CDlgBasicEditorParam::OnBnClickedCheckInterfacialeffect()
{
	UpdateData(true);
	if(m_bInterfacePsl)
	{
		MessageBox(L"一定要把背景图片设置为该特效对应的界面哦:-D");
	}

	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_nInterfacePsl = m_bInterfacePsl;
	pParticlesys->Reset();
}

void CDlgBasicEditorParam::OnBnClickedCheckskilleffectbottom()
{
	UpdateData(true);
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;
	pParticlesys->GetPSL()->m_nSkillEffectSpritePoint = m_bSkillEffectSpritePoint;
}
void CDlgBasicEditorParam::OnEnKillfocusEditTexturerow()
{
	UpdateData(true);

	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_nRowNum = m_TextureRow_Count;

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)((CParticleSystemEditorDoc*)GetEditorDoc());
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	Nuclear::ParticleManager* pm = pDoc->GetParticleSystemEditorView()->m_pParSysManager;

	std::wstring ptr = pParticlesys->GetPslName();
	XHardRef<PSTEXTUREINFOSTRUCT>* pst = pm->m_mapPslTexture[ptr];

	int tw = (*pst)->PsTextureInfo.m_nPslTexCol * (*pst)->PsTextureInfo.m_nTexWidth;
	int th = (*pst)->PsTextureInfo.m_nPslTexRow * (*pst)->PsTextureInfo.m_nTexHeight;

	(*pst)->PsTextureInfo.m_nPslTexRow = m_TextureRow_Count;
	(*pst)->PsTextureInfo.m_nTexHeight = th/m_TextureRow_Count;

}

void CDlgBasicEditorParam::OnEnKillfocusEditTexturecol()
{
	UpdateData(true);

	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_nColNum = m_TextureCol_Count;

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)((CParticleSystemEditorDoc*)GetEditorDoc());
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	Nuclear::ParticleManager* pm = pDoc->GetParticleSystemEditorView()->m_pParSysManager;

	std::wstring ptr = pParticlesys->GetPslName();
	XHardRef<PSTEXTUREINFOSTRUCT>* pst = pm->m_mapPslTexture[ptr];

	int tw = (*pst)->PsTextureInfo.m_nPslTexCol * (*pst)->PsTextureInfo.m_nTexWidth;
	int th = (*pst)->PsTextureInfo.m_nPslTexRow * (*pst)->PsTextureInfo.m_nTexHeight;

	(*pst)->PsTextureInfo.m_nPslTexCol = m_TextureCol_Count;
	(*pst)->PsTextureInfo.m_nTexWidth = tw/m_TextureCol_Count;
}

void CDlgBasicEditorParam::OnEnKillfocusEditTexturecount()
{
	UpdateData(true);

	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	pParticlesys->GetPSL()->m_nTextureNum = m_Texture_Count;

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)((CParticleSystemEditorDoc*)GetEditorDoc());
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	Nuclear::ParticleManager* pm = pDoc->GetParticleSystemEditorView()->m_pParSysManager;

	std::wstring ptr = pParticlesys->GetPslName();
	XHardRef<PSTEXTUREINFOSTRUCT>* pst = pm->m_mapPslTexture[ptr];

	(*pst)->PsTextureInfo.m_nTexNum = m_Texture_Count;
}

