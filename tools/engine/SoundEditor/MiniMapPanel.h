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
	int m_mouseState;			//����״̬
	CPoint	m_LMouseDownPos;	//�����������ʱ���λ�ã���Ļ����
	CPoint	m_RMouseDownPos;	//�������Ҽ���ʱ���λ�ã���Ļ����
	CPoint	m_MousePos;			//����λ�ã���Ļ����
	CSliderCtrl m_sliderScale;
	CImage m_miniMap;			//minimap��С
	//CImage m_envAreaMap;		//���ź�Ĵ�С
	//CImage m_bkgAreaMap;		//���ź�Ĵ�С
	CRect m_miniMapRect;
	CRect m_engineViewport;		//�����viewport, miniMapԭʼ����
	int m_nMiniMapWidth;		//miniMapԭʼ���꣬miniMap�Ŀ��
	int m_nMiniMapHeight;		//miniMapԭʼ���꣬miniMap�ĸ߶�
	int m_nViewportWidth;		//miniMapԭʼ���꣬viewport�Ŀ��
	int m_nViewportHeight;		//miniMapԭʼ���꣬viewport�Ŀ��
	float m_fRatio;
	float m_fMinRatio;			//��С�����ű�
	CPoint m_DrawScrollPos;		//miniMapԭʼ���꣬viewport���Ͻ�λ��

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
