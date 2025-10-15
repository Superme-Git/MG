// VersionCheckerView.cpp : implementation of the CVersionCheckerView class
//

#include "stdafx.h"
#include "VersionChecker.h"

#include "VersionCheckerDoc.h"
#include "VersionCheckerView.h"
#include "XMLHelper.h"
#include "DNSQuery.h"
#include "InetIO.h"
#include "DownloadFileList.h"
#include "GlobalNotification.h"

#include <wininet.h>
#include <Shlwapi.h>
#pragma comment(lib, "wininet.lib")

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CVersionCheckerView

IMPLEMENT_DYNCREATE(CVersionCheckerView, CListView)

BEGIN_MESSAGE_MAP(CVersionCheckerView, CListView)
	ON_WM_CREATE()
	ON_MESSAGE( WM_DATAFETCH, OnDataFetch)
	ON_MESSAGE( WM_DATAREADY, OnDataReady)
	ON_MESSAGE( WM_DATALOST, OnDataLost)
END_MESSAGE_MAP()

// CVersionCheckerView construction/destruction
class CVersionInfo
{
public:
	DWORD			m_Version;		// Version: Major.Minor.Build
	// DWORD			m_lastVersion;	// 上一版本版本号，只有差异包有这个属性
	long			m_ReleaseDate;	// __time32_t
	std::wstring	m_VersionString;
	std::wstring	m_Hash;
	// std::wstring	m_UpdateLog;	// updatelog

	CVersionInfo()
	{
		m_Version = 1;
		// m_lastVersion = 0;
		m_ReleaseDate = 0;
	}
};

bool ResolveVersion( LPVOID lpvData, int cbLength, CVersionInfo& rVersionInfo )
{
	using namespace XMLHELPER;
	CMSComServerScoped scoped;

	CComBSTR bstrXMLText( (cbLength-2) / sizeof(wchar_t), (wchar_t*)lpvData + 1  );
	MSXML2::IXMLDOMNodePtr spRootNodePtr;
	HRESULT hr = LoadRootNodeFromXMLText( bstrXMLText, &spRootNodePtr );
	if ( FAILED(hr) )
	{
		return false;
	}

	CXMLNode xNodeDoc( spRootNodePtr );
	
	rVersionInfo.m_Version = xNodeDoc.GetAttributeUInt32( L"version", 0 );
	xNodeDoc.GetAttribute( L"versionstring", rVersionInfo.m_VersionString );
	rVersionInfo.m_ReleaseDate = xNodeDoc.GetAttributeInt32( L"release-date", 0 );
	xNodeDoc.GetAttribute( L"hash", rVersionInfo.m_Hash );

	return true;
}

LRESULT CVersionCheckerView::OnDataFetch(WPARAM wParam, LPARAM lParam )
{
	DataId did = (DataId)lParam;
	Data rData;
	if( GlobalGetData( did, rData ) )
	{
		GetListCtrl().SetItemText( rData.iItemId, 4, L"[...]" );
	}

	return 1;
}

LRESULT CVersionCheckerView::OnDataLost(WPARAM wParam, LPARAM lParam )
{
	DataId did = (DataId)lParam;
	Data rData;
	if( GlobalGetData( did, rData ) )
	{
		GetListCtrl().SetItemText( rData.iItemId, 4, L"[LOST]" );
	}

	return 1;
}

LRESULT CVersionCheckerView::OnDataReady(WPARAM wParam, LPARAM lParam )
{
	// Handler of WM_DATAREADY
	DataId did = (DataId)lParam;
	Data rData;
	if( GlobalGetData( did, rData ) )
	{
		// rData.strItemName
		CVersionInfo VersionInfo;
		if( ResolveVersion( &rData.vData[0], (int)rData.vData.size(), VersionInfo ) )
		{
			CString strVersion;
			WORD wVersion = HIWORD(VersionInfo.m_Version);
			strVersion.Format(_T("%hu.%hu.%hu"),(WORD)HIBYTE(wVersion), (WORD)LOBYTE(wVersion), LOWORD(VersionInfo.m_Version) );
			GetListCtrl().SetItemText( rData.iItemId, 4, strVersion );

			tm when ={0};
			_localtime32_s( &when, &VersionInfo.m_ReleaseDate );
			CString strTime;
			strTime.Format( L"%u-%u-%u", when.tm_year+1900, when.tm_mon+1, when.tm_mday );

			GetListCtrl().SetItemText( rData.iItemId, 5, strTime );
		}
		else
		{
			GetListCtrl().SetItemText( rData.iItemId, 4, L"Done!" );
		}
	}

	return 1;
}

CVersionCheckerView::CVersionCheckerView()
{
	// TODO: add construction code here

}

CVersionCheckerView::~CVersionCheckerView()
{
}

BOOL CVersionCheckerView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CListView::PreCreateWindow(cs);
}

