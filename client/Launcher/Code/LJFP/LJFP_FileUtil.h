#ifndef LJFP_FileUtil_H
#define LJFP_FileUtil_H

#include "LJFP_Var.h"
#include <direct.h>
#include <io.h>
#include <stdio.h>
#include "LJFP_StringUtil.h"
#include "LJFP_XML.h"

#define MY_MKDIR(a) _mkdir((a))
#define MY_ACCESS _access
#define MY_RMDIR(a) _rmdir((a))
#define MY_RMFILE(a) remove((a))

class LJFP_FileUtil
{
public:
	static bool IsDir(std::wstring strName)
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
	static bool IsPath(std::wstring strName)
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
	static std::wstring DirToPath(std::wstring strName)
	{
		if (IsDir(strName))
		{
			return strName + L"/";
		}
		return strName;
	}
	static std::wstring PathToDir(std::wstring strName)
	{
		if (IsPath(strName))
		{
			return strName.substr(0, strName.size() - 1);
		}
		return strName;
	}
	static bool ExistFile(const std::wstring& strFileName)
	{
		std::string strPath = StringUtil::WS2S(strFileName);
		int nRet = MY_ACCESS(strPath.c_str(), 0);
		return nRet == 0;
	}
	static bool ExistDir(const std::wstring& strDir)
	{
		return ExistFile(strDir);
	}
	static std::wstring CreateDir(const std::wstring& strDir)
	{
		std::string strPath = StringUtil::WS2S(strDir);
		int nRet = MY_ACCESS(strPath.c_str(), 0);
		if (nRet != 0)
		{
			nRet = MY_MKDIR(strPath.c_str());
			if (nRet != 0)
			{
				return L"";
			}
		}
		return StringUtil::S2WS(strPath);
	}
	static std::wstring CreateDirEx(const std::wstring& strDir)
	{
		std::vector<std::wstring> ResultStr;
		std::size_t ResultCount = LJXMLStringHelper::SplitStrW(strDir, L"/", ResultStr);
		std::wstring CurPath;
		for (std::size_t i = 0; i < ResultCount; i++)
		{
			if (CurPath !=  L"")
			{
				CurPath += L"/";
			}
			CurPath += ResultStr[i];
			CreateDir(CurPath);
		}
		return strDir;
	}
	static bool RemoveFile(const std::wstring& strFileName)
	{
		std::string strPath = StringUtil::WS2S(strFileName);
		if (ExistFile(strFileName))
		{
			int nRet = MY_RMFILE(strPath.c_str());
			if (nRet == 0)
			{
				return true;
			}
		}
		return false;
	}
	static bool RemoveDir(const std::wstring& strDir)
	{
		std::string strPath = StringUtil::WS2S(strDir);
		if (ExistDir(strDir))
		{
			int nRet = MY_RMDIR(strPath.c_str());
			if (nRet == 0)
			{
				return true;
			}
		}
		return false;
	}
	static std::wstring GetFilePath(std::wstring wstrFileName, std::wstring wstrOffsetPath)
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
	static std::wstring GetFileNameFull(std::wstring wstrFileName)
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
	static std::wstring GetFileName(std::wstring wstrFileName)
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
	static std::wstring GetFileNameExt(std::wstring wstrFileName)
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
	static int CopyFileByFS(std::wstring Src, std::wstring Dst)
	{
		std::ifstream FSSrc(StringUtil::WS2S(Src).c_str(), std::ios_base::binary);
		if (!FSSrc.is_open())
		{
			return -1;
		}
		FSSrc.seekg(0, std::ios_base::beg);
		std::ofstream FSDst(StringUtil::WS2S(Dst).c_str(), std::ios_base::binary);
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
};

LJFP_FileUtil GetFileUtil()
{
	LJFP_FileUtil Result;
	return Result;
}


#endif //LJFP_FileUtil_H