#ifndef UpdateManagerEx_Helper_H
#define UpdateManagerEx_Helper_H

#include "GlobalFunction.h"
#include "../platform/utils/FileUtil.h"
#include "../platform/utils/StringUtil.h"
#include "../platform/utils/Utils.h"
#include "../../dependencies/LJXML/Include/LJXML.hpp"
#include "../../dependencies/LJFilePack/LJFP_Version.h"
#include "../../dependencies/LJFilePack/LJFP_FileInfo.h"
#include "../../dependencies/LJFilePack/LJFP_ZipFile.h"

#include "../ljfm/code/include/ljfileinfo.h"

#ifndef ANDROID
#include "utils/IOS_Utils.h"
#endif
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <thread>
#include <chrono>
#endif
#ifdef ANDROID
#include "../../common/platform/android/SDJniHelper.h"
#include <jni.h>
#endif

#ifdef WIN7_32
#include "win32/ccstdc.h"
#elif defined ANDROID
#include "FileDownloader.h"
#include "android/ccstdc.h"
#else//IOS
#include "FileDownloader.h"
#include "ios/ccstdc.h"
#endif


namespace UpdateUtil
{
	//H
	//Sys
	std::wstring GetRandomDownloadParam();

	//File
	std::wstring GetFilePath(std::wstring wstrFileName, std::wstring wstrOffsetPath);
	std::wstring GetFileNameFull(std::wstring wstrFileName);
	std::wstring GetFileName(std::wstring wstrFileName);
	std::wstring GetFileNameExt(std::wstring wstrFileName);
	bool GetFileSize(std::wstring wstrFileName, unsigned int& FileSize);
	bool GetFileSizeCRC32(std::wstring wstrFileName, unsigned int& FileSize, unsigned int& FileCRC32);
	bool ExistFile(std::wstring wstrFileName);
	bool ExistFileEx(std::wstring wstrFileName, unsigned int FileSize, unsigned int FileCRC32);
	bool DelFile(std::wstring wstrFileName);
	int CopyFile(std::wstring Src, std::wstring Dst);
	int CopyFileEx(std::wstring SrcRoot, std::wstring SrcPath, std::wstring SrcFName, std::wstring DstRoot, std::wstring DstPath, std::wstring DstFName);
	
	bool DownloadFile(std::wstring SrcURL, std::wstring SrcPath, std::wstring SrcFName, std::wstring DstRoot, std::wstring DstPath, std::wstring DstFName, bool isNotify);
	bool DownloadFileEx(URLInfoArr& SrcUIArr, std::wstring SrcSystem, std::wstring SrcNetwork, std::wstring SrcPath, std::wstring SrcFName,
		std::wstring DstRoot, std::wstring DstPath, std::wstring DstFName, bool isNotify);

	//-------------------------------------

	//Dir Path
	bool IsDir(std::wstring strName);
	bool IsPath(std::wstring strName);
	std::wstring DirToPath(std::wstring strName);
	std::wstring PathToDir(std::wstring strName);
	bool ExistDir(std::wstring wstrDirName);
	std::wstring CreateDir(std::wstring wstrDirName);
	std::wstring CreateDirEx(std::wstring wstrRootName, std::wstring wstrDirName);
	bool DelDir(std::wstring wstrDirName);
	bool DelDirEx(std::wstring wstrDirName, std::vector<std::wstring> vecDir, std::vector<std::wstring> vecFile);
	bool CopyDir(std::wstring SrcRoot, std::wstring DstRoot, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile);
	bool CopyDir(std::wstring SrcRoot, std::wstring DstRoot);
	bool ListDir(std::wstring Src, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile);

	//=====================================

