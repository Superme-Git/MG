#include <assert.h>
#include "thumbnailmanager.h"
#include "trenderer.h"

namespace TMan
{
	////////////////////////暂时放在这里吧/////////////////////////////
	IThumbnailManager *CreateThumbnailManager(TRenderer *pRenderer, IAllocator *pAllocator)
	{
		return new ThumbnailManager(pRenderer, pAllocator, 512);//图片的大小先写死在这里吧
	}

	void DestroyThumbnailManager(IThumbnailManager *pMan)
	{
		delete pMan;
	}


	////////////////////////////////////////////////////////////////////////

	ThumbnailManager::ThumbnailManager(TRenderer *pRenderer, IAllocator *pAllocator, int nPoolPicSize) 
		: m_pRenderer(pRenderer), m_pAllocator(pAllocator), m_pArith(NULL), m_nPoolPicSize(nPoolPicSize), m_handleSeed(0)
	{
		if (m_pAllocator == NULL)
		{
			m_pAllocator = &m_defaultAllocator;
		}
		m_pArith = ArithmeticsFactory::GetArithmetics(TMAT_SIMPLE, m_nPoolPicSize, m_pAllocator);
	}

	ThumbnailManager::~ThumbnailManager(void)
	{
		ArithmeticsFactory::DestoryArithmetics(m_pArith);
		m_pArith = NULL;
	}

	bool ThumbnailManager::SetPoolFormat(TPOOL_FORMAT fmt)
	{
		if (m_poolFormat != fmt)
		{
			int newPicSize = m_nPoolPicSize * m_nPoolPicSize * (fmt == TPF_8888?4:2);
			void* srcdata = m_pAllocator->TAlloc(newPicSize);
			if (srcdata == NULL)
				return false;
			PoolHandleMap tmpMap;
			m_pools.swap(tmpMap);
			PoolHandleMap::iterator pIt = tmpMap.begin(), pIe = tmpMap.end();
			bool result = true;
			for (;pIt!=pIe;++pIt)
			{
				bool ok = false;
				if (result)
				{
					PictureHandle handle = m_pRenderer->NewPicture(m_nPoolPicSize, m_nPoolPicSize, fmt);
					if (handle != INVALID_PICTURE_HANDLE)
					{
						if (m_pRenderer->GetPictureData(pIt->second, srcdata, newPicSize, fmt))
						{
							if (m_pRenderer->SetPictureData(handle, srcdata, newPicSize))
							{
								m_pools[pIt->first] = handle;
								m_pRenderer->ReleasePicture(pIt->second);
								ok = true;
							}
						}
						if (!ok)
						{
							m_pRenderer->ReleasePicture(handle);
						}
					}
				}
				if (!ok)
				{
					result = false;
					m_pools.insert(*pIt);
				}
				
			}
			m_pAllocator->TFree(srcdata);
			if (result) 
				m_poolFormat = fmt;
			return result;
		}
		return true;
	}

