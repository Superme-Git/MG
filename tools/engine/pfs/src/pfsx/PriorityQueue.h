#pragma once

#include <queue>
#include <vector>

namespace PRIQUEUE {
	namespace helper {

		class CHandle
		{
			HANDLE		m_hHandle;

			CHandle( const CHandle&)  {}
			CHandle& operator=( const CHandle&)  { return *this; }
		public:
			CHandle() 
				: m_hHandle( NULL) 
			{}
			virtual ~CHandle() 
			{
				if( m_hHandle)
					CloseHandle( m_hHandle);
			}
		protected:
			void Attach( HANDLE handle)
			{
				assert( NULL == m_hHandle);
				m_hHandle = handle;
			}
			HANDLE Detach()
			{
				assert( m_hHandle);
				HANDLE	temp = m_hHandle;
				m_hHandle = NULL;
				return temp;
			}
		public:
			HANDLE GetHandle() const
			{
				assert( m_hHandle);
				return m_hHandle;
			}
			void Close()
			{
				if( m_hHandle)
				{
					CloseHandle( m_hHandle);
					m_hHandle = NULL;
				}
			}
			bool Wait();
		};

		class CThread : public CHandle
		{
		public:
			CThread() 
			{}
			virtual ~CThread() 
			{}
		public:
			unsigned int Create();
		private:
			static unsigned long __stdcall ThreadProc( void* data);
		protected:
			virtual void OnRunning() = 0;
		};

		class CEvent : public CHandle
		{
		public:
			CEvent() 
			{}
			~CEvent() 
			{}
		public:
			unsigned int Create( bool signaled);
			bool TriggerEvent();
		public:
			class CScoped
			{
				CEvent&	m_event;
			public:
				CScoped( CEvent& e) 
					: m_event( e) 
				{
					m_event.Wait();
				}
				~CScoped()
				{
					m_event.TriggerEvent();
				}
			};
		};

		class CSemaphore : public CHandle
		{
		public:
			CSemaphore() 
			{}
			virtual ~CSemaphore() 
			{}
		public:
			unsigned int Create( int maxcount);
			bool Increase();
		};

		template<class OBJECT>
		struct DefaultDeleteFunc
		{
			void operator()( OBJECT obj)
			{
			}
		};

	} // namespace helper {

	template<class OBJECT, class DealFuncInThread, int QueueMaxSize = 2048, class DeleteFunc = helper::DefaultDeleteFunc<OBJECT>>
	class CPriorityQueue : protected helper::CThread
	{
		helper::CEvent		m_ExistEvent;
		helper::CEvent		m_SyncEvent;
		helper::CSemaphore	m_SyncSemaphore;
		DealFuncInThread	m_DealFunc;

		typedef std::vector<std::queue<OBJECT>> CQueue;
		CQueue		m_Queue;
	public:
		CPriorityQueue() 
		{}
		virtual ~CPriorityQueue() 
		{}
	private:
		template<class DeleteFunc>
		void ReleaseLeaveObjects( std::queue<OBJECT>& objects, DeleteFunc df)
		{
			while( !objects.empty())
			{
				OBJECT obj = objects.front();
				df( obj);
				objects.pop();
			}
		}

		template<class DeleteFunc>
		void ReleaseLeaveObjects( DeleteFunc df)
		{
			for( CQueue::iterator it = m_Queue.begin(), ite = m_Queue.end(); it != ite; ++ it)
			{
				std::queue<OBJECT>& objects = *it;
				ReleaseLeaveObjects( objects, df);
			}
			m_Queue.clear();
		}

		OBJECT PopWithoutSemaphore()
		{
			helper::CEvent::CScoped		scoped( m_SyncEvent);

			for( CQueue::iterator it = m_Queue.begin(), ite = m_Queue.end(); it != ite; ++ it)
			{
				std::queue<OBJECT>& objects = *it;
				if( objects.empty())
					continue;
				OBJECT obj = objects.front();
				objects.pop();
				return obj;
			}
			assert( false);
			return OBJECT();
		}

	protected:
		virtual void OnRunning()
		{
			HANDLE	hwait[2];
			hwait[0] = m_ExistEvent.GetHandle();
			hwait[1] = m_SyncSemaphore.GetHandle();

			while( true)
			{
				DWORD dwResult = ::WaitForMultipleObjects( 2, hwait, FALSE, INFINITE);
				switch( dwResult)
				{
				case WAIT_OBJECT_0 :
					return;
				case WAIT_OBJECT_0 + 1:
					m_DealFunc( PopWithoutSemaphore());
					break;
				default:
					assert( false);
					break;
				}
			}
		}

	public:
		bool Initialize( DealFuncInThread func, int levelcount)
		{
			unsigned int dwResult = m_SyncEvent.Create( true);
			if( 0 != dwResult)
				return false;
			dwResult = m_SyncSemaphore.Create( QueueMaxSize);
			if( 0 != dwResult)
				return false;
			m_Queue.resize( levelcount);

			dwResult = m_ExistEvent.Create( false);
			if( 0 != dwResult)
				return false;

			m_DealFunc = func;
			dwResult = CThread::Create();
			return 0 != dwResult;
		}
		void Uninitialize( DeleteFunc df = helper::DefaultDeleteFunc<OBJECT>())
		{
			m_ExistEvent.TriggerEvent();
			CThread::Wait();
			CThread::Close();
			m_SyncEvent.Close();
			m_SyncSemaphore.Close();

			ReleaseLeaveObjects( df);
		}

		bool Push( int level, OBJECT obj)
		{
			helper::CEvent::CScoped		scoped( m_SyncEvent);

			if( !m_SyncSemaphore.Increase())
				return false;

			if( level < 0 || level >= (int)m_Queue.size())
			{
				assert( !"CPriorityQueue level ²»ÕýÈ·");
				return false;
			}

			std::queue<OBJECT>& objects = m_Queue[level];
			objects.push( obj);
			return true;
		}
		void Clear( DeleteFunc df = helper::DefaultDeleteFunc<OBJECT>())
		{
			helper::CEvent::CScoped		scoped( m_SyncEvent);
			ReleaseLeaveObjects( df);
		}
	};

} // namespace PRIQUEUE {
