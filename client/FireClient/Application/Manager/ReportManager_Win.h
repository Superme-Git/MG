/***********************************************************************
filename: 	ReportManager.h
purpose:	数据上报管理器
*************************************************************************/
#pragma once
#include "Singleton.hpp"
#include "LoginManager.h"
#include "ConnectGetServerInfo.h"
#include "../../../../common/platform/utils/FileUtil.h"
#include "../../../../common/ljfm/code/include/ljfileinfo.h"

class ReportManager : public CSingleton < ReportManager >
{
public:
	ReportManager()
	{

	}
	~ReportManager()
	{

	}
	std::string GetRandomParamByTime()
	{
		std::string sResult;;
		std::wstring wsResult;
		wsResult = gGetLoginManager()->GetDeviceid() + L"_";
		struct timeval now;
		if (gettimeofday(&now, NULL) != 0)
		{
			std::string sResult = StringCover::to_string(wsResult);
			return sResult;
		}
		long CurSec = now.tv_sec + now.tv_usec / 1000000;
		if (CurSec < 0)
		{
			CurSec = CurSec * -1;
		}
		while (CurSec > 4294967296 - 1)
		{
			CurSec = CurSec / 2;
		}
		unsigned int uiSec = CurSec;
		wsResult += StringCover::NumTowstring(uiSec);
		sResult = StringCover::to_string(wsResult);
		sResult += ".zip";
		return sResult;
	}
	int UpdateFileByFTP(std::string sURL, std::string sUserName, std::string sPassWord, std::string sSrcFileNameFull, std::string sDstFileNameRemote)
	{
		return 0;
	}
	std::string CompressLog(std::string sCustomZipFN)
	{
		//std::string sZipFN;
		//std::string sZipFNFull;
		//if (sCustomZipFN != "")
		//{
		//	sZipFN = sCustomZipFN;
		//}
		//else
		//{
		//	sZipFN = GetRandomParamByTime();
		//}
		//sZipFNFull = CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), sZipFN.c_str());
		//std::string CurFile;
		//std::string CurFileFull;

		//CurFile = "mt3.log";
		//CurFileFull = CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), CurFile.c_str());
		//LJAddFileToZipFile(s2ws(sZipFNFull), s2ws(CurFile), s2ws(CurFileFull));
		//CurFile = "mt3_ct.log";
		//CurFileFull = CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), CurFile.c_str());
		//LJAddFileToZipFile(s2ws(sZipFNFull), s2ws(CurFile), s2ws(CurFileFull));
		//CurFile = "mt3_history.log";
		//CurFileFull = CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), CurFile.c_str());
		//LJAddFileToZipFile(s2ws(sZipFNFull), s2ws(CurFile), s2ws(CurFileFull));

		//CurFile = "CEGUI.log";
		//CurFileFull = CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), CurFile.c_str());
		//LJAddFileToZipFile(s2ws(sZipFNFull), s2ws(CurFile), s2ws(CurFileFull));
		//CurFile = "CEGUI_ct.log";
		//CurFileFull = CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), CurFile.c_str());
		//LJAddFileToZipFile(s2ws(sZipFNFull), s2ws(CurFile), s2ws(CurFileFull));
		//CurFile = "CEGUI_history.log";
		//CurFileFull = CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), CurFile.c_str());
		//LJAddFileToZipFile(s2ws(sZipFNFull), s2ws(CurFile), s2ws(CurFileFull));

		//return sZipFN;
		return "";
	}
	int ReportLogByFTP(std::string sCustomZipFN)
	{
		CompressLog(sCustomZipFN);
		return 0;
	}

#ifdef WIN7_32

#elif defined ANDROID

#else//IOS

#endif

};
inline ReportManager* gGetReportManager()
{
	return ReportManager::GetInstance();
}
