/***********************************************************************
filename: 	IOS_Utils.h
purpose:	
*************************************************************************/

#ifndef __IOS_UTILS_H
#define __IOS_UTILS_H

#include <string>

namespace IOS_MHSD_UTILS
{
#if (defined ANDROID) 
    extern void OpenURL(const std::string& urlstring);
#elif (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	extern void OpenURL(const std::string& urlstring);
	extern void GameResLoadError();
#else
	extern void OpenURL(const std::string& urlstring);
    extern std::string getIPAddressForHost(const std::string& urlstring);
	extern void GameResLoadError();
#endif

	extern float GetCurBrightness();
	extern void  SetCurBrightness(float brightness);
}

#endif
