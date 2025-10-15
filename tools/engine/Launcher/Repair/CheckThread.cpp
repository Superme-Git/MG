// CheckThread.cpp : implementation file
//

#include "stdafx.h"
#include "CheckClient.h"
#include "CheckThread.h"
#include "DownloadFileList.h"
#include "../Patcher/GlobalNotification.h"
#include "../Patcher/Profile.h"
#include "../Patcher/TextManager.h"
#include "../Patcher/TextID.h"

// CCheckThread
IMPLEMENT_DYNCREATE(CCheckThread, CWinThread)

bool CCheckThread::s_checkend = false;

CCheckThread::CCheckThread()
: m_downloadthread( NULL)
, m_stopevent( TRUE)
, m_listener( NULL)
, m_bCheckExeFile( false)
, m_bResetConfig( false)
, m_updateServerType( 0)
, m_bMerging( false)
, m_mergeEvent( TRUE)
{
	m_mergeEvent.SetEvent();
}

CCheckThread::~CCheckThread()
{
}

BOOL CCheckThread::InitInstance()
{
	// TODO:  perform and per-thread initialization here
	return TRUE;
}

int CCheckThread::ExitInstance()
{
	// TODO:  perform any per-thread cleanup here
	return CWinThread::ExitInstance();
}

BEGIN_MESSAGE_MAP(CCheckThread, CWinThread)
END_MESSAGE_MAP()
//
//void SubtractSetupMetaByName( const PFSX::SetupMetaVector& rMinuend, 
//							 const PFSX::SetupMetaVector& rSubtrahend, PFSX::SetupMetaVector& rDifference )
//{
//	for( PFSX::SetupMetaVector::const_iterator i = rMinuend.begin(); i!=rMinuend.end(); ++i)
//	{
//		const PFSX::SetupMetaInfo& smi = *i;
//		PFSX::SetupMetaVector::const_iterator iFind = std::find( rSubtrahend.begin(), rSubtrahend.end(), smi);
//		if( iFind == rSubtrahend.end())
//		{
//			rDifference.push_back( smi);
//		}
//	}
//}

// CCheckThread message handlers
int CCheckThread::Run()
{
	BOOL bSuccess = FALSE;
	do
	{
		if( !DownloadMetaFile())
		{
			break;
		}
		if( !CUpdateMountManager::GetInstance().MountPFS( m_apppath, false, 
			m_stopevent.GetHandle(), m_smvChecks))
		{
			break;
		}
		// 下载线程启动
		m_downloadthread = (CDownloadThread*)AfxBeginThread( RUNTIME_CLASS(CDownloadThread),
			THREAD_PRIORITY_BELOW_NORMAL, 0, CREATE_SUSPENDED, NULL);
		if ( NULL == m_downloadthread)
		{
			upd::SetLastError( ::GetLastError(), GlobalGetText( ERR_START_DOWNLOAD).c_str() );
			break;
		}
		m_downloadthread->m_bAutoDelete = FALSE;
		m_downloadthread->ResumeThread();

		// 检查所有文件
		if( !CheckAllFiles())
		{
			// CheckAllFiles 出错的唯一条件是用户取消，这时要让 download 线程退出
			CDownloadFileList::GetInstance().Clear();
		}
		s_checkend = true;		// 原子操作不用线程保护
		GlobalNotifyStart( (int)CDownloadFileList::GetInstance().Size());

		// push一个空文件名使download线程退出
		CDownloadFileList::GetInstance().Push( std::make_pair( L"", PFS::CMetaInfo()));
		::WaitForSingleObject( m_downloadthread->m_hThread, INFINITE);
		if( m_downloadthread->m_downloaderr)
		{
			// 文件下载出错
			m_downloadthread->Delete();
			m_downloadthread = NULL;
			break;
		}
		m_downloadthread->Delete();
		m_downloadthread = NULL;

		s_checkend = false;
		GlobalNotifyEnd();

		// 下载完成，开始合并
		if( !Merge())
		{
			break;
		}

		bSuccess = TRUE;
	}
	while( 0);

	::WaitForSingleObject( m_mergeEvent.GetHandle(), INFINITE);
	m_bMerging = TRUE;

	// 清理资源
	Destroy( bSuccess);
	ReportResult( bSuccess && !m_stopevent.IsBreak());
	return 0;
}

