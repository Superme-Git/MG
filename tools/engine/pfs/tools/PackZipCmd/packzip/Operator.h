#pragma once
#include "ZipTreeItem.h"
#include <assert.h>
#include "Project.h"
#include "BaseThread.h"
#include "../../../pfs2.h"
#include "../../../src/utility/md5hash.h"
#include "../../../src/zip/zipfile.h"
#include "../../../setupmetafile.h"


enum { BUFFERSIZE = 65536, };

using PFS::fsize_t;

class COperator
{
protected:
	CZipTreeItem&				m_parent;
	CThreadListener*			m_listener;
	
public:
	enum PROGRESS_TYPE
	{
		PT_TOTALCOUNT = 0,		// 总数
		PT_TOTALPROGRESS,		// 
		PT_TOTALEND,
		PT_FILENAME,
		PT_FILEERROR,			// 文件打开出错
		PT_ADDEND,				// 添加操作完成
		PT_DELETEEND,			// 删除操作完成

		PT_ADDEND_PROJECT,
		PT_DELETEEND_PROJECT,
		PT_SETCHECKED,
	};
	typedef std::vector<COperator*>		COperatorVector;
	
	static bool					m_bStop;	// 线程是否停止

	COperator( CZipTreeItem& parent) : m_parent( parent), m_listener( NULL) {}
	virtual ~COperator() {}

	void SetListener( CThreadListener* listener) { m_listener = listener; }
	virtual bool Do() = 0;
};

class CNeedProjectOperator : public COperator
{
protected:
	CProject&		m_project;

	void AddToProject( const std::wstring& path, const std::wstring& windowpath, bool bIsPath, COperatorManager::OPERATOR_TYPE type)
	{
		COperatorManager::COperatorItem op;
		op.m_path = path;
		op.m_ispath = bIsPath;
		op.m_type = type;
		op.m_windowpath = windowpath;
		if( type == COperatorManager::OT_ADD || type == COperatorManager::OT_ADDPATH)
			m_project.GetMountManager().GetRelatePath( op.m_windowpath);

		COperatorManager::COperatorItemVector& projects = m_project.GetOperators();
		COperatorManager::COperatorItemVector::iterator itor = std::find( projects.begin(), projects.end(), op);
		if( itor != projects.end())
			projects.erase( itor);		// 删掉重复操作

		projects.push_back( op);
	}

	CZipTreeItem* GetZipItemFromPath( const std::wstring& path, CZipTreeItem& parent)
	{
		if( path.empty())
			return &parent;

		CZipTreeItem::CZipTreeItemList& map = parent.GetChild();
		for( CZipTreeItem::CZipTreeItemList::iterator itor = map.begin(); itor != map.end(); ++itor)
		{
			const std::wstring& name = itor->GetName();
			if( name == path)
				return &( *itor);
			if( path.find( name) == 0)
				return GetZipItemFromPath( path, *itor);
		}
		return NULL;
	}

	const CZipTreeItem* GetZipItemFromPath( const std::wstring& path, const CZipTreeItem& parent) const
	{
		if( path.empty())
			return &parent;
		const CZipTreeItem::CZipTreeItemList& map = parent.GetChild();
		for( CZipTreeItem::CZipTreeItemList::const_iterator itor = map.begin(); itor != map.end(); ++itor)
		{
			const std::wstring& name = itor->GetName();
			if( name == path)
				return &( *itor);
			if( path.find( name) == 0)
				return GetZipItemFromPath( path, *itor);
		}
		return NULL;
	}

public:
	CNeedProjectOperator( CZipTreeItem& parent, CProject& project)
		: COperator( parent), m_project( project) {}
	CProject& GetProject() { return m_project; }
	
	static std::wstring GetWindowPath( CZipTreeItem& item, CProject& project)
	{
		std::wstring path = item.GetMount();
		std::wstring::iterator begin = path.begin();
		begin++;
		std::replace( begin, path.end(), L'/', L'\\');
		project.GetMountManager().GetWindowPath( path);
		return path;
	}
};


class COperatorThread : public CBaseThread
{
	COperator::COperatorVector		m_operators;
public:
	COperatorThread( ) {}
	void SetOperators( COperator::COperatorVector& operators) { m_operators = operators; }

