// install.h : main header file for the PROJECT_NAME application
//

#pragma once
#include "../packzip/khlogger.h"
#include "../../pfs2.h"

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// CinstallApp:
// See install.cpp for the implementation of this class
//

class CinstallApp : public CWinApp
{
public:
	CinstallApp();

// Overrides
	public:
	virtual BOOL InitInstance();

// Implementation

	DECLARE_MESSAGE_MAP()
};

class CInstallPFSLog : public  PFS::COutLog
{
public:
	virtual void Print( const wchar_t* lpszFormat, ...)
	{
		WCHAR szBuffer[16*1024] = {0};
		va_list args;
		va_start(args, lpszFormat);
		HRESULT hr = wvsprintf( szBuffer, lpszFormat, args);
		va_end(args);
		if(SUCCEEDED(hr))
		{
			KHMsgLogW(szBuffer);
		}
		else
		{
			ASSERT(FALSE);
		}
	}
	virtual void VPrint( const wchar_t* info, va_list vl)
	{
	}
};

extern CinstallApp theApp;
extern CInstallPFSLog g_CInstallPFSLog;