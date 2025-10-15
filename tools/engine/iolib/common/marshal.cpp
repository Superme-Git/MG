#include "winiolibdef.h"
#include "marshal.h"

namespace GNET {

unsigned char Marshal::OctetsStream::pop_byte_8() const
{
	unsigned char c;
	pop_byte(c);
	return c;
}

unsigned short Marshal::OctetsStream::pop_byte_16() const
{
	unsigned short s;
	pop_byte(s);
	return byteorder_16(s);
}

unsigned long Marshal::OctetsStream::pop_byte_32() const
{
	unsigned long l;
	pop_byte(l);
	return byteorder_32(l);
}

unsigned long long Marshal::OctetsStream::pop_byte_64() const
{
	unsigned long long ll;
	pop_byte(ll);
	return byteorder_64(ll);
}

Marshal::OctetsStream& Marshal::OctetsStream::compact_uint32(unsigned int x)
{
	if (x < 0x80) return push_byte((unsigned char)x);
	else if (x < 0x4000) return push_byte(byteorder_16(x|0x8000));
	else if (x < 0x20000000) return push_byte(byteorder_32(x|0xc0000000));
	push_byte((unsigned char)0xe0);
	return push_byte(byteorder_32(x));
}

Marshal::OctetsStream& Marshal::OctetsStream::compact_sint32(int x)
{
	if (x >= 0)
	{
		if (x < 0x40) return push_byte((unsigned char)x);
		else if (x < 0x2000) return push_byte(byteorder_16(x|0x8000));
		else if (x < 0x10000000) return push_byte(byteorder_32(x|0xc0000000));
		push_byte((unsigned char)0xe0);
		return push_byte(byteorder_32(x));
	}
	if (-x > 0)
	{
		x = -x;
		if (x < 0x40) return push_byte((unsigned char)x|0x40);
		else if (x < 0x2000) return push_byte(byteorder_16(x|0xa000));
		else if (x < 0x10000000) return push_byte(byteorder_32(x|0xd0000000));
		push_byte((unsigned char)0xf0);
		return push_byte(byteorder_32(x));
	}
	push_byte((unsigned char)0xf0);
	return push_byte(byteorder_32(x));
}

const Marshal::OctetsStream& Marshal::OctetsStream::uncompact_uint32(const unsigned int &x) const
{
	if (pos == data.size()) throw Marshal::Exception();
	switch ( *((unsigned char *)data.begin()+pos) & 0xe0)
	{
	case 0xe0:
		pop_byte_8();
		remove_const(x) = pop_byte_32();
		return *this;
	case 0xc0:
		remove_const(x) = pop_byte_32() & ~0xc0000000;
		return *this;
	case 0xa0:
	case 0x80:
		remove_const(x) = pop_byte_16() & ~0x8000; 
		return *this;
	}
	remove_const(x) = pop_byte_8();
	return *this;
}

const Marshal::OctetsStream& Marshal::OctetsStream::uncompact_sint32(const int &x) const
{
	if (pos == data.size()) throw Marshal::Exception();
	switch ( *((unsigned char *)data.begin()+pos) & 0xf0)
	{
	case 0xf0:
		pop_byte_8();
		remove_const(x) = -pop_byte_32();
		return *this;
	case 0xe0:
		pop_byte_8();
		remove_const(x) = pop_byte_32();
		return *this;
	case 0xd0:
		remove_const(x) = -(pop_byte_32() & ~0xd0000000);
		return *this;
	case 0xc0:
		remove_const(x) = pop_byte_32() & ~0xc0000000;
		return *this;
	case 0xb0:
	case 0xa0:
		remove_const(x) = -(pop_byte_16() & ~0xa000);
		return *this;
	case 0x90:
	case 0x80:
		remove_const(x) = pop_byte_16() & ~0x8000;
		return *this;
	case 0x70:
	case 0x60:
	case 0x50:
	case 0x40:
		remove_const(x) = -(pop_byte_8() & ~0x40);
		return *this;
	}
	remove_const(x) = pop_byte_8();
	return *this;
}

} // namespace GNET {