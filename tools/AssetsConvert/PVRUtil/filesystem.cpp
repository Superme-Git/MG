#include "StdAfx.h"
#include "filesystem.h"
#include "FreeImageWrapper.h"
#include <algorithm>
#include "control.h"

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
			}
			else
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

void dir::get_file_item_recursive( std::vector<item>& items, std::vector<std::wstring>& filters )
{
	for( size_t i = 0; i < m_items.size(); i++ )
	{
		bool valid_file = false;

		if( !m_items[i].isDir )
		{
			std::wstring _ext = m_items[i].ext;

			std::transform( _ext.begin(), _ext.end(), _ext.begin(), tolower );

			for( size_t j = 0; j < filters.size(); j++ )
			{
				if( _ext == filters[j] )
				{
					valid_file = true; 
					break;
				}
			}

			if( valid_file )
			{
				items.push_back(m_items[i]);
			}
		}

		else if( m_items[i].isDir )
		{
			dir in_dir( m_items[i].fullPath );
			in_dir.get_file_item_recursive(items, filters );
		}
	}
}

void dir::get_file_png_item_recursive( std::vector<item>& items, std::vector<std::wstring>& filters ,std::vector<item*>& in_items, float rescale)
{
	for( size_t i = 0; i < m_items.size(); i++ )
	{
		bool valid_file = false;

		if( !m_items[i].isDir )
		{
			std::wstring _ext = m_items[i].ext;

			std::transform( _ext.begin(), _ext.end(), _ext.begin(), tolower );

			for( size_t j = 0; j < filters.size(); j++ )
			{
				std::wstring strFullPath = m_items[i].fullPath.substr(0, m_items[i].fullPath.rfind(L"\\"));
				if (isExistInExcludePaths(strFullPath))
				{
					continue;
				}

				//size_t nPos = _ext.find(L"map_");

				if (_ext == filters[j] )
				{
					FreeImageWrapper::rescaleImage(m_items[i].fullPath, m_items[i].fullPath, rescale);
					if( _ext == L"tga" && !m_items[i].converted_png )
					{
						m_items[i].converted_fullpath = m_items[i].fullPath;
						size_t pos = m_items[i].converted_fullpath.rfind(L".");

						m_items[i].converted_fullpath.replace(pos, m_items[i].converted_fullpath.length(), L".png");

						if( FreeImageWrapper::convertImageFormat( m_items[i].fullPath, m_items[i].converted_fullpath ) )
						{
							m_items[i].converted_png = true;
						}
						else
						{
							::MessageBox( 0, std::wstring(m_items[i].fullPath + L" Converted to png Failed !").c_str(), L"Error", 0 );
							m_items[i].converted_fullpath = L"";
						}
					}
					else if( _ext == L"bmp" && !m_items[i].converted_png )
					{
						m_items[i].converted_fullpath = m_items[i].fullPath;
						size_t pos = m_items[i].converted_fullpath.rfind(L".");

						m_items[i].converted_fullpath.replace(pos, m_items[i].converted_fullpath.length(), L".png");

						if( FreeImageWrapper::convertImageFormat( m_items[i].fullPath, m_items[i].converted_fullpath ) )
						{
							m_items[i].converted_png = true;
						}
						else
						{
							::MessageBox( 0, std::wstring(m_items[i].fullPath + L" Converted to png Failed !").c_str(), L"Error", 0 );
							m_items[i].converted_fullpath = L"";
						}
					}

					else if( _ext == L"jpg" && !m_items[i].converted_png )
					{
						m_items[i].converted_fullpath = m_items[i].fullPath;
						size_t pos = m_items[i].converted_fullpath.rfind(L".");

						m_items[i].converted_fullpath.replace(pos, m_items[i].converted_fullpath.length(), L".png");

						if( FreeImageWrapper::convertImageFormat( m_items[i].fullPath, m_items[i].converted_fullpath ) )
						{
							m_items[i].converted_png = true;
						}
						else
						{
							::MessageBox( 0, std::wstring(m_items[i].fullPath + L" Converted to png Failed !").c_str(), L"Error", 0 );
							m_items[i].converted_fullpath = L"";
						}
					}
				
					else if (_ext == L"dds" && !m_items[i].converted_png)
					{
						m_items[i].converted_fullpath = m_items[i].fullPath;
						size_t pos = m_items[i].converted_fullpath.rfind(L".");

						m_items[i].converted_fullpath.replace(pos, m_items[i].converted_fullpath.length(), L".png");

						if (FreeImageWrapper::convertImageFormat(m_items[i].fullPath, m_items[i].converted_fullpath))
						{
							m_items[i].converted_png = true;
						}
						else
						{
							//::MessageBox(0, std::wstring(m_items[i].fullPath + L" Converted to png Failed !").c_str(), L"Error", 0);
							m_items[i].converted_fullpath = L"";
						}
					}
					else //wangbin add
					{
						//size_t nPos = _ext.find(L"map_");

						//if (nPos != std::wstring::npos && !m_items[i].converted_png)
						//{
						//	m_items[i].converted_fullpath = m_items[i].fullPath;
						//	size_t pos = m_items[i].converted_fullpath.length();

						//	m_items[i].converted_fullpath.replace(pos, m_items[i].converted_fullpath.length(), L".png");

						//	if (FreeImageWrapper::convertImageFormat(m_items[i].fullPath, m_items[i].converted_fullpath))
						//	{
						//		m_items[i].converted_png = true;
						//	}
						//	else
						//	{
						//		//::MessageBox(0, std::wstring(m_items[i].fullPath + L" Converted to png Failed !").c_str(), L"Error", 0);
						//		m_items[i].converted_fullpath = L"";
						//	}
						//}
					}

					valid_file = true; 
					break;
				}
				//end if
			}
			//end for

			if( valid_file )
			{
				for( size_t j = 0;j<in_items.size();++j)
				{
					if(m_items[i].fileName == in_items[j]->fileName)
					{
						m_items[i].isCreateMipMap = in_items[j]->isCreateMipMap;
						break;
					}
				}
				
				items.push_back(m_items[i]);
			}
		}

		else if( m_items[i].isDir )
		{
			dir in_dir( m_items[i].fullPath );
			in_dir.get_file_png_item_recursive(items, filters ,in_items, rescale);
		}
	}
}

