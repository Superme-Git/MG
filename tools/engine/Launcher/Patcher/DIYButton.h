// DIYButton.h : header file
//

#pragma once

/////////////////////////////////////////////////////////////////////////////
// CDIYButton

class CDIYButton : public CButton
{
	// ֧�ֶ�̬��ʶ��
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
	// һ����һ��λͼ�ļ��ṩ���е�״̬
	// �����������г�һ�ţ���������ͬ��
	// ��һ��λͼ��ľ�������rFirstRect����
	// ÿ��λͼ���������ΪyGap
	// ����λͼ�ļ������ṩ4��״̬����ͼ
	// ���ϵ�������Ϊ: 
	//
	//		Normal
	//		Hover
	//		Selected
	//		Disabled
	//
	// û�е����ṩFocus״̬����Normal�ṩ
	//
	// ʹ��DIBBitmap����ʵ��16bit/32Bit������ʾ�����µ��޲����Ⱦ
	//
	BOOL InitializeFromBitmap(
		const CBitmap& rDIBitmap,
		const CRect& rFirstRect,
		BOOL bHasDisabledStatusBitmap = TRUE); 

	// ��ʼ����ʽ2
	// ͨ�����õ����꣬��ȡ����ͼ����������ͼ��ͼ֮��������ļ����
	// ���ǣ����뱣֤��Щ״̬ͼ��һ�ŵ�ͼ�ϡ�
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
