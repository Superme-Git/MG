// PictureViewer.h : PictureViewer Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������


// CPictureViewerApp:
// �йش����ʵ�֣������ PictureViewer.cpp
//

class CPictureViewerApp : public CWinApp
{
public:
	CPictureViewerApp();


// ��д
public:
	virtual BOOL InitInstance();

// ʵ��

public:
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CPictureViewerApp theApp;