//////////////////////////////////////////////////////////////////////////
//
// LayoutManager.cpp
//
// ���ڲ��ֹ�����
//
// author: yanghaibo
// date : 2008-11-10
//

#include "StdAfx.h"
#include "Launcher.h"
#include "LayoutManager.h"
#include "../../pfs/pfs2.h"
#include "DIYButton.h"
#include "ODProgressCtrl.h"
#include "BitmapUtils.h"
#include "XmlReader.h" // for CXmlReader
#include <algorithm>
#include "IconFile.h"
#include "RenderHelper.h"

using namespace PFS;

#define LAYOUT_ROOT  L"/layout"

//
// �� "r,g,b,a" ��ʽ�ַ���ת��Ϊ COLORREF ��ɫֵ,����a�ֶ�
//
inline COLORREF String2Color( const wchar_t* color)
{
	if( NULL == color)
		return 0;
	if( 0 == color[0])
		return 0;

	int r = 0, g = 0, b = 0;

	// r
	r = _wtoi( color);

	// g
	const wchar_t* szFind  =  wcschr( color, L',');
	if( NULL == szFind)
		return RGB( r, g, b );
	color = szFind + 1;
	g = _wtoi( color);

	// b
	szFind  =  wcschr( color, L',');
	if( NULL == szFind)
		return RGB( r, g, b );
	color = szFind + 1;
	b = _wtoi( color);

	return RGB( r, g, b );
}


CLayoutManager::CLayoutManager(void)
	: m_wndPanel(NULL)
{
	CoInitialize( NULL );
}

CLayoutManager::~CLayoutManager(void)
{
	if( !m_mountName.empty())
	{
		CEnv::Unmount( m_mountName);
	}

	// Before calling the CoUninitialize(), we should release the XML DOM smart pointer first!
	if( m_rootNode != NULL )
	{
		m_rootNode.Release();
	}
	CoUninitialize();

	CString rKey;
	void* pValue = NULL;
	for( POSITION pos = m_ddbMap.GetStartPosition(); pos; )
	{
		m_ddbMap.GetNextAssoc(pos, rKey, pValue);
		if ( pValue != NULL )
		{
			DeleteObject( (HGDIOBJ)pValue );
		}
	}
	m_ddbMap.RemoveAll();

	for( POSITION pos = m_dibMap.GetStartPosition(); pos; )
	{
		m_dibMap.GetNextAssoc(pos, rKey, pValue);
		if ( pValue != NULL )
		{
			DeleteObject( (HGDIOBJ)pValue );
		}
	}
	m_dibMap.RemoveAll();
}

BOOL CLayoutManager::Load( LPCTSTR lpszDirectory, LPCTSTR lpszConfigFile, LPCTSTR lpszWndName )
{
	m_wndName = lpszWndName;
	
	// 
	// �趨��Դ·��
	//
	if( !_LoadLayout( lpszDirectory, lpszConfigFile ) )
	{
		// ����UI��Դʧ��!
		return FALSE;
	}

	return TRUE;
}

// ��ʼ����ָ����崰�ںͲ�����Դ·���������ļ�
BOOL CLayoutManager::SetPanel( CWnd* pPanel )
{
	ASSERT( pPanel != NULL );
	m_wndPanel = pPanel;

	//
	// Ȼ���ʼ�����
	// 
	if( !_InitPanel() )
	{
		return FALSE;
	}

	return TRUE;
}

// ���ز�����Դ�������ļ�
BOOL CLayoutManager::_LoadLayout( LPCTSTR lpszDirectory, LPCTSTR lpszConfigFile )
{
	std::wstring wndName = m_wndName;
	std::transform( wndName.begin(), wndName.end(), wndName.begin(), ::tolower);
	m_mountName = LAYOUT_ROOT;
	m_mountName += L"_";
	m_mountName += wndName;
	// ���ڼٶ�resource����ΪNative
	std::wstring device = lpszDirectory;
	bool bRet = CEnv::Mount( m_mountName, device, FST_NATIVE, MT_READONLY );
	if ( !bRet )
	{
		return FALSE;
	}

	std::wstring file = m_mountName;
	file += L"/";
	file += lpszConfigFile;
	CBaseFile* pFile = CEnv::OpenFile( file, FM_EXCL, FA_RDONLY );
	if ( pFile != NULL )
	{
		MSXMLDOM::CReader reader;
		if( reader.OpenFile( *pFile ) == 0 )
		{
			MSXMLDOM::CReadNode tempNode;
			reader.GetRootNode( tempNode );
			m_rootNode = tempNode.GetInterface();
		}

		CEnv::CloseFile( pFile );
		return TRUE;
	}

	return FALSE;
}


