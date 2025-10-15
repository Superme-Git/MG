#pragma once

namespace MODULEBASE {

typedef unsigned int ModuleID;
typedef unsigned int NotifyDataID;
typedef unsigned int ScenceStateID;

class CModuleBase;
class CModuleManage;

class CUncopyableObject
{
	CUncopyableObject( const CUncopyableObject&) {}
	CUncopyableObject& operator=( const CUncopyableObject&) { return *this; }
public:
	CUncopyableObject() {}
	virtual ~CUncopyableObject() {}
};

class CNotifyData : public CUncopyableObject
{
	NotifyDataID	m_id;
public:
	explicit CNotifyData( NotifyDataID ndid) : m_id( ndid) { }
	virtual ~CNotifyData() { }
public:
	NotifyDataID GetID() const { return m_id; }
};

class CModuleNotify : public CUncopyableObject
{
	friend class CModuleManage;
public:
	CModuleNotify() {}
	virtual ~CModuleNotify() {}
protected:
	virtual void OnModuleNotify( ModuleID mid, const CNotifyData& nd) = 0;
};

class CModuleBase : public CUncopyableObject
{
	friend class CModuleManage;

	class CScenceState : public CUncopyableObject
	{
		typedef std::set<ScenceStateID>		CIDSet;

		CIDSet		m_ids;
	public:
		CScenceState() { }
		virtual ~CScenceState() { }
	public:
		bool IsScenceStateIDIn( ScenceStateID ssid) const { return m_ids.find( ssid) != m_ids.end(); }
		void SetIDS( ScenceStateID* ids, int count)
		{
			m_ids.clear();
			for( int i = 0; i < count; i ++)
				m_ids.insert( ids[i]);
		}
	};
	
	typedef std::set<int> CIDSet;

	ModuleID		m_mid;
	CScenceState	m_ScenceState;
	CIDSet			m_Procotol;
public:
	explicit CModuleBase( ModuleID mid) : m_mid( mid) {}
	virtual ~CModuleBase() {}
protected:
	virtual bool OnWindowsMessage( UINT uMsg, WPARAM wParam, LPARAM lParam) { return false; }
	virtual void OnRender() {}
	virtual void OnScenceStateChangle( ScenceStateID n, ScenceStateID o) {}
	virtual void OnIdle() {}
	virtual void Reset() {}

	void FireNotify( const CNotifyData& nd);
	void ChangeScenceState( ScenceStateID ssid);
	ScenceStateID GetCurrentScenceState();
public:
	ModuleID GetModuleID() const { return m_mid; }

	bool IsScenceStateMatch( ScenceStateID ssid) const { return m_ScenceState.IsScenceStateIDIn( ssid); }
	void SetScenceStateIDS( ScenceStateID* ids, int count) { m_ScenceState.SetIDS( ids, count); }

	void RegisterProtocol( int id) { m_Procotol.insert( id); }
	bool IsProtocolMatch( int id) const { return m_Procotol.find( id) != m_Procotol.end(); }
};

class CModuleManage
{
	friend class CModuleBase;

	typedef std::map<ModuleID,CModuleBase*> CModuleBaseMap;

	typedef std::set<CModuleNotify*> CNotifySet;
	typedef std::map<NotifyDataID,CNotifySet> CNotifyMap;

	CModuleBaseMap		m_mbm;
	CNotifyMap			m_nm;

	ScenceStateID		m_ssidCurrent;
	ScenceStateID		m_ssidLast;

	CModuleManage() : m_ssidCurrent( -1), m_ssidLast( -1) {}
	virtual ~CModuleManage() {}
private:
	void FireNotify( ModuleID mid, const CNotifyData& nd)
	{
		assert( m_mbm.find( mid) != m_mbm.end());

		CNotifyMap::iterator itnm = m_nm.find( nd.GetID());
		if( itnm == m_nm.end())
			return;
		CNotifySet& nts = itnm->second;
		for( CNotifySet::iterator itntm = nts.begin(); itntm != nts.end(); ++ itntm)
		{
			CModuleNotify* const& Notify = *itntm;
			Notify->OnModuleNotify( mid, nd);
		}
	}
	void ChangeScenceState( ScenceStateID ssid)
	{
		m_ssidLast = m_ssidCurrent;
		m_ssidCurrent = ssid;
	}
public:
	void RegisterModule( CModuleBase& mb) { m_mbm.insert( std::make_pair( mb.GetModuleID(), &mb)); } 
	void UnregisterModule( CModuleBase& mb) { m_mbm.erase( mb.GetModuleID()); }

