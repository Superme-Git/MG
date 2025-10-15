#include "stdafx.h"
#include "ShowPartLoader.h"
#include "CommonUtils.h"
#include <algorithm>

enum
{
	IMAGE_CX = 24,
	IMAGE_CY = 24,
};

CShowPartLoader::CShowPartLoader()
: m_dcMemSrc( NULL)
, m_dcMemDes( NULL)
, m_maxID( CHANGED_PART_MIN_ID)
{
}

CShowPartLoader::~CShowPartLoader()
{
	if( m_dcMemSrc)
	{
		::DeleteDC( m_dcMemSrc);
		m_dcMemSrc = NULL;
	}
	if( m_dcMemDes)
	{
		::DeleteDC( m_dcMemDes);
		m_dcMemDes = NULL;
	}
}

template< class T>
bool ReadArrayFromStr( const std::wstring& temp, T* des, size_t count)
{
	std::wstringstream ss( temp);
	for( size_t i=0; i<count; i++)
	{
		std::wstring token;
		std::getline(ss, token, L';');
		if (ss.fail())
			break;
		T tokenChild;
		std::wistringstream iss( token);
		iss >> tokenChild;

		des[i] = tokenChild;
	}
	return true;
}

int CShowPartLoader::Load()
{
	if( !InitDefaultModel() )
	{
		return -1;
	}

	LPCTSTR lpszWorkDirectory = UTIL::GetWorkDirectory();

	CString szFullFileName(lpszWorkDirectory);
	szFullFileName += (L"cfg/data/qqshow.xml");

	_bstr_t bstrShowConfigFile(szFullFileName);
	MSXML2::IXMLDOMNodePtr spRootNode;
	HRESULT hr = XMLHELPER::LoadRootNodeFromXMLFile( bstrShowConfigFile, &spRootNode );
	if ( !SUCCEEDED(hr) )
	{
		ASSERT( !"Invalid Show config file!" );
		return -1;
	}

	MSXML2::IXMLDOMNodeListPtr spShowItems;
	hr = spRootNode->get_childNodes( &spShowItems );
	CHECK_HR_RETURN_VALUE( hr, -2 );

	long lCount = 0;
	hr = spShowItems->get_length(&lCount);
	CHECK_HR_RETURN_VALUE( hr, -3 );

	MSXML2::IXMLDOMNodePtr spShowNode;

	long lOriginalHave = (long)m_vShowParts.size();
	m_vShowParts.resize(lOriginalHave + lCount);

	// <Show ID="100" Svg="0/01/background.svg" Tga="0/01/background.tga" 
	//	Sex="0" Layer="0" Type="10" LinkNum="0"/>
	std::wostringstream wos;
	std::wstring temp;
	for( long lIndex = 0; lIndex < lCount; lIndex ++ )
	{
		hr = spShowItems->get_item(lIndex,&spShowNode);
		if ( !SUCCEEDED(hr) )
		{
			ASSERT( !"Invalid Show Item is in config file!" );
			CHECK_HR_RETURN_VALUE( hr, -4 );
		}

		XMLHELPER::CXMLNode xNode(spShowNode);
		ShowPartItem& rBack = m_vShowParts[lOriginalHave + lIndex];
		rBack.ulID      = xNode.GetAttributeInt32 ( L"id", 0 );
		if( rBack.ulID > m_maxID)
		{
			m_maxID = rBack.ulID;
		}
		rBack.bSexMan   = xNode.GetAttributeUInt32( L"showSex", 0 );
		rBack.ulLayer   = xNode.GetAttributeUInt32( L"layer", 0 );
		rBack.lType     = xNode.GetAttributeInt32 ( L"showType", -1 );
		rBack.ulPrice	= xNode.GetAttributeUInt32( L"price", 0);
		rBack.ulFlag	= xNode.GetAttributeUInt32( L"flag", 0);
		rBack.conflict	= xNode.GetAttributeInt64( L"conflict", 0);
		rBack.bIsSaling = xNode.GetAttributeInt32( L"issaling", 1);

		xNode.GetAttribute( L"fileName", rBack.strSvg );
		xNode.GetAttribute( L"iconFileName", rBack.strTga );
		xNode.GetAttribute( L"name", rBack.name);
		xNode.GetAttribute( L"showDescription", rBack.descript);

		// linkID
		temp.clear();
		xNode.GetAttribute( L"linkID", temp);
		if( !temp.empty())
		{
			if( !ReadArrayFromStr( temp, rBack.ulLinkID, LINK_MAX_COUNT))
			{
				wos.str( L"");
				wos << rBack.ulID << L" " << rBack.strTga.c_str() << L" read linkID error";
				AfxMessageBox( wos.str().c_str());
			}
		}
		// transform
		temp.clear();
		xNode.GetAttribute( L"transform", temp);
		if( !temp.empty())
		{
			if( !ReadArrayFromStr( temp, rBack.transform, 6))
			{
				wos.str( L"");
				wos << rBack.ulID << L" " << rBack.strTga.c_str() << L" read transform error";
				AfxMessageBox( wos.str().c_str());
			}
		}
	}

	m_image.Create( IMAGE_CX, IMAGE_CY, ILC_COLOR32 | ILC_MASK, 0, 16);
	SHFILEINFO sfi;
	SHGetFileInfo(L"1.svg", FILE_ATTRIBUTE_NORMAL, &sfi, sizeof(sfi),
		SHGFI_USEFILEATTRIBUTES|SHGFI_ICON|SHGFI_SMALLICON);
	if (sfi.hIcon)
	{
		// svg文件默认图标
		m_image.Add( sfi.hIcon);
	}
	return 0;
}