//
// summary: ��BaseFile�м���Iconͼ��
// 
// parameter:
//		pFile - Iconͼ����Դ�ļ�
//		cxDesired - Icon ���
//		cyDesired - Icon �߶�
//
// returns: Iconͼ����Դ���
//
// remarks:
//		��Ϊһ��Icon��Դ�ļ��п��ܰ�������Icon�����ԣ���Ҫָ����С�Ի�ȡ
//	���ϸ�ʽ��Icon��
//		��� cxDesired �� cy Desired ��Ϊ0�� ��ô�������� SM_CXICON System Metric Value
//	ȷ�����ռ��ص�Iconͼ�ꡣ
//
HICON LoadIconFromFile(PFS::CBaseFile* pFile, int cxDesired=0, int cyDesired=0);

HICON LoadIconFromFile(PFS::CBaseFile* pFile, int cxDesired, int cyDesired)
{
	PFS::fsize_t sizeBits = pFile->GetSize();
	if ( sizeBits == NULL || sizeBits > MAXDWORD )
	{
		return NULL;
	}

	BYTE * pBits = (BYTE *)LocalAlloc( LPTR, (SIZE_T)sizeBits );
	if ( pBits == NULL )
	{
		// OUT OF MEMORY!
		return NULL;
	}

	if( sizeBits != pFile->Read( pBits, (size_t)sizeBits ) )
	{
		LocalFree( pBits );
		return NULL;
	}

	if( cxDesired == 0 || cyDesired == 0 )
	{
		cyDesired = cxDesired = GetSystemMetrics( SM_CXICON );
	}

	CIconFile icon;
	icon.SetIconDesiredSize( cxDesired, cyDesired );
	icon.CreateFromData( pBits, (DWORD)sizeBits );
	LocalFree( pBits );

	return icon.Detach();
}


// ����Դ���м���ͼ����Դ
HICON CLayoutManager::LoadIcon(LPCTSTR lpszName, BOOL bBigIcon)
{
	//
	//<Icon n="ħ�����ɸ��³���" f="ui\launcher.ico"/>
	//
	
	CString strQueryString;
	strQueryString.Format(L"/Desktop/Content/Frame[@n=\"%s\"]/Icon[@n=\"%s\"]/@f", m_wndName.c_str(), lpszName);

	CComPtr<IXMLDOMNode> spAttriNode;
	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spAttriNode );
	if ( hr != 0 )
	{
		return NULL;
	}

	CComBSTR bstrValue;
	hr = spAttriNode->get_text(&bstrValue);
	if ( hr != 0 )
	{
		return NULL;
	}

	HICON hIcon = NULL;

	std::wstring fileName = m_mountName;
	fileName += L"/";
	fileName += (LPCWSTR)bstrValue;

	std::replace( fileName.begin(), fileName.end(), L'\\', L'/' );
	CBaseFile* pFile = CEnv::OpenFile(fileName, FM_EXCL,FA_RDONLY );
	if ( pFile != NULL )
	{
		int cxWidth, cyHeight;
		if( bBigIcon )
		{
			cxWidth = GetSystemMetrics(SM_CXICON);
			cyHeight = GetSystemMetrics(SM_CYICON);
		}
		else
		{
			cxWidth = GetSystemMetrics(SM_CXSMICON);
			cyHeight = GetSystemMetrics(SM_CYSMICON);
		}

		hIcon = LoadIconFromFile( pFile, cxWidth, cyHeight );
		CEnv::CloseFile( pFile );
	}

	return hIcon;
}

