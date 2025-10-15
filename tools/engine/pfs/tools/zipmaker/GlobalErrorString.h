#pragma once
#include <list>
#include <string>

class CMutex
{
	CRITICAL_SECTION		m_cs;
public:
	CMutex() { InitializeCriticalSection( &m_cs); }
	~CMutex() { DeleteCriticalSection( &m_cs); }
public:
	void Lock() { EnterCriticalSection( &m_cs); }
	void Unlock() { LeaveCriticalSection( &m_cs); }
};

class CScoped
{
	CMutex&	m_mutex;
public:
	CScoped( CMutex& m) : m_mutex( m) { m_mutex.Lock(); }
	~CScoped() { m_mutex.Unlock(); }
};

class CErrorStringArray
{
	
	std::list<std::wstring>		m_list;
	CMutex						m_lock;

	CErrorStringArray() {}
	virtual ~CErrorStringArray() {}

public:
	void Push( const std::wstring& err)
	{
		CScoped	scoped( m_lock);
		m_list.push_back( err);
	}
	void PushFront( const std::wstring& err)
	{
		CScoped	scoped( m_lock);
		m_list.push_front( err);
	}
	void Pop( std::wstring& err)
	{
		CScoped	scoped( m_lock);
		if( !m_list.empty())
		{
			err = m_list.front();
			m_list.pop_front();
		}
	}
	void Clear()
	{
		CScoped	scoped( m_lock);
		m_list.clear();
	}
	static CErrorStringArray& GetInstance()
	{
		static CErrorStringArray s_Instance;
		return s_Instance;
	}
};