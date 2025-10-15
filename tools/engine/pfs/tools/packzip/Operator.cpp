#include "StdAfx.h"
#include "Operator.h"

void Data2AnsiString( PFS::CDataBlock& in, std::string& out)
{
	static const std::string TABLE = "0123456789abcdef";

	out.clear();
	const char* data = (const char*)in.GetData();
	for( size_t i = 0; i < in.GetSize(); i ++)
	{
		const char& c = data[i];
		out += TABLE[ (c >> 4) & 0x0f];
		out += TABLE[ c & 0x0f];
	}
}


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
			strWarning = L"���ʧ�ܣ������Ѵ��ڣ� - " + sCur;
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
	// ����ӵ�Ŀ¼��ȱʡ������Pack��ʽ
	// ����ѧ 2010-06-12 
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
				// ����ʱ���������Ե�Ŀ¼����ٶ�
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

// ���Ŀ¼��Ŀ¼
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
				strWarning = L"������ڣ� - " + path;
				m_listener->OnNotify( (WPARAM)strWarning.c_str(), PT_FILEERROR);
			}
		}
	}
	return pItem;
}

void CAddPathOperator::AddPath( CZipTreeItem& parent, PROGRESS_TYPE type)
{
	if( !PFS::CFileIO::IsDirectoryExisting( m_name))
	{
		if( m_listener)
		{
			std::wstring strWarning;
			strWarning = L"������ڣ� - " + m_name;
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


void CRefreshCheckOperator::RefreshIsCheck( CZipTreeItem& parent)
{
	CZipTreeItem::CZipTreeItemList& children = parent.GetChild();
	for( CZipTreeItem::CZipTreeItemList::iterator itor = children.begin(); itor!=children.end(); ++itor)
	{
		if( itor->GetIsSetCheck() && !itor->GetIsCheck())
			itor->SetIsCheck( false);
		else
			itor->SetIsCheck( !m_project.GetIgnore().IsFileIgnore( itor->GetName()));

		if( itor->GetIsPath())
			RefreshIsCheck( *itor);
	}
}

void CRefreshZipLevelOperator::RefreshZipLevel( CZipTreeItem& parent)
{
	CZipTreeItem::CZipTreeItemList& children = parent.GetChild();
	for( CZipTreeItem::CZipTreeItemList::iterator itor = children.begin(); itor!=children.end(); ++itor)
	{
		itor->SetIsZipNoDef( false);
		itor->SetIsAttrNoDef( false);
		if( itor->GetIsPath())
		{
			RefreshZipLevel( *itor);
		}
		else
		{
			itor->SetZipLevel( m_project.GetAttribute().GetZipLevel( itor->GetName()));
			itor->SetAttributes( m_project.GetAttribute().GetSetupLevel( itor->GetName()));
		}
	}
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

bool CSetZipLevelOperator::RefreshPathIsNoDef( CZipTreeItem& parent)
{
	if( parent.GetIsPath())
	{
		bool bNoDef = false;
		CZipTreeItem::CZipTreeItemList& child = parent.GetChild();
		for( CZipTreeItem::CZipTreeItemList::iterator itor = child.begin(); itor != child.end(); ++itor)
		{
			if( RefreshPathIsNoDef( *itor))
			{
				bNoDef = true;
				//break;
			}
		}
		if( m_bResource)
			parent.SetIsZipNoDef( bNoDef);
		else
			parent.SetIsAttrNoDef( bNoDef);
		return bNoDef;
	}
	return m_bResource ? parent.GetIsZipNoDef() : parent.GetIsAttrNoDef();
}