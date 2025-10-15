#pragma once
// CCheckThread
#include <string>
#include "../Patcher/GlobalFunction.h"
#include "../../pfs/src/common.h"
#include "../../pfs/versionmetafile.h"
#include "../../pfs/setupmetafile.h"
#include "DownloadThread.h"
#include "Mutex.h"

#define WM_REPORTRESULT		(WM_USER+2000)
class CCheckThreadListener
{
	HWND		m_hWnd;
public:
	CCheckThreadListener() : m_hWnd( NULL) {}
	void SetHWnd( HWND hWnd) 
	{
		m_hWnd = hWnd;
	}
	bool OnNotify( WPARAM wParam, LPARAM lParam)
	{
		if( ::IsWindow( m_hWnd))
		{
			return !!::PostMessage( m_hWnd, WM_REPORTRESULT, wParam, lParam);
		}
		return true;
	}
};

class CCheckThread : public CWinThread
{
	DECLARE_DYNCREATE(CCheckThread)

public:
	CCheckThread();           // protected constructor used by dynamic creation
	virtual ~CCheckThread();

protected:
	bool DownloadMetaFile( );

	bool CheckAllFiles();

	bool CheckOneFile( const std::wstring& filename, const PFS::CMetaInfo& metainfo);

	bool Merge();

	void Destroy( BOOL bSuccess);

	void ReportResult( BOOL bSucess);

	virtual BOOL InitInstance();
	virtual int ExitInstance();

	// running and idle processing
	virtual int Run();

	// 得到需要检测文件列表
	bool GetCheckFilesMap();

	//// 从MetaMap中移除不检测的资源文件
	//bool EraseResourceFromMetaInfoMap( const PFSX::SetupMetaVector& unChecks, PFS::CMetaInfoMap& mim);

	//// 从MetaMap中移除不检测的文件
	//bool EraseUnCheckFromMetaInfoMap( PFS::CMetaInfoMap& mim);

	// 是否检查所有文件
	bool IsCheckAllFiles();

	// Destroy时写本地 .setup.meta 文件
	bool WriteLocalSetupMetaFile( const PFSX::SetupMetaVector& smvCheck);

public:
	void SetAppPath( LPCTSTR szRunDir, LPCTSTR szAppFile, bool bCheckExeFile, 
		bool bResetConfig, int serverType, const PFSX::SetupMetaVector& smvChecks)
	{
		m_apppath = szRunDir;
		m_appfile = L"/";
		m_appfile += szAppFile;
		std::replace( m_appfile.begin(), m_appfile.end(), L'\\', L'/');
		std::transform( m_appfile.begin(), m_appfile.end(), m_appfile.begin(), ::towlower);

		m_bCheckExeFile = bCheckExeFile;
		m_bResetConfig = bResetConfig;

		m_updateServerType = serverType;
		m_smvChecks = smvChecks;
	}

	bool Stop();

	void SetListener( CCheckThreadListener& listener)
	{
		m_listener = &listener;
	}

	BOOL SetMergeEvent()
	{
		return m_mergeEvent.SetEvent();
	}
	BOOL ResetMergeEvent()
	{
		return m_mergeEvent.ResetEvent();
	}
	bool IsMerging() const
	{
		return m_bMerging;
	}

protected:
	DECLARE_MESSAGE_MAP()

	std::wstring			m_apppath;
	std::wstring			m_appfile;
	PFS::CMetaInfoMap		m_mimServer;
	PFS::CMetaInfoMap		m_mimCached;
	PFS::CMetaInfoMap		m_mimUpdate;				// 更新成功列表
	PFSX::CVersionInfo		m_localVersion;
	PFSX::CVersionInfo		m_newVersionInfo;
	CDownloadThread*		m_downloadthread;
	MUTEX::CEvent					m_stopevent;
	CCheckThreadListener*	m_listener;

	bool					m_bCheckExeFile;			// 是否检测程序文件
	bool					m_bResetConfig;				// 是否改变配置

	int						m_updateServerType;			// 更新服务器类型

	PFSX::SetupMetaVector	m_smvChecks;				// 需要检测的项目列表
	PFSX::SetupMetaVector	m_smvServer;				// 服务器的 SetupMeta 列表

	volatile bool			m_bMerging;					// 是否正在合并
	MUTEX::CEvent			m_mergeEvent;				// 合并信号

public:
	static bool			s_checkend;
};


