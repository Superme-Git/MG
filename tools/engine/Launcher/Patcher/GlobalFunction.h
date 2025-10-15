#pragma once
#include <map>
#include <vector>
#include <algorithm>
#include <iostream>
#include <locale>
#include <sstream>
#include <fstream>
#include <assert.h>

#include "../../pfs/pfs.h"
#include "../../pfs/filesmetafile.h"
#include "../../pfs/setupmetafile.h"
#include "../../pfs/versionmetafile.h"

#define BRF_ROOT  L"/.brief"
#define RES_ROOT  L"/.cache"
#define RUN_ROOT  L"/.run"

class CUpdateMountManager
{
public:
	CUpdateMountManager() { m_bReadOnlyMount = true; m_bNeedUnmount = false; };
	~CUpdateMountManager();

	// 加一个参数hEvent使 GlobalFunction.cpp 不依赖于CUpdateManager
	bool MountPFS( const std::wstring& strRunDir, bool bReadOnly, 
		HANDLE hStopEvent, const PFSX::SetupMetaVector& setupMetas);

	// 为新增的pfs meta创建pfs包，最后将会在CommitPFS中集中Unmount
	bool MountPFSForNewAdded( const std::wstring& strRunDir,
		HANDLE hStopEvent, const PFSX::SetupMetaVector& newSetupMetas);

	// 提交修改的数据
	bool CommitPFS( HANDLE hStopEvent);

	bool UnmountPFS();

	// 得到资源文件的mount点
	PFSX::SetupMetaVector& GetSetupMetaVector()
	{
		return m_setupMetas;
	}

	static CUpdateMountManager& GetInstance()
	{
		static CUpdateMountManager _instance;
		return _instance;
	}

private:
	bool _MountForSetupMetas( const std::wstring& strRunDir, bool bReadOnly, 
		HANDLE hStopEvent, const PFSX::SetupMetaVector& setupMetas);

	// 生成 mount 目录的 .file.meta 文件的 hash 值
	PFS::FILE_DATA_HASH GetMetaFileHash( const std::wstring& mount);

private:
	bool m_bReadOnlyMount; // 以只读方式Mount
	bool m_bNeedUnmount;
	PFSX::SetupMetaVector m_setupMetas;
};

class CDownloadFiles
{
protected:
	PFS::CMetaInfoMap m_mimDownload;
	PFS::CMetaInfoMap m_mimDownloaded;
	PFS::CMetaInfoMap m_mimFailed;

	std::wstring m_strDir;
public:
	CDownloadFiles(std::wstring strDir)
		: m_strDir(strDir)
	{
	}

	~CDownloadFiles()
	{
	}

	// 批量下载Brief系统中的文件
	// 当下载失败的数目多于10个时，将返回 false.
	// 少于10个，即使失败数目不为0, 也将返回 true.
	bool Download( HANDLE hBreakEvent );

	PFS::CMetaInfoMap& GetDownloaded()
	{
		return m_mimDownloaded;
	}

	PFS::CMetaInfoMap& GetFailed()
	{
		return m_mimFailed;
	}

protected:
	bool DownloadOneFile( const std::wstring& fileName );
};

class CSetupMetasDownloader
{
private:
	typedef PFSX::SetupMetaVector SetupMetaVector;

	SetupMetaVector		m_smVector;
	std::wstring		m_strBaseUrl;
	std::wstring		m_strBaseDir;

public:
	CSetupMetasDownloader(
		const SetupMetaVector&	smVector,
		std::wstring strBaseUrl,
		std::wstring strBaseDir)
		: m_smVector( smVector )
		, m_strBaseUrl(strBaseUrl)
		, m_strBaseDir(strBaseDir)
	{
	}

	// 批量下载Brief系统中的文件
	// 当下载失败的数目多于10个时，将返回 false.
	// 少于10个，即使失败数目不为0, 也将返回 true.
	bool Download( HANDLE hBreakEvent );
};

extern bool DetectBreakEvent( HANDLE hEvent);

