#ifndef THUMBNAILMANAGER_H
#define THUMBNAILMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <map>

#include "ithumbnailmanager.h"
#include "iarithmetics.h"
#include "defaultallocator.h"

namespace TMan
{
	class IArithmetics;
	
	typedef std::map<ThumbnailHandle, ThumbnailPosInfo> ThumbnailHandleMap;
	typedef std::map<POOL_HANDLE, PictureHandle> PoolHandleMap;

	class ThumbnailManager :
		public IThumbnailManager
	{
	private:
		TRenderer *m_pRenderer;
		IAllocator *m_pAllocator;
		IArithmetics *m_pArith;
		ThumbnailHandleMap m_thumbnails;
		PoolHandleMap m_pools;
		TPOOL_FORMAT m_poolFormat;
		int m_nPoolPicSize;		//宽高都一样，最好是2的幂吧
		ThumbnailHandle m_handleSeed;
		DefaultAllocator m_defaultAllocator;

	public:
		ThumbnailManager(TRenderer *pRenderer, IAllocator *pAllocator, int nPoolPicSize);
		virtual ~ThumbnailManager(void);

		virtual ThumbnailHandle LoadPicture(const void* data, int size);
		virtual bool GetPictureInfo(ThumbnailHandle handle, int &width, int &height) const;
		virtual bool DrawPicture(ThumbnailHandle handle, const FRECT &rect, const FRECT *pSrcRect, unsigned int color);
		virtual void ReleasePicture(ThumbnailHandle handle);
		virtual bool SetPoolFormat(TPOOL_FORMAT fmt);
		virtual TPOOL_FORMAT GetPoolFormat() const { return m_poolFormat; }
	};
}

#endif //#ifndef THUMBNAILMANAGER_H