// ��ʼ����崰��
BOOL CLayoutManager::_InitPanel()
{
	CString strQueryString;
	strQueryString.Format(L"/Desktop/Content/Frame[@n=\"%s\"]/Image1/@f", m_wndName.c_str());

	CComPtr<IXMLDOMNode> spAttriNode;
	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spAttriNode );
	if ( hr != 0 )
	{
		return FALSE;
	}

	CComBSTR bstrValue;
	hr = spAttriNode->get_text(&bstrValue);
	if ( hr != 0 )
	{
		return FALSE;
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// ����λͼ��Դ
	// ����λͼ�������ڴ�С�����ò����򴰿�����
	// 
	HBITMAP hBmp = NULL;
	{
		BITMAPINFOHEADER InfoHeader;
		LPVOID lpBitsArray = NULL;
		std::wstring fileName = m_mountName;
		fileName += L"/";
		fileName += bstrValue;
		std::replace( fileName.begin(), fileName.end(), L'\\', L'/' );
		CBaseFile* pFile = CEnv::OpenFile(fileName, FM_EXCL,FA_RDONLY );
		if ( pFile != NULL )
		{
			lpBitsArray = LoadBitmapData(pFile, InfoHeader);
			CEnv::CloseFile( pFile );
		}

		m_wndPanel->MoveWindow(0, 0, InfoHeader.biWidth, InfoHeader.biHeight);

		CRgn wndRgn;
		CreateRgnByAlphaBits(InfoHeader, lpBitsArray, wndRgn);
		if( wndRgn.GetSafeHandle() )
		{
			m_wndPanel->SetWindowRgn( wndRgn, FALSE );
		}
		m_wndPanel->CenterWindow();

		// ���ö�ȡ�����ݿ�ֱ�Ӵ���BMP
		hBmp = MHZX::CRenderHelper::CreateDIBitmap(lpBitsArray, InfoHeader.biWidth, -InfoHeader.biHeight );
		
		LocalFree( lpBitsArray );
		lpBitsArray = NULL;
	}

	// ���� DIB �������ƴ��屳��
	if ( hBmp == NULL )
	{
		// No resource
		return FALSE;
	}
	m_bitmap.Attach(hBmp);
	m_bitmap.GetBitmap(&m_bitmapInfo);

	do 
	{
		// ��λͼ���ص��ڴ�DC��
		m_dcMemFR.CreateCompatibleDC( NULL );
		m_dcMemFR.SelectObject( &m_bitmap );
	}while (0);

	return TRUE;
}

// ����λͼ��Դ
HBITMAP CLayoutManager::_LoadBitmap( LPCTSTR lpszFileName )
{
	void* pVoid = NULL;
	if( m_ddbMap.Lookup(lpszFileName, pVoid ) )
	{
		return (HBITMAP)pVoid;
	}

	HBITMAP hBmp = NULL;

	std::wstring fileName = m_mountName;
	fileName += L"/";
	fileName += lpszFileName;

	std::replace( fileName.begin(), fileName.end(), L'\\', L'/' );
	CBaseFile* pFile = CEnv::OpenFile(fileName, FM_EXCL,FA_RDONLY );
	if ( pFile != NULL )
	{
		hBmp = LoadBitmap( pFile );
		
		CEnv::CloseFile( pFile );
	}

	m_ddbMap.SetAt( lpszFileName, (void*)hBmp );

	return hBmp;
}

// ����DIBλͼ��Դ
HBITMAP CLayoutManager::_LoadDIBitmap( LPCTSTR lpszFileName )
{
	void* pVoid = NULL;
	if( m_dibMap.Lookup(lpszFileName, pVoid ) )
	{
		return (HBITMAP)pVoid;
	}

	HBITMAP hBmp = NULL;
	std::wstring fileName = m_mountName;
	fileName += L"/";
	fileName += lpszFileName;

	std::replace( fileName.begin(), fileName.end(), L'\\', L'/' );
	CBaseFile* pFile = CEnv::OpenFile(fileName, FM_EXCL,FA_RDONLY );
	if ( pFile != NULL )
	{
		hBmp = LoadDIBitmap( pFile, TRUE );

		CEnv::CloseFile( pFile );
	}

	m_dibMap.SetAt( lpszFileName, (void*)hBmp );

	return hBmp;
}

