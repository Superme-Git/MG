#ifndef StringUtil_H
#define StringUtil_H

#include "../stdafx.h"

inline std::wstring ReplaceStringW(std::wstring strData, std::wstring strA, std::wstring strB)
{
	std::wstring wsResult = strData;
	size_t pos = 0;
	size_t newStrnewStrLen = strB.length();
	size_t oldStroldStrLen = strA.length();
	while (true)
	{
		pos = wsResult.find(strA, pos);
		if (pos == std::wstring::npos)
		{
			break;
		}
		wsResult.replace(pos, oldStroldStrLen, strB);
		pos += newStrnewStrLen;
	}
	return wsResult;
}

inline size_t utf8toutf16(const char* u8, size_t size, wchar_t* u16, size_t outBufferSize)
{
	size_t converted = 0;

	while (size > 0)
	{
		if ((*u8 & 0x80) == 0)
		{
			*(u16++) = *(u8++);
			--size;
			++converted;
		}
		else if ((*u8 & 0xe0) == 0xc0)
		{
			if (size < 2)
			{
				break;
			}
			*(u16++) = (*u8 & 0x1f) | ((*(u8 + 1) & 0x3f) << 5);
			u8 += 2;
			size -= 2;
			++converted;
		}
		else if ((*u8 & 0xf0) == 0xe0)
		{
			if (size < 3)
			{
				break;
			}
			*u16 = ((*u8 & 0x0f) << 12) | ((*(u8 + 1) & 0x3f) << 6) | (*(u8 + 2) & 0x3f);
			u8 += 3;
			++u16;
			size -= 3;
			++converted;
		}
		else
		{
			break;
		}
		if (converted == outBufferSize)
		{
			break;
		}
	}
	return converted;
}
inline size_t utf16toutf8(const wchar_t* u16, size_t size, char* u8, size_t outBufferSize)
{
	size_t converted = 0;

	while (size > 0)
	{
		if (*u16 < 0x80)
		{
			//1 byte
			if (converted == outBufferSize)
			{
				break;
			}
			*(u8++) = static_cast<char>(*(u16++));
			--size;
			++converted;
		}
		else if (*u16 < 0x800)
		{
			//2 bytes
			if (converted + 2 > outBufferSize)
			{
				break;
			}
			*u8 = (*u16 >> 6) | 0xc0;
			*(u8 + 1) = (*u16 & 0x3f) | 0x80;
			u8 += 2;
			++u16;
			--size;
			converted += 2;
		}
		else
		{
			//3 bytes
			if (converted + 3 > outBufferSize)
			{
				break;
			}
			*u8 = (*u16 >> 12) | 0xe0;
			*(u8 + 1) = ((*u16 >> 6) & 0x3f) | 0x80;
			*(u8 + 2) = (*u16 & 0x3f) | 0x80;
			u8 += 3;
			++u16;
			--size;
			converted += 3;
		}
	}
	return converted;
}

inline std::string ws2s(std::wstring& ws)
{
	char* buffer = new char[ws.length() * 3 + 1];
	memset(buffer, 0, ws.length() * 3 + 1);

	utf16toutf8(ws.c_str(), ws.length(), buffer, ws.length() * 3 + 1);

	std::string ret(buffer);
	delete[] buffer;
	return ret;
}
inline std::string ws2s(const std::wstring & ws)
{
	std::wstring str(ws);
	return ws2s(str);
}
inline std::wstring s2ws(std::string& s)
{
	wchar_t* buffer = new wchar_t[s.length() + 1];
	memset(buffer, 0, (s.length() + 1) * sizeof(wchar_t));

	utf8toutf16(s.c_str(), s.length(), buffer, s.length() + 1);

	std::wstring ret(buffer);
	delete[] buffer;
	return ret;
}
inline std::wstring  s2ws(const std::string& s)
{
	std::string str(s);
	return s2ws(str);
}
template <typename T> inline std::wstring NumTowstring(const T & val) //Num May be everything;
{
	std::wostringstream strstream;
	strstream << val;
	return strstream.str();
}
template<typename T> inline T WStrToNum(const std::wstring& str)
{
	std::wstringstream strstream(str);
	T val;
	strstream >> val;
	return val;
}
#endif