// 用CheckFileEx方式校验strDir目录中的文件
extern bool CheckFilesInFolder(const std::wstring& strDir,
							   PFS::CMetaInfoMap& rMimCache,// 需要校验的所有内容
							   HANDLE hStopEvent );

// 将strInputDir指定的目录中的文件，使用Translate方式打开并保存到strOutputDir指定的目录中
extern bool TranslateFilesInFolder(
							const std::wstring& strInputDir,
							const std::wstring& strOutputDir,
							PFS::CMetaInfoMap& rMimMerge, // 需要合并的所有内容
							PFS::CMetaInfoMap& rMimUpdate,// 统计成功合并的内容
							HANDLE hStopEvent,
							bool bPatch);

extern bool TranslateFilesInFolderEn(const std::wstring& strInputDir,
							  const std::wstring& strOutputDir,
							  PFS::CMetaInfoMap& rMimMerge, // 需要合并的所有内容
							  PFS::CMetaInfoMap& rMimUpdate,// 统计成功合并的内容
							  HANDLE hStopEvent );

extern bool TranslateFilesForLauncher(
							   const std::wstring&	strInputDir,
							   const std::wstring&	strOutputDir,
							   PFS::CMetaInfoMap&		rMimMerge, // 需要合并的所有内容
							   PFS::CMetaInfoMap&		rMimUpdate,// 统计成功合并的内容
							   HANDLE hStopEvent,
							   bool bPatch);

//
// summary:
//		比较两个MetaInfo是否不一样
//		这里只比较文件大小和Hash值
//;
extern bool IsEqualMetaInfo(const PFS::CMetaInfo& rLeft,const PFS::CMetaInfo& rRight);

// 
// summary:
//		对Server端的MetaInfo信息和用户本机版本的MetaInfo进行差异化判断
//		用于确定需要更新的列表内容
//
// parameters:
//		[in] rMinuend - 含全部信息的MetaInfo信息，Server端或Server与Local的差，用作被减数
//		[in] rSubtrahend - 用户本机的MetaInfo信息，Local或Cache中的信息，用作减数
//		[out] rDifference - 计算结果，包含要更新的MetaInfo信息
//
extern void SubtractMetaInfoMap(
						 const PFS::CMetaInfoMap& rMinuend,
						 const PFS::CMetaInfoMap& rSubtrahend,
						 PFS::CMetaInfoMap&		rDifference );

// 
// summary:
//		对上一版本的MetaInfo信息和当前版本的MetaInfo进行比较
//		用于确定当前版本需要删除的列表
//
// parameters:
//		[in] rLast - 上一版本的MetaInfo信息
//		[in] rCur  - 当前版本的MetaInfo信息
//		[out] rDelete - 计算结果，包含要删除的MetaInfo信息
//
extern void SubtractMetaInfoMapEx( const PFS::CMetaInfoMap& rLast, 
								  const PFS::CMetaInfoMap& rCur,
								  PFS::CMetaInfoMap&	rDelete );

//
// 输出MetaInfoMap中的文件名列表到流中
//
extern void DumpMetaInfoMap( const PFS::CMetaInfoMap& mim, std::wostream& oss );

// 读指定目录下的MetaInfo文件
extern bool ReadMetaInfoFile( const std::wstring& dir, PFS::CMetaInfoMap& mimFile );

extern bool ReadMetaInfoFile2( const std::wstring& dir, PFS::CMetaInfoMap& mimFile );

// 将MetaInfo文件写到指定目录下.files.meta文件中，版本1.1(不是1.0)
extern bool WriteMetaInfoFile( const std::wstring& dir, const PFS::CMetaInfoMap& mimFile );

// 将MetaInfo文件写到指定目录下.files2.meta文件中，版本1.1
extern bool WriteMetaInfoFile2( const std::wstring& dir, const PFS::CMetaInfoMap& mimFile );

// 从url指定的位置下载 .files.meta 文件，并读取内容到rMetaInfoMap对象中
extern bool DownloadMetaInfoFile( const std::wstring& url, PFS::CMetaInfoMap& rMetaInfoMap );

// 从url指定的位置下载 .files2.meta 文件，并读取内容到rMetaInfoMap对象中
extern bool DownloadMetaInfoFile2( const std::wstring& url, PFS::CMetaInfoMap& rMetaInfoMap );

