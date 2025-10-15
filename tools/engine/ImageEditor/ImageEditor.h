// ImageEditor.h : ImageEditor 应用程序的主头文件
//
#pragma once

#ifndef __AFXWIN_H__
	#error "在包含此文件之前包含“stdafx.h”以生成 PCH 文件"
#endif

#include "resource.h"       // 主符号


#include "..\engine/common\fileiomanager.h"
// CImageEditorApp:
// 有关此类的实现，请参阅 ImageEditor.cpp
//

class CImageEditorApp : public CWinApp
{
public:
	CImageEditorApp();

	Nuclear::CFileIOManager* m_pFileIOManager;

	CString GetResPath() { return m_strResPath; }

private:
	CString m_strResPath;

// 重写
public:
	virtual BOOL InitInstance();

// 实现
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
	virtual int ExitInstance();
	virtual BOOL OnIdle(LONG lCount);
};

extern CImageEditorApp theApp;