void CVersionCheckerView::OnInitialUpdate()
{
	CListView::OnInitialUpdate();

	// Clear pending download items
	DOWNLOAD::GetInstance<DOWNLOAD::ItemData>().Clear();

	GetListCtrl().DeleteAllItems();

	CString strFileName = GetDocument()->GetPathName();
	if( strFileName.IsEmpty() )
	{
		return;
	}

	using namespace XMLHELPER;
	CMSComServerScoped scoped;

	CComBSTR bstrXMLText( strFileName );
	MSXML2::IXMLDOMNodePtr spRootNodePtr;
	HRESULT hr = LoadRootNodeFromXMLFile( bstrXMLText, &spRootNodePtr );
	if ( FAILED(hr) )
	{
		return;
	}

	CXMLNode xNodeDoc( spRootNodePtr );
	std::wstring versionString;
	xNodeDoc.GetAttribute( L"versionstring", versionString );

	DWORD dwVersion = 0;
	dwVersion = xNodeDoc.GetAttributeUInt32( L"version", 0 );
	DWORD dwReleaseDate = 0;
	dwReleaseDate = xNodeDoc.GetAttributeUInt32( L"release-date", dwReleaseDate);
	MSXML2::IXMLDOMNodeListPtr spServerListPtr;
	hr = spRootNodePtr->get_childNodes(&spServerListPtr);
	if ( FAILED(hr) )
	{
		return;
	}

	long lCount = 0;
	hr = spServerListPtr->get_length( &lCount );

	int itemIndex = 0;
	for ( long index = 0; index < lCount; index ++ )
	{
		MSXML2::IXMLDOMNodePtr spServerNodePtr;
		hr = spServerListPtr->get_item( index, &spServerNodePtr);
		if ( FAILED(hr) )
		{
			continue;
		}

		MSXML2::DOMNodeType nodeType;
		if( FAILED( spServerNodePtr->get_nodeType(&nodeType) ) || nodeType != MSXML2::NODE_ELEMENT )
		{
			continue;
		}

		CXMLNode xNodeServer( spServerNodePtr );
		std::wstring name;
		xNodeServer.GetName( name );
		if( name != L"server" )
		{
			continue;
		}

		int serverType = xNodeServer.GetAttributeInt32( L"type", 0 );
		std::wstring serverTypeString;
		xNodeServer.GetAttribute( L"type",  serverTypeString);
		int iItem = GetListCtrl().InsertItem( itemIndex ++, serverTypeString.c_str() );

		std::wstring url;
		xNodeServer.GetAttribute( L"url", url );
		GetListCtrl().SetItemText( iItem, 1, url.c_str() );

		std::wstring baseUrl;
		std::wstring hostName;

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
		bRet = InternetCrackUrl( url.c_str(),0, 0, &UrlComponents );
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

		std::wstring strFullUrl;

		DOWNLOAD::ItemData itemData;

		std::vector<DWORD> rIpV4Records;
		if( hostName.find_first_not_of( L"0123456789.") == std::wstring::npos )
		{
			// IP address
			GetListCtrl().SetItemText( iItem, 2, hostName.c_str() );
			strFullUrl = url;
			strFullUrl += L"/.version.meta";
			GetListCtrl().SetItemText( iItem, 3, strFullUrl.c_str() );

			itemData.first = iItem;
			itemData.second = strFullUrl;
			DOWNLOAD::GetInstance<DOWNLOAD::ItemData>().Push( itemData );
		}
		else
		{
			GetListCtrl().SetItemText( iItem, 2,  hostName.c_str() );
			strFullUrl = url;
			strFullUrl += L"/.version.meta";
			itemData.first = iItem;
			itemData.second = strFullUrl;
			
			GetListCtrl().SetItemText( iItem, 3, strFullUrl.c_str() );

			DOWNLOAD::GetInstance<DOWNLOAD::ItemData>().Push( itemData );

			AfxPumpMessage();

			int nRet = DNSQuery4( hostName.c_str(), rIpV4Records );
			if ( nRet == 0 )
			{
				std::wstring ip;
			
				for ( size_t i = 0; i < rIpV4Records.size(); i ++ )
				{
					ipv4helper* ipv4 = (ipv4helper*)&rIpV4Records[i];

					ip.resize(15);
					int nCount = swprintf_s( &ip[0], 16,
						L"%hu.%hu.%hu.%hu", 
						ipv4->b1,ipv4->b2,ipv4->b3,ipv4->b4 );
					ip.resize(nCount);

					int iItem = GetListCtrl().InsertItem( itemIndex++, L"" );
					GetListCtrl().SetItemText( iItem, 2, ip.c_str() );


					strFullUrl = L"http://";
					strFullUrl += ip;
					strFullUrl += baseUrl;
					strFullUrl += L"/.version.meta";
					GetListCtrl().SetItemText( iItem, 3, strFullUrl.c_str() );

					itemData.first = iItem;
					itemData.second = strFullUrl;

					DOWNLOAD::GetInstance<DOWNLOAD::ItemData>().Push( itemData );
				}
			}
		}
	}

	GetDocument()->SetModifiedFlag( FALSE );
}

// CVersionCheckerView message handlers

int CVersionCheckerView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CListView::OnCreate(lpCreateStruct) == -1)
		return -1;

	GetListCtrl().SetView( LVS_REPORT );
	GetListCtrl().SetExtendedStyle( GetListCtrl().GetExtendedStyle() | LVS_EX_FULLROWSELECT | LVS_EX_CHECKBOXES );

	GetListCtrl().InsertColumn(0, _T("Type"), LVCFMT_LEFT, 50 );

	GetListCtrl().InsertColumn(1, _T("Url"), LVCFMT_LEFT, 250 );

	GetListCtrl().InsertColumn(2, _T("Host/IP"), LVCFMT_LEFT, 200 );

	GetListCtrl().InsertColumn(3, _T("VersionFile"), LVCFMT_LEFT, 300 );

	GetListCtrl().InsertColumn(4, _T("Version"), LVCFMT_LEFT, 100 );

	GetListCtrl().InsertColumn(5, _T("Date"), LVCFMT_LEFT, 100 );

	return 0;
}
