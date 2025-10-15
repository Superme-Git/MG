#include "StdAfx.h"
#include "GlobalFunction.h"
#include "ErrorInfo.h"
#include "GlobalNotification.h"
#include "../../pfs/src/common.h"
#include "../../pfs/versionmetafile.h"
#include "../../pfs/setupmetafile.h"
#include "KHLogger.h"
#include "LauncherConfig.h"
#include <wininet.h>
#include "ResumeBrokenDownload.h"
#include "TextManager.h"
#include "TextID.h"
#include "../../pfs/stringhelper.h"

CUpdateMountManager::~CUpdateMountManager()
{
}

// �ύ�޸ĵ�����
bool CUpdateMountManager::CommitPFS( HANDLE hStopEvent)
{
	if( m_bReadOnlyMount )
	{
		// ����ֻ����ʽ��Mount�����ﲻִ��Commit����
		return true;
	}

	bool bFailed = false;

	GlobalNotifyStart( (int) m_setupMetas.size() );
	
	for( PFSX::SetupMetaVector::iterator i=m_setupMetas.begin(); i != m_setupMetas.end(); ++i )
	{
		std::wstring pfsdir = RUN_ROOT + i->GetName();

		GlobalNotifyStep( pfsdir.c_str() );
		
		PFS::FILE_DATA_HASH hash = 0;

		bool bRet = false;
		bool bCommit = !DetectBreakEvent( hStopEvent);
		if( bCommit )
		{
			bRet = PFS::CEnv::Commit( pfsdir );
			if( !bRet )
			{
				upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_COMMIT, pfsdir.c_str()) );
				GlobalNotifyErrorInfo( upd::GetLastError() );
			}
			else
			{
				// �ύ�޸�֮�󣬻�ȡMetaFile��hashֵ
				if( !PFS::CEnv::GetMetaFileHash( pfsdir, hash))
				{
					hash = GetMetaFileHash( pfsdir);
				}
			}
		}

		if ( bRet )
		{
			i->SetHash( hash );
		}
		else
		{
			i->SetHash( 0 );
			bFailed = true;
		}
	}

	GlobalNotifyEnd();

	// Commit���и�ϵͳ
	bool bRetRoot = PFS::CEnv::Commit( RUN_ROOT );
	if ( !bRetRoot )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_COMMIT, RUN_ROOT) );
		GlobalNotifyErrorInfo(  upd::GetLastError() );
	}

	return !bFailed && bRetRoot;
}

bool CUpdateMountManager::UnmountPFS()
{
	if( !m_bNeedUnmount )
	{
		return true;
	}

	// ���෴˳��unmount�ļ���ϵͳ
	bool bRet = true;
	GlobalNotifyStart( (int) m_setupMetas.size() );
	for( PFSX::SetupMetaVector::iterator i=m_setupMetas.begin(); i != m_setupMetas.end(); ++i )
	{
		std::wstring pfsdir = RUN_ROOT + i->GetName();

		GlobalNotifyStep( pfsdir.c_str() );
		
		// ������commit = false��ʽunmount����Ϊcommit�����Ѿ�����ʵ��
		bRet = PFS::CEnv::Unmount( pfsdir, false );
		if ( !bRet )
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_UNMOUNT, pfsdir.c_str()) );
			GlobalNotifyErrorInfo( upd::GetLastError() );
			bRet = false;
		}
	}

	GlobalNotifyEnd();

	// Unmount���и�ϵͳ
	// ��commit = false��ʽunmount����Ϊcommit�����Ѿ�����ʵ��
	bRet = PFS::CEnv::Unmount( RUN_ROOT, false );
	if ( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_UNMOUNT, RUN_ROOT) );
		GlobalNotifyErrorInfo(  upd::GetLastError() );
	}

	m_bNeedUnmount = !bRet;
	return !m_bNeedUnmount;
}

static bool MoveFiles(const std::wstring& strFromDir,
					  const std::wstring& strToDir,
					  PFS::CMetaInfoMap& rMimNeedToMove,
					  PFS::CMetaInfoMap& rMimMoved,// ͳ��ת�Ƴɹ�������
					  bool bOverwrite,			// �Ƿ񸲸��Ѵ��ڵ��ļ�
					  HANDLE hStopEvent )
{
	bool bRet = true;

	PFS::CMetaInfoMap::const_iterator rEnd = rMimNeedToMove.end();
	for(PFS::CMetaInfoMap::const_iterator itr = rMimNeedToMove.begin(); itr != rEnd; ++ itr )
	{
		if( DetectBreakEvent( hStopEvent ) ) 
		{
			bRet = false;
			break;
		}

		const std::wstring& path =  (*itr).first;

		if( PFS::CEnv::FileCopy(strFromDir + path, strToDir + path, !bOverwrite) )
		{
			if(  PFS::CEnv::RemoveFile( strFromDir + path ) )
			{
				rMimMoved.insert(*itr);
				GlobalNotifyStep( path.c_str() );
			}
			else
			{
				int nLastError = PFS::CEnv::GetLastError();
				bRet = false;
				GlobalNotifyStep( path.c_str(), nLastError );
			}
		}
		else
		{
			int nLastError = PFS::CEnv::GetLastError();
			if( PFS::EC_FILE_NOT_FOUND == nLastError ||
				ERROR_FILE_NOT_FOUND == nLastError ||
				ERROR_PATH_NOT_FOUND  == nLastError )
			{
				// Դ�ļ��Ѿ������ڣ������ִ��ɾ��MetaInfo������
				PFS::CEnv::RemoveFile( strFromDir + path );
				GlobalNotifyStep( path.c_str(), nLastError );
			}
			else if( !bOverwrite && PFS::CEnv::IsFileExisting( strToDir + path ) )
			{
				// �������ǵ���������Ŀ���ļ��Ѵ��ڵ���Copyʧ�ܣ���ʱ��ɾ��Դ����Ϊ�ɹ���
				PFS::CEnv::RemoveFile( strFromDir + path );
				rMimMoved.insert(*itr);
				GlobalNotifyStep( path.c_str() );
			}
			else
			{	
				GlobalNotifyStep( path.c_str(), nLastError );
				bRet = false;
			}
		}
	}

	return bRet;
}

