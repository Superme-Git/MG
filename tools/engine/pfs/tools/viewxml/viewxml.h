// viewxml.h : main header file for the viewxml application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols


// CviewxmlApp:
// See viewxml.cpp for the implementation of this class
//

class CviewxmlApp : public CWinApp
{
public:
	CviewxmlApp();


// Overrides
public:
	virtual BOOL InitInstance();

// Implementation
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CviewxmlApp theApp;