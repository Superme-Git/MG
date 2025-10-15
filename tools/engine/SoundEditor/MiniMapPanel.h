#pragma once
#include "afxcmn.h"
#include <atlimage.h>

// CMiniMapPanel dialog
class CPropView;

class CMiniMapPanel : public CDialog
{
	DECLARE_DYNAMIC(CMiniMapPanel)
private:
	//enum AREA_MAP_STATE
	//{
	//	AMS_ENV_DIRTY = 1,
	//	AMS_BKG_DIRTY = 2,
	//	AMS_ENV_SIZE_DIRTY = 4,
	//	AMS_BKG_SIZE_DIRTY = 8,
	//	AMS_ALL = 0xFFFFFFFF
	//};
public:
	// Dialog Data
	enum { IDD = IDD_MINIMAP_PANEL };

private:
	CPropView* m_pView;
	bool m_bIsInited;
	bool m_bImgValid;
	//int m_nAreaMapState;
	int m_mouseState;			//鼠标的状态
	CPoint	m_LMouseDownPos;	//鼠标点下左键的时候的位置，屏幕坐标
	CPoint	m_RMouseDownPos;	//鼠标点下右键的时候的位置，屏幕坐标
	CPoint	m_MousePos;			//鼠标的位置，屏幕坐标
	CSliderCtrl m_sliderScale;
	CImage m_miniMap;			//minimap大小
	//CImage m_envAreaMap;		//缩放后的大小
	//CImage m_bkgAreaMap;		//缩放后的大小
	CRect m_miniMapRect;
	CRect m_engineViewport;		//引擎的viewport, miniMap原始坐标
	int m_nMiniMapWidth;		//miniMap原始坐标，miniMap的宽度
	int m_nMiniMapHeight;		//miniMap原始坐标，miniMap的高度
	int m_nViewportWidth;		//miniMap原始坐标，viewport的宽度
	int m_nViewportHeight;		//miniMap原始坐标，viewport的宽度
	float m_fRatio;
	float m_fMinRatio;			//最小的缩放比
	CPoint m_DrawScrollPos;		//miniMap原始坐标，viewport左上角位置

public:
	CMiniMapPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~CMiniMapPanel();

private:
	void ResetSize();
	void ResetMinRatio();
	void DrawCtrl(CDC *pDC);
	void FixViewport(CPoint &lt, const CPoint &v);
	void ResetEngineViewport();
	void DrawEnvArea(CDC *pDC, CPoint lt);
	void DrawBkgArea(CDC *pDC, CPoint lt);
	void DrawBkgVolArea(CDC *pDC, CPoint lt);

public:
	void ResetCont();
	void SetRatio(float ratio);


protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnRButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnTimer(UINT_PTR nIDEvent);
};
