#pragma once
#include "afxcmn.h"
#include "afxwin.h"

#include "..\engine\map\plinkedobject.h"

// CDlgLinkedObjectMoveParam 对话框

class CDlgLinkedObjectMoveParam : public CDialog
{
	DECLARE_DYNAMIC(CDlgLinkedObjectMoveParam)

public:
	CDlgLinkedObjectMoveParam(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CDlgLinkedObjectMoveParam();

// 对话框数据
	enum { IDD = IDD_DIALOG_PLANTMOVEPARAM };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()

public:
	void GetMoveParam(Nuclear::PLANTMOVEPARAM& param);
	void SetInitMoveParam(const Nuclear::PLANTMOVEPARAM& param);

private:
	float m_fAngle1;
	float m_fAngle2;
	float m_fSpeed;
	int m_nSkewPixelX;
	int m_nSkewSpeedX;

	int m_nDistortionType;
	int m_nSkewType;
	int m_nMoveFollowType;

	float m_fSizeLeft;
	float m_fSizeRight;
	float m_fSizeTop;
	float m_fSizeDown;
	float m_fTransformSpeed;

	CSliderCtrl m_sliderAngle1;
	CSliderCtrl m_sliderAngle2;
	CSliderCtrl m_sliderSpeed;
	CComboBox m_comboDistortionType;

	CSliderCtrl m_sliderSkew;
	CSliderCtrl m_sliderSkewXspeed;

	CComboBox m_comboSkewType;
	CComboBox m_comboMoveFollowType;

	CSliderCtrl m_sliderSizeLeft;
	CSliderCtrl m_sliderSizeRight;
	CSliderCtrl m_sliderSizeTop;
	CSliderCtrl m_sliderSizeDown;

	CSliderCtrl m_sliderTransformSpeedx;

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	virtual BOOL OnInitDialog();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);

	afx_msg void OnPaint();
	afx_msg void OnCbnSelchangeComboDistortiontype();
	afx_msg void OnCbnSelchangeComboSkewtype();

	afx_msg void OnEnKillFocusEdit(UINT nID);
	afx_msg void OnCbnSelchangeCombo1();

private:
	void InitDisableCtrl(int nDistortionType);

public:
};
