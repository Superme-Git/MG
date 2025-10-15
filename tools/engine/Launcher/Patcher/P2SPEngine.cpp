#include "stdafx.h"
#include "P2SPEngine.h"

#ifdef VERSION_TIYAN
	#define GAME_ID 9001
#else
	#define GAME_ID 9
#endif

#ifdef DEBUG
	#define CONST_STRING_DLL_PATH L"p2sp_4th_lib.dll"
#else
	#define CONST_STRING_DLL_PATH L"p2sp_4th_lib.dll"
#endif

typedef bool (__cdecl * DLL_P2SPSTART)(int gameid);
typedef bool (__cdecl * DLL_P2SPSETTING)(const SettingItemT* setting); 
typedef bool (__cdecl * DLL_P2SPGETSETTING)(SettingItemT* setting); 
typedef bool (__cdecl * DLL_P2SPADDDOWNLOAD)(const char* filemd5);
typedef bool (__cdecl * DLL_P2SPSTARTDOWNLOAD)(const char* filemd5);
typedef bool (__cdecl * DLL_P2SPSTOPDOWNLOAD)(const char* filemd5);
typedef bool (__cdecl * DLL_P2SPDELETEDOWNLOAD)(const char* filemd5);
typedef bool (__cdecl * DLL_P2SPGETTASKINFOMATION)(TaskInfomationT* info);
typedef bool (__cdecl * DLL_P2SPSTOP)();

HMODULE P2SPEngine::s_hHandle = NULL;

bool P2SPEngine::dll_p2spStart()
{
	s_hHandle = ::LoadLibrary(CONST_STRING_DLL_PATH);
	if(s_hHandle)
	{
		DLL_P2SPSTART h_dll_p2spStart = NULL;
		h_dll_p2spStart = (DLL_P2SPSTART)::GetProcAddress(s_hHandle, "p2spStart");
		if(h_dll_p2spStart)
		{
			return h_dll_p2spStart(GAME_ID);
		}
	}
	return false;
}

bool P2SPEngine::dll_p2spStop()
{
	bool ret = false;
	if(s_hHandle)
	{
		DLL_P2SPSTOP h_dll_p2spStop = NULL;
		h_dll_p2spStop = (DLL_P2SPSTOP)::GetProcAddress(s_hHandle, "p2spStop");
		if(h_dll_p2spStop)
		{
			ret = h_dll_p2spStop();
		}

		FreeLibrary(s_hHandle);
		s_hHandle = NULL;
	}
	return ret;
}

bool P2SPEngine::dll_p2spSetting(const SettingItemT* setting)
{
	if (s_hHandle)
	{
		DLL_P2SPSETTING h_dll_p2spSetting = NULL;
		h_dll_p2spSetting = (DLL_P2SPSETTING)::GetProcAddress(s_hHandle, "p2spSetting");
		if (h_dll_p2spSetting)
		{
			return h_dll_p2spSetting(setting);
		}
	}
	return false;
}

bool P2SPEngine::dll_p2spGetSetting(SettingItemT* setting)
{
	if (s_hHandle)
	{
		DLL_P2SPGETSETTING h_dll_p2spGetSetting = NULL;
		h_dll_p2spGetSetting = (DLL_P2SPGETSETTING)::GetProcAddress(s_hHandle, "p2spGetSetting");
		if (h_dll_p2spGetSetting)
		{
			return h_dll_p2spGetSetting(setting);
		}
	}
	return false;
}

bool P2SPEngine::dll_p2spAddDownload(const char* filemd5)
{
	if (s_hHandle)
	{
		DLL_P2SPADDDOWNLOAD h_dll_p2spAddDownload = NULL;
		h_dll_p2spAddDownload = (DLL_P2SPADDDOWNLOAD)::GetProcAddress(s_hHandle, "p2spAddDownload");
		if (h_dll_p2spAddDownload)
		{
			return h_dll_p2spAddDownload(filemd5);
		}
	}
	return false;
}

bool P2SPEngine::dll_p2spStartDownload(const char* filemd5)
{
	if (s_hHandle)
	{
		DLL_P2SPSTARTDOWNLOAD h_dll_p2spStartDownload = NULL;
		h_dll_p2spStartDownload = (DLL_P2SPSTARTDOWNLOAD)::GetProcAddress(s_hHandle, "p2spStartDownload");
		if (h_dll_p2spStartDownload)
		{
			return h_dll_p2spStartDownload(filemd5);
		}
	}
	return false;
}

bool P2SPEngine::dll_p2spStopDownload(const char* filemd5)
{
	if (s_hHandle)
	{
		DLL_P2SPSTOPDOWNLOAD h_dll_p2spStopDownload = NULL;
		h_dll_p2spStopDownload = (DLL_P2SPSTOPDOWNLOAD)::GetProcAddress(s_hHandle, "p2spStopDownload");
		if (h_dll_p2spStopDownload)
		{
			return h_dll_p2spStopDownload(filemd5);
		}
	}
	return false;
}

bool P2SPEngine::dll_p2spDeleteDownload(const char* filemd5)
{
	if (s_hHandle)
	{
		DLL_P2SPDELETEDOWNLOAD h_dll_p2spDeleteDownload = NULL;
		h_dll_p2spDeleteDownload = (DLL_P2SPDELETEDOWNLOAD)::GetProcAddress(s_hHandle, "p2spDeleteDownload");
		if (h_dll_p2spDeleteDownload)
		{
			return h_dll_p2spDeleteDownload(filemd5);
		}
	}
	return false;
}

bool P2SPEngine::dll_p2spGetTaskInfomation(TaskInfomationT* info)
{
	if (s_hHandle)
	{
		DLL_P2SPGETTASKINFOMATION h_dll_p2spGetTaskInfomation = NULL;
		h_dll_p2spGetTaskInfomation = (DLL_P2SPGETTASKINFOMATION)::GetProcAddress(s_hHandle, "p2spGetTaskInfomation");
		if (h_dll_p2spGetTaskInfomation)
		{
			return h_dll_p2spGetTaskInfomation(info);
		}
	}
	return false;
}

bool StartEngine()
{
	return P2SPEngine::dll_p2spStart();
}

bool StopEngine()
{
	return P2SPEngine::dll_p2spStop();
}