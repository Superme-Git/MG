#ifndef __SHARE_STRINGCOVER_H__
#define __SHARE_STRINGCOVER_H__
#pragma once

#include <string>
#include <vector>
#include <sstream>

#include <algorithm>
#include <time.h>
#include "StringUtil.h"
#include "stdint.h"
#include "assert.h"

#include "../../cauthc/include/octets.h"
//#include "Utils.h"

namespace StringCover
{
	/// @brief ascii和unicode之间的编码转换 
	inline size_t ascii_to_unicode(const char* src, wchar_t* dest, size_t dest_len, size_t src_len = 0)
	{
        assert(false);
        return 0;
	}
	inline size_t unicode_to_ascii(const wchar_t* src, char* dest, size_t dest_len, size_t src_len = 0)
	{
        assert(false);
        return 0;
	}
	/// @brief utf8和unicode之间的编码转换 
	inline size_t utf8_to_unicode(const unsigned char* src, wchar_t* dest, size_t dest_len, size_t src_len = 0)
	{
        assert(false);
        return 0;
	}

	inline size_t unicode_to_utf8(const wchar_t* src, unsigned char* dest, size_t dest_len, size_t src_len = 0)
	{
        assert(false);
        return 0;
	}
	/// @brief ascii和utf8之间的编码转换 
	inline size_t ascii_to_utf8(const char* src, unsigned char* dest, size_t dest_len, size_t src_len = 0)
	{
        assert(false);
        return 0;
	}
	inline size_t utf8_to_ascii(const unsigned char* src, char* dest, size_t dest_len, size_t src_len = 0)
	{
        assert(false);
        return 0;
	}

	/// @brief 普通字符串转宽字符串 
	inline std::wstring& to_wstring(const char* str, size_t len, std::wstring& ret)
	{
		if(len == 0)
			len = strlen(str);
		ret.resize(len);
		ret.resize(ascii_to_unicode(str, const_cast<wchar_t*>(ret.data()), ret.size(), len));
		return ret;
	}
		
	inline std::wstring to_wstring(const char* str, size_t len)
	{
		std::wstring ret;
		return to_wstring(str, len, ret);
	}
		
	inline std::wstring& to_wstring(const std::string& str, std::wstring& ret)
	{
		return to_wstring(str.data(), str.size(), ret);
	}
		
	/// @brief 宽字符串转普通字符串 
	inline std::string& to_string(const wchar_t* str, size_t len, std::string& ret)
	{
		if(len == 0)
#ifndef ANDROID
		len = wcslen(str);
#else
        len = wcslen_x(str);
#endif
		ret.resize(len * 2);
		ret.resize(unicode_to_ascii(str, const_cast<char*>(ret.data()), ret.size(), len));
		return ret;
	}
		
	inline std::string to_string(const wchar_t* str, size_t len)
	{
		std::string ret;
		return to_string(str, len, ret);
	}
		 
	inline std::string& to_string(const std::wstring& str, std::string& ret)
	{
		return to_string(str.data(), str.size(), ret);
	}

	inline std::wstring NetStringToWString(const FireNet::Octets& netstr)
	{
		std::wstring str((std::wstring::pointer)netstr.begin(), netstr.size()/2);

		return str;
	}
    
    inline std::wstring UTF16WStringToUTF32WString(const std::wstring &wstr)
    {
        unsigned short * pUtf16 = (unsigned short*)wstr.c_str();
            
        std::wstring result = L"";
        int sizeResult = 2 * wstr.length();
        for (int i = 0; i < sizeResult; i++)
        {
            result += (wchar_t)pUtf16[i];
        }
            
        return result;
    }
 
	inline std::string intToString(int val)
	{
		char buff[64] = {0};
		sprintf(buff, "%d", val);

		return std::string(buff);

#if 0 // slower
		std::ostringstream iss;
		iss << val;
		return String(iss.str());
#endif
	}

	inline std::string int64_tToString(int64_t val)
	{
		char buff[64] = { 0 };
		sprintf(buff, "%lld", val);
		return std::string(buff);

#if 0 // slower
		std::ostringstream iss;
		iss << val;
		return String(iss.str());
#endif
	}

