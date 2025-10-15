#include "Operator.h"


// 添加空目录
// "Add empty directory ..."
class CAddNewPathOperator : public CNeedProjectOperator
{
protected:
	std::wstring	m_name;
	CZipTreeItem*	AddNewPath(CZipTreeItem& parent) ;

public:
	CAddNewPathOperator( CZipTreeItem& parent, CProject& project, const std::wstring& name)
		: CNeedProjectOperator( parent, project), m_name( name) {}

	virtual CZipTreeItem* Do();	
};


// 添加文件或目录
class CAddOperator : public CNeedProjectOperator
{
protected:
	friend class CFindPath;

	std::wstring	m_name;
	bool			m_bPath;

	CZipTreeItem* AddPathOrFile( CZipTreeItem& parent );

public:
	CZipTreeItem* AddChild( CZipTreeItem& parent, const std::wstring& child, 
							const std::wstring& mount, bool bPath);

public:
	CAddOperator( CZipTreeItem& parent, CProject& project, const std::wstring& name, bool bPath) 
		: CNeedProjectOperator( parent, project), m_name(name), m_bPath( bPath) 
	{
	}

	virtual CZipTreeItem* Do()
	{
		CZipTreeItem* res = AddPathOrFile( m_parent );
		if( res)
		{
			AddToProject( m_parent.GetName(), m_name, res->GetIsPath(), COperatorManager::OT_ADD);
		}
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, PT_ADDEND);
		return res;
	}
};

//
// 添加目录内容
// "Add directory ..."
//
class CAddPathOperator : public CAddOperator
{
protected:
	void AddPath( CZipTreeItem& parent, PROGRESS_TYPE type);

public:
	CAddPathOperator( CZipTreeItem& parent, CProject& project, const std::wstring& name) 
		: CAddOperator( parent, project, name, true)
	{	
	}
	virtual CZipTreeItem* Do()
	{
		AddPath( m_parent, PT_ADDEND);
		AddToProject( m_parent.GetName(), m_name, true, COperatorManager::OT_ADDPATH);
		return NULL;
	}
};

class CAddNoInitOperator : public CAddPathOperator
{
public:
	CAddNoInitOperator( CZipTreeItem& parent, CProject& project)
		: CAddPathOperator( parent, project, L"") { m_name = GetWindowPath( parent, project); }
	virtual CZipTreeItem* Do()
	{
		m_parent.SetIsNoInit( false);
		AddPath( m_parent, PT_ADDEND_PROJECT);
		return NULL;
	}
};

class CAddNewPathOperator_Project : public CAddNewPathOperator
{
	std::wstring			m_fullpath;
public:
	CAddNewPathOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& name, const std::wstring& fullpath)
		: CAddNewPathOperator( root, project, name), m_fullpath( fullpath) {}
	virtual CZipTreeItem* Do()
	{
		const CZipTreeItem* parent = GetZipItemFromPath( m_fullpath, m_parent);
		if( !parent)
			return NULL;
		CZipTreeItem* res = AddNewPath( const_cast<CZipTreeItem&>(*parent) );
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, PT_ADDEND_PROJECT);
		return res;
	}
};

class CAddOperator_Project : public CAddOperator
{
	std::wstring			m_fullpath;		// 
public:
	CAddOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& name, bool bPath, const std::wstring& fullpath)
		: CAddOperator( root, project, name, bPath), m_fullpath( fullpath) {}
	virtual CZipTreeItem* Do()
	{
		CZipTreeItem* parent = const_cast<CZipTreeItem*>(GetZipItemFromPath( m_fullpath, m_parent));
		if( !parent)
			return NULL;
		m_project.GetMountManager().GetWindowPath( m_name);

		CZipTreeItem* res = AddPathOrFile( *parent );
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, PT_ADDEND_PROJECT);
		return res;
	}
};

class CAddPathOperator_Project : public CAddPathOperator
{
	std::wstring		m_fullpath;
public:
	CAddPathOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& name, const std::wstring& fullpath)
		: CAddPathOperator( root, project, name), m_fullpath( fullpath) {}
	virtual CZipTreeItem* Do()
	{
		CZipTreeItem* parent = const_cast<CZipTreeItem*>(GetZipItemFromPath( m_fullpath, m_parent));
		if( !parent)
			return NULL;
		m_project.GetMountManager().GetWindowPath( m_name);
		AddPath( const_cast<CZipTreeItem&>(*parent), PT_ADDEND_PROJECT);
		return NULL;
	}
};