bool CCheckThread::DownloadMetaFile( )
{
	GlobalNotify( GlobalGetText( TIP_START_CHECK_FILE).c_str() );

	if( !PFSX::GetLocalVersion( m_apppath, m_localVersion) )
	{
		// .version.meta 文件不存在也允许验证
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_VERSION) );
		//return false;
	}

	std::wstring url;
	const PFSX::VersionUpdateInfo *pUpdateInfo = m_localVersion.GetUpdateInfoByType( PFSX::VERSION_DEFAULT);
	if( pUpdateInfo )
	{
		url = pUpdateInfo->Url;
	}
	else
	{
		CProfile::GetInstance().GetServerUrl( url, UPDATE_MODE_DEFAULT);
	}
	
	// 在无法获取服务器版本信息的情况下，不执行任何操作
	if( !PFSX::GetRemoteVersion( url, m_newVersionInfo) )
	{
		if( PFS::EC_INVALID_VERSION_INFO == PFS::CEnv::GetLastError())
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_GET_SERVER_VERSION) );
		}
		else
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_CONNECT_TO_SERVER) );
		}
		return false;
	}
	
	// 在下载之前，首先根据更新服务期类型保存版本信息文件。
	int type = m_updateServerType*PFSX::VERSION_COUNT ;
	if( NULL == m_newVersionInfo.GetUpdateInfoByType( type))
	{
		// 尝试取另一种服务器的类型获取更新URL
		type = (!m_updateServerType)*PFSX::VERSION_COUNT;
		if( NULL == m_newVersionInfo.GetUpdateInfoByType( type ) )
		{
			// 如果指定type不存在，则使用默认type保证能继续更新
			type = PFSX::VERSION_DEFAULT;
		}
	}

	pUpdateInfo = m_newVersionInfo.GetUpdateInfoByType( type);
	if( pUpdateInfo == NULL)
	{
		return false;
	}

	// 这里得用服务器的version.meta的url作为更新的url，因为本地的version.meta不能保证为最新
	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_META_FILE).c_str() );

	m_mimServer.clear();
	if( m_bCheckExeFile)
	{
		if( !DownloadMetaInfoFile2( pUpdateInfo->Url, m_mimServer))
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_DOWNLOAD_ROOT_METAFILE) );
			return false;
		}
	}
	for( PFSX::SetupMetaVector::const_iterator i = m_smvChecks.begin(), e=m_smvChecks.end(); i!=e; ++i)
	{
		const std::wstring& mount = i->GetName();

		GlobalNotify( GlobalFormatText( TIP_DOWNLOAD_CHILD_META_FILE, mount.c_str()).c_str() );

		PFS::CMetaInfoMap mim;
		if( !DownloadMetaInfoFile2( pUpdateInfo->Url+mount, mim))
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_DOWNLOAD_CHILD_METAFILE, mount.c_str()) );
			return false;
		}
		for( PFS::CMetaInfoMap::iterator iMim=mim.begin(), eMim=mim.end(); iMim!=eMim; ++iMim)
		{
			m_mimServer.insert( std::make_pair( mount+iMim->first, iMim->second));
		}
	}

	if( m_stopevent.IsBreak())
	{
		return false;
	}

	// 下载服务器的 .setup.meta 文件
	if( !ReadRemoteSetupMeta( pUpdateInfo->Url, m_smvServer))
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_GET_SERVER_SETUPMETA) );
		return false;
	}
	// 对服务器的setupmeta进行排序。
	std::sort( m_smvServer.begin(), m_smvServer.end() );

	std::wstring briefDir;	// Brief目录
	briefDir = m_apppath + BRF_ROOT;
	// 在下载之前，首先根据更新模式保存版本信息文件。
	CreateHiddenDirectory( briefDir);
	if( !PFSX::WriteVersionMetaFile( briefDir + PFSX::VERSION_META_NAME, m_newVersionInfo, type) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_SAVE_NEW_VERSION) );
		return false;
	}
	// 先写一个假的.files.meta文件以便 brief 文件系统 mount 成功
	PFS::CMetaInfoMap mimTemp;
	mimTemp.insert( std::make_pair( PFSX::VERSION_META_NAME, PFS::CMetaInfo()));
	if( !WriteMetaInfoFile( briefDir, mimTemp))
	{
		return false;
	}
	if( !PFS::CEnv::Mount( BRF_ROOT, briefDir, PFS::FST_BRIEF, PFS::MT_READONLY) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT, BRF_ROOT, briefDir.c_str()) );
		return false;
	}
	std::wstring cacheDir = m_apppath + RES_ROOT;
	CreateHiddenDirectory( cacheDir);
	if( !PFS::CEnv::Mount( RES_ROOT, cacheDir, PFS::FST_NATIVE, PFS::MT_WRITABLE ) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT, RES_ROOT, ( m_apppath + RES_ROOT).c_str()) );
		return false;
	}

	return true;
}

