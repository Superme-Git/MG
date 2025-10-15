#pragma once
#include <list>
#include <string>
#include "Mutex.h"

namespace DOWNLOAD
{
	template< class ITEM >
	class CDownloadFileListT
	{
	public:

	private:
		CSemaphore			m_Semaphore;
		CMutex				m_mutex;
		std::list<ITEM>		m_list;

	public:
		CDownloadFileListT() {}
		~CDownloadFileListT() {}

		void Push( const ITEM& mipair)
		{
			{
				CMutex::CScoped scope( m_mutex);
				m_list.push_back( mipair);
			}
			m_Semaphore.AddRef();
		}

		bool Pop( ITEM& mipair)
		{
			m_Semaphore.Wait();

			CMutex::CScoped scope( m_mutex);
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
			CMutex::CScoped scope( m_mutex);
			m_list.clear();
			m_Semaphore.Clear();
		}

		size_t Size()
		{
			CMutex::CScoped scope( m_mutex);
			return m_list.size();
		}
	};

	template<class ITEMTYPE>
	CDownloadFileListT<ITEMTYPE>& GetInstance()
	{
		static CDownloadFileListT<ITEMTYPE> s_instance;
		return s_instance;
	}

	typedef std::pair< int, std::wstring > ItemData;

} // namespace DOWNLOAD