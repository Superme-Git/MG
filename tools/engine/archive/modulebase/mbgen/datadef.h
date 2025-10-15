#pragma once

#include "../../datapool/gendatacommon.h"

namespace DATA {

class CState : public CBase
{
public:
	CState() {}
	CState( const CState& src) : CBase( src) {}
	virtual ~CState() {}
public:
	virtual CBase* Clone() const { return new CState( *this); }
};

class CData : public CObject
{
public:
	CData() {}
	CData( const CData& src) : CObject( src) {}
	virtual ~CData() {}
public:
	virtual CBase* Clone() const { return new CData( *this); }
};

class CNotify : public CObject
{
public:
	CNotify() {}
	CNotify( const CNotify& src) : CObject( src) {}
	virtual ~CNotify() {}
public:
	virtual CBase* Clone() const { return new CNotify( *this); }
};

typedef std::list<std::wstring>			CWStringList;
typedef std::list<std::pair<int,int>>	CIDRangerList;

class CModule : public CBase
{
	bool		m_bMessage;
	bool		m_bRender;
	bool		m_bState;
	bool		m_bIdle;
	bool		m_bReset;

	CWStringList	m_listState;
	CWStringList	m_listProtocol;
	CWStringList	m_listNotify;
	CIDRangerList	m_listDatacore;
public:
	CModule() {}
	CModule( const CModule& src) 
		: CBase( src), m_bMessage( src.m_bMessage), m_bRender( src.m_bRender), m_bState( src.m_bState), m_bIdle( src.m_bIdle), m_bReset( src.m_bReset),
		m_listState( src.m_listState), m_listProtocol( src.m_listProtocol), m_listNotify( src.m_listNotify), m_listDatacore( src.m_listDatacore)
	{}
	virtual ~CModule() {}
public:
	virtual CBase* Clone() const { return new CModule( *this); }

	bool IsMessage() const { return m_bMessage; }
	void SetMessage( bool b) { m_bMessage = b; }
	
	bool IsRender() const { return m_bRender; }
	void SetRender( bool b) { m_bRender = b; }
	
	bool IsState() const { return m_bState; }
	void SetState( bool b) { m_bState = b; }
	
	bool IsIdle() const { return m_bIdle; }
	void SetIdle( bool b) { m_bIdle = b; }
	
	bool IsReset() const { return m_bReset; }
	void SetReset( bool b) { m_bReset = b; }

	CWStringList& GetStateList() { return m_listState; }
	const CWStringList& GetStateList() const { return m_listState; }

	CWStringList& GetProtocolList() { return m_listProtocol; }
	const CWStringList& GetProtocolList() const { return m_listProtocol; }

	CWStringList& GetNotifyList() { return m_listNotify; }
	const CWStringList& GetNotifyList() const { return m_listNotify; }

	CIDRangerList& GetDataCoreList() { return m_listDatacore; }
	const CIDRangerList& GetDataCoreList() const { return m_listDatacore; }
};

} // namespace DATADEF 