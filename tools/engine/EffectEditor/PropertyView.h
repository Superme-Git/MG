#pragma once
#include "TabPropPanel.h"
#include "EffectEditorDoc.h"



class CPropertyView : public CView
{
	DECLARE_DYNCREATE(CPropertyView)
private:
	CTabPropPanel m_TabPropPanel;
	bool m_bInited;

private:
	void ResetCount();
	void ResetSize();

protected:
	virtual ~CPropertyView();
	CPropertyView();

public:
	CEffectEditorDoc* GetDocument() const;
	virtual void OnDraw(CDC* pDC);   

#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
	virtual void OnInitialUpdate();

};


#ifndef _DEBUG  
inline CEffectEditorDoc* CPropertyView::GetDocument() const
{ return reinterpret_cast<CEffectEditorDoc*>(m_pDocument); }
#endif