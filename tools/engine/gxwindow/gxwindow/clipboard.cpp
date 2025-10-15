#include "gxwindowdef.h"

namespace GXWINDOW {

CClipBoard CClipBoard::s_instance;

CClipBoard::CClipBoard()
{
}

CClipBoard::~CClipBoard()
{
}

CClipBoard&  CClipBoard::GetInstace()
{
	return s_instance;
}

std::wstring CClipBoard::StringToWString( const std::string& str)
{
	int nCount = MultiByteToWideChar( CP_ACP, 0, str.c_str(), -1, NULL,	0);
	assert( nCount);
	wchar_t* wszBuf = new wchar_t[nCount];

	MultiByteToWideChar( CP_ACP, 0,	str.c_str(), -1, wszBuf, nCount);
	std::wstring wstr( wszBuf, nCount - 1);
	delete[] wszBuf;
	return wstr;
}

bool CClipBoard::SetString( const std::wstring& str)
{
	if( !OpenClipboard( CDesktop::GetDefault().GetMainHwnd())) 
		return false;
	try
	{
		EmptyClipboard();
		if( str.empty())
			throw true;

		size_t s = (str.size() + 1) * sizeof(wchar_t);
		HGLOBAL hCopy = GlobalAlloc( GMEM_MOVEABLE, s); 
		if( NULL == hCopy) 
			throw false;

		void* lptstrCopy = GlobalLock( hCopy); 
		memcpy( lptstrCopy, str.c_str(), s); 
		GlobalUnlock( hCopy); 
		SetClipboardData( CF_UNICODETEXT, hCopy); 
		throw true;
	}
	catch( bool b) 
	{
		CloseClipboard(); 
		return b;
	}
}

bool CClipBoard::GetString( std::wstring& str)
{
	if( !IsClipboardFormatAvailable( CF_TEXT) &&  !IsClipboardFormatAvailable( CF_UNICODETEXT)) 
		return false; 
	if( !OpenClipboard( CDesktop::GetDefault().GetMainHwnd())) 
		return false;
	try
	{
		HANDLE hglb = GetClipboardData( CF_UNICODETEXT);
		if( NULL == hglb)
		{
			hglb = GetClipboardData( CF_TEXT); 
			if( NULL == hglb)
				throw false;
			char* lpstr = (char*)GlobalLock(hglb); 
			if( lpstr)
				str = StringToWString( std::string( lpstr));
			GlobalUnlock(hglb); 
			throw !!lpstr;
		}
		else
		{
			wchar_t* lpwstr = (wchar_t*)GlobalLock(hglb); 
			if( lpwstr) 
				str.assign( lpwstr);
			GlobalUnlock(hglb); 
			throw !!lpwstr;
		}
	}
	catch( bool b) 
	{
		CloseClipboard(); 
		return b;
	}
}

} // namespace GXWINDOW {

