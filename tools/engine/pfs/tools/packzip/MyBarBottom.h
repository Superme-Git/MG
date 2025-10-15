// MyBarBottom.h : header file
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

#if !defined(MYBARBOTTOM_H_INCLUDED)
#define MYBARBOTTOM_H_INCLUDED

#if _MSC_VER >= 1000
#pragma once
#endif // _MSC_VER >= 1000

/////////////////////////////////////////////////////////////////////////////
// CMyBarBottom class

#include "CJControlBar.h"
#define CControlBar		CCJControlBar


/////////////////////////////////////////////////////////////////////////////
// MyRichEdit window

class CMyRichEdit : public CRichEditCtrl
{
// Construction
public:
	CMyRichEdit();

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(MyRichEdit)
	//}}AFX_VIRTUAL

// Implementation
public:
	HWND SetFocus();
	virtual ~CMyRichEdit();

	// Generated message map functions
protected:
	//{{AFX_MSG(MyRichEdit)
	afx_msg void OnRButtonDown( UINT nFlags, CPoint point);
	virtual BOOL OnCommand( WPARAM wParam, LPARAM lParam);
	//}}AFX_MSG

	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////
class CMyBarBottom : public CControlBar
{
// Construction
public:
	CMyBarBottom();

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMyBarBottom)
	//}}AFX_VIRTUAL

// Implementation
public:
	CMyRichEdit m_wndRichEdit;
	virtual ~CMyBarBottom();

// Generated message map functions
protected:
	//{{AFX_MSG(CMyBarBottom)
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnWindowPosChanged(WINDOWPOS FAR* lpwndpos);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////
//{{AFX_INSERT_LOCATION}}
// Microsoft Developer Studio will insert additional declarations immediately before the previous line.

#endif // !defined(MYBARBOTTOM_H_INCLUDED)

