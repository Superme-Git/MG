// DlgSpecialParticleParam.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "DlgSpecialParticleParam.h"


#include "ParticleSystemEditorDoc.h"
#include "MainFrm.h"
// CDlgSpecialParticleParam 对话框

using namespace Nuclear;

IMPLEMENT_DYNAMIC(CDlgSpecialParticleParam, CDialog)

CDlgSpecialParticleParam::CDlgSpecialParticleParam(CWnd* pParent /*=NULL*/)
: CDialog(CDlgSpecialParticleParam::IDD, pParent)
, m_bAbsrtactPsl(FALSE)
, m_bDirMotion(FALSE)
, m_bPologyOk(FALSE)
, m_nVertexRow(20)
, m_nVertexCol(20)
, m_nLineAngle(0)
{
	m_nVertexType = 0; //默认都是自定义的..
	m_bSetShapeTexture = FALSE;
}

CDlgSpecialParticleParam::~CDlgSpecialParticleParam()
{
}

void CDlgSpecialParticleParam::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Check(pDX, IDC_CHECK_AbstractPsl, m_bAbsrtactPsl);
	DDX_Check(pDX, IDC_CHECK_DIRmotion, m_bDirMotion);
	DDX_Check(pDX, IDC_CHECK_CreatePologyOK, m_bPologyOk);
	DDX_Control(pDX, IDC_COMBO_ApslVertexType, m_comboPslVertexType);
	DDX_Control(pDX, IDC_SLIDER_VERTEXROW, m_sliderVertexRow);
	DDX_Control(pDX, IDC_SLIDER_VERTEXCOL, m_sliderVertexCol);
	DDX_Text(pDX, IDC_EDIT_VertexRow, m_nVertexRow);
	DDV_MinMaxInt(pDX, m_nVertexRow, 0, 40);
	DDX_Text(pDX, IDC_EDIT_VertexCol, m_nVertexCol);
	DDV_MinMaxInt(pDX, m_nVertexCol, 0, 40);
	DDX_Text(pDX, IDC_EDIT_LineAngle, m_nLineAngle);
	DDV_MinMaxInt(pDX, m_nLineAngle, 0, 360);
	DDX_Control(pDX, IDC_SLIDER_LineAngle, m_sliderLineAngle);
}


BEGIN_MESSAGE_MAP(CDlgSpecialParticleParam, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgSpecialParticleParam::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CDlgSpecialParticleParam::OnBnClickedCancel)
	ON_WM_HSCROLL()

	ON_BN_CLICKED(IDC_CHECK_AbstractPsl, &CDlgSpecialParticleParam::OnBnClickedCheckAbstractpsl)
	ON_BN_CLICKED(IDC_CHECK_DIRmotion, &CDlgSpecialParticleParam::OnBnClickedCheckDirmotion)
	ON_BN_CLICKED(IDC_CHECK_CreatePologyOK, &CDlgSpecialParticleParam::OnBnClickedCheckCreatepologyok)
	ON_CBN_SELCHANGE(IDC_COMBO_ApslVertexType, &CDlgSpecialParticleParam::OnCbnSelchangeComboApslvertextype)

	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_SysPosX, IDC_EDIT_LineAngle, &CDlgSpecialParticleParam::OnEnKillFocus)

END_MESSAGE_MAP()


// CDlgSpecialParticleParam 消息处理程序

void CDlgSpecialParticleParam::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnOK();
}

void CDlgSpecialParticleParam::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnCancel();
}


CDocument* CDlgSpecialParticleParam::GetEditorDoc()
{
	CMainFrame* pwnd = (CMainFrame*)AfxGetMainWnd();
	CFrameWnd* pchildWnd = pwnd->GetActiveFrame(); 
	CDocument* pCurrentDoc = pchildWnd->GetActiveDocument();

	return pCurrentDoc;//(CParticleSystemEditorDoc*)
}

