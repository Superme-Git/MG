#pragma once
#include "ThreadPublic.h"
#include <string>
#include <list>

class CThreadStateList
{
public:
	struct Item
	{
		std::wstring	m_name;
		int				m_state;
	};

	void Push( const Item& cr)
	{
		THREAD::CMutex::CScoped	scoped( m_lock);
		m_list.push_back( cr);
	}

	void PushFront( const Item& cr)
	{
		THREAD::CMutex::CScoped	scoped( m_lock);
		m_list.push_front( cr);
	}

	void Pop( Item& cr)
	{
		THREAD::CMutex::CScoped	scoped( m_lock);
		if( !m_list.empty())
		{
			cr = m_list.front();
			m_list.pop_front();
		}
	}

	void Clear()
	{
		THREAD::CMutex::CScoped	scoped( m_lock);
		m_list.clear();
	}

	static CThreadStateList& GetInstance()
	{
		static CThreadStateList s_Instance;
		return s_Instance;
	}

private:

	std::list<Item>			m_list;
	THREAD::CMutex			m_lock;

	CThreadStateList() {}
};


#define WM_THREADSTATE		(WM_USER+100)		// 向窗口发送状态
#define	WM_THREADPROGRESS	(WM_USER+101)		// 向窗口发送进度

enum
{
	INIT = 0,
	PROGRESS,
	END,
};