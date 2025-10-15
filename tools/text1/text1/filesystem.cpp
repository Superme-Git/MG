#include "StdAfx.h"
#include "filesystem.h"

dir::dir()
{
}

dir::~dir()
{
}

std::vector<std::wstring> dir::get_files_by_type(std::wstring type)
{
	std::vector<std::wstring> ret;
	for (unsigned int i = 0; i < m_items.size(); i++)
	{
		if (!m_items[i].isDir && m_items[i].ext == type)
		{
			ret.push_back(m_items[i].fullPath);
		}
	}
	return ret;
}

void dir::push_dirs(const wchar_t* d)
{
	m_strDir = d;
	WIN32_FIND_DATA fd;
	std::wstring searchpath = d;
	searchpath += L"/*";
	HANDLE hFind = ::FindFirstFile(searchpath.c_str(), &fd);

	if (hFind != INVALID_HANDLE_VALUE)
	{
		do{
			item itm;
			if (fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
			{
				if (fd.cFileName[0] == L'.') continue;
				itm.isDir = false;
				itm.fileName = fd.cFileName;
				itm.fullPath = d;
				itm.fullPath += L"/";
				itm.fullPath += fd.cFileName;
				m_items.push_back(itm);
				push_dirs(itm.fullPath.c_str());
			}

		} while (::FindNextFile(hFind, &fd));

		::FindClose(hFind);
	}
}

void dir::push_dir_fires(const wchar_t* d)
{
	m_strDir = d;
	WIN32_FIND_DATA fd;
	std::wstring searchpath = d;
	searchpath += L"/*";
	HANDLE hFind = ::FindFirstFile(searchpath.c_str(), &fd);

	if (hFind != INVALID_HANDLE_VALUE)
	{
		do{
			item itm;
			if (0 == (fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))//Ŀ¼
			{
				if (fd.cFileName[0] == L'.') continue;
				itm.isDir = false;
				itm.fileName = fd.cFileName;
				itm.fullPath = d;
				itm.fullPath += L"/";
				itm.fullPath += fd.cFileName;
				itm.ext = itm.fileName.substr(itm.fileName.find_last_of(L'.') + 1, itm.fileName.length());
				m_items.push_back(itm);
			}
			else
			{
				if (fd.cFileName[0] == L'.') continue;
				std::wstring strPath = d;
				strPath += L"/";
				strPath += fd.cFileName;
				push_files(strPath.c_str());
				
				itm.isDir = true;
				itm.fileName = fd.cFileName;
				itm.fullPath = m_strDir + L"/" + fd.cFileName;
				itm.ext = L"";
				m_items.push_back(itm);
			}

		} while (::FindNextFile(hFind, &fd));

		::FindClose(hFind);
	}
}

void dir::push_files(const wchar_t* d)
{
	m_strDir = d;
	WIN32_FIND_DATA fd;
	std::wstring searchpath = d;
	searchpath += L"/*";
	HANDLE hFind = ::FindFirstFile(searchpath.c_str(), &fd);

	if (hFind != INVALID_HANDLE_VALUE)
	{
		do{
			item itm;
			if (0 == (fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))//Ŀ¼
			{
				if (fd.cFileName[0] == L'.') continue;
				itm.isDir = false;
				itm.fileName = fd.cFileName;
				itm.fullPath = d;
				itm.fullPath += L"/";
				itm.fullPath += fd.cFileName;
				itm.ext = itm.fileName.substr(itm.fileName.find_last_of(L'.') + 1, itm.fileName.length());
				m_items.push_back(itm);

				OutputDebugString(itm.fullPath.c_str());
				OutputDebugString(L"\n");
			}
			else
			{
				/*
				if (fd.cFileName[0] == L'.') continue;
				itm.isDir = true;
				itm.fileName = fd.cFileName;
				itm.fullPath = m_strDir + L"\\" + fd.cFileName;
				*/
				if (fd.cFileName[0] == L'.') continue;
				std::wstring strPath = d;
				strPath += L"/";
				strPath += fd.cFileName;
				push_files(strPath.c_str());
				//TRACE(itm.fileName.c_str());TRACE(L"\n");
				//TRACE(itm.fullPath.c_str());TRACE(L"\n");
				//TRACE(itm.ext.c_str());TRACE(L"\n");
			}

		} while (::FindNextFile(hFind, &fd));

		::FindClose(hFind);
	}
}