#ifndef __Nuclear_LOG_H__
#define __Nuclear_LOG_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <stdarg.h>
#include <time.h>
#include <locale.h>
#include <stdio.h>
#include <wchar.h>
#include "nuport.h"
#include <utils/StringUtil.h>

#define XPV(x) do { hr = (x); if( FAILED(hr) ) { DXTraceW(__FILE__, __LINE__, hr, L#x, TRUE);  }} while(0)

template<class T>
inline void checklogfile()
{
	struct _stat info;
	int ret = _wstat(T::fn(), &info);
	if (ret == 0 && info.st_size > 1024 * 1024 * 1)//1M
	{
	}
}

template<class T>
inline void dellogfile()
{
}

template<class T>
inline void xwprint(const wchar_t* fmt, ...)
{
	setlocale(LC_CTYPE, "chinese_china");
	FILE *f = NULL;

	std::wstring filename = T::fn();
	if (!(f = fopen(ws2s(filename).c_str(), "a+"))) return;
	time_t now = time(NULL);
	struct tm t;
	localtime_s(&t, &now);
	fwprintf(f, L"%d, %4d-%02d-%02d %02d:%02d:%02d:\t", T::s_nProcessId, t.tm_year + 1900, t.tm_mon + 1, t.tm_mday, t.tm_hour, t.tm_min, t.tm_sec);
	va_list args;
	va_start(args, fmt);
	vfwprintf(f, fmt, args);
	va_end(args);
	fclose(f);
}

struct NL_INFO 
{ 
	static DWORD s_nProcessId;
	static wchar_t s_path[MAX_PATH];
	static const wchar_t *fn() { return s_path; } 
};
struct NL_ERROR 
{ 
	static DWORD s_nProcessId;	
	static wchar_t s_path[MAX_PATH];
	static const wchar_t *fn() { return s_path; } 
};
struct NL_SEGMPAK
{
	static DWORD s_nProcessId;
	static wchar_t s_path[MAX_PATH];
	static const wchar_t *fn() { return s_path; } 
};

#ifdef XDEBUG_NONE
#undef XDEBUG_INFO
#undef XDEBUG_ERROR
#undef XDEBUG_TRACE
#undef XDEBUG_SEGMPAK
#else
#ifdef XDEBUG_ALL
#define XDEBUG_INFO
#define XDEBUG_ERROR
#define XDEBUG_TRACE
#define XDEBUG_SEGMPAK
#endif
#endif

#ifdef XDEBUG_INFO
#define XPLOG_INFO xwprint<NL_INFO>
#define XPLOG_INFO_CHECK checklogfile<NL_INFO>
#else
#define XPLOG_INFO(...)
#define XPLOG_INFO_CHECK dellogfile<NL_INFO>
#endif

#ifdef XDEBUG_SEGMPAK_NONE
#undef XDEBUG_SEGMPAK
#endif

#ifdef XDEBUG_SEGMPAK
#define XPLOG_SEGMPAK xwprint<NL_SEGMPAK>
#define XPLOG_SEGMPAK_CHECK checklogfile<NL_SEGMPAK>
#else
#define XPLOG_SEGMPAK(...)
#define XPLOG_SEGMPAK_CHECK dellogfile<NL_SEGMPAK>
#endif

#ifdef XDEBUG_ERROR
#define XPLOG_ERROR xwprint<NL_ERROR>
#define XPLOG_ERROR_CHECK checklogfile<NL_ERROR>
#else
#define XPLOG_ERROR(...)
#define XPLOG_ERROR_CHECK dellogfile<NL_ERROR>
#endif

#ifdef XDEBUG_TRACE
inline void XPTRACE(const wchar_t* fmt, ...){
	va_list args; 
	va_start(args, fmt);
	wchar_t ss[1024];
	_vsnwprintf_s(ss, 1024, 1024, fmt, args);
#ifdef _DEBUG
	OutputDebugStringW(ss);
#endif
	va_end(args); 
}
#else
#define XPTRACE(...)
#endif

#define XPALARM(x) do { ::MessageBox(0, x, 0, 0); XPTRACE(x); XPLOG_ERROR(x); } while(0)

#ifndef XPASSERT

#ifdef _DEBUG
#define XDEBUG_ASSERT
#endif

#ifdef XDEBUG_ASSERT
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <cocos2d.h>
#define XPASSERT(x) do { \
		if(!(x) ) { \
			cocos2d::CCMessageBox("Assertion Failed in "__FILE__ " \nExpr is \"" #x "\", Teminate Process?", "Assertion Failed"); \
		} \
	} while(0)
#else
#define XPASSERT(x) do { \
		if(!(x) ) { \
			if( IDYES == MessageBoxA(NULL, "Assertion Failed in "__FILE__ " \nExpr is \"" #x "\", Teminate Process?", "Assertion Failed", MB_YESNO) ) exit(0); \
		} \
	} while(0)
#endif
#else
	#define XPASSERT(...)
#endif
#endif

#endif