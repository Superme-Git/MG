// SoundEditor.h : SoundEditor Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������
#include "..\engine\common\pfsutil.h"
#include "..\engine\map\ppathmap.h"


// CSoundEditorApp:
// �йش����ʵ�֣������ SoundEditor.cpp
//

class CSoundEditorApp : public CWinApp
{
private:
	CString m_stringPath;

	Nuclear::PFSLog m_pfsLog;
	Nuclear::CFileIOManager *m_pFileIOMan;
	Nuclear::SubMap::PPathMap *m_pPathMap;

public:
	CSoundEditorApp();
	void EnableSecToolBar(bool b);


// ��д
public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();

	CString GetExePath() { return m_stringPath; }
// ʵ��
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CSoundEditorApp theApp;