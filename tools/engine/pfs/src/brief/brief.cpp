///=============================================================================
/// Product   : PFS
/// Project   : pfslib
/// Module    : brief.cpp
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Brief 文件系统实现类
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-15 Created.
///=============================================================================
#include "../common.h"
#include "brief.h"
#include "../base/packetfilesystem.h"
#include "../../versionmetafile.h"
#include <wininet.h>
#include <Shlwapi.h>

namespace PFS
{
	static LPCTSTR _GetAgentName()
	{
		static TCHAR AgentName[MAX_PATH];
		if ( AgentName[0] == NULL )
		{
			GetModuleFileName( NULL, AgentName, MAX_PATH);
			PathStripPath( AgentName );//Leaves the file name only
		}
		return AgentName;
	}

	static BOOL InetCheckConnection( LPCTSTR lpszUrl )
	{
		if( !PathIsURL(lpszUrl))
		{
			SetLastError(ERROR_INVALID_PARAMETER);
			return FALSE;
		}

		int tryCount = 3;
		BOOL bRet = FALSE;
		DWORD dwError = 0;
		do{
			bRet = InternetCheckConnection( lpszUrl, FLAG_ICC_FORCE_CONNECTION, 0 );
			if ( !bRet )
			{
				dwError = GetLastError();
			}
		}while ( !bRet && 
			(dwError == ERROR_INTERNET_CANNOT_CONNECT || dwError == ERROR_INTERNET_TIMEOUT) && 
			(Sleep(1000), --tryCount) );

		return bRet;
	}

	CBriefFS::CBriefFS( void*) 
		: CPFSBase( FST_BRIEF)
	{
		m_hInternet = NULL;
		m_hConnect = NULL;
	}

	CBriefFS::CBriefFS()
	{
		m_hInternet = NULL;
		m_hConnect = NULL;
	}

	CBriefFS::~CBriefFS() 
	{
		if( m_BasePFS)
			m_BasePFS->RemoveRefMountFSDevive( m_strBasePath);

		if( m_hConnect != NULL )
		{
			InternetCloseHandle( m_hConnect );
			m_hConnect = NULL;
		}
		if( m_hInternet != NULL )
		{
			InternetCloseHandle( m_hInternet );
			m_hInternet = NULL;
		}
	}

	// In InetIO.cpp
	extern BOOL IsProxyEnabled();

