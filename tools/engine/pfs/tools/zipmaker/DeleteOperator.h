#include "Operator.h"


// É¾³ý
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
	virtual CZipTreeItem* Do()
	{
		return DeleteTreeItem( m_parent, true);
	}
};

class CDeleteOperator_Project : public CDeleteOperator
{
	std::wstring		m_fullpath;
public:
	CDeleteOperator_Project( CZipTreeItem& root, CProject& project, const std::wstring& fullpath) 
		: CDeleteOperator( root, project), m_fullpath( fullpath) {}
	virtual CZipTreeItem* Do()
	{
		CZipTreeItem* deleteitem = const_cast<CZipTreeItem*>(GetZipItemFromPath( m_fullpath, m_parent));
		if( !deleteitem)
			return NULL;
		return DeleteTreeItem( *deleteitem, false);
	}
};


