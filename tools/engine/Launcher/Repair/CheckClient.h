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

// checkclient ����λ��install directory �� launcher ��Ŀ¼��
// ��ˣ�InstallDirΪModuleDir�ĸ�Ŀ¼
// pszApp ���� checkclient �Լ������ install directory ��·��
extern LPCTSTR GetInstallDir( LPCTSTR& pszAppFile);