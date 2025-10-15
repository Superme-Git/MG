#include "StdAfx.h"
#include "ShopItemManager.h"
#include <algorithm>

unsigned long CShopItem::m_maxShopID = 1;

bool CShopItemManager::Load( const std::wstring& filename)
{
	Close();

	XMLIO::CFileReader reader;
	if( XMLIO::EC_SUCCESS != reader.OpenNativeFile( filename))
	{
		return false;
	}
	XMLIO::CINode root;
	if( !reader.GetRootNode( root))
	{
		return false;
	}

	XMLIO::CINode child;
	int bigcount = root.GetChildrenCount();
	for( int i=0; i<bigcount; i++)
	{
		if( !root.GetChildAt( i, child))
		{
			continue;
		}
		LoadBigCategory( child);
	}
	return true;
}

bool CShopItemManager::LoadBigCategory( const XMLIO::CINode& node)
{
	std::wstring name;
	node.GetAttribute( L"name", name);
	if( name.empty())
	{
		return false;
	}
	CBigCategory* bcp = AddItem( name);
	if( NULL == bcp)
	{
		return false;
	}
	XMLIO::CINode child;
	int count = node.GetChildrenCount();
	for( int i=0; i<count; i++)
	{
		if( !node.GetChildAt( i, child))
		{
			continue;
		}
		LoadSmallCategory( child, *bcp);
	}
	return true;
}

bool CShopItemManager::LoadSmallCategory( const XMLIO::CINode& node,CBigCategory& bc)
{
	std::wstring name;
	node.GetAttribute( L"name", name);
	if( name.empty())
	{
		return false;
	}
	CSmallCategory* scp = bc.AddItem( name);
	if( NULL == scp)
	{
		return false;
	}
	XMLIO::CINode child;
	int count = node.GetChildrenCount();
	for( int i=0; i<count; i++)
	{
		if( !node.GetChildAt( i, child))
		{
			continue;
		}
		LoadShopItem( child, *scp);
	}
	return true;
}

bool CShopItemManager::LoadShopItem( const XMLIO::CINode& node, CSmallCategory& sc)
{
	std::wstring name;
	node.GetAttribute( L"name", name);
	if( name.empty())
	{
		return false;
	}
	CShopItem* scp = sc.AddItem( name);
	if( NULL == scp)
	{
		return false;
	}
	
	scp->m_ID = node.GetAttributeUnsignedInteger( L"id");
	scp->m_count = node.GetAttributeUnsignedInteger( L"count", 1);
	scp->m_price = node.GetAttributeUnsignedInteger( L"price");
	scp->m_overTime = node.GetAttributeUnsignedInteger( L"overtime");
	scp->m_returnScale = node.GetAttributeUnsignedInteger( L"returnscale");
	scp->m_limitCount = node.GetAttributeInteger( L"limitcount", -1);
	scp->m_discount = node.GetAttributeUnsignedInteger( L"discount", 100);
	node.GetAttribute( L"packpath", scp->m_packPath);
	scp->m_iconIndex = node.GetAttributeUnsignedInteger( L"iconindex");
	node.GetAttribute( L"descript", scp->m_descript);
	scp->m_isSaling = node.GetAttributeInteger( L"issaling");
	scp->m_flag = node.GetAttributeUnsignedInteger( L"flag");
	
	scp->m_shopID = node.GetAttributeUnsignedInteger( L"shopid");
	if( scp->m_shopID >= CShopItem::m_maxShopID)
	{
		CShopItem::m_maxShopID = scp->m_shopID;
		CShopItem::m_maxShopID++;
	}
	return true;
}

bool CShopItemManager::Save( const std::wstring& filename)
{
	XMLIO::CFileWriter writter;
	XMLIO::CONode root;
	if( !writter.GetRootNode( root))
	{
		return false;
	}
	XMLIO::CONode child;
	for( CBigCategoryList::const_iterator i=m_items.begin(), e=m_items.end(); i != e; ++i)
	{
		if( !root.CreateSubElement( L"big", child))
		{
			continue;
		}
		SaveBigCategory( child, *i);
	}
	return ( XMLIO::EC_SUCCESS == writter.SaveNative( filename));
}

bool CShopItemManager::SaveBigCategory( XMLIO::CONode& node, const CBigCategory& bc)
{
	if( !node.SetAttribute( L"name", bc.m_name))
	{
		return false;
	}
	XMLIO::CONode child;
	for( CSmallCategoryList::const_iterator i=bc.m_items.begin(), e=bc.m_items.end(); i != e; ++i)
	{
		if( !node.CreateSubElement( L"small", child))
		{
			continue;
		}
		SaveSmallCategory( child, *i);
	}
	return true;
}

bool CShopItemManager::SaveSmallCategory( XMLIO::CONode& node, const CSmallCategory& bc)
{
	if( !node.SetAttribute( L"name", bc.m_name))
	{
		return false;
	}
	XMLIO::CONode child;
	for( CShopItemList::const_iterator i=bc.m_items.begin(), e=bc.m_items.end(); i != e; ++i)
	{
		if( !node.CreateSubElement( L"item", child))
		{
			continue;
		}
		SaveShopItem( child, *i);
	}
	return true;
}

bool CShopItemManager::SaveShopItem( XMLIO::CONode& node, const CShopItem& si)
{
	node.SetAttribute( L"name", si.m_name);
	node.SetAttribute( L"id", (int)si.m_ID);
	node.SetAttribute( L"count", (int)si.m_count);
	node.SetAttribute( L"price", (int)si.m_price);
	node.SetAttribute( L"overtime", (int)si.m_overTime);
	node.SetAttribute( L"returnscale", (int)si.m_returnScale);
	node.SetAttribute( L"limitcount", si.m_limitCount);
	node.SetAttribute( L"discount", (int)si.m_discount);
	node.SetAttribute( L"packpath", si.m_packPath);
	node.SetAttribute( L"iconindex", (int)si.m_iconIndex);
	node.SetAttribute( L"descript", si.m_descript);
	node.SetAttribute( L"issaling", si.m_isSaling);
	node.SetAttribute( L"shopid", (int)si.m_shopID);
	node.SetAttribute( L"flag", (int)si.m_flag);
	return true;
}