static bool IsDirectoryExisting( const wchar_t * lpszDirName )
{
	DWORD	dwAttribute = GetFileAttributesW( lpszDirName );
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY == ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

//
// �����ʽ�仯ʱ���ͻ����ļ���Ԥ����
// �䴦�����������£�
// �½�һ��pfsFile����ԭĿ¼��ת���ļ���pfsFile�С�
//
static bool MoveFilesForNewPackage(std::wstring strPfsRunRoot, 
								   std::wstring strRunDir, 
								   std::wstring pakName,
								   HANDLE hStopEvent)
{
	// ���ԭ��pfsdir��һ��Ŀ¼��pfsFile��δ��������ô����ʱ��һ�������ʽ�仯�ĸ��¡�
	// �䴦�����������£�
	// �½�һ��pfsFile����pfsdir��ת��ԭ�е��ļ���pfsFile�С�
	// ����ת��ʧ�ܣ����ﲢ�����������ĸ��£���������һ�����ƴ�ʩ��
	std::wstring pfsdir = strPfsRunRoot + pakName;
	std::wstring pfsFile = pfsdir + L".pfs";
	std::wstring nativeDir = strRunDir + pakName;

	bool bOverwrite = !PFS::CFile::IsFileExisting(pfsFile);
	bool bSrcExisting = IsDirectoryExisting(nativeDir.c_str());
	
	std::time_t current = std::time( NULL );
	std::wstringstream wss;
	wss << pfsdir;
	wss << L".";
	wss << (unsigned int) (current & 0x0FFFFFFFF);
	std::wstring pfsdirtemp = wss.str(); // ��ʱ������һ����ʱ���ַ�����

	bool bRet = PFS::CEnv::Mount( pfsdirtemp, pfsFile, PFS::FST_ZIP, PFS::MT_ZIPUPDATE );
	if( !bRet )
	{
		return false;
	}

	PFS::CMetaInfoMap mimNeedToMove, mimMoved;
	
	// ��ȡ��Ŀ¼�µ������ļ��嵥pfsdir
	bRet = PFS::CEnv::GetMetaInfosEx(pfsdir, mimNeedToMove );

	if( !mimNeedToMove.empty() )
	{
		bRet = MoveFiles( pfsdir, pfsdirtemp, 
			mimNeedToMove, mimMoved, bOverwrite, hStopEvent );

		if( !mimMoved.empty() )
		{
			KHMsgLog_Warning(L"Info - %d files under %s were moved to package %s.\n",
				mimMoved.size(), nativeDir.c_str(), pfsFile.c_str() );

			std::wofstream fmoved;
			fmoved.open( (strRunDir + L"\\log\\moved.log").c_str(), std::ios_base::out | std::ios_base::app );
			DumpMetaInfoMap( mimMoved, fmoved);
			fmoved << L" --- from " << pfsdir << L" to " << pfsFile << L" ---- "  << std::endl;
		}

		if( mimNeedToMove.size() != mimMoved.size() )
		{
			PFS::CMetaInfoMap mimMoveFailed; 
			SubtractMetaInfoMap( mimNeedToMove, mimMoved, mimMoveFailed );
			KHMsgLog_Warning(L"Warning - %d files under %s were moved to %s failed.\n",
				mimMoveFailed.size(), nativeDir.c_str(), pfsFile.c_str() );

			std::wofstream fmovefailed;
			fmovefailed.open( (strRunDir + L"\\log\\movefailed.log").c_str(), 
				std::ios_base::out | std::ios_base::app );
			DumpMetaInfoMap( mimMoveFailed, fmovefailed);
			fmovefailed << L" --- from " << pfsdir << L" to " << pfsFile << L" ---- "  << std::endl;
		}
	}
	
	// ����ɾ��Ŀ¼�ˡ�
	if( bRet && bSrcExisting )
	{
		BOOL bRet2 = DeleteCacheFolder(nativeDir);
		if( bRet2 )
		{
			KHMsgLog_Info(L"Info - DeleteFolder(%s).\n", nativeDir.c_str() );
		}
		else
		{
			DWORD dwError = ::GetLastError();
			KHMsgLog_Warning(L"Warning - DeleteFolder(%s) failed(%d)\n", nativeDir.c_str(), dwError );
		}
	}

	PFS::CEnv::Unmount( pfsdirtemp );

	return bRet;
}

bool CUpdateMountManager::MountPFS(const std::wstring& strRunDir, bool bReadOnly, 
								   HANDLE hStopEvent, const PFSX::SetupMetaVector& setupMetas)
{
	m_setupMetas.clear();

	std::wstring strRunRoot = RUN_ROOT;
	m_bReadOnlyMount = bReadOnly;

	GlobalNotify( GlobalGetText( TIP_MOUNT_ROOT).c_str());
	GlobalNotifyStart( 1 );

	// Mount����ϵͳ
	bool bOK = PFS::CEnv::Mount( strRunRoot, strRunDir, PFS::FST_NATIVE, bReadOnly ? PFS::MT_READONLY : PFS::MT_WRITABLE );
	GlobalNotifyEnd();
	if ( !bOK )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT, strRunRoot.c_str(), strRunDir.c_str()) );

		GlobalNotifyErrorInfo( upd::GetLastError() );
		return false;
	}

	m_bNeedUnmount = true;

	if( DetectBreakEvent( hStopEvent))
	{
		PFS::CEnv::Unmount( RUN_ROOT );
		return false;
	}
	
	bool bRet = true;

	//TCHAR buffer[256] = {0};
	//DWORD timeTotalElapse = timeGetTime();
	//DWORD timeElapse = timeTotalElapse;

	GlobalNotify( GlobalGetText( TIP_MOUNT_RESOURCE).c_str());
	GlobalNotifyStart( (int) setupMetas.size() );

	for( PFSX::SetupMetaVector::const_iterator i=setupMetas.begin(); i != setupMetas.end(); ++i )
	{
		std::wstring pfsdir = strRunRoot + i->GetName();

		std::wstring pfsFile = pfsdir + L".pfs";

		GlobalNotifyStep( pfsFile.c_str() );

		// ���ﴦ��һЩ�������:
		// ���ԭ��pfsdir��һ��Ŀ¼��pfsFile��δ��������ô����ʱ��һ�������ʽ�仯�ĸ��¡�
		// �䴦�����������£�
		// �½�һ��pfsFile����pfsdir��ת��ԭ�е��ļ���pfsFile�С�
		// ����ת��ʧ�ܣ����ﲢ�����������ĸ��£���������һ�����ƴ�ʩ��
		if( !bReadOnly )
		{
			MoveFilesForNewPackage( strRunRoot, strRunDir,  i->GetName(), hStopEvent );

			if( DetectBreakEvent( hStopEvent) )
			{
				PFS::CEnv::Unmount( RUN_ROOT );

				return false;
			}
		}

		//timeElapse = timeGetTime();
		do 
		{
			bRet = PFS::CEnv::Mount( pfsdir, pfsFile, PFS::FST_ZIP, bReadOnly ? PFS::MT_READONLY : PFS::MT_ZIPUPDATE );
			//timeElapse = timeGetTime() - timeElapse;
			//StringCchPrintf( buffer, _countof(buffer), _T("mount %s -- %lu ms\n"), pfsFile.c_str(), timeElapse );
			//OutputDebugString( buffer );
			if ( !bRet )
			{
				int nLastError = PFS::CEnv::GetLastError();
				std::wostringstream oss;
				oss << GlobalFormatText( ERR_MOUNT, pfsdir.c_str(), pfsFile.c_str());

				if ( !bReadOnly &&
					(nLastError == PFS::EC_INVALID_MOUNT_DEVICE ||
					nLastError == PFS::EC_FILE_BAD_ZIPDATA)
					)
				{
					oss << GlobalGetText( TIP_REBUILD_FILE);
				}
				upd::SetLastError( nLastError, oss.str() );
				GlobalNotifyErrorInfo( upd::GetLastError() );
				if( !bReadOnly && 
					(nLastError == PFS::EC_INVALID_MOUNT_DEVICE ||
					nLastError == PFS::EC_FILE_BAD_ZIPDATA)
					)
				{
					PFS::CFile file;
					if( !file.Open(pfsFile, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR ) )
					{
						upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_REBUILD, pfsFile.c_str()));
						GlobalNotifyErrorInfo( upd::GetLastError() );
					}
					else
					{
						// �ؽ��ɹ����������
						continue;
					}
				}
			}
			else
			{
				m_setupMetas.push_back( *i);
				m_setupMetas.back().SetHash( 0);
			}
			break; // ���ٳ���
		} while( 1 );

		if ( !bRet )
		{
			break; // ����forѭ��
		}

		//if( CUpdateManager::DetectBreakEvent() )
		if( DetectBreakEvent( hStopEvent))
		{
			bRet = false;
			break;
		}

		//timeTotalElapse = timeGetTime() - timeTotalElapse;
		//StringCchPrintf( buffer, _countof(buffer), _T("-- ������ʱ�� %lu �� --\n"), timeTotalElapse/1000 );
		//OutputDebugString( buffer );
	}
	
	GlobalNotifyEnd( );

	if( !bRet )
		UnmountPFS();

	return bRet;
}

