#include "../include/common.h"

#ifdef _OS_ANDROID
#include <ext/atomicity.h>
#include <stdexcept>
#elif defined _OS_IOS
#include <libkern/OSAtomic.h>
#include <stdexcept>
#endif

namespace LJFM
{

	LJFMUO::LJFMUO(const LJFMUO&) {}
	LJFMUO& LJFMUO::operator=(const LJFMUO&)
	{
		return *this;
	}
	LJFMUO::LJFMUO() {}
	LJFMUO::~LJFMUO() {}
#ifndef _MSC_VER    
	typedef int32_t LONG;
#endif
	namespace LJFMHLP
	{

		class LJFMMD
		{
			enum
			{
				SMALL_BLOCK_SIZE = 1024,
				MEDIUM_BLOCK_SIZE = 1024 * 8,
				LARGE_BLOCK_SIZE = 1024 * 1024, // 1M
			};

			size_t		m_cur;
			size_t		m_max;
			char*		m_data;
			volatile LONG m_refcount;

		private:
			static size_t GetMaxSize(size_t s)
			{
				s += 3;
				s = s >> 2;
				s = s << 2;
				return s;
			}

			static size_t IncreaseMaxSize(size_t c)
			{
				if (c <= SMALL_BLOCK_SIZE)
				{
					c <<= 1;
				}
				else if (c <= LARGE_BLOCK_SIZE)
				{
					c = c + SMALL_BLOCK_SIZE;
				}
				else
				{
					c = c + MEDIUM_BLOCK_SIZE;
				}

				c = GetMaxSize(c);
				return c;
			}
		private:
			LJFMMD(LJFMMD&){}
			LJFMMD& operator = (LJFMMD&)
			{
				return *this;
			}

			LJFMMD(size_t c) : m_data(NULL), m_cur(0), m_refcount(1)
			{
				m_max = GetMaxSize(c);
				if (m_max != 0)
				{
					m_data = (char*)::malloc(m_max);
					if (m_data == NULL)
					{
						m_max = 0;
						throw std::bad_alloc();
					}
				}
			}
			~LJFMMD()
			{
				assert(0 == m_refcount);
				if (m_data != NULL)
				{
					::free(m_data);
					m_data = NULL;
				}
			}
		public:
			static LJFMMD* Create(size_t c = 0)
			{
				return new LJFMMD(c);
			}
			LJFMMD* Spawn(size_t c, size_t s) const
			{
				LJFMMD* egg = new LJFMMD(c);
				if (s > 0)
				{
#ifdef _MSC_VER
					egg->Append(m_data, min(m_cur, s));
#elif defined(_OS_IOS)
					egg->Append(m_data, std::min(m_cur, s));
#endif        
					egg->m_cur = s;
				}
				return egg;
			}

			LONG Release()
			{
				assert(m_refcount > 0);
#ifdef _MSC_VER
				if (LONG lResult = InterlockedDecrement(&m_refcount))
#elif defined(_OS_ANDROID)
				if (LONG lResult = __gnu_cxx::__exchange_and_add(&m_refcount, -1) - 1)
#elif defined(_OS_IOS)
				if (LONG lResult = OSAtomicDecrement32Barrier(&m_refcount))
#endif
				{
					return lResult;
				}
				else
				{
					delete this;
					return 0;
				}
			}
			LONG AddRef()
			{
				assert(m_refcount > 0);
#ifdef _MSC_VER
				return InterlockedIncrement(&m_refcount);
#elif defined(_OS_ANDROID)
				return __gnu_cxx::__exchange_and_add(&m_refcount, 1) + 1;
#elif defined(_OS_IOS)
				return OSAtomicIncrement32Barrier(&m_refcount);
#endif
			}

			LONG RefCount() const
			{
#ifdef _MSC_VER
				return InterlockedExchangeAdd((volatile LONG *)&m_refcount, 0);
#elif defined(_OS_ANDROID)
				return __gnu_cxx::__exchange_and_add((volatile LONG*)&m_refcount, 0);
#elif defined(_OS_IOS)
				return OSAtomicAdd32Barrier(0, (volatile int32_t*)&m_refcount);
#endif
			}

