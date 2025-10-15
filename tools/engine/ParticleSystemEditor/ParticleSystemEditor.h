// ParticleSystemEditor.h : ParticleSystemEditor Ӧ�ó������ͷ�ļ�
//
#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"       // ������

#include "..\engine\common\fileiomanager.h"
#include "..\engine\common\log.h"
#include "..\engine\common\pfsutil.h"

// CParticleSystemEditorApp:
// �йش����ʵ�֣������ ParticleSystemEditor.cpp
//

class CParticleSystemEditorApp : public CWinApp
{
public:
	CParticleSystemEditorApp();

	Nuclear::CFileIOManager* m_pFileIOManager;

// ��д
public:
	virtual BOOL InitInstance();
	CString GetResPath() { return m_strResPath; }

public:
	bool m_bUpdateParticleSys;//true��ʾҪ���£�false��ʾ���ø����ˡ�

private:
	CString m_strResPath;

// ʵ��
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
	virtual BOOL OnIdle(LONG lCount);
	virtual int ExitInstance();
};

extern CParticleSystemEditorApp theApp;