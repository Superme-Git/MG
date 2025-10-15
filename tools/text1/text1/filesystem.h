#pragma once
#include <vector>
#include <Windows.h>

class dir
{
public:
	std::wstring m_strDir;
	struct item
	{
		std::wstring ext;
		bool isDir;
		std::wstring fileName;
		std::wstring fullPath;
	};

	std::vector<item> m_items;
public:
	dir();
	~dir();

	void push_files(const wchar_t* d);
	void push_dirs(const wchar_t* d);
	void push_dir_fires(const wchar_t* d);
	std::vector<std::wstring> get_files_by_type(std::wstring type);
};