			bool Compare(const LJFMMD& dst) const
			{
				if (m_data == dst.m_data)
					return true;
				if (m_cur != dst.m_cur)
					return false;
				return 0 == memcmp(m_data, dst.m_data, m_cur);
			}
			void* GetData() const
			{
				return m_data;
			}
			size_t GetSize() const
			{
				return m_cur;
			}
			void Resize(size_t s)
			{
				Reserve(s);
				m_cur = s;
			}
			void Reserve(size_t s)
			{
				if (m_max >= s)
					return;
				size_t _max = GetMaxSize(s);
				char* _data = (char*)::realloc(m_data, _max);
				if (_data == NULL)
					throw std::bad_alloc();
				m_data = _data;
				m_max = _max;
			}
			LJFMMD* Clone() const
			{
				LJFMMD* data = new LJFMMD(m_max);
				if (m_cur > 0)
				{
					data->m_cur = m_cur;
					memcpy(data->m_data, m_data, m_cur);
				}
				return data;
			}
			void Clear(bool deep)
			{
				if (deep)
				{
					if (m_data != NULL)
					{
						::free(m_data);
						m_data = NULL;
					}
					m_max = 0;
				}
				m_cur = 0;
			}

			void Append(const void* data, size_t size)
			{
				if (m_max - m_cur < size)
				{
					size_t _max = IncreaseMaxSize(m_cur + size);

					if (m_data <= data || (char *)m_data >= ((char *)data + m_cur))
					{
						char* _data = (char*)::realloc(m_data, _max);
						if (_data == NULL)
							throw std::bad_alloc();

						m_data = _data;
						m_max = _max;

#ifdef _MSC_VER
						::memcpy_s(&m_data[m_cur], m_max, data, size);
#elif defined(_OS_IOS)
						memcpy(&m_data[m_cur], data, size);
#endif
					}
					else
					{
						char* newdata = (char*)::malloc(_max);
						if (newdata == NULL)
							throw std::bad_alloc();

						m_max = _max;
#ifdef _MSC_VER
						::memcpy_s(newdata, m_max, m_data, m_cur);
						::memcpy_s(&newdata[m_cur], m_max, data, size);
#elif defined(_OS_IOS)
						memcpy(newdata, m_data, m_cur);
						memcpy(&newdata[m_cur], data, size);
#endif
						m_data = newdata;
					}
				}
				else
				{
#ifdef _MSC_VER
					::memmove_s(&m_data[m_cur], m_max, data, size);
#elif defined(_OS_IOS)
					memmove(&m_data[m_cur], data, size);
#endif
				}

				m_cur += size;
			}
			void Dump() const
			{
				for (size_t i = 0; i < m_cur; i++)
				{
					printf("%02x ", m_data[i]);
				}
			}
		};

	}

	LJFMDB::LJFMDB(size_t c)
		: m_data(LJFMHLP::LJFMMD::Create(c))
	{
	}

	LJFMDB::LJFMDB(const void* data, size_t s)
		: m_data(LJFMHLP::LJFMMD::Create(s))
	{
		if (m_data)
		{
			m_data->Append(data, s);
		}
	}

	LJFMDB::~LJFMDB()
	{
		if (m_data)
		{
			m_data->Release();
		}
	}

	LJFMDB::LJFMDB(const LJFMDB& src)
		: m_data(src.m_data)
	{
		if (m_data)
		{
			m_data->AddRef();
		}
	}

	LJFMDB& LJFMDB::operator = (const LJFMDB& src)
	{
		if (this == &src)
		{
			return *this;
		}
		if (m_data)
		{
			m_data->Release();
		}
		m_data = src.m_data;
		if (m_data)
		{
			m_data->AddRef();
		}
		return *this;
	}

	void LJFMDB::Unique()
	{
		if (1 == m_data->RefCount())
			return;
		LJFMHLP::LJFMMD* data = m_data->Clone();
		m_data->Release();
		m_data = data;
	}

