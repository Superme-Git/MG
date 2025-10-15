// EditMetaFile.h : main header file for the EditMetaFile application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols


// CEditMetaFileApp:
// See EditMetaFile.cpp for the implementation of this class
//

class CEditMetaFileApp : public CWinApp
{
public:
	CEditMetaFileApp();


// Overrides
public:
	virtual BOOL InitInstance();

// Implementation
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CEditMetaFileApp theApp;