BOOL CLayoutManager::_ParseImageNode(CComPtr<IXMLDOMNode> spImageNode,
									 HBITMAP& rBitmap, BOOL bDIB, CRect& rRect)
{
	CXmlReader imageNode( spImageNode );

	std::wstring imageFileName;
	imageNode.GetAttribute( L"f", imageFileName );
	if ( imageFileName.empty() )
	{
		return FALSE;
	}
	
	if( bDIB )
	{
		// Load DIB
		rBitmap = _LoadDIBitmap( imageFileName.c_str() );
	}
	else
	{
		// Load DDB
		rBitmap = _LoadBitmap( imageFileName.c_str() );
	}

	if ( rBitmap == NULL )
	{
		return FALSE;
	}

	int x = imageNode.GetAttributeInt32( L"x" );
	int y = imageNode.GetAttributeInt32( L"y" );
	int w = imageNode.GetAttributeInt32( L"w" );
	int h = imageNode.GetAttributeInt32( L"h" );

	rRect.SetRect(x,y, x+w, y+h);

	return TRUE;
}

BOOL CLayoutManager::_ParseNodeRect(CComPtr<IXMLDOMNode> spNode, CRect& rRect)
{
	CXmlReader xNode( spNode );

	int x = xNode.GetAttributeInt32( L"x" );
	int y = xNode.GetAttributeInt32( L"y" );
	int w = xNode.GetAttributeInt32( L"w" );
	int h = xNode.GetAttributeInt32( L"h" );

	rRect.SetRect(x,y, x+w, y+h);

	return TRUE;
}


// �������ؼ�
BOOL CLayoutManager::ApplyProgressCtrl( UINT nID, CODProgressCtrl& rProgressCtrl)
{
	CString strName;
	strName.LoadString( nID );
	strName = strName.SpanExcluding( _T("\n\t") );

	CString strQueryString;
	strQueryString.Format(
		_T("/Desktop/Content/Frame[@n=\"%s\"]/Content/ProgressBar[@n=\"%s\"]"), m_wndName.c_str(),
		strName.GetString());

	CComPtr<IXMLDOMNode> spNode;

	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spNode );
	if ( hr != 0 )
	{
		return FALSE;
	}

	CXmlReader ctrlNode( spNode );

	std::wstring strValue;
#ifdef _DEBUG
	ctrlNode.GetAttribute( L"n", strValue);
	ASSERT ( strValue == strName.GetString() );
