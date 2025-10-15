// DlgLinkedObjectMoveParam.cpp : 实现文件
//

#include "stdafx.h"
#include "LinkedObjectEditor.h"
#include "DlgLinkedObjectMoveParam.h"
#include <math.h>


// CDlgLinkedObjectMoveParam 对话框

IMPLEMENT_DYNAMIC(CDlgLinkedObjectMoveParam, CDialog)

CDlgLinkedObjectMoveParam::CDlgLinkedObjectMoveParam(CWnd* pParent /*=NULL*/)
	: CDialog(CDlgLinkedObjectMoveParam::IDD, pParent)
	, m_fAngle1(-1.0)
	, m_fAngle2(1.0)
	, m_fSpeed(1.0)
	, m_nSkewPixelX(5)
	, m_nSkewSpeedX(5)
	, m_fSizeLeft(-2.0)
	, m_fSizeRight(2.0)
	, m_fTransformSpeed(2)
	, m_fSizeTop(0)
	, m_fSizeDown(0)
{
	m_nDistortionType = 0;
	m_nSkewType = 0;
	m_nMoveFollowType = 0;
}

CDlgLinkedObjectMoveParam::~CDlgLinkedObjectMoveParam()
{
}

void CDlgLinkedObjectMoveParam::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_SLIDER_ANGLE1, m_sliderAngle1);
	DDX_Control(pDX, IDC_SLIDER_ANGLE2, m_sliderAngle2);
	DDX_Control(pDX, IDC_SLIDER_SPEED, m_sliderSpeed);
	DDX_Control(pDX, IDC_COMBO_DistortionType, m_comboDistortionType);
	DDX_Control(pDX, IDC_SLIDER_SKEWX, m_sliderSkew);
	DDX_Control(pDX, IDC_SLIDER_SPEEDX, m_sliderSkewXspeed);
	DDX_Control(pDX, IDC_COMBO_SKEWTYPE, m_comboSkewType);
	DDX_Text(pDX, IDC_EDIT_ANGLE1, m_fAngle1);
	DDX_Text(pDX, IDC_EDIT_ANGLE2, m_fAngle2);
	DDX_Text(pDX, IDC_EDIT_SPEED, m_fSpeed);
	DDX_Text(pDX, IDC_EDIT_SKEWX, m_nSkewPixelX);
	DDX_Text(pDX, IDC_EDIT_SKEWSPEED, m_nSkewSpeedX);

	DDV_MinMaxFloat(pDX, m_fAngle1, -360.0, 360.0);
	DDV_MinMaxFloat(pDX, m_fAngle2, -360.0, 360.0);
	DDV_MinMaxFloat(pDX, m_fSpeed, 0.0, 360.0);
	DDV_MinMaxInt(pDX, m_nSkewPixelX, 0, 200);
	DDV_MinMaxInt(pDX, m_nSkewSpeedX, 0, 100);
	DDX_Control(pDX, IDC_COMBO1, m_comboMoveFollowType);
	DDX_Control(pDX, IDC_SLIDER_SizeLeft, m_sliderSizeLeft);
	DDX_Control(pDX, IDC_SLIDER_SizeRight, m_sliderSizeRight);
	DDX_Control(pDX, IDC_SLIDER_TransformSpeed, m_sliderTransformSpeedx);
	DDX_Text(pDX, IDC_EDIT_SIZELEFT, m_fSizeLeft);
	DDX_Text(pDX, IDC_EDIT_SIZERIGHT, m_fSizeRight);
	DDX_Text(pDX, IDC_EDIT_TRANSFORMSPEED, m_fTransformSpeed);
	DDV_MinMaxFloat(pDX, m_fSizeLeft, -20.0, 20.0);
	DDV_MinMaxFloat(pDX, m_fSizeRight, -20.0, 20.0);
	DDV_MinMaxFloat(pDX, m_fTransformSpeed, 0.0, 20.0);
	DDX_Control(pDX, IDC_SLIDER_SizeTop, m_sliderSizeTop);
	DDX_Control(pDX, IDC_SLIDER_SizeDown, m_sliderSizeDown);
	DDX_Text(pDX, IDC_EDIT_SIZETOP, m_fSizeTop);
	DDX_Text(pDX, IDC_EDIT_SIZEDOWN, m_fSizeDown);
	DDV_MinMaxFloat(pDX, m_fSizeTop, -20.0, 20.0);
	DDV_MinMaxFloat(pDX, m_fSizeDown, -20.0, 20.0);
}


