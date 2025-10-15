

#include "stdafx.h"
#include "MapEditor.h"
#include "../engine/map/pmap.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "TimeEffectEditorDlg.h"

#ifdef _DEBUG
#include "..\engine\common\log.h"
#endif



IMPLEMENT_DYNAMIC(CTimeEffectEditorDlg, CDialog)

void CTimeEffectEditorDlg::CaculateTwoAdjustPoints()
{

	float x1 = 0.0f;
	float y1 = 0.0f;
	float x2 = 0.0f;
	float y2 = 0.0f;

	bool bFind = false;

	Nuclear::FPOINT pt[2];

	if( (m_ColorBalanceParam.fContrastb>=0) && (m_ColorBalanceParam.fContrastb<=1) )
	{
		pt[0].x = 0.0f;
		pt[0].y = m_ColorBalanceParam.fContrastb;
		bFind = true;
	}

	float temp = m_ColorBalanceParam.fContrastk+m_ColorBalanceParam.fContrastb;
	if( (temp>=0) && (temp<=1) )
	{
		if(bFind)
		{
			pt[1].x = 1.0f;
			pt[1].y = temp;
		}
		else
		{
			pt[0].x = 1.0f;
			pt[0].y = temp;
			bFind = true;
		}
	}

	temp = -m_ColorBalanceParam.fContrastb/m_ColorBalanceParam.fContrastk;
	if( (temp>=0) && (temp<=1) )
	{
		if(bFind)
		{
			pt[1].x = temp;
			pt[1].y = 0.0f;
		}
		else
		{
			pt[0].x = temp;
			pt[0].y = 0.0f;
			bFind = true;
		}
	}

	temp = (1.0f-m_ColorBalanceParam.fContrastb)/m_ColorBalanceParam.fContrastk;
	if( (temp>=0) && (temp<=1) )
	{
		if(bFind)
		{
			pt[1].x = temp;
			pt[1].y = 1.0f;
		}
		else
		{
			pt[0].x = temp;
			pt[0].y = 1.0f;
			bFind = true;
		}
	}

	for(int i=0; i<2; i++)
	{
		m_AdjustPoints[i].m_Point.x = (int)( pt[i].x *m_CurveRect.Width() +m_CurveRect.left );
		m_AdjustPoints[i].m_Point.y = (int)( pt[i].y *m_CurveRect.Height() );

		m_AdjustPoints[i].m_Point.y = m_CurveRect.Height()-m_AdjustPoints[i].m_Point.y +m_CurveRect.top;
	}

	CRect rct = m_CurveRect;
	rct.InflateRect(3, 3);
	InvalidateRect(&rct, true);
}

float CTimeEffectEditorDlg::CaculateTwoPointDistance(CPoint pt1, CPoint pt2)
{
	float dis = powf((float)( pt1.x-pt2.x ), 2.0f) + powf((float)( pt1.y-pt2.y ), 2.0f);
	return sqrt(dis);
}

void CTimeEffectEditorDlg::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	if( m_CurveRect.PtInRect(point) )
	{
		m_AdjustPoints[0].m_Point = CPoint(m_CurveRect.left, m_CurveRect.bottom);
		m_AdjustPoints[1].m_Point = CPoint(m_CurveRect.right, m_CurveRect.top);
		m_AdjustPoints[0].m_bSel = false;
		m_AdjustPoints[1].m_bSel = false;

		m_ColorBalanceParam.fContrastk = 1.0f;
		m_ColorBalanceParam.fContrastb = 0.0f;
		ModifyParams();

		Invalidate(true);
	}
	CDialog::OnLButtonDblClk(nFlags, point);
}

