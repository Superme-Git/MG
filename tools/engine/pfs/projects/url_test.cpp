//////////////////////////////////////////////////////////////////////////
//
// URL_TEST.cpp
//
// URL 相关的测试
//
#include <string>
#include <algorithm>
#include <iostream>
#include <iomanip>

#define  WIN32_LEAN_AND_MEAN
#include <Windows.h>
#include <Shlwapi.h>
#include <wininet.h>
#include <strsafe.h>

#include "../pfs2.h"

namespace URLTEST
{
	class CStr
	{
		enum { CodePage = CP_ACP }; // OK
		//enum { CodePage = 936 };
	public:
		static size_t GetWideSize(const std::wstring& _src)
		{
			return WideCharToMultiByte( CodePage, 0, _src.c_str(), (int)_src.size(), NULL, 0, 0, FALSE);
		}

		static bool WideToMutilByte( const std::wstring& _src, std::string& des, UINT cp = CP_ACP )
		{
			des.resize( _src.size());
			int len = ::WideCharToMultiByte( cp, 0, _src.c_str(), (int)_src.size(), const_cast<LPSTR>( des.c_str()), (int)des.size(), 0, FALSE);
			bool bFailed = ( 0 == len); 
			if (bFailed)
			{
				if( ::GetLastError() == ERROR_INSUFFICIENT_BUFFER)
				{
					int nBufSize = WideCharToMultiByte( cp, 0, _src.c_str(), (int)_src.size(), NULL, 0, 0, FALSE); 
					if( 0 == nBufSize)
						return false;
					des.resize( nBufSize);
					bFailed = ( 0 == ::WideCharToMultiByte( cp, 0, _src.c_str(), (int)_src.size(), const_cast<LPSTR>( des.c_str()), nBufSize, 0, FALSE)); 
				}			
			}
			else
			{
				des.resize( len);
			}
			return !bFailed;
		}

		static bool MutilByteToWide( const std::string& _src, std::wstring& des, UINT cp = CP_ACP)
		{
			des.resize( _src.size() * 2);
			int len = ::MultiByteToWideChar( cp, 0, _src.c_str(), (int)_src.size(), const_cast<LPWSTR>( des.c_str()), (int)des.size());
			bool bFailed = ( 0 == len); 
			if (bFailed)
			{
				if( ::GetLastError() == ERROR_INSUFFICIENT_BUFFER)
				{
					int nBufSize = MultiByteToWideChar( cp, 0, _src.c_str(), (int)_src.size(), NULL, 0); 
					if( 0 == nBufSize)
						return false;
					des.resize( nBufSize);
					bFailed = ( 0 == ::MultiByteToWideChar( cp, 0, _src.c_str(), (int)_src.size(), const_cast<LPWSTR>( des.c_str()), nBufSize)); 
				}			
			}
			else
			{
				des.resize( len);
			}
			return !bFailed;
		}
	};

BOOL URLEncode(LPCWSTR lpwszURL, std::wstring &strDest );

void UrlEscapeTest( LPCWSTR pszURL)
{
	WCHAR pszEscaped[ INTERNET_MAX_URL_LENGTH ];
	DWORD chEscaped = INTERNET_MAX_URL_LENGTH;
	HRESULT hr = UrlEscapeW(  pszURL, pszEscaped, &chEscaped, 0 );
	if( hr == S_OK  )
	{
		std::wcout<< L"original: " << pszURL << L" escaped: " << pszEscaped << std::endl;
	}
	else
	{
		std::wcout<< L" failed to escape url: " << pszURL << std::endl;
	}
}

void UrlEncodeTest(LPCWSTR pszURL )
{
	std::wstring strDest;
	if( URLEncode( pszURL, strDest) )
	{
		std::wcout<< L" Url: " << pszURL << L" Encoded: " << strDest << std::endl;
	}
	else
	{
		std::wcout<< L" Failed to encode url: " << pszURL << std::endl;
	}
}

void UrlEscapeTests( )
{
	UrlEscapeTest( L"http://zhongguo.com/中国人.xml" );
	UrlEscapeTest( L"http://zhongguo.com/中国/人/a b 中国.xml");
}

void UrlEncodeTests( )
{
	UrlEncodeTest( L"http://zhongguo.com/中国人.xml" );
	UrlEncodeTest( L"http://zhongguo.com/中国/人/a b 中国.xml");
}

// 参考 http://www.blooberry.com/indexdot/html/topics/urlencoding.htm
// 对于 Reserved Characters 和 Unsafe Characters 交由 UrlEscape 处理
bool IsReservedChar( char c )
{
	const static char reserved[] = { 36, 38, 43, 44, 47, 58, 59, 61, 63, 64 };
	return std::binary_search( &reserved[0], &reserved[_countof(reserved)], c );
}

bool IsUnsafeChar( char c )
{
	const static char unsafe[] = { 32, 34, 60, 62, 35, 37, 91, 92, 93, 94, 96, 123, 124, 125, 126 };
	return std::binary_search( &unsafe[0], &unsafe[_countof(unsafe)], c );
}

BOOL URLEncode(LPCWSTR lpwszURL, std::wstring &strDest )
{
	std::string strRet;
	int nBufSize = WideCharToMultiByte( CP_UTF8, 0, lpwszURL, -1, NULL, 0, 0, FALSE);
	if( 0 == nBufSize)
		return false;

	strRet.resize( nBufSize);
	int iLength = WideCharToMultiByte( CP_UTF8, 0, lpwszURL, -1,
		const_cast<LPSTR>(strRet.c_str()), nBufSize, 0, FALSE);

	const char* strUTF8  = strRet.c_str();

	iLength --; // 跑去最后的空字符
	int i = 0;
	TCHAR TempBuffer[4];
	while(i < iLength)
	{
		if ((unsigned)strUTF8[i] <= (unsigned char)0x7f)
		{ //字母和数字不转换

			strDest += (wchar_t)strUTF8[i];

			//if ((strUTF8[i] >= '0' && strUTF8[i] <= '9') || 
			//	(strUTF8[i] >= 'A' && strUTF8[i] <= 'Z') ||
			//	(strUTF8[i] >= 'a' && strUTF8[i] <= 'z'))
			//{
			//	strDest += (wchar_t)strUTF8[i];
			//}
			//else if ( strUTF8[i] >= 0 && strUTF8[i] <= 31 )
			//{
			//	//strTemp.Format("%%%02X", (unsigned char)strUTF8[i]);    // 特殊字符
			//	StringCchPrintf(TempBuffer, _countof(TempBuffer), L"%%%02X",(unsigned char)strUTF8[i] );
			//	strDest += TempBuffer;
			//}
		}
		else
		{
			StringCchPrintf(TempBuffer, _countof(TempBuffer),  L"%%%02X",(unsigned char)strUTF8[i] );
			strDest += TempBuffer;
		}

		i ++;
	}

	if (i == 0)
	{
		return FALSE;
	}

	return TRUE;
}

void DownloadTest()
{
	//LPCTSTR lpszUrl = TEXT("http://172.16.2.126/2/中文/人民.txt"); // //DataFileName is 人民.txt
	//LPCTSTR lpszUrl = TEXT("http://www.baidu.com"); //DataFileName is baidu.htm
	LPCTSTR lpszUrl = TEXT("http://www.google.cn"); //DataFileName is google.htm
	TCHAR Info[1024] = {0};

	PFSX::CInetFile inetFile;
	bool bRet = inetFile.Open( lpszUrl );
	if( bRet )
	{
		StringCchPrintf(Info, 1024, TEXT("%s was opened successfully! \n"), lpszUrl/*, the data file name is %s. inetFile.GetDataFileName() */);
		std::wcout<< Info;
	}
	else
	{
		DWORD dwError = GetLastError();
		StringCchPrintf(Info, 1024, TEXT("Failed to download %s!(%d)\n"), lpszUrl, dwError);
		std::wcout<< Info;
	}
}

