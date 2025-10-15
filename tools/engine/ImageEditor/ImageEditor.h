// ImageEditor.h : ImageEditor Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������


#include "..\engine/common\fileiomanager.h"
// CImageEditorApp:
// �йش����ʵ�֣������ ImageEditor.cpp
//

class CImageEditorApp : public CWinApp
{
public:
	CImageEditorApp();

	Nuclear::CFileIOManager* m_pFileIOManager;

	CString GetResPath() { return m_strResPath; }

private:
	CString m_strResPath;

// ��д
public:
	virtual BOOL InitInstance();

// ʵ��
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
	virtual int ExitInstance();
	virtual BOOL OnIdle(LONG lCount);
};

extern CImageEditorApp theApp;