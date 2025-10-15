#include "CEGUILJXMLParser.h"
#include "CEGUILJXMLParserHelper.h"
#include "CEGUIResourceProvider.h"
#include "CEGUISystem.h"
#include "CEGUIXMLHandler.h"
#include "CEGUIXMLAttributes.h"
#include "CEGUILogger.h"
#include "CEGUIExceptions.h"
#include "CEGUIPfsResourceProvider.h"
#include "nuclear.h"
#include "../../../../common/ljfm/code/include/common.h"
#include "../../../../common/ljfm/code/include/ljfmbase.h"
#include "../../../../dependencies/LJXML/Include/LJXMLStringHelper.hpp"

namespace CEGUI
{
	namespace LJXMLParserHelper
	{
		std::wstring GUIStringToWString(const CEGUI::String& cs)
		{
			std::wstring ret(cs.length(), 0);
			for (CEGUI::String::size_type i = 0; i != cs.length(); ++i)
			{
				ret[i] = cs[i];
			}
			return ret;
		}
		CEGUI::String WStringToGUIString(const std::wstring& wstr)
		{
			CEGUI::String str(wstr);
			return str;
		}
		std::string GUIStringToString(const CEGUI::String& cs)
		{
			std::wstring ws = GUIStringToWString(cs);
			std::string result = LJXMLStringHelper::ws2s(ws);
			return result;
		}
		CEGUI::String StringToGUIString(const std::string& s)
		{
			std::wstring ws;
			ws = LJXMLStringHelper::s2ws(s);
			CEGUI::String str(ws);
			return str;
		}
	}
}