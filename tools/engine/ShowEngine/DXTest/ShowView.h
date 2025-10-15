#pragma once

#include "..\..\engine\renderer\renderer.h"
//#include "..\agg-2.5\examples\svg_viewer\svg_render_engine.h"

#include "..\ShowEngine\ShowRenderer.h"

// CShowView view
#ifndef D3DCOLOR_DEFINED
typedef DWORD D3DCOLOR;
#define D3DCOLOR_DEFINED
#endif

class CShowView : public CView
{
	DECLARE_DYNCREATE(CShowView)

protected:
	CShowView();           // protected constructor used by dynamic creation
	virtual ~CShowView();

	Nuclear::Renderer *m_pRenderer;

	LONG Init( HWND hwnd );
	void Destroy();

	typedef Nuclear::PictureHandle HPICTURE;

	HPICTURE	m_hPicture;

	BOOL		m_bClipToClient;
	BOOL		m_bFullTransparent;

	D3DCOLOR	m_clrBackground;

	D3DCOLOR	m_clrImageBackground;

	CRect		m_rcPicture;
	CString		m_strFilePath;
	HBRUSH		m_hbrBackground;
	BITMAPINFO	m_BitmapInfo;

	BOOL		m_bLastIsSVG;
	IShowRenderer* m_svg_render;

	int m_dx, m_dy;
	int m_x, m_y;

	bool m_drag_flag;

	void*		m_imageBuffer;
	unsigned int m_bufferSize;

	// ²Ù×÷
public:
	void ShowPicture( LPCTSTR lpszFilePath );

	// Attributes
public:
	CDXTestDoc* GetDocument() const
	{ return reinterpret_cast<CDXTestDoc*>(m_pDocument); }

	// ÖØÐ´
protected:

	void Refresh();

public:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view

	virtual void OnInitialUpdate();
protected:
	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnDestroy();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg void OnFileOpen();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnSvgZoomin();
	afx_msg void OnSvgZoomout();
	afx_msg void OnUpdateNeedSvg(CCmdUI *pCmdUI);
	afx_msg void OnSvgOriginalview();
	afx_msg void OnContextMenu(CWnd* /*pWnd*/, CPoint /*point*/);
	afx_msg void OnSvgRotateClockwised();
	afx_msg void OnSvgRotateCounterClockwised();
	afx_msg void OnOptionBackground();
	afx_msg void OnOptionClipByClient();
	afx_msg void OnUpdateOptionClipByClient(CCmdUI *pCmdUI);
	afx_msg void OnOptionImagebackground();
	afx_msg void OnOptionFulltransparent();
	afx_msg void OnUpdateOptionFulltransparent(CCmdUI *pCmdUI);
	afx_msg void OnUpdateOptionImagebackground(CCmdUI *pCmdUI);
};


