// DlgSwingCurveAdjust.cpp : 实现文件
//

#include "stdafx.h"
#include "DistortionEditor.h"
#include "DlgSwingCurveAdjust.h"


// CDlgSwingCurveAdjust 对话框

IMPLEMENT_DYNAMIC(CDlgSwingCurveAdjust, CDialog)

CDlgSwingCurveAdjust::CDlgSwingCurveAdjust(CWnd* pParent /*=NULL*/)
	: CDialog(CDlgSwingCurveAdjust::IDD, pParent)
	, m_bSampleFlag(FALSE)
	, m_nSamplePointNum(2)
{
	bInitDataFlag = false;
}

CDlgSwingCurveAdjust::~CDlgSwingCurveAdjust()
{
}

void CDlgSwingCurveAdjust::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Check(pDX, IDC_CHECK_SAMPLEPOINT, m_bSampleFlag);
	DDX_Text(pDX, IDC_EDIT_SAMPLEPOINTNUM, m_nSamplePointNum);
}


BEGIN_MESSAGE_MAP(CDlgSwingCurveAdjust, CDialog)
	ON_BN_CLICKED(IDC_CHECK_SAMPLEPOINT, &CDlgSwingCurveAdjust::OnBnClickedCheckSamplepoint)
	ON_BN_CLICKED(IDOK, &CDlgSwingCurveAdjust::OnBnClickedOk)
	ON_WM_PAINT()
	ON_EN_CHANGE(IDC_EDIT_SAMPLEPOINTNUM, &CDlgSwingCurveAdjust::OnEnChangeEditSamplepointnum)
END_MESSAGE_MAP()


// CDlgSwingCurveAdjust 消息处理程序

BOOL CDlgSwingCurveAdjust::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化
	CRect rect;
	GetClientRect(&rect);
	rect.InflateRect(-10, -10, -10, -40);

	m_bezier.Create(m_bezier.IDD, this);
	int height = rect.Height();
	int widht = rect.Width();
	m_bezier.MoveWindow(&rect);
	m_bezier.unitX = 0.01f;
	m_bezier.unitY = 0.1f;
	m_bezier.SetLineCount(1);

	m_bezier.vectorCurveColor.push_back(RGB(0,255,0));

	m_bezier.SetNotify(this);
	m_bezier.ShowWindow(SW_SHOW);
	m_bezier.SetMinX(0);
	m_bezier.SetMaxX(1);
	m_bezier.SetMinY(0);
	m_bezier.SetMaxY(5);

	bezier::FPoint pt;
	if(bInitDataFlag)
	{
		bInitDataFlag = false;

		bezier::TangentPointContanter::iterator itbegin = m_TPCTangentPoints.at(0).begin();
		bezier::TangentPointContanter::iterator itend = m_TPCTangentPoints.at(0).end();
		itend--;

		pt.x = itbegin->point.x;
		pt.y = itbegin->point.y;
		bezier::FPoint nextPt = itbegin->controlPt[1];		
		m_bezier.ModifyStartPoint(0, pt.y, nextPt);
		pt.x = itend->point.x;
		pt.y = itend->point.y;
		bezier::FPoint prePt = itend->controlPt[0];
		m_bezier.ModifyEndPoint(0, pt.y, prePt);

		bezier::TangentPoint tp;

		itbegin++; //去掉头节点 itend已经是指向最后一个节点的了
		for(; itbegin != itend; itbegin++)
		{
			tp = *itbegin;
			m_bezier.AddPoint(0, tp);
		}
		
		bezier::SamplePointContanter spc = m_SPCSamplePoints.at(0);
		m_bezier.SetSamples(spc);
	}
	else
	{
		pt.x = 0.0f;
		pt.y = 1.0f;
		m_bezier.ModifyStartPoint(0, pt.y, pt);
		pt.x = 1.0f;
		m_bezier.ModifyEndPoint(0, pt.y, pt);
	}

	
	GetDlgItem(IDOK)->EnableWindow(false);

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CDlgSwingCurveAdjust::OnBnClickedCheckSamplepoint()
{
	UpdateData(true);
	if(m_bSampleFlag)
	{
		m_bezier.SetWorkMode(bezier::CBezierControl::WORK_MODE_SAMPLEING);
		UpdateData(TRUE);
		Sleep(100);

		m_TPCTangentPoints = m_bezier.GetTangentPoints();
		int cnt = (int)m_TPCTangentPoints.at(0).size();
		
		if(m_nSamplePointNum < cnt)
		{
			m_nSamplePointNum = cnt;
			UpdateData(FALSE);
		}
		m_bezier.SetSampleCount(m_nSamplePointNum);
	}
	else
	{
		m_bezier.SetWorkMode(bezier::CBezierControl::WORK_MODE_CURVE);
	}
}

void CDlgSwingCurveAdjust::OnBnClickedOk()
{
	m_SPCSamplePoints = m_bezier.GetSamplePoints();
	m_TPCTangentPoints = m_bezier.GetTangentPoints();
	OnOK();
}

void CDlgSwingCurveAdjust::setCurveParamData(const bezier::SamplePointContanters& SPCSamplePoints,
											 const bezier::TangentPointContanters& TPCTangentPoints)
{
	m_SPCSamplePoints = SPCSamplePoints;
	m_TPCTangentPoints = TPCTangentPoints;

	bInitDataFlag = true;
}

void CDlgSwingCurveAdjust::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: 在此处添加消息处理程序代码
// 	if(bInitDataFlag)
// 	{
// 		bInitDataFlag = false;
// 		m_bezier.SetSamples(m_SPCSamplePoints.at(0));
// 
// 		bezier::TangentPointContanter::iterator itbegin = m_TPCTangentPoints.at(0).begin();
// 		bezier::TangentPointContanter::iterator itend = m_TPCTangentPoints.at(0).end();
// 		itbegin++;
// 		itend--;
// 		bezier::TangentPointContanter::iterator it;
// 		for(it=itbegin; it!=itend; ++it)
// 		{
// 			m_bezier.AddPoint(0, *it);
// 		}
// 	}
	// 不为绘图消息调用 __super::OnPaint()
}

void CDlgSwingCurveAdjust::OnEnChangeEditSamplepointnum()
{
	// TODO:  如果该控件是 RICHEDIT 控件，则它将不会
	// 发送该通知，除非重写 __super::OnInitDialog()
	// 函数并调用 CRichEditCtrl().SetEventMask()，
	// 同时将 ENM_CHANGE 标志“或”运算到掩码中。

	// TODO:  在此添加控件通知处理程序代码
	UpdateData(true);

	int aa = m_nSamplePointNum;
}
