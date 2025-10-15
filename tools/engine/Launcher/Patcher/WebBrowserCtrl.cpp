// WebBrowserCtrl.cpp : implementation file
//

#include "stdafx.h"
#include "Launcher.h"
#include "WebBrowserCtrl.h"
#include "LauncherConfig.h"
#include "KHLogger.h"
#include "TextId.h"
#include "TextManager.h"

// CWebBrowserCtrl

IMPLEMENT_DYNCREATE(CWebBrowserCtrl, CHtmlView)

CWebBrowserCtrl::CWebBrowserCtrl()
{
	m_bLocalVersionOK = false;
	m_bRemoteVersionOK = false;
}

CWebBrowserCtrl::~CWebBrowserCtrl()
{
}

BOOL CWebBrowserCtrl::PreTranslateMessage(MSG* pMsg)
{
	if ( pMsg->message == WM_KEYDOWN && pMsg->wParam == VK_F5 )
	{
		// 为了处理F5/Refresh页面刷新，
		// 我们不能通过WebBrowser控件的DownloadComplete事件来实现，
		// 无奈之下，只好通过这个并不地道的按键拦截方式来处理。
		CComQIPtr<IHTMLDocument2> spDocument2 = GetHtmlDocument();
		if( spDocument2 != NULL )
		{
			// 取得当前文档的URL，执行Navigate2而不执行Refresh,
			// 因为Refresh时我们无法更新版本信息
			CComBSTR currentUrl;
			spDocument2->get_URL( &currentUrl );
			if( currentUrl.Length() > 0)
			{
				Navigate2( currentUrl );
			}
		}

		return TRUE;
	}
	else
	{
		return CHtmlView::PreTranslateMessage(pMsg);
	}
}

HRESULT CWebBrowserCtrl::OnShowContextMenu(DWORD, LPPOINT, LPUNKNOWN, LPDISPATCH)
{
	// 屏蔽右键上下文菜单
	//return S_FALSE;
	return S_OK;
}

BEGIN_MESSAGE_MAP(CWebBrowserCtrl, CHtmlView)
END_MESSAGE_MAP()

// CWebBrowserCtrl message handlers

void CWebBrowserCtrl::OnNavigateError(LPCTSTR lpszURL, LPCTSTR lpszFrame, DWORD dwError, BOOL *pbCancel)
{
	// 屏蔽网页错误信息
	if( pbCancel )
	{
		*pbCancel = FALSE;
	}

	// CHtmlView::OnNavigateError(lpszURL, lpszFrame, dwError, pbCancel);
}

BOOL CWebBrowserCtrl::RequestVersionInfo()
{
	m_localVersion.Close();
	m_bLocalVersionOK = PFSX::GetLocalVersion( GetGameClientDir().GetString(), m_localVersion);
	if( !m_bLocalVersionOK )
	{
		// No version info
		CString strInfo;
		strInfo.Format( _T("在目录%s下找不到有效的版本信息!\n" ), GetGameClientDir().GetString() );
		KHMsgLog_Error( strInfo );
		return FALSE;
	}

	//
	// 特别说明:
	//	服务器的远程版本信息不再从这里获取，以避免由于网络问题导致patcher程序启动时窗口很长时间不显示的问题
	//
	//  [8/21/2009 yanghaibo]
	//

	//m_remoteVersion.Close();
	//m_bRemoteVersionOK = false;
	//const PFSX::VersionUpdateInfo* pUpdateInfo = m_localVersion.GetUpdateInfoByType( PFSX::VERSION_DEFAULT );
	//if( pUpdateInfo != NULL)
	//{
	//	m_bRemoteVersionOK = PFSX::GetRemoteVersion(pUpdateInfo->Url, m_remoteVersion);
	//	if( !m_bRemoteVersionOK )
	//	{
	//		CString strInfo;
	//		strInfo.Format( _T("无法获取最新的版本信息(%s)\n" ), pUpdateInfo->Url.c_str());
	//		KHMsgLog_Info( strInfo );
	//	}
	//}

	// 得不到远程版本信息我们也返回true
	return TRUE;	
}

