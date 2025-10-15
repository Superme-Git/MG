// LinkedObjectEditor.h : LinkedObjectEditor 应用程序的主头文件
//
#pragma once

#ifndef __AFXWIN_H__
	#error "在包含此文件之前包含“stdafx.h”以生成 PCH 文件"
#endif

#include "resource.h"       // 主符号

#include "..\engine\common\fileiomanager.h"
#include "..\engine\common\log.h"

// CLinkedObjectEditorApp:
// 有关此类的实现，请参阅 LinkedObjectEditor.cpp
//

class CLinkedObjectEditorApp : public CWinApp
{
public:
	CLinkedObjectEditorApp();


	Nuclear::CFileIOManager* m_pFileIOManager;
// 重写
public:
	virtual BOOL InitInstance();

// 实现
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
	virtual int ExitInstance();
	virtual BOOL OnIdle(LONG lCount);
};

extern CLinkedObjectEditorApp theApp;