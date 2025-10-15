#ifndef ISIMPLEARITHMETICS_H
#define ISIMPLEARITHMETICS_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <map>
#include "iarithmetics.h"

namespace TMan
{
	struct PoolPicInfo
	{
		unsigned char* data;
		int picCount;
		PoolPicInfo() : data(0), picCount(0) {}
	};
	typedef std::map<POOL_HANDLE, PoolPicInfo> PoolSignMap;//��ʵ������1λ����ʾһ��block����������
	class SimpleArithmetic :
		public IArithmetics
	{
	public:
		SimpleArithmetic(int poolPicSize, IAllocator *pAllocator);
		virtual ~SimpleArithmetic(void);

		bool NewThumbnail(int width, int height, ThumbnailPosInfo &info);
		//�������true�������Ӧ��POOL_HANDLE����ɾ����
		bool ReleaseThumbnail(const ThumbnailPosInfo &info);

	private:
		int m_nPoolPicSize;
		int m_nBlockSize;
		int m_nLineAlign;
		POOL_HANDLE m_handleSeed;
		IAllocator *m_pAllocator;
		PoolSignMap m_signMap;
	};

}

#endif //#ifndef ISIMPLEARITHMETICS_H