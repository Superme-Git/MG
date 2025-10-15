#pragma once


// CScrollNotifyEdit
#define	WM_SCROLLNOTIFY		(WM_USER+100)
class CScrollNotifyEdit : public CEdit
{
	DECLARE_DYNAMIC(CScrollNotifyEdit)

public:
	CScrollNotifyEdit();
	virtual ~CScrollNotifyEdit();

protected:
	DECLARE_MESSAGE_MAP()

	void OnVScrollButton();

public:
	afx_msg void OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg BOOL OnMouseWheel(UINT nFlags, short zDelta, CPoint pt);
};

