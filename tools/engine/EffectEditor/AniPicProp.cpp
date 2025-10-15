

#include "stdafx.h"
#include "EffectEditor.h"
#include "PropertyView.h"
#include "AniPicProp.h"
#include "EffectEditorDoc.h"
#include "DlgPropDoubleCurve.h"
#include "DlgPropSingleCurve.h"
#include "DlgPropColor.h"

#include "../engine/effect/xapeffect.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CAniPicProp, CDialog)

void CAniPicProp::OnBnClickedColorButton()
{
	CDlgPropColor dlg(m_pView->GetDocument());
	dlg.DoModal();
}

void CAniPicProp::OnBnClickedAlphaButton()
{
	CDlgPropSingleCurve::ConstructorParam param;
	param.fYdiff = 2.0f;
	param.kt = Nuclear::AbstractEffectClip::KT_ALPHA;
	param.pDoc = m_pView->GetDocument();
	param.title = L"Alpha曲线";
	param.fMaxMaxY = 2.0f;
	param.fMinMinY = 0.0f;
	param.bUseDefaultY = true;
	param.fDefaultY = 1.0f;
	CDlgPropSingleCurve dlg(param);
	dlg.DoModal();
}

void CAniPicProp::OnBnClickedRotationButton()
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

void CAniPicProp::OnBnClickedScalButton()
{
	CDlgPropDoubleCurve::ConstructorParam param;
	param.pDoc = m_pView->GetDocument();
	param.fYdiff = 5.0f;
	param.kt = Nuclear::AbstractEffectClip::KT_SCALE;
	param.title = L"缩放曲线";
	CDlgPropDoubleCurve dlg(param);
	dlg.DoModal();
}

void CAniPicProp::OnBnClickedPosButton()
{
	CDlgPropDoubleCurve::ConstructorParam param;
	param.pDoc = m_pView->GetDocument();
	param.fYdiff = 200.0f;
	param.kt = Nuclear::AbstractEffectClip::KT_POS;
	param.title = L"位置曲线";
	CDlgPropDoubleCurve dlg(param);
	dlg.DoModal();
}

void CAniPicProp::OnLButtonDown(UINT nFlags, CPoint point)
{
	GetDlgItem(IDC_FILE_NAME)->SetFocus();
	CDialog::OnLButtonDown(nFlags, point);
}

void CAniPicProp::OnBnClickedIsSpec()
{
	CButton *checkBox = reinterpret_cast<CButton*>(GetDlgItem(IDC_IS_SPEC));
	m_pView->GetDocument()->SetSelectedClipSpec(checkBox->GetCheck() == BST_CHECKED, m_pView);
}

void CAniPicProp::SavePlayModeAndTime()
{
	m_pView->GetDocument()->SaveSelectedClipPlayModeAndTime(m_nPartType, m_playTime, m_pView);	
}

void CAniPicProp::OnEnKillfocusTime()
{
	UpdateData(TRUE);
	SavePlayModeAndTime();
}


void CAniPicProp::OnCbnSelendokComboPlaymode()
{
	bool mustSave = false;
	switch (m_IDCComboPlayMode.GetCurSel())
	{
	case 0:
		m_nPartType = Nuclear::XPPM_LOOP;
		m_IDCTime.ShowWindow(SW_HIDE);
		mustSave = true;
		break;
	case 1:
		if (m_nPartType == Nuclear::XPPM_LOOP)
			mustSave = true;
		m_nPartType = Nuclear::XPPM_LOOPNUM;
		m_IDCTime.ShowWindow(SW_SHOW);
		if (m_nEditBoxLastType == Nuclear::XPPM_TIME)
		{
			m_nEditBoxLastType = Nuclear::XPPM_LOOPNUM;
			m_playTime /= m_life;
		}
		break;
	case 2:
		if (m_nPartType == Nuclear::XPPM_LOOP)
			mustSave = true;
		m_nPartType = Nuclear::XPPM_TIME;
		m_IDCTime.ShowWindow(SW_SHOW);
		if (m_nEditBoxLastType == Nuclear::XPPM_LOOPNUM)
		{
			m_nEditBoxLastType = Nuclear::XPPM_TIME;
			m_playTime *= m_life;
		}
		break;
	}
	if (mustSave)
	{
		SavePlayModeAndTime();
	}
	UpdateData(FALSE);
	Invalidate();
}

void CAniPicProp::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
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

void CAniPicProp::OnEnKillfocusEditAlpha()
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

void CAniPicProp::OnBnClickedButtonSelectColor()
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

void CAniPicProp::OnPaint()
{
	CPaintDC dc(this); 

	CBrush brush(m_color);
	dc.FillRect(&m_colorRect, &brush);

	ReleaseDC(&dc);
}

void CAniPicProp::OnEnKillfocusEditFps()
{
	float oldFps = m_fFPS;
	UpdateData(TRUE);
	if (!m_pView->GetDocument()->SetSelectedClipFPS(m_fFPS, m_pView))
	{
		m_fFPS = oldFps;
		UpdateData(FALSE);
	}
}

void CAniPicProp::OnCancel()
{
	ResetData();
}

void CAniPicProp::OnOK()
{
}