// ���� mount Ŀ¼�� .file.meta �ļ��� hash ֵ
PFS::FILE_DATA_HASH CUpdateMountManager::GetMetaFileHash( const std::wstring& mount)
{
	PFS::CMetaInfoMap mim;

	//��ʱ.files.meta�ļ�������δ���£����ܴ��ļ���ȡ
	//if( !ReadMetaInfoFile( mount, mim))
	//{
	//	return 0;
	//}	
	if( !PFS::CEnv::GetMetaInfos( mount, mim) )
	{
		// TODO:
		return 0;
	}
	mim.erase( PFSX::FILES_META_NAME);	// ɾ�� .file.meta ���Ϊ�������˵��б��ļ���û���� .files.meta ����

	PFS::CDataBlock buffer;
	if( !PFS::CMetaDataWrite::DoWrite( buffer, mim))
	{
		return 0;
	}
	PFS::CImageFile tempfile( buffer);
	PFS::FILE_DATA_HASH md5 = 0;
	PFS::helper::CMD5FileHashMethod md5Method;
	md5Method.MakeFileHash( tempfile, md5);

	return md5;
}

bool CDownloadFiles::DownloadOneFile( const std::wstring& fileName )
{
	// ֱ��Copy�ļ�ʵ�����أ������ͬʱCopy MetaInfo
	std::wstring strSrcPath = BRF_ROOT + fileName;
	std::wstring strDstPath = RES_ROOT + fileName;
	bool bRet = PFS::CEnv::FileCopy( strSrcPath, strDstPath, false );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_DOWNLOAD_FILE, strSrcPath.c_str()));
	}
	return bRet;
}

bool CDownloadFiles::Download( HANDLE hBreakEvent )
{
	if( !PFS::CEnv::Mount( BRF_ROOT, m_strDir + L".brief", PFS::FST_BRIEF, PFS::MT_READONLY) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT_BRIEF, BRF_ROOT, m_strDir.c_str()));
		return false;
	}

	if( !PFS::CEnv::GetMetaInfos( BRF_ROOT, m_mimDownload ) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_GETMETAINFO, BRF_ROOT));

		PFS::CEnv::Unmount( BRF_ROOT );
		return false;
	}

	std::wstring cachaPath = m_strDir + L".cache";
	CreateHiddenDirectory( cachaPath);
	if( !PFS::CEnv::Mount( RES_ROOT, cachaPath, PFS::FST_NATIVE, PFS::MT_WRITABLE ) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT_CACHE, RES_ROOT, m_strDir.c_str()));

		PFS::CEnv::Unmount( BRF_ROOT );
		return false;
	}

	// �Ƴ�����
	m_mimDownload.erase(L"/.");

	size_t total = m_mimDownload.size();
	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_FILE_COUNT).c_str(), total );
	GlobalNotifyStart( (int)total );

	size_t errorCounter = 0;	// ͳ�ƴ������
	int retryTimes = 0;			// ���Դ���
	while( true)
	{
		size_t maxFailedCount = m_mimDownload.size() / 32;
		if( maxFailedCount == 0 && m_mimDownload.size() > 1)
		{
			// ���m_mimDownload��������1��������ʧ�ܸ�����СΪ10
			maxFailedCount = min( 10, m_mimDownload.size());
		}
		errorCounter = 0;

		for( PFS::CMetaInfoMap::const_iterator i=m_mimDownload.begin(), e=m_mimDownload.end(); i != e; ++i)
		{
			if( DetectBreakEvent( hBreakEvent) )
			{
				// �û�Ҫ�˳��˹��̣���ʱ����Ϊ����ʧ�ܣ�
				break;
			}

			const std::wstring& filename = i->first;
			if( !DownloadOneFile( filename ) )
			{
				m_mimFailed.insert( *i );
				GlobalNotifyStep( filename, upd::GetLastError().ErrorCode );
				if( ++errorCounter > maxFailedCount )
				{
					GlobalNotify( GlobalGetText( TIP_DOWNLOAD_FILE_ERROR).c_str(), errorCounter );
					break;
				}
			}
			else
			{
				// �ɹ���errorCount��0���������ʧ�ܸ������� maxFailedCount ���˳�
				errorCounter = 0;
				GlobalNotifyStep( filename );
				m_mimDownloaded.insert( *i );
			}
		}
		if( errorCounter > maxFailedCount)
		{
			break;
		}
		if( m_mimFailed.empty())
		{
			break;
		}
		if( ++retryTimes >= 10)
		{
			break;	// ���Դ���������10��
		}
		m_mimDownload.swap( m_mimFailed);
		m_mimFailed.clear();
	}

	if ( errorCounter > 0 )
	{
		GlobalNotify( GlobalGetText( TIP_DOWNLOAD_COMPLETE).c_str(), errorCounter );
		// д����ʧ�ܵ��ļ��б�
		std::wofstream logFile;
		logFile.open( ( m_strDir + L".cache\\failed.log").c_str(), std::ios_base::out | std::ios_base::app );
		DumpMetaInfoMap( m_mimFailed, logFile);
	}

	if( m_mimDownloaded.size() > 0 )
	{
		// д�����ļ��б�����Ŀ¼�й�����
		GlobalNotify( GlobalGetText( TIP_DOWNLOAD_COMPLETE2).c_str(), m_mimDownloaded.size() );
		std::wofstream logFile;
		logFile.open( ( m_strDir + L".cache\\downloaded.log").c_str(), std::ios_base::out | std::ios_base::app );
		DumpMetaInfoMap( m_mimDownloaded, logFile );
	}

	PFS::CEnv::Unmount( BRF_ROOT );
	PFS::CEnv::Unmount( RES_ROOT );

	GlobalNotifyEnd();

	return m_mimDownloaded.size() > 0 && errorCounter == 0;
}