	//CPP
	//Sys
	std::wstring GetRandomDownloadParam()
	{
		std::wstring wsResult;
		wsResult = L"?r=x";
		struct timeval now;
		if (gettimeofday(&now, NULL) != 0)
		{
			return wsResult;
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
		wsResult += GetStringUtil().UI2WS(uiSec);
		return wsResult;
	}
	//File
	std::wstring GetFilePath(std::wstring wstrFileName, std::wstring wstrOffsetPath)
	{
		if (wstrOffsetPath != L"")
		{
			if (wstrFileName.substr(0, wstrOffsetPath.size()) == wstrOffsetPath)
			{
				wstrFileName = wstrFileName.substr(wstrOffsetPath.size());
			}
		}
		size_t FindStart;
		size_t FindPos;
		FindStart = wstrFileName.size() - 1;
		FindPos = wstrFileName.find_last_of(L"/", FindStart);
		if (FindPos == FindStart || FindPos == std::wstring::npos)
		{
			return L"";
		}
		else
		{
			std::wstring wstrResult;
			wstrResult = wstrFileName.substr(0, FindPos + 1);
			return wstrResult;
		}
	}
	std::wstring GetFileNameFull(std::wstring wstrFileName)
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
	std::wstring GetFileName(std::wstring wstrFileName)
	{
		std::wstring wstrResult = GetFileNameFull(wstrFileName);
		size_t FindStart;
		size_t FindPos;
		FindStart = 0;
		FindPos = wstrResult.find_first_of(L".", FindStart);
		if (FindPos == std::wstring::npos)
		{
			return L"";
		}
		else
		{
			wstrResult = wstrResult.substr(0, FindPos);
			return wstrResult;
		}
	}
	std::wstring GetFileNameExt(std::wstring wstrFileName)
	{
		size_t FindStart;
		size_t FindPos;
		FindStart = wstrFileName.size() - 1;
		FindPos = wstrFileName.find_last_of(L".", FindStart);
		if (FindPos == FindStart || FindPos == std::wstring::npos)
		{
			return L"";
		}
		else
		{
			std::wstring wstrResult;
			wstrResult = wstrFileName.substr(FindPos + 1);
			return wstrResult;
		}
	}
	bool GetFileSize(std::wstring wstrFileName, unsigned int& FileSize)
	{
		std::string strFileName = StringCover::to_string(wstrFileName);
		std::ifstream FS(strFileName.c_str(), std::ios_base::binary);
		if (!FS.is_open())
		{
			return false;
		}
		FS.seekg(0, std::ios_base::end);
		FileSize = FS.tellg();
		FS.close();
		return true;
	}
	bool GetFileSizeCRC32(std::wstring wstrFileName, unsigned int& FileSize, unsigned int& FileCRC32)
	{
		std::string strFileName = StringCover::to_string(wstrFileName);
		std::ifstream FS(strFileName.c_str(), std::ios_base::binary);
		if (!FS.is_open())
		{
			return false;
		}
		FS.seekg(0, std::ios_base::end);
		FileSize = FS.tellg();
		FS.seekg(0, std::ios_base::beg);
		unsigned char* CurData;
		CurData = new unsigned char[FileSize];
		FS.seekg(0, std::ios_base::beg);
		FS.read((char*)&CurData[0], FileSize);
		FileCRC32 = LJCRC32Func(0, CurData, FileSize);
		FS.close();
		return true;
	}
	bool ExistFile(std::wstring wstrFileName)
	{
		return MHSD_UTILS::ExistFile(wstrFileName);
	}
	bool ExistFileEx(std::wstring wstrFileName, unsigned int FileSize, unsigned int FileCRC32)
	{
		unsigned int CurFileSize;
		unsigned int CurFileCRC32;
		if (FileSize > 0)
		{
			if (!GetFileSize(wstrFileName, CurFileSize))
			{
				return false;
			}
			if (FileSize != CurFileSize)
			{
				return false;
			}
		}
		if (FileCRC32 > 0)
		{
			if (!GetFileSizeCRC32(wstrFileName, CurFileSize, CurFileCRC32))
			{
				return false;
			}
			if (FileCRC32 != CurFileCRC32)
			{
				return false;
			}
		}
		return true;
	}
	bool DelFile(std::wstring wstrFileName)
	{
		return MHSD_UTILS::RemoveFile(wstrFileName);
	}
	int CopyFile(std::wstring Src, std::wstring Dst)
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
	int CopyFileEx(std::wstring SrcRoot, std::wstring SrcPath, std::wstring SrcFName, std::wstring DstRoot, std::wstring DstPath, std::wstring DstFName)
	{
		CreateDirEx(DstRoot, DstPath);
		return CopyFile(SrcRoot + SrcPath + SrcFName, DstRoot + DstPath + DstFName);
	}
	
	//-------------------------------------

	//Dir Path
	bool IsDir(std::wstring strName)
	{
		if (strName == L"")
		{
			return false;
		}
		if (strName[strName.size() - 1] == L'/' || strName[strName.size() - 1] == L':')
		{
			return false;
		}
		return true;
	}
	bool IsPath(std::wstring strName)
	{
		if (strName == L"")
		{
			return false;
		}
		if (strName[strName.size() - 1] == L'/')
		{
			return true;
		}
		return false;
	}
	std::wstring DirToPath(std::wstring strName)
	{
		if (IsDir(strName))
		{
			return strName + L"/";
		}
		return strName;
	}
	std::wstring PathToDir(std::wstring strName)
	{
		if (IsPath(strName))
		{
			return strName.substr(0, strName.size() - 1);
		}
		return strName;
	}
	bool ExistDir(std::wstring wstrDirName)
	{
		return MHSD_UTILS::ExistDir(wstrDirName);
	}
	std::wstring CreateDir(std::wstring wstrDirName)
	{
		return MHSD_UTILS::CreateDir(wstrDirName);
	}
	std::wstring CreateDirEx(std::wstring wstrRootName, std::wstring wstrDirName)
	{
		CreateDir(wstrRootName);
		if (wstrDirName == L"")
		{
			return wstrDirName;
		}
		wstrDirName = PathToDir(wstrDirName);
		std::vector<std::wstring> ResultStr;
		std::size_t ResultCount = LJXMLStringHelper::SplitStrW(wstrDirName, L"/", ResultStr);
		std::wstring CurPath;
		for (std::size_t i = 0; i < ResultCount; i++)
		{
			if (CurPath != L"")
			{
				CurPath += L"/";
			}
			CurPath += ResultStr[i];
			CreateDir(wstrRootName + CurPath);
		}
		return wstrDirName;
	}
	bool DelDir(std::wstring wstrDirName)
	{
		return MHSD_UTILS::RemoveDir(wstrDirName);
	}
	bool DelDirEx(std::wstring wstrDirName, std::vector<std::wstring> vecDir, std::vector<std::wstring> vecFile)
	{
		{
			for (unsigned int i = 0; i < vecFile.size(); i++)
			{
				std::wstring SrcFileName = wstrDirName + L"/" + vecFile[i];
				DelFile(SrcFileName);
			}
		}
		{
			for (unsigned int i = 0; i < vecDir.size(); i++)
			{
				
				std::wstring CurDir = vecDir[i];
				DelDir(CurDir);
			}
		}
		return true;
	}
	bool CopyDir(std::wstring SrcRoot, std::wstring DstRoot, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile)
	{
		CreateDirEx(DstRoot,L"");
		//for (unsigned int i = 0; i < vecDir.size(); i++)
		//{
		//	std::wstring CurDir = vecDir[i];
		//	CreateDirEx(DstRoot, CurDir);
		//}
		for (unsigned int i = 0; i < vecFile.size(); i++)
		{
			std::wstring SrcFileName = SrcRoot + vecFile[i];
			std::wstring DstFileName = DstRoot + vecFile[i];
			CopyFileEx(SrcRoot, vecDir[i], vecFile[i], DstRoot, vecDir[i], DstFileName);
		}
		return true;
	}
	bool CopyDir(std::wstring Src, std::wstring Dst)
	{
		std::vector<std::wstring> vecDir;
		std::vector<std::wstring> vecFile;
		ListDir(Src, vecDir, vecFile);
		return CopyDir(Src, Dst, vecDir, vecFile);
	}

	bool DownloadFileEx(URLInfoArr& SrcUIArr, std::wstring SrcSystem, std::wstring SrcNetwork, std::wstring SrcPath, std::wstring SrcFName,
		std::wstring DstRoot, std::wstring DstPath, std::wstring DstFName, bool isNotify)
	{
		for (unsigned int i = 0; i < SrcUIArr.size(); i++)
		{
			if (SrcUIArr[i].m_System == SrcSystem && SrcUIArr[i].m_Network == SrcNetwork)
			{
				if (DownloadFile(SrcUIArr[i].m_URL, SrcPath, SrcFName, DstRoot, DstPath, DstFName, isNotify))
				{
					return true;
				}
			}
		}
		return false;
	}


#ifdef WIN7_32

	bool DownloadFile(std::wstring SrcURL, std::wstring SrcPath, std::wstring SrcFName, std::wstring DstRoot, std::wstring DstPath, std::wstring DstFName, bool isNotify)
	{
    //AsyncFileDownloader::Clear();
    //AsyncFileDownloader::InitUrlAndDestdir(m_strUrl,m_strDir);
    //for( int count = 0; count<=MAX_COUNT_IN_QUEUE && m_mDownloadQueue.size() > 0; ++count)
    //{
    //    const std::wstring& filename = m_mDownloadQueue.front();
    //    m_mDownloadQueue.pop();
    //    AsyncFileDownloader::DownloadOneFileAsyn( filename);
    //}
    //
		bool bResult = true;
    //AsyncFileDownloader::StartDownload();
		//bResult = FileDownloader::SynDownloadOneFile(DirToPath(SrcURL) + SrcPath + SrcFName, DstRoot + DstPath + DstFName);

		return bResult;
	}


	int FindFiles(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strPathName, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile);

	void OnFindDir(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strDirName, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile)
	{
		std::wstring strCurrentPath = strDirName + L"/";
		FindFiles(strRootPathName, strParentPathName, strCurrentPath, vecDir, vecFile);
	}
	void OnFindFile(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strFileName, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile)
	{
		vecDir.push_back(PathToDir(strParentPathName));
		vecFile.push_back(strFileName);
	}
	void OnFindData(std::wstring strRootPathName, std::wstring strParentPathName, WIN32_FIND_DATAW& FindData, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile)
	{
		if ('.' == FindData.cFileName[0])
		{
			return;
		}
		_wcslwr_s(FindData.cFileName, MAX_PATH);
		std::wstring strCurrentFileName(FindData.cFileName);
		if (FILE_ATTRIBUTE_DIRECTORY == (FILE_ATTRIBUTE_DIRECTORY & FindData.dwFileAttributes))
		{
			OnFindDir(strRootPathName, strParentPathName, strCurrentFileName, vecDir, vecFile);
		}
		else
		{
			if (FindData.nFileSizeLow == 0)
			{
				return;
			}
			OnFindFile(strRootPathName, strParentPathName, strCurrentFileName, vecDir, vecFile);
		}
	}
	int FindFiles(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strPathName, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile)
	{
		std::wstring strFindPath = strRootPathName + strParentPathName + strPathName + L"*.*";
		WIN32_FIND_DATAW FindData;
		//HANDLE hFind = FindFirstFileExA(strFindPath.c_str(), FindExInfoStandard, &FindData, FindExSearchNameMatch, NULL, FIND_FIRST_EX_CASE_SENSITIVE);
		HANDLE hFind = FindFirstFileW(strFindPath.c_str(), &FindData);
		if (INVALID_HANDLE_VALUE == hFind)
		{
			return -1;
		}
		OnFindData(strRootPathName, strParentPathName + strPathName, FindData, vecDir, vecFile);
		while (FindNextFileW(hFind, &FindData))
		{
			OnFindData(strRootPathName, strParentPathName + strPathName, FindData, vecDir, vecFile);
		}
		FindClose(hFind);

		return 0;
	}
	bool ListDir(std::wstring Src, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile)
	{
		FindFiles(Src + L"/", L"", L"", vecDir, vecFile);
		std::sort(vecDir.begin(), vecDir.end());
		std::sort(vecFile.begin(), vecFile.end());
		return true;
	}
#elif defined ANDROID
	bool DownloadFile(std::wstring SrcURL, std::wstring SrcPath, std::wstring SrcFName, std::wstring DstRoot, std::wstring DstPath, std::wstring DstFName, bool isNotify)
	{
		bool bResult = true;
		bResult = FileDownloader::SynDownloadOneFile(DirToPath(SrcURL) + SrcPath + SrcFName, DstRoot + DstPath + DstFName, isNotify);
		return bResult;
	}
	bool ListDir(std::wstring Src, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile)
	{

		std::sort(vecDir.begin(), vecDir.end());
		std::sort(vecFile.begin(), vecFile.end());
		return true;
	}

#else//IOS
	bool DownloadFile(std::wstring SrcURL, std::wstring SrcPath, std::wstring SrcFName, std::wstring DstRoot, std::wstring DstPath, std::wstring DstFName, bool isNotify)
	{
		bool bResult = true;
		bResult = FileDownloader::SynDownloadOneFile(DirToPath(SrcURL) + SrcPath + SrcFName, DstRoot + DstPath + DstFName, false);
		return bResult;
	}
	bool ListDir(std::wstring Src, std::vector<std::wstring>& vecDir, std::vector<std::wstring>& vecFile)
	{

		std::sort(vecDir.begin(), vecDir.end());
		std::sort(vecFile.begin(), vecFile.end());
		return true;
	}

#endif


}

#endif //UpdateManagerEx_Helper_H
