#pragma once

#include "ColorBarCtrl.h"
#include "EffectEditorDoc.h"
#include "afxwin.h"



class CDlgPropColor : public CDialog
{
	DECLARE_DYNAMIC(CDlgPropColor)
public:

	enum { IDD = IDD_PROP_COLOR };
private:
	colorbarctrl::CColorBarCtrl m_colorCtrl;
	CButton m_buttonOK;
	CButton m_buttonCancel;
	CEffectEditorDoc* m_pDoc;


private:
	void ResetSize();

public:
	virtual ~CDlgPropColor();
	CDlgPropColor(CEffectEditorDoc* pDoc);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedOk();

};
