// viewpfs.h : main header file for the viewpfs application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols


// CviewpfsApp:
// See viewpfs.cpp for the implementation of this class
//

class CviewpfsApp : public CWinApp
{
public:
	CviewpfsApp();


// Overrides
public:
	virtual BOOL InitInstance();

// Implementation
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CviewpfsApp theApp;