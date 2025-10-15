#include "BattleReplayManager.h"
#include "../platform/utils/Utils.h"
#include "BattleManager.h"
#include "DownloadManager.h"
#include "MainCharacter.h"
#include "GameUIManager.h"
#include "platform/platform_types.h"

#include "fire/pb/battle/SSendCameraUrl.hpp"
#include "fire/pb/battle/CReqRePlay.hpp"
#include "fire/pb/battle/SReqRePlay.hpp"

#include "ljfmopen.h"
using namespace LJFM;

#include "support/zip_support/ZipUtils.h"

void fire::pb::battle::SSendCameraUrl::Process(Manager * manager, Manager::Session::ID sid)
{
	if (ishave != 1)
	{
		if (GetTipsManager())
		{
			GetTipsManager()->AddMsgTips(180020);
		}
		return;
	}
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("bingfengwangzuoTips.DestroyDialog");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("bingfengwangzuoDlg.DestroyDialog");
	GetBattleReplayManager()->PlayReplay(battlecameraurl, sizebeforezip, sizeafterzip);
}
void fire::pb::battle::SReqRePlay::Process(Manager * manager, Manager::Session::ID sid)
{
	GetBattleReplayManager()->PlayReplayReal(battlecameraurl);
}

std::wstring BattleReplayManager::GetReplayServerAddres()
{
	return L"127.0.0.1/";
}
std::wstring BattleReplayManager::GetReplayLocalPath()
{
#ifdef WIN7_32
	#ifdef NoPack
		return  SHARE_String2Wstring(CFileUtil::GetRootDir() + "../../res/replay/");
	#else
		return  SHARE_String2Wstring(CFileUtil::GetRootDir() + "../../res1/replay/");
	#endif
#else
	return SHARE_String2Wstring(CFileUtil::GetCacheDir() + "/res/replay/");
#endif
}
std::wstring BattleReplayManager::GetFileNameFull(std::wstring wstrFileName)
{
	size_t FindStart;
	size_t FindPos;
	FindStart = wstrFileName.size() - 1;
	FindPos = wstrFileName.find_last_of(L"/", FindStart);
	if (FindPos == FindStart || FindPos == std::wstring::npos)
	{
		return wstrFileName;
	}
	else
	{
		std::wstring wstrResult;
		wstrResult = wstrFileName.substr(FindPos + 1);
		return wstrResult;
	}
	return wstrFileName;
}
int BattleReplayManager::CopyFileByFS(std::wstring Src, std::wstring Dst)
{
	std::ifstream FSSrc(ws2s(Src).c_str(), std::ios_base::binary);
	if (!FSSrc.is_open())
	{
		return -1;
	}
	FSSrc.seekg(0, std::ios_base::beg);
	std::ofstream FSDst(ws2s(Dst).c_str(), std::ios_base::binary);
	if (!FSDst.is_open())
	{
		FSSrc.close();
		return -2;
	}
	FSDst.seekp(0, std::ios_base::beg);
	FSDst << FSSrc.rdbuf();
	FSDst.close();
	FSSrc.close();
	return 0;
}
int BattleReplayManager::ClearReplayFile()
{

	return 0;
}
void BattleReplayManager::Clear()
{
	m_iState = 0;
}
BattleReplayManager::BattleReplayManager()
{
	m_iState = 0;
	m_iSizeOriginal = 0;
	m_iSizeCompress = 0;
	MHSD_UTILS::CreateDir(GetReplayLocalPath());
}
BattleReplayManager::~BattleReplayManager()
{
}
void BattleReplayManager::SetState(int iState)
{
	m_iState = iState;
	//LJFMOpen::GetOutLogInstance()->Print(L"BattleReplayManager::SetState(iState:%d)\n", iState);
}
int BattleReplayManager::GetState()
{
	return m_iState;
}
void BattleReplayManager::PlayReplay(std::wstring wsSrcURL, int iSizeOriginal, int iSizeCompress)
{
	if (m_iState != 0)
	{
		return;
	}
	if (IsCanPlayReplay() == false)
	{
		return;
	}
	LJFMOpen::GetOutLogInstance()->Print(L"BattleReplayManager::PlayReplay->SetState(iState:%d)\n", 1);
	SetState(1);
	m_wsSrcURLFull = wsSrcURL;
	m_wsSrcURL = GetFileNameFull(m_wsSrcURLFull);
	m_wsDstFileNameFullCompress = GetReplayLocalPath() + L"replay.rplz";//m_wsSrcURL;
	m_wsDstFileNameFullUnCompress = GetReplayLocalPath() + L"replay.rpl";;
	m_iSizeOriginal = iSizeOriginal;
	m_iSizeCompress = iSizeCompress;
	//if (MHSD_UTILS::ExistFile(m_wsDstFileNameFullUnCompress) == true)
	//{
	//	//PlayReplayReal(m_wsDstFileNameFullUnCompress);
	//	fire::pb::battle::CReqRePlay ReqRePlay;
	//	ReqRePlay.battlecameraurl = m_wsDstFileNameFullUnCompress;
	//	gGetNetConnection()->send(ReqRePlay);
	//	SetState(0);
	//}
	//else
	{
		GetDownloadManager()->AddTask<BattleReplayManager>(m_wsSrcURL, iSizeCompress, NULL, m_wsSrcURLFull, m_wsDstFileNameFullCompress, &BattleReplayManager::Callback_Download, this);
		LJFMOpen::GetOutLogInstance()->Print(L"BattleReplayManager::PlayReplay->AddTask\n");
	}
}
bool BattleReplayManager::Callback_Download(DownloadInfo& pDownloadInfo)
{
	LJFMOpen::GetOutLogInstance()->Print(L"BattleReplayManager::Callback_Download\n");
	if (pDownloadInfo.m_ResultCode != 200)
	{
		SetState(0);
		return false;
	}
	if (pDownloadInfo.m_wsDstFileName != m_wsDstFileNameFullCompress)
	{
		SetState(0);
		return false;
	}
	if (pDownloadInfo.m_wsSrcURL != m_wsSrcURLFull)
	{
		SetState(0);
		return false;
	}
	if (UnCompressReplayFile() == false)
	{
		SetState(0);
		return false;
	}
	fire::pb::battle::CReqRePlay ReqRePlay;
	ReqRePlay.battlecameraurl = m_wsDstFileNameFullUnCompress;
	gGetNetConnection()->send(ReqRePlay);
	SetState(0);
	return true;
}
void BattleReplayManager::PlayReplayReal(std::wstring wsDstFileNameFull)
{
	if (IsCanPlayReplay())
	{
		if (wsDstFileNameFull == m_wsDstFileNameFullUnCompress)
		{
			GetBattleManager()->BeginPlayBattleReplay(m_wsDstFileNameFullUnCompress);
			SetState(0);
		}
	}
}
void BattleReplayManager::OnOverReplay()
{
	MHSD_UTILS::RemoveFile(m_wsDstFileNameFullCompress);
	MHSD_UTILS::RemoveFile(m_wsDstFileNameFullUnCompress);
}
bool BattleReplayManager::IsCanPlayReplay()
{
	if (GetBattleManager()->IsInBattle())
	{
		return false;
	}

	if (GetMainCharacter()->IsOnTeam())
	{
		if (GetMainCharacter()->IsTeamLeader() == false)
		{
			if (GetMainCharacter()->GetTeamState() != eTeamMemberAbsent)
			{
				return false;
			}
			else
			{
			}
		}
		else
		{
		}
	}
	else
	{
	}
	
	return true;
}
bool BattleReplayManager::UnCompressReplayFile()
{
	if (MHSD_UTILS::ExistFile(m_wsDstFileNameFullCompress) == false)
	{
		return false;
	}

	unsigned char* ResultData;
	ResultData = new unsigned char[m_iSizeOriginal];
	memset(ResultData, 0, m_iSizeOriginal);
	int RealSize = cocos2d::ZipUtils::ccInflateGZipFile(ws2s(m_wsDstFileNameFullCompress).c_str(), &ResultData);
	std::ofstream FS(ws2s(m_wsDstFileNameFullUnCompress).c_str(), std::ios_base::binary);
	FS.write((const char*)(&ResultData[0]), RealSize);
	FS.close();

	//CopyFileByFS(m_wsDstFileNameFullCompress, m_wsDstFileNameFullUnCompress);//¡Ÿ ±¥˙¬Î

	return true;
}
void BattleReplayManager::PlayAndUnGZipReplay(std::wstring wsGZipFileNameFull)
{
	unsigned char* ResultData;
	m_iSizeOriginal = 1024 * 1024;
	ResultData = new unsigned char[m_iSizeOriginal];
	memset(ResultData, 0, m_iSizeOriginal);
	int RealSize = cocos2d::ZipUtils::ccInflateGZipFile(ws2s(wsGZipFileNameFull).c_str(), &ResultData);
	m_iSizeOriginal = RealSize;
	m_wsDstFileNameFullUnCompress = wsGZipFileNameFull + L".ungz";
	std::ofstream FS(ws2s(m_wsDstFileNameFullUnCompress).c_str(), std::ios_base::binary);
	FS.write((const char*)(&ResultData[0]), RealSize);
	FS.close();
	PlayReplayReal(m_wsDstFileNameFullUnCompress);
}