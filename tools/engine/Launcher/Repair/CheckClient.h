// CheckClient.h : main header file for the PROJECT_NAME application
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// CCheckClientApp:
// See CheckClient.cpp for the implementation of this class
//

class CCheckClientApp : public CWinApp
{
public:
	CCheckClientApp();

// Overrides
	public:
	virtual BOOL InitInstance();

// Implementation

	DECLARE_MESSAGE_MAP()
};

extern CCheckClientApp theApp;

// checkclient 程序位于install directory 的 launcher 子目录下
// 因此，InstallDir为ModuleDir的父目录
// pszApp 返回 checkclient 自己相对于 install directory 的路径
extern LPCTSTR GetInstallDir( LPCTSTR& pszAppFile);