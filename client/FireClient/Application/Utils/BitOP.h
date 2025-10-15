/***********************************************************************
	filename:	BitOP.h
	author:		—Ó”°”Ó
***********************************************************************/

#ifndef __BitOP_H__
#define __BitOP_H__

#include "tolua++.h"
#include <vector>

// Œª≤Ÿ◊˜∫Ø ˝.
namespace BitOP{
	unsigned int LoWord(unsigned int data){	return data & 0xffff;	}
	unsigned int HiWord(unsigned int data){ return data >> 16; }
	bool TestBit(unsigned int data, int bit){ return data & 1 << bit; }
	unsigned int GetMaxBit(unsigned int data)
	{
		unsigned int bit = 1<<31;
		unsigned int index = 0;
		do
		{
			if (data&bit)
				return 32 - index;
			bit >>= 1;
			++index;
		} while (index<32);
		return 0;
	}
}

#endif // _BitOP_H_
