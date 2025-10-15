#ifndef TIME_EFFECT_EDITOR_DLG_H
#define TIME_EFFECT_EDITOR_DLG_H

#pragma once
#include "afxwin.h"
#include "..\engine\common\xptypes.h"
#include "afxcmn.h"

class CMapEditorView;


class CTimeEffectEditorDlg : public CDialog
{
	DECLARE_DYNAMIC(CTimeEffectEditorDlg)

public:
	enum { IDD = IDD_DLG_TIME_EFFECT_EDITOR };
	virtual ~CTimeEffectEditorDlg();


	CTimeEffectEditorDlg(CWnd* pParent = NULL);   

private:
	struct sContrastAdjustPoint
	{
		CPoint m_Point;
		bool m_bSel;
		sContrastAdjustPoint()
		{
			m_Point = CPoint(0, 0);
			m_bSel = false;
		}
	};
	sContrastAdjustPoint m_AdjustPoints[2]; 
	CRect m_CurveRect; 


	DWORD m_ColorValue;

	CEdit m_editColorValue;
	bool m_bIsOnKeyFrame;

	CButton m_btnStop;
	CButton m_btnAddDelFrame;
	CSliderCtrl m_sliderTimeBar;
	CSliderCtrl m_sliderLightness;
	CSliderCtrl m_sliderCoefB;
	CSliderCtrl m_sliderCoefG;
	CSliderCtrl m_sliderCoefR;
	CListBox m_listKeyFrames;
	CMapEditorView* m_pMapEditorView;

public:
	Nuclear::XPColorBalanceParam m_ColorBalanceParam;

private:
	void CaculateTwoAdjustPoints();
	float CaculateTwoPointDistance(CPoint pt1, CPoint pt2);
	void SetItemState(bool isKeyFrame);

	bool GetCurrentTimeBarParam();
	void ResetKeyFrames();

	void UpdateColorEditor(CEdit *pEdit, Nuclear::XPColorBalanceParam& colorParam);
	void UpdateAllParamSilders();
	void UpdateText(CSliderCtrl *pSilder, float &text);
	void UpdateSilder(CSliderCtrl *pSilder, const float text);
	void ModifyParams();

protected:
	virtual void OnCancel();
	virtual void OnOK();
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	virtual BOOL OnInitDialog();
	void UpdateParam();
	void SetPlay(bool isPlay);
	void Reset();

	DECLARE_MESSAGE_MAP()
	afx_msg void OnSliderReleasedcapture(UINT nID, NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDblClk(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnPaint();
	afx_msg void OnShowWindow(BOOL bShow, UINT nStatus);
	afx_msg void OnBnClickedStop();
	afx_msg void OnBnClickedPlayQuickly();
	afx_msg void OnBnClickedPlay();
	afx_msg void OnLbnSelchangeList1();
	afx_msg void OnBnClickedAddRemoveFrame();
	afx_msg void OnEnKillfocusEditForSlider(UINT nID);
	afx_msg void OnEnKillfocusEdit1();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);

public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
};
#endif