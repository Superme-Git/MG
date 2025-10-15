#include <string>
#include <list>
#include <vector>
#include <map>
#include <set>
#include <assert.h>

#include "../datapool.h"

#define DEBUG_CHECK_NOTIFY

namespace DATAPOOL {

CDataModifyManager CDataModifyManager::s_instance;

void CDataModifyManager::RegisterDataObject( CNotifyBase& ndb)
{
	assert( m_map.end() == m_map.find( ndb.QueryDataID()));
	m_map.insert( std::make_pair( ndb.QueryDataID(), &ndb));
}

bool CDataModifyManager::RegisterNotify( NOTIYF_DATA_ID ndid, CDataModifyNotify& notify)
{
	CNotifyDataMap::iterator it = m_map.find( ndid);
	if( it == m_map.end())
		return false;
	CNotifyBase*& ndb = it->second;
	ndb->DataModifyNotifyAdd( notify);
	return true;
}

bool CDataModifyManager::UnregisterNotify( NOTIYF_DATA_ID ndid, CDataModifyNotify& notify)
{
	CNotifyDataMap::iterator it = m_map.find( ndid);
	if( it == m_map.end())
		return false;
	CNotifyBase*& ndb = it->second;
	ndb->DataModifyNotifyRemove( notify);
	return true;
}

class CNotifyBase::CNotifySet
{
	typedef std::set<CDataModifyNotify*> CDMNotifySet;

	CDMNotifySet		m_set;

#ifdef DEBUG_CHECK_NOTIFY
	mutable bool g_FireNotifyNow;
	class CFireNotifyNowScope
	{
		bool&		m_value;
	public:
		CFireNotifyNowScope( bool& v) : m_value( v) 
		{ 
			assert( !m_value);
			m_value = true; 
		}
		~CFireNotifyNowScope() { m_value = false; }
	};
	void DebugCheckNotify() { assert( !g_FireNotifyNow); }
#else
	void DebugCheckNotify() {}
#endif
public:
	CNotifySet() 
	{
#ifdef DEBUG_CHECK_NOTIFY
		g_FireNotifyNow = false;
#endif
	}
	~CNotifySet() {}
public:
	void Insert( CDataModifyNotify& dmn) 
	{ 
		DebugCheckNotify();
		m_set.insert( &dmn); 
	}
	void Remove( CDataModifyNotify& dmn) 
	{ 
		DebugCheckNotify();
		m_set.erase( &dmn); 
	}
	void FireNotify( CNotifyBase& ndb, NOTIYF_DATA_ID ndid, const CNotifyableData& data) const
	{
#ifdef DEBUG_CHECK_NOTIFY
		CFireNotifyNowScope		_scoped_( g_FireNotifyNow);
#endif
		for( CDMNotifySet::const_iterator it = m_set.begin(); it != m_set.end(); ++ it)
		{
			CDataModifyNotify* dmn = *it;
			ndb.FireNotify( *dmn, ndid, data);
		} // for( CDMNotifySet::const_iterator it = m_set.begin(); it != m_set.end(); ++ it)
	}
};

CNotifyBase::CNotifyBase() : m_NotifySet( new CNotifySet()) {}
CNotifyBase::~CNotifyBase() { delete m_NotifySet; }

void CNotifyBase::DataModifyNotifyAdd( CDataModifyNotify& dmn)
{
	m_NotifySet->Insert( dmn);
}

void CNotifyBase::DataModifyNotifyRemove( CDataModifyNotify& dmn)
{
	m_NotifySet->Remove( dmn);
}

void CNotifyBase::FireDataChange()
{
	m_NotifySet->FireNotify( *this, QueryDataID(), QueryDataObject());
}

CNotifyGroupManager	CNotifyGroupManager::s_instance;

void DataPoolInitialize()
{
	CDataModifyManager::GetInstance().RegisterAllDataObject();
	CNotifyGroupManager::GetInstance().RegisterAllDefineGroup();
}

} // namespace DATAPOOL {
