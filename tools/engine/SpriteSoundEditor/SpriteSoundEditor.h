// SpriteSoundEditor.h : SpriteSoundEditor Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������


// CSpriteSoundEditorApp:
// �йش����ʵ�֣������ SpriteSoundEditor.cpp
//

class CSpriteSoundEditorApp : public CWinApp
{
public:
	CSpriteSoundEditorApp();
	~CSpriteSoundEditorApp();


// ��д
public:
	virtual BOOL InitInstance();

// ʵ��
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CSpriteSoundEditorApp theApp;