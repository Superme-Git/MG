///=============================================================================
/// Product   : PFS
/// Project   : Miscellaneous Files
/// Module    : stringhelper.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	CStringHelper
///		1. std::wstring 和其它数据类型的相互转换函数
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-10-27 Created.
///=============================================================================
#pragma once

namespace ZIP
{
	class CStringHelper
	{
	private:
		CStringHelper() {}
		~CStringHelper() {}

	public:

		static const std::wstring TABLE;
		static wchar_t HexToInt( wchar_t hexChar )
		{
			if( hexChar >= L'0' && hexChar <= L'9' )
			{
				return (wchar_t)(hexChar - L'0');
			}
			else if( hexChar >= L'a' && hexChar <= L'f' )
			{
				return (wchar_t)(hexChar - L'a' + 10 );
			}
			else if( hexChar >= L'A' && hexChar <= L'F' )
			{
				return (wchar_t)(hexChar - L'A' + 10 );
			}
			else
			{
				return 0;
			}
		}
		static ZIP::CDataBlock String2Data( const std::wstring& _str);

		static int String2Int( const std::wstring& _str)
		{ return _wtoi( _str.c_str()); }

		static ZIP::fsize_t String2FileSize( const std::wstring& str)
		{ return _wtoi64( str.c_str()); }

		static std::wstring Data2String( const ZIP::CDataBlock& src)
		{
			std::wstring str;

			const char* data = (const char*)src.GetData();
			size_t count = src.GetSize();
			for( size_t i = 0; i < count; i ++)
			{
				const char& c = data[i];
				str += TABLE[ (c >> 4) & 0x0f];
				str += TABLE[ c & 0x0f];
			}
			return str;
		}

		static std::wstring Int2String( int n)
		{
			const size_t	size = 16;
			wchar_t		str[size];
			_itow_s( n, str, size, 10);
			return str;
		}

		static std::wstring FileSize2String( fsize_t fsize)
		{
			const size_t	size = 16;
			wchar_t		str[size];
			_i64tow_s( fsize, str, size, 10);
			return str;
		}
			
		///==============================================================
		///
		/// String Converter
		///
		///==============================================================
		// enum { CodePage = 936 }; // 明确指定ZIPFILE中的文件名编码为936
		static int GetMultiByteSize( const std::wstring& _src );
		static bool WideCharToMultiByte( const std::wstring& _src, std::string& _des );
		static bool MultiByteToWideChar( const std::string& _src, std::wstring& _des );
	};

} // namespace ZIP