BOOL CShowPartLoader::InitDefaultModel()
{
	int ulID(0);

	// 女 裸模
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_BODY;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/body.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_BROWS;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/brows.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_EYE;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/eyes.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_HEAD_EAR;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/head.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_ARM_R;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/hand01.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_ARM_L;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/hand02.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_MOUTH;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/mouth.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_NOSE;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/nose.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_TROUSERS_INNER;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/trousers.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_SHIRT;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/underwear.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_HAIR_B;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/hair01.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_HAIR_A;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/hair02.svg";
		kShowInfo.strTga	= L"";
		//kShowInfo.AddLink(ulID - 2);
		//kShowInfo.ulLinkNum = 1;
		kShowInfo.ulLinkID[0] = ulID - 2;
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 1;
		kShowInfo.ulLayer	= MH_SHOW_LEG;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"1/Default/leg.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	//------------------------------------------------------------------------------
	// 男 裸模
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID		= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_BODY;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/body.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_BROWS;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/brows.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_EYE;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/eyes.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_ARM_L;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/handL.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_ARM_R;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/handR.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_HEAD_EAR;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/head.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_LEG;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/leg.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_MOUTH;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/mouth.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_NOSE;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/nose.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_SHIRT;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/underwear.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_TROUSERS_INNER;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/trousers.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_HAIR_B;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/hair01.svg";
		kShowInfo.strTga	= L"";
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}
	{
		ShowPartItem kShowInfo;
		kShowInfo.ulID	= ulID++;
		kShowInfo.bSexMan	= 0;
		kShowInfo.ulLayer	= MH_SHOW_HAIR_A;
		kShowInfo.lType		= SHOW_TYPE_UNDEFINE;
		kShowInfo.strSvg	= L"0/Default/hair02.svg";
		kShowInfo.strTga	= L"";
		//kShowInfo.AddLink(ulID - 2);
		//kShowInfo.ulLinkNum = 1;
		kShowInfo.ulLinkID[0] = ulID - 2;
		m_vShowParts.push_back( kShowInfo );
		//kFriendData.SetDefaultShow(kShowInfo);
	}

	return TRUE;
}

int CShowPartLoader::GetIndexFromID( unsigned long id)
{
	int index = 0;
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i, ++index)
	{
		ShowPartItem& part = *i;
		if( part.ulID == id)
		{
			return index;
		}
	}
	return -1;
}

ShowPartItem* CShowPartLoader::GetItemFromID( unsigned long id)
{
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( part.ulID == id)
		{
			return &part;
		}
	}
	return NULL;
}

void CShowPartLoader::GetSvgFullPath( std::wstring& fullpath)
{
	fullpath = UTIL::GetWorkDirectory();
	fullpath += L"show/";
}

