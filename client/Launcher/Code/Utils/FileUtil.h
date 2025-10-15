#ifndef FileUtil_H
#define FileUtil_H

#include "../stdafx.h"
#include "StringUtil.h"
#include "../LJFP/LJFP_StringUtil.h"

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
static bool ExistDir(std::wstring wstrDirName)
{
	std::string strDirName = StringUtil::WS2S(wstrDirName);
	int nRet = _access(strDirName.c_str(), 0);
	return nRet == 0;
}
static std::wstring CreateDir(std::wstring wstrDirName)
{
	std::string strPath = StringUtil::WS2S(wstrDirName);
	int nRet = _access(strPath.c_str(), 0);
	if (nRet != 0)
	{
		nRet = _mkdir(strPath.c_str());
		if (nRet != 0)
		{
			return L"";
		}
	}
	return StringUtil::S2WS(strPath);
}
static std::wstring CreateDirEx(std::wstring wstrRootName, std::wstring wstrDirName)
{
	CreateDir(wstrRootName);
	if (wstrDirName == L"")
	{
		return wstrDirName;
	}
	wstrDirName = PathToDir(wstrDirName);
	std::vector<std::wstring> ResultStr;
	std::size_t ResultCount = GetStringUtil().SplitStrW(wstrDirName, L"/", ResultStr);
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
static bool ExistFile(std::wstring wstrFileName)
{
	std::string strFileName = StringUtil::WS2S(wstrFileName);
	int nRet = _access(strFileName.c_str(), 0);
	return nRet == 0;
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
static int GetDataFromFileByFS(std::wstring wsSrcFile, unsigned char*& pDstData, unsigned int& uiDstDataLen)
{
	std::ifstream FSSrc(StringUtil::WS2S(wsSrcFile).c_str(), std::ios_base::binary);
	if (!FSSrc.is_open())
	{
		return -1;
	}
	if (pDstData != NULL)
	{
		delete[] pDstData;
	}
	FSSrc.seekg(0, std::ios_base::end);
	uiDstDataLen = FSSrc.tellg();
	pDstData = new unsigned char[uiDstDataLen];
	FSSrc.seekg(0, std::ios_base::beg);
	FSSrc.read((char*)&pDstData[0], uiDstDataLen);
	FSSrc.close();
	return 0;
}
static int PutDataToFileByFS(unsigned char* pSrcData, unsigned int uiSrcDataLen, std::wstring wsDstFile)
{
	std::ofstream FSDst(StringUtil::WS2S(wsDstFile).c_str(), std::ios_base::binary);
	if (!FSDst.is_open())
	{
		return -1;
	}
	FSDst.seekp(0, std::ios_base::beg);
	FSDst.write((char*)&pSrcData[0], uiSrcDataLen);
	FSDst.close();
	return 0;
}

static int GetFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop, std::vector<std::wstring>& FileArr);

static int DelFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop);



#endif