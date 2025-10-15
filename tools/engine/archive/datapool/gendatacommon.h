#pragma once

#include <string>
#include <list>
#include <vector>

namespace DATA {

class CBase
{
	std::wstring	m_strName;
	std::wstring	m_strNotes;
private:
	CBase& operator=( const CBase& src) { return *this; }
public:
	CBase() {}
	CBase( const CBase& src) : m_strName( src.m_strName), m_strNotes( src.m_strNotes) {}
	virtual ~CBase() {}
public:
	const std::wstring& GetName() const { return m_strName; }
	void SetName( const std::wstring& name) { m_strName = name; }
	const std::wstring& GetNotes() const { return m_strNotes; }
	void SetNotes( const std::wstring& notes) { m_strNotes = notes; }
public:
	virtual CBase* Clone() const = 0;
	virtual void Delete() { delete this; }
};

struct EnumItem
{
	std::wstring		strName;
	std::wstring		strValue;
	std::wstring		strNotes;
};

typedef std::list<EnumItem> CEnumItemList;

class CEnumType : public CBase
{
	CEnumItemList	m_ItemList;
public:
	CEnumType(){}
	CEnumType( const CEnumType& src) : CBase( src), m_ItemList( src.m_ItemList) {}
	virtual ~CEnumType(){}
public:
	CEnumItemList& GetItemList() { return m_ItemList; }
	const CEnumItemList& GetItemList() const { return m_ItemList; }
public:
	virtual CBase* Clone() const { return new CEnumType( *this); }
};

struct Variant : public EnumItem
{
	std::wstring		strType;
	std::wstring		strDefault;
	std::wstring		strKey;
};

typedef std::list<Variant> CVariantList;

class CObject : public CBase
{
	CVariantList		m_Variants;
	std::wstring		m_strParent;
public:
	CObject() {}
	CObject( const CObject& src) : CBase( src), m_Variants( src.m_Variants), m_strParent( src.m_strParent) {}
	virtual ~CObject() {}
public:
	const std::wstring& GetParent() const { return m_strParent; }
	void SetParent( const std::wstring& prnt) { m_strParent = prnt; }
	CVariantList& GetVariantList() { return m_Variants; }
	const CVariantList& GetVariantList() const { return m_Variants; }
public:
	virtual CBase* Clone() const { return new CObject( *this); }
};

class CItemPointer
{
	CBase*	bi;
public:
	CItemPointer() : bi( NULL) { }
	CItemPointer( const CBase& b) : bi( b.Clone()) { }
	CItemPointer( const CItemPointer& src) : bi( src.bi ? src.bi->Clone() : NULL) {}
	~CItemPointer() { if( bi) bi->Delete(); }
public:
	CItemPointer& operator=( const CItemPointer& src)
	{
		if( this == &src)
			return *this;
		if( bi) 
			bi->Delete();
		if( src.bi) 
			bi = src.bi->Clone();
		else
			bi = NULL;
		return *this;
	}
public:
	CBase* operator->() { return bi; }
	const CBase* operator->() const { return bi; }
	CBase* GetBase() const { return bi; }
};

class CItemList : public std::list<CItemPointer>
{
public:
	CItemList() {}
	~CItemList() { }
public:
	void Insert( const CBase& bi)
	{
		push_back( CItemPointer( bi));
	}
};

} // namespace DATA {