//// 从MetaMap中移除不检测的资源文件
//bool CCheckThread::EraseResourceFromMetaInfoMap( const PFSX::SetupMetaVector& unChecks, PFS::CMetaInfoMap& mim)
//{
//	for( PFSX::SetupMetaVector::const_iterator i=unChecks.begin(), e=unChecks.end(); i!=e; ++i)
//	{
//		std::wstring beginStr = i->GetName();
//		if( beginStr.empty())
//		{
//			continue;
//		}
//		std::wstring endStr = beginStr;
//		beginStr += L"/";
//		PFS::CMetaInfoMap::iterator rBegin = mim.lower_bound( beginStr);
//		if( rBegin == mim.end())
//		{
//			continue;
//		}
//		endStr[endStr.size()-1]++;
//		endStr += L"/";
//		PFS::CMetaInfoMap::iterator rEnd = mim.lower_bound( endStr);
//		mim.erase( rBegin, rEnd);
//	}
//	
//	return true;
//}
//
//// 从MetaMap中移除不检测的文件
//bool CCheckThread::EraseUnCheckFromMetaInfoMap( PFS::CMetaInfoMap& mim)
//{
//	if( m_bCheckExeFile)
//	{
//		PFSX::SetupMetaVector smvUnChecks;
//		SubtractSetupMetaByName( m_smvServer, m_smvChecks, smvUnChecks);
//		EraseResourceFromMetaInfoMap( smvUnChecks, mim);
//	}
//	else
//	{
//		PFS::CMetaInfoMap temp;
//		temp.swap( mim);
//		for( PFSX::SetupMetaVector::const_iterator i=m_smvChecks.begin(), e=m_smvChecks.end(); i!=e; ++i)
//		{
//			std::wstring beginStr = i->GetName();
//			if( beginStr.empty())
//			{
//				continue;
//			}
//			std::wstring endStr = beginStr;
//			beginStr += L"/";
//			PFS::CMetaInfoMap::iterator rBegin = temp.lower_bound( beginStr);
//			if( rBegin == temp.end())
//			{
//				continue;
//			}
//			endStr[endStr.size()-1]++;
//			endStr += L"/";
//			PFS::CMetaInfoMap::iterator rEnd = temp.lower_bound( endStr);
//
//			mim.insert( rBegin, rEnd);
//		}
//
//	}
//	return true;
//}

// 得到需要检测文件列表
bool CCheckThread::GetCheckFilesMap()
{
	m_mimServer.erase( m_appfile);					// 删掉自身
	m_mimServer.erase( PFSX::VERSION_META_NAME);	// 最后才处理 .version.meta 文件
	m_mimServer.erase( PFSX::SETUP_META_NAME);		// 最后才处理 .setup.meta 文件

	if( !m_bResetConfig)
	{
		// 09.7.18 wtf目录下文件现在都不能排除，以后可能会特殊处理一些文件
		//PFS::CMetaInfoMap::iterator rBegin = m_mimServer.lower_bound( L"/wtf/" );
		//if( rBegin == m_mimServer.end() )
		//{
		//	return true;
		//}
		//PFS::CMetaInfoMap::iterator rEnd = m_mimServer.lower_bound( L"/wtg/" );
		//// rEnd may be at the end.
		//m_mimServer.erase( rBegin, rEnd);
	}

	//EraseUnCheckFromMetaInfoMap( m_mimServer);
	
	return true;
}

