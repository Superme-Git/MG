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

	std::wstring		m_name;				// ����

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

// ��Ʒ��
struct CShopItem : public CBaseItem
{
	static unsigned long m_maxShopID;		// �̳�id���ֵ

	unsigned long		m_shopID;			// �̳�id
	unsigned long		m_ID;				// id
	unsigned long		m_count;			// ����
	unsigned long		m_price;			// �۸�
	long				m_overTime;			// ʱ������
	unsigned long		m_returnScale;		// �������
	long				m_limitCount;		// ���Ƹ���
	unsigned long		m_discount;			// �ۿ�
	std::wstring		m_packPath;			// ͼ���
	unsigned long		m_iconIndex;		// ͼ���
	std::wstring		m_descript;			// ����
	BOOL				m_isSaling;			// �Ƿ��ϼ�
	unsigned long		m_flag;				// ��Ʒ���Ƽ�������

	CShopItem( const std::wstring& name) 
		: CBaseItem( name), m_ID(0), m_count(1), m_price(0), m_overTime(-1), m_returnScale(0), 
		m_limitCount(-1), m_discount(100), m_iconIndex(0), m_isSaling( true), m_flag(0) {}
};

// ÿ��������һ��name��list��ɣ�����д��һ��ģ����
template<typename T>
struct CBaseCategory : public CBaseItem
{
	typedef std::list<T>				CItemList;
	CItemList	m_items;

	CBaseCategory( const std::wstring& name) : CBaseItem( name) {}

	// ���
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
	
	// ���ƶ��Ľڵ�����item
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

	// ��һ�������еĶ�����ǰ�ƶ���
	// �ṩ������Ϊ(itemBeforeFirst,itemLast]��
	// ʵ�ʣ���ͨ����itemBeforeFirst����ɾ���ٲ��뵽itemLast֮��ʵ�ֵġ�
	// ����itemBeforeFirst�²���֮��Ķ����ָ�롣
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

	// ��һ�������еĶ��������ƶ���
	// �ṩ������Ϊ[itemFirst,itemAfterLast)��
	// ʵ�ʣ���ͨ����itemAfterLast����ɾ���ٲ��뵽itemFirst֮ǰʵ�ֵġ�
	// ����itemAfterLast�²���֮��Ķ����ָ�롣
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
				iNext++;	// ����Ҫ����2��
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
	//	iNext++;	// ����Ҫ����2��
	//	m_items.erase( i);
	//	iNext = m_items.insert( iNext, temp);
	//	return &(*iNext);
	//}

	//// �ƶ�λ��
	//// insertName Ҫ���������֮ǰ
	//// itemName Ҫ�ƶ��Ľڵ�
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


// ������
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