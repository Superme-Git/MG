#include "StdAfx.h"
#include "AddOperator.h"

CZipTreeItem* CAddNewPathOperator::Do()
{
	// 添加空目录（不需要递归）
	CZipTreeItem* res = AddNewPath( m_parent );
	if( res )
	{
		AddToProject( m_parent.GetName(), m_name, true, COperatorManager::OT_ADDNEW );
	}

	if( m_listener)
		m_listener->OnNotify( (WPARAM)res, PT_ADDEND );

	return res;
}

// 添加空目录
CZipTreeItem* CAddNewPathOperator::AddNewPath( CZipTreeItem& parent)
{
	std::wstring name = m_name;
	name.erase( 0, name.find_last_of( L'\\') + 1 );
	std::transform( name.begin(), name.end(), name.begin(), ::towlower);
	if( !parent.GetName().empty() )
		name = parent.GetName() + name;
	name += L"/";

	if( m_listener)
		m_listener->OnNotify( (WPARAM)name.c_str(), COperator::PT_FILENAME);

	CZipTreeItem::CZipTreeItemList& children = parent.GetChild();
	CZipTreeItem temp;
	temp.SetName( name );
	temp.SetIsPath( true );
	if( std::find( children.begin(), children.end(), temp) != children.end())
	{
		if( m_listener)
		{
			std::wstring strWarning;			
			strWarning = L"Add failed. The item with same name already exists - " + name;
			m_listener->OnNotify( (WPARAM)strWarning.c_str(), PT_FILEERROR);
		}
		return NULL;
	}
	
	CZipTreeItem::CZipTreeItemList::iterator itor = std::lower_bound( children.begin(), children.end(), temp);
	OutputDebugStringW(temp.GetName().c_str());	OutputDebugStringW(L"\n");

	CZipTreeItem::CZipTreeItemList::iterator insert = children.insert( itor, temp);
	insert->SetLevel( parent.GetLevel() + 1 );	
	insert->SetParent( &parent);
	insert->SetMount( L"/" + name );
	// is check?
	insert->SetIsCheck( !m_project.GetIgnore().IsFileIgnore( name));

	return &( *insert);
}

class CAddPath : public CFindPath
{
	CZipTreeItem&				m_parent;
	CAddOperator&				m_operater;
	CThreadListener*			m_listener;
	COperator::PROGRESS_TYPE	m_type;

	virtual bool OnFind( const std::wstring& child, bool bPath)
	{
		if( COperator::m_bStop)
			return true; // 停止枚举

		CZipTreeItem* res = m_operater.AddChild( m_parent, child, m_parent.GetMount(), bPath);
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, m_type);

		return false; // 继续枚举
	}

public:
	CAddPath( CZipTreeItem& parent, CAddOperator& operater, 
		CThreadListener* listener, COperator::PROGRESS_TYPE type)
		: CFindPath(L"")
		, m_parent( parent )
		, m_operater( operater )
		, m_listener( listener )
		, m_type( type )
	{
		m_basepath = CNeedProjectOperator::GetWindowPath( m_parent, m_operater.GetProject());
	}
};

class CSearchFiles : public CFindPath
{
	CZipTreeItem&				m_parent;
	CAddOperator&				m_operater;
	CThreadListener*			m_listener;
	COperator::PROGRESS_TYPE	m_type;

	virtual bool OnFind( const std::wstring& child, bool bPath)
	{
		if( COperator::m_bStop)
			return true; // 停止枚举

		CZipTreeItem* res = m_operater.AddChild( m_parent, child, m_parent.GetMount(), bPath);

		// CSearchFiles和CAddPath的区别就是，这里是否需要发通告。
		// 已经可以确认的一点就是，这里如果发通告，会导致树结点重复添加，而且位置也不对。
		// 待仔细研究，这是为什么。

		return false; // 继续枚举
	}

public:
	CSearchFiles( CZipTreeItem& parent, CAddOperator& operater )
		: CFindPath(L"")
		, m_parent( parent )
		, m_operater( operater )
	{
		m_basepath = CNeedProjectOperator::GetWindowPath( m_parent, m_operater.GetProject());
	}
};

bool IsFolderNotEmpty( const std::wstring& basepath )
{
	class CHasChild : public CFindPath
	{
		virtual bool OnFind( const std::wstring&, bool)
		{
			return true;
		}

	public:
		CHasChild( const std::wstring& basepath )
			: CFindPath( basepath ) 
		{
		}
	};
	
	CHasChild haschild( basepath );

	return haschild.DoFind();
}

