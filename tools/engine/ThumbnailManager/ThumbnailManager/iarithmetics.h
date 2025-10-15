#ifndef IARITHMETICSFACTORY_H
#define IARITHMETICSFACTORY_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "ttypes.h"

namespace TMan
{
	typedef unsigned int POOL_HANDLE;
	const POOL_HANDLE INVAILD_POOL_HANDLE = 0;

	class IAllocator;
	
	struct ThumbnailPosInfo
	{
		POOL_HANDLE poolhandle;
		CRECT rect;
	};

	enum TManArithemticsType
	{
		TMAT_SIMPLE = 0,
	};

	class IArithmetics
	{
	public:
		//IArithmetics不处理width 或者 height比池子图片大的情况，这个同样要由外面保证
		virtual bool NewThumbnail(int width, int height, ThumbnailPosInfo &info) = 0;

		//IArithmetics不用处理info被删除两次的情况。这个由外面保证
		virtual bool ReleaseThumbnail(const ThumbnailPosInfo &info) = 0;//如果返回true，代表对应的POOL_HANDLE可以删除了

		~IArithmetics(void) {}
	};

	class ArithmeticsFactory
	{
	public:
		static IArithmetics *GetArithmetics(TManArithemticsType t, int poolSize, IAllocator *pAllocator);
		static void DestoryArithmetics(IArithmetics * pArithmetics);

	private:
		ArithmeticsFactory() {};
	};
}

#endif //#ifndef IARITHMETICSFACTORY_H