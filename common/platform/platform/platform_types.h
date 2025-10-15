//  types.h
//  platform

#ifndef platform_types_h
#define platform_types_h
//#include "platform/platform.h"

#if defined _OS_WINDOWS || defined WIN32 || defined _WIN32
typedef __int64 int64;
#endif //XPP_WINDOWS

#ifdef _OS_IOS
typedef unsigned short WORD;
typedef long int LONG;
typedef long long int int64;
typedef int64 __int64;
typedef unsigned long long ULONG64;
typedef unsigned int DWORD;
typedef unsigned long DWORD_PTR;
#define localtime_s(a,b) localtime_r(b,a)
typedef const wchar_t* LPCTSTR;
typedef const char* LPCSTR;
typedef const wchar_t* LPCWSTR;
typedef wchar_t* LPWSTR;
typedef wchar_t TCHAR;
#ifndef OBJC_BOOL_DEFINED
    typedef signed char BOOL;
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

#define MAKEWORD(a,b) ((WORD)(((BYTE)((DWORD_PTR)(a)&0xff))|((WORD)((BYTE)((DWORD_PTR)(b) & 0xff))) << 8))

int _wtoi(const wchar_t* str);
float _wtof(const wchar_t* str);
int64 _wtoi64(const wchar_t* str);
long _wtol(const wchar_t* str);
unsigned int _wtou(const wchar_t* str);
//#include "geom.h"

#elif (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
unsigned int _wtou(const wchar_t* str);
#endif //XPP_IOS

#endif

#ifdef WIN7_32
	#ifdef _DEBUG
		#define NoPack
	#endif
#elif defined ANDROID
	//#ifdef _DEBUG
	//	#define NoPack
	//#endif
#else//IOS
	//#ifdef _DEBUG
	//	#define NoPack
	//#endif
#endif
