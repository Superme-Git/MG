

#include "stdafx.h"
#include "EffectEditor.h"
#include "DlgPropDoubleCurve.h"
#include "InputDialog.h"



IMPLEMENT_DYNAMIC(CDlgPropDoubleCurve, CDialog)

void CDlgPropDoubleCurve::OnLButtonDown(UINT nFlags, CPoint point)
{
	m_StaticSampleCount.SetFocus();
	__super::OnLButtonDown(nFlags, point);
}
void CDlgPropDoubleCurve::OnEnKillfocusEditSampleCount()
{
	if (m_comboWorkMode.GetCurSel() == 0)	
		return;
	int oldSampleCount = m_nSampleCount;
	UpdateData();
	if (m_nSampleCount != oldSampleCount)
	{
		m_bezier.SetSampleCount(m_nSampleCount);
		m_bezier.Invalidate(FALSE);
	}
}

void CDlgPropDoubleCurve::OnBnClickedButtonReset()
{
	CInputDialog dlg;
	if (dlg.DoModal() == IDOK)
	{
		bezier::FPoint pt;
		pt.x = m_bezier.GetMinX();
		pt.y = dlg.fInput;
		m_bezier.CleanAllLinePoints();
		m_bezier.ModifyStartPoint(0, pt.y, pt);
		m_bezier.ModifyStartPoint(1, pt.y, pt);
		pt.x = m_bezier.GetMaxX();
		m_bezier.ModifyEndPoint(0, pt.y, pt);
		m_bezier.ModifyEndPoint(1, pt.y, pt);
		m_bezier.SetWorkMode(bezier::CBezierControl::WORK_MODE_SAMPLEING);
		m_comboWorkMode.SetCurSel(1);
		m_nSampleCount = 2;
		m_bezier.SetSampleCount(3);
		m_bezier.SetSampleCount(2);
		UpdateData(FALSE);
	}
}

void CDlgPropDoubleCurve::OnCbnSelendokComboWorkMode()
{
	UpdateData();
	switch (m_comboWorkMode.GetCurSel())
	{
	case 0:
		m_bezier.SetWorkMode(bezier::CBezierControl::WORK_MODE_CURVE);
		break;
	case 1:
		m_bezier.SetWorkMode(bezier::CBezierControl::WORK_MODE_SAMPLEING);
		m_bezier.SetSampleCount(m_nSampleCount);
		break;
	}
}

void CDlgPropDoubleCurve::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CDlgPropDoubleCurve::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