void CShowPartLoader::GetTgaFullPath( std::wstring& fullpath)
{
	fullpath = UTIL::GetWorkDirectory();
	fullpath += L"ui/show/";
}

BITMAPINFO* texReadTGA( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels);
int CShowPartLoader::GetImageIndex( LPCTSTR pszImage)
{
	std::wstring fullpath;
	GetTgaFullPath( fullpath);
	fullpath += pszImage;

	HBITMAP hBmp = NULL;
	char* pPixels = NULL;
	BITMAPINFO* pInfo = texReadTGA( fullpath.c_str(), &hBmp, &pPixels);
	if( NULL == pInfo)
	{
		return -1;
	}
	HBITMAP hDes = StretchImage( hBmp, pInfo);
	CBitmap temp;
	temp.Attach( hDes);
	int index = m_image.Add( &temp, (COLORREF)0);

	::DeleteObject( hDes);
	::DeleteObject( hBmp);
	HeapFree (GetProcessHeap(), 0 , pInfo);
	return index;
}

// 缩放图片到m_image的大小
HBITMAP CShowPartLoader::StretchImage( HBITMAP hSrc, BITMAPINFO* pInfo)
{
	HDC dcScreen = ::GetDC( NULL);
	if( NULL == m_dcMemSrc)
	{
		m_dcMemSrc = ::CreateCompatibleDC( dcScreen);
	}
	HBITMAP hOldSrc = (HBITMAP)::SelectObject( m_dcMemSrc, hSrc);
	
	if( NULL == m_dcMemDes)
	{
		m_dcMemDes = ::CreateCompatibleDC( dcScreen);
		SetStretchBltMode( m_dcMemDes, COLORONCOLOR);
	}
	HBITMAP hDes = ::CreateCompatibleBitmap( dcScreen, IMAGE_CX, IMAGE_CY);
	HBITMAP hOldDes = (HBITMAP)::SelectObject( m_dcMemDes, hDes);

	::StretchBlt( m_dcMemDes, 0, 0, IMAGE_CX, IMAGE_CY, m_dcMemSrc, 
		0, 0, pInfo->bmiHeader.biWidth, pInfo->bmiHeader.biHeight, SRCCOPY);

	::SelectObject( m_dcMemDes, hOldDes);
	::SelectObject( m_dcMemSrc, hOldSrc);
	::DeleteDC( dcScreen);

	return hDes;
}

void CShowPartLoader::GetDefaultParts( TShowPartPtrContainer& tspc, int sex)
{
	tspc.clear();
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( part.bSexMan == sex && part.ulID < CHANGED_PART_MIN_ID)	// 小于 CHANGED_PART_MIN_ID 为裸模
		{
			tspc.push_back( &part);
		}
	}
}

// 保存配置
bool CShowPartLoader::Save( LPCTSTR pszFile)
{
	XMLHELPER::CXMLFileWriter writter;
	XMLHELPER::CXMLWriteNode root;
	if( !writter.GetRootNode( root))
	{
		AfxMessageBox(_T("保存失败!"));
		return false;
	}

	std::wostringstream wos;
	std::wstring temp;
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( part.ulID < CHANGED_PART_MIN_ID)
		{
			continue;
		}
		XMLHELPER::CXMLWriteNode child = root.CreateSubElement( L"Show");

		// id
		wos.str( L"");
		wos << part.ulID;
		child.SetAttribute( L"id", wos.str());
		// svg file
		child.SetAttribute( L"fileName", part.strSvg);
		// tga file
		child.SetAttribute( L"iconFileName", part.strTga);
		// sex
		wos.str( L"");
		wos << part.bSexMan;
		child.SetAttribute( L"showSex", wos.str());
		// layer
		wos.str( L"");
		wos << part.ulLayer;
		child.SetAttribute( L"layer", wos.str());
		// type
		wos.str( L"");
		wos << part.lType;
		child.SetAttribute( L"showType", wos.str());
		// price
		wos.str( L"");
		wos << part.ulPrice;
		child.SetAttribute( L"price", wos.str());
		// name
		child.SetAttribute( L"name", part.name);
		// descript
		child.SetAttribute( L"showDescription", part.descript);
		// flag
		wos.str( L"");
		wos << part.ulFlag;
		child.SetAttribute( L"flag", wos.str());
		// issaling
		wos.str( L"");
		wos << part.bIsSaling;
		child.SetAttribute( L"issaling", wos.str());

		// LinkID
		wos.str( L"");
		for( int i=0; i<LINK_MAX_COUNT; i++)
		{
			if( part.ulLinkID[i] == 0)
			{
				// linkID为0不处理
				continue;
			}
			if( i > 0)
			{
				wos << L";";
			}
			wos << part.ulLinkID[i];
		}
		child.SetAttribute( L"linkID", wos.str());
		// conflict
		wos.str( L"");
		wos << part.conflict;
		child.SetAttribute( L"conflict", wos.str());
		// transform
		wos.str( L"");
		for( int i=0; i<6; i++)
		{
			if( i > 0)
			{
				wos << L";";
			}
			wos << part.transform[i];
		}
		child.SetAttribute( L"transform", wos.str());
		// bounding box
		wos.str( L"");
		for( int i=0; i<4; i++)
		{
			if( i > 0)
			{
				wos << L";";
			}
			wos << part.boundingbox[i];
		}
		child.SetAttribute( L"box", wos.str());
	}
	return writter.SaveToFile( pszFile);
}


