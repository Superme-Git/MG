#pragma once

class CDIYEditCtrl : public CEdit
{
	DECLARE_DYNAMIC(CDIYEditCtrl)

public:
	CDIYEditCtrl();
	virtual ~CDIYEditCtrl();

	virtual void DrawItem(LPDRAWITEMSTRUCT lpDrawItemStruct);
	BOOL InitializeFromBitmap(
		const CBitmap& rDIBitmap,
		const CRect& rFirstRect,
		BOOL bHasDisabledStatusBitmap = TRUE); 
	BOOL LoadBitmaps(LPCTSTR lpszBitmapResource);

	void SetTipInfo(LPCTSTR lpszTipInfo)
	{
		m_strTipInfo = lpszTipInfo;
	}


protected:
	afx_msg void OnChange();
	afx_msg HBRUSH CtlColor(CDC *pDC, UINT nCtlColor);
	afx_msg void OnChar(UINT nChar, UINT nRep, UINT nFlags);
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	virtual void PreSubclassWindow();

	CBitmap m_bitmap;			// ±³¾°Í¼Æ¬ HBITMAP
	CRgn	m_wndClipRgn;
	CString m_strTipInfo;
	CBrush  m_brush;

	DECLARE_MESSAGE_MAP()
};