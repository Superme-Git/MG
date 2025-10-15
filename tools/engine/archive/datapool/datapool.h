#pragma once

namespace DATAPOOL {

typedef unsigned int NOTIYF_DATA_ID;

void DataPoolInitialize();

class CNotifyBase;

class CNoCopyDataObject
{
	CNoCopyDataObject( CNoCopyDataObject&) {}
	CNoCopyDataObject& operator=( CNoCopyDataObject&) { return *this; }
public:
	CNoCopyDataObject() {}
	virtual ~CNoCopyDataObject() {}
};

class CNotifyableData : public CNoCopyDataObject
{
public:
	CNotifyableData() {}
	virtual ~CNotifyableData() {}
};

class CDataModifyNotify : public CNoCopyDataObject
{
	friend class CNotifyBase;
public:
	CDataModifyNotify() {}
	virtual ~CDataModifyNotify() {}
protected:
	virtual void OnDataModify( NOTIYF_DATA_ID ndid, const CNotifyableData& data) = 0;
};

class CDataModifyManager
{
	friend void DataPoolInitialize();
	typedef std::map<NOTIYF_DATA_ID,CNotifyBase*>	CNotifyDataMap;

	CNotifyDataMap		m_map;

	static CDataModifyManager s_instance;

	CDataModifyManager() {}
	~CDataModifyManager() {}
public:
	static CDataModifyManager& GetInstance() { return s_instance; }
public:
	bool RegisterNotify( NOTIYF_DATA_ID ndid, CDataModifyNotify& notify);
	bool UnregisterNotify( NOTIYF_DATA_ID ndid, CDataModifyNotify& notify);

private:
	void RegisterAllDataObject();
	void RegisterDataObject( CNotifyBase& ndb);
};

class CNotifyBase : public CNoCopyDataObject
{
	friend class CDataModifyManager;

	class CNotifySet;
	friend class CNotifySet;
	CNotifySet*	m_NotifySet;
public:
	CNotifyBase();
	virtual ~CNotifyBase();
private:
	void FireNotify( CDataModifyNotify& dmn, NOTIYF_DATA_ID ndid, const CNotifyableData& data)
	{
		dmn.OnDataModify( ndid, data);
	}
protected:
	virtual NOTIYF_DATA_ID QueryDataID() const  = 0;
	virtual const CNotifyableData& QueryDataObject() const = 0;
public:
	void DataModifyNotifyAdd( CDataModifyNotify& dmn);
	void DataModifyNotifyRemove( CDataModifyNotify& dmn);
	void FireDataChange();
};

template<class T>
class CNotifyContainer : public CNoCopyDataObject
{
	T&	m_container;
	CNotifyBase&	m_notify;
public:
	CNotifyContainer( T& c, CNotifyBase& n) : m_container( c), m_notify( n) {}
	CNotifyContainer( CNotifyContainer& src) : m_container( src.m_container), m_notify( src.m_notify) {}
	virtual ~CNotifyContainer() { m_notify.FireDataChange(); }
public:
	T& GetContainer() { return m_container; }
	const T& GetContainer() const { return m_container; }
	operator T&() { return m_container; }
	T* operator->() { return &m_container; }
	const T* operator->() const { return &m_container; }
};

class CNotifyNamedData : public CNotifyBase
{
	NOTIYF_DATA_ID				m_ndid;
	const CNotifyableData&	m_object;
public:
	CNotifyNamedData( NOTIYF_DATA_ID ndid, const CNotifyableData& obj) : m_ndid( ndid), m_object( obj) {}
	virtual ~CNotifyNamedData() {}
protected:
	virtual NOTIYF_DATA_ID QueryDataID() const { return m_ndid; }
	virtual const CNotifyableData& QueryDataObject() const { return m_object; }
};

class CNotifyDataIDSet
{
	friend class CNotifyGroupManager;

	typedef std::set<NOTIYF_DATA_ID> CIDSet;
	CIDSet		m_set;
public:
	CNotifyDataIDSet() {}
	virtual ~CNotifyDataIDSet() {}
public:
	void Insert( NOTIYF_DATA_ID ndid) { m_set.insert( ndid); } 
	void Insert( const CNotifyDataIDSet&  idset) { m_set.insert( idset.m_set.begin(), idset.m_set.end()); }
	bool IsIDIn( NOTIYF_DATA_ID ndid) const { return m_set.find( ndid) != m_set.end(); }
};

class CNotifyGroupManager
{
	friend void DataPoolInitialize();

	typedef std::map<std::wstring,CNotifyDataIDSet> CNDIDSetMap;
	CNDIDSetMap		m_map;

	static CNotifyGroupManager	s_instance;

	CNotifyGroupManager() {}
	~CNotifyGroupManager() {}
public:
	static CNotifyGroupManager& GetInstance() { return s_instance; }
public:
	void RegisterIDSetToGroup( const std::wstring& dst, const CNotifyDataIDSet& idset)
	{
		CNotifyDataIDSet& dstset = m_map[dst];
		dstset.Insert( idset);
	}
	bool RegisterGroupToGroup( const std::wstring& dst, const std::wstring& src)
	{
		CNDIDSetMap::const_iterator it = m_map.find( src);
		if( it == m_map.end())
			return false;
		const CNotifyDataIDSet& idset = it->second;
		RegisterIDSetToGroup( dst, idset);
		return true;
	}

	bool RegisterNotify( const std::wstring& group, CDataModifyNotify& notify)
	{
		CNDIDSetMap::const_iterator it = m_map.find( group);
		if( it == m_map.end())
			return false;
		const CNotifyDataIDSet& idset = it->second;
		for( CNotifyDataIDSet::CIDSet::const_iterator it = idset.m_set.begin(); it != idset.m_set.end(); ++ it)
		{
			const NOTIYF_DATA_ID& ndid = *it;
			CDataModifyManager::GetInstance().RegisterNotify( ndid, notify);
		}
		return true;
	}
	bool UnregisterNotify( const std::wstring& group, CDataModifyNotify& notify)
	{
		CNDIDSetMap::const_iterator it = m_map.find( group);
		if( it == m_map.end())
			return false;
		const CNotifyDataIDSet& idset = it->second;
		for( CNotifyDataIDSet::CIDSet::const_iterator it = idset.m_set.begin(); it != idset.m_set.end(); ++ it)
		{
			const NOTIYF_DATA_ID& ndid = *it;
			CDataModifyManager::GetInstance().UnregisterNotify( ndid, notify);
		}
		return true;
	}
private:
	void RegisterAllDefineGroup();
};

} // namespace DATAPOOL {
