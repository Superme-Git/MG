#pragma once
#include <string>
#include <list>
#include <vector>
#include <map>
#include "../pfs/pfs2.h"
#include "../xmlio/xmlio.h"

struct CBaseItem
{
	virtual ~CBaseItem() {}

	std::wstring		m_name;				// 名字

	CBaseItem( const std::wstring& name ) : m_name( name) {}

	//bool operator==( const CBaseItem& right)
	//{
	//	return m_name == right.m_name;
	//}
};

enum
{
	FLAG_NEW = 0x00000001,
	FLAG_COMMEND = 0x00000002,
	FLAG_PROMOTION = 0x00000004,
};

// 物品类
struct CShopItem : public CBaseItem
{
	static unsigned long m_maxShopID;		// 商城id最大值

	unsigned long		m_shopID;			// 商城id
	unsigned long		m_ID;				// id
	unsigned long		m_count;			// 个数
	unsigned long		m_price;			// 价格
	long				m_overTime;			// 时间限制
	unsigned long		m_returnScale;		// 返点比例
	long				m_limitCount;		// 限制个数
	unsigned long		m_discount;			// 折扣
	std::wstring		m_packPath;			// 图标包
	unsigned long		m_iconIndex;		// 图标号
	std::wstring		m_descript;			// 描述
	BOOL				m_isSaling;			// 是否上架
	unsigned long		m_flag;				// 新品、推荐、促销

	CShopItem( const std::wstring& name) 
		: CBaseItem( name), m_ID(0), m_count(1), m_price(0), m_overTime(-1), m_returnScale(0), 
		m_limitCount(-1), m_discount(100), m_iconIndex(0), m_isSaling( true), m_flag(0) {}
};

// 每个分类由一个name和list组成，所以写成一个模板类
template<typename T>
struct CBaseCategory : public CBaseItem
{
	typedef std::list<T>				CItemList;
	CItemList	m_items;

	CBaseCategory( const std::wstring& name) : CBaseItem( name) {}

	// 添加
	T* AddItem( const std::wstring& itemName)
	{
		m_items.push_back( T( itemName));
		T& item = m_items.back();
		return &item;
	}

	T* AddItem( const T& itemAdd)
	{
		m_items.push_back( itemAdd);
		T& item = m_items.back();
		return &item;
	}
	
	// 在制定的节点后插入item
	T* InsertItem( const T* pos, const std::wstring& itemAdd)
	{
		if( pos )
		{
			for( CItemList::iterator i=m_items.begin(), e=m_items.end(); i != e; ++i)
			{
				if( pos == &(*i))
				{
					return &(*(m_items.insert(++i, T(itemAdd))));
				}
			}
		}
		return NULL;
	}

	bool DeleteItem( T* item)
	{
		for( CItemList::iterator i=m_items.begin(), e=m_items.end(); i != e; ++i)
		{
			if( item == &(*i))
			{
				m_items.erase( i);
				return true;
			}
		}
		return false;
	}

	size_t GetItemCount() const
	{
		return m_items.size();
	}

	bool IsEmpty() const
	{
		return m_items.empty();
	}

	// 将一个区间中的对象往前移动。
	// 提供的区间为(itemBeforeFirst,itemLast]。
	// 实际，是通过将itemBeforeFirst单独删除再插入到itemLast之后实现的。
	// 返回itemBeforeFirst新插入之后的对象的指针。
	T* MoveRangeUp( T* itemBeforeFirst, T* itemLast )
	{
		for( CItemList::iterator i=m_items.begin(), e=m_items.end(); i != e; ++i)
		{
			if( itemBeforeFirst == &(*i))
			{
				CItemList::iterator j=i;
				for( j ++; j != e; ++j)
				{
					if( itemLast == &(*j))
					{
						T copyOfItemErased = *i;
						m_items.erase( i );
						j ++;
						CItemList::iterator v = m_items.insert( j, copyOfItemErased );
						return &(*v);
					}
				}

				break;
			}
		}

		return NULL;
	}

	// 将一个区间中的对象往后移动。
	// 提供的区间为[itemFirst,itemAfterLast)。
	// 实际，是通过将itemAfterLast单独删除再插入到itemFirst之前实现的。
	// 返回itemAfterLast新插入之后的对象的指针。
	T* MoveRangeDown( T* itemFirst, T* itemAfterLast )
	{
		for( CItemList::iterator i=m_items.begin(), e=m_items.end(); i != e; ++i)
		{
			if( itemFirst == &(*i))
			{
				CItemList::iterator j=i;
				for( j ++; j != e; ++j)
				{
					if( itemAfterLast == &(*j))
					{
						T copyOfItemErased = *itemAfterLast;
						m_items.erase( j );
						CItemList::iterator v = m_items.insert( i, copyOfItemErased );
						return &(*v);
					}
				}

				break;
			}
		}

		return NULL;
	}

