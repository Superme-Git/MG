#ifndef __FireClient__UpdateEngine__
#define __FireClient__UpdateEngine__

#include "../stdafx.h"

class UpdateEngine
{
public:
    static void Initialize();
    static void Run();
    static void Continue();
    static void ContinueEx(int iResult);
    static void OnUpdateEnd(bool bRet, bool isFirstDownload = false);
    static void OnUpdateEnd2(bool bRet,bool isFirstDownload = false);

    //版本信息
    static unsigned int g_uiVersionOld;
    static std::wstring g_wsVersionOldCaption;
    static unsigned int g_uiVersion;
    static std::wstring g_wsVersionCaption;
	static unsigned int g_uiVersionBase;
	static std::wstring g_wsVersionBaseCaption;
	static unsigned int g_uiChannel;
	static std::wstring g_wsChannelCaption;
	static std::map<std::wstring, std::wstring> g_ExtendMap;

	static unsigned int g_uiNoPack;
	static unsigned int g_uiVersionDonotCheck;
};

#endif /* defined(__FireClient__UpdateEngine__) */
