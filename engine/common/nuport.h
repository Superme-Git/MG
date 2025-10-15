//  port.h
//  engine

#ifndef __Nuclear_port_H__
#define __Nuclear_port_H__

#include "platform/platform.h"

#if defined WIN32 || defined _WIN32
#if !(defined WIN7_32) && !(defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
typedef int64 __int64;
#else
typedef __int64 int64;
#endif
#else
typedef unsigned short WORD;
typedef long int LONG;
typedef long long int64;
typedef int64 __int64;
typedef unsigned long long ULONG64;
typedef unsigned int DWORD;
typedef unsigned long DWORD_PTR;
#define MAX_PATH 256
#define localtime_s(a,b) localtime_r(b,a)
typedef const wchar_t* LPCTSTR;
typedef const char* LPCSTR;
typedef const wchar_t* LPCWSTR;
typedef wchar_t* LPWSTR;
typedef wchar_t TCHAR;
#ifdef _OS_IOS
#ifndef OBJC_BOOL_DEFINED
    typedef signed char BOOL;
#endif
#endif
typedef unsigned int UINT;
typedef unsigned int HANDLE;

typedef unsigned int HWND;
typedef  int WPARAM;
typedef  int LPARAM;

#ifndef NULL
#define NULL 0
#endif

#define TRUE    true
#define FALSE   false

typedef unsigned char BYTE;

#include "nugeom.h"
#endif //WIN32

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
unsigned int _wtou(const wchar_t* str);
#endif

#include <cocos2d.h>

#endif //__Nuclear_port_H__
