#pragma once

#include "ToolsPanel.h"



class CEffectEditorDoc;

class CToolView : public CView, public IToolsNotify
{
	DECLARE_DYNCREATE(CToolView)
private:
	CToolsPanel m_Panel;
	bool m_bInited;
protected:
	virtual ~CToolView();
	CToolView();         

public:
	CEffectEditorDoc* GetDocument() const;
	virtual void OnAddClip() { OnAddAClip(); }

	virtual void OnToolsControl(UINT nID)
	{
		CFrameWnd* pWnd = GetParentFrame();
		WPARAM wParam = MAKEWPARAM(nID, 0);
		pWnd->SendMessage(WM_COMMAND, wParam, NULL);
	}

	virtual void OnDraw(CDC* pDC);      


#ifdef _DEBUG
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
	virtual void AssertValid() const;
#endif

protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnAddAClip();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
};


#ifndef _DEBUG  
inline CEffectEditorDoc* CToolView::GetDocument() const
{ return reinterpret_cast<CEffectEditorDoc*>(m_pDocument); }
#endif