void CTimeEffectEditorDlg::OnLButtonUp(UINT nFlags, CPoint point)
{
	bool bSel = false;
	for(int i=0; i<2; i++)
	{
		bSel |= m_AdjustPoints[i].m_bSel;
		m_AdjustPoints[i].m_bSel = false;
	}

	if(bSel)
	{
		float cx = (float)m_CurveRect.Width();
		float cy = (float)m_CurveRect.Height();
		float x1 = (m_AdjustPoints[0].m_Point.x - m_CurveRect.left)/cx;
		float y1 = (m_AdjustPoints[0].m_Point.y - m_CurveRect.top)/cy;
		float x2 = (m_AdjustPoints[1].m_Point.x - m_CurveRect.left)/cx;
		float y2 = (m_AdjustPoints[1].m_Point.y - m_CurveRect.top)/cy;
		if( fabs(x2-x1)<0.0001f )
		{
			m_ColorBalanceParam.fContrastk = 100;
			m_ColorBalanceParam.fContrastb = 100;
		}
		else
		{
			m_ColorBalanceParam.fContrastk = -(y2-y1)/(x2-x1);
			m_ColorBalanceParam.fContrastb = 1-y1 - m_ColorBalanceParam.fContrastk*x1;
		}

		ModifyParams();
	}

	CDialog::OnLButtonUp(nFlags, point);
}

void CTimeEffectEditorDlg::OnMouseMove(UINT nFlags, CPoint point)
{
	if( nFlags==MK_LBUTTON )
	{
		int nCurIndex = -1;
		bool bSel = false;
		for(int i=0; i<2; i++)
		{
			bSel = m_AdjustPoints[i].m_bSel;
			if(bSel)
			{
				nCurIndex = i;
				break;
			}
		}

		if(bSel)
		{
			CPoint CurPoint = m_AdjustPoints[nCurIndex].m_Point;
			int dx = point.x - CurPoint.x;
			int dy = point.y - CurPoint.y;

			if( (CurPoint.x == m_CurveRect.left) || (CurPoint.x == m_CurveRect.right) )
			{
				if(abs(dy) > abs(dx))
				{
					int y = point.y;
					y = y>m_CurveRect.bottom ? m_CurveRect.bottom:y;
					y = y<m_CurveRect.top ? m_CurveRect.top:y;
					m_AdjustPoints[nCurIndex].m_Point.y = y;
				}
			}
			if( (CurPoint.y == m_CurveRect.top) || (CurPoint.y == m_CurveRect.bottom) )
			{
				if(abs(dx) > abs(dy))
				{
					int x = point.x;
					x = x>m_CurveRect.right ? m_CurveRect.right:x;
					x = x<m_CurveRect.left ? m_CurveRect.left:x;
					m_AdjustPoints[nCurIndex].m_Point.x = x;
				}
			}

			CRect rct = m_CurveRect;
			rct.InflateRect(3, 3);
			InvalidateRect(&rct, true);

			float cx = (float)m_CurveRect.Width();
			float cy = (float)m_CurveRect.Height();
			float x1 = (m_AdjustPoints[0].m_Point.x - m_CurveRect.left)/cx;
			float y1 = (m_AdjustPoints[0].m_Point.y - m_CurveRect.top)/cy;
			float x2 = (m_AdjustPoints[1].m_Point.x - m_CurveRect.left)/cx;
			float y2 = (m_AdjustPoints[1].m_Point.y - m_CurveRect.top)/cy;
			if( fabs(x2-x1)<0.0001f )
			{
				m_ColorBalanceParam.fContrastk = 100;
				m_ColorBalanceParam.fContrastb = 100;
			}
			else
			{
				m_ColorBalanceParam.fContrastk = -(y2-y1)/(x2-x1);
				m_ColorBalanceParam.fContrastb = 1-y1 - m_ColorBalanceParam.fContrastk*x1;
			}
		}
	}

	CDialog::OnMouseMove(nFlags, point);
}

void CTimeEffectEditorDlg::OnLButtonDown(UINT nFlags, CPoint point)
{
	if (m_bIsOnKeyFrame)
	{
		for(int i=0; i<2; i++)
		{
			m_AdjustPoints[i].m_bSel = false;
			float dis = CaculateTwoPointDistance(point, m_AdjustPoints[i].m_Point);
			if(dis<5)
				m_AdjustPoints[i].m_bSel = true;
		}
		Invalidate(false);
	}

	CDialog::OnLButtonDown(nFlags, point);
}