	ThumbnailHandle ThumbnailManager::LoadPicture(const void* data, int size)
	{
		if (!m_pRenderer)
			return INVALID_THUNMBNAIL_HANDLE;
		if (!m_pArith)
			return INVALID_THUNMBNAIL_HANDLE;
		PictureHandle handle = m_pRenderer->LoadPicture(data, size, m_poolFormat);
		if (handle == INVALID_PICTURE_HANDLE)
			return INVALID_THUNMBNAIL_HANDLE;
		int width, height;
		if (!m_pRenderer->GetPictureInfo(handle, width, height))
		{
			m_pRenderer->ReleasePicture(handle);
			return INVALID_THUNMBNAIL_HANDLE;
		}
		if (width > m_nPoolPicSize || height > m_nPoolPicSize)
		{
			m_pRenderer->ReleasePicture(handle);
			return INVALID_THUNMBNAIL_HANDLE;
		}
		ThumbnailPosInfo info;
		if (!m_pArith->NewThumbnail(width, height, info))
		{
			m_pRenderer->ReleasePicture(handle);
			return INVALID_THUNMBNAIL_HANDLE;
		}
		PoolHandleMap::iterator poolIt = m_pools.find(info.poolhandle);
		PictureHandle poolHandle = INVALID_PICTURE_HANDLE;
		bool newpool = false;
		if (poolIt == m_pools.end())
		{
			poolHandle = m_pRenderer->NewPicture(m_nPoolPicSize, m_nPoolPicSize, m_poolFormat);
			if (poolHandle == INVALID_PICTURE_HANDLE)
			{
				m_pRenderer->ReleasePicture(handle);
				return INVALID_THUNMBNAIL_HANDLE;
			}
			newpool = true;
		} else {
			poolHandle = poolIt->second;
		}
		int picsize = width * height * (m_poolFormat == TPF_8888?4:2);
		void* srcdata = m_pAllocator->TAlloc(picsize);
		if (srcdata == NULL)
		{
			if (newpool)
			{
				m_pRenderer->ReleasePicture(poolHandle);
			}
			m_pArith->ReleaseThumbnail(info);
			m_pRenderer->ReleasePicture(handle);
			return INVALID_THUNMBNAIL_HANDLE;
		}
		ThumbnailHandle result = INVALID_THUNMBNAIL_HANDLE;
		if (m_pRenderer->GetPictureData(handle, srcdata, picsize, m_poolFormat))
		{
			if (m_pRenderer->SetPictureData(poolHandle, srcdata, picsize, &info.rect))
			{
				result = ++m_handleSeed;
			}
		}
		if (result == INVALID_THUNMBNAIL_HANDLE)
		{
			if (newpool)
			{
				m_pRenderer->ReleasePicture(poolHandle);
			}
			m_pArith->ReleaseThumbnail(info);
		} else {
			
			m_thumbnails[result] = info;
			if (newpool)
				m_pools[info.poolhandle] = poolHandle;
		}
		m_pRenderer->ReleasePicture(handle);
		m_pAllocator->TFree(srcdata);
		return result;
	}

	bool ThumbnailManager::GetPictureInfo(ThumbnailHandle handle, int &width, int &height) const
	{
		ThumbnailHandleMap::const_iterator it = m_thumbnails.find(handle);
		if (it == m_thumbnails.end())
			return false;
		width = it->second.rect.Width();
		height = it->second.rect.Height();
		return true;
	}

	bool ThumbnailManager::DrawPicture(ThumbnailHandle handle, const FRECT &rect, const FRECT *pSrcRect, unsigned int color)
	{
		ThumbnailHandleMap::iterator it = m_thumbnails.find(handle);
		if (it == m_thumbnails.end())
			return false;
		FRECT srcRect;
		CRECT &srect = it->second.rect;
		if (pSrcRect)
		{
			int width = srect.Width();
			int height = srect.Height();
			srcRect.left = (srect.left + width * pSrcRect->left) / m_nPoolPicSize;
			srcRect.top = (srect.top + height * pSrcRect->top) / m_nPoolPicSize;
			srcRect.right = (srect.left + width * pSrcRect->right) / m_nPoolPicSize;
			srcRect.bottom = (srect.top + height * pSrcRect->bottom) / m_nPoolPicSize;
		} else {
			//这个其实也可以存下来的
			srcRect.left = static_cast<float>(srect.left) / m_nPoolPicSize;
			srcRect.top = static_cast<float>(srect.top) / m_nPoolPicSize;
			srcRect.right = static_cast<float>(srect.right) / m_nPoolPicSize;
			srcRect.bottom = static_cast<float>(srect.bottom) / m_nPoolPicSize;
		}
		return m_pRenderer->DrawPicture(m_pools[it->second.poolhandle], rect, &srcRect, color);
	}

	void ThumbnailManager::ReleasePicture(ThumbnailHandle handle)
	{
		ThumbnailHandleMap::iterator it = m_thumbnails.find(handle);
		if (it == m_thumbnails.end())
			return;
		if (m_pArith->ReleaseThumbnail(it->second))
		{
			PoolHandleMap::iterator pIt = m_pools.find(it->second.poolhandle);
			assert(pIt != m_pools.end());
			m_pRenderer->ReleasePicture(pIt->second);//这个倒可以不着急，可以留下来做个池子
			m_pools.erase(pIt);
		}
		m_thumbnails.erase(it);
	}

}