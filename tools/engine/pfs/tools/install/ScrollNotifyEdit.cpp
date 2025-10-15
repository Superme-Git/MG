// ScrollNotifyEdit.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"
#include "ScrollNotifyEdit.h"


// CScrollNotifyEdit
IMPLEMENT_DYNAMIC(CScrollNotifyEdit, CEdit)

CScrollNotifyEdit::CScrollNotifyEdit()
{
}

CScrollNotifyEdit::~CScrollNotifyEdit()
{
}

BEGIN_MESSAGE_MAP(CScrollNotifyEdit, CEdit)
	ON_WM_VSCROLL()
	ON_WM_MOUSEWHEEL()
END_MESSAGE_MAP()

// CScrollNotifyEdit message handlers
void CScrollNotifyEdit::OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: Add your message handler code here and/or call default
	CEdit::OnVScroll(nSBCode, nPos, pScrollBar);
	OnVScrollButton();
}


BOOL CScrollNotifyEdit::OnMouseWheel(UINT nFlags, short zDelta, CPoint pt)
{
	// TODO: Add your message handler code here and/or call default
	BOOL res = CEdit::OnMouseWheel(nFlags, zDelta, pt);
	OnVScrollButton();
	return res;
}

void CScrollNotifyEdit::OnVScrollButton()
{
	if( GetScrollLimit( SB_VERT) <= GetScrollPos( SB_VERT))
		GetParent()->SendMessage( WM_SCROLLNOTIFY);
}
