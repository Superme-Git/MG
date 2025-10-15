#include <string>
#include "zipcommon.h"
#include "basefile.h"
#include "datablock.h"
#include "filemarshal.h"

namespace ZIP {

	//////////////////////////////////////////////////////////////////////////
	//
	// 下面的宏用于将参数x转换为BIG-ENDIAN字节序
	// 如果编译器已经自动支持BIG-ENDIAN字节序，则请在编译选项中定义
	// BYTE_ORDER_BIG_ENDIAN宏。
	// 目前在Win32/X86/IA64/AMD64平台下无需此宏
	//

#if defined BYTE_ORDER_BIG_ENDIAN
	#define bigendian_16(x)	(x)
	#define bigendian_32(x)	(x)
	#define bigendian_64(x)	(x)
#elif defined __GNUC__
	/*
	#define bigendian_16 htons
	#define bigendian_32 htonl
	*/
	inline unsigned short bigendian_16(unsigned short x)
	{
		register unsigned short v;
		__asm__ ("xchg %b0, %h0" : "=q"(v) : "0"(x));
		return v;
	}
	inline unsigned long bigendian_32(unsigned long x)
	{
		register unsigned long v;
		__asm__ ("bswap %0" : "=r"(v) : "0"(x));
		return v;
	}
	inline unsigned long long bigendian_64(unsigned long long x)
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
	inline unsigned __int16 bigendian_16(unsigned __int16 x)
	{
		__asm ror x, 8
		return x;
	}
	inline unsigned __int32 bigendian_32(unsigned __int32 x)
	{
		__asm mov eax, x
		__asm bswap eax
		__asm mov x, eax
		return x;
	}
	inline unsigned __int64 bigendian_64(unsigned __int64 x)
	{
		union
		{
			unsigned __int64 __ll;
			unsigned __int32 __l[2];
		} i, o;
		i.__ll = x;
		o.__l[0] = bigendian_32(i.__l[1]);
		o.__l[1] = bigendian_32(i.__l[0]);
		return o.__ll;
	}
#endif


	CFileMarshal::CFileMarshal() {}
	CFileMarshal::~CFileMarshal() {}


	CFileStream::CFileStream( ZIP::CBaseFile& file) : m_file( file) { }
	CFileStream::~CFileStream() {}

	fsize_t CFileStream::Seek( foff_t pos, ZIP::FILE_SEEK_METHOD fsm)
	{
		return m_file.Seek( pos, fsm);
	}

	CFileStream& CFileStream::push_byte( const void* x, size_t len)
	{
		size_t out = m_file.Write( x, len);
		if( ZIP::FILE_SIZE_ERROR == out)
			throw CFileStreamException();
		if( out != len)
			throw CFileStreamException();
		return *this;
	}

	CFileStream& CFileStream::push_string( const std::wstring& str)
	{
		push_size( str.length() * sizeof(wchar_t) );
		push_byte( str.c_str(), sizeof( wchar_t) * str.length());
		return *this;
	}

	CFileStream& CFileStream::push_data( const CDataBlock& data)
	{
		size_t	size = data.GetSize();
		push_size( size);
		push_byte( data.GetData(), size);
		return *this;
	}

	void CFileStream::pop_byte( void* x, size_t len) const
	{
		size_t valid = m_file.Read( x, len);
		if( ZIP::FILE_SIZE_ERROR == valid || valid < len)
			throw CFileStreamException();
	}

	void CFileStream::pop_string( std::wstring& str) const
	{
		size_t size = pop_size();
		str.resize( size / sizeof(wchar_t) );
		if( size > 0 )
			pop_byte( &str[0], size );
	}

	void CFileStream::pop_data( CDataBlock& data) const
	{
		size_t size = pop_size();
		data.Resize( size);
		if( size > 0 )
			pop_byte( data.GetData(), size);
	}

	CFileStream& CFileStream::push_size(size_t x)
	{
		if (x < 0x80)
		{
			// 0???-???? -> 0???-????
			return push_byte((unsigned char)x);
		}
		else if (x < 0x4000) 
		{
			// 00??-????-????-???? --> 10??-????-????-????
			return push_byte(bigendian_16(x|0x8000));
		}
		else if (x < 0x20000000) 
		{
			// 000?-????-????-????-????-????-????-???? --> 110?-????-????-????-????-????-????-????
			return push_byte(bigendian_32(x|0xc0000000));
		}

		// ????-????-????-????-????-????-????-???? --> 1110-?????-????-????-????-????-????-????-????
		push_byte((unsigned char)0xe0);
		return push_byte(bigendian_32(x));
	}

	size_t CFileStream::pop_size() const
	{
		size_t x = 0;
		size_t valid = m_file.Read( &x, 1 );
		if( ZIP::FILE_SIZE_ERROR == valid || valid < 1 )
			throw CFileStreamException();
		switch ( x & 0xf0 )
		{
		case 0xe0: //4 bytes to read	// 1110????		 
			x = pop_byte_32();
			x = bigendian_32(x); // swap byte-order
			break;
		case 0xd0: // 3 bytes to read	// 1101????
		case 0xc0: // 3 bytes to read	// 1100????
			x = ((x & 0x1f) << 24);
			x |= pop_byte_8() << 16 ;
			x |= pop_byte_8() << 8;
			x |= pop_byte_8();
			break;
		case 0xb0:// 1 byte to read	// 1011????
		case 0xa0:// 1 byte to read	// 1010????
		case 0x90:// 1 byte to read	// 1001????
		case 0x80:// 1 byte to read	// 1000????
			x = ((x & 0x3f ) << 8) | pop_byte_8();
			break;
		}
		// only 1 byte
		return x;
	}

} // namespace ZIP {