bool DetectBreakEvent( HANDLE hEvent)
{
	if ( WaitForSingleObject( hEvent, 1 ) == WAIT_OBJECT_0 )
	{
		return true;
	}
	return false;
}

int HandleCheckFileError( bool bPatch, std::wstring path )
{
	int nLastError = PFS::CEnv::GetLastError();

	bool bErrorProcessed = false;

	// �����ʽ��Ч��ɾ�������б��ֵ����ص��ļ����´θ������³�������
	if( PFS::ECX_BADFORMAT == nLastError )
	{
		if( !bPatch )
		{
			bool bRemoved = PFS::CEnv::RemoveFile( RES_ROOT + path);
			if ( !bRemoved )
			{
				upd::SetLastError( PFS::CEnv::GetLastError(), 
					GlobalFormatText( ERR_REMOVE_FILE, RES_ROOT, path.c_str()) );
				GlobalNotifyErrorInfo( upd::GetLastError(), __FUNCTIONW__ );
			}
			else
			{
				upd::SetLastError( nLastError, GlobalFormatText( ERR_FILE_NOT_INTACT, RES_ROOT, path.c_str()) );
				bErrorProcessed = true;
			}
		}
	}

	if( !bErrorProcessed )
	{
		upd::SetLastError( nLastError, GlobalFormatText( ERR_CHECK_FILE, path.c_str()) );
	}

	return nLastError;
}

int HandleDecodeFileError( bool bPatch, std::wstring path )
{
	int nLastError = PFS::CEnv::GetLastError();

	bool bErrorProcessed = false;

	// �����ʽ��Ч��ɾ�������б��ֵ����ص��ļ����´θ������³�������
	if( PFS::ECX_BADFORMAT == nLastError )
	{
		if( !bPatch )
		{
			bool bRemoved = PFS::CEnv::RemoveFile( RES_ROOT + path);
			if ( !bRemoved )
			{
				upd::SetLastError( PFS::CEnv::GetLastError(), 
					GlobalFormatText( ERR_REMOVE_FILE, RES_ROOT, path.c_str()) );
				GlobalNotifyErrorInfo( upd::GetLastError(), __FUNCTIONW__ );
			}
			else
			{
				upd::SetLastError( nLastError, GlobalFormatText( ERR_FILE_NOT_INTACT, RES_ROOT, path.c_str()) );
				bErrorProcessed = true;
			}
		}
		// ������Ŀ¼�е�metainfo��Ϣ��0
		if( !PFS::CEnv::SetFileMetaInfo( RUN_ROOT + path, PFS::CMetaInfo()))
		{
			std::wostringstream oss;
			oss<<L"����: SetFileMetaInfo("<< RUN_ROOT + path << L") ʧ��!" ;
			//upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );
		}
		else
		{
			bErrorProcessed = true;
		}
	}

	if( !bErrorProcessed )
	{
		upd::SetLastError( nLastError, GlobalFormatText( ERR_DECODE_FILE, path.c_str()) );
	}

	return nLastError;
}


bool CheckFilesPartly(PFS::CMetaInfoMap::const_iterator rBegin, 
					  PFS::CMetaInfoMap::const_iterator rEnd,
					  HANDLE hStopEvent,
					  bool bPatch )
{
	PFS::CMetaInfoMap::const_iterator::difference_type count = std::distance( rBegin, rEnd );
	if( count == 0 )
	{
		return true;
	}

	bool bRet = true;

	for(PFS::CMetaInfoMap::const_iterator itr = rBegin; itr != rEnd; ++ itr )
	{
		if( DetectBreakEvent( hStopEvent ) ) 
		{
			bRet = false;
			break;
		}

		const std::wstring& path =  (*itr).first;

		//
		// ������ʱ����ʹ������ĺ������ļ�����У�飬
		// ��Ϊ�������ļ�����.cache��ȡ�õ�metainfo��1.0�汾��
		// û��size2��hash2���޷����ļ����г�ֵ�У�顣
		// ���ң���ȷû�б�Ҫ�ٸ����ļ�����ȡһ��metainfo��
		// ���ڴ˺�����BUG���⣬�ȿ��Ǻ���RefreshMetaFile()�������޸ķ���֮���ٶ���
		//
		//  PFSX::CheckFileEx(RES_ROOT + path)
		//
		PFS::CFile theFile;
		if( !theFile.Open( RES_ROOT + path, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			return false;
		}

		if( PFSX::CheckFileEx( theFile, (*itr).second ) )
		{	
			GlobalNotifyStep( path.c_str() );
		}
		else
		{
			// ע������������ȹرմ��ļ�������HandleCheckFileError()�޷�ɾ�����ļ���
			theFile.Close();

			int nLastError = HandleCheckFileError(bPatch, path);
			bRet = false;
			GlobalNotifyStep( path.c_str(), nLastError );
		}
	}

	return bRet;
}

bool DecodeFilesPartly( PFS::CMetaInfoMap::const_iterator rBegin, 
					   PFS::CMetaInfoMap::const_iterator rEnd,
					   PFS::CMetaInfoMap&	rMimUpdate,// ͳ�Ƴɹ��ϲ�������
					   HANDLE hStopEvent,
					   bool bPatch )
{
	PFS::CMetaInfoMap::const_iterator::difference_type count = std::distance( rBegin, rEnd );
	if( count == 0 )
	{
		return true;
	}

	bool bRet = true;
	for(PFS::CMetaInfoMap::const_iterator itr = rBegin; itr != rEnd; ++ itr )
	{
		if( DetectBreakEvent( hStopEvent ) ) 
		{
			bRet = false;
			break;
		}

		const std::wstring& path =  (*itr).first;
		if( PFSX::DecodeFile(RES_ROOT + path,RUN_ROOT + path) )
		{
			rMimUpdate.insert(*itr);
			GlobalNotifyStep( path.c_str() );
		}
		else
		{
			int nLastError = HandleDecodeFileError(bPatch, path);
			bRet = false;
			GlobalNotifyStep( path.c_str(), nLastError );
		}
	}

	return bRet;
}

// ��CheckFileEx��ʽУ��strDirĿ¼�е��ļ�
bool CheckFilesInFolder(const std::wstring& strDir,
						PFS::CMetaInfoMap& rMimCache,// ��ҪУ�����������
						HANDLE hStopEvent ) 
{
	bool bRet = false;
	PFS::CMetaInfoMap::const_iterator itr;

	// ������ص��ļ����ݲ�����,������Ҫɾ����,���������Կ�д�ķ�ʽMount
	bRet = PFS::CEnv::Mount( RES_ROOT, strDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_MOUNT, RES_ROOT, strDir.c_str()) );
		return false;
	}

	// ʹ�ô���Ĳ�����������GetMetaInfos�еõ���,��ʵ����Ӧ��һ��

	GlobalNotify( GlobalGetText( TIP_CHECK_FILE_COUNT).c_str(), rMimCache.size() );

	GlobalNotifyStart( (int) rMimCache.size() );

	bRet = CheckFilesPartly( rMimCache.begin(), rMimCache.end(), hStopEvent, false );

	GlobalNotifyEnd();

	PFS::CEnv::Unmount( RES_ROOT );
	
	return bRet;
}



