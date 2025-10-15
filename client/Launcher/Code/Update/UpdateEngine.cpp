#include "UpdateEngine.h"
#include "UpdateBin.h"
#include "UpdateManagerEx.h"
#include "../Global/GlobalFunction.h"

unsigned int UpdateEngine::g_uiVersionOld = 1;
std::wstring UpdateEngine::g_wsVersionOldCaption = L"0.0.1";
unsigned int UpdateEngine::g_uiVersion = 1;
std::wstring UpdateEngine::g_wsVersionCaption = L"0.0.1";
unsigned int UpdateEngine::g_uiVersionBase = 1;
std::wstring UpdateEngine::g_wsVersionBaseCaption = L"0.0.1";
unsigned int UpdateEngine::g_uiChannel = 0;
std::wstring UpdateEngine::g_wsChannelCaption = L"0";
std::map<std::wstring, std::wstring> UpdateEngine::g_ExtendMap;
unsigned int UpdateEngine::g_uiVersionDonotCheck = 0;

void UpdateEngine::Initialize()
{
}
void UpdateEngine::Run()
{
	Initialize();
	bool bRet = true;

	if (GetUpdateManagerEx())
	{
		bRet = GetUpdateManagerEx()->Run();
		unsigned int uiVerOld = GetUpdateManagerEx()->m_VersionOld;
		std::wstring wsVerOldCap = GetUpdateManagerEx()->m_VersionOldCaption;
		UpdateEngine::g_uiVersionOld = uiVerOld;
		UpdateEngine::g_wsVersionOldCaption = wsVerOldCap;
		
		unsigned int uiVer = GetUpdateManagerEx()->m_Version;
        std::wstring wsVerCap = GetUpdateManagerEx()->m_VersionCaption;
        UpdateEngine::g_uiVersion = uiVer;
        UpdateEngine::g_wsVersionCaption = wsVerCap;
        
		unsigned int uiVerBase = GetUpdateManagerEx()->m_VersionBase;
		std::wstring wsVerBaseCap = GetUpdateManagerEx()->m_VersionBaseCaption;
		UpdateEngine::g_uiVersionBase = uiVerBase;
		UpdateEngine::g_wsVersionBaseCaption = wsVerBaseCap;

		unsigned int uiChannel = GetUpdateManagerEx()->m_Channel;
		std::wstring wsChannelCap = GetUpdateManagerEx()->m_ChannelCaption;
		UpdateEngine::g_uiChannel = uiChannel;
		UpdateEngine::g_wsChannelCaption = wsChannelCap;

		GetUpdateManagerEx()->CloneExtendMap(g_ExtendMap);

		ReleaseUpdateManagerEx();

		if (bRet)
		{
			GlobalNotifyEnd();
		}
		else
		{
			GlobalNotifyFail();
		}
	}

	return;
}
void UpdateEngine::OnUpdateEnd(bool bRet, bool isFirstDownload)
{

}
void UpdateEngine::OnUpdateEnd2(bool bRet, bool isFirstDownload)
{

}
void UpdateEngine::Continue()
{

}
void UpdateEngine::ContinueEx(int iResult)
{
    if(GetUpdateManagerEx())
	{
		GetUpdateManagerEx()->Continue(iResult);
	}
}