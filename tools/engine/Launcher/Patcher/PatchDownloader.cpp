#include "stdafx.h"
#include "../../pfs/src/common.h"
#include "../../pfs/patchmetafile.h"
#include "i_p2sp_download.h"
#include "P2SPEngine.h"
#include "GlobalFunction.h"
#include "GlobalNotification.h"
#include "TextID.h"
#include "TextManager.h"
#include "PatchDownloader.h"
#include "ResumeBrokenDownload.h"
#include "DownloadEvent.h"
#include "Profile.h"
#include "KHLogger.h"

const wchar_t CFG[] = L".cfg";	// 保存文件下载进度的配置文件后缀名

PatchDownloader::PatchDownloader(PFSX::PatchInfo& patchInfo)
: m_patchInfo( patchInfo)
, m_httpFailed( false)
, m_p2spFailed( false)
{
}

PatchDownloader::~PatchDownloader()
{
}

bool PatchDownloader::Download(HANDLE hBreakEvent, const std::wstring& url, const std::wstring& cacheDir)
{
	std::wstring filename = cacheDir + m_patchInfo.filename;
	if(PFS::CFileIO::IsFileExisting(filename))
	{
		KHMsgLog_InfoW( GlobalFormatText(TIP_PATCH_ALREADY_EXIST, m_patchInfo.filename.c_str()).c_str() );

		PFSX::CNativeFile patchFile;
		if(patchFile.Open(filename, PFS::FM_EXCL, PFS::FA_RDONLY))
		{
			PFS::FILE_DATA_HASH hashValue;
			PFS::helper::CMD5FileHashMethod hashMethod;
			if(hashMethod.MakeFileHash(patchFile, hashValue))
			{
				if(hashValue == m_patchInfo.hash)
				{
					KHMsgLog_InfoW( GlobalFormatText( TIP_PATCH_HASH, m_patchInfo.filename.c_str()).c_str() );

					patchFile.Close();
					return true;
				}
			}

			patchFile.Close();
		}

		KHMsgLog_InfoW( GlobalFormatText( ERR_PATCH_HASH, m_patchInfo.filename.c_str()).c_str() );
	}
	
	int dt = CProfile::GetInstance().GetFileDownloadType();
	if(dt == 0)
		SetEvent(g_hHttpStartEvent);
	else
		SetEvent(g_hP2spStartEvent);

	HANDLE hEventArray[] = { hBreakEvent, g_hHttpStartEvent, g_hP2spStartEvent };
	bool bRet = false;
	bool bLoop = true;
	do 
	{
		DWORD dwRet = WaitForMultipleObjects(ARRAYSIZE(hEventArray), hEventArray, false, INFINITE);
		switch(dwRet)
		{
		// 用户强制结束下载操作
		case WAIT_OBJECT_0:
			bLoop = false;
			break;
		// 使用http方式下载
		case WAIT_OBJECT_0 + 1:
			KHMsgLog_InfoW( GlobalFormatText( TIP_TRY_DOWNLOAD_PATCH, L"http", m_patchInfo.filename.c_str()).c_str() );

			bRet = HttpDownload(hBreakEvent, cacheDir, url);
			if(bRet)
			{
				KHMsgLog_InfoW( GlobalFormatText( TIP_DOWLOAD_PATCH, L"http", m_patchInfo.filename.c_str()).c_str() );

				bLoop = false;
			}
			else
			{
				KHMsgLog_InfoW( GlobalFormatText( ERR_DOWNLOAD_PATCH_ERROR, L"http", m_patchInfo.filename.c_str()).c_str() );

				// http下载失败，尝试p2sp下载
				m_httpFailed = true;
				if(m_p2spFailed)
					bLoop = false;
				else
					SetEvent(g_hP2spStartEvent);
			}
			break;
		// 使用p2sp方式下载
		case WAIT_OBJECT_0 + 2:
			KHMsgLog_InfoW( GlobalFormatText( TIP_TRY_DOWNLOAD_PATCH, L"p2sp", m_patchInfo.filename.c_str()).c_str() );

			bRet = P2spDownload(hBreakEvent, cacheDir);
			if(bRet)
			{
				KHMsgLog_InfoW( GlobalFormatText( TIP_DOWLOAD_PATCH, L"p2sp", m_patchInfo.filename.c_str()).c_str() );

				bLoop = false;
			}
			else
			{
				KHMsgLog_InfoW( GlobalFormatText( ERR_DOWNLOAD_PATCH_ERROR, L"p2sp", m_patchInfo.filename.c_str()).c_str() );

				// p2sp下载失败，尝试http下载
				m_p2spFailed = true;
				if(m_httpFailed)
					bLoop = false;
				else
					SetEvent(g_hHttpStartEvent);
			}
			break;
		}

	} while (bLoop);

	return bRet;
}

