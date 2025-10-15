// PSParameterDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "PSParameterDlg.h"


// CPSParameterDlg �Ի���

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


// CPSParameterDlg ��Ϣ�������

void CPSParameterDlg::OnBnClickedOk()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnOK();
}

void CPSParameterDlg::OnBnClickedCancel()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnCancel();
}

void CPSParameterDlg::OnEnChangeEditLayer()
{
	// TODO:  ����ÿؼ��� RICHEDIT �ؼ�������������
	// ���͸�֪ͨ��������д CDialog::OnInitDialog()
	// ���������� CRichEditCtrl().SetEventMask()��
	// ͬʱ�� ENM_CHANGE ��־�������㵽�����С�

	UpdateData(true);
//	m_psParam.layer = m_nPSlayer;
	int b = 0;
	// TODO:  �ڴ���ӿؼ�֪ͨ����������
}

void CPSParameterDlg::OnNMCustomdrawSliderposx(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMCUSTOMDRAW pNMCD = reinterpret_cast<LPNMCUSTOMDRAW>(pNMHDR);
	// TODO: �ڴ���ӿؼ�֪ͨ����������
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
	// �쳣: OCX ����ҳӦ���� FALSE
}

void CPSParameterDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: �ڴ������Ϣ�����������/�����Ĭ��ֵ

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
