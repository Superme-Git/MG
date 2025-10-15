#pragma once
#include "afxwin.h"
#include "afxcmn.h"

class CPropertyView;


class CParticleProp : public CDialog
{
	DECLARE_DYNAMIC(CParticleProp)
public:

	enum { IDD = IDD_PARTICLE_PROP };
protected:
	CPropertyView *m_pView;
	CComboBox m_IDCComboPlayMode;
	CEdit m_IDCTime;
	CButton m_buttonSelectColor;
	Nuclear::XPPLAY_MODE m_nPartType;
	Nuclear::XPPLAY_MODE m_nEditBoxLastType;
	int m_playTime;
	int m_life;
	COLORREF m_color;
	CRect m_colorRect;
	bool m_bUseColor;

protected:
	void SavePlayModeAndTime();
public:
	void ResetData();
	virtual ~CParticleProp();
	CParticleProp(UINT nIDTemplate, CWnd* pParentWnd = NULL) : CDialog(nIDTemplate, pParentWnd), m_playTime(0), m_life(1) {}
	CParticleProp(CWnd* pParent = NULL);   

protected:
	virtual void OnCancel() { ResetData(); }
	virtual void OnOK() {}
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedCheckColor();
	afx_msg void OnBnClickedRotationButton();
	afx_msg void OnBnClickedPosButton();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnPaint();
	afx_msg void OnBnClickedButtonSelectColor();
	afx_msg void OnBnClickedIsSpec();
	afx_msg void OnEnKillfocusTime();
	afx_msg void OnCbnSelendokComboPlayMode();
	virtual BOOL OnInitDialog();

};