void CTimeEffectEditorDlg::OnPaint()
{
	CPaintDC dc(this); 
	int bkMode = dc.GetBkMode();
	HPEN hOldPen;
	HPEN hGrayDOTPen = CreatePen(PS_DOT, 1, RGB(100, 100, 100) );
	HPEN hPurplePen = CreatePen(PS_SOLID, 1, RGB(255, 0, 255) );
	HPEN hGreenPen = CreatePen(PS_SOLID, 1, RGB(0, 255, 0) );
	dc.SetBkMode(TRANSPARENT);
	int nWidth = m_CurveRect.Width();
	int nHeight = m_CurveRect.Height();
	float dx = nWidth/10.0f;
	float dy = nHeight/10.0f;
	hOldPen = (HPEN)dc.SelectObject(hGrayDOTPen);
	for(int i=1; i<10; i++)
	{
		dc.MoveTo((int)(m_CurveRect.left +i*dx), m_CurveRect.top);
		dc.LineTo((int)(m_CurveRect.left +i*dx), m_CurveRect.bottom);
		dc.MoveTo(m_CurveRect.left, (int)(m_CurveRect.top +i*dy));
		dc.LineTo(m_CurveRect.right, (int)(m_CurveRect.top +i*dy));
	}
	dc.SelectObject(hPurplePen);
	dc.MoveTo(m_AdjustPoints[0].m_Point);
	dc.LineTo(m_AdjustPoints[1].m_Point);

	for(int i=0; i<2; i++)
	{
		CPoint pt = m_AdjustPoints[i].m_Point;
		if(m_AdjustPoints[i].m_bSel)
			dc.SelectObject(hGreenPen);
		else
			dc.SelectObject(hPurplePen);
		dc.Ellipse(pt.x-3, pt.y-3, pt.x+3, pt.y+3);
	}

	dc.SelectObject(hOldPen);
	dc.SetBkMode(bkMode);
	DeleteObject(hGrayDOTPen);
	DeleteObject(hPurplePen);
	DeleteObject(hGreenPen);

}

BOOL CTimeEffectEditorDlg::PreTranslateMessage(MSG* pMsg)
{
	if(pMsg->message == WM_KEYDOWN && IsCTRLPressed())
	{
		if (pMsg->wParam == 'Z')
		{
			m_pMapEditorView->UnDo();
			return TRUE;
		} else if (pMsg->wParam == 'Y')
		{
			m_pMapEditorView->ReDo();
			return TRUE;
		}
	}

	return CDialog::PreTranslateMessage(pMsg);
}

void CTimeEffectEditorDlg::Reset()
{
	ResetKeyFrames();
	SetItemState(GetCurrentTimeBarParam());
}


void CTimeEffectEditorDlg::OnShowWindow(BOOL bShow, UINT nStatus)
{
	CDialog::OnShowWindow(bShow, nStatus);
	m_pMapEditorView->SetSettingTimeEffect(bShow == TRUE);
}

void CTimeEffectEditorDlg::OnCancel()
{
	m_pMapEditorView->SetSettingTimeEffect(false);
	CDialog::OnCancel();
}

void CTimeEffectEditorDlg::OnOK() {}

void CTimeEffectEditorDlg::OnBnClickedStop()
{
	m_btnStop.EnableWindow(false);
	m_pMapEditorView->SetPlayingTime(false);
}

void CTimeEffectEditorDlg::SetPlay(bool isPlay)
{
	m_btnStop.EnableWindow(isPlay);
}

void CTimeEffectEditorDlg::OnBnClickedPlayQuickly()
{
	m_pMapEditorView->SetGameTimeScaleFactor(50);
	m_pMapEditorView->SetPlayingTime(true);
	m_pMapEditorView->SetGameTime(m_sliderTimeBar.GetPos());
	m_btnStop.EnableWindow(true);
}