BEGIN_MESSAGE_MAP(CDlgLinkedObjectMoveParam, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgLinkedObjectMoveParam::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CDlgLinkedObjectMoveParam::OnBnClickedCancel)
	ON_WM_HSCROLL()
	ON_WM_PAINT()
	ON_CBN_SELCHANGE(IDC_COMBO_DistortionType, &CDlgLinkedObjectMoveParam::OnCbnSelchangeComboDistortiontype)
	ON_CBN_SELCHANGE(IDC_COMBO_SKEWTYPE, &CDlgLinkedObjectMoveParam::OnCbnSelchangeComboSkewtype)
	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_ANGLE1, IDC_EDIT_SIZEDOWN, &CDlgLinkedObjectMoveParam::OnEnKillFocusEdit)

	ON_CBN_SELCHANGE(IDC_COMBO1, &CDlgLinkedObjectMoveParam::OnCbnSelchangeCombo1)
	
END_MESSAGE_MAP()


// CDlgLinkedObjectMoveParam 消息处理程序

void CDlgLinkedObjectMoveParam::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
	OnOK();
}

void CDlgLinkedObjectMoveParam::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
	OnCancel();
}

BOOL CDlgLinkedObjectMoveParam::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_sliderAngle1.SetRange(-3600, 3600, true);
	m_sliderAngle1.SetPos(-10);
	
	m_sliderAngle2.SetRange(-3600, 3600, true);
	m_sliderAngle2.SetPos(10);

	m_sliderSpeed.SetRange(0, 3600);
	m_sliderSpeed.SetPos(10);

	m_sliderSkew.SetRange(0, 200);
	m_sliderSkew.SetPos(5);
	m_sliderSkewXspeed.SetRange(0, 200);
	m_sliderSkewXspeed.SetPos(5);

	m_sliderSizeLeft.SetRange(-200, 200, true);
	m_sliderSizeLeft.SetPos(-20);
	m_sliderSizeRight.SetRange(-200, 200, true);
	m_sliderSizeRight.SetPos(20);

	m_sliderSizeTop.SetRange(-200, 200, true);
	m_sliderSizeTop.SetPos(0);
	m_sliderSizeDown.SetRange(-200, 200, true);
	m_sliderSizeDown.SetPos(0);

	m_sliderTransformSpeedx.SetRange(0, 200);
	m_sliderTransformSpeedx.SetPos(2);

	m_sliderAngle1.SetPos((int)(m_fAngle1*10));
	m_sliderAngle2.SetPos((int)(m_fAngle2*10));
	m_sliderSpeed.SetPos((int)(m_fSpeed*10));
	m_sliderSkew.SetPos(m_nSkewPixelX);
	m_sliderSkewXspeed.SetPos(m_nSkewSpeedX);

	m_sliderSizeLeft.SetPos((int)(m_fSizeLeft*10));
	m_sliderSizeRight.SetPos((int)(m_fSizeRight*10));
	m_sliderSizeTop.SetPos((int)(m_fSizeTop*10));
	m_sliderSizeDown.SetPos((int)(m_fSizeDown*10));
	m_sliderTransformSpeedx.SetPos((int)(m_fTransformSpeed*10));

	UpdateData(false);//变量到控件

	m_comboDistortionType.AddString(L"旋转");
	m_comboDistortionType.AddString(L"错切");
	m_comboDistortionType.AddString(L"平移");
	m_comboDistortionType.SetCurSel(m_nDistortionType);

	m_comboSkewType.AddString(L"水平切");
	m_comboSkewType.AddString(L"垂直切");
	m_comboSkewType.SetCurSel(m_nSkewType);

	m_comboMoveFollowType.AddString(L"不连接");
	m_comboMoveFollowType.AddString(L"柔性连接");
	m_comboMoveFollowType.AddString(L"刚性连接");
	m_comboMoveFollowType.SetCurSel(m_nMoveFollowType);

	InitDisableCtrl(m_nDistortionType);

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CDlgLinkedObjectMoveParam::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	m_fAngle1 = m_sliderAngle1.GetPos() *0.1f;
	m_fAngle2 = m_sliderAngle2.GetPos() *0.1f;
	m_fSpeed = m_sliderSpeed.GetPos() *0.1f;
	m_nSkewPixelX = m_sliderSkew.GetPos();
	m_nSkewSpeedX = m_sliderSkewXspeed.GetPos();

	m_fSizeLeft = m_sliderSizeLeft.GetPos() *0.1f;
	m_fSizeRight = m_sliderSizeRight.GetPos() *0.1f;
	m_fSizeTop = m_sliderSizeTop.GetPos() *0.1f;
	m_fSizeDown = m_sliderSizeDown.GetPos() *0.1f;
	m_fTransformSpeed = m_sliderTransformSpeedx.GetPos() *0.1f;

	UpdateData(false);

	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CDlgLinkedObjectMoveParam::GetMoveParam(Nuclear::PLANTMOVEPARAM& param)
{
	param.m_fAngle1 = m_fAngle1;
	param.m_fAngle2 = m_fAngle2;

	if(m_fAngle1 > m_fAngle2)
	{
		param.m_fAngle1 = m_fAngle2;
		param.m_fAngle2 = m_fAngle1;
	}

	param.m_fSpeed = m_fSpeed;
	param.m_nDistortionType = Nuclear::XPPLANTDISTORTIONTYPE(m_nDistortionType);
	param.m_nSkewType = Nuclear::XPPLANTSKEWTYPE(m_nSkewType);
	param.m_nMoveFollowType = Nuclear::XPMOVEFOLLOWTYPE(m_nMoveFollowType);
	param.m_nSkewPixelX = m_nSkewPixelX;
	param.m_nSkewSpeedX = m_nSkewSpeedX;

	param.m_fSizeLeft = m_fSizeLeft;
	param.m_fSizeRight = m_fSizeRight;
	if(m_fSizeLeft > m_fSizeRight)
	{
		param.m_fSizeLeft = m_fSizeRight;
		param.m_fSizeRight = m_fSizeLeft;
	}

	param.m_fSizeTop = m_fSizeTop;
	param.m_fSizeDown = m_fSizeDown;
	if(m_fSizeTop > m_fSizeDown)
	{
		param.m_fSizeTop = m_fSizeDown;
		param.m_fSizeDown = m_fSizeTop;
	}

	param.m_fTranslationSpeedx = m_fTransformSpeed;
	param.m_fTranslationSpeedy = m_fTransformSpeed;
}

