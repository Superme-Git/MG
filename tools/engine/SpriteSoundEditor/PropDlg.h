#pragma once
#include "afxwin.h"
#include "afxcmn.h"


// CPropDlg dialog
class CPropView;

namespace Nuclear
{
	struct SpriteSoundParam;
	typedef std::vector<SpriteSoundParam> SpriteSoundParamVet;
	typedef std::map<std::wstring, SpriteSoundParamVet> ActionSoundParamMap;//key是动作名
	typedef ActionSoundParamMap* PActionSoundParamMap;
	struct ModelSoundParam;
}

class CPropDlg : public CDialog
{
	DECLARE_DYNAMIC(CPropDlg)
private:
	CComboBox m_comboModel;
	CComboBox m_comboKeyComp;
	CComboBox m_comboAction;
	CComboBox m_comboSound;
	CSliderCtrl m_sliderVolume;
	CSliderCtrl m_sliderFreq;
	CComboBox m_comboLayers[8];
	CString m_strLayerDeses[8];
	Nuclear::SpriteSoundParamVet m_copyedSounds;
	int m_nVolume;
	int m_nFreq;
	int m_nComboKeySel;
	int m_nComboModel;
	CPropView* m_pView;
	bool m_bIsInited;
	

public:
	// Dialog Data
	enum { IDD = IDD_INFO_PANEL };

private:
	void OnChangeModel();
	void OnChangeAction();
	void OnChangeComponent(int i);
	void OnChangeSound();
	void ResetSoundList();
	void EnableCtrls(BOOL enable);

public:
	CPropDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CPropDlg();

	void ResetCont();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	BOOL Create(UINT nIDTemplate, CWnd* pParentWnd, CPropView* pView);
	afx_msg void OnBnClickedRadioDir(UINT nID);
	afx_msg void OnCbnSelchangeComboModel();
	afx_msg void OnCbnSelchangeComboKeyComp();
	afx_msg void OnCbnSelchangeComboAction();
	afx_msg void OnCbnSelchangeComboLayer(UINT nID);
	afx_msg void OnBnClickedButtonAdd();
	afx_msg void OnBnClickedButtonDel();
	virtual BOOL OnInitDialog();
	afx_msg void OnCbnSelchangeCombo1();
	afx_msg void OnBnClickedSave();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnEnKillfocusEditF();
	afx_msg void OnEnKillfocusEditV();
	afx_msg void OnBnClickedButtonCopy();
	afx_msg void OnBnClickedButtonPaste();
};
