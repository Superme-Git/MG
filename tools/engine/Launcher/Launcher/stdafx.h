// stdafx.h : include file for standard system include files,
// or project specific include files that are used frequently, but
// are changed infrequently
//

#pragma once

#ifndef _WIN32_WINNT		// Allow use of features specific to Windows XP or later.                   
#define _WIN32_WINNT 0x0501	// Change this to the appropriate value to target other versions of Windows.
#endif						

#include <stdio.h>
#include <tchar.h>

#define WIN32_LEAN_AND_MEAN
#include <Windows.h>
#include <Shellapi.h>
#include <Shlwapi.h>
#pragma comment(lib, "Shlwapi.lib")

#include <string>
namespace std
{
	typedef basic_string<TCHAR, char_traits<TCHAR>, allocator<TCHAR> > tstring;
}

// TODO: reference additional headers your program requires here
