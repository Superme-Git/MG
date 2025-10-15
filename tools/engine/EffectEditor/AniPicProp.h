#pragma once
#include "afxwin.h"
#include "afxcmn.h"

class CPropertyView;


class CAniPicProp : public CDialog
{
	DECLARE_DYNAMIC(CAniPicProp)
public:

	enum { IDD = IDD_ANI_PIC_PROP };

private:
	CPropertyView *m_pView;
	CSliderCtrl m_SilderAlpha;
	CComboBox m_IDCComboPlayMode;
	CEdit m_IDCTime;
	Nuclear::XPPLAY_MODE m_nPartType;
	Nuclear::XPPLAY_MODE m_nEditBoxLastType;
	float m_fFPS;
	COLORREF m_color;
	CRect m_colorRect;
	int m_playTime;
	int m_life;
	int m_nAlpha;
private:
	void SavePlayModeAndTime();
public:
	void ResetData();
	virtual ~CAniPicProp();
	CAniPicProp(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    
	DECLARE_MESSAGE_MAP()

protected:
	virtual void OnCancel();
	virtual void OnOK();

public:
	afx_msg void OnBnClickedColorButton();
	afx_msg void OnBnClickedAlphaButton();
	afx_msg void OnBnClickedRotationButton();
	afx_msg void OnBnClickedScalButton();
	afx_msg void OnBnClickedPosButton();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnBnClickedIsSpec();
	afx_msg void OnEnKillfocusTime();
	afx_msg void OnCbnSelendokComboPlaymode();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);	
	afx_msg void OnEnKillfocusEditAlpha();
	afx_msg void OnBnClickedButtonSelectColor();
	afx_msg void OnPaint();
	afx_msg void OnEnKillfocusEditFps();
	virtual BOOL OnInitDialog();

};