bool CCheckThread::CheckAllFiles()
{
	GetCheckFilesMap();

	PFS::CMetaInfoMap mimCached;
	PFS::CEnv::GetMetaInfos( RES_ROOT, mimCached);

	int count = (int)m_mimServer.size();
	GlobalNotify( GlobalFormatText( TIP_CHECK_FILE_COUNT, count).c_str());

	GlobalNotifyStart( count);

	int checkCount = 0;
	PFS::CMetaInfoMap::iterator iEnd = m_mimServer.end();
	for( PFS::CMetaInfoMap::iterator itor = m_mimServer.begin(); itor != iEnd; ++itor)
	{
		if( m_stopevent.IsBreak())
		{
			return false;
		}

		const std::wstring& filename = itor->first;
		GlobalNotifyStep( filename);
		if( !CheckOneFile( filename, itor->second))
		{
			PFS::CMetaInfoMap::iterator iCache = mimCached.find( filename);
			if( (iCache == mimCached.end()) || !IsEqualMetaInfo( iCache->second, itor->second))
			{
				// 缓存中没有或者缓存中文件不是最新才下载
				CDownloadFileList::GetInstance().Push( *itor);
			}
			m_mimCached.insert( *itor);
		}
		else
		{
			PFS::CEnv::AddFileMetaInfo( RUN_ROOT + filename, itor->second);
		}

		if( checkCount++ >= 20)
		{
			checkCount = 0;
			Sleep( 1);	// 让主窗口处理一下堆积的消息
		}
	}
	GlobalNotifyEnd();

	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_UPDATE_FILE).c_str() );
	return true;
}

bool CCheckThread::CheckOneFile( const std::wstring& filename, const PFS::CMetaInfo& metainfo)
{
	std::wstring mountroot = RUN_ROOT;

	PFS::CFile file;
	if( !file.Open( mountroot + filename, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}

	PFS::helper::CMD5Hash md5;

	const size_t sizeBuffer = 4096;
	char buffer[sizeBuffer] = {0};
	while( size_t s = file.Read( buffer, sizeBuffer))
	{
		if( PFS::FILE_SIZE_ERROR == s)
			return false;
		md5.Update( buffer, s);
	}

	PFS::FILE_DATA_HASH localMD5 = 0;
	md5.Final( localMD5);

	// 这里不能判断
	if( metainfo.GetFileHash() != localMD5 || 
		metainfo.GetFileSize() != file.GetSize() )
	{
		return false;
	}
	return true;
}

// GlobalFunction.cpp
extern int HandleDecodeFileError( bool bPatch, std::wstring path );

bool CCheckThread::Merge()
{
	if( m_stopevent.IsBreak())
	{
		return false;
	}

	PFS::CEnv::Unmount( BRF_ROOT);
	PFS::CEnv::Unmount( RES_ROOT);

	m_mimUpdate.clear();
	if( !TranslateFilesInFolderEn( m_apppath + RES_ROOT, m_apppath, m_mimCached, m_mimUpdate, 
		m_stopevent.GetHandle()) )
	{
		GlobalNotifyErrorInfo( upd::GetLastError(), GlobalFormatText( ERR_MERGE_FILE_COUNT, m_mimCached.size(), 
			m_mimCached.size() - m_mimUpdate.size()).c_str() );
		return false;
	}
	else
	{
		GlobalNotify( GlobalFormatText( TIP_MERGE_FILE_COMPLETE, m_mimCached.size()).c_str());
		return true;
	}
}

void CCheckThread::Destroy( BOOL bSuccess)
{
	// unmount
	GlobalNotify( GlobalGetText( TIP_DELETE_CACHE_FOLDER).c_str() );
	
	PFS::CEnv::Unmount( RES_ROOT);
	PFS::CEnv::Unmount( BRF_ROOT);
	// 删除 /.brief 目录
	std::wstring strTemp = m_apppath + BRF_ROOT;
	std::replace( strTemp.begin(), strTemp.end(), L'/', L'\\');
	DeleteCacheFolder( strTemp);
	
	if( !CUpdateMountManager::GetInstance().CommitPFS( m_stopevent.GetHandle()))
	{
		bSuccess = false;
	}
	if( !CUpdateMountManager::GetInstance().UnmountPFS( ))
	{
		bSuccess = false;
	}
	if( !WriteLocalSetupMetaFile( CUpdateMountManager::GetInstance().GetSetupMetaVector()) )
	{
		GlobalNotify( GlobalGetText( ERR_SAVE_LOCAL_SETUPMETA).c_str() );
		bSuccess = false;
	}

	if( bSuccess)	// 如果不成功的话，暂时不删除 /.cache目录下文件，用于断点续传
	{
		// 只有全部文件都校验成功才更新版本文件
		if( IsCheckAllFiles())
		{
			if( !PFSX::WriteVersionMetaFile( m_apppath + PFSX::VERSION_META_NAME, m_newVersionInfo) )
			{
				// 很不幸，只有版本信息文件更新失败！
				upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_SAVE_NEW_VERSION) );
				//GlobalNotifyStep( PFSX::VERSION_META_NAME.c_str(), PFS::CEnv::GetLastError() );
			}
		}

		// 删除临时目录
		std::wstring cacheDir = m_apppath + RES_ROOT;
		std::replace( cacheDir.begin(), cacheDir.end(), L'/', L'\\');
		DeleteCacheFolder( cacheDir );

		GlobalNotify( GlobalGetText( TIP_REPAIR_COMPLETE).c_str() );
	}
	else
	{
		if( !m_mimCached.empty() && m_mimCached.size() != m_mimUpdate.size())
		{
			// 下面两个GlobalNotify只记录到日志文件不显示在界面所以直接写在程序里
			GlobalNotify(L"版本验证失败，标记版本信息无效！");

			m_localVersion.m_bValid = false;
			if( !PFSX::WriteVersionMetaFile( m_apppath + PFSX::VERSION_META_NAME, m_localVersion) )
			{
				std::wostringstream oss;
				oss<< L"错误: 更新版本文件 "<< m_apppath + PFSX::VERSION_META_NAME << L" 失败!" ;
				GlobalNotify( oss.str().c_str());
			}
		}
		GlobalNotify( GlobalGetText( ERR_REPAIR_RESULT).c_str() );
	}
}

