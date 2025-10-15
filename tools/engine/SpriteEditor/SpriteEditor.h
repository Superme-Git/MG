

#pragma once

#ifndef __AFXWIN_H__
	#error "在包含此文件之前包含“stdafx.h”以生成 PCH 文件"
#endif

#include "resource.h"       



class CSpriteEditorView;

class CSpriteEditorApp : public CWinApp
{
private:
	CString m_ExePath;
public:
	afx_msg void OnAppAbout();
	virtual BOOL InitInstance();
	CSpriteEditorView *m_pView;

	virtual ~CSpriteEditorApp();
	CSpriteEditorApp();

	DECLARE_MESSAGE_MAP()

public:
	CString GetExePath() { return m_ExePath; }
	virtual BOOL OnIdle(LONG lCount);
	virtual bool OnFrameClose();

};

extern CSpriteEditorApp theApp;