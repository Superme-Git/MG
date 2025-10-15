


#include "stdafx.h"
#include "EffectEditor.h"
#include "..\engine\effect\particleeffect.h"
#include "PropertyView.h"
#include "ParticleProp.h"
#include "EffectEditorDoc.h"
#include "DlgPropDoubleCurve.h"
#include "DlgPropSingleCurve.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CParticleProp, CDialog)

void CParticleProp::OnBnClickedCheckColor()
{
	m_bUseColor = !m_bUseColor;
	if (m_bUseColor)
	{
		m_buttonSelectColor.ShowWindow(SW_SHOW);
		Nuclear::XPCOLOR color(255, GetRValue(m_color), GetGValue(m_color), GetBValue(m_color));
		if (m_pView->GetDocument()->SetSelectedClipVertexColor(color, m_pView))
		{
			InvalidateRect(&m_colorRect, FALSE);
		}
	} else {
		m_buttonSelectColor.ShowWindow(SW_HIDE);
		Nuclear::XPCOLOR color(255, 0, 0, 1);
		if (m_pView->GetDocument()->SetSelectedClipVertexColor(color, m_pView))
		{
			InvalidateRect(&m_colorRect);
		}
	}

}

void CParticleProp::OnBnClickedRotationButton()
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

void CParticleProp::OnBnClickedPosButton()
{
	CDlgPropDoubleCurve::ConstructorParam param;
	param.pDoc = m_pView->GetDocument();
	param.fYdiff = 200.0f;
	param.kt = Nuclear::AbstractEffectClip::KT_POS;
	param.title = L"位置曲线";
	CDlgPropDoubleCurve dlg(param);
	dlg.DoModal();
}

void CParticleProp::OnLButtonDown(UINT nFlags, CPoint point)
{
	GetDlgItem(IDC_FILE_NAME)->SetFocus();
	CDialog::OnLButtonDown(nFlags, point);
}

void CParticleProp::OnPaint()
{
	CPaintDC dc(this); 

	if (m_bUseColor)
	{
		CBrush brush(m_color);
		dc.FillRect(&m_colorRect, &brush);
	}
	ReleaseDC(&dc);
}

void CParticleProp::OnBnClickedButtonSelectColor()
{
	CColorDialog dlg(m_color);
	if (dlg.DoModal() == IDOK)
	{
		m_color = dlg.GetColor();
		Nuclear::XPCOLOR color(255, GetRValue(m_color), GetGValue(m_color), GetBValue(m_color));
		if (m_pView->GetDocument()->SetSelectedClipVertexColor(color, m_pView))
		{
			InvalidateRect(&m_colorRect, FALSE);
		}
	}
}

void CParticleProp::OnBnClickedIsSpec()
{
	CButton *checkBox = reinterpret_cast<CButton*>(GetDlgItem(IDC_IS_SPEC));
	m_pView->GetDocument()->SetSelectedClipSpec(checkBox->GetCheck() == BST_CHECKED, m_pView);
}

void CParticleProp::SavePlayModeAndTime()
{
	m_pView->GetDocument()->SaveSelectedClipPlayModeAndTime(m_nPartType, m_playTime, m_pView);
}

void CParticleProp::OnEnKillfocusTime()
{
	UpdateData(TRUE);
	SavePlayModeAndTime();
}

void CParticleProp::OnCbnSelendokComboPlayMode()
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
		if (m_nEditBoxLastType == Nuclear::XPPM_TIME)
		{
			m_nEditBoxLastType = Nuclear::XPPM_LOOPNUM;
			m_playTime /= m_life;
		}
		break;
	case 2:
		m_nPartType = Nuclear::XPPM_TIME;
		m_IDCTime.ShowWindow(SW_SHOW);
		if (m_nEditBoxLastType == Nuclear::XPPM_LOOPNUM)
		{
			m_nEditBoxLastType = Nuclear::XPPM_TIME;
			m_playTime *= m_life;
		}
		break;
	}
	SavePlayModeAndTime();
	UpdateData(FALSE);
	Invalidate();
}

BOOL CParticleProp::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_pView = dynamic_cast<CPropertyView*>(GetParent()->GetParent());
	m_IDCComboPlayMode.AddString(L"循环播放");
	m_IDCComboPlayMode.AddString(L"播放次数");
	m_IDCComboPlayMode.AddString(L"时间(秒)");
	m_colorRect.left = 80;
	m_colorRect.right = m_colorRect.left + 35;
	m_colorRect.top = 135;
	m_colorRect.bottom = m_colorRect.top + 15;
	return TRUE;  

}