void dir::CreateMipMapTree( std::vector<item*>& items,CTreeCtrl& m_Tree,const HTREEITEM& h_parent,std::vector<std::wstring>& filters )
{

	item* pItem;

	for( size_t i = 0; i < m_items.size(); i++ )
	{
		HTREEITEM h_chile = 0;
		if( !m_items[i].isDir )
		{
			std::wstring _ext = m_items[i].ext;

			std::transform( _ext.begin(), _ext.end(), _ext.begin(), tolower );

			for( size_t j = 0; j < filters.size(); j++ )
			{
				if( _ext == filters[j] )
				{
					pItem = new item;
					items.push_back(pItem);
					*pItem = m_items[i];
					h_chile = m_Tree.InsertItem(m_items[i].fileName.c_str(), 0, 0, h_parent, TVI_LAST);
					m_Tree.SetItemData(h_chile,reinterpret_cast<DWORD_PTR>(pItem));
				}
			}
		}
		else if( m_items[i].isDir )
		{
			h_chile = m_Tree.InsertItem(m_items[i].fileName.c_str(), 0, 0, h_parent, TVI_LAST);
			m_Tree.SetItemData(h_chile,0);
			dir in_dir( m_items[i].fullPath );
			in_dir.CreateMipMapTree(items,m_Tree, h_chile, filters );
		}
	}
}



bool check_dir_existence( const std::wstring& _dir )
{
	WIN32_FIND_DATA  wfd;
	bool rValue = false;
	HANDLE hFind = FindFirstFile(_dir.c_str(), &wfd);
	if ((hFind != INVALID_HANDLE_VALUE) && (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
	{
		rValue = true;   
	}
	FindClose(hFind);
	return rValue;
}
