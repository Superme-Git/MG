#pragma once




#include "..\engine\sprite\spritemanager.h"

class CHSLAdjustDlg : public CDialog
{
	DECLARE_DYNAMIC(CHSLAdjustDlg)

public:
	int m_CurPartProj[8];
	int m_Type;

	int m_CurSelProj;
	int m_CurSelPart;
	int m_nBright;

	int m_nL;
	int m_nS;
	int m_nH;

public:
	enum { IDD = IDD_HSL_DLG };
	virtual ~CHSLAdjustDlg();

	CHSLAdjustDlg( Nuclear::SpriteManager::XModel* pModel, Nuclear::XPDyeShaderParam* pParam, CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();

private:
	Nuclear::XPDyeShaderParam* m_pParam;
	Nuclear::SpriteManager::XModel* m_pModel;
	CSliderCtrl m_sliderBright;

	CSliderCtrl m_sliderL;
	CSliderCtrl m_sliderS;
	CSliderCtrl m_sliderH;
	CComboBox	m_comboBoxB;

	CComboBox	m_comboBoxG;
	CComboBox	m_comboBoxR;
	CComboBox	m_comboBoxDyeProj;

	CComboBox	m_comboBoxDyePart;
	CButton		m_radioProjs[2];
	void UpdateDyeView();

	void UpdateProjectView();
	void UpdatePartView();
	void ModifyParams();

public:
	afx_msg void OnBnClickedSave();
	afx_msg void OnUnDo();
	afx_msg void OnDelDyeProj();
	afx_msg void OnAddDyeProj();
	afx_msg void OnDelDyePart();
	afx_msg void OnAddDyePart();
	afx_msg void OnCbnSelchangeColor();
	afx_msg void OnCbnSelchangeDyePart();
	afx_msg void OnCbnSelchangeDyeProj();
	afx_msg void OnSliderReleasedcapture(UINT nID, NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancle();
	afx_msg void OnBnClickedRadioProj();
};