void CDlgSpecialParticleParam::SetCtrlPos()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();

	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	m_bAbsrtactPsl = pParticlesys->GetPSL()->m_nAbsrtactPsl;
	m_bDirMotion = pParticlesys->GetPSL()->m_nDirMotion;

	((CButton*)GetDlgItem(IDC_CHECK_DIRmotion))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);
	((CButton*)GetDlgItem(IDC_CHECK_CreatePologyOK))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);

	((CButton*)GetDlgItem(IDC_BUTTON_LoadVertex))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);
	((CButton*)GetDlgItem(IDC_BUTTON_SaveVertex))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture && m_bPologyOk);

	((CButton*)GetDlgItem(IDC_BUTTON_SetShapeTexture))->EnableWindow(m_bAbsrtactPsl);

	m_comboPslVertexType.EnableWindow(m_bAbsrtactPsl);


	m_nVertexRow = 20;
	m_nVertexCol = 20;
	m_nVertexType = 0;
	m_nLineAngle = 60;
	m_sliderVertexRow.SetPos(m_nVertexRow);
	m_sliderVertexCol.SetPos(m_nVertexCol);	
	m_sliderLineAngle.SetPos(m_nLineAngle);

	m_sliderVertexRow.EnableWindow(false);
	m_sliderVertexCol.EnableWindow(false);
	GetDlgItem(IDC_EDIT_VertexRow)->EnableWindow(false);
	GetDlgItem(IDC_EDIT_VertexCol)->EnableWindow(false);


	UpdateData(false);
}

void CDlgSpecialParticleParam::InitCtrl()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();

	m_bAbsrtactPsl = false;
	m_comboPslVertexType.ResetContent();
	m_comboPslVertexType.InsertString(0, L"自定义形状");
	m_comboPslVertexType.InsertString(1, L"矩形");
	m_comboPslVertexType.InsertString(2, L"三角形");
	m_comboPslVertexType.InsertString(3, L"2块转场");
	m_comboPslVertexType.SetCurSel(0);

	m_sliderVertexRow.SetRange(0, 40);
	m_sliderVertexRow.SetPos(20);
	m_sliderVertexCol.SetRange(0, 40);
	m_sliderVertexCol.SetPos(20);

	m_sliderLineAngle.SetRange(0, 360);
	m_sliderLineAngle.SetPos(60);

	m_sliderVertexRow.EnableWindow(false);
	m_sliderVertexCol.EnableWindow(false);
	m_sliderLineAngle.EnableWindow(false);
	GetDlgItem(IDC_EDIT_VertexRow)->EnableWindow(false);
	GetDlgItem(IDC_EDIT_VertexCol)->EnableWindow(false);
	GetDlgItem(IDC_EDIT_LineAngle)->EnableWindow(false);

	((CButton*)GetDlgItem(IDC_CHECK_DIRmotion))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);
	((CButton*)GetDlgItem(IDC_CHECK_CreatePologyOK))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);

	((CButton*)GetDlgItem(IDC_BUTTON_LoadVertex))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);
	((CButton*)GetDlgItem(IDC_BUTTON_SaveVertex))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture && m_bPologyOk);

	((CButton*)GetDlgItem(IDC_BUTTON_SetShapeTexture))->EnableWindow(m_bAbsrtactPsl);

	m_comboPslVertexType.EnableWindow(m_bAbsrtactPsl);
}

void CDlgSpecialParticleParam::SetParaListToParticleSys()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;







	pParticlesys->Reset();
}

BOOL CDlgSpecialParticleParam::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化
	InitCtrl();

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

BOOL CDlgSpecialParticleParam::PreTranslateMessage(MSG* pMsg)
{
	// TODO: 在此添加专用代码和/或调用基类
	int nID = GetWindowLong(pMsg->hwnd, GWL_ID);//由窗口句柄获得ID号，GetWindowLong为获得窗口的ID号。
	if(pMsg->message == WM_LBUTTONDOWN) 
	{
		switch(nID)
		{
		case IDC_BUTTON_SetShapeTexture:
		case IDC_BUTTON_LoadVertex:
		case IDC_BUTTON_SaveVertex:
			((CParticleSystemEditorDoc*)GetEditorDoc())->GetParticleParameterView()->ProcessMsg(pMsg);
			return TRUE;

		default:
			break;
		}
	}

	return CDialog::PreTranslateMessage(pMsg);
}

