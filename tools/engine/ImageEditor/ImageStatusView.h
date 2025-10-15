#pragma once
#include "afxwin.h"

#include <map>
#include "..\engine/common\xptypes.h"

class CImageStatusView : public CView
{
	DECLARE_DYNCREATE(CImageStatusView)

protected:
	CImageStatusView();           // protected constructor used by dynamic creation
	virtual ~CImageStatusView();

public:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	std::map<Nuclear::XPTEXTURE_FORMAT, std::wstring> m_fmtMap;

protected:
	DECLARE_MESSAGE_MAP()
};
