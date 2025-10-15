#pragma once
#include "afxwin.h"
#include "afxcmn.h"
#include "ColorListCtrl.h"
#include <vector>

// CStepPanel dialog
class CPropView;

class CStepPanel : public CDialog
{
	DECLARE_DYNAMIC(CStepPanel)
//自定义类型、常量
public:
	// Dialog Data
	enum { IDD = IDD_STEP_PANEL };

//属性们
private:
	CPropView* m_pView;
	bool m_bIsInited;
	int m_nBrushSize;
	CButton m_butAddGroundType;
	CButton m_butDelGroundType;
	CColorListCtrl m_lstGroundType;
	CStatic m_stcBrushSize;
	CComboBox m_comBrushSize;
	CStatic m_stcGround;
	CStatic m_stcActor;
	CListCtrl m_lstActor;
	CButton m_butAddActorType;
	CButton m_butDelActorType;
	CButton m_butResetSelAct;
	CButton m_butResetAllAct;

	std::vector<std::wstring> m_ActorNames;

public:
	CStepPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~CStepPanel();

private:
	void InsertActorItem(int pos, const std::wstring &name, const std::wstring &des);
	void InsertGroundItem(int pos, int id, const std::wstring &des, COLORREF color, bool visable);
	void ResetSize();
	void ResetActType(const std::wstring &name);

public:
	void ResetCont();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnBnClickedButtonResetSelAct();
	afx_msg void OnBnClickedButtonAddType();
	afx_msg void OnBnClickedButtonDelType();
	afx_msg void OnBnClickedButtonAddActor();
	afx_msg void OnBnClickedButtonDelActor();
	afx_msg void OnBnClickedButtonResetAllActType();
	afx_msg void OnLvnEndlabeleditListActorType(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnEndlabeleditGroundTypeList(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclkGroundTypeList(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnItemchangedGroundTypeList(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnCbnEditchangeComboBrushSize();
	afx_msg void OnCbnSelchangeComboBrushSize();
};