	inline std::string floatToString(float val)
	{
		char buff[64] = { 0 };
		sprintf(buff, "%f", val);

		return std::string(buff);

#if 0 // slower
		std::ostringstream iss;
		iss << val;
		return String(iss.str());
#endif
	}

	template <typename T>  inline std::wstring NumTowstring(const T & val) //Num May be everything;
	{
		std::wostringstream strstream;

		strstream << val;

		return strstream.str();
	}

    int FormatString( std::wstring& instring,const wchar_t *format ,...);
    
    inline std::wstring OctectToWString(const FireNet::Octets& oct)
	{
		std::wstring ret;
		if (oct.size() % 2 != 0)
		{
			throw new std::exception();
		}
		ret.reserve(oct.size() / 2);
		typedef unsigned short utf16;
		// only for little endian
		for (utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
			ret.insert(ret.end(), (wchar_t)*ch);
		}

		return ret;
	}

	inline FireNet::Octets StringToOctect(const char *str, const int length)
	{
		std::wstring snstr = SHARE_String2Wstring(std::string(str, length));

		// utf32 to utf16
		FireNet::Octets oct(snstr.length() * 2);
		for (size_t pos = 0; pos < snstr.length(); pos++)
		{
			unsigned short ch = (unsigned short)snstr[pos];
			oct.insert(oct.end(), &ch, 2);
		}

		return oct;
	}

	inline FireNet::Octets WStringToOctect(const wchar_t *str, const int length)
	{
		FireNet::Octets oct(length * 2);
		for (int pos = 0; pos < length; pos++)
		{
			unsigned short ch = (unsigned short)str[pos];
			oct.insert(oct.end(), &ch, 2);
		}

		return oct;
	}

	char Hex2Value(char in);
	char Value2Hex(char in);

	std::wstring WString2Hex(const wchar_t *pIn, const int nInLength);
	std::wstring Hex2WString(const wchar_t *pIn, const int nInLength);
	/*
	template<typename T> T String2Hex(typename const T::value_type *pIn, typename T::size_type nInLength)
	{
		static_assert(sizeof(wchar_t) == 2, "sizeof(wchar_t) != 2");

		int nValueSize = sizeof(T::value_type) / sizeof(char) * 2;

		T strOut;
		strOut.reserve(nInLength * nValueSize);

		for (int i = 0; i < nInLength; ++i)
		{
			typename T::value_type nValue = pIn[i];

			int nCount = nValueSize;
			while (nCount--)
			{
				strOut += Value2Hex(nValue >> 4 * nCount & 0xF);
			}
		}
		return std::move(strOut);
	}

	template<typename T> T Hex2String(typename const T::value_type *pIn, typename T::size_type nInLength)
	{
		static_assert(sizeof(wchar_t) == 2, "sizeof(wchar_t) != 2");

		int nValueSize = sizeof(T::value_type) / sizeof(char) * 2;

		T strOut;

		if (nInLength % nValueSize != 0)
			return std::move(strOut);

		strOut.reserve(nInLength / nValueSize);

		for (int i = 0; i < nInLength / nValueSize; ++i)
		{
			typename T::value_type nValue = 0;

			int nCount = nValueSize;
			while (nCount--)
			{
				nValue += Hex2Value(*pIn++) << 4 * nCount;
			}

			strOut += nValue;
		}
		return std::move(strOut);
	}
	*/
	inline std::wstring Replace( const std::wstring& orignStr, const std::wstring& oldStr, const std::wstring& newStr )
	{
		size_t pos = 0;
		std::wstring tempStr = orignStr;
		std::wstring::size_type newStrnewStrLen = newStr.length();
		std::wstring::size_type oldStroldStrLen = oldStr.length();
		while(true)
		{
			pos = tempStr.find(oldStr, pos);
			if (pos == std::wstring::npos)
				break;
			tempStr.replace(pos, oldStroldStrLen, newStr);
			pos += newStrnewStrLen;
		}
		return tempStr;
	} 

	//秒数转换为日期：—年—月—日
    std::wstring int64_tToDateTime(int64_t time);

	//秒数转换为日期：—年—月—日 —时—分
    std::wstring int64_tToPrecisionTime(int64_t time);

	//秒数转换为日期：—时—分
    std::wstring int64_tToClockTime(int64_t time);

	//秒数转换为日期：*年*月*日 时:分
    std::wstring int64_tToPrecisionTime3(int64_t time);

