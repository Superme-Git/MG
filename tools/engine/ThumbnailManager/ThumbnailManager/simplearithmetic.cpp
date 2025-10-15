#include "simplearithmetic.h"
#include "iallocator.h"

namespace TMan
{
	SimpleArithmetic::SimpleArithmetic(int poolPicSize, IAllocator *pAllocator)
		: m_nPoolPicSize(poolPicSize), m_handleSeed(0), m_pAllocator(pAllocator)
	{
		m_nBlockSize = 16;//一个block先按照16*16来吧
		m_nLineAlign = poolPicSize / m_nBlockSize;//一行、一列的block数
		//未处理poolPicSize不是m_nBlockSize的整数倍数的情况
	}

	SimpleArithmetic::~SimpleArithmetic(void)
	{
		PoolSignMap::iterator it = m_signMap.begin(), ie = m_signMap.end();
		for (;it!=ie;++it)
		{
			m_pAllocator->TFree(it->second.data);
		}
	}

	bool SimpleArithmetic::NewThumbnail(int width, int height, ThumbnailPosInfo &info)
	{
		bool findOut = false;
		PoolSignMap::iterator it = m_signMap.begin(), ie = m_signMap.end();
		int x, y, r, b, tx, ty, tr, tb, widthInBlock, heightInBlock;
		widthInBlock = (width + m_nBlockSize - 1) / m_nBlockSize;
		heightInBlock = (height + m_nBlockSize - 1) / m_nBlockSize;
		r = m_nLineAlign - widthInBlock;
		b = m_nLineAlign - heightInBlock;
		unsigned char *pTmpData;
		for (;it!=ie;++it)
		{	
			for (y=0;y<b;++y)//这里的优化空间大了去了
			{
				bool ok = true;
				for(x=0;x<r;++x)
				{	//左上角为x，y
					ok = true;
					ty = y;
					tr = x + widthInBlock;
					tb = y + heightInBlock;
					for (;ty<tb;++ty)
					{
						tx = x;
						pTmpData = it->second.data + m_nLineAlign * ty + x;
						for (;tx<tr;++tx,++pTmpData)
						{
							if (*pTmpData)//被用了。。。
							{
								ok = false;
								x = tx;//直接向右挪
								break;
							}
						}
						if (!ok)
						{
							break;
						}
					}
					if (ok)
					{
						//终于找到了
						tb = y + heightInBlock;
						for (ty = y;ty<tb;++ty)
						{
							pTmpData = it->second.data + m_nLineAlign * ty + x;
							memset(pTmpData, 1, widthInBlock);//标记
						}
						info.poolhandle = it->first;
						info.rect.left = x * m_nBlockSize;
						info.rect.top = y * m_nBlockSize;
						info.rect.right = info.rect.left + width;
						info.rect.bottom = info.rect.top + height;
						++(it->second.picCount);
						return true;
					}

				}
			}
			
		}

		//所有的pool都找过了，没有，要申请新空间了
		info.poolhandle = ++m_handleSeed;
		PoolPicInfo &poolinfo = m_signMap[info.poolhandle];
		int poolsize = m_nLineAlign * m_nLineAlign;
		poolinfo.data = (unsigned char*)m_pAllocator->TAlloc(poolsize);
		memset(poolinfo.data, 0, poolsize);
		for (ty=0;ty<heightInBlock;++ty)
		{
			pTmpData = poolinfo.data  + m_nLineAlign * ty;
			memset(pTmpData, 1, widthInBlock);//标记
		}
		info.rect.left = 0;
		info.rect.top = 0;
		info.rect.right = width;
		info.rect.bottom = height;
		++(poolinfo.picCount);
		return true;
	}

	//如果返回true，代表对应的POOL_HANDLE可以删除了
	bool SimpleArithmetic::ReleaseThumbnail(const ThumbnailPosInfo &info)
	{
		PoolSignMap::iterator it = m_signMap.find(info.poolhandle);
		if (it == m_signMap.end())
			return true;//找不到这个handle了。。。删掉吧
		--it->second.picCount;
		if (it->second.picCount == 0)
		{
			//这张图片里面，所有的小图片都没有了
			m_pAllocator->TFree(it->second.data);
			m_signMap.erase(it);
			return true;
		}
		int x = info.rect.left / m_nBlockSize;
		int y = info.rect.top / m_nBlockSize;
		int b = (info.rect.bottom + m_nBlockSize - 1) / m_nBlockSize;
		int widthInBlock = (info.rect.Width() + m_nBlockSize - 1) / m_nBlockSize;
		for (;y<b;++y)
		{
			memset(it->second.data + m_nLineAlign * y + x, 0, widthInBlock);//标记
		}
		return false;
	}
}