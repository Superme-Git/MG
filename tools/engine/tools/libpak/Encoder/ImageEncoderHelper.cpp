#include "ImageEncoderHelper.h"

namespace HELPER
{
	static bool is_string_end_with( const std::wstring& strFull, const std::wstring& strExt )
	{
		return 0 == _wcsicmp( strFull.c_str() + strFull.size() - strExt.size(), strExt.c_str() );
	}

	bool CImageEncoderHelper::AppendFileExtension( 
		const std::wstring& srcname,
		std::wstring& newname, const wchar_t* ext_with_dot )
	{
		if( srcname.empty() )
		{
			return false;
		}

		if( is_string_end_with( srcname, ext_with_dot ) )
		{
			return true;
		}
		
		newname = srcname + ext_with_dot;

		return true;
	}

	bool CImageEncoderHelper::ReplaceFileExtension( 
		const std::wstring& srcname,
		std::wstring& newname, const wchar_t* ext_with_dot )
	{
		if( srcname.empty() )
		{
			return false;
		}

		std::wstring::size_type dot = srcname.rfind(L'.');
		if( dot == std::wstring::npos )
		{
			newname = srcname + ext_with_dot;
			return true;
		}

		newname = srcname;
		newname.replace( dot, srcname.size() - dot, ext_with_dot );
		return true;
	}
	
	bool CImageEncoderHelper::TrimFileExtension( 
		const std::wstring& srcname,
		std::wstring& newname )
	{
		if( srcname.empty() )
		{
			return false;
		}

		std::wstring::size_type dot = srcname.rfind(L'.');
		if( dot == 0 )
		{
			return false;
		}

		if( dot == std::wstring::npos )
		{
			newname = srcname;
		}
		else
		{
			newname = srcname.substr( 0, dot );
		}

		return true;
	}

	bool CImageEncoderHelper::RetrieveFileExtension( 
		const std::wstring& srcname,
		std::wstring& ext )
	{
		if( srcname.empty() )
		{
			return false;
		}

		std::wstring::size_type dot = srcname.rfind(L'.');
		if( dot == std::wstring::npos )
		{
			return false;
		}
		else
		{
			ext = srcname.substr( dot );
			return true;
		}
	}
} // 