BOOL CDlgPropDoubleCurve::OnInitDialog()
{
	CDialog::OnInitDialog();
	SetWindowText(m_title);
	m_StaticSampleCount.SetWindowText(L"采样点数量：");
	m_ButtonOk.EnableWindow(false);
	m_bezier.Create(m_bezier.IDD, this);
	m_bezier.SetNotify(this);
	ResetSize();
	m_bezier.SetMinX(0);
	int endFrame, startFrame;
	eAnimationPlayType playType;
	const Nuclear::AbstractEffectClip* pClip = *m_param.pDoc->GetSelectedClips().begin();
	m_param.pDoc->GetClipStartEndFrame(pClip, playType, startFrame, endFrame);
	if (playType == APT_SPEC_LOOP)
	{
		endFrame = m_param.pDoc->GetTotalFrames();
	}
	m_bezier.SetMaxX(static_cast<float>(endFrame - startFrame));
	m_bezier.xMustInt = true;
	const Nuclear::AbstractEffectClip::KP_VECTOR& posVector = pClip->GetToolsKeyPoints(m_param.kt);
	Nuclear::AbstractEffectClip::KP_VECTOR::const_iterator iter = posVector.begin();
	float minY, maxY;
	m_bezier.SetLineCount(2);
	m_bezier.SetLineName(0, L"X");
	m_bezier.SetLineName(1, L"Y");
	if (iter == posVector.end())
	{
		maxY = m_param.fYdiff / 2;
		minY = -maxY;
		bezier::FPoint pt;
		pt.x = 0.0f;
		pt.y = (maxY + minY) / 2;
		m_bezier.ModifyStartPoint(0, pt.y, pt);
		m_bezier.ModifyStartPoint(1, pt.y, pt);
		pt.x = m_bezier.GetMaxX();
		m_bezier.ModifyEndPoint(0, pt.y, pt);
		m_bezier.ModifyEndPoint(1, pt.y, pt);
	} else {
		minY = iter->point[0].y;
		maxY = iter->point[0].y;
	}

	bezier::TangentPoint xTp;
	bezier::TangentPoint yTp;
	bezier::TangentPoint* pTp;
	int headXState = 0;	
	int headYState = 0; 
	bezier::FPoint pt;
	for (;iter!=posVector.end();++iter)
	{
		if (iter->t < 2)	
		{
			if (headXState == 0)
			{
				pt.x = iter->point[2].x;
				pt.y = iter->point[2].y;
				m_bezier.ModifyStartPoint(0, iter->point[0].y, pt);
				headXState = 1;
				continue;
			} else if (headXState == 1)
			{
				headXState = 2;
			} else if (headXState == 2)
			{
				if (xTp.point.x < m_bezier.GetMaxX())
				{
					m_bezier.AddPoint(0, xTp);
				}
			}
			pTp = &xTp;
		} else {
			if (headYState == 0)
			{
				pt.x = iter->point[2].x;
				pt.y = iter->point[2].y;
				m_bezier.ModifyStartPoint(1, iter->point[0].y, pt);
				headYState = 1;
				continue;
			} else if (headYState == 1)
			{
				headYState = 2;
			} else if (headYState == 2)
			{
				if (yTp.point.x < m_bezier.GetMaxX())
				{
					m_bezier.AddPoint(1, yTp);
				}
			}
			pTp = &yTp;
		}
		pTp->type = ((iter->t%2)==0)?bezier::POINT_TYPE_SMOOTH:bezier::POINT_TYPE_CORNER;
		pTp->controlPt[0].x = iter->point[1].x;
		pTp->controlPt[0].y = iter->point[1].y;
		pTp->controlPt[1].x = iter->point[2].x;
		pTp->controlPt[1].y = iter->point[2].y;
		pTp->point.x = iter->point[0].x;
		pTp->point.y = iter->point[0].y;
		if (minY > pTp->point.y)
		{
			minY = pTp->point.y;
		}
		if (maxY < pTp->point.y)
		{
			maxY = pTp->point.y;
		}
	}
	if (headXState == 2)
	{
		if (xTp.controlPt[0].x > m_bezier.GetMaxX())
		{
			xTp.controlPt[0].x = m_bezier.GetMaxX();
		}
		m_bezier.ModifyEndPoint(0, xTp.point.y, xTp.controlPt[0]);
	}
	if (headYState == 2)
	{
		if (yTp.controlPt[0].x > m_bezier.GetMaxX())
		{
			yTp.controlPt[0].x = m_bezier.GetMaxX();
		}
		m_bezier.ModifyEndPoint(1, yTp.point.y, yTp.controlPt[0]);
	}
	if (maxY - minY < m_param.fYdiff)
	{
		maxY = (maxY + minY) / 2;
		minY = maxY - m_param.fYdiff / 2;
		maxY += m_param.fYdiff / 2;
	}

	const Nuclear::AbstractEffectClip::SP_VECTOR& sp = pClip->GetToolsSamplePoints(m_param.kt);
	Nuclear::AbstractEffectClip::SP_VECTOR::const_iterator spIter = sp.begin();
	bezier::SamplePointContanter spc;
	bezier::SamplePoint samplePoint;
	samplePoint.inited = false;
	for (;spIter!=sp.end();++spIter)
	{
		samplePoint.u = spIter->scalar;
		if (samplePoint.u > 1.5f)
			continue;
		samplePoint.id = spIter->id;
		spc.push_back(samplePoint);
	}

	m_bezier.SetSamples(spc);



	m_bezier.SetMinY(minY - abs(maxY + minY) / 2);
	m_bezier.SetMaxY(maxY + abs(maxY + minY) / 2);
	m_bezier.unitX = 1;
	m_bezier.vectorCurveColor.push_back(RGB(0, 255, 128));
	m_bezier.vectorCurveColor.push_back(RGB(0, 0, 255));
	m_comboWorkMode.InsertString(0, L"曲线");
	m_comboWorkMode.InsertString(1, L"采样");
	m_comboWorkMode.SetCurSel(0);
	ResetSize();

	return TRUE;  

}

