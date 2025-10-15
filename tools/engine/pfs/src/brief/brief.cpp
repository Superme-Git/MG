///=============================================================================
/// Product   : PFS
/// Project   : pfslib
/// Module    : brief.cpp
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Brief �ļ�ϵͳʵ����
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
			// ��ȡ�汾��ϢԪ�ļ�
			PFSX::CVersionInfo versionFile;
			if( !PFSX::GetLocalVersion( device, versionFile ) )
			{
				return CEnv::GetLastError();
			}

			// ˵������ʹ��BriefFS֮ǰ���뱣֤.version.meta�Ѿ���URL����Ϊdefault���͡�
			const PFSX::VersionUpdateInfo* pUpdateInfo = versionFile.GetUpdateInfoByType( PFSX::VERSION_DEFAULT );
			if ( pUpdateInfo == NULL  )
			{
				// ��Ч�İ汾��Ϣ�ļ�
				return EC_INVALID_VERSION_INFO;
			}

			rooturl = pUpdateInfo->Url;
			if( rooturl.empty() )
			{
				assert( !rooturl.empty() );
				// ��Ч�İ汾��Ϣ�ļ�
				return EC_INVALID_VERSION_INFO;
			}

			// ��ȡ�ļ�ϵͳԪ�ļ�
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
			// MT_RUNTIME ģʽ��Brief�ļ�ϵͳ
			// 
			// �����͵��ļ�ϵͳ�����ӱ��ض�ȡ�汾�ź�Meta��Ϣ
			// ����ͨ��deviceȷ��url��
			//
			// ���ڸ�mount��deviceӦ����һ��url������http://127.0.0.1/mhzx��,
			// ���ڵݹ�mount,�丸ϵͳҲӦ����Brief,��Ϊruntimeģʽ����ʱ��deviceӦ����/a/bģʽ
			// ����/a��device�� http://127.0.0.1/mhzx
			// /a/b�������� http://127.0.0.1/mhzx/b,���Ƶ�
			// /a/b/c�������� http://127.0.0.1/mhzx/b/c
			//
			// �������͵�BriefFS����Initialize()ʱ������ʼ��metainfo map��
			// ����ͨ��ϵͳ��¶��CEnv::SetMetaInfos()�ӿڴӵ��÷��ṩ��FSϵͳ�����߸������ṩ��
			// ע�⣬������ṩmetainfo map��Ϣ��IsFileExisting()�����ṩ��Ч���ļ�ϵͳ��Ϣ�����÷���
			// ��������£����۴��ļ��������Ƿ���ڣ�����IsFileExisting()ȷ����
			//
			std::wstring newname = device;
			CPFSBase* base = CPacketFileSystem::GetInstance().FindPFSBase( newname);
			if( NULL != base )
			{
				CBriefFS *baseFS = dynamic_cast<CBriefFS *>(base);
				if( baseFS == NULL )
				{
					// �����base��base������BriefFS
					return EC_INVALID_MOUNT_DEVICE;
				}

				if( !IsMountRuntime( base->GetMountType() ) )
				{
					// �丸ϵͳҲӦ����runtimeģʽ
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
		// ��ʹ�ô���������, InetCheckConnection() �ᵼ���޷�������Щ����ͨ���������ӵķ�����
		// ����,���ﲻ�ٽ��� InetCheckConnection().
		//

		// ����ʹ�ô�Э���url���м��
		//if( !InetCheckConnection(rooturl.c_str()) )
		//{
		//	OutputDebugString( TEXT( "Cannot connect to " ) );
		//	OutputDebugString( rooturl.c_str() );
		//	OutputDebugString( TEXT("\n") );
		//	return ::GetLastError();
		//}
		
		// ����URL����ȡHostName��Port��BaseURL
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
		// INTERNET_OPEN_TYPE_DIRECT - ֱ������,��ͨ������
		// INTERNET_OPEN_TYPE_PRECONFIG - ʹ��IE���������߶�ע��������õĴ����������,���û�д���������Ϣ,��ֱ������
		//

		// ����ʹ��ע�������
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

			// �ر�PRECONFIGģʽ��hInternet���
			InternetCloseHandle( hInternet );

			// �����������Ƿ����ã����û�����ã�����ֱ��ģʽ������
			// ��Ϊ��������£�INTERNET_OPEN_TYPE_PRECONFIG����ֱ����
			if( !IsProxyEnabled() )
			{
				return dwError;
			}

			// ����ģʽ�ٴβ��ԷǴ������Ƿ��ܹ������Ϸ�����
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
			// meta�ļ����Ǵ���
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

		// RUNTIMEϵͳ��˵�� �����.files.meta.�ļ���������metainfomap�в���
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
