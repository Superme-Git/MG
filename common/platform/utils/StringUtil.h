//  StringUtil.h
//  share

#ifndef share_StringUtil_h
#define share_StringUtil_h

#include <string>



////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
#ifdef ANDROID
	#undef wcscspn
	#undef wcpncpy	
	#undef wcpcpy
	#undef wcscasecmp
	#undef wcschrnul
	#undef wcscmp
	#undef wcschr
	#undef wcslen_x
	#undef wcscat
	#undef wcsdup	
	#undef wcsncasecmp
	#undef wcsncat	
	#undef wcsncpy	
	#undef wcsnlen	

	#define wcscspn     wcscspn_x
	#define wcpncpy		wcpncpy_x
	#define wcpcpy		wcpcpy_x
	#define wcscasecmp	wcscasecmp_x
	#define wcschrnul	wcschrnul_x
	#define wcscmp		wcscmp_x
	#define wcschr		wcschr_x
	#define wcslen_x	wcslen_x
	#define wcscat		wcscat_x
	#define wcsdup		wcsdup_x
	#define wcsncasecmp	wcsncasecmp_x
	#define wcsncat		wcsncat_x
	#define wcsncpy		wcsncpy_x
	#define wcsnlen		wcsnlen_x

	size_t     wcscspn_x    (const wchar_t *wcs, const wchar_t *reject);
	wchar_t *  wcpncpy_x    (wchar_t *dest, const wchar_t *src , size_t n);
	wchar_t *  wcpcpy_x     (wchar_t *dest, const wchar_t *src);
	int        wcscasecmp_x (const wchar_t *s1, const wchar_t *s2);
	wchar_t *  wcschrnul_x  (const wchar_t *wcs, const wchar_t wc);
	int        wcscmp_x     (const wchar_t *s1, const wchar_t *s2);
	wchar_t *  wcschr_x     (const wchar_t *wcs, const wchar_t wc);
	size_t     wcslen_x     (const wchar_t *s);
	wchar_t *  wcscat_x     (wchar_t *dest, const wchar_t *src);
	wchar_t *  wcsdup_x     (const wchar_t *s);
	int        wcsncasecmp_x(const wchar_t *s1, const wchar_t *s2, size_t n );
	wchar_t *  wcsncat_x    (wchar_t *dest, const wchar_t *src, size_t n);
	wchar_t *  wcsncpy_x    (wchar_t *dest, const wchar_t *src, size_t n);
	size_t     wcsnlen_x    (const wchar_t *s, size_t maxlen);
	wchar_t *  wcsstr_x     (const wchar_t *haystack, const wchar_t *needle);
	int        swprintf_x   (wchar_t *buffer,size_t count, const wchar_t *fmt , ... );

	void       tolower_x    (wchar_t * buffer);
	void       toupper_x    (wchar_t * buffer);
#endif
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////


int vsnwprintf_x(wchar_t* _out , size_t count , const wchar_t * wcsfmtStr , va_list ap);
int SprintfW( wchar_t* out, int len ,const wchar_t *format ,...);
int FormatString( std::wstring& instring,const wchar_t *format ,...);

std::string     ws2s(std::wstring& ws);
std::wstring    s2ws(std::string& s);
std::string		ws2s(const std::wstring & ws);
std::wstring    s2ws(const std::string& s);

std::string SHARE_Wstring2String(const std::wstring& src);
std::wstring SHARE_String2Wstring(const std::string& src);

inline std::string intToString(int val)
{
    char buff[64] = {0};
    sprintf(buff, "%d", val);
    
    return std::string(buff);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
#endif