#endif

	int x = ctrlNode.GetAttributeInt32( L"x" );
	int y = ctrlNode.GetAttributeInt32( L"y" );
	int w = ctrlNode.GetAttributeInt32( L"w" );
	int h = ctrlNode.GetAttributeInt32( L"h" );

	rProgressCtrl.SubclassDlgItem(nID, m_wndPanel);
	rProgressCtrl.MoveWindow( x, y, w, h, FALSE );

	CComPtr<IXMLDOMNode> spImageNode;
	hr = ctrlNode.SelectSingleNode( L"Image1", spImageNode);
	if ( hr != 0 )
	{
		return FALSE;
	}

	HBITMAP hBitmap = NULL;
	CRect rcRect;

	// ʹ��DIB��ʽ��ȾProgressBar��ԭ��DDB��ʽ��Ⱦ����16λɫ��ʾģʽ�£��������޷���������Ⱦ����
	if( !_ParseImageNode(spImageNode, hBitmap, TRUE, rcRect) )
	{
		return FALSE;
	}

	CComPtr<IXMLDOMNode> spImageNode2;
	hr = ctrlNode.SelectSingleNode( L"Image2", spImageNode2);
	if ( hr != 0 )
	{
		return FALSE;
	}

	CRect rcRect2;
	if( !_ParseNodeRect(spImageNode2, rcRect2) )
	{
		return FALSE;
	}

	// �������λͼ
	CBitmap imageBitmap;
	imageBitmap.Attach(hBitmap);
	BOOL bRet = rProgressCtrl.SetAppearance( imageBitmap, rcRect, rcRect2 );
	imageBitmap.Detach();
	
	// ��ȡ�ؼ���������ʾ������Ϣ
	if( ctrlNode.GetAttribute( L"tipinfo", strValue) )
	{
		rProgressCtrl.SetTipInfo( strValue.c_str() );
	}

	// ��ȡ�ؼ����ı���ɫ������Ϣ�����û�У������ã��ؼ�������ȱʡֵ��
	std::wstring value;
	if( ctrlNode.GetAttribute( L"c", value) )
	{
		DWORD dwColor = String2Color( value.c_str());
		rProgressCtrl.SetTextColor(dwColor);
	}

	// ��ȡ�ؼ��Ƿ���ʾ�ٷֱȵ����á�
	if( ctrlNode.GetAttribute( L"percent", value) )
	{
		if( value.compare(L"true") == 0 || value.compare(L"1") == 0  )
			rProgressCtrl.SetShowPercent(TRUE);
		else if( value.compare(L"false") == 0 || value.compare(L"0") == 0 )
			rProgressCtrl.SetShowPercent(FALSE);
		// else default 
	}

	return bRet;
}

// ��ť�ؼ�
BOOL CLayoutManager::ApplyButton( UINT nID, CDIYButton& rButton )
{
	CString strName;
	strName.LoadString( nID );
	strName = strName.SpanExcluding( _T("\n\t") );

	CString strQueryString;
	strQueryString.Format(
		_T("/Desktop/Content/Frame[@n=\"%s\"]/Content/ButtonImage[@n=\"%s\"]"), m_wndName.c_str(),
		strName.GetString());

	CComPtr<IXMLDOMNode> spNode;

	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spNode );
	if ( hr != S_OK )
	{
		return FALSE;
	}

	CXmlReader ctrlNode( spNode );

	std::wstring strValue;
#ifdef _DEBUG
	ctrlNode.GetAttribute( L"n", strValue);
	ASSERT ( strValue == strName.GetString() );
#endif

	int x = ctrlNode.GetAttributeInt32( L"x" );
	int y = ctrlNode.GetAttributeInt32( L"y" );
	int w = ctrlNode.GetAttributeInt32( L"w" );
	int h = ctrlNode.GetAttributeInt32( L"h" );
	rButton.SubclassDlgItem( nID, m_wndPanel );
	rButton.MoveWindow( x, y, w, h, FALSE );

	CComPtr<IXMLDOMNode> spImageNode1, spImageNode0, spImageNode2, spImageNode3;
	CRect rcRect1, rcRect0, rcRect2, rcRect3;
	
	hr = ctrlNode.SelectSingleNode( L"Image1", spImageNode1);
	if ( hr != S_OK )
	{
		return FALSE;
	}

	HBITMAP hBitmap = NULL;
	if( !_ParseImageNode(spImageNode1, hBitmap, TRUE, rcRect1) )
	{
		return FALSE;
	}

	hr = ctrlNode.SelectSingleNode( L"Image2", spImageNode2);	
	if ( SUCCEEDED(hr) )
	{
		_ParseNodeRect(spImageNode2, rcRect2 );
	}

	hr = ctrlNode.SelectSingleNode( L"Image3", spImageNode3);	
	if ( SUCCEEDED(hr) )
	{
		_ParseNodeRect(spImageNode3, rcRect3 );
	}

	// ����û��
	hr = ctrlNode.SelectSingleNode( L"Image0", spImageNode0);	
	if ( SUCCEEDED(hr) )
	{
		_ParseNodeRect(spImageNode0, rcRect0 );
	}	

	// �������λͼ
	// ע��Ϊ�˼򻯲���������ٶ�����״̬��������imageBitmap��
	// �� rcRect0.IsRectEmpty() ʱ��Bitmap�в��᳢��ȡ��Disable��ͼ��״̬
	// ����Normal̬��ͼ����档
	CBitmap imageBitmap;
	imageBitmap.Attach(hBitmap);
	BOOL bRet = FALSE;

	if( rcRect2.IsRectEmpty() || rcRect3.IsRectEmpty() )
	{
		// ͨ����һ��Normalͼ�����µ��Ƴ�������״̬ͼ����Щͼ֮��û�м����
		// ������״̬���λͼ��rcRect��Y����ƽ�Ƶõ���
		bRet = rButton.InitializeFromBitmap(imageBitmap, 
			rcRect1, !rcRect0.IsRectEmpty() );
	}
	else
	{
		// ͨ�����õ����꣬��ȡ����ͼ����������ͼ��ͼ֮��������ļ����
		// ���ǣ����뱣֤��Щ״̬ͼ��һ�ŵ�ͼ�ϡ�
		bRet = rButton.InitializeFromBitmap2(
			imageBitmap,
			rcRect1, // rcNormal
			rcRect2, // rcSelected
			rcRect3, // rcHover
			rcRect0);// rcDisabled, may be empty rectangle.
	}
		
	imageBitmap.Detach();

	// ��ȡ�ؼ���������ʾ������Ϣ
	if( ctrlNode.GetAttribute( L"tipinfo", strValue) )
	{
		rButton.SetTipInfo( strValue.c_str() );
	}

	return bRet;
}

