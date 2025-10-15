#include "stdafx.h"
#include "Operator.h"
#include <windows.h>


bool COperator::m_bStop = false;

void CAddNewPathOperator::SearchFiles( CZipTreeItem& parent)
{
	class CSearchFiles : public COperatorFindPath
	{
		virtual bool OnFind( const std::wstring& child, bool bPath)
		{
			if( COperator::m_bStop)
				return true;
			CAddNewPathOperator& operater = static_cast<CAddNewPathOperator&>( m_operater);
			operater.AddChild( m_parent, child, m_parent.GetMount(), bPath);
			return false;
		}
	public:
		CSearchFiles( CZipTreeItem& parent, CNeedProjectOperator& operater)
			: COperatorFindPath( parent, operater) {}
	};
		
	CSearchFiles search( parent, *this);
	search.DoFind();
}

// 添加目录或目录
CZipTreeItem* CAddNewPathOperator::AddPathOrFile( CZipTreeItem& parent, const std::wstring& path, bool bPath)
{
	std::wstring name = path;
	name.erase( 0, name.find_last_of( L'\\')+1);

	std::wstring mount;
	if( m_iswindowpath)
	{


		std::wstring temp = path;
		temp.erase( temp.find_last_of( L'\\'));
		mount = m_project.GetMountManager().GetMountStr( temp);
	}
	CZipTreeItem* pItem =  AddChild( parent, name, mount, bPath);
	if( pItem && m_iswindowpath)
	{
		// file is exist?
		if( bPath)
			pItem->SetIsFileNoExist( !PFS::CFileIO::IsDirectoryExisting( path));
		else 
			pItem->SetIsFileNoExist( !PFS::CFileIO::IsFileExisting( path));
		if( pItem->GetIsFileNoExist())
		{
			if( m_listener)
			{
				std::wstring strWarning;
				strWarning = L"该项不存在！ - " + path;
				m_listener->OnNotify( (WPARAM)strWarning.c_str(), PT_FILEERROR);
			}
		}
	}
	return pItem;
}

CZipTreeItem* CAddNewPathOperator::AddChild( CZipTreeItem& parent, const std::wstring& child, 
									const std::wstring& mount, bool bPath)
{
	std::wstring name = child;
	std::transform( name.begin(), name.end(), name.begin(), ::towlower);
	std::wstring sCur = parent.GetName().empty() ? name : parent.GetName() + name;
	if( bPath)
		sCur += L"/";
	if( m_listener)
		m_listener->OnNotify( (WPARAM)sCur.c_str(), COperator::PT_FILENAME);

	CZipTreeItem::CZipTreeItemList& children = parent.GetChild();
	CZipTreeItem temp;
	temp.SetName( sCur);
	temp.SetIsPath( bPath);
	if( std::find( children.begin(), children.end(), temp) != children.end())
	{
		if( m_listener)
		{
			std::wstring strWarning;
			strWarning = L"添加失败，该项已存在！ - " + sCur;
			m_listener->OnNotify( (WPARAM)strWarning.c_str(), PT_FILEERROR);
		}
		return NULL;
	}
	CZipTreeItem::CZipTreeItemList::iterator itor = std::lower_bound( children.begin(), children.end(), temp);
	// OutputDebugStringW(parent.GetName().c_str());
	OutputDebugStringW(temp.GetName().c_str());
	OutputDebugStringW(L"\n");

	CZipTreeItem::CZipTreeItemList::iterator insert = children.insert( itor, temp);
	insert->SetLevel( parent.GetLevel() + 1);
	//insert->SetRelateName( sRelate);
	insert->SetParent( &parent);
	insert->SetMount( mount + L"/" + child);
	// is check?
	insert->SetIsCheck( !m_project.GetIgnore().IsFileIgnore( sCur));

	// is pack?
	// 新添加的目录，缺省不再是Pack方式
	// 刘东学 2010-06-12 
	//if( bPath)
	//	insert->SetIsPack( true);
	if( !bPath && parent.GetIsPack())
		insert->SetIsPack( true);
	
	if( bPath)
	{
		if( m_iswindowpath)
		{
			if( insert->GetIsCheck())
				SearchFiles( *insert);
			else
			{
				// 先暂时不遍历忽略的目录提高速度
				insert->SetIsNoInit( true);
				insert->SetIsHasChild( IsHasChild( *insert));
			}
		}
	}
	else
	{
		//PFS::CEnv::AddFileMetaInfo( insert->GetMount(), PFS::CMetaInfo());

		insert->SetZipLevel( m_project.GetAttribute().GetZipLevel( sCur));
		insert->SetAttributes( m_project.GetAttribute().GetSetupLevel( sCur));
	}
	return &( *insert);
}

void CAddPathOperator::AddPath( CZipTreeItem& parent, PROGRESS_TYPE type)
{
	if( !PFS::CFileIO::IsDirectoryExisting( m_name))
	{
		if( m_listener)
		{
			std::wstring strWarning;
			strWarning = L"该项不存在！ - " + m_name;
			m_listener->OnNotify( (WPARAM)strWarning.c_str(), PT_FILEERROR);
		}
		return;
	}



	parent.SetMount( m_project.GetMountManager().GetMountStr( m_name));

	class CAddPath : public COperatorFindPath
	{
		CThreadListener* m_listener;
		PROGRESS_TYPE	m_type;
		virtual bool OnFind( const std::wstring& child, bool bPath)
		{
			if( COperator::m_bStop)
				return true;
			CAddNewPathOperator& operater = static_cast<CAddNewPathOperator&>( m_operater);
			CZipTreeItem* res = operater.AddChild( m_parent, child, m_parent.GetMount(), bPath);
			if( m_listener)
				m_listener->OnNotify( (WPARAM)res, m_type);
			return false;
		}
	public:
		CAddPath( CZipTreeItem& parent, CNeedProjectOperator& operater, CThreadListener* listener, PROGRESS_TYPE type)
			: COperatorFindPath( parent, operater), m_listener( listener), m_type( type) {}
	};
	
	CAddPath adder( parent, *this, m_listener, type);
	adder.DoFind();
}

void CSetZipLevelOperator::SetZipLevel( CZipTreeItem& item)
{
	if( m_bResource)
		item.SetZipLevel( m_value);
	else
		item.SetAttributes( m_value);

	if( item.GetIsPath())
	{
		CZipTreeItem::CZipTreeItemList& child = item.GetChild();
		for( CZipTreeItem::CZipTreeItemList::iterator itor = child.begin(); itor != child.end(); ++itor)
			SetZipLevel( *itor);
	}
	else
	{
		if( m_bResource)
			item.SetIsZipNoDef( m_value != m_project.GetAttribute().GetZipLevel( item.GetName()));
		else
			item.SetIsAttrNoDef( m_value != m_project.GetAttribute().GetSetupLevel( item.GetName()));
	}
}

bool CAddNewPathOperator::IsHasChild( CZipTreeItem& parent)
{
	class CHasChild : public COperatorFindPath
	{
		virtual bool OnFind( const std::wstring& child, bool bPath)
		{
			return true;
		}
	public:
		CHasChild( CZipTreeItem& parent, CNeedProjectOperator& operater)
			: COperatorFindPath( parent, operater) {}
	};

	CHasChild haschild( parent, *this);
	return haschild.DoFind();
}