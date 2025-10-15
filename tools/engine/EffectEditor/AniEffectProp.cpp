

#include "stdafx.h"
#include "EffectEditor.h"
#include "PropertyView.h"
#include "AniEffectProp.h"
#include "EffectEditorDoc.h"
#include "DlgPropDoubleCurve.h"
#include "DlgPropSingleCurve.h"
#include "DlgPropColor.h"

#include "../engine/effect/geffect.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CAniEffectProp, CDialog)

void CAniEffectProp::OnBnClickedRotationButton()
{
	CDlgPropSingleCurve::ConstructorParam param;
	param.fYdiff = 400.0f * 2;
	param.kt = Nuclear::AbstractEffectClip::KT_ROTATION;
	param.pDoc = m_pView->GetDocument();
	param.title = L"旋转曲线";
	param.fMaxMaxY = 400.0f;
	param.fMinMinY = -400.0f;
	CDlgPropSingleCurve dlg(param);
	dlg.DoModal();
}

void CAniEffectProp::OnBnClickedScalButton()
{
	CDlgPropDoubleCurve::ConstructorParam param;
	param.pDoc = m_pView->GetDocument();
	param.fYdiff = 5.0f;
	param.kt = Nuclear::AbstractEffectClip::KT_SCALE;
	param.title = L"缩放曲线";
	CDlgPropDoubleCurve dlg(param);
	dlg.DoModal();
}

void CAniEffectProp::OnBnClickedPosButton()
{
	CDlgPropDoubleCurve::ConstructorParam param;
	param.pDoc = m_pView->GetDocument();
	param.fYdiff = 200.0f;
	param.kt = Nuclear::AbstractEffectClip::KT_POS;
	param.title = L"位置曲线";
	CDlgPropDoubleCurve dlg(param);
	dlg.DoModal();
}

void CAniEffectProp::OnLButtonDown(UINT nFlags, CPoint point)
{
	GetDlgItem(IDC_FILE_NAME)->SetFocus();
	CDialog::OnLButtonDown(nFlags, point);
}

void CAniEffectProp::OnBnClickedIsSpec()
{
	CButton *checkBox = reinterpret_cast<CButton*>(GetDlgItem(IDC_IS_SPEC));
	m_pView->GetDocument()->SetSelectedClipSpec(checkBox->GetCheck() == BST_CHECKED, m_pView);
}

void CAniEffectProp::SavePlayModeAndTime()
{
	m_pView->GetDocument()->SaveSelectedClipPlayModeAndTime(m_nPartType, m_playTimeCount, m_pView);	
}

void CAniEffectProp::OnEnKillfocusTime()
{
	UpdateData(TRUE);
	SavePlayModeAndTime();
}


void CAniEffectProp::OnCbnSelendokComboPlaymode()
{
	switch (m_IDCComboPlayMode.GetCurSel())
	{
	case 0:
		m_nPartType = Nuclear::XPPM_LOOP;
		m_IDCTime.ShowWindow(SW_HIDE);
		break;
	case 1:		
		m_nPartType = Nuclear::XPPM_LOOPNUM;
		m_IDCTime.ShowWindow(SW_SHOW);
		break;
	}
	SavePlayModeAndTime();
	UpdateData(FALSE);
	Invalidate();
}

void CAniEffectProp::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	if (pScrollBar->GetDlgCtrlID() == IDC_SLIDER_ALPHA)
	{
		int newPos = m_SilderAlpha.GetPos();
		Nuclear::XPCOLOR color(newPos, GetRValue(m_color), GetGValue(m_color), GetBValue(m_color));
		if (m_pView->GetDocument()->SetSelectedClipVertexColor(color, m_pView))
		{
			m_nAlpha = newPos;
			UpdateData(FALSE);
		} else {
			m_SilderAlpha.SetPos(m_nAlpha);
		}
	}

	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CAniEffectProp::OnEnKillfocusEditAlpha()
{
	int oldAlpha = m_nAlpha;
	UpdateData(TRUE);
	bool ok = false;
	if (m_nAlpha >=0 && m_nAlpha <= 255)
	{
		Nuclear::XPCOLOR color(m_nAlpha, GetRValue(m_color), GetGValue(m_color), GetBValue(m_color));
		if (m_pView->GetDocument()->SetSelectedClipVertexColor(color, m_pView))
		{
			m_SilderAlpha.SetPos(m_nAlpha);
		} else {
			m_nAlpha = oldAlpha;
			UpdateData(FALSE);
		}
	}
}

void CAniEffectProp::OnBnClickedButtonSelectColor()
{
	CColorDialog dlg(m_color);
	if (dlg.DoModal() == IDOK)
	{
		m_color = dlg.GetColor();
		Nuclear::XPCOLOR color(m_nAlpha, GetRValue(m_color), GetGValue(m_color), GetBValue(m_color));
		if (m_pView->GetDocument()->SetSelectedClipVertexColor(color, m_pView))
		{
			InvalidateRect(&m_colorRect, FALSE);
		}
	}
}
void CAniEffectProp::OnPaint()
{
	CPaintDC dc(this); 

	CBrush brush(m_color);
	dc.FillRect(&m_colorRect, &brush);

	ReleaseDC(&dc);
}

