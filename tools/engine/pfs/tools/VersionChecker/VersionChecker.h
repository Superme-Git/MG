// VersionChecker.h : main header file for the VersionChecker application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols


// CVersionCheckerApp:
// See VersionChecker.cpp for the implementation of this class
//

class CVersionCheckerApp : public CWinApp
{
public:
	CVersionCheckerApp();


// Overrides
public:
	virtual BOOL InitInstance();

// Implementation
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CVersionCheckerApp theApp;