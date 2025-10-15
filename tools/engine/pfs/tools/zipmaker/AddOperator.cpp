#include "StdAfx.h"
#include "AddOperator.h"

CZipTreeItem* CAddNewPathOperator::Do()
{
	// ��ӿ�Ŀ¼������Ҫ�ݹ飩
	CZipTreeItem* res = AddNewPath( m_parent );
	if( res )
	{
		AddToProject( m_parent.GetName(), m_name, true, COperatorManager::OT_ADDNEW );
	}

	if( m_listener)
		m_listener->OnNotify( (WPARAM)res, PT_ADDEND );

	return res;
}

// ��ӿ�Ŀ¼
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
			return true; // ֹͣö��

		CZipTreeItem* res = m_operater.AddChild( m_parent, child, m_parent.GetMount(), bPath);
		if( m_listener)
			m_listener->OnNotify( (WPARAM)res, m_type);

		return false; // ����ö��
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
			return true; // ֹͣö��

		CZipTreeItem* res = m_operater.AddChild( m_parent, child, m_parent.GetMount(), bPath);

		// CSearchFiles��CAddPath��������ǣ������Ƿ���Ҫ��ͨ�档
		// �Ѿ�����ȷ�ϵ�һ����ǣ����������ͨ�棬�ᵼ��������ظ���ӣ�����λ��Ҳ���ԡ�
		// ����ϸ�о�������Ϊʲô��

		return false; // ����ö��
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
	// ����ӵ�Ŀ¼��ȱʡ������Pack��ʽ
	// ����ѧ 2010-06-12 
	//if( bPath)
	//	insert->SetIsPack( true);
	if( !bPath && parent.GetIsPack())
		insert->SetIsPack( true);
	
	if( bPath )
	{
		// ��ӵ�·����windows·��
		// ��windows·������Ҫ�ݹ�Ĵ�����Ŀ¼������Ŀ¼���ļ���Ŀ¼���ӵ����ϡ�
		if( insert->GetIsCheck() )
		{
			CSearchFiles adder( *insert, *this );
			adder.DoFind();
		}
		else
		{
			// ����ʱ���������Ե�Ŀ¼����ٶ�
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
		// ��ӵ����ļ����趨ZipLevel��SetupLevel���ԡ�
		insert->SetZipLevel( m_project.GetAttribute().GetZipLevel( sCur));
		insert->SetAttributes( m_project.GetAttribute().GetSetupLevel( sCur));
	}

	return &( *insert);
}

// ���Ŀ¼���ļ�������ļ���Ŀ¼��������ϣ����Ƕ���Ϊ�������ӽ����ڣ�
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

// ���Ŀ¼(ö��Ŀ¼�µ����ݣ���Ŀ¼���ļ�����ӵ�ָ���ĸ�����ϣ�Ŀ¼��������ӵ��������)
void CAddPathOperator::AddPath( CZipTreeItem& parent, PROGRESS_TYPE type)
{
	// �ļ�ʵ��Ŀ¼�������
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

	// �޸ĸ�Ŀ¼��mount�㡣
	parent.SetMount( m_project.GetMountManager().GetMountStr( m_name));
	
	// ����ԴĿ¼�µ����ݣ��ӵ�parent�ϡ�
	CAddPath adder( parent, *this, m_listener, type );

	adder.DoFind();
}
