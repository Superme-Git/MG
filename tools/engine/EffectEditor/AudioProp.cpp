

#include "stdafx.h"
#include "EffectEditor.h"
#include "PropertyView.h"
#include "AudioProp.h"
#include "EffectEditorDoc.h"

#include "../engine/effect/audioeffect.h"



IMPLEMENT_DYNAMIC(CAudioProp, CDialog)

void CAudioProp::OnNMReleasedcaptureSliderSoundProp(UINT nID, NMHDR *pNMHDR, LRESULT *pResult)
{
	SaveSoundProp(nID - IDC_SLIDER_MAX_V);
	*pResult = 0;
}

void CAudioProp::OnEnKillfocusEditSoundProp(UINT nID)
{
	nID -= IDC_EDIT_MAX_V;
	int oldProp = m_nSoundProp[nID];
	int minProp = nID<2?0:-128;
	int maxProp = nID<2?100:127;
	UpdateData(TRUE);
	if (m_nSoundProp[nID] >= minProp && m_nSoundProp[nID] <= maxProp)
	{
		m_silderSoundProp[nID].SetPos(m_nSoundProp[nID]);
		SaveSoundProp(nID);
	} else {
		m_nSoundProp[nID] = oldProp;
		UpdateData(FALSE);
	}
}

void CAudioProp::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	int nID = pScrollBar->GetDlgCtrlID() - IDC_SLIDER_MAX_V;
	if (nID >= 0 && nID < 4)
	{
		int newPos = m_silderSoundProp[nID].GetPos();
		m_nSoundProp[nID] = newPos;
		UpdateData(FALSE);
	}
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

BOOL CAudioProp::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_pView = dynamic_cast<CPropertyView*>(GetParent()->GetParent());
	m_IDCComboPlayMode.AddString(L"循环播放");
	m_IDCComboPlayMode.AddString(L"播放次数");
	m_IDCComboPlayMode.AddString(L"时间(秒)");
	m_silderSoundProp[0].SetRange(0, 100, FALSE);
	m_silderSoundProp[0].SetPos(m_nSoundProp[0]);
	m_silderSoundProp[1].SetRange(0, 100, FALSE);
	m_silderSoundProp[1].SetPos(m_nSoundProp[1]);
	m_silderSoundProp[2].SetRange(-128, 127, FALSE);
	m_silderSoundProp[2].SetPos(127);
	m_silderSoundProp[2].SetPos(m_nSoundProp[2]);
	m_silderSoundProp[3].SetRange(-128, 127, FALSE);
	m_silderSoundProp[3].SetPos(127);
	m_silderSoundProp[3].SetPos(m_nSoundProp[3]);

	return FALSE;
}

void CAudioProp::OnLButtonDown(UINT nFlags, CPoint point)
{
	GetDlgItem(IDC_FILE_NAME)->SetFocus();
	CDialog::OnLButtonDown(nFlags, point);
}

void CAudioProp::OnEnKillfocusTime()
{
	UpdateData(TRUE);
	SavePlayModeAndTime();
}

void CAudioProp::OnCbnSelendokComboPlaymode()
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

BEGIN_MESSAGE_MAP(CAudioProp, CDialog)
	ON_CBN_SELENDOK(IDC_COMBO_PLAYMODE, &CAudioProp::OnCbnSelendokComboPlaymode)
	ON_EN_KILLFOCUS(IDC_TIME, &CAudioProp::OnEnKillfocusTime)
	ON_WM_LBUTTONDOWN()
	ON_WM_HSCROLL()
	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_MAX_V, IDC_EDIT_MIN_F, &CAudioProp::OnEnKillfocusEditSoundProp)
	ON_NOTIFY_RANGE(NM_RELEASEDCAPTURE, IDC_SLIDER_MAX_V, IDC_SLIDER_MIN_F, &CAudioProp::OnNMReleasedcaptureSliderSoundProp)
END_MESSAGE_MAP()




void CAudioProp::SaveSoundProp(int nID)
{
	m_pView->GetDocument()->SetSelectedAudioSoundProperty(nID, nID<2?Vol100to255(m_nSoundProp[nID]):FreqToSave(m_nSoundProp[nID]));
}

void CAudioProp::SavePlayModeAndTime()
{
	m_pView->GetDocument()->SaveSelectedClipPlayModeAndTime(m_nPartType, m_playTime, m_pView);	
}

void CAudioProp::ResetData()
{
	CEffectEditorDoc* pDoc = m_pView->GetDocument();
	const EffectClipSet& selectedClips = pDoc->GetSelectedClips();
	if (selectedClips.size() == 1)
	{
		const Nuclear::AbstractEffectClip* pClip = *(selectedClips.begin());
		const Nuclear::AudioEffect* pAni = dynamic_cast<const Nuclear::AudioEffect*>(pClip->GetAnimation());
		if (pAni)
		{
			m_nSoundProp[0] = Vol255to100(pAni->GetSoundProp( Nuclear::AudioEffect::SP_MAX_VOL));
			m_nSoundProp[1] = Vol255to100(pAni->GetSoundProp( Nuclear::AudioEffect::SP_MIN_VOL));
			m_nSoundProp[2] = FreqToShow(pAni->GetSoundProp( Nuclear::AudioEffect::SP_MAX_FREQ));
			m_nSoundProp[3] = FreqToShow(pAni->GetSoundProp( Nuclear::AudioEffect::SP_MIN_FREQ));
			for (int i = Nuclear::AudioEffect::SP_MAX_VOL;i<Nuclear::AudioEffect::SOUND_PROP_COUNT;++i)
			{
				m_silderSoundProp[i].SetPos(m_nSoundProp[i]);
			}			
			GetDlgItem(IDC_FILE_NAME)->SetWindowText(pAni->GetName().c_str());
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
			UpdateData(FALSE);
			Invalidate();
		}
	}
}

void CAudioProp::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_TIME, m_playTime);
	DDX_Text(pDX, IDC_EDIT_MAX_V, m_nSoundProp[0]);
	DDX_Text(pDX, IDC_EDIT_MIN_V, m_nSoundProp[1]);
	DDX_Text(pDX, IDC_EDIT_MAX_F, m_nSoundProp[2]);
	DDX_Text(pDX, IDC_EDIT_MIN_F, m_nSoundProp[3]);
	DDX_Control(pDX, IDC_COMBO_PLAYMODE, m_IDCComboPlayMode);
	DDX_Control(pDX, IDC_TIME, m_IDCTime);
	DDX_Control(pDX, IDC_SLIDER_MAX_V, m_silderSoundProp[0]);
	DDX_Control(pDX, IDC_SLIDER_MIN_V, m_silderSoundProp[1]);
	DDX_Control(pDX, IDC_SLIDER_MAX_F, m_silderSoundProp[2]);
	DDX_Control(pDX, IDC_SLIDER_MIN_F, m_silderSoundProp[3]);
}

CAudioProp::~CAudioProp()
{
}

CAudioProp::CAudioProp(CWnd* pParent /*=NULL*/)
: CDialog(CAudioProp::IDD, pParent)
{
	m_nSoundProp[0] = 100;
	m_nSoundProp[1] = 100;
	m_nSoundProp[2] = 0;
	m_nSoundProp[3] = 0;
}

