

#pragma once

#ifndef __AFXWIN_H__
	#error "在包含此文件之前包含“stdafx.h”以生成 PCH 文件"
#endif

#include "resource.h"       
#include "..\engine\common\fileiomanager.h"
#include "..\engine\effect\effect.h"



class CEffectEditorApp : public CWinApp
{

private:
	CString m_ExePath;
	Nuclear::CFileIOManager *m_pFileIOMgr;
	Nuclear::EngineBase *m_pEngine;
public:
	afx_msg void OnBatchExport();

	virtual int ExitInstance();

	afx_msg void OnAppAbout();

	Nuclear::CFileIOManager* GetFileIOManager() { return m_pFileIOMgr; }


	virtual BOOL InitInstance();

	CString GetExePath() { return m_ExePath; }



	CString GetCfgFilePath() { return m_ExePath + L"\\EffectEditorCfg.ini"; }

	Nuclear::EngineBase* GetEngine() { return m_pEngine; }

	void SetEngine(Nuclear::EngineBase *pEB) { m_pEngine = pEB; }
	virtual ~CEffectEditorApp();

	CEffectEditorApp();

	DECLARE_MESSAGE_MAP()

};

extern CEffectEditorApp theApp;
#ifdef _DEBUG
extern CMemoryState msOld;
#endif

#ifndef EPSILON
#define EPSILON Nuclear::XP_EPSILON
#endif