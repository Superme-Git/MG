#pragma once
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <stdint.h>
#endif

namespace LJFM
{
	namespace LJFMHLP
	{
		class LJFMMD;
	}

	typedef unsigned long long	fsize_t;
	typedef long long			foff_t;

	class LJFMUO
	{
		LJFMUO(const LJFMUO&);
		LJFMUO& operator=(const LJFMUO&);
	public:
		LJFMUO();
		virtual ~LJFMUO();
	};

	class LJFMDB
	{
		LJFMHLP::LJFMMD*	m_data;
	public:

		LJFMDB(size_t c = 0);

		LJFMDB(const void* data, size_t s);
		~LJFMDB();
		LJFMDB(const LJFMDB& src);
		LJFMDB& operator=(const LJFMDB& src);
	private:
		void Unique();

		void UniqueReserveAndResize(size_t c, size_t s);
	public:
		void* GetData();
		const void* GetData() const;
		size_t GetSize() const;
		void Resize(size_t s);
		void Reserve(size_t s);
		void Clear(bool deep = false);
		void Assign(const void* data, size_t size);
		void Append(const void* data, size_t size);
		void Append(const LJFMDB& data);
		void Swap(LJFMDB& dst);
	public:
		bool Compare(const LJFMDB& dst) const;
		bool operator!=(const LJFMDB& dst) const;
		bool operator==(const LJFMDB& dst) const;
	public:
		void Dump() const;
	};
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	class LJFMAIL : public LJFMUO
	{
		volatile uint64_t m_counter;
	public:
		LJFMAIL();
		virtual ~LJFMAIL();
	public:
		virtual uint64_t AddRef();
		virtual uint64_t Release();

		virtual const void* GetData() const = 0;
		virtual fsize_t GetSize() const = 0;
	};
#else
	class LJFMAIL : public LJFMUO
	{
		volatile int32_t m_counter;
	public:
		LJFMAIL();
		virtual ~LJFMAIL();
	public:
		virtual int32_t AddRef();
		virtual int32_t Release();

		virtual const void* GetData() const = 0;
		virtual fsize_t GetSize() const = 0;
	};
#endif
	class LJFMID
	{
		LJFMAIL*	m_impl;

	public:
		LJFMID();
		LJFMID(const LJFMID& src);
		explicit LJFMID(LJFMAIL* impl);
		~LJFMID();

		LJFMID& operator=(const LJFMID& src);
		LJFMID& Attach(LJFMAIL* impl);
		LJFMAIL* Detach();
	public:
		const void* GetData() const;
		fsize_t GetSize() const;
	};

	LJFM::LJFMID LJFMDBToID(const LJFM::LJFMDB& data);

	class COutLog : public LJFMUO
	{
	public:
		COutLog() {}
		virtual ~COutLog() {}
	public:
		virtual void Print(const wchar_t* info, ...);
		virtual void VPrint(const wchar_t* info, va_list vl) = 0;
	};

}