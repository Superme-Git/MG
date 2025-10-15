///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : UpdateManager.h
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	更新管理器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-11 Created.
///=============================================================================
#pragma once

#include "../../pfs/src/common.h"
#include "../../pfs/versionmetafile.h"
#include "../../pfs/setupmetafile.h"
#include "../../pfs/patchmetafile.h"
#include "GlobalFunction.h"
#include "Profile.h"

//////////////////////////////////////////////////////////////////////////
//
// 抽象的更新管理器，它实现缺省更新逻辑
//
class CUpdateManager
{
public:
	/// <summary>
	///		判断是否有新版本需要更新
	/// </summary>
	/// <param name="szRunDir">
	/// 	[in]程序运行目录
	/// </param>
	/// <param name="rHas">
	/// 	[out]当返回值为true时，rHas记录是否有新版本需要更新。
	///		当返回值为false时，rHas无意义。
	/// </param>
	/// <param name="rLocalValid">
	/// 	[out]rLocalValid为true时，本地版本有效。
	///		为false时，本地版本无效。
	/// </param>
	/// <param name="rAutoPatch">
	/// 	[out]rAutoPatch为true时，使用自动下载补丁包更新。
	///		为false时，普通更新方式。
	/// </param>
	/// <param name="rMergeCache">
	/// 	[out]rMergeCache为true时，需首先合并已经就绪的后台更新。然后重新检查新版本。
	/// </param>
	/// <return type="bool">
	/// 	检查新版本的动作是否成功完成。
	///		因其涉及到网络操作，当网络操作失败时，返回false，因此
	///		我们无法获知是否有新版本存在。
	/// </return>
	/// <remarks>
	///		注意：检查更新过程不要求Initialize，因此需要指定runDir和UpdateMode
	/// </remarks>
	virtual bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache );

	/// <summary>
	/// 	初始化游戏更新管理器
	/// </summary>
	/// <param name="szRunDir">
	/// 	[in]程序运行目录
	/// </param>
	/// <return type="bool">
	/// 	初始化游戏更新管理器是否成功。
	/// </return>
	/// <remarks>
	///		如果UpdateMode 为 UPDATE_MODE_FULL 或 UPDATE_MODE_BACKGROUND，
	///	那么更新时将只获取服务器最新文件列表，不进行比对，
	///	下载所有服务器文件到临时目录。
	///	对于FULL模式，替换本地文件，对于BACKGROUND模式下载完毕退出。
	/// 单独执行Update()才启动合并过程。
	/// FULL模式可帮助实现客户端零安装。
	/// </remarks>
	virtual bool Initialize( LPCTSTR szRunDir);
	
	/// <summary>
	/// 	更新管理器循环执行更新时循环一次前的初始化
	/// </summary>
	virtual bool BeginLoop( )
	{
		return true;
	}
	
	/// <summary>
	/// 	更新管理器循环执行更新时循环一次最后的处理
	/// </summary>
	virtual void EndLoop( bool bLoopSucceeded )
	{
	}

	/// <summary>
	/// 	更新管理器是否需要循环执行更新
	/// </summary>
	/// <remarks>
	///		对于一般的更新管理器，无需循环执行，因此，循环次数为1。
	///		而对于AUTOPATCH模式的更新管理器，需要根据补丁包数目决定是否需要循环更新。
	/// </remarks>
	/// <return type="bool">
	/// 	当前补丁包更新完毕后是否还有剩余补丁包需要继续更新。
	/// </return>
	virtual bool HasNextLoop()
	{
		return false;
	}

	/// <summary>
	/// 	检查需要更新的项目数量
	/// </summary>
	/// <param name="rDownload">
	/// 	[out]需要下载的项目数
	/// </param>
	/// <param name="rMerge">
	/// 	[out]需要合并的项目数(该值仅为估计值,因为实际下载时可能有失败的文件)
	/// </param>
	/// <param name="rDelete">
	/// 	[out]需要删除的项目数
	/// </param>
	/// <remarks>
	///		函数返回后, 如果rDownload为0，但是rMerge不为0,不执行Download，仅执行Merge.
	///		对于常规更新来说, 如果rDownload为0，但是rMerge不为0,需要执行Merge.
	///		对于后台更新来说, 不执行合并动作,因此rMerge总是为0,执行Merge函数,也不会合并任何文件.
	/// </remarks>
	/// <return type="bool">
	/// 	检查需要更新的项目数量过程是否成功
	/// </return>
	virtual bool CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete );

	/// <summary>
	/// 	下载更新包
	/// </summary>
	/// <remarks>
	///		只有CheckDifference中的rDownload不为0时需要执行该动作.
	/// </remarks>
	/// <return type="bool">
	/// 	下载更新包过程是否成功
	/// </return>
	virtual bool Download( );

	/// <summary>
	/// 	校验缓存文件
	/// </summary>
	/// <remarks>
	///		只有m_mimCached不为空时执行该过程.
	///		校验失败的文件将被删除,并重新下载.
	///		全部校验成功的文件将统计到rMerge中.
	/// </remarks>
	/// <return type="bool">
	/// 	校验缓存文件过程是否成功
	/// </return>
	virtual bool Verify( );

	/// <summary>
	/// 	清理客户端中废弃的文件
	/// </summary>
	/// <remarks>
	///		只有CheckDifference中的rDelete不为0时需要执行该动作.
	/// </remarks>
	/// <return type="bool">
	/// 	是否成功
	/// </return>
	virtual bool Purge( );

	/// <summary>
	/// 	用下载到的更新包更新游戏客户端
	/// </summary>
	/// <remarks>
	///		对于常规更新来说, 如果rDownload为0，但是rMerge不为0,需要执行Merge.
	///		对于后台更新来说, 不执行合并动作,因此rMerge总是为0,执行Merge函数,也不会合并任何文件.
	/// </remarks>
	/// <return type="bool">
	/// 	更新客户端过程是否成功
	/// </return>
	virtual bool Merge( );

	/// <summary>
	/// 	提交更新的数据
	/// </summary>
	/// <remarks>
	///		在只需要提交数据而不必unmount的时候使用
	/// </remarks>
	virtual bool Commit();

	/// <summary>
	/// 	释放更新管理器内部资源
	/// </summary>
	/// <remarks>
	///		该过程为Initialize对称过程。
	///		只要Initialize()执行成功，一定要在最后调用Destory().
	///		否则会导致资源泄露和更新过程不完整。
	/// </remarks>
	virtual bool Destroy( bool bSucceeded );

	// 是否使用补丁包
	virtual bool IsUsePatch()
	{
		return false;
	}

	void SetPatchPath( const std::wstring& patchpath)
	{
		m_patchpath = patchpath;
	}

	void SetPatchHash( const PFS::FILE_DATA_HASH& patchhash)
	{
		m_patchhash = patchhash;
	}

	void SetUpdateServerType( UPDATE_SERVER_TYPE type)
	{
		m_updateServerType = type;
	}

	// 设置更新补丁包信息列表（允许swap）
	virtual void SetPatchInfoVector( PFSX::CPatchInfoVector& rPatchInfos )
	{
		// 只有子类CUpdateManagerAutoPatch需要实现
	}

	/// <summary>
	/// 	获取本地版本信息
	/// </summary>
	const PFSX::CVersionInfo& GetLocalVersion() const
	{
		return m_localVersion;
	}

	/// <summary>
	/// 	获取HasNewVersion()方法取得的服务器版本信息
	/// </summary>
	/// <remarks>
	///		该方法没有加多线程保护,所以必须在更新线程里调用
	/// </remarks>
	const PFSX::CVersionInfo& GetServerVersion() const
	{
		return m_newVersionInfo;
	}

	/// <summary>
	/// 	设置其他UpdateManager::HasNewVersion()方法取得的服务器版本信息到本UpdateManager中。
	///		切换更新模式时需要。
	/// </summary>
	void SetServerVersion(const PFSX::CVersionInfo& rNewVersionInfo)
	{
		m_newVersionInfo = rNewVersionInfo;
	}

	/// <summary>
	/// 	虚拟化析构函数，否则会导致子类的数据成员被泄露
	/// </summary>
	virtual ~CUpdateManager()
	{
	}

