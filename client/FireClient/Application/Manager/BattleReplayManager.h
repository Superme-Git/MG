#ifndef BattleReplayManager_h
#define BattleReplayManager_h

#include "CCPlatformMacros.h"
/*#if CC_TARGET_PLATFORM==CC_PLATFORM_IOS
#include <AudioToolbox/CAFFile.h>
#include "sys/types.h"
#include "sys/sysctl.h"
#define MAKEWORD(a, b)      ((WORD)(((BYTE)(((DWORD_PTR)(a)) & 0xff)) | ((WORD)((BYTE)(((DWORD_PTR)(b)) & 0xff))) << 8))
#endif 
*/
#include "network/HttpClient.h"
#include "network/HttpRequest.h"
#include "network/HttpResponse.h"

#include "GameApplication.h"
#include "Utils/base64.h"
#include "Singleton.hpp"
#include "utils/StringCover.h"
#include "utils/FileUtil.h"
#include "ConfigManager.h"



#include <fstream>

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

class DownloadInfo;

class BattleReplayManager : public CSingleton<BattleReplayManager>
{
	int m_iState;
	int m_iSizeOriginal;
	int m_iSizeCompress;
	std::wstring m_wsSrcURL;
	std::wstring m_wsSrcURLFull;
	std::wstring m_wsDstFileNameFullCompress;
	std::wstring m_wsDstFileNameFullUnCompress;

	void SetState(int iState);
	int GetState();

public:
	BattleReplayManager();
	~BattleReplayManager();
	static std::wstring GetReplayServerAddres();
	static std::wstring GetReplayLocalPath();
	static std::wstring GetFileNameFull(std::wstring wstrFileName);
	static int CopyFileByFS(std::wstring Src, std::wstring Dst);
	static int ClearReplayFile();

	void Clear();
	void PlayReplay(std::wstring wsSrcURL, int iSizeOriginal, int iSizeCompress);
	bool Callback_Download(DownloadInfo& pDownloadInfo);
	void PlayReplayReal(std::wstring wsDstFileNameFull);
	
	void PlayAndUnGZipReplay(std::wstring wsGZipFileNameFull);

	void OnOverReplay();

private:
	bool IsCanPlayReplay();
	bool UnCompressReplayFile();
	
};

inline BattleReplayManager* GetBattleReplayManager()
{
	BattleReplayManager::NewInstance();
	return BattleReplayManager::GetInstance();
}

#endif //BattleReplayManager_h