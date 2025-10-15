// PackZip.h : main header file for the PackZip application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols


// CPackZipApp:
// See PackZip.cpp for the implementation of this class
//

class CPackZipApp : public CWinApp
{
public:
	CPackZipApp();


// Overrides
public:
	virtual BOOL InitInstance();

// Implementation

public:
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CPackZipApp theApp;