// ��strInputDirָ����Ŀ¼�е��ļ���ʹ��DecodeFile��ʽ�򿪲����浽strOutputDirָ����Ŀ¼��
bool TranslateFilesInFolder(
							const std::wstring& strInputDir,
							const std::wstring& strOutputDir,
							PFS::CMetaInfoMap& rMimMerge, // ��Ҫ�ϲ�����������
							PFS::CMetaInfoMap& rMimUpdate,// ͳ�Ƴɹ��ϲ�������
							HANDLE hStopEvent,
							bool bPatch) 
{
	rMimUpdate.clear();

	bool bRet = false;
	PFS::CMetaInfoMap::const_iterator itr;

	if( !bPatch)
	{
		// ������ص��ļ����ݲ�����,������Ҫɾ����,���������Կ�д�ķ�ʽMount
		bRet = PFS::CEnv::Mount( RES_ROOT, strInputDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if ( !bRet )
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_MOUNT, RES_ROOT, strInputDir.c_str()) );
			return false;
		}
	}

	// ʹ�ô���Ĳ�����������GetMetaInfos�еõ���,��ʵ����Ӧ��һ��
	PFS::CMetaInfoMap& paths = rMimMerge;

	bRet = true;

	GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT).c_str(), paths.size() );
	GlobalNotifyStart( (int) paths.size() );

	bRet = DecodeFilesPartly( paths.begin(), paths.end(), rMimUpdate, hStopEvent, bPatch );
		
	if( !bPatch)
	{
		PFS::CEnv::Unmount( RES_ROOT );
	}

	GlobalNotifyEnd( );

	return bRet;
}

// ��strInputDirָ����Ŀ¼�е��ļ���ʹ��DecodeFileEn��ʽ�򿪲����浽strOutputDirָ����Ŀ¼��
bool TranslateFilesInFolderEn(const std::wstring& strInputDir,
							  const std::wstring& strOutputDir,
							  PFS::CMetaInfoMap& rMimMerge, // ��Ҫ�ϲ�����������
							  PFS::CMetaInfoMap& rMimUpdate,// ͳ�Ƴɹ��ϲ�������
							  HANDLE hStopEvent ) 
{
	rMimUpdate.clear();

	bool bRet = false;
	PFS::CMetaInfoMap::const_iterator itr;

	// ������ص��ļ����ݲ�����,������Ҫɾ����,���������Կ�д�ķ�ʽMount
	bRet = PFS::CEnv::Mount( RES_ROOT, strInputDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
	if ( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT, RES_ROOT, strInputDir.c_str()) );
		return false;
	}

	// ʹ�ô���Ĳ�����������GetMetaInfos�еõ���,��ʵ����Ӧ��һ��
	PFS::CMetaInfoMap& paths = rMimMerge;

	bRet = true;

	GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT).c_str(), paths.size() );
	GlobalNotifyStart( (int) paths.size() );

	for( itr = paths.begin(); itr != paths.end(); ++ itr )
	{
		if( DetectBreakEvent( hStopEvent ) ) 
		{
			bRet = false;
			break;
		}

		const std::wstring& path =  (*itr).first;
		if( PFSX::DecodeFileEn( RES_ROOT + path, RUN_ROOT + path ) )
		{
			rMimUpdate.insert( *itr );
			GlobalNotifyStep( path.c_str() );
		}
		else
		{
			int nLastError = HandleDecodeFileError(false, path);
			bRet = false;
			GlobalNotifyStep( path.c_str(), nLastError );
		}
	}

	PFS::CEnv::Unmount( RES_ROOT );
	GlobalNotifyEnd( );

	return bRet;
}

bool TranslateFilesForLauncher(
							   const std::wstring&	strInputDir,
							   const std::wstring&	strOutputDir,
							   PFS::CMetaInfoMap&	rMimMerge, // ��Ҫ�ϲ�����������
							   PFS::CMetaInfoMap&	rMimUpdate,// ͳ�Ƴɹ��ϲ�������
							   HANDLE hStopEvent,
							   bool bPatch) 
{
	rMimUpdate.clear();

	std::wstring strLauncherFolder = L"/";
	strLauncherFolder += GetLauncherFolder();
	strLauncherFolder += L"/";
	std::wstring strLauncherFolderEnd = strLauncherFolder;
	strLauncherFolderEnd[strLauncherFolder.size()-1]++;

	PFS::CMetaInfoMap::const_iterator rBegin = rMimMerge.lower_bound( strLauncherFolder);
	if( rBegin == rMimMerge.end() )
	{
		return true;
	}

	PFS::CMetaInfoMap::const_iterator rEnd = rMimMerge.lower_bound( strLauncherFolderEnd );
	// rEnd may be at the end.

	bool bRet = false;
	PFS::CMetaInfoMap::const_iterator itr;

	if( !bPatch)
	{
		// ������ص��ļ����ݲ�����,������Ҫɾ����,���������Կ�д�ķ�ʽMount
		bRet = PFS::CEnv::Mount( RES_ROOT, strInputDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if ( !bRet )
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_MOUNT, RES_ROOT, strInputDir.c_str()) );
			return false;
		}
	}

	PFS::CMetaInfoMap::const_iterator::difference_type count = std::distance( rBegin, rEnd );
	bRet = true;

	GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT).c_str(), (unsigned int) (count) );
	GlobalNotifyStart( (int) (count) );

	if( !DecodeFilesPartly(rBegin, rEnd, rMimUpdate, hStopEvent, bPatch ) )
	{
		bRet = false;
	}

	// ��Ϊÿ�θ��¶��ӷ������������µ� .setup.meta ���������ٲ���Ҫ��������/.setup.meta

	if( !bPatch)
	{
		PFS::CEnv::Unmount( RES_ROOT );
	}

	GlobalNotifyEnd( );

	return bRet;
}

//
// summary:
//		�Ƚ�����MetaInfo�Ƿ�һ��
//		����ֻ�Ƚ��ļ���С��Hashֵ
//
bool IsEqualMetaInfo(const PFS::CMetaInfo& rLeft,const PFS::CMetaInfo& rRight)
{
	return rLeft.GetFileSize() == rRight.GetFileSize() &&
		rLeft.GetFileHash() == rRight.GetFileHash() && 
		rLeft.GetFilePfsxAttributes() == rRight.GetFilePfsxAttributes() &&
		rLeft.GetMethod() == rRight.GetMethod();
}