void CDlgSpecialParticleParam::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	m_nVertexRow = m_sliderVertexRow.GetPos();
	m_nVertexCol = m_sliderVertexCol.GetPos();

	m_nLineAngle = m_sliderLineAngle.GetPos();

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleSystemEditorView()->SetVertexRow(m_nVertexRow);
	pDoc->GetParticleSystemEditorView()->SetVertexCol(m_nVertexCol);
	pDoc->GetParticleSystemEditorView()->SetLineAngle(m_nLineAngle);

	SetParaListToParticleSys();

	UpdateData(false); //变量到控件..

	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CDlgSpecialParticleParam::OnCbnSelchangeComboApslvertextype()
{
	m_nVertexType = m_comboPslVertexType.GetCurSel();

	m_sliderVertexRow.EnableWindow(m_nVertexType != 0 && m_nVertexType != 3);
	m_sliderVertexCol.EnableWindow(m_nVertexType != 0 && m_nVertexType != 3);
	GetDlgItem(IDC_EDIT_VertexRow)->EnableWindow(m_nVertexType != 0 && m_nVertexType != 3);
	GetDlgItem(IDC_EDIT_VertexCol)->EnableWindow(m_nVertexType != 0 && m_nVertexType != 3);

	GetDlgItem(IDC_CHECK_DIRmotion)->EnableWindow(m_nVertexType != 3);

	if(m_nVertexType == 3)
	{
		m_bDirMotion = FALSE;
		UpdateData(false); //变量到控件..
	}

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;
	pDoc->GetParticleSystemEditorView()->SetVertexType(m_nVertexType);
}

void CDlgSpecialParticleParam::OnBnClickedCheckAbstractpsl()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	Nuclear::CParticleSystem* pParticlesys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);

	((CButton*)GetDlgItem(IDC_CHECK_DIRmotion))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);
	((CButton*)GetDlgItem(IDC_CHECK_CreatePologyOK))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);

	((CButton*)GetDlgItem(IDC_BUTTON_LoadVertex))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);
	((CButton*)GetDlgItem(IDC_BUTTON_SaveVertex))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture && m_bPologyOk);

	((CButton*)GetDlgItem(IDC_BUTTON_SetShapeTexture))->EnableWindow(m_bAbsrtactPsl);

	m_comboPslVertexType.EnableWindow(m_bAbsrtactPsl);

	//	m_pParticleSys->GetPSL()->m_nAbsrtactPsl = m_bAbsrtactPsl;

	std::wstring defaultPsl;
	std::wstring wsTmp = FNExt_ParticleWS;
	if(m_bAbsrtactPsl)
		defaultPsl = L"default_abstract." + wsTmp;
	else
		defaultPsl = L"default." + wsTmp;

	pDoc->GetParticleParameterView()->OnApplyEffectFile(defaultPsl.c_str());

}

void CDlgSpecialParticleParam::OnBnClickedCheckDirmotion()
{
	Nuclear::CParticleSystem* pParticlesys = ( (CParticleSystemEditorDoc*)GetEditorDoc() )->GetParticleParameterView()->GetParticleSys();
	if(pParticlesys == NULL)
		return;

	UpdateData(true);
	pParticlesys->GetPSL()->m_nDirMotion = m_bDirMotion;
	pParticlesys->Reset();
}

void CDlgSpecialParticleParam::OnBnClickedCheckCreatepologyok()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	UpdateData(true);

	((CButton*)GetDlgItem(IDC_CHECK_DIRmotion))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);
	((CButton*)GetDlgItem(IDC_CHECK_CreatePologyOK))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);

	((CButton*)GetDlgItem(IDC_BUTTON_LoadVertex))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture);
	((CButton*)GetDlgItem(IDC_BUTTON_SaveVertex))->EnableWindow(m_bAbsrtactPsl && m_bSetShapeTexture && m_bPologyOk);

	((CButton*)GetDlgItem(IDC_BUTTON_SetShapeTexture))->EnableWindow(m_bAbsrtactPsl);

	pDoc->GetParticleSystemEditorView()->SetPologyFlag(m_bPologyOk==TRUE);
	if(m_bPologyOk) 
	{//初始化参数..可以绘制了... 		
		pDoc->GetParticleSystemEditorView()->ComputeShapeVertex();//读取各块的数据 给shape赋值	
	}
}

