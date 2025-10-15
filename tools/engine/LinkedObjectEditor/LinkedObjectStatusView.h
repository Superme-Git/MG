#pragma once
#include "afxwin.h"
#include <list>

class CLinkedObjectStatusView : public CView
{
	DECLARE_DYNCREATE(CLinkedObjectStatusView)

protected:
	CLinkedObjectStatusView();           // protected constructor used by dynamic creation
	virtual ~CLinkedObjectStatusView();

public:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view

public:
	std::list<CString> m_strFileNameList;

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
