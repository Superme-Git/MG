#pragma once
#include "afxwin.h"


class CPropertyView;

class CRoleProp : public CDialog, public INotify
{
	DECLARE_DYNAMIC(CRoleProp)
public:

	enum { IDD = IDD_ROLE_PROP };
private:
	CComboBox m_model;
	CComboBox m_layer;
	CComboBox m_component;
	CComboBox m_action;
	CComboBox m_comboDivLayer;
	CStatic m_frameCount;
	CStatic m_totalTime;
	CButton m_RadioSD[8];
	CButton m_buttonShowRole;
	CButton m_buttonLayer;
	CButton m_buttonPos;
	CButton m_buttonRotation;
	CPropertyView *m_pView;
	bool m_bIsInited;
	std::map<std::wstring, int> m_modelMap;
public:

private:
	void SetAction(const std::wstring &actionName);
	void ResetComponent();
	void ResetEquip();
	void ResetActionCombo();
	void ResetSize();

public:
	virtual void OnRender();
	void ResetData();
	virtual ~CRoleProp();

	CRoleProp(CWnd* pParent = NULL);   


protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
	virtual void OnCancel() {}
	virtual void OnOK() {}

public:
	afx_msg void OnBnClickedButtonColor();
	afx_msg void OnCbnSelendokComboModel();
	afx_msg void OnCbnSelendokComboLayer();
	afx_msg void OnCbnSelendokComboComponent();
	afx_msg void OnCbnSelendokComboAction();
	afx_msg void OnCbnSelendokComboDivLayer();
	afx_msg void OnBnClickedRotationButton();
	afx_msg void OnBnClickedPosButton();
	afx_msg void OnBnClickedButtonLayer();
	afx_msg void OnBnClickedCheckShowRole();	
	afx_msg void OnBnClickedRadioSd(UINT nID);
	virtual BOOL OnInitDialog();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);

};
