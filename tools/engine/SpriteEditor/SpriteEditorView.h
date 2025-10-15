#pragma once
#include "afxwin.h"

#include "../engine/ISprite.h"
#include "afxcmn.h"


namespace Nuclear
{
	class PSpriteMaze;
}

class CSpriteEditorView : public CView
{
	DECLARE_DYNCREATE(CSpriteEditorView)

protected:
	unsigned int m_nStartTime;
	float m_fEffectAlpha;
	virtual ~CSpriteEditorView();
	CSpriteEditorView();           

public:
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif
	virtual void OnDraw(CDC* pDC);      
	void Render();
	int m_nAdjPosition;

	CPoint  m_MouseNowPos;
	CPoint  m_MouseDownPos;
	int		m_adBaseMode; 
	CPoint m_adBase2;
	CPoint m_adBase0;
	int		m_adBorderMode; 

	CPoint  m_adBorderRB;
	CPoint	m_adBorderLT;
	HCURSOR m_hMouseCursor;
	int m_d3dHeight;

	int m_d3dWidth;

private:
	int m_nCurRegion; 
	bool m_bSelObj;		 

	bool m_bTestObjSel;  
	int m_nCurSelMaskpt; 
	BOOL m_bModifyMask;
	int GetAniDir(int dirMode, int ndir);

	float CalPointsDistance(CPoint pt1, CPoint pt2);
	bool GetAdjRect(const Nuclear::PSpriteMaze *pSpriteMaze, RECT& rect);

	bool ResetMouseCurData();
	void RenewSize();

protected:
	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnUpdateTestSelectObject(CCmdUI *pCmdUI);
	afx_msg void OnTestSelectObject();
	afx_msg BOOL OnMouseWheel(UINT nFlags, short zDelta, CPoint pt);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnClose();
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnTimer(UINT_PTR nIDEvent);
	virtual void OnInitialUpdate();
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	virtual BOOL DestroyWindow();
};



class CAnimationOpView : public CFormView
{
	DECLARE_DYNCREATE(CAnimationOpView)
private:
	bool m_bInited;
	CComboBox m_ComboBoxCurAnimationGroup;
	CListBox m_ListBoxAnimation;
	CSliderCtrl m_SliderFreq;
	CSliderCtrl m_SliderVol;

protected:
	virtual ~CAnimationOpView();
	CAnimationOpView();         

public:
	enum { IDD = IDD_ACTIONOPVIEW };
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnBnClickedButton2();
	afx_msg void OnCbnSelChangeComboBoxCurAniGroup();
	afx_msg void OnBnClickedBtnRenameAnimation();
	afx_msg void OnContextMenu(CWnd* , CPoint );
	afx_msg void OnBnClickedBtnDeleteAnimation();
	afx_msg void OnBnClickedBtnOpenAnimation();
	afx_msg void OnBnClickedBtnNewAnimation();
	afx_msg void OnLbnDblclkListBoxAnimation();
	virtual void OnUpdate(CView* , LPARAM , CObject* );

protected:
	virtual void OnDraw(CDC* );
public:
	afx_msg void OnLbnSelchangeListBoxAnimation();
};


#pragma once





class CEquipSelectView : public CFormView
{
	DECLARE_DYNCREATE(CEquipSelectView)

protected:
	CEquipSelectView();           
	virtual ~CEquipSelectView();

public:
	enum { IDD = IDD_EQUIPSELECTVIEW };
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
	virtual void OnUpdate(CView* , LPARAM , CObject* );
private:
	CString m_CurActionName;
	CString m_CurAniName;
	BOOL m_bShowEcArr[Nuclear::XPSC_MAXCOUNT];

#define M_ACV_DCL_1(i) public: \
	CComboBox m_cmbCurL##i; \
	afx_msg void OnBnClickedChkShowL##i(); \
	afx_msg void OnBnClickedBtnNewL##i(); \
	afx_msg void OnBnClickedBtnReloadL##i(); \
	afx_msg void OnBnClickedBtnDelL##i(); \
	afx_msg void OnBnClickedBtnCcL##i(); \
	afx_msg void OnCbnSelchangeCmbCurL##i()

	M_ACV_DCL_1(1);
	M_ACV_DCL_1(2);
	M_ACV_DCL_1(3);
	M_ACV_DCL_1(4);
	M_ACV_DCL_1(5);
	M_ACV_DCL_1(6);
	M_ACV_DCL_1(7);
	M_ACV_DCL_1(8);
	M_ACV_DCL_1(9);
	M_ACV_DCL_1(10);
	M_ACV_DCL_1(11);
	M_ACV_DCL_1(12);

public:
	afx_msg void OnBnClickedBtnNewAnimation();
	afx_msg void OnBnClickedBtnCcAni();

	afx_msg void OnBnClickedBtnOpenRs();
	afx_msg void OnBnClickedChkEnableRS();
	afx_msg void OnBnClickedChkFenKuai();
	afx_msg void OnEnChangeEditPicPathPrefix();
};


#pragma once





class CActionControlView : public CFormView
{
	DECLARE_DYNCREATE(CActionControlView)

protected:
	CActionControlView();           
	virtual ~CActionControlView();

public:
	void Update();
public:
	enum { IDD = IDD_ACTIONCONTROLVIEW };
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
	virtual void OnUpdate(CView* , LPARAM , CObject* );
private:
	CComboBox m_ComboBoxWaterType;
	int m_Dirs1;
	int m_ecOrders[Nuclear::XPSC_MAXCOUNT];
	CScrollBar m_ScrollBarFrame;
	CSliderCtrl m_SilderScale;
	CString m_CurFrame;
	CListBox m_ListBoxEcOrder;
	int m_iTime;
	int m_iOffX;
	int m_iOffY;

public:
	afx_msg void OnBnClickedRadio();
	virtual void OnInitialUpdate();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnBnClickedBtnMoveUp();
	afx_msg void OnBnClickedBtnMoveDown();
	afx_msg void OnBnClickedBtnSaveAni();
	afx_msg void OnBnClickedBtnApplyAll();
	afx_msg void OnBnClickedBtnSetTime();
	afx_msg void OnBnClickedBtnEvTime();
	afx_msg void OnBnClickedButtonTitlePos();
	afx_msg void OnCbnSelchangeComboWaterType();
	afx_msg void OnBnClickedSettingNone();
	afx_msg void OnBnClickedSettingRide();
	afx_msg void OnBnClickedSettingEffectPosition();
	afx_msg void OnBnClickedButtonResetScale();
	afx_msg void OnBnClickedSettingBoundingBox();
	afx_msg void OnBnClickedBtnApplyAllDir();
	afx_msg void OnBnClickedBtnCopy();
	afx_msg void OnBnClickedSettingBase();

	afx_msg void OnBnClickedButtonSaveScale();
};


