#include "StdAfx.h"
#include "filesystem.h"

filesystem::filesystem(void)
{
}

filesystem::~filesystem(void)
{
}

dir::dir(std::wstring d)
{
	m_strDir = d;
	WIN32_FIND_DATA fd;
	std::wstring searchpath = d + L"/*";
	HANDLE hFind = ::FindFirstFile(searchpath.c_str(), &fd);

	if ( hFind != INVALID_HANDLE_VALUE )
	{
		do{
			item itm;
			if ( 0==(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) )//Ŀ¼
			{
				if (fd.cFileName[0] == L'.') continue;
				itm.isDir = false;
				itm.fileName = fd.cFileName;
				itm.fullPath = m_strDir + L"\\" + fd.cFileName;
				itm.ext = itm.fileName.substr(itm.fileName.find_last_of(L'.') + 1, itm.fileName.length());
			}else
			{
				if (fd.cFileName[0] == L'.') continue;
				itm.isDir = true;
				itm.fileName = fd.cFileName;
				itm.fullPath = m_strDir + L"\\" + fd.cFileName;
			}

			//TRACE(itm.fileName.c_str());TRACE(L"\n");
			//TRACE(itm.fullPath.c_str());TRACE(L"\n");
			//TRACE(itm.ext.c_str());TRACE(L"\n");
			m_items.push_back(itm);
		}while (::FindNextFile(hFind, &fd));

		::FindClose(hFind);
	}
}

dir::~dir()
{

}

std::vector<std::wstring> dir::get_subdirs()
{
	std::vector<std::wstring> ret;
	for (unsigned int i = 0; i < m_items.size(); i++)
	{
		if (m_items[i].isDir)
		{
			ret.push_back(m_items[i].fullPath);
		}
	}
	return ret;
}

std::vector<std::wstring> dir::get_files()
{
	std::vector<std::wstring> ret;
	for (unsigned int i = 0; i < m_items.size(); i++)
	{
		if (!m_items[i].isDir)
		{
			ret.push_back(m_items[i].fullPath);
		}
	}
	return ret;
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

std::vector<std::wstring> dir::get_files_not_type(std::vector<std::wstring> types)
{
	std::vector<std::wstring> ret;
	for (unsigned int i = 0; i < m_items.size(); i++)
	{
		bool bMatch = true;
		for (unsigned j = 0; j < types.size(); j++)
		{
			if (m_items[i].ext == types[j])
			{
				bMatch = false;
				break;
			}			
		}
		if (!m_items[i].isDir && bMatch)
		{
			ret.push_back(m_items[i].fullPath);
		}
	}
	return ret;
}
