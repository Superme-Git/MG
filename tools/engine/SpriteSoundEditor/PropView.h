#pragma once
#include "PropDlg.h"

// CPropView view
class CSpriteSoundEditorDoc;
class CPropView : public CView
{
	DECLARE_DYNCREATE(CPropView)
private:
	CPropDlg m_dlg;
	bool m_bIsInited;

private:
	void ResetCont();
	void ResetSize();

protected:
	CPropView();           // protected constructor used by dynamic creation
	virtual ~CPropView();

public:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

	CSpriteSoundEditorDoc* GetDocument() const;

protected:
	DECLARE_MESSAGE_MAP()
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
};


#ifndef _DEBUG  // SpriteSoundEditorView.cpp 中的调试版本
inline CSpriteSoundEditorDoc* CPropView::GetDocument() const
{ return reinterpret_cast<CSpriteSoundEditorDoc*>(m_pDocument); }
#endif
