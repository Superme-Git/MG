#include "stdafx.h"
#include "xmarshal.h"

namespace Nuclear 
{

unsigned char XIStream::pop_byte_8() const
{
	unsigned char c;
	pop_byte(c);
	return c;
}

unsigned short XIStream::pop_byte_16() const
{
	unsigned short s;
	pop_byte(s);
	return s;
}

unsigned long XIStream::pop_byte_32() const
{
	unsigned long l;
	pop_byte(l);
	return l;
}

unsigned long long XIStream::pop_byte_64() const
{
	unsigned long long ll;
	pop_byte(ll);
	return ll;
}

} 