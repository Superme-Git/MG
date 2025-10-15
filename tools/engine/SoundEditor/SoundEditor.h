// SoundEditor.h : SoundEditor 应用程序的主头文件
//
#pragma once

#ifndef __AFXWIN_H__
	#error "在包含此文件之前包含“stdafx.h”以生成 PCH 文件"
#endif

#include "resource.h"       // 主符号
#include "../engine/common/xptypes.h"

// CSoundEditorApp:
// 有关此类的实现，请参阅 SoundEditor.cpp
//

class CSoundEditorApp : public CWinApp
{
private:
	CString m_stringPath;
	Nuclear::XPMazeColors m_mcs;

public:
	CSoundEditorApp();
	virtual ~CSoundEditorApp();
	void EnableSecToolBar(bool b);
	const Nuclear::XPMazeColors &GetMazeColors() const { return m_mcs; }
	void ResetMazeColors() { LoadMazeColors(m_mcs); }


// 重写
public:
	virtual BOOL InitInstance();

	CString GetCfgFilePath() 
	{ 
		CString filePath = L"";  
		::GetModuleFileName(NULL, filePath.GetBufferSetLength(MAX_PATH+1), MAX_PATH);  
		return filePath + L"\\SoundEditorCfg.ini"; 
	}
	CString GetExePath() { return m_stringPath; }

	static void SaveMazeColors(const Nuclear::XPMazeColors &mcs, size_t oldcolorcount);
	static void LoadMazeColors(Nuclear::XPMazeColors &mcs);
// 实现
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CSoundEditorApp theApp;