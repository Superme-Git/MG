#ifndef DEFAULTALLOCATOR_H
#define DEFAULTALLOCATOR_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <stdio.h>
#include "iallocator.h"

namespace TMan
{
	class DefaultAllocator :
		public IAllocator
	{
	public:
		DefaultAllocator(void) {}
		virtual ~DefaultAllocator(void) {}
		virtual void* TAlloc(int size) { return malloc(size); }
		virtual void TFree(void *p) { free(p); }

	};

}

#endif //#ifndef DEFAULTALLOCATOR_H