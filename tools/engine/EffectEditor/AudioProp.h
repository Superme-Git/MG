#pragma once
#include "..\engine\effect\audioeffect.h"
class CPropertyView;


class CAudioProp : public CDialog
{
	DECLARE_DYNAMIC(CAudioProp)
public:

	enum { IDD = IDD_AUDIO_PROP };

private:
	CPropertyView *m_pView;
	CComboBox m_IDCComboPlayMode;
	CEdit m_IDCTime;
	CSliderCtrl m_silderSoundProp[Nuclear::AudioEffect::SOUND_PROP_COUNT];	
	int m_nSoundProp[Nuclear::AudioEffect::SOUND_PROP_COUNT];
	Nuclear::XPPLAY_MODE m_nPartType;
	Nuclear::XPPLAY_MODE m_nEditBoxLastType;
	int m_playTime;
	int m_life;
private:
	void SaveSoundProp(int nID);
	void SavePlayModeAndTime();

public:
	void ResetData();
	virtual ~CAudioProp();
	CAudioProp(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
protected:
	virtual void OnCancel() { ResetData(); }
	virtual void OnOK() {}

public:
	afx_msg void OnNMReleasedcaptureSliderSoundProp(UINT nID, NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnEnKillfocusEditSoundProp(UINT nID);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	virtual BOOL OnInitDialog();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnEnKillfocusTime();
	afx_msg void OnCbnSelendokComboPlaymode();
};
