// ZipFileViewer.h : ZipFileViewer Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������


// CZipFileViewerApp:
// �йش����ʵ�֣������ ZipFileViewer.cpp
//

class CZipFileViewerApp : public CWinApp
{
public:
	CZipFileViewerApp();


// ��д
public:
	virtual BOOL InitInstance();

// ʵ��
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CZipFileViewerApp theApp;