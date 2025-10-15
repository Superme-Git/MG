// DXTestView.h : interface of the CDXTestView class
//
#pragma once

//#include "..\..\engine\renderer\renderer.h"
#include "..\agg-2.5\examples\svg_viewer\svg_developer_engine.h"
#include <list>
#include "ShowPartLoader.h"

//#ifndef D3DCOLOR_DEFINED
//typedef DWORD D3DCOLOR;
//#define D3DCOLOR_DEFINED
//#endif

typedef std::map<int, svg::svg_image_ptr>	SvgImageMap;

class CEditPartView;
class CDXTestView : public CView
{
protected: // create from serialization only
	CDXTestView();
	DECLARE_DYNCREATE(CDXTestView)

	//Nuclear::Renderer *m_pRenderer;

	LONG Init( HWND hwnd );
	void Destroy();

	int m_hPicture;


	CRect		m_rcPicture;
	CString		m_strFilePath;
	HBRUSH		m_hbrBackground;
	BITMAPINFO	m_BitmapInfo;

	BOOL		m_bLastIsSVG;
	svg::svg_developer_engine m_svg_render;


	BOOL		m_bClipToClient;
	BOOL		m_bFullTransparent;
	//D3DCOLOR	m_clrBackground;
	//D3DCOLOR	m_clrImageBackground;
	DWORD		m_clrBackground;
	DWORD		m_clrImageBackground;

	char*		m_buffer;
	int			m_buffersize;

	CPoint		m_ptLast;
	BOOL		m_bMouseDown;

	svg::svg_image_ptr m_select;

	HDC			m_dcMem;

	// 操作
public:
	void ShowPicture( LPCTSTR lpszFilePath );

	bool ShowPart( const TShowPartPtrContainer& tspc);

	bool HidePart( const TShowPartPtrContainer& tspc);

	bool ChangeLayer( const TShowPartPtrContainer& tspc, unsigned long oldLayer);

	bool SelectPart( const TShowPartPtrContainer& tspc);

	bool RemovePart( unsigned long ulID, unsigned long ulLayer);

	bool ChangeLayer( const TShowPartPtrContainer& tspc);

	// 清除所有部件
	void ClearAllParts();

	// 得到当前所有显示部件
	void GetAllShowParts( SvgImageMap& showParts);

	// 清除conflict部件
	// bRefresh 是否刷新
	void ClearConflictParts( __int64 conflict, bool bRefresh);
	
	CEditPartView*	m_pEditPartView;

	// 重写
protected:
	void Refresh();
	
	// 移动选中部件
	void TranslateSelectPart( int x, int y);

	// Attributes
public:
	//CDXTestDoc* GetDocument() const
	//{ return reinterpret_cast<CDXTestDoc*>(m_pDocument); }

// Operations
public:

// Overrides
public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

// Implementation
public:
	virtual ~CDXTestView();

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnDestroy();
	virtual void OnInitialUpdate();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg void OnFileOpen();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
protected:
	virtual LRESULT WindowProc(UINT message, WPARAM wParam, LPARAM lParam);
public:
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

	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
};
