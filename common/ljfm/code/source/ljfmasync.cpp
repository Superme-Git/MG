#if defined _MSC_VER
#if !(defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <process.h>
#include "../include/common.h"
#include "../include/ljfmasync.h"
#include "../include/ljfmpq.h"

#ifdef WIN7_32
#ifdef OUT_SYNC_FILEIO_TIME_LOG___
#include "../include/timelog.h"
#endif
#endif


namespace LJFMX
{
	namespace helper
	{
		struct ReaderObject
		{
			CAyncReadBaseNotify*	notify;
			int						nError;
			std::wstring			filename;
			LJFM::LJFMID			buffer;
		};

		struct DealwithFuncDefine
		{
			void operator()(const ReaderObject& ro);
		};

		class CSyncArray : public LJFM::LJFMUO
		{
			typedef std::list<ReaderObject> CReaderList;

			CReaderList		m_list;
			mutable LJFMQ::helper::CEvent	m_lock;
		public:
			CSyncArray()
			{
				m_lock.Create(true);
			}
			virtual ~CSyncArray()
			{}
		public:
			int Size() const
			{
				LJFMQ::helper::CEvent::CScoped		scoped(m_lock);
				return (int)m_list.size();
			}

			void Push(const ReaderObject& ro)
			{
				LJFMQ::helper::CEvent::CScoped		scoped(m_lock);
				m_list.push_back(ro);
			}

			void PushFront(const ReaderObject& ro)
			{
				LJFMQ::helper::CEvent::CScoped		scoped(m_lock);
				m_list.push_front(ro);
			}
			ReaderObject Pop()
			{
				ReaderObject temp;
				LJFMQ::helper::CEvent::CScoped		scoped(m_lock);
				if (!m_list.empty())
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
				LJFMQ::helper::CEvent::CScoped		scoped(m_lock);
				m_list.clear();
			}
		};

		class CAsyncRead
		{
			friend struct DealwithFuncDefine;

			enum { MAX_ITEMS = 200, };

			LJFMQ::LJFMPQ<ReaderObject, DealwithFuncDefine, MAX_ITEMS>	m_PQ;
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
			void OnRead(ReaderObject ro)
			{
#ifdef WIN7_32
#ifdef OUT_SYNC_FILEIO_TIME_LOG___
				helper::CLogScoped		log_scoped(L"CAsyncRead::OnRead", ro.filename.c_str(), ro.buffer);
#endif
#endif
				assert(ro.notify);
				if (!ro.notify->DoRead(ro.filename, ro.buffer, ro.nError))
				{
					m_NotifyArray.Push(ro);
				}
			}
			bool UpdateOne()
			{
				ReaderObject ro = m_NotifyArray.Pop();
				if (NULL == ro.notify)
				{
					return false;
				}
#ifdef WIN7_32
#ifdef OUT_SYNC_FILEIO_TIME_LOG___
				helper::CLogScoped		log_scoped(L"CAsyncRead::UpdateOne", ro.filename.c_str(), ro.buffer);
#endif
#endif
				ro.notify->OnReady(ro.nError, ro.filename, ro.buffer);
				return true;
			}
		public:
			void Read(const std::wstring& filename, CAyncReadBaseNotify& notify, int level)
			{
				ReaderObject		ro;
				ro.filename = filename;
				ro.notify = &notify;
				ro.nError = 0;

				m_PQ.Push(level, ro);
			}
			void Clear()
			{
				m_PQ.Clear();
				m_NotifyArray.Clear();
			}
			void Update(bool bAll)
			{
				UpdateOne();
				while (bAll && UpdateOne());
			}

			bool Start(int levelcount)
			{
				return m_PQ.Initialize(m_Dealwith, levelcount);
			}
			void Stop()
			{
				m_PQ.Uninitialize();
			}
		};

		void DealwithFuncDefine::operator()(const ReaderObject& ro)
		{
			CAsyncRead::GetInstance().OnRead(ro);
		}

	}

	bool CAyncReadNotify::DoRead(const std::wstring& filename, LJFM::LJFMID& data, int& error)
	{
#ifdef WIN7_32
#ifdef OUT_SYNC_FILEIO_TIME_LOG___
		helper::CLogScoped	logscoped(L"CAyncReadNotify::DoRead", filename.c_str(), data);
#endif
#endif
		error = LJFM::EC_SUCCESS;
		LJFM::LJFMF file;
		if (!DoOpen(file, filename))
		{
			error = LJFM::LJFMOpen::GetLastError();
			return false;
		}
		data = file.GetImage();
		return DoAfterRead(error, filename, data);
	}

	int AsyncReadClear()
	{
		helper::CAsyncRead::GetInstance().Clear();
		return 0;
	}

	int AsyncReadUpdate(bool bAll)
	{
		helper::CAsyncRead::GetInstance().Update(bAll);
		return 0;
	}

	int AsyncRead(const std::wstring& filename, CAyncReadBaseNotify& notify, int level)
	{
		helper::CAsyncRead::GetInstance().Read(filename, notify, level);
		return 0;
	}

	int AsyncReadInitialize(int levelcount)
	{
		return helper::CAsyncRead::GetInstance().Start(levelcount);
	}

	int AsyncReadUninitialize()
	{
		helper::CAsyncRead::GetInstance().Stop();
		return 0;
	}

	bool CSyncFile::Open(const std::wstring& filename)
	{
		LJFM::LJFMID data;
#ifdef WIN7_32
#ifdef OUT_SYNC_FILEIO_TIME_LOG___
		helper::CLogScoped	logscoped(L"CSyncFile::Open", filename.c_str(), data);
#endif
#endif
		LJFM::LJFMF		file;
		if (!file.Open(filename, LJFM::FM_EXCL, LJFM::FA_RDONLY))
		{
			return false;
		}
		data = file.GetImage();
		LJFMIF::Open(data);
		return true;
	}
}
#endif
#elif (defined _OS_IOS) || (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)

#include "../include/common.h"
#include "../include/ljfmasync.h"

namespace LJFMX
{
	bool CSyncFile::Open(const std::wstring& filename)
	{
		LJFM::LJFMID		data;
#ifdef WIN7_32
#ifdef OUT_SYNC_FILEIO_TIME_LOG___
		helper::CLogScoped	logscoped(L"CSyncFile::Open", filename.c_str(), data);
#endif
#endif
		LJFM::LJFMF		file;
		if (!file.Open(filename, LJFM::FM_EXCL, LJFM::FA_RDONLY))
		{
			return false;
		}
		data = file.GetImage();
		LJFMIF::Open(data);
		return true;
	}
}

#endif
