#include "simplearithmetic.h"
#include "iallocator.h"

namespace TMan
{
	SimpleArithmetic::SimpleArithmetic(int poolPicSize, IAllocator *pAllocator)
		: m_nPoolPicSize(poolPicSize), m_handleSeed(0), m_pAllocator(pAllocator)
	{
		m_nBlockSize = 16;//һ��block�Ȱ���16*16����
		m_nLineAlign = poolPicSize / m_nBlockSize;//һ�С�һ�е�block��
		//δ����poolPicSize����m_nBlockSize���������������
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
			for (y=0;y<b;++y)//������Ż��ռ����ȥ��
			{
				bool ok = true;
				for(x=0;x<r;++x)
				{	//���Ͻ�Ϊx��y
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
							if (*pTmpData)//�����ˡ�����
							{
								ok = false;
								x = tx;//ֱ������Ų
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
						//�����ҵ���
						tb = y + heightInBlock;
						for (ty = y;ty<tb;++ty)
						{
							pTmpData = it->second.data + m_nLineAlign * ty + x;
							memset(pTmpData, 1, widthInBlock);//���
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

		//���е�pool���ҹ��ˣ�û�У�Ҫ�����¿ռ���
		info.poolhandle = ++m_handleSeed;
		PoolPicInfo &poolinfo = m_signMap[info.poolhandle];
		int poolsize = m_nLineAlign * m_nLineAlign;
		poolinfo.data = (unsigned char*)m_pAllocator->TAlloc(poolsize);
		memset(poolinfo.data, 0, poolsize);
		for (ty=0;ty<heightInBlock;++ty)
		{
			pTmpData = poolinfo.data  + m_nLineAlign * ty;
			memset(pTmpData, 1, widthInBlock);//���
		}
		info.rect.left = 0;
		info.rect.top = 0;
		info.rect.right = width;
		info.rect.bottom = height;
		++(poolinfo.picCount);
		return true;
	}

	//�������true�������Ӧ��POOL_HANDLE����ɾ����
	bool SimpleArithmetic::ReleaseThumbnail(const ThumbnailPosInfo &info)
	{
		PoolSignMap::iterator it = m_signMap.find(info.poolhandle);
		if (it == m_signMap.end())
			return true;//�Ҳ������handle�ˡ�����ɾ����
		--it->second.picCount;
		if (it->second.picCount == 0)
		{
			//����ͼƬ���棬���е�СͼƬ��û����
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
			memset(it->second.data + m_nLineAlign * y + x, 0, widthInBlock);//���
		}
		return false;
	}
}