CZipTreeItem* CAddOperator::AddChild( CZipTreeItem& parent, const std::wstring& child, 
									const std::wstring& mount, bool bPath)
{
	std::wstring name = child;
	std::transform( name.begin(), name.end(), name.begin(), ::towlower );
	std::wstring sCur = parent.GetName().empty() ? name : parent.GetName() + name;
	if( bPath )
		sCur += L"/";
	if( m_listener )
		m_listener->OnNotify( (WPARAM)sCur.c_str(), COperator::PT_FILENAME);

	CZipTreeItem::CZipTreeItemList& children = parent.GetChild();
	CZipTreeItem temp;
	temp.SetName( sCur );
	temp.SetIsPath( bPath );

	if( std::find( children.begin(), children.end(), temp) != children.end() )
	{
		if( m_listener )
		{
			std::wstring strWarning;
			strWarning = L"Add failed. The item with same name already exists - " + sCur;
			m_listener->OnNotify( (WPARAM)strWarning.c_str(), PT_FILEERROR);
		}
		return NULL;
	}

	CZipTreeItem::CZipTreeItemList::iterator itor = std::lower_bound( children.begin(), children.end(), temp);
	OutputDebugStringW(temp.GetName().c_str());	OutputDebugStringW(L"\n");

	std::wstring childMount =  mount + L"/" + child;
	CZipTreeItem::CZipTreeItemList::iterator insert = children.insert( itor, temp);
	insert->SetLevel( parent.GetLevel() + 1);
	insert->SetParent( &parent);
	insert->SetMount( childMount );
	insert->SetIsCheck( !m_project.GetIgnore().IsFileIgnore( sCur)); // is check?

	// is pack?
	// 新添加的目录，缺省不再是Pack方式
	// 刘东学 2010-06-12 
	//if( bPath)
	//	insert->SetIsPack( true);
	if( !bPath && parent.GetIsPack())
		insert->SetIsPack( true);
	
	if( bPath )
	{
		// 添加的路径是windows路径
		// 是windows路径，需要递归的处理子目录，将子目录中文件和目录都加到树上。
		if( insert->GetIsCheck() )
		{
			CSearchFiles adder( *insert, *this );
			adder.DoFind();
		}
		else
		{
			// 先暂时不遍历忽略的目录提高速度
			insert->SetIsNoInit( true);

			// std::wstring basepath = CNeedProjectOperator::GetWindowPath(  *insert, GetProject() );
			std::wstring basepath = childMount;
			std::replace( basepath.begin() ++, basepath.end(), L'/', L'\\');
			GetProject().GetMountManager().GetWindowPath( basepath );

			insert->SetIsHasChild( IsFolderNotEmpty( basepath ) );
		}
	}
	else
	{
		// 添加的是文件，设定ZipLevel和SetupLevel属性。
		insert->SetZipLevel( m_project.GetAttribute().GetZipLevel( sCur));
		insert->SetAttributes( m_project.GetAttribute().GetSetupLevel( sCur));
	}

	return &( *insert);
}

// 添加目录或文件（添加文件或目录到父结点上，他们都作为父结点的子结点存在）
CZipTreeItem* CAddOperator::AddPathOrFile( CZipTreeItem& parent )
{
	const std::wstring& path = m_name;
	size_t pos = path.find_last_of( L'\\') + 1;
	std::wstring name = path.substr(pos);
	std::wstring dir = path.substr(0, pos);

	std::wstring mount = m_project.GetMountManager().GetMountStr( dir );
	CZipTreeItem* pItem = AddChild( parent, name, mount, m_bPath );

	if( pItem )
	{
		// file exists?
		if( m_bPath)
			pItem->SetIsFileNoExist( !PFS::CFileIO::IsDirectoryExisting( path));
		else 
			pItem->SetIsFileNoExist( !PFS::CFileIO::IsFileExisting( path));

		if( pItem->GetIsFileNoExist() )
		{
			if( m_listener )
			{
				std::wstring strWarning;
				strWarning = L"The item does not exists - " + path;
				m_listener->OnNotify( (WPARAM)strWarning.c_str(), PT_FILEERROR);
			}
		}
	}

	return pItem;
}

// 添加目录(枚举目录下的内容（子目录或文件）添加到指定的父结点上，目录名本身不添加到父结点上)
void CAddPathOperator::AddPath( CZipTreeItem& parent, PROGRESS_TYPE type)
{
	// 文件实体目录必须存在
	if( !PFS::CFileIO::IsDirectoryExisting( m_name))
	{
		if( m_listener)
		{
			std::wstring strWarning;
			strWarning = L"The item does not exists - " + m_name;
			m_listener->OnNotify( (WPARAM)strWarning.c_str(), PT_FILEERROR);
		}
		return;
	}

	// 修改父目录的mount点。
	parent.SetMount( m_project.GetMountManager().GetMountStr( m_name));
	
	// 遍历源目录下的内容，加到parent上。
	CAddPath adder( parent, *this, m_listener, type );

	adder.DoFind();
}
