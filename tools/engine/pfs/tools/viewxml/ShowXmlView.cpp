// ShowXmlView.cpp : implementation file
//

#include "stdafx.h"
#include "viewxml.h"
#include "viewxmlDoc.h"
#include "ShowXmlView.h"


// CShowXmlView

IMPLEMENT_DYNCREATE(CShowXmlView, CEditView)

CShowXmlView::CShowXmlView()
{

}

CShowXmlView::~CShowXmlView()
{
}

BEGIN_MESSAGE_MAP(CShowXmlView, CEditView)
	ON_WM_CREATE()
END_MESSAGE_MAP()


//
// 对lpszMessage的换行符进行规范化
//
// 对单独的\r和\n都转换成\r\n对(PC标准)
//
// 如果lplpszMessage2为NULL,那么只对lpszMessage进行检测
// 否则，如果需要规范化，*lplpszMessage2为lpszMessage规范化的结果
// 
// 返回值为是否需要规范化，或是否对输入的lpszMessage进行了规范化处理
// 对于无需规范化的字符串，*lplpszMessage2将为NULL。
// 对于需规范化的字符串，如果lplpszMessage2不为NULL，*lplpszMessage2 为规范化结果
// 使用LocalFree释放之.
//
BOOL CanonicalizeLineFeedsW( LPCWSTR lpszMessage, LPCWSTR *lplpszMessage2 )
{
	ASSERT( lpszMessage != NULL);
	size_t len = wcslen( lpszMessage );
	if( len <= 0 )
	{
		return FALSE;
	}

	BOOL bNeedCanonicalize = FALSE;
	size_t pos = 0;
	size_t len2 = len;
	for( LPCWSTR lpPtr = lpszMessage; lpPtr[0]; lpPtr += pos, len2 -= pos)
	{
		pos = wcscspn( lpPtr, L"\r\n");
		if( pos >= len2 )
		{
			break;
		}

		bNeedCanonicalize = lpPtr[pos] != L'\r' || lpPtr[pos+1] != L'\n';
		if ( bNeedCanonicalize )
		{
			break;
		}
		else
		{
			pos += 2;
		}
	}

	if( bNeedCanonicalize && lplpszMessage2 != NULL )
	{
		size_t maxneed = len*2+2-pos;
		LPWSTR lpszMessage2 = (LPWSTR)LocalAlloc( LPTR, maxneed * 2);
		wcsncpy_s( lpszMessage2, maxneed, lpszMessage, pos );

		wchar_t c_curr = 0, c_last = 0;
		size_t iDes = pos;
		size_t iSrc = pos;
		for ( ; iSrc <= len; iSrc ++ )
		{
			c_curr = lpszMessage[iSrc];

			if ( c_curr == L'\n' )
			{
				if ( c_last != L'\r' )
					lpszMessage2[iDes++] = L'\r';
			}
			else if( c_last == L'\r' )
			{
				if( c_curr != L'\n' )
					lpszMessage2[iDes++] = L'\n';
			}
			lpszMessage2[iDes++] = c_curr;
			c_last = c_curr;
		}

		*lplpszMessage2 = lpszMessage2;
	}

	return bNeedCanonicalize;
}
// CShowXmlView message handlers

void CShowXmlView::OnInitialUpdate()
{
	CEditView::OnInitialUpdate();
	GetEditCtrl().Clear();

	GetEditCtrl().SetTabStops(16);

	if( GetDocument()->m_bOpen)
	{
		XMLIO::CFileReader& reader = GetDocument()->m_reader;
		// 因为当前版本的XMLIO已经完全实现了各种格式的SaveAsString方法，所以
		// 直接使用SaveAsString接口实现Octets-->XML的转换
		// [2010-04-01 yanghaibo] 
		std::wstring str;
		reader.SaveAsString(str);

		// 换行处理
		LPCWSTR pszTextCanonicalized = NULL;
		if( CanonicalizeLineFeedsW( str.c_str(), &pszTextCanonicalized ) )
		{
			GetEditCtrl().SetWindowText( pszTextCanonicalized );
			LocalFree( (HLOCAL)pszTextCanonicalized );
		}
		else
		{
			GetEditCtrl().SetWindowText( str.c_str() );
		}
	}
	else
	{
		GetEditCtrl().SetWindowText( L"" );
	}
}

int CShowXmlView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CEditView::OnCreate(lpCreateStruct) == -1)
		return -1;

	GetEditCtrl().SetReadOnly( TRUE);

	// 设置字体
	LOGFONTW logFont = {0};
	logFont.lfCharSet = DEFAULT_CHARSET;
	logFont.lfClipPrecision = CLIP_DEFAULT_PRECIS;
	wcscpy_s( logFont.lfFaceName, L"新宋体");
	logFont.lfQuality = CLEARTYPE_QUALITY;
	logFont.lfHeight = 14;
	logFont.lfStrikeOut = 0;
	logFont.lfWidth = 0;
	logFont.lfOrientation = 0;
	logFont.lfOutPrecision = OUT_TT_PRECIS;//OUT_DEFAULT_PRECIS;
	logFont.lfPitchAndFamily = (DEFAULT_PITCH | FF_DONTCARE);
	logFont.lfWeight = FW_DONTCARE;
	HFONT hFont = CreateFontIndirectW( &logFont );
	if( hFont == NULL )
	{
		hFont = (HFONT)::GetStockObject( DEFAULT_GUI_FONT );
	}
	GetEditCtrl().SendMessage( WM_SETFONT, (WPARAM)(LONG_PTR)hFont, 0 );

	return 0;
}
