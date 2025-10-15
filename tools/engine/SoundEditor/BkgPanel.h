#pragma once
#include "afxwin.h"
#include "SoundListBox.h"
#include "afxcmn.h"
#include "AllSoundListPanel.h"


// CBkgPanel dialog
class CPropView;
class CBkgPanel : public CDialog, public IAreaPanel
{
	DECLARE_DYNAMIC(CBkgPanel)

//自定义类型、枚举、常量们
public:
	// Dialog Data
	enum { IDD = IDD_BKG_PANEL };

//属性们
private:
	CPropView* m_pView;
	CSoundListBox m_listSound;
	AllSoundListPanel m_AllSoundListPanel;
	CButton m_ctrlUp;
	CButton m_ctrlDown;
	CButton m_buttonAddSound;
	CButton m_buttonDelSound;
	CStatic m_staticVol;
	CSliderCtrl m_silderVol;
	CEdit m_editVol;
	CEdit m_editMinInterval;
	CEdit m_editMaxInterval;
	CButton m_checkWaitPrevArea;
	CStatic m_staticPlayMode;
	CComboBox m_comboPlayMode;
	CStatic m_staticFadeIn;
	CEdit m_editFadeIn;
	CStatic m_staticFadeOut;
	CEdit m_editFadeOut;
	int m_nFadeIn;
	int m_nFadeOut;
	int m_nVol;
	int m_nMinInterval;
	int m_nMaxInterval;
	bool m_bIsInited;

public:
	CBkgPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~CBkgPanel();

private:
	void ResetSize();
	void EnableSoundPropCtrls(BOOL b);
	void ResetSoundList();
	void ResetSoundProp();
	void ShowPolygonProp(BOOL b);
public:
	void ResetCont();
	virtual void SelectASound(DWORD_PTR handle);


protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnLbnCheckChangeSoundList();
private:

public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnBnClickedAddSound();
	afx_msg void OnBnClickedRadioPolygonRadio(UINT nID);
	afx_msg void OnBnClickedDelSound();
	afx_msg void OnBnClickedButtonUp();
	afx_msg void OnBnClickedButtonDown();
	afx_msg void OnLbnSelchangeSoundList();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnEnKillfocusEditVol();
	afx_msg void OnNMReleasedcaptureSliderVol(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonDeleteCuts();
	afx_msg void OnEnKillfocusEditMaxInterval();
	afx_msg void OnEnKillfocusEditMinInterval();
	afx_msg void OnBnClickedCheck1();
	afx_msg void OnCbnSelchangeComboPlayMode();
	afx_msg void OnEnKillfocusEdit1();
	afx_msg void OnEnKillfocusEdit2();
};
