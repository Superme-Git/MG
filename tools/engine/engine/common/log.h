#ifndef __Nuclear_LOG_H
#define __Nuclear_LOG_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <stdarg.h>
#include <time.h>
#include <locale.h>
//#include <dxerr9.h>

#define XPV(x) do { hr = (x); if( FAILED(hr) ) { DXTraceW(__FILE__, __LINE__, hr, L#x, TRUE);  }} while(0)

template<class T>
inline void xwprint(const wchar_t* fmt, ... )
{
	setlocale(LC_CTYPE, "chinese_china");
	FILE *f = NULL;
	if( _wfopen_s(&f, T::fn(), L"a+")) return;
	time_t now = time(NULL);
	struct tm t;
	localtime_s(&t, &now);
	fwprintf(f, L"%d, %4d-%02d-%02d %02d:%02d:%02d:\t", T::s_nProcessId, t.tm_year+1900, t.tm_mon+1, t.tm_mday, t.tm_hour, t.tm_min, t.tm_sec);
    va_list args;
    va_start(args, fmt);
    vfwprintf(f, fmt, args);
    va_end(args);
	fclose(f);
}

template<class T>
inline void checklogfile()
{
	struct _stat info;
	int ret = _wstat(T::fn(), &info);
	if (ret == 0 && info.st_size > 1024 * 1024 * 1)//1M
	{
		_wremove(T::fn());
	}
}

template<class T>
inline void dellogfile()
{
	_wremove(T::fn());
}

struct XL_INFO 
{ 
	static DWORD s_nProcessId;
	static const wchar_t *fn() { return L"log/info.log"; } 
};
struct XL_ERROR 
{ 
	static DWORD s_nProcessId;
	static const wchar_t *fn() { return L"log/error.log"; } 
};

#ifdef XDEBUG_NONE
#undef XDEBUG_INFO
#undef XDEBUG_ERROR
#undef XDEBUG_TRACE
#else
#ifdef XDEBUG_ALL
#define XDEBUG_INFO
#define XDEBUG_ERROR
#define XDEBUG_TRACE
#endif
#endif

#ifdef XDEBUG_INFO
#define XPLOG_INFO xwprint<XL_INFO>
#define XPLOG_INFO_CHECK checklogfile<XL_INFO>
#else
#define XPLOG_INFO(...)
#define XPLOG_INFO_CHECK dellogfile<XL_INFO>
#endif

#ifdef XDEBUG_ERROR
#define XPLOG_ERROR xwprint<XL_ERROR>
#define XPLOG_ERROR_CHECK checklogfile<XL_ERROR>
#else
#define XPLOG_ERROR(...)
#define XPLOG_ERROR_CHECK dellogfile<XL_ERROR>
#endif

#ifdef XDEBUG_TRACE
inline void XPTRACE(const wchar_t* fmt, ...){
	va_list args; 
	va_start(args, fmt);
	wchar_t ss[1024];
	_vsnwprintf_s(ss, 1024, 1024, fmt, args);
	OutputDebugStringW(ss);
	va_end(args); 
}
#else
#define XPTRACE(...)
#endif

#define XPALARM(x) do { ::MessageBox(0, x, 0, 0); XPTRACE(x); XPLOG_ERROR(x); } while(0)

#ifndef XPASSERT
#ifdef _DEBUG
#define XPASSERT(x) do { \
		if(!(x) ) { \
			if( IDYES == MessageBoxA(NULL, "Assertion Failed in "__FILE__ " \nExpr is \"" #x "\", Teminate Process?", "Assertion Failed", MB_YESNO) ) exit(0); \
		} \
	} while(0)
#else
	#define XPASSERT(...)
#endif
#endif

#endif