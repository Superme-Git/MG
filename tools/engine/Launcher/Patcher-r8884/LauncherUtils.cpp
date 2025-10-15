/////////////////////////////////////////////////////////////////////////
//
// LauncherUtils.cpp
//
// author : yanghaibo
//
#include "StdAfx.h"
#include "LauncherUtils.h"
#include "../InetUtils/InetUtils.h"
#include "../../pfs/pfs2.h"
#include "LauncherConfig.h"
#include "UpdateManager.h"
#include "ErrorInfo.h"
#include "GlobalFunction.h"
#include "KHLogger.h"
#include "resource.h"
//
// ��ʽ��ϵͳAPI�����Ĵ�����Ϣ
//
void __cdecl FormatErrorMessage( DWORD dwErrorCode, CString* pMessage, ... )
{
	va_list vl;
	va_start(vl, pMessage);

	LPTSTR lpBuffer = NULL;
	::FormatMessage(
		FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_ALLOCATE_BUFFER, NULL, 
		dwErrorCode, 0, (LPTSTR)&lpBuffer, 0, &vl);
	if( lpBuffer != NULL )
	{
		*pMessage = lpBuffer;
		LocalFree(lpBuffer);
	}

	va_end(vl);
}

//
// ���������Ϣ
//
void __cdecl ReportMessage( LPCTSTR lpszMessageFormat, ... )
{
	va_list vl;
	va_start(vl, lpszMessageFormat);
	TCHAR buffer[ 1024 ];
	_vstprintf_s( buffer, _countof(buffer), lpszMessageFormat, vl);
	AfxMessageBox( buffer, MB_OK|MB_ICONERROR );
	va_end(vl);
}