void CDlgLinkedObjectMoveParam::SetInitMoveParam(const Nuclear::PLANTMOVEPARAM& param)
{
	m_fAngle1 = param.m_fAngle1;
	m_fAngle2 = param.m_fAngle2;
	m_fSpeed = fabs(param.m_fSpeed);
	
	m_nDistortionType = param.m_nDistortionType;
	m_nSkewType = param.m_nSkewType;

	m_nMoveFollowType = param.m_nMoveFollowType;

	m_nSkewPixelX = param.m_nSkewPixelX;
	m_nSkewSpeedX = abs(param.m_nSkewSpeedX);

	m_fSizeLeft = param.m_fSizeLeft;
	m_fSizeRight = param.m_fSizeRight;
	m_fSizeTop = param.m_fSizeTop;
	m_fSizeDown = param.m_fSizeDown;

	m_fTransformSpeed = fabs(param.m_fTranslationSpeedx);
}

void CDlgLinkedObjectMoveParam::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: 在此处添加消息处理程序代码

	int oldBKMode = dc.SetBkMode(TRANSPARENT);

	dc.SetBkMode(oldBKMode);

	// 不为绘图消息调用 CDialog::OnPaint()
}

void CDlgLinkedObjectMoveParam::InitDisableCtrl(int nDistortionType)
{
	switch(nDistortionType)
	{
	case 0://旋转
		m_sliderAngle1.EnableWindow(true);
		m_sliderAngle2.EnableWindow(true);
		m_sliderSpeed.EnableWindow(true);
		m_comboSkewType.EnableWindow(false);
		m_sliderSkew.EnableWindow(false);
		m_sliderSkewXspeed.EnableWindow(false);

		m_sliderSizeLeft.EnableWindow(false);
		m_sliderSizeRight.EnableWindow(false);
		m_sliderSizeTop.EnableWindow(false);
		m_sliderSizeDown.EnableWindow(false);
		m_sliderTransformSpeedx.EnableWindow(false);

		GetDlgItem(IDC_EDIT_ANGLE1)->EnableWindow(true);
		GetDlgItem(IDC_EDIT_ANGLE2)->EnableWindow(true);
		GetDlgItem(IDC_EDIT_SPEED)->EnableWindow(true);
		GetDlgItem(IDC_EDIT_SKEWX)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SKEWSPEED)->EnableWindow(false);

		GetDlgItem(IDC_EDIT_SIZELEFT)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SIZERIGHT)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SIZETOP)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SIZEDOWN)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_TRANSFORMSPEED)->EnableWindow(false);

		break;
	case 1://错切 skew
		m_sliderAngle1.EnableWindow(false);
		m_sliderAngle2.EnableWindow(false);
		m_sliderSpeed.EnableWindow(false);
		m_comboSkewType.EnableWindow(true);
		m_sliderSkew.EnableWindow(true);
		m_sliderSkewXspeed.EnableWindow(true);

		m_sliderSizeLeft.EnableWindow(false);
		m_sliderSizeRight.EnableWindow(false);
		m_sliderSizeTop.EnableWindow(false);
		m_sliderSizeDown.EnableWindow(false);
		m_sliderTransformSpeedx.EnableWindow(false);

		GetDlgItem(IDC_EDIT_ANGLE1)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_ANGLE2)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SPEED)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SKEWX)->EnableWindow(true);
		GetDlgItem(IDC_EDIT_SKEWSPEED)->EnableWindow(true);

		GetDlgItem(IDC_EDIT_SIZELEFT)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SIZERIGHT)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SIZETOP)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SIZEDOWN)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_TRANSFORMSPEED)->EnableWindow(false);
		break;

	case 2://平移
		m_sliderAngle1.EnableWindow(false);
		m_sliderAngle2.EnableWindow(false);
		m_sliderSpeed.EnableWindow(false);
		m_comboSkewType.EnableWindow(false);
		m_sliderSkew.EnableWindow(false);
		m_sliderSkewXspeed.EnableWindow(false);

		m_sliderSizeLeft.EnableWindow(true);
		m_sliderSizeRight.EnableWindow(true);
		m_sliderSizeTop.EnableWindow(true);
		m_sliderSizeDown.EnableWindow(true);
		m_sliderTransformSpeedx.EnableWindow(true);

		GetDlgItem(IDC_EDIT_ANGLE1)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_ANGLE2)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SPEED)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SKEWX)->EnableWindow(false);
		GetDlgItem(IDC_EDIT_SKEWSPEED)->EnableWindow(false);

		GetDlgItem(IDC_EDIT_SIZELEFT)->EnableWindow(true);
		GetDlgItem(IDC_EDIT_SIZERIGHT)->EnableWindow(true);
		GetDlgItem(IDC_EDIT_SIZETOP)->EnableWindow(true);
		GetDlgItem(IDC_EDIT_SIZEDOWN)->EnableWindow(true);
		GetDlgItem(IDC_EDIT_TRANSFORMSPEED)->EnableWindow(true);
		break;

	default:
		break;
	}
}