	void LJFMDB::UniqueReserveAndResize(size_t c, size_t s)
	{
		assert(c >= s);
		if (1 == m_data->RefCount())
		{
			if (c != s)
			{
				m_data->Reserve(c);
			}
			m_data->Resize(s);
			return;
		}
		else
		{
			LJFMHLP::LJFMMD* data = m_data->Spawn(c, s);
			m_data->Release();
			m_data = data;
		}
	}

	void* LJFMDB::GetData()
	{
		Unique();
		return m_data->GetData();
	}

	const void* LJFMDB::GetData() const
	{
		return m_data->GetData();
	}

	size_t LJFMDB::GetSize() const
	{
		return m_data->GetSize();
	}

	void LJFMDB::Resize(size_t s)
	{
		if (s == LJFM::FILE_SIZE_ERROR)
		{
			std::string message = "invalid argument: pass -1 as the data size! ";
			message += __FUNCTION__;
			throw std::invalid_argument(message);
		}
		UniqueReserveAndResize(s, s);
	}

	void LJFMDB::Reserve(size_t s)
	{
		if (s == LJFM::FILE_SIZE_ERROR)
		{
			std::string message = "invalid argument: pass -1 as the data size! ";
			message += __FUNCTION__;
			throw std::invalid_argument(message);
		}

		UniqueReserveAndResize(s, GetSize());
	}

	bool LJFMDB::Compare(const LJFMDB& dst) const
	{
		return m_data->Compare(*dst.m_data);
	}

	bool LJFMDB::operator!=(const LJFMDB& dst) const
	{
		return !Compare(dst);
	}

	bool LJFMDB::operator==(const LJFMDB& dst) const
	{
		return Compare(dst);
	}

	void LJFMDB::Clear(bool deep)
	{
		if (1 == m_data->RefCount())
		{
			m_data->Clear(deep);
		}
		else
		{
			m_data->Release();
			m_data = LJFMHLP::LJFMMD::Create(0);
		}
	}

	void LJFMDB::Assign(const void* data, size_t size)
	{
		UniqueReserveAndResize(size, 0);
		m_data->Append(data, size);
	}

	void LJFMDB::Append(const void* data, size_t size)
	{
		size_t curr = m_data->GetSize();
		UniqueReserveAndResize(curr + size, curr);
		m_data->Append(data, size);
	}

	void LJFMDB::Append(const LJFMDB& data)
	{
		Append(data.GetData(), data.GetSize());
	}

	void LJFMDB::Dump() const
	{
		m_data->Dump();
	}

	void LJFMDB::Swap(LJFMDB& dst)
	{
		LJFMHLP::LJFMMD* temp = dst.m_data;
		dst.m_data = m_data;
		m_data = temp;
	}

	LJFMAIL::LJFMAIL() : m_counter(1) {}
	LJFMAIL::~LJFMAIL()
	{
		assert(0 == m_counter);
	}
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	uint64_t LJFMAIL::AddRef()
	{
		assert(m_counter > 0);
		return InterlockedIncrement(&m_counter);
	}

	uint64_t LJFMAIL::Release()
	{
		assert(m_counter > 0);
		if (uint64_t lResult = InterlockedDecrement(&m_counter))
		{
			return lResult;
		}
		else
		{
			delete this;
			return 0;
		}
	}
#else
	int32_t LJFMAIL::AddRef()
	{
		assert(m_counter > 0);
#ifdef  _MSC_VER
		return InterlockedIncrement((LONG*)&m_counter);
#elif defined(_OS_ANDROID)
		return __gnu_cxx::__exchange_and_add(&m_counter, 1) + 1;
#elif defined(_OS_IOS)
		return OSAtomicIncrement32Barrier(&m_counter);
#endif
	}

