#pragma once

#include <set>
#include <assert.h>

namespace DATA {

class CBaseTypeDefine
{
public:
	typedef std::set<std::wstring> CDefTypeMap;
private:
	CDefTypeMap		m_rawType;

	CDefTypeMap		m_containerType;
	CDefTypeMap		m_classType;

	CBaseTypeDefine()
	{
		m_classType.insert( L"string");
		m_containerType.insert( L"list");
		m_containerType.insert( L"vector");
		m_containerType.insert( L"map");

		m_rawType.insert( L"int");
		m_rawType.insert( L"__int64");
		m_rawType.insert( L"short");
		m_rawType.insert( L"char");

		m_rawType.insert( L"unsinged int");
		m_rawType.insert( L"unsinged __int64");
		m_rawType.insert( L"unsinged short");
		m_rawType.insert( L"unsinged char");

		m_rawType.insert( L"float");
		m_rawType.insert( L"double");
	}
	~CBaseTypeDefine() {}
public:
	static CBaseTypeDefine& GetInstance()
	{
		static CBaseTypeDefine	btd;
		return btd;
	}
public:
	bool IsRawType( const std::wstring& type)
	{
		return m_rawType.find( type) != m_rawType.end();
	}
	bool IsClassType( const std::wstring& type)
	{
		if( m_classType.find( type) != m_classType.end())
			return true;
		else
			return IsContainerType( type);
	}
	bool IsContainerType( const std::wstring& type)
	{
		return m_containerType.find( type) != m_containerType.end();
	}
	void GetDefinedTypes( CDefTypeMap& map)
	{
		map.insert( m_classType.begin(), m_classType.end());
		map.insert( m_rawType.begin(), m_rawType.end());
		map.insert( m_containerType.begin(), m_containerType.end());
	}
};

bool SortDataItemList( CItemList& ildata)
{
	CItemList		ilsrc;

	CBaseTypeDefine::CDefTypeMap	mapType;
	
	CBaseTypeDefine::GetInstance().GetDefinedTypes( mapType);
	ildata.swap( ilsrc);

	for( CItemList::iterator it = ilsrc.begin(); it != ilsrc.end(); )
	{
		const CItemPointer& ip = *it;
		if( CEnumType* et = dynamic_cast<CEnumType*>( ip.GetBase()))
		{
			ildata.push_back( ip);
			if( !et->GetName().empty())
				mapType.insert( et->GetName());
			it = ilsrc.erase( it);
		}
		else
		{
			++ it;
		}
	} // for( CItemList::const_iterator it = ilsrc.begin(); it != ilsrc.end(); )

	while( !ilsrc.empty())
	{
		size_t sCount = ilsrc.size();
		for( CItemList::iterator it = ilsrc.begin(); it != ilsrc.end(); ++ it)
		{
			const CItemPointer& ip = *it;
			assert( NULL == dynamic_cast<CEnumType*>( ip.GetBase()));

			if( CClass* pc = dynamic_cast<CClass*>( ip.GetBase()))
			{
				const std::wstring& name = pc->GetParent();
				if( !name.empty() && mapType.find( name) == mapType.end())
					continue;

				bool  bHasUnDef = false;
				const CVariantList& vl = pc->GetVariantList();
				for( CVariantList::const_iterator itvl = vl.begin(); itvl != vl.end(); ++ itvl)
				{
					const Variant& var = *itvl;
					if( mapType.find( var.strType) == mapType.end())
					{
						bHasUnDef = true;
						break;
					}
					if( !var.strKey.empty() && mapType.find( var.strKey) == mapType.end())
					{
						bHasUnDef = true;
						break;
					}
					if( !var.strValue.empty() && mapType.find( var.strValue) == mapType.end())
					{
						bHasUnDef = true;
						break;
					}

				}
				if( bHasUnDef)
					continue;
				mapType.insert( pc->GetName());
				ildata.push_back( ip);
				ilsrc.erase( it);
				break;
			} // if( CClass* pc = dynamic_cast<CClass*>( ip.GetBase()))
		} // for( CItemList::const_iterator it = ilsrc.begin(); it != ilsrc.end(); ++ it)

		if( ilsrc.size() == sCount)
		{
			wprintf( L"%d objects cannot sort:\n", sCount);
			for( CItemList::const_iterator it = ilsrc.begin(); it != ilsrc.end(); ++ it)
			{
				const CItemPointer& ip = *it;
				wprintf( L"object %s\n", ip->GetName().c_str());
			}
			wprintf( L"\n");
			return false;
		}
	}
	return true;
}

bool SplitAndSortItemList( const CItemList& all, CItemList& ildata, CItemList& ilNotify)
{
	for( CItemList::const_iterator it = all.begin(); it != all.end(); ++ it)
	{
		const CItemPointer& ip = *it;

		if( CClass* pc = dynamic_cast<CClass*>( ip.GetBase()))
		{
			if( pc->IsNotify())
			{
				if( pc->GetParent().empty())
					ilNotify.push_back( ip);
				else
					printf( "error: %s notify cannot has parent %s\n", pc->GetName().c_str(), pc->GetParent().c_str());
			}
			else
			{
				ildata.push_back( ip);
			}
		} // if( CClass* pc = dynamic_cast<CClass*>( bi.GetBase()))
		else if( CEnumType* et = dynamic_cast<CEnumType*>( ip.GetBase()))
		{
			ildata.push_back( ip);
		} // else if( CEnumType* et = dynamic_cast<CEnumType*>( bi.GetBase()))
		else
		{
			CBase* pb = ip.GetBase();
			assert( false);
		}
	} // for( CItemList::const_iterator it = all.begin(); it != all.end(); ++ it)
	return SortDataItemList( ildata);
}


bool SortGroupList( CGroupList& gl)
{
	typedef std::set<std::wstring> CNameSet;

	CNameSet		nset;
	CGroupList		glsrc;
	gl.swap( glsrc);

	for( CGroupList::iterator it = glsrc.begin(); it != glsrc.end(); )
	{
		GroupSingleDef& gsd = *it;

		if( gsd.groups.empty())
		{
			nset.insert( gsd.name);
			gl.push_back( gsd);
			it = glsrc.erase( it);
		}
		else
			++ it;
	} // for( CItemList::iterator it = glsrc.begin(); it != glsrc.end(); )

	while( !glsrc.empty())
	{
		size_t sCount = glsrc.size();
		for( CGroupList::iterator it = glsrc.begin(); it != glsrc.end(); ++ it)
		{
			GroupSingleDef& gsd = *it;

			bool  bHasUnDef = false;
			for( CWStringList::const_iterator itvl = gsd.groups.begin(); itvl != gsd.groups.end(); ++ itvl)
			{
				const std::wstring& gname = *itvl;
				if( nset.find( gname) == nset.end())
				{
					bHasUnDef = true;
					break;
				}
			}
			if( bHasUnDef)
				continue;
			nset.insert( gsd.name);
			gl.push_back( gsd);
			glsrc.erase( it);
			break;
		} // for( CItemList::iterator it = glsrc.begin(); it != glsrc.end(); ++ it)

		if( glsrc.size() == sCount)
		{
			wprintf( L"%d group cannot sort:\n", sCount);
			for( CGroupList::iterator it = glsrc.begin(); it != glsrc.end(); ++ it)
			{
				GroupSingleDef& gsd = *it;
				wprintf( L"group %s\n", gsd.name.c_str());
			}
			wprintf( L"\n");
			return false;
		}
	}
	return true;
}

} // namespace DATA {