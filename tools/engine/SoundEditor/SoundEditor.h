// SoundEditor.h : SoundEditor Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������
#include "../engine/common/xptypes.h"

// CSoundEditorApp:
// �йش����ʵ�֣������ SoundEditor.cpp
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


// ��д
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
// ʵ��
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CSoundEditorApp theApp;