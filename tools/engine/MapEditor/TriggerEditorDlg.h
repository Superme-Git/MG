#pragma once

#include "..\engine\common\xptypes.h"
#include "..\engine\map\pmap.h"
#include "afxwin.h"
#include "afxcmn.h"

class CMapEditorView;

class CTriggerEditorDlg : public CDialog
{
	DECLARE_DYNAMIC(CTriggerEditorDlg)
private:
	CMapEditorView* m_pView;
	Nuclear::ActionTypeFlag m_Flag;
	Nuclear::XPEFFECT_LAYER m_Layer;
	POINT m_SpriteExcPoint;
	int m_nCurSelectedEffect;
	int m_nMaxDelta;
	int m_nDelta;
	float m_fFrequency;
	int m_nEndTime;
	int m_nBeginTime;
	BOOL m_bNonrandomAndSingle;
	BOOL m_bTimeTrigger;
	BOOL m_bSpriteTrigger;
	CString m_EffectName;
	bool m_bModifyFlag;


	CComboBox m_comboEffects;
	CListCtrl m_listActions;
	CComboBox m_comboLayer;
	CEdit m_editSpriteExcY;
	CEdit m_editSpriteExcX;
	CEdit m_editEndTime;
	CEdit m_editBeginTime;

public:
	enum { IDD = IDD_DLG_TRIGGER_EDIT };
	virtual ~CTriggerEditorDlg();

	CTriggerEditorDlg(CMapEditorView* pView, CWnd* pParent = NULL);   
	Nuclear::SubMap::TriggerInfoVet m_TriggerInfoArr;

	CString m_Name;

private:
	bool CheckData(CString &errStr);
	void FillFromActionTypeCtrl();
	void FillToActionTypeCtrl();
	void FillFromCtrl(int selID);
	void FillToCtrl();
	void ResetEnable();
	void ResetList();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedCtrlSingle();
	afx_msg void OnEnKillfocusEditY();
	afx_msg void OnEnKillfocusEditX();
	afx_msg void OnEnKillfocusEdit1();
	afx_msg void OnEnKillfocusMaxDelta();
	afx_msg void OnEnKillfocusFrequency();
	afx_msg void OnNMKillfocusList2(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnEnKillfocusEndTime();
	afx_msg void OnEnKillfocusDelta();
	afx_msg void OnCbnSelchangeComboLayer();
	afx_msg void OnEnKillfocusBeginTime();
	afx_msg void OnBnClickedButton3();
	afx_msg void OnBnClickedButton2();
	afx_msg void OnBnClickedCtrlSprite();
	afx_msg void OnCbnSelchangeCombo1();
	afx_msg void OnBnClickedButton1();
	afx_msg void OnBnClickedOk();
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedCtrlTime();
	afx_msg void OnBnClickedButtonBrowse();
};
