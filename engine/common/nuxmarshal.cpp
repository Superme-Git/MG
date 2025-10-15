#include "nuxmarshal.h"

namespace Nuclear 
{

	unsigned char INuclearStream::pop_byte_8() const
	{
		unsigned char c;
		pop_byte(c);
		return c;
	}

	unsigned short INuclearStream::pop_byte_16() const
	{
		unsigned short s;
		pop_byte(s);
		return s;
	}

	unsigned long INuclearStream::pop_byte_32() const
	{
		unsigned int l; //change from long to int, by liugeng
		pop_byte(l);
		return l;
	}

	unsigned long long INuclearStream::pop_byte_64() const
	{
		unsigned long long ll;
		pop_byte(ll);
		return ll;
	}

} 