	int32_t LJFMAIL::Release()
	{
		assert(m_counter > 0);
#ifdef _MSC_VER
		if (LONG lResult = InterlockedDecrement((LONG*)&m_counter))
#elif defined(_OS_ANDROID)
		if (LONG lResult = __gnu_cxx::__exchange_and_add(&m_counter, -1) - 1)
#elif defined(_OS_IOS)
		if (LONG lResult = OSAtomicDecrement32Barrier(&m_counter))
#endif
		{
			return lResult;
		}
		else
		{
			delete this;
			return 0;
		}
	}
#endif
	LJFMID::LJFMID() : m_impl(NULL) {}
	LJFMID::LJFMID(const LJFMID& src) : m_impl(src.m_impl)
	{
		if (m_impl) m_impl->AddRef();
	}
	LJFMID::LJFMID(LJFMAIL* impl) : m_impl(impl) {}
	LJFMID::~LJFMID()
	{
		if (m_impl) m_impl->Release();
	}

	LJFMID& LJFMID::operator=(const LJFMID& src)
	{
		if (this == &src)
		{
			return *this;
		}
		if (m_impl)
		{
			m_impl->Release();
		}
		m_impl = src.m_impl;
		if (m_impl)
		{
			m_impl->AddRef();
		}
		return *this;
	}

	LJFMID& LJFMID::Attach(LJFMAIL* impl)
	{
		if (m_impl)
			m_impl->Release();
		m_impl = impl;
		assert(m_impl);
		return *this;
	}

	LJFMAIL* LJFMID::Detach()
	{
		LJFMAIL*	temp = m_impl;
		m_impl = NULL;
		return temp;
	}

	const void* LJFMID::GetData() const
	{
		if (m_impl)
		{
			return m_impl->GetData();
		}
		else
		{
			return NULL;
		}
	}

	fsize_t LJFMID::GetSize() const
	{
		if (m_impl)
		{
			return m_impl->GetSize();
		}
		else
		{
			return 0;
		}
	}

	namespace LJFMHLP
	{

		class LJFMOID : public LJFM::LJFMAIL
		{
			LJFMDB		m_data;
		public:
			LJFMOID(const LJFMDB& data) : m_data(data) {}
			virtual ~LJFMOID() {}
		public:
			virtual const void* GetData() const
			{
				return m_data.GetData();
			}
			virtual fsize_t GetSize() const
			{
				return m_data.GetSize();
			}
		};

	}

	LJFM::LJFMID LJFMDBToID(const LJFM::LJFMDB& data)
	{
		return LJFM::LJFMID(new LJFMHLP::LJFMOID(data));
	}

	LJFM::LJFMID BaseFileToImageData(LJFM::LJFMBF& file)
	{
		fsize_t	size = file.GetSize();
		if (LJFM::FILE_SIZE_ERROR == size)
		{
			return LJFM::LJFMID();
		}
		LJFMDB Buffer;
		Buffer.Clear(true);
		Buffer.Resize((size_t)size);
		file.Seek(0, LJFM::FSM_SET);
		size_t sizeRead = file.Read(Buffer.GetData(), Buffer.GetSize());
		if (LJFM::FILE_SIZE_ERROR == sizeRead)
		{
			return LJFM::LJFMID();
		}
		if (size != sizeRead)
		{
			Buffer.Resize(sizeRead);
		}
		return LJFMDBToID(Buffer);
	}
	LJFM::LJFMID BaseFileToImageDataEx(LJFM::LJFMBF& file, fsize_t UnitOffset, fsize_t UnitSize)
	{
		fsize_t	size = UnitSize;
		if (LJFM::FILE_SIZE_ERROR == size)
		{
			return LJFM::LJFMID();
		}
		LJFMDB Buffer;
		Buffer.Clear(true);
		Buffer.Resize((size_t)size);
		file.Seek(UnitOffset, LJFM::FSM_SET);
		size_t sizeRead = file.Read(Buffer.GetData(), Buffer.GetSize());
		if (LJFM::FILE_SIZE_ERROR == sizeRead)
		{
			return LJFM::LJFMID();
		}
		if (size != sizeRead)
		{
			Buffer.Resize(sizeRead);
		}
		return LJFMDBToID(Buffer);
	}

	void COutLog::Print(const wchar_t* info, ...)
	{
		va_list vl;
		va_start(vl, info);
		VPrint(info, vl);
		va_end(vl);
	}
}