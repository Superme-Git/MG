#pragma once
#include "EffectEditorDoc.h"
#include "afxwin.h"
#include "..\engine\effect\xapeffect.h"
#include "stdafx.h"



class CAddEditClipDlg : public CDialog
{
public:
	DECLARE_DYNAMIC(CAddEditClipDlg)
private:
	CComboBox m_LayerCtrl;
	CComboBox m_EffectType;
	CComboBox m_IDCComboPlayMode;
	CEdit m_PlayTime;
	CButton m_ColorButton;
	CButton m_checkColor;

	DWORD m_color;
	RECT m_rectColor;


public:
	CString m_strFilepath;
	int m_layer;
	int m_nStartFrame;
	int m_nPicEndFrame;
	int m_nPlayTime;
	bool m_bUseDefColor;
	Nuclear::XPPLAY_MODE m_nPartType;
	Nuclear::XPEffectType m_nType;
public:
	DWORD GetColor() { return ReverseColor(m_color) | 0xFF000000; }
	virtual ~CAddEditClipDlg();
	CAddEditClipDlg(CWnd* pParent = NULL);

	enum { IDD = IDD_ADD_EDIT_CLIP };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnPaint();
	afx_msg void OnBnClickedCheckColor();
	afx_msg void OnCbnSelendokComboPlaymode();
	afx_msg void OnBnClickedColorButton();
	afx_msg void OnCbnSelendokComboEffecttype();
	afx_msg void OnBnClickedOk();
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedBrouse();
};
