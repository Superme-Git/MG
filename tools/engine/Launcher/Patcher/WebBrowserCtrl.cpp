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
		// Ϊ�˴���F5/Refreshҳ��ˢ�£�
		// ���ǲ���ͨ��WebBrowser�ؼ���DownloadComplete�¼���ʵ�֣�
		// ����֮�£�ֻ��ͨ����������ص��İ������ط�ʽ������
		CComQIPtr<IHTMLDocument2> spDocument2 = GetHtmlDocument();
		if( spDocument2 != NULL )
		{
			// ȡ�õ�ǰ�ĵ���URL��ִ��Navigate2����ִ��Refresh,
			// ��ΪRefreshʱ�����޷����°汾��Ϣ
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
	// �����Ҽ������Ĳ˵�
	//return S_FALSE;
	return S_OK;
}

BEGIN_MESSAGE_MAP(CWebBrowserCtrl, CHtmlView)
END_MESSAGE_MAP()

// CWebBrowserCtrl message handlers

void CWebBrowserCtrl::OnNavigateError(LPCTSTR lpszURL, LPCTSTR lpszFrame, DWORD dwError, BOOL *pbCancel)
{
	// ������ҳ������Ϣ
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
		strInfo.Format( _T("��Ŀ¼%s���Ҳ�����Ч�İ汾��Ϣ!\n" ), GetGameClientDir().GetString() );
		KHMsgLog_Error( strInfo );
		return FALSE;
	}

	//
	// �ر�˵��:
	//	��������Զ�̰汾��Ϣ���ٴ������ȡ���Ա��������������⵼��patcher��������ʱ���ںܳ�ʱ�䲻��ʾ������
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
	//		strInfo.Format( _T("�޷���ȡ���µİ汾��Ϣ(%s)\n" ), pUpdateInfo->Url.c_str());
	//		KHMsgLog_Info( strInfo );
	//	}
	//}

	// �ò���Զ�̰汾��Ϣ����Ҳ����true
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

			// "���ذ汾:<span>%hu.%hu.%hu</span> �l������:<span>%u��%u��</span>  ���°汾:<span>%hu.%hu.%hu</span> �l������:<span>%u��%u��</span>
			std::wstring strLocalVersion1 = GlobalFormatText(HTM_LOCAL_VERSION_1, (WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion));
			std::wstring strLocalReleaseDate1 = GlobalFormatText(HTM_RELEASE_DATE_1, when.tm_mon+1, when.tm_mday );
			std::wstring strLatestVersion= GlobalFormatText(HTM_LATEST_VERSION,(WORD)HIBYTE(wVersionRemote), (WORD)LOBYTE(wVersionRemote), LOWORD(dwVersionRemote) );
			std::wstring strLatestReleaseDate1 = GlobalFormatText(HTM_RELEASE_DATE_1, releaseDate.tm_mon+1, releaseDate.tm_mday );
			
			strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_0,
				strLocalVersion1.c_str(), strLocalReleaseDate1.c_str(), strLatestVersion.c_str(), strLatestReleaseDate1.c_str() );

			//strVersionInfo.Format(_T("���ذ汾:<span>%hu.%hu.%hu</span> �l������:<span>%u��%u��</span>  ���°汾:<span>%hu.%hu.%hu</span> �l������:<span>%u��%u��</span>" ),
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

			//strVersionInfo.Format(_T("���ذ汾:<span>%hu.%hu.%hu</span>�l������:<span>%u.%u.%u</span>���°汾:<span>%hu.%hu.%hu</span>�l������:<span>%u.%u.%u</span>" ),
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
			//		_T("��ǰ�汾:<span>%hu.%hu.%hu[����]</span>  "
			//		_T("�������:<span>%hu</span>  ")
			//		_T("�ļ���С:<span>%.2fG</span>  ")
			//		_T("��������:<span>%u-%u-%u</span>"),
			//		(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), LOWORD(dwVersion),
			//		localVersionInfo.m_size/1024.0f,
			//		when.tm_year+1900, when.tm_mon+1, when.tm_mday );
			//}
			//else
			//{
			// �����ļ���СΪ0�����,���ǲ���ʾ
			if( m_localVersion.IsValid() )
			{
				//strVersionInfo.Format(_T("   �����Ñ��˰汾:<span>%hu.%hu.%hu [����]</span>   �l�����:<span>%hu</span>   �l������:<span>%u��%u��%u��</span>"),
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

				/*strVersionInfo.Format(_T("   �����Ñ��˰汾:<span>%hu.%hu.%hu</span> <span>[���چ��}�����h���а汾��C]</span>"),
					(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion) );
				*/
			}
			//}
		}
		else
		{
			//// û�л�ȡ�����°汾��ֻ��ʾ��ǰ�汾��Ϣ
			//if( localVersionInfo.m_size > 0 )
			//{
			//	strVersionInfo.Format(_T("��ǰ�汾:<span>%hu.%hu.%hu</span> �������:<span>%hu</span>  �ļ���С:<span>%.2fG</span>  ��������:<span>%u-%u-%u</span>"),
			//		(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), LOWORD(dwVersion),
			//		localVersionInfo.m_size/1024.0f,
			//		when.tm_year+1900, when.tm_mon+1, when.tm_mday );
			//}
			//else
			//{
			// �����ļ���СΪ0�����,���ǲ���ʾ
			if( m_localVersion.IsValid() )
			{
				//strVersionInfo.Format(_T("   �����Ñ��˰汾:<span>%hu.%hu.%hu</span>   �l�����:<span>%hu</span>    �l������:<span>%u��%u��%u��</span>"),
				//(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(dwVersion), LOWORD(dwVersion),
				//when.tm_year+1900, when.tm_mon+1, when.tm_mday );

				strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_2,
					strLocalVersion2.c_str(), strReleaseSN.c_str(), strReleaseDate.c_str() );
			}
			else
			{
				strVersionInfo = GlobalFormatText(HTM_VERSION_INFO_FORMAT_4,
					strLocalVersion2.c_str(), strBadInstallation.c_str() );

				/*strVersionInfo.Format(_T("   �����Ñ��˰汾:<span>%hu.%hu.%hu</span> <span>[���چ��}�����h���а汾��C]</span>"),
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
	// �����жϱ���OfflineURL
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

		// Ȼ���ж�OnlineURL
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
	// ����index.htmlʱ�����Ǹ���index.html�ϵİ汾��Ϣ
	// ��������������� Navigate2 �� F5/Refresh ���������ҳ��ˢ��
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

	// ��update.htmlʱ�����Ǹ���update.html�ϵİ汾��Ϣ
	// ��������������� Navigate2 �� F5/Refresh ���������ҳ��ˢ��
	if ( strUrl.Right( _countof( _T("update.html")) -1 ) == _T("update.html") )
	{
		// ����Ĵ������ڸ���iframe�е����ݣ�������־��
		// ������ȡ�������˵ĸ�����־���Ǳ��صĸ�����־
		// �����ʾ���صĸ�����־�����Բ������µİ취�������Ҫ��ʾ�������˵ĸ�����־
		// ����Ҫ��ȡ�������˵�������Ϣ
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
				spElem->put_innerText( CComBSTR(L"=<< �������� >>=") );
			}
		}
	}
}

#endif // #if 0
