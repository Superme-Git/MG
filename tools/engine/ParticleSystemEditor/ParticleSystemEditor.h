// ParticleSystemEditor.h : ParticleSystemEditor 应用程序的主头文件
//
#pragma once

#ifndef __AFXWIN_H__
	#error "在包含此文件之前包含“stdafx.h”以生成 PCH 文件"
#endif

#include "resource.h"       // 主符号

#include "..\engine\common\fileiomanager.h"
#include "..\engine\common\log.h"
#include "..\engine\common\pfsutil.h"

// CParticleSystemEditorApp:
// 有关此类的实现，请参阅 ParticleSystemEditor.cpp
//

class CParticleSystemEditorApp : public CWinApp
{
public:
	CParticleSystemEditorApp();

	Nuclear::CFileIOManager* m_pFileIOManager;

// 重写
public:
	virtual BOOL InitInstance();
	CString GetResPath() { return m_strResPath; }

public:
	bool m_bUpdateParticleSys;//true表示要更新，false表示不用更新了。

private:
	CString m_strResPath;

// 实现
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
	virtual BOOL OnIdle(LONG lCount);
	virtual int ExitInstance();
};

extern CParticleSystemEditorApp theApp;