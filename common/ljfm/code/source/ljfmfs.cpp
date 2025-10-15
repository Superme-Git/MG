#include "../include/common.h"
#include <cmath>

namespace LJFM
{

#if defined BYTE_ORDER_BIG_ENDIAN
#define bigendian_16(x)	(x)
#define bigendian_32(x)	(x)
#define bigendian_64(x)	(x)
#elif defined(__x86_64__)
#if defined __GNUC__

	uint16_t bigendian_16(uint16_t x)
	{
		register unsigned short v;
		__asm__("xchg %b0, %h0" : "=q"(v) : "0"(x));
		return v;
	}

	uint32_t bigendian_32(uint32_t x)
	{
		register unsigned int v;
		__asm__("bswapl %0" : "=r"(v) : "0"(x));
		return v;
	}

	uint64_t bigendian_64(uint64_t x)
	{
		register unsigned long v;
		__asm__("bswapq %0":"=r"(v) : "0"(x));
		return v;
	}

#elif defined WIN32

	uint16_t bigendian_16(uint16_t x)
	{
		__asm ror x, 8
		return x;
	}

	uint32_t bigendian_32(uint32_t x)
	{
		__asm mov eax, x
		__asm bswap eax
		__asm mov x, eax
		return x;
	}

	uint64_t bigendian_64(uint64_t x)
	{
		__asm mov rax, x
		__asm bswap rax
		__asm mov x, rax
		return x;
	}
#endif
#elif defined(__i386__)
#if defined __GNUC__

	uint16_t bigendian_16(uint16_t x)
	{
		register unsigned short v;
		__asm__("xchg %b0, %h0" : "=q"(v) : "0"(x));
		return v;
	}

	uint32_t bigendian_32(uint32_t x)
	{
		register unsigned long v;
		__asm__("bswap %0" : "=r"(v) : "0"(x));
		return v;
	}

	uint64_t bigendian_64(uint64_t x)
	{
		union
		{
			unsigned long long __ll;
			unsigned long __l[2];
		} i, o;
		i.__ll = x;
		o.__l[0] = bigendian_32(i.__l[1]);
		o.__l[1] = bigendian_32(i.__l[0]);
		return o.__ll;
	}

#elif defined WIN32

	uint16_t bigendian_16(uint16_t x)
	{
		__asm ror x, 8
		return x;
	}

	uint32_t bigendian_32(uint32_t x)
	{
		__asm mov eax, x
		__asm bswap eax
		__asm mov x, eax
		return x;
	}

	uint64_t bigendian_64(uint64_t x)
	{
		union
		{
			unsigned __int64 __ll;
			unsigned __int32 __l[2];
		} i, o;
		i.__ll = x;
		o.__l[0] = bigendian_32(i.__l[1]);
		o.__l[1] = = bigendian_32(i.__l[0]);
		return o.__ll;
	}
#endif
#else
	uint16_t bigendian_16(uint16_t x)
	{
		union
		{
			uint16_t __ss;
			unsigned char __s[2];
		}	i, o;
		i.__ss = x;

		o.__s[0] = i.__s[1];
		o.__s[1] = i.__s[0];
		return o.__ss;
	}
	uint32_t bigendian_32(uint32_t x)
	{
		union
		{
			uint32_t __ww;
			uint16_t __w[2];
		}	i, o;
		i.__ww = x;

		o.__w[0] = bigendian_16(i.__w[1]);
		o.__w[1] = bigendian_16(i.__w[0]);
		return o.__ww;
	}
	uint64_t bigendian_64(uint64_t x)
	{
		union
		{
			uint64_t __ll;
			uint32_t __l[2];
		} i, o;
		i.__ll = x;
		o.__l[0] = bigendian_32(i.__l[1]);
		o.__l[1] = bigendian_32(i.__l[0]);
		return o.__ll;
	}
#endif

	template<typename T> inline void bigendian(T& value) {
		switch (sizeof(value)) {
		case 2:
			value = bigendian_16(value);
			break;

		case 4:
			value = bigendian_32(value);
			break;

		case 8:
			value = bigendian_64(value);
			break;

		default:
			break;
		}
	}

