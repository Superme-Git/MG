///=============================================================================
/// Product   : DXTest
/// Project   : DXTest
/// Module    : SvgEditView.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	SVG ±à¼­Æ÷ÒýÇæ²âÊÔ³ÌÐò
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-15 Created.
///=============================================================================
#pragma once

#include "..\..\engine\renderer\renderer.h"
#include "..\agg-2.5\examples\svg_viewer\svg_developer_engine.h"

#ifndef D3DCOLOR_DEFINED
typedef DWORD D3DCOLOR;
#define D3DCOLOR_DEFINED
#endif

struct ShowPart
{
	unsigned long	ulID;		// Part ID
	long			lType;		// Part Type
	std::wstring	strSvg;		// Svg File Path
	std::wstring	strTga;		// Tga thumbnail File Path
	unsigned long	ulLayer;	// Layer ID
	unsigned long	ulLinkNum;	// Number of Linking Parts, 0 - no any linking part.
	long			bSexMan;	// 0 - women, 1 - man
};

typedef std::vector<ShowPart> TShowPartContainer;

// CSvgEditView view

typedef Nuclear::PictureHandle HPICTURE;

class CSvgEditView : public CScrollView
{
	DECLARE_DYNCREATE(CSvgEditView)

protected:
	CSvgEditView();           // protected constructor used by dynamic creation
	virtual ~CSvgEditView();

	Nuclear::Renderer *m_pRenderer;
	LONG Init( HWND hwnd );
	void Destroy();
	svg::svg_developer_engine m_svg_render;
	BOOL		m_bClipToClient;
	BOOL		m_bFullTransparent;
	D3DCOLOR	m_clrBackground;
	D3DCOLOR	m_clrImageBackground;
	HPICTURE	m_hPicture;
	BOOL		m_bLastIsSVG;

	CRect		m_rcPicture;
	CString		m_strFilePath;

	void*		m_imageBuffer;
	unsigned int m_bufferSize;


	int m_dx, m_dy;
	int m_x, m_y;
	bool m_drag_flag;

	TShowPartContainer m_vShowParts;

	void Refresh();

public:
	void ShowPicture( LPCTSTR lpszFilePath );

protected:
	afx_msg void OnDestroy();
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg void OnFileOpen();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSvgZoomin();
	afx_msg void OnSvgZoomout();
	afx_msg void OnSvgRotateClockwised();
	afx_msg void OnSvgRotateCounterClockwised();
	afx_msg void OnUpdateNeedSvg(CCmdUI *pCmdUI);
	afx_msg void OnSvgOriginalview();
	afx_msg void OnContextMenu(CWnd* /*pWnd*/, CPoint /*point*/);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnOptionBackground();
	afx_msg void OnOptionClipByClient();
	afx_msg void OnOptionImagebackground();
	afx_msg void OnOptionFulltransparent();
	afx_msg void OnUpdateOptionClipByClient(CCmdUI *pCmdUI);
	afx_msg void OnUpdateOptionFulltransparent(CCmdUI *pCmdUI);
	afx_msg void OnUpdateOptionImagebackground(CCmdUI *pCmdUI);


	virtual void OnDraw(CDC* pDC);      // overridden to draw this view
	virtual void OnInitialUpdate();     // first time after construct

	DECLARE_MESSAGE_MAP()
};