void CTimeEffectEditorDlg::OnBnClickedPlay()
{
	m_pMapEditorView->SetGameTimeScaleFactor(0.5);
	m_pMapEditorView->SetPlayingTime(true);
	m_pMapEditorView->SetGameTime(m_sliderTimeBar.GetPos());
	m_btnStop.EnableWindow(true);
}

void CTimeEffectEditorDlg::OnLbnSelchangeList1()
{
	m_sliderTimeBar.SetPos(static_cast<int>(m_listKeyFrames.GetItemData(m_listKeyFrames.GetCurSel())));
	SetItemState(GetCurrentTimeBarParam());
}

void CTimeEffectEditorDlg::OnBnClickedAddRemoveFrame()
{
	int nTime = m_pMapEditorView->GetGameTime();
	CAction *pOperator = new CAction(CAction::AT_TIME_EFFECT, m_pMapEditorView->GetLayerEditId());
	CAction::CTimeEffectActionAtom* pOper = pOperator->GetTimeEffectActionData();
	pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = m_ColorBalanceParam;
	pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_nTime = nTime;
	pOper->oper[CAction::CActionAtom::AFTER_OPERATOR] = m_ColorBalanceParam;
	pOper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_nTime = nTime;
	if (m_bIsOnKeyFrame)
	{
		m_pMapEditorView->GetDocument()->GetToolsMap().RemoveAnTimeEffect(nTime);
		pOper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_nTime = -1;
	} else {
		m_pMapEditorView->GetDocument()->GetToolsMap().AddAnTimeEffect(nTime, m_ColorBalanceParam);
		pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_nTime = -1;
	}
	m_pMapEditorView->DoAction(pOperator);
	ResetKeyFrames();
	SetItemState(GetCurrentTimeBarParam());
}

void CTimeEffectEditorDlg::ModifyParams()
{
	Nuclear::PMap &map = m_pMapEditorView->GetDocument()->GetToolsMap();
	const Nuclear::SubMap::TimeEffectParamMap &paramMap = map.GetTimeEffectParams();
	int nTime = m_pMapEditorView->GetGameTime();
	Nuclear::SubMap::TimeEffectParamMap::const_iterator iter = paramMap.find(nTime);
	if (iter == paramMap.end())
		return;
	CAction *pOperator = new CAction(CAction::AT_TIME_EFFECT, m_pMapEditorView->GetLayerEditId());
	CAction::CTimeEffectActionAtom* pOper = pOperator->GetTimeEffectActionData();
	const Nuclear::XPColorBalanceParam &colorBalance = iter->second;
	pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = colorBalance;
	pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_nTime = nTime;
	pOper->oper[CAction::CActionAtom::AFTER_OPERATOR] = m_ColorBalanceParam;
	pOper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_nTime = nTime;
	if (m_ColorBalanceParam.fCoefr != colorBalance.fCoefr || m_ColorBalanceParam.fCoefg != colorBalance.fCoefg
		|| m_ColorBalanceParam.fCoefb != colorBalance.fCoefb || m_ColorBalanceParam.fLightness != colorBalance.fLightness
		|| m_ColorBalanceParam.fContrastb != colorBalance.fContrastb || m_ColorBalanceParam.fContrastk != colorBalance.fContrastk)
	{
		map.ModifyAnTimeEffect(nTime, nTime, m_ColorBalanceParam);
		m_pMapEditorView->DoAction(pOperator);
	} else {
		delete (pOperator);
	}

}

void CTimeEffectEditorDlg::UpdateParam()
{
	m_sliderTimeBar.SetPos(m_pMapEditorView->GetGameTime());
	SetItemState(GetCurrentTimeBarParam());
}