	void RegisterNotify( NotifyDataID mnt, CModuleNotify& notify)
	{
		CNotifyMap::iterator itnm = m_nm.find( mnt);
		if( itnm == m_nm.end())
		{
			CNotifySet& ntm = m_nm[mnt];
			ntm.insert( &notify);
		}
		else
		{
			CNotifySet& ntm = itnm->second;
			ntm.insert( &notify);
		}
	}
	void UnregisterNotify( CModuleNotify& notify)
	{
		GNET::MapEraser<CNotifyMap> me( m_nm);
		for( CNotifyMap::iterator itnm = m_nm.begin(); itnm != m_nm.end(); ++ itnm)
		{
			CNotifySet& ntm = itnm->second;
			ntm.erase( &notify);
			if( ntm.empty())
				me.push( itnm);
		}
	}
	void UnregisterNotify( NotifyDataID mnt, CModuleNotify& notify)
	{
		CNotifyMap::iterator itnm = m_nm.find( mnt);
		if( itnm == m_nm.end())
			return;
		CNotifySet& ntm = itnm->second;

		ntm.erase( &notify);

		if( ntm.empty())
			m_nm.erase( itnm);
	}

	bool DispatchMessage( UINT uMsg, WPARAM wParam, LPARAM lParam)
	{
		for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
		{
			CModuleBase*& mbl = it->second;
			if( mbl->IsScenceStateMatch( m_ssidCurrent) && mbl->OnWindowsMessage( uMsg, wParam, lParam))
				return true;
		} // for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
		return false;
	}

	void ResetAll()
	{
		for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
		{
			CModuleBase*&mb = it->second;
			mb->Reset();
		}
	}

	void SetInitSessionState( ScenceStateID ssid)
	{
		ChangeScenceState( ssid);
	}

	void IdleDo()
	{
		if( m_ssidLast != m_ssidCurrent)
		{
			for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
			{
				CModuleBase*& mbl = it->second;
				mbl->OnScenceStateChangle( m_ssidCurrent, m_ssidLast);
			} // for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
			m_ssidLast = m_ssidCurrent;
		} // if( m_ssidLast == m_ssidCurrent)

		for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
		{
			CModuleBase*& mbl = it->second;
			if( mbl->IsScenceStateMatch( m_ssidCurrent))
				mbl->OnIdle();
		} // for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
	}
	void RenderDo()
	{
		for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
		{
			CModuleBase*& mbl = it->second;
			if( mbl->IsScenceStateMatch( m_ssidCurrent))
				mbl->OnRender();
		} // for( CModuleBaseMap::iterator it = m_mbm.begin(); it != m_mbm.end(); ++ it)
	}

	ScenceStateID GetCurrentScenceState() const { return m_ssidCurrent; }
	CModuleBase* GetModule( ModuleID mid) const
	{
		CModuleBaseMap::const_iterator it = m_mbm.find( mid);
		if( it == m_mbm.end())
			return NULL;
		else
			it->second;
	}
	static CModuleManage& GetInstance()
	{
		return s_ModuleManager;
	}
private:
	static CModuleManage	s_ModuleManager;
};

inline void CModuleBase::FireNotify( const CNotifyData& nd)
{
	CModuleManage::GetInstance().FireNotify( m_mid, nd);
}

inline void CModuleBase::ChangeScenceState( ScenceStateID ssid)
{
	CModuleManage::GetInstance().ChangeScenceState( ssid);
}

inline ScenceStateID CModuleBase::GetCurrentScenceState()
{
	return CModuleManage::GetInstance().GetCurrentScenceState();
}



} // namespace MODULEBASE {