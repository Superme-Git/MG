// DIYButton.h : header file
//

#pragma once

/////////////////////////////////////////////////////////////////////////////
// CDIYButton

class CDIYButton : public CButton
{
	// 支持动态类识别
	DECLARE_DYNAMIC(CDIYButton)

// Construction
public:
	CDIYButton();

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDIYButton)
	public:
	virtual void DrawItem(LPDRAWITEMSTRUCT lpDrawItemStruct);
	virtual void SetCheck(BOOL bCheck);
	virtual BOOL GetCheck();
	protected:
	virtual void PreSubclassWindow();
	//}}AFX_VIRTUAL

// Implementation
public:
	BOOL AutoLoad(UINT nID, CWnd* pParent);
	void SizeToContent();
	BOOL LoadBitmaps(LPCTSTR lpszBitmapResource, 
		LPCTSTR lpszBitmapResourceSel = NULL,
		LPCTSTR lpszBitmapResourceHover = NULL,
		LPCTSTR lpszBitmapResourceFocus = NULL,
		LPCTSTR lpszBitmapResourceDisabled = NULL);
	
	BOOL LoadBitmaps(UINT nIDBitmapResource, 
		UINT nIDBitmapResourceSel = 0,
		UINT nIDBitmapResourceHover = 0,
		UINT nIDBitmapResourceFocus = 0,
		UINT nIDBitmapResourceDisabled = 0);

	//////////////////////////////////////////////////////////////////////////
	// 一个单一的位图文件提供所有的状态
	// 它们纵向排列成一排，横坐标相同，
	// 第一个位图块的矩形区由rFirstRect定义
	// 每个位图块的纵向间距为yGap
	// 整个位图文件至少提供4个状态的子图
	// 从上到下依次为: 
	//
	//		Normal
	//		Hover
	//		Selected
	//		Disabled
	//
	// 没有单独提供Focus状态，由Normal提供
	//
	// 使用DIBBitmap可以实现16bit/32Bit桌面显示环境下的无差别渲染
	//
	BOOL InitializeFromBitmap(
		const CBitmap& rDIBitmap,
		const CRect& rFirstRect,
		BOOL bHasDisabledStatusBitmap = TRUE); 

	// 初始化方式2
	// 通过配置的坐标，获取其他图，这样允许图与图之间有任意的间隔。
	// 但是，必须保证这些状态图在一张底图上。
	BOOL InitializeFromBitmap2(
		const CBitmap& rDIBitmap,
		const CRect& rRectNormal,
		const CRect& rRectHover,
		const CRect& rRectSelected,
		const CRect& rRectDisabled); 

	void SetTipInfo(LPCTSTR lpszTipInfo)
	{
		m_strTipInfo = lpszTipInfo;
	}

	LPCTSTR GetTipInfo() const
	{
		return m_strTipInfo.GetString();
	}

	BOOL DrawBitmap(HDC hDC, CBitmap *pBitmap);

	virtual ~CDIYButton();

	// Generated message map functions
protected:
	//{{AFX_MSG(CDIYButton)
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	//}}AFX_MSG
	LRESULT OnMouseLeave(WPARAM wParam, LPARAM lParam);
	
	BOOL	m_bHover;
	BOOL	m_selected;
	CBitmap m_bitmap; 
	CBitmap m_bitmapSel; 
	CBitmap m_bitmapHover; 
	CBitmap m_bitmapFocus; 
	CBitmap m_bitmapDisabled; 
	CRgn	m_wndClipRgn;
	CString m_strTipInfo;

	DECLARE_MESSAGE_MAP()
};
