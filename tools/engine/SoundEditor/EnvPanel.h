#pragma once
#include "afxwin.h"
#include "afxcmn.h"
#include "AllSoundListPanel.h"

// CEnvPanel dialog
class CPropView;
class CEnvPanel : public CDialog, public IAreaPanel
{
	DECLARE_DYNAMIC(CEnvPanel)
//自定义类型、常量
public:
	// Dialog Data
	enum { IDD = IDD_ENV_PANEL };

//属性们
private:
	CPropView* m_pView;
	CComboBox m_comboType;
	CComboBox m_comboSoundType;
	CButton m_bottomAddType;
	CButton m_bottomDelType;
	CButton m_buttonAddSound;
	CButton m_buttonDelSound;
	CButton m_checkLooping;
	CListBox m_listSound;
	AllSoundListPanel m_AllSoundListPanel;
	CSliderCtrl m_silderSoundProp[4];	//maxV minV maxF minF
	CEdit m_editBeginTime;		//编辑器进度条时间
	CEdit m_editEndTime;		//编辑器进度条时间
	CEdit m_editFadeInTime;		//实时时间（毫秒）
	CEdit m_editFadeOutTime;	//实时时间（毫秒）
	CEdit m_editorSoundProp[4];
	int m_nSoundProp[4];
	int m_nBeginTime;
	int m_nEndTime;
	int m_nFadeInTime;
	int m_nFadeOutTime;
	CEdit m_editL;
	CEdit m_editPriority;
	CEdit m_editMinInterval;
	CEdit m_editMaxInterval;
	int m_nTransition;
	int m_nMinInterval;
	int m_nMaxInterval;
	int m_nMax;
	BOOL m_bIsLooping;
	short m_nPriority;
	bool m_bIsInited;

//方法们
public:
	CEnvPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~CEnvPanel();

private:
	void ResetSize();
	void EnableSoundCtrls(BOOL b);
	void ResetTimeCtrls();
	void EnableSoundPropCtrls(BOOL b);
	void ResetSoundList();
	void ResetSoundProp();
	void SaveSoundProp(int nID);//从0~3
	void ShowPolygonProp(BOOL b);
	void CheckTimeValid();
public:
	void ResetCont();
	virtual void SelectASound(DWORD_PTR handle);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
	virtual void OnOK() {}
	virtual void OnCancel() {}
	afx_msg void OnBnClickedAddSound();
	afx_msg void OnBnClickedDelSound();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnEnKillfocusEditL();
	afx_msg void OnEnKillfocusEditSoundProp(UINT nID);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnLbnSelchangeSoundList();
	afx_msg void OnBnClickedButtonAddType();
	afx_msg void OnBnClickedButtonDelType();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnCbnSelchangeComboType();
	afx_msg void OnEnKillfocusEditMinInterval();
	afx_msg void OnEnKillfocusEditMaxInterval();
	afx_msg void OnNMReleasedcaptureSliderSoundProp(UINT nID, NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonDeleteCuts();
	afx_msg void OnBnClickedRadioPolygonRadio(UINT nID);
	afx_msg void OnEnKillfocusEditPriority();
	afx_msg void OnCbnSelchangeComboSoundType();
	afx_msg void OnEnKillfocusEditBeginTime();
	afx_msg void OnEnKillfocusEditEndTime();
	afx_msg void OnEnKillfocusEditFadeInTime();
	afx_msg void OnEnKillfocusEditFadeOutTime();
	afx_msg void OnBnClickedCheckLooping();
};
