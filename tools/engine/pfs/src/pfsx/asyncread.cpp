#include "../common.h"
#include <process.h>
#include "../../asyncread.h"
#include "../timelog/timelog.h"
#include "PriorityQueue.h"

namespace PFSX {

	namespace helper {

		struct ReaderObject
		{
			CAyncReadBaseNotify*	notify;
			int						nError;
			std::wstring			filename;
			PFS::CImageData			buffer;
		};

		struct DealwithFuncDefine
		{
			void operator()( const ReaderObject& ro);
		};

		class CSyncArray : public PFS::CUncopyableObject
		{
			typedef std::list<ReaderObject> CReaderList;

			CReaderList		m_list;
			mutable PRIQUEUE::helper::CEvent	m_lock;
		public:
			CSyncArray() 
			{
				m_lock.Create( true);
			}
			virtual ~CSyncArray() 
			{}
		public:
			int Size() const
			{
				PRIQUEUE::helper::CEvent::CScoped		scoped( m_lock);
				return (int)m_list.size();
			}

			void Push( const ReaderObject& ro)
			{
				PRIQUEUE::helper::CEvent::CScoped		scoped( m_lock);
				m_list.push_back( ro);
			}

			void PushFront( const ReaderObject& ro)
			{
				PRIQUEUE::helper::CEvent::CScoped		scoped( m_lock);
				m_list.push_front( ro);
			}
			ReaderObject Pop()
			{
				ReaderObject temp;
				PRIQUEUE::helper::CEvent::CScoped		scoped( m_lock);
				if( !m_list.empty())
				{
					temp = m_list.front();
					m_list.pop_front();
				}
				else
				{
					temp.notify = NULL;
				}
				return temp;
			}
			void Clear()
			{
				PRIQUEUE::helper::CEvent::CScoped		scoped( m_lock);
				m_list.clear();
			}
		};

		class CAsyncRead 
		{
			friend struct DealwithFuncDefine;

			enum { MAX_ITEMS = 200, };

			PRIQUEUE::CPriorityQueue<ReaderObject,DealwithFuncDefine,MAX_ITEMS>	m_PriorityQueue;
			CSyncArray							m_NotifyArray;
			DealwithFuncDefine					m_Dealwith;

			CAsyncRead() {}
			virtual ~CAsyncRead() {}
		public:
			static CAsyncRead& GetInstance()
			{
				static CAsyncRead ar;
				return ar;
			}
		private:
			void OnRead( ReaderObject ro)
			{
				helper::CLogScoped		log_scoped( L"CAsyncRead::OnRead", ro.filename.c_str(), ro.buffer);

				assert( ro.notify);
				if( !ro.notify->DoRead( ro.filename, ro.buffer, ro.nError))
					m_NotifyArray.Push( ro);
			}
			bool UpdateOne()
			{
				ReaderObject ro = m_NotifyArray.Pop();
				if( NULL == ro.notify)
					return false;

				helper::CLogScoped		log_scoped( L"CAsyncRead::UpdateOne", ro.filename.c_str(), ro.buffer);

				ro.notify->OnReady( ro.nError, ro.filename, ro.buffer);
				return true;
			}
		public:
			void Read( const std::wstring& filename, CAyncReadBaseNotify& notify, int level)
			{
				// ±» int AsyncRead(...) µ÷ÓÃ
				ReaderObject		ro;
				ro.filename = filename;
				ro.notify = &notify;
				ro.nError = 0;

				m_PriorityQueue.Push( level, ro);
			}
			void Clear()
			{
				m_PriorityQueue.Clear();
				m_NotifyArray.Clear();
			}
			void Update( bool bAll)
			{
				UpdateOne();
				while( bAll && UpdateOne());
			}

			bool Start( int levelcount)
			{
				return m_PriorityQueue.Initialize( m_Dealwith, levelcount);
			}
			void Stop()
			{
				m_PriorityQueue.Uninitialize();
			}
		};

		void DealwithFuncDefine::operator()( const ReaderObject& ro)
		{
			CAsyncRead::GetInstance().OnRead( ro);
		}

	} // namespace PFSX::helper

	bool CAyncReadNotify::DoRead( const std::wstring& filename, PFS::CImageData& data, int& error)
	{
		helper::CLogScoped	logscoped( L"CAyncReadNotify::DoRead", filename.c_str(), data);

		error = PFS::EC_SUCCESS;
		PFS::CFile file;
		if( !DoOpen( file, filename ) )
		{
			error = PFS::CEnv::GetLastError();
			return false;
		}
		data = file.GetImage();
		return DoAfterRead( error, filename, data);
	}

	int AsyncReadClear()
	{
		helper::CAsyncRead::GetInstance().Clear();
		return 0;
	}

	int AsyncReadUpdate( bool bAll)
	{
		helper::CAsyncRead::GetInstance().Update( bAll);
		return 0;
	}

	int AsyncRead( const std::wstring& filename, CAyncReadBaseNotify& notify, int level)
	{
		helper::CAsyncRead::GetInstance().Read( filename, notify, level);
		return 0;
	}

	int AsyncReadInitialize( int levelcount)
	{
		return helper::CAsyncRead::GetInstance().Start( levelcount);
	}

	int AsyncReadUninitialize()
	{
		helper::CAsyncRead::GetInstance().Stop();
		return 0;
	}

	bool CSyncFile::Open( const std::wstring& filename)
	{
		PFS::CImageData		data;
		helper::CLogScoped	logscoped( L"CSyncFile::Open", filename.c_str(), data);
		PFS::CFile		file;
		if( !file.Open( filename, PFS::FM_EXCL, PFS::FA_RDONLY))
			return false;
		data = file.GetImage();
		CImageFile::Open( data);

		return true;
	}

} // namespace PFSX {
