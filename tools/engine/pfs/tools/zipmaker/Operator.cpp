#include "StdAfx.h"
#include "Operator.h"
#include "AddOperator.h"

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


void CSetCheckedOperator::AddNoInitItem( CZipTreeItem& parent)
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