public:
	/// <summary>
	/// 	触发中止信号
	/// </summary>
	static void TriggerBreakEvent();

	/// <summary>
	/// 	检测中止信号
	/// </summary>
	/// <return type="bool">
	/// 	中止信号如果被置，返回true，否则返回false.
	/// </return>
	static bool DetectBreakEvent();

	/// <summary>
	/// 	更新完成后是否需要重起
	/// </summary>
	/// <return type="bool">
	/// 	true - 需要重起，false - 不需要重起
	/// </return>
	static bool IsNeedReboot();
	
	// 计算是否需要更新Launcher和Patcher相关的文件
	static bool IsNeedUpdateLauncher( const PFS::CMetaInfoMap& mim);

protected:
	/// <summary>
	/// 	保护的构造函数，避免外部构造
	/// </summary>
	/// <param name="UpdateMode">
	/// 	[in]缺省更新 或 全部无条件更新 或 后台更新模式
	/// </param>
	CUpdateManager(UPDATE_MODE updateMode);

	// 删除本地文件
	bool DeleteLocalFiles();

	// 获取版本信息提供的服务器url
	std::wstring GetVersionUrl(const PFSX::CVersionInfo& rVersionInfo, bool bUseConfigIfNeeded = false, bool bUseDefault = false ) const;
	
	bool IsBackground()
	{
		return m_UpdateMode == UPDATE_MODE_BACKGROUND;
	}

