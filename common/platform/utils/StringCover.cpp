#include "StringCover.h"
#include "FileUtil.h"
#include "StringUtil.h"
#include "stringbuilder.h"
#include <stdarg.h>


#if !(defined ANDROID) && !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
#include <xlocale.h>
#endif

typedef unsigned char   utf8;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
typedef  unsigned short  utf32;
#else
typedef unsigned int    utf32;
#endif
typedef	unsigned int    uint;

namespace StringCover
{
	static ComparePinyin ComparePinyinATOZ(StringCover::ComparePinyin::ORDERATOZ);
	static ComparePinyin ComparePinyinZTOA(StringCover::ComparePinyin::ORDERZTOA);

    int FormatString( std::wstring& instring,const wchar_t *format ,...)
    {
        va_list ap;
        va_start(ap,format);
        wchar_t buffer[1024] = {0};
        
        int numforwrite = 0;
        
        numforwrite = vsnwprintf_x( buffer,1024,format,ap);
        
        if ( numforwrite > 0)
        {
            std::wstring str(buffer);
            instring = str;
        }
        
        else
            instring = L"";
        
        va_end(ap);
        return numforwrite;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    typedef unsigned char utf8;
    typedef unsigned int utf32 ;
    
    // return number of code units in a null terminated string
	size_t utf_length(const utf8* utf8_str)
	{
		size_t cnt = 0;
		while (*utf8_str++)
			cnt++;
        
		return cnt;
	}
    
	// return number of code units in a null terminated string
	size_t utf_length(const utf32* utf32_str)
	{
		size_t cnt = 0;
		while (*utf32_str++)
			cnt++;
        
		return cnt;
	}
    
    // return the number of utf8 code units required to encode the given utf32 code point
	size_t encoded_size(utf32 code_point)
	{
		if (code_point < 0x80)
			return 1;
		else if (code_point < 0x0800)
			return 2;
		else if (code_point < 0x10000)
			return 3;
		else
			return 4;
	}
    
    // return number of code units required to re-encode given utf32 data as utf8.   len is number of code units in 'buf'.
	size_t encoded_size(const utf32* buf, size_t len)
	{
		size_t count = 0;
        
		while (len--)
		{
			count += encoded_size(*buf++);
		}
        
		return count;
	}
    
	// return number of code units required to re-encode given null-terminated utf32 data as utf8.  return does not include terminating null.
	size_t encoded_size(const utf32* buf)
	{
		return encoded_size(buf, utf_length(buf));
	}
    
    // return number of utf32 code units required to re-encode given utf8 data as utf32.  len is number of code units in 'buf'.
	size_t encoded_size(const utf8* buf, size_t len)
	{
		utf8 tcp;
		size_t count = 0;
        
		while (len--)
		{
			tcp = *buf++;
			++count;
			size_t size = 0;
            
			if (tcp < 0x80)
			{
			}
			else if (tcp < 0xE0)
			{
				size = 1;
				++buf;
			}
			else if (tcp < 0xF0)
			{
				size = 2;
				buf += 2;
			}
			else
			{
				size = 3;
				buf += 3;
			}
            
			if (len >= size)
				len -= size;
			else
				break;
		}
        
		return count;
	}
    
	// return number of utf32 code units required to re-encode given utf8 data as utf32.  return does not include terminating null.
	size_t encoded_size(const utf8* buf) 
	{
		return encoded_size(buf, utf_length(buf));
	}

    // encoding functions
	// for all:
	//	src_len is in code units, or 0 for null terminated string.
	//	dest_len is in code units.
	//	returns number of code units put into dest buffer.
	size_t encode(const utf32* src, utf8* dest, size_t dest_len, size_t src_len = 0)
	{
		// count length for null terminated source...
		if (src_len == 0)
		{
			src_len = utf_length(src);
		}
        
		size_t destCapacity = dest_len;
        
		// while there is data in the source buffer,
		for (uint idx = 0; idx < src_len; ++idx)
		{
			utf32	cp = src[idx];
            
			// check there is enough destination buffer to receive this encoded unit (exit loop & return if not)
			if (destCapacity < encoded_size(cp))
			{
				break;
			}
            
			if (cp < 0x80)
			{
				*dest++ = (utf8)cp;
				--destCapacity;
			}
			else if (cp < 0x0800)
			{
				*dest++ = (utf8)((cp >> 6) | 0xC0);
				*dest++ = (utf8)((cp & 0x3F) | 0x80);
				destCapacity -= 2;
			}
			else if (cp < 0x10000)
			{
				*dest++ = (utf8)((cp >> 12) | 0xE0);
				*dest++ = (utf8)(((cp >> 6) & 0x3F) | 0x80);
				*dest++ = (utf8)((cp & 0x3F) | 0x80);
				destCapacity -= 3;
			}
			else
			{
				*dest++ = (utf8)((cp >> 18) | 0xF0);
				*dest++ = (utf8)(((cp >> 12) & 0x3F) | 0x80);
				*dest++ = (utf8)(((cp >> 6) & 0x3F) | 0x80);
				*dest++ = (utf8)((cp & 0x3F) | 0x80);
				destCapacity -= 4;
			}
		}
        
		return dest_len - destCapacity;
	}
    
	size_t encode(const utf8* src, utf32* dest, size_t dest_len, size_t src_len = 0)
	{
		// count length for null terminated source...
		if (src_len == 0)
		{
			src_len = utf_length(src);
		}
        
		size_t destCapacity = dest_len;
        
		// while there is data in the source buffer, and space in the dest buffer
		for (uint idx = 0; ((idx < src_len) && (destCapacity > 0));)
		{
			utf32	cp;
			utf8	cu = src[idx++];
            
			if (cu < 0x80)
			{
				cp = (utf32)(cu);
			}
			else if (cu < 0xE0)
			{
				cp = ((cu & 0x1F) << 6);
				cp |= (src[idx++] & 0x3F);
			}
			else if (cu < 0xF0)
			{
				cp = ((cu & 0x0F) << 12);
				cp |= ((src[idx++] & 0x3F) << 6);
				cp |= (src[idx++] & 0x3F);
			}
			else
			{
				cp = ((cu & 0x07) << 18);
				cp |= ((src[idx++] & 0x3F) << 12);
				cp |= ((src[idx++] & 0x3F) << 6);
				cp |= (src[idx++] & 0x3F);
			}
            
			*dest++ = cp;
			--destCapacity;
		}
        
		return dest_len - destCapacity;
	}
    
    std::string SUNWstring2String(const std::wstring& src)
    {
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		return ws2s(src);
#else
		const int MAX_STRING_PATH = 4096;
		utf8 dst[MAX_STRING_PATH];
		size_t length = encode((utf32*)src.c_str(), dst, MAX_STRING_PATH);
		return std::string((char*)dst, length);
#endif
    }
    
    std::wstring SUNString2Wstring(const std::string& src)
    {
        const int MAX_STRING_PATH = 4096;
        utf32 dst[MAX_STRING_PATH];
        size_t length = encode((utf8*)src.c_str(), dst, MAX_STRING_PATH);
        return std::wstring((wchar_t*)dst, length);
    }

	char Hex2Value(char in)
	{
		switch (in)
		{
		case '0': return 0;
		case '1': return 1;
		case '2': return 2;
		case '3': return 3;
		case '4': return 4;
		case '5': return 5;
		case '6': return 6;
		case '7': return 7;
		case '8': return 8;
		case '9': return 9;
		case 'A': return 10;
		case 'B': return 11;
		case 'C': return 12;
		case 'D': return 13;
		case 'E': return 14;
		case 'F': return 15;
		default:
			assert(false);
			break;
		}
		return '?';
	}

	char Value2Hex(char in) 
	{
		switch (in)
		{
		case 0: return '0';
		case 1: return '1';
		case 2: return '2';
		case 3: return '3';
		case 4: return '4';
		case 5: return '5';
		case 6: return '6';
		case 7: return '7';
		case 8: return '8';
		case 9: return '9';
		case 10: return 'A';
		case 11: return 'B';
		case 12: return 'C';
		case 13: return 'D';
		case 14: return 'E';
		case 15: return 'F';
		default:
			assert(false);
			break;
		}
		return '?';
	}

	std::wstring WString2Hex(const wchar_t *pIn, const int nInLength)
	{
#if defined __cplusplus && __cplusplus >= 201103L
		//static_assert(sizeof(wchar_t) == 2, "sizeof(wchar_t) != 2");
#endif

		std::wstring strOut;
		strOut.reserve(nInLength * 4);

		for (int i = 0; i < nInLength; ++i)
		{
			wchar_t nValue = pIn[i];
			strOut += Value2Hex((nValue >> 12) & 0xF);
			strOut += Value2Hex((nValue >> 8) & 0xF);
			strOut += Value2Hex((nValue >> 4) & 0xF);
			strOut += Value2Hex((nValue)& 0xF);
		}
#if defined __cplusplus && __cplusplus >= 201103L
		return std::move(strOut);
#else
		return strOut;
#endif
	}

	std::wstring Hex2WString(const wchar_t *pIn, const int nInLength)
	{
#if defined __cplusplus && __cplusplus >= 201103L
		//static_assert(sizeof(wchar_t) == 2, "sizeof(wchar_t) != 2");
#endif

		std::wstring strOut;

		if (nInLength % 4 != 0)
		{
#if defined __cplusplus && __cplusplus >= 201103L
			return std::move(strOut);
#else
			return strOut;
#endif
		}

		strOut.reserve(nInLength / 4);

		for (int i = 0; i < nInLength / 4; ++i)
		{
			wchar_t nValue = 0;
			nValue += Hex2Value(*pIn++) << 12;
			nValue += Hex2Value(*pIn++) << 8;
			nValue += Hex2Value(*pIn++) << 4;
			nValue += Hex2Value(*pIn++);
			strOut += nValue;
		}
#if defined __cplusplus && __cplusplus >= 201103L
		return std::move(strOut);
#else
		return strOut;
#endif
	}

    //秒数转换为日期：—年—月—日
    std::wstring int64_tToDateTime(int64_t time)
    {
        time_t et = time;
        struct tm times;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
        localtime_s(&times, &et);
#else
        localtime_r(&et, &times);
#endif
        
        std::wstring strtime;
        FormatString(strtime,L"%d-%d-%d",(1900 + times.tm_year), 1 + times.tm_mon,times.tm_mday);
        
        return strtime;
    }
    
    //秒数转换为日期：—年—月—日 —时—分
    std::wstring int64_tToPrecisionTime(int64_t time)
    {
        time_t et = time;
        struct tm times;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		localtime_s(&times, &et);
#else
        localtime_r(&et, &times);
#endif
        std::wstring strtime;
        if(times.tm_min < 10)
        {
            FormatString(strtime,L"%d-%d-%d  %d:0%d",(1900 + times.tm_year), 1 + times.tm_mon,times.tm_mday,times.tm_hour,times.tm_min);
        }
        else
        {
            FormatString(strtime,L"%d-%d-%d  %d:%d",(1900 + times.tm_year), 1 + times.tm_mon,times.tm_mday,times.tm_hour,times.tm_min);
        }
        
        return strtime;
    }
    
    //秒数转换为日期：—时—分
     std::wstring int64_tToClockTime(int64_t time)
    {
        time_t et = time;
        struct tm times;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
        localtime_s(&times, &et);
#else
        localtime_r(&et, &times);
#endif
        
        std::wstring strtime;
        FormatString(strtime,L"%d:%.2d",times.tm_hour, times.tm_min);
        
        return strtime;
    }
    
    //		//秒数转换为日期：*年*月*日 时:分
    std::wstring int64_tToPrecisionTime3(int64_t time)
    {
        time_t et = time;
        struct tm times;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
        localtime_s(&times, &et);
#else
        localtime_r(&et, &times);
#endif
        
        std::wstring strtimedate = L"diansha";
		//	FormatString(strtimedate,MHSD_UTILS::GETSTRING(1237).c_str(),(1900 + times.tm_year), 1 + times.tm_mon,times.tm_mday);
        
        std::wstring strtimetime;
        FormatString(strtimetime,L" %2d:%02d",times.tm_hour,times.tm_min);
        
        strtimedate = strtimedate + strtimetime;
        return strtimedate;
    }
    
    //		//秒数转换为时间：？天 ？时？分
    std::wstring int64_tToTimeDHM(int64_t time)
    {
        int day = int(time/3600/24);			//天数
        int hour = int((time%(24*3600))/3600);	//小时数
        int minute  =  int((time%3600)/60);		//分钟数
        std::wstring strtime = L"yiyiyiyiyi";
        
        if(hour > 0)
        {
            //FormatString(strtime,MHSD_UTILS::GETSTRING(2519).c_str(),day, hour,minute);
        }
        else
        {
            //FormatString(strtime,MHSD_UTILS::GETSTRING(2519).c_str(),day, hour,minute);
        }
        
        return strtime;
    }
    
    std::wstring int64_tToTimeDHMShort(int64_t time)
    {
        int day = int(time/3600/24);			//天数
        int hour = int((time%(24*3600))/3600);	//小时数
        int minute  =  int((time%3600)/60);		//分钟数
        std::wstring strtime;
        
        if(hour > 0)
        {
            STRB::CStringBuilderW	sb;
            sb.SetFormat(L"parameter1", L"%d", day);
            sb.SetFormat(L"parameter2", L"%d", hour);
            sb.SetFormat(L"parameter3", L"%d", minute);
            //strtime = sb.GetString(MHSD_UTILS::GETSTRING(2963));//shield by yyy
        }
        else
        {
            STRB::CStringBuilderW	sb;
            sb.SetFormat(L"parameter1", L"%d", day);
            sb.SetFormat(L"parameter2", L"%d", hour);
            sb.SetFormat(L"parameter3", L"%d", minute);
            //strtime = sb.GetString(MHSD_UTILS::GETSTRING(2963));//shield by yyy
        }
        
        return strtime;
    }
    //秒数转换为时间： ？时？分？秒
    std::wstring intToTimeHMS(int time)
    {
        int hour = time/3600;			//小时数
        int minute  =  (time%3600)/60;	//分钟数
        int second = time%60;			//秒数
        std::wstring strtime;
        
        if(hour > 0)
        {
            FormatString(strtime,L"%02d:%02d:%02d",hour, minute,second);
        }
        else
        {
            FormatString(strtime,L"%02d:%02d",minute,second);
        }
        
        return strtime;
    }
    
	tm getTimeStruct(long time)
	{
		time_t et = time;
		struct tm times;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		localtime_s(&times, &et);
#else
		struct tm *tempt;
		//取标准时间  然后加上北京时区8个小时
		time_t newEt = time + 3600 * 8;
		tempt = gmtime(&newEt);
		times = *tempt;
#endif
		return times;
	}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
}