	//秒数转换为时间：？天 ？时？分
    std::wstring int64_tToTimeDHM(int64_t time);

    std::wstring int64_tToTimeDHMShort(int64_t time);

	//秒数转换为时间： ？时？分？秒
    std::wstring intToTimeHMS(int time);

	//分割字符串
	inline int split_string(const std::wstring &inputstr, const std::vector<std::wstring>& delimiters, std::vector<std::wstring> &substrs)   
	{   
		if (inputstr.length() == 0 || delimiters.size() == 0) {   
			return 0;   
		}   

		size_t find_pos = 0; //the finding position   
		while (true) {      
			size_t split_pos = std::wstring::npos; // 最近的分隔符位置    
			size_t delimiter_size = 0; // 所用到的分隔符长度   
			std::vector<std::wstring>::const_iterator itrbeg = delimiters.begin();   
			while (itrbeg != delimiters.end()) {   
				size_t tpos = inputstr.find(*itrbeg, find_pos);   
				if ((split_pos != std::wstring::npos && tpos != std::wstring::npos && tpos < split_pos)   
					|| (split_pos == std::wstring::npos && tpos != std::wstring::npos)) {   
						split_pos = tpos;   
						delimiter_size = itrbeg->size();   
				}   
				++itrbeg;   
			}   
			if (split_pos != std::wstring::npos) {   
				substrs.push_back(inputstr.substr(find_pos, split_pos - find_pos));   
				find_pos = split_pos + delimiter_size;   
			}   
			else {   
				substrs.push_back(inputstr.substr(find_pos)); // all the remainder is pushed back   
				break;   
			}   

		};   
		return static_cast<int>(substrs.size());    
	}
	//////////////////////////////////////////////////////////////////////////
	//String To NUM////
	//////////////////////////////////////////////////////////////////////////
	template<typename T> 
	T StrToNum(const std::string& str)
	{
		std::stringstream strstream(str);

		//strstream.setf(ios::hex,ios::basefield);

		T val;

		strstream >> val;

		return val;
	}
	//////////////////////////////////////////////////////////////////////////
	//WString To NUM////
	//////////////////////////////////////////////////////////////////////////
	template<typename T> 
	T WStrToNum(const std::wstring& str)
	{
		std::wstringstream strstream(str);

		//strstream.setf(ios::hex,ios::basefield);

		T val;

		strstream >> val;

		return val;
	}
        
	inline std::string to_string(const std::wstring& str)
    {
        return ws2s(str);
    }
    
    inline std::wstring to_wstring(const std::string& str)
    {
        return s2ws(str);
    }
    
    inline int randBetween(int a,int b)
    {
        if (a == b) return a;
        unsigned int lit = a < b ? a : b;
        unsigned int interval = a > b ? a - b + 1 : b - a + 1;
        srand((unsigned int)(time(NULL)));
        return lit + rand() % interval;
    }

    inline std::wstring intTowstring(int num)
    {
        return NumTowstring<int>(num);
    }

	class ComparePinyin
	{
		public:
		enum PinyinOrder{
			ORDERATOZ,
			ORDERZTOA
		};
		explicit ComparePinyin(const PinyinOrder& order):myOrder(order){}
		int operator()(const std::wstring& str1, const std::wstring& str2)
		{
			if (myOrder == ORDERATOZ)
				return wcscoll(str1.c_str(),str2.c_str()) < 0;
			else 
				return wcscoll(str1.c_str(),str2.c_str()) > 0;					 
		}
	private:
		PinyinOrder myOrder;
	};

	////两数之间取随机数/////////////////////////
	inline unsigned int randBetween(unsigned int s, unsigned int e)
	{
		if(s == e) return s;

		unsigned int lit = e > s ? s : e;
		unsigned int interval = e > s ? e-s+1 : s-e+1;
		srand((unsigned int)(time(NULL)));

		return lit + rand() %interval;
	}
    
    inline int strcmpByPinyin(const std::wstring& str1, const std::wstring& str2)
	{
        return  wcscoll(str1.c_str(),str2.c_str());
    }
    
    std::string SUNWstring2String(const std::wstring& src);
    std::wstring SUNString2Wstring(const std::string& src);
    
    tm getTimeStruct(long time);
}

#endif
