#pragma once
#include "TimeBarCtrl.h"



class CTimeBarView : public CView, public TimeBarNotify, public INotify
{
	DECLARE_DYNCREATE(CTimeBarView)

	CTimeBarCtrl m_TimeBar;
	bool m_bInited;
protected:
	virtual ~CTimeBarView();
	CTimeBarView();           

public:
	virtual void OnRender()
	{
		m_TimeBar.SetPlayPos(GetDocument()->GetPlayingFrame());
		m_TimeBar.Invalidate(FALSE);
	}
	virtual bool OnDragingPlayFrame(int frame)
	{
		return GetDocument()->SetPlayingFrame(frame);
	}
	virtual bool OnUnSelectedAllItem();
	virtual bool OnUnSelectedItem(DWORD objID);
	virtual bool OnSelectedItem(DWORD objID);
	virtual bool OnModifyMaxFrame(int nMaxFrame);
	virtual bool OnModifySpare(int pos);
	virtual bool OnModify(const ClipObj& obj);
	virtual void OnDraw(CDC* pDC);      
	CEffectEditorDoc* GetDocument() const;


#ifdef _DEBUG
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
	virtual void AssertValid() const;
#endif
private:
	void ReSizeTimeBar();

protected:
	DECLARE_MESSAGE_MAP()
	virtual void OnUpdate(CView* , LPARAM , CObject* );
public:
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	virtual void OnInitialUpdate();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
};

#ifndef _DEBUG  
inline CEffectEditorDoc* CTimeBarView::GetDocument() const
{ return reinterpret_cast<CEffectEditorDoc*>(m_pDocument); }
#endif

