#ifndef ITHUMBNAILMANAGER_H
#define ITHUMBNAILMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "ttypes.h"

namespace TMan
{
	class TRenderer;
	class IAllocator;

	class IThumbnailManager
	{
	public:
		IThumbnailManager(void)	{}
		virtual ~IThumbnailManager(void) {}

		virtual ThumbnailHandle LoadPicture(const void* data, int size) = 0;
		virtual bool GetPictureInfo(ThumbnailHandle handle, int &width, int &height) const = 0;
		virtual bool DrawPicture(ThumbnailHandle handle, const FRECT &rect, const FRECT *pSrcRect, unsigned int color) = 0;
		virtual void ReleasePicture(ThumbnailHandle handle) = 0;
		virtual bool SetPoolFormat(TPOOL_FORMAT fmt) = 0;
		virtual TPOOL_FORMAT GetPoolFormat() const = 0;
	};

	IThumbnailManager *CreateThumbnailManager(TRenderer *pRenderer, IAllocator *pAllocator);
	void DestroyThumbnailManager(IThumbnailManager *pMan);
}

#endif //#ifndef ITHUMBNAILMANAGER_H