// 
// summary:
//		��Server�˵�MetaInfo��Ϣ���û������汾��MetaInfo���в��컯�ж�
//		����ȷ����Ҫ���µ��б�����
//
// parameters:
//		[in] rMinuend - ��ȫ����Ϣ��MetaInfo��Ϣ��Server�˻�Server��Local�Ĳ����������
//		[in] rSubtrahend - �û�������MetaInfo��Ϣ��Local��Cache�е���Ϣ����������
//		[out] rDifference - ������������Ҫ���µ�MetaInfo��Ϣ
//
void SubtractMetaInfoMap(
						 const PFS::CMetaInfoMap& rMinuend,
						 const PFS::CMetaInfoMap& rSubtrahend,
						 PFS::CMetaInfoMap&		rDifference )
{
	PFS::CMetaInfoMap::const_iterator itrS = rMinuend.begin();
	PFS::CMetaInfoMap::const_iterator itrL = rSubtrahend.begin();
	PFS::CMetaInfoMap::key_compare comparator;
	for ( ; itrS != rMinuend.end() &&  itrL != rSubtrahend.end();  ++ itrL )
	{
		const std::wstring& rName2 = itrL->first;

		for( ; itrS != rMinuend.end() && comparator( itrS->first, rName2 ); ++ itrS )
		{
			rDifference.insert( *itrS );
		}

		if ( itrS == rMinuend.end() )
		{
			break;
		}

		// ���������ͬ���ж�MetaInfo�Ƿ�һ��������һ���ļӵ������б���
		if (  itrS->first.compare( rName2 ) == 0 ) 
		{
			if ( !IsEqualMetaInfo( itrS->second, itrL->second) )
			{
				rDifference.insert( *itrS );
			}

			++ itrS;
		}
	}

	for( ; itrS != rMinuend.end() ; ++ itrS )
	{
		rDifference.insert( *itrS );
	}

	//std::wofstream frun;
	//frun.open( L"D:\\rrun.txt" );
	//DumpMetaInfoMap( rSubtrahend, frun );
	//std::wcout<< L"D:\\rrun.txt !" <<std::endl;

	//std::wofstream fserver;
	//fserver.open( L"D:\\rserver.txt" );
	//DumpMetaInfoMap( rMinuend, fserver );
	//std::wcout<< L"D:\\rserver.txt !" <<std::endl;

	//std::wofstream fupdate;
	//fupdate.open( L"D:\\rupdate.txt" );
	//DumpMetaInfoMap( rDifference, fupdate );
	//std::wcout<< L"D:\\rupdate.txt !" <<std::endl;

	rDifference.erase( L"/." );
}

// 
// summary:
//		����һ�汾��MetaInfo��Ϣ�͵�ǰ�汾��MetaInfo���бȽ�
//		����ȷ����ǰ�汾��Ҫɾ�����б�
//
// parameters:
//		[in] rLast - ��һ�汾��MetaInfo��Ϣ
//		[in] rCur  - ��ǰ�汾��MetaInfo��Ϣ
//		[out] rDelete - ������������Ҫɾ����MetaInfo��Ϣ
//
void SubtractMetaInfoMapEx( const PFS::CMetaInfoMap& rLast, 
						   const PFS::CMetaInfoMap& rCur,
						   PFS::CMetaInfoMap&	rDelete )
{
	PFS::CMetaInfoMap::const_iterator iLast = rLast.begin();
	PFS::CMetaInfoMap::const_iterator iCur = rCur.begin();
	PFS::CMetaInfoMap::const_iterator lastEnd = rLast.end();
	PFS::CMetaInfoMap::const_iterator curEnd = rCur.end();
	PFS::CMetaInfoMap::key_compare comparator;

	for ( ; iLast != lastEnd &&  iCur != curEnd; )
	{
		const std::wstring& rName1 = iLast->first;
		const std::wstring& rName2 = iCur->first;

		if( comparator( rName1, rName2 ))
		{
			rDelete.insert( *iLast );
			++iLast;
		}
		else if( comparator( rName2, rName1))
		{
			++iCur;
		}
		else	// rName1 == rName2
		{
			++iLast;
			++iCur;
		}
	}

	for( ; iLast != lastEnd ; ++ iLast)
	{
		rDelete.insert( *iLast );
	}
};

//
// ���MetaInfoMap�е��ļ����б�����
//
void DumpMetaInfoMap( const PFS::CMetaInfoMap& mim, std::wostream& oss )
{
	try
	{
		oss.imbue(std::locale(".936"));
	}
	catch (std::runtime_error e)
	{
		KHMsgLog_Error(_T("oss.imbue(std::locale(\".936\")) throws an runtime error(%hs)\r\n"), e.what() );
	}
	
	// ���ʱ����Ϣ
	std::time_t current = std::time( NULL );
	wchar_t buffer[256] = {0};
	_wctime_s(buffer, _countof(buffer), &current);
	oss << L"------------------------------------------\n";
	oss <<  buffer ; // buffer �а������з�
	oss << L"------------------------------------------\n";

	for( PFS::CMetaInfoMap::const_iterator it = mim.begin(); it != mim.end(); ++ it)
	{
		oss << it->first << std::endl;
	}

	oss << std::endl;
}

// ��ָ��Ŀ¼�µ�MetaInfo�ļ�
bool ReadMetaInfoFile( const std::wstring& dir, PFS::CMetaInfoMap& mimFile )
{
	bool bRet = PFSX::ReadMetaInfoFile( dir + PFSX::FILES_META_NAME, mimFile );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_READ_FILE, dir.c_str(), PFSX::FILES_META_NAME.c_str()) );
	}
	return bRet;
}

// ��ָ��Ŀ¼�µ�MetaInfo2�ļ�
bool ReadMetaInfoFile2( const std::wstring& dir, PFS::CMetaInfoMap& mimFile )
{
	bool bRet = PFSX::ReadMetaInfoFile( dir + PFSX::FILES2_META_NAME, mimFile );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_READ_FILE, dir.c_str(), PFSX::FILES2_META_NAME.c_str()) );
	}
	return bRet;
}

// ��MetaInfo�ļ�д��ָ��Ŀ¼��
bool WriteMetaInfoFile( const std::wstring& dir, const PFS::CMetaInfoMap& mimFile )
{
	bool bRet = PFSX::WriteMetaInfoFile( dir + PFSX::FILES_META_NAME, mimFile );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_WRITE_FILE, dir.c_str(), PFSX::FILES_META_NAME.c_str()) );
	}

	return bRet;
}

//
// ��urlָ����λ������metainfo�ļ�������ȡ���ݵ�rMetaInfoMap������
//
bool DownloadMetaInfoFile( const std::wstring& url, PFS::CMetaInfoMap& rMetaInfoMap )
{
	return ReadRemoteFilesMetaFile( url + PFSX::FILES_META_NAME, rMetaInfoMap);
}

