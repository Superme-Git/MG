#pragma once

#include <string>

namespace HELPER
{
	class CImageEncoderHelper
	{
	private:
		CImageEncoderHelper(void);
		~CImageEncoderHelper(void);

	public:
		static bool AppendFileExtension( 
				const std::wstring& srcname,
				std::wstring& newname,
				const wchar_t* ext_with_dot );

		static bool ReplaceFileExtension( 
				const std::wstring& srcname,
				std::wstring& newname,
				const wchar_t* ext_with_dot );

		static bool TrimFileExtension( 
				const std::wstring& srcname,
				std::wstring& newname );
		
		static bool RetrieveFileExtension( 
				const std::wstring& srcname,
				std::wstring& ext );

	};

}