protected:
	std::wstring m_url;							// 下载url
	std::wstring m_runDir;						// 游戏目录
	PFSX::CVersionInfo m_localVersion;			// 本地版本信息
	PFSX::CVersionInfo m_newVersionInfo;		// 最新版本信息
	std::wstring m_cacheDir;					// 临时目录
	//std::wstring m_p2spDir;						// p2sp下载目录
	PFS::CMetaInfoMap m_mimDownload;			// 需要下载的项目
	PFS::CMetaInfoMap m_mimDownloadFailed;		// 下载失败的项目
	PFS::CMetaInfoMap m_mimCached;				// 缓存中已有的文件
	PFS::CMetaInfoMap m_mimUpdate;				// 成功合并的文件
	PFS::CMetaInfoMap m_mimDelete;				// 本地需要删除的文件

	PFSX::SetupMetaVector m_smvLocal;			// 本地 SetupMeta 信息
	PFSX::SetupMetaVector m_smvServer;			// 服务器 SetupMeta 信息

	static bool s_bNeedReboot;	// 更新程序需要重起然后继续更新(例如当Launcher完成自我更新时)

protected:
	UPDATE_MODE			m_UpdateMode;	// 更新模式
	std::wstring		m_patchpath;		// 补丁包路径
	std::wstring		m_patchmd5;			// 补丁包md5值
	PFS::FILE_DATA_HASH m_patchhash;		// 补丁包hash值，补丁包自动更新时需要
	UPDATE_SERVER_TYPE	m_updateServerType;	// 更新服务器类型

protected:
	volatile static HANDLE s_hBreakEvent;	// 中止信号
};

/// <summary>
/// 	获取更新管理器的单体实例
/// </summary>
/// <param name="eUpdateMode">
/// 	[in]缺省更新 或 全部无条件更新 或 后台更新模式
/// </param>
/// <return type="CUpdateManager &">
/// 	更新管理器的单体实例
/// </return>
extern CUpdateManager& GetUpdateManager(UPDATE_MODE eUpdateMode);

extern LPCWSTR _UpdateModeName( int mode );
