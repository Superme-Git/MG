#ifndef LJFP_StringUtil_H
#define LJFP_StringUtil_H

#include "LJFP_Var.h"
#include "../../common/platform/utils/StringCover.h"

class StringUtil
{
public:
	static unsigned int WS2I(std::wstring InSrc)
	{
		return StringCover::WStrToNum<int>(InSrc);
	}
	static unsigned int WS2UI(std::wstring InSrc)
	{
		return StringCover::WStrToNum<unsigned int>(InSrc);
	}
	static std::wstring UI2WS(unsigned int InSrc)
	{
		return StringCover::NumTowstring<unsigned int>(InSrc);
	}
	static std::string WS2S(const std::wstring& WS)
	{
		std::string curLocale = setlocale(LC_ALL, NULL);
		setlocale(LC_ALL, "chs");//这里感觉不太好将来再换种写法
		const wchar_t* _Source = WS.c_str();
		size_t _Dsize = 2 * WS.size() + 1;
		char* _Dest = new char[_Dsize];
		memset(_Dest, 0, _Dsize);
		wcstombs(_Dest, _Source, _Dsize);
		std::string result = _Dest;
		delete[] _Dest;
		setlocale(LC_ALL, curLocale.c_str());
		return result;
	}
	static std::wstring S2WS(const std::string& S)
	{
		std::string curLocale = setlocale(LC_ALL, NULL);
		setlocale(LC_ALL, "chs");
		const char* _Source = S.c_str();
		size_t _Dsize = S.size() + 1;
		wchar_t* _Dest = new wchar_t[_Dsize];
		wmemset(_Dest, 0, _Dsize);
		mbstowcs(_Dest, _Source, _Dsize);
		std::wstring result = _Dest;
		delete[] _Dest;
		setlocale(LC_ALL, curLocale.c_str());
		return result;
	}
	std::size_t SplitStrW(std::wstring SourceStr, std::wstring DelimitStr, std::vector<std::wstring>& ResultStr)
	{
		ResultStr.clear();
		size_t FindStart;
		size_t FindPos;
		FindStart = 0;
		do
		{
			FindPos = SourceStr.find_first_of(DelimitStr, FindStart);
			if (FindPos == FindStart)
			{
				// Do nothing
				FindStart = FindPos + 1;
			}
			else if (FindPos == std::wstring::npos)
			{
				// Copy the rest of the string
				std::wstring CurStr = SourceStr.substr(FindStart);
				if (CurStr.size() > 0 && CurStr.compare(DelimitStr) != 0)
				{
					if (CurStr.size() == 1 && CurStr[0] == 0)
					{
					}
					else
					{
						ResultStr.push_back(CurStr);
					}
				}
				break;
			}
			else
			{
				// Copy up to delimiter
				std::wstring CurStr = SourceStr.substr(FindStart, FindPos - FindStart);
				if (CurStr.size() > 0)
				{
					ResultStr.push_back(CurStr);
					FindStart = FindPos + 1;
				}
			}
			// parse up to next real data
			FindStart = SourceStr.find_first_not_of(DelimitStr, FindStart);

		} while (FindPos != std::wstring::npos);
		return ResultStr.size();
	}
};

inline StringUtil GetStringUtil()
{
	StringUtil Result;
	return Result;
}


#endif //LJFP_StringUtil_H