BOOL CTimeEffectEditorDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_sliderCoefR.SetRange(-100, 100, TRUE);
	m_sliderCoefG.SetRange(-100, 100, TRUE);
	m_sliderCoefB.SetRange(-100, 100, TRUE);
	m_sliderLightness.SetRange(-100, 100, TRUE);
	m_sliderTimeBar.SetRange(0, Nuclear::XP_A_DAY_TIME, TRUE);
	m_btnStop.EnableWindow(false);

	GetDlgItem(IDC_PICTURECTRL_CONTRASTCURVE)->GetWindowRect(&m_CurveRect);
	ScreenToClient(&m_CurveRect);
	m_AdjustPoints[0].m_Point = CPoint(m_CurveRect.left, m_CurveRect.bottom);
	m_AdjustPoints[1].m_Point = CPoint(m_CurveRect.right, m_CurveRect.top);

	UpdateAllParamSilders();
	ResetKeyFrames();
	SetItemState(GetCurrentTimeBarParam());
	return TRUE;  

}

bool CTimeEffectEditorDlg::GetCurrentTimeBarParam()
{
	const Nuclear::SubMap::TimeEffectParamMap &effectmap = m_pMapEditorView->GetDocument()->GetToolsMap().GetTimeEffectParams();
	Nuclear::SubMap::TimeEffectParamMap::const_iterator iter = effectmap.begin();
	int nTime = m_sliderTimeBar.GetPos();
	m_pMapEditorView->SetGameTime(nTime);
	if (iter == effectmap.end())
	{
		m_ColorBalanceParam.fCoefr = 0.0f;
		m_ColorBalanceParam.fCoefg = 0.0f;
		m_ColorBalanceParam.fCoefb = 0.0f;
		m_ColorBalanceParam.fLightness = 0.0f;
		m_ColorBalanceParam.fContrastk = 1.0f;
		m_ColorBalanceParam.fContrastb = 0.0f;
		CaculateTwoAdjustPoints();
		UpdateData(FALSE);
		UpdateAllParamSilders();
		m_listKeyFrames.SetCurSel(-1);
		return false;
	}
	int listPos = 0;
	Nuclear::SubMap::TimeEffectParamMap::const_iterator preIter = effectmap.end();
	preIter--;
	for (;iter!=effectmap.end();iter++,listPos++)
	{
		if (nTime<=iter->first)
			break;
		preIter = iter;
	}

	if (iter == effectmap.end())
	{
		iter = effectmap.begin();
		listPos = 0;
	}
	int length = iter->first - preIter->first;
	if ((length == 0) || (nTime == iter->first))
	{
		float oldSX = m_ColorBalanceParam.fCentx;
		float oldSY = m_ColorBalanceParam.fCenty;
		m_ColorBalanceParam = iter->second;
		m_ColorBalanceParam.fCentx = oldSX;
		m_ColorBalanceParam.fCenty = oldSY;
		CaculateTwoAdjustPoints();
		UpdateData(FALSE);
		UpdateAllParamSilders();
		m_listKeyFrames.SetCurSel(listPos);
		return nTime == iter->first;
	}
	if (length < 0)
		length += Nuclear::XP_A_DAY_TIME;
	int nPos = nTime - preIter->first;
	if (nPos < 0)
		nPos += Nuclear::XP_A_DAY_TIME;
	m_ColorBalanceParam.fCoefr = preIter->second.fCoefr + (iter->second.fCoefr - preIter->second.fCoefr) * nPos / length;
	m_ColorBalanceParam.fCoefg = preIter->second.fCoefg + (iter->second.fCoefg - preIter->second.fCoefg) * nPos / length;
	m_ColorBalanceParam.fCoefb = preIter->second.fCoefb + (iter->second.fCoefb - preIter->second.fCoefb) * nPos / length;
	m_ColorBalanceParam.fLightness = preIter->second.fLightness + (iter->second.fLightness - preIter->second.fLightness) * nPos / length;
	m_ColorBalanceParam.fContrastk = preIter->second.fContrastk + (iter->second.fContrastk - preIter->second.fContrastk) * nPos / length;
	m_ColorBalanceParam.fContrastb = preIter->second.fContrastb + (iter->second.fContrastb - preIter->second.fContrastb) * nPos / length;
	m_listKeyFrames.SetCurSel(-1);
	CaculateTwoAdjustPoints();
	UpdateData(FALSE);
	UpdateAllParamSilders();
	return false;
}

