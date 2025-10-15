#pragma once
#include "afxcmn.h"
#include "afxwin.h"
#include <map>

// CTestPanel dialog

class CPropView;
class CTestPanel : public CDialog
{
	DECLARE_DYNAMIC(CTestPanel)

//自定义枚举、类型
public:
	// Dialog Data
	enum { IDD = IDD_TEST_PANEL };

//属性们
private:
	CPropView* m_pView;
	CSliderCtrl m_silderCycle;
	CSliderCtrl m_silderTime;
	CSliderCtrl m_silderEnv;
	CSliderCtrl m_silderBkg;
	CStatic m_group;
	CComboBox m_comboModel;
	CComboBox m_comboActorType;
	CButton m_buttonSave;
	CComboBox m_comboLayer;
	CComboBox m_comboComponent;
	CComboBox m_comboActionType;
	CComboBox m_comboActionName;
	CButton m_buttonSetAsDefault;
	CComboBox m_comboRide;
	CListCtrl m_listNotify;
	CComboBox m_comboNActName;
	CComboBox m_comboNActFrame;
	BOOL m_drawEnvArea;
	BOOL m_drawBkgArea;
	BOOL m_drawBkgVolArea;
	BOOL m_drawStep;
	BOOL m_testingEnableDark;
	int m_nTime;
	int m_nRideLayer;
	int m_nOldModelSel;
	int m_nNotifyCurSel;
	bool m_bIsInited;
	BOOL m_bEnableEnv;
	BOOL m_bEnableBkg;
	bool m_bActorMapDirty;
	std::map<std::wstring, int> m_modelMap;
	std::map<std::wstring, int> m_actionMap;
	std::map<std::wstring, int> m_actorMap;
	std::map<int, std::wstring> m_actorTypeMap;
	

//方法们
private:
	void ResetActionCombo();
	void ResetRide();
	void ResetEquip();
	void ResetComponent();
	void SetActorType();
	void ResetActorType();
	void SetAction();
	void ResetNotify();
	void ResetNotifyCombo();
	void ResetNotifyFrameCombo();
	void ResetNotifyComboPosition();

public:
	CTestPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~CTestPanel();

	void ResetCont();
	void ResetSize();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	virtual void OnOK() {}
	virtual void OnCancel() {}
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedCheckDrawEnvArea();
	afx_msg void OnBnClickedCheckDrawBkgArea();	
	afx_msg void OnBnClickedCheckDark();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnBnClickedCheckBkg();
	afx_msg void OnBnClickedCheckEnv();
	afx_msg void OnBnClickedButtonSaveAsDefault();
	afx_msg void OnCbnSelchangeComboModel();
	afx_msg void OnBnClickedButtonSave();
	afx_msg void OnCbnSelchangeComboLayer();
	afx_msg void OnCbnSelchangeComboComponent();
	afx_msg void OnCbnSelchangeComboActionType();
	afx_msg void OnCbnSelchangeComboActionName();
	afx_msg void OnCbnSelchangeComboActType();
	afx_msg void OnCbnSelchangeComboRideType();
	afx_msg void OnLvnItemchangedListNotify(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnEndScrollListNotify(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnCbnSelchangeComboNotifyActionName();
	afx_msg void OnCbnSelchangeComboNotifyActionFrame();
	afx_msg void OnBnClickedCheckDrawStep();
	afx_msg void OnBnClickedCheckDrawBkgVol();
};