BOOL CWebBrowserCtrl::UpdateVersionInfo()
{
	CComQIPtr<IHTMLDocument3> spDocument3 = GetHtmlDocument();
	if(spDocument3 == NULL)
	{
		return FALSE;
	}

	CComPtr<IHTMLElement> spElem = NULL;
	spDocument3->getElementById(CComBSTR(L"top"), &spElem);
	if( spElem == NULL )
	{
		return FALSE;
	}

	if( !m_bLocalVersionOK )
	{
		return FALSE;
	}

	PFSX::CVersionInfo& localVersionInfo = m_localVersion;
	PFSX::CVersionInfo& remoteVersionInfo = m_remoteVersion;

	DWORD dwVersion = localVersionInfo.m_Version;
	//CString strVersionInfo;
	std::wstring strVersionInfo;
	WORD wVersion = HIWORD(dwVersion);
	tm when = {0};
	_localtime32_s( &when, &localVersionInfo.m_ReleaseDate );

	if( m_bRemoteVersionOK && localVersionInfo != remoteVersionInfo )
	{
		tm releaseDate = {0};
		_localtime32_s( &releaseDate, &remoteVersionInfo.m_ReleaseDate );

		DWORD dwVersionRemote = remoteVersionInfo.m_Version;
		WORD wVersionRemote = HIWORD(dwVersionRemote);
		if( when.tm_year == releaseDate.tm_year )
		{
			//HTM_LOCAL_VERSION_1	=				601,	
			//HTM_LOCAL_VERSION_2 =				602,	
			//HTM_LOCAL_VERSION_3 =				603,	
			//HTM_RELEASE_DATE_1 =				604,	
			//HTM_RELEASE_DATE_2 =				605,	
			//HTM_RELEASE_DATE_3 =				606,	
			//HTM_LATEST_VERSION =				607,	
			//HTM_RELEASE_SN =					608,	
			//HTM_BAD_LOCAL_INSTALLATION =		609,	
			//HTM_VERSION_INFO_FORMAT_0 =			620,	
			//HTM_VERSION_INFO_FORMAT_1 =			621,	
			//HTM_VERSION_INFO_FORMAT_2 =			622,	
			//HTM_VERSION_INFO_FORMAT_3 =			623,	
			//HTM_VERSION_INFO_FORMAT_4 =			624,	

			// "本地版本:<span>%hu.%hu.%hu</span> 發佈日期:<span>%u月%u日</span>  最新版本:<span>%hu.%hu.%hu</span> 發佈日期:<span>%u月%u日</span>
			std::wstring strLocalVersion1 = GlobalFormatText(HTM_LOCAL_VERSION_1, (WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion));
			std::wstring strLocalReleaseDate1 = GlobalFormatText(HTM_RELEASE_DATE_1, when.tm_mon+1, when.tm_mday );
			std::wstring strLatestVersion= GlobalFormatText(HTM_LATEST_VERSION,(WORD)HIBYTE(wVersionRemote), (WORD)LOBYTE(wVersionRemote), LOWORD(dwVersionRemote) );
			std::wstring strLatestReleaseDate1 = GlobalFormatText(HTM_RELEASE_DATE_1, releaseDate.tm_mon+1, releaseDate.tm_mday );
			
			strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_0,
				strLocalVersion1.c_str(), strLocalReleaseDate1.c_str(), strLatestVersion.c_str(), strLatestReleaseDate1.c_str() );

			//strVersionInfo.Format(_T("本地版本:<span>%hu.%hu.%hu</span> 發佈日期:<span>%u月%u日</span>  最新版本:<span>%hu.%hu.%hu</span> 發佈日期:<span>%u月%u日</span>" ),
			//	(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), 
			//	when.tm_mon+1, when.tm_mday,
			//	(WORD)HIBYTE(wVersionRemote), (WORD)LOBYTE(wVersionRemote), LOWORD(dwVersionRemote), 
			//	releaseDate.tm_mon+1, releaseDate.tm_mday);
		}
		else
		{
			/*
			std::wstring strLocalVersion2 = GlobalFormatText(HTM_LOCAL_VERSION_2, (WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion));
			std::wstring strLocalVersion3 = GlobalFormatText(HTM_LOCAL_VERSION_3, (WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion));
			*/

			//strVersionInfo.Format(_T("本地版本:<span>%hu.%hu.%hu</span>發佈日期:<span>%u.%u.%u</span>最新版本:<span>%hu.%hu.%hu</span>發佈日期:<span>%u.%u.%u</span>" ),
			//	(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), 
			//	when.tm_year+1900, 
			//	when.tm_mon+1, when.tm_mday,
			//	(WORD)HIBYTE(wVersionRemote), (WORD)LOBYTE(wVersionRemote), LOWORD(dwVersionRemote), 
			//	releaseDate.tm_year+1900, releaseDate.tm_mon+1, releaseDate.tm_mday);

			std::wstring strLocalVersion1 = GlobalFormatText(HTM_LOCAL_VERSION_1, (WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion));
			std::wstring strLocalReleaseDate2 = GlobalFormatText(HTM_RELEASE_DATE_2, when.tm_year+1900, when.tm_mon+1, when.tm_mday );
			std::wstring strLatestVersion= GlobalFormatText(HTM_LATEST_VERSION,(WORD)HIBYTE(wVersionRemote), (WORD)LOBYTE(wVersionRemote), LOWORD(dwVersionRemote) );
			std::wstring strLatestReleaseDate2 = GlobalFormatText(HTM_RELEASE_DATE_2, releaseDate.tm_year+1900, releaseDate.tm_mon+1, releaseDate.tm_mday );
			
			strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_1,
				strLocalVersion1.c_str(), strLocalReleaseDate2.c_str(), strLatestVersion.c_str(), strLatestReleaseDate2.c_str() );
		}
	}
	else
	{
		std::wstring strLocalVersion2 = GlobalFormatText(HTM_LOCAL_VERSION_2, (WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion));
		std::wstring strReleaseDate = GlobalFormatText(HTM_RELEASE_DATE_2, when.tm_year+1900, when.tm_mon+1, when.tm_mday );
		std::wstring strReleaseSN = GlobalFormatText(HTM_RELEASE_SN, LOWORD(dwVersion) );
		std::wstring strBadInstallation = GlobalGetText(HTM_BAD_LOCAL_INSTALLATION);

		if( m_bRemoteVersionOK )
		{
			//if( localVersionInfo.m_size > 0 )
			//{
			//	strVersionInfo.Format(
			//		_T("当前版本:<span>%hu.%hu.%hu[最新]</span>  "
			//		_T("发布序号:<span>%hu</span>  ")
			//		_T("文件大小:<span>%.2fG</span>  ")
			//		_T("发布日期:<span>%u-%u-%u</span>"),
			//		(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), LOWORD(dwVersion),
			//		localVersionInfo.m_size/1024.0f,
			//		when.tm_year+1900, when.tm_mon+1, when.tm_mday );
			//}
			//else
			//{
			// 对于文件大小为0的情况,我们不显示
			if( m_localVersion.IsValid() )
			{
				//strVersionInfo.Format(_T("   本地用戶端版本:<span>%hu.%hu.%hu [最新]</span>   發佈序号:<span>%hu</span>   發佈日期:<span>%u年%u月%u日</span>"),
				//	(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), LOWORD(dwVersion),
				//	when.tm_year+1900, when.tm_mon+1, when.tm_mday );

				std::wstring strLocalVersion3 = GlobalFormatText(HTM_LOCAL_VERSION_3,
					(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion));
				
				strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_3,
					strLocalVersion3.c_str(), strReleaseSN.c_str(), strReleaseDate.c_str() );
			}
			else
			{

				strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_4,
					strLocalVersion2.c_str(), strBadInstallation.c_str() );

				/*strVersionInfo.Format(_T("   本地用戶端版本:<span>%hu.%hu.%hu</span> <span>[存在問題，建議執行版本驗證]</span>"),
					(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion) );
				*/
			}
			//}
		}
		else
		{
			//// 没有获取到最新版本，只显示当前版本信息
			//if( localVersionInfo.m_size > 0 )
			//{
			//	strVersionInfo.Format(_T("当前版本:<span>%hu.%hu.%hu</span> 发布序号:<span>%hu</span>  文件大小:<span>%.2fG</span>  发布日期:<span>%u-%u-%u</span>"),
			//		(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), LOWORD(dwVersion),
			//		localVersionInfo.m_size/1024.0f,
			//		when.tm_year+1900, when.tm_mon+1, when.tm_mday );
			//}
			//else
			//{
			// 对于文件大小为0的情况,我们不显示
			if( m_localVersion.IsValid() )
			{
				//strVersionInfo.Format(_T("   本地用戶端版本:<span>%hu.%hu.%hu</span>   發佈序号:<span>%hu</span>    發佈日期:<span>%u年%u月%u日</span>"),
				//(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), LOWORD(dwVersion),
				//when.tm_year+1900, when.tm_mon+1, when.tm_mday );

				strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_2,
					strLocalVersion2.c_str(), strReleaseSN.c_str(), strReleaseDate.c_str() );
			}
			else
			{
				strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_4,
					strLocalVersion2.c_str(), strBadInstallation.c_str() );

				/*strVersionInfo.Format(_T("   本地用戶端版本:<span>%hu.%hu.%hu</span> <span>[存在問題，建議執行版本驗證]</span>"),
					(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion) );*/
			}
			//}
		}
	}

	spElem->put_innerHTML( CComBSTR(strVersionInfo.c_str()));

	return TRUE;
}

