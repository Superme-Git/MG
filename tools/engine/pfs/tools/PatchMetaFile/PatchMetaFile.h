// PatchMetaFile.h : main header file for the PatchMetaFile application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols


// CPatchMetaFileApp:
// See PatchMetaFile.cpp for the implementation of this class
//

class CPatchMetaFileApp : public CWinApp
{
public:
	CPatchMetaFileApp();


// Overrides
public:
	virtual BOOL InitInstance();

// Implementation
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CPatchMetaFileApp theApp;