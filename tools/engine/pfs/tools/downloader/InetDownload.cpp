#include "StdAfx.h"
#include "InetDownload.h"
#include "../../pfs2.h"
#include "../../InetFile.h"
#include "../../FileEx.h"
#include <Shlwapi.h>
#pragma comment( lib, "wininet.lib")
#pragma comment( lib, "Shlwapi.lib" )

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

DWORD Connect(std::wstring rooturl, std::wstring& strbaseUrl,
			  std::pair<HINTERNET, HINTERNET>& rHandles)
{
	rHandles.first = NULL;
	rHandles.second = NULL;

	std::wstring baseUrl, hostName;

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
			baseUrl.assign( UrlComponents.lpszUrlPath, UrlComponents.dwUrlPathLength );
		hostName.assign( UrlComponents.lpszHostName, UrlComponents.dwHostNameLength );
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

	// 优先使用注册表配置
	HINTERNET hInternet = InternetOpen(_GetAgentName(), 
		INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
	if ( hInternet == NULL )
	{
		return GetLastError();
	}

	HINTERNET hConnect = InternetConnect(
		hInternet,
		hostName.c_str(),
		UrlComponents.nPort,
		TEXT(""),
		TEXT(""),
		INTERNET_SERVICE_HTTP,
		0,
		0);
	if ( hConnect == NULL )
	{
		// 关闭PRECONFIG模式的hInternet句柄
		InternetCloseHandle( hInternet );

		// 更改模式再次测试非代理方案是否能够连接上服务器
		hInternet = InternetOpen(_GetAgentName(), 
			INTERNET_OPEN_TYPE_DIRECT, NULL, NULL, 0);
		if( hInternet == NULL )
		{
			TRACE(L"Can't connect to %s", hostName.c_str() );
			return GetLastError();
		}

		hConnect = InternetConnect( hInternet,
			hostName.c_str(), UrlComponents.nPort,
			TEXT(""), TEXT(""), INTERNET_SERVICE_HTTP, 0, 0);

		if ( hConnect == NULL )
		{
			TRACE(L"Can't connect to %s", hostName.c_str() );
			InternetCloseHandle( hInternet );
			return GetLastError();
		}
	}

	// save the connection handles
	rHandles.first = hInternet;
	rHandles.second = hConnect;
	strbaseUrl = baseUrl;

	return ERROR_SUCCESS;
}

void Disconnect( std::pair<HINTERNET, HINTERNET>& rHandles )
{
	if( rHandles.second )
	{
		InternetCloseHandle( rHandles.second );
		rHandles.second = NULL;
	}

	if( rHandles.first )
	{
		InternetCloseHandle( rHandles.first );
		rHandles.first = NULL;
	}
}

static bool _SaveInetFile( PFSX::CInetFile& source, CFile& dest )
{
	PFSX::CInetFile*	fsrc = &source;
	CFile*	fdst = &dest;
	const int	nBufferSize = 4096;
	unsigned char byteBuffer[nBufferSize];
	bool		bResult = true;
	size_t		s = 0;
	while( s = fsrc->Read( byteBuffer, nBufferSize ) )
	{
		if( PFS::FILE_SIZE_ERROR == s )
			return false;
		fdst->Write( byteBuffer, (UINT)s);
	}

	return true;
}

static bool IsDirectoryExisting( const std::wstring& dir)
{
	DWORD	dwAttribute = GetFileAttributesW( dir.c_str());
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY == ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

static bool CheckCreateParentDirectory( const std::wstring& filename)
{
	std::wstring fn = filename; 
	std::replace(fn.begin(), fn.end(), L'/', L'\\');
	std::wstring::size_type	posoffset = 0;
	std::wstring::size_type s = 0;
	while( (s = fn.find( L'\\', posoffset)) != std::wstring::npos )
	{
		std::wstring substring = fn.substr( 0, s);
		if( !IsDirectoryExisting( substring))
		{
			if( !CreateDirectoryW( substring.c_str(), NULL) )
			{
				return false;
			}
		}
		posoffset = s + 1;
	}

	return true;
}

bool Download( HINTERNET hConnect, std::wstring strBaseUrl, std::wstring objectName, std::wstring saveTo )
{
	PFSX::CInetFile file;
	if( !file.Open( hConnect, strBaseUrl+objectName ) )
	{
		TRACE(TEXT("Open(hConnect:%x,%s) failed (%d,%d)!\n"), hConnect, (strBaseUrl+objectName).c_str(), 
			::GetLastError(), PFS::CEnv::GetLastError() );
		return false;
	}

	std::wstring fullpath = saveTo + objectName;
	if( !CheckCreateParentDirectory(fullpath) )
	{
		TRACE(TEXT("CheckCreateParentDirectory(%s) failed!\n"), fullpath.c_str());
		return false;
	}

	CFile nativeFile;
	if( !nativeFile.Open( fullpath.c_str(), 
		CFile::modeWrite|CFile::modeCreate|CFile::typeBinary|CFile::shareDenyWrite, NULL) )
	{
		TRACE(TEXT("CFile::Open(%s) failed!\n"), fullpath.c_str());
		return false;
	}

	return _SaveInetFile( file, nativeFile );
}


bool Download( HINTERNET hConnect, std::wstring objectName, std::wstring saveTo )
{
	PFSX::CInetFile file;
	if( !file.Open( hConnect, objectName ) )
	{
		TRACE(TEXT("Open(hConnect:%x,%s) failed (%d,%d)!\n"), hConnect, objectName.c_str(), 
			::GetLastError(), PFS::CEnv::GetLastError() );
		return false;
	}

	std::wstring fullpath = saveTo + objectName;
	if( !CheckCreateParentDirectory(fullpath) )
	{
		TRACE(TEXT("CheckCreateParentDirectory(%s) failed!\n"), fullpath.c_str());
		return false;
	}

	CFile nativeFile;
	if( !nativeFile.Open( fullpath.c_str(), 
		CFile::modeWrite|CFile::modeCreate|CFile::typeBinary|CFile::shareDenyWrite, NULL) )
	{
		TRACE(TEXT("CFile::Open(%s) failed!\n"), fullpath.c_str());
		return false;
	}

	return _SaveInetFile( file, nativeFile );
}

//////////////////////////////////////////////////////////////////////////


IMPLEMENT_DYNCREATE(CDownloadThread, CWinThread)

BOOL CDownloadThread::InitInstance()
{
	m_bStop = FALSE;

	return TRUE;
}

void CDownloadThread::Stop( BOOL bWait )
{
	HANDLE hThread = m_hThread;
	m_bAutoDelete = TRUE;
	m_bStop = TRUE;

	if( bWait )
	{
		WaitForSingleObject(hThread, INFINITE);
	}
}

int CDownloadThread::Run()
{
	std::pair< HINTERNET, HINTERNET> handles;
	DWORD dwRet = Connect( m_strRootUrl, m_strBaseUrl, handles );
	if ( dwRet != ERROR_SUCCESS )
	{
		return -1;
	}

	wstringlist::const_iterator itr = m_strFileList.begin();
	wstringlist::const_iterator itre = m_strFileList.end();
	UINT nCount = 0;

	// 逐一下载
	try
	{
		if( !m_strBaseUrl.empty() )
		{
			for ( ; itr != itre; ++ itr )
			{
				if( m_bStop )
				{
					Disconnect( handles );
					return -3;
				}

				bool bRet = Download( handles.second,  m_strBaseUrl, *itr, m_strSaveTo  );
				if ( bRet )
				{
					TRACE( TEXT("%s%s download ok.\n"), m_strBaseUrl.c_str(), (*itr).c_str() );
				}
			}
		}
		else
		{
			for ( ; itr != itre; ++ itr )
			{

				if( m_bStop )
				{
					Disconnect( handles );
					return -3;
				}


				bool bRet = Download( handles.second,  *itr, m_strSaveTo  );
				if ( bRet )
				{
					TRACE( TEXT("%s download ok.\n"), (*itr).c_str() );
				}
			}
		}
	}
	catch (...)
	{
		Disconnect( handles );
		return -2;
	}

	Disconnect( handles );
	return 0;
}
