#pragma once
#include <list>
#include <string>
#include "Mutex.h"
#include "../../pfs/pfs.h"

class CDownloadFileList
{
public:
	typedef std::pair<std::wstring, PFS::CMetaInfo> CMetaInfoPair;

private:
	MUTEX::CSemaphore				m_Semaphore;
	MUTEX::CMutex					m_mutex;
	std::list<CMetaInfoPair>		m_list;

public:
	CDownloadFileList() {}
	~CDownloadFileList() {}

	void Push( const CMetaInfoPair& mipair)
	{
		{
			MUTEX::CMutex::CScoped scope( m_mutex);
			m_list.push_back( mipair);
		}
		m_Semaphore.AddRef();
	}

	bool Pop( CMetaInfoPair& mipair)
	{
		m_Semaphore.Wait();

		MUTEX::CMutex::CScoped scope( m_mutex);
		if( !m_list.empty())
		{
			mipair = m_list.front();
			m_list.pop_front();
			return true;
		}
		return false;
	}

	void Clear()
	{
		MUTEX::CMutex::CScoped scope( m_mutex);
		m_list.clear();
	}

	size_t Size()
	{
		MUTEX::CMutex::CScoped scope( m_mutex);
		return m_list.size();
	}

	static CDownloadFileList& GetInstance()
	{
		static CDownloadFileList s_instance;
		return s_instance;
	}
};