// 得到层为layer的部件
void CShowPartLoader::GetPartsByLayer( TShowPartPtrContainer& tspc, int sex, int layer)
{
	tspc.clear();
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( part.ulLayer == layer && part.bSexMan == sex && part.ulID >= CHANGED_PART_MIN_ID)
		{
			tspc.push_back( &part);
		}
	}
}

// 得到所有套件的名称
void CShowPartLoader::GetAllSuitName( std::set<std::wstring>& suits, int sex)
{
	suits.clear();
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( part.bSexMan == sex && part.ulID >= CHANGED_PART_MIN_ID)
		{
			std::wstring temp;
			std::wstring::size_type pos = part.strSvg.find_last_of( L'/');
			if( pos != std::wstring::npos)
			{
				temp = part.strSvg.substr( 0, pos+1);
			}
			else
			{
				temp = part.strSvg;
			}

			suits.insert( temp);
		}
	}
}

// 得到名称匹配套件的部件
void CShowPartLoader::GetPartsBySuitName( TShowPartPtrContainer& tspc, int sex, const wchar_t* szSuit)
{
	tspc.clear();
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( part.bSexMan == sex && part.ulID >= CHANGED_PART_MIN_ID)
		{
			if( wcsncmp( part.strSvg.c_str(), szSuit, wcslen( szSuit)) == 0)
			{
				tspc.push_back( &part);
			}
		}
	}
}

// 得到类型为type的部件
void CShowPartLoader::GetPartsByType( TShowPartPtrContainer& tspc, int sex, long type)
{
	tspc.clear();
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( part.lType == type && part.bSexMan == sex && part.ulID >= CHANGED_PART_MIN_ID)
		{
			tspc.push_back( &part);
		}
	}
}

// 添加一部件
bool CShowPartLoader::AddPart( const std::wstring& file, int sex, int layer, int type)
{
	m_vShowParts.push_back( ShowPartItem());
	ShowPartItem& part = m_vShowParts.back();

	part.ulID = ++m_maxID;
	part.bSexMan = sex;
	part.strSvg = file;
	part.ulLayer = 0;

	std::wstring tgaFile;
	std::wstring::size_type pos = file.find_last_of( L".");
	if( pos != std::wstring::npos)
	{
		tgaFile = file.substr( 0, pos+1);
	}
	else
	{
		tgaFile = file;
	}
	tgaFile += L"tga";

	part.strTga = tgaFile;
	part.ulLayer = layer;
	part.lType = type;

	// 将svg和tga文件名统一改为小写
	std::transform( part.strSvg.begin(), part.strSvg.end(), part.strSvg.begin(), ::towlower);
	std::transform( part.strTga.begin(), part.strTga.end(), part.strTga.begin(), ::towlower);

	return true;
}

// 删除一部件
bool CShowPartLoader::DeletePart( unsigned long ulID)
{
	for( TShowPartContainer::iterator i=m_vShowParts.begin(), e=m_vShowParts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( part.ulID == ulID)
		{
			m_vShowParts.erase( i);
			return true;
		}
	}
	return false;
}