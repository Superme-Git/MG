#ifndef LJFP_Seach_H
#define LJFP_Seach_H

#include "LJFP_Var.h"
#include "LJFP_Option.h"

#include <algorithm>

typedef void(*Find_Func)(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strFileName);

class LJFP_Find
{
public:
	Find_Func m_FindFunc;

	LJFP_Find()
	{
		m_FindFunc = NULL;
	}
	~LJFP_Find()
	{
		m_FindFunc = NULL;
	}
	int InitFind()
	{
		return 0;
	}
	void OnFindDir(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strDirName)
	{
		if (GetOption().IsFilterDir(strParentPathName, strDirName))
		{
			return;
		}
		std::wstring strCurrentPath = strDirName + L"/";
		FindFiles(strRootPathName, strParentPathName, strCurrentPath);
	}
	void OnFindFile(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strFileName)
	{
		if (GetOption().IsFilterFile(strParentPathName, strFileName))
		{
			return;
		}
		if (m_FindFunc)
		{
			m_FindFunc(strRootPathName, strParentPathName, strFileName);
		}
	}
	void OnFindData(std::wstring strRootPathName, std::wstring strParentPathName, WIN32_FIND_DATAW& FindData)
	{
		if ('.' == FindData.cFileName[0])
		{
			return;
		}
		//std::transform(FindData.cFileName.begin(), pfsname.end(), pfsname.begin(), ::tolower);
		//std::wstring strCurrentFileName(FindData.cFileName);
		//_wcslwr_s(strCurrentFileName.c_str(), MAX_PATH);
		_wcslwr_s(FindData.cFileName, MAX_PATH);
		std::wstring strCurrentFileName(FindData.cFileName);
		if (FILE_ATTRIBUTE_DIRECTORY == (FILE_ATTRIBUTE_DIRECTORY & FindData.dwFileAttributes))
		{
			OnFindDir(strRootPathName, strParentPathName, strCurrentFileName);
		}
		else
		{
			if (FindData.nFileSizeLow == 0)
			{
				return;
			}
			OnFindFile(strRootPathName, strParentPathName, strCurrentFileName);
		}
	}
	int FindFiles(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strPathName)
	{
		std::wstring strFindPath = strRootPathName + strParentPathName + strPathName + L"*.*";
		WIN32_FIND_DATAW FindData;
		//HANDLE hFind = FindFirstFileExA(strFindPath.c_str(), FindExInfoStandard, &FindData, FindExSearchNameMatch, NULL, FIND_FIRST_EX_CASE_SENSITIVE);
		HANDLE hFind = FindFirstFileW(strFindPath.c_str(), &FindData);
		if (INVALID_HANDLE_VALUE == hFind)
		{
			return -1;
		}
		OnFindData(strRootPathName, strParentPathName + strPathName, FindData);
		while (FindNextFileW(hFind, &FindData))
		{
			OnFindData(strRootPathName, strParentPathName + strPathName, FindData);
		}
		FindClose(hFind);

		return 0;
	}
};

LJFP_Find* g_Find = NULL;

LJFP_Find* GetFind()
{
	if (!g_Find)
	{
		g_Find = new LJFP_Find();
	}
	return g_Find;
}
void ReleaseFind()
{
	if (g_Find)
	{
		delete g_Find;
		g_Find = NULL;
	}
}
#endif //LJFP_Seach_H