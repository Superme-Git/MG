// LinkedObjectEditor.h : LinkedObjectEditor Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������

#include "..\engine\common\fileiomanager.h"
#include "..\engine\common\log.h"

// CLinkedObjectEditorApp:
// �йش����ʵ�֣������ LinkedObjectEditor.cpp
//

class CLinkedObjectEditorApp : public CWinApp
{
public:
	CLinkedObjectEditorApp();


	Nuclear::CFileIOManager* m_pFileIOManager;
// ��д
public:
	virtual BOOL InitInstance();

// ʵ��
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
	virtual int ExitInstance();
	virtual BOOL OnIdle(LONG lCount);
};

extern CLinkedObjectEditorApp theApp;