	virtual void OnRunning()
	{
		m_stop = false;
		for( COperator::COperatorVector::iterator itor = m_operators.begin(); itor != m_operators.end(); ++itor)
		{
			(*itor)->SetListener( m_listener);
			SetThreadResult(true);
			if(!(*itor)->Do())
			{
				SetThreadResult(false);
				throw std::exception("Operation failed");
			}
			if( m_stop)
				break;
		}		
		if( m_listener)
			m_listener->OnNotify( 0, COperator::PT_TOTALEND);
	}
};

class CSaveThreadListener : public CThreadListener
{
	wchar_t			m_buffer[MAX_PATH];
	int				m_writtencount;
	int				m_allfilecount;

public:
	CSaveThreadListener() {}
	
	virtual void OnNotify( WPARAM wParam, LPARAM lParam)
	{
		switch(lParam)
		{
		case COperator::PT_FILENAME:
			{
				/*wcscpy_s( m_buffer, MAX_PATH,  reinterpret_cast<LPCTSTR>( wParam));
				wParam = (WPARAM)m_buffer;*/
			}
			break;
		case COperator::PT_FILEERROR:
			{
			wchar_t	err[MAX_PATH] = {0};
			int nError = PFS::CEnv::GetLastError();
			if( nError == 0 ) 
				nError = GetLastError();
			if( nError == 0 )
			{
				// 既然没有错误信息，不再显示错误号。
				StringCchPrintf( err, MAX_PATH, L"%s", reinterpret_cast<LPCTSTR>( wParam) );
			}
			else
			{
				StringCchPrintf( err, MAX_PATH, L"%s 错误号: %d", reinterpret_cast<LPCTSTR>( wParam), PFS::CEnv::GetLastError());
			}
			ERRLOG(err);
			wParam = 0;
			break;
			}
		case COperator::PT_TOTALCOUNT:
			{
				m_allfilecount = (int)wParam;
				m_writtencount = 0;
				break;
			}
		case COperator::PT_TOTALPROGRESS:
			{
				m_writtencount += (int)wParam;
				printf("\r已完成:%d%%", m_writtencount*100/m_allfilecount);
				break;
			}
		case COperator::PT_TOTALEND:
			{
				break;
			}
		default:
			break;
		}
	}
};

class CFindPath
{
protected:
	std::wstring	m_basepath;
	virtual bool OnFind( const std::wstring& child, bool bPath) = 0;

public:
	CFindPath( const std::wstring& basepath) : m_basepath( basepath) {}
	bool DoFind()
	{
		std::wstring allpath = m_basepath + L"\\*.*";
		
		WIN32_FIND_DATAW wfd;
		HANDLE	hFind = FindFirstFileW( allpath.c_str(), &wfd);
		if( INVALID_HANDLE_VALUE == hFind)
			return false;
		bool res = false;
		do
		{
			std::wstring temp = wfd.cFileName;
			if( (L"." == temp) || (L".." == temp))
				continue;
			if( OnFind( temp, !!(FILE_ATTRIBUTE_DIRECTORY & wfd.dwFileAttributes)))
			{
				res = true;
				break;
			}
		}while( FindNextFile( hFind, &wfd));
		FindClose( hFind);
		return res;
	}
};

class COperatorFindPath : public CFindPath
{
protected:
	CZipTreeItem& m_parent;
	CNeedProjectOperator& m_operater;

public:
	COperatorFindPath( CZipTreeItem& parent, CNeedProjectOperator& operater) 
		: CFindPath( L""), m_parent( parent), m_operater( operater) 
	{
		m_basepath = CNeedProjectOperator::GetWindowPath( m_parent, m_operater.GetProject());
	}
};

// 添加空目录
class CAddNewPathOperator : public CNeedProjectOperator
{
	friend class CFindPath;

	void SearchFiles( CZipTreeItem& parent);
	bool IsHasChild( CZipTreeItem& parent);

protected:
	std::wstring	m_name;
	bool			m_iswindowpath;
	CZipTreeItem* AddPathOrFile( CZipTreeItem& parent, const std::wstring& path, bool bPath) ;

public:
	CAddNewPathOperator( CZipTreeItem& parent, CProject& project, const std::wstring& name)
		: CNeedProjectOperator( parent, project), m_name( name), m_iswindowpath( false) {}
	virtual bool Do()
	{
		CZipTreeItem* res = AddPathOrFile( m_parent, m_name, true);
		if( res)
		{
			AddToProject( m_parent.GetName(), m_name, true, COperatorManager::OT_ADDNEW);
		}
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, PT_ADDEND);
		return true;
	}
	CZipTreeItem* AddChild( CZipTreeItem& parent, const std::wstring& child, const std::wstring& mount, 
		bool bPath);
};