void CDlgLinkedObjectMoveParam::OnCbnSelchangeComboDistortiontype()
{
	m_nDistortionType = m_comboDistortionType.GetCurSel();
	InitDisableCtrl(m_nDistortionType);
}

void CDlgLinkedObjectMoveParam::OnCbnSelchangeComboSkewtype()
{
	m_nSkewType = m_comboSkewType.GetCurSel();
}

void CDlgLinkedObjectMoveParam::OnEnKillFocusEdit(UINT nID)
{
	// TODO:  如果该控件是 RICHEDIT 控件，则它将不会
	// 发送该通知，除非重写 CDialog::OnInitDialog()
	// 函数并调用 CRichEditCtrl().SetEventMask()，
	// 同时将 ENM_CHANGE 标志“或”运算到掩码中。
	// TODO:  在此添加控件通知处理程序代码
	UpdateData(true);
	m_fAngle1 = m_fAngle1>360 ? 360:m_fAngle1;
	m_fAngle1 = m_fAngle1<-360 ? -360:m_fAngle1;
	m_fAngle2 = m_fAngle2>360 ? 360:m_fAngle2;
	m_fAngle2 = m_fAngle2<-360 ? -360:m_fAngle2;
	m_fSpeed = m_fSpeed>360 ? 360:m_fSpeed;
	m_fSpeed = m_fSpeed<0 ? 0:m_fSpeed;
	m_nSkewPixelX = m_nSkewPixelX>200 ? 200:m_nSkewPixelX;
	m_nSkewPixelX = m_nSkewPixelX<0 ? 0:m_nSkewPixelX;
	m_nSkewSpeedX = m_nSkewSpeedX>200 ? 200:m_nSkewSpeedX;
	m_nSkewSpeedX = m_nSkewSpeedX<0 ? 0:m_nSkewSpeedX;

	m_fSizeLeft = m_fSizeLeft>20 ? 20:m_fSizeLeft;
	m_fSizeLeft = m_fSizeLeft<-20 ? -20:m_fSizeLeft;
	m_fSizeRight = m_fSizeRight>20 ? 20:m_fSizeRight;
	m_fSizeRight = m_fSizeRight<-20 ? -20:m_fSizeRight;

	m_fSizeTop = m_fSizeTop>20 ? 20:m_fSizeTop;
	m_fSizeTop = m_fSizeTop<-20 ? -20:m_fSizeTop;
	m_fSizeDown = m_fSizeDown>20 ? 20:m_fSizeDown;
	m_fSizeDown = m_fSizeDown<-20 ? -20:m_fSizeDown;

	m_fTransformSpeed = m_fTransformSpeed>20 ? 20:m_fTransformSpeed;
	m_fTransformSpeed = m_fTransformSpeed<0 ? 0:m_fTransformSpeed;
	UpdateData(false);

	m_sliderAngle1.SetPos((int)(m_fAngle1*10));
	m_sliderAngle2.SetPos((int)(m_fAngle2*10));
	m_sliderSpeed.SetPos((int)(m_fSpeed*10));
	m_sliderSkew.SetPos(m_nSkewPixelX);
	m_sliderSkewXspeed.SetPos(m_nSkewSpeedX);

	m_sliderSizeLeft.SetPos((int)(m_fSizeLeft*10));
	m_sliderSizeRight.SetPos((int)(m_fSizeRight*10));
	m_sliderSizeTop.SetPos((int)(m_fSizeTop*10));
	m_sliderSizeDown.SetPos((int)(m_fSizeDown*10));

	m_sliderTransformSpeedx.SetPos((int)(m_fTransformSpeed*10));
}

void CDlgLinkedObjectMoveParam::OnCbnSelchangeCombo1()
{
	m_nMoveFollowType = m_comboMoveFollowType.GetCurSel();
}
