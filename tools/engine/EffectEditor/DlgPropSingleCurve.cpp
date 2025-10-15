

#include "stdafx.h"
#include "EffectEditor.h"
#include "DlgPropSingleCurve.h"
#include "InputDialog.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CDlgPropSingleCurve, CDialog)

void CDlgPropSingleCurve::OnLButtonDown(UINT nFlags, CPoint point)
{
	m_StaticSampleCount.SetFocus();
	__super::OnLButtonDown(nFlags, point);
}
void CDlgPropSingleCurve::OnEnKillfocusEditSampleCount()
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

void CDlgPropSingleCurve::OnBnClickedButtonReset()
{
	CInputDialog dlg;
	if (dlg.DoModal() == IDOK)
	{
		bezier::FPoint pt;
		pt.x = m_bezier.GetMinX();
		pt.y = dlg.fInput;
		m_bezier.CleanAllLinePoints();
		m_bezier.ModifyStartPoint(0, pt.y, pt);
		pt.x = m_bezier.GetMaxX();
		m_bezier.ModifyEndPoint(0, pt.y, pt);
		m_bezier.SetWorkMode(bezier::CBezierControl::WORK_MODE_SAMPLEING);
		m_comboWorkMode.SetCurSel(1);
		m_nSampleCount = 2;
		m_bezier.SetSampleCount(3);
		m_bezier.SetSampleCount(2);
		UpdateData(FALSE);
	}
}

void CDlgPropSingleCurve::OnCbnSelendokComboWorkMode()
{
	switch (m_comboWorkMode.GetCurSel())
	{
	case 0:
		m_bezier.SetWorkMode(bezier::CBezierControl::WORK_MODE_CURVE);
		break;
	case 1:
		UpdateData();
		m_bezier.SetWorkMode(bezier::CBezierControl::WORK_MODE_SAMPLEING);
		m_bezier.SetSampleCount(m_nSampleCount);
		break;
	}
}

void CDlgPropSingleCurve::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CDlgPropSingleCurve::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

BOOL CDlgPropSingleCurve::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_StaticSampleCount.SetWindowText(L"采样点数量：");
	SetWindowText(m_Param.title);
	m_ButtonOk.EnableWindow(false);
	m_bezier.Create(m_bezier.IDD, this);
	m_bezier.SetNotify(this);
	ResetSize();
	m_bezier.SetMinX(0);
	int endFrame, startFrame;
	eAnimationPlayType playType;
	const Nuclear::AbstractEffectClip* pClip = *m_Param.pDoc->GetSelectedClips().begin();
	m_Param.pDoc->GetClipStartEndFrame(pClip, playType, startFrame, endFrame);
	if (playType == APT_SPEC_LOOP)
	{
		endFrame = m_Param.pDoc->GetTotalFrames();
	}
	m_bezier.SetMaxX(static_cast<float>(endFrame - startFrame));
	m_bezier.xMustInt = true;
	const Nuclear::AbstractEffectClip::KP_VECTOR& posVector = pClip->GetToolsKeyPoints(m_Param.kt);
	Nuclear::AbstractEffectClip::KP_VECTOR::const_iterator iter = posVector.begin();
	float minY, maxY;
	m_bezier.SetLineCount(1);
	if (iter == posVector.end())
	{
		maxY = m_Param.fYdiff / 2;
		minY = -maxY;
		bezier::FPoint pt;
		pt.x = 0.0f;
		if (m_Param.bUseDefaultY)
		{
			pt.y = m_Param.fDefaultY;
		} else {
			pt.y = (maxY + minY) / 2;
		}
		if (m_Param.kt == Nuclear::AbstractEffectClip::KT_ROTATION)
		{
			pt.y *= 180 / Nuclear::XP_PI;
		}
		m_bezier.ModifyStartPoint(0, pt.y, pt);
		pt.x = m_bezier.GetMaxX();
		m_bezier.ModifyEndPoint(0, pt.y, pt);
	} else {
		minY = iter->point[0].y;
		maxY = iter->point[0].y;
		if (m_Param.kt == Nuclear::AbstractEffectClip::KT_ROTATION)
		{
			minY *= 180 / Nuclear::XP_PI;
			maxY = minY;
		}
	}

	bezier::TangentPoint tp;
	bezier::TangentPoint* pTp;
	int headState = 0;	
	bezier::FPoint pt;
	for (;iter!=posVector.end();++iter)
	{
		if (headState == 0)
		{
			pt.x = iter->point[2].x;
			pt.y = iter->point[2].y;
			float fY = iter->point[0].y;
			if (m_Param.kt == Nuclear::AbstractEffectClip::KT_ROTATION)
			{
				pt.y *= 180 / Nuclear::XP_PI;
				fY *= 180 / Nuclear::XP_PI;
			}
			m_bezier.ModifyStartPoint(0, fY, pt);
			headState = 1;
			continue;
		} else if (headState == 1)
		{
			headState = 2;
		} else if (headState == 2)
		{
			if (tp.point.x < m_bezier.GetMaxX())
			{
				m_bezier.AddPoint(0, tp);
			}
		}
		pTp = &tp;
		pTp->type = ((iter->t%2)==0)?bezier::POINT_TYPE_SMOOTH:bezier::POINT_TYPE_CORNER;
		pTp->controlPt[0].x = iter->point[1].x;
		pTp->controlPt[0].y = iter->point[1].y;
		pTp->controlPt[1].x = iter->point[2].x;
		pTp->controlPt[1].y = iter->point[2].y;
		pTp->point.x = iter->point[0].x;
		pTp->point.y = iter->point[0].y;
		if (m_Param.kt == Nuclear::AbstractEffectClip::KT_ROTATION)
		{
			pTp->controlPt[0].y *= 180 / Nuclear::XP_PI;
			pTp->controlPt[1].y *= 180 / Nuclear::XP_PI;
			pTp->point.y *= 180 / Nuclear::XP_PI;
		}
		if (minY > pTp->point.y)
		{
			minY = pTp->point.y;
		}
		if (maxY < pTp->point.y)
		{
			maxY = pTp->point.y;
		}
	}
	if (headState == 2)
	{
		if (tp.controlPt[0].x > m_bezier.GetMaxX())
		{
			tp.controlPt[0].x = m_bezier.GetMaxX();
		}
		m_bezier.ModifyEndPoint(0, tp.point.y, tp.controlPt[0]);
	}


	const Nuclear::AbstractEffectClip::SP_VECTOR& sp = pClip->GetToolsSamplePoints(m_Param.kt);
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


	float middle = (maxY + minY) / 2;
	minY -= middle;
	maxY += middle;
	if (minY < m_Param.fMinMinY)
		minY = m_Param.fMinMinY;
	if (maxY > m_Param.fMaxMaxY)
		maxY = m_Param.fMaxMaxY;
	if (maxY - minY < m_Param.fYdiff)
	{
		middle = (maxY + minY) / 2;
		if (middle - m_Param.fYdiff / 2 < m_Param.fMinMinY)
		{
			minY = m_Param.fMinMinY;
			maxY = minY + m_Param.fYdiff;
			if (maxY > m_Param.fMaxMaxY)
				maxY = m_Param.fMaxMaxY;
		} else if (middle + m_Param.fYdiff / 2 > m_Param.fMaxMaxY)
		{
			maxY = m_Param.fMaxMaxY;
			minY = maxY - m_Param.fYdiff;
			if (minY < m_Param.fMinMinY)
				minY = m_Param.fMinMinY;
		} else {
			minY = middle - m_Param.fYdiff / 2;
			maxY = minY + m_Param.fYdiff;
		}
	}

	m_bezier.SetMinY(minY);
	m_bezier.SetMaxY(maxY);
	m_bezier.unitX = 1;
	m_bezier.vectorCurveColor.push_back(RGB(0, 0, 255));
	m_comboWorkMode.InsertString(0, L"曲线");
	m_comboWorkMode.InsertString(1, L"采样");
	m_comboWorkMode.SetCurSel(0);

	ResetSize();
	return TRUE;  

}