	T* MoveUp( T* item)
	{
		for( CItemList::iterator i=m_items.begin(), e=m_items.end(); i != e; ++i)
		{
			if( item == &(*i))
			{
				if( i == m_items.begin())
				{
					return NULL;
				}
				T temp = *i;
				CItemList::iterator iLast = i;
				iLast--;
				m_items.erase( i);
				iLast = m_items.insert( iLast, temp);
				return &(*iLast);
			}
		}

		return NULL;
	}

	T* MoveDown( T* item)
	{
		for( CItemList::iterator i=m_items.begin(), e=m_items.end(); i != e; ++i)
		{
			if( item == &(*i))
			{
				CItemList::iterator iNext = m_items.end();
				iNext--;
				if( i == iNext)
				{
					return NULL;
				}
				T temp = *i;
				iNext = i;
				iNext++;
				iNext++;	// 这里要右移2次
				m_items.erase( i);
				iNext = m_items.insert( iNext, temp);
				return &(*iNext);
			}
		}
		return NULL;
	}

	//bool DeleteItem( const std::wstring& itemName)
	//{
	//	CItemList::iterator i = std::find( m_items.begin(), m_items.end(), itemName);
	//	if( i == m_items.end())
	//	{
	//		return false;
	//	}
	//	m_items.erase( i);
	//	return true;
	//}

	//T* MoveUp( const std::wstring& itemName)
	//{
	//	CItemList::iterator i = std::find( m_items.begin(), m_items.end(), itemName);
	//	if( i == m_items.end())
	//	{
	//		return NULL;
	//	}
	//	if( i == m_items.begin())
	//	{
	//		return NULL;
	//	}
	//	T temp = *i;
	//	CItemList::iterator iLast = i;
	//	iLast--;
	//	m_items.erase( i);
	//	iLast = m_items.insert( iLast, temp);
	//	return &(*iLast);
	//}

	//T* MoveDown( const std::wstring& itemName)
	//{
	//	CItemList::iterator i = std::find( m_items.begin(), m_items.end(), itemName);
	//	if( i == m_items.end())
	//	{
	//		return NULL;
	//	}
	//	CItemList::iterator iNext = m_items.end();
	//	iNext--;
	//	if( i == iNext)
	//	{
	//		return NULL;
	//	}
	//	T temp = *i;
	//	iNext = i;
	//	iNext++;
	//	iNext++;	// 这里要右移2次
	//	m_items.erase( i);
	//	iNext = m_items.insert( iNext, temp);
	//	return &(*iNext);
	//}

	//// 移动位置
	//// insertName 要插入的名字之前
	//// itemName 要移动的节点
	//bool MoveTo( const std::wstring& insertName, const std::wstring& itemName)
	//{
	//	CItemList::iterator i = std::find( m_items.begin(), m_items.end(), itemName);
	//	if( i == m_items.end())
	//	{
	//		return false;
	//	}
	//	T temp = *i;
	//	m_items.erase( i);
	//	CItemList::iterator iInsert = std::find( m_items.begin(), m_items.end(), insertName);
	//	m_items.insert( iInsert, temp);
	//	return true;
	//}
};

typedef std::list<CShopItem>			CShopItemList;

typedef CBaseCategory<CShopItem>		CSmallCategory;

typedef std::list<CSmallCategory>		CSmallCategoryList;

typedef CBaseCategory<CSmallCategory>	CBigCategory;

typedef std::list<CBigCategory>			CBigCategoryList;

typedef CBaseCategory<CBigCategory>		CBigCategoryManager;


// 管理类
class CShopItemManager : public CBigCategoryManager
{

	bool LoadBigCategory( const XMLIO::CINode& node);

	bool LoadSmallCategory( const XMLIO::CINode& node,CBigCategory& bc);

	bool LoadShopItem( const XMLIO::CINode& node, CSmallCategory& bc);

	bool SaveBigCategory( XMLIO::CONode& node, const CBigCategory& bc);

	bool SaveSmallCategory( XMLIO::CONode& node, const CSmallCategory& bc);

	bool SaveShopItem( XMLIO::CONode& node, const CShopItem& si);

public:
	CShopItemManager() : CBigCategoryManager( L"") {}

	bool Load( const std::wstring& filename);

	bool Save( const std::wstring& filename);

	void Close() { m_items.clear(); }
};