	LJFMFS::LJFMFS(LJFM::LJFMBF& file) : m_file(file) { }
	LJFMFS::~LJFMFS() {}

	fsize_t LJFMFS::Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm) const
	{
		return m_file.Seek(pos, fsm);
	}

	LJFMFS& LJFMFS::push_byte(const void* x, size_t len)
	{
		size_t out = m_file.Write(x, len);
		if (LJFM::FILE_SIZE_ERROR == out)
		{
			throw LJFMFSExc();
		}
		if (out != len)
		{
			throw LJFMFSExc();
		}
		return *this;
	}

	LJFMFS& LJFMFS::push_string(const std::wstring& str)
	{
		typedef uint16_t utf16;
		push_size(str.length() * sizeof(utf16));

		utf16* buffer = new utf16[str.length()];
		for (int i = 0; i < str.length(); i++)
		{
			buffer[i] = (utf16)str[i];
		}
		push_byte(buffer, sizeof(utf16) * str.length());

		delete[] buffer;
		buffer = NULL;
		return *this;
	}

	LJFMFS& LJFMFS::push_data(const LJFMDB& data)
	{
		size_t	size = data.GetSize();
		push_size(size);
		push_byte(data.GetData(), size);
		return *this;
	}

	void LJFMFS::pop_byte(void* x, size_t len) const
	{
		size_t valid = m_file.Read(x, len);
		if (LJFM::FILE_SIZE_ERROR == valid || valid < len)
		{
			throw LJFMFSExc();
		}
	}

	void LJFMFS::pop_string(std::wstring& str) const
	{
		size_t size = pop_size();
		typedef uint16_t utf16;
		if (size > 0)
		{
			utf16* buffer = new utf16[size / sizeof(utf16)];
			pop_byte(buffer, size);

			str.resize(size / sizeof(utf16));
			for (int i = 0; i < size / sizeof(utf16); i++)
			{
				str[i] = (wchar_t)buffer[i];
			}
			delete[] buffer;
			buffer = NULL;
		}
	}

	void LJFMFS::pop_data(LJFMDB& data) const
	{
		size_t size = pop_size();
		data.Resize(size);
		if (size > 0)
		{
			pop_byte(data.GetData(), size);
		}
	}

	LJFMFS& LJFMFS::push_size(uint32_t x)
	{
		if (x < 0x80)
		{
			return push_byte((unsigned char)x);
		}
		else if (x < 0x4000)
		{
			return push_byte(bigendian_16(x | 0x8000));
		}
		else if (x < 0x20000000)
		{
			return push_byte(bigendian_32(x | 0xc0000000));
		}

		push_byte((unsigned char)0xe0);
		return push_byte(bigendian_32(x));
	}

	uint32_t LJFMFS::pop_size() const
	{
		size_t x = 0;
		size_t valid = m_file.Read(&x, 1);
		if (LJFM::FILE_SIZE_ERROR == valid || valid < 1)
		{
			throw LJFMFSExc();
		}
		switch (x & 0xf0)
		{
		case 0xe0:
			x = pop_byte_32();
			x = bigendian_32(x);
			break;
		case 0xd0:
		case 0xc0:
			x = ((x & 0x1f) << 24);
			x |= pop_byte_8() << 16;
			x |= pop_byte_8() << 8;
			x |= pop_byte_8();
			break;
		case 0xb0:
		case 0xa0:
		case 0x90:
		case 0x80:
			x = ((x & 0x3f) << 8) | pop_byte_8();
			break;
		}
		return x;
	}

	uint8_t LJFMFS::pop_byte_8() const
	{
		uint8_t	value;
		pop_byte(value);
		return value;
	}
	uint16_t LJFMFS::pop_byte_16() const
	{
		uint16_t	value;
		pop_byte(value);
		return value;
	}
	uint32_t LJFMFS::pop_byte_32() const
	{
		uint32_t	value;
		pop_byte(value);
		return value;
	}
	uint64_t LJFMFS::pop_byte_64() const
	{
		uint64_t	value;
		pop_byte(value);
		return value;
	}
	LJFMFS& LJFMFS::push_byte_8(const uint8_t x)
	{
		return push_byte(x);
	}
	LJFMFS& LJFMFS::push_byte_16(const uint16_t x)
	{
		return push_byte(x);
	}
	LJFMFS& LJFMFS::push_byte_32(const uint32_t x)
	{
		return push_byte(x);
	}
	LJFMFS& LJFMFS::push_byte_64(const uint64_t x)
	{
		return push_byte(x);
	}

	LJFMFS& LJFMFS::operator << (char x)
	{
		return push_byte_8(x);
	}
	LJFMFS& LJFMFS::operator << (unsigned char x)
	{
		return push_byte_8(x);
	}
	LJFMFS& LJFMFS::operator << (bool x)
	{
		return push_byte_8(x);
	}
	LJFMFS& LJFMFS::operator << (short x)
	{
		return push_byte_16(x);
	}
	LJFMFS& LJFMFS::operator << (unsigned short x)
	{
		return push_byte_16(x);
	}
	LJFMFS& LJFMFS::operator << (int x)
	{
		return push_byte_32(x);
	}
	LJFMFS& LJFMFS::operator << (unsigned int x)
	{
		return push_byte_32(x);
	}
	LJFMFS& LJFMFS::operator << (long x)
	{
		return push_byte_32(x);
	}
	LJFMFS& LJFMFS::operator << (unsigned long x)
	{
		return push_byte_32(x);
	}
	LJFMFS& LJFMFS::operator << (long long x)
	{
		return push_byte_64(x);
	}
	LJFMFS& LJFMFS::operator << (unsigned long long x)
	{
		return push_byte_64(x);
	}
	LJFMFS& LJFMFS::operator << (double x)
	{
		return push_byte(x);
	}
	LJFMFS& LJFMFS::operator << (const std::wstring& str)
	{
		return push_string(str);
	}
	LJFMFS& LJFMFS::operator << (const LJFMDB& data)
	{
		return push_data(data);
	}

	const LJFMFS& LJFMFS::operator >> (const char &x) const
	{
		remove_const(x) = pop_byte_8();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const unsigned char &x) const
	{
		remove_const(x) = pop_byte_8();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const bool &x) const
	{
		remove_const(x) = !!pop_byte_8();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const short &x) const
	{
		remove_const(x) = pop_byte_16();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const unsigned short &x) const
	{
		remove_const(x) = pop_byte_16();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const int &x) const
	{
		remove_const(x) = pop_byte_32();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const unsigned int &x) const
	{
		remove_const(x) = pop_byte_32();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const long &x) const
	{
		remove_const(x) = pop_byte_32();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const unsigned long &x) const
	{
		remove_const(x) = pop_byte_32();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const long long &x) const
	{
		remove_const(x) = pop_byte_64();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const unsigned long long &x) const
	{
		remove_const(x) = pop_byte_64();
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const double &x) const
	{
		pop_byte(remove_const(x));
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const std::wstring& x) const
	{
		pop_string(remove_const(x));
		return *this;
	}
	const LJFMFS& LJFMFS::operator >> (const LJFMDB& x)  const
	{
		pop_data(remove_const(x));
		return *this;
	}

	LJFMFSLE::LJFMFSLE(LJFM::LJFMBF& file) : LJFMFS(file) { }
	LJFMFSLE::~LJFMFSLE() { }
	LJFMFS& LJFMFSLE::push_size(uint32_t x)
	{
		*(LJFMFS*)this << x;
		return *this;
	}
	LJFMFS& LJFMFSLE::operator << (double x)
	{
		uint8_t xiaoshuweishu = 4;
#ifdef WIN7_32
		int64_t zhuanzhengshu = (int64_t)(std::powf(10, xiaoshuweishu)*x);
#else
		int64_t zhuanzhengshu = (int64_t)(powf(10, xiaoshuweishu)*x);
#endif
		*(LJFMFS*)this << zhuanzhengshu << xiaoshuweishu;
		return *this;
	}
	uint32_t LJFMFSLE::pop_size() const
	{
		uint32_t size = 0;
		*(LJFMFS*)this >> size;
		return size;
	}
	const LJFMFS& LJFMFSLE::operator >> (const double &x) const
	{
		uint8_t xiaoshuweishu = 1;
		int64_t zhuanzhengshu = 0;
		*(LJFMFS*)this >> zhuanzhengshu >> xiaoshuweishu;
#ifdef WIN7_32
		remove_const(x) = (double)zhuanzhengshu / (std::powf(10, xiaoshuweishu));
#else
		remove_const(x) = (double)zhuanzhengshu / (powf(10, xiaoshuweishu));
#endif
		return *this;
	}

	LJFMFSBE::LJFMFSBE(LJFM::LJFMBF& file) : LJFMFS(file) { }
	LJFMFSBE::~LJFMFSBE() { }
	LJFMFS& LJFMFSBE::push_byte_16(uint16_t x)
	{
		bigendian(remove_const(x));
		return LJFMFS::push_byte_16(x);
	}
	LJFMFS& LJFMFSBE::push_byte_32(uint32_t x)
	{
		bigendian(remove_const(x));
		return LJFMFS::push_byte_32(x);
	}
	LJFMFS& LJFMFSBE::push_byte_64(uint64_t x)
	{
		bigendian(remove_const(x));
		return LJFMFS::push_byte_64(x);
	}
	uint16_t LJFMFSBE::pop_byte_16() const
	{
		uint16_t	value;
		value = LJFMFS::pop_byte_16();
		bigendian(value);
		return value;
	}
	uint32_t LJFMFSBE::pop_byte_32() const
	{
		uint32_t	value;
		value = LJFMFS::pop_byte_32();
		bigendian(value);
		return value;
	}
	uint64_t LJFMFSBE::pop_byte_64() const
	{
		uint64_t	value;
		value = LJFMFS::pop_byte_64();
		bigendian(value);
		return value;
	}
	LJFMFS& LJFMFSBE::push_size(uint32_t x)
	{
		*(LJFMFS*)this << x;
		return *this;
	}
	uint32_t LJFMFSBE::pop_size() const
	{
		uint32_t size = 0;
		*(LJFMFS*)this >> size;
		return size;
	}
	LJFMFS& LJFMFSBE::push_string(const std::wstring& str)
	{

		typedef uint16_t utf16;
		push_size(str.length() * sizeof(utf16));

		utf16* buffer = new utf16[str.length()];
		for (int i = 0; i < str.length(); i++)
		{
			buffer[i] = (utf16)str[i];
			bigendian(buffer[i]);
		}
		push_byte(buffer, sizeof(utf16) * str.length());

		delete[] buffer;
		buffer = NULL;
		return *this;
	}
	void LJFMFSBE::pop_string(std::wstring& str) const
	{
		uint32_t size = pop_size();
		typedef uint16_t utf16;
		if (size > 0)
		{
			utf16* buffer = new utf16[size / sizeof(utf16)];
			pop_byte(buffer, size);

			str.resize(size / sizeof(utf16));
			for (int i = 0; i < size / sizeof(utf16); i++)
			{
				bigendian(buffer[i]);
				str[i] = (wchar_t)buffer[i];
			}

			delete[] buffer;
			buffer = NULL;
		}
	}
	LJFMFS& LJFMFSBE::operator << (double x)
	{
		uint8_t xiaoshuweishu = 4;
#ifdef WIN7_32
		int64_t zhuanzhengshu = (int64_t)(std::powf(10, xiaoshuweishu)*x);
#else
		int64_t zhuanzhengshu = (int64_t)(powf(10, xiaoshuweishu)*x);
#endif
		*(LJFMFS*)this << zhuanzhengshu << xiaoshuweishu;
		return *this;
	}
	const LJFMFS& LJFMFSBE::operator >> (const double &x) const
	{
		uint8_t xiaoshuweishu = 1;
		int64_t zhuanzhengshu = 0;
		*(LJFMFS*)this >> zhuanzhengshu >> xiaoshuweishu;
#ifdef WIN7_32
		remove_const(x) = (double)zhuanzhengshu / (std::powf(10, xiaoshuweishu));
#else
		remove_const(x) = (double)zhuanzhengshu / (powf(10, xiaoshuweishu));
#endif
		return *this;
	}

}