	int CBriefFS::Initialize( const std::wstring& device, MOUNT_TYPE mt)
	{
		m_deviceName = device;
		m_MountType = mt;

		if( !IsMountReadOnly( mt ) )
		{
			return EC_FS_READONLY;
		}

		std::wstring rooturl;
		if( !IsMountRuntime(mt) )
		{
			// 获取版本信息元文件
			PFSX::CVersionInfo versionFile;
			if( !PFSX::GetLocalVersion( device, versionFile ) )
			{
				return CEnv::GetLastError();
			}

			// 说明：在使用BriefFS之前，须保证.version.meta已经将URL设置为default类型。
			const PFSX::VersionUpdateInfo* pUpdateInfo = versionFile.GetUpdateInfoByType( PFSX::VERSION_DEFAULT );
			if ( pUpdateInfo == NULL  )
			{
				// 无效的版本信息文件
				return EC_INVALID_VERSION_INFO;
			}

			rooturl = pUpdateInfo->Url;
			if( rooturl.empty() )
			{
				assert( !rooturl.empty() );
				// 无效的版本信息文件
				return EC_INVALID_VERSION_INFO;
			}

			// 获取文件系统元文件
			std::wstring metafile = CPFSBase::GetMetaFileName();
			CBaseFile* basemetafile = NULL;
			if( CFileIO::IsDirectoryExisting( device) )
			{
				metafile = device + metafile;
				PFSX::CNativeFile* nativemetafile = new PFSX::CNativeFile();
				if( !nativemetafile->Open( metafile, FM_EXCL, FA_RDONLY))
				{
					delete nativemetafile;
					return CEnv::GetLastError();
				}

				basemetafile = nativemetafile;
			}
			else
			{
				std::wstring newname = device;
				CPFSBase* base = CPacketFileSystem::GetInstance().FindPFSBase( newname);
				if( NULL == base)
					return EC_INVALID_MOUNT_DEVICE;
				assert( CheckDirFileStringFormat( newname));

				metafile = newname + metafile;
				int nRes = base->OpenFile( metafile, FM_EXCL, FA_RDONLY, basemetafile);
				if( EC_SUCCESS != nRes)
					return nRes;
				m_BasePFS = base;
				m_strBasePath = newname;
				base->AddRefMountFSDevive( newname);
			}

			if( !CMetaFileRead::DoRead( *basemetafile, GetMetaInfoMap()) )
			{
				CEnv::CloseFile( basemetafile);
				return CEnv::GetLastError();
			}

			CEnv::CloseFile( basemetafile);
		}
		else
		{
			//
			// MT_RUNTIME 模式的Brief文件系统
			// 
			// 此类型的文件系统，不从本地读取版本号和Meta信息
			// 仅仅通过device确定url。
			//
			// 对于根mount，device应该是一个url，比如http://127.0.0.1/mhzx等,
			// 对于递归mount,其父系统也应该是Brief,且为runtime模式，此时的device应该是/a/b模式
			// 假设/a的device是 http://127.0.0.1/mhzx
			// /a/b换算结果是 http://127.0.0.1/mhzx/b,类似的
			// /a/b/c换算结果是 http://127.0.0.1/mhzx/b/c
			//
			// 此种类型的BriefFS，在Initialize()时并不初始化metainfo map，
			// 而是通过系统暴露的CEnv::SetMetaInfos()接口从调用方提供给FS系统，或者根本不提供。
			// 注意，如果不提供metainfo map信息，IsFileExisting()不能提供有效的文件系统信息给调用方。
			// 这种情况下，无论此文件物理上是否存在，不由IsFileExisting()确定。
			//
			std::wstring newname = device;
			CPFSBase* base = CPacketFileSystem::GetInstance().FindPFSBase( newname);
			if( NULL != base )
			{
				CBriefFS *baseFS = dynamic_cast<CBriefFS *>(base);
				if( baseFS == NULL )
				{
					// 如果有base，base必须是BriefFS
					return EC_INVALID_MOUNT_DEVICE;
				}

				if( !IsMountRuntime( base->GetMountType() ) )
				{
					// 其父系统也应该是runtime模式
					return EC_INVALID_MOUNT_DEVICE;
				}

				assert( CheckDirFileStringFormat( newname) );
				base->AddRefMountFSDevive( newname);
			}

			m_BasePFS = base;
			m_strBasePath = newname;
			rooturl = newname;
		}

		//
		// 在使用代理的情况下, InetCheckConnection() 会导致无法连接那些仅能通过代理连接的服务器
		// 所以,这里不再进行 InetCheckConnection().
		//

		// 必须使用带协议的url进行检测
		//if( !InetCheckConnection(rooturl.c_str()) )
		//{
		//	OutputDebugString( TEXT( "Cannot connect to " ) );
		//	OutputDebugString( rooturl.c_str() );
		//	OutputDebugString( TEXT("\n") );
		//	return ::GetLastError();
		//}
		
		// 分析URL，获取HostName，Port，BaseURL
		DWORD dwError = 0;
		BOOL bRet = TRUE;
		URL_COMPONENTS UrlComponents = {0};
		UrlComponents.dwStructSize = sizeof(URL_COMPONENTS);
		UrlComponents.dwUrlPathLength = 1;
		UrlComponents.dwHostNameLength = 1;
		UrlComponents.nPort = INTERNET_DEFAULT_HTTP_PORT;
		UrlComponents.dwSchemeLength = 1;
		UrlComponents.dwExtraInfoLength = 1;
		bRet = InternetCrackUrl( rooturl.c_str(),0, 0, &UrlComponents );
		if ( bRet && UrlComponents.dwHostNameLength > 0 )
		{
			if( UrlComponents.dwUrlPathLength > 0 ) // Maybe empty path
				m_baseUrl.assign( UrlComponents.lpszUrlPath, UrlComponents.dwUrlPathLength );
			m_hostName.assign( UrlComponents.lpszHostName, UrlComponents.dwHostNameLength );
		}
		else
		{
			if( bRet )
			{
				::SetLastError(dwError = ERROR_INTERNET_INVALID_URL);
				bRet = FALSE;
			}
			else
			{
				dwError = ::GetLastError();
			}
		}

		if ( !bRet )
		{
			return dwError;
		}

		//
		// INTERNET_OPEN_TYPE_DIRECT - 直接连接,不通过代理
		// INTERNET_OPEN_TYPE_PRECONFIG - 使用IE或其他工具对注册表项配置的代理进行连接,如果没有代理配置信息,则直接连接
		//

		// 优先使用注册表配置
		HINTERNET hInternet = InternetOpen(_GetAgentName(), 
			INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
		if ( hInternet == NULL )
		{
			dwError = ::GetLastError();
			::PFS::CEnv::GetOutLogInstance()->Print(TEXT("InternetOpen(PRECONFIG) failed: %d.\n"), dwError );
			return dwError;
		}

		DWORD dwTimeout = 5000; // 5s
		bRet = InternetSetOptionW( hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwTimeout, sizeof(DWORD) );
		if( !bRet )
		{
			dwError = ::GetLastError();
			::PFS::CEnv::GetOutLogInstance()->Print(TEXT("PRECONFIG:InternetSetOptionW(INTERNET_OPTION_CONNECT_TIMEOUT, %u): %d.\n"), dwTimeout, dwError );
		}

		HINTERNET hConnect = InternetConnect(
			hInternet,
			m_hostName.c_str(),
			UrlComponents.nPort, // INTERNET_INVALID_PORT_NUMBER, // INTERNET_DEFAULT_HTTP_PORT,
			TEXT(""), // LPCTSTR lpszUsername,
			TEXT(""), // LPCTSTR lpszPassword,
			INTERNET_SERVICE_HTTP,
			0,
			0);
		if ( hConnect == NULL )
		{
			dwError = ::GetLastError();

			// 关闭PRECONFIG模式的hInternet句柄
			InternetCloseHandle( hInternet );

			// 检查代理设置是否启用，如果没有启用，放弃直连模式的重试
			// 因为这种情况下，INTERNET_OPEN_TYPE_PRECONFIG就是直连。
			if( !IsProxyEnabled() )
			{
				return dwError;
			}

			// 更改模式再次测试非代理方案是否能够连接上服务器
			hInternet = InternetOpen(_GetAgentName(), 
				INTERNET_OPEN_TYPE_DIRECT, NULL, NULL, 0);
			if( hInternet == NULL )
			{
				dwError = ::GetLastError();
				PFS::CEnv::GetOutLogInstance()->Print(L"Can't connect to %s on pre-config(%d).\n", m_hostName.c_str(), dwError );
				return dwError;
			}

			BOOL bRet = InternetSetOptionW( hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwTimeout, sizeof(DWORD) );
			if( !bRet )
			{
				dwError = ::GetLastError();
				::PFS::CEnv::GetOutLogInstance()->Print(TEXT("DIRECT:InternetSetOptionW(INTERNET_OPTION_CONNECT_TIMEOUT, %u): %d.\n"), dwTimeout, dwError );
			}

			hConnect = InternetConnect( hInternet,
				m_hostName.c_str(), UrlComponents.nPort,
				TEXT(""), TEXT(""), INTERNET_SERVICE_HTTP, 0, 0);
			if ( hConnect == NULL )
			{
				dwError = ::GetLastError();
				PFS::CEnv::GetOutLogInstance()->Print(L"Can't connect to %s directly(%d).\n", m_hostName.c_str(), dwError );
				InternetCloseHandle( hInternet );
				return dwError;
			}
		}

		// save the connection handles
		m_hInternet = hInternet;
		m_hConnect = hConnect;

		CalcNativeDevicePath();

		return EC_SUCCESS;
	}

	bool CBriefFS::IsFileExisting( const std::wstring& filename)
	{
		if( filename == GetMetaFileName() )
		{
			// meta文件总是存在
			return true;
		}
		else
		{
			const CMetaInfoMap& mim = GetMetaInfoMap();
			return ( mim.find( filename) != mim.end()) ;
		}
	}

	int CBriefFS::OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file)
	{
		if( (FA_RDONLY != fa) || (FM_EXCL != fm))
			return EC_FILE_READONLY;
		
		std::wstring objName = m_baseUrl;

		// RUNTIME系统来说， 或对于.files.meta.文件本身，不从metainfomap中查找
		if( IsMountRuntime(GetMountType()) || filename == GetMetaFileName() )
		{
			objName += filename;
		}
		else
		{
			CMetaInfoMap& mim = GetMetaInfoMap();
			CMetaInfoMap::iterator itor = mim.find( filename);
			if( itor == mim.end())
				return EC_FILE_NOT_FOUND;

			if( itor->second.GetFileDownloadURI().empty())
				objName += filename;
			else
				objName += itor->second.GetFileDownloadURI();
		}

		CBriefBaseFile* basefile = new CBriefBaseFile( this);
		if( !basefile->Open(m_hConnect, objName))
		{
			delete basefile;
			return EC_FILE_URLOPENERROR;
		}
		file = basefile;

		CMutex::CScoped lock( m_cs );
		m_setFiles.insert( file );
		return EC_SUCCESS;
	}

	void CBriefFS::CloseAllFiles()
	{
		CBriefFileSet temp;
		{
			CMutex::CScoped lock( m_cs );
			temp.swap(m_setFiles);
		}

		for( CBriefFileSet::iterator itor = temp.begin(); itor != temp.end(); ++itor)
			CEnv::CloseFile( *itor);
		
		if( m_hConnect != NULL )
		{
			InternetCloseHandle( m_hConnect );
			m_hConnect = NULL;
		}
		if( m_hInternet != NULL )
		{
			InternetCloseHandle( m_hInternet );
			m_hInternet = NULL;
		}
	}

} // namespace PFS
