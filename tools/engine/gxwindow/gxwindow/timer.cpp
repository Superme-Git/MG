#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS { 

__int64 GenerateTimerNotifyID()
{
	static __int64 n = 0;
	return ++ n;
}

struct TimerItem
{
	CTimerNotify*	tn;
	unsigned int	time;
//	__int64			uid;
};

struct EraseTimerItem : public INLINECLASS::TimerItem
{
	EraseTimerItem( CTimerNotify* tn, unsigned int time, __int64 _tid)
		: tid( _tid)
	{
		this->tn = tn;
		this->time = time;
	}
	__int64		tid;
};

class CTimerNotifyMap : public std::map<__int64,TimerItem>
{
public:
	CTimerNotifyMap() {}
	virtual ~CTimerNotifyMap() {}
};

} // namespace INLINECLASS { 

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComponentNotify::CComponentNotify()
{
}

CComponentNotify::~CComponentNotify()
{
}

CComponentNotify::CComponentNotify( const CComponentNotify& src)
{
}

CComponentNotify& CComponentNotify::operator=( const CComponentNotify& src)
{
	return *this;
}

bool CComponentNotify::IsSameNotify( const CComponentNotify& cn) const
{
	return this == &cn;
}

bool CComponentNotify::IsSameNotify( const CComponentNotify* cn) const
{
	return this == cn;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTimerNotify::CTimerNotify()
{
}

CTimerNotify::~CTimerNotify()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

unsigned int		CTimer::s_uiCurrentTime = 0;
CTimer				CTimer::g_Instance;

CTimer::CTimer()
	: m_map( new INLINECLASS::CTimerNotifyMap())
{
}

CTimer::~CTimer()
{
	delete m_map;
}

void CTimer::NewTimer( CTimerNotify& tn, unsigned int nDelay, TIMERID& tid)
{
	__int64 nid = tid.GetID();
	if( -1 == nid)
		nid = INLINECLASS::GenerateTimerNotifyID();

	INLINECLASS::TimerItem& ti = (*g_Instance.m_map)[nid];
	ti.tn = &tn;
	ti.time = s_uiCurrentTime + nDelay;

	tid.ResetID( nid);
}

void CTimer::ResetTimer( CTimerNotify& tn, const TIMERID& tid, unsigned int nDelay)
{
	assert( !tid.IsNull());

	__int64 nid = tid.GetID();
	INLINECLASS::TimerItem& ti = (*g_Instance.m_map)[nid];
	ti.tn = &tn;
	ti.time = s_uiCurrentTime + nDelay;
}

void CTimer::CancelTimer( const TIMERID& nid)
{
	g_Instance.m_map->erase( nid.GetID());
}

void CTimer::DoCheckTimer()
{
	typedef std::list<INLINECLASS::EraseTimerItem> CItemSet;
	CItemSet	items;

	for( INLINECLASS::CTimerNotifyMap::iterator it = m_map->begin(); it != m_map->end(); ++ it)
	{
		INLINECLASS::TimerItem& ti = it->second;
		if( s_uiCurrentTime > ti.time)
		{
			items.push_back( INLINECLASS::EraseTimerItem( ti.tn, ti.time, it->first) );
		}
	}
	for( CItemSet::iterator it = items.begin(); it != items.end(); ++ it)
	{
		INLINECLASS::EraseTimerItem& ti = *it;
		m_map->erase( ti.tid);
	}

	for( CItemSet::iterator it = items.begin(); it != items.end(); ++ it)
	{
		INLINECLASS::EraseTimerItem& ti = *it;
		static CTimerID dummy_timeid;
		dummy_timeid.m_id = ti.tid;
		ti.tn->OnTimer( dummy_timeid);
		dummy_timeid.m_id = -1;
	}
}

unsigned int CTimer::GetCurrentTime()
{
	return s_uiCurrentTime;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void GXTimerUpdate()
{
	CTimer::s_uiCurrentTime = timeGetTime();
	CTimer::g_Instance.DoCheckTimer();	
}

CTimerID::CTimerID() 
	: m_id( -1)
{}

CTimerID::~CTimerID() 
{
	CancelTimer();
}

void CTimerID::CancelTimer()
{
	if( -1 != m_id)
		CTimer::CancelTimer( *this);
}

void CTimerID::ResetTimer( CTimerNotify& tn, unsigned int nDelay)
{
	CTimer::ResetTimer( tn, *this, nDelay);
}

void CTimerID::NewTimer( CTimerNotify& tn, unsigned int nDelay)
{
	CTimer::NewTimer( tn, nDelay, *this);
}

bool CTimerID::IsNull() const
{
	return -1 == m_id;
}

__int64 CTimerID::GetID() const
{
	return m_id;
}

void CTimerID::ResetID( __int64 nid)
{
	m_id = nid;
}

bool CTimerID::operator==( const CTimerID& src) const
{
	return m_id == src.m_id;
}

} // namespace GXWINDOW { 