void CTimeEffectEditorDlg::SetItemState(bool isKeyFrame)
{
	m_sliderCoefR.EnableWindow(isKeyFrame);
	m_sliderCoefG.EnableWindow(isKeyFrame);
	m_sliderCoefB.EnableWindow(isKeyFrame);
	m_sliderLightness.EnableWindow(isKeyFrame);

	GetDlgItem(IDC_EDIT1)->EnableWindow(isKeyFrame);
	GetDlgItem(IDC_EDIT_COEF_R)->EnableWindow(isKeyFrame);
	GetDlgItem(IDC_EDIT_COEF_G)->EnableWindow(isKeyFrame);
	GetDlgItem(IDC_EDIT_COEF_B)->EnableWindow(isKeyFrame);
	GetDlgItem(IDC_EDIT_LIGHT_NESS)->EnableWindow(isKeyFrame);

	m_btnAddDelFrame.SetWindowText(isKeyFrame?_T("É¾³ý¹Ø¼üÖ¡"):_T("Ìí¼Ó¹Ø¼üÖ¡"));
	m_bIsOnKeyFrame = isKeyFrame;
}

void CTimeEffectEditorDlg::ResetKeyFrames()
{
	const Nuclear::SubMap::TimeEffectParamMap &effectmap = m_pMapEditorView->GetDocument()->GetToolsMap().GetTimeEffectParams();
	Nuclear::SubMap::TimeEffectParamMap::const_iterator iter = effectmap.begin();
	m_listKeyFrames.ResetContent();
	CString name;
	int nID = 0;
	for (;iter!=effectmap.end();iter++,nID++)
	{
		name.Format(_T("%d"), iter->first);
		m_listKeyFrames.InsertString(nID, name);
		m_listKeyFrames.SetItemData(nID, iter->first);
	}
}

