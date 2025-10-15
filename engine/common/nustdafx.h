#if _MSC_VER > 1000
#pragma once
#endif
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <windows.h>
#else
#ifndef WINVER				// 允许使用特定于 Windows 2000 或更高版本的功能。
#define WINVER 0x0500
#endif

#ifndef _WIN32_WINNT		// 允许使用特定于 Windows 2000 或更高版本的功能。
#define _WIN32_WINNT 0x0500
#endif						
//
//#ifndef _WIN32_WINDOWS		// 允许使用特定于 Windows 98 或更高版本的功能。
//#define _WIN32_WINDOWS 0x0410
//#endif
#endif

#include <stdio.h>
//#include <tchar.h>
#include <string>
//Assert
#include <assert.h>
//STL
#include <vector>
#include <map>
#include <list>
#include <set>
#include <deque>
//#include <hash_map>

#include "util.h"