	void StringTranslationTest(const std::wstring& strW, UINT cp = CP_ACP )
	{
		std::string strA;
		CStr::WideToMutilByte( strW, strA, cp);
		std::cout << "ANSI: " << strA << std::endl;
	}

	void CodePageTest()
	{
		UINT rgCodePage [] =
		{
			936  ,
			10008,
			20936,
			50227,
			50935,
			50936,
			51936,
			52936,
			54936
		};

		//std::wcout.imbue(std::locale("chs"));
		try
		{
			std::wcout.imbue(std::locale(".936"));
		}
		catch( std::runtime_error re )
		{
			std::wcout<< re.what() << std::endl;
		}
		std::wstring strW = L"abc中国人def";
		std::wcout << L"UNICODE: " << strW << std::endl;
		char cpstr[10] = {'.', 0};

		for ( int i = 0; i < _countof(rgCodePage); i ++ )
		{
			UINT cp = rgCodePage[i];
			if( IsValidCodePage( cp ) )
			{
				try
				{
					_itoa_s(cp,cpstr+1, 9, 10 );
					std::wcout.imbue(std::locale(cpstr));
				}
				catch( std::runtime_error re )
				{
					std::wcout<< re.what() << std::endl;
				}

				std::cout << "  VALID: " << std::setw(6) << cp << " -------  ";
				StringTranslationTest(strW, cp );
			}
			else
			{
				std::cout << "INVALID: " << std::setw(6) << cp << " -------  " << std::endl;
			}
		}
	}

	//
	// 测试MetaInfo差异比较和MetaInfo信息的读写
	//
	void Test()
	{
		//try
		//{
		//	//std::wcout.imbue(std::locale("chs"));
		//	std::wcout.imbue(std::locale(".936"));
		//}
		//catch( std::runtime_error re )
		//{
		//	std::wcout<<re.what() << std::endl;
		//}

		//UrlEncodeTests();
		//DownloadTest();
		CodePageTest();
	}

};//  namespace URLTEST