BOOL CTimeEffectEditorDlg::Create(CMapEditorView* pMapEditorView/* = NULL*/)
{
	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

void CTimeEffectEditorDlg::UpdateAllParamSilders()
{
	UpdateSilder(&m_sliderCoefR, m_ColorBalanceParam.fCoefr);
	UpdateSilder(&m_sliderCoefG, m_ColorBalanceParam.fCoefg);
	UpdateSilder(&m_sliderCoefB, m_ColorBalanceParam.fCoefb);
	UpdateSilder(&m_sliderLightness, m_ColorBalanceParam.fLightness);
	UpdateColorEditor(&m_editColorValue,m_ColorBalanceParam);
}


void CTimeEffectEditorDlg::OnEnKillfocusEditForSlider(UINT nID)
{
	if (UpdateData(TRUE))
	{
		switch(nID)
		{
		case IDC_EDIT_COEF_R:
			UpdateSilder(&m_sliderCoefR, m_ColorBalanceParam.fCoefr);
			break;
		case IDC_EDIT_COEF_G:
			UpdateSilder(&m_sliderCoefG, m_ColorBalanceParam.fCoefg);
			break;
		case IDC_EDIT_COEF_B:
			UpdateSilder(&m_sliderCoefB, m_ColorBalanceParam.fCoefb);
			break;
		case IDC_EDIT_LIGHT_NESS:
			UpdateSilder(&m_sliderLightness, m_ColorBalanceParam.fLightness);
			break;
		}
		ModifyParams();
	}
}

void CTimeEffectEditorDlg::OnEnKillfocusEdit1()
{
	int oldTime = m_pMapEditorView->GetGameTime();
	UpdateData(TRUE);
	int newTime = m_pMapEditorView->GetGameTime();
	if (oldTime != newTime)
	{
		CAction *pOperator = new CAction(CAction::AT_TIME_EFFECT, m_pMapEditorView->GetLayerEditId());
		CAction::CTimeEffectActionAtom* pOper = pOperator->GetTimeEffectActionData();
		pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = m_ColorBalanceParam;
		pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_nTime = oldTime;
		m_pMapEditorView->GetDocument()->GetToolsMap().ModifyAnTimeEffect(oldTime, newTime, m_ColorBalanceParam);
		pOper->oper[CAction::CActionAtom::AFTER_OPERATOR] = m_ColorBalanceParam;
		pOper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_nTime = newTime;
		m_pMapEditorView->DoAction(pOperator);
		m_sliderTimeBar.SetPos(newTime);
		SetItemState(GetCurrentTimeBarParam());
		ResetKeyFrames();
		m_pMapEditorView->GetDocument()->SetModifiedFlag();
	}
}

void CTimeEffectEditorDlg::OnSliderReleasedcapture(UINT nID, NMHDR *pNMHDR, LRESULT *pResult)
{
	ModifyParams();
	*pResult = 0;
}


void CTimeEffectEditorDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	switch (pScrollBar->GetDlgCtrlID())
	{
	case IDC_SLIDER1:
		SetItemState(GetCurrentTimeBarParam());
		break;
	case IDC_SLIDER_COEF_R:
		UpdateText(&m_sliderCoefR, m_ColorBalanceParam.fCoefr);
		break;
	case IDC_SLIDER_COEF_G:
		UpdateText(&m_sliderCoefG, m_ColorBalanceParam.fCoefg);
		break;
	case IDC_SLIDER_COEF_B:
		UpdateText(&m_sliderCoefB, m_ColorBalanceParam.fCoefb);
		break;
	case IDC_SLIDER_LIGHT_NESS:
		UpdateText(&m_sliderLightness, m_ColorBalanceParam.fLightness);
		break;
	}
	UpdateData(FALSE);
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CTimeEffectEditorDlg::UpdateColorEditor(CEdit *pEdit, Nuclear::XPColorBalanceParam& colorParam)
{
	float a = colorParam.fLightness > 0.0f ? colorParam.fLightness : 0.0f;
	float r = colorParam.fCoefr > 0.0f ? colorParam.fCoefr : 0.0f;
	float g = colorParam.fCoefg > 0.0f ? colorParam.fCoefg : 0.0f;
	float b = colorParam.fCoefb > 0.0f ? colorParam.fCoefb : 0.0f;
	int iA = int(a * 255);
	int iR = int(r * 255);
	int iG = int(g * 255);
	int iB = int(b * 255);

	DWORD cVal = iA<<24;
	cVal += iR<<16;
	cVal += iG<<8;
	cVal += iB;
	wchar_t buff[32];
	_itow(cVal, buff, 16);

	pEdit->SetWindowText(buff);
}

void CTimeEffectEditorDlg::UpdateText(CSliderCtrl *pSilder, float &text)
{
	text = static_cast<float>(pSilder->GetPos()) / 100;

	UpdateColorEditor(&m_editColorValue,m_ColorBalanceParam);
}

void CTimeEffectEditorDlg::UpdateSilder(CSliderCtrl *pSilder, const float text)
{
	pSilder->SetPos(static_cast<int>(100 * text));
}

BEGIN_MESSAGE_MAP(CTimeEffectEditorDlg, CDialog)
	ON_WM_HSCROLL()
	ON_EN_KILLFOCUS(IDC_EDIT1, &CTimeEffectEditorDlg::OnEnKillfocusEdit1)
	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_COEF_R, IDC_EDIT_LIGHT_NESS, &CTimeEffectEditorDlg::OnEnKillfocusEditForSlider)
	ON_BN_CLICKED(IDC_ADD_REMOVE_FRAME, &CTimeEffectEditorDlg::OnBnClickedAddRemoveFrame)
	ON_LBN_SELCHANGE(IDC_LIST1, &CTimeEffectEditorDlg::OnLbnSelchangeList1)
	ON_BN_CLICKED(IDC_PLAY, &CTimeEffectEditorDlg::OnBnClickedPlay)
	ON_BN_CLICKED(IDC_PLAY_QUICKLY, &CTimeEffectEditorDlg::OnBnClickedPlayQuickly)
	ON_BN_CLICKED(IDC_STOP, &CTimeEffectEditorDlg::OnBnClickedStop)
	ON_WM_SHOWWINDOW()
	ON_WM_PAINT()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_LBUTTONDBLCLK()
	ON_WM_MOUSEMOVE()
	ON_NOTIFY_RANGE(NM_RELEASEDCAPTURE, IDC_SLIDER_COEF_R, IDC_SLIDER_LIGHT_NESS, &CTimeEffectEditorDlg::OnSliderReleasedcapture)