void CDlgPropDoubleCurve::OnBnClickedOk()
{
	const bezier::SamplePointContanters& sps = m_bezier.GetSamplePoints();
	Nuclear::AbstractEffectClip::KP_VECTOR posVector;
	Nuclear::AbstractEffectClip::KeyPoint kp;
	const bezier::TangentPointContanters& tp = m_bezier.GetTangentPoints();
	bezier::TangentPointContanters::const_iterator tpIter = tp.begin();
	bezier::TangentPointContanter::const_iterator tpcIter;
	int i = 0;
	for (;tpIter!=tp.end();++tpIter,++i)
	{
		tpcIter = tpIter->begin();
		for (;tpcIter!=tpIter->end();++tpcIter)
		{
			kp.point[0].x = tpcIter->point.x;
			kp.point[0].y = tpcIter->point.y;
			kp.point[1].x = tpcIter->controlPt[0].x;
			kp.point[1].y = tpcIter->controlPt[0].y;
			kp.point[2].x = tpcIter->controlPt[1].x;
			kp.point[2].y = tpcIter->controlPt[1].y;
			kp.t = tpcIter->type;
			kp.t += i*2;
			posVector.push_back(kp);
		}
	}	
	bezier::SamplePointContanters::const_iterator spIter = sps.begin();
	bezier::SamplePointContanter::const_iterator spcIter;
	Nuclear::AbstractEffectClip::SP_VECTOR posSampleVector;
	Nuclear::AbstractEffectClip::SamplePoint sp;
	spcIter = spIter->begin();

	for (;spcIter!=spIter->end();++spcIter)
	{
		sp.id = spcIter->id;
		sp.scalar = spcIter->u;
		posSampleVector.push_back(sp);
	}
	bezier::SamplePointContanter::const_iterator spcIterY;
	spIter = sps.begin();
	spcIter = spIter->begin();
	++spIter;
	spcIterY = spIter->begin();
	DoubleKeyMap keyMap;
	Nuclear::FPOINT fp;
	for (;spcIterY!=spIter->end();++spcIter,++spcIterY)
	{
		fp.x = spcIter->point.y;
		fp.y = spcIterY->point.y;
		keyMap[Round(spcIter->point.x)] = fp;
	}
	m_param.pDoc->SetSelectedClipDoubleKeys(m_param.kt, posVector, posSampleVector, keyMap);
	OnOK();
}

void CDlgPropDoubleCurve::ResetSize()
{
	if (m_bezier.m_hWnd)
	{
		CRect rect;
		GetClientRect(&rect);
		rect.left += 5;
		rect.top += 2;
		rect.bottom -= 30;
		rect.right -= 15;
		m_bezier.unitY = (float)(Round((m_bezier.GetMaxY() - m_bezier.GetMinY()) * 1000 / rect.Height()) / 200);
		if (fabs(m_bezier.unitY) < EPSILON)
		{
			m_bezier.unitY = (m_bezier.GetMaxY() - m_bezier.GetMinY()) * 5 / rect.Height();
		}
		m_bezier.MoveWindow(&rect);
		m_bezier.ShowWindow(SW_SHOW);
		rect.top = rect.bottom + 4;
		rect.bottom += 28;
		rect.left = rect.right - 50;
		m_ButtonCancel.MoveWindow(&rect);
		rect.right = rect.left - 5;
		rect.left = rect.right - 50;
		m_ButtonOk.MoveWindow(&rect);
		rect.right = rect.left - 5;
		rect.left = rect.right - 60;
		rect.top += 1;
		rect.bottom -= 1;
		m_comboWorkMode.MoveWindow(&rect);
		rect.right = rect.left - 5;
		rect.left -= 50;
		m_EditSampleCount.MoveWindow(&rect);
		rect.right = rect.left - 5;
		rect.left -= 79;
		rect.top += 4;
		m_StaticSampleCount.MoveWindow(&rect);
		rect.top -= 4;
		rect.right = rect.left - 5;
		rect.left = rect.right - 60;
		GetDlgItem(IDC_BUTTON_RESET)->MoveWindow(&rect);
	}
}



BEGIN_MESSAGE_MAP(CDlgPropDoubleCurve, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgPropDoubleCurve::OnBnClickedOk)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_CBN_SELENDOK(IDC_COMBO_WORK_MODE, &CDlgPropDoubleCurve::OnCbnSelendokComboWorkMode)
	ON_BN_CLICKED(IDC_BUTTON_RESET, &CDlgPropDoubleCurve::OnBnClickedButtonReset)
	ON_EN_KILLFOCUS(IDC_EDIT_SAMPLE_COUNT, &CDlgPropDoubleCurve::OnEnKillfocusEditSampleCount)
	ON_WM_LBUTTONDOWN()
END_MESSAGE_MAP()

void CDlgPropDoubleCurve::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_COMBO_WORK_MODE, m_comboWorkMode);
	DDX_Control(pDX, IDOK, m_ButtonOk);
	DDX_Control(pDX, IDCANCEL, m_ButtonCancel);
	DDX_Control(pDX, IDC_STATIC_SAMPLE_COUNT, m_StaticSampleCount);
	DDX_Control(pDX, IDC_EDIT_SAMPLE_COUNT, m_EditSampleCount);
	DDX_Text(pDX, IDC_EDIT_SAMPLE_COUNT, m_nSampleCount);
}


CDlgPropDoubleCurve::~CDlgPropDoubleCurve()
{
}

CDlgPropDoubleCurve::CDlgPropDoubleCurve(const ConstructorParam& param)
: CDialog(CDlgPropDoubleCurve::IDD, NULL), m_param(param), m_nSampleCount(2)
{
	m_param.fYdiff = fabs(m_param.fYdiff);
}

