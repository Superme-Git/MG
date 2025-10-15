#ifndef	_CEGUILJXMLPARSERHELPER_H_
#define _CEGUILJXMLPARSERHELPER_H_

#include "CEGUISystem.h"

namespace CEGUI
{
	namespace LJXMLParserHelper
	{
		//enum Encode
		//{
		//	ANSI = 0,
		//	UTF_8,
		//	UTF_8_NO_MARK,
		//	UTF_16,
		//	UTF_16_BIG_ENDIAN,

		//	//#if defined (SLIM_USE_WCHAR) || defined (__GNUC__)
		//	//		DefaultEncode = UTF_16
		//	//#else
		//	//		DefaultEncode = ANSI
		//	//#endif
		//};
		std::wstring GUIStringToWString(const CEGUI::String& cs);
		CEGUI::String WStringToGUIString(const std::wstring& wstr);
		std::string GUIStringToString(const CEGUI::String& cs);
		CEGUI::String StringToGUIString(const std::string& s);
		//Encode GetBufferCodeType(char* buffer, std::size_t size, bool& multiBytes);
		//Encode detectEncode(const char* str, std::size_t size, bool& multiBytes);
		//bool reallyLoadFromMemory(char* buffer, std::size_t size, std::wstring& ws);
	}
}

#endif