END_MESSAGE_MAP()

void CTimeEffectEditorDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	int nTime = 0;
	if ((!pDX->m_bSaveAndValidate) && m_pMapEditorView)
	{
		nTime = m_pMapEditorView->GetGameTime();
	}
	DDX_Text(pDX, IDC_EDIT1, nTime);
	if (pDX->m_bSaveAndValidate && m_pMapEditorView)
	{
		if (nTime >= 0 && nTime < Nuclear::XP_A_DAY_TIME)
		{
			m_pMapEditorView->SetGameTime(nTime);
		}
	}
	DDX_Text(pDX, IDC_EDIT_COEF_R, m_ColorBalanceParam.fCoefr);
	DDX_Text(pDX, IDC_EDIT_COEF_G, m_ColorBalanceParam.fCoefg);
	DDX_Text(pDX, IDC_EDIT_COEF_B, m_ColorBalanceParam.fCoefb);
	DDX_Text(pDX, IDC_EDIT_LIGHT_NESS, m_ColorBalanceParam.fLightness);
	DDV_MinMaxFloat(pDX, m_ColorBalanceParam.fCoefr, -1.0f, 1.0f);
	DDV_MinMaxFloat(pDX, m_ColorBalanceParam.fCoefg, -1.0f, 1.0f);
	DDV_MinMaxFloat(pDX, m_ColorBalanceParam.fCoefb, -1.0f, 1.0f);
	DDV_MinMaxFloat(pDX, m_ColorBalanceParam.fLightness, -1.0f, 1.0f);
	DDV_MinMaxInt(pDX, nTime, 0, Nuclear::XP_A_DAY_TIME);
	DDX_Control(pDX, IDC_LIST1, m_listKeyFrames);
	DDX_Control(pDX, IDC_SLIDER_COEF_R, m_sliderCoefR);
	DDX_Control(pDX, IDC_SLIDER_COEF_G, m_sliderCoefG);
	DDX_Control(pDX, IDC_SLIDER_COEF_B, m_sliderCoefB);
	DDX_Control(pDX, IDC_SLIDER_LIGHT_NESS, m_sliderLightness);
	DDX_Control(pDX, IDC_SLIDER1, m_sliderTimeBar);
	DDX_Control(pDX, IDC_ADD_REMOVE_FRAME, m_btnAddDelFrame);
	DDX_Control(pDX, IDC_STOP, m_btnStop);
	DDX_Control(pDX, IDC_EDIT_VERTEX_COLOR, m_editColorValue);
}

CTimeEffectEditorDlg::~CTimeEffectEditorDlg()
{
}

CTimeEffectEditorDlg::CTimeEffectEditorDlg(CWnd* pParent /*=NULL*/)
: CDialog(CTimeEffectEditorDlg::IDD, pParent)
, m_bIsOnKeyFrame(false)
{
	m_ColorBalanceParam.fContrastk = 1.0f;
	m_ColorBalanceParam.fContrastb = 0.0f;
	m_ColorBalanceParam.fCoefr = 0.0f;
	m_ColorBalanceParam.fCoefg = 0.0f;
	m_ColorBalanceParam.fCoefb = 0.0f;
	m_ColorBalanceParam.fLightness = 0.0f;
}

