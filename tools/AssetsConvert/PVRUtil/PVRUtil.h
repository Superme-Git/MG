// PVRUtil.h : PROJECT_NAME Ӧ�ó������ͷ�ļ�
//

#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"		// ������


// CPVRUtilApp:
// �йش����ʵ�֣������ PVRUtil.cpp
//

class CPVRUtilApp : public CWinApp
{
public:
	CPVRUtilApp();

// ��д
	public:
	virtual BOOL InitInstance();

// ʵ��

	DECLARE_MESSAGE_MAP()
};

extern CPVRUtilApp theApp;