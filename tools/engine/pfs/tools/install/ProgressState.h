#pragma once

#include "InstallState.h"
#include "ProgressDlg.h"
#include "InstallThread.h"
#include "Config.h"
#include "../packzip/KHLogger.h"

class CProgressState : public CInstallState
{
	CProgressDlg	m_dialog;
	CInstallThread	m_installthread;
	CProgressThreadListener	m_listener;
	CProgressState() { }

public:
	enum { IDT_PROGRESS = 1002, };
	virtual void Show()
	{
		m_dialog.Create( m_parent);
		m_dialog.CenterWindow();
		m_dialog.ShowWindow( SW_SHOW);
		m_parent->SetTimer( IDT_PROGRESS, CConfig::GetInstance().m_bkChangeTime, NULL);
	}
	void StartInstall()
	{
		std::vector<std::wstring> zipfiles;
		for( CConfig::CPackageVector::iterator itor = CConfig::GetInstance().m_packages.begin(); 
			itor != CConfig::GetInstance().m_packages.end(); ++itor)
		{
			zipfiles.push_back( CInstallState::GetAppPathStr() + L"/" + itor->m_name);
		}
		std::wstring temp = m_parent->GetInstallPath();
		temp += L"\\";
		KHMsgLog_SetFilePathW( temp.c_str());
		PFS::CEnv::SetOutLogInstance( &g_CInstallPFSLog );

		m_installthread.SetPath( zipfiles, m_parent->GetInstallPath());
		m_listener.SetHWnd( m_dialog.GetSafeHwnd());
		m_installthread.SetListener( &m_listener);
		m_installthread.Start();
	}
	void EndInstall()
	{
		//m_installthread.Stop( 30000 );
		// 最好不要强行终止，否则可能会导致进程死锁而无法退出
		m_installthread.Stop( INFINITE ); 
		m_parent->KillTimer( IDT_PROGRESS);
	}
	DWORD GetError() { return m_listener.GetError(); }
	CInstallThread& GetInstallThread() { return m_installthread; }
	static CProgressState& GetInstance()
	{
		static CProgressState state;
		return state;
	}
};