bool CCheckThread::Stop()
{
	upd::SetLastError( SC_USER_CANCELED);
	m_stopevent.SetEvent();
	if( m_downloadthread)
	{
		// 让下载线程退出
		CDownloadFileList::GetInstance().Clear();
	}
	return true;
}

void CCheckThread::ReportResult( BOOL bSucess)
{
	if( m_listener)
	{
		m_listener->OnNotify( bSucess, IsCheckAllFiles());
	}
}

// 是否检查所有文件
bool CCheckThread::IsCheckAllFiles()
{
	if( !m_bCheckExeFile)
	{
		return false;
	}

	PFSX::SetupMetaVector smvUnChecks;
	PFSX::DiffSetupMeta(m_smvServer, m_smvChecks, smvUnChecks);
	return smvUnChecks.empty();
}

// Destroy时写本地 .setup.meta 文件
bool CCheckThread::WriteLocalSetupMetaFile( const PFSX::SetupMetaVector& smvCheck)
{
	PFSX::SetupMetaVector smvLocal;
	if( !PFSX::CSetupMetaFile::DoRead( m_apppath+PFSX::SETUP_META_NAME, smvLocal))
	{
		// 本地的 .setup.meta 文件可能不存在
	}

	for( PFSX::SetupMetaVector::iterator i=m_smvServer.begin(), e=m_smvServer.end(); i!=e; ++i)
	{
		PFSX::SetupMetaInfo& smi = *i;
		PFSX::SetupMetaVector::iterator iFind = std::find( smvLocal.begin(), smvLocal.end(), smi);
		if( iFind == smvLocal.end())
		{
			smi.SetHash( 0);	// 将服务器新添加项的hash值清0，并且添加到本地
			smvLocal.push_back( smi);
		}
	}
	
	std::sort( smvLocal.begin(), smvLocal.end() );

	// 删除可能废弃的包(无论smvCheck有几个)。
	PFSX::SetupMetaVector smvUseless;
	PFSX::DiffSetupMeta( smvLocal, m_smvServer, smvUseless );
	if( !smvUseless.empty() )
	{
		PFSX::PurgeSetupMeta( smvLocal, smvUseless );

		// 从smvCheck中删除服务器已经不存在的包。
		PFSX::SetupMetaVector smvCheck2 = smvCheck;
		PFSX::PurgeSetupMeta( smvCheck2, smvUseless );
		PFSX::MergeSetupMeta( smvCheck2, smvLocal );
	}
	else
	{
		PFSX::MergeSetupMeta( smvCheck, smvLocal );
	}

	return PFSX::CSetupMetaFile::DoWrite( m_apppath+PFSX::SETUP_META_NAME, smvLocal);
}