//
// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ����������ʾ��Ϣ,��Ӧ��tipinfo����
//
BOOL CLayoutManager::GetControlTipInfo( LPCTSTR lpszControlName, CStringW& rstrTipInfo )
{
	CString strQueryString;

	// ����ͨ��
	strQueryString.Format(
		_T("/Desktop/Content/Frame[@n=\"%s\"]/Content//*[@n=\"%s\"]"), m_wndName.c_str(),
		lpszControlName );

	CComPtr<IXMLDOMNode> spNode;

	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spNode);
	if ( hr == 0 )
	{
		CXmlReader ctrlNode( spNode );

		std::wstring strValue;
		if( ctrlNode.GetAttribute( L"tipinfo", strValue) )
		{
			rstrTipInfo = strValue.c_str();
			return TRUE;
		}
	}

	return FALSE;
}

// �ؼ�λ��
BOOL CLayoutManager::GetControlRect( LPCTSTR lpszName, CRect& rRect )
{
	CString strQueryString;

	// ����ͨ��
	strQueryString.Format(
		_T("/Desktop/Content/Frame[@n=\"%s\"]/Content//*[@n=\"%s\"]"), m_wndName.c_str(),
		lpszName );

	CComPtr<IXMLDOMNode> spNode;

	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spNode);
	if ( hr == 0 )
	{
		CXmlReader ctrlNode( spNode );

#ifdef _DEBUG
		std::wstring strValue;
		ctrlNode.GetAttribute( L"n", strValue);
		ASSERT ( strValue == lpszName );
#endif

		int x = ctrlNode.GetAttributeInt32( L"x" );
		int y = ctrlNode.GetAttributeInt32( L"y" );
		int w = ctrlNode.GetAttributeInt32( L"w" );
		int h = ctrlNode.GetAttributeInt32( L"h" );

		rRect.SetRect( x, y, x + w, y + h );

		return TRUE;
	}

	return FALSE;
}

//
// ���ƴ���
//
BOOL CLayoutManager::OnPanelPaint( CDC *pDC )
{
	if (m_wndPanel == NULL || !m_bitmap.GetSafeHandle() )
	{
		return FALSE;
	}
	else
	{
		//
		// ���ƴ���
		//
		pDC->BitBlt(0, 0, m_bitmapInfo.bmWidth, 
			m_bitmapInfo.bmHeight, &m_dcMemFR, 0, 0, SRCCOPY );

		return TRUE;
	}
}

