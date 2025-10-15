#ifndef IALLOCATOR_H
#define IALLOCATOR_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace TMan
{
	class IAllocator
	{
	public:
		virtual void* TAlloc(int size) = 0;
		virtual void TFree(void *p) = 0;
	};
}

#endif //#ifndef IALLOCATOR_H