// 添加文件或目录
class CAddOperator : public CAddNewPathOperator
{
protected:
	bool			m_bPath;
public:
	CAddOperator( CZipTreeItem& parent, CProject& project, const std::wstring& name, bool bPath) 
		: CAddNewPathOperator( parent, project, name), m_bPath( bPath) { m_iswindowpath = true; }
	virtual bool Do()
	{
		CZipTreeItem* res = AddPathOrFile( m_parent, m_name, m_bPath);
		if( res)
		{
			AddToProject( m_parent.GetName(), m_name, res->GetIsPath(), COperatorManager::OT_ADD);
		}
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, PT_ADDEND);
		return true;
	}
};

// 添加目录内容
class CAddPathOperator : public CAddOperator
{
protected:
	void AddPath( CZipTreeItem& parent, PROGRESS_TYPE type);

public:
	CAddPathOperator( CZipTreeItem& parent, CProject& project, const std::wstring& name) 
		: CAddOperator( parent, project, name, true) {}
	virtual bool Do()
	{
		AddPath( m_parent, PT_ADDEND);
		AddToProject( m_parent.GetName(), m_name, true, COperatorManager::OT_ADDPATH);
		return true;
	}
};

class CAddNoInitOperator : public CAddPathOperator
{
public:
	CAddNoInitOperator( CZipTreeItem& parent, CProject& project)
		: CAddPathOperator( parent, project, L"") { m_name = GetWindowPath( parent, project); }
	virtual bool Do()
	{
		m_parent.SetIsNoInit( false);
		AddPath( m_parent, PT_ADDEND_PROJECT);
		return true;
	}
};

class CAddNewPathOperator_Project : public CAddNewPathOperator
{
	std::wstring			m_fullpath;
public:
	CAddNewPathOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& name, const std::wstring& fullpath)
		: CAddNewPathOperator( root, project, name), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		CZipTreeItem* parent = GetZipItemFromPath( m_fullpath, m_parent);
		if( !parent)
			return NULL;
		CZipTreeItem* res = AddPathOrFile( *parent, m_name, true);
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, PT_ADDEND_PROJECT);
		return true;
	}
};

class CAddOperator_Project : public CAddOperator
{
	std::wstring			m_fullpath;		// 
public:
	CAddOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& name, bool bPath, const std::wstring& fullpath)
		: CAddOperator( root, project, name, bPath), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		CZipTreeItem* parent = GetZipItemFromPath( m_fullpath, m_parent);
		if( !parent)
			return NULL;
		m_project.GetMountManager().GetWindowPath( m_name);

		CZipTreeItem* res = AddPathOrFile( *parent, m_name, m_bPath);
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, PT_ADDEND_PROJECT);
		return true;
	}
};

class CAddPathOperator_Project : public CAddPathOperator
{
	std::wstring		m_fullpath;
public:
	CAddPathOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& name, const std::wstring& fullpath)
		: CAddPathOperator( root, project, name), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		CZipTreeItem* parent = GetZipItemFromPath( m_fullpath, m_parent);
		if( !parent)
			return false;
		m_project.GetMountManager().GetWindowPath( m_name);
		AddPath( *parent, PT_ADDEND_PROJECT);
		return true;
	}
};

// 删除
class CDeleteOperator : public CNeedProjectOperator
{
protected:
	CZipTreeItem* DeleteTreeItem( CZipTreeItem& item, bool bAddProject)
	{
		CZipTreeItem* parent = item.GetParent();
		CZipTreeItem::CZipTreeItemList& map = parent->GetChild();
		CZipTreeItem::CZipTreeItemList::iterator itor = std::find( map.begin(), map.end(), item);
		if( itor != map.end())
		{
			CZipTreeItem* temp = &( *itor);
			if( bAddProject)
				AddToProject( temp->GetName(), L"", temp->GetIsPath(), COperatorManager::OT_DELETE);
			map.erase( itor);
			if( m_listener)
				m_listener->OnNotify( (WPARAM)temp, bAddProject ? PT_DELETEEND : PT_DELETEEND_PROJECT);
			return temp;
		}
		return NULL;
	}
public:
	CDeleteOperator( CZipTreeItem& deleteitem, CProject& project) 
		: CNeedProjectOperator( deleteitem, project) {}
	virtual bool Do()
	{
		return DeleteTreeItem( m_parent, true) != NULL;
	}
};