void CDlgPropSingleCurve::OnBnClickedOk()
{
	const bezier::SamplePointContanters& sps = m_bezier.GetSamplePoints();
	Nuclear::AbstractEffectClip::KP_VECTOR posVector;
	Nuclear::AbstractEffectClip::KeyPoint kp;
	const bezier::TangentPointContanters& tp = m_bezier.GetTangentPoints();
	bezier::TangentPointContanters::const_iterator tpIter = tp.begin();
	bezier::TangentPointContanter::const_iterator tpcIter;
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
		if (m_Param.kt == Nuclear::AbstractEffectClip::KT_ROTATION)
		{
			kp.point[0].y *= Nuclear::XP_PI / 180;
			kp.point[1].y *= Nuclear::XP_PI / 180;
			kp.point[2].y *= Nuclear::XP_PI / 180;
		}
		posVector.push_back(kp);
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
	spIter = sps.begin();
	spcIter = spIter->begin();
	SingleKeyMap keyMap;
	for (;spcIter!=spIter->end();++spcIter)
	{
		float fvalue = spcIter->point.y;
		if (m_Param.kt == Nuclear::AbstractEffectClip::KT_ROTATION)
		{
			fvalue *= Nuclear::XP_PI / 180;
		}
		keyMap[Round(spcIter->point.x)] = fvalue;
	}
	m_Param.pDoc->SetSelectedClipSingleKeys(m_Param.kt, posVector, posSampleVector, keyMap);
	OnOK();
}

void CDlgPropSingleCurve::ResetSize()
{
	if (m_bezier.m_hWnd)
	{
		CRect rect;
		GetClientRect(&rect);
		rect.left += 5;
		rect.top += 2;
		rect.bottom -= 30;
		rect.right -= 5;
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



BEGIN_MESSAGE_MAP(CDlgPropSingleCurve, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgPropSingleCurve::OnBnClickedOk)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_CBN_SELENDOK(IDC_COMBO_WORK_MODE, &CDlgPropSingleCurve::OnCbnSelendokComboWorkMode)
	ON_BN_CLICKED(IDC_BUTTON_RESET, &CDlgPropSingleCurve::OnBnClickedButtonReset)
	ON_EN_KILLFOCUS(IDC_EDIT_SAMPLE_COUNT, &CDlgPropSingleCurve::OnEnKillfocusEditSampleCount)
	ON_WM_LBUTTONDOWN()
END_MESSAGE_MAP()

void CDlgPropSingleCurve::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_COMBO_WORK_MODE, m_comboWorkMode);
	DDX_Control(pDX, IDOK, m_ButtonOk);
	DDX_Control(pDX, IDCANCEL, m_ButtonCancel);
	DDX_Control(pDX, IDC_STATIC_SAMPLE_COUNT, m_StaticSampleCount);
	DDX_Control(pDX, IDC_EDIT_SAMPLE_COUNT, m_EditSampleCount);
	DDX_Text(pDX, IDC_EDIT_SAMPLE_COUNT, m_nSampleCount);
}


CDlgPropSingleCurve::~CDlgPropSingleCurve()
{
}

CDlgPropSingleCurve::CDlgPropSingleCurve(const ConstructorParam& param)
: CDialog(CDlgPropSingleCurve::IDD, NULL), m_Param(param), m_nSampleCount(2)
{

}

