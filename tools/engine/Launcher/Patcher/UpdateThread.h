#pragma once
#include "Profile.h"

// 更新线程
class CUpdateThread : public CWinThread
{
	DECLARE_DYNCREATE(CUpdateThread)

	CUpdateThread();
	~CUpdateThread();
public:

	// thread initialization
	virtual BOOL InitInstance();

	// running and idle processing
	virtual int Run();

	void CheckVersion(UPDATE_MODE mode);
	void Update(UPDATE_MODE mode);

	void Stop();

	BOOL IsUpdatingInProgress() const
	{
		return m_bUpdatingInProgress;
	}

	BOOL IsBKUpdatingInProgress() const
	{
		return m_bBKUpdatingInProgress;
	}

	BOOL IsMerging() const
	{
		return m_bMerging;
	}

	void SetPatchPath( const std::wstring& patchpath)
	{
		m_patchpath = patchpath;
	}

	const std::wstring& GetPatchPath() const
	{
		return m_patchpath;
	}

	void SetUpdateServerType( UPDATE_SERVER_TYPE type)
	{
		m_updateServerType = type;
	}

	UPDATE_SERVER_TYPE GetUpdateServerType() const
	{
		return m_updateServerType;
	}

	UPDATE_MODE GetUpdateMode() const
	{
		return m_mode;
	}

	void SetRebooted( BOOL bRebooted )
	{
		LPTSTR lpcmdline = _tcsstr( AfxGetApp()->m_lpCmdLine, L"-r ");
		if( lpcmdline != NULL )
		{
			// 从命令行中清除重启标志。
			_wcsnset_s( lpcmdline, -1, L' ', 2 );
		}

		m_bRebootedFlag = bRebooted;
	}

	BOOL GetRebooted() const
	{
		return m_bRebootedFlag;
	}

	const PFSX::CVersionInfo& GetServerVersion() const
	{
		return m_serverVersion;
	}

	BOOL SetMergeEvent();
	BOOL ResetMergeEvent();

	// See Implementation
	static bool IsNeedReboot();

protected:
	BOOL _DoCheckVersion( UPDATE_MODE mode, BOOL& bHasNewVersion, BOOL& bLocalValid, BOOL& bAutoPatch, BOOL& bMergeCache );
	BOOL _DoUpdate( UPDATE_MODE mode );

protected:
	volatile BOOL m_bUpdatingInProgress;
	volatile BOOL m_bBKUpdatingInProgress;	// 后台更新是否在进行
	volatile BOOL m_bMerging;	// 是否正在合并

	HANDLE m_hStopEvent;
	HANDLE m_hCheckEvent;
	HANDLE m_hUpdateEvent;
	HANDLE m_hBKCheckEvent;
	HANDLE m_hBKUpdateEvent;

	HANDLE m_hMergeEvent;					// 合并信号

	UPDATE_MODE m_mode;
	std::wstring m_patchpath;	// 补丁包路径
	BOOL m_bRebootedFlag;				// 是否为重启后的进程
	UPDATE_SERVER_TYPE	m_updateServerType;	// 更新服务器类型
	PFSX::CVersionInfo	m_serverVersion; // 服务器端最新版本号
};
