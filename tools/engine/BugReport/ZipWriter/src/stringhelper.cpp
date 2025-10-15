#include <windows.h>
#include <string>
#include <cassert>
#include "zipcommon.h"
#include "datablock.h"
#include "stringhelper.h"

namespace ZIP
{
	const std::wstring CStringHelper::TABLE = L"0123456789abcdef";

	ZIP::CDataBlock CStringHelper::String2Data( const std::wstring& str)
	{
		ZIP::CDataBlock data;
		const wchar_t* pwstr = str.c_str();
		size_t len = str.size();

		// str必须保证为偶数长度字符数，否则无法保证正确性
		assert( len % 2 == 0 );

		data.Resize( (len + 1) /2 );
		char* pstr = (char*)data.GetData();
		size_t j = 0;
		if ( len %2 != 0 )
		{
			len--;
			for( size_t i=0; i<len; i+=2)
			{
				pstr[j++] = (char)(((unsigned int)HexToInt(pwstr[i]) << 4) + HexToInt(pwstr[i+1]));
			}

			pstr[j++] = (char) HexToInt(pwstr[len]);
		}
		else
		{
			for( size_t i=0; i<len; i+=2)
			{
				pstr[j++] = (char)(((unsigned int)HexToInt(pwstr[i]) << 4) + HexToInt(pwstr[i+1]));
			}
		}

		data.Resize( j );
		return data;
	}

	///==============================================================
	///
	/// String Converter
	///
	///==============================================================
	enum { CodePage = 936 }; // 明确指定ZIPFILE中的文件名编码为936
	int CStringHelper::GetMultiByteSize( const std::wstring& _src )
	{
		return ::WideCharToMultiByte( CodePage, 0, _src.c_str(), (int)_src.size(), NULL, 0, 0, FALSE);
	}

	bool CStringHelper::WideCharToMultiByte( const std::wstring& _src, std::string& _des )
	{
		_des.resize( _src.size() );
		int len = ::WideCharToMultiByte( CodePage, 0, _src.c_str(), (int)_src.size(),
			const_cast<LPSTR>( _des.c_str()), (int)_des.size(), 0, FALSE );
		bool bFailed = ( 0 == len); 
		if (bFailed)
		{
			if( ::GetLastError() == ERROR_INSUFFICIENT_BUFFER)
			{
				int nBufSize = ::WideCharToMultiByte( CodePage, 0, _src.c_str(), (int)_src.size(), NULL, 0, 0, FALSE); 
				if( 0 == nBufSize)
					return false;
				_des.resize( nBufSize);
				bFailed = ( 0 == ::WideCharToMultiByte( CodePage, 0, _src.c_str(),
					(int)_src.size(), const_cast<LPSTR>( _des.c_str()), nBufSize, 0, FALSE)); 
			}			
		}
		else
		{
			_des.resize( len);
		}

		return !bFailed;
	}

	bool CStringHelper::MultiByteToWideChar( const std::string& _src, std::wstring& _des )
	{
		_des.resize( _src.size() );
		int len = ::MultiByteToWideChar( CodePage, 0, _src.c_str(), (int)_src.size(),
			const_cast<LPWSTR>( _des.c_str()), (int)_des.size());
		bool bFailed = ( 0 == len); 
		if (bFailed)
		{
			if( ::GetLastError() == ERROR_INSUFFICIENT_BUFFER)
			{
				int nBufSize = ::MultiByteToWideChar( CodePage, 0, _src.c_str(), (int)_src.size(), NULL, 0); 
				if( 0 == nBufSize)
					return false;
				_des.resize( nBufSize);
				bFailed = ( 0 == ::MultiByteToWideChar( CodePage, 0, _src.c_str(), (int)_src.size(),
					const_cast<LPWSTR>( _des.c_str()), nBufSize)); 
			}			
		}
		else
		{
			_des.resize( len);
		}

		return !bFailed;
	}
}
