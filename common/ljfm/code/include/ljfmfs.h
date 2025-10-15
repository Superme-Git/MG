#pragma once

#if defined _OS_IOS || (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "ljfmopen.h"
#include "ljfmimage.h"
#include <vector>
#include <set>
#endif

namespace LJFM
{
	class LJFMFS;
	class LJFMFSExc {};

	class LJFMFS : public LJFM::LJFMUO
	{
	private:

		LJFM::LJFMBF&		m_file;

	public:
		LJFMFS(LJFM::LJFMBF& file);
		virtual ~LJFMFS();

		fsize_t Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm) const;
		template<typename T> static inline T& remove_const(const T &t)
		{
			return const_cast<T&>(t);
		}
	private:
		template<typename T> LJFMFS& push_byte(T t)
		{
			return push_byte(&t, sizeof(t));
		}

		template<typename T> void pop_byte(T &t) const
		{
			pop_byte(&t, sizeof(t));
		}

	public:
		virtual uint8_t pop_byte_8() const;
		virtual uint16_t pop_byte_16() const;
		virtual uint32_t pop_byte_32() const;
		virtual uint64_t pop_byte_64() const;
    
		virtual LJFMFS& push_byte_8(uint8_t);
		virtual LJFMFS& push_byte_16(uint16_t);
		virtual LJFMFS& push_byte_32(uint32_t);
		virtual LJFMFS& push_byte_64(uint64_t);


	public:
		virtual LJFMFS& push_byte(const void* x, size_t len);
		virtual LJFMFS& push_string(const std::wstring& str);
		virtual LJFMFS& push_data(const LJFMDB& data);
		virtual LJFMFS& operator << (char x);
		virtual LJFMFS& operator << (unsigned char x);
		virtual LJFMFS& operator << (bool x);
		virtual LJFMFS& operator << (short x);
		virtual LJFMFS& operator << (unsigned short x);
		virtual LJFMFS& operator << (int x);
		virtual LJFMFS& operator << (unsigned int x);
		virtual LJFMFS& operator << (long x);
		virtual LJFMFS& operator << (unsigned long x);
		virtual LJFMFS& operator << (long long x);
		virtual LJFMFS& operator << (unsigned long long x);
		virtual LJFMFS& operator << (double x);
		virtual LJFMFS& operator << (const std::wstring& str);
		LJFMFS& operator << (const LJFMDB& data);

		template<class Container> inline LJFM::LJFMFS& ContainerToFileStream(const Container& src)
		{
			uint32_t count = (uint32_t)src.size();
			*this << count;
			for (typename Container::const_iterator it = src.begin(), ite = src.end(); it != ite; ++it)
			{
				const typename Container::value_type& value = *it;
				*this << value;
			}
			return *this;
		}
    
		template<class ValueType> inline LJFM::LJFMFS& operator << (const std::vector<ValueType>& src)
		{
			return ContainerToFileStream(src);
		}
		template<class ValueType> inline LJFM::LJFMFS& operator << (const std::set<ValueType>& src)
		{
			return ContainerToFileStream(src);
		}
    
		template<class ValueType> inline const LJFM::LJFMFS& operator >> (std::vector<ValueType>& src) const
		{
			uint32_t size;
			*this >> size;
			src.reserve( size);
			for (unsigned int i = 0; i < size; i++)
			{
				ValueType	value;
				*this >> value;
				src.push_back(value);
			}
			return *this;
		}
    
		template<class ValueType> inline const LJFM::LJFMFS& operator>>(std::set<ValueType>& src) const
		{
			uint32_t size;
			*this >> size;
			for (unsigned int i = 0; i < size; i++)
			{
				ValueType	value;
				*this >> value;
				src.insert(value);
			}
			return *this;
		}
    
	public:
		virtual LJFMFS& push_size(uint32_t x);
		virtual uint32_t pop_size() const;

		virtual void pop_byte(void* x, size_t len) const;
		virtual void pop_string(std::wstring& str) const;
		virtual void pop_data(LJFMDB& data) const;
		virtual const LJFMFS& operator >> (const char &x) const;
		virtual const LJFMFS& operator >> (const unsigned char &x) const;
		virtual const LJFMFS& operator >> (const bool &x) const;
		virtual const LJFMFS& operator >> (const short &x) const;
		virtual const LJFMFS& operator >> (const unsigned short &x) const;
		virtual const LJFMFS& operator >> (const int &x) const;
		virtual const LJFMFS& operator >> (const unsigned int &x) const;
		virtual const LJFMFS& operator >> (const long &x) const;
		virtual const LJFMFS& operator >> (const unsigned long &x) const;
		virtual const LJFMFS& operator >> (const long long &x) const;
		virtual const LJFMFS& operator >> (const unsigned long long &x) const;
		virtual const LJFMFS& operator >> (const double &x) const;
		virtual const LJFMFS& operator >> (const std::wstring& x) const;
		const LJFMFS& operator >> (const LJFMDB& x)  const;
	};

	class LJFMFSLE : public LJFMFS
	{
	public:
		LJFMFSLE(LJFM::LJFMBF&);
		virtual ~LJFMFSLE();
	public:
		virtual LJFMFS& push_size(uint32_t x);
		virtual LJFMFS& operator << (double x);
	public:
		virtual uint32_t pop_size() const;
		virtual const LJFMFS& operator >> (const double &x) const;
	};

	class LJFMFSBE : public LJFMFS
	{
	public:
		LJFMFSBE(LJFM::LJFMBF&);
		virtual ~LJFMFSBE();

	public:
		virtual LJFMFS& push_byte_16(uint16_t);
		virtual LJFMFS& push_byte_32(uint32_t);
		virtual LJFMFS& push_byte_64(uint64_t);
		virtual LJFMFS& push_size(uint32_t x);
		virtual LJFMFS& push_string(const std::wstring& str);
		virtual LJFMFS& operator << (double);

	public:
		virtual uint16_t pop_byte_16() const;
		virtual uint32_t pop_byte_32() const;
		virtual uint64_t pop_byte_64() const;
		virtual uint32_t pop_size() const;
		virtual void pop_string(std::wstring& str) const;
		virtual const LJFMFS& operator >> (const double &) const;
	};
}