bool PatchDownloader::HttpDownload(HANDLE hBreakEvent, const std::wstring& dir, const std::wstring& url)
{
	CResumeBrokenDownload downloader(m_patchInfo.filename, url, dir, m_patchInfo.hash);
	return downloader.Download(hBreakEvent);
}

bool PatchDownloader::P2spDownload(HANDLE hBreakEvent, const std::wstring& dir)
{
	SettingItemT setting;
	//setting.mask = P2P_SETTING_ITEM_FILE_SAVE_PATH | P2P_SETTING_ITEM_MAX_DOWNLOAD_SPEED;
	//setting.maxDownloadSpeed = 1024 * 1000;
	setting.mask = P2P_SETTING_ITEM_FILE_SAVE_PATH;
	StrCpyNW(setting.savePath, dir.c_str(), MAX_PATH);

	bool bRet = P2SPEngine::dll_p2spSetting(&setting);
	if(!bRet)
	{
		KHMsgLog_InfoW( GlobalFormatText( ERR_P2SP_SETTING).c_str() );

		return false;
	}

	TaskInfomationT task = {0};
	WideCharToMultiByte(CP_ACP, 0, m_patchInfo.md5.c_str(), sizeof(task.md5), task.md5, sizeof(task.md5), 0, 0);

	bRet = P2SPEngine::dll_p2spAddDownload(task.md5);
	if(!bRet)
	{
		KHMsgLog_InfoW( GlobalFormatText( ERR_P2SP_ADD_DOWNLOAD, m_patchInfo.filename.c_str()).c_str() );

		return false;
	}

	bRet = P2SPEngine::dll_p2spStartDownload(task.md5);
	if(!bRet)
	{
		KHMsgLog_InfoW( GlobalFormatText( ERR_P2SP_START_DOWNLOAD, m_patchInfo.filename.c_str()).c_str() );

		return false;
	}

	GlobalNotifyStart(100);

	int trycnt = 0;
	int percent = 0;
	bool bLoop = true;
	do 
	{
		if(DetectBreakEvent(hBreakEvent))
			break;

		Sleep(1000);
		bRet = P2SPEngine::dll_p2spGetTaskInfomation(&task);
		if(!bRet)
		{
			if(++trycnt > 10)
			{
				KHMsgLog_InfoW( GlobalFormatText( ERR_P2SP_TASK_INFOMATION, m_patchInfo.filename.c_str()).c_str() );

				break;
			}

			continue;
		}

		switch(task.state)
		{
		case P2P_TASK_STATE_NOREADY:		//未准备好
		case P2P_TASK_QUEUING:				//排队
		case P2P_TASK_STATE_READY:			//准备
			KHMsgLog_InfoW( GlobalFormatText( ERR_P2SP_TASK_STATE, task.state).c_str() );

			break;
		case P2P_TASK_STATE_STOP:			//停止
		case P2P_TASK_STATE_ERROR:			//错误
			KHMsgLog_InfoW( GlobalFormatText( ERR_P2SP_TASK_STATE, task.state).c_str() );

			bLoop = false;
			break;;
		case P2P_TASK_STATE_DOWNLOADING:	//下载
			percent = int( task.complateSize * 100.0/ task.size);
			GlobalNotifyStep( GlobalFormatText( TIP_INET_FILE_DOWNLOADED_PERCENT,  percent), 0, percent);
			break;
		case P2P_TASK_STATE_COMPLETE:		//完成
			GlobalNotifyEnd();

			PFSX::CNativeFile cfgFile;
			if(cfgFile.Open(dir + m_patchInfo.filename + CFG, PFS::FM_CREAT, PFS::FA_WRONLY))
			{
				cfgFile.Write(&m_patchInfo.size, sizeof(size_t));
				cfgFile.Write(&m_patchInfo.size, sizeof(PFS::fsize_t));
				cfgFile.Write(&m_patchInfo.hash, sizeof(PFS::FILE_DATA_HASH));

				cfgFile.Close();
			}

			bLoop = false;
			break;
		}

	} while (bLoop);

	return bRet;
}