class CDeleteOperator_Project : public CDeleteOperator
{
	std::wstring		m_fullpath;
public:
	CDeleteOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& fullpath) 
		: CDeleteOperator( root, project), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		CZipTreeItem* deleteitem = GetZipItemFromPath( m_fullpath, m_parent);
		if( !deleteitem)
			return false;
		return DeleteTreeItem( *deleteitem, false) != NULL;
	}
};


// ignore规则改变，刷新所有item
class CRefreshCheckOperator : public CNeedProjectOperator
{
	void RefreshIsCheck( CZipTreeItem& parent);

public:
	CRefreshCheckOperator( CZipTreeItem& parent, CProject& project) 
		: CNeedProjectOperator( parent, project) {}
	virtual bool Do()
	{
		RefreshIsCheck( m_parent);
		return true;
	}
};

// ziplevel规则改变，刷新所有item
class CRefreshZipLevelOperator : public CNeedProjectOperator
{
	void RefreshZipLevel( CZipTreeItem& parent);
	void DeleteSetZipOperator()
	{
		COperatorManager::COperatorItemVector& projects = m_project.GetOperators();
		for( COperatorManager::COperatorItemVector::iterator itor = projects.begin(); itor != projects.end(); )
		{
			if( itor->m_type == COperatorManager::OT_SETZIPLEVEL)
				itor = projects.erase( itor);
			else
				++itor;
		}
	}

public:
	CRefreshZipLevelOperator( CZipTreeItem& parent, CProject& project)
		: CNeedProjectOperator( parent, project) {}
	virtual bool Do()
	{
		RefreshZipLevel( m_parent);
		DeleteSetZipOperator();
		return true;
	}
};

// 设置某个item的ziplevel
class CSetZipLevelOperator : public CNeedProjectOperator
{
	bool				m_bResource;
	int					m_value;

protected:
	CZipTreeItem*		m_item;
	void SetZipLevel( CZipTreeItem& item);
	//bool RefreshPathIsNoDef( CZipTreeItem& parent);

public:
	CSetZipLevelOperator( CZipTreeItem& root, CProject& project, CZipTreeItem* item, bool bResource, int value)
		: CNeedProjectOperator( root, project), m_item( item), m_bResource( bResource), m_value( value) {}
	virtual bool Do()
	{
		if( m_item)
		{
			SetZipLevel( *m_item);
			//RefreshPathIsNoDef( m_parent);
			const size_t size = 16;
			wchar_t buf[size] = {0};
			_itow_s( m_value, buf, size, 10);
			AddToProject( m_item->GetName(), buf, m_bResource, COperatorManager::OT_SETZIPLEVEL);
		}
		return true;
	}
};

class CSetZipLevelOperator_Project : public CSetZipLevelOperator
{
	std::wstring		m_fullpath;
public:
	CSetZipLevelOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& fullpath, bool bResource, int value)
		: CSetZipLevelOperator( root, project, NULL, bResource, value), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		m_item = GetZipItemFromPath( m_fullpath, m_parent);
		if( m_item)
		{
			SetZipLevel( *m_item);
			//RefreshPathIsNoDef( m_parent);
		}
		return true;
	}
};

// 设置某个item是否checked
class CSetCheckedOperator : public CNeedProjectOperator
{
protected:
	bool		m_check;
	void AddNoInitItem( CZipTreeItem& parent)
	{
		if( parent.GetIsNoInit() && parent.GetIsHasChild()) 
		{
			CAddNoInitOperator adder( parent, m_project);
			adder.SetListener( m_listener);
			adder.Do();
		}
		CZipTreeItem::CZipTreeItemList& map = parent.GetChild();
		for( CZipTreeItem::CZipTreeItemList::iterator itor = map.begin(); itor != map.end(); ++itor)
		{
			AddNoInitItem( *itor);
		}
	}
public:
	CSetCheckedOperator( CZipTreeItem& item, CProject& project, bool check)
		: CNeedProjectOperator( item, project), m_check( check) {}
	virtual bool Do()
	{
		if( m_check)
			AddNoInitItem( m_parent);
		CZipTreeItem::SetItemState( m_parent, CZipTreeItem::ZS_CHECK, m_check);
		CZipTreeItem::SetItemState( m_parent, CZipTreeItem::ZS_SETCHECK, true);
		AddToProject( m_parent.GetName(), L"", m_check, COperatorManager::OT_SETCHECKED);
		return true;
	}
};

