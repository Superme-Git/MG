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

		bool isCreateMipMap;
		bool has_alpha;
		bool converted_png;
		std::wstring converted_fullpath;

		item()
			: has_alpha( false )
			, converted_png( false )
			, isCreateMipMap(false)
		{
		}
	};

	std::vector<item> m_items;
public:
	dir(std::wstring d);
	~dir();

	std::vector<std::wstring> get_subdirs();
	std::vector<std::wstring> get_files();
	std::vector<std::wstring> get_files_by_type(std::wstring type);
	std::vector<std::wstring> get_files_not_type(std::vector<std::wstring> types);
	void get_file_item_recursive( std::vector<item>& items, std::vector<std::wstring>& filters );
	void get_file_png_item_recursive( std::vector<item>& items, std::vector<std::wstring>& filters ,std::vector<item*>& in_items, float rescale = 1.0f);
	void CreateMipMapTree(std::vector<item*>& items,CTreeCtrl& m_Tree,const HTREEITEM& h_parent,std::vector<std::wstring>& filters);
};


bool check_dir_existence( const std::wstring& _dir );