//
// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ������ɫ��Ϣ,��Ӧ��lpszAttrName����
//
BOOL CLayoutManager::GetControlAttributeColor( LPCTSTR lpszControlName, LPCTSTR lpszAttrName, COLORREF& dwColor )
{
	CString strQueryString;

	// ����ͨ��
	strQueryString.Format(
		_T("/Desktop/Content/Frame[@n=\"%s\"]/Content//*[@n=\"%s\"]"), m_wndName.c_str(),
		lpszControlName );

	CComPtr<IXMLDOMNode> spNode;

	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spNode);
	if ( hr == 0 )
	{
		CXmlReader ctrlNode( spNode );

#ifdef _DEBUG
		std::wstring strValue;
		ctrlNode.GetAttribute( L"n", strValue);
		ASSERT ( strValue == lpszControlName );
#endif

		std::wstring value;
		ctrlNode.GetAttribute( lpszAttrName, value);
		dwColor = String2Color( value.c_str());

		return TRUE;
	}

	return FALSE;
}

//
// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ������ɫ��Ϣ,c="r,g,b,a"
//
BOOL CLayoutManager::GetControlColor( LPCTSTR lpszName, COLORREF& dwColor )
{
	return GetControlAttributeColor( lpszName, _T("c"), dwColor );
}

//
// summary: ��ȡ����������Ϣ��Ϊ�ؼ�ָ���ı���ɫ��Ϣ,b="r,g,b,a"
//
BOOL CLayoutManager::GetControlBkColor( LPCTSTR lpszName, COLORREF& dwColor )
{
	return GetControlAttributeColor( lpszName, _T("b"), dwColor );
}

//
// summary: ��ȡ����������Ϣ��Ϊ���ڱ���ͼƬָ����λ�ü��߿���Ϣ
//
BOOL CLayoutManager::GetFRImageRect( CRect& rRect, CRect& rBorder )
{
	CString strQueryString;

	// ����ͨ��
	strQueryString.Format(
		_T("/Desktop/Content/Frame[@n=\"%s\"]/Image1"), m_wndName.c_str());

	CComPtr<IXMLDOMNode> spNode;

	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spNode);
	if ( hr == 0 )
	{
		CXmlReader ctrlNode( spNode );

		int x = ctrlNode.GetAttributeInt32( L"x" );
		int y = ctrlNode.GetAttributeInt32( L"y" );
		int w = ctrlNode.GetAttributeInt32( L"w" );
		int h = ctrlNode.GetAttributeInt32( L"h" );

		rRect.SetRect( x, y, x + w, y + h );

		int w0 = ctrlNode.GetAttributeInt32( L"w0" );
		int w1 = ctrlNode.GetAttributeInt32( L"w1" );
		int h0 = ctrlNode.GetAttributeInt32( L"h0" );
		int h1 = ctrlNode.GetAttributeInt32( L"h1" );

		rBorder.SetRect( w0, h0, w1, h1 );

		return TRUE;
	}

	return FALSE;
}

//
// summary: ��ȡ����������Ϣ���ƶ�ͼƬ HBITMAP
//
BOOL CLayoutManager::GetBitmap( LPCTSTR lpszName, HBITMAP& rBitmap, BOOL bDIB, CRect& rRect)
{
	CString strQueryString;
	strQueryString.Format(
		_T("/Desktop/Content/Frame[@n=\"%s\"]/Content/*[@n=\"%s\"]"), m_wndName.c_str(),
		lpszName);

	CComPtr<IXMLDOMNode> spNode;

	// ʹ��XPath�ӿ�
	CComBSTR bstr = strQueryString.AllocSysString();
	HRESULT hr = m_rootNode->selectSingleNode(bstr, &spNode );
	if ( hr != S_OK )
	{
		return FALSE;
	}

	CXmlReader ctrlNode( spNode );

	CComPtr<IXMLDOMNode> spImageNode;
	hr = ctrlNode.SelectSingleNode( L"Image1", spImageNode);
	if ( hr != S_OK )
	{
		return FALSE;
	}

	if( !_ParseImageNode( spImageNode, rBitmap, bDIB, rRect) )
	{
		return FALSE;
	}

	return TRUE;
}