BOOL UnzipResource( LPCTSTR lpszInputZipFile, LPCTSTR lpszOutputDir )
{
	ASSERT( lpszOutputDir != NULL && lpszInputZipFile != NULL );
	
	const std::wstring WEB_SRC = L"/web_src";
	const std::wstring WEB_DES = L"/web_des";
	
	bool res = false;
	do
	{
		res = PFS::CEnv::Mount( WEB_SRC, lpszInputZipFile, PFS::FST_ZIP, PFS::MT_READONLY );
		if( !res ) { break; }

		res = PFS::CEnv::CheckMetaInfo( WEB_SRC );
		if( !res ) { break; }

		PFS::CMetaInfoMap paths ;
		res = PFS::CEnv::GetMetaInfos( WEB_SRC, paths );
		if( !res ) { break; }

		res = PFS::CEnv::Mount( WEB_DES, lpszOutputDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if( !res ) { break; }

		for( PFS::CMetaInfoMap::const_iterator i=paths.begin(), e=paths.end(); i != e; ++i )
		{
			const std::wstring& path = i->first;
			if( std::find_if( path.begin(), path.end(), ::iswupper) != path.end())
			{
				size_t nLen = std::wstring(L"thumbs.db").size();
				if( path.size() >= nLen && 
					_wcsicmp( path.substr(path.size()-nLen).c_str(), L"thumbs.db" ) == 0 )
				{
					continue;
				}
			}
			res = PFS::CEnv::FileCopy( WEB_SRC + path , WEB_DES + path, false);
			if( !res ) { break; }
		}
	}
	while(0);

	int nLastError = 0;
	if( !res )
		nLastError = PFS::CEnv::GetLastError();

	PFS::CEnv::Unmount( WEB_SRC);
	PFS::CEnv::Unmount( WEB_DES);

	if( !res )
		PFS::CEnv::SetLastError(nLastError);

	return res;
}

BOOL DeployResource( LPCTSTR szResourcePath, LPCTSTR szLocalPagePath)
{
	CString pathOrg = szLocalPagePath;
	pathOrg.TrimRight( L"/\\");
	CString pathCur( pathOrg);
	CString temp( pathCur);
	pathCur += L".1";
	temp += L".2";

	// ɾ����ʷ���������������ļ�
	if( ::PathFileExistsW( pathCur.GetString() ) )
	{
		DeleteCacheFolder( pathCur.GetString() );
	}
	if( ::PathFileExistsW( temp.GetString() ) )
	{
		DeleteCacheFolder( temp.GetString() );
	}

	// �Ƚ�ѹ����ʱĿ¼�ٸ�����������;������ƻ�Դ�ļ�
	BOOL bRet = UnzipResource( szResourcePath, pathCur);
	if( bRet)
	{
		if ( ::PathFileExistsW( pathOrg.GetString() ) )
		{
			// ���ԭ����Ŀ¼���ڣ�������

			bRet = ( 0 == _wrename( pathOrg.GetString(), temp.GetString()));
			if ( !bRet )
			{
				// webpageĿ¼�µ��ļ�����webcontrol���أ��������rename�����ᵼ��ERROR_ACCESS_DENIED����
				// ����ͨ���ӳٲ����ԵĻ��ƴ�������⡣
				DWORD dwLastError = ::GetLastError();
				if( dwLastError == ERROR_ACCESS_DENIED ) 
				{
					// �ٳ���3��
					int nTryCount = 3;
					do 
					{
						Sleep( 2000 );
						bRet = ( 0 == _wrename( pathOrg.GetString(), temp.GetString() ) );
					} while( !bRet && --nTryCount );

					if( !bRet )
					{
						KHMsgLog_Error(_T("_wrename(from %s, to %s) failed(%d)!\n"),  
							pathOrg.GetString(), temp.GetString(), ::GetLastError() );
					}
				}
			}
		}

		if( bRet )
		{
			bRet = ( 0 == _wrename( pathCur.GetString(), pathOrg.GetString()));
			if( bRet )
			{
				DeleteCacheFolder( temp.GetString());
			}
			else
			{
				KHMsgLog_Error(_T("_wrename(from %s, to %s) failed(%d)!\n"),  
					pathCur.GetString(), pathOrg.GetString(), ::GetLastError() );

				// �����ٰ�ԭ����Ŀ¼�Ļ�ȥ
				_wrename( temp.GetString(), pathOrg.GetString());
				DeleteCacheFolder( pathCur.GetString());
			}
		}
		else
		{
			KHMsgLog_Error(_T("_wrename(from %s, to %s) failed(%d)!\n"),  
				pathOrg.GetString(), temp.GetString(), ::GetLastError() );

			DeleteCacheFolder( pathCur.GetString());
		}
	}
	else
	{
		KHMsgLog_Error(_T("UnzipResource(from %s, to %s) failed(%d)!\n"),  
			szResourcePath, pathCur.GetString(), PFS::CEnv::GetLastError() );

		// ��ѹ��ʧ��,ɾ������������ʱ�ļ��м����ļ�
		if( ::PathFileExistsW( pathCur.GetString() ) )
		{
			DeleteCacheFolder( pathCur.GetString() );
		}
	}

	return bRet;
}

BOOL CDownloadWebPageThread::DownloadOnlineResourceZip()
{
	CString szResourceUrl = GetLauncherOnlineResourceUrl();
	CString szResourceDir = GetLauncherUIResourceDir();
	TCHAR szResourcePath[MAX_PATH];
	DWORD dwError = InetHttpDownload(
		szResourceUrl.GetString(), szResourceDir.GetString(), 
		szResourcePath, MAX_PATH );

	BOOL bRet = ( 0 == dwError );
	if ( bRet )
	{
		m_strResourcePath = szResourcePath;
		return TRUE;
	}
	else
	{
		KHMsgLog_Error(_T("InetHttpDownload(from %s, to %s) failed(%d)!\n"),  
			szResourceUrl.GetString(), szResourceDir.GetString(), dwError );
	}

	return bRet;
}

IMPLEMENT_DYNCREATE(CDownloadWebPageThread, CWinThread)

BOOL CDownloadWebPageThread::InitInstance()
{
	return TRUE;
}

BOOL CDownloadWebPageThread::DeployWebPageResource( CString strResourcePath )
{
	BOOL bRet = FALSE;
	if( ::PathFileExistsW(strResourcePath) )
	{
		CString strOfflineDirectory = GetLauncherLocalPagePath();
		bRet = DeployResource( strResourcePath, strOfflineDirectory );
	}
	return bRet;
}

BOOL CDownloadWebPageThread::Deploy()
{
	return DeployWebPageResource(m_strResourcePath);
}

int CDownloadWebPageThread::Run()
{
	BOOL bRet = !!DownloadOnlineResourceZip();
	if(  m_pMainWnd != NULL )
	{
		m_pMainWnd->PostMessage( WM_DOWNLOADWEBPAGE_END, (WPARAM)bRet, 0);
	}

	// ���ֱ���������ֹ�����һ��launcher.zip���Ƿ������Զ���ѹ���أ�
	// ����DownloadOnlineResourceZip()����ʧ�ܣ�m_strResourcePath���ᱻ��ʼ����
	// ���ԣ������޷�ȷ�����صİ������֡�
	// ���⣬Ϊ��ʵ�ֵ���ִ��DeployResource������ͨ��m_bAutoDeploy���п��ơ�
	if( m_bAutoDeploy )
	{
		bRet = Deploy();

		if( m_pMainWnd != NULL )
		{
			m_pMainWnd->PostMessage( WM_DEPLOYWEBPAGE_END, (WPARAM)bRet, 0);
		}
	}
	else
	{
		// ������Զ������߳��˳��󣬲�ִ��delete this.
		m_bAutoDelete = FALSE;
	}

	return 0;
}
