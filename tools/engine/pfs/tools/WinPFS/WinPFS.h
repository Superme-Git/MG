// WinPFS.h : WinPFS Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������


// CWinPFSApp:
// �йش����ʵ�֣������ WinPFS.cpp
//

class CWinPFSApp : public CWinApp
{
public:
	CWinPFSApp();


// ��д
public:
	virtual BOOL InitInstance();

// ʵ��
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CWinPFSApp theApp;