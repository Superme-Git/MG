#ifndef LJXMLSTRINGHELPER_H_INCLUDED
#define LJXMLSTRINGHELPER_H_INCLUDED
#include <iostream>
namespace LJXMLStringHelper
{
	inline std::size_t utf16toutf8(const wchar_t* u16, std::size_t size, unsigned  char* u8, std::size_t outBufferSize)
	{
		std::size_t converted = 0;

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
	inline std::string U16ToU8(std::wstring& ws)
	{
		unsigned  char* buffer = new unsigned  char[ws.length() * 3 + 1];
		memset(buffer, 0, ws.length() * 3 + 1);
		int ss = 0;
		ss = utf16toutf8(ws.c_str(), ws.length(), buffer, ws.length() * 3 + 1);
		std::string Result;
		Result.resize(ss);
		for (int i = 0; i < ss; i++)//暂时这样解决之后再用内存拷贝等方法
		{
			Result[i] = ((char*)buffer)[i];//这里最后一个字符可能有问题
		}
		delete[] buffer;
		buffer = NULL;
		return Result;
	}
	inline size_t utf8toutf16(const unsigned char* u8, size_t size, wchar_t* u16, size_t outBufferSize)
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
	inline std::wstring U8ToU16(std::string& s, bool bMark = false)
	{
		unsigned  char* buffer = new unsigned  char[s.length() * sizeof(wchar_t)];
		wchar_t* text = (wchar_t*)buffer;
		int ss = 0;
		if (bMark)
		{
			ss = utf8toutf16((const unsigned  char*)s.c_str() + 3, s.length() - 3, text, s.length());
		}
		else
		{
			ss = utf8toutf16((const unsigned  char*)s.c_str(), s.length(), text, s.length());
		}
		std::wstring Result;
		Result.resize(ss);
		for (int i = 0; i < ss; i++)//暂时这样解决之后再用内存拷贝等方法
		{
			Result[i] = text[i];
		}
		//Result = Result.substr(0, ss);
		delete[] buffer;
		return Result;
	}
#ifdef ANDROID
	inline std::string ws2s(std::wstring& ws)
	{
		char* buffer = new char[ws.length() * 3 + 1];
		memset(buffer, 0, ws.length() * 3 + 1);

		utf16toutf8(ws.c_str(), ws.length(), (unsigned char*)buffer, ws.length() * 3 + 1);

		std::string ret(buffer);
		delete[] buffer;
		return ret;
	}

	inline std::wstring s2ws(std::string& s)
	{
		wchar_t* buffer = new wchar_t[s.length() + 1];
		memset(buffer, 0, (s.length() + 1) * sizeof(wchar_t));

		utf8toutf16((unsigned char*)s.c_str(), s.length(), buffer, s.length() + 1);

		std::wstring ret(buffer);
		delete[] buffer;
		return ret;
	}

	inline std::string ws2s(const std::wstring & ws)
	{
		std::wstring str(ws);
		return ws2s(str);
	}

	inline std::wstring  s2ws(const std::string& s)
	{
		std::string str(s);
		return s2ws(str);
	}
#else
	inline std::string ws2s(const std::wstring& ws)
	{
		std::string curLocale = setlocale(LC_ALL, NULL);
		setlocale(LC_ALL, "chs");//这里感觉不太好将来再换种写法
		const wchar_t* _Source = ws.c_str();
		size_t _Dsize = 2 * ws.size() + 1;
		char* _Dest = new char[_Dsize];
		memset(_Dest, 0, _Dsize);
		wcstombs(_Dest, _Source, _Dsize);
		std::string result = _Dest;
		delete[] _Dest;
		setlocale(LC_ALL, curLocale.c_str());
		return result;
	}
	inline std::wstring s2ws(const std::string& s)
	{
		std::string curLocale = setlocale(LC_ALL, NULL);
		setlocale(LC_ALL, "chs");
		const char* _Source = s.c_str();
		size_t _Dsize = s.size() + 1;
		wchar_t* _Dest = new wchar_t[_Dsize];
		wmemset(_Dest, 0, _Dsize);
		mbstowcs(_Dest, _Source, _Dsize);
		std::wstring result = _Dest;
		delete[] _Dest;
		setlocale(LC_ALL, curLocale.c_str());
		return result;
	}
#endif
	inline std::size_t SplitStrW(std::wstring SourceStr, std::wstring DelimitStr, std::vector<std::wstring>& ResultStr)
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
	enum EncodeLJ
	{
		LJ_ANSI = 0,
		LJ_UTF_8,
		LJ_UTF_8_NO_MARK,
		LJ_UTF_16,
		LJ_UTF_16_BIG_ENDIAN,

		//#if defined (SLIM_USE_WCHAR) || defined (__GNUC__)
		//		DefaultEncode = UTF_16
		//#else
		//		DefaultEncode = ANSI
		//#endif
	};
	inline EncodeLJ detectEncode(const unsigned char* str, std::size_t size, bool& multiBytes)
	{
		while (size > 0)
		{
			if ((*str & 0x80) == 0)
			{
				//1 byte
				++str;
				--size;
			}
			else
			{
				multiBytes = true;
				if ((*str & 0xf0) == 0xe0)
				{
					//3 bytes
					if (size < 3)
					{
						return LJ_ANSI;
					}
					if ((*(str + 1) & 0xc0) != 0x80 || (*(str + 2) & 0xc0) != 0x80)
					{
						return LJ_ANSI;
					}
					str += 3;
					size -= 3;
				}
				else if ((*str & 0xe0) == 0xc0)
				{
					//2 bytes
					if (size < 2)
					{
						return LJ_ANSI;
					}
					if ((*(str + 1) & 0xc0) != 0x80)
					{
						return LJ_ANSI;
					}
					str += 2;
					size -= 2;
				}
				else if ((*str & 0xf8) == 0xf0)
				{
					//4 bytes
					if (size < 4)
					{
						return LJ_ANSI;
					}
					if ((*(str + 1) & 0xc0) != 0x80 || (*(str + 2) & 0xc0) != 0x80 || (*(str + 3) & 0xc0) != 0x80)
					{
						return LJ_ANSI;
					}
					str += 4;
					size -= 4;
				}
				else
				{
					return LJ_ANSI;
				}
			}
		}
		return LJ_UTF_8_NO_MARK;
	}
	inline EncodeLJ GetBufferCodeType(unsigned char* buffer, std::size_t size, bool& multiBytes)
	{
		EncodeLJ encode = LJ_ANSI;
		const unsigned char* bom = reinterpret_cast<const unsigned char*>(buffer);
		if (bom[0] == 0xfe && bom[1] == 0xff)
		{
			encode = LJ_UTF_16_BIG_ENDIAN;
		}
		else if (bom[0] == 0xff && bom[1] == 0xfe)
		{
			encode = LJ_UTF_16;
		}
		else if (bom[0] == 0xef && bom[1] == 0xbb && bom[2] == 0xbf)
		{
			encode = LJ_UTF_8;
		}
		else
		{
			encode = detectEncode(buffer, size, multiBytes);
		}
		return encode;
	}
	inline bool reallyLoadFromMemory(unsigned char* buffer, std::size_t size, std::wstring& ws, EncodeLJ& bufferCode)
	{
		unsigned char* m_buffer = NULL;
		if (size < 3)
		{
			assert("reallyLoad from memory enter size <3");

			return false;
		}
		//get encode
		bufferCode = LJ_ANSI;
		bool multiBytes = false;
		bufferCode = GetBufferCodeType(buffer, size, multiBytes);
		std::size_t characterCount;
		if (bufferCode == LJ_UTF_16)
		{
			//note:
			//in vc sizeof(wchar_t) == 2,in ios sizeof(wchar_t) == 4
			//and in our xml config sizeof(wchar_t) == 2
			//so buffer need to be process yi xia xia
			//skip bom
			characterCount = (size - 2) / 2;
			m_buffer = new unsigned char[characterCount*sizeof(wchar_t)];
			unsigned short* src = reinterpret_cast<unsigned short*>(buffer + 2);
			wchar_t* dst = reinterpret_cast<wchar_t*>(m_buffer);
			//memcpy(m_buffer, buffer, 2);//bom

			for (size_t i = 0; i < characterCount; i++) {
				dst[i] = (wchar_t)src[i];
			}
			ws = reinterpret_cast<wchar_t*>(m_buffer);
		}
		else if (bufferCode == LJ_UTF_16_BIG_ENDIAN)
		{
			//swap. can be faster
			characterCount = (size - 2) / 2;
			m_buffer = new unsigned char[characterCount * sizeof(wchar_t)];
			const wchar_t* src = reinterpret_cast<const wchar_t*>(buffer + 2);
			const wchar_t* srcEnd = src + characterCount;
			wchar_t* dst = (wchar_t*)m_buffer;
			for (; src < srcEnd; ++src, ++dst)
			{
				*((char*)dst) = *(((const char*)src) + 1);
				*(((char*)dst) + 1) = *((const char*)src);
			}
			ws = reinterpret_cast<wchar_t*>(m_buffer);
		}
		else if (bufferCode == LJ_UTF_8 || bufferCode == LJ_UTF_8_NO_MARK || (bufferCode == LJ_ANSI && !multiBytes))
		{
			m_buffer = new unsigned char[size * sizeof(wchar_t)];
			wchar_t* text = reinterpret_cast<wchar_t*>(m_buffer);//for parser
			if (bufferCode == LJ_UTF_8)
			{
				//skip bom
				characterCount = LJXMLStringHelper::utf8toutf16(buffer + 3, size - 3, text, size);
			}
			else
			{
				characterCount = LJXMLStringHelper::utf8toutf16(buffer, size, text, size);
			}
			ws = reinterpret_cast<wchar_t*>(m_buffer);
		}
		else if (bufferCode == LJ_ANSI && multiBytes)
		{
			ws = LJXMLStringHelper::s2ws((char*)buffer);
			characterCount = ws.size();
		}
		else
		{
			//LOGD("reallyLoad from memory enter encode");
			return false;
		}
		ws = ws.substr(0, characterCount);

		if (m_buffer != NULL)
		{
			delete[] m_buffer;
		}

		return true;
	}
}
#endif