void CAniEffectProp::OnCancel()
{
	ResetData();
}

void CAniEffectProp::OnOK()
{
}

void CAniEffectProp::ResetData()
{
	CEffectEditorDoc* pDoc = m_pView->GetDocument();
	const EffectClipSet& selectedClips = pDoc->GetSelectedClips();
	if (selectedClips.size() == 1)
	{
		const Nuclear::AbstractEffectClip* pClip = *(selectedClips.begin());
		const Nuclear::GEffect* pAni = dynamic_cast<const Nuclear::GEffect*>(pClip->GetAnimation());
		if (pAni)
		{
			GetDlgItem(IDC_FILE_NAME)->SetWindowText(pAni->GetName().c_str());
			CButton *checkBox = reinterpret_cast<CButton*>(GetDlgItem(IDC_IS_SPEC));
			checkBox->SetCheck(pClip->GetEndFrame() == -1);
			CString str;
			m_life = pAni->GetPlayOnceTime();
			str.Format(L"%f", m_life / 1000.0f);
			GetDlgItem(IDC_PLAY_ONE_TIME)->SetWindowText(str);

			switch(pAni->GetPlayMode())
			{
			case Nuclear::XPPM_LOOP:
				m_IDCComboPlayMode.SetCurSel(0);
				m_IDCTime.ShowWindow(SW_HIDE);
				m_nPartType = Nuclear::XPPM_LOOP;
				m_playTimeCount = 1;
				break;
			case Nuclear::XPPM_LOOPNUM:
				m_IDCComboPlayMode.SetCurSel(1);
				m_IDCTime.ShowWindow(SW_SHOW);
				m_nPartType = Nuclear::XPPM_LOOPNUM;
				m_playTimeCount = pAni->GetLoopNum();
				break;
			}

			m_color = RGB(pAni->GetVertexColor().r, pAni->GetVertexColor().g, pAni->GetVertexColor().b);
			m_nAlpha = pAni->GetVertexColor().a;
			m_SilderAlpha.SetPos(m_nAlpha);

			UpdateData(FALSE);
			Invalidate();
		}
	}
}

BOOL CAniEffectProp::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_pView = dynamic_cast<CPropertyView*>(GetParent()->GetParent());
	m_IDCComboPlayMode.AddString(L"循环播放");
	m_IDCComboPlayMode.AddString(L"播放次数");

	m_SilderAlpha.SetRange(0, 255, TRUE);

	m_colorRect.left = 47;
	m_colorRect.right = m_colorRect.left + 50;
	m_colorRect.top = 158;
	m_colorRect.bottom = m_colorRect.top + 15;

	return TRUE;  

}

BEGIN_MESSAGE_MAP(CAniEffectProp, CDialog)
	ON_WM_PAINT()
	ON_BN_CLICKED(IDC_BUTTON_SELECT_COLOR, &CAniEffectProp::OnBnClickedButtonSelectColor)
	ON_EN_KILLFOCUS(IDC_EDIT_ALPHA, &CAniEffectProp::OnEnKillfocusEditAlpha)
	ON_WM_HSCROLL()
	ON_CBN_SELENDOK(IDC_COMBO_PLAYMODE, &CAniEffectProp::OnCbnSelendokComboPlaymode)
	ON_EN_KILLFOCUS(IDC_TIME, &CAniEffectProp::OnEnKillfocusTime)
	ON_BN_CLICKED(IDC_IS_SPEC, &CAniEffectProp::OnBnClickedIsSpec)
	ON_WM_LBUTTONDOWN()
	ON_BN_CLICKED(IDC_POS_BUTTON, &CAniEffectProp::OnBnClickedPosButton)
	ON_BN_CLICKED(IDC_SCAL_BUTTON, &CAniEffectProp::OnBnClickedScalButton)
	ON_BN_CLICKED(IDC_ROTATION_BUTTON, &CAniEffectProp::OnBnClickedRotationButton)
END_MESSAGE_MAP()




void CAniEffectProp::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_ALPHA, m_nAlpha);
	DDX_Text(pDX, IDC_TIME, m_playTimeCount);
	DDX_Control(pDX, IDC_SLIDER_ALPHA, m_SilderAlpha);
	DDX_Control(pDX, IDC_COMBO_PLAYMODE, m_IDCComboPlayMode);
	DDX_Control(pDX, IDC_TIME, m_IDCTime);
}


CAniEffectProp::~CAniEffectProp()
{
}

CAniEffectProp::CAniEffectProp(CWnd* pParent /*=NULL*/)
: CDialog(CAniEffectProp::IDD, pParent)
, m_nAlpha(0)
{

}

