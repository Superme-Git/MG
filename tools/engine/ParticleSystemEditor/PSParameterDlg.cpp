// PSParameterDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "PSParameterDlg.h"


// CPSParameterDlg 对话框

IMPLEMENT_DYNAMIC(CPSParameterDlg, CDialog)

CPSParameterDlg::CPSParameterDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CPSParameterDlg::IDD, pParent)
	, m_nPSlayer(0)
	, m_bPSCycle(TRUE)
{
//	m_psParam.cycle = (m_bPSCycle != 0);

	m_bInitCtrlPos = false;
}

CPSParameterDlg::~CPSParameterDlg()
{
}

void CPSParameterDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_LAYER, m_nPSlayer);
	DDV_MinMaxInt(pDX, m_nPSlayer, 0, 10);
	DDX_Control(pDX, IDC_SLIDER_posx, m_sliderPSposx);
	DDX_Control(pDX, IDC_SLIDER_posy, m_sliderPSposy);
	DDX_Control(pDX, IDC_SLIDER_trackobjectx, m_sliderTrackObjectx);
	DDX_Control(pDX, IDC_SLIDER_trackobjecty, m_sliderTrackObjecty);
	DDX_Check(pDX, IDC_CHECK_CYCLE, m_bPSCycle);
}


BEGIN_MESSAGE_MAP(CPSParameterDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CPSParameterDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CPSParameterDlg::OnBnClickedCancel)
	ON_EN_CHANGE(IDC_EDIT_LAYER, &CPSParameterDlg::OnEnChangeEditLayer)
	ON_NOTIFY(NM_CUSTOMDRAW, IDC_SLIDER_posx, &CPSParameterDlg::OnNMCustomdrawSliderposx)
	ON_WM_HSCROLL()
	ON_BN_CLICKED(IDC_CHECK_CYCLE, &CPSParameterDlg::OnBnClickedCheckCycle)
END_MESSAGE_MAP()


// CPSParameterDlg 消息处理程序

void CPSParameterDlg::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
	OnOK();
}

void CPSParameterDlg::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
	OnCancel();
}

void CPSParameterDlg::OnEnChangeEditLayer()
{
	// TODO:  如果该控件是 RICHEDIT 控件，则它将不会
	// 发送该通知，除非重写 CDialog::OnInitDialog()
	// 函数并调用 CRichEditCtrl().SetEventMask()，
	// 同时将 ENM_CHANGE 标志“或”运算到掩码中。

	UpdateData(true);
//	m_psParam.layer = m_nPSlayer;
	int b = 0;
	// TODO:  在此添加控件通知处理程序代码
}

void CPSParameterDlg::OnNMCustomdrawSliderposx(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMCUSTOMDRAW pNMCD = reinterpret_cast<LPNMCUSTOMDRAW>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码
	*pResult = 0;
}

BOOL CPSParameterDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_sliderPSposx.SetRange(0, 1000);
	m_sliderPSposx.SetPos(300);
	m_sliderPSposy.SetRange(0, 1000);
	m_sliderPSposy.SetPos(300);
	m_sliderTrackObjectx.SetRange(0, 1000);
	m_sliderTrackObjectx.SetPos(500);
	m_sliderTrackObjecty.SetRange(0, 1000);
	m_sliderTrackObjecty.SetPos(500);

	if(!m_bInitCtrlPos)
	{
		UpdateData(false);
		m_sliderPSposx.SetPos((int)m_psParam.m_psModifyParam.m_pointCent.x);
		m_sliderPSposy.SetPos((int)m_psParam.m_psModifyParam.m_pointCent.y);
		m_sliderTrackObjectx.SetPos((int)m_psParam.m_psModifyParam.m_pointObject.x);
		m_sliderTrackObjecty.SetPos((int)m_psParam.m_psModifyParam.m_pointObject.y);
		m_bInitCtrlPos = true;
	}
	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CPSParameterDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	m_psParam.m_psModifyParam.m_pointCent.x = (float)m_sliderPSposx.GetPos();
	m_psParam.m_psModifyParam.m_pointCent.y = (float)m_sliderPSposy.GetPos();
	m_psParam.m_psModifyParam.m_pointObject.x = (float)m_sliderTrackObjectx.GetPos();
	m_psParam.m_psModifyParam.m_pointObject.y = (float)m_sliderTrackObjecty.GetPos();

	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CPSParameterDlg::OnBnClickedCheckCycle()
{
	UpdateData(true);
//	m_psParam.cycle = (m_bPSCycle!=0);
}

void CPSParameterDlg::SetPSParameter(const Nuclear::PSParameter& param)
{
	m_psParam = param;
	m_nPSlayer = m_psParam.layer;
	m_bPSCycle = m_psParam.cycle;

	m_bInitCtrlPos = false;
}