void CWebBrowserCtrl::OnBeforeNavigate2(LPCTSTR lpszURL, DWORD nFlags,
										LPCTSTR lpszTargetFrameName, CByteArray& baPostedData,
										LPCTSTR lpszHeaders, BOOL* pbCancel)
{
	RequestVersionInfo();
}

void CWebBrowserCtrl::OnDocumentComplete(LPCTSTR lpszURL)
{
	// 优先判断本地OfflineURL
	CString strLocalPathUrl = GetLauncherOfflineUrl();
	if ( strLocalPathUrl.CompareNoCase(lpszURL) == 0 )
	{
		UpdateVersionInfo();
	}
	else
	{
		CString strInfo;
		strInfo.Format( _T("%s\n" ), lpszURL );
		KHMsgLog( strInfo );		

		// 然后判断OnlineURL
		CString strOnlineUrl = GetLauncherOnlineUrl();
		if( strOnlineUrl.CompareNoCase(lpszURL) == 0 )
		{
			UpdateVersionInfo();
		}
	}
}

#if 0
void CWebBrowserCtrl::OnNavigateComplete2(LPCTSTR lpszURL)
{
	CString strUrl = lpszURL;
	// 不是index.html时，我们更新index.html上的版本信息
	// 这种情况可以用于 Navigate2 和 F5/Refresh 两种情况的页面刷新
	if ( strUrl.Right( _countof( _T("index.html")) -1 ) != _T("index.html") )
	{
		DWORD dwVersion = localVersionInfo.m_Version;
		CString strVersion;
		WORD wVersion = HIWORD(dwVersion);
		strVersion.Format(_T("%hu.%hu.%hu"),(WORD)HIBYTE(wVersion), (WORD)LOBYTE( wVersion), LOWORD(dwVersion) );

		if(spDocument3 != NULL)
		{
			CComPtr<IHTMLElement> spElem = NULL;
			spDocument3->getElementById(CComBSTR(L"version"), &spElem);
			if( spElem != NULL )
			{
				spElem->put_innerText( CComBSTR(strVersion));
			}
			spElem.Release();

			strVersion.Format(_T("%hu"),LOWORD(dwVersion) );
			spDocument3->getElementById(CComBSTR(L"releasing-SN"), &spElem);
			if( spElem != NULL )
			{
				spElem->put_innerText( CComBSTR(strVersion));
			}

			spElem.Release();
			tm when ={0};
			_localtime32_s( &when, &localVersionInfo.m_ReleaseDate );
			CString strTime;
			strTime.Format( L"%u-%u-%u", when.tm_year+1900, when.tm_mon+1, when.tm_mday );
			spDocument3->getElementById(CComBSTR(L"released-date"), &spElem);
			if( spElem != NULL )
			{
				spElem->put_innerText( CComBSTR(strTime));
			}
		}
	}

	// 是update.html时，我们更新update.html上的版本信息
	// 这种情况可以用于 Navigate2 和 F5/Refresh 两种情况的页面刷新
	if ( strUrl.Right( _countof( _T("update.html")) -1 ) == _T("update.html") )
	{
		// 下面的代码用于更改iframe中的数据（更新日志）
		// 问题是取服务器端的更新日志还是本地的更新日志
		// 如果显示本地的更新日志，可以采用如下的办法，如果需要显示服务器端的更新日志
		// 则需要获取服务器端的最新信息
		//
		CComQIPtr<IHTMLDocument2> spFrameDocument;
		CComQIPtr<IHTMLDocument2> spDocument2 = spDocument3;
		CComPtr <IHTMLFramesCollection2> spFrames;
		if ( FAILED( spDocument2->get_frames( &spFrames ) ) )
		{
			return;
		}

		long lCount = 0;
		spFrames->get_length(&lCount);
		for ( long iIndex = 0; iIndex < lCount; iIndex ++ )
		{
			CComVariant varIndex(iIndex);
			CComVariant varResult;
			spFrames->item( &varIndex, &varResult);
			if ( varResult.vt == VT_DISPATCH )
			{
				CComQIPtr <IHTMLWindow2> spFrameWindow = varResult.pdispVal;
				if( spFrameWindow != NULL )
				{
					CComBSTR bstrName;
					spFrameWindow->get_name( &bstrName );
					if( bstrName == L"update-frame" )
					{
						spFrameWindow->get_document(&spFrameDocument);
						break;
					}
				}
			}
		}

		spDocument3 = spFrameDocument;
		if(spDocument3 != NULL)
		{
			DWORD dwVersion = localVersionInfo.m_Version;
			CString strVersion;
			WORD wVersion = HIWORD(dwVersion);
			strVersion.Format(_T("%hu.%hu.%hu"),(WORD)HIBYTE(wVersion), (WORD)LOBYTE( wVersion), LOWORD(dwVersion) );

			CComPtr<IHTMLElement> spElem = NULL;
			spDocument3->getElementById(CComBSTR(L"version"), &spElem);
			if( spElem != NULL )
			{
				spElem->put_innerText( CComBSTR(strVersion));
			}
			spElem.Release();

			strVersion.Format(_T("%hu"),LOWORD(dwVersion) );
			spDocument3->getElementById(CComBSTR(L"releasing-SN"), &spElem);
			if( spElem != NULL )
			{
				spElem->put_innerText( CComBSTR(strVersion));
			}

			spElem.Release();
			spDocument3->getElementById(CComBSTR(L"update-log"), &spElem);
			if( spElem != NULL )
			{
				spElem->put_innerText( CComBSTR(localVersionInfo.m_UpdateLog.c_str()));
			}

			spElem.Release();
			spDocument3->getElementById(CComBSTR(L"update-log-title"), &spElem);
			if ( spElem != NULL )
			{
				spElem->put_innerText( CComBSTR(L"=<< 更新内容 >>=") );
			}
		}
	}
}

#endif // #if 0
