//  UpdateEngine.cpp
//  FireClient

#include "UpdateEngine.h"
#ifdef WIN7_32
#include "UpdateManagerEx_Win.h"
#else//IOS ANDROID
#include "UpdateManagerEx.h"
#endif
#include "GlobalFunction.h"


#ifndef ANDROID
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
    #include "utils/IOS_Utils.h"
#endif
#endif

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "utils/FileUtil.h"
#endif

unsigned int UpdateEngine::g_uiVersionOld = 1;
std::wstring UpdateEngine::g_wsVersionOldCaption = L"0.0.1";
unsigned int UpdateEngine::g_uiVersion = 1;
std::wstring UpdateEngine::g_wsVersionCaption = L"0.0.1";
unsigned int UpdateEngine::g_uiVersionBase = 1;
std::wstring UpdateEngine::g_wsVersionBaseCaption = L"0.0.1";
unsigned int UpdateEngine::g_uiChannel = 0;
std::wstring UpdateEngine::g_wsChannelCaption = L"0";
std::map<std::wstring, std::wstring> UpdateEngine::g_ExtendMap;
unsigned int UpdateEngine::g_uiNoPack = 0;
unsigned int UpdateEngine::g_uiVersionDonotCheck = 0;
std::wstring UpdateEngine::g_WGAdressStr = L"";

void UpdateEngine::Initialize()
{
#ifdef NoPack
	g_uiNoPack = 1;
#endif
}
void UpdateEngine::Run()
{
	Initialize();
	bool bRet = true;

	UpdateManagerEx::NewInstance();
	if (UpdateManagerEx::GetInstance())
	{
		if (g_uiNoPack == 0)
		{
			bRet = UpdateManagerEx::GetInstance()->Run();
		}
		else
		{
			bRet = UpdateManagerEx::GetInstance()->RunNoPack();
		}
		unsigned int uiVerOld = UpdateManagerEx::GetInstance()->m_VersionOld;
		std::wstring wsVerOldCap = UpdateManagerEx::GetInstance()->m_VersionOldCaption;
		UpdateEngine::g_uiVersionOld = uiVerOld;
		UpdateEngine::g_wsVersionOldCaption = wsVerOldCap;
		
		unsigned int uiVer = UpdateManagerEx::GetInstance()->m_Version;
        std::wstring wsVerCap = UpdateManagerEx::GetInstance()->m_VersionCaption;
        UpdateEngine::g_uiVersion = uiVer;
        UpdateEngine::g_wsVersionCaption = wsVerCap;
        
		unsigned int uiVerBase = UpdateManagerEx::GetInstance()->m_VersionBase;
		std::wstring wsVerBaseCap = UpdateManagerEx::GetInstance()->m_VersionBaseCaption;
		UpdateEngine::g_uiVersionBase = uiVerBase;
		UpdateEngine::g_wsVersionBaseCaption = wsVerBaseCap;

		unsigned int uiChannel = UpdateManagerEx::GetInstance()->m_Channel;
		std::wstring wsChannelCap = UpdateManagerEx::GetInstance()->m_ChannelCaption;
		UpdateEngine::g_uiChannel = uiChannel;
		UpdateEngine::g_wsChannelCaption = wsChannelCap;

		UpdateManagerEx::GetInstance()->CloneExtendMap(g_ExtendMap);
		UpdateManagerEx::RemoveInstance();

		std::wstring wsTmp;
		std::map<std::wstring, std::wstring>::iterator it = UpdateEngine::g_ExtendMap.find(L"VersionDonotCheck");
		if (it != UpdateEngine::g_ExtendMap.end())
		{
			wsTmp = it->second;
		}
		if (wsTmp == L"1")
		{
			g_uiVersionDonotCheck = 1;
		}

#ifdef WIN7_32

#else
		if (bRet)
		{
			GlobalNotifyEnd();
		}
		else
		{
			GlobalNotifyFail();
		}
#endif
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
    if(UpdateManagerEx::GetInstance())
        UpdateManagerEx::GetInstance()->Continue(iResult);
}

std::wstring UpdateEngine::GetWGAdress()
{	
    return g_WGAdressStr;
}
