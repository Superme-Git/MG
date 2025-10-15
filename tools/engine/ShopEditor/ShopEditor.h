// ShopEditor.h : main header file for the ShopEditor application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols


// CShopEditorApp:
// See ShopEditor.cpp for the implementation of this class
//

class CShopEditorApp : public CWinApp
{
public:
	CShopEditorApp();


// Overrides
public:
	virtual BOOL InitInstance();

// Implementation
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CShopEditorApp theApp;