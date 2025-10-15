// MyBarBottom.cpp : implementation file
//  
// Copyright 1998 Written by Kirk Stowell     
//		mailto:kstowel@sprynet.com  
//		http://www.geocities.com/SiliconValley/Haven/8230  
//  
// This code may be used in compiled form in any way you desire. This    
// file may be redistributed unmodified by any means PROVIDING it is     
// not sold for profit without the authors written consent, and     
// providing that this notice and the authors name and all copyright     
// notices remains intact. If the source code in this file is used in     
// any  commercial application then a statement along the lines of     
// "Portions Copyright ?1998 Kirk Stowell" must be included in     
// the startup banner, "About" box or printed documentation. An email     
// letting me know that you are using it would be nice as well. That's     
// not much to ask considering the amount of work that went into this.    
//    
// This file is provided "as is" with no expressed or implied warranty.    
// The author accepts no liability for any damage/loss of business that    
// this product may cause.    
//  
/////////////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "MyBarBottom.h"
#include "Resource.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CMyBarBottom

CMyBarBottom::CMyBarBottom()
{
	// TODO: add construction code here.
}

CMyBarBottom::~CMyBarBottom()
{
	// TODO: add destruction code here.
}


BEGIN_MESSAGE_MAP(CMyBarBottom, CControlBar)
	//{{AFX_MSG_MAP(CMyBarBottom)
	ON_WM_CREATE()
	ON_WM_WINDOWPOSCHANGED()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CMyBarBottom message handlers

#define IDC_RECOMMCONTENT 8354 // 暂时随意定义的ID

int CMyBarBottom::OnCreate(LPCREATESTRUCT lpCreateStruct) 
{
	if (CControlBar::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	// TODO: Add your specialized creation code here
	if (!m_wndRichEdit.Create (WS_CHILD | WS_VISIBLE | WS_VSCROLL | 
		ES_AUTOHSCROLL | ES_AUTOVSCROLL | 
		ES_MULTILINE | ES_NOHIDESEL | 
		ES_SELECTIONBAR |
		ES_READONLY,
		CRect(0,0,200,200), this, IDC_RECOMMCONTENT))
	{
		TRACE0("Failed to create RichEdit for CommContentt\n");
		return -1;
	}
	m_wndRichEdit.ModifyStyleEx(0, WS_EX_CLIENTEDGE);

	CHARFORMAT charformat;
	charformat.cbSize = sizeof(CHARFORMAT); 
	m_wndRichEdit.GetDefaultCharFormat(charformat);
	charformat.dwMask |= CFM_COLOR;
	charformat.dwMask |= CFM_SIZE;
	charformat.dwMask |= CFM_ITALIC;
	charformat.dwEffects &= ~CFE_AUTOCOLOR;
	charformat.dwEffects &= ~CFE_BOLD;
	charformat.dwEffects &=  ~CFE_ITALIC;
	charformat.yHeight = 180;
//	strcpy(charformat.szFaceName, _T("simfang"));
	wcscpy_s(charformat.szFaceName, _T("simfang"));
	m_wndRichEdit.SetDefaultCharFormat(charformat);
	return 0;
}

void CMyBarBottom::OnWindowPosChanged(WINDOWPOS FAR* lpwndpos) 
{
	CControlBar::OnWindowPosChanged(lpwndpos);
	
	// TODO: Add your message handler code here
	if (IsFloating()) {
		m_wndRichEdit.MoveWindow( 5, 5, lpwndpos->cx-10, lpwndpos->cy-10 );
	}

	else if (IsHorzDocked()) {
		m_wndRichEdit.MoveWindow( 17, 3, lpwndpos->cx-25, lpwndpos->cy-17 );
	}

	else {
		m_wndRichEdit.MoveWindow( 3, 17, lpwndpos->cx-17, lpwndpos->cy-30 );
	}
}


/////////////////////////////////////////////////////////////////////////////
// MyRichEdit

CMyRichEdit::CMyRichEdit()
{
}

CMyRichEdit::~CMyRichEdit()
{
}


BEGIN_MESSAGE_MAP(CMyRichEdit, CRichEditCtrl)
	//{{AFX_MSG_MAP(MyRichEdit)
	ON_WM_RBUTTONDOWN()
	ON_COMMAND( ID_EDIT_COPY, &CRichEditCtrl::Copy)
	ON_COMMAND( ID_EDIT_CLEAR_ALL, &CRichEditCtrl::Clear)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// MyRichEdit message handlers

HWND CMyRichEdit::SetFocus()
{
	return NULL;
}

#define IDR_CLEARRICHEDIT	13000
#define IDR_COPY			13001
void CMyRichEdit::OnRButtonDown( UINT nFlags, CPoint point)
{
	ClientToScreen( &point);
	CMenu menu;
	menu.CreatePopupMenu();
	menu.AppendMenuW( MF_STRING, ID_EDIT_COPY, L"复制");
	menu.AppendMenuW( MF_STRING, ID_EDIT_CLEAR_ALL, L"清空");
	menu.TrackPopupMenu( TPM_LEFTALIGN, point.x, point.y, this);
}

BOOL CMyRichEdit::OnCommand( WPARAM wParam, LPARAM lParam)
{
	switch( wParam)
	{
	case ID_EDIT_COPY:		Copy();		return true;
	case ID_EDIT_CLEAR_ALL:	SetWindowText(L"");	return true;
	}
	return CRichEditCtrl::OnCommand( wParam, lParam);
}