class CSetCheckedOperator_Project : public CSetCheckedOperator
{
	std::wstring	m_fullpath;
public:
	CSetCheckedOperator_Project( CZipTreeItem& root, CProject& project, bool check, const std::wstring& fullpath)
		: CSetCheckedOperator( root, project, check), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		CZipTreeItem* item = GetZipItemFromPath( m_fullpath, m_parent);
		if( item)
		{
			if( m_check)
				AddNoInitItem( *item);
			CZipTreeItem::SetItemState( *item, CZipTreeItem::ZS_CHECK, m_check);
			CZipTreeItem::SetItemState( *item, CZipTreeItem::ZS_SETCHECK, true);
			if( m_listener)
				m_listener->OnNotify( (WPARAM)item, COperator::PT_SETCHECKED);
		}
		return true;
	}
};

// 设置某个item的download url
class CSetUrlOperator : public CNeedProjectOperator
{
protected:
	std::wstring		m_url;
public:
	CSetUrlOperator( CZipTreeItem& parent, CProject& project, const std::wstring& url)
		: CNeedProjectOperator( parent, project), m_url( url) {}
	virtual bool Do()
	{
		m_parent.SetUrl( m_url);
		AddToProject( m_parent.GetName(), m_url, true, COperatorManager::OT_SETURL);
		return true;
	}
};

class CSetUrlOperator_Project : public CSetUrlOperator
{
	std::wstring	m_fullpath;
public:
	CSetUrlOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& url, const std::wstring& fullpath)
		: CSetUrlOperator( root, project, url), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		CZipTreeItem* item = GetZipItemFromPath( m_fullpath, m_parent);
		if( item)
			item->SetUrl( m_url);
		return true;
	}
};

// 设置某个item是否pack
class CSetPackOperator : public CNeedProjectOperator
{
protected:
	bool		m_pack;
public:
	CSetPackOperator( CZipTreeItem& item, CProject& project, bool pack)
		: CNeedProjectOperator( item, project), m_pack( pack) {}
	virtual bool Do()
	{
		CZipTreeItem::SetItemState( m_parent, CZipTreeItem::ZS_PACK, m_pack);
		AddToProject( m_parent.GetName(), L"", m_pack, COperatorManager::OT_SETPACK);
		return true;
	}
};

class CSetPackOperator_Project : public CSetPackOperator
{
	std::wstring	m_fullpath;
public:
	CSetPackOperator_Project( CZipTreeItem& root, CProject& project, bool pack, const std::wstring& fullpath)
		: CSetPackOperator( root, project, pack), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		CZipTreeItem* item = GetZipItemFromPath( m_fullpath, m_parent);
		if( item)
			CZipTreeItem::SetItemState( *item, CZipTreeItem::ZS_PACK, m_pack);
		return true;
	}
};

// 设置某个item是否为child package
class CSetChildPackageOperator : public CNeedProjectOperator
{
protected:
	bool		m_childpackage;
public:
	CSetChildPackageOperator( CZipTreeItem& item, CProject& project, bool childpackage)
		: CNeedProjectOperator( item, project), m_childpackage( childpackage) {}
	virtual bool Do()
	{
		m_parent.SetIsChildPackage( m_childpackage);
		AddToProject( m_parent.GetName(), L"", m_childpackage, COperatorManager::OT_SETCHILDPACKAGE);
		return true;
	}
};

class CSetChildPackageOperator_Project : public CSetChildPackageOperator
{
	std::wstring	m_fullpath;
public:
	CSetChildPackageOperator_Project( CZipTreeItem& root, CProject& project, bool childpackage, const std::wstring& fullpath)
		: CSetChildPackageOperator( root, project, childpackage), m_fullpath( fullpath) {}
	virtual bool Do()
	{
		CZipTreeItem* item = GetZipItemFromPath( m_fullpath, m_parent);
		if( item)
			item->SetIsChildPackage( m_childpackage);
		return true;
	}
};