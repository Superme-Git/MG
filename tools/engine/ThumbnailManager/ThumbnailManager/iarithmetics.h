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
		//IArithmetics������width ���� height�ȳ���ͼƬ�����������ͬ��Ҫ�����汣֤
		virtual bool NewThumbnail(int width, int height, ThumbnailPosInfo &info) = 0;

		//IArithmetics���ô���info��ɾ�����ε��������������汣֤
		virtual bool ReleaseThumbnail(const ThumbnailPosInfo &info) = 0;//�������true�������Ӧ��POOL_HANDLE����ɾ����

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