// 删除指定的目录以及目录下的所有文件
extern bool DeleteCacheFolder( std::wstring strFolder );

// 创建隐藏文件夹
// 如果不存在就创建，存在就设为隐藏
extern bool CreateHiddenDirectory( const std::wstring& path);

// 游戏客户端程序所创建的Mutex名字(暂时没用)
// static const WCHAR SZ_GAME_CLIENT_MUTEX_NAME[] = L"MHZX_PRODUCT_MUTEX.{471CA900-DBAD-4d9e-9AD2-5FDC89B9B52E}";

// 安装版标志前缀
static const WCHAR SZ_INSTALLATION_NAME_PREFIX[] = L"MHZX_INSTALLATION_MUTEX.{471CA900-DBAD-4d9e-9AD2-5FDC89B9B52E}";

// 得到安置版本特有的Mutex名字 - 不同位置的安装版本，名字不同
extern LPCWSTR GetInstallationMutexName();

// 判断指定的Mutex对象是否已创建
extern bool NamedMutexExists( LPCWSTR szName );

// 如果日志文件超过上限,删除全部日志
extern bool PurgeLogFile( LPCWSTR lpszFileName, ULONGLONG ullFileSizeLimit );

// 将隐藏文件设为可见
// 因为隐藏文件已可写方式打开时会返回错误号5
extern bool ShowHiddenFile( const std::wstring& filename);

// 从url指定的位置读取 .setup.meta 文件，内容保存在 metas
extern bool ReadRemoteSetupMeta( const std::wstring& url, PFSX::SetupMetaVector& metas);

// 从 file 指定的文件读取meta信息，并读取内容到rMetaInfoMap对象中
extern bool ReadRemoteFilesMetaFile( const std::wstring& metafile, PFS::CMetaInfoMap& rMetaInfoMap );

// 处理不能删除的文件
extern void DealUnDeleteFiles(PFS::CMetaInfoMap& rDelete, const PFSX::SetupMetaVector& smvServer);

// 从URL中获取HostName或者IP地址。
extern bool GetHostNameFromUrl( LPCWSTR lpszUrl, std::wstring& hostname );

extern void DumpSetupMeta(const wchar_t* prefix, std::wostream& wos, const PFSX::SetupMetaVector& setupmeta );

// 根据服务器或者更新包的SetupMeta和本地的SetupMeta，计算新增项目和废弃项目
// 以及所有差异修改项目（含新增和修改）
//
// smvServer - 服务器或者Patch中的setupmeta。如果此为空，不做计算。
// smvLocal - 本地客户端中的setupmeta。计算结果将剔除无用项目(smvUseless)。
// smvDifference - smvServer 和 smvLocal的全部差异项目（新增项目和修改项目，但不包括本地需要删除的项目）
// smvNewAdded - smvDifference中的新增项目。
// smvUseless - smvLocal中的删除项目。即smvLocal存在，但smvServer中不存在的项目。
//
extern void AnalyzeSetupMetaUpdateInfo(const PFSX::SetupMetaVector& smvServer,
								PFSX::SetupMetaVector& smvLocal,
								PFSX::SetupMetaVector& smvDifference,
								PFSX::SetupMetaVector& smvNewAdded,
								PFSX::SetupMetaVector& smvUseless);

//
// 根据rMetaInfoMap中的FileSize2统计计算所有文件的大小总和。
//
extern PFS::fsize_t SumMetaInfoFileSize2( const PFS::CMetaInfoMap& rMetaInfoMap );

//
// 根据config.xml中配置的相关忽略更新的信息，从rMetaInfoMap中排除相应文件
//
extern void EraseMetaInfosByIgnoringConfig( PFS::CMetaInfoMap& rMetaInfoMap );

//
// 根据本地版本信息，获取服务器自动更新或补丁包自动更新模式的最新版本信息。
//
extern bool GetOnlineServerVersionInfo( const PFSX::CVersionInfo& rLocalVersionInfo, 
								 PFSX::CVersionInfo& rServerVersionInfo );