#pragma once

class filesystem
{
public:
	filesystem(void);
	~filesystem(void);
};

typedef std::vector<std::wstring> VF;
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
	dir(std::wstring d);
	~dir();

	std::vector<std::wstring> get_subdirs();
	std::vector<std::wstring> get_files();
	std::vector<std::wstring> get_files_by_type(std::wstring type);
	std::vector<std::wstring> get_files_not_type(std::vector<std::wstring> types);
};