// ��urlָ����λ������ .files2.meta �ļ�������ȡ���ݵ�rMetaInfoMap������
bool DownloadMetaInfoFile2( const std::wstring& url, PFS::CMetaInfoMap& rMetaInfoMap )
{
	return ReadRemoteFilesMetaFile( url + PFSX::FILES2_META_NAME, rMetaInfoMap);
}

// �� file ָ�����ļ���ȡmeta��Ϣ������ȡ���ݵ�rMetaInfoMap������
bool ReadRemoteFilesMetaFile( const std::wstring& metafile, PFS::CMetaInfoMap& rMetaInfoMap )
{
	PFSX::CInetFile inetFile;
	if( !inetFile.Open( metafile ) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_DOWNLOAD_FILE, metafile.c_str()) );
		return false;
	}

	PFS::fsize_t szTotal = inetFile.GetSize( );
	if( szTotal == 0 )
	{
		upd::SetLastError( PFS::FILE_SIZE_ERROR, GlobalFormatText( ERR_INVALID_FILE, metafile.c_str()) );
		return false;
	}

	PFS::CMemFile theFile;
	//if( !theFile.Open() )
	//{
	//	std::wostringstream oss;
	//	oss<<L"����: �޷�������ʱ�ļ����ڱ���" << metafile;
	//	upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );

	//	return false;
	//}
	//else
	{
		const size_t BUFSIZE = 40960;

		GlobalNotifyStart( ((int)szTotal+BUFSIZE-1)/BUFSIZE);

		PFS::fsize_t szTotalRead = 0;
		size_t szRead = 0;
		BYTE buffer[ BUFSIZE ] = {0};
		int nTriedTimes1 = 0;
		int nTriedTimes2 = 0;
		while( szTotalRead < szTotal )
		{
			szRead = inetFile.Read( buffer, BUFSIZE);
			if ( PFS::FILE_SIZE_ERROR != szRead && szRead > 0 )
			{
				nTriedTimes1 = 0; // ���سɹ�,�����Դ�������Ϊ0
				nTriedTimes2 = 0; 
				theFile.Write( buffer, szRead );
				szTotalRead += szRead;

				GlobalNotifyStep( GlobalFormatText( TIP_DOWNLOAD_PROGRESS, int( szTotalRead*100.0/szTotal)));
			}
			else
			{
				if( PFS::CEnv::GetLastError() == ERROR_INTERNET_TIMEOUT || 
					PFS::CEnv::GetLastError() == ERROR_INTERNET_FORCE_RETRY )
				{
					// ��ʱ����,����30��,ÿ��1s,��30s
					if( nTriedTimes1 ++ > 30 )
					{
						break;
					}
					Sleep( 1000 );
				}
				else
				{
					// ����������10��,ÿ��0.1s,��1s
					if( nTriedTimes2 ++ > 10 )
					{
						break;
					}
					Sleep( 100 );
				}
			}
		}

		GlobalNotifyEnd();

		bool bOK = false;
		// ���������ļ�ָ�뵽�ļ�ͷ����������Ķ�������ʧ��
		if( szTotalRead == szTotal )
		{
			theFile.Seek( 0, PFS::FSM_SET );
			bOK = PFS::CMetaFileRead::DoRead( theFile, rMetaInfoMap );
		}

		if ( bOK )
		{
			return true;
		}
		else
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_DOWNLOAD_FILE, metafile.c_str()) );
			return false;
		}
	}
}

// ɾ��ָ����Ŀ¼�Լ�Ŀ¼�µ������ļ�
bool DeleteCacheFolder( std::wstring strFolder )
{
	// ���Ǳ��뱣֤�ַ��������2��0��β
	// �ַ������ܺ��� "/"
	// �ַ��������� "\" ��β

	std::replace( strFolder.begin(), strFolder.end(), L'/', L'\\');
	std::wstring::size_type pos = strFolder.find_last_not_of( L"\\");
	if( pos != strFolder.size()-1)
	{
		strFolder.erase( pos+1);
	}
	strFolder.resize( strFolder.size() + 1, 0 );
	
	SHFILEOPSTRUCT FileOp = {0};
	FileOp.pFrom = strFolder.c_str();
	FileOp.wFunc = FO_DELETE;
	FileOp.fFlags = FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_SILENT;
	int iRet = SHFileOperation(&FileOp);
	if ( iRet != ERROR_SUCCESS )
	{
		::SetLastError( iRet );
		return false;
	}

	return true;
}

// ���������ļ���
bool CreateHiddenDirectory( const std::wstring& path)
{
	if( !PFS::CFileIO::IsDirectoryExisting( path))
	{
		if( !::CreateDirectory( path.c_str(), NULL))
		{
			return false;
		}
	}
	return !!::SetFileAttributes( path.c_str(), FILE_ATTRIBUTE_HIDDEN);
}

// �ж�ָ����Mutex�����Ƿ��Ѵ���
bool NamedMutexExists( LPCWSTR szName )
{
	HANDLE hMutex = OpenMutexW( STANDARD_RIGHTS_READ, FALSE, szName);
	if( hMutex == NULL )
	{
		DWORD dwError = ::GetLastError();// normal error code
		if ( dwError == ERROR_FILE_NOT_FOUND )
		{
			return false;
		}
		else 
		{
			assert ( dwError == ERROR_ACCESS_DENIED );
			KHMsgLog_Error(L"NamedMutexExists::OpenMutexW(%s) failed (%d)\n", szName, dwError );
			return true;
		}
	}
	else
	{
		CloseHandle( hMutex );
		return true;				
	}
}

// �õ����ð汾���е�Mutex���� - ��ͬλ�õİ�װ�汾�����ֲ�ͬ
LPCWSTR GetInstallationMutexName()
{
	static WCHAR szObjectName[MAX_PATH] = {0};

	if( szObjectName[0] == 0 )
	{
		wcscpy_s( szObjectName, MAX_PATH, SZ_INSTALLATION_NAME_PREFIX );
		wcscat_s( szObjectName, MAX_PATH, GetInstallDir() );

		// ��·���ָ����滻Ϊ '!'
		for( LPWSTR lpszPtr = szObjectName; *lpszPtr; ++ lpszPtr  )
		{
			if( *lpszPtr == _T('/') || *lpszPtr == _T('\\') )
			{
				*lpszPtr = _T('!');
			}
		}
	}

	return szObjectName;
}

// �����־�ļ���������,ɾ��ȫ����־
bool PurgeLogFile( LPCWSTR lpszFileName, ULONGLONG ullFileSizeLimit )
{
	PFSX::CNativeFile file;
	if( file.Open( lpszFileName, PFS::FM_EXCL, PFS::FA_RDWR ) )
	{
		if( file.GetSize() > ullFileSizeLimit )
		{
			file.SetEnd();
		}
		return true;
	}
	return false;
}

