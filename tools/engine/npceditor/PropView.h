#pragma once
#include "afxwin.h"
#include "TabPropPanel.h"


class CSoundEditorDoc;
class CPropView :
	public CView
{
private:
	bool m_bIsInited;
	

protected:
	CPropView(void);
	DECLARE_DYNCREATE(CPropView)

public:
	~CPropView(void);
	CSoundEditorDoc* GetDocument() const;
	CTabPropPanel m_tabPropPanel;

#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif


private:
	void ResetSize();
	void ResetCont();

	// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
	virtual void OnDraw(CDC* /*pDC*/);
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
};

#ifndef _DEBUG  // CPropView.cpp 中的调试版本
inline CSoundEditorDoc* CPropView::GetDocument() const
{ return reinterpret_cast<CSoundEditorDoc*>(m_pDocument); }
#endif