void CAniPicProp::ResetData()
{
	CEffectEditorDoc* pDoc = m_pView->GetDocument();
	const EffectClipSet& selectedClips = pDoc->GetSelectedClips();
	if (selectedClips.size() == 1)
	{
		const Nuclear::AbstractEffectClip* pClip = *(selectedClips.begin());
		const Nuclear::XapEffect* pAni = dynamic_cast<const Nuclear::XapEffect*>(pClip->GetAnimation());
		if (pAni)
		{
			GetDlgItem(IDC_FILE_NAME)->SetWindowText(pAni->GetName().c_str());
			CButton *checkBox = reinterpret_cast<CButton*>(GetDlgItem(IDC_IS_SPEC));
			checkBox->SetCheck(pClip->GetEndFrame() == -1);
			m_fFPS = pAni->GetFps();
			CString str;
			m_life = pAni->GetPlayOnceTime();
			str.Format(L"%f", m_life / 1000.0f);
			GetDlgItem(IDC_PLAY_ONE_TIME)->SetWindowText(str);

			switch(pAni->GetPlayMode())
			{
			case Nuclear::XPPM_LOOP:
				m_IDCComboPlayMode.SetCurSel(0);
				m_IDCTime.ShowWindow(SW_HIDE);
				m_nEditBoxLastType = Nuclear::XPPM_LOOPNUM;
				m_nPartType = Nuclear::XPPM_LOOP;
				m_playTime = 1;
				break;
			case Nuclear::XPPM_LOOPNUM:
				m_IDCComboPlayMode.SetCurSel(1);
				m_IDCTime.ShowWindow(SW_SHOW);
				m_nEditBoxLastType = Nuclear::XPPM_LOOPNUM;
				m_nPartType = Nuclear::XPPM_LOOPNUM;
				m_playTime = pAni->GetLoopNum();
				break;
			case Nuclear::XPPM_TIME:
				m_IDCComboPlayMode.SetCurSel(2);
				m_IDCTime.ShowWindow(SW_SHOW);
				m_nEditBoxLastType = Nuclear::XPPM_TIME;
				m_nPartType = Nuclear::XPPM_TIME;
				m_playTime = pAni->GetPlayTime();
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
BOOL CAniPicProp::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_pView = dynamic_cast<CPropertyView*>(GetParent()->GetParent());
	m_IDCComboPlayMode.AddString(L"循环播放");
	m_IDCComboPlayMode.AddString(L"播放次数");
	m_IDCComboPlayMode.AddString(L"时间(秒)");



	m_SilderAlpha.SetRange(0, 255, TRUE);

	m_colorRect.left = 47;
	m_colorRect.right = m_colorRect.left + 50;
	m_colorRect.top = 158;
	m_colorRect.bottom = m_colorRect.top + 15;

	return TRUE;  

}


BEGIN_MESSAGE_MAP(CAniPicProp, CDialog)
	ON_EN_KILLFOCUS(IDC_EDIT_FPS, &CAniPicProp::OnEnKillfocusEditFps)
	ON_WM_PAINT()
	ON_BN_CLICKED(IDC_BUTTON_SELECT_COLOR, &CAniPicProp::OnBnClickedButtonSelectColor)
	ON_EN_KILLFOCUS(IDC_EDIT_ALPHA, &CAniPicProp::OnEnKillfocusEditAlpha)
	ON_WM_HSCROLL()
	ON_CBN_SELENDOK(IDC_COMBO_PLAYMODE, &CAniPicProp::OnCbnSelendokComboPlaymode)
	ON_EN_KILLFOCUS(IDC_TIME, &CAniPicProp::OnEnKillfocusTime)
	ON_BN_CLICKED(IDC_IS_SPEC, &CAniPicProp::OnBnClickedIsSpec)
	ON_WM_LBUTTONDOWN()
	ON_BN_CLICKED(IDC_POS_BUTTON, &CAniPicProp::OnBnClickedPosButton)
	ON_BN_CLICKED(IDC_SCAL_BUTTON, &CAniPicProp::OnBnClickedScalButton)
	ON_BN_CLICKED(IDC_ROTATION_BUTTON, &CAniPicProp::OnBnClickedRotationButton)
	ON_BN_CLICKED(IDC_ALPHA_BUTTON, &CAniPicProp::OnBnClickedAlphaButton)
	ON_BN_CLICKED(IDC_COLOR_BUTTON, &CAniPicProp::OnBnClickedColorButton)
END_MESSAGE_MAP()




void CAniPicProp::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_FPS, m_fFPS);
	DDX_Text(pDX, IDC_EDIT_ALPHA, m_nAlpha);
	DDX_Text(pDX, IDC_TIME, m_playTime);
	DDX_Control(pDX, IDC_SLIDER_ALPHA, m_SilderAlpha);
	DDX_Control(pDX, IDC_COMBO_PLAYMODE, m_IDCComboPlayMode);
	DDX_Control(pDX, IDC_TIME, m_IDCTime);
}


CAniPicProp::~CAniPicProp()
{
}

CAniPicProp::CAniPicProp(CWnd* pParent /*=NULL*/)
: CDialog(CAniPicProp::IDD, pParent)
, m_fFPS(0)
, m_nAlpha(0)
{

}

