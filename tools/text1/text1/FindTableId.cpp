#include "stdafx.h"
#include "filesystem.h"
#include <vector>
#include <list>
#include <set>
#include <map>

wchar_t findTableId_GetNextChar(const wchar_t* s)
{
	while (*s)
	{
		++s;
		if ((*s >= L'0'&& *s <= L'9') ||
			(*s >= L'a'&& *s <= L'z') ||
			(*s >= L'A'&& *s <= L'Z') ||
			*s == L'_'
			)
		{
			return *s;
		}
	}

	return *s;
}

bool findTableId_SeekText(const wchar_t* s,const wchar_t* text)
{
	int s_len = wcslen(s);
	int text_len = wcslen(text);

	if (s_len >= text_len)
	{
		return !memcmp(s, text, text_len);
	}
	return false;
}

CString findTableId_GetKuoHaoText(const wchar_t* s)
{
	CString ret;
	if (findTableId_GetNextChar(s) == L'(')
	{
		while (*s)
		{
			if (*s != ')')
			{
				ret += *s;
			}
			else
				break;
		}
	}
	return ret;
}

CString findTableId_GetRecord(const wchar_t* s)
{
	CString ret;
	// 寻找第一个括号后面的.
	while (*s)
	{
		if (findTableId_GetNextChar(s) == L'(' &&
			findTableId_GetNextChar(s) == L')' &&
			findTableId_GetNextChar(s) == L'.' &&
			findTableId_SeekText(s, L"Record") &&
			findTableId_GetNextChar(s) == L'('
			)
		{
			return findTableId_GetKuoHaoText(s);
		}
	}

	return L"";
}

std::set<CString> g_CStringRes;
std::set<CString> g_SStringRes;
std::set<CString> g_CMessageTip;
std::set<CString> g_ItemClass;
std::set<CString> g_ItemType;
std::set<CString> g_CItemType;
std::set<CString> g_SServiceConds;
std::set<CString> g_CNpcServer;

void findTableId_Parse(std::vector<CString> rets,CString& line,CString& type)
{
	CString s;
	for (int i = 0; i < line.GetLength(); ++i)
	{
		if ((line[i] >= L'0'&& line[i] <= L'9') ||
			(line[i] >= L'a'&& line[i] <= L'z') ||
			(line[i] >= L'A'&& line[i] <= L'Z') ||
			line[i] == L'_'
			)
		{
			s += line[i];
		}
		else
		{
			if (type == "h" || type == "cpp")
			{
				if (s == L"GetCStringResTableInstance")
					g_CStringRes.insert(findTableId_GetRecord(s));
			}
			/*
			else if (s == L"GetSStringResTableInstance")
				g_SStringRes.insert(findTableId_GetRecord(s));
			else if (s == L"GetCMessageTipResTableInstance")
				g_CMessageTip.insert(findTableId_GetRecord(s));
			else if (s == L"GetItemClassToLoadTableInstance")
				g_ItemClass.insert(findTableId_GetRecord(s));
			else if (s == L"GetItemTypeTableInstance")
				g_ItemType.insert(findTableId_GetRecord(s));
			else if (s == L"GetCItemTypeTableInstance")
				g_CItemType.insert(findTableId_GetRecord(s));
			else if (s == L"GetSServiceCondsTableInstance")
				g_SServiceConds.insert(findTableId_GetRecord(s));
			else if (s == L"GetCNpcServerConfigTableInstance")
				g_CNpcServer.insert(findTableId_GetRecord(s));
				*/
		}
	}
}
void findTables(dir& dirFiles, CString& type)
{
	std::vector<std::wstring>& vwsFiles = dirFiles.get_files_by_type(type.GetString());
	for (int i = 0; i < vwsFiles.size(); ++i)
	{
		// 加载文件.
		CStdioFile file;
		std::vector<CString> words;
		if (file.Open(vwsFiles[i].c_str(), CFile::modeRead | CFile::typeText))
		{
			CString s;
			while (file.ReadString(s))
			{
				findTableId_Parse(words, s, type);
			}
			file.Close();
		}

		// 写解析结果到文本里.
		if (file.Open(L"ids.txt", CFile::modeCreate | CFile::modeWrite | CFile::typeText))
		{
			file.WriteString(L"CStringRes\n");
			std::set<CString>::iterator it = g_CStringRes.begin();
			while (it != g_CStringRes.end())
			{
				file.WriteString(*it + L"\n");
				++it;
			}

			file.WriteString(L"SStringRes\n");
			it = g_SStringRes.begin();
			while (it != g_SStringRes.end())
			{
				file.WriteString(*it + L"\n");
				++it;
			}

			file.WriteString(L"CMessageTip\n");
			it = g_CMessageTip.begin();
			while (it != g_CMessageTip.end())
			{
				file.WriteString(*it + L"\n");
				++it;
			}

			file.WriteString(L"ItemClass\n");
			it = g_ItemClass.begin();
			while (it != g_ItemClass.end())
			{
				file.WriteString(*it + L"\n");
				++it;
			}
			file.WriteString(L"ItemType\n");
			it = g_ItemType.begin();
			while (it != g_ItemType.end())
			{
				file.WriteString(*it + L"\n");
				++it;
			}
			file.WriteString(L"CItemType\n");
			it = g_CItemType.begin();
			while (it != g_CItemType.end())
			{
				file.WriteString(*it + L"\n");
				++it;
			}
			file.WriteString(L"SServiceConds\n");
			it = g_SServiceConds.begin();
			while (it != g_SServiceConds.end())
			{
				file.WriteString(*it + L"\n");
				++it;
			}
			file.WriteString(L"CNpcServer\n");
			it = g_CNpcServer.begin();
			while (it != g_CNpcServer.end())
			{
				file.WriteString(*it + L"\n");
				++it;
			}

			file.Flush();
			file.Close();
		}
	}
}


void myFindTableId(CString& path, std::vector<CString>& vsTypes)
{
	//整体替换.
	dir dirFiles;
	dirFiles.push_files(path);
	for (int i = 0; i < vsTypes.size(); ++i)
	{
		findTables(dirFiles, vsTypes[i]);
	}
}