void CParticleProp::ResetData()
{
	CEffectEditorDoc* pDoc = m_pView->GetDocument();
	const EffectClipSet& selectedClips = pDoc->GetSelectedClips();
	if (selectedClips.size() == 1)
	{
		const Nuclear::AbstractEffectClip* pClip = *(selectedClips.begin());
		const Nuclear::Effect* pAni = pClip->GetAnimation();
		if (pAni && pAni->GetType() == Nuclear::XPET_PARTICLE)
		{
			const Nuclear::ParticleEffect* pPart = dynamic_cast<const Nuclear::ParticleEffect*>(pAni);
			if (pPart)
			{
				CButton *checkBox = reinterpret_cast<CButton*>(GetDlgItem(IDC_IS_SPEC));
				checkBox->SetCheck(pClip->GetEndFrame() == -1);
				GetDlgItem(IDC_FILE_NAME)->SetWindowText(pAni->GetName().c_str());
				CString str;
				m_life = pAni->GetPlayOnceTime();
				str.Format(L"%f", m_life / 1000.0f);
				GetDlgItem(IDC_PLAY_ONE_TIME)->SetWindowText(str);
				m_nPartType = pAni->GetPlayMode();
				switch(m_nPartType)
				{
				case Nuclear::XPPM_LOOP:
					m_IDCComboPlayMode.SetCurSel(0);
					m_IDCTime.ShowWindow(SW_HIDE);
					m_nEditBoxLastType = Nuclear::XPPM_LOOPNUM;
					m_playTime = 1;
					break;
				case Nuclear::XPPM_LOOPNUM:
					m_IDCComboPlayMode.SetCurSel(1);
					m_IDCTime.ShowWindow(SW_SHOW);
					m_nEditBoxLastType = Nuclear::XPPM_LOOPNUM;
					m_playTime = pAni->GetLoopNum();
					break;
				case Nuclear::XPPM_TIME:
					m_IDCComboPlayMode.SetCurSel(2);
					m_IDCTime.ShowWindow(SW_SHOW);
					m_nEditBoxLastType = Nuclear::XPPM_TIME;
					m_playTime = pAni->GetPlayTime();
					break;
				}
				const Nuclear::PSParameter &param = pPart->GetCreateParmeter();			
				m_color = ReverseColor(param.color);
				checkBox = reinterpret_cast<CButton*>(GetDlgItem(IDC_CHECK_COLOR));
				if (param.color == 0xff000001)
				{
					m_bUseColor = false;
					m_buttonSelectColor.ShowWindow(SW_HIDE);
				} else {
					m_bUseColor = true;
					m_buttonSelectColor.ShowWindow(SW_SHOW);
				}
				checkBox->SetCheck(m_bUseColor);
			}

		}
	}
	UpdateData(FALSE);
	Invalidate();
}



BEGIN_MESSAGE_MAP(CParticleProp, CDialog)
	ON_CBN_SELENDOK(IDC_COMBO_PLAYMODE, &CParticleProp::OnCbnSelendokComboPlayMode)
	ON_EN_KILLFOCUS(IDC_TIME, &CParticleProp::OnEnKillfocusTime)
	ON_BN_CLICKED(IDC_IS_SPEC, &CParticleProp::OnBnClickedIsSpec)
	ON_BN_CLICKED(IDC_BUTTON_SELECT_COLOR, &CParticleProp::OnBnClickedButtonSelectColor)
	ON_WM_PAINT()
	ON_WM_LBUTTONDOWN()
	ON_BN_CLICKED(IDC_POS_BUTTON, &CParticleProp::OnBnClickedPosButton)
	ON_BN_CLICKED(IDC_ROTATION_BUTTON, &CParticleProp::OnBnClickedRotationButton)
	ON_BN_CLICKED(IDC_CHECK_COLOR, &CParticleProp::OnBnClickedCheckColor)
END_MESSAGE_MAP()


void CParticleProp::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_TIME, m_playTime);
	DDX_Control(pDX, IDC_COMBO_PLAYMODE, m_IDCComboPlayMode);
	DDX_Control(pDX, IDC_TIME, m_IDCTime);
	DDX_Control(pDX, IDC_BUTTON_SELECT_COLOR, m_buttonSelectColor);
}


CParticleProp::~CParticleProp()
{
}

CParticleProp::CParticleProp(CWnd* pParent /*=NULL*/)
: CDialog(CParticleProp::IDD, pParent)
, m_playTime(0)
, m_life(1)
{

}