// �������ļ���Ϊ�ɼ�
// ��Ϊ�����ļ��ѿ�д��ʽ��ʱ�᷵�ش����5
bool ShowHiddenFile( const std::wstring& filename)
{
	if( !PFS::CFileIO::IsFileExisting( filename))
	{
		return false;
	}
	DWORD attr = ::GetFileAttributes( filename.c_str());
	if( (FILE_ATTRIBUTE_HIDDEN|FILE_ATTRIBUTE_READONLY) & attr)
	{
		attr &= ~(FILE_ATTRIBUTE_HIDDEN|FILE_ATTRIBUTE_READONLY);
		return !!::SetFileAttributes( filename.c_str(), attr);
	}
	return true;
}

// ��urlָ����λ�ö�ȡ .setup.meta �ļ������ݱ�����
bool ReadRemoteSetupMeta( const std::wstring& url, PFSX::SetupMetaVector& metas)
{
	std::wstring urlFile = url + PFSX::SETUP_META_NAME;
	PFSX::CInetFile netfile;
	if( !netfile.Open( urlFile ))
		return false;

	// ��Ϊ�����ļ�CInetFile��Seek����֧�ֲ��Ǻܺ�
	// �����ȴ������϶����ڴ��У�Ȼ����ΪCImageFileװ��
	PFS::CDataBlock db;
	size_t filelen = (size_t)netfile.GetSize();
	db.Resize( filelen);
	if( filelen != netfile.Read( db.GetData(), filelen))
	{
		return false;
	}

	PFS::CImageFile imf( db);
	if( !PFSX::CSetupMetaFile::DoRead( imf, metas))
	{
		return false;
	}
	return true;
}

bool CSetupMetasDownloader::Download( HANDLE hBreakEvent )
{
	bool bRet = true;
	for( PFSX::SetupMetaVector::const_iterator iV = m_smVector.begin(); 
		iV != m_smVector.end(); ++iV )
	{
		const std::wstring& mount = iV->GetName();

		GlobalNotify( GlobalGetText( TIP_DOWNLOAD_CHILD_META_FILE).c_str(), mount.c_str() );

		CResumeBrokenDownload downloader( 
			PFSX::FILES2_META_NAME, m_strBaseUrl + mount, m_strBaseDir + mount, iV->GetHash2() );

		int iTryCounter = 0;
		do 
		{
			if( downloader.Download( hBreakEvent ) )
			{
				break;
			}

			if( DetectBreakEvent( hBreakEvent) )
			{
				bRet = false;
				break;
			}
		} while( ++iTryCounter < 3 );

		bRet &= iTryCounter < 3;
	}

	return bRet;
}

// ������ɾ�����ļ�
void DealUnDeleteFiles(PFS::CMetaInfoMap& rDelete, 
					   const PFSX::SetupMetaVector& smvServer )
{
	rDelete.erase( PFSX::FILES_META_NAME);	// ��Ŀ¼��.files.meta
	for( PFSX::SetupMetaVector::const_iterator i=smvServer.begin(); i != smvServer.end(); ++i)
	{
		const std::wstring& file = i->GetName();
		rDelete.erase( file + L".pfs");
		rDelete.erase( file + PFSX::FILES_META_NAME);
	}
}

// ��URL�л�ȡHostName����IP��ַ��
bool GetHostNameFromUrl( LPCWSTR lpszUrl, std::wstring& hostname )
{
	DWORD dwError = 0;
	bool bRet = TRUE;
	URL_COMPONENTS UrlComponents = {0};
	UrlComponents.dwStructSize = sizeof(URL_COMPONENTS);
	UrlComponents.dwUrlPathLength = 1;
	UrlComponents.dwHostNameLength = 1;
	UrlComponents.nPort = INTERNET_DEFAULT_HTTP_PORT;
	UrlComponents.dwSchemeLength = 1;
	UrlComponents.dwExtraInfoLength = 1;
	bRet = !!InternetCrackUrl( lpszUrl, 0, 0, &UrlComponents );
	if ( bRet && UrlComponents.dwHostNameLength > 0 )
	{
		//if( UrlComponents.dwUrlPathLength > 0 ) // Maybe empty path
		//	m_baseUrl.assign( UrlComponents.lpszUrlPath, UrlComponents.dwUrlPathLength );
		hostname.assign( UrlComponents.lpszHostName, UrlComponents.dwHostNameLength );
	}
	else
	{
		if( bRet )
		{
			::SetLastError(dwError = ERROR_INTERNET_INVALID_URL);
			bRet = FALSE;
		}
	}

	return bRet;
}

void DumpSetupMeta(const wchar_t* prefix, std::wostream& wos, const PFSX::SetupMetaVector& setupmeta )
{
	wos << prefix;
	for( PFSX::SetupMetaVector::const_iterator i = setupmeta.begin(); i != setupmeta.end(); ++ i )
	{
		wos << i->GetName() << L": " << PFSX::CStringHelper::Hash2String(i->GetHash()) << L", " 
			<< PFSX::CStringHelper::Hash2String(i->GetHash2() ) << std::endl;
	}
}

void AnalyzeSetupMetaUpdateInfo(const PFSX::SetupMetaVector& _smvServer,
								PFSX::SetupMetaVector& smvLocal,
								PFSX::SetupMetaVector& smvDifference,
								PFSX::SetupMetaVector& smvNewAdded,
								PFSX::SetupMetaVector& smvUseless)
{
	if( _smvServer.empty() )
	{
		return;
	}

	PFSX::SetupMetaVector smvModified;

	// ��smvServer��smvLocal��������
	PFSX::SetupMetaVector smvServer = _smvServer;
	std::sort( smvServer.begin(), smvServer.end() );
	std::sort( smvLocal.begin(), smvLocal.end() );

	// ���غͷ���������Ч��������졣
	PFSX::SubtractSetupMeta( smvServer, smvLocal, smvDifference );

	// �������ͱ����������������setupmeta
	PFSX::DiffSetupMeta( smvServer, smvLocal, smvNewAdded );

	// ȫ�������޳������ģ������޸ĵ�setupmeta
	PFSX::DiffSetupMeta( smvDifference, smvNewAdded, smvModified );

	// ���غͲ������汾������������setupmeta
	PFSX::DiffSetupMeta( smvLocal, smvServer, smvUseless );

	std::wstringstream sstream;
	
	DumpSetupMeta( L"\nserver or patch: \n", sstream, smvServer);
	DumpSetupMeta( L"\nlocal: \n", sstream, smvLocal);
	
	if( !smvNewAdded.empty() )
	{
		DumpSetupMeta( L"\nnewly added: \n", sstream, smvNewAdded);
	}

	if( !smvModified.empty())
	{
		DumpSetupMeta( L"\nmodified: \n", sstream, smvModified);
	}

	if( !smvUseless.empty() )
	{
		DumpSetupMeta( L"\nuseless: \n", sstream, smvUseless);
		
		// �ӱ��ؼ����У�ɾ������ʹ�õ�setupmeta��Ŀ��
		PFSX::PurgeSetupMeta( smvLocal, smvUseless );

		DumpSetupMeta( L"\nafter remove useless ones, the local: \n", sstream, smvLocal);
	}

	KHMsgLog_DebugW( sstream.str().c_str() );
}