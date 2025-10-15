#pragma once
#include "afxwin.h"
#include <list>

class CDistortionStatusView : public CView
{
	DECLARE_DYNCREATE(CDistortionStatusView)

protected:
	CDistortionStatusView();           // protected constructor used by dynamic creation
	virtual ~CDistortionStatusView();

public:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view

public:

#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnLButtonDblClk(UINT nFlags, CPoint point);
};