void CDlgSpecialParticleParam::UpdateCtrlStateParam(int nVertexRow, int nVertexCol, int nVertexType, BOOL bSetShapeTexture, BOOL bAbsrtactPsl)
{
	m_nVertexRow = nVertexRow;
	m_nVertexCol = nVertexCol;
	m_nVertexType = nVertexType;
	m_bSetShapeTexture = bSetShapeTexture;
	m_bAbsrtactPsl = bAbsrtactPsl;

	m_comboPslVertexType.SetCurSel(m_nVertexType);
	m_sliderVertexRow.SetPos(m_nVertexRow);
	m_sliderVertexCol.SetPos(m_nVertexCol);

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;
	m_nLineAngle = pDoc->GetParticleParameterView()->GetLineAngle();
	m_sliderLineAngle.SetPos(m_nLineAngle);

	UpdateData(false);
	pDoc->GetParticleSystemEditorView()->SetLineAngle(m_nLineAngle);

	m_sliderVertexRow.EnableWindow(m_nVertexType != 0 && m_bAbsrtactPsl);
	m_sliderVertexCol.EnableWindow(m_nVertexType != 0 && m_bAbsrtactPsl);
	m_sliderLineAngle.EnableWindow(m_nVertexType != 0 && m_bAbsrtactPsl);
	GetDlgItem(IDC_EDIT_VertexRow)->EnableWindow(m_nVertexType != 0 && m_bAbsrtactPsl);
	GetDlgItem(IDC_EDIT_VertexCol)->EnableWindow(m_nVertexType != 0 && m_bAbsrtactPsl);
	GetDlgItem(IDC_EDIT_LineAngle)->EnableWindow(m_nVertexType != 0 && m_bAbsrtactPsl);

	((CButton*)GetDlgItem(IDC_CHECK_DIRmotion))->EnableWindow(m_bSetShapeTexture && m_bAbsrtactPsl);
	((CButton*)GetDlgItem(IDC_CHECK_CreatePologyOK))->EnableWindow(m_bSetShapeTexture && m_bAbsrtactPsl);

	((CButton*)GetDlgItem(IDC_BUTTON_LoadVertex))->EnableWindow(m_bSetShapeTexture && m_bAbsrtactPsl);
	((CButton*)GetDlgItem(IDC_BUTTON_SaveVertex))->EnableWindow(m_bSetShapeTexture && m_bPologyOk && m_bAbsrtactPsl);

	((CButton*)GetDlgItem(IDC_BUTTON_SetShapeTexture))->EnableWindow(m_bAbsrtactPsl);
}

void CDlgSpecialParticleParam::OnEnKillFocus(UINT nID)
{
	UpdateData(true);

	NormalizeParam(m_nVertexRow, 0, 40);
	NormalizeParam(m_nVertexCol, 0, 40);
	NormalizeParam(m_nLineAngle, 0, 360);

	m_sliderVertexRow.SetPos(m_nVertexRow);
	m_sliderVertexCol.SetPos(m_nVertexCol);
	m_sliderLineAngle.SetPos(m_nLineAngle);

	if(nID == IDC_EDIT_VertexRow || nID == IDC_EDIT_VertexCol || nID == IDC_EDIT_LineAngle)
	{
		CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetEditorDoc();
		ASSERT_VALID(pDoc);
		if(!pDoc)
			return;
		pDoc->GetParticleSystemEditorView()->SetVertexRow(m_nVertexRow);
		pDoc->GetParticleSystemEditorView()->SetVertexCol(m_nVertexCol);
		pDoc->GetParticleSystemEditorView()->SetLineAngle(m_nLineAngle);
	}

	UpdateData(false);
	SetParaListToParticleSys();
}