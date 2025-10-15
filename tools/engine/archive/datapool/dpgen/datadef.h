#pragma once

#include "../gendatacommon.h"

namespace DATA {

class CClass : public CObject
{
	bool				m_bNotify;
public:
	CClass() {}
	CClass( const CClass& src) : CObject( src), m_bNotify( src.m_bNotify) {}
	virtual ~CClass() {}
public:
	bool IsNotify() const { return m_bNotify; }
	void SetNotify( bool notify) { m_bNotify = notify; }
public:
	virtual CBase* Clone() const { return new CClass( *this); }
};

typedef std::list<std::wstring> CWStringList;
struct GroupSingleDef
{
	std::wstring		notes;
	std::wstring		name;
	CWStringList		objects;
	CWStringList		groups;
};
typedef std::list<GroupSingleDef> CGroupList;

} // namespace DATA {